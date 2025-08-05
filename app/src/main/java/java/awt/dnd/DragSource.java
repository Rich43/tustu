package java.awt.dnd;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.EventListener;
import sun.awt.dnd.SunDragSourceContextPeer;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:java/awt/dnd/DragSource.class */
public class DragSource implements Serializable {
    private static final long serialVersionUID = 6236096958971414066L;
    public static final Cursor DefaultCopyDrop = load("DnD.Cursor.CopyDrop");
    public static final Cursor DefaultMoveDrop = load("DnD.Cursor.MoveDrop");
    public static final Cursor DefaultLinkDrop = load("DnD.Cursor.LinkDrop");
    public static final Cursor DefaultCopyNoDrop = load("DnD.Cursor.CopyNoDrop");
    public static final Cursor DefaultMoveNoDrop = load("DnD.Cursor.MoveNoDrop");
    public static final Cursor DefaultLinkNoDrop = load("DnD.Cursor.LinkNoDrop");
    private static final DragSource dflt;
    static final String dragSourceListenerK = "dragSourceL";
    static final String dragSourceMotionListenerK = "dragSourceMotionL";
    private transient FlavorMap flavorMap = SystemFlavorMap.getDefaultFlavorMap();
    private transient DragSourceListener listener;
    private transient DragSourceMotionListener motionListener;

    private static Cursor load(String str) {
        if (GraphicsEnvironment.isHeadless()) {
            return null;
        }
        try {
            return (Cursor) Toolkit.getDefaultToolkit().getDesktopProperty(str);
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException("failed to load system cursor: " + str + " : " + e2.getMessage());
        }
    }

    static {
        dflt = GraphicsEnvironment.isHeadless() ? null : new DragSource();
    }

    public static DragSource getDefaultDragSource() {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        return dflt;
    }

    public static boolean isDragImageSupported() {
        Toolkit.getDefaultToolkit();
        try {
            return ((Boolean) Toolkit.getDefaultToolkit().getDesktopProperty("DnD.isDragImageSupported")).booleanValue();
        } catch (Exception e2) {
            return false;
        }
    }

