// --- File: src/test/java/com/example/survey/service/StatsServiceTest.java ---
// --- (请用此版本替换您现有的测试文件) ---
package com.example.survey;

import com.example.survey.controller.dto.QuestionStatsDto;
import com.example.survey.controller.dto.SurveyStatsResponse;
import com.example.survey.entity.*;
import com.example.survey.mapper.*;
import com.example.survey.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StatsService 的集成测试类
 */
@SpringBootTest
@Transactional
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ResponseHeaderMapper responseHeaderMapper;
    @Autowired
    private ResponseDetailMapper responseDetailMapper;

    private Survey testSurvey;
    private Question radioQuestion;

    @BeforeEach
    void setUp() {
        // 1. 创建一个测试用户，并显式设置所有 NOT NULL 字段，这是一个好习惯
        User testUser = new User();
        testUser.setUsername("stats_test_user");
        testUser.setPassword("password");
        testUser.setEmail("stats@example.com");
        testUser.setRole("USER");           // NOT NULL 字段
        testUser.setStatus(0);              // NOT NULL 字段
        userMapper.insert(testUser);

        // 2. 用该用户创建一个问卷，并显式设置所有 NOT NULL 字段
        Survey survey = new Survey();
        survey.setTitle("用于统计测试的问卷");
        survey.setCreatorId(testUser.getId()); // NOT NULL 字段
        survey.setStatus(1);                   // NOT NULL 字段
        survey.setType("PUBLIC");              // NOT NULL 字段
        surveyMapper.insert(survey);
        this.testSurvey = survey;

        // 3. 为问卷创建问题，并显式设置所有 NOT NULL 字段
        Question q1 = new Question();
        q1.setSurveyId(survey.getId());         // NOT NULL 字段
        q1.setContent("{\"blocks\":[{\"type\":\"text\",\"data\":\"您最喜欢的颜色是？\"}]}"); // NOT NULL 字段
        q1.setType("RADIO");                    // NOT NULL 字段
        q1.setIsRequired(true);                 // NOT NULL 字段
        q1.setSortOrder(1);                     // NOT NULL 字段
        q1.setOptions("[\"红色\", \"蓝色\", \"绿色\"]");
        questionMapper.batchInsert(List.of(q1));

        // 在 batchInsert 后，q1 的 ID 仍然是 null，因为 batchInsert 没有配置主键回填
        // 我们需要重新查询一次来获取完整的 Question 对象
        this.radioQuestion = questionMapper.findBySurveyId(survey.getId()).get(0);

        // 4. 模拟3次答卷提交
        submitResponse("session1", testUser.getId(), "\"红色\"");
        submitResponse("session2", null, "\"蓝色\"");
        submitResponse("session3", testUser.getId(), "\"红色\"");
    }

    private void submitResponse(String sessionId, Long userId, String answerData) {
        ResponseHeader header = new ResponseHeader();
        header.setSurveyId(testSurvey.getId());
        header.setSessionId(sessionId);
        header.setUserId(userId);
        responseHeaderMapper.insert(header);

        ResponseDetail detail = new ResponseDetail();
        detail.setResponseId(header.getId());
        detail.setQuestionId(radioQuestion.getId());
        detail.setAnswerData(answerData);
        responseDetailMapper.batchInsert(List.of(detail));
    }

    @Test
    @WithMockUser(username = "stats_test_user")
    void testGetSurveyStats() {
        // --- 1. Act ---
        SurveyStatsResponse stats = statsService.getSurveyStats(testSurvey.getId());

        // --- 2. Assert ---
        assertNotNull(stats);
        assertEquals(testSurvey.getId(), stats.getSurveyId());
        assertEquals(3, stats.getTotalSubmissions());

        QuestionStatsDto radioStats = stats.getQuestionStats().get(0);
        assertEquals(radioQuestion.getId(), radioStats.getQuestionId());
        assertEquals(2L, radioStats.getOptionStats().get("红色"));
        assertEquals(1L, radioStats.getOptionStats().get("蓝色"));
        assertNull(radioStats.getOptionStats().get("绿色"));
    }
}
