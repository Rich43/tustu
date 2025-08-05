package com.sun.xml.internal.ws.util.xml;

import java.util.Iterator;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/NodeListIterator.class */
public class NodeListIterator implements Iterator {
    protected NodeList _list;
    protected int _index = 0;

    public NodeListIterator(NodeList list) {
        this._list = list;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this._list != null && this._index < this._list.getLength();
    }

    @Override // java.util.Iterator
    public Object next() {
        Object obj = this._list.item(this._index);
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
