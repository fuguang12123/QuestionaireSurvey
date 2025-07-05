package com.example.survey.exception;

/**
 * 自定义异常，用于表示请求过于频繁
 */
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}