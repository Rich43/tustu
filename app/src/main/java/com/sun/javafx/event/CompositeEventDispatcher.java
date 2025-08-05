package com.sun.javafx.event;

import javafx.event.Event;

/* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventDispatcher.class */
public abstract class CompositeEventDispatcher extends BasicEventDispatcher {
    public abstract BasicEventDispatcher getFirstDispatcher();

    public abstract BasicEventDispatcher getLastDispatcher();

    @Override // com.sun.javafx.event.BasicEventDispatcher
    public final Event dispatchCapturingEvent(Event event) {
        BasicEventDispatcher firstDispatcher = getFirstDispatcher();
        while (true) {
            BasicEventDispatcher childDispatcher = firstDispatcher;
            if (childDispatcher == null) {
                break;
            }
            event = childDispatcher.dispatchCapturingEvent(event);
            if (event.isConsumed()) {
                break;
            }
            firstDispatcher = childDispatcher.getNextDispatcher();
        }
        return event;
    }

    @Override // com.sun.javafx.event.BasicEventDispatcher
    public final Event dispatchBubblingEvent(Event event) {
        BasicEventDispatcher lastDispatcher = getLastDispatcher();
        while (true) {
            BasicEventDispatcher childDispatcher = lastDispatcher;
            if (childDispatcher == null) {
                break;
            }
            event = childDispatcher.dispatchBubblingEvent(event);
            if (event.isConsumed()) {
                break;
            }
            lastDispatcher = childDispatcher.getPreviousDispatcher();
        }
        return event;
    }
}
