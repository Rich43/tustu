package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSModelGroup.class */
public interface XSModelGroup extends XSTerm {
    public static final short COMPOSITOR_SEQUENCE = 1;
    public static final short COMPOSITOR_CHOICE = 2;
    public static final short COMPOSITOR_ALL = 3;

    short getCompositor();

    XSObjectList getParticles();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
