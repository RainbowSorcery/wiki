package com.lyra.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.entity.CollectDoc;
import com.lyra.wiki.entity.vo.CollectVO;
import com.lyra.wiki.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private ICollectService collectService;

    @PostMapping("/addCollect")
    public Result<Object> addCollect(@RequestBody CollectDoc collectDoc) {
        collectService.save(collectDoc);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }

    @GetMapping("/getCollectByDocIdAndUserId")
    public Result<Boolean> getCollectByDocIdAndUserId(Long docId, Long userId) {
        QueryWrapper<CollectDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doc_id", docId);
        queryWrapper.eq("user_id", userId);

        CollectDoc one = collectService.getOne(queryWrapper);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, one == null);
    }

    @GetMapping("/getCollectList")
    public Result<List<CollectVO>> getCollectList(Long userId) {

        List<CollectVO> list = collectService.getCollectList(userId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true, list);
    }

    @PostMapping("/deleteCollectById")
    public Result<Object> deleteCollectById(Long collectId) {
        collectService.removeById(collectId);

        return new Result<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMessage(), true);
    }
}
