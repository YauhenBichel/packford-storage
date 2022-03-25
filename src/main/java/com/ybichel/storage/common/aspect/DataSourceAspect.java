package com.ybichel.storage.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Connection;

@Component
@Aspect
public class DataSourceAspect {

    private static final Logger logger = LogManager.getLogger(DataSourceAspect.class);

    @Around("target(javax.sql.DataSource)")
    public Object aroundDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Data Source Trace: {}", joinPoint.getSignature());

        Object returnObject = joinPoint.proceed();
        if (returnObject instanceof Connection) {
            return createConnectionProxy((Connection) returnObject);
        } else
            return returnObject;
    }

    private Connection createConnectionProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(
                DataSourceAspect.class.getClassLoader(),
                new Class[]{Connection.class},
                new ConnectionInvocationHandler(connection)
        );
    }
}
