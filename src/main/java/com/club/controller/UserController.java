package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.dto.user.UserListQueryDto;
import com.club.entity.dto.user.UserModifyRoleDto;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.vo.UserVo;
import com.club.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @date 2024年02月10日
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户接口", description = "用户相关接口")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("/list")
    @Operation(summary = "获取用户列表")
    public Result<Page<UserVo>> list(@RequestBody @Validated UserListQueryDto userListQueryDto) {
        Page<UserVo> res = userService.getUserList(userListQueryDto);
        return Result.ok(res);
    }

    @PostMapping("status")
    @Operation(summary = "修改用户状态")
    public Result<String> status(@RequestBody @Validated ModifyStatusDto modifyStatusDto) {
        userService.modifyUserStatus(modifyStatusDto, getUserId());
        return Result.ok();
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除用户")
    public Result<String> remove(@RequestParam Long id) {
        userService.removeUser(id, getUserId());
        return Result.ok();
    }

    @PostMapping("export")
    @Operation(summary = "导出用户")
    public void export(@RequestBody @Validated UserListQueryDto userListQueryDto, HttpServletResponse response) {
        userService.exportUser(userListQueryDto, response);
    }

    @PostMapping("modifyRole")
    @Operation(summary = "修改用户角色")
    public Result<String> modifyRole(@RequestBody @Validated UserModifyRoleDto userModifyRoleDto) {
        userService.modifyUserRole(userModifyRoleDto, getUserId());
        return Result.ok();
    }

}
