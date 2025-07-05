package com.example.survey.controller;

import com.example.survey.common.Result;
import com.example.survey.controller.dto.*;
import com.example.survey.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public Result<PageDto<AdminUserDto>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageDto<AdminUserDto> userPage = adminService.listUsers(page, size);
        return Result.success(userPage);
    }

    @PatchMapping("/users/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestBody UpdateUserStatusRequest request) {
        adminService.updateUserStatus(id, request.getStatus());

        return Result.success();
    }
    @GetMapping("/surveys")
    public Result<PageDto<AdminSurveyListItemDto>> getSurveys(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {

        PageDto<AdminSurveyListItemDto> surveyPage = adminService.listAllSurveys(page, size, title, username, status);
        return Result.success(surveyPage);
    }
    @DeleteMapping("/surveys/{id}")
    public Result<Void> deleteSurvey(@PathVariable Long id) {
        adminService.deleteSurveyByAdmin(id);
        return Result.success();
    }
    @GetMapping("/dashboard")
    public Result<AdminDashboardStatsDto> getDashboardStats() {
        AdminDashboardStatsDto stats = adminService.getDashboardStats();
        return Result.success(stats);
    }
}