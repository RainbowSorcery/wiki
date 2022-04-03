package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Category;
import com.lyra.wiki.service.ICategoryService;
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
public class CategoryController {
    @Resource
    private ICategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        List<Category> list = categoryService.list();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @GetMapping("/list/tree")
    public Result<List<Category>> tree() {
        List<Category> categories = categoryService.tree();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, categories);
    }

    @GetMapping("/getCategoryById")
    public Result<Category> getCategoryById(Long id) {
        Category byId = categoryService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, byId);
    }

    @GetMapping("/getParentCategory")
    public Result<List<Category>> getParentCategory() {
        List<Category> parentCategory = categoryService.list(new QueryWrapper<Category>().eq("parent", "0"));

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, parentCategory);
    }

    @PostMapping("/addCategory")
    public Result<Object> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/updateCategory")
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
    public Result<Object> delete(Long categoryId) {
        // 有问题 分类删除的话 分类下的电子书也会一并删除 这里可以添加MQ
        categoryService.removeById(categoryId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
