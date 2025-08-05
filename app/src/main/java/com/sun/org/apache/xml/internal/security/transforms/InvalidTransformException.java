package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/InvalidTransformException.class */
public class InvalidTransformException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public InvalidTransformException() {
    }

    public InvalidTransformException(String str) {
        super(str);
    }

    public InvalidTransformException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public InvalidTransformException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public InvalidTransformException(String str, Exception exc) {
        this(exc, str);
    }

    public InvalidTransformException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public InvalidTransformException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
