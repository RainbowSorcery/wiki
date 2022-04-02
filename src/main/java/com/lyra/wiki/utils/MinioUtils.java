package com.lyra.wiki.utils;

import com.lyra.wiki.common.constant.ResponseEnums;
import com.lyra.wiki.exception.MyGraceException;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MinioUtils {
    @Autowired
    private MinioResource minioResource;

    private static final Logger log = LoggerFactory.getLogger(MinioUtils.class);

    public String fileUpload(String fileName, InputStream fileInputStream, long size) {
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(minioResource.getEndpoint())
                        .credentials(minioResource.getAccessKey(), minioResource.getSecretKey())
                        .build();

        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(minioResource.getBucket()).object(fileName).stream(fileInputStream, size, -1).build());

            return minioResource.getEndpoint() + minioResource.getBucket() + "/" + fileName;
        } catch (Exception e) {
            log.error("文件上传失败, 错误信息:{}", e.getMessage());
            throw new MyGraceException(ResponseEnums.FILE_UPLOAD_FILED);
        }
    }
}
