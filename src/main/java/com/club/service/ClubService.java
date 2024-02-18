package com.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.club.entity.domain.Club;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.club.*;
import com.club.entity.vo.club.ClubBalanceDetailVo;
import com.club.entity.vo.club.ClubListVo;
import com.club.entity.vo.club.ClubUserVo;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @description 针对表【t_club(社团表)】的数据库操作Service
 * @createDate 2024-02-07 18:31:00
 */
public interface ClubService extends IService<Club> {

    /**
     * 获取社团列表
     *
     * @param clubQueryDto
     * @return
     */
    Page<ClubListVo> getClubList(ClubQueryDto clubQueryDto);

    /**
     * 新增或更新社团
     *
     * @param clubSaveOrUpdateDto
     * @param userId
     */
    void saveOrUpdateClub(ClubSaveOrUpdateDto clubSaveOrUpdateDto, Long userId);

    /**
     * 修改社团状态
     *
     * @param modifyStatusDto
     */
    void modifyClubStatus(ModifyStatusDto modifyStatusDto);

    /**
     * 获取成员列表
     *
     * @param clubQueryUserDto
     * @return
     */
    Page<ClubUserVo> getClubUserList(ClubQueryUserDto clubQueryUserDto);

    /**
     * 移除成员
     *
     * @param id
     * @param userId
     */
    void removeClub(Long id, Long userId);

    /**
     * 导出社团
     *
     * @param clubQueryDto
     * @param response
     */
    void exportClub(ClubQueryDto clubQueryDto, HttpServletResponse response);

    /**
     * 获取社团余额明细
     *
     * @param clubQueryUserDto
     * @return
     */
    Page<ClubBalanceDetailVo> getClubBalanceDetail(ClubQueryUserDto clubQueryUserDto);

    /**
     * 移除成员
     *
     * @param clubUserRemoveDto
     */
    void removeClubUser(ClubUserRemoveOrJoinDto clubUserRemoveDto);

    /**
     *  加入社团
     * @param clubUserJoinDto
     */
    void joinClub(ClubUserRemoveOrJoinDto clubUserJoinDto);

    /**
     *  处理成员请求
     * @param clubUserHandlerDto
     * @param userId
     */
    void handlerClubUser(ClubUserHandlerDto clubUserHandlerDto, Long userId);

}
