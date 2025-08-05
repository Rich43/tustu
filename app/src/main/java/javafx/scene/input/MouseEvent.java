package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import javafx.scene.control.ColorPicker;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/MouseEvent.class */
public class MouseEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<MouseEvent> ANY = new EventType<>(InputEvent.ANY, "MOUSE");
    public static final EventType<MouseEvent> MOUSE_PRESSED = new EventType<>(ANY, "MOUSE_PRESSED");
    public static final EventType<MouseEvent> MOUSE_RELEASED = new EventType<>(ANY, "MOUSE_RELEASED");
    public static final EventType<MouseEvent> MOUSE_CLICKED = new EventType<>(ANY, "MOUSE_CLICKED");
    public static final EventType<MouseEvent> MOUSE_ENTERED_TARGET = new EventType<>(ANY, "MOUSE_ENTERED_TARGET");
    public static final EventType<MouseEvent> MOUSE_ENTERED = new EventType<>(MOUSE_ENTERED_TARGET, "MOUSE_ENTERED");
    public static final EventType<MouseEvent> MOUSE_EXITED_TARGET = new EventType<>(ANY, "MOUSE_EXITED_TARGET");
    public static final EventType<MouseEvent> MOUSE_EXITED = new EventType<>(MOUSE_EXITED_TARGET, "MOUSE_EXITED");
    public static final EventType<MouseEvent> MOUSE_MOVED = new EventType<>(ANY, "MOUSE_MOVED");
    public static final EventType<MouseEvent> MOUSE_DRAGGED = new EventType<>(ANY, "MOUSE_DRAGGED");
    public static final EventType<MouseEvent> DRAG_DETECTED = new EventType<>(ANY, "DRAG_DETECTED");
    private final Flags flags;

    /* renamed from: x, reason: collision with root package name */
    private transient double f12699x;

    /* renamed from: y, reason: collision with root package name */
    private transient double f12700y;

    /* renamed from: z, reason: collision with root package name */
    private transient double f12701z;
    private final double screenX;
    private final double screenY;
    private final double sceneX;
    private final double sceneY;
    private final MouseButton button;
    private final int clickCount;
    private final boolean stillSincePress;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;
    private final boolean metaDown;
    private final boolean synthesized;
    private final boolean popupTrigger;
    private final boolean primaryButtonDown;
    private final boolean secondaryButtonDown;
    private final boolean middleButtonDown;
    private PickResult pickResult;

    void recomputeCoordinatesToSource(MouseEvent oldEvent, Object newSource) {
        Point3D newCoordinates = InputEventUtils.recomputeCoordinates(this.pickResult, newSource);
        this.f12699x = newCoordinates.getX();
        this.f12700y = newCoordinates.getY();
        this.f12701z = newCoordinates.getZ();
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<? extends MouseEvent> getEventType() {
        return super.getEventType();
    }

    @Override // javafx.event.Event
    public MouseEvent copyFor(Object newSource, EventTarget newTarget) {
        MouseEvent e2 = (MouseEvent) super.copyFor(newSource, newTarget);
        e2.recomputeCoordinatesToSource(this, newSource);
        return e2;
    }

    public MouseEvent copyFor(Object newSource, EventTarget newTarget, EventType<? extends MouseEvent> eventType) {
        MouseEvent e2 = copyFor(newSource, newTarget);
        e2.eventType = eventType;
        return e2;
    }

    public MouseEvent(@NamedArg("eventType") EventType<? extends MouseEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg(ColorPicker.STYLE_CLASS_BUTTON) MouseButton button, @NamedArg("clickCount") int clickCount, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("primaryButtonDown") boolean primaryButtonDown, @NamedArg("middleButtonDown") boolean middleButtonDown, @NamedArg("secondaryButtonDown") boolean secondaryButtonDown, @NamedArg("synthesized") boolean synthesized, @NamedArg("popupTrigger") boolean popupTrigger, @NamedArg("stillSincePress") boolean stillSincePress, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, button, clickCount, shiftDown, controlDown, altDown, metaDown, primaryButtonDown, middleButtonDown, secondaryButtonDown, synthesized, popupTrigger, stillSincePress, pickResult);
    }

    public MouseEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<? extends MouseEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg(ColorPicker.STYLE_CLASS_BUTTON) MouseButton button, @NamedArg("clickCount") int clickCount, @NamedArg("shiftDown") boolean shiftDown, @NamedArg("controlDown") boolean controlDown, @NamedArg("altDown") boolean altDown, @NamedArg("metaDown") boolean metaDown, @NamedArg("primaryButtonDown") boolean primaryButtonDown, @NamedArg("middleButtonDown") boolean middleButtonDown, @NamedArg("secondaryButtonDown") boolean secondaryButtonDown, @NamedArg("synthesized") boolean synthesized, @NamedArg("popupTrigger") boolean popupTrigger, @NamedArg("stillSincePress") boolean stillSincePress, @NamedArg("pickResult") PickResult pickResult) {
        super(source, target, eventType);
        this.flags = new Flags();
        this.f12699x = x2;
        this.f12700y = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.sceneX = x2;
        this.sceneY = y2;
        this.button = button;
        this.clickCount = clickCount;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
        this.primaryButtonDown = primaryButtonDown;
        this.middleButtonDown = middleButtonDown;
        this.secondaryButtonDown = secondaryButtonDown;
        this.synthesized = synthesized;
        this.stillSincePress = stillSincePress;
        this.popupTrigger = popupTrigger;
        this.pickResult = pickResult;
        this.pickResult = pickResult != null ? pickResult : new PickResult(target, x2, y2);
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12699x = p2.getX();
        this.f12700y = p2.getY();
        this.f12701z = p2.getZ();
    }

    public static MouseDragEvent copyForMouseDragEvent(MouseEvent e2, Object source, EventTarget target, EventType<MouseDragEvent> type, Object gestureSource, PickResult pickResult) {
        MouseDragEvent ev = new MouseDragEvent(source, target, type, e2.sceneX, e2.sceneY, e2.screenX, e2.screenY, e2.button, e2.clickCount, e2.shiftDown, e2.controlDown, e2.altDown, e2.metaDown, e2.primaryButtonDown, e2.middleButtonDown, e2.secondaryButtonDown, e2.synthesized, e2.popupTrigger, pickResult, gestureSource);
        ev.recomputeCoordinatesToSource(e2, source);
        return ev;
    }

    public boolean isDragDetect() {
        return this.flags.dragDetect;
    }

    public void setDragDetect(boolean dragDetect) {
        this.flags.dragDetect = dragDetect;
    }

    public final double getX() {
        return this.f12699x;
    }

    public final double getY() {
        return this.f12700y;
    }

    public final double getZ() {
        return this.f12701z;
    }

    public final double getScreenX() {
        return this.screenX;
    }

    public final double getScreenY() {
        return this.screenY;
    }

    public final double getSceneX() {
        return this.sceneX;
    }

    public final double getSceneY() {
        return this.sceneY;
    }

    public final MouseButton getButton() {
        return this.button;
    }

    public final int getClickCount() {
        return this.clickCount;
    }

    public final boolean isStillSincePress() {
        return this.stillSincePress;
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

    public boolean isSynthesized() {
        return this.synthesized;
    }

    public final boolean isShortcutDown() {
        switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT:
                return this.shiftDown;
            case CONTROL:
                return this.controlDown;
            case ALT:
                return this.altDown;
            case META:
                return this.metaDown;
            default:
                return false;
        }
    }

    public final boolean isPopupTrigger() {
        return this.popupTrigger;
    }

    public final boolean isPrimaryButtonDown() {
        return this.primaryButtonDown;
    }

    public final boolean isSecondaryButtonDown() {
        return this.secondaryButtonDown;
    }

    public final boolean isMiddleButtonDown() {
        return this.middleButtonDown;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("MouseEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
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

    public final PickResult getPickResult() {
        return this.pickResult;
    }

    /* loaded from: jfxrt.jar:javafx/scene/input/MouseEvent$Flags.class */
    private static class Flags implements Cloneable {
        boolean dragDetect;

        private Flags() {
            this.dragDetect = true;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Flags m3882clone() {
            try {
                return (Flags) super.clone();
            } catch (CloneNotSupportedException e2) {
                return null;
            }
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.f12699x = this.sceneX;
        this.f12700y = this.sceneY;
    }
}
