package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcEmpDpt;
import com.ey.cn.fssc.umc.entity.UmcEmployee;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:51 上午 2020/2/5
 */
public class UmcEmployeeSpec implements Specification<UmcEmployee> {

    private Map<String, Object> params;

    public UmcEmployeeSpec(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcEmployee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String empId = convertObj2Str(this.params.get("empId"));
        String code = convertObj2Str(this.params.get("code"));
        String name = convertObj2Str(this.params.get("name"));
        String job = convertObj2Str(this.params.get("job"));
        //String orgCode = convertObj2Str(this.params.get("orgCode"));
        String phone = convertObj2Str(this.params.get("phone"));
        String email = convertObj2Str(this.params.get("email"));
        String isDeleted = convertObj2Str(this.params.get("isDeleted"));
        String enabled = convertObj2Str(this.params.get("enabled"));
        String createUser = convertObj2Str(this.params.get("createUser"));
        String createTime = convertObj2Str(this.params.get("createTime"));
        String updateUser = convertObj2Str(this.params.get("updateUser"));
        String updateTime = convertObj2Str(this.params.get("updateTime"));
        List<String> orgCodes = this.params.get("orgCodes") == null ? null : (ArrayList)this.params.get("orgCodes");
        List<Predicate> ps = new ArrayList<>();
        if (!StringUtils.isEmpty(empId)) {
            ps.add(criteriaBuilder.equal(root.get("empId"), empId));
        }

        if (!StringUtils.isEmpty(code)) {
            ps.add(criteriaBuilder.equal(root.get("code"), code));
        }
        if (!StringUtils.isEmpty(name)) {
            ps.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (!StringUtils.isEmpty(job)) {
            ps.add(criteriaBuilder.like(root.get("job"), "%" + job + "%"));
        }
        if (!StringUtils.isEmpty(phone)) {
            ps.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
        }
        if (!StringUtils.isEmpty(email)) {
            ps.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
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

        if (!StringUtils.isEmpty(isDeleted)) {
            ps.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
        }

        if (!StringUtils.isEmpty(enabled)) {
            ps.add(criteriaBuilder.equal(root.get("enabled"), enabled));
        }

        if (!CollectionUtils.isEmpty(orgCodes)){
            Join<UmcEmployee, UmcOrgStrc> join = root.join("umcEmpDpts", JoinType.INNER).join("umcOrgStrc", JoinType.INNER);
            CriteriaBuilder.In<String> in = criteriaBuilder.in(join.get("umcOrgStrc").get("id"));
            orgCodes.forEach(n -> in.value(n));
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