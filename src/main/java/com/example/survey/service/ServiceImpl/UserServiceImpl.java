package com.example.survey.service.ServiceImpl;

import com.example.survey.controller.dto.UpdatePasswordRequest;
import com.example.survey.controller.dto.UpdateUserProfileRequest;
import com.example.survey.controller.dto.UpdateUsernameRequest;
import com.example.survey.exception.InvalidRequestException;
import com.example.survey.service.UserService;
import com.example.survey.mapper.UserMapper;
import com.example.survey.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 声明这是一个Spring的服务类
    public class UserServiceImpl implements UserService {

        @Autowired // 自动注入UserMapper实例
        private UserMapper userMapper;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public void register(String username, String password, String email) {
            // 1. 检查用户名是否已存在
            if (userMapper.findByUsername(username) != null) {
                // 在实际应用中，这里应该抛出一个自定义的异常
                throw new RuntimeException("用户名已存在");
            }

            // 2. 对密码进行加密
            String encodedPassword = passwordEncoder.encode(password);

            // 3. 创建User对象并设置属性
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setRole("USER"); // 默认角色为普通用户
            user.setStatus(0); // 默认状态为正常

            // 4. 调用Mapper将用户数据插入数据库
            userMapper.insert(user);
        }

        @Override
        public User getCurrentUser() {
            // 从Security上下文中获取当前认证的principal (通常是UserDetails)
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            // 使用用户名从数据库中查找完整的User对象
            return userMapper.findByUsername(username);
        }
    @Override
    @Transactional // 建议为所有修改操作添加事务注解
    public void updateCurrentUserProfile(UpdateUserProfileRequest request) {
        // 1. 获取当前登录的用户，确保用户只能修改自己的信息
        User currentUser = getCurrentUser();

        // 2. 更新对象的字段
        currentUser.setEmail(request.getEmail());
        currentUser.setAvatar(request.getAvatar());

        // 3. 调用Mapper执行更新，并捕获返回值
        int affectedRows = userMapper.updateProfile(currentUser);

        // 4. 检查更新是否真正生效
        if (affectedRows == 0) {
            // 如果影响行数为0，说明更新失败，可能是ID不匹配等原因
            throw new RuntimeException("更新用户信息失败，没有记录被修改。");
        }
    }

    @Override
    @Transactional
    public void updateCurrentUserPassword(UpdatePasswordRequest request) {
        User currentUser = getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new InvalidRequestException("旧密码不正确");
        }
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        int affectedRows = userMapper.updatePassword(currentUser.getId(), encodedNewPassword);
        if (affectedRows == 0) {
            throw new RuntimeException("更新密码失败。");
        }
    }

    @Override
    @Transactional
    public void updateCurrentUsername(UpdateUsernameRequest request) {
        User currentUser = getCurrentUser();
        if (!passwordEncoder.matches(request.getPassword(), currentUser.getPassword())) {
            throw new InvalidRequestException("密码不正确");
        }
        if (userMapper.findByUsername(request.getNewUsername()) != null) {
            throw new InvalidRequestException("此用户名已被占用");
        }
        int affectedRows = userMapper.updateUsername(currentUser.getId(), request.getNewUsername());
        if (affectedRows == 0) {
            throw new RuntimeException("更新用户名失败。");
        }
    }
 }
