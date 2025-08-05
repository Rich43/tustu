package java.awt.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TooManyListenersException;

/* loaded from: rt.jar:java/awt/dnd/DragSourceContext.class */
public class DragSourceContext implements DragSourceListener, DragSourceMotionListener, Serializable {
    private static final long serialVersionUID = -115407898692194719L;
    protected static final int DEFAULT = 0;
    protected static final int ENTER = 1;
    protected static final int OVER = 2;
    protected static final int CHANGED = 3;
    private static Transferable emptyTransferable;
    private transient DragSourceContextPeer peer;
    private DragGestureEvent trigger;
    private Cursor cursor;
    private transient Transferable transferable;
    private transient DragSourceListener listener;
    private boolean useCustomCursor;
    private int sourceActions;

    public DragSourceContext(DragSourceContextPeer dragSourceContextPeer, DragGestureEvent dragGestureEvent, Cursor cursor, Image image, Point point, Transferable transferable, DragSourceListener dragSourceListener) throws InvalidDnDOperationException {
        if (dragSourceContextPeer == null) {
            throw new NullPointerException("DragSourceContextPeer");
        }
        if (dragGestureEvent == null) {
            throw new NullPointerException("Trigger");
        }
        if (dragGestureEvent.getDragSource() == null) {
            throw new IllegalArgumentException("DragSource");
        }
        if (dragGestureEvent.getComponent() == null) {
            throw new IllegalArgumentException("Component");
        }
        if (dragGestureEvent.getSourceAsDragGestureRecognizer().getSourceActions() == 0) {
            throw new IllegalArgumentException("source actions");
        }
        if (dragGestureEvent.getDragAction() == 0) {
            throw new IllegalArgumentException("no drag action");
        }
        if (transferable == null) {
            throw new NullPointerException("Transferable");
        }
        if (image != null && point == null) {
            throw new NullPointerException("offset");
        }
        this.peer = dragSourceContextPeer;
        this.trigger = dragGestureEvent;
        this.cursor = cursor;
        this.transferable = transferable;
        this.listener = dragSourceListener;
        this.sourceActions = dragGestureEvent.getSourceAsDragGestureRecognizer().getSourceActions();
        this.useCustomCursor = cursor != null;
        updateCurrentCursor(dragGestureEvent.getDragAction(), getSourceActions(), 0);
    }

    public DragSource getDragSource() {
        return this.trigger.getDragSource();
    }

    public Component getComponent() {
        return this.trigger.getComponent();
    }

    public DragGestureEvent getTrigger() {
        return this.trigger;
    }

    public int getSourceActions() {
        return this.sourceActions;
    }

