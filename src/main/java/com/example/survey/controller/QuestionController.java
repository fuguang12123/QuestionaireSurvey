package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.QuestionDto;
import com.example.survey.entity.Question;
import com.example.survey.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/surveys/{surveyId}/questions")
public class QuestionController {
    @Autowired private QuestionService questionService;

    /**
     * 批量添加新问题到指定问卷
     * @param surveyId 问卷ID
     * @param questionDtos 新问题的数据列表
     * @return 包含新生成ID的Question实体列表
     */
    @PostMapping("/batch")
    public Result<List<Long>> addQuestionsBatch(
            @PathVariable Long surveyId,
            @RequestBody List<QuestionDto> questionDtos) {
        List<Question> newQuestions = questionService.addQuestionsBatch(surveyId, questionDtos);
        // 返回新创建的问题的ID列表，方便前端更新状态
        List<Long> newIds = newQuestions.stream().map(Question::getId).collect(Collectors.toList());
        return Result.success(newIds);
    }

    /**
     * 更新单个问题
     */
    @PutMapping("/{questionId}")
    public Result<Void> updateQuestion(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @RequestBody QuestionDto questionDto) {
        questionService.updateQuestion(surveyId, questionId, questionDto);
        return Result.success();
    }

    /**
     * 删除单个问题
     */
    @DeleteMapping("/{questionId}")
    public Result<Void> deleteQuestion(
            @PathVariable Long surveyId,
            @PathVariable Long questionId) {
        questionService.deleteQuestion(surveyId, questionId);
        return Result.success();
    }
}
