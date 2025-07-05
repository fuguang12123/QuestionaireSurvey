package com.example.survey.mapper;

import com.example.survey.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 问题实体的数据库操作接口。
 * 所有SQL实现均定义在 "resources/com/example/survey/mapper/QuestionMapper.xml" 中。
 */
@Mapper
public interface QuestionMapper {

    /**
     * 插入单个问题。
     * @param question 问题实体对象
     * @return 影响的行数
     */
    int insert(Question question);

    /**
     * 批量插入多个问题。
     * @param questions 包含多个问题的列表容器
     */
    void batchInsert(@Param("questions") List<Question> questions);

    /**
     * 更新问题信息。
     * @param question 问题实体对象（需包含ID）
     * @return 影响的行数
     */
    int update(Question question);

    /**
     * 根据问题ID删除问题。
     * @param id 问题ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 查询指定问卷下的所有问题。
     * @param surveyId 问卷ID
     * @return 属于该问卷的问题列表
     */
    List<Question> findBySurveyId(Long surveyId);

    /**
     * 根据问卷ID删除所有关联问题。
     * 主要用于删除问卷时级联清理问题。
     * @param surveyId 问卷ID
     * @return 影响的行数
     */
    int deleteBySurveyId(Long surveyId);
}