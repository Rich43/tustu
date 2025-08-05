package com.sun.org.apache.xml.internal.security.keys;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/ContentHandlerAlreadyRegisteredException.class */
public class ContentHandlerAlreadyRegisteredException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public ContentHandlerAlreadyRegisteredException() {
    }

    public ContentHandlerAlreadyRegisteredException(String str) {
        super(str);
    }

    public ContentHandlerAlreadyRegisteredException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public ContentHandlerAlreadyRegisteredException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public ContentHandlerAlreadyRegisteredException(String str, Exception exc) {
        this(exc, str);
    }

    public ContentHandlerAlreadyRegisteredException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public ContentHandlerAlreadyRegisteredException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
