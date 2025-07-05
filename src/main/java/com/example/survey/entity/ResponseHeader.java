package com.example.survey.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 答卷头实体类，对应 response_header 表
 */
@Data
public class ResponseHeader {
    private Long id;
    private Long surveyId;
    private String sessionId;
    private Long userId; // 可为空，代表匿名用户
    private String ipAddress;
    private String deviceInfo;
    private LocalDateTime submittedAt;
}