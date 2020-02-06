package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcCompany;
import com.ey.cn.fssc.umc.entity.UmcDepartment;
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
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:13 上午 2020/2/6
 */
public class UmcDepartmentSpec implements Specification<UmcDepartment> {
    private Map<String, Object> params;

    public UmcDepartmentSpec(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcDepartment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String dptId = convertObj2Str(this.params.get("dptId"));
        String code = convertObj2Str(this.params.get("code"));
        String name = convertObj2Str(this.params.get("name"));
        String phone = convertObj2Str(this.params.get("phone"));
        String cntct = convertObj2Str(this.params.get("cntct"));
        String isDeleted = convertObj2Str(this.params.get("isDeleted"));
        String createUser = convertObj2Str(this.params.get("createUser"));
        String createTime = convertObj2Str(this.params.get("createTime"));
        String updateUser = convertObj2Str(this.params.get("updateUser"));
        String updateTime = convertObj2Str(this.params.get("updateTime"));
        List<String> orgCodes = this.params.get("orgCodes") == null ? null : (ArrayList)this.params.get("orgCodes");
        List<Predicate> ps = new ArrayList<>();
        if (!StringUtils.isEmpty(dptId)) {
            ps.add(criteriaBuilder.equal(root.get("dptId"), dptId));
        }

        if (!StringUtils.isEmpty(code)) {
            ps.add(criteriaBuilder.equal(root.get("code"), code));
        }
        if (!StringUtils.isEmpty(name)) {
            ps.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (!StringUtils.isEmpty(cntct)) {
            ps.add(criteriaBuilder.like(root.get("cntct"), "%" + cntct + "%"));
        }
        if (!StringUtils.isEmpty(phone)) {
            ps.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
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

        if (!CollectionUtils.isEmpty(orgCodes)){
            CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("orgCode"));
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
