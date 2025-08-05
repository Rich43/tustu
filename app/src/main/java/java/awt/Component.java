package java.awt;

import java.applet.Applet;
import java.awt.BufferCapabilities;
import java.awt.GraphicsCallback;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.PaintEvent;
import java.awt.event.WindowEvent;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferStrategy;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.print.ServiceUIFactory;
import javax.swing.JComponent;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.ConstrainableGraphics;
import sun.awt.EmbeddedFrame;
import sun.awt.EventQueueItem;
import sun.awt.RequestFocusController;
import sun.awt.SubRegionShowable;
import sun.awt.SunToolkit;
import sun.awt.WindowClosingListener;
import sun.awt.dnd.SunDropTargetEvent;
import sun.awt.im.CompositionArea;
import sun.awt.image.VSyncedBSManager;
import sun.font.FontDesignMetrics;
import sun.font.FontManager;
import sun.font.FontManagerFactory;
import sun.font.SunFontManager;
import sun.java2d.SunGraphics2D;
import sun.java2d.SunGraphicsEnvironment;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
import sun.security.action.GetPropertyAction;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/Component.class */
public abstract class Component implements ImageObserver, MenuContainer, Serializable {
    private static final PlatformLogger log;
    private static final PlatformLogger eventLog;
    private static final PlatformLogger focusLog;
    private static final PlatformLogger mixingLog;
    transient ComponentPeer peer;
    transient Container parent;

    /* renamed from: x, reason: collision with root package name */
    int f12361x;

    /* renamed from: y, reason: collision with root package name */
    int f12362y;
    int width;
    int height;
    Color foreground;
    Color background;
    volatile Font font;
    Font peerFont;
    Cursor cursor;
    Locale locale;
    private volatile transient GraphicsConfiguration graphicsConfig;
    DropTarget dropTarget;
    Vector<PopupMenu> popups;
    private String name;
    private static final int FOCUS_TRAVERSABLE_UNKNOWN = 0;
    private static final int FOCUS_TRAVERSABLE_DEFAULT = 1;
    private static final int FOCUS_TRAVERSABLE_SET = 2;
    Set<AWTKeyStroke>[] focusTraversalKeys;
    private static final String[] focusTraversalKeyPropertyNames;
    static final Object LOCK;
    Dimension minSize;
    boolean minSizeSet;
    Dimension prefSize;
    boolean prefSizeSet;
    Dimension maxSize;
    boolean maxSizeSet;
    transient ComponentListener componentListener;
    transient FocusListener focusListener;
    transient HierarchyListener hierarchyListener;
    transient HierarchyBoundsListener hierarchyBoundsListener;
    transient KeyListener keyListener;
    transient MouseListener mouseListener;
    transient MouseMotionListener mouseMotionListener;
    transient MouseWheelListener mouseWheelListener;
    transient InputMethodListener inputMethodListener;
    static final String actionListenerK = "actionL";
    static final String adjustmentListenerK = "adjustmentL";
    static final String componentListenerK = "componentL";
    static final String containerListenerK = "containerL";
    static final String focusListenerK = "focusL";
    static final String itemListenerK = "itemL";
    static final String keyListenerK = "keyL";
    static final String mouseListenerK = "mouseL";
    static final String mouseMotionListenerK = "mouseMotionL";
    static final String mouseWheelListenerK = "mouseWheelL";
    static final String textListenerK = "textL";
    static final String ownedWindowK = "ownedL";
    static final String windowListenerK = "windowL";
    static final String inputMethodListenerK = "inputMethodL";
    static final String hierarchyListenerK = "hierarchyL";
    static final String hierarchyBoundsListenerK = "hierarchyBoundsL";
    static final String windowStateListenerK = "windowStateL";
    static final String windowFocusListenerK = "windowFocusL";
    static boolean isInc;
    static int incRate;
    public static final float TOP_ALIGNMENT = 0.0f;
    public static final float CENTER_ALIGNMENT = 0.5f;
    public static final float BOTTOM_ALIGNMENT = 1.0f;
    public static final float LEFT_ALIGNMENT = 0.0f;
    public static final float RIGHT_ALIGNMENT = 1.0f;
    private static final long serialVersionUID = -7644114512714619750L;
    private PropertyChangeSupport changeSupport;
    transient boolean backgroundEraseDisabled;
    transient EventQueueItem[] eventCache;
    private static final Map<Class<?>, Boolean> coalesceMap;
    private static final Class[] coalesceEventsParams;
    private static RequestFocusController requestFocusController;
    static final /* synthetic */ boolean $assertionsDisabled;
    transient BufferStrategy bufferStrategy = null;
    boolean ignoreRepaint = false;
    boolean visible = true;
    boolean enabled = true;
    private volatile boolean valid = false;
    private boolean nameExplicitlySet = false;
    private boolean focusable = true;
    private int isFocusTraversableOverridden = 0;
    private boolean focusTraversalKeysEnabled = true;
    private volatile transient AccessControlContext acc = AccessController.getContext();
    transient ComponentOrientation componentOrientation = ComponentOrientation.UNKNOWN;
    boolean newEventsOnly = false;
    transient RuntimeException windowClosingException = null;
    long eventMask = 4096;
    private transient Object objectLock = new Object();
    boolean isPacked = false;
    private int boundsOp = 3;
    private transient Region compoundShape = null;
    private transient Region mixingCutoutRegion = null;
    private transient boolean isAddNotifyComplete = false;
    private transient boolean coalescingEnabled = checkCoalescing();
    private boolean autoFocusTransferOnDisposal = true;
    private int componentSerializedDataVersion = 4;
    protected AccessibleContext accessibleContext = null;
    transient AppContext appContext = AppContext.getAppContext();

    /* loaded from: rt.jar:java/awt/Component$BaselineResizeBehavior.class */
    public enum BaselineResizeBehavior {
        CONSTANT_ASCENT,
        CONSTANT_DESCENT,
        CENTER_OFFSET,
        OTHER
    }

    private static native void initIDs();

