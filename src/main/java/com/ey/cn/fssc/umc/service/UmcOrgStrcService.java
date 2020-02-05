package com.ey.cn.fssc.umc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ey.cn.fssc.umc.constant.Constant;
import com.ey.cn.fssc.umc.dto.UmcAccountDto;
import com.ey.cn.fssc.umc.dto.UmcCompanyDto;
import com.ey.cn.fssc.umc.dto.UmcEmployeeDto;
import com.ey.cn.fssc.umc.dto.UmcOrgStrcItemDto;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import com.ey.cn.fssc.umc.enums.OrgStrcTypeEnum;
import com.ey.cn.fssc.umc.exception.BizException;
import com.ey.cn.fssc.umc.repository.UmcOrgStrcRepository;
import com.ey.cn.pi.cc.common.oauth.SecurityUser;
import com.ey.cn.pi.cc.common.utils.UserUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 1:33 下午 2020/2/4
 */
@Service
public class UmcOrgStrcService {
    @Autowired
    private UmcOrgStrcRepository orgStrcRepository;
    @Autowired
    private UmcAccountService accountService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * 树展开
     *
     * @param type
     * @param parentId
     * @return
     */
    public List<UmcOrgStrc> unfoldTree(String type,
                                       String parentId) {

        List<UmcOrgStrc> result = new ArrayList<>();

        // 查询根节点
        if ("0".equals(parentId) || StringUtils.isBlank(parentId)) {
            if (Constant.TreeType.ORG_STRC.equals(type)) {
                // 查询架构书根节点
                result = orgStrcRepository.findByParentIdAndType(null, Constant.NodeType.COMPANY);
            }
        } else {
            result = orgStrcRepository.findByParentId(parentId);
        }

        return result;
    }

    /**
     * 新增节点
     *
     * @param treeNode
     * @return
     */
    public Boolean createNode(UmcOrgStrc treeNode) {

        treeNode.setIsLeaf(true);

        // 校验重复
        int count = orgStrcRepository.countByCode(treeNode.getCode());
        if (count > 0 && !Constant.NodeType.EMPLOYEE.equals(treeNode.getType())) {
            throw new BizException("节点编码重复");
        }

        if (!StringUtils.isEmpty(treeNode.getParentId())) {
            // 更新父节点
            UmcOrgStrc parent = this.detailNodeById(treeNode.getParentId());
            parent.setIsLeaf(false);
            orgStrcRepository.save(parent);

            //增加父节点数组字段 2020-01-15 xiayihua
            List<String> parentIds = Lists.newArrayList();
            parentIds.add(treeNode.getParentId());
            if (StringUtils.isNotBlank(parent.getParentIds())) {
                treeNode.setParentIds(parent.getParentIds());
            }
        }
        return null != orgStrcRepository.save(treeNode);
    }

    /**
     * 编辑节点
     *
     * @param treeNode
     * @return
     */
    public Boolean updateNode(UmcOrgStrc treeNode) {

        // 校验重复
        UmcOrgStrc exsit = this.detailNode(treeNode.getId());
        treeNode.setId(exsit.getId());

        return null != orgStrcRepository.save(treeNode);

    }

    /**
     * 根据code查询node
     *
     * @param id
     * @return
     */
    public UmcOrgStrc detailNode(String id) {
        UmcOrgStrc exsit = orgStrcRepository.findById(id).orElse(null);
        if (Objects.isNull(exsit)) {
            throw new BizException("节点不存在");
        }
        return exsit;
    }

    /**
     * 根据code查询node
     *
     * @param code
     * @return
     */
    public List<UmcOrgStrc> treeNodes(String code) {
        List<UmcOrgStrc> exsits = orgStrcRepository.findByCode(code);
        //TreeNode node = null;
        if (CollectionUtils.isEmpty(exsits)) {
            throw new BizException("节点不存在");
        } else {

        }
        return exsits;
    }

