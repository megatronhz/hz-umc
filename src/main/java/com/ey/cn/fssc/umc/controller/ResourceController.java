package com.ey.cn.fssc.umc.controller;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.TreeNode;
import com.ey.cn.fssc.umc.dto.UmcResourcesDto;
import com.ey.cn.fssc.umc.enums.NodeType;
import com.ey.cn.fssc.umc.service.UmcResourcesService;
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
 * <资源管理业务控制层>
 *
 * @author wangqixia
 * @version 1.0 2019/03/19
 **/
@Slf4j
@Api(tags = "资源管理接口", description = "资源管理接口")
@RestController
public class ResourceController {

    private static final String BASE_PATH = Constant.CONTEXT_PATH + "/resource";

    @Autowired
    private UmcResourcesService umcResourcesService;

    @ApiOperation(value = "系统资源树形展开", notes = "系统资源树形展开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "strcNodeType", dataType = "String", required = true),
            @ApiImplicitParam(name = "parentId", value = "上级节点ID", dataType = "String", required = true)
    })
    @RequestMapping(value = BASE_PATH + "/queryResourceTree", method = {RequestMethod.GET})
    public Result<List<TreeNode>> queryResourceTree(@RequestParam("strcNodeType") NodeType strcNodeType, @RequestParam("parentId") String parentId) {

        return Result.ok(umcResourcesService.queryResourceTree(strcNodeType, parentId));
    }

    @ApiOperation(value = "资源详情", notes = "资源详情")
    @ApiImplicitParam(name = "id", value = "资源Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/detail", method = {RequestMethod.GET})
    public Result<UmcResourcesDto> detail(@RequestParam String id) {

        return Result.ok(umcResourcesService.detail(id));
    }

    @ApiOperation(value = "资源删除", notes = "资源删除")
    @ApiImplicitParam(name = "id", value = "资源Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/delete", method = {RequestMethod.GET})
    public Result<Boolean> delete(@RequestParam String id) {

        return umcResourcesService.delete(id);
    }

    @ApiOperation(value = "新增编辑资源", notes = "新增编辑资源")
    @ApiImplicitParam(name = "umcResourcesDto", value = "资源", dataType = "UmcResourcesDto", required = true)
    @RequestMapping(value = BASE_PATH + "/save", method = {RequestMethod.POST})
    public Result<Boolean> save(@RequestBody UmcResourcesDto umcResourcesDto) {

        return Result.ok(umcResourcesService.save(umcResourcesDto));
    }

}
