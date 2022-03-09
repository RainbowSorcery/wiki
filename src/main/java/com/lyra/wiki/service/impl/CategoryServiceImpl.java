package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.Category;
import com.lyra.wiki.mapper.CategoryMapper;
import com.lyra.wiki.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> tree() {
        List<Category> categories = categoryMapper.selectList(null);

        List<Category> collect = categories.stream().filter((category -> {
            return category.getParent() == 0;
        })).peek((category -> {
            List<Category> categoryChildren = getCategoryChildren(category, categories);
            category.setChildren(categoryChildren);
        })).collect(Collectors.toList());

        return collect;
    }

    private List<Category> getCategoryChildren(Category root, List<Category> categoryList) {
        List<Category> collect = categoryList.stream().filter((category -> {
            return Objects.equals(category.getParent(), root.getId());
        })).peek((category -> {
            List<Category> categoryChildren = this.getCategoryChildren(category, categoryList);
            category.setChildren(categoryChildren);
        })).collect(Collectors.toList());

        return collect;
    }
}
