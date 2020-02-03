package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
* 描述：UmcRole Specification
* @author king
* @date 2019-03-18
*/


public class UmcRoleSpec implements Specification<UmcRole> {

    private Map<String, String> params;

    public UmcRoleSpec(Map<String, String> params){
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String roleId = this.params.get("roleId");
        String sysId = this.params.get("sysId");
        String ctgryId = this.params.get("ctgryId");
        String parentId = this.params.get("parentId");
        String roleCode = this.params.get("roleCode");
        String roleName = this.params.get("roleName");
        String remarks = this.params.get("remarks");
        String isLeaf = this.params.get("isLeaf");
        String isDeleted = this.params.get("isDeleted");
        String createUser = this.params.get("createUser");
        String createTime = this.params.get("createTime");
        String updateUser = this.params.get("updateUser");
        String updateTime = this.params.get("updateTime");
        List<Predicate> ps = new ArrayList<>();

        if (!StringUtils.isEmpty(roleId)) {
            ps.add(criteriaBuilder.equal(root.get("roleId"), roleId));
        }
        if (!StringUtils.isEmpty(sysId)) {
            ps.add(criteriaBuilder.equal(root.get("sysId"), sysId));
        }
        if (!StringUtils.isEmpty(ctgryId)) {
            ps.add(criteriaBuilder.equal(root.get("ctgryId"), ctgryId));
        }
        if (!StringUtils.isEmpty(parentId)) {
            ps.add(criteriaBuilder.equal(root.get("parentId"), parentId));
        }
        if (!StringUtils.isEmpty(roleCode)) {
            ps.add(criteriaBuilder.like(root.get("roleCode"), "%" + roleCode + "%"));
        }
        if (!StringUtils.isEmpty(roleName)) {
            ps.add(criteriaBuilder.like(root.get("roleName"), "%" + roleName + "%"));
        }
        if (!StringUtils.isEmpty(remarks)) {
            ps.add(criteriaBuilder.like(root.get("remarks"), "%" + remarks + "%"));
        }
        if (!StringUtils.isEmpty(isLeaf)) {
            ps.add(criteriaBuilder.like(root.get("isLeaf"), "%" + isLeaf + "%"));
        }
        if (!StringUtils.isEmpty(isDeleted)) {
            ps.add(criteriaBuilder.like(root.get("isDeleted"), "%" + isDeleted + "%"));
        }
        if (!StringUtils.isEmpty(createUser)) {
            ps.add(criteriaBuilder.like(root.get("createUser"), "%" + createUser + "%"));
        }
        if (!StringUtils.isEmpty(createTime)) {
            ps.add(criteriaBuilder.like(root.get("createTime"), "%" + createTime + "%"));
        }
        if (!StringUtils.isEmpty(updateUser)) {
            ps.add(criteriaBuilder.like(root.get("updateUser"), "%" + updateUser + "%"));
        }
        if (!StringUtils.isEmpty(updateTime)) {
            ps.add(criteriaBuilder.like(root.get("updateTime"), "%" + updateTime + "%"));
        }

        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

}