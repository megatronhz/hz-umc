package com.ey.cn.fssc.umc.constant;

public interface Constant extends com.ey.cn.fssc.umc.api.Constant {

    String SWAGGER_GROUP_NAME = "RB-UMC";
    String SWAGGER_TITLE = "红牛用户中心接口";
    String SWAGGER_DESCRIPTION = "红牛用户中心接口定义";
    String INIT_PASSWORD = "123456";
    String VALIDATE_ACCOUNT_KEY = "EY_AUTH_UMC_SECURITY_KEY";
    // 权限编码
    public static class AuthCode {
        // 查询所有员工信息
        public static final String DATA_MDM_EMPLOYEE_ALL = "DATA_MDM_EMPLOYEE_ALL";
        // 可分配系统管理员角色
        public static final String DATA_SYS_ACCOUNT_ADMIN = "DATA_SYS_ACCOUNT_ADMIN";
    }
}
