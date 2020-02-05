package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.UmcEmployeeDto;
import com.ey.cn.fssc.umc.entity.UmcEmployee;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcEmployeeRepository;
import com.ey.cn.fssc.umc.req.EmployeePageDto;
import com.ey.cn.fssc.umc.spec.UmcEmployeeSpec;
import com.ey.cn.fssc.umc.util.UmcUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 8:22 上午 2020/2/5
 */
@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private UmcEmployeeRepository employeeRepository;
    @Autowired
    private UmcAccountService accountService;
    @Autowired
    private UmcOrgStrcService orgStrcService;

    /**
     * 新增员工
     *
     * @param umcEmployeeDto
     * @return
     */
    public Boolean create(UmcEmployeeDto umcEmployeeDto) {
        // 校验是否存在
        List<UmcEmployee> umcEmployees =  employeeRepository.findByCode(umcEmployeeDto.getCode());
        if (!CollectionUtils.isEmpty(umcEmployees)) {
            throw new BizException("员工已存在");
        }
        UmcEmployee umcEmployee = new UmcEmployee();
        BeanUtils.copyProperties(umcEmployeeDto,umcEmployee);
        employeeRepository.save(umcEmployee);
        return true;
    }

    /**
     * 编辑员工
     *
     * @param umcEmployeeDto
     * @return
     */
    public Boolean update(UmcEmployeeDto umcEmployeeDto) {
        // 校验是否存在
        List<UmcEmployee> umcEmployees =  employeeRepository.findByCode(umcEmployeeDto.getCode());
        if (CollectionUtils.isEmpty(umcEmployees)) {
            throw new BizException("员工不存在");
        }
        UmcEmployee umcEmployee = new UmcEmployee();
        BeanUtils.copyProperties(umcEmployeeDto,umcEmployee);
        employeeRepository.save(umcEmployee);
        return true;
    }

    /**
     * 删除员工
     *
     * @param employeeId
     * @return
     */
    public Boolean delete(String employeeId) {
        employeeRepository.deleteById(employeeId);
        return true;
    }

    /**
     * 员工详情
     *
     * @param employeeId
     * @return
     */
    public UmcEmployeeDto detail(String employeeId) {

        UmcEmployee umcEmployee = employeeRepository.findById(employeeId).orElse(new UmcEmployee());

        if (Objects.isNull(umcEmployee)) {
            throw new BizException("员工不存在");
        }
        UmcEmployeeDto umcEmployeeDto = new UmcEmployeeDto();
        BeanUtils.copyProperties(umcEmployee, umcEmployeeDto);
        return umcEmployeeDto;
    }

    /**
     * 员工分页查询
     *
     * @param pageReqDto
     * @return
     */
    public Page<UmcEmployeeDto> page(EmployeePageDto pageReqDto) {
        Map<String, Object> params = new HashMap<>();
        List<String> orgCodes = orgStrcService.queryOrgCode();
        params.put("code", pageReqDto.getCode());
        params.put("name", pageReqDto.getName());
        params.put("enabled", pageReqDto.getEnabled());
        params.put("orgCode", orgCodes);
        UmcEmployeeSpec specification = new UmcEmployeeSpec(params);
        Pageable pageable = UmcUtils.makePageable(pageReqDto);
        List<UmcEmployeeDto> list = new ArrayList<>();
        Page<UmcEmployee> nodePage = employeeRepository.findAll(specification, pageable);
        for (UmcEmployee node : nodePage) {
            UmcEmployeeDto employeeDto = new UmcEmployeeDto();
            BeanUtils.copyProperties(node, employeeDto);
            list.add(employeeDto);
        }

        return new PageImpl<>(list, nodePage.getPageable(), nodePage.getTotalElements());
    }

    /**
     * 员工查询（不分页）
     *
     * @param keyWord
     * @return
     */
    public List<UmcEmployeeDto> findList(String keyWord) {
        List<UmcEmployeeDto> umcEmployeeDtoList = Lists.newArrayList();
        List<String> orgCodes = orgStrcService.queryOrgCode();
        List<UmcEmployee> umcEmployeeList = employeeRepository.findeByKeyWord(orgCodes, keyWord);
        if(!CollectionUtils.isEmpty(umcEmployeeList)){
            umcEmployeeList.forEach(e->{
                UmcEmployeeDto dto = new UmcEmployeeDto();
                BeanUtils.copyProperties(e, dto);
                umcEmployeeDtoList.add(dto);
            });
        }
        return umcEmployeeDtoList;
    }
}
