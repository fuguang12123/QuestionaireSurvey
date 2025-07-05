package com.example.survey.controller.dto;

import com.example.survey.entity.Survey;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 获取单份问卷详情的响应DTO
 */
@Data
public class SurveyDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String accessCode;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String settings;
    private List<QuestionDetailDto> questions;

    public static SurveyDetailResponse fromSurvey(Survey survey, List<QuestionDetailDto> questions) {
        SurveyDetailResponse response = new SurveyDetailResponse();
        response.setId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setDescription(survey.getDescription());
        response.setType(survey.getType());
        response.setAccessCode(survey.getAccessCode());
        response.setStatus(survey.getStatus());
        response.setStartTime(survey.getStartTime());
        response.setEndTime(survey.getEndTime());
        response.setSettings(survey.getSettings());
        response.setQuestions(questions);
        return response;
    }
}