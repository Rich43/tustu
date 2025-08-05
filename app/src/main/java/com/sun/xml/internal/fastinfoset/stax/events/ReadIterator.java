package com.sun.xml.internal.fastinfoset.stax.events;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/ReadIterator.class */
public class ReadIterator implements Iterator {
    Iterator iterator;

    public ReadIterator() {
        this.iterator = EmptyIterator.getInstance();
    }

    public ReadIterator(Iterator iterator) {
        this.iterator = EmptyIterator.getInstance();
        if (iterator != null) {
            this.iterator = iterator;
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override // java.util.Iterator
    public Object next() {
        return this.iterator.next();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.readonlyList"));
    }
}
