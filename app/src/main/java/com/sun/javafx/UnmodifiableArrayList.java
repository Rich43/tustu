package com.sun.javafx;

import java.util.AbstractList;
import java.util.RandomAccess;

/* loaded from: jfxrt.jar:com/sun/javafx/UnmodifiableArrayList.class */
public class UnmodifiableArrayList<T> extends AbstractList<T> implements RandomAccess {
    private T[] elements;
    private final int size;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UnmodifiableArrayList.class.desiredAssertionStatus();
    }

    public UnmodifiableArrayList(T[] elements, int size) {
        if (!$assertionsDisabled && (elements != null ? size > elements.length : size != 0)) {
            throw new AssertionError();
        }
        this.size = size;
        this.elements = elements;
    }

    @Override // java.util.AbstractList, java.util.List
    public T get(int index) {
        return this.elements[index];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }
}
