package com.example.survey.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建问卷的完整请求DTO
 */
@Data
public class CreateSurveyRequest {
    private String title;
    private String description;
    private String type;
    private String accessCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String settings; // 前端传入的JSON字符串
    private List<QuestionDto> questions;
}