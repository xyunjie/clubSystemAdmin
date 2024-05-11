package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.dto.club.ClubActivitySaveDto;
import com.club.entity.dto.club.ClubQueryUserDto;
import com.club.entity.vo.club.ClubActivityUserVo;
import com.club.entity.vo.club.ClubActivityVo;
import com.club.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        clubActivityQueryDto.setUserId(getUserId());
        Page<ClubActivityVo> res = activityService.getClubNoticeList(clubActivityQueryDto, getUserId());
        return Result.ok(res);
    }

    @PostMapping("/getMyActivity")
    @Operation(summary = "获取我的活动报名人员")
    public Result<Page<ClubActivityUserVo>> getMyActivity(@RequestBody @Validated ClubActivityQueryDto clubActivityQueryDto) {
        clubActivityQueryDto.setUserId(getUserId());
        Page<ClubActivityUserVo> res = activityService.getMyActivity(clubActivityQueryDto);
        return Result.ok(res);
    }

    @GetMapping("getClubWarning")
    @Operation(summary = "获取社团预警")
    public Result<List<ClubActivityVo>> getClubWarning() {
        List<ClubActivityVo> res = activityService.getClubWarning(getUserId());
        return Result.ok(res);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门活动列表")
    public Result<List<ClubActivityVo>> getHotActivityList() {
        List<ClubActivityVo> res = activityService.getHotActivityList();
        return Result.ok(res);
    }

    @PostMapping("/user/list")
    @Operation(summary = "获取活动报名列表")
    public Result<Page<ClubActivityUserVo>> getActivityUserList(@RequestBody @Validated ClubQueryUserDto clubQueryUserDto) {
        Page<ClubActivityUserVo> res = activityService.getActivityUserList(clubQueryUserDto);
        return Result.ok(res);
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

    @PostMapping("export")
    @Operation(summary = "导出活动报名人员")
    public void exportActivityUser(@RequestBody @Validated ClubQueryUserDto clubQueryUserDto, HttpServletResponse response) {
        activityService.exportActivityUserList(clubQueryUserDto, getUserId(), response);
    }
}
