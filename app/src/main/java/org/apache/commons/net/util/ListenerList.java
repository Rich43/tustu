package org.apache.commons.net.util;

import java.io.Serializable;
import java.util.EventListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/ListenerList.class */
public class ListenerList implements Serializable, Iterable<EventListener> {
    private static final long serialVersionUID = -1934227607974228213L;
    private final CopyOnWriteArrayList<EventListener> __listeners = new CopyOnWriteArrayList<>();

    public void addListener(EventListener listener) {
        this.__listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        this.__listeners.remove(listener);
    }

    public int getListenerCount() {
        return this.__listeners.size();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<EventListener> iterator() {
        return this.__listeners.iterator();
    }
}
