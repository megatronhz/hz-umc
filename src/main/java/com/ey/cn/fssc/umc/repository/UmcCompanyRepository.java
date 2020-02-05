package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcCompany;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 8:22 上午 2020/2/5
 */
@Repository
public interface UmcCompanyRepository extends JpaRepository<UmcCompany, String>, JpaSpecificationExecutor<UmcCompany> {

    List<UmcCompany> findByCode(String code);

    @Query(value = "select c from UmcCompany c where c.type =?1 and (c.code like CONCAT('%', ?2, '%') or c.name like CONCAT('%', ?2, '%'))")
    List<UmcCompany> findChildCompany(String type, String keyWord);
}
