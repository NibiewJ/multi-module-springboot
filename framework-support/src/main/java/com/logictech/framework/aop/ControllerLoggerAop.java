package com.logictech.framework.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
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
import java.util.Set;

/**
 * @author JG.Hannibal
 * @since 2017/11/9 下午3:43
 */
@Aspect
@Component
public class ControllerLoggerAop {
    /**
     * 一分钟，即1000ms
     */
    private static final long ONE_MINUTE = 1000;
    private static final String HEAD = "##########|    ";

    public static final Logger logger = LoggerFactory.getLogger(ControllerLoggerAop.class);

    @Pointcut("execution(* com.logictech.api..*(..)) || " +
            "execution(* com.logictech.manage..*(..))")
    public void controllerPointCut() {
    }

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Before("controllerPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        // Receives the request and get request content
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info(HEAD + "URL : " + request.getRequestURL().toString());
        logger.info(HEAD + "HTTP_METHOD : " + request.getMethod());
        logger.info(HEAD + "IP : " + request.getRemoteAddr());
        logger.info(HEAD + "CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info(HEAD + "ARGS : " + Arrays.toString(joinPoint.getArgs()));
        // =================================
        // 获得切入目标对象
        Object target = joinPoint.getThis();
        // 获得切入方法参数
        Object[] args = joinPoint.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    }

    @AfterReturning(returning = "ret", pointcut = "controllerPointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // Processes the request and returns the content
        logger.info(HEAD + "RESPONSE : " + ret);
        logger.info("====================================================");
    }


    @Around("controllerPointCut()")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        stopWatch.stop();

        Long cost = stopWatch.getTotalTimeMillis();
        if (cost > ONE_MINUTE) {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

            logger.info(HEAD + "执行 {} 方法, 用时: {} ms.", methodName, cost);
        }

        logger.info("====================================================");
        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     *
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        if (diffTime > ONE_MINUTE) {
            logger.warn(HEAD + methodName + " 方法执行耗时：" + diffTime + " ms.");
        }
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }

}
    