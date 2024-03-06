package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.domain.Activity;
import com.club.entity.domain.System;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date 2024/3/1 10:17
 **/
@RestController
@RequestMapping("/api/system")
@Tag(name = "系统接口", description = "系统接口")
public class SystemController extends BaseController {

    @Resource
    private SystemService systemService;

    @Operation(summary = "获取系统信息")
    @GetMapping("/info")
    public Result<System> getSystemInfo() {
        System system = systemService.getSystemInfo();
        return Result.ok(system);
    }

    @PostMapping("/notice")
    @Operation(summary = "获取系统公告")
    public Result<Page<ClubActivityVo>> getNotice(@RequestBody @Validated PageQuery pageQuery) {
        Page<ClubActivityVo> notice = systemService.getNotice(pageQuery);
        return Result.ok(notice);
    }

    @Operation(summary = "保存系统信息")
    @PostMapping("/save")
    public Result<System> saveSystemInfo(@RequestBody System system) {
        systemService.saveSystemInfo(system, getUserId());
        return Result.ok();
    }

}
