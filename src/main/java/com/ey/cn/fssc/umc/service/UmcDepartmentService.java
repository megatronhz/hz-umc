package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.UmcCompanyDto;
import com.ey.cn.fssc.umc.dto.UmcDepartmentDto;
import com.ey.cn.fssc.umc.entity.UmcDepartment;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcDepartmentRepository;
import com.ey.cn.fssc.umc.req.DepartmentPageDto;
import com.ey.cn.fssc.umc.spec.UmcDepartmentSpec;
import com.ey.cn.fssc.umc.util.UmcUtils;
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

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 11:21 上午 2020/2/6
 */
@Slf4j
@Service
public class UmcDepartmentService {
    @Autowired
    private UmcDepartmentRepository departmentRepository;

    @Autowired
    private UmcOrgStrcService orgStrcService;

    /**
     * 新增部门
     *
     * @param umcDepartmentDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UmcDepartmentDto umcDepartmentDto) {
        // 校验是否存在
        List<UmcDepartment> umcDepartments = departmentRepository.findByCode(umcDepartmentDto.getCode());
        if (!CollectionUtils.isEmpty(umcDepartments)) {
            throw new BizException("部门已存在");
        }
        String id = UUID.randomUUID().toString();
        umcDepartmentDto.setDptId(id);

        this.createRootTreeNode(umcDepartmentDto);

        UmcDepartment umcDepartment = new UmcDepartment();
        BeanUtils.copyProperties(umcDepartmentDto, umcDepartment);
        departmentRepository.save(umcDepartment);
        return true;
    }

    /**
     * 创建组织架构根节点
     *
     * @param departmentDto
     */
    private void createRootTreeNode(UmcDepartmentDto departmentDto) {
        // 判断是否为子公司
        Boolean isLeaf = true;

        UmcOrgStrc rootNode = new UmcOrgStrc();

        rootNode.setParentId(departmentDto.getCoId());
        rootNode.setId(departmentDto.getDptId());
        rootNode.setName(departmentDto.getName());
        rootNode.setCode(departmentDto.getCode());
        rootNode.setType(Constant.NodeType.DEPARTMENT);
        rootNode.setIsLeaf(isLeaf);
        orgStrcService.createNode(rootNode);
    }


    /**
     * 编辑部门
     *
     * @param umcDepartmentDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UmcDepartmentDto umcDepartmentDto) {
        // 校验是否存在
        List<UmcDepartment> umcDepartments = departmentRepository.findByCode(umcDepartmentDto.getCode());
        if (CollectionUtils.isEmpty(umcDepartments)) {
            throw new BizException("部门不存在");
        }
        this.updateRootTreeNode(umcDepartmentDto);

        UmcDepartment umcDepartment = new UmcDepartment();
        BeanUtils.copyProperties(umcDepartmentDto, umcDepartment);
        departmentRepository.save(umcDepartment);
        return true;
    }

    /**
     * 编辑组织架构根节点
     *
     * @param umcDepartmentDto
     */
    private void updateRootTreeNode(UmcDepartmentDto umcDepartmentDto) {

        UmcOrgStrc rootNode = orgStrcService.detailNodeById(umcDepartmentDto.getDptId());
        rootNode.setName(umcDepartmentDto.getName());
        orgStrcService.updateNode(rootNode);
    }

    /**
     * 删除部门
     *
     * @param dptId
     * @return
     */
    public Boolean delete(String dptId) {
        //TODO 需要判断部门下是否有员工，如果有员工就不能删除
        List<UmcOrgStrc> orgStrcList = orgStrcService.queryNodeByParentId(dptId);
        if (!CollectionUtils.isEmpty(orgStrcList)) {
            throw new BizException("部门下存在数据，不能删除！");
        }
        departmentRepository.deleteById(dptId);
        return true;
    }

    /**
     * 部门详情
     *
     * @param dptId
     * @return
     */
    public UmcDepartmentDto detail(String dptId) {

        UmcDepartment umcDepartment = departmentRepository.findById(dptId).orElse(new UmcDepartment());

        if (Objects.isNull(umcDepartment)) {
            throw new BizException("部门不存在");
        }
        UmcDepartmentDto umcDepartmentDto = new UmcDepartmentDto();
        BeanUtils.copyProperties(umcDepartment, umcDepartmentDto);
        return umcDepartmentDto;
    }

    /**
     * 部门分页查询
     *
     * @param pageReqDto
     * @return
     */
    public Page<UmcDepartmentDto> page(DepartmentPageDto pageReqDto) {
        Map<String, Object> params = new HashMap<>();
        List<String> orgCodes = orgStrcService.queryOrgCode();
        params.put("code", pageReqDto.getCode());
        params.put("name", pageReqDto.getName());
        params.put("orgCode", orgCodes);
        UmcDepartmentSpec specification = new UmcDepartmentSpec(params);
        Pageable pageable = UmcUtils.makePageable(pageReqDto);
        List<UmcDepartmentDto> list = new ArrayList<>();
        Page<UmcDepartment> nodePage = departmentRepository.findAll(specification, pageable);
        for (UmcDepartment node : nodePage) {
            UmcDepartmentDto departmentDto = new UmcDepartmentDto();
            BeanUtils.copyProperties(node, departmentDto);
            list.add(departmentDto);
        }

        return new PageImpl<>(list, nodePage.getPageable(), nodePage.getTotalElements());
    }

}
