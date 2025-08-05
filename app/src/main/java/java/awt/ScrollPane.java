package java.awt;

import java.awt.Container;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.peer.ScrollPanePeer;
import java.beans.ConstructorProperties;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import sun.awt.ScrollPaneWheelScroller;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/ScrollPane.class */
public class ScrollPane extends Container implements Accessible {
    public static final int SCROLLBARS_AS_NEEDED = 0;
    public static final int SCROLLBARS_ALWAYS = 1;
    public static final int SCROLLBARS_NEVER = 2;
    private int scrollbarDisplayPolicy;
    private ScrollPaneAdjustable vAdjustable;
    private ScrollPaneAdjustable hAdjustable;
    private static final String base = "scrollpane";
    private static int nameCounter;
    private static final boolean defaultWheelScroll = true;
    private boolean wheelScrollingEnabled;
    private static final long serialVersionUID = 7956609840827222915L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        nameCounter = 0;
    }

    public ScrollPane() throws HeadlessException {
        this(0);
    }

    @ConstructorProperties({"scrollbarDisplayPolicy"})
    public ScrollPane(int i2) throws HeadlessException {
        this.wheelScrollingEnabled = true;
        GraphicsEnvironment.checkHeadless();
        this.layoutMgr = null;
        this.width = 100;
        this.height = 100;
        switch (i2) {
            case 0:
            case 1:
            case 2:
                this.scrollbarDisplayPolicy = i2;
                this.vAdjustable = new ScrollPaneAdjustable(this, new PeerFixer(this), 1);
                this.hAdjustable = new ScrollPaneAdjustable(this, new PeerFixer(this), 0);
                setWheelScrollingEnabled(true);
                return;
            default:
                throw new IllegalArgumentException("illegal scrollbar display policy");
        }
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (ScrollPane.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    private void addToPanel(Component component, Object obj, int i2) {
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add(component);
        super.addImpl(panel, obj, i2);
        validate();
    }

    @Override // java.awt.Container
    protected final void addImpl(Component component, Object obj, int i2) {
        synchronized (getTreeLock()) {
            if (getComponentCount() > 0) {
                remove(0);
            }
            if (i2 > 0) {
                throw new IllegalArgumentException("position greater than 0");
            }
            if (!SunToolkit.isLightweightOrUnknown(component)) {
                super.addImpl(component, obj, i2);
            } else {
                addToPanel(component, obj, i2);
            }
        }
    }

    public int getScrollbarDisplayPolicy() {
        return this.scrollbarDisplayPolicy;
    }

    public Dimension getViewportSize() {
        Insets insets = getInsets();
        return new Dimension((this.width - insets.right) - insets.left, (this.height - insets.top) - insets.bottom);
    }

    public int getHScrollbarHeight() {
        ScrollPanePeer scrollPanePeer;
        int hScrollbarHeight = 0;
        if (this.scrollbarDisplayPolicy != 2 && (scrollPanePeer = (ScrollPanePeer) this.peer) != null) {
            hScrollbarHeight = scrollPanePeer.getHScrollbarHeight();
        }
        return hScrollbarHeight;
    }

    public int getVScrollbarWidth() {
        ScrollPanePeer scrollPanePeer;
        int vScrollbarWidth = 0;
        if (this.scrollbarDisplayPolicy != 2 && (scrollPanePeer = (ScrollPanePeer) this.peer) != null) {
            vScrollbarWidth = scrollPanePeer.getVScrollbarWidth();
        }
        return vScrollbarWidth;
    }

    public Adjustable getVAdjustable() {
        return this.vAdjustable;
    }

    public Adjustable getHAdjustable() {
        return this.hAdjustable;
    }

    public void setScrollPosition(int i2, int i3) {
        synchronized (getTreeLock()) {
            if (getComponentCount() == 0) {
                throw new NullPointerException("child is null");
            }
            this.hAdjustable.setValue(i2);
            this.vAdjustable.setValue(i3);
        }
    }

    public void setScrollPosition(Point point) {
        setScrollPosition(point.f12370x, point.f12371y);
    }

    @Transient
    public Point getScrollPosition() {
        Point point;
        synchronized (getTreeLock()) {
            if (getComponentCount() == 0) {
                throw new NullPointerException("child is null");
            }
            point = new Point(this.hAdjustable.getValue(), this.vAdjustable.getValue());
        }
        return point;
    }

    @Override // java.awt.Container
    public final void setLayout(LayoutManager layoutManager) {
        throw new AWTError("ScrollPane controls layout");
    }

    @Override // java.awt.Container, java.awt.Component
    public void doLayout() {
        layout();
    }

    Dimension calculateChildSize() {
        boolean z2;
        boolean z3;
        Dimension size = getSize();
        Insets insets = getInsets();
        int i2 = size.width - (insets.left * 2);
        int i3 = size.height - (insets.top * 2);
        Dimension dimension = new Dimension(getComponent(0).getPreferredSize());
        if (this.scrollbarDisplayPolicy == 0) {
            z3 = dimension.height > i3;
            z2 = dimension.width > i2;
        } else if (this.scrollbarDisplayPolicy == 1) {
            z2 = true;
            z3 = true;
        } else {
            z2 = false;
            z3 = false;
        }
        int vScrollbarWidth = getVScrollbarWidth();
        int hScrollbarHeight = getHScrollbarHeight();
        if (z3) {
            i2 -= vScrollbarWidth;
        }
        if (z2) {
            i3 -= hScrollbarHeight;
        }
        if (dimension.width < i2) {
            dimension.width = i2;
        }
        if (dimension.height < i3) {
            dimension.height = i3;
        }
        return dimension;
    }

    @Override // java.awt.Container, java.awt.Component
    @Deprecated
    public void layout() {
        if (getComponentCount() == 0) {
            return;
        }
        Component component = getComponent(0);
        Point scrollPosition = getScrollPosition();
        Dimension dimensionCalculateChildSize = calculateChildSize();
        getViewportSize();
        component.reshape(-scrollPosition.f12370x, -scrollPosition.f12371y, dimensionCalculateChildSize.width, dimensionCalculateChildSize.height);
        ScrollPanePeer scrollPanePeer = (ScrollPanePeer) this.peer;
        if (scrollPanePeer != null) {
            scrollPanePeer.childResized(dimensionCalculateChildSize.width, dimensionCalculateChildSize.height);
        }
        Dimension viewportSize = getViewportSize();
        this.hAdjustable.setSpan(0, dimensionCalculateChildSize.width, viewportSize.width);
        this.vAdjustable.setSpan(0, dimensionCalculateChildSize.height, viewportSize.height);
    }

    @Override // java.awt.Container
    public void printComponents(Graphics graphics) {
        if (getComponentCount() == 0) {
            return;
        }
        Component component = getComponent(0);
        Point location = component.getLocation();
        Dimension viewportSize = getViewportSize();
        Insets insets = getInsets();
        Graphics graphicsCreate = graphics.create();
        try {
            graphicsCreate.clipRect(insets.left, insets.top, viewportSize.width, viewportSize.height);
            graphicsCreate.translate(location.f12370x, location.f12371y);
            component.printAll(graphicsCreate);
            graphicsCreate.dispose();
        } catch (Throwable th) {
            graphicsCreate.dispose();
            throw th;
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            int value = 0;
            int value2 = 0;
            if (getComponentCount() > 0) {
                value = this.vAdjustable.getValue();
                value2 = this.hAdjustable.getValue();
                this.vAdjustable.setValue(0);
                this.hAdjustable.setValue(0);
            }
            if (this.peer == null) {
                this.peer = getToolkit().createScrollPane(this);
            }
            super.addNotify();
            if (getComponentCount() > 0) {
                this.vAdjustable.setValue(value);
                this.hAdjustable.setValue(value2);
            }
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public String paramString() {
        String str;
        switch (this.scrollbarDisplayPolicy) {
            case 0:
                str = "as-needed";
                break;
            case 1:
                str = "always";
                break;
            case 2:
                str = "never";
                break;
            default:
                str = "invalid display policy";
                break;
        }
        Point scrollPosition = getComponentCount() > 0 ? getScrollPosition() : new Point(0, 0);
        Insets insets = getInsets();
        return super.paramString() + ",ScrollPosition=(" + scrollPosition.f12370x + "," + scrollPosition.f12371y + "),Insets=(" + insets.top + "," + insets.left + "," + insets.bottom + "," + insets.right + "),ScrollbarDisplayPolicy=" + str + ",wheelScrollingEnabled=" + isWheelScrollingEnabled();
    }

    @Override // java.awt.Component
    void autoProcessMouseWheel(MouseWheelEvent mouseWheelEvent) {
        processMouseWheelEvent(mouseWheelEvent);
    }

    @Override // java.awt.Component
    protected void processMouseWheelEvent(MouseWheelEvent mouseWheelEvent) {
        if (isWheelScrollingEnabled()) {
            ScrollPaneWheelScroller.handleWheelScrolling(this, mouseWheelEvent);
            mouseWheelEvent.consume();
        }
        super.processMouseWheelEvent(mouseWheelEvent);
    }

    @Override // java.awt.Component
    protected boolean eventTypeEnabled(int i2) {
        if (i2 == 507 && isWheelScrollingEnabled()) {
            return true;
        }
        return super.eventTypeEnabled(i2);
    }

    public void setWheelScrollingEnabled(boolean z2) {
        this.wheelScrollingEnabled = z2;
    }

    public boolean isWheelScrollingEnabled() {
        return this.wheelScrollingEnabled;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.scrollbarDisplayPolicy = fields.get("scrollbarDisplayPolicy", 0);
        this.hAdjustable = (ScrollPaneAdjustable) fields.get("hAdjustable", (Object) null);
        this.vAdjustable = (ScrollPaneAdjustable) fields.get("vAdjustable", (Object) null);
        this.wheelScrollingEnabled = fields.get("wheelScrollingEnabled", true);
    }

    /* loaded from: rt.jar:java/awt/ScrollPane$PeerFixer.class */
    class PeerFixer implements AdjustmentListener, Serializable {
        private static final long serialVersionUID = 1043664721353696630L;
        private ScrollPane scroller;

        PeerFixer(ScrollPane scrollPane) {
            this.scroller = scrollPane;
        }

        @Override // java.awt.event.AdjustmentListener
        public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            Adjustable adjustable = adjustmentEvent.getAdjustable();
            int value = adjustmentEvent.getValue();
            ScrollPanePeer scrollPanePeer = (ScrollPanePeer) this.scroller.peer;
            if (scrollPanePeer != null) {
                scrollPanePeer.setValue(adjustable, value);
            }
            Component component = this.scroller.getComponent(0);
            switch (adjustable.getOrientation()) {
                case 0:
                    component.move(-value, component.getLocation().f12371y);
                    return;
                case 1:
                    component.move(component.getLocation().f12370x, -value);
                    return;
                default:
                    throw new IllegalArgumentException("Illegal adjustable orientation");
            }
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTScrollPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/ScrollPane$AccessibleAWTScrollPane.class */
    protected class AccessibleAWTScrollPane extends Container.AccessibleAWTContainer {
        private static final long serialVersionUID = 6100703663886637L;

        protected AccessibleAWTScrollPane() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SCROLL_PANE;
        }
    }
}
