package com.ey.cn.fssc.umc.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <角色资源分页查询结果>
 *
 * @author wangqixia
 * @version 1.0 2019/03/22
 **/
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色资源分页查询结果Dto")
@Data
public class RoleResourcesPageResDto {

    @ApiModelProperty("角色Id")
    private String roleId;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("系统Id")
    private String sysId;
    @ApiModelProperty("系统名称")
    private String sysName;
//    @ApiModelProperty("关联用户")
//    private String userName;
    @ApiModelProperty("资源内容")
    private String resName;
}
