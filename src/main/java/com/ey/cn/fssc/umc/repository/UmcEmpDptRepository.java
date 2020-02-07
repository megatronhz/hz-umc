package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcEmpDpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 8:40 上午 2020/2/7
 */
@Repository
public interface UmcEmpDptRepository extends JpaRepository<UmcEmpDpt, String>, JpaSpecificationExecutor<UmcEmpDpt> {

    @Modifying
    @Query("delete from UmcEmpDpt c where c.umcEmployee.empId = ?1")
    int deleteByEmpId(String empId);

    @Query("select c from UmcEmpDpt c where c.umcEmployee.empId = ?1")
    List<UmcEmpDpt> findByEmpId(String empId);
}
