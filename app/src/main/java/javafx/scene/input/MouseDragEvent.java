package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.ColorPicker;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/MouseDragEvent.class */
public final class MouseDragEvent extends MouseEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<MouseDragEvent> ANY = new EventType<>(MouseEvent.ANY, "MOUSE-DRAG");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_OVER = new EventType<>(ANY, "MOUSE-DRAG_OVER");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_RELEASED = new EventType<>(ANY, "MOUSE-DRAG_RELEASED");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_ENTERED_TARGET = new EventType<>(ANY, "MOUSE-DRAG_ENTERED_TARGET");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_ENTERED = new EventType<>(MOUSE_DRAG_ENTERED_TARGET, "MOUSE-DRAG_ENTERED");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_EXITED_TARGET = new EventType<>(ANY, "MOUSE-DRAG_EXITED_TARGET");
    public static final EventType<MouseDragEvent> MOUSE_DRAG_EXITED = new EventType<>(MOUSE_DRAG_EXITED_TARGET, "MOUSE-DRAG_EXITED");
    private final transient Object gestureSource;

    @Override // javafx.scene.input.MouseEvent
    public /* bridge */ /* synthetic */ MouseEvent copyFor(Object obj, EventTarget eventTarget, EventType eventType) {
        return copyFor(obj, eventTarget, (EventType<? extends MouseEvent>) eventType);
    }

    public MouseDragEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<MouseDragEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg(ColorPicker.STYLE_CLASS_BUTTON) MouseButton button, @NamedArg("clickCount") int clickCount, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("primaryButtonDown") boolean primaryButtonDown, @NamedArg("middleButtonDown") boolean middleButtonDown, @NamedArg("secondaryButtonDown") boolean secondaryButtonDown, @NamedArg("synthesized") boolean synthesized, @NamedArg("popupTrigger") boolean popupTrigger, @NamedArg("pickResult") PickResult pickResult, @NamedArg("gestureSource") Object gestureSource) {
        super(source, target, eventType, x2, y2, screenX, screenY, button, clickCount, shiftDown, controlDown, altDown, metaDown, primaryButtonDown, middleButtonDown, secondaryButtonDown, synthesized, popupTrigger, false, pickResult);
        this.gestureSource = gestureSource;
    }

    public MouseDragEvent(@NamedArg("eventType") EventType<MouseDragEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg(ColorPicker.STYLE_CLASS_BUTTON) MouseButton button, @NamedArg("clickCount") int clickCount, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("primaryButtonDown") boolean primaryButtonDown, @NamedArg("middleButtonDown") boolean middleButtonDown, @NamedArg("secondaryButtonDown") boolean secondaryButtonDown, @NamedArg("synthesized") boolean synthesized, @NamedArg("popupTrigger") boolean popupTrigger, @NamedArg("pickResult") PickResult pickResult, @NamedArg("gestureSource") Object gestureSource) {
        this(null, null, eventType, x2, y2, screenX, screenY, button, clickCount, shiftDown, controlDown, altDown, metaDown, primaryButtonDown, middleButtonDown, secondaryButtonDown, synthesized, popupTrigger, pickResult, gestureSource);
    }

    public Object getGestureSource() {
        return this.gestureSource;
    }

    @Override // javafx.scene.input.MouseEvent, java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("MouseDragEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", gestureSource = ").append(getGestureSource());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", x = ").append(getX()).append(", y = ").append(getY()).append(", z = ").append(getZ());
        if (getButton() != null) {
            sb.append(", button = ").append((Object) getButton());
        }
        if (getClickCount() > 1) {
            sb.append(", clickCount = ").append(getClickCount());
        }
        if (isPrimaryButtonDown()) {
            sb.append(", primaryButtonDown");
        }
        if (isMiddleButtonDown()) {
            sb.append(", middleButtonDown");
        }
        if (isSecondaryButtonDown()) {
            sb.append(", secondaryButtonDown");
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
        if (isSynthesized()) {
            sb.append(", synthesized");
        }
        sb.append(", pickResult = ").append((Object) getPickResult());
        return sb.append("]").toString();
    }

    @Override // javafx.scene.input.MouseEvent, javafx.event.Event
    public MouseDragEvent copyFor(Object newSource, EventTarget newTarget) {
        return (MouseDragEvent) super.copyFor(newSource, newTarget);
    }

    @Override // javafx.scene.input.MouseEvent
    public MouseDragEvent copyFor(Object newSource, EventTarget newTarget, EventType<? extends MouseEvent> type) {
        return (MouseDragEvent) super.copyFor(newSource, newTarget, type);
    }

    @Override // javafx.scene.input.MouseEvent, javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<MouseDragEvent> getEventType() {
        return super.getEventType();
    }
}
