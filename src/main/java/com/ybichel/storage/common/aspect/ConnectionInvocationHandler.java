package com.ybichel.storage.common.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConnectionInvocationHandler implements InvocationHandler {
    private static final Logger logger = LogManager.getLogger(ConnectionInvocationHandler.class);

    private final Connection connection;

    private static Set<String> LOGGABLE_METHODS = new HashSet<>(Arrays.asList(
            "commit", "rollback", "close", "setAutoCommit"
    ));

    public ConnectionInvocationHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (shouldLogInvocation(method)) {
            logger.info("Connection Trace: {}", method.toGenericString());
        }

        return method.invoke(connection, args);
    }

    private boolean shouldLogInvocation(Method method) {
        return LOGGABLE_METHODS.contains(method.getName());
    }
}
