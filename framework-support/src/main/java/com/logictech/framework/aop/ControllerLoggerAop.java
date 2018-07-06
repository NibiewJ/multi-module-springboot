package com.logictech.framework.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.logictech.framework.entity.so.AppException;
import com.logictech.framework.entity.so.FieldError;
import com.logictech.framework.entity.so.ParamValidException;
import com.logictech.framework.entity.so.ResultEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JG.Hannibal
 * @since 2017/11/9 下午3:43
 */
@Aspect
@Component
public class ControllerLoggerAop {
    /**
     * 一分钟，即 1000 * 60 ms
     */
    private static final long ONE_MINUTE = 1000 * 60;
    private static final String HEAD = "##########|    ";

    public static final Logger logger = LoggerFactory.getLogger(ControllerLoggerAop.class);

    @Pointcut("execution(* com.logictech.api.restful..*(..)) || " +
            "execution(* com.logictech.manage.web..*(..))")
    public void controllerPointCut() {
    }

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Before("controllerPointCut()")
    public void doBefore(JoinPoint joinPoint) throws ParamValidException {
        // Receives the request and get request content
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (logger.isDebugEnabled()) {
            logger.debug("==========================StartControllerAOP==========================");
            logger.debug(HEAD + "URL : " + request.getRequestURL().toString());
            logger.debug(HEAD + "HTTP_METHOD : " + request.getMethod());
            logger.debug(HEAD + "IP : " + request.getRemoteAddr());
            logger.debug(HEAD + "CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint
                    .getSignature().getName());
            // 请求参数
            if (joinPoint.getArgs().length == 1) {
                try {
                    logger.debug(HEAD + "ARGS : " + JSON.toJSON(joinPoint.getArgs()));
                } catch (JSONException e) {
                    // JSON 转换出错的情况
                    logger.debug(HEAD + "ARGS : " + Arrays.toString(joinPoint.getArgs()));
                }
            } else {
                logger.debug(HEAD + "ARGS : " + Arrays.toString(joinPoint.getArgs()));
            }
        }

        // =================================
        // 获得切入目标对象
        Object target = joinPoint.getThis();
        // 获得切入方法参数
        Object[] args = joinPoint.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

        if (!validResult.isEmpty()) {
            // 获得方法的参数名称
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            List<FieldError> errors = validResult.stream().map(constraintViolation -> {
                // 获得校验的参数路径信息
                PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
                // 获得校验的参数位置
                int paramIndex = pathImpl.getLeafNode().getParameterIndex();
                // 获得校验的参数名称
                String paramName = parameterNames[paramIndex];
                // 将需要的信息包装成简单的对象，方便后面处理
                FieldError error = new FieldError();
                // 参数名称（校验错误的参数名称）
                error.setName(paramName);
                // 校验的错误信息
                error.setMessage(constraintViolation.getMessage());
                return error;
            }).collect(Collectors.toList());
            // 抛出异常，交给上层处理
            logger.error(HEAD + "参数校验发生错误 ===> {}", errors.toString());
            ParamValidException ex = new ParamValidException(errors);
            logger.error(HEAD + "RESPONSE.ERROR : " + JSON.toJSONString(new ResultEntity(ex){{
                setData(ex.getFieldErrors());
            }}));
            logger.error("==========================ErrorEndControllerAOP==========================");
            throw new ParamValidException(errors);
        }

    }

    @AfterReturning(returning = "ret", pointcut = "controllerPointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // Processes the request and returns the content
        if (logger.isDebugEnabled()) {
            logger.debug(HEAD + "RESPONSE : " + ret);
            logger.debug("==========================EndControllerAOP==========================");
        }
    }


    @Around("controllerPointCut()")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        stopWatch.stop();

        Long cost = stopWatch.getTotalTimeMillis();
        if (cost > ONE_MINUTE && logger.isDebugEnabled()) {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
            logger.debug(HEAD + "执行 {} 方法, 用时: {} ms.", methodName, cost);
        }
        return obj;
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }

}
    