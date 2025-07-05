package com.example.survey.controller.dto;

import lombok.Data;

/**
 * 用户修改用户名的请求DTO
 */
@Data
public class UpdateUsernameRequest {
    private String newUsername;
    private String password; // 需要验证当前密码以确保安全
}
