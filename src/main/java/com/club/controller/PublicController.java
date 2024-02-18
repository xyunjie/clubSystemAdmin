package com.club.controller;

import com.club.common.result.Result;
import com.club.utils.QiNiuUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author jzx
 * @date 2024年02月12日
 */
@RestController
@RequestMapping("/api/public")
@Tag(name = "公共接口", description = "公共接口")
public class PublicController extends BaseController {

    @PostMapping("upload")
    @ResponseBody
    @Operation(summary = "文件上传")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = QiNiuUtil.saveFile(file);
        return Result.ok(fileUrl);
    }

}
