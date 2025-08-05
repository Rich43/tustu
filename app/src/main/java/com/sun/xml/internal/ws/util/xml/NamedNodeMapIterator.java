package com.sun.xml.internal.ws.util.xml;

import java.util.Iterator;
import org.w3c.dom.NamedNodeMap;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/NamedNodeMapIterator.class */
public class NamedNodeMapIterator implements Iterator {
    protected NamedNodeMap _map;
    protected int _index = 0;

    public NamedNodeMapIterator(NamedNodeMap map) {
        this._map = map;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this._map != null && this._index < this._map.getLength();
    }

    @Override // java.util.Iterator
    public Object next() {
        Object obj = this._map.item(this._index);
        if (obj != null) {
            this._index++;
        }
        return obj;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
