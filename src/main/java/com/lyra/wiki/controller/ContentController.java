package com.lyra.wiki.controller;


import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Content;
import com.lyra.wiki.service.IContentService;
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
public class ContentController {
    @Autowired
    private IContentService contentService;

    @GetMapping("/getContentById")
    public Result<Content> getContentById(Long id) {

        Content content = contentService.getContentById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, content);
    }
}
