package com.ey.cn.fssc.umc.entity;

import com.ey.cn.fssc.umc.enums.AccountChangedPass;
import com.ey.cn.fssc.umc.enums.AccountStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 2:59 下午 2020/2/4
 */
@Data
@Entity
@Table(name = "umc_org_strc")
public class UmcOrgStrc extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Basic
    @Column(name = "code", nullable = false, length = 24)
    private String code;

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Basic
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @Basic
    @Column(name = "is_leaf", nullable = false)
    private Boolean isLeaf;

    @Basic
    @Column(name = "parent_id", nullable = false, length = 36)
    private String parentId;

    @Basic
    @Column(name = "parent_ids", nullable = false, length = 720)
    private String parentIds;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcOrgStrc that = (UmcOrgStrc) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(isLeaf, that.isLeaf);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, name, type, isLeaf);
    }

}
