package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.AnswerDto;
import com.example.survey.controller.dto.SubmitSurveyRequest;
import com.example.survey.entity.*;
import com.example.survey.exception.ForbiddenException;
import com.example.survey.exception.InvalidSubmissionDataException;
import com.example.survey.exception.ResourceNotFoundException;
import com.example.survey.exception.TooManyRequestsException;
import com.example.survey.mapper.*;
import com.example.survey.service.SubmissionService;
import com.example.survey.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ResponseHeaderMapper responseHeaderMapper;
    @Autowired
    private ResponseDetailMapper responseDetailMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final long SUBMIT_LOCK_TIMEOUT_SECONDS = 30;

    @Override
    @Transactional
    public void submitSurvey(Long surveyId, SubmitSurveyRequest request, HttpServletRequest httpRequest) {
        String ipAddress = httpRequest.getRemoteAddr();
        String redisKey = "survey:submit_lock:" + surveyId + ":" + ipAddress;
        log.info(redisKey);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            throw new TooManyRequestsException("您提交得太频繁了，请稍后再试");
        }

        Survey survey = surveyMapper.findById(surveyId);
        if (survey == null || survey.getStatus() != 1) {
            throw new ResourceNotFoundException("问卷不存在或未开放填写");
        }

        if ("PRIVATE".equals(survey.getType())) {
            System.out.println("accessCode: " + request.getAccessCode());
            if (request.getAccessCode() == null || !request.getAccessCode().equals(survey.getAccessCode())) {
                throw new ForbiddenException("访问码错误");
            }
        }

        List<Question> questionsInDb = questionMapper.findBySurveyId(surveyId);
        Set<Long> validQuestionIds = questionsInDb.stream()
                .map(Question::getId)
                .collect(Collectors.toSet());

        for (AnswerDto answerDto : request.getAnswers()) {
            if (!validQuestionIds.contains(answerDto.getQuestionId())) {
                throw new InvalidSubmissionDataException("提交的答案包含无效的问题ID：" + answerDto.getQuestionId());
            }
        }

        ResponseHeader header = new ResponseHeader();
        header.setSurveyId(surveyId);
        header.setSessionId(UUID.randomUUID().toString());
        header.setIpAddress(ipAddress);
        header.setDeviceInfo(httpRequest.getHeader("User-Agent"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            User currentUser = userService.getCurrentUser();
            header.setUserId(currentUser.getId());
        }

        responseHeaderMapper.insert(header);

        List<ResponseDetail> details = new ArrayList<>();
        for (AnswerDto answerDto : request.getAnswers()) {
            ResponseDetail detail = new ResponseDetail();
            detail.setResponseId(header.getId());
            detail.setQuestionId(answerDto.getQuestionId());
            detail.setAnswerData(answerDto.getAnswerData());
            details.add(detail);
        }

        if (!details.isEmpty()) {
            responseDetailMapper.batchInsert(details);
        }


        redisTemplate.opsForValue().set(redisKey, "locked", SUBMIT_LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
}