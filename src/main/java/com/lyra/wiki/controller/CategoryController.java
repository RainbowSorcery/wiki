package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Category;
import com.lyra.wiki.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/category")
@Tag(name = "电子书分类", description = "电子书分类接口")
public class CategoryController {
    @Resource
    private ICategoryService categoryService;

    @GetMapping("/list")
    @Operation(description = "查询电子书分类列表",
            summary = "查询电子书分类列表")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.list();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @GetMapping("/list/tree")
    @Operation(description = "以树形方式展示电子书",
            summary = "查询电子数分类树")
    public Result<List<Category>> tree() {
        List<Category> categories = categoryService.tree();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, categories);
    }

    @GetMapping("/getCategoryById")
    @Operation(description = "根据id查询分类",
            summary = "根据id查询分类",
            parameters = {
            @Parameter(name = "id", description = "分类id")
            })
    public Result<Category> getCategoryBIyd(Long id) {
        Category byId = categoryService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, byId);
    }

    @GetMapping("/getParentCategory")
    @Operation(description = "获取所有父分类",
            summary = "获取所有父分类")
    public Result<List<Category>> getParentCategory() {
        List<Category> parentCategory = categoryService.list(new QueryWrapper<Category>().eq("parent", "0"));

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, parentCategory);
    }

    @PostMapping("/addCategory")
    @Operation(description = "添加分类",
            summary = "添加分类")
    public Result<Object> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/updateCategory")
    @Operation(description = "更新分类",
            summary = "更新分类")
    public Result<Object> updateCategory(@RequestBody Category category) {
        Category oldCategory = categoryService.getById(category.getId());
        if (examineUpdateOrCategory(oldCategory)) {
            categoryService.updateById(category);
            return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
        }
        return new Result<>(ResponseEnums.FILED.getCode(), ResponseEnums.FILED.getMessage(), false);
    }

    /**
     * 校验是否可以更新或添加分类 如果父分类有子元素则不允许进行更新操作
     * @param category category
     * @return 是否可以更新
     */
    private Boolean examineUpdateOrCategory(Category category) {
        if (category.getParent() == 0) {
            List<Category> categories = categoryService.list(new QueryWrapper<Category>().eq("id", category.getId()));

            return categories.size() <= 0;
        }

        return true;
    }

    @PostMapping("/delete")
    @Operation(description = "删除分类",
            summary = "删除分类")
    public Result<Object> delete(Long categoryId) {
        // 有问题 分类删除的话 分类下的电子书也会一并删除 这里可以添加MQ
        categoryService.removeById(categoryId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
