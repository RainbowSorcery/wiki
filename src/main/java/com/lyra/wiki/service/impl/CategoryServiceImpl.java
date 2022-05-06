package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyra.wiki.common.constant.RedisConstant;
import com.lyra.wiki.entity.Category;
import com.lyra.wiki.mapper.CategoryMapper;
import com.lyra.wiki.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Category> tree() {
        String stringCategoryCache = redisTemplate.opsForValue().get(RedisConstant.CATEGORY_CACHE);

        // 判断cache中是否有数据 若无数据则进行保存至redis中 若有则直接返回
        if (StringUtils.isBlank(stringCategoryCache)) {

            List<Category> categories = categoryMapper.selectCategory();

            List<Category> collect = categories.stream().filter((category -> {
                return category.getParent() == 0;
            })).peek((category -> {
                List<Category> categoryChildren = getCategoryChildren(category, categories);
                category.setChildren(categoryChildren);
            })).collect(Collectors.toList());

            try {
                redisTemplate.opsForValue().set(RedisConstant.CATEGORY_CACHE, objectMapper.writeValueAsString(collect));
            } catch (JsonProcessingException e) {
                log.error("分类转转json失败 失败信息:{}" + e.getMessage());
                e.printStackTrace();
            }

            return collect;
        } else  {
            List<Category> categories = null;
            try {
                categories = objectMapper.readValue(stringCategoryCache, new TypeReference<List<Category>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("分类转转json失败 失败信息:{}" + e.getMessage());
                e.printStackTrace();
            }

            return categories;
        }


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
