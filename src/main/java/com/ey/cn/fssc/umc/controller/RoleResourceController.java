package com.ey.cn.fssc.umc.controller;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.FullTreeNode;
import com.ey.cn.fssc.umc.dto.RoleResourcesDto;
import com.ey.cn.fssc.umc.dto.UmcRoleResourcesDto;
import com.ey.cn.fssc.umc.req.RoleResourcePageDto;
import com.ey.cn.fssc.umc.req.RoleResourcesPageResDto;
import com.ey.cn.fssc.umc.service.UmcRoleResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <角色资源管理业务逻辑层>
 *
 * @author wangqixia
 * @version 1.0 2019/03/20
 **/
@Slf4j
@Api(tags = "角色资源管理", description = "角色资源管理")
@RestController
public class RoleResourceController {

    private static final String BASE_PATH = Constant.CONTEXT_PATH + "/roleResource";

    @Autowired
    private UmcRoleResourcesService umcRoleResourcesService;

    @ApiOperation(value = "角色资源分页查询", notes = "账号分页查询")
    @ApiImplicitParam(name = "roleResourcePageDto", dataType = "RoleResourcePageDto", required = true)
    @RequestMapping(value = BASE_PATH + "/page", method = {RequestMethod.POST})
    public Result<Page<RoleResourcesPageResDto>> page(@RequestBody RoleResourcePageDto roleResourcePageDto) {

        return Result.ok(umcRoleResourcesService.page(roleResourcePageDto));
    }

    @ApiOperation(value = "角色对应资源的详情", notes = "角色对应资源的详情")
    @ApiImplicitParam(name = "roleId", dataType = "string", required = true)
    @RequestMapping(value = BASE_PATH + "/detail", method = {RequestMethod.GET})
    public Result<UmcRoleResourcesDto> detail(@RequestParam("roleId") String roleId) {

        return Result.ok(umcRoleResourcesService.detail(roleId));
    }

    @ApiOperation(value = "删除角色资源关系", notes = "删除角色资源关系")
    @ApiImplicitParam(name = "roleId", dataType = "string", required = true)
    @RequestMapping(value = BASE_PATH + "/delete", method = {RequestMethod.GET})
    public Result<Boolean> delete(@RequestParam("roleId") String roleId) {

        return Result.ok(umcRoleResourcesService.delete(roleId));
    }

    @ApiOperation(value = "新增编辑角色资源关系", notes = "新增编辑角色资源关系")
    @ApiImplicitParam(name = "roleResourcesDto", dataType = "RoleResourcesDto", required = true)
    @RequestMapping(value = BASE_PATH + "/save", method = {RequestMethod.POST})
    public Result<Boolean> save(@RequestBody RoleResourcesDto roleResourcesDto) {

        return Result.ok(umcRoleResourcesService.save(roleResourcesDto));
    }

    @ApiOperation(value = "系统资源整棵树形展开", notes = "系统资源整棵树形展开")
    @ApiImplicitParam(name = "parentId", value = "系统ID", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/queryFullResourceTree", method = {RequestMethod.GET})
    public Result<List<FullTreeNode>> queryFullResourceTree(@RequestParam("parentId") String parentId) {

        return Result.ok(umcRoleResourcesService.queryFullResourceTree(parentId));
    }

}
