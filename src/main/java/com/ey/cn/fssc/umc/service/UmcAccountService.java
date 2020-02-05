package com.ey.cn.fssc.umc.service;

import com.ey.cn.pi.cc.common.dto.DicValueDto;
import com.ey.cn.pi.cc.common.oauth.CustomGrantedAuthority;
import com.ey.cn.pi.cc.common.oauth.SecurityUser;
import com.ey.cn.pi.cc.common.utils.UserUtils;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.*;
import com.ey.cn.fssc.umc.entity.UmcAccount;
import com.ey.cn.fssc.umc.entity.UmcAccountRole;
import com.ey.cn.fssc.umc.entity.UmcRole;
import com.ey.cn.fssc.umc.entity.UmcSystem;
import com.ey.cn.fssc.umc.enums.AccountChangedPass;
import com.ey.cn.fssc.umc.enums.AccountStatus;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcAccountRepository;
import com.ey.cn.fssc.umc.repository.UmcAccountRoleRepository;
import com.ey.cn.fssc.umc.req.AccountPageDto;
import com.ey.cn.fssc.umc.spec.UmcAccountRoleSpec;
import com.ey.cn.fssc.umc.spec.UmcAccountSpec;
import com.ey.cn.fssc.umc.util.UmcUserUtils;
import com.ey.cn.fssc.umc.util.UmcUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述：UmcAccount Service
 *
 * @author king
 * @date 2019-03-18
 */

@Slf4j
@Service
@Transactional
public class UmcAccountService {

    @Autowired
    private UmcAccountRepository repository;

    @Autowired
    private UmcAccountRoleRepository accountRoleRepository;

//    @Autowired
//    private MdmUserApi mdmUserApi;

    @Autowired
    private UmcAccountRoleService umcAccountRoleService;

    @Autowired
    private UmcRoleService umcRoleService;

    @Autowired
    private UmcRoleResourcesService resourcesService;

//    @Autowired
//    private CommonApi commonApi;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
////    private ShopApi shopApi;


    public boolean save(UmcAccountDto source) {
        if (!CollectionUtils.isEmpty(source.getUmcRoleIds())) {
            List<RoleDto> roleDtos = umcRoleService.queryByIds(source.getUmcRoleIds());
            source.setUmcRoleDto(roleDtos);
        }
        if (StringUtils.isNotBlank(source.getSnId())) {
            source.setUserId(source.getSnId());
        }

        if (StringUtils.isNotEmpty(source.getAcctId())) {
            return this.update(source.getAcctId(), source);
        } else {
            return this.create(source);
        }
    }

    /**
     * 创建UmcAccount
     *
     * @param umcAccountDto 账号dto
     * @return 成功true 失败false
     */
    public boolean create(UmcAccountDto umcAccountDto) {
        /*
         * 判断账号不能重复
         */
        UmcAccount umcAccount = repository.findByAccount(umcAccountDto.getAccount());
        if (umcAccount != null) {
            throw new BizException("账号已存在");
        }

        UmcAccount umcAccountTarget = new UmcAccount();
        BeanUtils.copyProperties(umcAccountDto, umcAccountTarget);
        //固定字段设置
        //状态
        umcAccountTarget.setStatus(AccountStatus.ENABLE);
        umcAccountTarget.setChangedPass(AccountChangedPass.NEED_CHANGE);
        //初始密码
        umcAccountTarget.setPassword(passwordEncoder.encode(Constant.INIT_PASSWORD));
        //删除标识
        umcAccountTarget.setIsDeleted(false);
        //创建人
        umcAccountTarget.setCreateUser(UmcUserUtils.getCurrentUserName());
//        //通过SnId查找关联用户信息
//        UserDTO userDTO = mdmUserApi.getUserByUserId(source.getUserId()).getResult();
//        if (null == userDTO) {
//            throw new BizException("关联用户不存在");
//        }

        umcAccountTarget.setUserId(umcAccountDto.getUserId());
//        target.setUserCode(userDTO.getUserCode());
//        target.setUserName(userDTO.getUserName());
//        target.setUserType(userDTO.getUserType());
        UmcSystem umcSystem = new UmcSystem();
        umcSystem.setSysId(umcAccountDto.getSysId());
        umcAccountTarget.setUmcSystem(umcSystem);
        repository.save(umcAccountTarget);

        //保存角色账号关系
        List<RoleDto> roles = umcAccountDto.getUmcRoleDto();
        this.accountRoleBatch(roles, umcAccountTarget);
        return true;
    }

