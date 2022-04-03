package com.lyra.wiki.service.impl;

import com.lyra.wiki.entity.EbookSnapshot;
import com.lyra.wiki.entity.vo.SnapshotStatisticVO;
import com.lyra.wiki.mapper.EbookSnapshotMapper;
import com.lyra.wiki.service.IEbookSnapshotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 电子书快照表 服务实现类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot> implements IEbookSnapshotService {
    @Autowired
    private EbookSnapshotMapper ebookSnapshotMapper;

    @Override
    @Transactional
    public void doSnapshot() {
        ebookSnapshotMapper.initEbookSnapshot();
        ebookSnapshotMapper.updateCurrentDay();
        ebookSnapshotMapper.updateEbookSnapshot();
    }

    @Override
    public List<SnapshotStatisticVO> getStatistic() {
        return ebookSnapshotMapper.getStatistic();
    }

    @Override
    public List<SnapshotStatisticVO> get30Statistic() {
        return ebookSnapshotMapper.get30Statistic();
    }
}
