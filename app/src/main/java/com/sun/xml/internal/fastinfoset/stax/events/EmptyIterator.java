package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/EmptyIterator.class */
public class EmptyIterator implements Iterator {
    public static final EmptyIterator instance = new EmptyIterator();

    private EmptyIterator() {
    }

    public static EmptyIterator getInstance() {
        return instance;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return false;
    }

    @Override // java.util.Iterator
    public Object next() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.emptyIterator"));
    }
}
