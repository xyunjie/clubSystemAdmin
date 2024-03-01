package com.club.controller;

import com.club.common.result.Result;
import com.club.entity.domain.System;
import com.club.service.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "保存系统信息")
    @PostMapping("/save")
    public Result<System> saveSystemInfo(@RequestBody System system) {
        systemService.saveSystemInfo(system, getUserId());
        return Result.ok();
    }

}
