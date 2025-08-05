package org.w3c.dom.events;

/* loaded from: rt.jar:org/w3c/dom/events/EventTarget.class */
public interface EventTarget {
    void addEventListener(String str, EventListener eventListener, boolean z2);

    void removeEventListener(String str, EventListener eventListener, boolean z2);

    boolean dispatchEvent(Event event) throws EventException;
}
