package java.awt.dnd;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TooManyListenersException;

/* loaded from: rt.jar:java/awt/dnd/DragGestureRecognizer.class */
public abstract class DragGestureRecognizer implements Serializable {
    private static final long serialVersionUID = 8996673345831063337L;
    protected DragSource dragSource;
    protected Component component;
    protected transient DragGestureListener dragGestureListener;
    protected int sourceActions;
    protected ArrayList<InputEvent> events;

    protected abstract void registerListeners();

    protected abstract void unregisterListeners();

    protected DragGestureRecognizer(DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        this.events = new ArrayList<>(1);
        if (dragSource == null) {
            throw new IllegalArgumentException("null DragSource");
        }
        this.dragSource = dragSource;
        this.component = component;
        this.sourceActions = i2 & 1073741827;
        if (dragGestureListener != null) {
            try {
                addDragGestureListener(dragGestureListener);
            } catch (TooManyListenersException e2) {
            }
        }
    }

    protected DragGestureRecognizer(DragSource dragSource, Component component, int i2) {
        this(dragSource, component, i2, null);
    }

    protected DragGestureRecognizer(DragSource dragSource, Component component) {
        this(dragSource, component, 0);
    }

    protected DragGestureRecognizer(DragSource dragSource) {
        this(dragSource, null);
    }

    public DragSource getDragSource() {
        return this.dragSource;
    }

    public synchronized Component getComponent() {
        return this.component;
    }

    public synchronized void setComponent(Component component) {
        if (this.component != null && this.dragGestureListener != null) {
            unregisterListeners();
        }
        this.component = component;
        if (this.component != null && this.dragGestureListener != null) {
            registerListeners();
        }
    }

    public synchronized int getSourceActions() {
        return this.sourceActions;
    }

    public synchronized void setSourceActions(int i2) {
        this.sourceActions = i2 & 1073741827;
    }

    public InputEvent getTriggerEvent() {
        if (this.events.isEmpty()) {
            return null;
        }
        return this.events.get(0);
    }

    public void resetRecognizer() {
        this.events.clear();
    }

    public synchronized void addDragGestureListener(DragGestureListener dragGestureListener) throws TooManyListenersException {
        if (this.dragGestureListener != null) {
            throw new TooManyListenersException();
        }
        this.dragGestureListener = dragGestureListener;
        if (this.component != null) {
            registerListeners();
        }
    }

    public synchronized void removeDragGestureListener(DragGestureListener dragGestureListener) {
        if (this.dragGestureListener == null || !this.dragGestureListener.equals(dragGestureListener)) {
            throw new IllegalArgumentException();
        }
        this.dragGestureListener = null;
        if (this.component != null) {
            unregisterListeners();
        }
    }

    protected synchronized void fireDragGestureRecognized(int i2, Point point) {
        try {
            if (this.dragGestureListener != null) {
                this.dragGestureListener.dragGestureRecognized(new DragGestureEvent(this, i2, point, this.events));
            }
        } finally {
            this.events.clear();
        }
    }

    protected synchronized void appendEvent(InputEvent inputEvent) {
        this.events.add(inputEvent);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationTester.test(this.dragGestureListener) ? this.dragGestureListener : null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        DragSource dragSource = (DragSource) fields.get("dragSource", (Object) null);
        if (dragSource == null) {
            throw new InvalidObjectException("null DragSource");
        }
        this.dragSource = dragSource;
        this.component = (Component) fields.get("component", (Object) null);
        this.sourceActions = fields.get("sourceActions", 0) & 1073741827;
        this.events = (ArrayList) fields.get("events", new ArrayList(1));
        this.dragGestureListener = (DragGestureListener) objectInputStream.readObject();
    }
}
