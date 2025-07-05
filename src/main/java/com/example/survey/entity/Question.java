// --- File: src/main/java/com/example/survey/entity/Question.java ---
package com.example.survey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 问题实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
    private Long id;
    private Long surveyId;
    private String content; // JSON
    private String type; // RADIO, CHECKBOX, TEXT, RATING, FILE
    private Boolean isRequired;
    private String options; // JSON
    private String logic; // JSON
    private Integer sortOrder;
    private LocalDateTime createdAt;
}