package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.entity.UmcSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcSystem Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcSystemRepository extends JpaRepository<UmcSystem, String>, JpaSpecificationExecutor<UmcSystem> {

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(s.sysId, s.sysName,s.sysCode,'SYSTEM', (s.umcResources.size=0) ) from UmcSystem s where s.isDeleted = false")
    List<TreeNode> findAllSystemNodeOfResList();

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(s.sysId, s.sysName,s.sysCode,'SYSTEM', (s.umcRoleCategories.size=0) ) from UmcSystem s where s.isDeleted = false")
    List<TreeNode> findAllSystemNodeOfRoleList();

    UmcSystem findBySysCode(String sysCode);

}