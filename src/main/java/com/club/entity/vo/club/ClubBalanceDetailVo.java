package com.club.entity.vo.club;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @date 2024年02月12日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubBalanceDetailVo {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "社团ID")
    private Long clubId;

    @Schema(description = "社团名称")
    private String clubName;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "余额")
    private BigDecimal balance;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

}
