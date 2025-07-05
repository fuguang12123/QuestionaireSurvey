package com.example.survey.controller.dto;

import lombok.Data;

@Data
public class UpdateSurveyMetaRequest {
    private String title;
    private String description;
    private String type;
    private String accessCode;
}
