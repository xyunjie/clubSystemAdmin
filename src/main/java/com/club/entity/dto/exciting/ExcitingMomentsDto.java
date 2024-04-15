package com.club.entity.dto.exciting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @date 2024年04月15日
 */
@Data
public class ExcitingMomentsDto {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "俱乐部ID")
    private Long clubId;

    @Schema(description = "图片ID")
    private Long id;

    @Schema(description = "图片链接")
    private String url;
}
