package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <角色资源分页请求参数>
 *
 * @author wangqixia
 * @version 1.0 2019/03/20
 **/
@Data
public class RoleResourcePageDto extends PageDto {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("系统Id")
    private String sysId;
}
