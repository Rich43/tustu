package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSModelGroupDefinition.class */
public interface XSModelGroupDefinition extends XSObject {
    XSModelGroup getModelGroup();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
