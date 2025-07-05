package com.example.survey.service;

import com.example.survey.controller.dto.SubmitSurveyRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface SubmissionService {
    /**
     * 处理问卷提交
     * @param surveyId 问卷ID
     * @param request 提交的答卷数据
     * @param httpRequest 用于获取IP等信息
     */
    void submitSurvey(Long surveyId, SubmitSurveyRequest request, HttpServletRequest httpRequest);
}