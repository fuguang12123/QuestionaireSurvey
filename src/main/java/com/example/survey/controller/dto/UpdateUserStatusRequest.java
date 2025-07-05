package com.example.survey.controller.dto;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    /**
     * 新的状态 (0-正常, 1-受限)
     */
    private Integer status;
}