package com.club.entity.vo.club;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.club.entity.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @date 2024年02月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubListVo {

    @Schema(name = "id", description = "社团ID")
    @ExcelIgnore
    private Long id;

    @Schema(name = "name", description = "社团名称")
    @ExcelProperty(value = "社团名称", index = 0)
    private String name;

    @Schema(name = "description", description = "社团描述")
    @ExcelProperty(value = "社团描述", index = 1)
    private String description;

    @Schema(name = "money", description = "所需社费")
    @ExcelProperty(value = "所需社费", index = 2)
    private BigDecimal money;

    @Schema(name = "createdBy", description = "社团创建者")
    @ExcelIgnore
    private Long createdBy;

    @Schema(name = "createdName", description = "社团创建者")
    @ExcelProperty(value = "创建者", index = 3)
    private String createdName;

    @Schema(name = "createdUser", description = "社团创建者信息")
    @ExcelIgnore
    private User createdUser;

    @Schema(name = "createdTime", description = "社团创建时间")
    @ExcelProperty(value = "创建时间", index = 4)
    private LocalDateTime createdTime;

    @Schema(description = "状态")
    @ExcelIgnore
    private Integer status;

    @Schema(description = "状态名称")
    @ExcelProperty(value = "状态", index = 7)
    private String statusName;

    @Schema(description = "成员数")
    @ExcelProperty(value = "成员数", index = 5)
    private Long memberCount;

    @Schema(description = "余额")
    @ExcelProperty(value = "余额", index = 6)
    private BigDecimal balance;

    @Schema(description = "加入状态")
    @ExcelIgnore
    private Integer joinStatus;

    @Schema(description = "附件")
    @ExcelIgnore
    private String appendix;

    @Schema(description = "排序")
    @ExcelIgnore
    private Integer sort;

    @Schema(description = "是否置顶")
    @ExcelIgnore
    private Boolean top;

}
