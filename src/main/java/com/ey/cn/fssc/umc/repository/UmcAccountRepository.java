package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.dto.AccountDto;
import com.ey.cn.fssc.umc.dto.AccountUserDto;
import com.ey.cn.fssc.umc.dto.UmcAccountDto;
import com.ey.cn.fssc.umc.entity.UmcAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：UmcAccount Repository
 *
 * @author king
 * @date 2019-03-18
 */

@Repository
public interface UmcAccountRepository extends JpaRepository<UmcAccount, String>, JpaSpecificationExecutor<UmcAccount> {

    UmcAccount findByAccount(String account);

    UmcAccount findByUserId(String userId);

    @Query("select new com.ey.cn.fssc.umc.dto.AccountDto(u.acctId, u.userName, u.account) from UmcAccount u  where u.isDeleted = false and u.account like ?1 ")
    Page<AccountDto> pageAccount(String account, Pageable pageable);

    List<UmcAccount> findAllByAccountIn(List<String> accounts);

    UmcAccount findByAcctId(String acctId);

    @Query("select new com.ey.cn.fssc.umc.dto.AccountUserDto(u.acctId,u.account,u.userId,u.userName,u.userCode,u.status) from UmcAccount u where u.account in ?1 ")
    List<AccountUserDto> findAccountUserDtoByAccountIn(List<String> accounts);

    @Query("select new com.ey.cn.fssc.umc.dto.AccountUserDto(u.acctId,u.account,u.userId,u.userName,u.userCode,u.status) from UmcAccount u where u.acctId in ?1 ")
    List<AccountUserDto> findAllByAcctIdIn(List<String> acctIds);

    @Query("select new com.ey.cn.fssc.umc.dto.UmcAccountDto(u.userId) from UmcAccount u  where u.isDeleted = false ")
    List<UmcAccountDto> findAllAccount();
}