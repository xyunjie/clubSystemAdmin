package com.club.entity.dto.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @date 2024年03月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubBalanceSaveDto {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "社团id")
    private Long clubId;

    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Schema(description = "备注")
    private String remark;
}
