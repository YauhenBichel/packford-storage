package com.ybichel.storage.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CallTracer {
    private static final Logger logger = LogManager.getLogger(CallTracer.class);

    @Pointcut("within(com..service.*)")
    public void callTraceComponentsPointcut() {
    }

    @Around("callTraceComponentsPointcut()")
    public Object traceCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();

        logger.info("Starting " + methodName);
        Object returnValue = proceedingJoinPoint.proceed();

        logger.info("Completed " + methodName);
        return returnValue;
    }
}
