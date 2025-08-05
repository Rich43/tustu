package com.sun.org.apache.xalan.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/utils/ConfigurationError.class */
public final class ConfigurationError extends Error {
    private Exception exception;

    ConfigurationError(String msg, Exception x2) {
        super(msg);
        this.exception = x2;
    }

    public Exception getException() {
        return this.exception;
    }
}
