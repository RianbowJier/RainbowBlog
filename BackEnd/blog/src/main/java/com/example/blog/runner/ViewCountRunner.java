package com.example.blog.runner;

import com.example.framework.Utils.RedisCache;
import com.example.framework.domain.Article;
import com.example.framework.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:当项目启动时，就把博客的浏览量(id和viewCount字段)存储到redis中。也叫启动预处理。
 * @Author:Rainbow
 * @CreateTime:2023/12/2112:08
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 将浏览次数存储到redis中
     * 下面的写法是stream流+函数式编程
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        //查询数据库中的博客信息，注意只需要查询id、viewCount字段的博客信息
        List<Article> articles = articleMapper.selectList(null);//为null即无条件，表示查询所有
        Map<String, Integer> viewCountMap = articles.stream()
                //由于我们需要key、value的数据，所有可以通过stream流，转为map集合。new Function表示函数式编程
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        //把查询到的id作为key，viewCount作为value，一起存入Redis
        //article为库名，viewCount为表名，viewCountMap为要存入的数据
        redisCache.setCacheMap("article:viewCount", viewCountMap);
    }
}
