package com.sun.xml.internal.bind.v2.util;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/FlattenIterator.class */
public final class FlattenIterator<T> implements Iterator<T> {
    private final Iterator<? extends Map<?, ? extends T>> parent;
    private Iterator<? extends T> child = null;
    private T next;

    public FlattenIterator(Iterable<? extends Map<?, ? extends T>> core) {
        this.parent = core.iterator();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        getNext();
        return this.next != null;
    }

    @Override // java.util.Iterator
    public T next() {
        T r2 = this.next;
        this.next = null;
        if (r2 == null) {
            throw new NoSuchElementException();
        }
        return r2;
    }

    private void getNext() {
        if (this.next != null) {
            return;
        }
        if (this.child != null && this.child.hasNext()) {
            this.next = this.child.next();
        } else if (this.parent.hasNext()) {
            this.child = this.parent.next().values().iterator();
            getNext();
        }
    }
}
