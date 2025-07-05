package com.example.survey.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.example.survey.entity.AliyunOssProperity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliyunOSSOperator {

    @Autowired
    private AliyunOssProperity aliyunOssProperity;

    public String upload(byte[] content, String originalFilename) throws Exception {
        String endpoint = aliyunOssProperity.getEndpoint();
        String bucketName = aliyunOssProperity.getBucketName();
        // region 字段在此逻辑中暂时未使用，但保留
        // String region = aliyunOssProperity.getRegion();

        // 推荐从配置文件或环境变量中获取AccessKey，这里遵循您之前的代码
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = dir + "/" + newFileName;

        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }


        String url = "https://" + bucketName + "." + endpoint + "/" + objectName;

        return url;
    }
}