package com.example.survey.common;

import com.example.survey.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 它会捕获所有Controller抛出的异常，并返回统一的错误格式
 */
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        /**
         * 专门处理“资源未找到”的异常
         * 当Service层抛出ResourceNotFoundException时，此方法被调用。
         * @param e 捕获到的异常对象
         * @return 包含404状态码和错误信息的Result
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND) // 确保HTTP响应的状态码是 404
        public Result<Void> handleResourceNotFoundException(ResourceNotFoundException e) {
            return Result.error(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

        /**
         * 专门处理“权限不足”的异常
         * 当Service层抛出ForbiddenException时，此方法被调用。
         * @param e 捕获到的异常对象
         * @return 包含403状态码和错误信息的Result
         */
        @ExceptionHandler(ForbiddenException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN) // 确保HTTP响应的状态码是 403
        public Result<Void> handleForbiddenException(ForbiddenException e) {
            return Result.error(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }

        /**
         * 捕获所有其他未被处理的运行时异常，作为最后的“安全网”
         * @param e 捕获到的异常对象
         * @return 包含500状态码和错误信息的Result
         */
        @ExceptionHandler(RuntimeException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 确保HTTP响应的状态码是 500
        public Result<Void> handleRuntimeException(RuntimeException e) {
            // 在控制台打印完整的错误堆栈，方便调试
            e.printStackTrace();
            // 向前端返回一个通用的服务器内部错误信息
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系管理员");
        }

    /**
     * 新增：专门处理“请求过于频繁”的异常
     * @param e 捕获到的异常对象
     * @return 包含429状态码和错误信息的Result
     */
    @ExceptionHandler(TooManyRequestsException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS) // 确保HTTP响应的状态码是 429
    public Result<Void> handleTooManyRequestsException(TooManyRequestsException e) {
        return Result.error(HttpStatus.TOO_MANY_REQUESTS.value(), e.getMessage());
    }

    @ExceptionHandler(InvalidSubmissionDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP响应的状态码是 400 Bad Request
    public Result<Void> handleInvalidSubmissionDataException(InvalidSubmissionDataException e) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 确保HTTP响应的状态码是 401 Unauthorized
    public Result<Void> handleBadCredentialsException(BadCredentialsException e) {
        return Result.error(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
    }
    @ExceptionHandler(InvalidRequestException.class)
 @ResponseStatus(HttpStatus.BAD_REQUEST)
 public Result<Void> handleInvalidRequestException(InvalidRequestException e) {
     return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
 }

}
