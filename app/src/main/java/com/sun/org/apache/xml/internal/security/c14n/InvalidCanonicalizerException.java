package com.sun.org.apache.xml.internal.security.c14n;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/InvalidCanonicalizerException.class */
public class InvalidCanonicalizerException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public InvalidCanonicalizerException() {
    }

    public InvalidCanonicalizerException(String str) {
        super(str);
    }

    public InvalidCanonicalizerException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public InvalidCanonicalizerException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public InvalidCanonicalizerException(String str, Exception exc) {
        this(exc, str);
    }

    public InvalidCanonicalizerException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public InvalidCanonicalizerException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
