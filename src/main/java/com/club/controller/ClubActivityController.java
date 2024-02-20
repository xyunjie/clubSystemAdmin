package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.dto.club.ClubActivitySaveDto;
import com.club.entity.dto.club.ClubQueryDto;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @date 2024年02月18日
 */
@RestController
@RequestMapping("/api/activity")
@Tag(name = "社团/组织公告接口", description = "社团/组织公告相关接口")
public class ClubActivityController extends BaseController {

    @Resource
    private ActivityService activityService;

    @PostMapping("save")
    @Operation(summary = "保存公告")
    public Result<String> saveActivity(@RequestBody @Validated ClubActivitySaveDto clubActivitySaveDto) {
        activityService.saveActivity(clubActivitySaveDto, getUserId());
        return Result.ok();
    }

    @PostMapping("/list")
    @Operation(summary = "获取公告/活动列表")
    public Result<Page<ClubActivityVo>> list(@RequestBody @Validated ClubActivityQueryDto clubActivityQueryDto) {
        Page<ClubActivityVo> res = activityService.getClubNoticeList(clubActivityQueryDto);
        return Result.ok(res);
    }

    @PostMapping("/user/list")
    @Operation(summary = "获取活动报名列表")
    public Result<Page<ClubActivityVo>> getActivityUserList(@RequestBody @Validated ClubQueryDto clubQueryDto) {
//        Page<ClubActivityVo> res = activityService.getActivityUserList(clubQueryDto);
        return Result.ok();
    }

    @PostMapping("status")
    @Operation(summary = "修改通知/活动状态")
    public Result<String> modifyClubStatus(@RequestBody @Validated ModifyStatusDto modifyStatusDto) {
        activityService.modifyActivityStatus(modifyStatusDto);
        return Result.ok();
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除通知/活动")
    public Result<String> remove(@RequestParam Long id) {
        activityService.removeAcvitity(id, getUserId());
        return Result.ok();
    }

    @GetMapping("entry")
    @Operation(summary = "活动报名")
    public Result<String> entryActivity(@RequestParam Long id) {
        activityService.entryActivity(id, getUserId());
        return Result.ok();
    }

    @PostMapping("entry/status")
    @Operation(summary = "修改活动报名状态")
    public Result<String> modifyEntryStatus(@RequestBody @Validated ModifyStatusDto modifyStatusDto) {
        activityService.modifyActivityEntryStatus(modifyStatusDto);
        return Result.ok();
    }

    @DeleteMapping("entry/remove")
    @Operation(summary = "删除用户活动报名")
    public Result<String> removeEntry(@RequestParam Long id) {
        activityService.removeEntry(id, getUserId());
        return Result.ok();
    }
}
