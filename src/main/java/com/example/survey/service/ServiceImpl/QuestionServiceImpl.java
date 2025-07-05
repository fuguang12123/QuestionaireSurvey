package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.QuestionDto;
import com.example.survey.entity.Question;
import com.example.survey.exception.ResourceNotFoundException;
import com.example.survey.mapper.QuestionMapper;
import com.example.survey.mapper.ResponseDetailMapper;
import com.example.survey.service.QuestionService;
import com.example.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


    @Service
    public class QuestionServiceImpl implements QuestionService {
        @Autowired private SurveyService surveyService;
        @Autowired private QuestionMapper questionMapper;
        @Autowired private ResponseDetailMapper responseDetailMapper;

        @Override
        @Transactional
        public List<Question> addQuestionsBatch(Long surveyId, List<QuestionDto> questionDtos) {
            // 权限校验
            surveyService.getSurveyDetails(surveyId);

            List<Question> newQuestions = new ArrayList<>();
            if (questionDtos == null || questionDtos.isEmpty()) {
                return newQuestions;
            }

            for (QuestionDto dto : questionDtos) {
                Question question = new Question();
                question.setSurveyId(surveyId);
                question.setContent(dto.getContent());
                question.setType(dto.getType());
                question.setIsRequired(dto.getIsRequired() != null ? dto.getIsRequired() : false);
                question.setOptions(dto.getOptions());
                question.setLogic(dto.getLogic());
                question.setSortOrder(dto.getSortOrder());

                // 逐个插入，以确保能获取到回填的ID
                questionMapper.insert(question);
                newQuestions.add(question);
            }
            return newQuestions;
        }

        @Override
        @Transactional
        public void updateQuestion(Long surveyId, Long questionId, QuestionDto questionDto) {
            surveyService.getSurveyDetails(surveyId);
            responseDetailMapper.deleteByQuestionId(questionId);

            Question questionToUpdate = new Question();
            questionToUpdate.setId(questionId);
            questionToUpdate.setContent(questionDto.getContent());
            questionToUpdate.setType(questionDto.getType());
            questionToUpdate.setIsRequired(questionDto.getIsRequired());
            questionToUpdate.setOptions(questionDto.getOptions());
            questionToUpdate.setLogic(questionDto.getLogic());
            questionToUpdate.setSortOrder(questionDto.getSortOrder());

            questionMapper.update(questionToUpdate);
        }

        @Override
        @Transactional
        public void deleteQuestion(Long surveyId, Long questionId) {
            // 权限校验
            surveyService.getSurveyDetails(surveyId);

            // 可以在这里增加一步校验，确保questionId确实属于surveyId
            // ...

            // 删除问题前，先删除其所有回答，以保证数据一致性
            responseDetailMapper.deleteByQuestionId(questionId);

            // 删除问题本身
            int affectedRows = questionMapper.deleteById(questionId);
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("ID为 " + questionId + " 的问题不存在");
            }
        }
    }

