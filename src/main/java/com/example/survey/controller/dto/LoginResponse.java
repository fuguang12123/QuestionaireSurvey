package com.example.survey.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录响应的数据传输对象 (DTO)
 */
@Data
@AllArgsConstructor // Lombok 注解，生成一个包含所有参数的构造函数
public class LoginResponse {
    private String token;
}