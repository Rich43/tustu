package com.sun.org.apache.xml.internal.security.transforms;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/TransformationException.class */
public class TransformationException extends XMLSecurityException {
    private static final long serialVersionUID = 1;

    public TransformationException() {
    }

    public TransformationException(Exception exc) {
        super(exc);
    }

    public TransformationException(String str) {
        super(str);
    }

    public TransformationException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public TransformationException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public TransformationException(String str, Exception exc) {
        this(exc, str);
    }

    public TransformationException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public TransformationException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
