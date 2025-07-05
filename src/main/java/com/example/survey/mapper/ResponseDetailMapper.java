    package com.example.survey.mapper;

    import com.example.survey.controller.dto.RawAnswerDto;
    import com.example.survey.entity.ResponseDetail;
    import org.apache.ibatis.annotations.Mapper;
    import org.apache.ibatis.annotations.Param;
    import java.util.List;


    /**
     * 回答详情的数据库操作接口。
     * 所有SQL实现均定义在 "resources/com/example/survey/mapper/ResponseDetailMapper.xml" 中。
     */
    @Mapper
    public interface ResponseDetailMapper {

        /**
         * 批量插入多个回答详情。
         * @param details 包含多个回答详情的列表容器
         */
        void batchInsert(@Param("details") List<ResponseDetail> details);

        /**
         * 根据问卷ID查询所有相关的回答详情。
         * @param surveyId 问卷ID
         * @return 属于该问卷的所有回答详情列表
         */
        List<ResponseDetail> findBySurveyId(Long surveyId);

        /**
         * 分页查询指定问题的原始答案。
         * 这个方法会连接用户表，直接返回一个包含用户名等信息的DTO列表。
         * @param questionId 问题ID
         * @return 原始答案DTO列表
         */
        List<RawAnswerDto> findRawAnswersByQuestionId(@Param("questionId") Long questionId);

        /**
         * 根据单个答卷的ID，查询其包含的所有回答详情。
         * @param responseId 答卷头ID (response_header.id)
         * @return 该份答卷的所有回答详情列表
         */
        List<ResponseDetail> findByResponseId(@Param("responseId") Long responseId);

        /**
         * 根据问题ID删除所有相关的回答详情。
         * 主要用于编辑问卷并修改了某个问题时，清除该问题的旧答案。
         * @param questionId 问题ID
         * @return 影响的行数
         */
        int deleteByQuestionId(@Param("questionId") Long questionId);
    }