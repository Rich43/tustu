package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSNotationDeclaration.class */
public interface XSNotationDeclaration extends XSObject {
    String getSystemId();

    String getPublicId();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
