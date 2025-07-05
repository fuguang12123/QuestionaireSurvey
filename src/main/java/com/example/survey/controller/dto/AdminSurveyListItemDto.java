package com.example.survey.controller.dto;

import com.example.survey.entity.Survey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // 继承父类的equals和hashCode
public class AdminSurveyListItemDto extends SurveyListItemResponse {

    private Long creatorId;
    private String creatorUsername;

    // 扩展静态工厂方法，从Survey和User实体创建DTO
    public static AdminSurveyListItemDto fromSurvey(Survey survey) {
        AdminSurveyListItemDto dto = new AdminSurveyListItemDto();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setStatus(survey.getStatus());
        dto.setType(survey.getType());
        dto.setCreatedAt(survey.getCreatedAt());
        // 如果Survey实体中包含了关联的User对象，则填充创建者信息
        if (survey.getCreator() != null) {
            dto.setCreatorId(survey.getCreator().getId());
            dto.setCreatorUsername(survey.getCreator().getUsername());
        }
        return dto;
    }
}