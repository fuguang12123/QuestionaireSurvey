package com.example.survey.service.ServiceImpl;

import com.example.survey.service.StorageService;
import com.example.survey.utils.AliyunOSSOperator; // 引入您之前编写的工具类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("无法存储空文件。");
            }
            // 调用阿里云OSS工具类执行上传
            return aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        } catch (Exception e) {
            // 在实际应用中，这里应该记录详细的错误日志
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
}