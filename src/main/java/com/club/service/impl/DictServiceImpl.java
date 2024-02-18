package com.club.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.club.common.exception.GlobalException;
import com.club.entity.domain.Dict;
import com.club.entity.dto.dict.DictDto;
import com.club.entity.vo.DictVo;
import com.club.mapper.DictMapper;
import com.club.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 针对表【t_dict(字典表)】的数据库操作Service实现
 * @createDate 2024-02-07 18:31:00
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

    @Override
    public List<DictVo> getDictList(Long parentId, Boolean isGrade) {
        List<Dict> list = lambdaQuery().eq(parentId != null, Dict::getParentId, parentId).eq(Dict::getIsGrade, isGrade).list();
        // 封装vo
        List<DictVo> dictVos = list.stream().map(item -> {
            DictVo vo = new DictVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).toList();
        return listToTree(dictVos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDict(DictDto dictDto) {
        Dict dict = new Dict();
        // 判断是否存在重名
        Dict one = lambdaQuery().eq(Dict::getName, dictDto.getName()).one();
        if (one != null && !one.getId().equals(dictDto.getId())) {
            throw new GlobalException("字典名称已存在");
        }
        BeanUtils.copyProperties(dictDto, dict);
        saveOrUpdate(dict);
    }

    @Override
    public List<DictVo> getListByGrade(Long gradeId) {
        // 查询父ID为grade的班级
        List<Dict> list = lambdaQuery().eq(gradeId != null, Dict::getParentId, gradeId).eq(Dict::getIsGrade, false).list();
        // 转为VO
        List<DictVo> dictVos = list.stream().map(item -> {
            DictVo dictVo = new DictVo();
            BeanUtils.copyProperties(item, dictVo);
            return dictVo;
        }).toList();
        return listToTree(dictVos);
    }

    /**
     * 将单位列表转换为树结构(递归处理)
     *
     * @param list
     * @return
     */
    private List<DictVo> listToTree(List<DictVo> list) {
        // 使用递归实现
        List<DictVo> treeList = new ArrayList<>();
        for (DictVo tree : list) {
            if (tree.getParentId() == null) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }


    /**
     * 递归查找子节点
     *
     * @param tree
     * @param list
     * @return
     */
    private DictVo findChildren(DictVo tree, List<DictVo> list) {
        for (DictVo node : list) {
            if (node.getParentId() != null && node.getParentId().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}




