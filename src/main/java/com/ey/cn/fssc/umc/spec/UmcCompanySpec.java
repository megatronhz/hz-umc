package com.ey.cn.fssc.umc.spec;

import com.ey.cn.fssc.umc.entity.UmcCompany;
import com.ey.cn.fssc.umc.entity.UmcEmployee;
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
 * @Date: Created in 3:46 下午 2020/2/5
 */
public class UmcCompanySpec implements Specification<UmcCompany> {
    private Map<String, Object> params;

    public UmcCompanySpec(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<UmcCompany> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String coId = convertObj2Str(this.params.get("coId"));
        String code = convertObj2Str(this.params.get("code"));
        String name = convertObj2Str(this.params.get("name"));
        String region = convertObj2Str(this.params.get("region"));
        String phone = convertObj2Str(this.params.get("phone"));
        String uniCrCode = convertObj2Str(this.params.get("uniCrCode"));
        String isDeleted = convertObj2Str(this.params.get("isDeleted"));
        String legal = convertObj2Str(this.params.get("legal"));
        String addr = convertObj2Str(this.params.get("addr"));
        String type = convertObj2Str(this.params.get("type"));
        String createUser = convertObj2Str(this.params.get("createUser"));
        String createTime = convertObj2Str(this.params.get("createTime"));
        String updateUser = convertObj2Str(this.params.get("updateUser"));
        String updateTime = convertObj2Str(this.params.get("updateTime"));

        List<Predicate> ps = new ArrayList<>();
        if (!StringUtils.isEmpty(coId)) {
            ps.add(criteriaBuilder.equal(root.get("coId"), coId));
        }

        if (!StringUtils.isEmpty(code)) {
            ps.add(criteriaBuilder.equal(root.get("code"), code));
        }
        if (!StringUtils.isEmpty(name)) {
            ps.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (!StringUtils.isEmpty(region)) {
            ps.add(criteriaBuilder.like(root.get("job"), "%" + region + "%"));
        }
        if (!StringUtils.isEmpty(phone)) {
            ps.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
        }
        if (!StringUtils.isEmpty(legal)) {
            ps.add(criteriaBuilder.like(root.get("email"), "%" + legal + "%"));
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

        if (!StringUtils.isEmpty(uniCrCode)) {
            ps.add(criteriaBuilder.equal(root.get("enabled"), uniCrCode));
        }

        if (!StringUtils.isEmpty(type)) {
            ps.add(criteriaBuilder.equal(root.get("type"), type));
        }
        if (!StringUtils.isEmpty(addr)) {
            ps.add(criteriaBuilder.like(root.get("addr"), "%" + addr + "%"));
        }
        criteriaQuery = criteriaQuery.where(ps.toArray(new Predicate[ps.size()]));

        return criteriaQuery.getRestriction();
    }

    private String convertObj2Str(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }
}
