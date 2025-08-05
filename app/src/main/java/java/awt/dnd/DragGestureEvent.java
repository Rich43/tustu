package java.awt.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:java/awt/dnd/DragGestureEvent.class */
public class DragGestureEvent extends EventObject {
    private static final long serialVersionUID = 9080172649166731306L;
    private transient List events;
    private DragSource dragSource;
    private Component component;
    private Point origin;
    private int action;

    public DragGestureEvent(DragGestureRecognizer dragGestureRecognizer, int i2, Point point, List<? extends InputEvent> list) {
        super(dragGestureRecognizer);
        Component component = dragGestureRecognizer.getComponent();
        this.component = component;
        if (component == null) {
            throw new IllegalArgumentException("null component");
        }
        DragSource dragSource = dragGestureRecognizer.getDragSource();
        this.dragSource = dragSource;
        if (dragSource == null) {
            throw new IllegalArgumentException("null DragSource");
        }
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("null or empty list of events");
        }
        if (i2 != 1 && i2 != 2 && i2 != 1073741824) {
            throw new IllegalArgumentException("bad action");
        }
        if (point == null) {
            throw new IllegalArgumentException("null origin");
        }
        this.events = list;
        this.action = i2;
        this.origin = point;
    }

    public DragGestureRecognizer getSourceAsDragGestureRecognizer() {
        return (DragGestureRecognizer) getSource();
    }

    public Component getComponent() {
        return this.component;
    }

    public DragSource getDragSource() {
        return this.dragSource;
    }

    public Point getDragOrigin() {
        return this.origin;
    }

    public Iterator<InputEvent> iterator() {
        return this.events.iterator();
    }

    public Object[] toArray() {
        return this.events.toArray();
    }

    public Object[] toArray(Object[] objArr) {
        return this.events.toArray(objArr);
    }

    public int getDragAction() {
        return this.action;
    }

    public InputEvent getTriggerEvent() {
        return getSourceAsDragGestureRecognizer().getTriggerEvent();
    }

    public void startDrag(Cursor cursor, Transferable transferable) throws InvalidDnDOperationException {
        this.dragSource.startDrag(this, cursor, transferable, null);
    }

    public void startDrag(Cursor cursor, Transferable transferable, DragSourceListener dragSourceListener) throws InvalidDnDOperationException {
        this.dragSource.startDrag(this, cursor, transferable, dragSourceListener);
    }

    public void startDrag(Cursor cursor, Image image, Point point, Transferable transferable, DragSourceListener dragSourceListener) throws InvalidDnDOperationException {
        this.dragSource.startDrag(this, cursor, image, point, transferable, dragSourceListener);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationTester.test(this.events) ? this.events : null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        List listEmptyList;
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        DragSource dragSource = (DragSource) fields.get("dragSource", (Object) null);
        if (dragSource == null) {
            throw new InvalidObjectException("null DragSource");
        }
        this.dragSource = dragSource;
        Component component = (Component) fields.get("component", (Object) null);
        if (component == null) {
            throw new InvalidObjectException("null component");
        }
        this.component = component;
        Point point = (Point) fields.get("origin", (Object) null);
        if (point == null) {
            throw new InvalidObjectException("null origin");
        }
        this.origin = point;
        int i2 = fields.get("action", 0);
        if (i2 != 1 && i2 != 2 && i2 != 1073741824) {
            throw new InvalidObjectException("bad action");
        }
        this.action = i2;
        try {
            listEmptyList = (List) fields.get("events", (Object) null);
        } catch (IllegalArgumentException e2) {
            listEmptyList = (List) objectInputStream.readObject();
        }
        if (listEmptyList != null && listEmptyList.isEmpty()) {
            throw new InvalidObjectException("empty list of events");
        }
        if (listEmptyList == null) {
            listEmptyList = Collections.emptyList();
        }
        this.events = listEmptyList;
    }
}
