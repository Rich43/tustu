package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/ScrollEvent.class */
public final class ScrollEvent extends GestureEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<ScrollEvent> ANY = new EventType<>(GestureEvent.ANY, "ANY_SCROLL");
    public static final EventType<ScrollEvent> SCROLL = new EventType<>(ANY, "SCROLL");
    public static final EventType<ScrollEvent> SCROLL_STARTED = new EventType<>(ANY, "SCROLL_STARTED");
    public static final EventType<ScrollEvent> SCROLL_FINISHED = new EventType<>(ANY, "SCROLL_FINISHED");
    private final double deltaX;
    private final double deltaY;
    private double totalDeltaX;
    private final double totalDeltaY;
    private final HorizontalTextScrollUnits textDeltaXUnits;
    private final VerticalTextScrollUnits textDeltaYUnits;
    private final double textDeltaX;
    private final double textDeltaY;
    private final int touchCount;
    private final double multiplierX;
    private final double multiplierY;

    /* loaded from: jfxrt.jar:javafx/scene/input/ScrollEvent$HorizontalTextScrollUnits.class */
    public enum HorizontalTextScrollUnits {
        NONE,
        CHARACTERS
    }

    /* loaded from: jfxrt.jar:javafx/scene/input/ScrollEvent$VerticalTextScrollUnits.class */
    public enum VerticalTextScrollUnits {
        NONE,
        LINES,
        PAGES
    }

    private ScrollEvent(Object source, EventTarget target, EventType<ScrollEvent> eventType, double x2, double y2, double screenX, double screenY, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown, boolean direct, boolean inertia, double deltaX, double deltaY, double totalDeltaX, double totalDeltaY, double multiplierX, double multiplierY, HorizontalTextScrollUnits textDeltaXUnits, double textDeltaX, VerticalTextScrollUnits textDeltaYUnits, double textDeltaY, int touchCount, PickResult pickResult) {
        super(source, target, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, pickResult);
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.totalDeltaX = totalDeltaX;
        this.totalDeltaY = totalDeltaY;
        this.textDeltaXUnits = textDeltaXUnits;
        this.textDeltaX = textDeltaX;
        this.textDeltaYUnits = textDeltaYUnits;
        this.textDeltaY = textDeltaY;
        this.touchCount = touchCount;
        this.multiplierX = multiplierX;
        this.multiplierY = multiplierY;
    }

    public ScrollEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<ScrollEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("deltaX") double deltaX, @NamedArg("deltaY") double deltaY, @NamedArg("totalDeltaX") double totalDeltaX, @NamedArg("totalDeltaY") double totalDeltaY, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits textDeltaXUnits, @NamedArg("textDeltaX") double textDeltaX, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits textDeltaYUnits, @NamedArg("textDeltaY") double textDeltaY, @NamedArg("touchCount") int touchCount, @NamedArg("pickResult") PickResult pickResult) {
        this(source, target, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, deltaX, deltaY, totalDeltaX, totalDeltaY, 1.0d, 1.0d, textDeltaXUnits, textDeltaX, textDeltaYUnits, textDeltaY, touchCount, pickResult);
    }

    public ScrollEvent(@NamedArg("eventType") EventType<ScrollEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("deltaX") double deltaX, @NamedArg("deltaY") double deltaY, @NamedArg("totalDeltaX") double totalDeltaX, @NamedArg("totalDeltaY") double totalDeltaY, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits textDeltaXUnits, @NamedArg("textDeltaX") double textDeltaX, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits textDeltaYUnits, @NamedArg("textDeltaY") double textDeltaY, @NamedArg("touchCount") int touchCount, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, deltaX, deltaY, totalDeltaX, totalDeltaY, 1.0d, 1.0d, textDeltaXUnits, textDeltaX, textDeltaYUnits, textDeltaY, touchCount, pickResult);
    }

    public ScrollEvent(@NamedArg("eventType") EventType<ScrollEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("direct") boolean direct, @NamedArg("inertia") boolean inertia, @NamedArg("deltaX") double deltaX, @NamedArg("deltaY") double deltaY, @NamedArg("totalDeltaX") double totalDeltaX, @NamedArg("totalDeltaY") double totalDeltaY, @NamedArg("multiplierX") double multiplierX, @NamedArg("multiplierY") double multiplierY, @NamedArg("textDeltaXUnits") HorizontalTextScrollUnits textDeltaXUnits, @NamedArg("textDeltaX") double textDeltaX, @NamedArg("textDeltaYUnits") VerticalTextScrollUnits textDeltaYUnits, @NamedArg("textDeltaY") double textDeltaY, @NamedArg("touchCount") int touchCount, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, deltaX, deltaY, totalDeltaX, totalDeltaY, multiplierX, multiplierY, textDeltaXUnits, textDeltaX, textDeltaYUnits, textDeltaY, touchCount, pickResult);
    }

    public double getDeltaX() {
        return this.deltaX;
    }

    public double getDeltaY() {
        return this.deltaY;
    }

    public double getTotalDeltaX() {
        return this.totalDeltaX;
    }

    public double getTotalDeltaY() {
        return this.totalDeltaY;
    }

    public HorizontalTextScrollUnits getTextDeltaXUnits() {
        return this.textDeltaXUnits;
    }

    public VerticalTextScrollUnits getTextDeltaYUnits() {
        return this.textDeltaYUnits;
    }

    public double getTextDeltaX() {
        return this.textDeltaX;
    }

    public double getTextDeltaY() {
        return this.textDeltaY;
    }

    public int getTouchCount() {
        return this.touchCount;
    }

    public double getMultiplierX() {
        return this.multiplierX;
    }

    public double getMultiplierY() {
        return this.multiplierY;
    }

    @Override // javafx.scene.input.GestureEvent, java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("ScrollEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", deltaX = ").append(getDeltaX()).append(", deltaY = ").append(getDeltaY());
        sb.append(", totalDeltaX = ").append(getTotalDeltaX()).append(", totalDeltaY = ").append(getTotalDeltaY());
        sb.append(", textDeltaXUnits = ").append((Object) getTextDeltaXUnits()).append(", textDeltaX = ").append(getTextDeltaX());
        sb.append(", textDeltaYUnits = ").append((Object) getTextDeltaYUnits()).append(", textDeltaY = ").append(getTextDeltaY());
        sb.append(", touchCount = ").append(getTouchCount());
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
    public ScrollEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ScrollEvent) super.copyFor(newSource, newTarget);
    }

    public ScrollEvent copyFor(Object newSource, EventTarget newTarget, EventType<ScrollEvent> type) {
        ScrollEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.GestureEvent, javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<ScrollEvent> getEventType() {
        return super.getEventType();
    }
}
