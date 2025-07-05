package com.example.survey.controller.dto;

import com.example.survey.entity.User;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 在管理员用户列表中，用于表示单个用户的DTO
 */
@Data
public class AdminUserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Integer status;
    private LocalDateTime createdAt;

    public static AdminUserDto fromUser(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
