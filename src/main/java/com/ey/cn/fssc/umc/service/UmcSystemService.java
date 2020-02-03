package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.dto.UmcSystemDto;
import com.ey.cn.fssc.umc.entity.UmcSystem;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcSystemRepository;
import com.ey.cn.fssc.umc.util.UmcUserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 描述：UmcSystem Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcSystemService {

    @Autowired
    private UmcSystemRepository repository;

    /**
     * 创建UmcSystem
     *
     * @param source 系统
     * @return 成功true  失败false
     */
    public boolean create(UmcSystemDto source) {

        UmcSystem target = new UmcSystem();
        BeanUtils.copyProperties(source, target);
        target.setCreateUser(UmcUserUtils.getCurrentUserName());
        target.setIsDeleted(false);
        repository.save(target);

        return true;

    }

    /**
     * 根据ID删除UmcSystem
     *
     * @param id 系统主键id
     * @return 成功true  失败false
     */
    public boolean delete(String id) {

        repository.deleteById(id);

        return true;

    }

    /**
     * 更新UmcSystem
     *
     * @param id     系统主键id
     * @param source 系统
     * @return 成功true  失败false
     */
    public boolean update(String id, UmcSystemDto source) {

        UmcSystem exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("系统记录不存在");
        }
        if (StringUtils.isNotEmpty(source.getSysCode())) {
            exist.setSysCode(source.getSysCode());
        }
        if (StringUtils.isNotEmpty(source.getSysName())) {
            exist.setSysName(source.getSysName());
        }
        if (StringUtils.isNotEmpty(source.getRemarks())) {
            exist.setRemarks(source.getRemarks());
        }
        exist.setUpdateUser(UmcUserUtils.getCurrentUserName());
        repository.save(exist);

        return true;

    }

    /**
     * 根据ID查询UmcSystem
     *
     * @param id 系统主键id
     * @return 系统详情
     */
    public UmcSystemDto detail(String id) {

        UmcSystem exist = repository.getOne(id);
        if (null == exist) {
            throw new BizException("系统记录不存在");
        }

        UmcSystemDto target = new UmcSystemDto();
        BeanUtils.copyProperties(exist, target, new String[]{"umcResources", "umcRoles", "umcRoleCategories"});

        return target;

    }

    public UmcSystem detailEntity(String id) {

        UmcSystem exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("记录不存在");
        }

        return exist;
    }

    public List<UmcSystem> findAll() {
        return repository.findAll();
    }

    /**
     * 分页查询查询UmcSystem
     *
     * @param pageNo   页码
     * @param pageSize 页面大小
     * @param paramMap 查询条件
     * @return 分页结果
     */
    public Page<UmcSystemDto> page(Integer pageNo, Integer pageSize, Map<String, String> paramMap) {

//        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
//        Page<UmcSystem> data = repository.findAll(new UmcSystemSpec(paramMap), pageable);
//
//        List<UmcSystemDto> list = new ArrayList<>();
//        for (UmcSystem obj : data.getContent()) {
//            UmcSystemDto target = new UmcSystemDto();
//            BeanUtils.copyProperties(obj, target, new String[]{"umcResources", "umcRoles", "umcRoleCategories"});
//            list.add(target);
//        }
//
//        return new PageImpl<>(list, pageable, data.getTotalElements());

        return null;
    }

    /**
     * 获得业务系统列表,下级节点是资源
     *
     * @return 系统树列表（下级节点是资源）
     */
    public List<TreeNode> findAllSystemNodeOfResList() {

        return repository.findAllSystemNodeOfResList();
    }

    /**
     * 获得业务系统列表,下级节点是角色分类
     *
     * @return 系统树列表（下级节点是角色分类）
     */
    public List<TreeNode> findAllSystemNodeOfRoleList() {

        return repository.findAllSystemNodeOfRoleList();
    }
}