package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.xs.ShortList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/identity/ValueStore.class */
public interface ValueStore {
    void addValue(Field field, Object obj, short s2, ShortList shortList);

    void reportError(String str, Object[] objArr);
}
