package com.club.controller;

import com.club.common.result.Result;
import com.club.entity.dto.club.ClubUserHandlerDto;
import com.club.entity.dto.club.ClubUserRemoveOrJoinDto;
import com.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2024年02月15日
 */
@RestController
@RequestMapping("/api/clubUser")
@Tag(name = "社团用户管理", description = "社团用户管理接口")
public class ClubUserController extends BaseController {

    @Resource
    private ClubService clubService;

    @PostMapping("/remove")
    @Operation(summary = "删除社团用户")
    public Result<String> remove(@RequestBody @Validated ClubUserRemoveOrJoinDto clubUserRemoveDto) {
        clubService.removeClubUser(clubUserRemoveDto);
        return Result.ok();
    }

    @PostMapping("join")
    @Operation(summary = "申请加入社团")
    public Result<String> join(@RequestBody @Validated ClubUserRemoveOrJoinDto clubUserJoinDto) {
        clubService.joinClub(clubUserJoinDto);
        return Result.ok();
    }

    @PostMapping("handler")
    @Operation(summary = "处理社团用户申请")
    public Result<String> handler(@RequestBody @Validated ClubUserHandlerDto clubUserHandlerDto) {

        clubService.handlerClubUser(clubUserHandlerDto, getUserId());

        return Result.ok();
    }
}
