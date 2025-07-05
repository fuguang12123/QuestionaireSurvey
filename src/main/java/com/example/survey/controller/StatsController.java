package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.PageDto;
import com.example.survey.controller.dto.RawAnswerDto;
import com.example.survey.controller.dto.SurveyStatsResponse;
import com.example.survey.service.StatsService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse; // 引入HttpServletResponse
import java.io.IOException; // 引入IOException
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * 获取指定问卷的统计数据
     * @param surveyId 问卷ID
     * @return 包含统计结果的响应
     */
    @GetMapping("/survey/{surveyId}")
    public Result<SurveyStatsResponse> getStats(@PathVariable Long surveyId) {
        SurveyStatsResponse stats = statsService.getSurveyStats(surveyId);
        return Result.success(stats);
    }
    @GetMapping("/export/{surveyId}")
    public void exportStats(@PathVariable Long surveyId, HttpServletResponse response) throws IOException {
        // 1. 调用Service生成CSV格式的字符串
        String csvData = statsService.exportSurveyData(surveyId);

        // 2. 设置HTTP响应头，告诉浏览器这是一个需要下载的CSV文件
        response.setContentType("text/csv; charset=UTF-8");
        String fileName = "survey_results_" + surveyId + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        //打印返回      内容
        System.out.println("Exporting survey data to CSV: " + csvData);
        // 3. 将CSV数据写入响应流
        response.getWriter().write(csvData);
    }
    @GetMapping("/survey/{surveyId}/question/{questionId}/answers")
    public Result<PageDto<RawAnswerDto>> getRawAnswers(
            @PathVariable Long surveyId,
            @PathVariable Long questionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageInfo<RawAnswerDto> pageInfo = statsService.getRawAnswersForQuestion(surveyId, questionId, page, size);

        PageDto<RawAnswerDto> pageDto = new PageDto<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getList()
        );

        return Result.success(pageDto);
    }
}