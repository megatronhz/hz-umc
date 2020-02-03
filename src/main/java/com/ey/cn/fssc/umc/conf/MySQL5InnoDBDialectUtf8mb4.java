package com.ey.cn.fssc.umc.conf;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author King 2019/6/10 下午2:06
 * <>
 */
public class MySQL5InnoDBDialectUtf8mb4 extends MySQL5InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci";
    }
}
