package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/control/DialogEvent.class */
public class DialogEvent extends Event {
    private static final long serialVersionUID = 20140716;
    public static final EventType<DialogEvent> ANY = new EventType<>(Event.ANY, "DIALOG");
    public static final EventType<DialogEvent> DIALOG_SHOWING = new EventType<>(ANY, "DIALOG_SHOWING");
    public static final EventType<DialogEvent> DIALOG_SHOWN = new EventType<>(ANY, "DIALOG_SHOWN");
    public static final EventType<DialogEvent> DIALOG_HIDING = new EventType<>(ANY, "DIALOG_HIDING");
    public static final EventType<DialogEvent> DIALOG_HIDDEN = new EventType<>(ANY, "DIALOG_HIDDEN");
    public static final EventType<DialogEvent> DIALOG_CLOSE_REQUEST = new EventType<>(ANY, "DIALOG_CLOSE_REQUEST");

    public DialogEvent(@NamedArg("source") Dialog<?> source, @NamedArg("eventType") EventType<? extends Event> eventType) {
        super(source, source, eventType);
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("DialogEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        return sb.append("]").toString();
    }

    @Override // javafx.event.Event
    public DialogEvent copyFor(Object newSource, EventTarget newTarget) {
        return (DialogEvent) super.copyFor(newSource, newTarget);
    }

    public DialogEvent copyFor(Object newSource, EventTarget newTarget, EventType<DialogEvent> type) {
        DialogEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.event.Event
    public EventType<DialogEvent> getEventType() {
        return super.getEventType();
    }
}
