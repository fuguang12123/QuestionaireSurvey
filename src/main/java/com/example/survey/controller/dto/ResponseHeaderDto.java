package com.example.survey.controller.dto;

import com.example.survey.entity.ResponseHeader;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseHeaderDto {
    private Long id;
    private LocalDateTime submittedAt;
    private String ipAddress;
    private String submitterUsername; // 提交者用户名 (可能为 "Anonymous")

    public static ResponseHeaderDto fromEntity(ResponseHeader header) {
        ResponseHeaderDto dto = new ResponseHeaderDto();
        dto.setId(header.getId());
        dto.setSubmittedAt(header.getSubmittedAt());
        dto.setIpAddress(header.getIpAddress());
        // 这里的 submitterUsername 需要通过JOIN查询来填充
        // 我们将在Mapper中直接查询成DTO
        return dto;
    }
}