package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 3:04 下午 2020/2/6
 */
@Data
@Entity
@Table(name = "umc_emp_dpt")
public class UmcEmpDpt extends BaseEntity{
//    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
//    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

//    @Basic
//    @Column(name = "emp_id", nullable = false, length = 36)
//    private String empId;
//
//    @Basic
//    @Column(name = "ref_id", nullable = false, length = 36)
//    private String refId;

    @ManyToOne
    @JoinColumn(name = "ref_id", referencedColumnName = "id", nullable = false)
    private UmcOrgStrc umcOrgStrc;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private UmcEmployee umcEmployee;
}
