package com.example.survey.controller.dto;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String email;
    private String avatar;
}