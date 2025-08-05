package com.sun.org.apache.xml.internal.security.signature;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/InvalidDigestValueException.class */
public class InvalidDigestValueException extends XMLSignatureException {
    private static final long serialVersionUID = 1;

    public InvalidDigestValueException() {
    }

    public InvalidDigestValueException(String str) {
        super(str);
    }

    public InvalidDigestValueException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public InvalidDigestValueException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public InvalidDigestValueException(String str, Exception exc) {
        this(exc, str);
    }

    public InvalidDigestValueException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public InvalidDigestValueException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
