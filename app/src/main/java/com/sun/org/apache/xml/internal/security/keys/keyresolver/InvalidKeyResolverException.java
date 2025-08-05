package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/InvalidKeyResolverException.class */
public class InvalidKeyResolverException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public InvalidKeyResolverException() {
    }

    public InvalidKeyResolverException(String str) {
        super(str);
    }

    public InvalidKeyResolverException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public InvalidKeyResolverException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public InvalidKeyResolverException(String str, Exception exc) {
        this(exc, str);
    }

    public InvalidKeyResolverException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public InvalidKeyResolverException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
