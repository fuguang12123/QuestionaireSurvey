package com.example.survey.service;

import com.example.survey.controller.dto.RawAnswerDto;
import com.example.survey.controller.dto.SurveyStatsResponse;
import com.github.pagehelper.PageInfo;
public interface StatsService {
    /**
     * 获取指定问卷的统计数据
     * @param surveyId 问卷ID
     * @return 组装好的统计数据响应对象
     */
    SurveyStatsResponse getSurveyStats(Long surveyId);

    /**
     * 导出指定问卷的原始数据为CSV格式字符串
     * @param surveyId 问卷ID
     * @return CSV格式的字符串
     */
    String exportSurveyData(Long surveyId);
    PageInfo<RawAnswerDto> getRawAnswersForQuestion(Long surveyId, Long questionId, int page, int size);
}