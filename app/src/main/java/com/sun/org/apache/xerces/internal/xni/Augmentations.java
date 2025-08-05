package com.sun.org.apache.xerces.internal.xni;

import java.util.Enumeration;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/Augmentations.class */
public interface Augmentations {
    Object putItem(String str, Object obj);

    Object getItem(String str);

    Object removeItem(String str);

    Enumeration keys();

    void removeAllItems();
}
