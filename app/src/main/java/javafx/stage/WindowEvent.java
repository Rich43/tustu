package javafx.stage;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/stage/WindowEvent.class */
public class WindowEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<WindowEvent> ANY = new EventType<>(Event.ANY, "WINDOW");
    public static final EventType<WindowEvent> WINDOW_SHOWING = new EventType<>(ANY, "WINDOW_SHOWING");
    public static final EventType<WindowEvent> WINDOW_SHOWN = new EventType<>(ANY, "WINDOW_SHOWN");
    public static final EventType<WindowEvent> WINDOW_HIDING = new EventType<>(ANY, "WINDOW_HIDING");
    public static final EventType<WindowEvent> WINDOW_HIDDEN = new EventType<>(ANY, "WINDOW_HIDDEN");
    public static final EventType<WindowEvent> WINDOW_CLOSE_REQUEST = new EventType<>(ANY, "WINDOW_CLOSE_REQUEST");

    public WindowEvent(@NamedArg("source") Window source, @NamedArg("eventType") EventType<? extends Event> eventType) {
        super(source, source, eventType);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("WindowEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        return sb.append("]").toString();
    }

    @Override // javafx.event.Event
    public WindowEvent copyFor(Object newSource, EventTarget newTarget) {
        return (WindowEvent) super.copyFor(newSource, newTarget);
    }

    public WindowEvent copyFor(Object newSource, EventTarget newTarget, EventType<WindowEvent> type) {
        WindowEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.event.Event
    public EventType<WindowEvent> getEventType() {
        return super.getEventType();
    }
}
