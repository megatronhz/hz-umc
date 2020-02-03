package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcResources;
import com.ey.cn.fssc.umc.entity.UmcRoleResources;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
* 描述：UmcResources Specification
* @author king
* @date 2019-03-18
*/


public class UmcResourcesSpec implements Specification<UmcResources> {

    private Map<String, String> params;

    public UmcResourcesSpec(Map<String, String> params){
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcResources> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String resId = this.params.get("resId");
        String resName = this.params.get("resName");
        String pathCode = this.params.get("pathCode");
        String parentId = this.params.get("parentId");
        String sysId = this.params.get("sysId");
        String icon = this.params.get("icon");
        String menuUrl = this.params.get("menuUrl");
        String resType = this.params.get("resType");
        String orderNum = this.params.get("orderNum");
        String remarks = this.params.get("remarks");
        String createTime = this.params.get("createTime");
        String createUser = this.params.get("createUser");
        String updateTime = this.params.get("updateTime");
        String updateUser = this.params.get("updateUser");
        String isDeleted = this.params.get("isDeleted");
        String roleId = this.params.get("roleId");
        List<Predicate> ps = new ArrayList<>();
        Join<UmcResources, UmcRoleResources> join = root.join("umcRoleResources", JoinType.LEFT);

        if (!StringUtils.isEmpty(resId)) {
            ps.add(criteriaBuilder.equal(root.get("resId"), resId));
        }
        if (!StringUtils.isEmpty(resName)) {
            ps.add(criteriaBuilder.like(root.get("resName"), "%" + resName + "%"));
        }
        if (!StringUtils.isEmpty(pathCode)) {
            ps.add(criteriaBuilder.like(root.get("pathCode"), "%" + pathCode + "%"));
        }
        if (!StringUtils.isEmpty(parentId)) {
            ps.add(criteriaBuilder.equal(root.get("parentId"), parentId));
        }
        if (!StringUtils.isEmpty(sysId)) {
            ps.add(criteriaBuilder.equal(root.get("sysId"), sysId));
        }
        if (!StringUtils.isEmpty(icon)) {
            ps.add(criteriaBuilder.like(root.get("icon"), "%" + icon + "%"));
        }
        if (!StringUtils.isEmpty(menuUrl)) {
            ps.add(criteriaBuilder.like(root.get("menuUrl"), "%" + menuUrl + "%"));
        }
        if (!StringUtils.isEmpty(resType)) {
            ps.add(criteriaBuilder.like(root.get("resType"), "%" + resType + "%"));
        }
        if (!StringUtils.isEmpty(orderNum)) {
            ps.add(criteriaBuilder.like(root.get("orderNum"), "%" + orderNum + "%"));
        }
        if (!StringUtils.isEmpty(remarks)) {
            ps.add(criteriaBuilder.like(root.get("remarks"), "%" + remarks + "%"));
        }
        if (!StringUtils.isEmpty(createTime)) {
            ps.add(criteriaBuilder.like(root.get("createTime"), "%" + createTime + "%"));
        }
        if (!StringUtils.isEmpty(createUser)) {
            ps.add(criteriaBuilder.like(root.get("createUser"), "%" + createUser + "%"));
        }
        if (!StringUtils.isEmpty(updateTime)) {
            ps.add(criteriaBuilder.like(root.get("updateTime"), "%" + updateTime + "%"));
        }
        if (!StringUtils.isEmpty(updateUser)) {
            ps.add(criteriaBuilder.like(root.get("updateUser"), "%" + updateUser + "%"));
        }
        if (!StringUtils.isEmpty(isDeleted)) {
            ps.add(criteriaBuilder.like(root.get("isDeleted"), "%" + isDeleted + "%"));
        }
        if (!StringUtils.isEmpty(roleId)) {
            ps.add(criteriaBuilder.equal(join.get("umcRole").get("roleId"),  roleId));
        }

        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

}