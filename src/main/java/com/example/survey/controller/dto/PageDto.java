package com.example.survey.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用的分页响应DTO
 * @param <T> 列表项的数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页的数据列表
     */
    private List<T> items;
}
