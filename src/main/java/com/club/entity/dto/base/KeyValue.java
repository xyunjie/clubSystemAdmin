package com.club.entity.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @date 2024年02月27日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class KeyValue {
    private Object key;
    private Object value;
}
