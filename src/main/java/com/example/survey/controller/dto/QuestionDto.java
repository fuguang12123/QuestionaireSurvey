// --- File: src/main/java/com/example/survey/controller/dto/QuestionDto.java ---
package com.example.survey.controller.dto;

import lombok.Data;

/**
 * 创建问卷时，用于表示单个问题的DTO
 */
@Data
public class QuestionDto {
    private String content;
    private String type;
    private Boolean isRequired;
    private String options; // 前端传入的JSON字符串
    private String logic;   // 前端传入的JSON字符串
    private Integer sortOrder;
}