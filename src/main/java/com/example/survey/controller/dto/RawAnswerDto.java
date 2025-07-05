package com.example.survey.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RawAnswerDto {
    private Long responseId; // 所属答卷ID
    private String answerData; // 答案内容
    private LocalDateTime submittedAt; // 提交时间
    private String submitterUsername; // 提交者用户名 (可能为 "Anonymous")
}