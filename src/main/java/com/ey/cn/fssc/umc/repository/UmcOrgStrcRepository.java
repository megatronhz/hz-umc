package com.ey.cn.fssc.umc.repository;

import com.ey.cn.fssc.umc.entity.UmcAccountRole;
import com.ey.cn.fssc.umc.entity.UmcOrgStrc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: xiayihua
 * @Description:
 * @Date: Created in 1:36 下午 2020/2/4
 */
@Repository
public interface UmcOrgStrcRepository extends JpaRepository<UmcOrgStrc, String>, JpaSpecificationExecutor<UmcOrgStrc> {

    /**
     * 查询code对应的记录条数
     *
     * @param code
     * @return
     */
    int countByCode(String code);

    /**
     * 查询code对应的记录条数
     *
     * @param code
     * @return
     */
    List<UmcOrgStrc> findByCode(String code);

    /**
     * 查询code和type对应的记录
     *
     * @param code
     * @param type
     * @return
     */
    List<UmcOrgStrc> findByCodeAndType(String code, String type);

    @Query(value = "select c from UmcOrgStrc c where c.code like CONCAT('%', ?1, '%') or c.name like CONCAT('%', ?1, '%')")
    List<UmcOrgStrc> findByKeyWord(String keyWord);

    /**
     * 查询parentId和nodeType对应的记录
     *
     * @param parentId
     * @param nodeType
     * @return
     */
    List<UmcOrgStrc> findByParentIdAndType(String parentId, String nodeType);

    /**
     * 查询type对应的记录
     *
     * @param nodeType
     * @return
     */
    List<UmcOrgStrc> findByType(String nodeType);

    /**
     * 查询type对应的父类型不为空的记录
     *
     * @param type
     * @return
     */
    Optional<List<UmcOrgStrc>> findByParentIdIsNotNullAndType(String type);

    /**
     * 查询parentId对应的记录
     *
     * @param parentId
     * @return
     */
    List<UmcOrgStrc> findByParentId(String parentId);

    /**
     * 查询parentIds对应的记录
     *
     * @param parentIds
     * @return
     */
    List<UmcOrgStrc> findByParentIdIn(List<String> parentIds);

    /**
     * 根据id列表查询
     *
     * @param ids
     * @return
     */
    List<UmcOrgStrc> findByIdIn(List<String> ids);

    @Query(value = "select c from UmcOrgStrc c where c.type =?1 and c.parentIds like CONCAT('%', ?2, '%')")
    List<UmcOrgStrc> findOrgStrcByParentId(String type, String parentId);


}
