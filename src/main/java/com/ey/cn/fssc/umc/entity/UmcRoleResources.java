package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:53.125
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_role_resources")
public class UmcRoleResources extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "auth_id", nullable = false, length = 36)
    private String authId;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private UmcRole umcRole;
    @ManyToOne
    @JoinColumn(name = "res_id", referencedColumnName = "res_id", nullable = false)
    private UmcResources umcResources;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcRoleResources that = (UmcRoleResources) o;
        return Objects.equals(authId, that.authId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authId);
    }

}
