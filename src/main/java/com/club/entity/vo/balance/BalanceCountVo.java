package com.club.entity.vo.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @date 2024年04月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BalanceCountVo {

    @Schema(description = "收入")
    private List<BigDecimal> expectedData;

    @Schema(description = "支出")
    private List<BigDecimal> actualData;

    @Schema(description = "余额")
    private List<BigDecimal> totalData;

    @Schema(description = "时间")
    private List<String> countData;

}
