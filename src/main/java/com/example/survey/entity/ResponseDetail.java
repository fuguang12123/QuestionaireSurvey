package com.example.survey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 回答详情实体类，对应 response_detail 表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDetail {
    private Long id;
    private Long responseId;
    private Long questionId;
    // --- 关键修正：将字段类型从 String 修改为 byte[] ---
    private String answerData;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "ResponseDetail{" +
                "id=" + id +
                ", responseId=" + responseId +
                ", questionId=" + questionId +
                ", answerData='" + answerData + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}