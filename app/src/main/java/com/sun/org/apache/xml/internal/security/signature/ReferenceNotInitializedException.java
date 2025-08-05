package com.sun.org.apache.xml.internal.security.signature;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/ReferenceNotInitializedException.class */
public class ReferenceNotInitializedException extends XMLSignatureException {
    private static final long serialVersionUID = 1;

    public ReferenceNotInitializedException() {
    }

    public ReferenceNotInitializedException(Exception exc) {
        super(exc);
    }

    public ReferenceNotInitializedException(String str) {
        super(str);
    }

    public ReferenceNotInitializedException(String str, Object[] objArr) {
        super(str, objArr);
    }

    public ReferenceNotInitializedException(Exception exc, String str) {
        super(exc, str);
    }

    @Deprecated
    public ReferenceNotInitializedException(String str, Exception exc) {
        this(exc, str);
    }

    public ReferenceNotInitializedException(Exception exc, String str, Object[] objArr) {
        super(exc, str, objArr);
    }

    @Deprecated
    public ReferenceNotInitializedException(String str, Object[] objArr, Exception exc) {
        this(exc, str, objArr);
    }
}
