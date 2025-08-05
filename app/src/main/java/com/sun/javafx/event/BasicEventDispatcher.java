package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

/* loaded from: jfxrt.jar:com/sun/javafx/event/BasicEventDispatcher.class */
public abstract class BasicEventDispatcher implements EventDispatcher {
    private BasicEventDispatcher previousDispatcher;
    private BasicEventDispatcher nextDispatcher;

    @Override // javafx.event.EventDispatcher
    public Event dispatchEvent(Event event, EventDispatchChain tail) {
        Event event2 = dispatchCapturingEvent(event);
        if (event2.isConsumed()) {
            return null;
        }
        Event event3 = tail.dispatchEvent(event2);
        if (event3 != null) {
            event3 = dispatchBubblingEvent(event3);
            if (event3.isConsumed()) {
                return null;
            }
        }
        return event3;
    }

    public Event dispatchCapturingEvent(Event event) {
        return event;
    }

    public Event dispatchBubblingEvent(Event event) {
        return event;
    }

    public final BasicEventDispatcher getPreviousDispatcher() {
        return this.previousDispatcher;
    }

    public final BasicEventDispatcher getNextDispatcher() {
        return this.nextDispatcher;
    }

    public final void insertNextDispatcher(BasicEventDispatcher newDispatcher) {
        if (this.nextDispatcher != null) {
            this.nextDispatcher.previousDispatcher = newDispatcher;
        }
        newDispatcher.nextDispatcher = this.nextDispatcher;
        newDispatcher.previousDispatcher = this;
        this.nextDispatcher = newDispatcher;
    }
}
