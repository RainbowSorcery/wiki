package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyra.wiki.entity.CollectDoc;
import com.lyra.wiki.entity.vo.CollectVO;
import com.lyra.wiki.mapper.CollectMapper;
import com.lyra.wiki.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectDocServiceImpl extends ServiceImpl<CollectMapper, CollectDoc> implements ICollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public List<CollectVO> getCollectList(Long userId) {
        return collectMapper.selectCollectList(userId);
    }
}
