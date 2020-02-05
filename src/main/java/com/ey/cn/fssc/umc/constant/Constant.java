package com.ey.cn.fssc.umc.constant;

public interface Constant extends com.ey.cn.fssc.umc.api.Constant {

    String SWAGGER_GROUP_NAME = "FSSC-UMC";
    String SWAGGER_TITLE = "财务共享平台用户中心接口";
    String SWAGGER_DESCRIPTION = "财务共享平台用户中心接口定义";
    String INIT_PASSWORD = "123456";
    String VALIDATE_ACCOUNT_KEY = "EY_AUTH_UMC_SECURITY_KEY";
    // 权限编码
    public static class AuthCode {
        // 查询所有员工信息
        public static final String DATA_MDM_EMPLOYEE_ALL = "DATA_MDM_EMPLOYEE_ALL";
        // 可分配系统管理员角色
        public static final String DATA_SYS_ACCOUNT_ADMIN = "DATA_SYS_ACCOUNT_ADMIN";
    }

    /**
     * 节点类型
     */
    interface NodeType {
        /**
         * 品牌
         */
        String BRAND = "Brand";
        /**
         * 销售组织
         */
        String SLS_COMPANY = "SlsCompany";
        /**
         * 销售渠道
         */
        String SLS_CHANNEL = "SlsChannel";
        /**
         * 客户
         */
        String CUSTOMER = "Customer";
        /**
         * 公司
         */
        String COMPANY = "Company";

        /**
         * 子公司
         */
        String CHILD_COMPANY = "childCompany";

        /**
         * 部门
         */
        String DEPARTMENT = "department";

        /**
         * 员工
         */
        String EMPLOYEE = "employee";

        /**
         * 销售区域
         */
        String SALES_RGN = "SalesRgn";


    }

    /**
     * 树类型
     */
    interface TreeType {
        /**
         * 组织架构
         */
        String ORG_STRC = "ORG_STRC";
        /**
         * 销售网络
         */
        String DSTN = "DSTN";
    }
}
