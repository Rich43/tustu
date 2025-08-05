package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSParticle.class */
public interface XSParticle extends XSObject {
    int getMinOccurs();

    int getMaxOccurs();

    boolean getMaxOccursUnbounded();

    XSTerm getTerm();

    XSObjectList getAnnotations();
}
