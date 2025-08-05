package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/ZoomEvent.class */
public final class ZoomEvent extends GestureEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<ZoomEvent> ANY = new EventType<>(GestureEvent.ANY, "ANY_ZOOM");
    public static final EventType<ZoomEvent> ZOOM = new EventType<>(ANY, "ZOOM");
    public static final EventType<ZoomEvent> ZOOM_STARTED = new EventType<>(ANY, "ZOOM_STARTED");
    public static final EventType<ZoomEvent> ZOOM_FINISHED = new EventType<>(ANY, "ZOOM_FINISHED");
    private final double zoomFactor;
    private final double totalZoomFactor;

    public ZoomEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<ZoomEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("zoomFactor") double zoomFactor, @NamedArg("totalZoomFactor") double totalZoomFactor, @NamedArg("pickResult") PickResult pickResult) {
        super(source, target, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, pickResult);
        this.zoomFactor = zoomFactor;
        this.totalZoomFactor = totalZoomFactor;
    }

    public ZoomEvent(@NamedArg("eventType") EventType<ZoomEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("zoomFactor") double zoomFactor, @NamedArg("totalZoomFactor") double totalZoomFactor, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, zoomFactor, totalZoomFactor, pickResult);
    }

    public double getZoomFactor() {
        return this.zoomFactor;
    }

    public double getTotalZoomFactor() {
        return this.totalZoomFactor;
    }

    @Override // javafx.scene.input.GestureEvent, java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("ZoomEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", zoomFactor = ").append(getZoomFactor());
        sb.append(", totalZoomFactor = ").append(getTotalZoomFactor());
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
    public ZoomEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ZoomEvent) super.copyFor(newSource, newTarget);
    }

    public ZoomEvent copyFor(Object newSource, EventTarget newTarget, EventType<ZoomEvent> type) {
        ZoomEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.GestureEvent, javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<ZoomEvent> getEventType() {
        return super.getEventType();
    }
}
