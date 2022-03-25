package com.ybichel.storage.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
public class PerformanceLoggerAspect {

    private static final Logger logger = LogManager.getLogger(PerformanceLoggerAspect.class);

    @Around("@annotation(com.ybichel.storage.common.aspect.annotation.PerformanceLogger)")
    public Object logPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long finishTime = System.currentTimeMillis();
            Duration duration = Duration.ofMillis(finishTime - startTime);

            logger.info("Duration of {} execution was {}", proceedingJoinPoint.getSignature(), duration);
        }
    }
}
