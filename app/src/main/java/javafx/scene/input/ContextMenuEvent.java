package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/ContextMenuEvent.class */
public class ContextMenuEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<ContextMenuEvent> CONTEXT_MENU_REQUESTED = new EventType<>(InputEvent.ANY, "CONTEXTMENUREQUESTED");
    public static final EventType<ContextMenuEvent> ANY = CONTEXT_MENU_REQUESTED;
    private final boolean keyboardTrigger;

    /* renamed from: x, reason: collision with root package name */
    private transient double f12664x;

    /* renamed from: y, reason: collision with root package name */
    private transient double f12665y;

    /* renamed from: z, reason: collision with root package name */
    private transient double f12666z;
    private final double screenX;
    private final double screenY;
    private final double sceneX;
    private final double sceneY;
    private PickResult pickResult;

    public ContextMenuEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<ContextMenuEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("keyboardTrigger") boolean keyboardTrigger, @NamedArg("pickResult") PickResult pickResult) {
        super(source, target, eventType);
        this.screenX = screenX;
        this.screenY = screenY;
        this.sceneX = x2;
        this.sceneY = y2;
        this.f12664x = x2;
        this.f12665y = y2;
        this.pickResult = pickResult != null ? pickResult : new PickResult(target, x2, y2);
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12664x = p2.getX();
        this.f12665y = p2.getY();
        this.f12666z = p2.getZ();
        this.keyboardTrigger = keyboardTrigger;
    }

    public ContextMenuEvent(@NamedArg("eventType") EventType<ContextMenuEvent> eventType, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("keyboardTrigger") boolean keyboardTrigger, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, x2, y2, screenX, screenY, keyboardTrigger, pickResult);
    }

    private void recomputeCoordinatesToSource(ContextMenuEvent newEvent, Object newSource) {
        Point3D newCoordinates = InputEventUtils.recomputeCoordinates(this.pickResult, newSource);
        newEvent.f12664x = newCoordinates.getX();
        newEvent.f12665y = newCoordinates.getY();
        newEvent.f12666z = newCoordinates.getZ();
    }

    @Override // javafx.event.Event
    public ContextMenuEvent copyFor(Object newSource, EventTarget newTarget) {
        ContextMenuEvent e2 = (ContextMenuEvent) super.copyFor(newSource, newTarget);
        recomputeCoordinatesToSource(e2, newSource);
        return e2;
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<ContextMenuEvent> getEventType() {
        return super.getEventType();
    }

    public boolean isKeyboardTrigger() {
        return this.keyboardTrigger;
    }

    public final double getX() {
        return this.f12664x;
    }

    public final double getY() {
        return this.f12665y;
    }

    public final double getZ() {
        return this.f12666z;
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

    public final PickResult getPickResult() {
        return this.pickResult;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("ContextMenuEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", x = ").append(getX()).append(", y = ").append(getY()).append(", z = ").append(getZ());
        sb.append(", pickResult = ").append((Object) getPickResult());
        return sb.append("]").toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.f12664x = this.sceneX;
        this.f12665y = this.sceneY;
    }
}
