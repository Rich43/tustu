package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/input/InputEvent.class */
public class InputEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<InputEvent> ANY = new EventType<>(Event.ANY, "INPUT");

    public InputEvent(@NamedArg("eventType") EventType<? extends InputEvent> eventType) {
        super(eventType);
    }

    public InputEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<? extends InputEvent> eventType) {
        super(source, target, eventType);
    }

    @Override // javafx.event.Event
    public EventType<? extends InputEvent> getEventType() {
        return super.getEventType();
    }
}
