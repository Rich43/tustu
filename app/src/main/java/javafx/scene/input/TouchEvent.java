package javafx.scene.input;

import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/input/TouchEvent.class */
public final class TouchEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<TouchEvent> ANY = new EventType<>(InputEvent.ANY, "TOUCH");
    public static final EventType<TouchEvent> TOUCH_PRESSED = new EventType<>(ANY, "TOUCH_PRESSED");
    public static final EventType<TouchEvent> TOUCH_MOVED = new EventType<>(ANY, "TOUCH_MOVED");
    public static final EventType<TouchEvent> TOUCH_RELEASED = new EventType<>(ANY, "TOUCH_RELEASED");
    public static final EventType<TouchEvent> TOUCH_STATIONARY = new EventType<>(ANY, "TOUCH_STATIONARY");
    private final int eventSetId;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;
    private final boolean metaDown;
    private final TouchPoint touchPoint;
    private final List<TouchPoint> touchPoints;

    public TouchEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<TouchEvent> eventType, @NamedArg("touchPoint") TouchPoint touchPoint, @NamedArg("touchPoints") List<TouchPoint> touchPoints, @NamedArg("eventSetId") int eventSetId, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown) {
        super(source, target, eventType);
        this.touchPoints = touchPoints != null ? Collections.unmodifiableList(touchPoints) : null;
        this.eventSetId = eventSetId;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
        this.touchPoint = touchPoint;
    }

    public TouchEvent(@NamedArg("eventType") EventType<TouchEvent> eventType, @NamedArg("touchPoint") TouchPoint touchPoint, @NamedArg("touchPoints") List<TouchPoint> touchPoints, @NamedArg("eventSetId") int eventSetId, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown) {
        this(null, null, eventType, touchPoint, touchPoints, eventSetId, shiftDown, controlDown, altDown, metaDown);
    }

    public int getTouchCount() {
        return this.touchPoints.size();
    }

    private static void recomputeToSource(TouchEvent event, Object oldSource, Object newSource) {
        for (TouchPoint tp : event.touchPoints) {
            tp.recomputeToSource(oldSource, newSource);
        }
    }

    @Override // javafx.event.Event
    public TouchEvent copyFor(Object newSource, EventTarget newTarget) {
        TouchEvent e2 = (TouchEvent) super.copyFor(newSource, newTarget);
        recomputeToSource(e2, getSource(), newSource);
        return e2;
    }

    public TouchEvent copyFor(Object newSource, EventTarget newTarget, EventType<TouchEvent> type) {
        TouchEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<TouchEvent> getEventType() {
        return super.getEventType();
    }

    public final int getEventSetId() {
        return this.eventSetId;
    }

    public final boolean isShiftDown() {
        return this.shiftDown;
    }

    public final boolean isControlDown() {
        return this.controlDown;
    }

    public final boolean isAltDown() {
        return this.altDown;
    }

    public final boolean isMetaDown() {
        return this.metaDown;
    }

    public TouchPoint getTouchPoint() {
        return this.touchPoint;
    }

    public List<TouchPoint> getTouchPoints() {
        return this.touchPoints;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("TouchEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", touchCount = ").append(getTouchCount());
        sb.append(", eventSetId = ").append(getEventSetId());
        sb.append(", touchPoint = ").append(getTouchPoint().toString());
        return sb.append("]").toString();
    }
}
