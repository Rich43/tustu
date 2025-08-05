package sun.awt.dnd;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.SortedMap;
import sun.awt.SunToolkit;
import sun.awt.datatransfer.DataTransferer;

/* loaded from: rt.jar:sun/awt/dnd/SunDragSourceContextPeer.class */
public abstract class SunDragSourceContextPeer implements DragSourceContextPeer {
    private DragGestureEvent trigger;
    private Component component;
    private Cursor cursor;
    private Image dragImage;
    private Point dragImageOffset;
    private long nativeCtxt;
    private DragSourceContext dragSourceContext;
    private int sourceActions;
    private static boolean dragDropInProgress = false;
    private static boolean discardingMouseEvents = false;
    protected static final int DISPATCH_ENTER = 1;
    protected static final int DISPATCH_MOTION = 2;
    protected static final int DISPATCH_CHANGED = 3;
    protected static final int DISPATCH_EXIT = 4;
    protected static final int DISPATCH_FINISH = 5;
    protected static final int DISPATCH_MOUSE_MOVED = 6;

    protected abstract void startDrag(Transferable transferable, long[] jArr, Map map);

    protected abstract void setNativeCursor(long j2, Cursor cursor, int i2);

    public SunDragSourceContextPeer(DragGestureEvent dragGestureEvent) {
        this.trigger = dragGestureEvent;
        if (this.trigger != null) {
            this.component = this.trigger.getComponent();
        } else {
            this.component = null;
        }
    }

    public void startSecondaryEventLoop() {
    }

    public void quitSecondaryEventLoop() {
    }

    @Override // java.awt.dnd.peer.DragSourceContextPeer
    public void startDrag(DragSourceContext dragSourceContext, Cursor cursor, Image image, Point point) throws InvalidDnDOperationException {
        if (getTrigger().getTriggerEvent() == null) {
            throw new InvalidDnDOperationException("DragGestureEvent has a null trigger");
        }
        this.dragSourceContext = dragSourceContext;
        this.cursor = cursor;
        this.sourceActions = getDragSourceContext().getSourceActions();
        this.dragImage = image;
        this.dragImageOffset = point;
        Transferable transferable = getDragSourceContext().getTransferable();
        SortedMap<Long, DataFlavor> formatsForTransferable = DataTransferer.getInstance().getFormatsForTransferable(transferable, DataTransferer.adaptFlavorMap(getTrigger().getDragSource().getFlavorMap()));
        DataTransferer.getInstance();
        startDrag(transferable, DataTransferer.keysToLongArray(formatsForTransferable), formatsForTransferable);
        discardingMouseEvents = true;
        EventQueue.invokeLater(new Runnable() { // from class: sun.awt.dnd.SunDragSourceContextPeer.1
            @Override // java.lang.Runnable
            public void run() {
                boolean unused = SunDragSourceContextPeer.discardingMouseEvents = false;
            }
        });
    }

    @Override // java.awt.dnd.peer.DragSourceContextPeer
    public void setCursor(Cursor cursor) throws InvalidDnDOperationException {
        synchronized (this) {
            if (this.cursor == null || !this.cursor.equals(cursor)) {
                this.cursor = cursor;
                setNativeCursor(getNativeContext(), cursor, cursor != null ? cursor.getType() : 0);
            }
        }
    }

    @Override // java.awt.dnd.peer.DragSourceContextPeer
    public Cursor getCursor() {
        return this.cursor;
    }

    public Image getDragImage() {
        return this.dragImage;
    }

    public Point getDragImageOffset() {
        if (this.dragImageOffset == null) {
            return new Point(0, 0);
        }
        return new Point(this.dragImageOffset);
    }

    protected synchronized void setTrigger(DragGestureEvent dragGestureEvent) {
        this.trigger = dragGestureEvent;
        if (this.trigger != null) {
            this.component = this.trigger.getComponent();
        } else {
            this.component = null;
        }
    }

    protected DragGestureEvent getTrigger() {
        return this.trigger;
    }

    protected Component getComponent() {
        return this.component;
    }

    protected synchronized void setNativeContext(long j2) {
        this.nativeCtxt = j2;
    }

    protected synchronized long getNativeContext() {
        return this.nativeCtxt;
    }

    protected DragSourceContext getDragSourceContext() {
        return this.dragSourceContext;
    }

    @Override // java.awt.dnd.peer.DragSourceContextPeer
    public void transferablesFlavorsChanged() {
    }

