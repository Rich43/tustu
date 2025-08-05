package javafx.event;

/* loaded from: jfxrt.jar:javafx/event/ActionEvent.class */
public class ActionEvent extends Event {
    private static final long serialVersionUID = 20121107;
    public static final EventType<ActionEvent> ACTION = new EventType<>(Event.ANY, "ACTION");
    public static final EventType<ActionEvent> ANY = ACTION;

    public ActionEvent() {
        super(ACTION);
    }

    public ActionEvent(Object source, EventTarget target) {
        super(source, target, ACTION);
    }

    @Override // javafx.event.Event
    public ActionEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ActionEvent) super.copyFor(newSource, newTarget);
    }

    @Override // javafx.event.Event
    public EventType<? extends ActionEvent> getEventType() {
        return super.getEventType();
    }
}
