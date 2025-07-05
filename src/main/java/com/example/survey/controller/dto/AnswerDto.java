package com.example.survey.controller.dto;

import lombok.Data;

/**
 * 在提交答卷时，用于表示单个问题答案的DTO
 */
@Data
public class AnswerDto {
    private Long questionId;
    private String answerData; // 前端传入的JSON字符串格式的答案
}
