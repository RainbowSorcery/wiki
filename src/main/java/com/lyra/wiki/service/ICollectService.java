package com.lyra.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyra.wiki.entity.CollectDoc;
import com.lyra.wiki.entity.vo.CollectVO;

import java.util.List;

public interface ICollectService extends IService<CollectDoc> {
    List<CollectVO> getCollectList(Long userId);
}
