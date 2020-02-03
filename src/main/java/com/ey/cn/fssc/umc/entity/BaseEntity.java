package com.ey.cn.fssc.umc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column
    private Boolean isDeleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private Date createTime;

    @Column
    @JsonIgnore
    private String createUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    @JsonIgnore
    private Date updateTime;

    @Column
    @JsonIgnore
    private String updateUser;
}
