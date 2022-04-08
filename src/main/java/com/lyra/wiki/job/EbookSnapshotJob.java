package com.lyra.wiki.job;

import com.lyra.wiki.service.IEbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {
    @Autowired
    private IEbookSnapshotService ebookSnapshotService;

    @Scheduled(cron = "0 0 */1 * * ?")
    public void doSnapshot() {
        ebookSnapshotService.doSnapshot();
    }
}
