package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.entity.UmcResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcResources Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcResourcesRepository extends JpaRepository<UmcResources, String>, JpaSpecificationExecutor<UmcResources> {

    List<UmcResources> findByParentId(String parentId);

    List<UmcResources> findByUmcSystem_SysId(String sysId);

    UmcResources findByPathCode(String code);

    @Query("select new com.ey.cn.fssc.umc.dto.TreeNode(r.resId, r.resName,r.pathCode,'RESOURCE', ((select count(ur.resId) from UmcResources ur where ur.parentId = r.resId and ur.isDeleted = false) = 0) ) from UmcResources r where r.isDeleted = false and r.parentId=?1")
    List<TreeNode> findByResourcesNodeList(String parentId);
}