package com.sun.xml.internal.ws.api.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/MethodUtil.class */
class MethodUtil {
    private static final Logger LOGGER = Logger.getLogger(MethodUtil.class.getName());
    private static final Method INVOKE_METHOD;

    MethodUtil() {
    }

    static {
        Method method;
        try {
            Class<?> clazz = Class.forName("sun.reflect.misc.MethodUtil");
            method = clazz.getMethod("invoke", Method.class, Object.class, Object[].class);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil found; it will be used to invoke methods.");
            }
        } catch (Throwable th) {
            method = null;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Class sun.reflect.misc.MethodUtil not found, probably non-Oracle JVM");
            }
        }
        INVOKE_METHOD = method;
    }

    static Object invoke(Object target, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        if (INVOKE_METHOD != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Invoking method using sun.reflect.misc.MethodUtil");
            }
            try {
                return INVOKE_METHOD.invoke(null, method, target, args);
            } catch (InvocationTargetException ite) {
                throw unwrapException(ite);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Invoking method directly, probably non-Oracle JVM");
        }
        return method.invoke(target, args);
    }

    private static InvocationTargetException unwrapException(InvocationTargetException ite) {
        Throwable targetException = ite.getTargetException();
        if (targetException != null && (targetException instanceof InvocationTargetException)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unwrapping invocation target exception");
            }
            return (InvocationTargetException) targetException;
        }
        return ite;
    }
}
