package com.sun.org.apache.xml.internal.security.c14n;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/CanonicalizationException.class */
public class CanonicalizationException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public CanonicalizationException() {
    }

    public CanonicalizationException(Exception exc) {
        super(exc);
    }

    public CanonicalizationException(String str) {
        super(str);
    }

    public CanonicalizationException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public CanonicalizationException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public CanonicalizationException(String str, Exception exc) {
        this(exc, str);
    }

    public CanonicalizationException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public CanonicalizationException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
