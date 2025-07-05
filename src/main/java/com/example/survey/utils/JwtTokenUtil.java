package com.example.survey.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ... (其他方法保持不变) ...

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        final Date now = new Date();
        log.info("[JWT调试] - 检查是否过期 -> 过期时间: {}, 当前时间: {}", expirationDate, now);
        return expirationDate.before(now);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // --- validateToken 方法已添加详细日志 ---
    public Boolean validateToken(String token, UserDetails userDetails) {
        log.info("--- [JWT调试] 开始执行核心验证逻辑 ---");
        try {
            final String usernameFromToken = getUsernameFromToken(token);
            final String usernameFromUserDetails = userDetails.getUsername();
            log.info("[JWT调试] - 用户名对比 -> Token中的用户名: '{}', 数据库中的用户名: '{}'", usernameFromToken, usernameFromUserDetails);

            boolean isUsernameMatch = usernameFromToken.equals(usernameFromUserDetails);
            log.info("[JWT调试] - 用户名是否匹配? {}", isUsernameMatch);

            boolean isExpired = isTokenExpired(token);
            log.info("[JWT调试] - Token是否已过期? {}", isExpired);

            if (!isUsernameMatch) {
                log.error("[JWT调试] 验证失败原因: 用户名不匹配!");
            }
            if (isExpired) {
                log.error("[JWT调试] 验证失败原因: Token已过期!");
            }

            return (isUsernameMatch && !isExpired);
        } catch (Exception e) {
            log.error("[JWT调试] Token验证过程中出现异常: ", e);
            return false;
        } finally {
            log.info("--- [JWT调试] 核心验证逻辑结束 ---");
        }
    }
}