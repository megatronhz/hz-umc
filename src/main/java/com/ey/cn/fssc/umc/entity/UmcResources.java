package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:52.679
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_resources")
public class UmcResources extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "res_id", nullable = false, length = 36)
    private String resId;
    @Basic
    @Column(name = "res_name", nullable = true, length = 100)
    private String resName;
    @Basic
    @Column(name = "path_code", nullable = true, length = 255)
    private String pathCode;
    @Basic
    @Column(name = "parent_id", nullable = true, length = 36)
    private String parentId;
    @Basic
    @Column(name = "icon", nullable = true, length = 500)
    private String icon;
    @Basic
    @Column(name = "menu_url", nullable = true, length = 500)
    private String menuUrl;
    @Basic
    @Column(name = "res_type", nullable = true, length = 8)
    private String resType;
    @Basic
    @Column(name = "order_num", nullable = true)
    private Integer orderNum;
    @Basic
    @Column(name = "remarks", nullable = true, length = 4000)
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "sys_id", referencedColumnName = "sys_id", nullable = false)
    private UmcSystem umcSystem;
    @OneToMany(mappedBy = "umcResources")
    private Collection<UmcRoleResources> umcRoleResources;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        UmcResources that = (UmcResources) o;
        return Objects.equals(resId, that.resId) &&
                Objects.equals(resName, that.resName) &&
                Objects.equals(pathCode, that.pathCode) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(menuUrl, that.menuUrl) &&
                Objects.equals(resType, that.resType) &&
                Objects.equals(orderNum, that.orderNum) &&
                Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(resId, resName, pathCode, parentId, icon, menuUrl, resType, orderNum, remarks);
    }

}
