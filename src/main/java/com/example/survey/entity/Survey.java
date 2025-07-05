package com.example.survey.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问卷实体类
 */
@Data
public class Survey {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private String type; // PUBLIC, PRIVATE
    private String accessCode;
    private Integer status; // 0-草稿, 1-发布, 2-结束, 3-删除
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String settings; // JSON
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User creator;
}