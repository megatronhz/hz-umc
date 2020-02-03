package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.entity.UmcRoleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcRoleCategory Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcRoleCategoryRepository extends JpaRepository<UmcRoleCategory, String>, JpaSpecificationExecutor<UmcRoleCategory> {

    List<UmcRoleCategory> findByUmcSystem_SysId(String sysId);

    List<UmcRoleCategory> findByParentId(String parentId);

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(u.ctgryId, u.ctgryName, u.ctgryCode, 'ROLECATE', (u.umcRoles.size=0) ) from UmcRoleCategory u where u.isDeleted = false and u.parentId = ?1")
    List<TreeNode> findAllRoleCategoryList(String parentId);

    UmcRoleCategory findByCtgryCodeAndUmcSystem_SysCode(String code, String sysCode);
}