package com.club.controller;

import com.club.common.result.Result;
import com.club.entity.dto.exciting.ExcitingMomentsDto;
import com.club.service.ExcitingMomentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date 2024年03月10日
 */
@RestController
@RequestMapping("/api/exciting")
@Tag(name = "精彩瞬间接口", description = "精彩瞬间接口")
public class ExcitingMomentsController extends BaseController {

    @Resource
    private ExcitingMomentsService excitingMomentsService;

    @PostMapping("save")
    @Operation(summary = "保存精彩瞬间")
    public Result<String> saveExcitingMoments(@RequestBody ExcitingMomentsDto excitingMomentsDto) {
        excitingMomentsService.saveExcitingMoments(excitingMomentsDto, getUserId());
        return Result.ok();
    }

    @GetMapping("list")
    @Operation(summary = "获取精彩瞬间列表")
    public Result<List<ExcitingMomentsDto>> getExcitingMomentsList(@RequestParam Long id) {
        List<ExcitingMomentsDto> res = excitingMomentsService.getExcitingMomentsList(id);
        return Result.ok(res);
    }

    @DeleteMapping("remove")
    @Operation(summary = "删除精彩瞬间")
    public Result<String> removeExcitingMoments(@RequestParam Long id) {
        excitingMomentsService.removeExcitingMoments(id, getUserId());
        return Result.ok();
    }

}
