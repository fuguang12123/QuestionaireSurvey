package com.example.survey.controller.dto;

import com.example.survey.entity.User;
import lombok.Data;

@Data
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String role;

    // 一个方便的静态工厂方法，用于从User实体创建DTO
    public static UserProfileResponse fromUser(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        return response;
    }
}
