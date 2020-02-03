package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:52.501
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_system")
public class UmcSystem extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "sys_id", nullable = false, length = 36)
    private String sysId;
    @Basic
    @Column(name = "sys_code", nullable = true, length = 255)
    private String sysCode;
    @Basic
    @Column(name = "sys_name", nullable = true, length = 100)
    private String sysName;
    @Basic
    @Column(name = "remarks", nullable = true, length = 4000)
    private String remarks;
    @OneToMany(mappedBy = "umcSystem")
    private Collection<UmcResources> umcResources;
    @OneToMany(mappedBy = "umcSystem")
    private Collection<UmcRole> umcRoles;
    @OneToMany(mappedBy = "umcSystem")
    private Collection<UmcRoleCategory> umcRoleCategories;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcSystem umcSystem = (UmcSystem) o;
        return Objects.equals(sysId, umcSystem.sysId) &&
                Objects.equals(sysCode, umcSystem.sysCode) &&
                Objects.equals(sysName, umcSystem.sysName) &&
                Objects.equals(remarks, umcSystem.remarks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sysId, sysCode, sysName, remarks);
    }

}
