package com.example.survey.controller.dto;

import lombok.Data;

@Data
public class UpdateSurveyStatusRequest {
    // 0:草稿, 1:发布, 2:结束
    private Integer status;
}