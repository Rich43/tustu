package com.sun.naming.internal;

import java.lang.ref.WeakReference;

/* loaded from: rt.jar:com/sun/naming/internal/NamedWeakReference.class */
class NamedWeakReference<T> extends WeakReference<T> {
    private final String name;

    NamedWeakReference(T t2, String str) {
        super(t2);
        this.name = str;
    }

    String getName() {
        return this.name;
    }
}
