package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.RoleDto;
import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.dto.UmcAccountRoleDto;
import com.ey.cn.fssc.umc.entity.UmcAccount;
import com.ey.cn.fssc.umc.entity.UmcAccountRole;
import com.ey.cn.fssc.umc.entity.UmcRole;
import com.ey.cn.fssc.umc.repository.UmcAccountRoleRepository;
import com.ey.cn.fssc.umc.util.UmcUserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 描述：UmcAccountRole Service
 *
 * @author king
 * @date 2019-03-18
 */

@Service
@Transactional
public class UmcAccountRoleService {

    @Autowired
    private UmcAccountRoleRepository repository;

    public List<UmcAccountRole> findByUmcRoleByRoleId(String roleId) {
        return repository.findByUmcRole_RoleId(roleId);
    }

    public List<RoleDto> findRoleDtosByAccountId(String accountId) {
        return repository.findRoleDtosByAccountId(accountId);
    }

    /**
     * 创建UmcAccountRole
     *
     * @param source 账号角色关联关系对象
     * @return 成功true 失败false
     */
    public boolean create(UmcAccountRoleDto source) {

        /*
         * 判断账号角色是否已经关联，如果已关联就不再新增  此处用count计算数量，不需要查询整个对象
         */
        int umcAccountRoleCount = repository.countByUmcRole_RoleIdAndUmcAccount_AcctId(source.getRoleDto().getRoleId(), source.getAccountDto().getAcctId());
        if (umcAccountRoleCount > 0) {
            return true;
        }
        UmcAccountRole acctRole = new UmcAccountRole();
        UmcRole umcRole = new UmcRole();
        UmcAccount umcAccount = new UmcAccount();
        BeanUtils.copyProperties(source.getRoleDto(), umcRole);
        BeanUtils.copyProperties(source.getAccountDto(), umcAccount);
        acctRole.setUmcAccount(umcAccount);
        acctRole.setUmcRole(umcRole);
        acctRole.setCreateUser(UmcUserUtils.getCurrentUserName());
        acctRole.setIsDeleted(false);
        repository.save(acctRole);
        return true;
    }

    /**
     * 账号角色批量插入
     *
     * @param targets 账号角色列表
     * @return 成功true 失败false
     */
    public boolean batch(List<UmcAccountRole> targets) {
        repository.saveAll(targets);
        return true;
    }

    /**
     * 根据ID删除UmcAccountRole
     *
     * @param id 账号角色关联关系id
     * @return 成功true 失败false
     */
    public boolean delete(String id) {

        repository.deleteById(id);
        return true;
    }

    public boolean deleteRoleAcct(String roleId, String acctId) {

        repository.deleteByUmcRole_RoleIdAndUmcAccount_AcctId(roleId, acctId);
        return true;
    }

    /**
     * 通过账号删除账号角色关联关系
     *
     * @param acctId 账号id
     * @return 成功true 失败false
     */
    public boolean deleteByAcctId(String acctId) {

        repository.deleteByUmcAccount_AcctId(acctId);
        return true;
    }

    /**
     * 更新UmcAccountRole
     *
     * @param id     账号角色关联关系主键id
     * @param source 账号角色关联关系对象
     * @return 成功true 失败false
     */
    public boolean update(String id, UmcAccountRoleDto source) {

        UmcAccountRole exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("记录不存在");
        }
        String[] excludeStr = new String[]{"id"};
        BeanUtils.copyProperties(source, exist, excludeStr);
        repository.save(exist);
        return true;
    }

    /**
     * 根据ID查询UmcAccountRole
     *
     * @param id 账号角色关联关系id
     * @return 账号角色关联关系对象
     */
    public UmcAccountRoleDto detail(String id) {

        UmcAccountRole exist = repository.findById(id).get();
        if (null == exist) {
            throw new RuntimeException("记录不存在");
        }
        UmcAccountRoleDto target = new UmcAccountRoleDto();
        BeanUtils.copyProperties(exist, target);
        return target;
    }

    /**
     * 分页查询查询UmcAccountRole
     *
     * @param pageNo   页码
     * @param pageSize 页面大小
     * @param paramMap 查询条件
     * @return 分页结果
     */
    public Page<UmcAccountRoleDto> page(Integer pageNo, Integer pageSize, Map<String, String> paramMap) {

//        Pageable pageable = new PageRequest(pageNo - 1, pageSize);
//        Page<UmcAccountRole> data = repository.findAll(new UmcAccountRoleSpec(paramMap), pageable);
//        List<UmcAccountRoleDto> list = new ArrayList<>();
//        for (UmcAccountRole obj : data.getContent()) {
//            UmcAccountRoleDto dto = new UmcAccountRoleDto();
//            BeanUtils.copyProperties(obj, dto);
//            list.add(dto);
//        }
//        return new PageImpl<>(list, pageable, data.getTotalElements());
        return null;
    }

    List<TreeNode> findAllAccountList(String parentId) {
        return repository.findAllAccountList(parentId);
    }
}