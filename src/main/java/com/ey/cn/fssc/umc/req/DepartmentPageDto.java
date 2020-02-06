package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:31 上午 2020/2/6
 */
@Data
@ApiModel("部门分页")
public class DepartmentPageDto extends PageDto {

    @ApiModelProperty("部门编码")
    private String code;
    @ApiModelProperty("部门名称")
    private String name;
//    @ApiModelProperty("所属公司")
//    private String orgCode;
}
