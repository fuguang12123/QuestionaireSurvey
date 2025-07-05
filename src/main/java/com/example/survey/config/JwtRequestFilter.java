package com.example.survey.config;

import com.example.survey.service.ServiceImpl.JwtUserDetailsService;
import com.example.survey.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // 使用标准的日志框架
    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        log.info("--- [JWT调试] 开始处理新请求: {} ---", request.getRequestURI());

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            log.info("[JWT调试] 从请求头中提取到Token: {}", jwtToken);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                log.info("[JWT调试] 从Token中成功解析出用户名: '{}'", username);
            } catch (IllegalArgumentException e) {
                log.error("[JWT调试] 无法获取JWT, 参数错误", e);
            } catch (ExpiredJwtException e) {
                log.error("[JWT调试] JWT已过期", e);
            } catch (Exception e) {
                log.error("[JWT调试] 解析JWT时发生未知错误", e);
            }
        } else {
            log.warn("[JWT调试] 请求头中缺少 'Bearer ' 前缀的Authorization信息");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("[JWT调试] SecurityContext为空, 开始为用户 '{}' 进行Token验证", username);
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                log.info("<<<<< [JWT调试] Token验证成功! 准备设置安全上下文。 >>>>>");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                log.error("<<<<< [JWT调试] Token验证失败! >>>>>");
            }
        }

        chain.doFilter(request, response);
        log.info("--- [JWT调试] 请求处理结束: {} ---", request.getRequestURI());
    }
}