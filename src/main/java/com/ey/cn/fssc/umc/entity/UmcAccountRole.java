package com.ey.cn.fssc.umc.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:53
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_account_role")
public class UmcAccountRole extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "uar_id", nullable = false, length = 36)
    private String uarId;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private UmcRole umcRole;
    @ManyToOne
    @JoinColumn(name = "acct_id", referencedColumnName = "acct_id")
    private UmcAccount umcAccount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        UmcAccountRole that = (UmcAccountRole) o;
        return Objects.equals(uarId, that.uarId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uarId);
    }

}
