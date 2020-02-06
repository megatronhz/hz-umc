package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcDepartment;
import com.ey.cn.fssc.umc.entity.UmcEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:19 上午 2020/2/6
 */
@Repository
public interface UmcDepartmentRepository extends JpaRepository<UmcDepartment, String>, JpaSpecificationExecutor<UmcDepartment> {

    List<UmcDepartment> findByCode(String code);

//    @Query(value = "select c from UmcEmployee c where c.orgCode in (?1) and c.code like CONCAT('%', ?2, '%') or c.name like CONCAT('%', ?2, '%')")
//    List<UmcDepartment> findeByKeyWord(List<String> orgCodes, String keyWord);
}
