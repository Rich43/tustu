package com.sun.org.apache.xerces.internal.xs;

import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/StringList.class */
public interface StringList extends List {
    int getLength();

    boolean contains(String str);

    String item(int i2);
}
