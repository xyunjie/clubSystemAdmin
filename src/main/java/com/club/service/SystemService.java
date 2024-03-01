package com.club.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.domain.System;

/**
 * @date 2024/2/29 15:45
 **/
public interface SystemService extends IService<System> {
    /**
     * 获取系统信息
     * @return
     */
    System getSystemInfo();

    /**
     * 保存系统信息
     * @param system
     */
    void saveSystemInfo(System system, Long userId);
}
