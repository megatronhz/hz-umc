package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.UmcEmployeeDto;
import com.ey.cn.fssc.umc.entity.UmcEmpDpt;
import com.ey.cn.fssc.umc.entity.UmcEmployee;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcEmpDptRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private UmcEmpDptRepository empDptRepository;
    @Autowired
    private UmcOrgStrcService orgStrcService;

    /**
     * 新增员工
     *
     * @param umcEmployeeDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UmcEmployeeDto umcEmployeeDto) {
        // 校验是否存在
        List<UmcEmployee> umcEmployees =  employeeRepository.findByCode(umcEmployeeDto.getCode());
        if (!CollectionUtils.isEmpty(umcEmployees)) {
            throw new BizException("员工已存在");
        }
        List<String> orgIds = umcEmployeeDto.getOrgIds();
        if(CollectionUtils.isEmpty(orgIds)){
            throw new BizException("新增员工没有所属机构");
        }
        String id = UmcUtils.getUUID();
        //创建员工数据
        UmcEmployee umcEmployee = new UmcEmployee();
        BeanUtils.copyProperties(umcEmployeeDto,umcEmployee);
        umcEmployee.setEmpId(id);
        employeeRepository.save(umcEmployee);

        List<UmcEmpDpt> umcEmpDpts = Lists.newArrayList();
        UmcEmpDpt umcEmpDpt=null;
        List<UmcOrgStrc> umcOrgStrcs=  orgStrcService.queryNodeByIds(orgIds);
        if(CollectionUtils.isEmpty(umcOrgStrcs)){
            throw new BizException("没有找到相应的所属机构");
        }
        Map<String, UmcOrgStrc> orgStrcMap = umcOrgStrcs.stream().collect(Collectors.toMap(UmcOrgStrc::getId, e->e));
        //创建员工与所属机构的关联数据
        for(String e : orgIds){
            umcEmpDpt = new UmcEmpDpt();
            umcEmpDpt.setId(UmcUtils.getUUID());
            umcEmpDpt.setUmcEmployee(umcEmployee);
            umcEmpDpt.setUmcOrgStrc(orgStrcMap.get(e));
            umcEmpDpts.add(umcEmpDpt);
        }
        empDptRepository.saveAll(umcEmpDpts);

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
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String employeeId) {

        empDptRepository.deleteByEmpId(employeeId);

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
        orgCodes.add("167b1950-97e4-490d-88df-e81d16016699");
        params.put("code", pageReqDto.getCode());
        params.put("name", pageReqDto.getName());
        params.put("enabled", pageReqDto.getEnabled());
        params.put("orgCodes", orgCodes);
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
