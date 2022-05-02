package com.lyra.wiki.mapper;

import com.lyra.wiki.entity.EbookSnapshot;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyra.wiki.entity.vo.SnapshotStatisticVO;

import java.util.List;

/**
 * <p>
 * 电子书快照表 Mapper 接口
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {
    /**
     * 初始化快照信息
     */
        void initEbookSnapshot();

    /**
     * 将今日点赞数和今日浏览数统计到数据库中
     */
    void updateCurrentDay();


    /**
     * 统计增量 增量 = 今天数 - 昨天
     */
    void updateEbookSnapshot();

    /**
     * 获取今日统计数据 将昨天的数据和今天的数据查出并分组排序返回
     * @return 将统计数据返回
     */
    List<SnapshotStatisticVO> getStatistic();

    List<SnapshotStatisticVO> get30Statistic();

}
