package com.sun.org.apache.xerces.internal.xs;

import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSNamedMap.class */
public interface XSNamedMap extends Map {
    int getLength();

    XSObject item(int i2);

    XSObject itemByName(String str, String str2);
}
