package com.sun.org.apache.xml.internal.security.exceptions;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/exceptions/AlgorithmAlreadyRegisteredException.class */
public class AlgorithmAlreadyRegisteredException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public AlgorithmAlreadyRegisteredException() {
    }

    public AlgorithmAlreadyRegisteredException(String str) {
        super(str);
    }

    public AlgorithmAlreadyRegisteredException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public AlgorithmAlreadyRegisteredException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public AlgorithmAlreadyRegisteredException(String str, Exception exc) {
        this(exc, str);
    }

    public AlgorithmAlreadyRegisteredException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public AlgorithmAlreadyRegisteredException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
