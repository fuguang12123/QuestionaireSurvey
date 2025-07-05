package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.FileUploadResponse;
import com.example.survey.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    /**
     * 通用文件上传接口
     * @param file 通过multipart/form-data方式上传的文件
     * @return 包含文件URL的成功响应
     */
    @PostMapping("/upload")
    public Result<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = storageService.store(file);
        return Result.success(new FileUploadResponse(url));
    }
}