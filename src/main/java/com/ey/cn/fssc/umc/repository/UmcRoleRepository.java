package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.entity.UmcRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcRole Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcRoleRepository extends JpaRepository<UmcRole, String>, JpaSpecificationExecutor<UmcRole> {

    List<UmcRole> findByUmcRoleCategory_CtgryId(String ctgryId);

    UmcRole findByRoleCode(String roleCode);

    List<UmcRole> findByParentId(String parentId);

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(u.roleId, u.roleName, u.roleCode, 'ROLE', (u.umcAccountRoles.size=0) ) from UmcRole u where u.isDeleted = false and u.parentId = ?1")
    List<TreeNode> findAllRoleList(String parentId);

}