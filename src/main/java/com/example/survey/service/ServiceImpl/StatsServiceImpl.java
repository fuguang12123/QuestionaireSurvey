package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.QuestionStatsDto;
import com.example.survey.controller.dto.RawAnswerDto;
import com.example.survey.controller.dto.SurveyStatsResponse;
import com.example.survey.entity.Question;
import com.example.survey.entity.ResponseDetail;
import com.example.survey.entity.ResponseHeader;
import com.example.survey.entity.Survey;
import com.example.survey.mapper.QuestionMapper;
import com.example.survey.mapper.ResponseDetailMapper;
import com.example.survey.mapper.ResponseHeaderMapper;
import com.example.survey.service.StatsService;
import com.example.survey.service.SurveyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap; // 引入 HashMap
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ResponseHeaderMapper responseHeaderMapper;
    @Autowired
    private ResponseDetailMapper responseDetailMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SurveyStatsResponse getSurveyStats(Long surveyId) {  
        Survey survey = surveyService.getSurveyDetails(surveyId);
        List<Question> questions = questionMapper.findBySurveyId(surveyId);
        List<ResponseDetail> allDetails = responseDetailMapper.findBySurveyId(surveyId);

        Map<Long, List<ResponseDetail>> detailsByQuestionId = allDetails.stream()
                .collect(Collectors.groupingBy(ResponseDetail::getQuestionId));

        SurveyStatsResponse response = new SurveyStatsResponse();
        response.setSurveyId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setTotalSubmissions(responseHeaderMapper.countBySurveyId(surveyId));

        List<QuestionStatsDto> questionStatsList = new ArrayList<>();
        for (Question question : questions) {
            QuestionStatsDto questionStatsDto = new QuestionStatsDto();
            questionStatsDto.setQuestionId(question.getId());
            questionStatsDto.setContent(question.getContent());
            questionStatsDto.setType(question.getType());

            List<ResponseDetail> questionDetails = detailsByQuestionId.getOrDefault(question.getId(), new ArrayList<>());

            if ("RADIO".equals(question.getType()) || "CHECKBOX".equals(question.getType())) {
                Map<String, Long> optionCounts = questionDetails.stream()
                        .flatMap(detail -> parseAnswer(detail.getAnswerData()).stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                questionStatsDto.setOptionStats(optionCounts);
            }
            // --- 关键修正：如果问题类型是文本题 ---
            else if ("TEXT".equals(question.getType())) {
                // 将所有原始答案提取出来，放入rawAnswers列表
                List<String> answers = questionDetails.stream()
                        .map(ResponseDetail::getAnswerData)
                        .collect(Collectors.toList());
                questionStatsDto.setRawAnswers(answers);
            }
            questionStatsList.add(questionStatsDto);
        }
        response.setQuestionStats(questionStatsList);

        return response;
    }

    // --- 关键修正：重写 exportSurveyData 方法的实现 ---
    @Override
    public String exportSurveyData(Long surveyId) {
        // 1. 获取所有需要的数据
        surveyService.getSurveyDetails(surveyId); // 这步主要用于权限校验
        List<Question> questions = questionMapper.findBySurveyId(surveyId);
        List<ResponseHeader> headers = responseHeaderMapper.findBySurveyId(surveyId);
        List<ResponseDetail> details = responseDetailMapper.findBySurveyId(surveyId);

        // 2. 将所有答案详情放入一个易于查找的Map中
        // Key是 "答卷ID-问题ID"，Value是答案字符串
        Map<String, String> answerMap = new HashMap<>();
        for (ResponseDetail detail : details) {
            String key = detail.getResponseId() + "-" + detail.getQuestionId();
            answerMap.put(key, detail.getAnswerData());
            System.out.println("Mapping answer: " + key + " -> " + detail.getAnswerData());
        }

        // 3. 构建CSV字符串
        StringBuilder csvBuilder = new StringBuilder();
        // 构建表头...
        csvBuilder.append("Submission ID,Submission Time,Submitter UserID,");
        questions.forEach(q -> {
            try {
                JsonNode contentNode = objectMapper.readTree(q.getContent());
                String questionText = contentNode.at("/blocks/0/data").asText("Question " + q.getId());
                csvBuilder.append("\"").append(questionText.replace("\"", "\"\"")).append("\",");
            } catch (JsonProcessingException e) {
                csvBuilder.append("Question ").append(q.getId()).append(",");
            }
        });
        csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");

        // 构建数据行
        for (ResponseHeader header : headers) {
            csvBuilder.append(header.getId()).append(",");
            csvBuilder.append(header.getSubmittedAt()).append(",");
            csvBuilder.append(header.getUserId() != null ? header.getUserId() : "Anonymous").append(",");

            // 对每一个问题，都去Map中查找对应的答案
            for (Question question : questions) {
                String lookupKey = header.getId() + "-" + question.getId(); // 使用新的Map查找
                String answer = answerMap.getOrDefault(lookupKey,   ""); // 使用新的Map进行查找
                System.out.println("Exporting answer for key: " + lookupKey + " - Answer: " + answer+"!!!!!!!!!!!!!!");
                answer = answer.replace(",", "，"); // 简单清理一下，防止破坏CSV格式
                csvBuilder.append("\"").append(answer).append("\",");
            }
            csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");
        }

        return csvBuilder.toString();
    }

    // ... (parseAnswer 方法无变化) ...
    private List<String> parseAnswer(String answerData) {
        if (answerData == null || answerData.isEmpty()) {
            return List.of();
        }
        try {
            if (answerData.startsWith("[")) {
                return objectMapper.readValue(answerData, new TypeReference<List<String>>() {});
            }
            return List.of(answerData);
        } catch (JsonProcessingException e) {
            return List.of(answerData);
        }
    }
    @Override
    public PageInfo<RawAnswerDto> getRawAnswersForQuestion(Long surveyId, Long questionId, int page, int size) {
        // 1. 权限校验：确保当前用户有权查看该问卷的统计数据
        surveyService.getSurveyDetails(surveyId);

        // 2. 启动分页
        PageHelper.startPage(page, size);

        // 3. 查询数据
        List<RawAnswerDto> rawAnswers = responseDetailMapper.findRawAnswersByQuestionId(questionId);

        // 4. 用PageInfo包装并返回
        return new PageInfo<>(rawAnswers);
    }

}