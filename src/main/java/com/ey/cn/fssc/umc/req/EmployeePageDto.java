package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:31 上午 2020/2/5
 */
@Data
@ApiModel("员工分页")
public class EmployeePageDto extends PageDto {

    @ApiModelProperty("员工号")
    private String code;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("是否启用")
    private String enabled;
}
