package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.*;
import com.example.survey.entity.Question;
import com.example.survey.entity.ResponseDetail;
import com.example.survey.entity.Survey;
import com.example.survey.entity.User;
import com.example.survey.exception.ForbiddenException;
import com.example.survey.exception.ResourceNotFoundException;
import com.example.survey.mapper.QuestionMapper;
import com.example.survey.mapper.ResponseDetailMapper;
import com.example.survey.mapper.ResponseHeaderMapper;
import com.example.survey.mapper.SurveyMapper;
import com.example.survey.service.SurveyService;
import com.example.survey.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private QuestionMapper questionMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ResponseDetailMapper responseDetailMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ResponseHeaderMapper responseHeaderMapper;


    @Override
    @Transactional
    public Survey createSurvey(CreateSurveyRequest request) {
        User currentUser = userService.getCurrentUser();
        Survey survey = new Survey();
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());
        survey.setCreatorId(currentUser.getId());
        survey.setType(request.getType());
        survey.setAccessCode(request.getAccessCode());
        survey.setStartTime(request.getStartTime());
        survey.setEndTime(request.getEndTime());
        survey.setSettings(request.getSettings());
        survey.setStatus(0);
        surveyMapper.insert(survey);

        List<QuestionDto> questionDtos = request.getQuestions();
        if (questionDtos != null && !questionDtos.isEmpty()) {
            List<Question> questionsToInsert = new ArrayList<>();
            for (QuestionDto dto : questionDtos) {
                Question question = new Question();
                question.setSurveyId(survey.getId());
                question.setContent(dto.getContent());
                question.setType(dto.getType());

                // --- 关键修正 ---
                // 如果dto.getIsRequired()为null, 我们默认其为false
                question.setIsRequired(dto.getIsRequired() != null ? dto.getIsRequired() : false);

                question.setOptions(dto.getOptions());
                question.setLogic(dto.getLogic());
                question.setSortOrder(dto.getSortOrder());
                questionsToInsert.add(question);
            }
            questionMapper.batchInsert(questionsToInsert);
        }
        return survey;
    }

    // --- updateSurvey 方法已添加修正 ---


    @Override
    public PageInfo<Survey> findSurveysByCurrentUser(int page, int size, String title, Integer status, String type) {
        User currentUser = userService.getCurrentUser();
        PageHelper.startPage(page, size);
        // 调用Mapper方法，传入新的type参数
        List<Survey> surveys = surveyMapper.findByCreatorId(currentUser.getId(), title, status, type);
        return new PageInfo<>(surveys);
    }
    @Override
    public void updateSurveyStatus(Long id, Integer status) {
        // 1. 权限校验：复用getSurveyDetails来确保只有所有者能修改
        getSurveyDetails(id);

        // 2. 获取当前操作的用户
        User currentUser = userService.getCurrentUser();

        // 3. 核心校验逻辑：如果用户想发布(status=1)，但自身状态是受限(status=1)，则拒绝
        if (Integer.valueOf(1).equals(status) && Integer.valueOf(1).equals(currentUser.getStatus())) {
            throw new ForbiddenException("您的账户已被限制，无法发布新问卷。");
        }

        // 4. 执行更新
        int affectedRows = surveyMapper.updateStatus(id, status);
        if (affectedRows == 0) {
            throw new RuntimeException("更新问卷状态失败，没有记录被修改。");
        }
    }

    @Override
    public Survey getSurveyDetails(Long id) {
        // 1. 根据ID查找问卷
        Survey survey = surveyMapper.findById(id);
        if (survey == null) {
            // 如果问卷不存在，抛出资源未找到异常
            throw new ResourceNotFoundException("问卷不存在");
        }

        // 2. 获取当前登录用户
        User currentUser = userService.getCurrentUser();

        // 3. 权限校验：检查当前用户是否为问卷的创建者
        if (!survey.getCreatorId().equals(currentUser.getId())) {
            // 如果不是创建者，抛出无权限异常
            throw new ForbiddenException("您无权访问此问卷");
        }

        // 4. 返回问卷信息
        return survey;
    }


    @Override
    public void deleteSurvey(Long id) {
        // 1. 验证问卷是否存在以及当前用户是否有权删除
        // 我们复用getSurveyDetails方法，它内部已经包含了权限校验逻辑
        getSurveyDetails(id);

        // 2. 调用Mapper执行删除操作
        // 由于数据库设置了外键的级联删除(ON DELETE CASCADE)，
        // 当删除survey表中的记录时，question表中关联的记录也会被自动删除。
        surveyMapper.deleteById(id);
    }
    @Override
    public Survey getPublicSurveyDetails(Long id, String accessCode) {
        // 1. 根据ID查找问卷
        Survey survey = surveyMapper.findById(id);

        // 2. 验证问卷是否存在且已发布
        if (survey == null || survey.getStatus() != 1) {
            throw new ResourceNotFoundException("问卷不存在或未开放");
        }
        log.info("获取问卷详情，问卷ID: {}, 访问码: {}", id, accessCode);
        log.info("问卷访问吗" + "：{}", survey.getAccessCode());
        // 3. 如果是私密问卷，则必须验证访问码
        if ("PRIVATE".equals(survey.getType())) {
            if (accessCode == null || !accessCode.equals(survey.getAccessCode())) {
                throw new ForbiddenException("访问码错误或缺失，无法访问该问卷");
            }
        }

        // 4. 如果是公开问卷，或私密问卷访问码验证通过，则返回问卷信息
        return survey;
    }
    @Override
    public PageInfo<Survey> findPublicSurveys(int page, int size, String title, String username, LocalDate startDate, LocalDate endDate) {
        PageHelper.startPage(page, size);
        List<Survey> surveys = surveyMapper.findPublicSurveys(title, username, startDate, endDate);
        return new PageInfo<>(surveys);
    }

    @Override
    public PageInfo<ResponseHeaderDto> listResponsesForSurvey(Long surveyId, int page, int size) {
        // 1. 权限校验
        getSurveyDetails(surveyId);

        // 2. 启动分页
        PageHelper.startPage(page, size);

        // 3. 查询数据
        List<ResponseHeaderDto> headers = responseHeaderMapper.findHeadersBySurveyId(surveyId);

        // 4. 用PageInfo包装并返回
        return new PageInfo<>(headers);
    }


    @Override
    public List<AnswerDetailDto> getResponseDetails(Long surveyId, Long responseId) {
        // 1. 权限校验
        getSurveyDetails(surveyId);

        // 2. 获取该答卷的所有回答详情
        List<ResponseDetail> details = responseDetailMapper.findByResponseId(responseId);

        // 3. 获取该问卷的所有问题，以便匹配问题内容
        List<Question> questions = questionMapper.findBySurveyId(surveyId);
        Map<Long, String> questionContentMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Question::getContent));

        // 4. 组装成最终的DTO列表
        return details.stream().map(detail -> {
            AnswerDetailDto dto = new AnswerDetailDto();
            String questionContent = questionContentMap.get(detail.getQuestionId());
            try {
                // 解析问题内容JSON，提取文本
                JsonNode contentNode = objectMapper.readTree(questionContent);
                dto.setQuestionContent(contentNode.at("/blocks/0/data").asText("问题内容获取失败"));
            } catch (JsonProcessingException | NullPointerException e) {
                dto.setQuestionContent("问题内容解析失败");
            }
            // 解析答案JSON，提取文本
            try {
                dto.setAnswerData(objectMapper.readValue(detail.getAnswerData(), String.class));
            } catch(Exception e) {
                dto.setAnswerData(detail.getAnswerData()); // 解析失败则返回原始数据
            }
            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public void updateSurveyMeta(Long id, UpdateSurveyMetaRequest request) {
        // 1. 权限校验：复用getSurveyDetails来确保只有所有者能修改
        Survey existingSurvey = getSurveyDetails(id);

        // 2. 更新对象的字段
        existingSurvey.setTitle(request.getTitle());
        existingSurvey.setDescription(request.getDescription());
        existingSurvey.setType(request.getType());
        existingSurvey.setAccessCode(request.getAccessCode());

        // 3. 调用Mapper执行更新
        surveyMapper.updateMeta(existingSurvey);
    }

}