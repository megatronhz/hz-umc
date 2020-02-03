package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <账号分页查询条件类>
 *
 * @author wangqixia
 * @version 1.0 2019/03/19
 **/
@Data
public class PageAccountReqDto extends PageDto {

    @ApiModelProperty("账号编码")
    private String code;

    @ApiModelProperty("系统编号")
    private String systemCode;

    @ApiModelProperty("用户类型")
    private String userTypeNo;

    @ApiModelProperty("关联用户编码")
    private String userCode;
}
