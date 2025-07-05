package com.example.survey.controller.dto;

import com.example.survey.entity.Question;
import lombok.Data;

/**
 * 在问卷详情中，用于表示单个问题的DTO
 */
@Data
public class QuestionDetailDto {
    private Long id;
    private String content;
    private String type;
    private Boolean isRequired;
    private String options;
    private String logic;
    private Integer sortOrder;

    public static QuestionDetailDto fromQuestion(Question question) {
        QuestionDetailDto dto = new QuestionDetailDto();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setType(question.getType());
        dto.setIsRequired(question.getIsRequired());
        dto.setOptions(question.getOptions());
        dto.setLogic(question.getLogic());
        dto.setSortOrder(question.getSortOrder());
        return dto;
    }
}
