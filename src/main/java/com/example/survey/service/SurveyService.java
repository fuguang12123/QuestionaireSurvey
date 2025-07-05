package com.example.survey.service;

import com.example.survey.controller.dto.*;
import com.example.survey.entity.Survey;
import com.github.pagehelper.PageInfo;

import java.time.LocalDate;
import java.util.List;

public interface SurveyService {

    Survey createSurvey(CreateSurveyRequest request);
    Survey getSurveyDetails(Long id);
    void deleteSurvey(Long id);
    Survey getPublicSurveyDetails(Long id, String accessCode);
    PageInfo<Survey> findSurveysByCurrentUser(int page, int size, String title, Integer status, String type);
    void updateSurveyStatus(Long id, Integer status);
    PageInfo<Survey> findPublicSurveys(int page, int size, String title, String username, LocalDate startDate, LocalDate endDate);
    PageInfo<ResponseHeaderDto> listResponsesForSurvey(Long surveyId, int page, int size);
    List<AnswerDetailDto> getResponseDetails(Long surveyId, Long responseId);
    void updateSurveyMeta(Long id, UpdateSurveyMetaRequest request);


}