package com.lyra.wiki.controller;

import com.lyra.wiki.common.Result;
import com.lyra.wiki.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    private MinioUtils minioUtils;

    @GetMapping("/upload")
    public Result<Object> upload() {

        return null;
    }
}
