package com.lyra.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Content;
import com.lyra.wiki.entity.ContentSnapshot;
import com.lyra.wiki.service.IContentService;
import com.lyra.wiki.service.IContentSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/content-snapshot")
public class ContentSnapshotController {
    @Autowired
    private IContentSnapshotService contentSnapshotService;

    @Autowired
    private IContentService contentService;

    @PostMapping("/rollBackContent")
    @Transactional
    public Result<Object> rollback(Long docId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {

        QueryWrapper<ContentSnapshot> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("content_id", docId);
        queryWrapper.eq("date", date);

        ContentSnapshot contentSnapshot = contentSnapshotService.getOne(queryWrapper);

        Content content = contentService.getById(docId);

        content.setContent(contentSnapshot.getContent());

        contentService.updateById(content);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);

    }

    @GetMapping("/getContentSnapshotByDocId")
    public Result<List<ContentSnapshot>> getContentSnapshotById(Long docId) {
        QueryWrapper<ContentSnapshot> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("content_id", docId);
        List<ContentSnapshot> list = contentSnapshotService.list(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @GetMapping("getContentSnapshotByDocIdAndDate")
    public Result<ContentSnapshot> getContentSnapshotByDocIdAndDate(Long docId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        QueryWrapper<ContentSnapshot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", docId);
        queryWrapper.eq("date", date);
        ContentSnapshot contentSnapshot = contentSnapshotService.getOne(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, contentSnapshot);
    }
}
