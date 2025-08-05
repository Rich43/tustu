package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;

/* loaded from: jfxrt.jar:javafx/scene/input/GestureEvent.class */
public class GestureEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<GestureEvent> ANY = new EventType<>(InputEvent.ANY, "GESTURE");

    /* renamed from: x, reason: collision with root package name */
    private transient double f12670x;

    /* renamed from: y, reason: collision with root package name */
    private transient double f12671y;

    /* renamed from: z, reason: collision with root package name */
    private transient double f12672z;
    private final double screenX;
    private final double screenY;
    private final double sceneX;
    private final double sceneY;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;
    private final boolean metaDown;
    private final boolean direct;
    private final boolean inertia;
    private PickResult pickResult;

    @Deprecated
    protected GestureEvent(EventType<? extends GestureEvent> eventType) {
        this(eventType, 0.0d, 0.0d, 0.0d, 0.0d, false, false, false, false, false, false, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [javafx.scene.input.GestureEvent] */
    @Deprecated
    protected GestureEvent(Object obj, EventTarget eventTarget, EventType<? extends GestureEvent> eventType) {
        super(obj, eventTarget, eventType);
        this.sceneY = 0.0d;
        this.sceneX = 0.0d;
        0.screenY = this;
        this.screenX = this;
        this.f12671y = 0.0d;
        0L.f12670x = this;
        this.inertia = false;
        this.direct = false;
        this.metaDown = false;
        this.altDown = false;
        this.controlDown = false;
        this.shiftDown = false;
    }

    protected GestureEvent(Object source, EventTarget target, EventType<? extends GestureEvent> eventType, double x2, double y2, double screenX, double screenY, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown, boolean direct, boolean inertia, PickResult pickResult) {
        super(source, target, eventType);
        this.f12670x = x2;
        this.f12671y = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.sceneX = x2;
        this.sceneY = y2;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
        this.direct = direct;
        this.inertia = inertia;
        this.pickResult = pickResult != null ? pickResult : new PickResult(target, x2, y2);
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12670x = p2.getX();
        this.f12671y = p2.getY();
        this.f12672z = p2.getZ();
    }

    protected GestureEvent(EventType<? extends GestureEvent> eventType, double x2, double y2, double screenX, double screenY, boolean shiftDown, boolean controlDown, boolean altDown, boolean metaDown, boolean direct, boolean inertia, PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, shiftDown, controlDown, altDown, metaDown, direct, inertia, pickResult);
    }

    private void recomputeCoordinatesToSource(GestureEvent newEvent, Object newSource) {
        Point3D newCoordinates = InputEventUtils.recomputeCoordinates(this.pickResult, newSource);
        newEvent.f12670x = newCoordinates.getX();
        newEvent.f12671y = newCoordinates.getY();
        newEvent.f12672z = newCoordinates.getZ();
    }

    @Override // javafx.event.Event
    public GestureEvent copyFor(Object newSource, EventTarget newTarget) {
        GestureEvent e2 = (GestureEvent) super.copyFor(newSource, newTarget);
        recomputeCoordinatesToSource(e2, newSource);
        return e2;
    }

    public final double getX() {
        return this.f12670x;
    }

    public final double getY() {
        return this.f12671y;
    }

    public final double getZ() {
        return this.f12672z;
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

    public final boolean isDirect() {
        return this.direct;
    }

    public boolean isInertia() {
        return this.inertia;
    }

    public final PickResult getPickResult() {
        return this.pickResult;
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

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("GestureEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.f12670x = this.sceneX;
        this.f12671y = this.sceneY;
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<? extends GestureEvent> getEventType() {
        return super.getEventType();
    }
}
