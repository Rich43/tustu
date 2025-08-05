package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/RotateEvent.class */
public final class RotateEvent extends GestureEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<RotateEvent> ANY = new EventType<>(GestureEvent.ANY, "ANY_ROTATE");
    public static final EventType<RotateEvent> ROTATE = new EventType<>(ANY, "ROTATE");
    public static final EventType<RotateEvent> ROTATION_STARTED = new EventType<>(ANY, "ROTATION_STARTED");
    public static final EventType<RotateEvent> ROTATION_FINISHED = new EventType<>(ANY, "ROTATION_FINISHED");
    private final double angle;
    private final double totalAngle;

    public RotateEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<RotateEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("angle") double angle, @NamedArg("totalAngle") double totalAngle, @NamedArg("pickResult") PickResult pickResult) {
        super(source, target, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, pickResult);
        this.angle = angle;
        this.totalAngle = totalAngle;
    }

    public RotateEvent(@NamedArg("eventType") EventType<RotateEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("angle") double angle, @NamedArg("totalAngle") double totalAngle, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, angle, totalAngle, pickResult);
    }

    public double getAngle() {
        return this.angle;
    }

    public double getTotalAngle() {
        return this.totalAngle;
    }

    @Override // javafx.scene.input.GestureEvent, java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("RotateEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", angle = ").append(getAngle());
        sb.append(", totalAngle = ").append(getTotalAngle());
        sb.append(", x = ").append(getX()).append(", y = ").append(getY()).append(", z = ").append(getZ());
        sb.append(isDirect() ? ", direct" : ", indirect");
        if (isInertia()) {
            sb.append(", inertia");
        }
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
    public RotateEvent copyFor(Object newSource, EventTarget newTarget) {
        return (RotateEvent) super.copyFor(newSource, newTarget);
    }

    public RotateEvent copyFor(Object newSource, EventTarget newTarget, EventType<RotateEvent> type) {
        RotateEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.GestureEvent, javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<RotateEvent> getEventType() {
        return super.getEventType();
    }
}
