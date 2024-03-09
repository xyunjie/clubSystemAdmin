package com.club.entity.dto.club;

import com.club.entity.dto.base.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月18日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubActivityQueryDto extends PageQuery {

    @Schema(description = "社团id")
    private Long id;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "类型")
    private String kind;

    @Schema(description = "是否为管理员")
    private Boolean isAdmin;

    @Schema(description = "用户id", hidden = true)
    private Long userId;

}
