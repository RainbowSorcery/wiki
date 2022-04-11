package com.lyra.wiki.mapper;

import com.lyra.wiki.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 分类 Mapper 接口
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> selectCategory();
}
