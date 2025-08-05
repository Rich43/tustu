package com.sun.org.apache.xerces.internal.dom.events;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/events/EventImpl.class */
public class EventImpl implements Event {
    public EventTarget target;
    public EventTarget currentTarget;
    public short eventPhase;
    public String type = null;
    public boolean initialized = false;
    public boolean bubbles = true;
    public boolean cancelable = false;
    public boolean stopPropagation = false;
    public boolean preventDefault = false;
    protected long timeStamp = System.currentTimeMillis();

    @Override // org.w3c.dom.events.Event
    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
        this.type = eventTypeArg;
        this.bubbles = canBubbleArg;
        this.cancelable = cancelableArg;
        this.initialized = true;
    }

    @Override // org.w3c.dom.events.Event
    public boolean getBubbles() {
        return this.bubbles;
    }

    @Override // org.w3c.dom.events.Event
    public boolean getCancelable() {
        return this.cancelable;
    }

    @Override // org.w3c.dom.events.Event
    public EventTarget getCurrentTarget() {
        return this.currentTarget;
    }

    @Override // org.w3c.dom.events.Event
    public short getEventPhase() {
        return this.eventPhase;
    }

    @Override // org.w3c.dom.events.Event
    public EventTarget getTarget() {
        return this.target;
    }

    @Override // org.w3c.dom.events.Event
    public String getType() {
        return this.type;
    }

    @Override // org.w3c.dom.events.Event
    public long getTimeStamp() {
        return this.timeStamp;
    }

    @Override // org.w3c.dom.events.Event
    public void stopPropagation() {
        this.stopPropagation = true;
    }

    @Override // org.w3c.dom.events.Event
    public void preventDefault() {
        this.preventDefault = true;
    }
}
