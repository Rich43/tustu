package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSAttributeGroupDefinition.class */
public interface XSAttributeGroupDefinition extends XSObject {
    XSObjectList getAttributeUses();

    XSWildcard getAttributeWildcard();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
