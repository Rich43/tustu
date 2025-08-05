package java.awt.dnd;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.dnd.peer.DropTargetPeer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TooManyListenersException;
import javax.swing.Timer;

/* loaded from: rt.jar:java/awt/dnd/DropTarget.class */
public class DropTarget implements DropTargetListener, Serializable {
    private static final long serialVersionUID = -6283860791671019047L;
    private DropTargetContext dropTargetContext;
    private Component component;
    private transient ComponentPeer componentPeer;
    private transient ComponentPeer nativePeer;
    int actions;
    boolean active;
    private transient DropTargetAutoScroller autoScroller;
    private transient DropTargetListener dtListener;
    private transient FlavorMap flavorMap;
    private transient boolean isDraggingInside;

    public DropTarget(Component component, int i2, DropTargetListener dropTargetListener, boolean z2, FlavorMap flavorMap) throws HeadlessException {
        this.dropTargetContext = createDropTargetContext();
        this.actions = 3;
        this.active = true;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.component = component;
        setDefaultActions(i2);
        if (dropTargetListener != null) {
            try {
                addDropTargetListener(dropTargetListener);
            } catch (TooManyListenersException e2) {
            }
        }
        if (component != null) {
            component.setDropTarget(this);
            setActive(z2);
        }
        if (flavorMap != null) {
            this.flavorMap = flavorMap;
        } else {
            this.flavorMap = SystemFlavorMap.getDefaultFlavorMap();
        }
    }

    public DropTarget(Component component, int i2, DropTargetListener dropTargetListener, boolean z2) throws HeadlessException {
        this(component, i2, dropTargetListener, z2, null);
    }

    public DropTarget() throws HeadlessException {
        this(null, 3, null, true, null);
    }

    public DropTarget(Component component, DropTargetListener dropTargetListener) throws HeadlessException {
        this(component, 3, dropTargetListener, true, null);
    }

    public DropTarget(Component component, int i2, DropTargetListener dropTargetListener) throws HeadlessException {
        this(component, i2, dropTargetListener, true);
    }

    public synchronized void setComponent(Component component) {
        if (this.component != component) {
            if (this.component != null && this.component.equals(component)) {
                return;
            }
            ComponentPeer componentPeer = null;
            Component component2 = this.component;
            if (component2 != null) {
                clearAutoscroll();
                this.component = null;
                if (this.componentPeer != null) {
                    componentPeer = this.componentPeer;
                    removeNotify(this.componentPeer);
                }
                component2.setDropTarget(null);
            }
            this.component = component;
            if (component != null) {
                try {
                    component.setDropTarget(this);
                } catch (Exception e2) {
                    if (component2 != null) {
                        component2.setDropTarget(this);
                        addNotify(componentPeer);
                    }
                }
            }
        }
    }

    public synchronized Component getComponent() {
        return this.component;
    }

    public void setDefaultActions(int i2) {
        getDropTargetContext().setTargetActions(i2 & 1073741827);
    }

    void doSetDefaultActions(int i2) {
        this.actions = i2;
    }

    public int getDefaultActions() {
        return this.actions;
    }

