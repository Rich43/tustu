package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/SwipeEvent.class */
public final class SwipeEvent extends GestureEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<SwipeEvent> ANY = new EventType<>(GestureEvent.ANY, "ANY_SWIPE");
    public static final EventType<SwipeEvent> SWIPE_LEFT = new EventType<>(ANY, "SWIPE_LEFT");
    public static final EventType<SwipeEvent> SWIPE_RIGHT = new EventType<>(ANY, "SWIPE_RIGHT");
    public static final EventType<SwipeEvent> SWIPE_UP = new EventType<>(ANY, "SWIPE_UP");
    public static final EventType<SwipeEvent> SWIPE_DOWN = new EventType<>(ANY, "SWIPE_DOWN");
    private final int touchCount;

    public SwipeEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<SwipeEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("touchCount") int touchCount, @NamedArg("pickResult") PickResult pickResult) {
        super(source, target, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, false, pickResult);
        this.touchCount = touchCount;
    }

    public SwipeEvent(@NamedArg("eventType") EventType<SwipeEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("touchCount") int touchCount, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, touchCount, pickResult);
    }

    public int getTouchCount() {
        return this.touchCount;
    }

    @Override // javafx.scene.input.GestureEvent, java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("SwipeEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", touchCount = ").append(getTouchCount());
        sb.append(", x = ").append(getX()).append(", y = ").append(getY()).append(", z = ").append(getZ());
        sb.append(isDirect() ? ", direct" : ", indirect");
        if (isShiftDown()) {
            sb.append(", shiftDown");
        }
        if (isControlDown()) {
            sb.append(", controlDown");
        }
        if (isAltDown()) {
            sb.append(", altDown");
        }
        if (isMetaDown()) {
            sb.append(", metaDown");
        }
        if (isShortcutDown()) {
            sb.append(", shortcutDown");
        }
        sb.append(", pickResult = ").append((Object) getPickResult());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.input.GestureEvent, javafx.event.Event
    public SwipeEvent copyFor(Object newSource, EventTarget newTarget) {
        return (SwipeEvent) super.copyFor(newSource, newTarget);
    }

    public SwipeEvent copyFor(Object newSource, EventTarget newTarget, EventType<SwipeEvent> type) {
        SwipeEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.GestureEvent, javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<SwipeEvent> getEventType() {
        return super.getEventType();
    }
}
