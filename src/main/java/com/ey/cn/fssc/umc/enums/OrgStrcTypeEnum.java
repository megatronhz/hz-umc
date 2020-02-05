package com.ey.cn.fssc.umc.enums;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 2:14 下午 2020/2/5
 */
public enum OrgStrcTypeEnum {
    COMPANY("Company","集团公司"),
    CHILD_COMPANY("ChildCompany","子公司"),
    DEPARTMENT("Department","部门"),
    JOB("Job","岗位"),
    EMPLOYEE("Employee","员工");

    private String code;
    private String name;

    OrgStrcTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
