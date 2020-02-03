package com.ey.cn.fssc.umc.entity;

import com.ey.cn.fssc.umc.enums.AccountChangedPass;
import com.ey.cn.fssc.umc.enums.AccountStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Updated by ey entity tools at 2019-03-18T11:44:52.539
 *
 * @author king
 */
@Data
@Entity
@Table(name = "umc_account")
public class UmcAccount extends BaseEntity {
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @GeneratedValue(generator = "system-uuid")
    @Id
    @Column(name = "acct_id", nullable = false, length = 36)
    private String acctId;
    @Basic
    @Column(name = "account", nullable = false, length = 64)
    private String account;
    @Basic
    @Column(name = "password", nullable = false, length = 64)
    private String password;
    @Basic
    @Column(name = "status", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Basic
    @Column(name = "changed_pass", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccountChangedPass changedPass;
    @ManyToOne
    @JoinColumn(name = "sys_id", referencedColumnName = "sys_id", nullable = false)
    private UmcSystem umcSystem;

    @Basic
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Basic
    @Column(name = "user_code", nullable = false, length = 36)
    private String userCode;

    @Basic
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Basic
    @Column(name = "user_type", nullable = false, length = 10)
    private String userType;

    @OneToMany(mappedBy = "umcAccount")
    private Collection<UmcAccountRole> umcAccountRoles;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UmcAccount that = (UmcAccount) o;
        return Objects.equals(acctId, that.acctId) &&
                Objects.equals(account, that.account) &&
                Objects.equals(password, that.password) &&
                Objects.equals(status, that.status) &&
                Objects.equals(changedPass, that.changedPass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(acctId, account, password, status, changedPass);
    }

}
