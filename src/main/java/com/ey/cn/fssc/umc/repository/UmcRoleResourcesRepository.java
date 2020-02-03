package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcRoleResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* 描述：UmcRoleResources Repository
* @author king
* @date 2019-03-18
*/

@Repository
public interface UmcRoleResourcesRepository extends JpaRepository<UmcRoleResources, String>, JpaSpecificationExecutor<UmcRoleResources> {

    void deleteByUmcRole_RoleId(String roleId);

    //    @Query(value =
//            "select ur.roleId, ur.roleName, ue.sysId, ue.sysName, group_concat(DISTINCT(ur.userName) separator ',') as userName, group_concat(DISTINCT(ue.resName) separator ',') as resName " +
//            "from (  " +
//                "select res.res_id, r.role_id as roleId,r.role_name as roleName, a.user_name as userName  " +
//                 "from umc_role_resources res  " +
//                       "left join umc_role r on res.role_id = r.role_id  " +
//                       "left join umc_account_role ar on r.role_id = ar.role_id  " +
//                       "left join umc_account a on ar.acct_id = a.acct_id  " +
//                      // "left join mdm_node n on a.sn_id = n.sn_id  " +
//                 "where res.is_deleted = 0 ) ur  " +
//            "left join (  " +
//                "select e.res_id ,s.sys_id as sysId, s.sys_name as sysName, e.res_name as resName " +
//                "from  umc_resources e  " +
//                      "left join umc_system s on e.sys_id = s.sys_id " +
//                "where e.is_deleted = 0 ) ue on ur.res_id = ue.res_id " +
//            "where 1=1 and ur.roleName like :roleName and ( ue.sysId = :sysId or :sysId = '') " +
//            "GROUP BY ur.roleId, ur.roleName, ue.sysId, ue.sysName limit :startIndex,:limit", nativeQuery = true)
    @Query(value =
            "select ur.roleId, ur.roleName, ue.sysId, ue.sysName, group_concat(DISTINCT(ue.resName) separator ',') as resName " +
                    "from (  " +
                "select res.res_id, r.role_id as roleId,r.role_name as roleName  " +
                    "from umc_role_resources res  " +
                    "left join umc_role r on res.role_id = r.role_id  " +
                    "where res.is_deleted = 0 ) ur  " +
                    "left join (  " +
                "select e.res_id ,s.sys_id as sysId, s.sys_name as sysName, e.res_name as resName " +
                "from  umc_resources e  " +
                    "left join umc_system s on e.sys_id = s.sys_id " +
                "where e.is_deleted = 0 ) ue on ur.res_id = ue.res_id " +
                    "where 1=1 and ur.roleName like :roleName and ( ue.sysId = :sysId or :sysId = '') " +
                    "GROUP BY ur.roleId, ur.roleName, ue.sysId, ue.sysName limit :startIndex,:limit", nativeQuery = true)
    List<Map<String, String>> page(@Param("roleName") String roleName, @Param("sysId") String sysId, @Param("startIndex") Integer startIndex, @Param("limit") Integer limit);

    @Query(value =
            "select count(t.roleId) " +
                    "from (select ur.roleId, ur.roleName, ue.sysId, ue.sysName, group_concat(DISTINCT(ur.userName) separator ',') as userName, group_concat(DISTINCT(ue.resName) separator ',') as resName " +
                    "from (  " +
                    "select res.res_id, r.role_id as roleId,r.role_name as roleName, a.user_name as userName  " +
                    "from umc_role_resources res  " +
                    "left join umc_role r on res.role_id = r.role_id  " +
                    "left join umc_account_role ar on r.role_id = ar.role_id  " +
                    "left join umc_account a on ar.acct_id = a.acct_id  " +
                    //"left join umc_strc_node n on a.sn_id = n.sn_id  " +
                    "where res.is_deleted = 0 ) ur  " +
                    "left join (  " +
                    "select e.res_id ,s.sys_id as sysId, s.sys_name as sysName, e.res_name as resName " +
                    "from  umc_resources e  " +
                    "left join umc_system s on e.sys_id = s.sys_id " +
                    "where e.is_deleted = 0 ) ue on ur.res_id = ue.res_id " +
                    "where 1=1 and ur.roleName like :roleName and ( ue.sysId = :sysId or :sysId = '') " +
                    "GROUP BY ur.roleId, ur.roleName, ue.sysId, ue.sysName ) t", nativeQuery = true)
    long countByQuery(@Param("roleName") String roleName, @Param("sysId") String sysId);

}