package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSTypeDefinition.class */
public interface XSTypeDefinition extends XSObject {
    public static final short COMPLEX_TYPE = 15;
    public static final short SIMPLE_TYPE = 16;

    short getTypeCategory();

    XSTypeDefinition getBaseType();

    boolean isFinal(short s2);

    short getFinal();

    boolean getAnonymous();

    boolean derivedFromType(XSTypeDefinition xSTypeDefinition, short s2);

    boolean derivedFrom(String str, String str2, short s2);
}
