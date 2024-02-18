package com.club;

import cn.hutool.crypto.digest.BCrypt;
import com.club.entity.domain.User;
import com.club.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ClubSystemAdminApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setStudentId("admin");
        user.setName("admin");
        user.setUserInfo("超级管理员账号");
        user.setRole("admin");
        user.setStatus(0);
        user.setPassword(BCrypt.hashpw("123456"));
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        userService.save(user);
    }

}
