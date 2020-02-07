package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.dto.UmcEmpDptDto;
import com.ey.cn.fssc.umc.entity.UmcEmpDpt;
import com.ey.cn.fssc.umc.repository.UmcEmpDptRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 9:23 上午 2020/2/7
 */
@Service
public class UmcEmpDptService {
    @Autowired
    private UmcEmpDptRepository empDptRepository;

    /**
     * 根据员工ID查询所属机构ID
     * @param empId
     * @return
     */
    public List<UmcEmpDptDto> findByEmpId(String empId) {
        List<UmcEmpDptDto> umcEmpDptDtos = Lists.newArrayList();
        List<UmcEmpDpt> umcEmpDpts = empDptRepository.findByEmpId(empId);
        if (!CollectionUtils.isEmpty(umcEmpDpts)) {
            for (UmcEmpDpt ed : umcEmpDpts) {
                UmcEmpDptDto dto = new UmcEmpDptDto();
                BeanUtils.copyProperties(ed, dto);
                umcEmpDptDtos.add(dto);
            }
        }
        return umcEmpDptDtos;
    }
}
