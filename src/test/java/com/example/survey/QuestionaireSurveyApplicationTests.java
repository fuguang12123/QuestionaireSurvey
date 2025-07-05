package com.example.survey;

import com.example.survey.entity.Question;
import com.example.survey.entity.Survey;
import com.example.survey.mapper.QuestionMapper;
import com.example.survey.mapper.SurveyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionaireSurveyApplicationTests {
    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private QuestionMapper questionMapper;

    private Survey testSurvey; //
    @Test
    void contextLoads() {
    }
    // 在每个测试方法运行前，先创建一个问卷
    @BeforeEach
    void setUp() {
        Survey survey = new Survey();
        survey.setTitle("用于测试问题的前置问卷");
        survey.setDescription("描述");
        survey.setCreatorId(1L); // 假设ID为1的用户存在
        survey.setType("PUBLIC");
        survey.setStatus(0);
        surveyMapper.insert(survey);
        this.testSurvey = survey; // 保存这个已插入的问卷
    }

    @Test
    void testInsertSurvey() {
        // 创建一个问题列表
        List<Question> questionsToInsert = new ArrayList<>();

        Question q1 = new Question();
        q1.setSurveyId(testSurvey.getId());
        q1.setContent("问题1的内容");
        q1.setType("TEXT");
        q1.setIsRequired(true);
        q1.setSortOrder(1);
        questionsToInsert.add(q1);

        Question q2 = new Question();
        q2.setSurveyId(testSurvey.getId());
        q2.setContent("问题2的内容");
        q2.setType("RADIO");
        q2.setIsRequired(false);
        q2.setSortOrder(2);
        q2.setOptions("[\"选项A\", \"选项B\"]");
        questionsToInsert.add(q2);

        // --- 2. Act (执行阶段) ---
        // 调用批量插入方法
        questionMapper.batchInsert(questionsToInsert);

        // --- 3. Assert (断言阶段) ---
        // 使用问卷ID查询刚刚插入的问题
        List<Question> foundQuestions = questionMapper.findBySurveyId(testSurvey.getId());

        // 验证查询结果
        assertNotNull(foundQuestions, "根据问卷ID应该能找到问题列表");
        assertEquals(2, foundQuestions.size(), "查询出的问题数量应与插入时一致");

        // 进一步验证第一条问题的数据
        Question foundQ1 = foundQuestions.get(0);
        assertEquals("问题1的内容", foundQ1.getContent());
        assertEquals(1, foundQ1.getSortOrder());
        assertTrue(foundQ1.getIsRequired());
    }

}
