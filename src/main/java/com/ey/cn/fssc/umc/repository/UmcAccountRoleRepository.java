package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.AccountUserDto;
import com.ey.cn.fssc.umc.dto.RoleDto;
import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.entity.UmcAccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcAccountRole Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcAccountRoleRepository extends JpaRepository<UmcAccountRole, String>, JpaSpecificationExecutor<UmcAccountRole> {

    List<UmcAccountRole> findByUmcRole_RoleId(String roleId);

    UmcAccountRole findByUmcRole_RoleIdAndUmcAccount_AcctId(String roleId, String acctId);

    int countByUmcRole_RoleIdAndUmcAccount_AcctId(String roleId, String acctId);

    void deleteByUmcAccount_AcctId(String acctId);

    void deleteByUmcRole_RoleId(String roleId);

    void deleteByUmcRole_RoleIdAndUmcAccount_AcctId(String roleId, String acctId);

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(a.acctId, concat(a.account,' ',a.userName), a.userCode , 'ROLEACCT', true ) from UmcAccountRole ar left join  ar.umcRole r  left join ar.umcAccount a  where ar.isDeleted = false and r.roleId = ?1")
    List<TreeNode> findAllAccountList(String parentId);
    @Query("select new com.ey.cn.fssc.umc.dto.RoleDto(ar.umcRole.roleId,ar.umcRole.roleCode,ar.umcRole.roleName)from UmcAccountRole ar  where ar.umcAccount.acctId = ?1 ")
    List<RoleDto> findRoleDtosByAccountId(String accountId);

    @Query("select new com.ey.cn.fssc.umc.dto.AccountUserDto(uar.umcAccount.userId, uar.umcAccount.acctId) from UmcAccountRole uar where uar.isDeleted = false and uar.umcRole.roleCode = ?1 and uar.umcAccount.isDeleted = false and uar.umcAccount.status = 'ENABLE' ")
    List<AccountUserDto> findUsersByRoleCode(String roleCode);
}