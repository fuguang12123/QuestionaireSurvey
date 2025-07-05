package com.example.survey.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * 存储上传的文件
     * @param file 前端上传的文件
     * @return 文件的公开访问URL
     */
    String store(MultipartFile file);
}