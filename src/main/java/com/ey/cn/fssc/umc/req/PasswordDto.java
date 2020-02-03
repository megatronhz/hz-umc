package com.ey.cn.fssc.umc.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PasswordDto {
    @ApiModelProperty("旧密码")
    String oldPassword;
    @ApiModelProperty("新密码")
    String password;
}
