package com.example.survey.controller;

import com.example.survey.controller.dto.LoginRequest;
import com.example.survey.controller.dto.LoginResponse;
import com.example.survey.controller.dto.RegisterRequest;
import com.example.survey.service.ServiceImpl.JwtUserDetailsService;
import com.example.survey.service.UserService;
import com.example.survey.common.Result;
import com.example.survey.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * 处理用户注册请求
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return Result.success();
    }

    /**
     * 处理用户登录请求
     * @param request 包含用户名和密码的登录请求
     * @return 包含JWT的响应
     * @throws Exception 如果认证失败
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            // 使用 AuthenticationManager 进行用户认证
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            // --- 关键修正：捕获更通用的 AuthenticationException ---
        } catch (AuthenticationException e) {
            // 无论是用户名不存在、密码错误、账户被锁定等认证失败，都统一返回此提示
            // 这样更安全，可以防止恶意用户通过返回信息的不同来猜测用户名是否存在
            return Result.error("用户名或密码错误");
        }

        // 如果认证成功，则生成JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return Result.success(new LoginResponse(token));
    }
}
