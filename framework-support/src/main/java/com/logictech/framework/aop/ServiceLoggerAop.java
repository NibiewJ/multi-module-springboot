package com.logictech.framework.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author JG.Hannibal
 * @since 2018/7/5 13:31
 */
@Aspect
@Component
public class ServiceLoggerAop {
    private static final String HEAD = "##########|    ";

    public static final Logger logger = LoggerFactory.getLogger(ServiceLoggerAop.class);

    @Pointcut("execution(* com.logictech.api.service..*(..)) || " +
            "execution(* com.logictech.manage.service..*(..))")
    public void servicePointCut() {
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "servicePointCut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error(HEAD + "ERROR {}.{}() with cause = '{}' ERROR MESSAGE: '{}'", joinPoint.getSignature()
                        .getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
    }


    @Around("servicePointCut()")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("==========================StartServiceAOP==========================");
            logger.debug(HEAD + "CLASS_METHOD : " + proceedingJoinPoint.getSignature()
                    .getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName());
            logger.debug(HEAD + "ARGS : {}",
                    proceedingJoinPoint.getArgs().length == 1 ?
                            JSON.toJSON(proceedingJoinPoint.getArgs()) : Arrays.toString
                            (proceedingJoinPoint.getArgs()));
        }
        try {
            Object result = proceedingJoinPoint.proceed();
            if (logger.isDebugEnabled()) {
                logger.debug(HEAD + "RESULT: {}", result);
                logger.debug("==========================EndServiceAOP==========================");
            }
            return result;
        } catch (IllegalArgumentException e) {
            logger.error(HEAD + "ERROR: {} OCCURED {}.{}()", Arrays.toString(proceedingJoinPoint.getArgs()),
                    proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());

            throw e;
        }
    }

}
    