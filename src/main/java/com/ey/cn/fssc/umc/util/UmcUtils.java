package com.ey.cn.fssc.umc.util;

import com.ey.cn.fssc.umc.dto.PageDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author King 2019/6/12 下午6:20
 * <>
 */
public class UmcUtils {

    public static String nulltoStr(String str) {

        return str == null || "null".equals(str) ? "" : str;
    }

    /**
     * 模糊查询字符串生成
     *
     * @param str
     * @return likeStr
     */
    public static String likeStr(String str) {

        return org.apache.commons.lang.StringUtils.isBlank(str) ? "%%" : "%" + str + "%";
    }

    public static String strToNull(String str) {

        return org.apache.commons.lang.StringUtils.isBlank(str) ? null : str;
    }

    public static Pageable makePageable(PageDto pageDto) {
        if (StringUtils.isBlank(pageDto.getSortColumn())) {
            return PageRequest.of(pageDto.getPage() - 1, pageDto.getLimit());
        } else {
            return PageRequest.of(pageDto.getPage() - 1, pageDto.getLimit(),
                    makeSort(pageDto.getSortColumn(), pageDto.getSortDirection()));
        }
    }

    public static Sort makeSort(String column, String direction) {
//        Sort.Direction sortDirection = StringUtils.equals(direction, "desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        return new Sort(sortDirection, column);
        return null;
    }
}
