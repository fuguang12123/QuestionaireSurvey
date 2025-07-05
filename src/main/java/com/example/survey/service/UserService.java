package com.example.survey.service;


import com.example.survey.controller.dto.UpdatePasswordRequest;
import com.example.survey.controller.dto.UpdateUserProfileRequest;
import com.example.survey.controller.dto.UpdateUsernameRequest;
import com.example.survey.entity.User;

public interface UserService {

    /**
     * 注册一个新用户
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     */
    void register(String username, String password, String email);
    User getCurrentUser();
    void updateCurrentUserProfile(UpdateUserProfileRequest request);
    void updateCurrentUserPassword(UpdatePasswordRequest request);
    void updateCurrentUsername(UpdateUsernameRequest request);
}