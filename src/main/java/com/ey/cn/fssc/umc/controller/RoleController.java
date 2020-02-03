package com.ey.cn.fssc.umc.controller;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.pi.cc.common.oauth.SecurityUser;
import com.ey.cn.fssc.umc.api.RoleApi;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.*;
import com.ey.cn.fssc.umc.enums.NodeType;
import com.ey.cn.fssc.umc.service.UmcRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <角色管理业务控制层>
 *
 * @author wangqixia
 * @version 1.0 2019/03/19
 **/
@Slf4j
@Api(tags = "角色管理接口", description = "角色管理接口")
@RestController
public class RoleController implements RoleApi {

    private static final String BASE_PATCH = Constant.CONTEXT_PATH + "/role";

    @Autowired
    private UmcRoleService umcRoleService;

    @ApiOperation(value = "系统角色树形展开", notes = "系统角色树形展开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "strcNodeType", dataType = "String", required = true),
            @ApiImplicitParam(name = "parentId", value = "上级节点ID", dataType = "String", required = true)
    })
    @RequestMapping(value = BASE_PATCH + "/querySysRoleTree", method = {RequestMethod.GET})
    public Result<List<TreeNode>> querySysRoleTree(@RequestParam("strcNodeType") NodeType strcNodeType, @RequestParam("parentId") String parentId) {

        return Result.ok(umcRoleService.querySysRoleTree(strcNodeType, parentId));
    }

    @ApiOperation(value = "角色详情", notes = "角色详情")
    @ApiImplicitParam(name = "id", value = "角色Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/detail", method = {RequestMethod.GET})
    public Result<UmcRoleDto> detail(@RequestParam String id) {

        return Result.ok(umcRoleService.detail(id));
    }

    @ApiOperation(value = "删除角色", notes = "角色删除")
    @ApiImplicitParam(name = "id", value = "角色Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/delete", method = {RequestMethod.GET})
    public Result<Boolean> delete(@RequestParam String id) {

        return umcRoleService.delete(id);
    }

    @ApiOperation(value = "新增编辑角色", notes = "新增编辑角色")
    @ApiImplicitParam(name = "umcRoleDto", value = "角色", dataType = "UmcRoleDto", required = true)
    @RequestMapping(value = BASE_PATCH + "/save", method = {RequestMethod.POST})
    public Result<Boolean> save(@RequestBody UmcRoleDto umcRoleDto) {

        return Result.ok(umcRoleService.save(umcRoleDto));
    }

    @ApiOperation(value = "新增编辑角色分类", notes = "新增编辑角色分类")
    @ApiImplicitParam(name = "umcRoleCategoryDto", value = "角色分类", dataType = "UmcRoleCategoryDto", required = true)
    @RequestMapping(value = BASE_PATCH + "/saveRctg", method = {RequestMethod.POST})
    public Result<Boolean> saveRctg(@RequestBody UmcRoleCategoryDto umcRoleCategoryDto) {

        return Result.ok(umcRoleService.saveRctg(umcRoleCategoryDto));
    }

    @ApiOperation(value = "删除角色分类", notes = "角色分类删除")
    @ApiImplicitParam(name = "id", value = "角色分类Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/deleteRctg", method = {RequestMethod.GET})
    public Result<Boolean> deleteRctg(@RequestParam String id) {

        return umcRoleService.deleteRctg(id);
    }

    @ApiOperation(value = "新增编辑系统", notes = "新增编辑系统")
    @ApiImplicitParam(name = "umcSystemDto", value = "系统", dataType = "UmcSystemDto", required = true)
    @RequestMapping(value = BASE_PATCH + "/saveSystem", method = {RequestMethod.POST})
    public Result<Boolean> saveSystem(@RequestBody UmcSystemDto umcSystemDto) {

        return Result.ok(umcRoleService.saveSystem(umcSystemDto));
    }

    @ApiOperation(value = "删除系统", notes = "系统删除")
    @ApiImplicitParam(name = "id", value = "系统Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/deleteSys", method = {RequestMethod.GET})
    public Result<Boolean> deleteSys(@RequestParam String id) {

        return umcRoleService.deleteSys(id);
    }

    @ApiOperation(value = "添加角色账号关联关系", notes = "添加角色账号关联关系")
    @ApiImplicitParam(name = "accountRoleDto", value = "角色账号", dataType = "AccountRoleDto", required = true)
    @RequestMapping(value = BASE_PATCH + "/saveAccountRole", method = {RequestMethod.POST})
    public Result<Boolean> saveAccountRole(@RequestBody AccountRoleDto accountRoleDto) {

        return Result.ok(umcRoleService.saveAccountRole(accountRoleDto));
    }

    @ApiOperation(value = "删除角色账号关联关系", notes = "删除角色账号关联关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", dataType = "String", required = true),
            @ApiImplicitParam(name = "acctId", value = "账号Id", dataType = "String", required = true)
    })
    @RequestMapping(value = BASE_PATCH + "/deleteRoleAcct", method = {RequestMethod.GET})
    public Result<Boolean> deleteRoleAcct(@RequestParam String roleId, @RequestParam String acctId) {

        return Result.ok(umcRoleService.deleteRoleAcct(roleId, acctId));
    }

    @ApiOperation(value = "系统详情", notes = "系统详情")
    @ApiImplicitParam(name = "id", value = "系统Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/detailSystem", method = {RequestMethod.GET})
    public Result<UmcSystemDto> detailSystem(@RequestParam String id) {

        return Result.ok(umcRoleService.detailSystem(id));
    }

    @ApiOperation(value = "角色分类详情", notes = "角色分类详情")
    @ApiImplicitParam(name = "id", value = "角色分类Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATCH + "/detaiRctg", method = {RequestMethod.GET})
    public Result<UmcRoleCategoryDto> detaiRctg(@RequestParam String id) {

        return Result.ok(umcRoleService.detaiRctg(id));
    }

    @ApiOperation(value = "角色整棵树", notes = "角色整棵树，根节点为角色分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysId", value = "系统Id", dataType = "String", required = true)
    })
    @RequestMapping(value = BASE_PATCH + "/queryFullRoleTree", method = {RequestMethod.GET})
    public Result<List<FullTreeNode>> queryFullRoleTree(@RequestParam("sysId") String sysId) {

        return Result.ok(umcRoleService.queryFullRoleTree(sysId));
    }

    /**
     * 通过角色id查询用户信息
     *
     * @param roleId
     * @param shopCd
     * @return
     */
    @Override
    public Result<List<SecurityUser>> listUserListByRoleIdAndShopCd(String roleId, String shopCd) {
        return Result.ok(umcRoleService.listUserListByRoleIdAndShopCd(roleId, shopCd));
    }

    /**
     * 通过用户id查询所拥有的角色
     *
     * @param userId
     * @return
     */
    @ApiOperation("通过账户id查询所拥有的角色")
    @ApiImplicitParam(name = "userId", value = "用户id")
    @Override
    public Result<List<RoleDto>> findRolesByUserId(String userId) {
        return Result.ok(umcRoleService.findRolesByUserId(userId));
    }

    @Override
    public Result<List<AccountUserDto>> queryUserIdByRoleCode(@RequestParam String roleCode) {
        return Result.ok(umcRoleService.queryUserIdByRoleCode(roleCode));
    }


}
