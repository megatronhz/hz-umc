package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.dto.UmcRoleCategoryDto;
import com.ey.cn.fssc.umc.entity.UmcRoleCategory;
import com.ey.cn.fssc.umc.entity.UmcSystem;
import com.ey.cn.fssc.umc.repository.UmcRoleCategoryRepository;
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
 * 描述：UmcRoleCategory Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcRoleCategoryService {

    @Autowired
    private UmcRoleCategoryRepository repository;

    /**
     * 创建UmcRoleCategory
     *
     * @param source 角色分类
     * @return 成功true 失败false
     */
    public boolean create(UmcRoleCategoryDto source) {

        UmcRoleCategory target = new UmcRoleCategory();
        BeanUtils.copyProperties(source, target);
        target.setCreateUser(UmcUserUtils.getCurrentUserName());
        target.setIsDeleted(false);
        target.setIsLeaf(false);
        UmcSystem umcSystem = new UmcSystem();
        umcSystem.setSysId(source.getSysId());
        target.setUmcSystem(umcSystem);
        target.setParentId(source.getSysId());
        repository.save(target);
        return true;

    }

    /**
     * 根据ID删除UmcRoleCategory
     *
     * @param id 角色分类主键id
     * @return 成功true 失败false
     */
    public boolean delete(String id) {

        repository.deleteById(id);

        return true;

    }

    /**
     * 更新UmcRoleCategory
     *
     * @param id     角色分类主键id
     * @param source 角色分类
     * @return 成功true 失败false
     */
    public boolean update(String id, UmcRoleCategoryDto source) {

        UmcRoleCategory exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("记录不存在");
        }
        if (StringUtils.isNotEmpty(source.getCtgryCode())) {
            exist.setCtgryCode(source.getCtgryCode());
        }
        if (StringUtils.isNotEmpty(source.getCtgryName())) {
            exist.setCtgryName(source.getCtgryName());
        }
        if (StringUtils.isNotEmpty(source.getRemarks())) {
            exist.setRemarks(source.getRemarks());
        }
        exist.setUpdateUser(UmcUserUtils.getCurrentUserName());
        repository.save(exist);

        return true;

    }

    /**
     * 根据ID查询UmcRoleCategory
     *
     * @param id 角色分类主键id
     * @return 角色分类信息
     */
    public UmcRoleCategoryDto detail(String id) {

        UmcRoleCategory exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("角色类别记录不存在");
        }

        UmcRoleCategoryDto target = new UmcRoleCategoryDto();
        String[] excludeStr = new String[]{"umcRoles", "umcSystem"};
        BeanUtils.copyProperties(exist, target, excludeStr);

        return target;

    }

    public UmcRoleCategory detailEntity(String id) {

        UmcRoleCategory exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("记录不存在");
        }

        return exist;

    }

    /**
     * 分页查询查询UmcRoleCategory
     *
     * @param pageNo   页码
     * @param pageSize 页面大小
     * @param paramMap 查询条件
     * @return 成功true 失败false
     */
    public Page<UmcRoleCategoryDto> page(Integer pageNo, Integer pageSize, Map<String, String> paramMap) {

//        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
//        Page<UmcRoleCategory> data = repository.findAll(new UmcRoleCategorySpec(paramMap), pageable);
//
//        List<UmcRoleCategoryDto> list = new ArrayList<>();
//        for (UmcRoleCategory obj : data.getContent()) {
//            UmcRoleCategoryDto dto = new UmcRoleCategoryDto();
//            BeanUtils.copyProperties(obj, dto);
//            list.add(dto);
//        }
//
//        return new PageImpl<>(list, pageable, data.getTotalElements());
        return null;

    }

    public List<UmcRoleCategory> findByUmcSystemBySysId(String sysId) {
        return repository.findByUmcSystem_SysId(sysId);
    }

    public List<UmcRoleCategory> findByParentId(String parentId) {
        return repository.findByParentId(parentId);
    }

    List<TreeNode> findAllRoleCategoryList(String parentId) {
        return repository.findAllRoleCategoryList(parentId);
    }
}