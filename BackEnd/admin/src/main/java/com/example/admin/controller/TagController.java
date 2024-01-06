package com.example.admin.controller;

import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.annotation.Systemlog;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.Tag;
import com.example.framework.domain.dto.AddTagDto;
import com.example.framework.domain.dto.EditTagDto;
import com.example.framework.domain.dto.TagListDto;
import com.example.framework.domain.vo.TagVo;
import com.example.framework.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/2718:29
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 查询标签列表
     */
    @GetMapping("/list")
    @Systemlog(businessName = "查询标签列表接口")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    /**
     * 新增标签
     */
    @PostMapping()
    @Systemlog(businessName = "新增标签接口")
    public ResponseResult add(@RequestBody AddTagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.save(tag);

        return ResponseResult.okResult();
    }

    /**
     * 根据id删除标签
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @Systemlog(businessName = "删除标签接口")
    public ResponseResult delete(@PathVariable Long id) {
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 根据id查询标签
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    /**
     * 根据标签的id来修改标签
     * 标签名+备注
     *
     * @param tagDto
     * @return
     */
    @PutMapping
    @Systemlog(businessName = "修改标签接口")
    public ResponseResult edit(@RequestBody EditTagDto tagDto) {
        System.out.println(tagDto);
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 写博文-查询文章标签的接口
     *
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}




























