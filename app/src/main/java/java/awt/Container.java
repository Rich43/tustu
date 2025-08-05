package java.awt;

import java.awt.Component;
import java.awt.GraphicsCallback;
import java.awt.dnd.DropTarget;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.OptionalDataException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.swing.JInternalFrame;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;
import sun.java2d.pipe.Region;
import sun.security.action.GetBooleanAction;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/Container.class */
public class Container extends Component {
    LayoutManager layoutMgr;
    private LightweightDispatcher dispatcher;
    private transient FocusTraversalPolicy focusTraversalPolicy;
    private boolean focusTraversalPolicyProvider;
    private transient Set<Thread> printingThreads;
    transient ContainerListener containerListener;
    transient int listeningChildren;
    transient int listeningBoundsChildren;
    transient int descendantsCount;
    private static final long serialVersionUID = 4613797578919906343L;
    static final boolean INCLUDE_SELF = true;
    static final boolean SEARCH_HEAVYWEIGHTS = true;
    private static final boolean isJavaAwtSmartInvalidate;
    private static boolean descendUnconditionallyWhenValidating;
    transient Component modalComp;
    transient AppContext modalAppContext;
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Container");
    private static final PlatformLogger eventLog = PlatformLogger.getLogger("java.awt.event.Container");
    private static final Component[] EMPTY_ARRAY = new Component[0];
    private static final PlatformLogger mixingLog = PlatformLogger.getLogger("java.awt.mixing.Container");
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("ncomponents", Integer.TYPE), new ObjectStreamField("component", Component[].class), new ObjectStreamField("layoutMgr", LayoutManager.class), new ObjectStreamField("dispatcher", LightweightDispatcher.class), new ObjectStreamField("maxSize", Dimension.class), new ObjectStreamField("focusCycleRoot", Boolean.TYPE), new ObjectStreamField("containerSerializedDataVersion", Integer.TYPE), new ObjectStreamField("focusTraversalPolicyProvider", Boolean.TYPE)};
    private java.util.List<Component> component = new ArrayList();
    private boolean focusCycleRoot = false;
    private transient boolean printing = false;
    transient Color preserveBackgroundColor = null;
    private transient int numOfHWComponents = 0;
    private transient int numOfLWComponents = 0;
    private int containerSerializedDataVersion = 1;

    /* loaded from: rt.jar:java/awt/Container$EventTargetFilter.class */
    interface EventTargetFilter {
        boolean accept(Component component);
    }

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setContainerAccessor(new AWTAccessor.ContainerAccessor() { // from class: java.awt.Container.1
            @Override // sun.awt.AWTAccessor.ContainerAccessor
            public void validateUnconditionally(Container container) {
                container.validateUnconditionally();
            }

            @Override // sun.awt.AWTAccessor.ContainerAccessor
            public Component findComponentAt(Container container, int i2, int i3, boolean z2) {
                return container.findComponentAt(i2, i3, z2);
            }
        });
        isJavaAwtSmartInvalidate = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("java.awt.smartInvalidate"))).booleanValue();
        descendUnconditionallyWhenValidating = false;
    }

    @Override // java.awt.Component
    void initializeFocusTraversalKeys() {
        this.focusTraversalKeys = new Set[4];
    }

    public int getComponentCount() {
        return countComponents();
    }

    @Deprecated
    public int countComponents() {
        return this.component.size();
    }

    public Component getComponent(int i2) {
        try {
            return this.component.get(i2);
        } catch (IndexOutOfBoundsException e2) {
            throw new ArrayIndexOutOfBoundsException("No such child: " + i2);
        }
    }

    public Component[] getComponents() {
        return getComponents_NoClientCode();
    }

    final Component[] getComponents_NoClientCode() {
        return (Component[]) this.component.toArray(EMPTY_ARRAY);
    }

    Component[] getComponentsSync() {
        Component[] components;
        synchronized (getTreeLock()) {
            components = getComponents();
        }
        return components;
    }

    public Insets getInsets() {
        return insets();
    }

    @Deprecated
    public Insets insets() {
        ComponentPeer componentPeer = this.peer;
        if (componentPeer instanceof ContainerPeer) {
            return (Insets) ((ContainerPeer) componentPeer).getInsets().clone();
        }
        return new Insets(0, 0, 0, 0);
    }

    public Component add(Component component) {
        addImpl(component, null, -1);
        return component;
    }

    public Component add(String str, Component component) {
        addImpl(component, str, -1);
        return component;
    }

    public Component add(Component component, int i2) {
        addImpl(component, null, i2);
        return component;
    }

    private void checkAddToSelf(Component component) {
        if (component instanceof Container) {
            Container container = this;
            while (true) {
                Container container2 = container;
                if (container2 != null) {
                    if (container2 != component) {
                        container = container2.parent;
                    } else {
                        throw new IllegalArgumentException("adding container's parent to itself");
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void checkNotAWindow(Component component) {
        if (component instanceof Window) {
            throw new IllegalArgumentException("adding a window to a container");
        }
    }

    private void checkAdding(Component component, int i2) {
        checkTreeLock();
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        if (i2 > this.component.size() || i2 < 0) {
            throw new IllegalArgumentException("illegal component position");
        }
        if (component.parent == this && i2 == this.component.size()) {
            throw new IllegalArgumentException("illegal component position " + i2 + " should be less then " + this.component.size());
        }
        checkAddToSelf(component);
        checkNotAWindow(component);
        if (getContainingWindow() != component.getContainingWindow()) {
            throw new IllegalArgumentException("component and container should be in the same top-level window");
        }
        if (graphicsConfiguration != null) {
            component.checkGD(graphicsConfiguration.getDevice().getIDstring());
        }
    }

    private boolean removeDelicately(Component component, Container container, int i2) throws SecurityException {
        checkTreeLock();
        int componentZOrder = getComponentZOrder(component);
        boolean zIsRemoveNotifyNeeded = isRemoveNotifyNeeded(component, this, container);
        if (zIsRemoveNotifyNeeded) {
            component.removeNotify();
        }
        if (container != this) {
            if (this.layoutMgr != null) {
                this.layoutMgr.removeLayoutComponent(component);
            }
            adjustListeningChildren(32768L, -component.numListening(32768L));
            adjustListeningChildren(65536L, -component.numListening(65536L));
            adjustDescendants(-component.countHierarchyMembers());
            component.parent = null;
            if (zIsRemoveNotifyNeeded) {
                component.setGraphicsConfiguration(null);
            }
            this.component.remove(componentZOrder);
            invalidateIfValid();
        } else {
            this.component.remove(componentZOrder);
            this.component.add(i2, component);
        }
        if (component.parent == null) {
            if (this.containerListener != null || (this.eventMask & 2) != 0 || Toolkit.enabledOnToolkit(2L)) {
                dispatchEvent(new ContainerEvent(this, 301, component));
            }
            component.createHierarchyEvents(1400, component, this, 1L, Toolkit.enabledOnToolkit(32768L));
            if (this.peer != null && this.layoutMgr == null && isVisible()) {
                updateCursorImmediately();
            }
        }
        return zIsRemoveNotifyNeeded;
    }

    boolean canContainFocusOwner(Component component) {
        if (!isEnabled() || !isDisplayable() || !isVisible() || !isFocusable()) {
            return false;
        }
        if (isFocusCycleRoot()) {
            FocusTraversalPolicy focusTraversalPolicy = getFocusTraversalPolicy();
            if ((focusTraversalPolicy instanceof DefaultFocusTraversalPolicy) && !((DefaultFocusTraversalPolicy) focusTraversalPolicy).accept(component)) {
                return false;
            }
        }
        synchronized (getTreeLock()) {
            if (this.parent != null) {
                return this.parent.canContainFocusOwner(component);
            }
            return true;
        }
    }

    final boolean hasHeavyweightDescendants() {
        checkTreeLock();
        return this.numOfHWComponents > 0;
    }

    final boolean hasLightweightDescendants() {
        checkTreeLock();
        return this.numOfLWComponents > 0;
    }

    Container getHeavyweightContainer() {
        checkTreeLock();
        if (this.peer != null && !(this.peer instanceof LightweightPeer)) {
            return this;
        }
        return getNativeContainer();
    }

    private static boolean isRemoveNotifyNeeded(Component component, Container container, Container container2) {
        if (container == null || component.peer == null) {
            return false;
        }
        if (container2.peer == null) {
            return true;
        }
        if (component.isLightweight()) {
            boolean z2 = component instanceof Container;
            if (!z2) {
                return false;
            }
            if (z2 && !((Container) component).hasHeavyweightDescendants()) {
                return false;
            }
        }
        return (container.getHeavyweightContainer() == container2.getHeavyweightContainer() || component.peer.isReparentSupported()) ? false : true;
    }

    public void setComponentZOrder(Component component, int i2) {
        synchronized (getTreeLock()) {
            Container container = component.parent;
            int componentZOrder = getComponentZOrder(component);
            if (container == this && i2 == componentZOrder) {
                return;
            }
            checkAdding(component, i2);
            boolean zRemoveDelicately = container != null ? container.removeDelicately(component, this, i2) : false;
            addDelicately(component, container, i2);
            if (!zRemoveDelicately && componentZOrder != -1) {
                component.mixOnZOrderChanging(componentZOrder, i2);
            }
        }
    }

    private void reparentTraverse(ContainerPeer containerPeer, Container container) {
        checkTreeLock();
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            Component component = container.getComponent(i2);
            if (component.isLightweight()) {
                if (component instanceof Container) {
                    reparentTraverse(containerPeer, (Container) component);
                }
            } else {
                component.getPeer().reparent(containerPeer);
            }
        }
    }

    private void reparentChild(Component component) {
        checkTreeLock();
        if (component == null) {
            return;
        }
        if (component.isLightweight()) {
            if (component instanceof Container) {
                reparentTraverse((ContainerPeer) getPeer(), (Container) component);
                return;
            }
            return;
        }
        component.getPeer().reparent((ContainerPeer) getPeer());
    }

    private void addDelicately(Component component, Container container, int i2) {
        Component focusOwner;
        checkTreeLock();
        if (container != this) {
            if (i2 == -1) {
                this.component.add(component);
            } else {
                this.component.add(i2, component);
            }
            component.parent = this;
            component.setGraphicsConfiguration(getGraphicsConfiguration());
            adjustListeningChildren(32768L, component.numListening(32768L));
            adjustListeningChildren(65536L, component.numListening(65536L));
            adjustDescendants(component.countHierarchyMembers());
        } else if (i2 < this.component.size()) {
            this.component.set(i2, component);
        }
        invalidateIfValid();
        if (this.peer != null) {
            if (component.peer == null) {
                component.addNotify();
            } else {
                Container heavyweightContainer = getHeavyweightContainer();
                if (container.getHeavyweightContainer() != heavyweightContainer) {
                    heavyweightContainer.reparentChild(component);
                }
                component.updateZOrder();
                if (!component.isLightweight() && isLightweight()) {
                    component.relocateComponent();
                }
            }
        }
        if (container != this) {
            if (this.layoutMgr != null) {
                if (this.layoutMgr instanceof LayoutManager2) {
                    ((LayoutManager2) this.layoutMgr).addLayoutComponent(component, (Object) null);
                } else {
                    this.layoutMgr.addLayoutComponent(null, component);
                }
            }
            if (this.containerListener != null || (this.eventMask & 2) != 0 || Toolkit.enabledOnToolkit(2L)) {
                dispatchEvent(new ContainerEvent(this, 300, component));
            }
            component.createHierarchyEvents(1400, component, this, 1L, Toolkit.enabledOnToolkit(32768L));
            if (component.isFocusOwner() && !component.canBeFocusOwnerRecursively()) {
                component.transferFocus();
            } else if ((component instanceof Container) && (focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) != null && isParentOf(focusOwner) && !focusOwner.canBeFocusOwnerRecursively()) {
                focusOwner.transferFocus();
            }
        } else {
            component.createHierarchyEvents(1400, component, this, 1400L, Toolkit.enabledOnToolkit(32768L));
        }
        if (this.peer != null && this.layoutMgr == null && isVisible()) {
            updateCursorImmediately();
        }
    }

    public int getComponentZOrder(Component component) {
        if (component == null) {
            return -1;
        }
        synchronized (getTreeLock()) {
            if (component.parent != this) {
                return -1;
            }
            return this.component.indexOf(component);
        }
    }

    public void add(Component component, Object obj) {
        addImpl(component, obj, -1);
    }

    public void add(Component component, Object obj, int i2) {
        addImpl(component, obj, i2);
    }

    protected void addImpl(Component component, Object obj, int i2) {
        synchronized (getTreeLock()) {
            GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
            if (i2 > this.component.size() || (i2 < 0 && i2 != -1)) {
                throw new IllegalArgumentException("illegal component position");
            }
            checkAddToSelf(component);
            checkNotAWindow(component);
            if (component.parent != null) {
                component.parent.remove(component);
                if (i2 > this.component.size()) {
                    throw new IllegalArgumentException("illegal component position");
                }
            }
            if (graphicsConfiguration != null) {
                component.checkGD(graphicsConfiguration.getDevice().getIDstring());
            }
            if (i2 == -1) {
                this.component.add(component);
            } else {
                this.component.add(i2, component);
            }
            component.parent = this;
            component.setGraphicsConfiguration(graphicsConfiguration);
            adjustListeningChildren(32768L, component.numListening(32768L));
            adjustListeningChildren(65536L, component.numListening(65536L));
            adjustDescendants(component.countHierarchyMembers());
            invalidateIfValid();
            if (this.peer != null) {
                component.addNotify();
            }
            if (this.layoutMgr != null) {
                if (this.layoutMgr instanceof LayoutManager2) {
                    ((LayoutManager2) this.layoutMgr).addLayoutComponent(component, obj);
                } else if (obj instanceof String) {
                    this.layoutMgr.addLayoutComponent((String) obj, component);
                }
            }
            if (this.containerListener != null || (this.eventMask & 2) != 0 || Toolkit.enabledOnToolkit(2L)) {
                dispatchEvent(new ContainerEvent(this, 300, component));
            }
            component.createHierarchyEvents(1400, component, this, 1L, Toolkit.enabledOnToolkit(32768L));
            if (this.peer != null && this.layoutMgr == null && isVisible()) {
                updateCursorImmediately();
            }
        }
    }

    @Override // java.awt.Component
    boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        checkTreeLock();
        boolean zUpdateGraphicsData = super.updateGraphicsData(graphicsConfiguration);
        for (Component component : this.component) {
            if (component != null) {
                zUpdateGraphicsData |= component.updateGraphicsData(graphicsConfiguration);
            }
        }
        return zUpdateGraphicsData;
    }

    @Override // java.awt.Component
    void checkGD(String str) {
        for (Component component : this.component) {
            if (component != null) {
                component.checkGD(str);
            }
        }
    }

    public void remove(int i2) {
        synchronized (getTreeLock()) {
            if (i2 >= 0) {
                if (i2 < this.component.size()) {
                    Component component = this.component.get(i2);
                    if (this.peer != null) {
                        component.removeNotify();
                    }
                    if (this.layoutMgr != null) {
                        this.layoutMgr.removeLayoutComponent(component);
                    }
                    adjustListeningChildren(32768L, -component.numListening(32768L));
                    adjustListeningChildren(65536L, -component.numListening(65536L));
                    adjustDescendants(-component.countHierarchyMembers());
                    component.parent = null;
                    this.component.remove(i2);
                    component.setGraphicsConfiguration(null);
                    invalidateIfValid();
                    if (this.containerListener != null || (this.eventMask & 2) != 0 || Toolkit.enabledOnToolkit(2L)) {
                        dispatchEvent(new ContainerEvent(this, 301, component));
                    }
                    component.createHierarchyEvents(1400, component, this, 1L, Toolkit.enabledOnToolkit(32768L));
                    if (this.peer != null && this.layoutMgr == null && isVisible()) {
                        updateCursorImmediately();
                    }
                }
            }
            throw new ArrayIndexOutOfBoundsException(i2);
        }
    }

    public void remove(Component component) {
        int iIndexOf;
        synchronized (getTreeLock()) {
            if (component.parent == this && (iIndexOf = this.component.indexOf(component)) >= 0) {
                remove(iIndexOf);
            }
        }
    }

    public void removeAll() {
        synchronized (getTreeLock()) {
            adjustListeningChildren(32768L, -this.listeningChildren);
            adjustListeningChildren(65536L, -this.listeningBoundsChildren);
            adjustDescendants(-this.descendantsCount);
            while (!this.component.isEmpty()) {
                Component componentRemove = this.component.remove(this.component.size() - 1);
                if (this.peer != null) {
                    componentRemove.removeNotify();
                }
                if (this.layoutMgr != null) {
                    this.layoutMgr.removeLayoutComponent(componentRemove);
                }
                componentRemove.parent = null;
                componentRemove.setGraphicsConfiguration(null);
                if (this.containerListener != null || (this.eventMask & 2) != 0 || Toolkit.enabledOnToolkit(2L)) {
                    dispatchEvent(new ContainerEvent(this, 301, componentRemove));
                }
                componentRemove.createHierarchyEvents(1400, componentRemove, this, 1L, Toolkit.enabledOnToolkit(32768L));
            }
            if (this.peer != null && this.layoutMgr == null && isVisible()) {
                updateCursorImmediately();
            }
            invalidateIfValid();
        }
    }

    @Override // java.awt.Component
    int numListening(long j2) {
        int iNumListening = super.numListening(j2);
        if (j2 == 32768) {
            if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
                int iNumListening2 = 0;
                Iterator<Component> it = this.component.iterator();
                while (it.hasNext()) {
                    iNumListening2 += it.next().numListening(j2);
                }
                if (this.listeningChildren != iNumListening2) {
                    eventLog.fine("Assertion (listeningChildren == sum) failed");
                }
            }
            return this.listeningChildren + iNumListening;
        }
        if (j2 == 65536) {
            if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
                int iNumListening3 = 0;
                Iterator<Component> it2 = this.component.iterator();
                while (it2.hasNext()) {
                    iNumListening3 += it2.next().numListening(j2);
                }
                if (this.listeningBoundsChildren != iNumListening3) {
                    eventLog.fine("Assertion (listeningBoundsChildren == sum) failed");
                }
            }
            return this.listeningBoundsChildren + iNumListening;
        }
        if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
            eventLog.fine("This code must never be reached");
        }
        return iNumListening;
    }

    void adjustListeningChildren(long j2, int i2) {
        if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
            if (!(j2 == 32768 || j2 == 65536 || j2 == 98304)) {
                eventLog.fine("Assertion failed");
            }
        }
        if (i2 == 0) {
            return;
        }
        if ((j2 & 32768) != 0) {
            this.listeningChildren += i2;
        }
        if ((j2 & 65536) != 0) {
            this.listeningBoundsChildren += i2;
        }
        adjustListeningChildrenOnParent(j2, i2);
    }

    void adjustDescendants(int i2) {
        if (i2 == 0) {
            return;
        }
        this.descendantsCount += i2;
        adjustDecendantsOnParent(i2);
    }

    void adjustDecendantsOnParent(int i2) {
        if (this.parent != null) {
            this.parent.adjustDescendants(i2);
        }
    }

    @Override // java.awt.Component
    int countHierarchyMembers() {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            int iCountHierarchyMembers = 0;
            Iterator<Component> it = this.component.iterator();
            while (it.hasNext()) {
                iCountHierarchyMembers += it.next().countHierarchyMembers();
            }
            if (this.descendantsCount != iCountHierarchyMembers) {
                log.fine("Assertion (descendantsCount == sum) failed");
            }
        }
        return this.descendantsCount + 1;
    }

    private int getListenersCount(int i2, boolean z2) {
        checkTreeLock();
        if (z2) {
            return this.descendantsCount;
        }
        switch (i2) {
            case 1400:
                return this.listeningChildren;
            case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
            case 1402:
                return this.listeningBoundsChildren;
            default:
                return 0;
        }
    }

    @Override // java.awt.Component
    final int createHierarchyEvents(int i2, Component component, Container container, long j2, boolean z2) {
        checkTreeLock();
        int listenersCount = getListenersCount(i2, z2);
        int iCreateHierarchyEvents = listenersCount;
        int i3 = 0;
        while (iCreateHierarchyEvents > 0) {
            iCreateHierarchyEvents -= this.component.get(i3).createHierarchyEvents(i2, component, container, j2, z2);
            i3++;
        }
        return listenersCount + super.createHierarchyEvents(i2, component, container, j2, z2);
    }

    final void createChildHierarchyEvents(int i2, long j2, boolean z2) {
        checkTreeLock();
        if (this.component.isEmpty()) {
            return;
        }
        int listenersCount = getListenersCount(i2, z2);
        int i3 = 0;
        while (listenersCount > 0) {
            listenersCount -= this.component.get(i3).createHierarchyEvents(i2, this, this.parent, j2, z2);
            i3++;
        }
    }

    public LayoutManager getLayout() {
        return this.layoutMgr;
    }

    public void setLayout(LayoutManager layoutManager) {
        this.layoutMgr = layoutManager;
        invalidateIfValid();
    }

    @Override // java.awt.Component
    public void doLayout() {
        layout();
    }

    @Override // java.awt.Component
    @Deprecated
    public void layout() {
        LayoutManager layoutManager = this.layoutMgr;
        if (layoutManager != null) {
            layoutManager.layoutContainer(this);
        }
    }

    public boolean isValidateRoot() {
        return false;
    }

    @Override // java.awt.Component
    void invalidateParent() {
        if (!isJavaAwtSmartInvalidate || !isValidateRoot()) {
            super.invalidateParent();
        }
    }

    @Override // java.awt.Component
    public void invalidate() {
        LayoutManager layoutManager = this.layoutMgr;
        if (layoutManager instanceof LayoutManager2) {
            ((LayoutManager2) layoutManager).invalidateLayout(this);
        }
        super.invalidate();
    }

    @Override // java.awt.Component
    public void validate() {
        boolean zIsVisible = false;
        synchronized (getTreeLock()) {
            if ((!isValid() || descendUnconditionallyWhenValidating) && this.peer != null) {
                ContainerPeer containerPeer = null;
                if (this.peer instanceof ContainerPeer) {
                    containerPeer = (ContainerPeer) this.peer;
                }
                if (containerPeer != null) {
                    containerPeer.beginValidate();
                }
                validateTree();
                if (containerPeer != null) {
                    containerPeer.endValidate();
                    if (!descendUnconditionallyWhenValidating) {
                        zIsVisible = isVisible();
                    }
                }
            }
        }
        if (zIsVisible) {
            updateCursorImmediately();
        }
    }

    final void validateUnconditionally() {
        boolean zIsVisible = false;
        synchronized (getTreeLock()) {
            descendUnconditionallyWhenValidating = true;
            validate();
            if (this.peer instanceof ContainerPeer) {
                zIsVisible = isVisible();
            }
            descendUnconditionallyWhenValidating = false;
        }
        if (zIsVisible) {
            updateCursorImmediately();
        }
    }

    protected void validateTree() {
        checkTreeLock();
        if (!isValid() || descendUnconditionallyWhenValidating) {
            if (this.peer instanceof ContainerPeer) {
                ((ContainerPeer) this.peer).beginLayout();
            }
            if (!isValid()) {
                doLayout();
            }
            for (int i2 = 0; i2 < this.component.size(); i2++) {
                Component component = this.component.get(i2);
                if ((component instanceof Container) && !(component instanceof Window) && (!component.isValid() || descendUnconditionallyWhenValidating)) {
                    ((Container) component).validateTree();
                } else {
                    component.validate();
                }
            }
            if (this.peer instanceof ContainerPeer) {
                ((ContainerPeer) this.peer).endLayout();
            }
        }
        super.validate();
    }

    void invalidateTree() {
        synchronized (getTreeLock()) {
            for (int i2 = 0; i2 < this.component.size(); i2++) {
                Component component = this.component.get(i2);
                if (component instanceof Container) {
                    ((Container) component).invalidateTree();
                } else {
                    component.invalidateIfValid();
                }
            }
            invalidateIfValid();
        }
    }

    @Override // java.awt.Component
    public void setFont(Font font) {
        Font font2 = getFont();
        super.setFont(font);
        Font font3 = getFont();
        if (font3 != font2) {
            if (font2 == null || !font2.equals(font3)) {
                invalidateTree();
            }
        }
    }

    @Override // java.awt.Component
    public Dimension getPreferredSize() {
        return preferredSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension preferredSize() {
        Dimension dimensionPreferredSize;
        Dimension dimension = this.prefSize;
        if (dimension == null || (!isPreferredSizeSet() && !isValid())) {
            synchronized (getTreeLock()) {
                if (this.layoutMgr != null) {
                    dimensionPreferredSize = this.layoutMgr.preferredLayoutSize(this);
                } else {
                    dimensionPreferredSize = super.preferredSize();
                }
                this.prefSize = dimensionPreferredSize;
                dimension = this.prefSize;
            }
        }
        if (dimension != null) {
            return new Dimension(dimension);
        }
        return dimension;
    }

    @Override // java.awt.Component
    public Dimension getMinimumSize() {
        return minimumSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension minimumSize() {
        Dimension dimensionMinimumSize;
        Dimension dimension = this.minSize;
        if (dimension == null || (!isMinimumSizeSet() && !isValid())) {
            synchronized (getTreeLock()) {
                if (this.layoutMgr != null) {
                    dimensionMinimumSize = this.layoutMgr.minimumLayoutSize(this);
                } else {
                    dimensionMinimumSize = super.minimumSize();
                }
                this.minSize = dimensionMinimumSize;
                dimension = this.minSize;
            }
        }
        if (dimension != null) {
            return new Dimension(dimension);
        }
        return dimension;
    }

    @Override // java.awt.Component
    public Dimension getMaximumSize() {
        Dimension dimension = this.maxSize;
        if (dimension == null || (!isMaximumSizeSet() && !isValid())) {
            synchronized (getTreeLock()) {
                if (this.layoutMgr instanceof LayoutManager2) {
                    this.maxSize = ((LayoutManager2) this.layoutMgr).maximumLayoutSize(this);
                } else {
                    this.maxSize = super.getMaximumSize();
                }
                dimension = this.maxSize;
            }
        }
        if (dimension != null) {
            return new Dimension(dimension);
        }
        return dimension;
    }

    @Override // java.awt.Component
    public float getAlignmentX() {
        float alignmentX;
        if (this.layoutMgr instanceof LayoutManager2) {
            synchronized (getTreeLock()) {
                alignmentX = ((LayoutManager2) this.layoutMgr).getLayoutAlignmentX(this);
            }
        } else {
            alignmentX = super.getAlignmentX();
        }
        return alignmentX;
    }

    @Override // java.awt.Component
    public float getAlignmentY() {
        float alignmentY;
        if (this.layoutMgr instanceof LayoutManager2) {
            synchronized (getTreeLock()) {
                alignmentY = ((LayoutManager2) this.layoutMgr).getLayoutAlignmentY(this);
            }
        } else {
            alignmentY = super.getAlignmentY();
        }
        return alignmentY;
    }

    @Override // java.awt.Component
    public void paint(Graphics graphics) {
        if (isShowing()) {
            synchronized (getObjectLock()) {
                if (this.printing && this.printingThreads.contains(Thread.currentThread())) {
                    return;
                }
                GraphicsCallback.PaintCallback.getInstance().runComponents(getComponentsSync(), graphics, 2);
            }
        }
    }

    @Override // java.awt.Component
    public void update(Graphics graphics) {
        if (isShowing()) {
            if (!(this.peer instanceof LightweightPeer)) {
                graphics.clearRect(0, 0, this.width, this.height);
            }
            paint(graphics);
        }
    }

    @Override // java.awt.Component
    public void print(Graphics graphics) {
        if (isShowing()) {
            Thread threadCurrentThread = Thread.currentThread();
            try {
                synchronized (getObjectLock()) {
                    if (this.printingThreads == null) {
                        this.printingThreads = new HashSet();
                    }
                    this.printingThreads.add(threadCurrentThread);
                    this.printing = true;
                }
                super.print(graphics);
                synchronized (getObjectLock()) {
                    this.printingThreads.remove(threadCurrentThread);
                    this.printing = !this.printingThreads.isEmpty();
                }
                GraphicsCallback.PrintCallback.getInstance().runComponents(getComponentsSync(), graphics, 2);
            } catch (Throwable th) {
                synchronized (getObjectLock()) {
                    this.printingThreads.remove(threadCurrentThread);
                    this.printing = !this.printingThreads.isEmpty();
                    throw th;
                }
            }
        }
    }

    public void paintComponents(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PaintAllCallback.getInstance().runComponents(getComponentsSync(), graphics, 4);
        }
    }

    @Override // java.awt.Component
    void lightweightPaint(Graphics graphics) {
        super.lightweightPaint(graphics);
        paintHeavyweightComponents(graphics);
    }

    @Override // java.awt.Component
    void paintHeavyweightComponents(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PaintHeavyweightComponentsCallback.getInstance().runComponents(getComponentsSync(), graphics, 3);
        }
    }

    public void printComponents(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PrintAllCallback.getInstance().runComponents(getComponentsSync(), graphics, 4);
        }
    }

    @Override // java.awt.Component
    void lightweightPrint(Graphics graphics) {
        super.lightweightPrint(graphics);
        printHeavyweightComponents(graphics);
    }

    @Override // java.awt.Component
    void printHeavyweightComponents(Graphics graphics) {
        if (isShowing()) {
            GraphicsCallback.PrintHeavyweightComponentsCallback.getInstance().runComponents(getComponentsSync(), graphics, 3);
        }
    }

    public synchronized void addContainerListener(ContainerListener containerListener) {
        if (containerListener == null) {
            return;
        }
        this.containerListener = AWTEventMulticaster.add(this.containerListener, containerListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeContainerListener(ContainerListener containerListener) {
        if (containerListener == null) {
            return;
        }
        this.containerListener = AWTEventMulticaster.remove(this.containerListener, containerListener);
    }

    public synchronized ContainerListener[] getContainerListeners() {
        return (ContainerListener[]) getListeners(ContainerListener.class);
    }

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ContainerListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.containerListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        int id = aWTEvent.getID();
        if (id != 300 && id != 301) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 2) != 0 || this.containerListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ContainerEvent) {
            processContainerEvent((ContainerEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processContainerEvent(ContainerEvent containerEvent) {
        ContainerListener containerListener = this.containerListener;
        if (containerListener != null) {
            switch (containerEvent.getID()) {
                case 300:
                    containerListener.componentAdded(containerEvent);
                    break;
                case 301:
                    containerListener.componentRemoved(containerEvent);
                    break;
            }
        }
    }

    @Override // java.awt.Component
    void dispatchEventImpl(AWTEvent aWTEvent) {
        if (this.dispatcher != null && this.dispatcher.dispatchEvent(aWTEvent)) {
            aWTEvent.consume();
            if (this.peer != null) {
                this.peer.handleEvent(aWTEvent);
                return;
            }
            return;
        }
        super.dispatchEventImpl(aWTEvent);
        synchronized (getTreeLock()) {
            switch (aWTEvent.getID()) {
                case 100:
                    createChildHierarchyEvents(HierarchyEvent.ANCESTOR_MOVED, 0L, Toolkit.enabledOnToolkit(65536L));
                    break;
                case 101:
                    createChildHierarchyEvents(1402, 0L, Toolkit.enabledOnToolkit(65536L));
                    break;
            }
        }
    }

    void dispatchEventToSelf(AWTEvent aWTEvent) {
        super.dispatchEventImpl(aWTEvent);
    }

    Component getMouseEventTarget(int i2, int i3, boolean z2) {
        return getMouseEventTarget(i2, i3, z2, MouseEventTargetFilter.FILTER, false);
    }

    Component getDropTargetEventTarget(int i2, int i3, boolean z2) {
        return getMouseEventTarget(i2, i3, z2, DropTargetEventTargetFilter.FILTER, true);
    }

    private Component getMouseEventTarget(int i2, int i3, boolean z2, EventTargetFilter eventTargetFilter, boolean z3) {
        Component mouseEventTargetImpl = null;
        if (z3) {
            mouseEventTargetImpl = getMouseEventTargetImpl(i2, i3, z2, eventTargetFilter, true, z3);
        }
        if (mouseEventTargetImpl == null || mouseEventTargetImpl == this) {
            mouseEventTargetImpl = getMouseEventTargetImpl(i2, i3, z2, eventTargetFilter, false, z3);
        }
        return mouseEventTargetImpl;
    }

    private Component getMouseEventTargetImpl(int i2, int i3, boolean z2, EventTargetFilter eventTargetFilter, boolean z3, boolean z4) {
        synchronized (getTreeLock()) {
            for (int i4 = 0; i4 < this.component.size(); i4++) {
                Component component = this.component.get(i4);
                if (component != null && component.visible && (((!z3 && (component.peer instanceof LightweightPeer)) || (z3 && !(component.peer instanceof LightweightPeer))) && component.contains(i2 - component.f12361x, i3 - component.f12362y))) {
                    if (component instanceof Container) {
                        Container container = (Container) component;
                        Component mouseEventTarget = container.getMouseEventTarget(i2 - container.f12361x, i3 - container.f12362y, z2, eventTargetFilter, z4);
                        if (mouseEventTarget != null) {
                            return mouseEventTarget;
                        }
                    } else if (eventTargetFilter.accept(component)) {
                        return component;
                    }
                }
            }
            boolean z5 = (this.peer instanceof LightweightPeer) || z2;
            if (contains(i2, i3) && z5 && eventTargetFilter.accept(this)) {
                return this;
            }
            return null;
        }
    }

    /* loaded from: rt.jar:java/awt/Container$MouseEventTargetFilter.class */
    static class MouseEventTargetFilter implements EventTargetFilter {
        static final EventTargetFilter FILTER = new MouseEventTargetFilter();

        private MouseEventTargetFilter() {
        }

        @Override // java.awt.Container.EventTargetFilter
        public boolean accept(Component component) {
            return ((component.eventMask & 32) == 0 && (component.eventMask & 16) == 0 && (component.eventMask & 131072) == 0 && component.mouseListener == null && component.mouseMotionListener == null && component.mouseWheelListener == null) ? false : true;
        }
    }

    /* loaded from: rt.jar:java/awt/Container$DropTargetEventTargetFilter.class */
    static class DropTargetEventTargetFilter implements EventTargetFilter {
        static final EventTargetFilter FILTER = new DropTargetEventTargetFilter();

        private DropTargetEventTargetFilter() {
        }

        @Override // java.awt.Container.EventTargetFilter
        public boolean accept(Component component) {
            DropTarget dropTarget = component.getDropTarget();
            return dropTarget != null && dropTarget.isActive();
        }
    }

    void proxyEnableEvents(long j2) {
        if (this.peer instanceof LightweightPeer) {
            if (this.parent != null) {
                this.parent.proxyEnableEvents(j2);
            }
        } else if (this.dispatcher != null) {
            this.dispatcher.enableEvents(j2);
        }
    }

    @Override // java.awt.Component
    @Deprecated
    public void deliverEvent(Event event) {
        Component componentAt = getComponentAt(event.f12363x, event.f12364y);
        if (componentAt != null && componentAt != this) {
            event.translate(-componentAt.f12361x, -componentAt.f12362y);
            componentAt.deliverEvent(event);
        } else {
            postEvent(event);
        }
    }

    @Override // java.awt.Component
    public Component getComponentAt(int i2, int i3) {
        return locate(i2, i3);
    }

    @Override // java.awt.Component
    @Deprecated
    public Component locate(int i2, int i3) {
        if (!contains(i2, i3)) {
            return null;
        }
        Component component = null;
        synchronized (getTreeLock()) {
            for (Component component2 : this.component) {
                if (component2.contains(i2 - component2.f12361x, i3 - component2.f12362y)) {
                    if (!component2.isLightweight()) {
                        return component2;
                    }
                    if (component == null) {
                        component = component2;
                    }
                }
            }
            return component != null ? component : this;
        }
    }

    @Override // java.awt.Component
    public Component getComponentAt(Point point) {
        return getComponentAt(point.f12370x, point.f12371y);
    }

    public Point getMousePosition(boolean z2) throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        PointerInfo pointerInfo = (PointerInfo) AccessController.doPrivileged(new PrivilegedAction<PointerInfo>() { // from class: java.awt.Container.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public PointerInfo run() {
                return MouseInfo.getPointerInfo();
            }
        });
        synchronized (getTreeLock()) {
            if (isSameOrAncestorOf(findUnderMouseInWindow(pointerInfo), z2)) {
                return pointRelativeToComponent(pointerInfo.getLocation());
            }
            return null;
        }
    }

    @Override // java.awt.Component
    boolean isSameOrAncestorOf(Component component, boolean z2) {
        return this == component || (z2 && isParentOf(component));
    }

    public Component findComponentAt(int i2, int i3) {
        return findComponentAt(i2, i3, true);
    }

    final Component findComponentAt(int i2, int i3, boolean z2) {
        synchronized (getTreeLock()) {
            if (isRecursivelyVisible()) {
                return findComponentAtImpl(i2, i3, z2);
            }
            return null;
        }
    }

    final Component findComponentAtImpl(int i2, int i3, boolean z2) {
        if (!contains(i2, i3) || !this.visible) {
            return null;
        }
        if (!z2 && !this.enabled) {
            return null;
        }
        Component childAt = null;
        for (Component component : this.component) {
            int i4 = i2 - component.f12361x;
            int i5 = i3 - component.f12362y;
            if (component.contains(i4, i5)) {
                if (!component.isLightweight()) {
                    Component childAt2 = getChildAt(component, i4, i5, z2);
                    if (childAt2 != null) {
                        return childAt2;
                    }
                } else if (childAt == null) {
                    childAt = getChildAt(component, i4, i5, z2);
                }
            }
        }
        return childAt != null ? childAt : this;
    }

    private static Component getChildAt(Component component, int i2, int i3, boolean z2) {
        Component componentAt;
        if (component instanceof Container) {
            componentAt = ((Container) component).findComponentAtImpl(i2, i3, z2);
        } else {
            componentAt = component.getComponentAt(i2, i3);
        }
        if (componentAt == null || !componentAt.visible) {
            return null;
        }
        if (z2 || componentAt.enabled) {
            return componentAt;
        }
        return null;
    }

    public Component findComponentAt(Point point) {
        return findComponentAt(point.f12370x, point.f12371y);
    }

    @Override // java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            super.addNotify();
            if (!(this.peer instanceof LightweightPeer)) {
                this.dispatcher = new LightweightDispatcher(this);
            }
            for (int i2 = 0; i2 < this.component.size(); i2++) {
                this.component.get(i2).addNotify();
            }
        }
    }

    @Override // java.awt.Component
    public void removeNotify() {
        synchronized (getTreeLock()) {
            for (int size = this.component.size() - 1; size >= 0; size--) {
                Component component = this.component.get(size);
                if (component != null) {
                    component.setAutoFocusTransferOnDisposal(false);
                    component.removeNotify();
                    component.setAutoFocusTransferOnDisposal(true);
                }
            }
            if (containsFocus() && KeyboardFocusManager.isAutoFocusTransferEnabledFor(this) && !transferFocus(false)) {
                transferFocusBackward(true);
            }
            if (this.dispatcher != null) {
                this.dispatcher.dispose();
                this.dispatcher = null;
            }
            super.removeNotify();
        }
    }

    public boolean isAncestorOf(Component component) {
        if (component == null) {
            return false;
        }
        Container parent = component.getParent();
        if (parent == null) {
            return false;
        }
        for (Container parent2 = parent; parent2 != null; parent2 = parent2.getParent()) {
            if (parent2 == this) {
                return true;
            }
        }
        return false;
    }

    private void startLWModal() {
        this.modalAppContext = AppContext.getAppContext();
        long mostRecentKeyEventTime = Toolkit.getEventQueue().getMostRecentKeyEventTime();
        Component mostRecentFocusOwner = Component.isInstanceOf(this, "javax.swing.JInternalFrame") ? ((JInternalFrame) this).getMostRecentFocusOwner() : null;
        if (mostRecentFocusOwner != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().enqueueKeyEvents(mostRecentKeyEventTime, mostRecentFocusOwner);
        }
        synchronized (getTreeLock()) {
            final Container heavyweightContainer = getHeavyweightContainer();
            if (heavyweightContainer.modalComp != null) {
                this.modalComp = heavyweightContainer.modalComp;
                heavyweightContainer.modalComp = this;
                return;
            }
            heavyweightContainer.modalComp = this;
            Runnable runnable = new Runnable() { // from class: java.awt.Container.3
                @Override // java.lang.Runnable
                public void run() {
                    ((EventDispatchThread) Thread.currentThread()).pumpEventsForHierarchy(new Conditional() { // from class: java.awt.Container.3.1
                        @Override // java.awt.Conditional
                        public boolean evaluate() {
                            return Container.this.windowClosingException == null && heavyweightContainer.modalComp != null;
                        }
                    }, Container.this);
                }
            };
            if (EventQueue.isDispatchThread()) {
                SequencedEvent currentSequencedEvent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent();
                if (currentSequencedEvent != null) {
                    currentSequencedEvent.dispose();
                }
                runnable.run();
            } else {
                synchronized (getTreeLock()) {
                    Toolkit.getEventQueue().postEvent(new PeerEvent(this, runnable, 1L));
                    while (this.windowClosingException == null && heavyweightContainer.modalComp != null) {
                        try {
                            getTreeLock().wait();
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            }
            if (this.windowClosingException != null) {
                this.windowClosingException.fillInStackTrace();
                throw this.windowClosingException;
            }
            if (mostRecentFocusOwner != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().dequeueKeyEvents(mostRecentKeyEventTime, mostRecentFocusOwner);
            }
        }
    }

    private void stopLWModal() {
        synchronized (getTreeLock()) {
            if (this.modalAppContext != null) {
                Container heavyweightContainer = getHeavyweightContainer();
                if (heavyweightContainer != null) {
                    if (this.modalComp != null) {
                        heavyweightContainer.modalComp = this.modalComp;
                        this.modalComp = null;
                        return;
                    }
                    heavyweightContainer.modalComp = null;
                }
                SunToolkit.postEvent(this.modalAppContext, new PeerEvent(this, new WakingRunnable(), 1L));
            }
            EventQueue.invokeLater(new WakingRunnable());
            getTreeLock().notifyAll();
        }
    }

    /* loaded from: rt.jar:java/awt/Container$WakingRunnable.class */
    static final class WakingRunnable implements Runnable {
        WakingRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        String strParamString = super.paramString();
        LayoutManager layoutManager = this.layoutMgr;
        if (layoutManager != null) {
            strParamString = strParamString + ",layout=" + layoutManager.getClass().getName();
        }
        return strParamString;
    }

    @Override // java.awt.Component
    public void list(PrintStream printStream, int i2) {
        super.list(printStream, i2);
        synchronized (getTreeLock()) {
            for (int i3 = 0; i3 < this.component.size(); i3++) {
                Component component = this.component.get(i3);
                if (component != null) {
                    component.list(printStream, i2 + 1);
                }
            }
        }
    }

    @Override // java.awt.Component
    public void list(PrintWriter printWriter, int i2) {
        super.list(printWriter, i2);
        synchronized (getTreeLock()) {
            for (int i3 = 0; i3 < this.component.size(); i3++) {
                Component component = this.component.get(i3);
                if (component != null) {
                    component.list(printWriter, i2 + 1);
                }
            }
        }
    }

    @Override // java.awt.Component
    public void setFocusTraversalKeys(int i2, Set<? extends AWTKeyStroke> set) {
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        setFocusTraversalKeys_NoIDCheck(i2, set);
    }

    @Override // java.awt.Component
    public Set<AWTKeyStroke> getFocusTraversalKeys(int i2) {
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        return getFocusTraversalKeys_NoIDCheck(i2);
    }

    @Override // java.awt.Component
    public boolean areFocusTraversalKeysSet(int i2) {
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        return (this.focusTraversalKeys == null || this.focusTraversalKeys[i2] == null) ? false : true;
    }

    @Override // java.awt.Component
    public boolean isFocusCycleRoot(Container container) {
        if (isFocusCycleRoot() && container == this) {
            return true;
        }
        return super.isFocusCycleRoot(container);
    }

    private Container findTraversalRoot() {
        Container focusCycleRootAncestor;
        Container currentFocusCycleRoot = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentFocusCycleRoot();
        if (currentFocusCycleRoot == this) {
            focusCycleRootAncestor = this;
        } else {
            focusCycleRootAncestor = getFocusCycleRootAncestor();
            if (focusCycleRootAncestor == null) {
                focusCycleRootAncestor = this;
            }
        }
        if (focusCycleRootAncestor != currentFocusCycleRoot) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRootPriv(focusCycleRootAncestor);
        }
        return focusCycleRootAncestor;
    }

    @Override // java.awt.Component
    final boolean containsFocus() {
        return isParentOf(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
    }

    private boolean isParentOf(Component component) {
        boolean z2;
        synchronized (getTreeLock()) {
            while (component != null && component != this) {
                if (component instanceof Window) {
                    break;
                }
                component = component.getParent();
            }
            z2 = component == this;
        }
        return z2;
    }

    @Override // java.awt.Component
    void clearMostRecentFocusOwnerOnHide() {
        Window containingWindow;
        boolean z2 = false;
        synchronized (getTreeLock()) {
            containingWindow = getContainingWindow();
            if (containingWindow != null) {
                Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(containingWindow);
                z2 = mostRecentFocusOwner == this || isParentOf(mostRecentFocusOwner);
                synchronized (KeyboardFocusManager.class) {
                    Component temporaryLostComponent = containingWindow.getTemporaryLostComponent();
                    if (isParentOf(temporaryLostComponent) || temporaryLostComponent == this) {
                        containingWindow.setTemporaryLostComponent(null);
                    }
                }
            }
        }
        if (z2) {
            KeyboardFocusManager.setMostRecentFocusOwner(containingWindow, null);
        }
    }

    @Override // java.awt.Component
    void clearCurrentFocusCycleRootOnHide() {
        KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Container currentFocusCycleRoot = currentKeyboardFocusManager.getCurrentFocusCycleRoot();
        if (currentFocusCycleRoot == this || isParentOf(currentFocusCycleRoot)) {
            currentKeyboardFocusManager.setGlobalCurrentFocusCycleRootPriv(null);
        }
    }

    @Override // java.awt.Component
    final Container getTraversalRoot() {
        if (isFocusCycleRoot()) {
            return findTraversalRoot();
        }
        return super.getTraversalRoot();
    }

    public void setFocusTraversalPolicy(FocusTraversalPolicy focusTraversalPolicy) {
        FocusTraversalPolicy focusTraversalPolicy2;
        synchronized (this) {
            focusTraversalPolicy2 = this.focusTraversalPolicy;
            this.focusTraversalPolicy = focusTraversalPolicy;
        }
        firePropertyChange("focusTraversalPolicy", focusTraversalPolicy2, focusTraversalPolicy);
    }

    public FocusTraversalPolicy getFocusTraversalPolicy() {
        if (!isFocusTraversalPolicyProvider() && !isFocusCycleRoot()) {
            return null;
        }
        FocusTraversalPolicy focusTraversalPolicy = this.focusTraversalPolicy;
        if (focusTraversalPolicy != null) {
            return focusTraversalPolicy;
        }
        Container focusCycleRootAncestor = getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null) {
            return focusCycleRootAncestor.getFocusTraversalPolicy();
        }
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy();
    }

    public boolean isFocusTraversalPolicySet() {
        return this.focusTraversalPolicy != null;
    }

    public void setFocusCycleRoot(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.focusCycleRoot;
            this.focusCycleRoot = z2;
        }
        firePropertyChange("focusCycleRoot", z3, z2);
    }

    public boolean isFocusCycleRoot() {
        return this.focusCycleRoot;
    }

    public final void setFocusTraversalPolicyProvider(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.focusTraversalPolicyProvider;
            this.focusTraversalPolicyProvider = z2;
        }
        firePropertyChange("focusTraversalPolicyProvider", z3, z2);
    }

    public final boolean isFocusTraversalPolicyProvider() {
        return this.focusTraversalPolicyProvider;
    }

    public void transferFocusDownCycle() {
        if (isFocusCycleRoot()) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setGlobalCurrentFocusCycleRootPriv(this);
            Component defaultComponent = getFocusTraversalPolicy().getDefaultComponent(this);
            if (defaultComponent != null) {
                defaultComponent.requestFocus(CausedFocusEvent.Cause.TRAVERSAL_DOWN);
            }
        }
    }

    void preProcessKeyEvent(KeyEvent keyEvent) {
        Container container = this.parent;
        if (container != null) {
            container.preProcessKeyEvent(keyEvent);
        }
    }

    void postProcessKeyEvent(KeyEvent keyEvent) {
        Container container = this.parent;
        if (container != null) {
            container.postProcessKeyEvent(keyEvent);
        }
    }

    @Override // java.awt.Component
    boolean postsOldMouseEvents() {
        return true;
    }

    @Override // java.awt.Component
    public void applyComponentOrientation(ComponentOrientation componentOrientation) {
        super.applyComponentOrientation(componentOrientation);
        synchronized (getTreeLock()) {
            for (int i2 = 0; i2 < this.component.size(); i2++) {
                this.component.get(i2).applyComponentOrientation(componentOrientation);
            }
        }
    }

    @Override // java.awt.Component, java.beans.PropertyEditor
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        super.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // java.awt.Component
    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        super.addPropertyChangeListener(str, propertyChangeListener);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("ncomponents", this.component.size());
        putFieldPutFields.put("component", this.component.toArray(EMPTY_ARRAY));
        putFieldPutFields.put("layoutMgr", this.layoutMgr);
        putFieldPutFields.put("dispatcher", this.dispatcher);
        putFieldPutFields.put("maxSize", this.maxSize);
        putFieldPutFields.put("focusCycleRoot", this.focusCycleRoot);
        putFieldPutFields.put("containerSerializedDataVersion", this.containerSerializedDataVersion);
        putFieldPutFields.put("focusTraversalPolicyProvider", this.focusTraversalPolicyProvider);
        objectOutputStream.writeFields();
        AWTEventMulticaster.save(objectOutputStream, "containerL", this.containerListener);
        objectOutputStream.writeObject(null);
        if (this.focusTraversalPolicy instanceof Serializable) {
            objectOutputStream.writeObject(this.focusTraversalPolicy);
        } else {
            objectOutputStream.writeObject(null);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        Component[] componentArr = (Component[]) fields.get("component", (Object) null);
        if (componentArr == null) {
            componentArr = EMPTY_ARRAY;
        }
        int iIntValue = Integer.valueOf(fields.get("ncomponents", 0)).intValue();
        if (iIntValue < 0 || iIntValue > componentArr.length) {
            throw new InvalidObjectException("Incorrect number of components");
        }
        this.component = new ArrayList(iIntValue);
        for (int i2 = 0; i2 < iIntValue; i2++) {
            this.component.add(componentArr[i2]);
        }
        this.layoutMgr = (LayoutManager) fields.get("layoutMgr", (Object) null);
        this.dispatcher = (LightweightDispatcher) fields.get("dispatcher", (Object) null);
        if (this.maxSize == null) {
            this.maxSize = (Dimension) fields.get("maxSize", (Object) null);
        }
        this.focusCycleRoot = fields.get("focusCycleRoot", false);
        this.containerSerializedDataVersion = fields.get("containerSerializedDataVersion", 1);
        this.focusTraversalPolicyProvider = fields.get("focusTraversalPolicyProvider", false);
        for (Component component : this.component) {
            component.parent = this;
            adjustListeningChildren(32768L, component.numListening(32768L));
            adjustListeningChildren(65536L, component.numListening(65536L));
            adjustDescendants(component.countHierarchyMembers());
        }
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("containerL" == ((String) object).intern()) {
                    addContainerListener((ContainerListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                try {
                    break;
                } catch (OptionalDataException e2) {
                    if (!e2.eof) {
                        throw e2;
                    }
                    return;
                }
            }
        }
        Object object2 = objectInputStream.readObject();
        if (object2 instanceof FocusTraversalPolicy) {
            this.focusTraversalPolicy = (FocusTraversalPolicy) object2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Container$AccessibleAWTContainer.class */
    public class AccessibleAWTContainer extends Component.AccessibleAWTComponent {
        private static final long serialVersionUID = 5081320404842566097L;
        private volatile transient int propertyListenersCount;
        protected ContainerListener accessibleContainerHandler;

        protected AccessibleAWTContainer() {
            super();
            this.propertyListenersCount = 0;
            this.accessibleContainerHandler = null;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return Container.this.getAccessibleChildrenCount();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return Container.this.getAccessibleChild(i2);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            return Container.this.getAccessibleAt(point);
        }

        /* loaded from: rt.jar:java/awt/Container$AccessibleAWTContainer$AccessibleContainerHandler.class */
        protected class AccessibleContainerHandler implements ContainerListener {
            protected AccessibleContainerHandler() {
            }

            @Override // java.awt.event.ContainerListener
            public void componentAdded(ContainerEvent containerEvent) {
                MenuContainer child = containerEvent.getChild();
                if (child != null && (child instanceof Accessible)) {
                    AccessibleAWTContainer.this.firePropertyChange(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY, null, ((Accessible) child).getAccessibleContext());
                }
            }

            @Override // java.awt.event.ContainerListener
            public void componentRemoved(ContainerEvent containerEvent) {
                MenuContainer child = containerEvent.getChild();
                if (child != null && (child instanceof Accessible)) {
                    AccessibleAWTContainer.this.firePropertyChange(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY, ((Accessible) child).getAccessibleContext(), null);
                }
            }
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            if (this.accessibleContainerHandler == null) {
                this.accessibleContainerHandler = new AccessibleContainerHandler();
            }
            int i2 = this.propertyListenersCount;
            this.propertyListenersCount = i2 + 1;
            if (i2 == 0) {
                Container.this.addContainerListener(this.accessibleContainerHandler);
            }
            super.addPropertyChangeListener(propertyChangeListener);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            int i2 = this.propertyListenersCount - 1;
            this.propertyListenersCount = i2;
            if (i2 == 0) {
                Container.this.removeContainerListener(this.accessibleContainerHandler);
            }
            super.removePropertyChangeListener(propertyChangeListener);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    Accessible getAccessibleAt(Point point) {
        AccessibleComponent accessibleComponent;
        synchronized (getTreeLock()) {
            if (this instanceof Accessible) {
                AccessibleContext accessibleContext = ((Accessible) this).getAccessibleContext();
                if (accessibleContext != null) {
                    int accessibleChildrenCount = accessibleContext.getAccessibleChildrenCount();
                    for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                        Accessible accessibleChild = accessibleContext.getAccessibleChild(i2);
                        if (accessibleChild != null) {
                            accessibleContext = accessibleChild.getAccessibleContext();
                            if (accessibleContext != null && (accessibleComponent = accessibleContext.getAccessibleComponent()) != null && accessibleComponent.isShowing()) {
                                Point location = accessibleComponent.getLocation();
                                if (accessibleComponent.contains(new Point(point.f12370x - location.f12370x, point.f12371y - location.f12371y))) {
                                    return accessibleChild;
                                }
                            }
                        }
                    }
                }
                return (Accessible) this;
            }
            Component component = this;
            if (!contains(point.f12370x, point.f12371y)) {
                component = null;
            } else {
                int componentCount = getComponentCount();
                for (int i3 = 0; i3 < componentCount; i3++) {
                    Component component2 = getComponent(i3);
                    if (component2 != null && component2.isShowing()) {
                        Point location2 = component2.getLocation();
                        if (component2.contains(point.f12370x - location2.f12370x, point.f12371y - location2.f12371y)) {
                            component = component2;
                        }
                    }
                }
            }
            if (component instanceof Accessible) {
                return (Accessible) component;
            }
            return null;
        }
    }

    int getAccessibleChildrenCount() {
        int i2;
        synchronized (getTreeLock()) {
            int i3 = 0;
            for (Component component : getComponents()) {
                if (component instanceof Accessible) {
                    i3++;
                }
            }
            i2 = i3;
        }
        return i2;
    }

    Accessible getAccessibleChild(int i2) {
        synchronized (getTreeLock()) {
            Object[] components = getComponents();
            int i3 = 0;
            for (int i4 = 0; i4 < components.length; i4++) {
                if (components[i4] instanceof Accessible) {
                    if (i3 == i2) {
                        return (Accessible) components[i4];
                    }
                    i3++;
                }
            }
            return null;
        }
    }

    final void increaseComponentCount(Component component) {
        synchronized (getTreeLock()) {
            if (!component.isDisplayable()) {
                throw new IllegalStateException("Peer does not exist while invoking the increaseComponentCount() method");
            }
            int i2 = 0;
            int i3 = 0;
            if (component instanceof Container) {
                i3 = ((Container) component).numOfLWComponents;
                i2 = ((Container) component).numOfHWComponents;
            }
            if (component.isLightweight()) {
                i3++;
            } else {
                i2++;
            }
            for (Container container = this; container != null; container = container.getContainer()) {
                container.numOfLWComponents += i3;
                container.numOfHWComponents += i2;
            }
        }
    }

    final void decreaseComponentCount(Component component) {
        synchronized (getTreeLock()) {
            if (!component.isDisplayable()) {
                throw new IllegalStateException("Peer does not exist while invoking the decreaseComponentCount() method");
            }
            int i2 = 0;
            int i3 = 0;
            if (component instanceof Container) {
                i3 = ((Container) component).numOfLWComponents;
                i2 = ((Container) component).numOfHWComponents;
            }
            if (component.isLightweight()) {
                i3++;
            } else {
                i2++;
            }
            for (Container container = this; container != null; container = container.getContainer()) {
                container.numOfLWComponents -= i3;
                container.numOfHWComponents -= i2;
            }
        }
    }

    private int getTopmostComponentIndex() {
        checkTreeLock();
        if (getComponentCount() > 0) {
            return 0;
        }
        return -1;
    }

    private int getBottommostComponentIndex() {
        checkTreeLock();
        if (getComponentCount() > 0) {
            return getComponentCount() - 1;
        }
        return -1;
    }

    @Override // java.awt.Component
    final Region getOpaqueShape() {
        checkTreeLock();
        if (isLightweight() && isNonOpaqueForMixing() && hasLightweightDescendants()) {
            Region union = Region.EMPTY_REGION;
            for (int i2 = 0; i2 < getComponentCount(); i2++) {
                Component component = getComponent(i2);
                if (component.isLightweight() && component.isShowing()) {
                    union = union.getUnion(component.getOpaqueShape());
                }
            }
            return union.getIntersection(getNormalShape());
        }
        return super.getOpaqueShape();
    }

    final void recursiveSubtractAndApplyShape(Region region) {
        recursiveSubtractAndApplyShape(region, getTopmostComponentIndex(), getBottommostComponentIndex());
    }

    final void recursiveSubtractAndApplyShape(Region region, int i2) {
        recursiveSubtractAndApplyShape(region, i2, getBottommostComponentIndex());
    }

    final void recursiveSubtractAndApplyShape(Region region, int i2, int i3) {
        checkTreeLock();
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("this = " + ((Object) this) + "; shape=" + ((Object) region) + "; fromZ=" + i2 + "; toZ=" + i3);
        }
        if (i2 == -1 || region.isEmpty()) {
            return;
        }
        if (getLayout() != null && !isValid()) {
            return;
        }
        for (int i4 = i2; i4 <= i3; i4++) {
            Component component = getComponent(i4);
            if (!component.isLightweight()) {
                component.subtractAndApplyShape(region);
            } else if ((component instanceof Container) && ((Container) component).hasHeavyweightDescendants() && component.isShowing()) {
                ((Container) component).recursiveSubtractAndApplyShape(region);
            }
        }
    }

    final void recursiveApplyCurrentShape() {
        recursiveApplyCurrentShape(getTopmostComponentIndex(), getBottommostComponentIndex());
    }

    final void recursiveApplyCurrentShape(int i2) {
        recursiveApplyCurrentShape(i2, getBottommostComponentIndex());
    }

    final void recursiveApplyCurrentShape(int i2, int i3) {
        checkTreeLock();
        if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
            mixingLog.fine("this = " + ((Object) this) + "; fromZ=" + i2 + "; toZ=" + i3);
        }
        if (i2 == -1) {
            return;
        }
        if (getLayout() != null && !isValid()) {
            return;
        }
        for (int i4 = i2; i4 <= i3; i4++) {
            Component component = getComponent(i4);
            if (!component.isLightweight()) {
                component.applyCurrentShape();
            }
            if ((component instanceof Container) && ((Container) component).hasHeavyweightDescendants()) {
                ((Container) component).recursiveApplyCurrentShape();
            }
        }
    }

    private void recursiveShowHeavyweightChildren() {
        ComponentPeer peer;
        if (!hasHeavyweightDescendants() || !isVisible()) {
            return;
        }
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (component.isLightweight()) {
                if (component instanceof Container) {
                    ((Container) component).recursiveShowHeavyweightChildren();
                }
            } else if (component.isVisible() && (peer = component.getPeer()) != null) {
                peer.setVisible(true);
            }
        }
    }

    private void recursiveHideHeavyweightChildren() {
        ComponentPeer peer;
        if (!hasHeavyweightDescendants()) {
            return;
        }
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (component.isLightweight()) {
                if (component instanceof Container) {
                    ((Container) component).recursiveHideHeavyweightChildren();
                }
            } else if (component.isVisible() && (peer = component.getPeer()) != null) {
                peer.setVisible(false);
            }
        }
    }

    private void recursiveRelocateHeavyweightChildren(Point point) {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (component.isLightweight()) {
                if ((component instanceof Container) && ((Container) component).hasHeavyweightDescendants()) {
                    Point point2 = new Point(point);
                    point2.translate(component.getX(), component.getY());
                    ((Container) component).recursiveRelocateHeavyweightChildren(point2);
                }
            } else {
                ComponentPeer peer = component.getPeer();
                if (peer != null) {
                    peer.setBounds(point.f12370x + component.getX(), point.f12371y + component.getY(), component.getWidth(), component.getHeight(), 1);
                }
            }
        }
    }

    final boolean isRecursivelyVisibleUpToHeavyweightContainer() {
        if (!isLightweight()) {
            return true;
        }
        Container container = this;
        while (true) {
            Container container2 = container;
            if (container2 != null && container2.isLightweight()) {
                if (container2.isVisible()) {
                    container = container2.getContainer();
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override // java.awt.Component
    void mixOnShowing() {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            boolean zIsLightweight = isLightweight();
            if (zIsLightweight && isRecursivelyVisibleUpToHeavyweightContainer()) {
                recursiveShowHeavyweightChildren();
            }
            if (isMixingNeeded()) {
                if (!zIsLightweight || (zIsLightweight && hasHeavyweightDescendants())) {
                    recursiveApplyCurrentShape();
                }
                super.mixOnShowing();
            }
        }
    }

    @Override // java.awt.Component
    void mixOnHiding(boolean z2) {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; isLightweight=" + z2);
            }
            if (z2) {
                recursiveHideHeavyweightChildren();
            }
            super.mixOnHiding(z2);
        }
    }

    @Override // java.awt.Component
    void mixOnReshaping() {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            boolean zIsMixingNeeded = isMixingNeeded();
            if (isLightweight() && hasHeavyweightDescendants()) {
                Point point = new Point(getX(), getY());
                for (Container container = getContainer(); container != null && container.isLightweight(); container = container.getContainer()) {
                    point.translate(container.getX(), container.getY());
                }
                recursiveRelocateHeavyweightChildren(point);
                if (!zIsMixingNeeded) {
                    return;
                } else {
                    recursiveApplyCurrentShape();
                }
            }
            if (zIsMixingNeeded) {
                super.mixOnReshaping();
            }
        }
    }

    @Override // java.awt.Component
    void mixOnZOrderChanging(int i2, int i3) {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this) + "; oldZ=" + i2 + "; newZ=" + i3);
            }
            if (isMixingNeeded()) {
                if ((i3 < i2) && isLightweight() && hasHeavyweightDescendants()) {
                    recursiveApplyCurrentShape();
                }
                super.mixOnZOrderChanging(i2, i3);
            }
        }
    }

    @Override // java.awt.Component
    void mixOnValidating() {
        synchronized (getTreeLock()) {
            if (mixingLog.isLoggable(PlatformLogger.Level.FINE)) {
                mixingLog.fine("this = " + ((Object) this));
            }
            if (isMixingNeeded()) {
                if (hasHeavyweightDescendants()) {
                    recursiveApplyCurrentShape();
                }
                if (isLightweight() && isNonOpaqueForMixing()) {
                    subtractAndApplyShapeBelowMe();
                }
                super.mixOnValidating();
            }
        }
    }
}
