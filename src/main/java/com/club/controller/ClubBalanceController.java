package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.dto.balance.BalanceCountDto;
import com.club.entity.dto.balance.ClubBalanceSaveDto;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.vo.balance.BalanceCountVo;
import com.club.entity.vo.club.ClubBalanceDetailVo;
import com.club.service.FinanceDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @date 2024年03月10日
 */
@RestController
@RequestMapping("/api/balance")
@Tag(name = "社团财务接口", description = "社团财务接口")
public class ClubBalanceController extends BaseController {

    @Resource
    private FinanceDetailService financeDetailService;

    @PostMapping("/list")
    @Operation(summary = "社团财务列表")
    public Result<Page<ClubBalanceDetailVo>> list(@RequestBody @Validated PageQuery pageQuery) {
        Page<ClubBalanceDetailVo> res = financeDetailService.getClubFinanceDetailList(pageQuery, getUserId());
        return Result.ok(res);
    }

    @PostMapping("/save")
    @Operation(summary = "保存社团财务")
    public Result<String> saveBalance(@RequestBody @Validated ClubBalanceSaveDto clubBalanceSaveDto) {
        financeDetailService.saveClubFinanceDetail(clubBalanceSaveDto, getUserId());
        return Result.ok();
    }

    @PostMapping("/count")
    @Operation(summary = "获取社团财务总额")
    public Result<BalanceCountVo> getBalanceCount(@RequestBody @Validated BalanceCountDto balanceCountDto) {
        BalanceCountVo res = financeDetailService.getBalanceCount(balanceCountDto);
        return Result.ok(res);
    }

}
