package com.example.survey.controller.dto;

import lombok.Data;

/**
 * 用户修改密码的请求DTO
 */
@Data
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}