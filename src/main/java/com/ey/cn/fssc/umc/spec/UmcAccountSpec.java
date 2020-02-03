package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcAccount;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 描述：UmcAccount Specification
 *
 * @author king
 * @date 2019-03-18
 */


public class UmcAccountSpec implements Specification<UmcAccount> {

    private Map<String, Object> params;

    public UmcAccountSpec(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcAccount> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String acctId = convertObj2Str(this.params.get("acctId"));
        String sysId = convertObj2Str(this.params.get("sysId"));
        String snId = convertObj2Str(this.params.get("snId"));
        String snName = convertObj2Str(this.params.get("snName"));
        String account = convertObj2Str(this.params.get("account"));
        String password = convertObj2Str(this.params.get("password"));
        String isDeleted = convertObj2Str(this.params.get("isDeleted"));
        String status = convertObj2Str(this.params.get("status"));
        String createUser = convertObj2Str(this.params.get("createUser"));
        String createTime = convertObj2Str(this.params.get("createTime"));
        String updateUser = convertObj2Str(this.params.get("updateUser"));
        String updateTime = convertObj2Str(this.params.get("updateTime"));
        List<String> userIds = this.params.get("userIds") == null ? null : (ArrayList)this.params.get("userIds");
        // String changedPass = this.params.get("changedPass");
        String type = convertObj2Str(this.params.get("type"));
        List<Predicate> ps = new ArrayList<>();
        if (!StringUtils.isEmpty(acctId)) {
            ps.add(criteriaBuilder.equal(root.get("acctId"), acctId));
        }
        if (!StringUtils.isEmpty(sysId)) {
            ps.add(criteriaBuilder.equal(root.get("umcSystem").get("sysId"), sysId));
        }
        if (!StringUtils.isEmpty(account)) {
            ps.add(criteriaBuilder.like(root.get("account"), "%" + account + "%"));
        }
        if (!StringUtils.isEmpty(password)) {
            ps.add(criteriaBuilder.like(root.get("password"), "%" + password + "%"));
        }
        if (!StringUtils.isEmpty(isDeleted)) {
            ps.add(criteriaBuilder.like(root.get("isDeleted"), "%" + isDeleted + "%"));
        }
        if (!StringUtils.isEmpty(status)) {
            ps.add(criteriaBuilder.like(root.get("status"), "%" + status + "%"));
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

        if (!StringUtils.isEmpty(type)) {
            ps.add(criteriaBuilder.equal(root.get("userType"), type));
        }

        if (!StringUtils.isEmpty(snName)) {
            ps.add(criteriaBuilder.like(root.get("userName"), "%" + snName + "%"));
        }
        if (!CollectionUtils.isEmpty(userIds)){
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("userId"));
            userIds.forEach(n -> in.value(n));
            ps.add(in);

        }

        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

    private String convertObj2Str(Object obj){
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }

}