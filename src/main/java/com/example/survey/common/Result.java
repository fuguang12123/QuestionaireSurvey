package com.example.survey.common;

import lombok.Data;

/**
 * 通用的API响应结果封装类
 * @param <T> 响应数据的类型
 */
@Data
public class Result<T> {
    /**
     * 状态码 (例如: 200代表成功, 500代表服务器错误)
     */
    private Integer code;

    /**
     * 响应消息 (例如: "操作成功", "用户名已存在")
     */
    private String msg;

    /**
     * 响应的业务数据
     */
    private T data;

    // 私有构造函数，强制使用静态工厂方法创建实例
    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 创建一个表示成功的响应结果（包含数据）
     * @param data 响应数据
     * @param <T>  数据的泛型
     * @return Result 实例
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 创建一个表示成功的响应结果（不包含数据）
     * @return Result 实例
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 创建一个表示失败的响应结果
     * @param code 错误码
     * @param msg  错误消息
     * @param <T>  泛型
     * @return Result 实例
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 创建一个表示失败的响应结果（使用默认错误码500）    
     * @param msg 错误消息
     * @param <T> 泛型
     * @return Result 实例
     */
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }
}
