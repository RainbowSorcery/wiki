package com.lyra.wiki.controller;

import com.lyra.wiki.common.Result;
import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.exception.MyGraceException;
import com.lyra.wiki.utils.MinioUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequestMapping("/file")
@RestController
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private MinioUtils minioUtils;

    @PostMapping("/upload")
    public Result<Object> upload(MultipartFile file) {
        String suffix = "";
        if (StringUtils.isNotBlank(file.getOriginalFilename())) {
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        }


        try {
            InputStream fileInputStream = file.getInputStream();

            String fileUrl = minioUtils.fileUpload(UUID.randomUUID().toString() + suffix, fileInputStream, file.getSize());

            return new Result<>(ResponseEnums.FILE_UPLOAD_SUCCESS.getCode(), ResponseEnums.FILE_UPLOAD_SUCCESS.getMessage(), true, fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败, 错误信息:{}", e.getMessage());
            throw new MyGraceException(ResponseEnums.FILE_UPLOAD_FILED);
        }
    }
}
