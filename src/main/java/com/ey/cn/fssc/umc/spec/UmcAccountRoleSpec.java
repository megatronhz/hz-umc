package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcAccountRole;
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
* 描述：UmcAccountRole Specification
* @author king
* @date 2019-03-18
*/


public class UmcAccountRoleSpec implements Specification<UmcAccountRole> {

    private Map<String, String> params;

    public UmcAccountRoleSpec(Map<String, String> params){
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcAccountRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String uarId = this.params.get("uarId");
        String roleId = this.params.get("roleId");
        String acctId = this.params.get("acctId");
        String updateTime = this.params.get("updateTime");
        String createTime = this.params.get("createTime");
        String createUser = this.params.get("createUser");
        String updateUser = this.params.get("updateUser");
        String isDeleted = this.params.get("isDeleted");
        List<Predicate> ps = new ArrayList<>();

        if (!StringUtils.isEmpty(uarId)) {
            ps.add(criteriaBuilder.equal(root.get("uarId"), uarId ));
        }
        if (!StringUtils.isEmpty(roleId)) {
            ps.add(criteriaBuilder.equal(root.get("umcRole").get("roleId"), roleId));
        }
        if (!StringUtils.isEmpty(acctId)) {
            ps.add(criteriaBuilder.equal(root.get("acctId"), acctId));
        }
        if (!StringUtils.isEmpty(updateTime)) {
            ps.add(criteriaBuilder.equal(root.get("updateTime"), "%" + updateTime + "%"));
        }
        if (!StringUtils.isEmpty(createTime)) {
            ps.add(criteriaBuilder.like(root.get("createTime"), "%" + createTime + "%"));
        }
        if (!StringUtils.isEmpty(createUser)) {
            ps.add(criteriaBuilder.like(root.get("createUser"), "%" + createUser + "%"));
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