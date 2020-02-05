package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 6:35 下午 2020/2/4
 */
@Data
@Entity
@Table(name = "umc_employee")
public class UmcEmployee extends BaseEntity{
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "emp_id", nullable = false, length = 36)
    private String empId;

    @Basic
    @Column(name = "name", nullable = false, length = 28)
    private String name;

    @Basic
    @Column(name = "code", nullable = false, length = 24)
    private String code;

    @Basic
    @Column(name = "org_code", nullable = false, length = 24)
    private String orgCode;

    @Basic
    @Column(name = "job", nullable = true, length = 46)
    private String job;

    @Basic
    @Column(name = "phone", nullable = true, length = 18)
    private String phone;

    @Basic
    @Column(name = "email", nullable = true, length = 36)
    private String email;

    @Basic
    @Column(name = "enabled", nullable = false, length = 1)
    private String enabled;
}
