package com.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.domain.Activity;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.ClubActivityQueryDto;
import com.club.entity.dto.club.ClubActivitySaveDto;
import com.club.entity.dto.club.ClubQueryUserDto;
import com.club.entity.vo.club.ClubActivityUserVo;
import com.club.entity.vo.club.ClubActivityVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @description 针对表【t_activity(社团公告/活动表)】的数据库操作Service
 * @createDate 2024-02-07 18:31:00
 */
public interface ActivityService extends IService<Activity> {

    /**
     * 获取社团公告/活动列表
     *
     * @param clubActivityQueryDto
     * @return
     */
    Page<ClubActivityVo> getClubNoticeList(ClubActivityQueryDto clubActivityQueryDto, Long userId);

    /**
     * 修改通知/活动状态
     *
     * @param modifyStatusDto
     */
    void modifyActivityStatus(ModifyStatusDto modifyStatusDto);

    /**
     * 删除通知/活动
     *
     * @param id
     * @param userId
     */
    void removeAcvitity(Long id, Long userId);

    /**
     * 保存通知/活动
     *
     * @param clubActivitySaveDto
     * @param userId
     */
    void saveActivity(ClubActivitySaveDto clubActivitySaveDto, Long userId);

    /**
     * 进入通知/活动
     *
     * @param id
     * @param userId
     */
    void entryActivity(Long id, Long userId);

    /**
     * 修改通知/活动进入状态
     *
     * @param modifyStatusDto
     */
    void modifyActivityEntryStatus(ModifyStatusDto modifyStatusDto);

    /**
     * 删除通知/活动进入状态
     *
     * @param id
     * @param userId
     */
    void removeEntry(Long id, Long userId);

    /**
     * 获取活动报名成员
     *
     * @param clubQueryUserDto
     * @return
     */
    Page<ClubActivityUserVo> getActivityUserList(ClubQueryUserDto clubQueryUserDto);

    /**
     * 获取热门公告/活动列表
     *
     * @return
     */
    List<ClubActivityVo> getHotActivityList();

    /**
     * 获取我的通知/活动列表
     *
     * @param userId
     * @return
     */
    List<ClubActivityVo> getClubWarning(Long userId);

    /**
     * 获取我的活动参加的人员
     *
     * @param clubActivityQueryDto
     * @return
     */
    Page<ClubActivityUserVo> getMyActivity(ClubActivityQueryDto clubActivityQueryDto);

    void exportActivityUserList(ClubQueryUserDto clubQueryUserDto, Long userId, HttpServletResponse response);
}
