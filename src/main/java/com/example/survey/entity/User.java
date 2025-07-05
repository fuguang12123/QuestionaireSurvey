package com.example.survey.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类，对应数据库中的 'user' 表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 加密密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 角色 (USER, ADMIN)
     */
    private String role;

    /**
     * 状态 (0-正常, 1-受限)
     */
    private Integer status;

    /**
     * 注册时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}