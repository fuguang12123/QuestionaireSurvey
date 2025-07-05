package com.example.survey.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    /**
     * 文件上传成功后返回的公开访问URL
     */
    private String url;
}