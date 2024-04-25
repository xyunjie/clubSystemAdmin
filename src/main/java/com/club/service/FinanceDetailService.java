package com.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.entity.domain.FinanceDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.dto.balance.BalanceCountDto;
import com.club.entity.dto.balance.ClubBalanceSaveDto;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.vo.balance.BalanceCountVo;
import com.club.entity.vo.club.ClubBalanceDetailVo;

/**
 * @description 针对表【t_finance_detail(财务明细)】的数据库操作Service
 * @createDate 2024-02-07 18:31:00
 */
public interface FinanceDetailService extends IService<FinanceDetail> {

    /**
     * 获取俱乐部财务明细列表
     *
     * @param pageQuery
     * @param userId
     * @return
     */
    Page<ClubBalanceDetailVo> getClubFinanceDetailList(PageQuery pageQuery, Long userId);

    /**
     *  保存俱乐部财务明细
     * @param clubBalanceSaveDto
     * @param userId
     */
    void saveClubFinanceDetail(ClubBalanceSaveDto clubBalanceSaveDto, Long userId);

    BalanceCountVo getBalanceCount(BalanceCountDto balanceCountDto);
}
