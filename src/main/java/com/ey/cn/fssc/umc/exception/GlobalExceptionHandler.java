package com.ey.cn.fssc.umc.exception;

import com.ey.cn.pi.cc.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liaoguangan
 * @description <>
 * @date 2018/4/6 9:26
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public Result<String> exceptionHandler(Exception e) {
        log.error("error message:{}", e.getMessage());
        log.error(e.getMessage(), e);
        if (e instanceof BizException) {
            BizException biz = (BizException) e;
            return Result.failWithStack(biz.getMessage(), biz.getStackTrace()[0].toString());
        } else {
            return Result.failWithStack(e.getMessage());
        }
    }
}