    /**
     * 根据id查询node
     *
     * @param id
     * @return
     */
    public UmcOrgStrc detailNodeById(String id) {
        UmcOrgStrc exsit = orgStrcRepository.findById(id).orElse(null);
        if (null == exsit) {
            throw new BizException("节点不存在");
        }

        return exsit;
    }

    /**
     * 根据id查询node
     *
     * @param ids
     * @return
     */
    public List<UmcOrgStrc> queryNodeByIds(List<String> ids) {
        List<UmcOrgStrc> exsits = orgStrcRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(exsits)) {
            throw new BizException("节点不存在");
        }
        return exsits;
    }

    /**
     * 根据父节点id查询node
     *
     * @param parentId
     * @return
     */
    public List<UmcOrgStrc> queryNodeByParentId(String parentId) {
        List<UmcOrgStrc> exsits = orgStrcRepository.findByParentId(parentId);
        if (CollectionUtils.isEmpty(exsits)) {
            throw new BizException("子节点不存在");
        }
        return exsits;
    }

    /**
     * 根据父节点ids查询nodes
     *
     * @param parentIds
     * @return
     */
    public List<UmcOrgStrc> queryNodeByParentIds(List<String> parentIds) {
        List<UmcOrgStrc> treeNodes = orgStrcRepository.findByParentIdIn(parentIds);
        if (CollectionUtils.isEmpty(treeNodes)) {
            throw new BizException("子节点不存在");
        }

        return treeNodes;
    }

    /**
     * 节点更名
     *
     * @param nodeCode
     * @param newName
     * @return
     */
    public Boolean rename(String nodeCode, String newName) {
        UmcOrgStrc exsit = this.detailNode(nodeCode);
        exsit.setName(newName);
        return null != orgStrcRepository.save(exsit);
    }

    /**
     * @param nodeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(String nodeId) {
        UmcOrgStrc exsit = this.detailNodeById(nodeId);
        List<UmcOrgStrc> children = orgStrcRepository.findByParentId(nodeId);
        if (!CollectionUtils.isEmpty(children)) {
            throw new BizException("存在子节点, 删除失败");
        }
        orgStrcRepository.delete(exsit);

        String parentId = exsit.getParentId();
        UmcOrgStrc parent = orgStrcRepository.findById(parentId).orElseThrow(() -> new BizException("节点不存在"));
        List<UmcOrgStrc> parentNodes = orgStrcRepository.findByParentId(parentId);
        if (CollectionUtils.isEmpty(parentNodes)) {
            parent.setIsLeaf(true);
        } else {
            if (parentNodes.size() == 1 && parentNodes.get(0).getCode().equalsIgnoreCase(exsit.getCode())) {
                parent.setIsLeaf(true);
            }
        }

        orgStrcRepository.save(parent);

        // 更新父节点状态

        return true;
    }

    /**
     * 查询下级节点
     *
     * @param pageNo
     * @param pageSize
     * @param nodeId
     * @return
     */
    public Page<UmcOrgStrc> pageSubNode(Integer pageNo, Integer pageSize, String nodeId, String nodeCode, String nodeName) {
        ExampleMatcher matcher = ExampleMatcher.matching();
        // 构造查询条件
        UmcOrgStrc query = new UmcOrgStrc();
        query.setParentId(nodeId);
        if (StringUtils.isNotBlank(nodeCode)) {
            matcher.withMatcher("code", ExampleMatcher.GenericPropertyMatchers.contains());
            query.setCode(nodeCode);
        }

        if (StringUtils.isNotBlank(nodeName)) {
            matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
            query.setName(nodeName);
        }

        Example<UmcOrgStrc> ex = Example.of(query, matcher.withIgnoreNullValues());
        return orgStrcRepository.findAll(ex, PageRequest.of(pageNo - 1, pageSize));
    }


    /**
     * 根据code查询上级名称集合
     *
     * @param code
     * @return
     */
    public List<String> parentNms(String code) {
        // 查询parents
        List<UmcOrgStrc> nodes = orgStrcRepository.findByCodeAndType(code, Constant.NodeType.EMPLOYEE);

        List<String> names = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nodes)) {
            List<String> pids = nodes.stream().map(UmcOrgStrc::getParentId).collect(Collectors.toList());
            List<UmcOrgStrc> parents = orgStrcRepository.findByIdIn(pids);
            names = parents.stream().map(UmcOrgStrc::getName).collect(Collectors.toList());
        }

        return names;
    }

    /**
     * 根据code查询上级名称集合
     *
     * @param code
     * @return
     */
    public List<String> parentChildCompany(String code) {
        // 查询parents
        List<UmcOrgStrc> nodes = orgStrcRepository.findByCodeAndType(code, Constant.NodeType.EMPLOYEE);

        List<String> names = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nodes)) {
            List<String> pids = nodes.stream().map(UmcOrgStrc::getParentId).collect(Collectors.toList());
            List<UmcOrgStrc> parents = orgStrcRepository.findByIdIn(pids);
            names = parents.stream().map(UmcOrgStrc::getName).collect(Collectors.toList());
        }

        return names;
    }


    /**
     * @param keyword
     * @param type
     * @return
     */
    public List<UmcOrgStrcItemDto> codeSearch(String keyword, String type) {

        List<UmcCompanyDto> rs = Lists.newArrayList();
        switch (type) {
            case Constant.NodeType.CHILD_COMPANY:
                // 查询所有子公司
                List<UmcCompanyDto> coms = companyService.findChildCompany("1", keyword);
                // 查询组织架构中的所有子公司
                List<UmcOrgStrc> nodes = orgStrcRepository.findByParentIdIsNotNullAndType(Constant.NodeType.CHILD_COMPANY).orElse(Collections.emptyList());

                // 排除交集
                List<String> existIds = nodes.stream().map(UmcOrgStrc::getCode).collect(Collectors.toList());
                rs = coms.stream().filter(c -> !existIds.contains(c.getCode())).collect(Collectors.toList());
                break;
            default:
                // 查询所有员工
                List<UmcEmployeeDto> umcEmployees = employeeService.findList(keyword);
                if (!CollectionUtils.isEmpty(umcEmployees)) {
                    for (UmcEmployeeDto e : umcEmployees) {
                        UmcCompanyDto dto = new UmcCompanyDto();
                        BeanUtils.copyProperties(e, dto);
                        dto.setCoId(e.getEmpId());
                        rs.add(dto);
                    }
                }
                break;
        }

        List<UmcOrgStrcItemDto> items = new ArrayList<>();
        rs.forEach(d -> {
            UmcOrgStrcItemDto item = new UmcOrgStrcItemDto();
            item.setId(d.getCoId());
            item.setCode(d.getCode());
            item.setName(d.getName());

            items.add(item);
        });

        return items;
    }

    public List<UmcOrgStrc> querySalesRgn() {
        List<UmcOrgStrc> nodes = orgStrcRepository.findByType(Constant.NodeType.SALES_RGN);
        return nodes;
    }