    /**
     * 账号关联角色批量新增关联关系
     *
     * @param roles   角色列表
     * @param account 账号
     */
    public void accountRoleBatch(List<RoleDto> roles, UmcAccount account) {
        if (!CollectionUtils.isEmpty(roles)) {
            List<UmcAccountRole> targets = new ArrayList<>();
            for (RoleDto role : roles) {
                UmcAccountRole umcAccountRole = new UmcAccountRole();
                UmcRole umcRole = new UmcRole();
                BeanUtils.copyProperties(role, umcRole);
                umcAccountRole.setUmcRole(umcRole);
                umcAccountRole.setUmcAccount(account);
                targets.add(umcAccountRole);
            }
            umcAccountRoleService.batch(targets);
        }
    }

    /**
     * 根据ID删除UmcAccount
     *
     * @param id 账号主键id
     * @return 成功true 失败false
     */
    public boolean delete(String id) {

        //第一步：删除角色账号关联关系
        umcAccountRoleService.deleteByAcctId(id);
        //第二步：删除账号
        repository.deleteById(id);
        return true;

    }

    /**
     * 账号状态更新
     *
     * @param acctId 账号
     * @param status 状态
     * @return 成功true 失败false
     */
    public boolean updateStatus(String acctId, AccountStatus status) {
        UmcAccount umcAccount = repository.findById(acctId).get();
        if (null == umcAccount) {
            throw new RuntimeException("账号记录不存在");
        }
        umcAccount.setStatus(status);
        repository.save(umcAccount);
        return true;
    }

    /**
     * 重置密码
     *
     * @param acctId 账号主键id
     * @return 成功true 失败false
     */
    public boolean updatePassWord(String acctId) {
        UmcAccount umcAccount = repository.findById(acctId).get();
        if (null == umcAccount) {
            throw new BizException("账号记录不存在");
        }
        umcAccount.setPassword(passwordEncoder.encode(Constant.INIT_PASSWORD));
        umcAccount.setChangedPass(AccountChangedPass.CHANGED);
        repository.save(umcAccount);
        return true;

    }

    /**
     * 更新UmcAccount
     *
     * @param id            账号id
     * @param umcAccountDto 账号更新参数
     * @return 成功:true   失败:false
     */
    public boolean update(String id, UmcAccountDto umcAccountDto) {

        UmcAccount umcAccountEntity = repository.findById(id).orElse(null);
        if (null == umcAccountEntity) {
            throw new BizException("账号记录不存在");
        }

        /*
         * 判断账号不能重复
         */
        if (!umcAccountDto.getAccount().equals(umcAccountEntity.getAccount())) {
            UmcAccount umcAccount = repository.findByAccount(umcAccountDto.getAccount());
            if (umcAccount != null) {
                throw new BizException("账号已存在");
            }
            umcAccountEntity.setAccount(umcAccountDto.getAccount());
        }

        if (!umcAccountDto.getUserId().equals(umcAccountEntity.getUserId())) {
//            UserDTO userDTO = mdmUserApi.getUserByUserId(source.getUserId()).getResult();
//            if (null == userDTO) {
//                throw new BizException("关联用户不存在");
//            }
            umcAccountEntity.setUserId(umcAccountDto.getUserId());
            umcAccountEntity.setUserCode(umcAccountDto.getUserCode());
            umcAccountEntity.setUserName(umcAccountDto.getUserName());
            umcAccountEntity.setUserType(umcAccountDto.getUserType());
        }
        if (!umcAccountDto.getSysId().equals(umcAccountEntity.getUmcSystem().getSysId())) {
            UmcSystem umcSystem = new UmcSystem();
            umcSystem.setSysId(umcAccountDto.getSysId());
            umcAccountEntity.setUmcSystem(umcSystem);
        }
        //更新人
        umcAccountEntity.setUpdateUser(UmcUserUtils.getCurrentUserName());
        repository.save(umcAccountEntity);

        /*
         * 更新角色账号关系
         */
        List<RoleDto> roles = umcAccountDto.getUmcRoleDto();
        //第一步，首先删除所有关联关系
        umcAccountRoleService.deleteByAcctId(umcAccountDto.getAcctId());
        //第二步：批量新增角色账号关联关系
        this.accountRoleBatch(roles, umcAccountEntity);
        return true;

    }

