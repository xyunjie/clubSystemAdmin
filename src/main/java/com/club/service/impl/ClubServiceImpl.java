package com.club.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.*;
import com.club.entity.dto.ModifyStatusDto;
import com.club.entity.dto.base.KeyValue;
import com.club.entity.dto.club.*;
import com.club.entity.enums.ClubStatusEnum;
import com.club.entity.enums.ClubUserStatusEnum;
import com.club.entity.enums.FinanceDetailStatusEnum;
import com.club.entity.enums.UserRoleEnum;
import com.club.entity.vo.UserVo;
import com.club.entity.vo.club.ClubBalanceDetailVo;
import com.club.entity.vo.club.ClubListVo;
import com.club.entity.vo.club.ClubUserVo;
import com.club.mapper.ClubMapper;
import com.club.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 针对表【t_club(社团表)】的数据库操作Service实现
 * @createDate 2024-02-07 18:31:00
 */
@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements ClubService {

    @Resource
    private UserService userService;

    @Resource
    private ClubUserMapService clubUserMapService;

    @Resource
    private DictService dictService;

    @Resource
    private FinanceDetailService financeDetailService;

    @Override
    public Page<ClubListVo> getClubList(ClubQueryDto clubQueryDto) {
        Page<Club> page = new Page<>(clubQueryDto.getPageNumber(), clubQueryDto.getPageSize());
        LambdaQueryWrapper<Club> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(clubQueryDto.getQuery()), Club::getName, clubQueryDto.getQuery());
        this.page(page, queryWrapper);
        if (page.getRecords().isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        }
        // 封装Vo
        List<Long> userIds = page.getRecords().stream().map(Club::getCreatedBy).toList();
        Map<Long, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, item -> item));
        List<Long> clubIds = page.getRecords().stream().map(Club::getId).toList();
        Map<Long, List<ClubUserMap>> clubMap = clubUserMapService.lambdaQuery().in(ClubUserMap::getClubId, clubIds).list().stream().collect(Collectors.groupingBy(ClubUserMap::getClubId));
        List<ClubListVo> result = page.getRecords().stream().map(item -> {
            ClubListVo clubListVo = new ClubListVo();
            BeanUtils.copyProperties(item, clubListVo);
            clubListVo.setCreatedUser(userMap.get(item.getCreatedBy()));
            clubListVo.setCreatedName(userMap.get(item.getCreatedBy()).getName());
            List<ClubUserMap> userMaps = clubMap.get(item.getId());
            clubListVo.setMemberCount(userMaps == null ? 0 : userMaps.size());
            clubListVo.setStatusName(ClubStatusEnum.getDesc(item.getStatus()));
            return clubListVo;
        }).toList();
        Page<ClubListVo> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        resultPage.setRecords(result);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateClub(ClubSaveOrUpdateDto clubSaveOrUpdateDto, Long userId) {
        // 保证社团名称不可重复
        Club one = lambdaQuery().eq(Club::getName, clubSaveOrUpdateDto.getName()).one();
        if (one != null && clubSaveOrUpdateDto.getId() != null) {
            throw new GlobalException("社团名称不可重复");
        }
        // 保存
        Club club = new Club();
        BeanUtils.copyProperties(clubSaveOrUpdateDto, club, "appendix");
        if (clubSaveOrUpdateDto.getId() != null) {
            // 如果是修改社团信息
            club.setStatus(ClubStatusEnum.UPDATE_INFO.getValue());
        } else {
            club.setAppendix(clubSaveOrUpdateDto.getAppendix());
            if (clubSaveOrUpdateDto.getCreatedBy() == null) {
                club.setCreatedBy(userId);
            }
            // 判断当前用户是否已经创建过社团
            List<Club> list = lambdaQuery().eq(Club::getCreatedBy, club.getCreatedBy()).list();
            if (!list.isEmpty()) {
                throw new GlobalException("每名成员只可以创建一个社团！");
            }
        }
        this.saveOrUpdate(club);
        if (clubSaveOrUpdateDto.getId() == null) {
            //  如果是创建社团
            ClubUserMap clubUserMap = new ClubUserMap();
            clubUserMap.setClubId(club.getId());
            clubUserMap.setUserId(club.getCreatedBy());
            // -1表示创建者
            clubUserMap.setStatus(ClubUserStatusEnum.CLUB_CREATOR.getValue());
            clubUserMapService.save(clubUserMap);
        }
    }

    @Override
    public void modifyClubStatus(ModifyStatusDto modifyStatusDto) {
        Club club = this.getById(modifyStatusDto.getId());
        if (club == null) {
            throw new GlobalException("社团不存在");
        }
        ClubStatusEnum clubEnum = ClubStatusEnum.getEnum(modifyStatusDto.getStatus());
        if (clubEnum == null) {
            throw new GlobalException("社团状态不正确");
        }
        boolean update = lambdaUpdate().eq(Club::getId, modifyStatusDto.getId()).set(Club::getStatus, clubEnum.getValue()).update();
        if (!update) {
            throw new GlobalException("社团状态修改失败");
        }
    }

    @Override
    public Page<ClubUserVo> getClubUserList(ClubQueryUserDto clubQueryUserDto) {
        // 获取社团成员列表
        Club club = getById(clubQueryUserDto.getClubId());
        if (club == null) {
            throw new GlobalException("社团不存在");
        }
        List<Dict> dicts = dictService.list();
        Page<ClubUserMap> page = new Page<>(clubQueryUserDto.getPageNumber(), clubQueryUserDto.getPageSize());
        LambdaQueryWrapper<ClubUserMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ClubUserMap::getClubId, clubQueryUserDto.getClubId());
        queryWrapper.last("order by status <> -1, status <> 1, status <> 0, status desc");
        clubUserMapService.page(page, queryWrapper);
        List<Long> list = page.getRecords().stream().map(ClubUserMap::getUserId).toList();
        if (list.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        }
        List<User> userList = userService.listByIds(list);
        List<ClubUserVo> result = page.getRecords().stream().map(item -> {
            User userInfo = userList.stream().filter(user -> user.getId().equals(item.getUserId())).findFirst().orElse(null);
            if (userInfo == null) {
                throw new GlobalException("用户不存在");
            }
            UserVo userVo = userService.parseUserToUserVo(userInfo, dicts);
            ClubUserVo clubUserVo = new ClubUserVo();
            BeanUtils.copyProperties(userVo, clubUserVo);
            clubUserVo.setId(item.getId());
            clubUserVo.setClubStatus(item.getStatus());
            return clubUserVo;
        }).toList();
        Page<ClubUserVo> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(result);
        return resultPage;
    }

    @Override
    public void removeClub(Long id, Long userId) {
        // 删除
        boolean b = this.removeById(id);
        if (!b) {
            throw new GlobalException("删除失败");
        }
    }

    @Override
    public void exportClub(ClubQueryDto clubQueryDto, HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(LocalDateTime.now() + "导出社团信息", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".xlsx");
        clubQueryDto.setPageSize(Integer.MAX_VALUE);
        Page<ClubListVo> clubList = this.getClubList(clubQueryDto);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, ClubListVo.class).sheet("社团").doWrite(clubList.getRecords());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new GlobalException(e.getMessage(), 400);
        }
    }

    @Override
    public Page<ClubBalanceDetailVo> getClubBalanceDetail(ClubQueryUserDto clubQueryUserDto) {
        Page<FinanceDetail> page = new Page<>(clubQueryUserDto.getPageNumber(), clubQueryUserDto.getPageSize());
        financeDetailService.lambdaQuery().eq(FinanceDetail::getClubId, clubQueryUserDto.getClubId()).page(page);
        if (page.getRecords().isEmpty()) {
            return new Page<>();
        }
        // 封装用户信息
        List<Long> userIds = page.getRecords().stream().map(FinanceDetail::getCreatedBy).distinct().toList();
        Map<Long, String> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, User::getName));
        List<ClubBalanceDetailVo> resultRecords = page.getRecords().stream().map(item -> {
            ClubBalanceDetailVo clubBalanceDetailVo = new ClubBalanceDetailVo();
            BeanUtils.copyProperties(item, clubBalanceDetailVo);
            clubBalanceDetailVo.setUserName(userMap.get(item.getCreatedBy()));
            return clubBalanceDetailVo;
        }).toList();
        Page<ClubBalanceDetailVo> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage, "records");
        resultPage.setRecords(resultRecords);
        return resultPage;
    }

    @Override
    public void removeClubUser(ClubUserRemoveOrJoinDto clubUserRemoveDto) {
        ClubUserMap clubUserMap = clubUserMapService.lambdaQuery().eq(ClubUserMap::getClubId, clubUserRemoveDto.getClubId()).eq(ClubUserMap::getUserId, clubUserRemoveDto.getUserId()).one();
        if (clubUserMap == null) {
            throw new GlobalException("用户不存在");
        }
        if (clubUserMap.getStatus().equals(ClubUserStatusEnum.CLUB_CREATOR.getValue())) {
            throw new GlobalException("创建者不能删除");
        }
        // 删除
        clubUserMapService.removeById(clubUserMap.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinClub(ClubUserRemoveOrJoinDto clubUserJoinDto) {
        // 首先查询是否已经加入了这个社团
        ClubUserMap clubUserMap = clubUserMapService.lambdaQuery().eq(ClubUserMap::getClubId, clubUserJoinDto.getClubId()).eq(ClubUserMap::getUserId, clubUserJoinDto.getUserId()).one();
        if (clubUserMap != null) {
            throw new GlobalException("您无法加入该社团！");
        }
        // 可以加入
        clubUserMap = new ClubUserMap();
        clubUserMap.setClubId(clubUserJoinDto.getClubId());
        clubUserMap.setUserId(clubUserJoinDto.getUserId());
        clubUserMap.setStatus(ClubUserStatusEnum.CLUB_APPLY.getValue());
        // 保存
        clubUserMapService.save(clubUserMap);
        // 添加社费
        Club club = this.getById(clubUserJoinDto.getClubId());
        BigDecimal money = club.getMoney();
        club.setBalance(club.getBalance().add(money));
        // 保存
        this.updateById(club);
        // 添加明细
        FinanceDetail financeDetail = new FinanceDetail();
        financeDetail.setClubId(clubUserJoinDto.getClubId());
        financeDetail.setCreatedBy(clubUserJoinDto.getUserId());
        // 交易金额
        financeDetail.setAmount(club.getMoney());
//        financeDetail.setBalance(club.getBalance());
        financeDetail.setStatus(FinanceDetailStatusEnum.AUDITING.getValue());
        financeDetail.setRemark("加入社团");
        financeDetailService.save(financeDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerClubUser(ClubUserHandlerDto clubUserHandlerDto, Long userId) {
        // 判断是否有权限
        User user = userService.getById(userId);
        ClubUserMap clubUserMap = clubUserMapService.getById(clubUserHandlerDto.getId());
        if (clubUserMap == null) {
            throw new GlobalException("系统异常！");
        }
        // 获取社团信息
        Club club = getById(clubUserMap.getClubId());
        if (user == null || club == null) {
            throw new GlobalException("您没有权限处理该申请！");
        }
        if (user.getRole().equals(UserRoleEnum.USER.getValue()) && !club.getCreatedBy().equals(userId)) {
            throw new GlobalException("您没有权限处理该申请！");
        }
        ClubUserStatusEnum clubUserStatusEnum = ClubUserStatusEnum.getEnumByValue(clubUserHandlerDto.getStatus());
        if (clubUserStatusEnum == null) {
            throw new GlobalException("非法请求！");
        }
        // 处理申请
        if (clubUserMap.getStatus().equals(ClubUserStatusEnum.CLUB_APPLY.getValue())) {
            clubUserMap.setStatus(clubUserHandlerDto.getStatus());
        }
        clubUserMapService.updateById(clubUserMap);
        FinanceDetail financeDetail = financeDetailService.lambdaQuery().eq(FinanceDetail::getClubId, clubUserMap.getClubId()).eq(FinanceDetail::getCreatedBy, userId).eq(FinanceDetail::getClubUserMapId, clubUserMap.getId()).one();
        if (financeDetail != null) {
            //  处理财务
            if (clubUserStatusEnum.equals(ClubUserStatusEnum.CLUB_MEMBER)) {
                // 允许加入
                financeDetail.setStatus(FinanceDetailStatusEnum.AUDIT_PASS.getValue());
                // 设置交易后金额
                financeDetail.setBalance(club.getBalance().add(financeDetail.getAmount()));
                club.setBalance(club.getBalance().add(financeDetail.getAmount()));
            } else {
                // 不允许加入
                financeDetail.setStatus(FinanceDetailStatusEnum.AUDIT_REJECT.getValue());
            }
            financeDetailService.updateById(financeDetail);
            // 处理俱乐部余额
            this.updateById(club);
        }
    }

    @Override
    public List<KeyValue> getAllClub() {
        List<Club> list = lambdaQuery().in(Club::getStatus, ClubStatusEnum.UPDATE_INFO.getValue(), ClubStatusEnum.PASS.getValue()).list();
        return list.stream().map(item -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setKey(item.getName());
            keyValue.setValue(item.getId());
            return keyValue;
        }).toList();
    }

    @Override
    public Page<ClubListVo> getMyClub(ClubQueryDto clubQueryDto, Long userId) {
        Page<Club> page = new Page<>(clubQueryDto.getPageNumber(), clubQueryDto.getPageSize());
        LambdaQueryWrapper<Club> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(clubQueryDto.getQuery()), Club::getName, clubQueryDto.getQuery());
        List<ClubUserMap> myJoinClubList = clubUserMapService.lambdaQuery()
                .eq(ClubUserMap::getUserId, userId)
                .in(!clubQueryDto.getIsAdmin(), ClubUserMap::getStatus, ClubUserStatusEnum.CLUB_CREATOR.getValue(), ClubUserStatusEnum.CLUB_MEMBER.getValue())
                .list();
        if (myJoinClubList.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize());
        }
        List<Long> myJoinClubIds = myJoinClubList.stream().map(ClubUserMap::getClubId).toList();
        queryWrapper.in(Club::getId, myJoinClubIds);
        this.page(page, queryWrapper);
        if (page.getRecords().isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        }
        // 封装Vo
        List<Long> userIds = page.getRecords().stream().map(Club::getCreatedBy).distinct().toList();
        Map<Long, User> userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(User::getId, item -> item));
        Map<Long, ClubUserMap> clubMap = myJoinClubList.stream().collect(Collectors.toMap(ClubUserMap::getClubId, item -> item));
        List<ClubListVo> result = page.getRecords().stream().map(item -> {
            ClubListVo clubListVo = new ClubListVo();
            BeanUtils.copyProperties(item, clubListVo);
            clubListVo.setCreatedUser(userMap.get(item.getCreatedBy()));
            clubListVo.setCreatedName(userMap.get(item.getCreatedBy()).getName());
            clubListVo.setStatusName(ClubStatusEnum.getDesc(item.getStatus()));
            clubListVo.setJoinStatus(clubMap.get(item.getId()).getStatus());
            return clubListVo;
        }).toList();
        Page<ClubListVo> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        resultPage.setRecords(result);
        return resultPage;
    }

    @Override
    public List<ClubListVo> getHot() {
        List<Club> list = this.lambdaQuery().orderByDesc(Club::getViews).orderByAsc(Club::getCreatedTime).last("limit 10").list();
        return list.stream().map(item -> {
            ClubListVo clubListVo = new ClubListVo();
            BeanUtils.copyProperties(item, clubListVo);
            return clubListVo;
        }).toList();
    }

    @Override
    public ClubListVo getClubDetail(Long id) {
        Club club = this.getById(id);
        if (club == null) {
            return new ClubListVo();
        }
        ClubListVo clubListVo = new ClubListVo();
        BeanUtils.copyProperties(club, clubListVo);
        User user = userService.getById(club.getCreatedBy());
        Long count = clubUserMapService.lambdaQuery().eq(ClubUserMap::getClubId, club.getId())
                .in(ClubUserMap::getStatus, ClubUserStatusEnum.CLUB_CREATOR.getValue(), ClubUserStatusEnum.CLUB_MEMBER.getValue()).count();
        clubListVo.setCreatedUser(user);
        clubListVo.setCreatedName(user.getName());
        clubListVo.setStatusName(ClubStatusEnum.getDesc(club.getStatus()));
        clubListVo.setMemberCount(count.intValue());
        return clubListVo;
    }
}




