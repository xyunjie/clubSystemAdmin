package com.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.Club;
import com.club.entity.domain.ClubUserMap;
import com.club.entity.domain.FinanceDetail;
import com.club.entity.domain.User;
import com.club.entity.dto.balance.BalanceCountDto;
import com.club.entity.dto.balance.ClubBalanceSaveDto;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.enums.ClubUserStatusEnum;
import com.club.entity.enums.FinanceDetailStatusEnum;
import com.club.entity.vo.balance.BalanceCountVo;
import com.club.entity.vo.club.ClubBalanceDetailVo;
import com.club.mapper.ClubMapper;
import com.club.mapper.FinanceDetailMapper;
import com.club.service.ClubUserMapService;
import com.club.service.FinanceDetailService;
import com.club.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 针对表【t_finance_detail(财务明细)】的数据库操作Service实现
 * @createDate 2024-02-07 18:31:00
 */
@Service
public class FinanceDetailServiceImpl extends ServiceImpl<FinanceDetailMapper, FinanceDetail>
        implements FinanceDetailService {

    @Resource
    private ClubUserMapService clubUserMapService;

    @Resource
    private ClubMapper clubMapper;

    @Resource
    private UserService userService;

    @Override
    public Page<ClubBalanceDetailVo> getClubFinanceDetailList(PageQuery pageQuery, Long userId) {
        List<ClubUserMap> list = clubUserMapService.lambdaQuery().eq(ClubUserMap::getUserId, userId).eq(ClubUserMap::getStatus, ClubUserStatusEnum.CLUB_CREATOR.getValue()).list();
        List<Long> clubIds = list.stream().map(ClubUserMap::getClubId).toList();
        if (clubIds.isEmpty()) {
            return new Page<>();
        }
        LambdaQueryWrapper<Club> clubLambdaQueryWrapper = new LambdaQueryWrapper<>();
        clubLambdaQueryWrapper.in(Club::getId, clubIds);
        List<Club> clubs = clubMapper.selectList(clubLambdaQueryWrapper);
        Page<FinanceDetail> page = new Page<>(pageQuery.getPageNumber(), pageQuery.getPageSize());
        this.lambdaQuery().in(FinanceDetail::getClubId, clubIds).page(page);
        if (page.getRecords().isEmpty()) {
            return new Page<>();
        }
        List<Long> userIds = page.getRecords().stream().map(FinanceDetail::getCreatedBy).distinct().toList();
        List<User> users = userService.listByIds(userIds);
        List<ClubBalanceDetailVo> result = page.getRecords().stream().map(item -> {
            ClubBalanceDetailVo clubBalanceDetailVo = new ClubBalanceDetailVo();
            BeanUtils.copyProperties(item, clubBalanceDetailVo);
            clubBalanceDetailVo.setClubId(item.getClubId());
            clubBalanceDetailVo.setClubName(clubs.stream().filter(club -> club.getId().equals(item.getClubId())).findFirst().orElse(new Club()).getName());
            clubBalanceDetailVo.setUserName(users.stream().filter(user -> user.getId().equals(item.getCreatedBy())).findFirst().orElse(new User()).getName());
            return clubBalanceDetailVo;
        }).toList();
        Page<ClubBalanceDetailVo> resultPage = new Page<>(pageQuery.getPageNumber(), pageQuery.getPageSize());
        BeanUtils.copyProperties(page, resultPage, "records");
        resultPage.setRecords(result);
        return resultPage;
    }

    @Override
    public void saveClubFinanceDetail(ClubBalanceSaveDto clubBalanceSaveDto, Long userId) {
        FinanceDetail financeDetail = new FinanceDetail();
        if (clubBalanceSaveDto.getAmount().equals(BigDecimal.ZERO)) {
            throw new GlobalException("金额不能为0");
        }
        BeanUtils.copyProperties(clubBalanceSaveDto, financeDetail);
        // 根据创建时间倒叙查询最后一个
        FinanceDetail one = this.lambdaQuery()
                .eq(FinanceDetail::getClubId, clubBalanceSaveDto.getClubId())
                .eq(FinanceDetail::getStatus, FinanceDetailStatusEnum.AUDIT_PASS.getValue()).orderByDesc(FinanceDetail::getCreatedTime)
                .last("limit 1").one();
        if (one == null) {
            one = new FinanceDetail();
            one.setBalance(BigDecimal.ZERO);
        }
        if (clubBalanceSaveDto.getAmount().add(one.getBalance()).compareTo(BigDecimal.ZERO) < 0) {
            throw new GlobalException("余额不足");
        }
        financeDetail.setBalance(clubBalanceSaveDto.getAmount().add(one.getBalance()));
        financeDetail.setCreatedBy(userId);
        financeDetail.setStatus(FinanceDetailStatusEnum.AUDIT_PASS.getValue());
        save(financeDetail);
    }

    @Override
    public BalanceCountVo getBalanceCount(BalanceCountDto balanceCountDto) {
        // 计算日期
        LocalDate startTime = LocalDate.parse(balanceCountDto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate startTime2 = LocalDate.parse(balanceCountDto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endTime = LocalDate.parse(balanceCountDto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (startTime.isAfter(endTime)) {
            throw new GlobalException("开始时间不能大于结束时间");
        }
        // 循环每一天
        List<String> countDate = new ArrayList<>();
        while (!startTime2.isAfter(endTime)) {
            countDate.add(startTime2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            startTime2 = startTime2.plusDays(1);
        }
        // 获取期间指定的财务明细
        List<FinanceDetail> financeDetails = this.lambdaQuery()
                .eq(FinanceDetail::getClubId, balanceCountDto.getClubId())
                .in(FinanceDetail::getStatus, FinanceDetailStatusEnum.AUDIT_PASS.getValue())
                .le(FinanceDetail::getCreatedTime, LocalDateTime.of(endTime, LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ge(FinanceDetail::getCreatedTime, LocalDateTime.of(startTime, LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .list();
        // 循环每一天
        List<BigDecimal> expectedData = new ArrayList<>();
        List<BigDecimal> actualData = new ArrayList<>();
        List<BigDecimal> totalData = new ArrayList<>();
        if (financeDetails.isEmpty()) {
            countDate.forEach(e -> {
                expectedData.add(BigDecimal.ZERO);
                actualData.add(BigDecimal.ZERO);
                totalData.add(BigDecimal.ZERO);
            });
            return new BalanceCountVo(expectedData, actualData, totalData, countDate);
        }
        BigDecimal lastTotal = BigDecimal.ZERO;
        for (String date : countDate) {
            List<FinanceDetail> list = financeDetails.stream().filter(e -> e.getCreatedTime().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(date)).toList();
            // 收入
            BigDecimal expected = BigDecimal.ZERO;
            // 支出
            BigDecimal actual = BigDecimal.ZERO;
            for (FinanceDetail financeDetail : list) {
                if (financeDetail.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    expected = expected.add(financeDetail.getAmount());
                } else {
                    actual = actual.add(financeDetail.getAmount().abs());
                }
            }
            BigDecimal total = list.isEmpty() ? BigDecimal.ZERO : list.get(list.size() - 1).getBalance();
            if (total.equals(BigDecimal.ZERO)) {
                total = lastTotal;
            } else {
                lastTotal = total;
            }
            expectedData.add(expected);
            actualData.add(actual);
            totalData.add(total);
        }
        return new BalanceCountVo(expectedData, actualData, totalData, countDate);
    }
}




