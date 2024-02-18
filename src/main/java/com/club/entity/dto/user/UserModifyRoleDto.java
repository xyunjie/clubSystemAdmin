package com.club.entity.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月11日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserModifyRoleDto {

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long id;

    @Schema(description = "角色")
    @NotNull(message = "角色不能为空")
    private String role;

}
