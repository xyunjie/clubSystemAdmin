package com.club.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.common.result.Result;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.base.KeyValue;
import com.club.entity.dto.club.ClubQueryDto;
import com.club.entity.dto.club.ClubQueryUserDto;
import com.club.entity.dto.club.ClubSaveOrUpdateDto;
import com.club.entity.vo.club.ClubBalanceDetailVo;
import com.club.entity.vo.club.ClubListVo;
import com.club.entity.vo.club.ClubUserVo;
import com.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date 2024年02月10日
 */
@RestController
@RequestMapping("/api/club")
@Tag(name = "社团/组织接口", description = "社团/组织相关接口")
public class ClubController extends BaseController {

    @Resource
    private ClubService clubService;

    @PostMapping("/list")
    @Operation(summary = "获取社团列表")
    public Result<Page<ClubListVo>> list(@RequestBody @Validated ClubQueryDto clubQueryDto) {
        Page<ClubListVo> res = clubService.getClubList(clubQueryDto);
        return Result.ok(res);
    }

    @PostMapping("myClub")
    @Operation(summary = "获取我加入的社团列表")
    public Result<Page<ClubListVo>> myClub(@RequestBody @Validated ClubQueryDto clubQueryDto) {
        Page<ClubListVo> res = clubService.getMyClub(clubQueryDto, getUserId());
        return Result.ok(res);
    }

    @GetMapping("detail")
    @Operation(summary = "获取社团详情")
    public Result<ClubListVo> detail(@RequestParam Long id) {
        ClubListVo res = clubService.getClubDetail(id);
        return Result.ok(res);
    }

    @GetMapping("hot")
    @Operation(summary = "获取热门社团")
    public Result<List<ClubListVo>> hot() {
        List<ClubListVo> res = clubService.getHot();
        return Result.ok(res);
    }

    @GetMapping("/all")
    @Operation(summary = "获取全部审核通过社团（k-v）")
    public Result<List<KeyValue>> getAllClub() {
        List<KeyValue> res = clubService.getAllClub();
        return Result.ok(res);
    }

    @PostMapping("save")
    @Operation(summary = "创建/修改社团信息")
    public Result<String> saveClub(@RequestBody @Validated ClubSaveOrUpdateDto clubSaveOrUpdateDto) {
        clubService.saveOrUpdateClub(clubSaveOrUpdateDto, getUserId());
        return Result.ok();
    }

    @PostMapping("status")
    @Operation(summary = "修改社团状态")
    public Result<String> modifyClubStatus(@RequestBody @Validated ModifyStatusDto modifyStatusDto) {
        clubService.modifyClubStatus(modifyStatusDto);
        return Result.ok();
    }

    @PostMapping("getUser")
    @Operation(summary = "获取社团成员信息")
    public Result<Page<ClubUserVo>> getUserInfo(@RequestBody @Validated ClubQueryUserDto clubQueryUserDto) {
        Page<ClubUserVo> list = clubService.getClubUserList(clubQueryUserDto);
        return Result.ok(list);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除社团")
    public Result<String> remove(@RequestParam Long id) {
        clubService.removeClub(id, getUserId());
        return Result.ok();
    }

    @PostMapping("export")
    @Operation(summary = "导出社团信息")
    public void export(@RequestBody @Validated ClubQueryDto clubQueryDto, HttpServletResponse response) {
        clubService.exportClub(clubQueryDto, response);
    }

    @PostMapping("balance")
    @Operation(summary = "获取社团余额详细信息")
    public Result<Page<ClubBalanceDetailVo>> getClubBalance(@RequestBody @Validated ClubQueryUserDto clubQueryUserDto) {
        Page<ClubBalanceDetailVo> res = clubService.getClubBalanceDetail(clubQueryUserDto);
        return Result.ok(res);
    }

}
