package com.example.survey.controller;

import com.example.survey.controller.dto.UpdatePasswordRequest;
import com.example.survey.controller.dto.UpdateUserProfileRequest;
import com.example.survey.controller.dto.UpdateUsernameRequest;
import com.example.survey.controller.dto.UserProfileResponse;
import com.example.survey.service.UserService;

import com.example.survey.common.Result;
import com.example.survey.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    // 返回类型修改为 Result<UserProfileResponse>
    public Result<UserProfileResponse> getCurrentUserProfile() {
        User currentUser = userService.getCurrentUser();
        // 使用 Result.success(data) 返回
        return Result.success(UserProfileResponse.fromUser(currentUser));
    }

    @PutMapping("/me")
    public Result<Void> updateProfile(@RequestBody UpdateUserProfileRequest request) {
        userService.updateCurrentUserProfile(request);
        return Result.success();
    }

    @PutMapping("/me/password")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordRequest request) {
        userService.updateCurrentUserPassword(request);
        return Result.success();
    }

    @PutMapping("/me/username")
    public Result<Void> updateUsername(@RequestBody UpdateUsernameRequest request) {
        userService.updateCurrentUsername(request);
        return Result.success();
    }
}