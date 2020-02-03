package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:52.391
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_role")
public class UmcRole extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "role_id", nullable = false, length = 36)
    private String roleId;
    @Basic
    @Column(name = "parent_id", nullable = false, length = 36)
    private String parentId;
    @Basic
    @Column(name = "role_code", nullable = false, length = 36)
    private String roleCode;
    @Basic
    @Column(name = "role_name", nullable = false, length = 100)
    private String roleName;
    @Basic
    @Column(name = "remarks", nullable = true, length = 4000)
    private String remarks;
    @Basic
    @Column(name = "is_leaf", nullable = true)
    private Boolean isLeaf;
    @OneToMany(mappedBy = "umcRole")
    private Collection<UmcAccountRole> umcAccountRoles;
    @ManyToOne
    @JoinColumn(name = "sys_id", referencedColumnName = "sys_id", nullable = false)
    private UmcSystem umcSystem;
    @ManyToOne
    @JoinColumn(name = "ctgry_id", referencedColumnName = "ctgry_id", nullable = false)
    private UmcRoleCategory umcRoleCategory;
    @OneToMany(mappedBy = "umcRole")
    private Collection<UmcRoleResources> umcRoleResources;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcRole umcRole = (UmcRole) o;
        return Objects.equals(roleId, umcRole.roleId) &&
                Objects.equals(parentId, umcRole.parentId) &&
                Objects.equals(roleCode, umcRole.roleCode) &&
                Objects.equals(roleName, umcRole.roleName) &&
                Objects.equals(remarks, umcRole.remarks) &&
                Objects.equals(isLeaf, umcRole.isLeaf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roleId, parentId, roleCode, roleName, remarks, isLeaf);
    }

}
