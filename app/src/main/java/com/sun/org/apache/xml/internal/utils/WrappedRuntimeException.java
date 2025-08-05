package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/WrappedRuntimeException.class */
public class WrappedRuntimeException extends RuntimeException {
    static final long serialVersionUID = 7140414456714658073L;
    private Exception m_exception;

    public WrappedRuntimeException(Exception e2) {
        super(e2.getMessage());
        this.m_exception = e2;
    }

    public WrappedRuntimeException(String msg, Exception e2) {
        super(msg);
        this.m_exception = e2;
    }

    public Exception getException() {
        return this.m_exception;
    }
}
