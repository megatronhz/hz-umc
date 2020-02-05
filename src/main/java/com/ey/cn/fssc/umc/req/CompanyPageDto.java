package com.ey.cn.fssc.umc.req;

import com.ey.cn.fssc.umc.dto.PageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 3:39 下午 2020/2/5
 */
@Data
@ApiModel("公司分页")
public class CompanyPageDto extends PageDto {

    @ApiModelProperty("公司编码")
    private String code;

    @ApiModelProperty("公司名称")
    private String name;
}
