package com.club.entity.dto.club;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月15日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubUserRemoveOrJoinDto {

    @Schema(description = "俱乐部id")
    @NotNull(message = "俱乐部id不能为空")
    private Long clubId;

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

}
