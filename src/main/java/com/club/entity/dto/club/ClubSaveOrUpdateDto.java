package com.club.entity.dto.club;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * @date 2024年02月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClubSaveOrUpdateDto {

    @Schema(name = "id", description = "社团ID")
    private Long id;

    @Schema(name = "name", description = "社团名称")
    @NotNull(message = "社团名称不能为空")
    @Length(min = 1, max = 20, message = "社团名称长度在1-20之间")
    private String name;

    @Schema(name = "description", description = "社团描述")
    @NotNull(message = "社团描述不能为空")
    @Length(max = 200, message = "社团描述长度不能超过200字")
    private String description;

    @Schema(name = "money", description = "社费")
    @NotNull(message = "所需社费不能为空")
    @Max(value = 200, message = "社费不能超过200")
    @Min(value = 0, message = "社费不能小于0")
    private BigDecimal money;

    @Schema(name = "appendix", description = "附件")
    private String appendix;

    @Schema(name = "createdBy", description = "创建人ID")
    private Long createdBy;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否置顶")
    private Boolean top;

}
