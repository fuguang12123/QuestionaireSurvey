package com.example.survey.controller.dto;

import lombok.Data;
import java.util.List; // 引入List
import java.util.Map;

@Data
public class QuestionStatsDto {
    private Long questionId;
    private String content;
    private String type;

    // 用于单选/多选题
    private Map<String, Long> optionStats;

    // --- 新增字段：用于存储文本题的所有原始答案 ---
    private List<String> rawAnswers;
}