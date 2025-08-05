package com.sun.org.apache.xml.internal.dtm;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMException.class */
public class DTMException extends RuntimeException {
    static final long serialVersionUID = -775576419181334734L;

    public DTMException(String message) {
        super(message);
    }

    public DTMException(Throwable e2) {
        super(e2);
    }

    public DTMException(String message, Throwable e2) {
        super(message, e2);
    }
}
