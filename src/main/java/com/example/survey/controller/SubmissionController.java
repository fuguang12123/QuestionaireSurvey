package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.SubmitSurveyRequest;
import com.example.survey.service.SubmissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submit")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    /**
     * 提交指定ID的问卷答案
     * @param surveyId 问卷ID
     * @param request 包含答案和访问码的请求体
     * @param httpRequest 用于获取IP等信息
     * @return 提交成功响应
     */
    @PostMapping("/{surveyId}")
    public Result<Void> submit(@PathVariable Long surveyId,
                               @RequestBody SubmitSurveyRequest request,
                               HttpServletRequest httpRequest) {

        System.out.println("Received submission for survey ID: " + surveyId);
        submissionService.submitSurvey(surveyId, request, httpRequest);
        System.out.println("Received submission for survey ID: " + surveyId);
        return Result.success();
    }
}