    public DragSource() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
    }

    public void startDrag(DragGestureEvent dragGestureEvent, Cursor cursor, Image image, Point point, Transferable transferable, DragSourceListener dragSourceListener, FlavorMap flavorMap) throws InvalidDnDOperationException {
        SunDragSourceContextPeer.setDragDropInProgress(true);
        if (flavorMap != null) {
            try {
                this.flavorMap = flavorMap;
            } catch (RuntimeException e2) {
                SunDragSourceContextPeer.setDragDropInProgress(false);
                throw e2;
            }
        }
        DragSourceContextPeer dragSourceContextPeerCreateDragSourceContextPeer = Toolkit.getDefaultToolkit().createDragSourceContextPeer(dragGestureEvent);
        DragSourceContext dragSourceContextCreateDragSourceContext = createDragSourceContext(dragSourceContextPeerCreateDragSourceContextPeer, dragGestureEvent, cursor, image, point, transferable, dragSourceListener);
        if (dragSourceContextCreateDragSourceContext == null) {
            throw new InvalidDnDOperationException();
        }
        dragSourceContextPeerCreateDragSourceContextPeer.startDrag(dragSourceContextCreateDragSourceContext, dragSourceContextCreateDragSourceContext.getCursor(), image, point);
    }

    public void startDrag(DragGestureEvent dragGestureEvent, Cursor cursor, Transferable transferable, DragSourceListener dragSourceListener, FlavorMap flavorMap) throws InvalidDnDOperationException {
        startDrag(dragGestureEvent, cursor, null, null, transferable, dragSourceListener, flavorMap);
    }

    public void startDrag(DragGestureEvent dragGestureEvent, Cursor cursor, Image image, Point point, Transferable transferable, DragSourceListener dragSourceListener) throws InvalidDnDOperationException {
        startDrag(dragGestureEvent, cursor, image, point, transferable, dragSourceListener, null);
    }

    public void startDrag(DragGestureEvent dragGestureEvent, Cursor cursor, Transferable transferable, DragSourceListener dragSourceListener) throws InvalidDnDOperationException {
        startDrag(dragGestureEvent, cursor, null, null, transferable, dragSourceListener, null);
    }

    protected DragSourceContext createDragSourceContext(DragSourceContextPeer dragSourceContextPeer, DragGestureEvent dragGestureEvent, Cursor cursor, Image image, Point point, Transferable transferable, DragSourceListener dragSourceListener) {
        return new DragSourceContext(dragSourceContextPeer, dragGestureEvent, cursor, image, point, transferable, dragSourceListener);
    }

    public FlavorMap getFlavorMap() {
        return this.flavorMap;
    }

    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, Component component, int i2, DragGestureListener dragGestureListener) {
        return (T) Toolkit.getDefaultToolkit().createDragGestureRecognizer(cls, this, component, i2, dragGestureListener);
    }

    public DragGestureRecognizer createDefaultDragGestureRecognizer(Component component, int i2, DragGestureListener dragGestureListener) {
        return Toolkit.getDefaultToolkit().createDragGestureRecognizer(MouseDragGestureRecognizer.class, this, component, i2, dragGestureListener);
    }

    public void addDragSourceListener(DragSourceListener dragSourceListener) {
        if (dragSourceListener != null) {
            synchronized (this) {
                this.listener = DnDEventMulticaster.add(this.listener, dragSourceListener);
            }
        }
    }

    public void removeDragSourceListener(DragSourceListener dragSourceListener) {
        if (dragSourceListener != null) {
            synchronized (this) {
                this.listener = DnDEventMulticaster.remove(this.listener, dragSourceListener);
            }
        }
    }

    public DragSourceListener[] getDragSourceListeners() {
        return (DragSourceListener[]) getListeners(DragSourceListener.class);
    }

    public void addDragSourceMotionListener(DragSourceMotionListener dragSourceMotionListener) {
        if (dragSourceMotionListener != null) {
            synchronized (this) {
                this.motionListener = DnDEventMulticaster.add(this.motionListener, dragSourceMotionListener);
            }
        }
    }

    public void removeDragSourceMotionListener(DragSourceMotionListener dragSourceMotionListener) {
        if (dragSourceMotionListener != null) {
            synchronized (this) {
                this.motionListener = DnDEventMulticaster.remove(this.motionListener, dragSourceMotionListener);
            }
        }
    }

    public DragSourceMotionListener[] getDragSourceMotionListeners() {
        return (DragSourceMotionListener[]) getListeners(DragSourceMotionListener.class);
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        EventListener eventListener = null;
        if (cls == DragSourceListener.class) {
            eventListener = this.listener;
        } else if (cls == DragSourceMotionListener.class) {
            eventListener = this.motionListener;
        }
        return (T[]) DnDEventMulticaster.getListeners(eventListener, cls);
    }

    void processDragEnter(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragEnter(dragSourceDragEvent);
        }
    }

    void processDragOver(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragOver(dragSourceDragEvent);
        }
    }

    void processDropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dropActionChanged(dragSourceDragEvent);
        }
    }

    void processDragExit(DragSourceEvent dragSourceEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragExit(dragSourceEvent);
        }
    }

    void processDragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        DragSourceListener dragSourceListener = this.listener;
        if (dragSourceListener != null) {
            dragSourceListener.dragDropEnd(dragSourceDropEvent);
        }
    }

    void processDragMouseMoved(DragSourceDragEvent dragSourceDragEvent) {
        DragSourceMotionListener dragSourceMotionListener = this.motionListener;
        if (dragSourceMotionListener != null) {
            dragSourceMotionListener.dragMouseMoved(dragSourceDragEvent);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationTester.test(this.flavorMap) ? this.flavorMap : null);
        DnDEventMulticaster.save(objectOutputStream, dragSourceListenerK, this.listener);
        DnDEventMulticaster.save(objectOutputStream, dragSourceMotionListenerK, this.motionListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.flavorMap = (FlavorMap) objectInputStream.readObject();
        if (this.flavorMap == null) {
            this.flavorMap = SystemFlavorMap.getDefaultFlavorMap();
        }
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                String strIntern = ((String) object).intern();
                if (dragSourceListenerK == strIntern) {
                    addDragSourceListener((DragSourceListener) objectInputStream.readObject());
                } else if (dragSourceMotionListenerK == strIntern) {
                    addDragSourceMotionListener((DragSourceMotionListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                return;
            }
        }
    }

    public static int getDragThreshold() {
        int iIntValue = ((Integer) AccessController.doPrivileged(new GetIntegerAction("awt.dnd.drag.threshold", 0))).intValue();
        if (iIntValue > 0) {
            return iIntValue;
        }
        Integer num = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("DnD.gestureMotionThreshold");
        if (num != null) {
            return num.intValue();
        }
        return 5;
    }
}
