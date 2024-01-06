package com.example.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.framework.Utils.BeanCopyUtils;
import com.example.framework.Utils.WebUtils;
import com.example.framework.domain.Category;
import com.example.framework.domain.ResponseResult;
import com.example.framework.domain.dto.CategoryDto;
import com.example.framework.domain.enums.AppHttpCodeEnum;
import com.example.framework.domain.vo.CategoryVo;
import com.example.framework.domain.vo.ExcelCategoryVo;
import com.example.framework.domain.vo.PageVo;
import com.example.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:写博文-查询文章分类的接口
 * @Author:Rainbow
 * @CreateTime:2023/12/2916:07
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询文章分类的接口
     *
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }


    /**
     * 根据类别查询文章
     *
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Category category, Integer pageNum, Integer pageSize) {
        PageVo pageVo = categoryService.selectCategoryPage(category, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }


    /**
     * 新增类别
     *
     * @param categoryDto
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }


    /**
     * 删除文章类别
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseResult remove(@PathVariable(value = "id") Long id) {
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 根据分类的id来查询分类
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        Category category = categoryService.getById(id);
        return ResponseResult.okResult(category);
    }

    /**
     * 根据id修改分类
     *
     * @param category
     * @return
     */

    @PutMapping
    public ResponseResult edit(@RequestBody Category category) {
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    /**
     * 把分类数据写入到Excel并导出
     *
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 设置下载文件的请求头及文件名
            WebUtils.setDownLoadHeader("分类.xlsx", response);

            // 获取需要导出的数据
            List<Category> categories = categoryService.list();

            // 转换为Excel实体类列表
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);

            // 将数据写入到Excel并设置工作表名称
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(false) // 不关闭输出流，以便浏览器可以下载
                    .sheet("文章分类") // 设置工作表名称
                    .doWrite(excelCategoryVos); // 写入数据

        } catch (Exception e) {
            // 创建错误响应结果
            ResponseResult errorResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);

            // 将错误结果序列化为JSON字符串并返回给前端
            String errorMessage = JSON.toJSONString(errorResult);

            // 输出错误信息至HTTP响应体
            WebUtils.renderString(response, errorMessage);
        }
    }
}









