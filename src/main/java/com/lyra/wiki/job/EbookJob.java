package com.lyra.wiki.job;

import com.lyra.wiki.service.IEbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookJob {
    private static final Logger log = LoggerFactory.getLogger(EbookJob.class);

    @Autowired
    private IEbookService ebookService;

    // 每24小时计算一次电子书信息
    @Scheduled(cron = "0 0 0-23 * * ? ")
    public void runEbookJob() {
        log.info("开始更新电子书信息----------------");
        long start = System.currentTimeMillis();
        ebookService.updateEbookInfo();
        long end = System.currentTimeMillis();
        log.info("更新电子书下所有文档结束 耗时:{}ms --------------", end - start);
    }
}
