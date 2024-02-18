package com.club.controller;

import com.club.common.result.Result;
import com.club.config.AllowAnonymous;
import com.club.entity.dto.user.ChangePasswordDto;
import com.club.entity.dto.user.UserLoginDto;
import com.club.entity.dto.user.UserAddOrUpdateDto;
import com.club.entity.vo.UserVo;
import com.club.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jzx
 * @date 2024年02月07日
 */
@RestController
@RequestMapping("/api/account")
@Tag(name = "账户接口", description = "账户相关接口")
public class AccountController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @AllowAnonymous
    public Result<String> test(@RequestBody @Validated UserLoginDto userLoginDto) {
        String token = userService.login(userLoginDto);
        return Result.ok(token);
    }

    @PostMapping("logout")
    @Operation(summary = "用户登出")
    public Result<String> logout() {
        userService.logout(getUserId());
        return Result.ok();
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<UserVo> getUserInfo() {
        UserVo userInfo = userService.getUserInfo(getUserId());
        return Result.ok(userInfo);
    }

    @PostMapping("register")
    @Operation(summary = "用户注册")
    @AllowAnonymous
    public Result<String> register(@RequestBody @Validated UserAddOrUpdateDto userAddOrUpdateDto) {
        userService.registerUser(userAddOrUpdateDto);
        return Result.ok();
    }

    @PostMapping("update")
    @Operation(summary = "更新用户信息")
    public Result<String> updateUserInfo(@RequestBody @Validated UserAddOrUpdateDto userAddOrUpdateDto) {
        userService.updateUserInfo(getUserId(), userAddOrUpdateDto);
        return Result.ok();
    }

    @PostMapping("changePassword")
    @Operation(summary = "修改密码")
    public Result<String> changePassword(@RequestBody @Validated ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto, getUserId());
        return Result.ok();
    }

}
