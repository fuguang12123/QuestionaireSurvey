package com.example.survey.mapper;

import com.example.survey.controller.dto.ResponseHeaderDto;
import com.example.survey.entity.ResponseHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 答卷头的数据库操作接口。
 * 所有SQL实现均定义在 "resources/com/example/survey/mapper/ResponseHeaderMapper.xml" 中。
 */
@Mapper
public interface ResponseHeaderMapper {

    int insert(ResponseHeader responseHeader);

    /**
     * 根据问卷ID统计总提交数
     * @param surveyId 问卷ID
     * @return 总提交数
     */
    long countBySurveyId(Long surveyId);

    /**
     * 根据问卷ID查询所有的答卷头信息
     * @param surveyId 问卷ID
     * @return 答卷头列表
     */
    List<ResponseHeader> findBySurveyId(Long surveyId);

    /**
     * 统计所有答卷的总数
     * @return 答卷总数
     */
    long countAll();

    /**
     * 分页查询指定问卷的答卷头信息
     * @param surveyId 问卷ID
     * @return 答卷头DTO列表
     */
    List<ResponseHeaderDto> findHeadersBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * 根据问卷ID删除所有相关的答卷头
     * @param surveyId 问卷ID
     */
    void deleteBySurveyId(Long surveyId);
}
