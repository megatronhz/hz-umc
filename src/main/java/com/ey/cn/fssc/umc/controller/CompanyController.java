package com.ey.cn.fssc.umc.controller;

import com.ey.cn.fssc.umc.api.Constant;
import com.ey.cn.fssc.umc.dto.UmcCompanyDto;
import com.ey.cn.fssc.umc.req.CompanyPageDto;
import com.ey.cn.fssc.umc.service.CompanyService;
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
@Api(tags = "公司管理")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    private static final String BASE_PATCH = Constant.CONTEXT_PATH + "/company";

    @ApiOperation(value = "创建公司", notes = "创建公司")
    @PostMapping(BASE_PATCH + "/create")
    //@PreAuthorize("hasAuthority('RB_COMPANY_CREATE')")
    public Result<Boolean> create(@RequestBody UmcCompanyDto umcCompanyDto) {
        return Result.ok(companyService.create(umcCompanyDto));
    }

    @ApiOperation(value = "删除公司", notes = "删除公司")
    @GetMapping(BASE_PATCH + "/delete")
    public Result<Boolean> delete(@ApiParam("公司主键ID") @RequestParam String coId) {
        return Result.ok(companyService.delete(coId));
    }

    @ApiOperation(value = "修改公司", notes = "修改公司")
    @PostMapping(BASE_PATCH + "/update")
    //@PreAuthorize("hasAuthority('RB_COMPANY_UPDATE')")
    public Result<Boolean> update(@RequestBody UmcCompanyDto umcCompanyDto) {
        return Result.ok(companyService.update(umcCompanyDto));
    }

    @ApiOperation(value = "公司详情", notes = "公司详情")
    @GetMapping(BASE_PATCH + "/detail")
    //@PreAuthorize("hasAuthority('RB_COMPANY_DETAIL')")
    public Result<UmcCompanyDto> detail(@ApiParam("公司主键ID") @RequestParam String coId) {
        return Result.ok(companyService.detail(coId));
    }

    @ApiOperation(value = "分页查询公司", notes = "分页查询公司")
    @PostMapping(BASE_PATCH + "/page")
    //@PreAuthorize("hasAuthority('RB_COMPANY_PAGE')")
    public Result<Page<UmcCompanyDto>> page(@RequestBody CompanyPageDto pageReqDto) {
        return Result.ok(companyService.page(pageReqDto));
    }

//    @ApiOperation(value = "公司列表", notes = "公司列表")
//    @PostMapping(BASE_PATCH + "/list")
//    public Result<List<JSONObject>> list(@RequestBody JSONObject jsonObject) {
//        return Result.ok(companyService.list(jsonObject));
//    }
}
