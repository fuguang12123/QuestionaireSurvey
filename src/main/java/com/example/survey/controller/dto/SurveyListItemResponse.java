package com.example.survey.controller.dto;

import com.example.survey.entity.Survey;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SurveyListItemResponse {
    private Long id;
    private String title;
    private Integer status;
    private String type;
    private LocalDateTime createdAt;
    private String accessCode;

    // 新增字段
    private String creatorUsername;

    public static SurveyListItemResponse fromSurvey(Survey survey) {
        SurveyListItemResponse response = new SurveyListItemResponse();
        response.setId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setStatus(survey.getStatus());
        response.setType(survey.getType());
        response.setCreatedAt(survey.getCreatedAt());
        response.setAccessCode(survey.getAccessCode());
        // 如果Survey实体通过JOIN查询填充了creator信息
        if (survey.getCreator() != null) {
            response.setCreatorUsername(survey.getCreator().getUsername());
        }
        return response;
    }
}