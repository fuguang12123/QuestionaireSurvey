package com.example.survey.service;

import com.example.survey.controller.dto.AdminDashboardStatsDto;
import com.example.survey.controller.dto.AdminSurveyListItemDto;
import com.example.survey.controller.dto.AdminUserDto;
import com.example.survey.controller.dto.PageDto;

public interface AdminService {
    /**
     * 分页获取用户列表
     * @param page 页码 (从1开始)
     * @param size 每页数量
     * @return 封装好的分页数据
     */
    PageDto<AdminUserDto> listUsers(int page, int size);
    void updateUserStatus(Long userId, Integer status);
    PageDto<AdminSurveyListItemDto> listAllSurveys(int page, int size, String title, String username, Integer status);
    void deleteSurveyByAdmin(Long surveyId);
    AdminDashboardStatsDto getDashboardStats();
}