//    public Map<String, String> querySalesRgnName(List<String> salesRgnCodes) {
//        if (!CollectionUtils.isEmpty(salesRgnCodes)) {
//            Query query = Query.query(Criteria.where("type").is(StrcConst.NodeType.SALES_RGN)
//                    .and("code").in(salesRgnCodes));
//            List<UmcOrgStrc> treeNodes = mongoOperations.find(query, UmcOrgStrc.class);
//            if (!CollectionUtils.isEmpty(treeNodes)) {
//                Map<String, String> map = treeNodes.stream().collect(Collectors.toMap(TreeNode::getCode, a -> a.getName()));
//
//                return map;
//            }
//        }
//
//        return new HashMap<>(16);
//    }
//
//    public Map<String, String> querySalesRgnCode(List<String> salesAreaNames) {
//        Map<String, String> map = new HashMap<>(16);
//        if (!CollectionUtils.isEmpty(salesAreaNames)) {
//            Query query = Query.query(Criteria.where("type").is(StrcConst.NodeType.SALES_RGN)
//                    .and("name").in(salesAreaNames));
//            List<TreeNode> treeNodes = mongoOperations.find(query, TreeNode.class);
//            if (!CollectionUtils.isEmpty(treeNodes)) {
//                map = treeNodes.stream().collect(Collectors.toMap(TreeNode::getName, a -> a.getCode()));
//            }
//        }
//        return map;
//    }

    /**
     * 是否是客户
     *
     * @return
     */
    public Boolean isCustomer() {
        SecurityUser securityUser = UserUtils.getSecurityUser();
        String userType = securityUser.getUserType();
        return !"EMPLOYEE".equals(userType);
    }

    /**
     * 查询当前用户所拥有权限的销售分公司，当前用户非集团用户
     *
     * @return
     */
    public List<JSONObject> querySalesOrgByCurrentUser() {
        String userType = UserUtils.getSecurityUser().getUserType();
        String custCode = UserUtils.getSecurityUser().getCustCode();
        if (!"CUSTOMER".equals(userType)) {
            List<UmcOrgStrc> employees = this.treeNodes(custCode);
            if (!CollectionUtils.isEmpty(employees)) {
                List<String> parentIds = new ArrayList<>();
                for (UmcOrgStrc node : employees) {
                    if (!StringUtils.isEmpty(node.getParentIds())) {
                        parentIds.add(node.getParentIds());
                    }
                }
                //当前用户的父节点ID，查询父节点数据
                List<UmcOrgStrc> parentNodes = this.queryNodeByIds(parentIds);
                List<UmcOrgStrc> nodes = this.querySalesRgnNodes(parentNodes);
                if (CollectionUtils.isEmpty(nodes)) {
                    //nodes为空，那么当前用户应该是销售大区用户
                    List<String> salesRgnIds = parentNodes.stream().filter(e -> "SalesRgn".equals(e.getType())).map(UmcOrgStrc::getId).collect(Collectors.toList());
                    parentNodes = this.queryNodeByParentIds(salesRgnIds);
                    nodes = this.querySalesRgnNodes(parentNodes);
                }
                return JSONObject.parseArray(JSON.toJSONString(nodes), JSONObject.class);
            }
        }
        return null;
    }

    /**
     * 查询到的父节点中，过滤为销售公司的数据
     *
     * @param parentNodes
     * @return
     */
    public List<UmcOrgStrc> querySalesRgnNodes(List<UmcOrgStrc> parentNodes) {
        if (!CollectionUtils.isEmpty(parentNodes)) {
            List<UmcOrgStrc> nodes = parentNodes.stream().filter(e -> "childCompany".equals(e.getType())).collect(Collectors.toList());
            return nodes;
        }
        return null;
    }

    /**
     * 查询当前用户所属的组织机构
     *
     * @return
     */
    public List<String> queryOrgCode() {
        List<String> orgCodes = Lists.newArrayList();
        //获取当前账户的ID
        String acctId = UserUtils.getSecurityUser().getAcctId();
        UmcAccountDto umcAccountDto = accountService.findByAcctId(acctId);
        String orgCode = umcAccountDto.getOrgCode();
        List<UmcOrgStrc> orgStrcs = orgStrcRepository.findByCode(orgCode);
        if (CollectionUtils.isEmpty(orgStrcs)) {
            throw new BizException("组织架构树中不存在所属机构");
        }
        String id = orgStrcs.get(0).getId();
        List<UmcOrgStrc> orgStrcList = orgStrcRepository.findOrgStrcByParentId(OrgStrcTypeEnum.CHILD_COMPANY.getCode(), id);
        if (!CollectionUtils.isEmpty(orgStrcList)) {
            orgCodes.addAll(orgStrcList.stream().map(UmcOrgStrc::getCode).collect(Collectors.toList()));
        }
        orgCodes.add(orgStrcs.get(0).getCode());
        return orgCodes;
    }
}
