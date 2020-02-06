package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:04 上午 2020/2/6
 */
@Data
@Entity
@Table(name = "umc_department")
public class UmcDepartment extends BaseEntity{
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
//    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "dpt_id", nullable = false, length = 36)
    private String dptId;

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Basic
    @Column(name = "code", nullable = false, length = 24)
    private String code;

    @Basic
    @Column(name = "co_id", nullable = false, length = 36)
    private String coId;

    @Basic
    @Column(name = "cntct", nullable = true, length = 18)
    private String cntct;

    @Basic
    @Column(name = "phone", nullable = true, length = 36)
    private String phone;
}
