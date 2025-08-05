package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/KeyResolverException.class */
public class KeyResolverException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public KeyResolverException() {
    }

    public KeyResolverException(Exception exc) {
        super(exc);
    }

    public KeyResolverException(String str) {
        super(str);
    }

    public KeyResolverException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public KeyResolverException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public KeyResolverException(String str, Exception exc) {
        this(exc, str);
    }

    public KeyResolverException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public KeyResolverException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
