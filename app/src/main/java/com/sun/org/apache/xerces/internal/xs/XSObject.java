package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSObject.class */
public interface XSObject {
    short getType();

    String getName();

    String getNamespace();

    XSNamespaceItem getNamespaceItem();
}