    public synchronized void setActive(boolean z2) {
        if (z2 != this.active) {
            this.active = z2;
        }
        if (!this.active) {
            clearAutoscroll();
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public synchronized void addDropTargetListener(DropTargetListener dropTargetListener) throws TooManyListenersException {
        if (dropTargetListener == null) {
            return;
        }
        if (equals(dropTargetListener)) {
            throw new IllegalArgumentException("DropTarget may not be its own Listener");
        }
        if (this.dtListener == null) {
            this.dtListener = dropTargetListener;
            return;
        }
        throw new TooManyListenersException();
    }

    public synchronized void removeDropTargetListener(DropTargetListener dropTargetListener) {
        if (dropTargetListener != null && this.dtListener != null) {
            if (this.dtListener.equals(dropTargetListener)) {
                this.dtListener = null;
                return;
            }
            throw new IllegalArgumentException("listener mismatch");
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public synchronized void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
        this.isDraggingInside = true;
        if (this.active) {
            if (this.dtListener != null) {
                this.dtListener.dragEnter(dropTargetDragEvent);
            } else {
                dropTargetDragEvent.getDropTargetContext().setTargetActions(0);
            }
            initializeAutoscrolling(dropTargetDragEvent.getLocation());
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public synchronized void dragOver(DropTargetDragEvent dropTargetDragEvent) {
        if (this.active) {
            if (this.dtListener != null && this.active) {
                this.dtListener.dragOver(dropTargetDragEvent);
            }
            updateAutoscroll(dropTargetDragEvent.getLocation());
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public synchronized void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
        if (this.active) {
            if (this.dtListener != null) {
                this.dtListener.dropActionChanged(dropTargetDragEvent);
            }
            updateAutoscroll(dropTargetDragEvent.getLocation());
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public synchronized void dragExit(DropTargetEvent dropTargetEvent) {
        this.isDraggingInside = false;
        if (this.active) {
            if (this.dtListener != null && this.active) {
                this.dtListener.dragExit(dropTargetEvent);
            }
            clearAutoscroll();
        }
    }

    @Override // java.awt.dnd.DropTargetListener
    public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
        this.isDraggingInside = false;
        clearAutoscroll();
        if (this.dtListener != null && this.active) {
            this.dtListener.drop(dropTargetDropEvent);
        } else {
            dropTargetDropEvent.rejectDrop();
        }
    }

    public FlavorMap getFlavorMap() {
        return this.flavorMap;
    }

    public void setFlavorMap(FlavorMap flavorMap) {
        this.flavorMap = flavorMap == null ? SystemFlavorMap.getDefaultFlavorMap() : flavorMap;
    }

    public void addNotify(ComponentPeer componentPeer) {
        if (componentPeer == this.componentPeer) {
            return;
        }
        this.componentPeer = componentPeer;
        Component parent = this.component;
        while (true) {
            Component component = parent;
            if (component == null || !(componentPeer instanceof LightweightPeer)) {
                break;
            }
            componentPeer = component.getPeer();
            parent = component.getParent();
        }
        if (componentPeer instanceof DropTargetPeer) {
            this.nativePeer = componentPeer;
            ((DropTargetPeer) componentPeer).addDropTarget(this);
        } else {
            this.nativePeer = null;
        }
    }

    public void removeNotify(ComponentPeer componentPeer) {
        if (this.nativePeer != null) {
            ((DropTargetPeer) this.nativePeer).removeDropTarget(this);
        }
        this.nativePeer = null;
        this.componentPeer = null;
        synchronized (this) {
            if (this.isDraggingInside) {
                dragExit(new DropTargetEvent(getDropTargetContext()));
            }
        }
    }

    public DropTargetContext getDropTargetContext() {
        return this.dropTargetContext;
    }

    protected DropTargetContext createDropTargetContext() {
        return new DropTargetContext(this);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(SerializationTester.test(this.dtListener) ? this.dtListener : null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        try {
            this.dropTargetContext = (DropTargetContext) fields.get("dropTargetContext", (Object) null);
        } catch (IllegalArgumentException e2) {
        }
        if (this.dropTargetContext == null) {
            this.dropTargetContext = createDropTargetContext();
        }
        this.component = (Component) fields.get("component", (Object) null);
        this.actions = fields.get("actions", 3);
        this.active = fields.get("active", true);
        try {
            this.dtListener = (DropTargetListener) fields.get("dtListener", (Object) null);
        } catch (IllegalArgumentException e3) {
            this.dtListener = (DropTargetListener) objectInputStream.readObject();
        }
    }

    /* loaded from: rt.jar:java/awt/dnd/DropTarget$DropTargetAutoScroller.class */
    protected static class DropTargetAutoScroller implements ActionListener {
        private Component component;
        private Autoscroll autoScroll;
        private Timer timer;
        private Point locn;
        private Point prev;
        private Rectangle outer = new Rectangle();
        private Rectangle inner = new Rectangle();
        private int hysteresis;

        protected DropTargetAutoScroller(Component component, Point point) {
            this.hysteresis = 10;
            this.component = component;
            this.autoScroll = (Autoscroll) this.component;
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            Integer num = 100;
            Integer num2 = 100;
            try {
                num = (Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.initialDelay");
            } catch (Exception e2) {
            }
            try {
                num2 = (Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.interval");
            } catch (Exception e3) {
            }
            this.timer = new Timer(num2.intValue(), this);
            this.timer.setCoalesce(true);
            this.timer.setInitialDelay(num.intValue());
            this.locn = point;
            this.prev = point;
            try {
                this.hysteresis = ((Integer) defaultToolkit.getDesktopProperty("DnD.Autoscroll.cursorHysteresis")).intValue();
            } catch (Exception e4) {
            }
            this.timer.start();
        }

        private void updateRegion() {
            Insets autoscrollInsets = this.autoScroll.getAutoscrollInsets();
            Dimension size = this.component.getSize();
            if (size.width != this.outer.width || size.height != this.outer.height) {
                this.outer.reshape(0, 0, size.width, size.height);
            }
            if (this.inner.f12372x != autoscrollInsets.left || this.inner.f12373y != autoscrollInsets.top) {
                this.inner.setLocation(autoscrollInsets.left, autoscrollInsets.top);
            }
            int i2 = size.width - (autoscrollInsets.left + autoscrollInsets.right);
            int i3 = size.height - (autoscrollInsets.top + autoscrollInsets.bottom);
            if (i2 != this.inner.width || i3 != this.inner.height) {
                this.inner.setSize(i2, i3);
            }
        }

        protected synchronized void updateLocation(Point point) {
            this.prev = this.locn;
            this.locn = point;
            if (Math.abs(this.locn.f12370x - this.prev.f12370x) > this.hysteresis || Math.abs(this.locn.f12371y - this.prev.f12371y) > this.hysteresis) {
                if (this.timer.isRunning()) {
                    this.timer.stop();
                }
            } else if (!this.timer.isRunning()) {
                this.timer.start();
            }
        }

        protected void stop() {
            this.timer.stop();
        }

        @Override // java.awt.event.ActionListener
        public synchronized void actionPerformed(ActionEvent actionEvent) {
            updateRegion();
            if (this.outer.contains(this.locn) && !this.inner.contains(this.locn)) {
                this.autoScroll.autoscroll(this.locn);
            }
        }
    }

    protected DropTargetAutoScroller createDropTargetAutoScroller(Component component, Point point) {
        return new DropTargetAutoScroller(component, point);
    }

    protected void initializeAutoscrolling(Point point) {
        if (this.component == null || !(this.component instanceof Autoscroll)) {
            return;
        }
        this.autoScroller = createDropTargetAutoScroller(this.component, point);
    }

    protected void updateAutoscroll(Point point) {
        if (this.autoScroller != null) {
            this.autoScroller.updateLocation(point);
        }
    }

    protected void clearAutoscroll() {
        if (this.autoScroller != null) {
            this.autoScroller.stop();
            this.autoScroller = null;
        }
    }
}
