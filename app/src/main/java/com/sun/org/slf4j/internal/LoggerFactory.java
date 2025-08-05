package com.sun.org.slf4j.internal;

/* loaded from: rt.jar:com/sun/org/slf4j/internal/LoggerFactory.class */
public class LoggerFactory {
    public static Logger getLogger(Class<?> cls) {
        return new Logger(cls.getName());
    }
}
