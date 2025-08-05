package com.sun.javafx.stage;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/FocusUngrabEvent.class */
public final class FocusUngrabEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<FocusUngrabEvent> FOCUS_UNGRAB = new EventType<>(Event.ANY, "FOCUS_UNGRAB");
    public static final EventType<FocusUngrabEvent> ANY = FOCUS_UNGRAB;

    public FocusUngrabEvent() {
        super(FOCUS_UNGRAB);
    }

    public FocusUngrabEvent(Object source, EventTarget target) {
        super(source, target, FOCUS_UNGRAB);
    }
}
