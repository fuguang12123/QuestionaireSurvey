package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.AdminDashboardStatsDto;
import com.example.survey.controller.dto.AdminSurveyListItemDto;
import com.example.survey.controller.dto.AdminUserDto;
import com.example.survey.controller.dto.PageDto;
import com.example.survey.entity.Survey;
import com.example.survey.entity.User;
import com.example.survey.exception.InvalidRequestException;
import com.example.survey.exception.ResourceNotFoundException;
import com.example.survey.mapper.ResponseHeaderMapper;
import com.example.survey.mapper.SurveyMapper;
import com.example.survey.mapper.UserMapper;
import com.example.survey.service.AdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private ResponseHeaderMapper responseHeaderMapper;

    @Override
    public PageDto<AdminUserDto> listUsers(int page, int size) {
        PageHelper.startPage(page, size);
        List<User> users = userMapper.findAll();
        System.out.println("Fetched users: " + size);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1Fetched users: " + users.size());
        // 2. 执行常规的查询

        // 3. 用PageInfo对象包装查询结果，以获取总数等分页信息
        PageInfo<User> pageInfo = new PageInfo<>(users);
        List<AdminUserDto> userDtos = pageInfo.getList().stream()
                .map(AdminUserDto::fromUser)
                .collect(Collectors.toList());
        return new PageDto<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), userDtos);
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        // 1. 校验传入的status值是否有效
        if (status == null || (status != 0 && status != 1)) {
            throw new InvalidRequestException("无效的状态值");
        }

        // 2. 校验要修改的用户是否存在
        User userToUpdate = userMapper.findById(userId);
        if (userToUpdate == null) {
            throw new ResourceNotFoundException("ID为 " + userId + " 的用户不存在");
        }

        // 3. 校验管理员不能修改自己的状态

        // 4. 执行更新并检查结果
        int affectedRows = userMapper.updateStatus(userId, status);
        if (affectedRows == 0) {
            // 正常情况下，如果用户存在，这里不应该执行到
            throw new RuntimeException("更新用户状态失败，没有记录被修改。");
        }
    }
    @Override
    public PageDto<AdminSurveyListItemDto> listAllSurveys(int page, int size, String title, String username, Integer status) {
        PageHelper.startPage(page, size);
        List<Survey> surveys = surveyMapper.findAllAdmin(title, username, status);
        PageInfo<Survey> pageInfo = new PageInfo<>(surveys);

        List<AdminSurveyListItemDto> surveyDtos = pageInfo.getList().stream()
                .map(AdminSurveyListItemDto::fromSurvey)
                .collect(Collectors.toList());

        return new PageDto<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), surveyDtos);
    }

    @Override
    public void deleteSurveyByAdmin(Long surveyId) {
        // 1. 验证问卷是否存在
        Survey survey = surveyMapper.findById(surveyId);
        if (survey == null) {
            throw new ResourceNotFoundException("无法删除：ID为 " + surveyId + " 的问卷不存在");
        }

        // 2. 直接调用Mapper执行删除操作
        // 因为数据库外键设置了级联删除，相关的question记录也会被一并删除
        surveyMapper.deleteById(surveyId);
    }
    @Override
    public AdminDashboardStatsDto getDashboardStats() {
        long totalUsers = userMapper.countAll();
        long totalSurveys = surveyMapper.countAll();
        long totalResponses = responseHeaderMapper.countAll();

        return new AdminDashboardStatsDto(totalUsers, totalSurveys, totalResponses);
    }
}