package com.example.survey.controller.dto;

import lombok.Data;
import java.util.List;

/**
 * 提交答卷的完整请求DTO
 */
@Data
public class SubmitSurveyRequest {
    // 如果是隐私问卷，需要提供访问码
    private String accessCode;
    // 包含所有问题的答案列表
    private List<AnswerDto> answers;
}