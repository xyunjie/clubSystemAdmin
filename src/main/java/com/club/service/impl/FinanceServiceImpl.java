package com.club.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.entity.domain.Finance;
import com.club.mapper.FinanceMapper;
import com.club.service.FinanceService;
import org.springframework.stereotype.Service;

/**
* @description 针对表【t_finance(财务表)】的数据库操作Service实现
* @createDate 2024-02-07 18:31:00
*/
@Service
public class FinanceServiceImpl extends ServiceImpl<FinanceMapper, Finance>
    implements FinanceService{

}




