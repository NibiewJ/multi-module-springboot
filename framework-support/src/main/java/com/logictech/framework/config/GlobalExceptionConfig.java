package com.logictech.framework.config;

import com.logictech.framework.entity.so.AppException;
import com.logictech.framework.entity.so.ParamValidException;
import com.logictech.framework.entity.so.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 16:07
 */
@RestController
@ControllerAdvice
public class GlobalExceptionConfig {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    /**
     * Hibernate-Validator 异常处理
     * ExceptionHandler 配置异常处理
     * ResponseStatus 配置响应码
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ParamValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultEntity paramValidExceptionHandler(ParamValidException ex) {
        ResultEntity result = new ResultEntity(ex);
        result.setData(ex.getFieldErrors());
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultEntity bindExceptionHandler(BindException ex) {
        return paramValidExceptionHandler(new ParamValidException(ex));
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultEntity undeclaredThrowableException(UndeclaredThrowableException ex) throws Exception {
        // 获得实际异常
        Throwable throwable = ex.getUndeclaredThrowable();
        // 如果是我们自定义异常就调用自定义异常的处理方法
        if (throwable instanceof ParamValidException) {
            return paramValidExceptionHandler((ParamValidException) throwable);
        }
        return exception(ex);
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultEntity appThrowableException(AppException ex) throws Exception {
        return new ResultEntity(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultEntity exception(Exception ex) throws Exception {
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }
        // 原始错误集合数据偏多，包含native错误
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        // 过滤错误信息
        final List<StackTraceElement> filters = new LinkedList<>();
        for (StackTraceElement st: stackTrace) {
            if (st.getClassName().contains("logictech.") && st.getFileName().contains(".java")) {
                filters.add(st);
            }
        }
        StackTraceElement[] stackTraceElements = new StackTraceElement[filters.size()];
        // 计数器
        Integer i = 0;
        for (StackTraceElement filter: filters) {
            stackTraceElements[i++] = filter;
        }
        // 填充stackTrace
        ex.setStackTrace(stackTraceElements);
        ex.printStackTrace();
        logger.error(ex.getMessage(), ex);
        return new ResultEntity("发生未知错误, 请联系管理员", 1, new HashMap() {{
            put("ex", ex.getClass().getName());
            put("orgEXMessage", ex.getMessage());
            put("clazz", stackTraceElements.length == 0 ? null : stackTraceElements[0].getClassName());
            put("method", stackTraceElements.length == 0 ? null : stackTraceElements[0].getMethodName());
            put("lineNumber", stackTraceElements.length == 0 ? null : stackTraceElements[0].getLineNumber());
        }});
    }
}
    