package com.lyra.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyra.wiki.entity.ContentSnapshot;

public interface IContentSnapshotService extends IService<ContentSnapshot> {
    public void insertContentSnapshot(String content, Long docId);
}
