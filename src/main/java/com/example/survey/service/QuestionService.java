package com.example.survey.service;

import com.example.survey.controller.dto.QuestionDto;
import com.example.survey.entity.Question;

import java.util.List;

public interface QuestionService {
    /**
     * 为指定问卷批量添加新问题
     * @param surveyId 问卷ID
     * @param questionDtos 新问题的数据列表
     * @return 包含新生成ID的Question实体列表
     */
    List<Question> addQuestionsBatch(Long surveyId, List<QuestionDto> questionDtos);

    /**
     * 更新单个问题
     * @param surveyId 问卷ID (用于权限校验)
     * @param questionId 问题ID
     * @param questionDto 包含新内容的数据
     */
    void updateQuestion(Long surveyId, Long questionId, QuestionDto questionDto);

    /**
     * 删除指定问题
     * @param surveyId 问卷ID (用于权限校验)
     * @param questionId 问题ID
     */
    void deleteQuestion(Long surveyId, Long questionId);
}