package com.ey.cn.fssc.umc.service;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.fssc.umc.dto.FullTreeNode;
import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.dto.UmcResourcesDto;
import com.ey.cn.fssc.umc.dto.UmcSystemDto;
import com.ey.cn.fssc.umc.entity.UmcResources;
import com.ey.cn.fssc.umc.entity.UmcSystem;
import com.ey.cn.fssc.umc.enums.NodeType;
import com.ey.cn.fssc.umc.enums.ResTtpe;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcResourcesRepository;
import com.ey.cn.fssc.umc.spec.UmcResourcesSpec;
import com.ey.cn.fssc.umc.util.UmcUserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：UmcResources Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcResourcesService {

    @Autowired
    private UmcResourcesRepository repository;

    @Autowired
    private UmcSystemService umcSystemService;

    /**
     * 创建UmcResources
     *
     * @param source 资源dto
     * @return 成功true 失败false
     */
    public boolean create(UmcResourcesDto source) {

        UmcResources target = new UmcResources();
        BeanUtils.copyProperties(source, target);
        UmcSystem umcSystemBySysId = new UmcSystem();
        try {
            UmcResources parentRes = this.detailEntity(source.getParentId());
            umcSystemBySysId = parentRes.getUmcSystem();
        } catch (Exception e) {
            umcSystemBySysId.setSysId(source.getParentId());
        }

        target.setUmcSystem(umcSystemBySysId);
        target.setCreateUser(UmcUserUtils.getCurrentUserName());
        target.setUpdateUser(UmcUserUtils.getCurrentUserName());
        target.setIsDeleted(false);
        target.setResType(source.getResType().toString());
        repository.save(target);
        return true;

    }

    public boolean save(UmcResourcesDto source) {

        if (StringUtils.isNotEmpty(source.getResId())) {
            return this.update(source.getResId(), source);
        } else {
            return this.create(source);
        }
    }

    /**
     * 根据ID删除UmcResources
     *
     * @param id 资源主键id
     * @return 成功true 失败false
     */
    public Result<Boolean> delete(String id) {

        //检查资源下面是否挂有子节点，存在就不能删除
        List<UmcResources> resources = repository.findByParentId(id);
        if (resources != null && resources.size() > 0) {
            throw new BizException("资源下面挂有子节点，不能删除");
        }
        UmcResources umcResource = repository.findById(id).get();
        if (umcResource.getUmcRoleResources() != null && umcResource.getUmcRoleResources().size() > 0) {
            throw new BizException("资源存在关联的角色，不能删除");
        }
        repository.deleteById(id);
        return Result.ok(true);

    }

    /**
     * 更新UmcResources
     *
     * @param id     资源主键id
     * @param source 资源dto
     * @return 成功true 失败false
     */
    public boolean update(String id, UmcResourcesDto source) {

        UmcResources exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("资源记录不存在");
        }
        if (StringUtils.isNotEmpty(source.getResName())) {
            exist.setResName(source.getResName());
        }
        if (StringUtils.isNotEmpty(source.getIcon())) {
            exist.setIcon(source.getIcon());
        }
        if (StringUtils.isNotEmpty(source.getMenuUrl())) {
            exist.setMenuUrl(source.getMenuUrl());
        }
        if (StringUtils.isNotEmpty(source.getPathCode())) {
            exist.setPathCode(source.getPathCode());
        }
        if (StringUtils.isNotEmpty(source.getRemarks())) {
            exist.setRemarks(source.getRemarks());
        }
        if (StringUtils.isNotEmpty(source.getResType().toString())) {
            exist.setResType(source.getResType().toString());
        }
        if (null != source.getOrderNum() && source.getOrderNum() != 0) {
            exist.setOrderNum(source.getOrderNum());
        }
        UmcSystem umcSystemBySysId = new UmcSystem();
        //把父节点做为资源id查资源信息，判断是否为资源根节点
        UmcResources parentRes = repository.findById(source.getParentId()).orElse(null);
        if (null == parentRes) {
            //是根节点，设置根节点的系统字段
            umcSystemBySysId.setSysId(source.getParentId());
        } else {
            //不是根节点，当前资源的系统取根节点的系统信息
            umcSystemBySysId = parentRes.getUmcSystem();
        }
        exist.setUmcSystem(umcSystemBySysId);
        exist.setUpdateUser(UmcUserUtils.getCurrentUserName());
        exist.setResType(source.getResType().toString());
        repository.save(exist);
        return true;

    }

    /**
     * 根据ID查询UmcResources
     *
     * @param id 资源id
     * @return 成功true 失败false
     */
    public UmcResourcesDto detail(String id) {

        UmcResources exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("资源记录不存在");
        }
        UmcResourcesDto target = new UmcResourcesDto();
        String[] excludeStr = new String[]{"umcSystem", "umcRoleResources"};
        BeanUtils.copyProperties(exist, target, excludeStr);
        target.setResType(ResTtpe.valueOf(exist.getResType()));
        return target;

    }

    public UmcResources detailEntity(String id) {

        UmcResources exist = repository.findById(id).orElse(null);
        if (null == exist) {
            throw new BizException("资源记录不存在");
        }
        return exist;

    }

    /**
     * 分页查询查询UmcResources
     *
     * @param pageNo   页码
     * @param pageSize 页面大小
     * @param paramMap 查询条件
     * @return 分页查询结果
     */
    public Page<UmcResourcesDto> page(Integer pageNo, Integer pageSize, Map<String, String> paramMap) {

//        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
//        Page<UmcResources> data = repository.findAll(new UmcResourcesSpec(paramMap), pageable);
//
//        List<UmcResourcesDto> list = new ArrayList<>();
//        for (UmcResources obj : data.getContent()) {
//            UmcResourcesDto dto = new UmcResourcesDto();
//            BeanUtils.copyProperties(obj, dto);
//            UmcSystemDto systemDto = new UmcSystemDto();
//            BeanUtils.copyProperties(obj.getUmcSystem(), systemDto);
//            dto.setSystemDto(systemDto);
//            list.add(dto);
//        }

//        return new PageImpl<>(list, pageable, data.getTotalElements());
        return null;
    }

    /**
     * 通过角色id查询资源列表
     *
     * @param roleId 角色id
     * @return 资源列表
     */
    public List<UmcResourcesDto> findResByRole(String roleId) {
        List<UmcResourcesDto> resourcesDtos = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("roleId", roleId);
        UmcResourcesSpec specification = new UmcResourcesSpec(params);
        List<UmcResources> umcResources = repository.findAll(specification);
        for (UmcResources resources :
                umcResources) {
            UmcResourcesDto resourcesDto = new UmcResourcesDto();
            String[] excludeStr = new String[]{"umcRoleResources"};
            BeanUtils.copyProperties(resources, resourcesDto, excludeStr);
            UmcSystemDto systemDto = new UmcSystemDto();
            BeanUtils.copyProperties(resources.getUmcSystem(), systemDto);
            resourcesDto.setSystemDto(systemDto);
            resourcesDtos.add(resourcesDto);
        }
        return resourcesDtos;
    }

    public List<TreeNode> queryResourceTree(NodeType strcNodeType, String parentId) {
        List<TreeNode> treeNode = new ArrayList<>();
        switch (strcNodeType) {
            case SYSTEM:
                treeNode = umcSystemService.findAllSystemNodeOfResList();
                break;
            case RESOURCE:
                treeNode = this.getResourceList(parentId);
                break;
            default:
                break;
        }

        return treeNode;
    }

    private List<TreeNode> getResourceList(String parentId) {

        return repository.findByResourcesNodeList(parentId);
    }

    /**
     * 查询系统对应所有资源的树形结构
     *
     * @param sysId 系统id
     * @return 资源树形结构
     */
    public List<FullTreeNode> getFullResourceList(String sysId) {
        List<FullTreeNode> treeNodes = new ArrayList<>();
        //系统对应的所有资源列表
        List<UmcResources> umcResources = repository.findByUmcSystem_SysId(sysId);
        if (null != umcResources) {
            //列表转换为树形结构转换
            treeNodes = this.listToTree(umcResources);
        }
        return treeNodes;
    }

    public List<UmcResources> findBySysId(String sysId) {
        return repository.findByUmcSystem_SysId(sysId);
    }

    /**
     * 资源列表转为树形结构
     *
     * @param source 资源列表
     * @return 树形结构
     */
    private List<FullTreeNode> listToTree(List<UmcResources> source) {
        List<FullTreeNode> treeNodes = new ArrayList<>();
        //列表转换为树形结构转换
        for (int i = 0; i < source.size(); i++) {
            UmcResources umcResource = source.get(i);
            if (umcResource.getParentId().equals(umcResource.getUmcSystem().getSysId())) {
                FullTreeNode root = new FullTreeNode();
                //资源类型转为树节点类型，并查询设置下级节点
                this.resourceToFullTreeNode(root, umcResource, source);
                treeNodes.add(root);
            }
        }
        return treeNodes;
    }

    /**
     * 递归设置节点的子节点，直到找到叶子节点为止
     *
     * @param parentNode 父节点
     * @param source     资源列表
     */
    private void setChildNode(FullTreeNode parentNode, List<UmcResources> source) {
        List<FullTreeNode> children = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            UmcResources umcResource = source.get(i);
            if (parentNode.getId().equals(umcResource.getParentId())) {
                FullTreeNode child = new FullTreeNode();
                this.resourceToFullTreeNode(child, umcResource, source);
                children.add(child);
            }
        }
        if (children.size() == 0) {
            parentNode.setIsLeaf(true);
        } else {
            parentNode.setIsLeaf(false);
        }
        parentNode.setChildren(children);
    }

    /**
     * 资源类型转为树节点类型，并设置树节点的子节点
     *
     * @param node        树节点
     * @param umcResource 资源
     * @param source      资源列表
     */
    private void resourceToFullTreeNode(FullTreeNode node, UmcResources umcResource, List<UmcResources> source) {
        node.setCode(umcResource.getPathCode());
        node.setId(umcResource.getResId());
        node.setName(umcResource.getResName());
        node.setType(NodeType.RESOURCE.name());
        //查询并设置子节点
        this.setChildNode(node, source);
    }

}