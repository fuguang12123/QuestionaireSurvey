package com.example.survey.config;

import com.example.survey.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义认证入口点
 * 当一个未认证的用户尝试访问受保护的资源时，这个类的方法会被触发。
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 设置HTTP响应状态为401 (未授权)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // 创建统一的错误响应Result对象
        Result<Void> errorResult = Result.error(HttpServletResponse.SC_UNAUTHORIZED, "用户未登录或Token已过期，请重新登录");

        // 使用ObjectMapper将Result对象转换为JSON字符串，并写入响应体
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResult));
    }
}
