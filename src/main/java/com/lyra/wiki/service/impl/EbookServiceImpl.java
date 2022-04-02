package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.Ebook;
import com.lyra.wiki.mapper.EbookMapper;
import com.lyra.wiki.service.IEbookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电子书 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements IEbookService {
    @Autowired
    private EbookMapper ebookMapper;

    @Override
    public void updateEbookInfo() {
        ebookMapper.updateEbookInfo();
    }
}
