package com.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.domain.System;
import com.club.entity.dto.base.PageQuery;
import com.club.entity.vo.club.ClubActivityVo;

import java.util.List;

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

    /**
     * 获取系统公告
     * @param pageQuery
     * @return
     */
    Page<ClubActivityVo> getNotice(PageQuery pageQuery);
}
