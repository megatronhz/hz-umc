package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcSystem;
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
* 描述：UmcSystem Specification
* @author king
* @date 2019-03-18
*/


public class UmcSystemSpec implements Specification<UmcSystem> {

    private Map<String, String> params;

    public UmcSystemSpec(Map<String, String> params){
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcSystem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String sysId = this.params.get("sysId");
        String sysCode = this.params.get("sysCode");
        String sysName = this.params.get("sysName");
        String remarks = this.params.get("remarks");
        String createTime = this.params.get("createTime");
        String createUser = this.params.get("createUser");
        String updateTime = this.params.get("updateTime");
        String updateUser = this.params.get("updateUser");
        String isDeleted = this.params.get("isDeleted");
        List<Predicate> ps = new ArrayList<>();

        if (!StringUtils.isEmpty(sysId)) {
            ps.add(criteriaBuilder.equal(root.get("sysId"), sysId));
        }
        if (!StringUtils.isEmpty(sysCode)) {
            ps.add(criteriaBuilder.like(root.get("sysCode"), "%" + sysCode + "%"));
        }
        if (!StringUtils.isEmpty(sysName)) {
            ps.add(criteriaBuilder.like(root.get("sysName"), "%" + sysName + "%"));
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

        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

}