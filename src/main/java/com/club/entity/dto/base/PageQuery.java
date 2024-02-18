package com.club.entity.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月10日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageQuery {

    @Schema(description = "页码")
    private Integer pageNumber;

    @Schema(description = "每页条数")
    private Integer pageSize;

    @Schema(description = "查询条件")
    private String query;

}
