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
@Table(name = "umc_company")
public class UmcCompany extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "co_id", nullable = false, length = 36)
    private String coId;

    @Basic
    @Column(name = "name", nullable = false, length = 260)
    private String name;

    @Basic
    @Column(name = "code", nullable = false, length = 24)
    private String code;

    @Basic
    @Column(name = "region", nullable = true, length = 80)
    private String region;

    @Basic
    @Column(name = "uni_cr_code", nullable = true, length = 24)
    private String uniCrCode;

    @Basic
    @Column(name = "phone", nullable = true, length = 18)
    private String phone;

    @Basic
    @Column(name = "legal", nullable = true, length = 28)
    private String legal;

    @Basic
    @Column(name = "addr", nullable = true, length = 120)
    private String addr;

    @Basic
    @Column(name = "type", nullable = false, length = 8)
    private String type;
}
