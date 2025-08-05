package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSAnnotation.class */
public interface XSAnnotation extends XSObject {
    public static final short W3C_DOM_ELEMENT = 1;
    public static final short SAX_CONTENTHANDLER = 2;
    public static final short W3C_DOM_DOCUMENT = 3;

    boolean writeAnnotation(Object obj, short s2);

    String getAnnotationString();
}