    protected final void postDragSourceDragEvent(int i2, int i3, int i4, int i5, int i6) {
        SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), new EventDispatcher(i6, new DragSourceDragEvent(getDragSourceContext(), convertModifiersToDropAction(i3, this.sourceActions), i2 & this.sourceActions, i3, i4, i5)));
        startSecondaryEventLoop();
    }

    protected void dragEnter(int i2, int i3, int i4, int i5) {
        postDragSourceDragEvent(i2, i3, i4, i5, 1);
    }

    private void dragMotion(int i2, int i3, int i4, int i5) {
        postDragSourceDragEvent(i2, i3, i4, i5, 2);
    }

    private void operationChanged(int i2, int i3, int i4, int i5) {
        postDragSourceDragEvent(i2, i3, i4, i5, 3);
    }

    protected final void dragExit(int i2, int i3) {
        SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), new EventDispatcher(4, new DragSourceEvent(getDragSourceContext(), i2, i3)));
        startSecondaryEventLoop();
    }

    private void dragMouseMoved(int i2, int i3, int i4, int i5) {
        postDragSourceDragEvent(i2, i3, i4, i5, 6);
    }

    protected final void dragDropFinished(boolean z2, int i2, int i3, int i4) {
        SunToolkit.invokeLaterOnAppContext(SunToolkit.targetToAppContext(getComponent()), new EventDispatcher(5, new DragSourceDropEvent(getDragSourceContext(), i2 & this.sourceActions, z2, i3, i4)));
        startSecondaryEventLoop();
        setNativeContext(0L);
        this.dragImage = null;
        this.dragImageOffset = null;
    }

    public static void setDragDropInProgress(boolean z2) throws InvalidDnDOperationException {
        synchronized (SunDragSourceContextPeer.class) {
            if (dragDropInProgress == z2) {
                throw new InvalidDnDOperationException(getExceptionMessage(z2));
            }
            dragDropInProgress = z2;
        }
    }

    public static boolean checkEvent(AWTEvent aWTEvent) {
        if (discardingMouseEvents && (aWTEvent instanceof MouseEvent) && !(((MouseEvent) aWTEvent) instanceof SunDropTargetEvent)) {
            return false;
        }
        return true;
    }

    public static void checkDragDropInProgress() throws InvalidDnDOperationException {
        if (dragDropInProgress) {
            throw new InvalidDnDOperationException(getExceptionMessage(true));
        }
    }

    private static String getExceptionMessage(boolean z2) {
        return z2 ? "Drag and drop in progress" : "No drag in progress";
    }

    public static int convertModifiersToDropAction(int i2, int i3) {
        int i4 = 0;
        switch (i2 & 192) {
            case 64:
                i4 = 2;
                break;
            case 128:
                i4 = 1;
                break;
            case 192:
                i4 = 1073741824;
                break;
            default:
                if ((i3 & 2) != 0) {
                    i4 = 2;
                    break;
                } else if ((i3 & 1) != 0) {
                    i4 = 1;
                    break;
                } else if ((i3 & 1073741824) != 0) {
                    i4 = 1073741824;
                    break;
                }
                break;
        }
        return i4 & i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanup() throws InvalidDnDOperationException {
        this.trigger = null;
        this.component = null;
        this.cursor = null;
        this.dragSourceContext = null;
        SunDropTargetContextPeer.setCurrentJVMLocalSourceTransferable(null);
        setDragDropInProgress(false);
    }

    /* loaded from: rt.jar:sun/awt/dnd/SunDragSourceContextPeer$EventDispatcher.class */
    private class EventDispatcher implements Runnable {
        private final int dispatchType;
        private final DragSourceEvent event;

        EventDispatcher(int i2, DragSourceEvent dragSourceEvent) {
            switch (i2) {
                case 1:
                case 2:
                case 3:
                case 6:
                    if (!(dragSourceEvent instanceof DragSourceDragEvent)) {
                        throw new IllegalArgumentException("Event: " + ((Object) dragSourceEvent));
                    }
                    break;
                case 4:
                    break;
                case 5:
                    if (!(dragSourceEvent instanceof DragSourceDropEvent)) {
                        throw new IllegalArgumentException("Event: " + ((Object) dragSourceEvent));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Dispatch type: " + i2);
            }
            this.dispatchType = i2;
            this.event = dragSourceEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            DragSourceContext dragSourceContext = SunDragSourceContextPeer.this.getDragSourceContext();
            try {
                switch (this.dispatchType) {
                    case 1:
                        dragSourceContext.dragEnter((DragSourceDragEvent) this.event);
                        return;
                    case 2:
                        dragSourceContext.dragOver((DragSourceDragEvent) this.event);
                        return;
                    case 3:
                        dragSourceContext.dropActionChanged((DragSourceDragEvent) this.event);
                        return;
                    case 4:
                        dragSourceContext.dragExit(this.event);
                        return;
                    case 5:
                        try {
                            dragSourceContext.dragDropEnd((DragSourceDropEvent) this.event);
                            SunDragSourceContextPeer.this.cleanup();
                            return;
                        } catch (Throwable th) {
                            SunDragSourceContextPeer.this.cleanup();
                            throw th;
                        }
                    case 6:
                        dragSourceContext.dragMouseMoved((DragSourceDragEvent) this.event);
                        return;
                    default:
                        throw new IllegalStateException("Dispatch type: " + this.dispatchType);
                }
            } finally {
                SunDragSourceContextPeer.this.quitSecondaryEventLoop();
            }
        }
    }
}
