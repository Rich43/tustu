package com.sun.xml.internal.stream.util;

import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/stream/util/ReadOnlyIterator.class */
public class ReadOnlyIterator implements Iterator {
    Iterator iterator;

    public ReadOnlyIterator() {
        this.iterator = null;
    }

    public ReadOnlyIterator(Iterator itr) {
        this.iterator = null;
        this.iterator = itr;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.iterator != null) {
            return this.iterator.hasNext();
        }
        return false;
    }

    @Override // java.util.Iterator
    public Object next() {
        if (this.iterator != null) {
            return this.iterator.next();
        }
        return null;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported");
    }
}
