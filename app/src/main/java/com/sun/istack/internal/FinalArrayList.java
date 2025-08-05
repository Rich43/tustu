package com.sun.istack.internal;

import java.util.ArrayList;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/istack/internal/FinalArrayList.class */
public final class FinalArrayList<T> extends ArrayList<T> {
    public FinalArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public FinalArrayList() {
    }

    public FinalArrayList(Collection<? extends T> ts) {
        super(ts);
    }
}
