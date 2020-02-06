package com.ey.cn.fssc.umc.service;

import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.UmcCompanyDto;
import com.ey.cn.fssc.umc.entity.UmcCompany;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcCompanyRepository;
import com.ey.cn.fssc.umc.req.CompanyPageDto;
import com.ey.cn.fssc.umc.spec.UmcCompanySpec;
import com.ey.cn.fssc.umc.util.UmcUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 8:22 上午 2020/2/5
 */
@Slf4j
@Service
public class CompanyService {

    @Autowired
    private UmcCompanyRepository companyRepository;

    @Autowired
    private UmcOrgStrcService orgStrcService;

    /**
     * 创建组织架构根节点
     *
     * @param companyDto
     */
    private void createRootTreeNode(UmcCompanyDto companyDto) {
        // 判断是否为子公司
        String companyType = companyDto.getType();
        Boolean isLeaf = false;

        UmcOrgStrc rootNode = new UmcOrgStrc();
        // 创建子公司
        if ("1".equals(companyType)) {
            isLeaf = true;
            rootNode.setParentId(companyDto.getParentId());
            rootNode.setType(Constant.NodeType.CHILD_COMPANY);
        }else{
            rootNode.setType(Constant.NodeType.COMPANY);
        }
        rootNode.setId(companyDto.getCoId());
        rootNode.setName(companyDto.getName());
        rootNode.setCode(companyDto.getCode());

        rootNode.setIsLeaf(isLeaf);
        orgStrcService.createNode(rootNode);
    }

    /**
     * 编辑组织架构根节点
     * @param companyDto
     */
    private void updateRootTreeNode(UmcCompanyDto companyDto) {

        UmcOrgStrc rootNode = orgStrcService.detailNodeById(companyDto.getCoId());
        rootNode.setName(companyDto.getName());
        orgStrcService.updateNode(rootNode);
    }

    /**
     * 新增公司
     *
     * @param companyDto
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UmcCompanyDto companyDto) {
        List<UmcCompany> umcCompanys = companyRepository.findByCode(companyDto.getCode());
        // 校验是否存在
        if (!CollectionUtils.isEmpty(umcCompanys)) {
            throw new BizException("公司已存在");
        }
        String id = UUID.randomUUID().toString();
        companyDto.setCoId(id);
        this.createRootTreeNode(companyDto);

        UmcCompany umcCompany = new UmcCompany();
        BeanUtils.copyProperties(companyDto, umcCompany);
        companyRepository.save(umcCompany);
        return true;
    }

    /**
     * 编辑公司
     *
     * @param companyDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UmcCompanyDto companyDto) {
        List<UmcCompany> umcCompanys = companyRepository.findByCode(companyDto.getCode());
        // 校验是否存在
        if (CollectionUtils.isEmpty(umcCompanys)) {
            throw new BizException("公司不存在");
        }
        UmcCompany umcCompany = umcCompanys.get(0);
        BeanUtils.copyProperties(companyDto, umcCompany);
        companyRepository.save(umcCompany);

        this.updateRootTreeNode(companyDto);

        return true;
    }

    /**
     * 删除公司
     *
     * @param coId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String coId) {
        List<UmcOrgStrc> orgStrcList = orgStrcService.queryNodeByParentId(coId);
        if (!CollectionUtils.isEmpty(orgStrcList)) {
            throw new BizException("组织机构下存在数据，不能删除！");
        }
        companyRepository.deleteById(coId);

        return true;
    }

    /**
     * 公司详情
     *
     * @param coId
     * @return
     */
    public UmcCompanyDto detail(String coId) {
        UmcCompany umcCompany = companyRepository.findById(coId).orElse(new UmcCompany());
        UmcCompanyDto umcCompanyDto = new UmcCompanyDto();
        BeanUtils.copyProperties(umcCompany, umcCompanyDto);
        return umcCompanyDto;
    }

    /**
     * 公司分页查询
     *
     * @param pageReqDto
     * @return
     */
    public Page<UmcCompanyDto> page(CompanyPageDto pageReqDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", pageReqDto.getCode());
        params.put("name", pageReqDto.getName());
        UmcCompanySpec specification = new UmcCompanySpec(params);
        Pageable pageable = UmcUtils.makePageable(pageReqDto);
        List<UmcCompanyDto> list = Lists.newArrayList();
        Page<UmcCompany> nodePage = companyRepository.findAll(specification, pageable);
        for (UmcCompany node : nodePage) {
            UmcCompanyDto compnayDto = new UmcCompanyDto();
            BeanUtils.copyProperties(node, compnayDto);
            list.add(compnayDto);
        }

        return new PageImpl<>(list, nodePage.getPageable(), nodePage.getTotalElements());
    }

    /**
     * 查询指定类型的公司数据
     *
     * @param type
     * @param keyWord
     * @return
     */
    public List<UmcCompanyDto> findChildCompany(String type, String keyWord) {
        List<UmcCompanyDto> umcCompanyDtos = Lists.newArrayList();
        List<UmcCompany> childCompanyList = companyRepository.findChildCompany(type, keyWord);
        if (!CollectionUtils.isEmpty(childCompanyList)) {
            childCompanyList.forEach(e -> {
                UmcCompanyDto dto = new UmcCompanyDto();
                BeanUtils.copyProperties(e, dto);
                umcCompanyDtos.add(dto);
            });
        }
        return umcCompanyDtos;
    }
}
