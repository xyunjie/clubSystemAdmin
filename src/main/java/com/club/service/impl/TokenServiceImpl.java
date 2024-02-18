package com.club.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.entity.domain.Token;
import com.club.mapper.TokenMapper;
import com.club.service.TokenService;
import org.springframework.stereotype.Service;

/**
* @description 针对表【t_token(token表)】的数据库操作Service实现
* @createDate 2024-02-07 18:31:00
*/
@Service
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token>
    implements TokenService{

}




