package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcRoleResources;
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
* 描述：UmcRoleResources Specification
* @author king
* @date 2019-03-18
*/


public class UmcRoleResourcesSpec implements Specification<UmcRoleResources> {

    private Map<String, String> params;

    public UmcRoleResourcesSpec(Map<String, String> params){
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcRoleResources> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String authId = this.params.get("authId");
        String roleId = this.params.get("roleId");
        String resId = this.params.get("resId");
        String createTime = this.params.get("createTime");
        String createUser = this.params.get("createUser");
        String updateTime = this.params.get("updateTime");
        String updateUser = this.params.get("updateUser");
        String isDeleted = this.params.get("isDeleted");
        List<Predicate> ps = new ArrayList<>();

        if (!StringUtils.isEmpty(authId)) {
            ps.add(criteriaBuilder.equal(root.get("authId"), authId));
        }
        if (!StringUtils.isEmpty(roleId)) {
            ps.add(criteriaBuilder.equal(root.get("roleId"), roleId));
        }
        if (!StringUtils.isEmpty(resId)) {
            ps.add(criteriaBuilder.equal(root.get("resId"), resId));
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

        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

}