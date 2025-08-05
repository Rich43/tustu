package org.w3c.dom.events;

/* loaded from: rt.jar:org/w3c/dom/events/Event.class */
public interface Event {
    public static final short CAPTURING_PHASE = 1;
    public static final short AT_TARGET = 2;
    public static final short BUBBLING_PHASE = 3;

    String getType();

    EventTarget getTarget();

    EventTarget getCurrentTarget();

    short getEventPhase();

    boolean getBubbles();

    boolean getCancelable();

    long getTimeStamp();

    void stopPropagation();

    void preventDefault();

    void initEvent(String str, boolean z2, boolean z3);
}