    /**
     * 根据ID查询UmcAccount
     *
     * @param id 主键id
     * @return 账号dto
     */
    public UmcAccountDto detail(String id) {

        UmcAccount umcAccount = repository.findById(id).orElse(null);
        if (null == umcAccount) {
            throw new BizException("账号记录不存在");
        }
        UmcAccountDto umcAccountDto = new UmcAccountDto();
        BeanUtils.copyProperties(umcAccount, umcAccountDto);

        umcAccountDto.setUserTypeName(this.getStrcTypeName(umcAccount.getUserType()));
        umcAccountDto.setSnId(umcAccount.getUserId());

        //系统
        umcAccountDto.setSysId(umcAccount.getUmcSystem().getSysId());
        umcAccountDto.setSysName(umcAccount.getUmcSystem().getSysName());

        //角色ID
        if (!CollectionUtils.isEmpty(umcAccount.getUmcAccountRoles())) {
            List<String> roleIds = umcAccount.getUmcAccountRoles().stream().map(e -> e.getUmcRole().getRoleId()).collect(Collectors.toList());
            umcAccountDto.setUmcRoleIds(roleIds);
        }

        //角色
        //**Collection<UmcAccountRole> umcAccountRoles = exist.getUmcAccountRoles();
        //**target.setUmcRoleDto(umcAccountRoles.stream().map((x) -> new RoleDto(x.getUmcRole().getRoleId(), x.getUmcRole().getRoleCode(), x.getUmcRole().getRoleName())).collect(Collectors.toList()));

        return umcAccountDto;
    }