    public synchronized void setCursor(Cursor cursor) throws InvalidDnDOperationException {
        this.useCustomCursor = cursor != null;
        setCursorImpl(cursor);
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public synchronized void addDragSourceListener(DragSourceListener dragSourceListener) throws TooManyListenersException {
        if (dragSourceListener == null) {
            return;
        }
        if (equals(dragSourceListener)) {
            throw new IllegalArgumentException("DragSourceContext may not be its own listener");
        }
        if (this.listener != null) {
            throw new TooManyListenersException();
        }
        this.listener = dragSourceListener;
    }

    public synchronized void removeDragSourceListener(DragSourceListener dragSourceListener) {
        if (this.listener != null && this.listener.equals(dragSourceListener)) {
            this.listener = null;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void transferablesFlavorsChanged() {
        if (this.peer != null) {
            this.peer.transferablesFlavorsChanged();
        }
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) throws InvalidDnDOperationException {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragEnter(dragSourceDragEvent);
        }
        getDragSource().processDragEnter(dragSourceDragEvent);
        updateCurrentCursor(getSourceActions(), dragSourceDragEvent.getTargetActions(), 1);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) throws InvalidDnDOperationException {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragOver(dragSourceDragEvent);
        }
        getDragSource().processDragOver(dragSourceDragEvent);
        updateCurrentCursor(getSourceActions(), dragSourceDragEvent.getTargetActions(), 2);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragExit(DragSourceEvent dragSourceEvent) throws InvalidDnDOperationException {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragExit(dragSourceEvent);
        }
        getDragSource().processDragExit(dragSourceEvent);
        updateCurrentCursor(0, 0, 0);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) throws InvalidDnDOperationException {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dropActionChanged(dragSourceDragEvent);
        }
        getDragSource().processDropActionChanged(dragSourceDragEvent);
        updateCurrentCursor(getSourceActions(), dragSourceDragEvent.getTargetActions(), 3);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragDropEnd(dragSourceDropEvent);
        }
        getDragSource().processDragDropEnd(dragSourceDropEvent);
    }

    @Override // java.awt.dnd.DragSourceMotionListener
    public void dragMouseMoved(DragSourceDragEvent dragSourceDragEvent) {
        getDragSource().processDragMouseMoved(dragSourceDragEvent);
    }

    public Transferable getTransferable() {
        return this.transferable;
    }

    protected synchronized void updateCurrentCursor(int i2, int i3, int i4) throws InvalidDnDOperationException {
        Cursor cursor;
        if (this.useCustomCursor) {
            return;
        }
        switch (i4) {
            case 1:
            case 2:
            case 3:
                break;
            default:
                i3 = 0;
                break;
        }
        int i5 = i2 & i3;
        if (i5 == 0) {
            if ((i2 & 1073741824) == 1073741824) {
                cursor = DragSource.DefaultLinkNoDrop;
            } else if ((i2 & 2) == 2) {
                cursor = DragSource.DefaultMoveNoDrop;
            } else {
                cursor = DragSource.DefaultCopyNoDrop;
            }
        } else if ((i5 & 1073741824) == 1073741824) {
            cursor = DragSource.DefaultLinkDrop;
        } else if ((i5 & 2) == 2) {
            cursor = DragSource.DefaultMoveDrop;
        } else {
            cursor = DragSource.DefaultCopyDrop;
        }
        setCursorImpl(cursor);
    }

    private void setCursorImpl(Cursor cursor) throws InvalidDnDOperationException {
        if (this.cursor == null || !this.cursor.equals(cursor)) {
            this.cursor = cursor;
            if (this.peer != null) {
                this.peer.setCursor(this.cursor);
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationTester.test(this.transferable) ? this.transferable : null);
        objectOutputStream.writeObject(SerializationTester.test(this.listener) ? this.listener : null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        DragGestureEvent dragGestureEvent = (DragGestureEvent) fields.get("trigger", (Object) null);
        if (dragGestureEvent == null) {
            throw new InvalidObjectException("Null trigger");
        }
        if (dragGestureEvent.getDragSource() == null) {
            throw new InvalidObjectException("Null DragSource");
        }
        if (dragGestureEvent.getComponent() == null) {
            throw new InvalidObjectException("Null trigger component");
        }
        int i2 = fields.get("sourceActions", 0) & 1073741827;
        if (i2 == 0) {
            throw new InvalidObjectException("Invalid source actions");
        }
        int dragAction = dragGestureEvent.getDragAction();
        if (dragAction != 1 && dragAction != 2 && dragAction != 1073741824) {
            throw new InvalidObjectException("No drag action");
        }
        this.trigger = dragGestureEvent;
        this.cursor = (Cursor) fields.get("cursor", (Object) null);
        this.useCustomCursor = fields.get("useCustomCursor", false);
        this.sourceActions = i2;
        this.transferable = (Transferable) objectInputStream.readObject();
        this.listener = (DragSourceListener) objectInputStream.readObject();
        if (this.transferable == null) {
            if (emptyTransferable == null) {
                emptyTransferable = new Transferable() { // from class: java.awt.dnd.DragSourceContext.1
                    @Override // java.awt.datatransfer.Transferable
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[0];
                    }

                    @Override // java.awt.datatransfer.Transferable
                    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
                        return false;
                    }

                    @Override // java.awt.datatransfer.Transferable
                    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
                        throw new UnsupportedFlavorException(dataFlavor);
                    }
                };
            }
            this.transferable = emptyTransferable;
        }
    }
}
