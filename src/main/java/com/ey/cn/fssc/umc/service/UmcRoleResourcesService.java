package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.*;
import com.ey.cn.fssc.umc.entity.UmcAccount;
import com.ey.cn.fssc.umc.entity.UmcResources;
import com.ey.cn.fssc.umc.entity.UmcRole;
import com.ey.cn.fssc.umc.entity.UmcRoleResources;
import com.ey.cn.fssc.umc.repository.UmcRoleResourcesRepository;
import com.ey.cn.fssc.umc.req.RoleResourcePageDto;
import com.ey.cn.fssc.umc.req.RoleResourcesPageResDto;
import com.ey.cn.fssc.umc.util.UmcUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：UmcRoleResources Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcRoleResourcesService {

    @Autowired
    private UmcRoleResourcesRepository repository;

    @Autowired
    private UmcResourcesService umcResourcesService;

    @Autowired
    private UmcRoleService umcRoleService;

    public boolean save(RoleResourcesDto source) {

        //第一步：删除所有角色对应的资源关系信息
        repository.deleteByUmcRole_RoleId(source.getRoleId());
        //第二步：批量新增角色资源关系
        List<UmcRoleResources> roleResources = new ArrayList<>();
        List<String> resIds = source.getResIds();
        UmcRole umcRole = new UmcRole();
        umcRole.setRoleId(source.getRoleId());
        for (String resId :
                resIds) {
            UmcResources umcResources = new UmcResources();
            umcResources.setResId(resId);
            UmcRoleResources umcRoleResource = new UmcRoleResources();
            umcRoleResource.setUmcRole(umcRole);
            umcRoleResource.setUmcResources(umcResources);
            roleResources.add(umcRoleResource);
        }
        return this.batch(roleResources);

    }

    public boolean batch(List<UmcRoleResources> roleResources) {
        repository.saveAll(roleResources);
        return true;
    }

    /**
     * 根据角色Id删除UmcRoleResources
     *
     * @param roleId 角色主键id
     * @return 成功true  失败false
     */
    public boolean delete(String roleId) {
        repository.deleteByUmcRole_RoleId(roleId);
        return true;

    }

    /**
     * 更新UmcRoleResources
     *
     * @param id     角色资源中间表主键id
     * @param source 角色资源dto
     * @return 成功true  失败false
     */
    public boolean update(String id, UmcRoleResourcesDto source) {

        UmcRoleResources exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("记录不存在");
        }
        String[] excludeStr = new String[]{"id"};
        BeanUtils.copyProperties(source, exist, excludeStr);
        repository.save(exist);

        return true;

    }

    /**
     * 根据ID查询UmcRoleResources
     *
     * @param roleId 角色Id
     * @return 角色资源关联关系dto
     */
    public UmcRoleResourcesDto detail(String roleId) {

        UmcRoleResourcesDto roleRes = new UmcRoleResourcesDto();
        //角色
        UmcRoleDto roleDto = umcRoleService.detail(roleId);
        roleRes.setRoleDto(roleDto);
        //角色关联的所有资源
        List<UmcResourcesDto> resourcesDtos = umcResourcesService.findResByRole(roleId);
        //查询系统所有资源的整颗树结构
        List<FullTreeNode> fullTreeNodes = umcResourcesService.getFullResourceList(roleDto.getSysId());
        //获得当前角色包含的资源树
        List<FullTreeNode> exist = new ArrayList<>();
        this.setTreeNode(resourcesDtos, fullTreeNodes, exist);
        roleRes.setResTreeNode(exist);
        return roleRes;
    }

    /**
     * 通过资源列表构造树
     *
     * @param resourcesDtos  资源列表
     * @param fullTreeNodes  资源整棵树
     * @param existTreeNodes 最终构造出的资源列表树结构
     */
    private void setTreeNode(List<UmcResourcesDto> resourcesDtos, List<FullTreeNode> fullTreeNodes, List<FullTreeNode> existTreeNodes) {

        for (int i = 0; i < fullTreeNodes.size(); i++) {
            FullTreeNode resourcesDto = fullTreeNodes.get(i);
            //判断树节点是否是在查询的资源列表中
            if (this.nodeIsExistList(resourcesDto.getId(), resourcesDtos) && resourcesDto.getIsLeaf()) {
                //是，就添加到最终的资源列表中
                resourcesDto.setChildren(null);
                existTreeNodes.add(resourcesDto);
                continue;
            }
            if (resourcesDto.getChildren() != null && resourcesDto.getChildren().size() > 0) {
                //去查询当前树节点的子节点是否包含资源列表中的数据
                List<FullTreeNode> childs = this.getTreeChilds(resourcesDtos, resourcesDto.getChildren());
                if (childs != null && childs.size() > 0) {
                    //找到存在的子节点，就把子节点添加到根节点上，构造树结构
                    resourcesDto.setChildren(childs);
                    existTreeNodes.add(resourcesDto);
                }
            }
        }
    }

    /**
     * 递归查询子树中是否包含资源列表中的数据
     *
     * @param resourcesDtos  资源列表
     * @param childTreeNodes 树
     * @return 返回构造的子树
     */
    private List<FullTreeNode> getTreeChilds(List<UmcResourcesDto> resourcesDtos, List<FullTreeNode> childTreeNodes) {
        List<FullTreeNode> existChilds = new ArrayList<>();
        if (childTreeNodes == null || childTreeNodes.size() <= 0) {
            return null;
        }
        for (FullTreeNode child :
                childTreeNodes) {
            if (this.nodeIsExistList(child.getId(), resourcesDtos) && child.getIsLeaf()) {
                //当前节点是要找的节点，返回该节点，并去掉子树
                child.setChildren(null);
                existChilds.add(child);
            } else {
                List<FullTreeNode> childOfchilds = this.getTreeChilds(resourcesDtos, child.getChildren());
                if (childOfchilds != null && childOfchilds.size() > 0) {
                    child.setChildren(childOfchilds);
                    existChilds.add(child);
                }
            }
        }
        return existChilds;
    }

    /**
     * 判断节点id是否存在在资源列表中
     *
     * @param nodeId        节点ID(资源ID)
     * @param resourcesDtos 资源列表
     * @return 存在true, 不存在false
     */
    private boolean nodeIsExistList(String nodeId, List<UmcResourcesDto> resourcesDtos) {
        for (UmcResourcesDto resourcesDto :
                resourcesDtos) {
            if (nodeId.equals(resourcesDto.getResId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 分页查询查询UmcRoleResources
     *
     * @param roleResourcePageDto 分页查询条件
     * @return 分页查询结果
     */
    public Page<RoleResourcesPageResDto> page(RoleResourcePageDto roleResourcePageDto) {

        Pageable pageable = PageRequest.of(roleResourcePageDto.getPage() - 1, roleResourcePageDto.getLimit());
        List<Map<String, String>> mapReault = repository.page(UmcUtils.likeStr(roleResourcePageDto.getRoleName()), UmcUtils.nulltoStr(roleResourcePageDto.getSysId()),
                (roleResourcePageDto.getPage() - 1) * roleResourcePageDto.getLimit(), roleResourcePageDto.getLimit());
        List<RoleResourcesPageResDto> list = mapReault.stream().map((x) -> new RoleResourcesPageResDto(x.get("roleId"), x.get("roleName"), x.get("sysId"), x.get("sysName"), x.get("resName"))).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, repository.countByQuery(UmcUtils.likeStr(roleResourcePageDto.getRoleName()), UmcUtils.nulltoStr(roleResourcePageDto.getSysId())));
    }

    /**
     * 获得系统下的资源整棵树结构
     *
     * @param sysId 系统id
     * @return 资源树结构
     */
    public List<FullTreeNode> queryFullResourceTree(String sysId) {

        return umcResourcesService.getFullResourceList(sysId);
    }

    /**
     * 根据账号获取对应资源权限
     *
     * @param account
     * @return
     */
    public List<String> getResourcesListByAccount(UmcAccount account) {
        List<String> rs = new ArrayList<>();
        // 查询对应角色
        account.getUmcAccountRoles().stream().map(
                ar -> ar.getUmcRole().getUmcRoleResources().stream().map(
                        rr -> rr.getUmcResources().getPathCode()).collect(Collectors.toList())).forEach(r -> rs.addAll(r));
        return rs;
    }

}