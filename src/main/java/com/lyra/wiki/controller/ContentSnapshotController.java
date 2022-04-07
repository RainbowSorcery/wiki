package com.lyra.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Content;
import com.lyra.wiki.entity.ContentSnapshot;
import com.lyra.wiki.service.IContentService;
import com.lyra.wiki.service.IContentSnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "内容快照", description = "内容快照")
public class ContentSnapshotController {
    @Autowired
    private IContentSnapshotService contentSnapshotService;

    @Autowired
    private IContentService contentService;

    @PostMapping("/rollBackContent")
    @Operation(description = "回滚快照",
            summary = "回滚快照", parameters = {
            @Parameter(name = "docId", description = "文档id"),
            @Parameter(name = "date", description = "快照日期")
    })
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
    @Operation(description = "根据文档id查询所有快照",
            summary = "根据文档id查询所有快照", parameters = {
            @Parameter(name = "docId", description = "文档id")
    })
    public Result<List<ContentSnapshot>> getContentSnapshotById(Long docId) {
        QueryWrapper<ContentSnapshot> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("content_id", docId);
        List<ContentSnapshot> list = contentSnapshotService.list(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @Operation(description = "根据文档id和日期查询快照内容",
            summary = "根据文档id查询所有快照", parameters = {
            @Parameter(name = "根据文档id和日期查询快照内容", description = "文档id"),
            @Parameter(name = "date", description = "快照日期")
    })
    @GetMapping("getContentSnapshotByDocIdAndDate")
    public Result<ContentSnapshot> getContentSnapshotByDocIdAndDate(Long docId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        QueryWrapper<ContentSnapshot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", docId);
        queryWrapper.eq("date", date);
        ContentSnapshot contentSnapshot = contentSnapshotService.getOne(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, contentSnapshot);
    }
}
