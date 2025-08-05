package com.sun.org.apache.xml.internal.security.signature;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/MissingResourceFailureException.class */
public class MissingResourceFailureException extends XMLSignatureException {
    private static final long serialVersionUID = 1;
    private Reference uninitializedReference;

    public MissingResourceFailureException(Reference reference, String str) {
        super(str);
        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String str, Reference reference) {
        this(reference, str);
    }

    public MissingResourceFailureException(Reference reference, String str, Object[] objArr) {
        super(str, objArr);
        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String str, Object[] objArr, Reference reference) {
        this(reference, str, objArr);
    }

    public MissingResourceFailureException(Exception exc, Reference reference, String str) {
        super(exc, str);
        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String str, Exception exc, Reference reference) {
        this(exc, reference, str);
    }

    public MissingResourceFailureException(Exception exc, Reference reference, String str, Object[] objArr) {
        super(exc, str, objArr);
        this.uninitializedReference = reference;
    }

    @Deprecated
    public MissingResourceFailureException(String str, Object[] objArr, Exception exc, Reference reference) {
        this(exc, reference, str, objArr);
    }

    public void setReference(Reference reference) {
        this.uninitializedReference = reference;
    }

    public Reference getReference() {
        return this.uninitializedReference;
    }
}
