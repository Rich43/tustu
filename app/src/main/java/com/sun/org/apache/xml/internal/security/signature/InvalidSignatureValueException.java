package com.sun.org.apache.xml.internal.security.signature;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/InvalidSignatureValueException.class */
public class InvalidSignatureValueException extends XMLSignatureException {
    private static final long serialVersionUID = 1;

    public InvalidSignatureValueException() {
    }

    public InvalidSignatureValueException(String str) {
        super(str);
    }

    public InvalidSignatureValueException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public InvalidSignatureValueException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public InvalidSignatureValueException(String str, Exception exc) {
        this(exc, str);
    }

    public InvalidSignatureValueException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public InvalidSignatureValueException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