    private boolean isOwnedAuthCode(String code) {
        for (String authCode : UserUtils.getSecurityUser().getAuthorityList()) {
            if (code.equals(authCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 分页查询UmcAccount
     *
     * @param accountPageDto 查询条件
     * @return 账号分页结果
     */
    public Page<UmcAccountDto> page(AccountPageDto accountPageDto) {
        List<String> userIds = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        params.put("account", accountPageDto.getAccount());
        params.put("snName", accountPageDto.getSnName());
        params.put("sysId", accountPageDto.getSysId());
        params.put("type", accountPageDto.getUserType());
        UmcAccountSpec specification = new UmcAccountSpec(params);
        Pageable pageable = UmcUtils.makePageable(accountPageDto);
        List<UmcAccountDto> list = new ArrayList<>();
        Page<UmcAccount> nodePage = repository.findAll(specification, pageable);
        for (UmcAccount node : nodePage) {
            UmcAccountDto umcAccountDto = new UmcAccountDto();
            umcAccountDto.setAcctId(node.getAcctId());
            umcAccountDto.setUserCode(node.getUserCode());
            umcAccountDto.setChangedPass(node.getChangedPass());
            umcAccountDto.setStatus(node.getStatus());
            umcAccountDto.setStatusDesc(AccountStatus.getDesc(node.getStatus()));
            umcAccountDto.setAccount(node.getAccount());
            umcAccountDto.setSysId(node.getUmcSystem().getSysId());
            umcAccountDto.setSysName(node.getUmcSystem().getSysName());
            umcAccountDto.setUserTypeName("EMPLOYEE".equals(node.getUserType()) ? "内部员工" : "外部客户");
            umcAccountDto.setUserId(node.getUserId());
            umcAccountDto.setUserName(node.getUserName());
            umcAccountDto.setUserType(node.getUserType());
            List<RoleDto> roleList = new ArrayList<>();
            Set<UmcRoleCategoryDto> roleCategoryDtoSet = new HashSet<>();
            if (!CollectionUtils.isEmpty(node.getUmcAccountRoles())) {
                for (UmcAccountRole accountRole : node.getUmcAccountRoles()) {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setRoleCode(accountRole.getUmcRole().getRoleCode());
                    roleDto.setRoleId(accountRole.getUmcRole().getRoleId());
                    roleDto.setRoleName(accountRole.getUmcRole().getRoleName());
                    roleList.add(roleDto);

                    UmcRoleCategoryDto roleCategoryDto = new UmcRoleCategoryDto();
                    roleCategoryDto.setSysId(accountRole.getUmcRole().getUmcRoleCategory().getUmcSystem().getSysId());
                    roleCategoryDto.setCtgryId(accountRole.getUmcRole().getUmcRoleCategory().getCtgryId());
                    roleCategoryDto.setCtgryCode(accountRole.getUmcRole().getUmcRoleCategory().getCtgryCode());
                    roleCategoryDto.setCtgryName(accountRole.getUmcRole().getUmcRoleCategory().getCtgryName());
                    roleCategoryDtoSet.add(roleCategoryDto);
                }
            }
            umcAccountDto.setUmcRoleDto(roleList);
            umcAccountDto.setUmcRoleCtgyDtos(Lists.newArrayList(roleCategoryDtoSet));
            list.add(umcAccountDto);
        }
        return new PageImpl<>(list, nodePage.getPageable(), nodePage.getTotalElements());
    }

    public Page<AccountDto> pageAccount(AccountPageDto accountPageDto) {

        Pageable pageable = PageRequest.of(accountPageDto.getPage() - 1, accountPageDto.getLimit());
        return repository.pageAccount("%" + UmcUtils.nulltoStr(accountPageDto.getAccount()) + "%", pageable);
    }


    /**
     * 通过角色ID获得账号关联用户详细信息
     *
     * @param roleId 角色Id
     * @return 用户列表
     */
    public List<UmcAccountDto> queryAcctByRoleId(String roleId) {
        List<UmcAccountDto> umcAccountDtos = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("roleId", roleId);
        UmcAccountRoleSpec specification = new UmcAccountRoleSpec(params);
        List<UmcAccountRole> acctRoles = accountRoleRepository.findAll(specification);
        for (UmcAccountRole acctRole :
                acctRoles) {
            UmcAccountDto umcAccountDto = new UmcAccountDto();
            UmcAccount acct = acctRole.getUmcAccount();
            String[] excludeStr = new String[]{"umcAccountRoles"};
            BeanUtils.copyProperties(acct, umcAccountDto, excludeStr);
            //系统
            umcAccountDto.setSysId(acct.getUmcSystem().getSysId());
            umcAccountDto.setSysName(acct.getUmcSystem().getSysName());
            umcAccountDto.setUserId(acct.getUserId());
            umcAccountDtos.add(umcAccountDto);
        }
        return umcAccountDtos;
    }

    /**
     * 关联用户模糊搜索
     *
     * @param userType 用户类型 （精确查询）
     * @param userName 用户名称 （模糊搜索）
     * @return 用户列表
     */
    public List<UmcStrcNodeDto> queryUser(String userType, String userName) {
//        List<UserDTO> users = mdmUserApi.queryUser(userType, UmcUtils.nulltoStr(userName)).getResult();

        ////// 这里需要过滤已存在账号的用户

        // 查询所有拥有账号的用户
        List<UmcAccountDto> rs = repository.findAllAccount();
        List<String> exists = rs.stream().map(a -> a.getUserId()).collect(Collectors.toList());

//        if (CollectionUtils.isEmpty(users)) {
//            return Collections.emptyList();
//        }
//        return users.stream().filter(u -> !exists.contains(u.getUserId())).map(u -> new UmcStrcNodeDto(u.getUserId(), u.getUserType(), u.getUserCode(), u.getUserName(), false)).collect(Collectors.toList());
        return new ArrayList<>();
    }

    /**
     * 获得用户类型字典列表
     *
     * @return 用户类型字典列表
     */
    public List<DicValueDto> queryStrcType() {
//        Result<List<DicValueDto>> dicStrcType = commonApi.queryListByAttrCode(DicAttrCode.STRCTYPE.toString());
//        if (dicStrcType.isSuccess()) {
//            return dicStrcType.getResult();
//        } else {
//            throw new BizException(dicStrcType.getMessage());
//        }

        return new ArrayList<>();
    }

    /**
     * 通过用户类型编码得到用户类型名称
     *
     * @param code 用户类型编码
     * @return 用户类型名称
     */
    public String getStrcTypeName(String code) {
        if (null == code) {
            return "";
        }
        List<DicValueDto> strcTypeList = this.queryStrcType();
        for (DicValueDto dic :
                strcTypeList) {
            if (code.equals(dic.getCode())) {
                return dic.getName();
            }
        }
        return "";
    }

    /**
     * 根据手机号码获取用户
     *
     * @param phone
     * @return
     */
    public SecurityUser getUserByPhone(String phone) {
//        UserDTO node = mdmUserApi.getUserByPhone(phone).getResult();
//        if (null == node) {
//            return null;
//        }
//
//        UmcAccount account = repository.findByUserId(node.getUserId());
//
//        if (null == account) {
//            return null;
//        }
//        return this.getUserByAccount(account.getAccount());
        return null;
    }

    /**
     * 获取request
     *
     * @return
     */
    private HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 根据账号获取用户信息
     *
     * @param account
     * @return
     */
    public SecurityUser getUserByAccount(String account) {

        UmcAccount umcAccount = repository.findByAccount(account);
        // 查询account是否禁用
        if (null == umcAccount) {
            throw new BizException("账号不存在");
        }
        log.info("account: {}, status: {}", umcAccount.getAccount(), umcAccount.getStatus());
        if (AccountStatus.DISABLED.equals(umcAccount.getStatus())) {
            log.warn("account has been disabled: {}", account);
            throw new BizException("账号被禁用，请联系管理员");
        }
        SecurityUser user = new SecurityUser();
        user.setAcctId(umcAccount.getAcctId());
        user.setAccount(account);
        user.setPassword(umcAccount.getPassword());
        user.setName(umcAccount.getUserName());
        user.setStatus(umcAccount.getChangedPass().name());
        user.setCustCode(umcAccount.getUserCode());
        user.setUserType(umcAccount.getUserType());
        user.setUserId(umcAccount.getUserId());

        List<String> resources = resourcesService.getResourcesListByAccount(umcAccount);
        user.setAuthorityList(resources);

        // 查询用户角色
        List<RoleDto> roles = accountRoleRepository.findRoleDtosByAccountId(umcAccount.getAcctId());
        if (!CollectionUtils.isEmpty(roles)) {
            user.setRoleIdList(roles.stream().map(r -> r.getRoleCode()).collect(Collectors.toList()));
        }

        // 设置权限
        if (!CollectionUtils.isEmpty(resources)) {
            List<CustomGrantedAuthority> authorities = resources.stream().map(n -> new CustomGrantedAuthority(n)).collect(Collectors.toList());
            user.setAuthorities(authorities);
        }
        return user;
    }

    /**
     * 根据账户批量查询用户
     *
     * @param accounts
     * @return
     */
    public List<SecurityUser> listUserByAccounts(List<String> accounts, String shopCd) {

        List<UmcAccount> accountDtos = repository.findAllByAccountIn(accounts);
        List<UmcAccount> needAccountUserDtos = accountDtos.stream().filter(au -> AccountStatus.ENABLE.equals(au.getStatus())).collect(Collectors.toList());
        Map<String, UmcAccount> needMap = new HashMap<>();
        needAccountUserDtos.forEach(u -> {
            needMap.put(u.getUserId(), u);
        });
        // 查询account是否禁用
        if (CollectionUtils.isEmpty(accountDtos)) {
            throw new BizException("账号不存在");
        }
        if (CollectionUtils.isEmpty(needAccountUserDtos)) {
            log.warn("account has been disabled: {}", needAccountUserDtos.toString());
            throw new BizException("账号被禁用，请联系管理员");
        }

//        List<UserDTO> nodes = mdmUserApi.getUsersByUserIds(needAccountUserDtos.stream().map(UmcAccount::getUserId).collect(Collectors.toList())).getResult();
//        if (!StringUtils.isEmpty(shopCd)) {
//            List<UserDTO> needNodes = nodes.stream().filter(n -> shopCd.equals(n.getShopCd())).collect(Collectors.toList());
//            nodes = needNodes;
//        }
//        if (CollectionUtils.isEmpty(nodes)) {
//            return null;
//        }
        List<SecurityUser> users = new ArrayList<>();
//        nodes.forEach(node -> {
//            String userId = node.getUserId();
//            UmcAccount account = needMap.get(userId);
//            SecurityUser user = new SecurityUser();
//            user.setUserId(node.getUserId());
//            user.setName(account.getUserName());
//            user.setCustCode(node.getUserCode());
//            user.setAccount(account.getAccount());
//            user.setAcctId(account.getAcctId());
//            users.add(user);
//        });

        return users;
    }


    /**
     * 根据账号获取用户名
     *
     * @param accounts
     * @return
     */
    public Map<String, String> getNamesBuAccounts(List<String> accounts) {
        List<UmcAccount> accountList = repository.findAllByAccountIn(accounts);
        Map<String, String> names = accountList.stream().collect(Collectors.toMap(UmcAccount::getAccount, account -> account.getUserName()));
        return names;
    }

    public UmcAccount findAccountByUserId(String userId) {
        UmcAccount account = repository.findByUserId(userId);
        return account;
    }

    public void updatePassword(String oldPassword, String password) {
        String acctId = UserUtils.getSecurityUser().getAcctId();
        UmcAccount umcAccount = repository.findById(acctId).get();
        if (oldPassword != null) {

            if (!passwordEncoder.matches(oldPassword, umcAccount.getPassword())) {
                throw new BizException("旧密码错误");
            }
        }
        umcAccount.setPassword(passwordEncoder.encode(password));
        umcAccount.setChangedPass(AccountChangedPass.CHANGED);
        repository.save(umcAccount);

    }

    /**
     * 通过账户id批量查询用户id
     *
     * @param acctIds
     * @return
     */
    public List<String> findUserIdsByAcctIds(List<String> acctIds) {
        List<AccountUserDto> accountUserDtos = repository.findAllByAcctIdIn(acctIds);
        List<String> userIds = accountUserDtos.stream().map(u -> u.getAcctId()).collect(Collectors.toList());
        return userIds;
    }

    /**
     * 根据账户ID查询账户信息
     * @param acctId
     * @return
     */
    public UmcAccountDto findByAcctId(String acctId){
        UmcAccount umcAccount = repository.findByAcctId(acctId);
        if(Objects.isNull(umcAccount)){
            throw new BizException("账户不存在！");
        }
        UmcAccountDto umcAccountDto = new UmcAccountDto();
        BeanUtils.copyProperties(umcAccount, umcAccountDto);

        return umcAccountDto;
    }
}