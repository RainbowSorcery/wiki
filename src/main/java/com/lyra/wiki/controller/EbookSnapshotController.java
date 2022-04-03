package com.lyra.wiki.controller;


import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.EbookSnapshot;
import com.lyra.wiki.entity.vo.SnapshotStatisticVO;
import com.lyra.wiki.service.IEbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 电子书快照表 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {
    @Autowired
    private IEbookSnapshotService ebookSnapshotService;

    @GetMapping("/getSnapshotStatistic")
    public Result<List<SnapshotStatisticVO>> getSnapshotStatistic() {
        List<SnapshotStatisticVO> ebookList = ebookSnapshotService.getStatistic();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, ebookList);
    }

    @GetMapping("/get-30Statistic")
    public Result<List<SnapshotStatisticVO>> get30DayStatistic() {
        List<SnapshotStatisticVO> ebookList = ebookSnapshotService.get30Statistic();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, ebookList);
    }
}
