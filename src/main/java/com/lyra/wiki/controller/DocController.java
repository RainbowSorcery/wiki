package com.lyra.wiki.controller;


import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.RedisConstant;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.Doc;
import com.lyra.wiki.service.IDocService;
import com.lyra.wiki.utils.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
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
@Tag(name = "文档", description = "文档")
public class DocController {
    @Autowired
    private IDocService docService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/list/tree")
    @Operation(description = "根据电子书id查询文档",
            summary = "根据电子id查询文档",
            parameters = {
            @Parameter(name = "ebookId", description = "电子书id")
            })
    public Result<List<Doc>> treeList(Long ebookId) {
        List<Doc> list = docService.treeList(ebookId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @GetMapping("/pageList")
    @Operation(description = "分页查询文档",
            summary = "分页查询文档",
            parameters = {
                    @Parameter(name = "current", description = "当前页"),
                    @Parameter(name = "pageSize", description = "每页条数"),
                    @Parameter(name = "condition", description = "条件")
            })
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
    @Operation(description = "查询文档列表",
            summary = "查询文档列表"
    )
    public Result<List<Doc>> list() {
        List<Doc> docs = docService.list();

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, docs);

    }

    @GetMapping("/getDocById")
    @Operation(description = "根据id查询文档",
            summary = "根据id查询文档",
            parameters = {
            @Parameter(name = "id", description = "文档id")
            }
    )
    public Result<Doc> getDocById(Long id) {
        Doc doc = docService.getById(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, doc);
    }

    @Operation(description = "获取所有父文档",
            summary = "获取所有父文档"
    )
    @GetMapping("/getParentDoc")
    public Result<List<Doc>> getParentDoc() {
        List<Doc> parentList = docService.list(new QueryWrapper<Doc>().eq("parent", 0));

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, parentList);
    }

    @Operation(description = "获取下拉框数据, 若被传入文档id, 那么表示查询整个电子书tree",
            summary = "获取下拉框数据",
            parameters = {
                    @Parameter(name = "id", description = "文档id"),
                    @Parameter(name = "ebookId", description = "电子书id")
            }
    )
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

    @Operation(description = "更新文档",
            summary = "更新文档"
    )
    @PostMapping("/updateDoc")
    public Result<Objects> update(@RequestBody Doc doc) {

        docService.updateDoc(doc);
        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/addDoc")
    @Operation(description = "添加文档",
            summary = "添加文档"
    )
    public Result<Objects> save(@RequestBody Doc doc) {
        docService.addDoc(doc);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/deleteDoc")
    @Operation(description = "删除文档",
            summary = "删除文档"
    )
    public Result<Objects> deleteDoc(Long id) {
        docService.deleteDoc(id);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @PostMapping("/increaseVoteCount")
    @Operation(description = "点赞",
            summary = "点赞"
    )
    public Result<Objects> increaseVoteCount(Long id, HttpServletRequest request) {
        docService.increaseVoteCount(id);

        String ip = IpUtil.getRemoteIp(request);

        redisTemplate.opsForValue().set(RedisConstant.REDIS_VOTE + ip + ":" + id, ip);


        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    public Result<List<Doc>> getDocListByIdAndName(Long ebookId, String name) {
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("ebook_id", ebookId);

        List<Doc> list = docService.list(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

}
