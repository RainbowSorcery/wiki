package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.RedisConstant;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Doc;
import com.lyra.wiki.service.IDocService;
import com.lyra.wiki.utils.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文档 前端控制器
 * </p>
 *
 * @author lyra
 * @since 2022-02-26
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private IDocService docService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/list/tree")
    public Result<List<Doc>> treeList(Long ebookId) {
        List<Doc> list = docService.treeList(ebookId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @GetMapping("/pageList")
    public Result<Page<Doc>> list(Integer current, Integer pageSize, String condition) {
        if (current == null) {
            current = 0;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", condition);

        Page<Doc> page = docService.page(new Page<>(current, pageSize), queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, page);
    }

    @GetMapping("/list")
    public Result<List<Doc>> list() {
        List<Doc> docs = docService.list();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, docs);

    }

    @GetMapping("/getDocById")
    public Result<Doc> getDocById(Long id) {
        Doc doc = docService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, doc);
    }

    @GetMapping("/getParentDoc")
    public Result<List<Doc>> getParentDoc() {
        List<Doc> parentList = docService.list(new QueryWrapper<Doc>().eq("parent", 0));

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, parentList);
    }

    @GetMapping("/getSelectedTreeData")
    public Result<List<Doc>> getSelectedTreeData(Long id, Long ebookId) {
        List<Doc> list = null;
        if (id != null) {
            list = docService.getSelectedTreeData(id, ebookId);
        } else {
            list = docService.treeList(ebookId);
        }

        Doc doc = new Doc();
        doc.setId(0L);
        doc.setName("无");
        list.add(0, doc);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @PostMapping("/updateDoc")
    public Result<Objects> update(@RequestBody Doc doc) {

        docService.updateDoc(doc);
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/addDoc")
    public Result<Objects> save(@RequestBody Doc doc) {
        docService.addDoc(doc);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/deleteDoc")
    public Result<Objects> deleteDoc(Long id) {
        docService.deleteDoc(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/increaseVoteCount")
    public Result<Objects> increaseVoteCount(Long id, HttpServletRequest request) {
        docService.increaseVoteCount(id);

        String ip = IpUtil.getRemoteIp(request);

        redisTemplate.opsForValue().set(RedisConstant.REDIS_VOTE + ip + ":" + id, ip);


        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

}
