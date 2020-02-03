package com.ey.cn.fssc.umc.util;

import com.ey.cn.pi.cc.common.oauth.SecurityUser;

/**
 * @author King 2019/3/22 下午3:42
 * <>
 */
public class UmcUserUtils {

    // 集成OAuth后统一打开


    /**
     * 获取用户
     *
     * @return
     */
    private static SecurityUser getCurrentUser() {
//        SecurityUser user = UserUtils.getSecurityUser();
//        if (null == user) user = new SecurityUser();
        return null;
    }

    public static String getCurrentUserName() {
        return "";
    }

    public static String getCurrentAccount() {
        return "";
    }
}
