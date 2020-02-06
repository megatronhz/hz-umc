package com.ey.cn.fssc.umc.controller;

import com.ey.cn.fssc.umc.api.Constant;
import com.ey.cn.fssc.umc.dto.UmcDepartmentDto;
import com.ey.cn.fssc.umc.req.DepartmentPageDto;
import com.ey.cn.fssc.umc.service.UmcDepartmentService;
import com.ey.cn.pi.cc.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:38 上午 2020/2/6
 */
@RestController
@Api(tags = "部门管理")
public class DepartmentController {
    @Autowired
    private UmcDepartmentService departmentService;

    private static final String BASE_PATCH = Constant.CONTEXT_PATH + "/dpt";

    @ApiOperation(value = "创建部门", notes = "创建部门")
    @PostMapping(BASE_PATCH + "/create")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_CREATE')")
    public Result<Boolean> create(@RequestBody UmcDepartmentDto umcDepartmentDto) {
        return Result.ok(departmentService.create(umcDepartmentDto));
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @GetMapping(BASE_PATCH + "/delete")
    public Result<Boolean> delete(@ApiParam("部门主键ID")  @RequestParam String dptId) {
        return Result.ok(departmentService.delete(dptId));
    }

    @ApiOperation(value = "修改部门", notes = "修改部门")
    @PostMapping(BASE_PATCH + "/update")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_UPDATE')")
    public Result<Boolean> update(@RequestBody UmcDepartmentDto umcDepartmentDto) {
        return Result.ok(departmentService.update(umcDepartmentDto));
    }

    @ApiOperation(value = "员工详情", notes = "员工详情")
    @GetMapping(BASE_PATCH + "/detail")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_DETAIL')")
    public Result<UmcDepartmentDto> detail(@ApiParam("部门主键ID") @RequestParam String dptId) {
        return Result.ok(departmentService.detail(dptId));
    }

    @ApiOperation(value = "分页查询员工", notes = "分页查询员工")
    @PostMapping(BASE_PATCH + "/page")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_PAGE')")
    public Result<Page<UmcDepartmentDto>> page(@RequestBody DepartmentPageDto pageReqDto) {
        return Result.ok(departmentService.page(pageReqDto));
    }
}
