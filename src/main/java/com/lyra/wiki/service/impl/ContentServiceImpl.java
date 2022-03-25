package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.Content;
import com.lyra.wiki.mapper.ContentMapper;
import com.lyra.wiki.service.IContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyra.wiki.service.IDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 文档内容 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements IContentService {
    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private IDocService docService;

    @Override
    @Transactional
    public Content getContentById(Long id) {
        Content content = contentMapper.selectById(id);
        docService.increaseViewCount(id);

        return content;
    }
}
