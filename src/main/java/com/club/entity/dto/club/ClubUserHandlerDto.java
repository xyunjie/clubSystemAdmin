package com.club.entity.dto.club;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ClubUserHandlerDto {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "状态")
    private Integer status;

}
