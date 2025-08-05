package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSIDCDefinition.class */
public interface XSIDCDefinition extends XSObject {
    public static final short IC_KEY = 1;
    public static final short IC_KEYREF = 2;
    public static final short IC_UNIQUE = 3;

    short getCategory();

    String getSelectorStr();

    StringList getFieldStrs();

    XSIDCDefinition getRefKey();

    XSObjectList getAnnotations();
}
