package java.util;

import java.util.EventListener;

/* loaded from: rt.jar:java/util/EventListenerProxy.class */
public abstract class EventListenerProxy<T extends EventListener> implements EventListener {
    private final T listener;

    public EventListenerProxy(T t2) {
        this.listener = t2;
    }

    public T getListener() {
        return this.listener;
    }
}
