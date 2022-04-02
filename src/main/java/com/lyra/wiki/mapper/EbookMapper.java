package com.lyra.wiki.mapper;

import com.lyra.wiki.entity.Ebook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 电子书 Mapper 接口
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface EbookMapper extends BaseMapper<Ebook> {

    void updateEbookInfo();
}
