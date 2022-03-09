package com.lyra.wiki.service;

import com.lyra.wiki.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 分类 服务类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface ICategoryService extends IService<Category> {

    List<Category> tree();
}
