package com.club.controller;

import com.club.common.result.Result;
import com.club.config.AllowAnonymous;
import com.club.entity.dto.dict.DictDto;
import com.club.entity.vo.DictVo;
import com.club.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @date 2024年02月09日
 */
@RestController
@RequestMapping("/api/dict")
@Tag(name = "字典接口", description = "字典相关接口")
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("/list")
    @Operation(summary = "获取字典列表")
    @AllowAnonymous
    public Result<List<DictVo>> list(@RequestParam(required = false) Long parentId,
                                     @RequestParam(required = false, defaultValue = "false") Boolean isGrade) {
        List<DictVo> res = dictService.getDictList(parentId, isGrade);
        return Result.ok(res);
    }

    @GetMapping("/listByGrade")
    @Operation(summary = "获取字典列表(根据年级筛选)")
    @AllowAnonymous
    public Result<List<DictVo>> getListByGrade(@RequestParam(required = false) Long gradeId) {
        List<DictVo> res = dictService.getListByGrade(gradeId);
        return Result.ok(res);
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存或更新字典")
    public Result<String> save(@RequestBody DictDto dictDto) {
        dictService.saveDict(dictDto);
        return Result.ok();
    }

    @DeleteMapping("remove")
    @Operation(summary = "删除字典")
    public Result<String> remove(@RequestParam Long id) {
        dictService.removeById(id);
        return Result.ok();
    }
}
