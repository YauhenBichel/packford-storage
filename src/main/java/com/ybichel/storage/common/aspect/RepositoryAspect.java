package com.ybichel.storage.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RepositoryAspect {

    private static final Logger logger = LogManager.getLogger(RepositoryAspect.class);

    @Pointcut("within(com..repository.*)")
    public void serviceOrSamePackageRepository() {
    }

    @Before("serviceOrSamePackageRepository()")
    public void before(JoinPoint joinPoint) {
        logger.info("before - {}", joinPoint.getSignature());
    }

    @After("serviceOrSamePackageRepository()")
    public void after(JoinPoint joinPoint) {
        logger.info("after - {}", joinPoint.getSignature());
    }

    @AfterThrowing(value = "serviceOrSamePackageRepository()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.info("after throwing exception - {}; exception = {}", joinPoint.getSignature(), exception);
    }

    @AfterReturning(value = "serviceOrSamePackageRepository()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        logger.info("after returning {}; returnValue = {}", joinPoint.getSignature(), returnValue);
    }

    @Around("serviceOrSamePackageRepository()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("around - before - {}", joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } finally {
            logger.info("around - after - {}", joinPoint.getSignature());
        }
    }
}
