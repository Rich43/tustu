package com.sun.org.apache.xml.internal.security.exceptions;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/exceptions/Base64DecodingException.class */
public class Base64DecodingException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public Base64DecodingException() {
    }

    public Base64DecodingException(String str) {
        super(str);
    }

    public Base64DecodingException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public Base64DecodingException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public Base64DecodingException(String str, Exception exc) {
        this(exc, str);
    }

    public Base64DecodingException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public Base64DecodingException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
