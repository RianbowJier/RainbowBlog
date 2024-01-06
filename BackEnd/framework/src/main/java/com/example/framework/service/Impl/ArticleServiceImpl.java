package com.example.framework.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.Utils.RedisCache;
import com.example.framework.constants.SystemConstants;
import com.example.framework.domain.Article;
import com.example.framework.domain.ArticleTag;
import com.example.framework.domain.Category;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.dto.AddArticleDto;
import com.example.framework.domain.dto.ArticleDto;
import com.example.framework.domain.vo.*;
import com.example.framework.mapper.ArticleMapper;
import com.example.framework.mapper.CategoryMapper;
import com.example.framework.service.ArticleService;
import com.example.framework.service.ArticleTagService;
import com.example.framework.service.ArticleVoService;
import com.example.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author admin
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    @Lazy
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleVoService articleVoService;

    /**
     * 获取热门文章，存储到列表
     */
    @Override
    public ResponseResult hotArticleList() {
        // 创建查询条件包装器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);

        // 分页查询热门文章，每页10条数据
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT, SystemConstants.ARTICLE_STATUS_SIZE);
        page(page, queryWrapper);

        // 获取文章列表
        List<Article> articles = page.getRecords();

        // 从Redis中获取浏览量映射，并同步更新文章的浏览量信息
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        List<Article> collect = articles.stream()
                .map(article -> {
                    Integer viewCount = viewCountMap.getOrDefault(article.getId().toString(), 0);
                    article.setViewCount(viewCount.longValue());
                    return article;
                }).collect(Collectors.toList());

        // 实体类映射
        List<HotArticleVO> hotArticleVos = BeanCopyUtils.copyBeanList(collect, HotArticleVO.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    /**
     * 获取不同类别的文章，存储到列表
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 初始化查询结果列表和分页对象
        List<Article> articles;
        Page<Article> page = new Page<>(pageNum, pageSize);

        if (categoryId > 0) { // 根据类别查询文章列表
            articles = categoryMapper.articleListByCategory(categoryId, page);
        } else { // 首页查询文章列表
            articles = articleMapper.articleList(page);
        }

        /*-----------------------读取redis中的浏览量--------------------------------------*/
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");

        //根据Article的id将从mysql中读到的Article的viewCount设置为redis中的
        List<Article> collect = articles.stream()
                .map(article -> {
                    Integer viewCount = viewCountMap.getOrDefault(article.getId().toString(), 0);
                    article.setViewCount(viewCount.longValue());
                    return article;
                }).collect(Collectors.toList());
        /*-----------------------------------------------------------------------*/

        // 映射实体类
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(collect, ArticleListVo.class);

        // 封装查询结果和文章总数到 PageVo（自定义实体类）
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());


        return ResponseResult.okResult(pageVo);
    }

    /**
     * 文章详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);

        //-------------------从redis查询文章的浏览量，展示在文章详情---------------------------
        //从redis查询文章浏览量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //-----------------------------------------------------------------------------

        //实体类映射
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        //根据分类id，来查询分类名
        Long categoryId = articleDetailVo.getCategoryId();

        Category category = categoryService.getById(categoryId);
        //如果根据分类id查询的到分类名，那么就把查询到的值设置给ArticleDetailVo实体类的categoryName字段
        articleDetailVo.setCategoryName(category != null ? category.getName() : "");

        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 根据redis中文章id从mysql查询文章
     * 更新redis中的浏览量，对应文章id的viewCount浏览量。article:viewCount是ViewCountRunner类里面写的
     * 用户每从mysql根据文章id查询一次浏览量，那么redis的浏览量就增加1
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateRedisViewCount(Long id) {
        try {
            // 增加指定ID文章的浏览次数（假设 incrementCacheMapValue 方法原子性地递增缓存中的值）
            redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        } catch (Exception e) {
            log.error("更新Redis中文章({})浏览次数时发生错误");
        }

        // 返回操作成功的结果
        return ResponseResult.okResult();
    }

    /**
     * 发布文章
     *
     * @param articleDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult add(AddArticleDto articleDto) {
        // 转换并添加博客信息
        ArticleVo articleVo = BeanCopyUtils.copyBean(articleDto, ArticleVo.class);
        try {
            articleVoService.save(articleVo);

            // 创建文章与标签关联列表
            List<ArticleTag> articleTags = articleDto.getTags().stream()
                    .map(tagId -> new ArticleTag(articleVo.getId(), tagId))
                    .collect(Collectors.toList());

            // 批量添加文章与标签关联
            articleTagService.saveBatch(articleTags);
        } catch (Exception e) {
            log.error("添加文章及其标签关联时发生错误");
            throw new RuntimeException("添加失败", e);
        }

        return ResponseResult.okResult();
    }

    /**
     * 根据类别查询文章基本信息
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        // 根据标题和摘要字段是否非空进行模糊查询
        queryWrapper.like(StringUtils.hasText(article.getTitle()), Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()), Article::getSummary, article.getSummary());

        //分页查询
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换成VO
        List<Article> articles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);

        return pageVo;
    }

    /**
     * 根据id查询文章信息
     *
     * @param id
     * @return
     */
    @Override
    public ArticleByIdVo getInfo(Long id) {
        //根据id查询文章信息
        Article article = getById(id);

        //获取关联标签
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();

        articleTagLambdaQueryWrapper
                .eq(ArticleTag::getArticleId, article.getId());

        //查询的信息存储到List集合
        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
        //tags集合
        List<Long> tags = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());

        ArticleByIdVo articleVo = BeanCopyUtils.copyBean(article, ArticleByIdVo.class);
        articleVo.setTags(tags);

        return articleVo;
    }

    /**
     * 修改文章信息
     *
     * @param articleDto
     */
    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);

        //删除原有的 标签和博客 的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();

        articleTagLambdaQueryWrapper
                .eq(ArticleTag::getArticleId, article.getId());

        articleTagService.remove(articleTagLambdaQueryWrapper);

        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());

        //批量修改tags
        articleTagService.saveBatch(articleTags);
    }
}













