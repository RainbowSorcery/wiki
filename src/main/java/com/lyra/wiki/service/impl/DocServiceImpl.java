package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.Doc;
import com.lyra.wiki.mapper.DocMapper;
import com.lyra.wiki.service.IDocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文档 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements IDocService {

}
