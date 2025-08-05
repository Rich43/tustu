package com.sun.org.apache.xerces.internal.xs.datatypes;

import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/datatypes/ObjectList.class */
public interface ObjectList extends List {
    int getLength();

    @Override // java.util.List, java.util.Collection, java.util.Set
    boolean contains(Object obj);

    Object item(int i2);
}
