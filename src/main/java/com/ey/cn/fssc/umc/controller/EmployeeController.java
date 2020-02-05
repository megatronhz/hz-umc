package com.ey.cn.fssc.umc.controller;

import com.ey.cn.fssc.umc.api.Constant;
import com.ey.cn.fssc.umc.dto.UmcEmployeeDto;
import com.ey.cn.fssc.umc.req.EmployeePageDto;
import com.ey.cn.fssc.umc.service.EmployeeService;
import com.ey.cn.pi.cc.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 8:22 上午 2020/2/5
 */
@RestController
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    private static final String BASE_PATCH = Constant.CONTEXT_PATH + "/employee";

    @ApiOperation(value = "创建员工", notes = "创建员工")
    @PostMapping(BASE_PATCH + "/create")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_CREATE')")
    public Result<Boolean> create(@RequestBody UmcEmployeeDto umcEmployeeDto) {
        return Result.ok(employeeService.create(umcEmployeeDto));
    }

    @ApiOperation(value = "删除员工", notes = "删除员工")
    @GetMapping(BASE_PATCH + "/delete")
    public Result<Boolean> delete(@ApiParam("员工主键ID")  @RequestParam String empId) {
        return Result.ok(employeeService.delete(empId));
    }

    @ApiOperation(value = "修改员工", notes = "修改员工")
    @PostMapping(BASE_PATCH + "/update")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_UPDATE')")
    public Result<Boolean> update(@RequestBody UmcEmployeeDto umcEmployeeDto) {
        return Result.ok(employeeService.update(umcEmployeeDto));
    }

    @ApiOperation(value = "员工详情", notes = "员工详情")
    @GetMapping(BASE_PATCH + "/detail")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_DETAIL')")
    public Result<UmcEmployeeDto> detail(@ApiParam("员工主键ID") @RequestParam String empId) {
        return Result.ok(employeeService.detail(empId));
    }

    @ApiOperation(value = "分页查询员工", notes = "分页查询员工")
    @PostMapping(BASE_PATCH + "/page")
    //@PreAuthorize("hasAuthority('RB_EMPLOYEE_PAGE')")
    public Result<Page<UmcEmployeeDto>> page(@RequestBody EmployeePageDto pageReqDto) {
        return Result.ok(employeeService.page(pageReqDto));
    }

    @ApiOperation(value = "员工列表", notes = "员工列表")
    @PostMapping(BASE_PATCH + "/list")
    public Result<List<UmcEmployeeDto>> list(@ApiParam("员工编码或名称") @RequestBody String keyWord) {
        return Result.ok(employeeService.findList(keyWord));
    }
}