    static {
        $assertionsDisabled = !Component.class.desiredAssertionStatus();
        log = PlatformLogger.getLogger("java.awt.Component");
        eventLog = PlatformLogger.getLogger("java.awt.event.Component");
        focusLog = PlatformLogger.getLogger("java.awt.focus.Component");
        mixingLog = PlatformLogger.getLogger("java.awt.mixing.Component");
        focusTraversalKeyPropertyNames = new String[]{"forwardFocusTraversalKeys", "backwardFocusTraversalKeys", "upCycleFocusTraversalKeys", "downCycleFocusTraversalKeys"};
        LOCK = new AWTTreeLock();
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("awt.image.incrementaldraw"));
        isInc = str == null || str.equals("true");
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("awt.image.redrawrate"));
        incRate = str2 != null ? Integer.parseInt(str2) : 100;
        AWTAccessor.setComponentAccessor(new AWTAccessor.ComponentAccessor() { // from class: java.awt.Component.1
            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setBackgroundEraseDisabled(Component component, boolean z2) {
                component.backgroundEraseDisabled = z2;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean getBackgroundEraseDisabled(Component component) {
                return component.backgroundEraseDisabled;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Rectangle getBounds(Component component) {
                return new Rectangle(component.f12361x, component.f12362y, component.width, component.height);
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setMixingCutoutShape(Component component, Shape shape) {
                Region region = shape == null ? null : Region.getInstance(shape, null);
                synchronized (component.getTreeLock()) {
                    boolean z2 = false;
                    boolean z3 = false;
                    if (!component.isNonOpaqueForMixing()) {
                        z3 = true;
                    }
                    component.mixingCutoutRegion = region;
                    if (!component.isNonOpaqueForMixing()) {
                        z2 = true;
                    }
                    if (component.isMixingNeeded()) {
                        if (z3) {
                            component.mixOnHiding(component.isLightweight());
                        }
                        if (z2) {
                            component.mixOnShowing();
                        }
                    }
                }
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setGraphicsConfiguration(Component component, GraphicsConfiguration graphicsConfiguration) {
                component.setGraphicsConfiguration(graphicsConfiguration);
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean requestFocus(Component component, CausedFocusEvent.Cause cause) {
                return component.requestFocus(cause);
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean canBeFocusOwner(Component component) {
                return component.canBeFocusOwner();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean isVisible(Component component) {
                return component.isVisible_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setRequestFocusController(RequestFocusController requestFocusController2) {
                Component.setRequestFocusController(requestFocusController2);
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public AppContext getAppContext(Component component) {
                return component.appContext;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setAppContext(Component component, AppContext appContext) {
                component.appContext = appContext;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Container getParent(Component component) {
                return component.getParent_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setParent(Component component, Container container) {
                component.parent = container;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setSize(Component component, int i2, int i3) {
                component.width = i2;
                component.height = i3;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Point getLocation(Component component) {
                return component.location_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setLocation(Component component, int i2, int i3) {
                component.f12361x = i2;
                component.f12362y = i3;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean isEnabled(Component component) {
                return component.isEnabledImpl();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean isDisplayable(Component component) {
                return component.peer != null;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Cursor getCursor(Component component) {
                return component.getCursor_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public ComponentPeer getPeer(Component component) {
                return component.peer;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setPeer(Component component, ComponentPeer componentPeer) {
                component.peer = componentPeer;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean isLightweight(Component component) {
                return component.peer instanceof LightweightPeer;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public boolean getIgnoreRepaint(Component component) {
                return component.ignoreRepaint;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public int getWidth(Component component) {
                return component.width;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public int getHeight(Component component) {
                return component.height;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public int getX(Component component) {
                return component.f12361x;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public int getY(Component component) {
                return component.f12362y;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Color getForeground(Component component) {
                return component.foreground;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Color getBackground(Component component) {
                return component.background;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void setBackground(Component component, Color color) {
                component.background = color;
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public Font getFont(Component component) {
                return component.getFont_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void processEvent(Component component, AWTEvent aWTEvent) {
                component.processEvent(aWTEvent);
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public AccessControlContext getAccessControlContext(Component component) {
                return component.getAccessControlContext();
            }

            @Override // sun.awt.AWTAccessor.ComponentAccessor
            public void revalidateSynchronously(Component component) {
                component.revalidateSynchronously();
            }
        });
        coalesceMap = new WeakHashMap();
        coalesceEventsParams = new Class[]{AWTEvent.class, AWTEvent.class};
        requestFocusController = new DummyRequestFocusController();
    }

    /* loaded from: rt.jar:java/awt/Component$AWTTreeLock.class */
    static class AWTTreeLock {
        AWTTreeLock() {
        }
    }

    Object getObjectLock() {
        return this.objectLock;
    }

    final AccessControlContext getAccessControlContext() {
        if (this.acc == null) {
            throw new SecurityException("Component is missing AccessControlContext");
        }
        return this.acc;
    }

    int getBoundsOp() {
        if ($assertionsDisabled || Thread.holdsLock(getTreeLock())) {
            return this.boundsOp;
        }
        throw new AssertionError();
    }

    void setBoundsOp(int i2) {
        if (!$assertionsDisabled && !Thread.holdsLock(getTreeLock())) {
            throw new AssertionError();
        }
        if (i2 == 5) {
            this.boundsOp = 3;
        } else if (this.boundsOp == 3) {
            this.boundsOp = i2;
        }
    }

    protected Component() {
    }

    void initializeFocusTraversalKeys() {
        this.focusTraversalKeys = new Set[3];
    }

    String constructComponentName() {
        return null;
    }

    public String getName() {
        if (this.name == null && !this.nameExplicitlySet) {
            synchronized (getObjectLock()) {
                if (this.name == null && !this.nameExplicitlySet) {
                    this.name = constructComponentName();
                }
            }
        }
        return this.name;
    }

    public void setName(String str) {
        String str2;
        synchronized (getObjectLock()) {
            str2 = this.name;
            this.name = str;
            this.nameExplicitlySet = true;
        }
        firePropertyChange("name", str2, str);
    }

    public Container getParent() {
        return getParent_NoClientCode();
    }

    final Container getParent_NoClientCode() {
        return this.parent;
    }

    Container getContainer() {
        return getParent_NoClientCode();
    }

    @Deprecated
    public ComponentPeer getPeer() {
        return this.peer;
    }

    public synchronized void setDropTarget(DropTarget dropTarget) {
        if (dropTarget != this.dropTarget) {
            if (this.dropTarget != null && this.dropTarget.equals(dropTarget)) {
                return;
            }
            DropTarget dropTarget2 = this.dropTarget;
            if (dropTarget2 != null) {
                if (this.peer != null) {
                    this.dropTarget.removeNotify(this.peer);
                }
                DropTarget dropTarget3 = this.dropTarget;
                this.dropTarget = null;
                try {
                    dropTarget3.setComponent(null);
                } catch (IllegalArgumentException e2) {
                }
            }
            this.dropTarget = dropTarget;
            if (dropTarget != null) {
                try {
                    this.dropTarget.setComponent(this);
                    if (this.peer != null) {
                        this.dropTarget.addNotify(this.peer);
                    }
                } catch (IllegalArgumentException e3) {
                    if (dropTarget2 != null) {
                        try {
                            dropTarget2.setComponent(this);
                            if (this.peer != null) {
                                this.dropTarget.addNotify(this.peer);
                            }
                        } catch (IllegalArgumentException e4) {
                        }
                    }
                }
            }
        }
    }

    public synchronized DropTarget getDropTarget() {
        return this.dropTarget;
    }

    public GraphicsConfiguration getGraphicsConfiguration() {
        return getGraphicsConfiguration_NoClientCode();
    }

    final GraphicsConfiguration getGraphicsConfiguration_NoClientCode() {
        return this.graphicsConfig;
    }

    void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        synchronized (getTreeLock()) {
            if (updateGraphicsData(graphicsConfiguration)) {
                removeNotify();
                addNotify();
            }
        }
    }

    boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        checkTreeLock();
        if (this.graphicsConfig == graphicsConfiguration) {
            return false;
        }
        this.graphicsConfig = graphicsConfiguration;
        ComponentPeer peer = getPeer();
        if (peer != null) {
            return peer.updateGraphicsData(graphicsConfiguration);
        }
        return false;
    }

    void checkGD(String str) {
        if (this.graphicsConfig != null && !this.graphicsConfig.getDevice().getIDstring().equals(str)) {
            throw new IllegalArgumentException("adding a container to a container on a different GraphicsDevice");
        }
    }

    public final Object getTreeLock() {
        return LOCK;
    }

    final void checkTreeLock() {
        if (!Thread.holdsLock(getTreeLock())) {
            throw new IllegalStateException("This function should be called while holding treeLock");
        }
    }

    public Toolkit getToolkit() {
        return getToolkitImpl();
    }

    final Toolkit getToolkitImpl() {
        Container container = this.parent;
        if (container != null) {
            return container.getToolkitImpl();
        }
        return Toolkit.getDefaultToolkit();
    }

    public boolean isValid() {
        return this.peer != null && this.valid;
    }

    public boolean isDisplayable() {
        return getPeer() != null;
    }

    @Transient
    public boolean isVisible() {
        return isVisible_NoClientCode();
    }

    final boolean isVisible_NoClientCode() {
        return this.visible;
    }

    boolean isRecursivelyVisible() {
        return this.visible && (this.parent == null || this.parent.isRecursivelyVisible());
    }

    private Rectangle getRecursivelyVisibleBounds() {
        Container container = getContainer();
        Rectangle bounds = getBounds();
        if (container == null) {
            return bounds;
        }
        Rectangle recursivelyVisibleBounds = container.getRecursivelyVisibleBounds();
        recursivelyVisibleBounds.setLocation(0, 0);
        return recursivelyVisibleBounds.intersection(bounds);
    }

    Point pointRelativeToComponent(Point point) {
        Point locationOnScreen = getLocationOnScreen();
        return new Point(point.f12370x - locationOnScreen.f12370x, point.f12371y - locationOnScreen.f12371y);
    }

    Component findUnderMouseInWindow(PointerInfo pointerInfo) {
        if (!isShowing()) {
            return null;
        }
        Window containingWindow = getContainingWindow();
        if (!Toolkit.getDefaultToolkit().getMouseInfoPeer().isWindowUnderMouse(containingWindow)) {
            return null;
        }
        Point pointPointRelativeToComponent = containingWindow.pointRelativeToComponent(pointerInfo.getLocation());
        return containingWindow.findComponentAt(pointPointRelativeToComponent.f12370x, pointPointRelativeToComponent.f12371y, true);
    }

    public Point getMousePosition() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        PointerInfo pointerInfo = (PointerInfo) AccessController.doPrivileged(new PrivilegedAction<PointerInfo>() { // from class: java.awt.Component.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PointerInfo run2() {
                return MouseInfo.getPointerInfo();
            }
        });
        synchronized (getTreeLock()) {
            if (!isSameOrAncestorOf(findUnderMouseInWindow(pointerInfo), true)) {
                return null;
            }
            return pointRelativeToComponent(pointerInfo.getLocation());
        }
    }

    boolean isSameOrAncestorOf(Component component, boolean z2) {
        return component == this;
    }

    public boolean isShowing() {
        if (this.visible && this.peer != null) {
            Container container = this.parent;
            return container == null || container.isShowing();
        }
        return false;
    }

    public boolean isEnabled() {
        return isEnabledImpl();
    }

    final boolean isEnabledImpl() {
        return this.enabled;
    }

    public void setEnabled(boolean z2) {
        enable(z2);
    }

    @Deprecated
    public void enable() {
        if (!this.enabled) {
            synchronized (getTreeLock()) {
                this.enabled = true;
                ComponentPeer componentPeer = this.peer;
                if (componentPeer != null) {
                    componentPeer.setEnabled(true);
                    if (this.visible && !getRecursivelyVisibleBounds().isEmpty()) {
                        updateCursorImmediately();
                    }
                }
            }
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.ENABLED);
            }
        }
    }

    @Deprecated
    public void enable(boolean z2) {
        if (z2) {
            enable();
        } else {
            disable();
        }
    }

    @Deprecated
    public void disable() {
        if (this.enabled) {
            KeyboardFocusManager.clearMostRecentFocusOwner(this);
            synchronized (getTreeLock()) {
                this.enabled = false;
                if ((isFocusOwner() || (containsFocus() && !isLightweight())) && KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                    transferFocus(false);
                }
                ComponentPeer componentPeer = this.peer;
                if (componentPeer != null) {
                    componentPeer.setEnabled(false);
                    if (this.visible && !getRecursivelyVisibleBounds().isEmpty()) {
                        updateCursorImmediately();
                    }
                }
            }
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.ENABLED);
            }
        }
    }

    public boolean isDoubleBuffered() {
        return false;
    }

    public void enableInputMethods(boolean z2) {
        InputContext inputContext;
        InputContext inputContext2;
        if (!z2) {
            if ((this.eventMask & 4096) != 0 && (inputContext = getInputContext()) != null) {
                inputContext.endComposition();
                inputContext.removeNotify(this);
            }
            this.eventMask &= -4097;
            return;
        }
        if ((this.eventMask & 4096) != 0) {
            return;
        }
        if (isFocusOwner() && (inputContext2 = getInputContext()) != null) {
            inputContext2.dispatchEvent(new FocusEvent(this, 1004));
        }
        this.eventMask |= 4096;
    }

    public void setVisible(boolean z2) {
        show(z2);
    }

    @Deprecated
    public void show() {
        if (!this.visible) {
            synchronized (getTreeLock()) {
                this.visible = true;
                mixOnShowing();
                ComponentPeer componentPeer = this.peer;
                if (componentPeer != null) {
                    componentPeer.setVisible(true);
                    createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
                    if (componentPeer instanceof LightweightPeer) {
                        repaint();
                    }
                    updateCursorImmediately();
                }
                if (this.componentListener != null || (this.eventMask & 1) != 0 || Toolkit.enabledOnToolkit(1L)) {
                    Toolkit.getEventQueue().postEvent(new ComponentEvent(this, 102));
                }
            }
            Container container = this.parent;
            if (container != null) {
                container.invalidate();
            }
        }
    }

    @Deprecated
    public void show(boolean z2) {
        if (z2) {
            show();
        } else {
            hide();
        }
    }

    boolean containsFocus() {
        return isFocusOwner();
    }

    void clearMostRecentFocusOwnerOnHide() {
        KeyboardFocusManager.clearMostRecentFocusOwner(this);
    }

    void clearCurrentFocusCycleRootOnHide() {
    }

    @Deprecated
    public void hide() {
        this.isPacked = false;
        if (this.visible) {
            clearCurrentFocusCycleRootOnHide();
            clearMostRecentFocusOwnerOnHide();
            synchronized (getTreeLock()) {
                this.visible = false;
                mixOnHiding(isLightweight());
                if (containsFocus() && KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                    transferFocus(true);
                }
                ComponentPeer componentPeer = this.peer;
                if (componentPeer != null) {
                    componentPeer.setVisible(false);
                    createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
                    if (componentPeer instanceof LightweightPeer) {
                        repaint();
                    }
                    updateCursorImmediately();
                }
                if (this.componentListener != null || (this.eventMask & 1) != 0 || Toolkit.enabledOnToolkit(1L)) {
                    Toolkit.getEventQueue().postEvent(new ComponentEvent(this, 103));
                }
            }
            Container container = this.parent;
            if (container != null) {
                container.invalidate();
            }
        }
    }

    @Transient
    public Color getForeground() {
        Color color = this.foreground;
        if (color != null) {
            return color;
        }
        Container container = this.parent;
        if (container != null) {
            return container.getForeground();
        }
        return null;
    }

    public void setForeground(Color color) {
        Color color2 = this.foreground;
        ComponentPeer componentPeer = this.peer;
        this.foreground = color;
        if (componentPeer != null) {
            color = getForeground();
            if (color != null) {
                componentPeer.setForeground(color);
            }
        }
        firePropertyChange("foreground", color2, color);
    }

    public boolean isForegroundSet() {
        return this.foreground != null;
    }

    @Transient
    public Color getBackground() {
        Color color = this.background;
        if (color != null) {
            return color;
        }
        Container container = this.parent;
        if (container != null) {
            return container.getBackground();
        }
        return null;
    }

    public void setBackground(Color color) {
        Color color2 = this.background;
        ComponentPeer componentPeer = this.peer;
        this.background = color;
        if (componentPeer != null) {
            color = getBackground();
            if (color != null) {
                componentPeer.setBackground(color);
            }
        }
        firePropertyChange("background", color2, color);
    }

    public boolean isBackgroundSet() {
        return this.background != null;
    }

    @Transient
    public Font getFont() {
        return getFont_NoClientCode();
    }

    final Font getFont_NoClientCode() {
        Font font = this.font;
        if (font != null) {
            return font;
        }
        Container container = this.parent;
        if (container != null) {
            return container.getFont_NoClientCode();
        }
        return null;
    }

    public void setFont(Font font) {
        Font font2;
        synchronized (getTreeLock()) {
            font2 = this.font;
            this.font = font;
            ComponentPeer componentPeer = this.peer;
            if (componentPeer != null) {
                font = getFont();
                if (font != null) {
                    componentPeer.setFont(font);
                    this.peerFont = font;
                }
            }
        }
        firePropertyChange("font", font2, font);
        if (font != font2) {
            if (font2 == null || !font2.equals(font)) {
                invalidateIfValid();
            }
        }
    }

    public boolean isFontSet() {
        return this.font != null;
    }

    public Locale getLocale() {
        Locale locale = this.locale;
        if (locale != null) {
            return locale;
        }
        Container container = this.parent;
        if (container == null) {
            throw new IllegalComponentStateException("This component must have a parent in order to determine its locale");
        }
        return container.getLocale();
    }

    public void setLocale(Locale locale) {
        Locale locale2 = this.locale;
        this.locale = locale;
        firePropertyChange("locale", locale2, locale);
        invalidateIfValid();
    }

    public ColorModel getColorModel() {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer != null && !(componentPeer instanceof LightweightPeer)) {
            return componentPeer.getColorModel();
        }
        if (GraphicsEnvironment.isHeadless()) {
            return ColorModel.getRGBdefault();
        }
        return getToolkit().getColorModel();
    }

    public Point getLocation() {
        return location();
    }

    public Point getLocationOnScreen() {
        Point locationOnScreen_NoTreeLock;
        synchronized (getTreeLock()) {
            locationOnScreen_NoTreeLock = getLocationOnScreen_NoTreeLock();
        }
        return locationOnScreen_NoTreeLock;
    }

    final Point getLocationOnScreen_NoTreeLock() {
        if (this.peer != null && isShowing()) {
            if (this.peer instanceof LightweightPeer) {
                Container nativeContainer = getNativeContainer();
                Point locationOnScreen = nativeContainer.peer.getLocationOnScreen();
                Component parent = this;
                while (true) {
                    Component component = parent;
                    if (component != nativeContainer) {
                        locationOnScreen.f12370x += component.f12361x;
                        locationOnScreen.f12371y += component.f12362y;
                        parent = component.getParent();
                    } else {
                        return locationOnScreen;
                    }
                }
            } else {
                return this.peer.getLocationOnScreen();
            }
        } else {
            throw new IllegalComponentStateException("component must be showing on the screen to determine its location");
        }
    }

    @Deprecated
    public Point location() {
        return location_NoClientCode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point location_NoClientCode() {
        return new Point(this.f12361x, this.f12362y);
    }

    public void setLocation(int i2, int i3) {
        move(i2, i3);
    }

    @Deprecated
    public void move(int i2, int i3) {
        synchronized (getTreeLock()) {
            setBoundsOp(1);
            setBounds(i2, i3, this.width, this.height);
        }
    }

    public void setLocation(Point point) {
        setLocation(point.f12370x, point.f12371y);
    }

    public Dimension getSize() {
        return size();
    }

    @Deprecated
    public Dimension size() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(int i2, int i3) {
        resize(i2, i3);
    }

    @Deprecated
    public void resize(int i2, int i3) {
        synchronized (getTreeLock()) {
            setBoundsOp(2);
            setBounds(this.f12361x, this.f12362y, i2, i3);
        }
    }

    public void setSize(Dimension dimension) {
        resize(dimension);
    }

    @Deprecated
    public void resize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public Rectangle getBounds() {
        return bounds();
    }

    @Deprecated
    public Rectangle bounds() {
        return new Rectangle(this.f12361x, this.f12362y, this.width, this.height);
    }

    public void setBounds(int i2, int i3, int i4, int i5) {
        reshape(i2, i3, i4, i5);
    }

    @Deprecated
    public void reshape(int i2, int i3, int i4, int i5) {
        synchronized (getTreeLock()) {
            try {
                setBoundsOp(3);
                boolean z2 = (this.width == i4 && this.height == i5) ? false : true;
                boolean z3 = (this.f12361x == i2 && this.f12362y == i3) ? false : true;
                if (!z2 && !z3) {
                    return;
                }
                int i6 = this.f12361x;
                int i7 = this.f12362y;
                int i8 = this.width;
                int i9 = this.height;
                this.f12361x = i2;
                this.f12362y = i3;
                this.width = i4;
                this.height = i5;
                if (z2) {
                    this.isPacked = false;
                }
                boolean z4 = true;
                mixOnReshaping();
                if (this.peer != null) {
                    if (!(this.peer instanceof LightweightPeer)) {
                        reshapeNativePeer(i2, i3, i4, i5, getBoundsOp());
                        z2 = (i8 == this.width && i9 == this.height) ? false : true;
                        z3 = (i6 == this.f12361x && i7 == this.f12362y) ? false : true;
                        if (this instanceof Window) {
                            z4 = false;
                        }
                    }
                    if (z2) {
                        invalidate();
                    }
                    if (this.parent != null) {
                        this.parent.invalidateIfValid();
                    }
                }
                if (z4) {
                    notifyNewBounds(z2, z3);
                }
                repaintParentIfNeeded(i6, i7, i8, i9);
                setBoundsOp(5);
            } finally {
                setBoundsOp(5);
            }
        }
    }

    private void repaintParentIfNeeded(int i2, int i3, int i4, int i5) {
        if (this.parent != null && (this.peer instanceof LightweightPeer) && isShowing()) {
            this.parent.repaint(i2, i3, i4, i5);
            repaint();
        }
    }

    private void reshapeNativePeer(int i2, int i3, int i4, int i5, int i6) {
        int i7 = i2;
        int i8 = i3;
        Container container = this.parent;
        while (true) {
            Container container2 = container;
            if (container2 == null || !(container2.peer instanceof LightweightPeer)) {
                break;
            }
            i7 += container2.f12361x;
            i8 += container2.f12362y;
            container = container2.parent;
        }
        this.peer.setBounds(i7, i8, i4, i5, i6);
    }

    private void notifyNewBounds(boolean z2, boolean z3) {
        if (this.componentListener != null || (this.eventMask & 1) != 0 || Toolkit.enabledOnToolkit(1L)) {
            if (z2) {
                Toolkit.getEventQueue().postEvent(new ComponentEvent(this, 101));
            }
            if (z3) {
                Toolkit.getEventQueue().postEvent(new ComponentEvent(this, 100));
                return;
            }
            return;
        }
        if ((this instanceof Container) && ((Container) this).countComponents() > 0) {
            boolean zEnabledOnToolkit = Toolkit.enabledOnToolkit(65536L);
            if (z2) {
                ((Container) this).createChildHierarchyEvents(1402, 0L, zEnabledOnToolkit);
            }
            if (z3) {
                ((Container) this).createChildHierarchyEvents(HierarchyEvent.ANCESTOR_MOVED, 0L, zEnabledOnToolkit);
            }
        }
    }

    public void setBounds(Rectangle rectangle) {
        setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public int getX() {
        return this.f12361x;
    }

    public int getY() {
        return this.f12362y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Rectangle getBounds(Rectangle rectangle) {
        if (rectangle == null) {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        rectangle.setBounds(getX(), getY(), getWidth(), getHeight());
        return rectangle;
    }

    public Dimension getSize(Dimension dimension) {
        if (dimension == null) {
            return new Dimension(getWidth(), getHeight());
        }
        dimension.setSize(getWidth(), getHeight());
        return dimension;
    }

    public Point getLocation(Point point) {
        if (point == null) {
            return new Point(getX(), getY());
        }
        point.setLocation(getX(), getY());
        return point;
    }

    public boolean isOpaque() {
        return (getPeer() == null || isLightweight()) ? false : true;
    }

    public boolean isLightweight() {
        return getPeer() instanceof LightweightPeer;
    }

    public void setPreferredSize(Dimension dimension) {
        Dimension dimension2;
        if (this.prefSizeSet) {
            dimension2 = this.prefSize;
        } else {
            dimension2 = null;
        }
        this.prefSize = dimension;
        this.prefSizeSet = dimension != null;
        firePropertyChange("preferredSize", dimension2, dimension);
    }

    public boolean isPreferredSizeSet() {
        return this.prefSizeSet;
    }

    public Dimension getPreferredSize() {
        return preferredSize();
    }

    @Deprecated
    public Dimension preferredSize() {
        Dimension minimumSize;
        Dimension dimension = this.prefSize;
        if (dimension == null || (!isPreferredSizeSet() && !isValid())) {
            synchronized (getTreeLock()) {
                if (this.peer != null) {
                    minimumSize = this.peer.getPreferredSize();
                } else {
                    minimumSize = getMinimumSize();
                }
                this.prefSize = minimumSize;
                dimension = this.prefSize;
            }
        }
        return new Dimension(dimension);
    }

    public void setMinimumSize(Dimension dimension) {
        Dimension dimension2;
        if (this.minSizeSet) {
            dimension2 = this.minSize;
        } else {
            dimension2 = null;
        }
        this.minSize = dimension;
        this.minSizeSet = dimension != null;
        firePropertyChange("minimumSize", dimension2, dimension);
    }

    public boolean isMinimumSizeSet() {
        return this.minSizeSet;
    }

    public Dimension getMinimumSize() {
        return minimumSize();
    }

    @Deprecated
    public Dimension minimumSize() {
        Dimension size;
        Dimension dimension = this.minSize;
        if (dimension == null || (!isMinimumSizeSet() && !isValid())) {
            synchronized (getTreeLock()) {
                if (this.peer != null) {
                    size = this.peer.getMinimumSize();
                } else {
                    size = size();
                }
                this.minSize = size;
                dimension = this.minSize;
            }
        }
        return new Dimension(dimension);
    }

    public void setMaximumSize(Dimension dimension) {
        Dimension dimension2;
        if (this.maxSizeSet) {
            dimension2 = this.maxSize;
        } else {
            dimension2 = null;
        }
        this.maxSize = dimension;
        this.maxSizeSet = dimension != null;
        firePropertyChange("maximumSize", dimension2, dimension);
    }

    public boolean isMaximumSizeSet() {
        return this.maxSizeSet;
    }

    public Dimension getMaximumSize() {
        if (isMaximumSizeSet()) {
            return new Dimension(this.maxSize);
        }
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    public float getAlignmentX() {
        return 0.5f;
    }

    public float getAlignmentY() {
        return 0.5f;
    }

    public int getBaseline(int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        return -1;
    }

    public BaselineResizeBehavior getBaselineResizeBehavior() {
        return BaselineResizeBehavior.OTHER;
    }

    public void doLayout() {
        layout();
    }

    @Deprecated
    public void layout() {
    }

    public void validate() {
        synchronized (getTreeLock()) {
            ComponentPeer componentPeer = this.peer;
            boolean zIsValid = isValid();
            if (!zIsValid && componentPeer != null) {
                Font font = getFont();
                Font font2 = this.peerFont;
                if (font != font2 && (font2 == null || !font2.equals(font))) {
                    componentPeer.setFont(font);
                    this.peerFont = font;
                }
                componentPeer.layout();
            }
            this.valid = true;
            if (!zIsValid) {
                mixOnValidating();
            }
        }
    }

    public void invalidate() {
        synchronized (getTreeLock()) {
            this.valid = false;
            if (!isPreferredSizeSet()) {
                this.prefSize = null;
            }
            if (!isMinimumSizeSet()) {
                this.minSize = null;
            }
            if (!isMaximumSizeSet()) {
                this.maxSize = null;
            }
            invalidateParent();
        }
    }

    void invalidateParent() {
        if (this.parent != null) {
            this.parent.invalidateIfValid();
        }
    }

    final void invalidateIfValid() {
        if (isValid()) {
            invalidate();
        }
    }

    public void revalidate() {
        revalidateSynchronously();
    }

    final void revalidateSynchronously() {
        synchronized (getTreeLock()) {
            invalidate();
            Container container = getContainer();
            if (container == null) {
                validate();
            } else {
                while (!container.isValidateRoot() && container.getContainer() != null) {
                    container = container.getContainer();
                }
                container.validate();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Graphics getGraphics() {
        Graphics graphics;
        if (this.peer instanceof LightweightPeer) {
            if (this.parent == null || (graphics = this.parent.getGraphics()) == 0) {
                return null;
            }
            if (graphics instanceof ConstrainableGraphics) {
                ((ConstrainableGraphics) graphics).constrain(this.f12361x, this.f12362y, this.width, this.height);
            } else {
                graphics.translate(this.f12361x, this.f12362y);
                graphics.setClip(0, 0, this.width, this.height);
            }
            graphics.setFont(getFont());
            return graphics;
        }
        ComponentPeer componentPeer = this.peer;
        if (componentPeer != null) {
            return componentPeer.getGraphics();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    final Graphics getGraphics_NoClientCode() {
        Graphics graphics_NoClientCode;
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof LightweightPeer) {
            Container container = this.parent;
            if (container == null || (graphics_NoClientCode = container.getGraphics_NoClientCode()) == 0) {
                return null;
            }
            if (graphics_NoClientCode instanceof ConstrainableGraphics) {
                ((ConstrainableGraphics) graphics_NoClientCode).constrain(this.f12361x, this.f12362y, this.width, this.height);
            } else {
                graphics_NoClientCode.translate(this.f12361x, this.f12362y);
                graphics_NoClientCode.setClip(0, 0, this.width, this.height);
            }
            graphics_NoClientCode.setFont(getFont_NoClientCode());
            return graphics_NoClientCode;
        }
        if (componentPeer != null) {
            return componentPeer.getGraphics();
        }
        return null;
    }

    public FontMetrics getFontMetrics(Font font) {
        FontManager fontManagerFactory = FontManagerFactory.getInstance();
        if ((fontManagerFactory instanceof SunFontManager) && ((SunFontManager) fontManagerFactory).usePlatformFontMetrics() && this.peer != null && !(this.peer instanceof LightweightPeer)) {
            return this.peer.getFontMetrics(font);
        }
        return FontDesignMetrics.getMetrics(font);
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        updateCursorImmediately();
    }

    final void updateCursorImmediately() {
        ComponentPeer peer;
        if (!(this.peer instanceof LightweightPeer)) {
            if (this.peer != null) {
                this.peer.updateCursorImmediately();
            }
        } else {
            Container nativeContainer = getNativeContainer();
            if (nativeContainer != null && (peer = nativeContainer.getPeer()) != null) {
                peer.updateCursorImmediately();
            }
        }
    }

    public Cursor getCursor() {
        return getCursor_NoClientCode();
    }

    final Cursor getCursor_NoClientCode() {
        Cursor cursor = this.cursor;
        if (cursor != null) {
            return cursor;
        }
        Container container = this.parent;
        if (container != null) {
            return container.getCursor_NoClientCode();
        }
        return Cursor.getPredefinedCursor(0);
    }

    public boolean isCursorSet() {
        return this.cursor != null;
    }

    public void paint(Graphics graphics) {
    }

    public void update(Graphics graphics) {
        paint(graphics);
    }

    public void paintAll(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PeerPaintCallback.getInstance().runOneComponent(this, new Rectangle(0, 0, this.width, this.height), graphics, graphics.getClip(), 3);
        }
    }

    void lightweightPaint(Graphics graphics) {
        paint(graphics);
    }

    void paintHeavyweightComponents(Graphics graphics) {
    }

    public void repaint() {
        repaint(0L, 0, 0, this.width, this.height);
    }

    public void repaint(long j2) {
        repaint(j2, 0, 0, this.width, this.height);
    }

    public void repaint(int i2, int i3, int i4, int i5) {
        repaint(0L, i2, i3, i4, i5);
    }

    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        if (this.peer instanceof LightweightPeer) {
            if (this.parent != null) {
                if (i2 < 0) {
                    i4 += i2;
                    i2 = 0;
                }
                if (i3 < 0) {
                    i5 += i3;
                    i3 = 0;
                }
                int i6 = i4 > this.width ? this.width : i4;
                int i7 = i5 > this.height ? this.height : i5;
                if (i6 <= 0 || i7 <= 0) {
                    return;
                }
                this.parent.repaint(j2, this.f12361x + i2, this.f12362y + i3, i6, i7);
                return;
            }
            return;
        }
        if (isVisible() && this.peer != null && i4 > 0 && i5 > 0) {
            SunToolkit.postEvent(SunToolkit.targetToAppContext(this), new PaintEvent(this, 801, new Rectangle(i2, i3, i4, i5)));
        }
    }

    public void print(Graphics graphics) {
        paint(graphics);
    }

    public void printAll(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PeerPrintCallback.getInstance().runOneComponent(this, new Rectangle(0, 0, this.width, this.height), graphics, graphics.getClip(), 3);
        }
    }

    void lightweightPrint(Graphics graphics) {
        print(graphics);
    }

    void printHeavyweightComponents(Graphics graphics) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Insets getInsets_NoClientCode() {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof ContainerPeer) {
            return (Insets) ((ContainerPeer) componentPeer).getInsets().clone();
        }
        return new Insets(0, 0, 0, 0);
    }

    @Override // java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        int i7 = -1;
        if ((i2 & 48) != 0) {
            i7 = 0;
        } else if ((i2 & 8) != 0 && isInc) {
            i7 = incRate;
            if (i7 < 0) {
                i7 = 0;
            }
        }
        if (i7 >= 0) {
            repaint(i7, 0, 0, this.width, this.height);
        }
        return (i2 & 160) == 0;
    }

    public Image createImage(ImageProducer imageProducer) {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer != null && !(componentPeer instanceof LightweightPeer)) {
            return componentPeer.createImage(imageProducer);
        }
        return getToolkit().createImage(imageProducer);
    }

    public Image createImage(int i2, int i3) {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof LightweightPeer) {
            if (this.parent != null) {
                return this.parent.createImage(i2, i3);
            }
            return null;
        }
        if (componentPeer != null) {
            return componentPeer.createImage(i2, i3);
        }
        return null;
    }

    public VolatileImage createVolatileImage(int i2, int i3) {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof LightweightPeer) {
            if (this.parent != null) {
                return this.parent.createVolatileImage(i2, i3);
            }
            return null;
        }
        if (componentPeer != null) {
            return componentPeer.createVolatileImage(i2, i3);
        }
        return null;
    }

    public VolatileImage createVolatileImage(int i2, int i3, ImageCapabilities imageCapabilities) throws AWTException {
        return createVolatileImage(i2, i3);
    }

    public boolean prepareImage(Image image, ImageObserver imageObserver) {
        return prepareImage(image, -1, -1, imageObserver);
    }

    public boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof LightweightPeer) {
            if (this.parent != null) {
                return this.parent.prepareImage(image, i2, i3, imageObserver);
            }
            return getToolkit().prepareImage(image, i2, i3, imageObserver);
        }
        if (componentPeer != null) {
            return componentPeer.prepareImage(image, i2, i3, imageObserver);
        }
        return getToolkit().prepareImage(image, i2, i3, imageObserver);
    }

    public int checkImage(Image image, ImageObserver imageObserver) {
        return checkImage(image, -1, -1, imageObserver);
    }

    public int checkImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof LightweightPeer) {
            if (this.parent != null) {
                return this.parent.checkImage(image, i2, i3, imageObserver);
            }
            return getToolkit().checkImage(image, i2, i3, imageObserver);
        }
        if (componentPeer != null) {
            return componentPeer.checkImage(image, i2, i3, imageObserver);
        }
        return getToolkit().checkImage(image, i2, i3, imageObserver);
    }

    void createBufferStrategy(int i2) {
        if (i2 > 1) {
            try {
                createBufferStrategy(i2, new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.UNDEFINED));
                return;
            } catch (AWTException e2) {
            }
        }
        try {
            createBufferStrategy(i2, new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), null));
        } catch (AWTException e3) {
            try {
                createBufferStrategy(i2, new BufferCapabilities(new ImageCapabilities(false), new ImageCapabilities(false), null));
            } catch (AWTException e4) {
                throw new InternalError("Could not create a buffer strategy", e4);
            }
        }
    }

    void createBufferStrategy(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        if (i2 < 1) {
            throw new IllegalArgumentException("Number of buffers must be at least 1");
        }
        if (bufferCapabilities == null) {
            throw new IllegalArgumentException("No capabilities specified");
        }
        if (this.bufferStrategy != null) {
            this.bufferStrategy.dispose();
        }
        if (i2 == 1) {
            this.bufferStrategy = new SingleBufferStrategy(bufferCapabilities);
            return;
        }
        SunGraphicsEnvironment sunGraphicsEnvironment = (SunGraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!bufferCapabilities.isPageFlipping() && sunGraphicsEnvironment.isFlipStrategyPreferred(this.peer)) {
            bufferCapabilities = new ProxyCapabilities(bufferCapabilities);
        }
        if (bufferCapabilities.isPageFlipping()) {
            this.bufferStrategy = new FlipSubRegionBufferStrategy(i2, bufferCapabilities);
        } else {
            this.bufferStrategy = new BltSubRegionBufferStrategy(i2, bufferCapabilities);
        }
    }

    /* loaded from: rt.jar:java/awt/Component$ProxyCapabilities.class */
    private class ProxyCapabilities extends ExtendedBufferCapabilities {
        private BufferCapabilities orig;

        private ProxyCapabilities(BufferCapabilities bufferCapabilities) {
            super(bufferCapabilities.getFrontBufferCapabilities(), bufferCapabilities.getBackBufferCapabilities(), bufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.BACKGROUND ? BufferCapabilities.FlipContents.BACKGROUND : BufferCapabilities.FlipContents.COPIED);
            this.orig = bufferCapabilities;
        }
    }

    BufferStrategy getBufferStrategy() {
        return this.bufferStrategy;
    }

    Image getBackBuffer() {
        if (this.bufferStrategy != null) {
            if (this.bufferStrategy instanceof BltBufferStrategy) {
                return ((BltBufferStrategy) this.bufferStrategy).getBackBuffer();
            }
            if (this.bufferStrategy instanceof FlipBufferStrategy) {
                return ((FlipBufferStrategy) this.bufferStrategy).getBackBuffer();
            }
            return null;
        }
        return null;
    }

    /* loaded from: rt.jar:java/awt/Component$FlipBufferStrategy.class */
    protected class FlipBufferStrategy extends BufferStrategy {
        protected int numBuffers;
        protected BufferCapabilities caps;
        protected Image drawBuffer;
        protected VolatileImage drawVBuffer;
        protected boolean validatedContents;
        int width;
        int height;

        protected FlipBufferStrategy(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
            if (!(Component.this instanceof Window) && !(Component.this instanceof Canvas)) {
                throw new ClassCastException("Component must be a Canvas or Window");
            }
            this.numBuffers = i2;
            this.caps = bufferCapabilities;
            createBuffers(i2, bufferCapabilities);
        }

        protected void createBuffers(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
            if (i2 < 2) {
                throw new IllegalArgumentException("Number of buffers cannot be less than two");
            }
            if (Component.this.peer == null) {
                throw new IllegalStateException("Component must have a valid peer");
            }
            if (bufferCapabilities == null || !bufferCapabilities.isPageFlipping()) {
                throw new IllegalArgumentException("Page flipping capabilities must be specified");
            }
            this.width = Component.this.getWidth();
            this.height = Component.this.getHeight();
            if (this.drawBuffer != null) {
                this.drawBuffer = null;
                this.drawVBuffer = null;
                destroyBuffers();
            }
            if (bufferCapabilities instanceof ExtendedBufferCapabilities) {
                ExtendedBufferCapabilities extendedBufferCapabilities = (ExtendedBufferCapabilities) bufferCapabilities;
                if (extendedBufferCapabilities.getVSync() == ExtendedBufferCapabilities.VSyncType.VSYNC_ON && !VSyncedBSManager.vsyncAllowed(this)) {
                    bufferCapabilities = extendedBufferCapabilities.derive(ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT);
                }
            }
            Component.this.peer.createBuffers(i2, bufferCapabilities);
            updateInternalBuffers();
        }

        private void updateInternalBuffers() {
            this.drawBuffer = getBackBuffer();
            if (this.drawBuffer instanceof VolatileImage) {
                this.drawVBuffer = (VolatileImage) this.drawBuffer;
            } else {
                this.drawVBuffer = null;
            }
        }

        protected Image getBackBuffer() {
            if (Component.this.peer != null) {
                return Component.this.peer.getBackBuffer();
            }
            throw new IllegalStateException("Component must have a valid peer");
        }

        protected void flip(BufferCapabilities.FlipContents flipContents) {
            if (Component.this.peer != null) {
                Image backBuffer = getBackBuffer();
                if (backBuffer != null) {
                    Component.this.peer.flip(0, 0, backBuffer.getWidth(null), backBuffer.getHeight(null), flipContents);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Component must have a valid peer");
        }

        void flipSubRegion(int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
            if (Component.this.peer != null) {
                Component.this.peer.flip(i2, i3, i4, i5, flipContents);
                return;
            }
            throw new IllegalStateException("Component must have a valid peer");
        }

        protected void destroyBuffers() {
            VSyncedBSManager.releaseVsync(this);
            if (Component.this.peer != null) {
                Component.this.peer.destroyBuffers();
                return;
            }
            throw new IllegalStateException("Component must have a valid peer");
        }

        @Override // java.awt.image.BufferStrategy
        public BufferCapabilities getCapabilities() {
            if (this.caps instanceof ProxyCapabilities) {
                return ((ProxyCapabilities) this.caps).orig;
            }
            return this.caps;
        }

        @Override // java.awt.image.BufferStrategy
        public Graphics getDrawGraphics() {
            revalidate();
            return this.drawBuffer.getGraphics();
        }

        protected void revalidate() {
            revalidate(true);
        }

        void revalidate(boolean z2) {
            this.validatedContents = false;
            if (z2 && (Component.this.getWidth() != this.width || Component.this.getHeight() != this.height)) {
                try {
                    createBuffers(this.numBuffers, this.caps);
                } catch (AWTException e2) {
                }
                this.validatedContents = true;
            }
            updateInternalBuffers();
            if (this.drawVBuffer != null) {
                GraphicsConfiguration graphicsConfiguration_NoClientCode = Component.this.getGraphicsConfiguration_NoClientCode();
                int iValidate = this.drawVBuffer.validate(graphicsConfiguration_NoClientCode);
                if (iValidate == 2) {
                    try {
                        createBuffers(this.numBuffers, this.caps);
                    } catch (AWTException e3) {
                    }
                    if (this.drawVBuffer != null) {
                        this.drawVBuffer.validate(graphicsConfiguration_NoClientCode);
                    }
                    this.validatedContents = true;
                    return;
                }
                if (iValidate == 1) {
                    this.validatedContents = true;
                }
            }
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsLost() {
            if (this.drawVBuffer == null) {
                return false;
            }
            return this.drawVBuffer.contentsLost();
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsRestored() {
            return this.validatedContents;
        }

        @Override // java.awt.image.BufferStrategy
        public void show() {
            flip(this.caps.getFlipContents());
        }

        void showSubRegion(int i2, int i3, int i4, int i5) {
            flipSubRegion(i2, i3, i4, i5, this.caps.getFlipContents());
        }

        @Override // java.awt.image.BufferStrategy
        public void dispose() {
            if (Component.this.bufferStrategy == this) {
                Component.this.bufferStrategy = null;
                if (Component.this.peer != null) {
                    destroyBuffers();
                }
            }
        }
    }

    /* loaded from: rt.jar:java/awt/Component$BltBufferStrategy.class */
    protected class BltBufferStrategy extends BufferStrategy {
        protected BufferCapabilities caps;
        protected VolatileImage[] backBuffers;
        protected boolean validatedContents;
        protected int width;
        protected int height;
        private Insets insets;

        protected BltBufferStrategy(int i2, BufferCapabilities bufferCapabilities) {
            this.caps = bufferCapabilities;
            createBackBuffers(i2 - 1);
        }

        @Override // java.awt.image.BufferStrategy
        public void dispose() {
            if (this.backBuffers != null) {
                for (int length = this.backBuffers.length - 1; length >= 0; length--) {
                    if (this.backBuffers[length] != null) {
                        this.backBuffers[length].flush();
                        this.backBuffers[length] = null;
                    }
                }
            }
            if (Component.this.bufferStrategy == this) {
                Component.this.bufferStrategy = null;
            }
        }

        protected void createBackBuffers(int i2) {
            if (i2 == 0) {
                this.backBuffers = null;
                return;
            }
            this.width = Component.this.getWidth();
            this.height = Component.this.getHeight();
            this.insets = Component.this.getInsets_NoClientCode();
            int i3 = (this.width - this.insets.left) - this.insets.right;
            int i4 = (this.height - this.insets.top) - this.insets.bottom;
            int iMax = Math.max(1, i3);
            int iMax2 = Math.max(1, i4);
            if (this.backBuffers == null) {
                this.backBuffers = new VolatileImage[i2];
            } else {
                for (int i5 = 0; i5 < i2; i5++) {
                    if (this.backBuffers[i5] != null) {
                        this.backBuffers[i5].flush();
                        this.backBuffers[i5] = null;
                    }
                }
            }
            for (int i6 = 0; i6 < i2; i6++) {
                this.backBuffers[i6] = Component.this.createVolatileImage(iMax, iMax2);
            }
        }

        @Override // java.awt.image.BufferStrategy
        public BufferCapabilities getCapabilities() {
            return this.caps;
        }

        @Override // java.awt.image.BufferStrategy
        public Graphics getDrawGraphics() {
            revalidate();
            Image backBuffer = getBackBuffer();
            if (backBuffer == null) {
                return Component.this.getGraphics();
            }
            SunGraphics2D sunGraphics2D = (SunGraphics2D) backBuffer.getGraphics();
            sunGraphics2D.constrain(-this.insets.left, -this.insets.top, backBuffer.getWidth(null) + this.insets.left, backBuffer.getHeight(null) + this.insets.top);
            return sunGraphics2D;
        }

        Image getBackBuffer() {
            if (this.backBuffers != null) {
                return this.backBuffers[this.backBuffers.length - 1];
            }
            return null;
        }

        @Override // java.awt.image.BufferStrategy
        public void show() {
            showSubRegion(this.insets.left, this.insets.top, this.width - this.insets.right, this.height - this.insets.bottom);
        }

        void showSubRegion(int i2, int i3, int i4, int i5) {
            if (this.backBuffers == null) {
                return;
            }
            int i6 = i2 - this.insets.left;
            int i7 = i4 - this.insets.left;
            int i8 = i3 - this.insets.top;
            int i9 = i5 - this.insets.top;
            Graphics graphics_NoClientCode = Component.this.getGraphics_NoClientCode();
            if (graphics_NoClientCode == null) {
                return;
            }
            try {
                graphics_NoClientCode.translate(this.insets.left, this.insets.top);
                for (int i10 = 0; i10 < this.backBuffers.length; i10++) {
                    graphics_NoClientCode.drawImage(this.backBuffers[i10], i6, i8, i7, i9, i6, i8, i7, i9, null);
                    graphics_NoClientCode.dispose();
                    graphics_NoClientCode = this.backBuffers[i10].getGraphics();
                }
            } finally {
                if (graphics_NoClientCode != null) {
                    graphics_NoClientCode.dispose();
                }
            }
        }

        protected void revalidate() {
            revalidate(true);
        }

        void revalidate(boolean z2) {
            this.validatedContents = false;
            if (this.backBuffers == null) {
                return;
            }
            if (z2) {
                Insets insets_NoClientCode = Component.this.getInsets_NoClientCode();
                if (Component.this.getWidth() != this.width || Component.this.getHeight() != this.height || !insets_NoClientCode.equals(this.insets)) {
                    createBackBuffers(this.backBuffers.length);
                    this.validatedContents = true;
                }
            }
            GraphicsConfiguration graphicsConfiguration_NoClientCode = Component.this.getGraphicsConfiguration_NoClientCode();
            int iValidate = this.backBuffers[this.backBuffers.length - 1].validate(graphicsConfiguration_NoClientCode);
            if (iValidate == 2) {
                if (z2) {
                    createBackBuffers(this.backBuffers.length);
                    this.backBuffers[this.backBuffers.length - 1].validate(graphicsConfiguration_NoClientCode);
                }
                this.validatedContents = true;
                return;
            }
            if (iValidate == 1) {
                this.validatedContents = true;
            }
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsLost() {
            if (this.backBuffers == null) {
                return false;
            }
            return this.backBuffers[this.backBuffers.length - 1].contentsLost();
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsRestored() {
            return this.validatedContents;
        }
    }

    /* loaded from: rt.jar:java/awt/Component$FlipSubRegionBufferStrategy.class */
    private class FlipSubRegionBufferStrategy extends FlipBufferStrategy implements SubRegionShowable {
        protected FlipSubRegionBufferStrategy(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
            super(i2, bufferCapabilities);
        }

        @Override // sun.awt.SubRegionShowable
        public void show(int i2, int i3, int i4, int i5) {
            showSubRegion(i2, i3, i4, i5);
        }

        @Override // sun.awt.SubRegionShowable
        public boolean showIfNotLost(int i2, int i3, int i4, int i5) {
            if (!contentsLost()) {
                showSubRegion(i2, i3, i4, i5);
                return !contentsLost();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:java/awt/Component$BltSubRegionBufferStrategy.class */
    private class BltSubRegionBufferStrategy extends BltBufferStrategy implements SubRegionShowable {
        protected BltSubRegionBufferStrategy(int i2, BufferCapabilities bufferCapabilities) {
            super(i2, bufferCapabilities);
        }

        @Override // sun.awt.SubRegionShowable
        public void show(int i2, int i3, int i4, int i5) {
            showSubRegion(i2, i3, i4, i5);
        }

        @Override // sun.awt.SubRegionShowable
        public boolean showIfNotLost(int i2, int i3, int i4, int i5) {
            if (!contentsLost()) {
                showSubRegion(i2, i3, i4, i5);
                return !contentsLost();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:java/awt/Component$SingleBufferStrategy.class */
    private class SingleBufferStrategy extends BufferStrategy {
        private BufferCapabilities caps;

        public SingleBufferStrategy(BufferCapabilities bufferCapabilities) {
            this.caps = bufferCapabilities;
        }

        @Override // java.awt.image.BufferStrategy
        public BufferCapabilities getCapabilities() {
            return this.caps;
        }

        @Override // java.awt.image.BufferStrategy
        public Graphics getDrawGraphics() {
            return Component.this.getGraphics();
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsLost() {
            return false;
        }

        @Override // java.awt.image.BufferStrategy
        public boolean contentsRestored() {
            return false;
        }

        @Override // java.awt.image.BufferStrategy
        public void show() {
        }
    }

    public void setIgnoreRepaint(boolean z2) {
        this.ignoreRepaint = z2;
    }

    public boolean getIgnoreRepaint() {
        return this.ignoreRepaint;
    }

    public boolean contains(int i2, int i3) {
        return inside(i2, i3);
    }

    @Deprecated
    public boolean inside(int i2, int i3) {
        return i2 >= 0 && i2 < this.width && i3 >= 0 && i3 < this.height;
    }

    public boolean contains(Point point) {
        return contains(point.f12370x, point.f12371y);
    }

    public Component getComponentAt(int i2, int i3) {
        return locate(i2, i3);
    }

    @Deprecated
    public Component locate(int i2, int i3) {
        if (contains(i2, i3)) {
            return this;
        }
        return null;
    }

    public Component getComponentAt(Point point) {
        return getComponentAt(point.f12370x, point.f12371y);
    }

    @Deprecated
    public void deliverEvent(Event event) {
        postEvent(event);
    }

    public final void dispatchEvent(AWTEvent aWTEvent) {
        dispatchEventImpl(aWTEvent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void dispatchEventImpl(AWTEvent aWTEvent) {
        InputContext inputContext;
        Event eventConvertToOld;
        Component component;
        Container nativeContainer;
        InputContext inputContext2;
        int id = aWTEvent.getID();
        AppContext appContext = this.appContext;
        if (appContext != null && !appContext.equals(AppContext.getAppContext()) && eventLog.isLoggable(PlatformLogger.Level.FINE)) {
            eventLog.fine("Event " + ((Object) aWTEvent) + " is being dispatched on the wrong AppContext");
        }
        if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
            eventLog.finest("{0}", aWTEvent);
        }
        if (!(aWTEvent instanceof KeyEvent)) {
            EventQueue.setCurrentEventAndMostRecentTime(aWTEvent);
        }
        if (aWTEvent instanceof SunDropTargetEvent) {
            ((SunDropTargetEvent) aWTEvent).dispatch();
            return;
        }
        if (!aWTEvent.focusManagerIsDispatching) {
            if (aWTEvent.isPosted) {
                aWTEvent = KeyboardFocusManager.retargetFocusEvent(aWTEvent);
                aWTEvent.isPosted = true;
            }
            if (KeyboardFocusManager.getCurrentKeyboardFocusManager().dispatchEvent(aWTEvent)) {
                return;
            }
        }
        if ((aWTEvent instanceof FocusEvent) && focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
            focusLog.finest("" + ((Object) aWTEvent));
        }
        if (id == 507 && !eventTypeEnabled(id) && this.peer != null && !this.peer.handlesWheelScrolling() && dispatchMouseWheelToAncestor((MouseWheelEvent) aWTEvent)) {
            return;
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        defaultToolkit.notifyAWTEventListeners(aWTEvent);
        if (!aWTEvent.isConsumed() && (aWTEvent instanceof KeyEvent)) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().processKeyEvent(this, (KeyEvent) aWTEvent);
            if (aWTEvent.isConsumed()) {
                return;
            }
        }
        if (areInputMethodsEnabled()) {
            if ((((aWTEvent instanceof InputMethodEvent) && !(this instanceof CompositionArea)) || (aWTEvent instanceof InputEvent) || (aWTEvent instanceof FocusEvent)) && (inputContext2 = getInputContext()) != null) {
                inputContext2.dispatchEvent(aWTEvent);
                if (aWTEvent.isConsumed()) {
                    if ((aWTEvent instanceof FocusEvent) && focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                        focusLog.finest("3579: Skipping " + ((Object) aWTEvent));
                        return;
                    }
                    return;
                }
            }
        } else if (id == 1004 && (inputContext = getInputContext()) != null && (inputContext instanceof sun.awt.im.InputContext)) {
            ((sun.awt.im.InputContext) inputContext).disableNativeIM();
        }
        switch (id) {
            case 201:
                if (defaultToolkit instanceof WindowClosingListener) {
                    this.windowClosingException = ((WindowClosingListener) defaultToolkit).windowClosingNotify((WindowEvent) aWTEvent);
                    if (checkWindowClosingException()) {
                        return;
                    }
                }
                break;
            case 401:
            case 402:
                Container container = (Container) (this instanceof Container ? this : this.parent);
                if (container != null) {
                    container.preProcessKeyEvent((KeyEvent) aWTEvent);
                    if (aWTEvent.isConsumed()) {
                        if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                            focusLog.finest("Pre-process consumed event");
                            return;
                        }
                        return;
                    }
                }
                break;
        }
        if (this.newEventsOnly) {
            if (eventEnabled(aWTEvent)) {
                processEvent(aWTEvent);
            }
        } else if (id == 507) {
            autoProcessMouseWheel((MouseWheelEvent) aWTEvent);
        } else if ((!(aWTEvent instanceof MouseEvent) || postsOldMouseEvents()) && (eventConvertToOld = aWTEvent.convertToOld()) != null) {
            int i2 = eventConvertToOld.key;
            int i3 = eventConvertToOld.modifiers;
            postEvent(eventConvertToOld);
            if (eventConvertToOld.isConsumed()) {
                aWTEvent.consume();
            }
            switch (eventConvertToOld.id) {
                case 401:
                case 402:
                case 403:
                case 404:
                    if (eventConvertToOld.key != i2) {
                        ((KeyEvent) aWTEvent).setKeyChar(eventConvertToOld.getKeyEventChar());
                    }
                    if (eventConvertToOld.modifiers != i3) {
                        ((KeyEvent) aWTEvent).setModifiers(eventConvertToOld.modifiers);
                        break;
                    }
                    break;
            }
        }
        if (id == 201 && !aWTEvent.isConsumed() && (defaultToolkit instanceof WindowClosingListener)) {
            this.windowClosingException = ((WindowClosingListener) defaultToolkit).windowClosingDelivered((WindowEvent) aWTEvent);
            if (checkWindowClosingException()) {
                return;
            }
        }
        if (!(aWTEvent instanceof KeyEvent)) {
            ComponentPeer peer = this.peer;
            if ((aWTEvent instanceof FocusEvent) && ((peer == null || (peer instanceof LightweightPeer)) && (component = (Component) aWTEvent.getSource()) != null && (nativeContainer = component.getNativeContainer()) != null)) {
                peer = nativeContainer.getPeer();
            }
            if (peer != null) {
                peer.handleEvent(aWTEvent);
            }
        }
        if (SunToolkit.isTouchKeyboardAutoShowEnabled() && (defaultToolkit instanceof SunToolkit)) {
            if ((aWTEvent instanceof MouseEvent) || (aWTEvent instanceof FocusEvent)) {
                ((SunToolkit) defaultToolkit).showOrHideTouchKeyboard(this, aWTEvent);
            }
        }
    }

    void autoProcessMouseWheel(MouseWheelEvent mouseWheelEvent) {
    }

    boolean dispatchMouseWheelToAncestor(MouseWheelEvent mouseWheelEvent) {
        int x2 = mouseWheelEvent.getX() + getX();
        int y2 = mouseWheelEvent.getY() + getY();
        if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
            eventLog.finest("dispatchMouseWheelToAncestor");
            eventLog.finest("orig event src is of " + ((Object) mouseWheelEvent.getSource().getClass()));
        }
        synchronized (getTreeLock()) {
            Container parent = getParent();
            while (parent != null && !parent.eventEnabled(mouseWheelEvent)) {
                x2 += parent.getX();
                y2 += parent.getY();
                if (parent instanceof Window) {
                    break;
                }
                parent = parent.getParent();
            }
            if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
                eventLog.finest("new event src is " + ((Object) parent.getClass()));
            }
            if (parent != null && parent.eventEnabled(mouseWheelEvent)) {
                MouseWheelEvent mouseWheelEvent2 = new MouseWheelEvent(parent, mouseWheelEvent.getID(), mouseWheelEvent.getWhen(), mouseWheelEvent.getModifiers(), x2, y2, mouseWheelEvent.getXOnScreen(), mouseWheelEvent.getYOnScreen(), mouseWheelEvent.getClickCount(), mouseWheelEvent.isPopupTrigger(), mouseWheelEvent.getScrollType(), mouseWheelEvent.getScrollAmount(), mouseWheelEvent.getWheelRotation(), mouseWheelEvent.getPreciseWheelRotation());
                mouseWheelEvent.copyPrivateDataInto(mouseWheelEvent2);
                parent.dispatchEventToSelf(mouseWheelEvent2);
                if (mouseWheelEvent2.isConsumed()) {
                    mouseWheelEvent.consume();
                }
                return true;
            }
            return false;
        }
    }

    boolean checkWindowClosingException() {
        if (this.windowClosingException != null) {
            if (this instanceof Dialog) {
                ((Dialog) this).interruptBlocking();
                return true;
            }
            this.windowClosingException.fillInStackTrace();
            this.windowClosingException.printStackTrace();
            this.windowClosingException = null;
            return true;
        }
        return false;
    }

    boolean areInputMethodsEnabled() {
        return ((this.eventMask & 4096) == 0 || ((this.eventMask & 8) == 0 && this.keyListener == null)) ? false : true;
    }

    boolean eventEnabled(AWTEvent aWTEvent) {
        return eventTypeEnabled(aWTEvent.id);
    }

    boolean eventTypeEnabled(int i2) {
        switch (i2) {
            case 100:
            case 101:
            case 102:
            case 103:
                if ((this.eventMask & 1) != 0 || this.componentListener != null) {
                    return true;
                }
                break;
            case 400:
            case 401:
            case 402:
                if ((this.eventMask & 8) != 0 || this.keyListener != null) {
                    return true;
                }
                break;
            case 500:
            case 501:
            case 502:
            case 504:
            case 505:
                if ((this.eventMask & 16) != 0 || this.mouseListener != null) {
                    return true;
                }
                break;
            case 503:
            case 506:
                if ((this.eventMask & 32) != 0 || this.mouseMotionListener != null) {
                    return true;
                }
                break;
            case 507:
                if ((this.eventMask & 131072) != 0 || this.mouseWheelListener != null) {
                    return true;
                }
                break;
            case 601:
                if ((this.eventMask & 256) != 0) {
                    return true;
                }
                break;
            case 701:
                if ((this.eventMask & 512) != 0) {
                    return true;
                }
                break;
            case 900:
                if ((this.eventMask & 1024) != 0) {
                    return true;
                }
                break;
            case 1001:
                if ((this.eventMask & 128) != 0) {
                    return true;
                }
                break;
            case 1004:
            case 1005:
                if ((this.eventMask & 4) != 0 || this.focusListener != null) {
                    return true;
                }
                break;
            case 1100:
            case 1101:
                if ((this.eventMask & 2048) != 0 || this.inputMethodListener != null) {
                    return true;
                }
                break;
            case 1400:
                if ((this.eventMask & 32768) != 0 || this.hierarchyListener != null) {
                    return true;
                }
                break;
            case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
            case 1402:
                if ((this.eventMask & 65536) != 0 || this.hierarchyBoundsListener != null) {
                    return true;
                }
                break;
        }
        if (i2 > 1999) {
            return true;
        }
        return false;
    }

    @Override // java.awt.MenuContainer
    @Deprecated
    public boolean postEvent(Event event) {
        ComponentPeer componentPeer = this.peer;
        if (handleEvent(event)) {
            event.consume();
            return true;
        }
        Container container = this.parent;
        int i2 = event.f12363x;
        int i3 = event.f12364y;
        if (container != null) {
            event.translate(this.f12361x, this.f12362y);
            if (container.postEvent(event)) {
                event.consume();
                return true;
            }
            event.f12363x = i2;
            event.f12364y = i3;
            return false;
        }
        return false;
    }

    public synchronized void addComponentListener(ComponentListener componentListener) {
        if (componentListener == null) {
            return;
        }
        this.componentListener = AWTEventMulticaster.add(this.componentListener, componentListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeComponentListener(ComponentListener componentListener) {
        if (componentListener == null) {
            return;
        }
        this.componentListener = AWTEventMulticaster.remove(this.componentListener, componentListener);
    }

    public synchronized ComponentListener[] getComponentListeners() {
        return (ComponentListener[]) getListeners(ComponentListener.class);
    }

    public synchronized void addFocusListener(FocusListener focusListener) {
        if (focusListener == null) {
            return;
        }
        this.focusListener = AWTEventMulticaster.add(this.focusListener, focusListener);
        this.newEventsOnly = true;
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(4L);
        }
    }

    public synchronized void removeFocusListener(FocusListener focusListener) {
        if (focusListener == null) {
            return;
        }
        this.focusListener = AWTEventMulticaster.remove(this.focusListener, focusListener);
    }

    public synchronized FocusListener[] getFocusListeners() {
        return (FocusListener[]) getListeners(FocusListener.class);
    }

    public void addHierarchyListener(HierarchyListener hierarchyListener) {
        boolean z2;
        if (hierarchyListener == null) {
            return;
        }
        synchronized (this) {
            boolean z3 = this.hierarchyListener == null && (this.eventMask & 32768) == 0;
            this.hierarchyListener = AWTEventMulticaster.add(this.hierarchyListener, hierarchyListener);
            z2 = z3 && this.hierarchyListener != null;
            this.newEventsOnly = true;
        }
        if (z2) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(32768L, 1);
            }
        }
    }

    public void removeHierarchyListener(HierarchyListener hierarchyListener) {
        boolean z2;
        if (hierarchyListener == null) {
            return;
        }
        synchronized (this) {
            boolean z3 = this.hierarchyListener != null && (this.eventMask & 32768) == 0;
            this.hierarchyListener = AWTEventMulticaster.remove(this.hierarchyListener, hierarchyListener);
            z2 = z3 && this.hierarchyListener == null;
        }
        if (z2) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(32768L, -1);
            }
        }
    }

    public synchronized HierarchyListener[] getHierarchyListeners() {
        return (HierarchyListener[]) getListeners(HierarchyListener.class);
    }

    public void addHierarchyBoundsListener(HierarchyBoundsListener hierarchyBoundsListener) {
        boolean z2;
        if (hierarchyBoundsListener == null) {
            return;
        }
        synchronized (this) {
            boolean z3 = this.hierarchyBoundsListener == null && (this.eventMask & 65536) == 0;
            this.hierarchyBoundsListener = AWTEventMulticaster.add(this.hierarchyBoundsListener, hierarchyBoundsListener);
            z2 = z3 && this.hierarchyBoundsListener != null;
            this.newEventsOnly = true;
        }
        if (z2) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(65536L, 1);
            }
        }
    }

    public void removeHierarchyBoundsListener(HierarchyBoundsListener hierarchyBoundsListener) {
        boolean z2;
        if (hierarchyBoundsListener == null) {
            return;
        }
        synchronized (this) {
            boolean z3 = this.hierarchyBoundsListener != null && (this.eventMask & 65536) == 0;
            this.hierarchyBoundsListener = AWTEventMulticaster.remove(this.hierarchyBoundsListener, hierarchyBoundsListener);
            z2 = z3 && this.hierarchyBoundsListener == null;
        }
        if (z2) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(65536L, -1);
            }
        }
    }

    int numListening(long j2) {
        if (eventLog.isLoggable(PlatformLogger.Level.FINE) && j2 != 32768 && j2 != 65536) {
            eventLog.fine("Assertion failed");
        }
        if (j2 == 32768 && (this.hierarchyListener != null || (this.eventMask & 32768) != 0)) {
            return 1;
        }
        if (j2 != 65536) {
            return 0;
        }
        if (this.hierarchyBoundsListener != null || (this.eventMask & 65536) != 0) {
            return 1;
        }
        return 0;
    }

    int countHierarchyMembers() {
        return 1;
    }

    int createHierarchyEvents(int i2, Component component, Container container, long j2, boolean z2) {
        switch (i2) {
            case 1400:
                if (this.hierarchyListener != null || (this.eventMask & 32768) != 0 || z2) {
                    dispatchEvent(new HierarchyEvent(this, i2, component, container, j2));
                    return 1;
                }
                return 0;
            case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
            case 1402:
                if (eventLog.isLoggable(PlatformLogger.Level.FINE) && j2 != 0) {
                    eventLog.fine("Assertion (changeFlags == 0) failed");
                }
                if (this.hierarchyBoundsListener != null || (this.eventMask & 65536) != 0 || z2) {
                    dispatchEvent(new HierarchyEvent(this, i2, component, container));
                    return 1;
                }
                return 0;
            default:
                if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
                    eventLog.fine("This code must never be reached");
                    return 0;
                }
                return 0;
        }
    }

    public synchronized HierarchyBoundsListener[] getHierarchyBoundsListeners() {
        return (HierarchyBoundsListener[]) getListeners(HierarchyBoundsListener.class);
    }

    void adjustListeningChildrenOnParent(long j2, int i2) {
        if (this.parent != null) {
            this.parent.adjustListeningChildren(j2, i2);
        }
    }

    public synchronized void addKeyListener(KeyListener keyListener) {
        if (keyListener == null) {
            return;
        }
        this.keyListener = AWTEventMulticaster.add(this.keyListener, keyListener);
        this.newEventsOnly = true;
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(8L);
        }
    }

    public synchronized void removeKeyListener(KeyListener keyListener) {
        if (keyListener == null) {
            return;
        }
        this.keyListener = AWTEventMulticaster.remove(this.keyListener, keyListener);
    }

    public synchronized KeyListener[] getKeyListeners() {
        return (KeyListener[]) getListeners(KeyListener.class);
    }

    public synchronized void addMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) {
            return;
        }
        this.mouseListener = AWTEventMulticaster.add(this.mouseListener, mouseListener);
        this.newEventsOnly = true;
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(16L);
        }
    }

    public synchronized void removeMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) {
            return;
        }
        this.mouseListener = AWTEventMulticaster.remove(this.mouseListener, mouseListener);
    }

    public synchronized MouseListener[] getMouseListeners() {
        return (MouseListener[]) getListeners(MouseListener.class);
    }

    public synchronized void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) {
            return;
        }
        this.mouseMotionListener = AWTEventMulticaster.add(this.mouseMotionListener, mouseMotionListener);
        this.newEventsOnly = true;
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(32L);
        }
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) {
            return;
        }
        this.mouseMotionListener = AWTEventMulticaster.remove(this.mouseMotionListener, mouseMotionListener);
    }

    public synchronized MouseMotionListener[] getMouseMotionListeners() {
        return (MouseMotionListener[]) getListeners(MouseMotionListener.class);
    }

    public synchronized void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        if (mouseWheelListener == null) {
            return;
        }
        this.mouseWheelListener = AWTEventMulticaster.add(this.mouseWheelListener, mouseWheelListener);
        this.newEventsOnly = true;
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(131072L);
        }
    }

    public synchronized void removeMouseWheelListener(MouseWheelListener mouseWheelListener) {
        if (mouseWheelListener == null) {
            return;
        }
        this.mouseWheelListener = AWTEventMulticaster.remove(this.mouseWheelListener, mouseWheelListener);
    }

    public synchronized MouseWheelListener[] getMouseWheelListeners() {
        return (MouseWheelListener[]) getListeners(MouseWheelListener.class);
    }

    public synchronized void addInputMethodListener(InputMethodListener inputMethodListener) {
        if (inputMethodListener == null) {
            return;
        }
        this.inputMethodListener = AWTEventMulticaster.add(this.inputMethodListener, inputMethodListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeInputMethodListener(InputMethodListener inputMethodListener) {
        if (inputMethodListener == null) {
            return;
        }
        this.inputMethodListener = AWTEventMulticaster.remove(this.inputMethodListener, inputMethodListener);
    }

    public synchronized InputMethodListener[] getInputMethodListeners() {
        return (InputMethodListener[]) getListeners(InputMethodListener.class);
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        EventListener eventListener = null;
        if (cls == ComponentListener.class) {
            eventListener = this.componentListener;
        } else if (cls == FocusListener.class) {
            eventListener = this.focusListener;
        } else if (cls == HierarchyListener.class) {
            eventListener = this.hierarchyListener;
        } else if (cls == HierarchyBoundsListener.class) {
            eventListener = this.hierarchyBoundsListener;
        } else if (cls == KeyListener.class) {
            eventListener = this.keyListener;
        } else if (cls == MouseListener.class) {
            eventListener = this.mouseListener;
        } else if (cls == MouseMotionListener.class) {
            eventListener = this.mouseMotionListener;
        } else if (cls == MouseWheelListener.class) {
            eventListener = this.mouseWheelListener;
        } else if (cls == InputMethodListener.class) {
            eventListener = this.inputMethodListener;
        } else if (cls == PropertyChangeListener.class) {
            return getPropertyChangeListeners();
        }
        return (T[]) AWTEventMulticaster.getListeners(eventListener, cls);
    }

    public InputMethodRequests getInputMethodRequests() {
        return null;
    }

    public InputContext getInputContext() {
        Container container = this.parent;
        if (container == null) {
            return null;
        }
        return container.getInputContext();
    }

    protected final void enableEvents(long j2) {
        long j3 = 0;
        synchronized (this) {
            if ((j2 & 32768) != 0 && this.hierarchyListener == null && (this.eventMask & 32768) == 0) {
                j3 = 0 | 32768;
            }
            if ((j2 & 65536) != 0 && this.hierarchyBoundsListener == null && (this.eventMask & 65536) == 0) {
                j3 |= 65536;
            }
            this.eventMask |= j2;
            this.newEventsOnly = true;
        }
        if (this.peer instanceof LightweightPeer) {
            this.parent.proxyEnableEvents(this.eventMask);
        }
        if (j3 != 0) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(j3, 1);
            }
        }
    }

    protected final void disableEvents(long j2) {
        long j3 = 0;
        synchronized (this) {
            if ((j2 & 32768) != 0 && this.hierarchyListener == null && (this.eventMask & 32768) != 0) {
                j3 = 0 | 32768;
            }
            if ((j2 & 65536) != 0 && this.hierarchyBoundsListener == null && (this.eventMask & 65536) != 0) {
                j3 |= 65536;
            }
            this.eventMask &= j2 ^ (-1);
        }
        if (j3 != 0) {
            synchronized (getTreeLock()) {
                adjustListeningChildrenOnParent(j3, -1);
            }
        }
    }

    private boolean checkCoalescing() {
        if (getClass().getClassLoader() == null) {
            return false;
        }
        final Class<?> cls = getClass();
        synchronized (coalesceMap) {
            Boolean bool = coalesceMap.get(cls);
            if (bool != null) {
                return bool.booleanValue();
            }
            Boolean bool2 = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.awt.Component.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Boolean run2() {
                    return Boolean.valueOf(Component.isCoalesceEventsOverriden(cls));
                }
            });
            coalesceMap.put(cls, bool2);
            return bool2.booleanValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isCoalesceEventsOverriden(Class<?> cls) throws SecurityException {
        if (!$assertionsDisabled && !Thread.holdsLock(coalesceMap)) {
            throw new AssertionError();
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null) {
            return false;
        }
        if (superclass.getClassLoader() != null) {
            Boolean bool = coalesceMap.get(superclass);
            if (bool == null) {
                if (isCoalesceEventsOverriden(superclass)) {
                    coalesceMap.put(superclass, true);
                    return true;
                }
            } else if (bool.booleanValue()) {
                return true;
            }
        }
        try {
            cls.getDeclaredMethod("coalesceEvents", coalesceEventsParams);
            return true;
        } catch (NoSuchMethodException e2) {
            return false;
        }
    }

    final boolean isCoalescingEnabled() {
        return this.coalescingEnabled;
    }

    protected AWTEvent coalesceEvents(AWTEvent aWTEvent, AWTEvent aWTEvent2) {
        return null;
    }

    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof FocusEvent) {
            processFocusEvent((FocusEvent) aWTEvent);
            return;
        }
        if (aWTEvent instanceof MouseEvent) {
            switch (aWTEvent.getID()) {
                case 500:
                case 501:
                case 502:
                case 504:
                case 505:
                    processMouseEvent((MouseEvent) aWTEvent);
                    break;
                case 503:
                case 506:
                    processMouseMotionEvent((MouseEvent) aWTEvent);
                    break;
                case 507:
                    processMouseWheelEvent((MouseWheelEvent) aWTEvent);
                    break;
            }
        }
        if (aWTEvent instanceof KeyEvent) {
            processKeyEvent((KeyEvent) aWTEvent);
            return;
        }
        if (aWTEvent instanceof ComponentEvent) {
            processComponentEvent((ComponentEvent) aWTEvent);
            return;
        }
        if (aWTEvent instanceof InputMethodEvent) {
            processInputMethodEvent((InputMethodEvent) aWTEvent);
            return;
        }
        if (aWTEvent instanceof HierarchyEvent) {
            switch (aWTEvent.getID()) {
                case 1400:
                    processHierarchyEvent((HierarchyEvent) aWTEvent);
                    break;
                case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
                case 1402:
                    processHierarchyBoundsEvent((HierarchyEvent) aWTEvent);
                    break;
            }
        }
    }

    protected void processComponentEvent(ComponentEvent componentEvent) {
        ComponentListener componentListener = this.componentListener;
        if (componentListener != null) {
            switch (componentEvent.getID()) {
                case 100:
                    componentListener.componentMoved(componentEvent);
                    break;
                case 101:
                    componentListener.componentResized(componentEvent);
                    break;
                case 102:
                    componentListener.componentShown(componentEvent);
                    break;
                case 103:
                    componentListener.componentHidden(componentEvent);
                    break;
            }
        }
    }

    protected void processFocusEvent(FocusEvent focusEvent) {
        FocusListener focusListener = this.focusListener;
        if (focusListener != null) {
            switch (focusEvent.getID()) {
                case 1004:
                    focusListener.focusGained(focusEvent);
                    break;
                case 1005:
                    focusListener.focusLost(focusEvent);
                    break;
            }
        }
    }

    protected void processKeyEvent(KeyEvent keyEvent) {
        KeyListener keyListener = this.keyListener;
        if (keyListener != null) {
            switch (keyEvent.getID()) {
                case 400:
                    keyListener.keyTyped(keyEvent);
                    break;
                case 401:
                    keyListener.keyPressed(keyEvent);
                    break;
                case 402:
                    keyListener.keyReleased(keyEvent);
                    break;
            }
        }
    }

    protected void processMouseEvent(MouseEvent mouseEvent) {
        MouseListener mouseListener = this.mouseListener;
        if (mouseListener != null) {
            switch (mouseEvent.getID()) {
                case 500:
                    mouseListener.mouseClicked(mouseEvent);
                    break;
                case 501:
                    mouseListener.mousePressed(mouseEvent);
                    break;
                case 502:
                    mouseListener.mouseReleased(mouseEvent);
                    break;
                case 504:
                    mouseListener.mouseEntered(mouseEvent);
                    break;
                case 505:
                    mouseListener.mouseExited(mouseEvent);
                    break;
            }
        }
    }

    protected void processMouseMotionEvent(MouseEvent mouseEvent) {
        MouseMotionListener mouseMotionListener = this.mouseMotionListener;
        if (mouseMotionListener != null) {
            switch (mouseEvent.getID()) {
                case 503:
                    mouseMotionListener.mouseMoved(mouseEvent);
                    break;
                case 506:
                    mouseMotionListener.mouseDragged(mouseEvent);
                    break;
            }
        }
    }

    protected void processMouseWheelEvent(MouseWheelEvent mouseWheelEvent) {
        MouseWheelListener mouseWheelListener = this.mouseWheelListener;
        if (mouseWheelListener != null) {
            switch (mouseWheelEvent.getID()) {
                case 507:
                    mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
                    break;
            }
        }
    }

    boolean postsOldMouseEvents() {
        return false;
    }

    protected void processInputMethodEvent(InputMethodEvent inputMethodEvent) {
        InputMethodListener inputMethodListener = this.inputMethodListener;
        if (inputMethodListener != null) {
            switch (inputMethodEvent.getID()) {
                case 1100:
                    inputMethodListener.inputMethodTextChanged(inputMethodEvent);
                    break;
                case 1101:
                    inputMethodListener.caretPositionChanged(inputMethodEvent);
                    break;
            }
        }
    }

    protected void processHierarchyEvent(HierarchyEvent hierarchyEvent) {
        HierarchyListener hierarchyListener = this.hierarchyListener;
        if (hierarchyListener != null) {
            switch (hierarchyEvent.getID()) {
                case 1400:
                    hierarchyListener.hierarchyChanged(hierarchyEvent);
                    break;
            }
        }
    }

    protected void processHierarchyBoundsEvent(HierarchyEvent hierarchyEvent) {
        HierarchyBoundsListener hierarchyBoundsListener = this.hierarchyBoundsListener;
        if (hierarchyBoundsListener != null) {
            switch (hierarchyEvent.getID()) {
                case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
                    hierarchyBoundsListener.ancestorMoved(hierarchyEvent);
                    break;
                case 1402:
                    hierarchyBoundsListener.ancestorResized(hierarchyEvent);
                    break;
            }
        }
    }

    @Deprecated
    public boolean handleEvent(Event event) {
        switch (event.id) {
            case 401:
            case 403:
                return keyDown(event, event.key);
            case 402:
            case 404:
                return keyUp(event, event.key);
            case 501:
                return mouseDown(event, event.f12363x, event.f12364y);
            case 502:
                return mouseUp(event, event.f12363x, event.f12364y);
            case 503:
                return mouseMove(event, event.f12363x, event.f12364y);
            case 504:
                return mouseEnter(event, event.f12363x, event.f12364y);
            case 505:
                return mouseExit(event, event.f12363x, event.f12364y);
            case 506:
                return mouseDrag(event, event.f12363x, event.f12364y);
            case 1001:
                return action(event, event.arg);
            case 1004:
                return gotFocus(event, event.arg);
            case 1005:
                return lostFocus(event, event.arg);
            default:
                return false;
        }
    }

    @Deprecated
    public boolean mouseDown(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean mouseDrag(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean mouseUp(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean mouseMove(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean mouseEnter(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean mouseExit(Event event, int i2, int i3) {
        return false;
    }

    @Deprecated
    public boolean keyDown(Event event, int i2) {
        return false;
    }

    @Deprecated
    public boolean keyUp(Event event, int i2) {
        return false;
    }

    @Deprecated
    public boolean action(Event event, Object obj) {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.awt.peer.ComponentPeer] */
    public void addNotify() {
        synchronized (getTreeLock()) {
            LightweightPeer lightweightPeer = this.peer;
            if (lightweightPeer == null || (lightweightPeer instanceof LightweightPeer)) {
                if (lightweightPeer == null) {
                    LightweightPeer lightweightPeerCreateComponent = getToolkit().createComponent(this);
                    lightweightPeer = lightweightPeerCreateComponent;
                    this.peer = lightweightPeerCreateComponent;
                }
                if (this.parent != null) {
                    long j2 = 0;
                    if (this.mouseListener != null || (this.eventMask & 16) != 0) {
                        j2 = 0 | 16;
                    }
                    if (this.mouseMotionListener != null || (this.eventMask & 32) != 0) {
                        j2 |= 32;
                    }
                    if (this.mouseWheelListener != null || (this.eventMask & 131072) != 0) {
                        j2 |= 131072;
                    }
                    if (this.focusListener != null || (this.eventMask & 4) != 0) {
                        j2 |= 4;
                    }
                    if (this.keyListener != null || (this.eventMask & 8) != 0) {
                        j2 |= 8;
                    }
                    if (j2 != 0) {
                        this.parent.proxyEnableEvents(j2);
                    }
                }
            } else {
                Container container = getContainer();
                if (container != null && container.isLightweight()) {
                    relocateComponent();
                    if (!container.isRecursivelyVisibleUpToHeavyweightContainer()) {
                        lightweightPeer.setVisible(false);
                    }
                }
            }
            invalidate();
            int size = this.popups != null ? this.popups.size() : 0;
            for (int i2 = 0; i2 < size; i2++) {
                this.popups.elementAt(i2).addNotify();
            }
            if (this.dropTarget != null) {
                this.dropTarget.addNotify(lightweightPeer);
            }
            this.peerFont = getFont();
            if (getContainer() != null && !this.isAddNotifyComplete) {
                getContainer().increaseComponentCount(this);
            }
            updateZOrder();
            if (!this.isAddNotifyComplete) {
                mixOnShowing();
            }
            this.isAddNotifyComplete = true;
            if (this.hierarchyListener != null || (this.eventMask & 32768) != 0 || Toolkit.enabledOnToolkit(32768L)) {
                dispatchEvent(new HierarchyEvent(this, 1400, this, this.parent, 2 | (isRecursivelyVisible() ? 4 : 0)));
            }
        }
    }

    public void removeNotify() throws SecurityException {
        InputContext inputContext;
        KeyboardFocusManager.clearMostRecentFocusOwner(this);
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == this) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalPermanentFocusOwner(null);
        }
        synchronized (getTreeLock()) {
            if (isFocusOwner() && KeyboardFocusManager.isAutoFocusTransferEnabledFor(this)) {
                transferFocus(true);
            }
            if (getContainer() != null && this.isAddNotifyComplete) {
                getContainer().decreaseComponentCount(this);
            }
            int size = this.popups != null ? this.popups.size() : 0;
            for (int i2 = 0; i2 < size; i2++) {
                this.popups.elementAt(i2).removeNotify();
            }
            if ((this.eventMask & 4096) != 0 && (inputContext = getInputContext()) != null) {
                inputContext.removeNotify(this);
            }
            ComponentPeer componentPeer = this.peer;
            if (componentPeer != null) {
                boolean zIsLightweight = isLightweight();
                if (this.bufferStrategy instanceof FlipBufferStrategy) {
                    ((FlipBufferStrategy) this.bufferStrategy).destroyBuffers();
                }
                if (this.dropTarget != null) {
                    this.dropTarget.removeNotify(this.peer);
                }
                if (this.visible) {
                    componentPeer.setVisible(false);
                }
                this.peer = null;
                this.peerFont = null;
                Toolkit.getEventQueue().removeSourceEvents(this, false);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().discardKeyEvents(this);
                componentPeer.dispose();
                mixOnHiding(zIsLightweight);
                this.isAddNotifyComplete = false;
                this.compoundShape = null;
            }
            if (this.hierarchyListener != null || (this.eventMask & 32768) != 0 || Toolkit.enabledOnToolkit(32768L)) {
                dispatchEvent(new HierarchyEvent(this, 1400, this, this.parent, 2 | (isRecursivelyVisible() ? 4 : 0)));
            }
        }
    }

    @Deprecated
    public boolean gotFocus(Event event, Object obj) {
        return false;
    }

    @Deprecated
    public boolean lostFocus(Event event, Object obj) {
        return false;
    }

    @Deprecated
    public boolean isFocusTraversable() {
        if (this.isFocusTraversableOverridden == 0) {
            this.isFocusTraversableOverridden = 1;
        }
        return this.focusable;
    }

    public boolean isFocusable() {
        return isFocusTraversable();
    }

    public void setFocusable(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.focusable;
            this.focusable = z2;
        }
        this.isFocusTraversableOverridden = 2;
        firePropertyChange("focusable", z3, z2);
        if (z3 && !z2) {
            if (isFocusOwner() && KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                transferFocus(true);
            }
            KeyboardFocusManager.clearMostRecentFocusOwner(this);
        }
    }

    final boolean isFocusTraversableOverridden() {
        return this.isFocusTraversableOverridden != 1;
    }

    public void setFocusTraversalKeys(int i2, Set<? extends AWTKeyStroke> set) {
        if (i2 < 0 || i2 >= 3) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        setFocusTraversalKeys_NoIDCheck(i2, set);
    }

    public Set<AWTKeyStroke> getFocusTraversalKeys(int i2) {
        if (i2 < 0 || i2 >= 3) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        return getFocusTraversalKeys_NoIDCheck(i2);
    }

    final void setFocusTraversalKeys_NoIDCheck(int i2, Set<? extends AWTKeyStroke> set) {
        Set<AWTKeyStroke> set2;
        synchronized (this) {
            if (this.focusTraversalKeys == null) {
                initializeFocusTraversalKeys();
            }
            if (set != null) {
                for (AWTKeyStroke aWTKeyStroke : set) {
                    if (aWTKeyStroke == null) {
                        throw new IllegalArgumentException("cannot set null focus traversal key");
                    }
                    if (aWTKeyStroke.getKeyChar() != 65535) {
                        throw new IllegalArgumentException("focus traversal keys cannot map to KEY_TYPED events");
                    }
                    for (int i3 = 0; i3 < this.focusTraversalKeys.length; i3++) {
                        if (i3 != i2 && getFocusTraversalKeys_NoIDCheck(i3).contains(aWTKeyStroke)) {
                            throw new IllegalArgumentException("focus traversal keys must be unique for a Component");
                        }
                    }
                }
            }
            set2 = this.focusTraversalKeys[i2];
            this.focusTraversalKeys[i2] = set != null ? Collections.unmodifiableSet(new HashSet(set)) : null;
        }
        firePropertyChange(focusTraversalKeyPropertyNames[i2], set2, set);
    }

    final Set<AWTKeyStroke> getFocusTraversalKeys_NoIDCheck(int i2) {
        Set<AWTKeyStroke> set = this.focusTraversalKeys != null ? this.focusTraversalKeys[i2] : null;
        if (set != null) {
            return set;
        }
        Container container = this.parent;
        if (container != null) {
            return container.getFocusTraversalKeys(i2);
        }
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(i2);
    }

    public boolean areFocusTraversalKeysSet(int i2) {
        if (i2 < 0 || i2 >= 3) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        return (this.focusTraversalKeys == null || this.focusTraversalKeys[i2] == null) ? false : true;
    }

    public void setFocusTraversalKeysEnabled(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.focusTraversalKeysEnabled;
            this.focusTraversalKeysEnabled = z2;
        }
        firePropertyChange("focusTraversalKeysEnabled", z3, z2);
    }

    public boolean getFocusTraversalKeysEnabled() {
        return this.focusTraversalKeysEnabled;
    }

    public void requestFocus() {
        requestFocusHelper(false, true);
    }

    boolean requestFocus(CausedFocusEvent.Cause cause) {
        return requestFocusHelper(false, true, cause);
    }

    protected boolean requestFocus(boolean z2) {
        return requestFocusHelper(z2, true);
    }

    boolean requestFocus(boolean z2, CausedFocusEvent.Cause cause) {
        return requestFocusHelper(z2, true, cause);
    }

    public boolean requestFocusInWindow() {
        return requestFocusHelper(false, false);
    }

    boolean requestFocusInWindow(CausedFocusEvent.Cause cause) {
        return requestFocusHelper(false, false, cause);
    }

    protected boolean requestFocusInWindow(boolean z2) {
        return requestFocusHelper(z2, false);
    }

    boolean requestFocusInWindow(boolean z2, CausedFocusEvent.Cause cause) {
        return requestFocusHelper(z2, false, cause);
    }

    final boolean requestFocusHelper(boolean z2, boolean z3) {
        return requestFocusHelper(z2, z3, CausedFocusEvent.Cause.UNKNOWN);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a4, code lost:
    
        if ((r8.peer instanceof java.awt.peer.LightweightPeer) == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00a7, code lost:
    
        r0 = getNativeContainer();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ae, code lost:
    
        r0 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00af, code lost:
    
        r15 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00b3, code lost:
    
        if (r15 == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00bb, code lost:
    
        if (r15.isVisible() != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00c7, code lost:
    
        if (java.awt.Component.focusLog.isLoggable(sun.util.logging.PlatformLogger.Level.FINEST) == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ca, code lost:
    
        java.awt.Component.focusLog.finest("Component is not a part of visible hierarchy");
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00d3, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00d5, code lost:
    
        r0 = r15.peer;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00de, code lost:
    
        if (r0 != null) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ea, code lost:
    
        if (java.awt.Component.focusLog.isLoggable(sun.util.logging.PlatformLogger.Level.FINEST) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00ed, code lost:
    
        java.awt.Component.focusLog.finest("Peer is null");
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f6, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00fe, code lost:
    
        if (java.awt.EventQueue.isDispatchThread() == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0101, code lost:
    
        r16 = java.awt.Toolkit.getEventQueue().getMostRecentKeyEventTime();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x010c, code lost:
    
        r16 = java.lang.System.currentTimeMillis();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0111, code lost:
    
        r0 = r0.requestFocus(r8, r9, r10, r16, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0122, code lost:
    
        if (r0 != false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0125, code lost:
    
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager(r8.appContext).dequeueKeyEvents(r16, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x013b, code lost:
    
        if (java.awt.Component.focusLog.isLoggable(sun.util.logging.PlatformLogger.Level.FINEST) == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x013e, code lost:
    
        java.awt.Component.focusLog.finest("Peer request failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0153, code lost:
    
        if (java.awt.Component.focusLog.isLoggable(sun.util.logging.PlatformLogger.Level.FINEST) == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0156, code lost:
    
        java.awt.Component.focusLog.finest("Pass for " + ((java.lang.Object) r8));
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0172, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:?, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final boolean requestFocusHelper(boolean r9, boolean r10, sun.awt.CausedFocusEvent.Cause r11) {
        /*
            Method dump skipped, instructions count: 371
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Component.requestFocusHelper(boolean, boolean, sun.awt.CausedFocusEvent$Cause):boolean");
    }

    private boolean isRequestFocusAccepted(boolean z2, boolean z3, CausedFocusEvent.Cause cause) {
        if (!isFocusable() || !isVisible()) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("Not focusable or not visible");
                return false;
            }
            return false;
        }
        if (this.peer == null) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("peer is null");
                return false;
            }
            return false;
        }
        Window containingWindow = getContainingWindow();
        if (containingWindow == null || !containingWindow.isFocusableWindow()) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("Component doesn't have toplevel");
                return false;
            }
            return false;
        }
        Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(containingWindow);
        if (mostRecentFocusOwner == null) {
            mostRecentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (mostRecentFocusOwner != null && mostRecentFocusOwner.getContainingWindow() != containingWindow) {
                mostRecentFocusOwner = null;
            }
        }
        if (mostRecentFocusOwner == this || mostRecentFocusOwner == null) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("focus owner is null or this");
                return true;
            }
            return true;
        }
        if (CausedFocusEvent.Cause.ACTIVATION == cause) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("cause is activation");
                return true;
            }
            return true;
        }
        boolean zAcceptRequestFocus = requestFocusController.acceptRequestFocus(mostRecentFocusOwner, this, z2, z3, cause);
        if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
            focusLog.finest("RequestFocusController returns {0}", Boolean.valueOf(zAcceptRequestFocus));
        }
        return zAcceptRequestFocus;
    }

    /* loaded from: rt.jar:java/awt/Component$DummyRequestFocusController.class */
    private static class DummyRequestFocusController implements RequestFocusController {
        private DummyRequestFocusController() {
        }

        @Override // sun.awt.RequestFocusController
        public boolean acceptRequestFocus(Component component, Component component2, boolean z2, boolean z3, CausedFocusEvent.Cause cause) {
            return true;
        }
    }

    static synchronized void setRequestFocusController(RequestFocusController requestFocusController2) {
        if (requestFocusController2 == null) {
            requestFocusController = new DummyRequestFocusController();
        } else {
            requestFocusController = requestFocusController2;
        }
    }

    public Container getFocusCycleRootAncestor() {
        Container container;
        Container container2 = this.parent;
        while (true) {
            container = container2;
            if (container == null || container.isFocusCycleRoot()) {
                break;
            }
            container2 = container.parent;
        }
        return container;
    }

    public boolean isFocusCycleRoot(Container container) {
        return getFocusCycleRootAncestor() == container;
    }

    Container getTraversalRoot() {
        return getFocusCycleRootAncestor();
    }

    public void transferFocus() {
        nextFocus();
    }

    @Deprecated
    public void nextFocus() {
        transferFocus(false);
    }

    boolean transferFocus(boolean z2) {
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("clearOnFailure = " + z2);
        }
        Component nextFocusCandidate = getNextFocusCandidate();
        boolean zRequestFocusInWindow = false;
        if (nextFocusCandidate != null && !nextFocusCandidate.isFocusOwner() && nextFocusCandidate != this) {
            zRequestFocusInWindow = nextFocusCandidate.requestFocusInWindow(CausedFocusEvent.Cause.TRAVERSAL_FORWARD);
        }
        if (z2 && !zRequestFocusInWindow) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                focusLog.finer("clear global focus owner");
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwnerPriv();
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("returning result: " + zRequestFocusInWindow);
        }
        return zRequestFocusInWindow;
    }

    final Component getNextFocusCandidate() {
        Applet appletIfAncestorOf;
        Container traversalRoot = getTraversalRoot();
        Container container = this;
        while (traversalRoot != null && (!traversalRoot.isShowing() || !traversalRoot.canBeFocusOwner())) {
            container = traversalRoot;
            traversalRoot = container.getFocusCycleRootAncestor();
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("comp = " + ((Object) container) + ", root = " + ((Object) traversalRoot));
        }
        Component component = null;
        if (traversalRoot != null) {
            FocusTraversalPolicy focusTraversalPolicy = traversalRoot.getFocusTraversalPolicy();
            Component componentAfter = focusTraversalPolicy.getComponentAfter(traversalRoot, container);
            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                focusLog.finer("component after is " + ((Object) componentAfter));
            }
            if (componentAfter == null) {
                componentAfter = focusTraversalPolicy.getDefaultComponent(traversalRoot);
                if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                    focusLog.finer("default component is " + ((Object) componentAfter));
                }
            }
            if (componentAfter == null && (appletIfAncestorOf = EmbeddedFrame.getAppletIfAncestorOf(this)) != null) {
                componentAfter = appletIfAncestorOf;
            }
            component = componentAfter;
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("Focus transfer candidate: " + ((Object) component));
        }
        return component;
    }

    public void transferFocusBackward() {
        transferFocusBackward(false);
    }

    boolean transferFocusBackward(boolean z2) {
        Container traversalRoot = getTraversalRoot();
        Container container = this;
        while (traversalRoot != null && (!traversalRoot.isShowing() || !traversalRoot.canBeFocusOwner())) {
            container = traversalRoot;
            traversalRoot = container.getFocusCycleRootAncestor();
        }
        boolean zRequestFocusInWindow = false;
        if (traversalRoot != null) {
            FocusTraversalPolicy focusTraversalPolicy = traversalRoot.getFocusTraversalPolicy();
            Component componentBefore = focusTraversalPolicy.getComponentBefore(traversalRoot, container);
            if (componentBefore == null) {
                componentBefore = focusTraversalPolicy.getDefaultComponent(traversalRoot);
            }
            if (componentBefore != null) {
                zRequestFocusInWindow = componentBefore.requestFocusInWindow(CausedFocusEvent.Cause.TRAVERSAL_BACKWARD);
            }
        }
        if (z2 && !zRequestFocusInWindow) {
            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                focusLog.finer("clear global focus owner");
            }
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwnerPriv();
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("returning result: " + zRequestFocusInWindow);
        }
        return zRequestFocusInWindow;
    }

    public void transferFocusUpCycle() {
        Container container;
        Component defaultComponent;
        Container focusCycleRootAncestor = getFocusCycleRootAncestor();
        while (true) {
            container = focusCycleRootAncestor;
            if (container == null || (container.isShowing() && container.isFocusable() && container.isEnabled())) {
                break;
            } else {
                focusCycleRootAncestor = container.getFocusCycleRootAncestor();
            }
        }
        if (container != null) {
            Container focusCycleRootAncestor2 = container.getFocusCycleRootAncestor();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRootPriv(focusCycleRootAncestor2 != null ? focusCycleRootAncestor2 : container);
            container.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_UP);
        } else {
            Window containingWindow = getContainingWindow();
            if (containingWindow != null && (defaultComponent = containingWindow.getFocusTraversalPolicy().getDefaultComponent(containingWindow)) != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRootPriv(containingWindow);
                defaultComponent.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_UP);
            }
        }
    }

    public boolean hasFocus() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this;
    }

    public boolean isFocusOwner() {
        return hasFocus();
    }

    void setAutoFocusTransferOnDisposal(boolean z2) {
        this.autoFocusTransferOnDisposal = z2;
    }

    boolean isAutoFocusTransferOnDisposal() {
        return this.autoFocusTransferOnDisposal;
    }

    public void add(PopupMenu popupMenu) {
        synchronized (getTreeLock()) {
            if (popupMenu.parent != null) {
                popupMenu.parent.remove(popupMenu);
            }
            if (this.popups == null) {
                this.popups = new Vector<>();
            }
            this.popups.addElement(popupMenu);
            popupMenu.parent = this;
            if (this.peer != null && popupMenu.peer == null) {
                popupMenu.addNotify();
            }
        }
    }

    @Override // java.awt.MenuContainer
    public void remove(MenuComponent menuComponent) {
        synchronized (getTreeLock()) {
            if (this.popups == null) {
                return;
            }
            int iIndexOf = this.popups.indexOf(menuComponent);
            if (iIndexOf >= 0) {
                PopupMenu popupMenu = (PopupMenu) menuComponent;
                if (popupMenu.peer != null) {
                    popupMenu.removeNotify();
                }
                popupMenu.parent = null;
                this.popups.removeElementAt(iIndexOf);
                if (this.popups.size() == 0) {
                    this.popups = null;
                }
            }
        }
    }

    protected String paramString() {
        return Objects.toString(getName(), "") + ',' + this.f12361x + ',' + this.f12362y + ',' + this.width + 'x' + this.height + (isValid() ? "" : ",invalid") + (this.visible ? "" : ",hidden") + (this.enabled ? "" : ",disabled");
    }

    public String toString() {
        return getClass().getName() + '[' + paramString() + ']';
    }

    public void list() {
        list(System.out, 0);
    }

    public void list(PrintStream printStream) {
        list(printStream, 0);
    }

    public void list(PrintStream printStream, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            printStream.print(" ");
        }
        printStream.println(this);
    }

    public void list(PrintWriter printWriter) {
        list(printWriter, 0);
    }

    public void list(PrintWriter printWriter, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            printWriter.print(" ");
        }
        printWriter.println(this);
    }

    final Container getNativeContainer() {
        Container container;
        Container container2 = getContainer();
        while (true) {
            container = container2;
            if (container == null || !(container.peer instanceof LightweightPeer)) {
                break;
            }
            container2 = container.getContainer();
        }
        return container;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (getObjectLock()) {
            if (propertyChangeListener == null) {
                return;
            }
            if (this.changeSupport == null) {
                this.changeSupport = new PropertyChangeSupport(this);
            }
            this.changeSupport.addPropertyChangeListener(propertyChangeListener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (getObjectLock()) {
            if (propertyChangeListener != null) {
                if (this.changeSupport != null) {
                    this.changeSupport.removePropertyChangeListener(propertyChangeListener);
                }
            }
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        synchronized (getObjectLock()) {
            if (this.changeSupport == null) {
                return new PropertyChangeListener[0];
            }
            return this.changeSupport.getPropertyChangeListeners();
        }
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        synchronized (getObjectLock()) {
            if (propertyChangeListener == null) {
                return;
            }
            if (this.changeSupport == null) {
                this.changeSupport = new PropertyChangeSupport(this);
            }
            this.changeSupport.addPropertyChangeListener(str, propertyChangeListener);
        }
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        synchronized (getObjectLock()) {
            if (propertyChangeListener != null) {
                if (this.changeSupport != null) {
                    this.changeSupport.removePropertyChangeListener(str, propertyChangeListener);
                }
            }
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        synchronized (getObjectLock()) {
            if (this.changeSupport == null) {
                return new PropertyChangeListener[0];
            }
            return this.changeSupport.getPropertyChangeListeners(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void firePropertyChange(String str, Object obj, Object obj2) {
        PropertyChangeSupport propertyChangeSupport;
        synchronized (getObjectLock()) {
            propertyChangeSupport = this.changeSupport;
        }
        if (propertyChangeSupport != null) {
            if (obj != null && obj2 != null && obj.equals(obj2)) {
                return;
            }
            propertyChangeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    protected void firePropertyChange(String str, boolean z2, boolean z3) {
        PropertyChangeSupport propertyChangeSupport = this.changeSupport;
        if (propertyChangeSupport == null || z2 == z3) {
            return;
        }
        propertyChangeSupport.firePropertyChange(str, z2, z3);
    }

    protected void firePropertyChange(String str, int i2, int i3) {
        PropertyChangeSupport propertyChangeSupport = this.changeSupport;
        if (propertyChangeSupport == null || i2 == i3) {
            return;
        }
        propertyChangeSupport.firePropertyChange(str, i2, i3);
    }

    public void firePropertyChange(String str, byte b2, byte b3) {
        if (this.changeSupport == null || b2 == b3) {
            return;
        }
        firePropertyChange(str, Byte.valueOf(b2), Byte.valueOf(b3));
    }

    public void firePropertyChange(String str, char c2, char c3) {
        if (this.changeSupport == null || c2 == c3) {
            return;
        }
        firePropertyChange(str, new Character(c2), new Character(c3));
    }

    public void firePropertyChange(String str, short s2, short s3) {
        if (this.changeSupport == null || s2 == s3) {
            return;
        }
        firePropertyChange(str, Short.valueOf(s2), Short.valueOf(s3));
    }

    public void firePropertyChange(String str, long j2, long j3) {
        if (this.changeSupport == null || j2 == j3) {
            return;
        }
        firePropertyChange(str, Long.valueOf(j2), Long.valueOf(j3));
    }

    public void firePropertyChange(String str, float f2, float f3) {
        if (this.changeSupport == null || f2 == f3) {
            return;
        }
        firePropertyChange(str, Float.valueOf(f2), Float.valueOf(f3));
    }

    public void firePropertyChange(String str, double d2, double d3) {
        if (this.changeSupport == null || d2 == d3) {
            return;
        }
        firePropertyChange(str, Double.valueOf(d2), Double.valueOf(d3));
    }

    private void doSwingSerialization() throws IllegalArgumentException {
        Package r0 = Package.getPackage("javax.swing");
        Class<?> superclass = getClass();
        while (true) {
            final Class<?> cls = superclass;
            if (cls != null) {
                if (cls.getPackage() == r0 && cls.getClassLoader() == null) {
                    Method[] methodArr = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: java.awt.Component.4
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Method[] run2() {
                            return cls.getDeclaredMethods();
                        }
                    });
                    for (int length = methodArr.length - 1; length >= 0; length--) {
                        final Method method = methodArr[length];
                        if (method.getName().equals("compWriteObjectNotify")) {
                            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.Component.5
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedAction
                                /* renamed from: run */
                                public Void run2() {
                                    method.setAccessible(true);
                                    return null;
                                }
                            });
                            try {
                                method.invoke(this, (Object[]) null);
                                return;
                            } catch (IllegalAccessException e2) {
                                return;
                            } catch (InvocationTargetException e3) {
                                return;
                            }
                        }
                    }
                }
                superclass = cls.getSuperclass();
            } else {
                return;
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, IllegalArgumentException {
        doSwingSerialization();
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, componentListenerK, this.componentListener);
        AWTEventMulticaster.save(objectOutputStream, focusListenerK, this.focusListener);
        AWTEventMulticaster.save(objectOutputStream, keyListenerK, this.keyListener);
        AWTEventMulticaster.save(objectOutputStream, mouseListenerK, this.mouseListener);
        AWTEventMulticaster.save(objectOutputStream, mouseMotionListenerK, this.mouseMotionListener);
        AWTEventMulticaster.save(objectOutputStream, inputMethodListenerK, this.inputMethodListener);
        objectOutputStream.writeObject(null);
        objectOutputStream.writeObject(this.componentOrientation);
        AWTEventMulticaster.save(objectOutputStream, hierarchyListenerK, this.hierarchyListener);
        AWTEventMulticaster.save(objectOutputStream, hierarchyBoundsListenerK, this.hierarchyBoundsListener);
        objectOutputStream.writeObject(null);
        AWTEventMulticaster.save(objectOutputStream, mouseWheelListenerK, this.mouseWheelListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.objectLock = new Object();
        this.acc = AccessController.getContext();
        objectInputStream.defaultReadObject();
        this.appContext = AppContext.getAppContext();
        this.coalescingEnabled = checkCoalescing();
        if (this.componentSerializedDataVersion < 4) {
            this.focusable = true;
            this.isFocusTraversableOverridden = 0;
            initializeFocusTraversalKeys();
            this.focusTraversalKeysEnabled = true;
        }
        while (true) {
            Object object = objectInputStream.readObject();
            if (null == object) {
                break;
            }
            String strIntern = ((String) object).intern();
            if (componentListenerK == strIntern) {
                addComponentListener((ComponentListener) objectInputStream.readObject());
            } else if (focusListenerK == strIntern) {
                addFocusListener((FocusListener) objectInputStream.readObject());
            } else if (keyListenerK == strIntern) {
                addKeyListener((KeyListener) objectInputStream.readObject());
            } else if (mouseListenerK == strIntern) {
                addMouseListener((MouseListener) objectInputStream.readObject());
            } else if (mouseMotionListenerK == strIntern) {
                addMouseMotionListener((MouseMotionListener) objectInputStream.readObject());
            } else if (inputMethodListenerK == strIntern) {
                addInputMethodListener((InputMethodListener) objectInputStream.readObject());
            } else {
                objectInputStream.readObject();
            }
        }
        Object object2 = null;
        try {
            object2 = objectInputStream.readObject();
        } catch (OptionalDataException e2) {
            if (!e2.eof) {
                throw e2;
            }
        }
        if (object2 != null) {
            this.componentOrientation = (ComponentOrientation) object2;
        } else {
            this.componentOrientation = ComponentOrientation.UNKNOWN;
        }
        while (true) {
            try {
                Object object3 = objectInputStream.readObject();
                if (null == object3) {
                    break;
                }
                String strIntern2 = ((String) object3).intern();
                if (hierarchyListenerK == strIntern2) {
                    addHierarchyListener((HierarchyListener) objectInputStream.readObject());
                } else if (hierarchyBoundsListenerK == strIntern2) {
                    addHierarchyBoundsListener((HierarchyBoundsListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } catch (OptionalDataException e3) {
                if (!e3.eof) {
                    throw e3;
                }
            }
        }
        while (true) {
            try {
                Object object4 = objectInputStream.readObject();
                if (null == object4) {
                    break;
                } else if (mouseWheelListenerK == ((String) object4).intern()) {
                    addMouseWheelListener((MouseWheelListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } catch (OptionalDataException e4) {
                if (!e4.eof) {
                    throw e4;
                }
            }
        }
        if (this.popups != null) {
            int size = this.popups.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.popups.elementAt(i2).parent = this;
            }
        }
    }

    public void setComponentOrientation(ComponentOrientation componentOrientation) {
        ComponentOrientation componentOrientation2 = this.componentOrientation;
        this.componentOrientation = componentOrientation;
        firePropertyChange("componentOrientation", componentOrientation2, componentOrientation);
        invalidateIfValid();
    }

    public ComponentOrientation getComponentOrientation() {
        return this.componentOrientation;
    }

    public void applyComponentOrientation(ComponentOrientation componentOrientation) {
        if (componentOrientation == null) {
            throw new NullPointerException();
        }
        setComponentOrientation(componentOrientation);
    }

    final boolean canBeFocusOwner() {
        if (isEnabled() && isDisplayable() && isVisible() && isFocusable()) {
            return true;
        }
        return false;
    }

    final boolean canBeFocusOwnerRecursively() {
        if (!canBeFocusOwner()) {
            return false;
        }
        synchronized (getTreeLock()) {
            if (this.parent != null) {
                return this.parent.canContainFocusOwner(this);
            }
            return true;
        }
    }

    final void relocateComponent() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                return;
            }
            int i2 = this.f12361x;
            int i3 = this.f12362y;
            for (Container container = getContainer(); container != null && container.isLightweight(); container = container.getContainer()) {
                i2 += container.f12361x;
                i3 += container.f12362y;
            }
            this.peer.setBounds(i2, i3, this.width, this.height, 1);
        }
    }

    Window getContainingWindow() {
        return SunToolkit.getContainingWindow(this);
    }

    public AccessibleContext getAccessibleContext() {
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Component$AccessibleAWTComponent.class */
    public abstract class AccessibleAWTComponent extends AccessibleContext implements Serializable, AccessibleComponent {
        private static final long serialVersionUID = 642321655757800191L;
        private volatile transient int propertyListenersCount = 0;
        protected ComponentListener accessibleAWTComponentHandler = null;
        protected FocusListener accessibleAWTFocusHandler = null;

        protected AccessibleAWTComponent() {
        }

        /* loaded from: rt.jar:java/awt/Component$AccessibleAWTComponent$AccessibleAWTComponentHandler.class */
        protected class AccessibleAWTComponentHandler implements ComponentListener {
            protected AccessibleAWTComponentHandler() {
            }

            @Override // java.awt.event.ComponentListener
            public void componentHidden(ComponentEvent componentEvent) {
                if (Component.this.accessibleContext != null) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.VISIBLE, null);
                }
            }

            @Override // java.awt.event.ComponentListener
            public void componentShown(ComponentEvent componentEvent) {
                if (Component.this.accessibleContext != null) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.VISIBLE);
                }
            }

            @Override // java.awt.event.ComponentListener
            public void componentMoved(ComponentEvent componentEvent) {
            }

            @Override // java.awt.event.ComponentListener
            public void componentResized(ComponentEvent componentEvent) {
            }
        }

        /* loaded from: rt.jar:java/awt/Component$AccessibleAWTComponent$AccessibleAWTFocusHandler.class */
        protected class AccessibleAWTFocusHandler implements FocusListener {
            protected AccessibleAWTFocusHandler() {
            }

            @Override // java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                if (Component.this.accessibleContext != null) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED);
                }
            }

            @Override // java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) {
                if (Component.this.accessibleContext != null) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.FOCUSED, null);
                }
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            if (this.accessibleAWTComponentHandler == null) {
                this.accessibleAWTComponentHandler = new AccessibleAWTComponentHandler();
            }
            if (this.accessibleAWTFocusHandler == null) {
                this.accessibleAWTFocusHandler = new AccessibleAWTFocusHandler();
            }
            int i2 = this.propertyListenersCount;
            this.propertyListenersCount = i2 + 1;
            if (i2 == 0) {
                Component.this.addComponentListener(this.accessibleAWTComponentHandler);
                Component.this.addFocusListener(this.accessibleAWTFocusHandler);
            }
            super.addPropertyChangeListener(propertyChangeListener);
        }

        @Override // javax.accessibility.AccessibleContext
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            int i2 = this.propertyListenersCount - 1;
            this.propertyListenersCount = i2;
            if (i2 == 0) {
                Component.this.removeComponentListener(this.accessibleAWTComponentHandler);
                Component.this.removeFocusListener(this.accessibleAWTFocusHandler);
            }
            super.removePropertyChangeListener(propertyChangeListener);
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            return this.accessibleName;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            return this.accessibleDescription;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.AWT_COMPONENT;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            return Component.this.getAccessibleStateSet();
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleParent() {
            if (this.accessibleParent != null) {
                return this.accessibleParent;
            }
            MenuContainer parent = Component.this.getParent();
            if (parent instanceof Accessible) {
                return (Accessible) parent;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return Component.this.getAccessibleIndexInParent();
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() {
            return Component.this.getLocale();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            return this;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getBackground() {
            return Component.this.getBackground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBackground(Color color) {
            Component.this.setBackground(color);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getForeground() {
            return Component.this.getForeground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setForeground(Color color) {
            Component.this.setForeground(color);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Cursor getCursor() {
            return Component.this.getCursor();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setCursor(Cursor cursor) {
            Component.this.setCursor(cursor);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Font getFont() {
            return Component.this.getFont();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setFont(Font font) {
            Component.this.setFont(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public FontMetrics getFontMetrics(Font font) {
            if (font == null) {
                return null;
            }
            return Component.this.getFontMetrics(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isEnabled() {
            return Component.this.isEnabled();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setEnabled(boolean z2) {
            boolean zIsEnabled = Component.this.isEnabled();
            Component.this.setEnabled(z2);
            if (z2 != zIsEnabled && Component.this.accessibleContext != null) {
                if (z2) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.ENABLED);
                } else {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.ENABLED, null);
                }
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isVisible() {
            return Component.this.isVisible();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setVisible(boolean z2) {
            boolean zIsVisible = Component.this.isVisible();
            Component.this.setVisible(z2);
            if (z2 != zIsVisible && Component.this.accessibleContext != null) {
                if (z2) {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.VISIBLE);
                } else {
                    Component.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.VISIBLE, null);
                }
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isShowing() {
            return Component.this.isShowing();
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean contains(Point point) {
            return Component.this.contains(point);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocationOnScreen() {
            synchronized (Component.this.getTreeLock()) {
                if (Component.this.isShowing()) {
                    return Component.this.getLocationOnScreen();
                }
                return null;
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocation() {
            return Component.this.getLocation();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setLocation(Point point) {
            Component.this.setLocation(point);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Rectangle getBounds() {
            return Component.this.getBounds();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBounds(Rectangle rectangle) {
            Component.this.setBounds(rectangle);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Dimension getSize() {
            return Component.this.getSize();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setSize(Dimension dimension) {
            Component.this.setSize(dimension);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isFocusTraversable() {
            return Component.this.isFocusTraversable();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void requestFocus() {
            Component.this.requestFocus();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void addFocusListener(FocusListener focusListener) {
            Component.this.addFocusListener(focusListener);
        }

        @Override // javax.accessibility.AccessibleComponent
        public void removeFocusListener(FocusListener focusListener) {
            Component.this.removeFocusListener(focusListener);
        }
    }

    int getAccessibleIndexInParent() {
        synchronized (getTreeLock()) {
            int i2 = -1;
            Container parent = getParent();
            if (parent != null && (parent instanceof Accessible)) {
                Component[] components = parent.getComponents();
                for (int i3 = 0; i3 < components.length; i3++) {
                    if (components[i3] instanceof Accessible) {
                        i2++;
                    }
                    if (equals(components[i3])) {
                        return i2;
                    }
                }
            }
            return -1;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    AccessibleStateSet getAccessibleStateSet() {
        AccessibleStateSet accessibleStateSet;
        AccessibleContext accessibleContext;
        Accessible accessibleParent;
        AccessibleContext accessibleContext2;
        AccessibleSelection accessibleSelection;
        synchronized (getTreeLock()) {
            accessibleStateSet = new AccessibleStateSet();
            if (isEnabled()) {
                accessibleStateSet.add(AccessibleState.ENABLED);
            }
            if (isFocusTraversable()) {
                accessibleStateSet.add(AccessibleState.FOCUSABLE);
            }
            if (isVisible()) {
                accessibleStateSet.add(AccessibleState.VISIBLE);
            }
            if (isShowing()) {
                accessibleStateSet.add(AccessibleState.SHOWING);
            }
            if (isFocusOwner()) {
                accessibleStateSet.add(AccessibleState.FOCUSED);
            }
            if ((this instanceof Accessible) && (accessibleContext = ((Accessible) this).getAccessibleContext()) != null && (accessibleParent = accessibleContext.getAccessibleParent()) != null && (accessibleContext2 = accessibleParent.getAccessibleContext()) != null && (accessibleSelection = accessibleContext2.getAccessibleSelection()) != null) {
                accessibleStateSet.add(AccessibleState.SELECTABLE);
                int accessibleIndexInParent = accessibleContext.getAccessibleIndexInParent();
                if (accessibleIndexInParent >= 0 && accessibleSelection.isAccessibleChildSelected(accessibleIndexInParent)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
                }
            }
            if (isInstanceOf(this, ServiceUIFactory.JCOMPONENT_UI) && ((JComponent) this).isOpaque()) {
                accessibleStateSet.add(AccessibleState.OPAQUE);
            }
        }
        return accessibleStateSet;
    }

    static boolean isInstanceOf(Object obj, String str) {
        if (obj == null || str == null) {
            return false;
        }
        Class<?> superclass = obj.getClass();
        while (true) {
            Class<?> cls = superclass;
            if (cls != null) {
                if (cls.getName().equals(str)) {
                    return true;
                }
                superclass = cls.getSuperclass();
            } else {
                return false;
            }
        }
    }

    final boolean areBoundsValid() {
        Container container = getContainer();
        return container == null || container.isValid() || container.getLayout() == null;
    }

    void applyCompoundShape(Region region) {
        ComponentPeer peer;
        checkTreeLock();
        if (!areBoundsValid()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; areBoundsValid = " + areBoundsValid());
                return;
            }
            return;
        }
        if (!isLightweight() && (peer = getPeer()) != null) {
            if (region.isEmpty()) {
                region = Region.EMPTY_REGION;
            }
            if (region.equals(getNormalShape())) {
                if (this.compoundShape == null) {
                    return;
                }
                this.compoundShape = null;
                peer.applyShape(null);
                return;
            }
            if (region.equals(getAppliedShape())) {
                return;
            }
            this.compoundShape = region;
            Point locationOnWindow = getLocationOnWindow();
            if (mixingLog.isLoggable(PlatformLogger.Level.FINER)) {
                mixingLog.fine("this = " + ((Object) this) + "; compAbsolute=" + ((Object) locationOnWindow) + "; shape=" + ((Object) region));
            }
            peer.applyShape(region.getTranslatedRegion(-locationOnWindow.f12370x, -locationOnWindow.f12371y));
        }
    }

    private Region getAppliedShape() {
        checkTreeLock();
        return (this.compoundShape == null || isLightweight()) ? getNormalShape() : this.compoundShape;
    }

    Point getLocationOnWindow() {
        checkTreeLock();
        Point location = getLocation();
        Container container = getContainer();
        while (true) {
            Container container2 = container;
            if (container2 == null || (container2 instanceof Window)) {
                break;
            }
            location.f12370x += container2.getX();
            location.f12371y += container2.getY();
            container = container2.getContainer();
        }
        return location;
    }

    final Region getNormalShape() {
        checkTreeLock();
        Point locationOnWindow = getLocationOnWindow();
        return Region.getInstanceXYWH(locationOnWindow.f12370x, locationOnWindow.f12371y, getWidth(), getHeight());
    }

    Region getOpaqueShape() {
        checkTreeLock();
        if (this.mixingCutoutRegion != null) {
            return this.mixingCutoutRegion;
        }
        return getNormalShape();
    }

    final int getSiblingIndexAbove() {
        int componentZOrder;
        checkTreeLock();
        Container container = getContainer();
        if (container != null && (componentZOrder = container.getComponentZOrder(this) - 1) >= 0) {
            return componentZOrder;
        }
        return -1;
    }

    final ComponentPeer getHWPeerAboveMe() {
        checkTreeLock();
        int siblingIndexAbove = getSiblingIndexAbove();
        for (Container container = getContainer(); container != null; container = container.getContainer()) {
            for (int i2 = siblingIndexAbove; i2 > -1; i2--) {
                Component component = container.getComponent(i2);
                if (component != null && component.isDisplayable() && !component.isLightweight()) {
                    return component.getPeer();
                }
            }
            if (container.isLightweight()) {
                siblingIndexAbove = container.getSiblingIndexAbove();
            } else {
                return null;
            }
        }
        return null;
    }

    final int getSiblingIndexBelow() {
        int componentZOrder;
        checkTreeLock();
        Container container = getContainer();
        if (container != null && (componentZOrder = container.getComponentZOrder(this) + 1) < container.getComponentCount()) {
            return componentZOrder;
        }
        return -1;
    }

    final boolean isNonOpaqueForMixing() {
        return this.mixingCutoutRegion != null && this.mixingCutoutRegion.isEmpty();
    }

    private Region calculateCurrentShape() {
        checkTreeLock();
        Region normalShape = getNormalShape();
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("this = " + ((Object) this) + "; normalShape=" + ((Object) normalShape));
        }
        if (getContainer() != null) {
            Component component = this;
            Container container = component.getContainer();
            while (true) {
                Container container2 = container;
                if (container2 == null) {
                    break;
                }
                for (int siblingIndexAbove = component.getSiblingIndexAbove(); siblingIndexAbove != -1; siblingIndexAbove--) {
                    Component component2 = container2.getComponent(siblingIndexAbove);
                    if (component2.isLightweight() && component2.isShowing()) {
                        normalShape = normalShape.getDifference(component2.getOpaqueShape());
                    }
                }
                if (!container2.isLightweight()) {
                    break;
                }
                normalShape = normalShape.getIntersection(container2.getNormalShape());
                component = container2;
                container = container2.getContainer();
            }
        }
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("currentShape=" + ((Object) normalShape));
        }
        return normalShape;
    }

    void applyCurrentShape() {
        checkTreeLock();
        if (!areBoundsValid()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; areBoundsValid = " + areBoundsValid());
            }
        } else {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            applyCompoundShape(calculateCurrentShape());
        }
    }

    final void subtractAndApplyShape(Region region) {
        checkTreeLock();
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("this = " + ((Object) this) + "; s=" + ((Object) region));
        }
        applyCompoundShape(getAppliedShape().getDifference(region));
    }

    private final void applyCurrentShapeBelowMe() {
        checkTreeLock();
        Container container = getContainer();
        if (container != null && container.isShowing()) {
            container.recursiveApplyCurrentShape(getSiblingIndexBelow());
            Container container2 = container.getContainer();
            while (true) {
                Container container3 = container2;
                if (!container.isOpaque() && container3 != null) {
                    container3.recursiveApplyCurrentShape(container.getSiblingIndexBelow());
                    container = container3;
                    container2 = container.getContainer();
                } else {
                    return;
                }
            }
        }
    }

    final void subtractAndApplyShapeBelowMe() {
        checkTreeLock();
        Container container = getContainer();
        if (container != null && isShowing()) {
            Region opaqueShape = getOpaqueShape();
            container.recursiveSubtractAndApplyShape(opaqueShape, getSiblingIndexBelow());
            Container container2 = container.getContainer();
            while (true) {
                Container container3 = container2;
                if (!container.isOpaque() && container3 != null) {
                    container3.recursiveSubtractAndApplyShape(opaqueShape, container.getSiblingIndexBelow());
                    container = container3;
                    container2 = container.getContainer();
                } else {
                    return;
                }
            }
        }
    }

    void mixOnShowing() {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            if (isMixingNeeded()) {
                if (isLightweight()) {
                    subtractAndApplyShapeBelowMe();
                } else {
                    applyCurrentShape();
                }
            }
        }
    }

    void mixOnHiding(boolean z2) {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; isLightweight = " + z2);
            }
            if (isMixingNeeded()) {
                if (z2) {
                    applyCurrentShapeBelowMe();
                }
            }
        }
    }

    void mixOnReshaping() {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            if (isMixingNeeded()) {
                if (isLightweight()) {
                    applyCurrentShapeBelowMe();
                } else {
                    applyCurrentShape();
                }
            }
        }
    }

    void mixOnZOrderChanging(int i2, int i3) {
        synchronized (getTreeLock()) {
            boolean z2 = i3 < i2;
            Container container = getContainer();
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; oldZorder=" + i2 + "; newZorder=" + i3 + "; parent=" + ((Object) container));
            }
            if (isMixingNeeded()) {
                if (isLightweight()) {
                    if (z2) {
                        if (container != null && isShowing()) {
                            container.recursiveSubtractAndApplyShape(getOpaqueShape(), getSiblingIndexBelow(), i2);
                        }
                    } else if (container != null) {
                        container.recursiveApplyCurrentShape(i2, i3);
                    }
                } else if (z2) {
                    applyCurrentShape();
                } else if (container != null) {
                    Region appliedShape = getAppliedShape();
                    for (int i4 = i2; i4 < i3; i4++) {
                        Component component = container.getComponent(i4);
                        if (component.isLightweight() && component.isShowing()) {
                            appliedShape = appliedShape.getDifference(component.getOpaqueShape());
                        }
                    }
                    applyCompoundShape(appliedShape);
                }
            }
        }
    }

    void mixOnValidating() {
    }

    final boolean isMixingNeeded() {
        if (SunToolkit.getSunAwtDisableMixing()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINEST)) {
                mixingLog.finest("this = " + ((Object) this) + "; Mixing disabled via sun.awt.disableMixing");
                return false;
            }
            return false;
        }
        if (!areBoundsValid()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; areBoundsValid = " + areBoundsValid());
                return false;
            }
            return false;
        }
        Window containingWindow = getContainingWindow();
        if (containingWindow != null) {
            if (!containingWindow.hasHeavyweightDescendants() || !containingWindow.hasLightweightDescendants() || containingWindow.isDisposing()) {
                if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                    mixingLog.fine("containing window = " + ((Object) containingWindow) + "; has h/w descendants = " + containingWindow.hasHeavyweightDescendants() + "; has l/w descendants = " + containingWindow.hasLightweightDescendants() + "; disposing = " + containingWindow.isDisposing());
                    return false;
                }
                return false;
            }
            return true;
        }
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("this = " + ((Object) this) + "; containing window is null");
            return false;
        }
        return false;
    }

    void updateZOrder() {
        this.peer.setZOrder(getHWPeerAboveMe());
    }
}
