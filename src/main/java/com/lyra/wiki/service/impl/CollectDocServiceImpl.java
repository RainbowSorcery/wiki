package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyra.wiki.entity.CollectDoc;
import com.lyra.wiki.mapper.CollectMapper;
import com.lyra.wiki.service.ICollectService;
import org.springframework.stereotype.Service;

@Service
public class CollectDocServiceImpl extends ServiceImpl<CollectMapper, CollectDoc> implements ICollectService {
}
