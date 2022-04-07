package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Ebook;
import com.lyra.wiki.entity.vo.PageRequestVO;
import com.lyra.wiki.service.IEbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
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
@Tag(name = "电子书", description = "电子书接口")
public class EbookController {
    @Resource
    private IEbookService ebookService;

    private static final Logger log = LoggerFactory.getLogger(EbookController.class);

    @Operation(description = "查询电子书列表")
    @GetMapping("/list")
    public Result<List<Ebook>> list() {
        List<Ebook> ebookList = ebookService.list();
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, ebookList);
    }

    @GetMapping("/pageList")
    public Result<Page<Ebook>> pageList(PageRequestVO page, String name) {
        if (page.getPageSize() == null) {
            page.setPageSize(5);
        }

        if (page.getCurrent() == null) {
            page.setCurrent(0);
        }

        QueryWrapper<Ebook> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }

        Page<Ebook> ebookPage = ebookService.page(new Page<>(page.getCurrent(), page.getPageSize()), queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, ebookPage);
    }

    @GetMapping("/getEbookById")
    public Result<Ebook> getEbookById(Long id) {
        Ebook byId = ebookService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, byId);
    }

    @PostMapping("/updateEbook")
    public Result<Object> updateEbook(@RequestBody Ebook ebook) {
        ebookService.updateById(ebook);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/addEbook")
    public Result<Object> addEbook(@RequestBody Ebook ebook) {
        ebook.setDocCount(0);
        ebook.setViewCount(0);
        ebook.setVoteCount(0);
        ebookService.save(ebook);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @GetMapping("/getEbookByCategoryId")
    public Result<List<Ebook>> getEbookByCategoryId(Long id) {
        List<Ebook> ebookList = ebookService.list(new QueryWrapper<Ebook>().eq("category2_id", id));

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(),true, ebookList);
    }

    @PostMapping("/delete")
    public Result<Object> delete(Long ebookId) {
        ebookService.removeById(ebookId);
        
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
