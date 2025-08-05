package com.sun.org.apache.xml.internal.security.signature;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/XMLSignatureException.class */
public class XMLSignatureException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public XMLSignatureException() {
    }

    public XMLSignatureException(Exception exc) {
        super(exc);
    }

    public XMLSignatureException(String str) {
        super(str);
    }

    public XMLSignatureException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public XMLSignatureException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public XMLSignatureException(String str, Exception exc) {
        this(exc, str);
    }

    public XMLSignatureException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public XMLSignatureException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
