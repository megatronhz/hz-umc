package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcEmployee;
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
public interface UmcEmployeeRepository extends JpaRepository<UmcEmployee, String>, JpaSpecificationExecutor<UmcEmployee> {

    List<UmcEmployee> findByCode(String code);

    @Query(value = "select c from UmcEmployee c where   c.code like CONCAT('%', ?2, '%') or c.name like CONCAT('%', ?2, '%')")
    List<UmcEmployee> findeByKeyWord(List<String> orgCodes, String keyWord);

    //List<UmcEmployee> find
}
