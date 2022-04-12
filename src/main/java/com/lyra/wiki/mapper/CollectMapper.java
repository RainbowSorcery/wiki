package com.lyra.wiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyra.wiki.entity.CollectDoc;
import com.lyra.wiki.entity.vo.CollectVO;

import java.util.List;

public interface CollectMapper extends BaseMapper<CollectDoc> {
    List<CollectVO> selectCollectList(Long userId);
}
