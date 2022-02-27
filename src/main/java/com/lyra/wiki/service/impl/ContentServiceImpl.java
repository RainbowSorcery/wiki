package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.Content;
import com.lyra.wiki.mapper.ContentMapper;
import com.lyra.wiki.service.IContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
