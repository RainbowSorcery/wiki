package com.lyra.wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyra.wiki.entity.ContentSnapshot;
import com.lyra.wiki.mapper.ContentSnapshotMapper;
import com.lyra.wiki.service.IContentSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ContentSnapshotService extends ServiceImpl<ContentSnapshotMapper, ContentSnapshot> implements IContentSnapshotService {
    @Autowired
    private ContentSnapshotMapper contentSnapshotMapper;

    @Override
    public void insertContentSnapshot(String content, Long docId) {
        // 更新快照信息
        ContentSnapshot contentSnapshot = new ContentSnapshot();
        contentSnapshot.setContent(content);
        contentSnapshot.setDate(LocalDateTime.now());
        contentSnapshot.setContentId(docId);
        contentSnapshotMapper.insert(contentSnapshot);

    }
}
