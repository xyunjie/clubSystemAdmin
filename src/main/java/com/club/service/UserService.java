package com.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.domain.Dict;
import com.club.entity.domain.User;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.user.*;
import com.club.entity.vo.UserVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @description 针对表【t_user(用户表)】的数据库操作Service
 * @createDate 2024-02-07 18:31:00
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userLoginDto 用户登录信息
     */
    String login(UserLoginDto userLoginDto);

    /**
     * 用户登出
     *
     * @param userId
     */
    void logout(Long userId);

    /**
     * 获取当前登录用户信息/根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    UserVo getUserInfo(Long userId);

    /**
     * 用户注册
     *
     * @param userAddOrUpdateDto
     */
    void registerUser(UserAddOrUpdateDto userAddOrUpdateDto);

    /**
     * 更新用户信息
     *
     * @param userId
     * @param userAddOrUpdateDto
     */
    void updateUserInfo(Long userId, UserAddOrUpdateDto userAddOrUpdateDto);

    /**
     * 修改密码
     *
     * @param changePasswordDto
     * @param id
     */
    void changePassword(ChangePasswordDto changePasswordDto, Long id);

    /**
     * 获取用户列表
     *
     * @param userListQueryDto
     * @return
     */
    Page<UserVo> getUserList(UserListQueryDto userListQueryDto);

    /**
     * 修改用户状态
     *
     * @param modifyStatusDto
     * @param userId
     */
    void modifyUserStatus(ModifyStatusDto modifyStatusDto, Long userId);

    /**
     * 删除用户
     *
     * @param id
     * @param userId
     */
    void removeUser(Long id, Long userId);

    /**
     * 导出用户列表
     *
     * @param userListQueryDto
     * @param response
     */
    void exportUser(UserListQueryDto userListQueryDto, HttpServletResponse response);

    /**
     * 修改用户角色
     *
     * @param userModifyRoleDto
     * @param userId
     */
    void modifyUserRole(UserModifyRoleDto userModifyRoleDto, Long userId);

    /**
     * 获取用户信息
     *
     * @param user
     * @param dicts
     * @return
     */
    UserVo parseUserToUserVo(User user, List<Dict> dicts);
}
