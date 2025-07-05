package com.example.survey.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新问卷的完整请求DTO
 * 它与CreateSurveyRequest结构相同，但为了语义清晰而单独创建
 */
@Data
public class UpdateSurveyRequest {
    private String title;
    private String description;
    private String type;
    private String accessCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String settings;
    // 这里复用 QuestionDto 即可
    private List<QuestionDto> questions;
}