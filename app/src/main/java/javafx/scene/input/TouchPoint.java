package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Scene;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/TouchPoint.class */
public final class TouchPoint implements Serializable {
    private transient EventTarget target;
    private transient Object source;
    private EventTarget grabbed = null;
    private int id;
    private State state;

    /* renamed from: x, reason: collision with root package name */
    private transient double f12702x;

    /* renamed from: y, reason: collision with root package name */
    private transient double f12703y;

    /* renamed from: z, reason: collision with root package name */
    private transient double f12704z;
    private double screenX;
    private double screenY;
    private double sceneX;
    private double sceneY;
    private PickResult pickResult;

    /* loaded from: jfxrt.jar:javafx/scene/input/TouchPoint$State.class */
    public enum State {
        PRESSED,
        MOVED,
        STATIONARY,
        RELEASED
    }

    public TouchPoint(@NamedArg("id") int id, @NamedArg("state") State state, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("target") EventTarget target, @NamedArg("pickResult") PickResult pickResult) {
        this.target = target;
        this.id = id;
        this.state = state;
        this.f12702x = x2;
        this.f12703y = y2;
        this.sceneX = x2;
        this.sceneY = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.pickResult = pickResult != null ? pickResult : new PickResult(target, x2, y2);
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12702x = p2.getX();
        this.f12703y = p2.getY();
        this.f12704z = p2.getZ();
    }

    void recomputeToSource(Object oldSource, Object newSource) {
        Point3D newCoordinates = InputEventUtils.recomputeCoordinates(this.pickResult, newSource);
        this.f12702x = newCoordinates.getX();
        this.f12703y = newCoordinates.getY();
        this.f12704z = newCoordinates.getZ();
        this.source = newSource;
    }

    public boolean belongsTo(EventTarget target) {
        if (this.target instanceof Node) {
            Node n2 = (Node) this.target;
            if (target instanceof Scene) {
                return n2.getScene() == target;
            }
            while (n2 != null) {
                if (n2 == target) {
                    return true;
                }
                n2 = n2.getParent();
            }
        }
        return target == this.target;
    }

    @Deprecated
    public void impl_reset() {
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12702x = p2.getX();
        this.f12703y = p2.getY();
        this.f12704z = p2.getZ();
    }

    public EventTarget getGrabbed() {
        return this.grabbed;
    }

    public void grab() {
        if (this.source instanceof EventTarget) {
            this.grabbed = (EventTarget) this.source;
            return;
        }
        throw new IllegalStateException("Cannot grab touch point, source is not an instance of EventTarget: " + this.source);
    }

    public void grab(EventTarget target) {
        this.grabbed = target;
    }

    public void ungrab() {
        this.grabbed = null;
    }

    public final int getId() {
        return this.id;
    }

    public final State getState() {
        return this.state;
    }

    public final double getX() {
        return this.f12702x;
    }

    public final double getY() {
        return this.f12703y;
    }

    public final double getZ() {
        return this.f12704z;
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

    public EventTarget getTarget() {
        return this.target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("TouchPoint [");
        sb.append("state = ").append((Object) getState());
        sb.append(", id = ").append(getId());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", x = ").append(getX()).append(", y = ").append(getY()).append(", z = ").append(getZ());
        sb.append(", pickResult = ").append((Object) getPickResult());
        return sb.append("]").toString();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.f12702x = this.sceneX;
        this.f12703y = this.sceneY;
    }
}
