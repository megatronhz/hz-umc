package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:53.086
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_role_category")
public class UmcRoleCategory extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "ctgry_id", nullable = false, length = 36)
    private String ctgryId;
    @Basic
    @Column(name = "parent_id", nullable = false, length = 36)
    private String parentId;
    @Basic
    @Column(name = "ctgry_code", nullable = false, length = 36)
    private String ctgryCode;
    @Basic
    @Column(name = "ctgry_name", nullable = false, length = 36)
    private String ctgryName;
    @Basic
    @Column(name = "remarks", nullable = false, length = 500)
    private String remarks;
    @Basic
    @Column(name = "is_leaf", nullable = true)
    private Boolean isLeaf;
    @OneToMany(mappedBy = "umcRoleCategory")
    private Collection<UmcRole> umcRoles;
    @ManyToOne
    @JoinColumn(name = "sys_id", referencedColumnName = "sys_id")
    private UmcSystem umcSystem;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcRoleCategory that = (UmcRoleCategory) o;
        return Objects.equals(ctgryId, that.ctgryId) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(ctgryCode, that.ctgryCode) &&
                Objects.equals(ctgryName, that.ctgryName) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(isLeaf, that.isLeaf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ctgryId, parentId, ctgryCode, ctgryName, remarks, isLeaf);
    }

}
