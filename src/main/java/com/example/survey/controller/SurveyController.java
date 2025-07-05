package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.*;
import com.example.survey.entity.Question;
import com.example.survey.entity.Survey;
import com.example.survey.mapper.QuestionMapper;
import com.example.survey.service.SurveyService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // 引入List
import java.util.Map;
import java.util.stream.Collectors; // 引入Collectors

@Slf4j
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private QuestionMapper questionMapper;

    @PostMapping
    public Result<Map<String, Long>> createSurvey(@RequestBody CreateSurveyRequest request) {
        // ... 已有的 createSurvey 方法 ...
        Survey createdSurvey = surveyService.createSurvey(request);
        Map<String, Long> response = Collections.singletonMap("surveyId", createdSurvey.getId());
        return Result.success(response);
    }
    /**
     * 更新问卷元数据（标题、描述、访问码等）
     * @param id 问卷ID
     * @param request 包含更新信息的请求体
     * @return 成功响应
     */
    @PatchMapping("/{id}/meta")
    public Result<Void> updateSurveyMeta(@PathVariable Long id, @RequestBody UpdateSurveyMetaRequest request) {
        surveyService.updateSurveyMeta(id, request);
        return Result.success();
    }
    /**
     * 获取当前用户创建的所有问卷列表
     * @return 问卷列表响应
     */
    @GetMapping("/user")
    public Result<PageDto<SurveyListItemResponse>> getUserSurveys(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String type) { // 新增type参数

        PageInfo<Survey> surveyPageInfo = surveyService.findSurveysByCurrentUser(page, size, title, status, type);

        List<SurveyListItemResponse> responseList = surveyPageInfo.getList().stream()
                .map(SurveyListItemResponse::fromSurvey)
                .collect(Collectors.toList());

        PageDto<SurveyListItemResponse> pageDto = new PageDto<>(
                surveyPageInfo.getPageNum(), surveyPageInfo.getPageSize(), surveyPageInfo.getTotal(), responseList
        );

        return Result.success(pageDto);
    }

    @GetMapping("/{id}")
        public Result<SurveyDetailResponse> getSurveyById(@PathVariable Long id) {
        // 1. 调用Service获取问卷主体信息（已包含权限校验）
        Survey survey = surveyService.getSurveyDetails(id);

        // 2. 根据问卷ID获取其所有问题
        List<Question> questions = questionMapper.findBySurveyId(id);

        // 3. 将Question实体列表转换为DTO列表
        List<QuestionDetailDto> questionDtos = questions.stream()
                .map(QuestionDetailDto::fromQuestion)
                .collect(Collectors.toList());

        // 4. 组装最终的响应DTO
        SurveyDetailResponse response = SurveyDetailResponse.fromSurvey(survey, questionDtos);

        return Result.success(response);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return Result.success();
    }
    /**
     * 获取公开问卷的详情（供答题人使用）
     * @param id 问卷ID
     * @return 问卷的公开详细信息
     */
    @GetMapping("/{id}/details")
    public Result<PublicSurveyDetailResponse> getPublicSurveyById(
            @PathVariable Long id,
            @RequestParam(required = false)String accessCode) { // 新增 accessCode 参数

        Survey survey = surveyService.getPublicSurveyDetails(id, accessCode);
        log.info("获取问卷详情，问卷ID: {}, 访问码: {}", id, accessCode);
        List<Question> questions = questionMapper.findBySurveyId(id);

        List<QuestionDetailDto> questionDtos = questions.stream()
                .map(QuestionDetailDto::fromQuestion)
                .collect(Collectors.toList());
        PublicSurveyDetailResponse response = PublicSurveyDetailResponse.fromSurvey(survey, questionDtos);

        return Result.success(response);
    }
    @GetMapping("/public")
    public Result<PageDto<SurveyListItemResponse>> getPublicSurveys(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        PageInfo<Survey> surveyPageInfo = surveyService.findPublicSurveys(page, size, title, username, startDate, endDate);

        List<SurveyListItemResponse> responseList = surveyPageInfo.getList().stream()
                .map(SurveyListItemResponse::fromSurvey)
                .collect(Collectors.toList());

        PageDto<SurveyListItemResponse> pageDto = new PageDto<>(
                surveyPageInfo.getPageNum(), surveyPageInfo.getPageSize(), surveyPageInfo.getTotal(), responseList
        );

        return Result.success(pageDto);
    }
    @PatchMapping("/{id}/status")
    public Result<Void> updateSurveyStatus(@PathVariable Long id, @RequestBody UpdateSurveyStatusRequest request) {
        surveyService.updateSurveyStatus(id, request.getStatus());
        return Result.success();
    }

    @GetMapping("/{surveyId}/responses")
    public Result<PageDto<ResponseHeaderDto>> getSurveyResponses(
            @PathVariable Long surveyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageInfo<ResponseHeaderDto> pageInfo = surveyService.listResponsesForSurvey(surveyId, page, size);

        PageDto<ResponseHeaderDto> pageDto = new PageDto<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );

        return Result.success(pageDto);
    }

    @GetMapping("/{surveyId}/responses/{responseId}")
    public Result<List<AnswerDetailDto>> getSingleResponseDetails(
            @PathVariable Long surveyId,
            @PathVariable Long responseId) {
        List<AnswerDetailDto> details = surveyService.getResponseDetails(surveyId, responseId);
        return Result.success(details);
    }

}
