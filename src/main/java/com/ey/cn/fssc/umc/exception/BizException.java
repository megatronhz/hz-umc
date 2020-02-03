package com.ey.cn.fssc.umc.exception;

/**
 * @author liaoguangan
 * @description <>
 * @date 2018/4/6 9:36
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BizException() {
    }

    public BizException(String msg) {
        super(msg);
    }

}
