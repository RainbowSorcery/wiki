package com.lyra.wiki.service;

import com.lyra.wiki.entity.EbookSnapshot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyra.wiki.entity.vo.SnapshotStatisticVO;

import java.util.List;

/**
 * <p>
 * 电子书快照表 服务类
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface IEbookSnapshotService extends IService<EbookSnapshot> {
    void doSnapshot();

    List<SnapshotStatisticVO> getStatistic();

    List<SnapshotStatisticVO> get30Statistic();

}
