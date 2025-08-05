package com.sun.org.apache.xml.internal.security.keys.storage;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/storage/StorageResolverException.class */
public class StorageResolverException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public StorageResolverException() {
    }

    public StorageResolverException(Exception exc) {
        super(exc);
    }

    public StorageResolverException(String str) {
        super(str);
    }

    public StorageResolverException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public StorageResolverException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public StorageResolverException(String str, Exception exc) {
        this(exc, str);
    }

    public StorageResolverException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public StorageResolverException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
