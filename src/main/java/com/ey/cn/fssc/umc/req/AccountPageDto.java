package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <账号分页查询请求参数>
 *
 * @author wangqixia
 * @version 1.0 2019/03/20
 **/
@Data
public class AccountPageDto extends PageDto {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("关联用户")
    private String snName;

    @ApiModelProperty("系统")
    private String sysId;

    @ApiModelProperty("用户类型")
    private String userType;
}
