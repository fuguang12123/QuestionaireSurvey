package com.example.survey.mapper;

import com.example.survey.entity.Survey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

/**
 * 问卷表的数据库操作接口。
 * 所有SQL实现均定义在 "resources/com/example/survey/mapper/SurveyMapper.xml" 中。
 */
@Mapper
public interface SurveyMapper {

    /**
     * 插入一份新问卷
     * @param survey 问卷对象
     * @return 影响的行数
     */
    int insert(Survey survey);

    /**
     * 只更新问卷的元数据
     * @param survey 包含更新后信息的问卷对象
     * @return 影响的行数
     */
    int updateMeta(Survey survey);

    /**
     * 根据ID查询问卷
     * @param id 问卷ID
     * @return 问卷对象
     */
    Survey findById(Long id);

    /**
     * 更新一份完整的问卷 (包括问题，通常由Service层编排)
     * @param survey 包含更新后信息的问卷对象
     * @return 影响的行数
     */
    int update(Survey survey);

    /**
     * 根据ID删除问卷
     * @param id 问卷ID
     * @return 影响的行数
     */
    int deleteById(Long id);

    /**
     * 管理员查询所有问卷（支持多条件组合查询）
     * @param title 问卷标题 (模糊查询)
     * @param username 创建者用户名 (精确查询)
     * @param status 问卷状态 (精确查询)
     * @return 问卷列表
     */
    List<Survey> findAllAdmin(@Param("title") String title, @Param("username") String username, @Param("status") Integer status);

    /**
     * 统计所有问卷的总数
     * @return 问卷总数
     */
    long countAll();

    /**
     * 查询所有已发布的公开问卷（支持多条件组合查询）
     * @param title 问卷标题 (模糊查询)
     * @param username 创建者用户名 (模糊查询)
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 问卷列表
     */
    List<Survey> findPublicSurveys(@Param("title") String title, @Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 根据创建者ID查询其所有的问卷列表（支持多条件组合查询）
     * @param creatorId 创建者ID
     * @param title 问卷标题
     * @param status 问卷状态
     * @param type 问卷类型
     * @return 问卷列表
     */
    List<Survey> findByCreatorId(@Param("creatorId") Long creatorId, @Param("title") String title, @Param("status") Integer status, @Param("type") String type);

    /**
     * 更新问卷的状态
     * @param id 问卷ID
     * @param status 新的状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}