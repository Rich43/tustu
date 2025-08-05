package javafx.scene.input;

import com.sun.javafx.scene.input.InputEventUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.EnumSet;
import java.util.Set;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/input/DragEvent.class */
public final class DragEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<DragEvent> ANY = new EventType<>(InputEvent.ANY, "DRAG");
    public static final EventType<DragEvent> DRAG_ENTERED_TARGET = new EventType<>(ANY, "DRAG_ENTERED_TARGET");
    public static final EventType<DragEvent> DRAG_ENTERED = new EventType<>(DRAG_ENTERED_TARGET, "DRAG_ENTERED");
    public static final EventType<DragEvent> DRAG_EXITED_TARGET = new EventType<>(ANY, "DRAG_EXITED_TARGET");
    public static final EventType<DragEvent> DRAG_EXITED = new EventType<>(DRAG_EXITED_TARGET, "DRAG_EXITED");
    public static final EventType<DragEvent> DRAG_OVER = new EventType<>(ANY, "DRAG_OVER");
    public static final EventType<DragEvent> DRAG_DROPPED = new EventType<>(ANY, "DRAG_DROPPED");
    public static final EventType<DragEvent> DRAG_DONE = new EventType<>(ANY, "DRAG_DONE");

    /* renamed from: x, reason: collision with root package name */
    private transient double f12667x;

    /* renamed from: y, reason: collision with root package name */
    private transient double f12668y;

    /* renamed from: z, reason: collision with root package name */
    private transient double f12669z;
    private final double screenX;
    private final double screenY;
    private final double sceneX;
    private final double sceneY;
    private PickResult pickResult;
    private Object gestureSource;
    private Object gestureTarget;
    private TransferMode transferMode;
    private final State state;
    private transient Dragboard dragboard;

    public DragEvent copyFor(Object source, EventTarget target, Object gestureSource, Object gestureTarget, EventType<DragEvent> eventType) {
        DragEvent copyEvent = copyFor(source, target, eventType);
        recomputeCoordinatesToSource(copyEvent, source);
        copyEvent.gestureSource = gestureSource;
        copyEvent.gestureTarget = gestureTarget;
        return copyEvent;
    }

    public DragEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<DragEvent> eventType, @NamedArg("dragboard") Dragboard dragboard, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("transferMode") TransferMode transferMode, @NamedArg("gestureSource") Object gestureSource, @NamedArg("gestureTarget") Object gestureTarget, @NamedArg("pickResult") PickResult pickResult) {
        PickResult pickResult2;
        super(source, target, eventType);
        this.state = new State();
        this.gestureSource = gestureSource;
        this.gestureTarget = gestureTarget;
        this.f12667x = x2;
        this.f12668y = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.sceneX = x2;
        this.sceneY = y2;
        this.transferMode = transferMode;
        this.dragboard = dragboard;
        if (eventType == DRAG_DROPPED || eventType == DRAG_DONE) {
            this.state.accepted = transferMode != null;
            this.state.acceptedTrasferMode = transferMode;
        }
        if (pickResult != null) {
            pickResult2 = pickResult;
        } else {
            pickResult2 = new PickResult(eventType == DRAG_DONE ? null : target, x2, y2);
        }
        this.pickResult = pickResult2;
        Point3D p2 = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.f12667x = p2.getX();
        this.f12668y = p2.getY();
        this.f12669z = p2.getZ();
    }

    public DragEvent(@NamedArg("eventType") EventType<DragEvent> eventType, @NamedArg("dragboard") Dragboard dragboard, @NamedArg(LanguageTag.PRIVATEUSE) double x2, @NamedArg(PdfOps.y_TOKEN) double y2, @NamedArg("screenX") double screenX, @NamedArg("screenY") double screenY, @NamedArg("transferMode") TransferMode transferMode, @NamedArg("gestureSource") Object gestureSource, @NamedArg("gestureTarget") Object gestureTarget, @NamedArg("pickResult") PickResult pickResult) {
        this(null, null, eventType, dragboard, x2, y2, screenX, screenY, transferMode, gestureSource, gestureTarget, pickResult);
    }

    private void recomputeCoordinatesToSource(DragEvent newEvent, Object newSource) {
        if (newEvent.getEventType() == DRAG_DONE) {
            return;
        }
        Point3D newCoordinates = InputEventUtils.recomputeCoordinates(this.pickResult, newSource);
        newEvent.f12667x = newCoordinates.getX();
        newEvent.f12668y = newCoordinates.getY();
        newEvent.f12669z = newCoordinates.getZ();
    }

    @Override // javafx.event.Event
    public DragEvent copyFor(Object newSource, EventTarget newTarget) {
        DragEvent e2 = (DragEvent) super.copyFor(newSource, newTarget);
        recomputeCoordinatesToSource(e2, newSource);
        return e2;
    }

    public DragEvent copyFor(Object source, EventTarget target, EventType<DragEvent> type) {
        DragEvent e2 = copyFor(source, target);
        e2.eventType = type;
        return e2;
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<DragEvent> getEventType() {
        return super.getEventType();
    }

    public final double getX() {
        return this.f12667x;
    }

    public final double getY() {
        return this.f12668y;
    }

    public final double getZ() {
        return this.f12669z;
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

    public final Object getGestureSource() {
        return this.gestureSource;
    }

    public final Object getGestureTarget() {
        return this.gestureTarget;
    }

    public final TransferMode getTransferMode() {
        return this.transferMode;
    }

    public final boolean isAccepted() {
        return this.state.accepted;
    }

    public final TransferMode getAcceptedTransferMode() {
        return this.state.acceptedTrasferMode;
    }

    public final Object getAcceptingObject() {
        return this.state.acceptingObject;
    }

    public final Dragboard getDragboard() {
        return this.dragboard;
    }

    private static TransferMode chooseTransferMode(Set<TransferMode> supported, TransferMode[] accepted, TransferMode proposed) {
        TransferMode result = null;
        Set<TransferMode> intersect = EnumSet.noneOf(TransferMode.class);
        for (TransferMode tm : InputEventUtils.safeTransferModes(accepted)) {
            if (supported.contains(tm)) {
                intersect.add(tm);
            }
        }
        if (intersect.contains(proposed)) {
            result = proposed;
        } else if (intersect.contains(TransferMode.MOVE)) {
            result = TransferMode.MOVE;
        } else if (intersect.contains(TransferMode.COPY)) {
            result = TransferMode.COPY;
        } else if (intersect.contains(TransferMode.LINK)) {
            result = TransferMode.LINK;
        }
        return result;
    }

    public void acceptTransferModes(TransferMode... transferModes) {
        if (this.dragboard == null || this.dragboard.getTransferModes() == null || this.transferMode == null) {
            this.state.accepted = false;
            return;
        }
        TransferMode tm = chooseTransferMode(this.dragboard.getTransferModes(), transferModes, this.transferMode);
        if (tm == null && getEventType() == DRAG_DROPPED) {
            throw new IllegalStateException("Accepting unsupported transfer modes inside DRAG_DROPPED handler");
        }
        this.state.accepted = tm != null;
        this.state.acceptedTrasferMode = tm;
        this.state.acceptingObject = this.state.accepted ? this.source : null;
    }

    public void setDropCompleted(boolean isTransferDone) {
        if (getEventType() != DRAG_DROPPED) {
            throw new IllegalStateException("setDropCompleted can be called only from DRAG_DROPPED handler");
        }
        this.state.dropCompleted = isTransferDone;
    }

    public boolean isDropCompleted() {
        return this.state.dropCompleted;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.f12667x = this.sceneX;
        this.f12668y = this.sceneY;
    }

    /* loaded from: jfxrt.jar:javafx/scene/input/DragEvent$State.class */
    private static class State {
        boolean accepted;
        boolean dropCompleted;
        TransferMode acceptedTrasferMode;
        Object acceptingObject;

        private State() {
            this.accepted = false;
            this.dropCompleted = false;
            this.acceptedTrasferMode = null;
            this.acceptingObject = null;
        }
    }
}
