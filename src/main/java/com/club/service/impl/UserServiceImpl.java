package com.club.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.Dict;
import com.club.entity.domain.Token;
import com.club.entity.domain.User;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.user.*;
import com.club.entity.enums.UserRoleEnum;
import com.club.entity.enums.UserStatusEnum;
import com.club.entity.vo.UserVo;
import com.club.mapper.UserMapper;
import com.club.service.DictService;
import com.club.service.TokenService;
import com.club.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2024-02-07 18:31:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private TokenService tokenService;

    @Resource
    private DictService dictService;

    @Override
    public String login(UserLoginDto userLoginDto) {
        User user = this.lambdaQuery().eq(User::getStudentId, userLoginDto.getAccount()).one();
        if (user == null) {
            throw new GlobalException("用户名或密码错误！");
        }
        if (!BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword())) {
            throw new GlobalException("用户名或密码错误！");
        }
        if (user.getStatus() < 0) {
            throw new GlobalException("账号正在审核中，请耐心等待！");
        }
        if (user.getStatus() != 0) {
            // 禁用
            throw new GlobalException("账号已被禁用，请联系管理员！");
        }
        // 登录成功
        // 获取当前数据库中的token
        Token userToken = tokenService.lambdaQuery().eq(Token::getUserId, user.getId()).one();
        if (userToken != null) {
            if (userToken.getExpireTime().isAfter(LocalDateTime.now())) {
                return userToken.getToken();
            } else {
                // token 过期了
                userToken.setToken(UUID.randomUUID().toString());
                userToken.setExpireTime(LocalDateTime.now().plusDays(7));
            }
        } else {
            userToken = new Token();
            userToken.setUserId(user.getId());
            userToken.setToken(UUID.randomUUID().toString());
            userToken.setExpireTime(LocalDateTime.now().plusDays(7));
        }
        tokenService.saveOrUpdate(userToken);
        return userToken.getToken();
    }

    @Override
    public void logout(Long userId) {
        Token token = tokenService.lambdaQuery().eq(Token::getUserId, userId).one();
        if (token != null) {
            token.setExpireTime(LocalDateTime.now());
            tokenService.updateById(token);
        }
    }

    @Override
    public UserVo getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new GlobalException("登录认证失败，用户不存在");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public void registerUser(UserAddOrUpdateDto userAddOrUpdateDto) {
        User user = new User();
        // 查询是否存在相同学号
        User one = lambdaQuery().eq(User::getStudentId, userAddOrUpdateDto.getStudentId()).one();
        if (one != null) {
            throw new GlobalException("注册账号已存在！");
        }
        BeanUtils.copyProperties(userAddOrUpdateDto, user, "id");
        user.setPassword(BCrypt.hashpw(userAddOrUpdateDto.getPassword()));
        user.setStatus(UserStatusEnum.WAIT_AUDIT.getValue());
        // 保存
        boolean save = save(user);
        if (!save) {
            throw new GlobalException("注册失败！");
        }
    }

    @Override
    public void updateUserInfo(Long userId, UserAddOrUpdateDto userAddOrUpdateDto) {
        User user = getById(userAddOrUpdateDto.getId());
        if (user == null) {
            throw new GlobalException("用户不存在！");
        }
        BeanUtils.copyProperties(userAddOrUpdateDto, user, "password");
        boolean update = updateById(user);
        if (!update) {
            throw new GlobalException("更新失败！");
        }
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto, Long id) {
        // 获取当前登录的用户
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new GlobalException("两次输入的密码不一致！");
        }
        User user = getById(id);
        if (user == null) {
            throw new GlobalException("用户不存在！");
        }
        // 验证旧密码是否正确
        if (!BCrypt.checkpw(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new GlobalException("密码错误！");
        }

        // 更新密码
        boolean update = lambdaUpdate().eq(User::getId, user.getId())
                .set(User::getPassword, BCrypt.hashpw(changePasswordDto.getNewPassword()))
                .update();
        if (!update) {
            throw new GlobalException("更新失败！");
        }
    }

    @Override
    public Page<UserVo> getUserList(UserListQueryDto userListQueryDto) {
        // 获取用户列表
        Page<User> page = new Page<>(userListQueryDto.getPageNumber(), userListQueryDto.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(userListQueryDto.getDict() != null && !userListQueryDto.getDict().isEmpty(), User::getCollege, userListQueryDto.getDict()).or();
        queryWrapper.in(userListQueryDto.getDict() != null && !userListQueryDto.getDict().isEmpty(), User::getMajor, userListQueryDto.getDict()).or();
        queryWrapper.in(userListQueryDto.getDict() != null && !userListQueryDto.getDict().isEmpty(), User::getClazz, userListQueryDto.getDict()).or();
        queryWrapper.in(userListQueryDto.getDict() != null && !userListQueryDto.getDict().isEmpty(), User::getGrade, userListQueryDto.getDict());
        queryWrapper.eq(StringUtils.isNotEmpty(userListQueryDto.getRole()), User::getRole, userListQueryDto.getRole());
        queryWrapper.like(StringUtils.isNotEmpty(userListQueryDto.getQuery()), User::getName, userListQueryDto.getQuery());
        this.page(page, queryWrapper);
        Page<UserVo> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage, "records");
        List<UserVo> userVos = parseUserListToUserVoList(page.getRecords());
        resultPage.setRecords(userVos);
        return resultPage;
    }

    @Override
    public List<UserVo> getUserListByIds(List<Long> ids) {
        List<User> users = this.listByIds(ids);
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        return parseUserListToUserVoList(users);
    }

    @Override
    public void modifyUserStatus(ModifyStatusDto modifyStatusDto, Long userId) {
        UserStatusEnum status = UserStatusEnum.getEnum(modifyStatusDto.getStatus());
        if (status == null) {
            throw new GlobalException("状态不合法");
        }
        // 不允许修改自己的
        if (userId.equals(modifyStatusDto.getId())) {
            throw new GlobalException("不允许修改自己的状态");
        }
        // 不允许普通管理员修改超级管理员的
        User loginUser = this.getById(userId);
        User modifyUser = this.getById(modifyStatusDto.getId());
        if ("admin".equals(modifyUser.getStudentId())) {
            // 系统内置超级管理
            throw new GlobalException("无法修改超级管理员权限！");
        }
        if (modifyUser.getRole().equals(UserRoleEnum.ADMIN.getValue()) && !loginUser.getRole().equals(UserRoleEnum.ADMIN.getValue())) {
            throw new GlobalException("你无权修改超级管理员的状态！");
        }
        boolean update = lambdaUpdate().eq(User::getId, modifyStatusDto.getId()).set(User::getStatus, status.getValue()).update();
        if (!update) {
            throw new GlobalException("修改失败！");
        }
    }

    @Override
    public void removeUser(Long id, Long userId) {
        // 不允许修改自己的
        if (userId.equals(id)) {
            throw new GlobalException("无法删除自己！");
        }
        // 不允许普通管理员修改超级管理员的
        User loginUser = this.getById(userId);
        User removeUser = this.getById(id);
        if ("admin".equals(removeUser.getStudentId())) {
            throw new GlobalException("无法删除超级管理员！");
        }
        if (removeUser.getRole().equals(UserRoleEnum.ADMIN.getValue()) && !loginUser.getRole().equals(UserRoleEnum.ADMIN.getValue())) {
            throw new GlobalException("你无权删除该用户！");
        }
        boolean remove = this.removeById(id);
        if (!remove) {
            throw new GlobalException("删除失败！");
        }
    }

    @Override
    public void exportUser(UserListQueryDto userListQueryDto, HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(LocalDateTime.now() + "导出用户", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
        userListQueryDto.setPageSize(Integer.MAX_VALUE);
        Page<UserVo> userList = this.getUserList(userListQueryDto);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, UserVo.class).sheet("user").doWrite(userList.getRecords());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new GlobalException(e.getMessage(), 400);
        }
    }

    @Override
    public void modifyUserRole(UserModifyRoleDto userModifyRoleDto, Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new GlobalException("用户不存在！");
        }
        boolean update = lambdaUpdate().eq(User::getId, userModifyRoleDto.getId()).set(User::getRole, userModifyRoleDto.getRole()).update();
        if (!update) {
            throw new GlobalException("修改失败！");
        }
    }

    public List<UserVo> parseUserListToUserVoList(List<User> users) {
        List<Long> dictIds = new ArrayList<>();
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        users.forEach(item -> {
            dictIds.add(item.getCollege());
            dictIds.add(item.getMajor());
            dictIds.add(item.getClazz());
            dictIds.add(item.getGrade());
        });
        // 过滤
        List<Long> dictIdsFinal = dictIds.stream().filter(Objects::nonNull).distinct().toList();
        List<Dict> dicts = dictService.listByIds(dictIdsFinal);
        return users.stream().map(item -> this.parseUserToUserVo(item, dicts)).toList();
     }

    @Override
    public UserVo parseUserToUserVo(User user, List<Dict> dicts) {
        UserVo userVo = new UserVo();
        Map<Long, String> collect = dicts.stream().collect(Collectors.toMap(Dict::getId, Dict::getName));
        BeanUtils.copyProperties(user, userVo);
        if (dicts.isEmpty()) {
            return userVo;
        }
        if (user.getCollege() != null) {
            userVo.setCollegeName(collect.get(user.getCollege()));
            userVo.setUnitInfo(collect.get(user.getCollege()) + "/");
        } else {
            userVo.setUnitInfo("");
        }
        if (user.getMajor() != null) {
            userVo.setMajorName(collect.get(user.getMajor()));
            userVo.setUnitInfo(userVo.getUnitInfo() + collect.get(user.getMajor()) + "/");
        }
        if (user.getClazz() != null) {
            userVo.setClazzName(collect.get(user.getClazz()));
            userVo.setUnitInfo(userVo.getUnitInfo() + collect.get(user.getClazz()) + "/");
        }
        if (user.getGrade() != null) {
            userVo.setGradeName(collect.get(user.getGrade()));
        }
        // 去尾
        if (StringUtils.isNotEmpty(userVo.getUnitInfo())) {
            userVo.setUnitInfo(userVo.getUnitInfo().substring(0, userVo.getUnitInfo().length() - 1));
        }

        if (userVo.getSex()) {
            userVo.setSexName("男");
        } else {
            userVo.setSexName("女");
        }
        return userVo;
    }
}




