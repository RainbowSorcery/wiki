package com.lyra.wiki.controller;


import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Ebook;
import com.lyra.wiki.service.IEbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 电子书 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/ebook")
@Tag(name = "ebook", description = "电子书接口")
public class EbookController {
    @Autowired
    private IEbookService ebookService;

    @Operation(description = "查询电子书列表")
    @GetMapping("/list")
    public Result<List<Ebook>> list() {
        List<Ebook> ebookList = ebookService.list();
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), ebookList);
    }
}
