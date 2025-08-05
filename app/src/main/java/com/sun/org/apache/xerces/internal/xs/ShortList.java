package com.sun.org.apache.xerces.internal.xs;

import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/ShortList.class */
public interface ShortList extends List {
    int getLength();

    boolean contains(short s2);

    short item(int i2) throws XSException;
}
