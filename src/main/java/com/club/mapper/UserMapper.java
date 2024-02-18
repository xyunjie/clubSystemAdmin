package com.club.mapper;

import com.club.entity.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @description 针对表【t_user(用户表)】的数据库操作Mapper
* @createDate 2024-02-07 18:31:00
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




