package com.example.survey.controller.dto;

import lombok.Data;
import java.util.List;

/**
 * 整份问卷的统计结果响应 DTO
 */
@Data
public class SurveyStatsResponse {

    private Long surveyId;
    private String title;

    /**
     * 问卷总提交数
     */
    private long totalSubmissions;

    /**
     * 每个问题的详细统计列表
     */
    private List<QuestionStatsDto> questionStats;
}