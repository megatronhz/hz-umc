package com.ey.cn.fssc.umc.exception;

import com.ey.cn.pi.cc.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liaoguangan
 * @description <>
 * @date 2018/4/6 9:26
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler
//    @ResponseBody
//    public Result<String> exceptionHandler(Exception e) {
//        log.error("error message:{}", e.getMessage());
//        log.error(e.getMessage(), e);
//        if (e instanceof BizException) {
//            BizException biz = (BizException) e;
//            return Result.failWithStack(biz.getMessage(), biz.getStackTrace()[0].toString());
//        } else {
//            return Result.failWithStack(e.getMessage());
//        }
//    }

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler
    @ResponseBody
    public Result<String> exceptionHandler(Exception e) {

        String stack = null;
        StackTraceElement[] traceElement = e.getStackTrace();
        if (!ArrayUtils.isEmpty(traceElement)) {
            stack = String.valueOf(traceElement[0]);
        }

        String msgKey = "fssc.server.error";

        if (e instanceof BizException) {
            msgKey = e.getMessage();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return Result.failWithStack(messageSource.getMessage(msgKey, null, new AcceptHeaderLocaleResolver().resolveLocale(request)), stack);
    }
}
