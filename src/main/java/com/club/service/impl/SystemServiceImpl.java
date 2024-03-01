package com.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.entity.domain.System;
import com.club.mapper.SystemMapper;
import com.club.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @date 2024/2/29 15:46
 **/
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System>
        implements SystemService {
    @Override
    public System getSystemInfo() {
        return lambdaQuery().one();
    }

    @Override
    public void saveSystemInfo(System system, Long userId) {
        System one = this.lambdaQuery().one();
        system.setUpdatedBy(userId);
        if (one == null) {
            save(system);
            return;
        }
        LambdaUpdateWrapper<System> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(System::getId, one.getId());
        updateWrapper.set(StringUtils.isNotEmpty(system.getAbout()), System::getAbout, system.getAbout());
        updateWrapper.set(StringUtils.isNotEmpty(system.getClubTemplate()), System::getClubTemplate, system.getClubTemplate());
        updateWrapper.set(StringUtils.isNotEmpty(system.getIntroduction()), System::getIntroduction, system.getIntroduction());
        updateWrapper.set(System::getUpdatedBy, system.getUpdatedBy());
        this.update(updateWrapper);
    }
}
