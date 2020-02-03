package com.ey.cn.fssc.umc.service;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.pi.cc.common.oauth.SecurityUser;
import com.ey.cn.pi.cc.common.utils.UserUtils;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.*;
import com.ey.cn.fssc.umc.entity.*;
import com.ey.cn.fssc.umc.enums.NodeType;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcAccountRoleRepository;
import com.ey.cn.fssc.umc.repository.UmcRoleRepository;
import com.ey.cn.fssc.umc.util.UmcUserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：UmcRole Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcRoleService {

    @Autowired
    private UmcRoleRepository repository;

    @Autowired
    private UmcSystemService umcSystemService;

    @Autowired
    private UmcRoleCategoryService umcRoleCategoryService;

    @Autowired
    private UmcAccountRoleService umcAccountRoleService;

    @Autowired
    private UmcAccountRoleRepository umcAccountRoleRepository;

    @Autowired
    private UmcResourcesService umcResourcesService;
    @Autowired
    private UmcAccountService accountService;
    @Autowired
    private UmcRoleResourcesService roleResourcesService;

    public List<TreeNode> querySysRoleTree(NodeType strcNodeType, String parentId) {
        List<TreeNode> treeNode = new ArrayList<>();
        switch (strcNodeType) {
            case SYSTEM:
                treeNode = umcSystemService.findAllSystemNodeOfRoleList();
                break;
            case ROLECATE:
                treeNode = umcRoleCategoryService.findAllRoleCategoryList(parentId);
                break;
            case ROLE:
                treeNode = this.getRoleList(parentId);
                break;
            case ROLEACCT:
                treeNode = this.getRoleAccountList(parentId);
                break;
            default:
                break;
        }
        return treeNode;
    }

    /**
     * 获得角色分类对应的所有角色
     *
     * @return 角色树形列表
     */
    private List<TreeNode> getRoleList(String parentId) {
        return repository.findAllRoleList(parentId);
    }

    /**
     * 获得角色对应的所有账号
     *
     * @return 账号树形列表
     */
    private List<TreeNode> getRoleAccountList(String parentId) {
        return umcAccountRoleService.findAllAccountList(parentId);
    }

    public boolean save(UmcRoleDto source) {
        if (StringUtils.isNotEmpty(source.getRoleId())) {
            return this.update(source.getRoleId(), source);
        } else {
            return this.create(source);
        }
    }

    /**
     * 创建UmcRole
     *
     * @param source 角色dto
     * @return 成功true  失败false
     */
    public boolean create(UmcRoleDto source) {
        UmcRole target = new UmcRole();
        BeanUtils.copyProperties(source, target);
        //角色分类
        UmcRoleCategory umcRoleCategoryByCtgryId = umcRoleCategoryService.detailEntity(source.getCtgryId());
        if (umcRoleCategoryByCtgryId == null) {
            throw new BizException("角色分类不存在");
        }
        target.setUmcRoleCategory(umcRoleCategoryByCtgryId);
        target.setUmcSystem(umcRoleCategoryByCtgryId.getUmcSystem());
        target.setIsDeleted(false);
        target.setCreateUser(UmcUserUtils.getCurrentUserName());
        target.setIsLeaf(true);
        target.setParentId(source.getCtgryId());
        repository.save(target);
        return true;

    }

    /**
     * 根据ID删除UmcRole
     *
     * @param id 角色主键id
     * @return 成功true  失败false
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(String id) {
        UmcRole role = repository.findById(id).get();
        if (!role.getUmcAccountRoles().isEmpty()) {
            throw new BizException("角色下面挂有账号，不能删除");
        }
        List<UmcRole> roleChildren = repository.findByParentId(id);
        if (roleChildren != null && roleChildren.size() > 0) {
            throw new BizException("角色下面挂有子节点，不能删除");
        }
        roleResourcesService.delete(id);
        repository.deleteById(id);
        return Result.ok(true);
    }

    /**
     * 更新UmcRole
     *
     * @param id     角色主键id
     * @param source 角色dto
     * @return 成功true  失败false
     */
    public boolean update(String id, UmcRoleDto source) {
        UmcRole exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("角色记录不存在");
        }
        /*
         * 更新字段
         */
        //系统
        if (StringUtils.isNotEmpty(source.getSysId())) {
            UmcSystem umcSystemBySysId = umcSystemService.detailEntity(source.getSysId());
            if (umcSystemBySysId == null) {
                throw new BizException("系统不存在");
            }
            exist.setUmcSystem(umcSystemBySysId);
        }
        //角色分类
        if (StringUtils.isNotEmpty(source.getCtgryId())) {
            UmcRoleCategory umcRoleCategoryByCtgryId = umcRoleCategoryService.detailEntity(source.getCtgryId());
            if (umcRoleCategoryByCtgryId == null) {
                throw new BizException("角色分类不存在");
            }
            exist.setUmcRoleCategory(umcRoleCategoryByCtgryId);
        }
        if (StringUtils.isNotEmpty(source.getRoleCode())) {
            exist.setRoleCode(source.getRoleCode());
        }
        if (StringUtils.isNotEmpty(source.getRoleName())) {
            exist.setRoleName(source.getRoleName());
        }
        if (StringUtils.isNotEmpty(source.getRemarks())) {
            exist.setRemarks(source.getRemarks());
        }
        repository.save(exist);
        return true;
    }

    /**
     * 根据ID查询UmcRole
     *
     * @param id 角色主键id
     * @return 成功true  失败false
     */
    public UmcRoleDto detail(String id) {
        UmcRole exist = repository.findById(id).get();
        if (null == exist) {
            throw new BizException("角色记录不存在");
        }
        UmcRoleDto target = new UmcRoleDto();
        String[] excludeStr = new String[]{"umcAccountRoles", "umcRoleCategory", "umcRoleResources"};
        BeanUtils.copyProperties(exist, target, excludeStr);
        target.setSysId(exist.getUmcSystem().getSysId());
        target.setCtgryId(exist.getUmcRoleCategory().getCtgryId());
        target.setCtgryName(exist.getUmcRoleCategory().getCtgryName());
        return target;
    }

    /**
     * 根据ID查询UmcRole
     *
     * @param ids 角色主键id
     * @return 成功true  失败false
     */
    public List<RoleDto> queryByIds(List<String> ids) {
        List<UmcRole> umcRoles = repository.findAllById(ids);
        if (CollectionUtils.isEmpty(umcRoles)) {
            throw new BizException("角色记录不存在");
        }
        List<RoleDto> roleDtos = new ArrayList<>();
        for(UmcRole role : umcRoles){
            RoleDto dto = new RoleDto();
            BeanUtils.copyProperties(role, dto);
            roleDtos.add(dto);
        }
        return roleDtos;
    }

    /**
     * 分页查询查询UmcRole
     *
     * @param pageNo   页码
     * @param pageSize 页面大小
     * @param paramMap 查询条件
     * @return 角色分页结果
     */
    public Page<UmcRoleDto> page(Integer pageNo, Integer pageSize, Map<String, String> paramMap) {
//        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
//        Page<UmcRole> data = repository.findAll(new UmcRoleSpec(paramMap), pageable);
//        List<UmcRoleDto> list = new ArrayList<>();
//        for (UmcRole obj : data.getContent()) {
//            UmcRoleDto dto = new UmcRoleDto();
//            BeanUtils.copyProperties(obj, dto);
//            list.add(dto);
//        }
//        return new PageImpl<>(list, pageable, data.getTotalElements());
        return null;
    }

    /**
     * 角色分类详情
     *
     * @param ctgryId 角色分类主键id
     * @return 角色分类
     */
    public UmcRoleCategoryDto detaiRctg(String ctgryId) {
        return umcRoleCategoryService.detail(ctgryId);
    }

    /**
     * 保存角色分类
     *
     * @param umcRoleCategoryDto 角色分类dto
     * @return 成功true  失败false
     */
    public boolean saveRctg(UmcRoleCategoryDto umcRoleCategoryDto) {
        if (StringUtils.isNotEmpty(umcRoleCategoryDto.getCtgryId())) {
            return umcRoleCategoryService.update(umcRoleCategoryDto.getCtgryId(), umcRoleCategoryDto);
        } else {
            return umcRoleCategoryService.create(umcRoleCategoryDto);
        }
    }

    /**
     * 删除角色分类
     *
     * @param id 角色分类主键id
     * @return 成功true  失败false
     */
    public Result<Boolean> deleteRctg(String id) {
        //判断角色分类下是否存在角色或者子节点，存在就不能删除
        List<UmcRole> roles = repository.findByUmcRoleCategory_CtgryId(id);
        if (roles != null && roles.size() > 0) {
            throw new BizException("角色分类下挂有角色，不能删除");
        }
        List<UmcRoleCategory> roleCategories = umcRoleCategoryService.findByParentId(id);
        if (roleCategories != null && roleCategories.size() > 0) {
            throw new BizException("角色分类下挂有子节点，不能删除");
        }
        umcRoleCategoryService.delete(id);
        return Result.ok(true);
    }

    /**
     * 系统详情
     *
     * @param sysId 系统主键id
     * @return 成功true  失败false
     */
    public UmcSystemDto detailSystem(String sysId) {
        return umcSystemService.detail(sysId);
    }

    /**
     * 保存系统信息
     *
     * @param umcSystemDto 系统dto
     * @return 成功true  失败false
     */
    public boolean saveSystem(UmcSystemDto umcSystemDto) {
        if (StringUtils.isNotEmpty(umcSystemDto.getSysId())) {
            return umcSystemService.update(umcSystemDto.getSysId(), umcSystemDto);
        } else {
            return umcSystemService.create(umcSystemDto);
        }
    }

    /**
     * 删除系统信息
     *
     * @param id 系统主键id
     * @return 成功true  失败false
     */
    public Result<Boolean> deleteSys(String id) {
        //查询系统是否挂有角色分类和资源，存在就不能删除
        List<UmcResources> res = umcResourcesService.findBySysId(id);
        if (res != null && res.size() > 0) {
            throw new BizException("系统下面挂有资源信息，不能删除");
        }
        List<UmcRoleCategory> roleCategories = umcRoleCategoryService.findByUmcSystemBySysId(id);
        if (roleCategories != null && roleCategories.size() > 0) {
            throw new BizException("系统下面挂有角色分类信息，不能删除");
        }
        umcSystemService.delete(id);
        return Result.ok(true);
    }

    /**
     * 批量保存角色对应的账号关联关系
     *
     * @param accountRoleDto 角色及账号列表类型
     * @return 成功true  失败false成功true  失败false
     */
    public boolean saveAccountRole(AccountRoleDto accountRoleDto) {
        //第一步：找到该角色下挂的所有账号
        List<String> acctIds = accountRoleDto.getAcctIds();
        List<UmcAccountRole> acctRolesOlds = umcAccountRoleService.findByUmcRoleByRoleId(accountRoleDto.getRoleId());

        //第二步：判断该角色关联的账号是否已经关联，已关联就不再重复新增
        for (UmcAccountRole acctRolesOld :
                acctRolesOlds) {
            String acctId = acctRolesOld.getUmcAccount().getAcctId();
            if (acctIds.contains(acctId)) {
                acctIds.remove(acctId);
            }
        }
        //第三步：批量新增角色账号关系
        List<UmcAccountRole> acctRoles = new ArrayList<>();
        UmcRole role = new UmcRole();
        role.setRoleId(accountRoleDto.getRoleId());
        if (!acctIds.isEmpty()) {
            for (String acctId :
                    acctIds) {
                UmcAccount account = new UmcAccount();
                account.setAcctId(acctId);
                UmcAccountRole acctRole = new UmcAccountRole();
                acctRole.setUmcRole(role);
                acctRole.setUmcAccount(account);
                acctRoles.add(acctRole);
            }
            umcAccountRoleService.batch(acctRoles);
        }
        return true;
    }

    /**
     * 删除角色账号关联关系
     *
     * @param roleId 角色主键id
     * @param acctId 账号主键id
     * @return 成功true  失败false
     */
    public boolean deleteRoleAcct(String roleId, String acctId) {
        umcAccountRoleService.deleteRoleAcct(roleId, acctId);
        return true;
    }

    /**
     * 通过系统得到角色分类维度下的整棵角色树
     *
     * @param sysId 系统id
     * @return 角色树
     */
    public List<FullTreeNode> queryFullRoleTree(String sysId) {
        List<FullTreeNode> treeNodes = new ArrayList<>();
        //第一步：查询角色分类列表
        List<UmcRoleCategory> roleCategories = umcRoleCategoryService.findByUmcSystemBySysId(sysId);
        for (UmcRoleCategory roleCategorie :
                roleCategories) {
            //第二步：查询角色分类下的角色列表
            List<UmcRole> roles = repository.findByUmcRoleCategory_CtgryId(roleCategorie.getCtgryId());
            List<UmcRole> newRoles = new ArrayList<>();
            FullTreeNode treeNode = new FullTreeNode();
            if (roles != null && roles.size() > 0) {
                for (UmcRole role : roles) {
                    if ("ADMIN".equalsIgnoreCase(role.getRoleCode()) || ("GACB_FINC_QUERY".equalsIgnoreCase(role.getRoleCode()))) {
                        if (isOwnedAuthCode(Constant.AuthCode.DATA_SYS_ACCOUNT_ADMIN)) {
                            newRoles.add(role);
                        }
                    } else {
                        newRoles.add(role);
                    }
                }

                //第三步：将角色分类和角色转换为树形结构
                setRoleTree(roleCategorie, newRoles, treeNode);
                treeNodes.add(treeNode);
            }
        }
        return treeNodes;
    }

    private boolean isOwnedAuthCode(String code) {
        for (String authCode : UserUtils.getSecurityUser().getAuthorityList()) {
            if (code.equals(authCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建角色分类与角色树
     *
     * @param roleCategorie 角色分类
     * @param roles         角色
     * @param treeNode      树节点
     */
    private void setRoleTree(UmcRoleCategory roleCategorie, List<UmcRole> roles, FullTreeNode treeNode) {
        treeNode.setId(roleCategorie.getCtgryId());
        treeNode.setName(roleCategorie.getCtgryName());
        treeNode.setType(NodeType.ROLECATE.getTypeId());
        treeNode.setCode(roleCategorie.getCtgryCode());
        treeNode.setIsLeaf(false);
        treeNode.setChildren(roles.stream().map((x) -> new FullTreeNode(x.getRoleId(), x.getRoleName(), x.getRoleCode(), NodeType.ROLE.toString(), true)).collect(Collectors.toList()));
    }

    public List<SecurityUser> listUserListByRoleIdAndShopCd(String roleId, String shopCd) {
        List<UmcAccountRole> accountRoles = umcAccountRoleService.findByUmcRoleByRoleId(roleId);
        List<String> accounts = accountRoles.stream().map(UmcAccountRole::getUmcAccount).map(UmcAccount::getAccount).collect(Collectors.toList());
        List<SecurityUser> result = accountService.listUserByAccounts(accounts, shopCd);
        return result;

    }

    /**
     * 根据UserId获取角色列表
     *
     * @param userId
     * @return
     */
    public List<RoleDto> findRolesByUserId(String userId) {

        UmcAccount exist = accountService.findAccountByUserId(userId);
        if (null == exist) {
            throw new BizException("用户不存在");
        }

        //角色
        Collection<UmcAccountRole> umcAccountRoles = exist.getUmcAccountRoles();

        return umcAccountRoles.stream().map((x) -> new RoleDto(x.getUmcRole().getRoleId(), x.getUmcRole().getRoleCode(), x.getUmcRole().getRoleName())).collect(Collectors.toList());

    }

    /**
     * 根据角色编码查询用户ID集合
     *
     * @param roleCode
     * @return
     */
    public List<AccountUserDto> queryUserIdByRoleCode(String roleCode) {
        return umcAccountRoleRepository.findUsersByRoleCode(roleCode);
    }

}