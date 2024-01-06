package com.example.admin.controller;

import com.example.framework.domain.Link;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author:Rainbow
 * @CreateTime:2023/12/3018:11
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 分页查询友链
     *
     * @param link
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        PageVo pageVo = linkService.selectLinkPage(link, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 增加友联
     *
     * @param link
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Link link) {
        linkService.save(link);
        return ResponseResult.okResult();
    }


    /**
     * 修改友联
     *
     * @param link
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Link link) {
        linkService.updateById(link);
        return ResponseResult.okResult();
    }


    /**
     * 删除友联
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
