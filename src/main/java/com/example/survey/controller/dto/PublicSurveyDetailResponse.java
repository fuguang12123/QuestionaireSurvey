package com.example.survey.controller.dto;

import com.example.survey.controller.dto.QuestionDetailDto;
import com.example.survey.entity.Survey;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PublicSurveyDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuestionDetailDto> questions; // 复用已有的QuestionDetailDto

    public static PublicSurveyDetailResponse fromSurvey(Survey survey, List<QuestionDetailDto> questions) {
        PublicSurveyDetailResponse response = new PublicSurveyDetailResponse();
        response.setId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setDescription(survey.getDescription());
        response.setType(survey.getType());
        response.setStartTime(survey.getStartTime());
        response.setEndTime(survey.getEndTime());
        response.setQuestions(questions);
        return response;
    }
}