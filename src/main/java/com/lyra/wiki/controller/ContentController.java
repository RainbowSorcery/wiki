package com.lyra.wiki.controller;


import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Content;
import com.lyra.wiki.service.IContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 文档内容 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/content")
@Tag(name = "文档内容", description = "文档内容")
public class ContentController {
    @Autowired
    private IContentService contentService;

    @GetMapping("/getContentById")
    @Operation(description = "根据电子书id查询文档内容",
            summary = "根据电子书id查询文档内容", parameters = {
            @Parameter(name = "id", description = "文档id")
    })
    public Result<Content> getContentById(Long id) {

        Content content = contentService.getContentById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, content);
    }
}
