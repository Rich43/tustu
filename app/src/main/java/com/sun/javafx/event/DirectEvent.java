package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:com/sun/javafx/event/DirectEvent.class */
public class DirectEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<DirectEvent> DIRECT = new EventType<>(Event.ANY, "DIRECT");
    private final Event originalEvent;

    public DirectEvent(Event originalEvent) {
        this(originalEvent, null, null);
    }

    public DirectEvent(Event originalEvent, Object source, EventTarget target) {
        super(source, target, DIRECT);
        this.originalEvent = originalEvent;
    }

    public Event getOriginalEvent() {
        return this.originalEvent;
    }
}
