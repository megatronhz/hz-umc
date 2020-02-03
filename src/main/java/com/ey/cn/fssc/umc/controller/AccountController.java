package com.ey.cn.fssc.umc.controller;

import com.ey.cn.pi.cc.common.Result;
import com.ey.cn.pi.cc.common.dto.DicValueDto;
import com.ey.cn.pi.cc.common.oauth.SecurityUser;
import com.ey.cn.fssc.umc.api.AccountApi;
import com.ey.cn.fssc.umc.dto.AccountDto;
import com.ey.cn.fssc.umc.dto.UmcAccountDto;
import com.ey.cn.fssc.umc.dto.UmcStrcNodeDto;
import com.ey.cn.fssc.umc.enums.AccountStatus;
import com.ey.cn.fssc.umc.req.AccountPageDto;
import com.ey.cn.fssc.umc.req.PasswordDto;
import com.ey.cn.fssc.umc.service.UmcAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <账号管理业务控制层>
 *
 * @author wangqixia
 * @version 1.0 2019/03/19
 **/
@Slf4j
@Api(tags = "账号管理接口", description = "账号管理接口")
@RestController
public class AccountController implements AccountApi {

    @Autowired
    private UmcAccountService umcAccountService;

    @ApiOperation(value = "账号密码重置", notes = "账号密码重置")
    @ApiImplicitParam(name = "acctId", value = "账号Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/updatePassWord", method = {RequestMethod.GET})
    public Result<Boolean> updatePassWord(@RequestParam String acctId) {

        return Result.ok(umcAccountService.updatePassWord(acctId));
    }

    @ApiOperation(value = "账号启用", notes = "账号启用")
    @ApiImplicitParam(name = "acctId", value = "账号Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/acctEnable", method = {RequestMethod.GET})
    public Result<Boolean> acctEnable(@RequestParam String acctId) {

        return Result.ok(umcAccountService.updateStatus(acctId, AccountStatus.ENABLE));
    }

    @ApiOperation(value = "账号禁用", notes = "账号禁用")
    @ApiImplicitParam(name = "acctId", value = "账号Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/acctDisabled", method = {RequestMethod.GET})
    public Result<Boolean> acctDisabled(@RequestParam String acctId) {

        return Result.ok(umcAccountService.updateStatus(acctId, AccountStatus.DISABLED));
    }

    @ApiOperation(value = "账号详情", notes = "账号详情")
    @ApiImplicitParam(name = "id", value = "账号Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/detail", method = {RequestMethod.GET})
    public Result<UmcAccountDto> detail(@RequestParam String id) {

        return Result.ok(umcAccountService.detail(id));
    }

    @ApiOperation(value = "账号删除", notes = "账号删除")
    @ApiImplicitParam(name = "id", value = "账号Id", dataType = "String", required = true)
    @RequestMapping(value = BASE_PATH + "/delete", method = {RequestMethod.GET})
    public Result<Boolean> delete(@RequestParam String id) {

        return Result.ok(umcAccountService.delete(id));
    }

    @ApiOperation(value = "新增编辑账号", notes = "新增编辑账号")
    @ApiImplicitParam(name = "umcAccountDto", value = "账号", dataType = "UmcAccountDto", required = true)
    @RequestMapping(value = BASE_PATH + "/save", method = {RequestMethod.POST})
    public Result<Boolean> save(@RequestBody UmcAccountDto umcAccountDto) {

        return Result.ok(umcAccountService.save(umcAccountDto));
    }

    @ApiOperation(value = "账号分页查询", notes = "账号分页查询")
    @ApiImplicitParam(name = "accountPageDto", dataType = "AccountPageDto", required = true)
    @RequestMapping(value = BASE_PATH + "/page", method = {RequestMethod.POST})
    public Result<Page<UmcAccountDto>> page(@RequestBody AccountPageDto accountPageDto) {

        return Result.ok(umcAccountService.page(accountPageDto));
    }

    @ApiOperation(value = "账号分页查询-角色关联账号分页页面", notes = "账号分页查询-角色关联账号分页页面")
    @ApiImplicitParam(name = "accountPageDto", dataType = "AccountPageDto", required = true)
    @RequestMapping(value = BASE_PATH + "/pageAccount", method = {RequestMethod.POST})
    public Result<Page<AccountDto>> pageAccount(@RequestBody AccountPageDto accountPageDto) {

        return Result.ok(umcAccountService.pageAccount(accountPageDto));
    }

    @ApiOperation(value = "通过角色查询账号用户详情列表", notes = "通过角色查询账号用户详情列表")
    @ApiImplicitParam(name = "roleId", dataType = "string", required = true)
    @RequestMapping(value = BASE_PATH + "/queryAcctByRoleId", method = {RequestMethod.GET})
    public Result<List<UmcAccountDto>> queryAcctByRoleId(@RequestParam String roleId) {

        return Result.ok(umcAccountService.queryAcctByRoleId(roleId));
    }

    @ApiOperation(value = "关联用户模糊搜索", notes = "关联用户模糊搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userType", value = "用户类型", dataType = "string", required = true),
            @ApiImplicitParam(name = "userName", value = "用户名称", dataType = "string")
    })
    @RequestMapping(value = BASE_PATH + "/queryUser", method = {RequestMethod.GET})
    public Result<List<UmcStrcNodeDto>> queryUser(@RequestParam String userType, String userName) {

        return Result.ok(umcAccountService.queryUser(userType, userName));
    }

    @ApiOperation(value = "用户类型数据字典列表", notes = "用户类型数据字典列表")
    @RequestMapping(value = BASE_PATH + "/dicStrcType", method = {RequestMethod.GET})
    public Result<List<DicValueDto>> dicStrcType() {

        return Result.ok(umcAccountService.queryStrcType());
    }

    @ApiOperation(value = "根据账号获取账号详细信息", notes = "根据账号获取账号详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", dataType = "string", required = true)
    })
    @Override
    public Result<SecurityUser> getUser(@PathVariable String account) {
        return Result.ok(umcAccountService.getUserByAccount(account));
    }

    @Override
    @ApiOperation(value = "根据手机号获取账号详细信息", notes = "根据手机号获取账号详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", dataType = "string", required = true)
    })
    public Result<SecurityUser> getUserByPhone(@PathVariable String phone) {
        return Result.ok(umcAccountService.getUserByPhone(phone));
    }

    @Override
    @ApiOperation(value = "根据账号获取名称", notes = "根据账号获取名称")
    public Result<Map<String, String>> getNamesBuAccounts(List<String> accounts) {
        return Result.ok(umcAccountService.getNamesBuAccounts(accounts));
    }

    @Override
    public Result<List<String>> findUserIdsByAcctIds(List<String> acctIds) {
        return Result.ok(umcAccountService.findUserIdsByAcctIds(acctIds));
    }

    @ApiOperation("修改密码")
    @RequestMapping(value = BASE_PATH + "/changepass", method = {RequestMethod.POST})
    public Result<Boolean> updatePassword(@RequestBody PasswordDto passwordDto) {
        umcAccountService.updatePassword(passwordDto.getOldPassword(), passwordDto.getPassword());
        return Result.ok();
    }


}
