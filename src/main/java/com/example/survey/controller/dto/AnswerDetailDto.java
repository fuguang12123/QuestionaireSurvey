package com.example.survey.controller.dto;

import lombok.Data;

@Data
public class AnswerDetailDto {
    private String questionContent; // 问题内容
    private String answerData; // 答案内容
}