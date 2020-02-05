package com.ey.cn.fssc.umc.controller;

import com.alibaba.fastjson.JSONObject;
import com.ey.cn.fssc.umc.api.Constant;
import com.ey.cn.fssc.umc.dto.UmcOrgStrcItemDto;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import com.ey.cn.fssc.umc.service.UmcOrgStrcService;
import com.ey.cn.pi.cc.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 1:30 下午 2020/2/4
 */
@Slf4j
@Api(tags = "组织架构管理", description = "组织架构管理")
@RestController
public class OrgStrcController {
    @Autowired
    private UmcOrgStrcService orgStrcService;
    private static final String BASE_PATCH = Constant.CONTEXT_PATH  + "/tree";

    @ApiOperation(value = "树展开", notes = "组织架构树展开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "架构类型", dataType = "string", required = true),
            @ApiImplicitParam(name = "parentId", value = "上级节点ID", dataType = "string", required = false)
    })
    @RequestMapping(value = BASE_PATCH + "/unfold", method = {RequestMethod.GET})
    //@PreAuthorize("hasAuthority('RB_ORG_TREE_UNFOLD')")
    public Result<List<UmcOrgStrc>> unfoldTree(@RequestParam("type") String type,
                                               String parentId) {

        return Result.ok(orgStrcService.unfoldTree(type, parentId));
    }

    @ApiOperation(value = "节点更名", notes = "组织架构节点更名")
    @RequestMapping(value = BASE_PATCH + "/rename", method = {RequestMethod.GET})
    public Result<Boolean> renameNode(@RequestParam String nodeCode, @RequestParam String nodeName) {

        orgStrcService.rename(nodeCode, nodeName);
        return Result.ok();
    }

    @ApiOperation(value = "下级节点分页查询", notes = "当前节点的下级节点分页查询")
    @RequestMapping(value = BASE_PATCH + "/page", method = {RequestMethod.GET})
    //@PreAuthorize("hasAuthority('RB_ORG_TREE_PAGE')")
    public Result<Page<UmcOrgStrc>> pageSubNode(@RequestParam Integer pageNo, @RequestParam Integer pageSize,
                                              @RequestParam String nodeId,
                                              @RequestParam(required = false) String nodeCode,
                                              @RequestParam(required = false) String nodeName) {

        return Result.ok(orgStrcService.pageSubNode(pageNo, pageSize, nodeId, nodeCode, nodeName));
    }

    @ApiOperation(value = "节点删除", notes = "节点删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "组织架构树类型", paramType = "string", required = true),
            @ApiImplicitParam(name = "nodeId", value = "节点ID", paramType = "string", required = true)
    })
    @RequestMapping(value = BASE_PATCH + "/deleteNode", method = {RequestMethod.GET})
    //@PreAuthorize("hasAuthority('RB_ORG_TREE_DELETE')")
    public Result<Boolean> delete(@RequestParam("nodeId") String nodeId) {
        orgStrcService.delete(nodeId);
        return Result.ok();
    }

    @ApiOperation(value = "创建下级节点", notes = "创建下级节点")
    @RequestMapping(value = BASE_PATCH + "/createSubNode", method = {RequestMethod.POST})
    //@PreAuthorize("hasAuthority('RB_ORG_TREE_CREATE')")
    public Result<Boolean> userDetail(@RequestBody UmcOrgStrc subNode) {

        return Result.ok(orgStrcService.createNode(subNode));
    }

    @ApiOperation(value = "下级节点搜索", notes = "下级节点搜索")
    @RequestMapping(value = BASE_PATCH + "/codeSearch", method = {RequestMethod.GET})
    public Result<List<UmcOrgStrcItemDto>> codeSearch(@RequestParam String keyWord, @RequestParam String type) {

        return Result.ok(orgStrcService.codeSearch(keyWord, type));
    }

//    @ApiOperation(value = "查询销售区域", notes = "查询销售区域")
//    @RequestMapping(value = BASE_PATCH + "/querySalesRgn", method = {RequestMethod.GET})
//    public Result<List<TreeNode>> querySalesRgn() {
//        List<TreeNode> list = treeService.querySalesRgn();
//        return Result.ok(list);
//    }
//
//    @ApiOperation(value = "test", notes = "test")
//    @RequestMapping(value = BASE_PATCH + "/test", method = {RequestMethod.GET})
//    public Result<String> test() {
//        treeService.isGroupUser();
//        return Result.ok();
//    }
//
//    @ApiOperation(value = "查询查询当前用户所拥有的销售分公司", notes = "查询查询当前用户所拥有的销售分公司")
//    @RequestMapping(value = BASE_PATCH + "/findCurrentUserSalesOrg", method = {RequestMethod.GET})
//    public Result<List<JSONObject>> findCurrentUserSalesOrg() {
//        return Result.ok(treeService.querySalesOrgByCurrentUser());
//    }
//
//    @ApiOperation(value = "查询查询当前用户是否是集团用户", notes = "查询查询当前用户是否是集团用户")
//    @RequestMapping(value = BASE_PATCH + "/isGroupUser", method = {RequestMethod.GET})
//    public Result<Boolean> isGroupUser() {
//        return Result.ok(treeService.isGroupUser());
//    }
}
