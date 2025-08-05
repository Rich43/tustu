package java.awt;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.KeyboardFocusManagerPeerProvider;
import sun.awt.SunToolkit;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/KeyboardFocusManager.class */
public abstract class KeyboardFocusManager implements KeyEventDispatcher, KeyEventPostProcessor {
    private static final PlatformLogger focusLog;
    transient KeyboardFocusManagerPeer peer;
    private static final PlatformLogger log;
    public static final int FORWARD_TRAVERSAL_KEYS = 0;
    public static final int BACKWARD_TRAVERSAL_KEYS = 1;
    public static final int UP_CYCLE_TRAVERSAL_KEYS = 2;
    public static final int DOWN_CYCLE_TRAVERSAL_KEYS = 3;
    static final int TRAVERSAL_KEY_LENGTH = 4;
    private static Component focusOwner;
    private static Component permanentFocusOwner;
    private static Window focusedWindow;
    private static Window activeWindow;
    private static final String[] defaultFocusTraversalKeyPropertyNames;
    private static Container currentFocusCycleRoot;
    private VetoableChangeSupport vetoableSupport;
    private PropertyChangeSupport changeSupport;
    private LinkedList<KeyEventDispatcher> keyEventDispatchers;
    private LinkedList<KeyEventPostProcessor> keyEventPostProcessors;
    private static Map<Window, WeakReference<Component>> mostRecentFocusOwners;
    private static AWTPermission replaceKeyboardFocusManagerPermission;
    private static LinkedList<HeavyweightFocusRequest> heavyweightRequests;
    private static LinkedList<LightweightFocusRequest> currentLightweightRequests;
    private static boolean clearingCurrentLightweightRequests;
    private static boolean allowSyncFocusRequests;
    private static Component newFocusOwner;
    private static volatile boolean disableRestoreFocus;
    static final int SNFH_FAILURE = 0;
    static final int SNFH_SUCCESS_HANDLED = 1;
    static final int SNFH_SUCCESS_PROCEED = 2;
    static Field proxyActive;
    static final /* synthetic */ boolean $assertionsDisabled;
    private FocusTraversalPolicy defaultPolicy = new DefaultFocusTraversalPolicy();
    private Set<AWTKeyStroke>[] defaultFocusTraversalKeys = new Set[4];
    transient SequencedEvent currentSequencedEvent = null;

    private static native void initIDs();

    public abstract boolean dispatchEvent(AWTEvent aWTEvent);

    @Override // java.awt.KeyEventDispatcher
    public abstract boolean dispatchKeyEvent(KeyEvent keyEvent);

    public abstract boolean postProcessKeyEvent(KeyEvent keyEvent);

    public abstract void processKeyEvent(Component component, KeyEvent keyEvent);

    protected abstract void enqueueKeyEvents(long j2, Component component);

    protected abstract void dequeueKeyEvents(long j2, Component component);

    protected abstract void discardKeyEvents(Component component);

    public abstract void focusNextComponent(Component component);

    public abstract void focusPreviousComponent(Component component);

    public abstract void upFocusCycle(Component component);

    public abstract void downFocusCycle(Container container);

    static {
        $assertionsDisabled = !KeyboardFocusManager.class.desiredAssertionStatus();
        focusLog = PlatformLogger.getLogger("java.awt.focus.KeyboardFocusManager");
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setKeyboardFocusManagerAccessor(new AWTAccessor.KeyboardFocusManagerAccessor() { // from class: java.awt.KeyboardFocusManager.1
            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public int shouldNativelyFocusHeavyweight(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
                return KeyboardFocusManager.shouldNativelyFocusHeavyweight(component, component2, z2, z3, j2, cause);
            }

            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public boolean processSynchronousLightweightTransfer(Component component, Component component2, boolean z2, boolean z3, long j2) {
                return KeyboardFocusManager.processSynchronousLightweightTransfer(component, component2, z2, z3, j2);
            }

            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public void removeLastFocusRequest(Component component) {
                KeyboardFocusManager.removeLastFocusRequest(component);
            }

            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public void setMostRecentFocusOwner(Window window, Component component) {
                KeyboardFocusManager.setMostRecentFocusOwner(window, component);
            }

            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext appContext) {
                return KeyboardFocusManager.getCurrentKeyboardFocusManager(appContext);
            }

            @Override // sun.awt.AWTAccessor.KeyboardFocusManagerAccessor
            public Container getCurrentFocusCycleRoot() {
                return KeyboardFocusManager.currentFocusCycleRoot;
            }
        });
        log = PlatformLogger.getLogger("java.awt.KeyboardFocusManager");
        defaultFocusTraversalKeyPropertyNames = new String[]{"forwardDefaultFocusTraversalKeys", "backwardDefaultFocusTraversalKeys", "upCycleDefaultFocusTraversalKeys", "downCycleDefaultFocusTraversalKeys"};
        mostRecentFocusOwners = new WeakHashMap();
        heavyweightRequests = new LinkedList<>();
        allowSyncFocusRequests = true;
        newFocusOwner = null;
    }

    public static KeyboardFocusManager getCurrentKeyboardFocusManager() {
        return getCurrentKeyboardFocusManager(AppContext.getAppContext());
    }

    static synchronized KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext appContext) {
        KeyboardFocusManager defaultKeyboardFocusManager = (KeyboardFocusManager) appContext.get(KeyboardFocusManager.class);
        if (defaultKeyboardFocusManager == null) {
            defaultKeyboardFocusManager = new DefaultKeyboardFocusManager();
            appContext.put(KeyboardFocusManager.class, defaultKeyboardFocusManager);
        }
        return defaultKeyboardFocusManager;
    }

    public static void setCurrentKeyboardFocusManager(KeyboardFocusManager keyboardFocusManager) throws SecurityException {
        KeyboardFocusManager currentKeyboardFocusManager;
        checkReplaceKFMPermission();
        synchronized (KeyboardFocusManager.class) {
            AppContext appContext = AppContext.getAppContext();
            if (keyboardFocusManager != null) {
                currentKeyboardFocusManager = getCurrentKeyboardFocusManager(appContext);
                appContext.put(KeyboardFocusManager.class, keyboardFocusManager);
            } else {
                currentKeyboardFocusManager = getCurrentKeyboardFocusManager(appContext);
                appContext.remove(KeyboardFocusManager.class);
            }
        }
        if (currentKeyboardFocusManager != null) {
            currentKeyboardFocusManager.firePropertyChange("managingFocus", Boolean.TRUE, Boolean.FALSE);
        }
        if (keyboardFocusManager != null) {
            keyboardFocusManager.firePropertyChange("managingFocus", Boolean.FALSE, Boolean.TRUE);
        }
    }

    final void setCurrentSequencedEvent(SequencedEvent sequencedEvent) {
        synchronized (SequencedEvent.class) {
            if (!$assertionsDisabled && sequencedEvent != null && this.currentSequencedEvent != null) {
                throw new AssertionError();
            }
            this.currentSequencedEvent = sequencedEvent;
        }
    }

    final SequencedEvent getCurrentSequencedEvent() {
        SequencedEvent sequencedEvent;
        synchronized (SequencedEvent.class) {
            sequencedEvent = this.currentSequencedEvent;
        }
        return sequencedEvent;
    }

    static Set<AWTKeyStroke> initFocusTraversalKeysSet(String str, Set<AWTKeyStroke> set) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        while (stringTokenizer.hasMoreTokens()) {
            set.add(AWTKeyStroke.getAWTKeyStroke(stringTokenizer.nextToken()));
        }
        return set.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public KeyboardFocusManager() {
        AWTKeyStroke[] aWTKeyStrokeArr = {new AWTKeyStroke[]{AWTKeyStroke.getAWTKeyStroke(9, 0, false), AWTKeyStroke.getAWTKeyStroke(9, 130, false)}, new AWTKeyStroke[]{AWTKeyStroke.getAWTKeyStroke(9, 65, false), AWTKeyStroke.getAWTKeyStroke(9, 195, false)}, new AWTKeyStroke[0], new AWTKeyStroke[0]};
        for (int i2 = 0; i2 < 4; i2++) {
            HashSet hashSet = new HashSet();
            for (int i3 = 0; i3 < aWTKeyStrokeArr[i2].length; i3++) {
                hashSet.add(aWTKeyStrokeArr[i2][i3]);
            }
            this.defaultFocusTraversalKeys[i2] = hashSet.isEmpty() ? Collections.EMPTY_SET : Collections.unmodifiableSet(hashSet);
        }
        initPeer();
    }

    private void initPeer() {
        this.peer = ((KeyboardFocusManagerPeerProvider) Toolkit.getDefaultToolkit()).getKeyboardFocusManagerPeer();
    }

    public Component getFocusOwner() {
        synchronized (KeyboardFocusManager.class) {
            if (focusOwner == null) {
                return null;
            }
            return focusOwner.appContext == AppContext.getAppContext() ? focusOwner : null;
        }
    }

    protected Component getGlobalFocusOwner() throws SecurityException {
        Component component;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            component = focusOwner;
        }
        return component;
    }

    protected void setGlobalFocusOwner(Component component) throws SecurityException {
        Component focusOwner2 = null;
        boolean z2 = false;
        if (component == null || component.isFocusable()) {
            synchronized (KeyboardFocusManager.class) {
                checkKFMSecurity();
                focusOwner2 = getFocusOwner();
                try {
                    fireVetoableChange("focusOwner", focusOwner2, component);
                    focusOwner = component;
                    if (component != null && (getCurrentFocusCycleRoot() == null || !component.isFocusCycleRoot(getCurrentFocusCycleRoot()))) {
                        Container focusCycleRootAncestor = component.getFocusCycleRootAncestor();
                        if (focusCycleRootAncestor == null && (component instanceof Window)) {
                            focusCycleRootAncestor = (Container) component;
                        }
                        if (focusCycleRootAncestor != null) {
                            setGlobalCurrentFocusCycleRootPriv(focusCycleRootAncestor);
                        }
                    }
                    z2 = true;
                } catch (PropertyVetoException e2) {
                    return;
                }
            }
        }
        if (z2) {
            firePropertyChange("focusOwner", focusOwner2, component);
        }
    }

    public void clearFocusOwner() throws SecurityException {
        if (getFocusOwner() != null) {
            clearGlobalFocusOwner();
        }
    }

    public void clearGlobalFocusOwner() throws SecurityException {
        checkReplaceKFMPermission();
        if (!GraphicsEnvironment.isHeadless()) {
            Toolkit.getDefaultToolkit();
            _clearGlobalFocusOwner();
        }
    }

    private void _clearGlobalFocusOwner() {
        this.peer.clearGlobalFocusOwner(markClearGlobalFocusOwner());
    }

    void clearGlobalFocusOwnerPriv() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.KeyboardFocusManager.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                KeyboardFocusManager.this.clearGlobalFocusOwner();
                return null;
            }
        });
    }

    Component getNativeFocusOwner() {
        return this.peer.getCurrentFocusOwner();
    }

    void setNativeFocusOwner(Component component) {
        if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
            focusLog.finest("Calling peer {0} setCurrentFocusOwner for {1}", String.valueOf(this.peer), String.valueOf(component));
        }
        this.peer.setCurrentFocusOwner(component);
    }

    Window getNativeFocusedWindow() {
        return this.peer.getCurrentFocusedWindow();
    }

    public Component getPermanentFocusOwner() {
        synchronized (KeyboardFocusManager.class) {
            if (permanentFocusOwner == null) {
                return null;
            }
            return permanentFocusOwner.appContext == AppContext.getAppContext() ? permanentFocusOwner : null;
        }
    }

    protected Component getGlobalPermanentFocusOwner() throws SecurityException {
        Component component;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            component = permanentFocusOwner;
        }
        return component;
    }

    protected void setGlobalPermanentFocusOwner(Component component) throws SecurityException {
        Component permanentFocusOwner2 = null;
        boolean z2 = false;
        if (component == null || component.isFocusable()) {
            synchronized (KeyboardFocusManager.class) {
                checkKFMSecurity();
                permanentFocusOwner2 = getPermanentFocusOwner();
                try {
                    fireVetoableChange("permanentFocusOwner", permanentFocusOwner2, component);
                    permanentFocusOwner = component;
                    setMostRecentFocusOwner(component);
                    z2 = true;
                } catch (PropertyVetoException e2) {
                    return;
                }
            }
        }
        if (z2) {
            firePropertyChange("permanentFocusOwner", permanentFocusOwner2, component);
        }
    }

    public Window getFocusedWindow() {
        synchronized (KeyboardFocusManager.class) {
            if (focusedWindow == null) {
                return null;
            }
            return focusedWindow.appContext == AppContext.getAppContext() ? focusedWindow : null;
        }
    }

    protected Window getGlobalFocusedWindow() throws SecurityException {
        Window window;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            window = focusedWindow;
        }
        return window;
    }

    protected void setGlobalFocusedWindow(Window window) throws SecurityException {
        Window focusedWindow2 = null;
        boolean z2 = false;
        if (window == null || window.isFocusableWindow()) {
            synchronized (KeyboardFocusManager.class) {
                checkKFMSecurity();
                focusedWindow2 = getFocusedWindow();
                try {
                    fireVetoableChange("focusedWindow", focusedWindow2, window);
                    focusedWindow = window;
                    z2 = true;
                } catch (PropertyVetoException e2) {
                    return;
                }
            }
        }
        if (z2) {
            firePropertyChange("focusedWindow", focusedWindow2, window);
        }
    }

    public Window getActiveWindow() {
        synchronized (KeyboardFocusManager.class) {
            if (activeWindow == null) {
                return null;
            }
            return activeWindow.appContext == AppContext.getAppContext() ? activeWindow : null;
        }
    }

    protected Window getGlobalActiveWindow() throws SecurityException {
        Window window;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            window = activeWindow;
        }
        return window;
    }

    protected void setGlobalActiveWindow(Window window) throws SecurityException {
        Window activeWindow2;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            activeWindow2 = getActiveWindow();
            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                focusLog.finer("Setting global active window to " + ((Object) window) + ", old active " + ((Object) activeWindow2));
            }
            try {
                fireVetoableChange("activeWindow", activeWindow2, window);
                activeWindow = window;
            } catch (PropertyVetoException e2) {
                return;
            }
        }
        firePropertyChange("activeWindow", activeWindow2, window);
    }

    public synchronized FocusTraversalPolicy getDefaultFocusTraversalPolicy() {
        return this.defaultPolicy;
    }

    public void setDefaultFocusTraversalPolicy(FocusTraversalPolicy focusTraversalPolicy) {
        FocusTraversalPolicy focusTraversalPolicy2;
        if (focusTraversalPolicy == null) {
            throw new IllegalArgumentException("default focus traversal policy cannot be null");
        }
        synchronized (this) {
            focusTraversalPolicy2 = this.defaultPolicy;
            this.defaultPolicy = focusTraversalPolicy;
        }
        firePropertyChange("defaultFocusTraversalPolicy", focusTraversalPolicy2, focusTraversalPolicy);
    }

    public void setDefaultFocusTraversalKeys(int i2, Set<? extends AWTKeyStroke> set) {
        Set<AWTKeyStroke> set2;
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        if (set == null) {
            throw new IllegalArgumentException("cannot set null Set of default focus traversal keys");
        }
        synchronized (this) {
            for (AWTKeyStroke aWTKeyStroke : set) {
                if (aWTKeyStroke == null) {
                    throw new IllegalArgumentException("cannot set null focus traversal key");
                }
                if (aWTKeyStroke.getKeyChar() != 65535) {
                    throw new IllegalArgumentException("focus traversal keys cannot map to KEY_TYPED events");
                }
                for (int i3 = 0; i3 < 4; i3++) {
                    if (i3 != i2 && this.defaultFocusTraversalKeys[i3].contains(aWTKeyStroke)) {
                        throw new IllegalArgumentException("focus traversal keys must be unique for a Component");
                    }
                }
            }
            set2 = this.defaultFocusTraversalKeys[i2];
            this.defaultFocusTraversalKeys[i2] = Collections.unmodifiableSet(new HashSet(set));
        }
        firePropertyChange(defaultFocusTraversalKeyPropertyNames[i2], set2, set);
    }

    public Set<AWTKeyStroke> getDefaultFocusTraversalKeys(int i2) {
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        return this.defaultFocusTraversalKeys[i2];
    }

    public Container getCurrentFocusCycleRoot() {
        synchronized (KeyboardFocusManager.class) {
            if (currentFocusCycleRoot == null) {
                return null;
            }
            return currentFocusCycleRoot.appContext == AppContext.getAppContext() ? currentFocusCycleRoot : null;
        }
    }

    protected Container getGlobalCurrentFocusCycleRoot() throws SecurityException {
        Container container;
        synchronized (KeyboardFocusManager.class) {
            checkKFMSecurity();
            container = currentFocusCycleRoot;
        }
        return container;
    }

    public void setGlobalCurrentFocusCycleRoot(Container container) throws SecurityException {
        Container currentFocusCycleRoot2;
        checkReplaceKFMPermission();
        synchronized (KeyboardFocusManager.class) {
            currentFocusCycleRoot2 = getCurrentFocusCycleRoot();
            currentFocusCycleRoot = container;
        }
        firePropertyChange("currentFocusCycleRoot", currentFocusCycleRoot2, container);
    }

    void setGlobalCurrentFocusCycleRootPriv(final Container container) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.KeyboardFocusManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                KeyboardFocusManager.this.setGlobalCurrentFocusCycleRoot(container);
                return null;
            }
        });
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            synchronized (this) {
                if (this.changeSupport == null) {
                    this.changeSupport = new PropertyChangeSupport(this);
                }
                this.changeSupport.addPropertyChangeListener(propertyChangeListener);
            }
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            synchronized (this) {
                if (this.changeSupport != null) {
                    this.changeSupport.removePropertyChangeListener(propertyChangeListener);
                }
            }
        }
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            this.changeSupport = new PropertyChangeSupport(this);
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            synchronized (this) {
                if (this.changeSupport == null) {
                    this.changeSupport = new PropertyChangeSupport(this);
                }
                this.changeSupport.addPropertyChangeListener(str, propertyChangeListener);
            }
        }
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            synchronized (this) {
                if (this.changeSupport != null) {
                    this.changeSupport.removePropertyChangeListener(str, propertyChangeListener);
                }
            }
        }
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String str) {
        if (this.changeSupport == null) {
            this.changeSupport = new PropertyChangeSupport(this);
        }
        return this.changeSupport.getPropertyChangeListeners(str);
    }

    protected void firePropertyChange(String str, Object obj, Object obj2) {
        PropertyChangeSupport propertyChangeSupport;
        if (obj != obj2 && (propertyChangeSupport = this.changeSupport) != null) {
            propertyChangeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    public void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener != null) {
            synchronized (this) {
                if (this.vetoableSupport == null) {
                    this.vetoableSupport = new VetoableChangeSupport(this);
                }
                this.vetoableSupport.addVetoableChangeListener(vetoableChangeListener);
            }
        }
    }

    public void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener != null) {
            synchronized (this) {
                if (this.vetoableSupport != null) {
                    this.vetoableSupport.removeVetoableChangeListener(vetoableChangeListener);
                }
            }
        }
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        if (this.vetoableSupport == null) {
            this.vetoableSupport = new VetoableChangeSupport(this);
        }
        return this.vetoableSupport.getVetoableChangeListeners();
    }

    public void addVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener != null) {
            synchronized (this) {
                if (this.vetoableSupport == null) {
                    this.vetoableSupport = new VetoableChangeSupport(this);
                }
                this.vetoableSupport.addVetoableChangeListener(str, vetoableChangeListener);
            }
        }
    }

    public void removeVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        if (vetoableChangeListener != null) {
            synchronized (this) {
                if (this.vetoableSupport != null) {
                    this.vetoableSupport.removeVetoableChangeListener(str, vetoableChangeListener);
                }
            }
        }
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners(String str) {
        if (this.vetoableSupport == null) {
            this.vetoableSupport = new VetoableChangeSupport(this);
        }
        return this.vetoableSupport.getVetoableChangeListeners(str);
    }

    protected void fireVetoableChange(String str, Object obj, Object obj2) throws PropertyVetoException {
        VetoableChangeSupport vetoableChangeSupport;
        if (obj != obj2 && (vetoableChangeSupport = this.vetoableSupport) != null) {
            vetoableChangeSupport.fireVetoableChange(str, obj, obj2);
        }
    }

    public void addKeyEventDispatcher(KeyEventDispatcher keyEventDispatcher) {
        if (keyEventDispatcher != null) {
            synchronized (this) {
                if (this.keyEventDispatchers == null) {
                    this.keyEventDispatchers = new LinkedList<>();
                }
                this.keyEventDispatchers.add(keyEventDispatcher);
            }
        }
    }

    public void removeKeyEventDispatcher(KeyEventDispatcher keyEventDispatcher) {
        if (keyEventDispatcher != null) {
            synchronized (this) {
                if (this.keyEventDispatchers != null) {
                    this.keyEventDispatchers.remove(keyEventDispatcher);
                }
            }
        }
    }

    protected synchronized java.util.List<KeyEventDispatcher> getKeyEventDispatchers() {
        if (this.keyEventDispatchers != null) {
            return (java.util.List) this.keyEventDispatchers.clone();
        }
        return null;
    }

    public void addKeyEventPostProcessor(KeyEventPostProcessor keyEventPostProcessor) {
        if (keyEventPostProcessor != null) {
            synchronized (this) {
                if (this.keyEventPostProcessors == null) {
                    this.keyEventPostProcessors = new LinkedList<>();
                }
                this.keyEventPostProcessors.add(keyEventPostProcessor);
            }
        }
    }

    public void removeKeyEventPostProcessor(KeyEventPostProcessor keyEventPostProcessor) {
        if (keyEventPostProcessor != null) {
            synchronized (this) {
                if (this.keyEventPostProcessors != null) {
                    this.keyEventPostProcessors.remove(keyEventPostProcessor);
                }
            }
        }
    }

    protected java.util.List<KeyEventPostProcessor> getKeyEventPostProcessors() {
        if (this.keyEventPostProcessors != null) {
            return (java.util.List) this.keyEventPostProcessors.clone();
        }
        return null;
    }

    static void setMostRecentFocusOwner(Component component) {
        Component component2;
        Component component3 = component;
        while (true) {
            component2 = component3;
            if (component2 == null || (component2 instanceof Window)) {
                break;
            } else {
                component3 = component2.parent;
            }
        }
        if (component2 != null) {
            setMostRecentFocusOwner((Window) component2, component);
        }
    }

    static synchronized void setMostRecentFocusOwner(Window window, Component component) {
        WeakReference<Component> weakReference = null;
        if (component != null) {
            weakReference = new WeakReference<>(component);
        }
        mostRecentFocusOwners.put(window, weakReference);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x004e A[Catch: all -> 0x0066, TryCatch #0 {, blocks: (B:25:0x0037, B:27:0x0042, B:30:0x004e, B:32:0x005b, B:34:0x0062), top: B:42:0x0037 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void clearMostRecentFocusOwner(java.awt.Component r3) {
        /*
            r0 = r3
            if (r0 != 0) goto L5
            return
        L5:
            r0 = r3
            java.lang.Object r0 = r0.getTreeLock()
            r1 = r0
            r5 = r1
            monitor-enter(r0)
            r0 = r3
            java.awt.Container r0 = r0.getParent()     // Catch: java.lang.Throwable -> L29
            r4 = r0
        L11:
            r0 = r4
            if (r0 == 0) goto L24
            r0 = r4
            boolean r0 = r0 instanceof java.awt.Window     // Catch: java.lang.Throwable -> L29
            if (r0 != 0) goto L24
            r0 = r4
            java.awt.Container r0 = r0.getParent()     // Catch: java.lang.Throwable -> L29
            r4 = r0
            goto L11
        L24:
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L29
            goto L2e
        L29:
            r6 = move-exception
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L29
            r0 = r6
            throw r0
        L2e:
            java.lang.Class<java.awt.KeyboardFocusManager> r0 = java.awt.KeyboardFocusManager.class
            r1 = r0
            r5 = r1
            monitor-enter(r0)
            r0 = r4
            if (r0 == 0) goto L4a
            r0 = r4
            java.awt.Window r0 = (java.awt.Window) r0     // Catch: java.lang.Throwable -> L66
            java.awt.Component r0 = getMostRecentFocusOwner(r0)     // Catch: java.lang.Throwable -> L66
            r1 = r3
            if (r0 != r1) goto L4a
            r0 = r4
            java.awt.Window r0 = (java.awt.Window) r0     // Catch: java.lang.Throwable -> L66
            r1 = 0
            setMostRecentFocusOwner(r0, r1)     // Catch: java.lang.Throwable -> L66
        L4a:
            r0 = r4
            if (r0 == 0) goto L61
            r0 = r4
            java.awt.Window r0 = (java.awt.Window) r0     // Catch: java.lang.Throwable -> L66
            r6 = r0
            r0 = r6
            java.awt.Component r0 = r0.getTemporaryLostComponent()     // Catch: java.lang.Throwable -> L66
            r1 = r3
            if (r0 != r1) goto L61
            r0 = r6
            r1 = 0
            java.awt.Component r0 = r0.setTemporaryLostComponent(r1)     // Catch: java.lang.Throwable -> L66
        L61:
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L66
            goto L6d
        L66:
            r7 = move-exception
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L66
            r0 = r7
            throw r0
        L6d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.KeyboardFocusManager.clearMostRecentFocusOwner(java.awt.Component):void");
    }

    static synchronized Component getMostRecentFocusOwner(Window window) {
        WeakReference<Component> weakReference = mostRecentFocusOwners.get(window);
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public final void redispatchEvent(Component component, AWTEvent aWTEvent) {
        aWTEvent.focusManagerIsDispatching = true;
        component.dispatchEvent(aWTEvent);
        aWTEvent.focusManagerIsDispatching = false;
    }

    public final void focusNextComponent() {
        Component focusOwner2 = getFocusOwner();
        if (focusOwner2 != null) {
            focusNextComponent(focusOwner2);
        }
    }

    public final void focusPreviousComponent() {
        Component focusOwner2 = getFocusOwner();
        if (focusOwner2 != null) {
            focusPreviousComponent(focusOwner2);
        }
    }

    public final void upFocusCycle() {
        Component focusOwner2 = getFocusOwner();
        if (focusOwner2 != null) {
            upFocusCycle(focusOwner2);
        }
    }

    public final void downFocusCycle() {
        Component focusOwner2 = getFocusOwner();
        if (focusOwner2 instanceof Container) {
            downFocusCycle((Container) focusOwner2);
        }
    }

    void dumpRequests() {
        System.err.println(">>> Requests dump, time: " + System.currentTimeMillis());
        synchronized (heavyweightRequests) {
            Iterator<HeavyweightFocusRequest> it = heavyweightRequests.iterator();
            while (it.hasNext()) {
                System.err.println(">>> Req: " + ((Object) it.next()));
            }
        }
        System.err.println("");
    }

    /* loaded from: rt.jar:java/awt/KeyboardFocusManager$LightweightFocusRequest.class */
    private static final class LightweightFocusRequest {
        final Component component;
        final boolean temporary;
        final CausedFocusEvent.Cause cause;

        LightweightFocusRequest(Component component, boolean z2, CausedFocusEvent.Cause cause) {
            this.component = component;
            this.temporary = z2;
            this.cause = cause;
        }

        public String toString() {
            return "LightweightFocusRequest[component=" + ((Object) this.component) + ",temporary=" + this.temporary + ", cause=" + ((Object) this.cause) + "]";
        }
    }

    /* loaded from: rt.jar:java/awt/KeyboardFocusManager$HeavyweightFocusRequest.class */
    private static final class HeavyweightFocusRequest {
        final Component heavyweight;
        final LinkedList<LightweightFocusRequest> lightweightRequests;
        static final HeavyweightFocusRequest CLEAR_GLOBAL_FOCUS_OWNER = new HeavyweightFocusRequest();

        private HeavyweightFocusRequest() {
            this.heavyweight = null;
            this.lightweightRequests = null;
        }

        HeavyweightFocusRequest(Component component, Component component2, boolean z2, CausedFocusEvent.Cause cause) {
            if (KeyboardFocusManager.log.isLoggable(PlatformLogger.Level.FINE) && component == null) {
                KeyboardFocusManager.log.fine("Assertion (heavyweight != null) failed");
            }
            this.heavyweight = component;
            this.lightweightRequests = new LinkedList<>();
            addLightweightRequest(component2, z2, cause);
        }

        boolean addLightweightRequest(Component component, boolean z2, CausedFocusEvent.Cause cause) {
            if (KeyboardFocusManager.log.isLoggable(PlatformLogger.Level.FINE)) {
                if (this == CLEAR_GLOBAL_FOCUS_OWNER) {
                    KeyboardFocusManager.log.fine("Assertion (this != HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) failed");
                }
                if (component == null) {
                    KeyboardFocusManager.log.fine("Assertion (descendant != null) failed");
                }
            }
            if (component != (this.lightweightRequests.size() > 0 ? this.lightweightRequests.getLast().component : null)) {
                this.lightweightRequests.add(new LightweightFocusRequest(component, z2, cause));
                return true;
            }
            return false;
        }

        LightweightFocusRequest getFirstLightweightRequest() {
            if (this == CLEAR_GLOBAL_FOCUS_OWNER) {
                return null;
            }
            return this.lightweightRequests.getFirst();
        }

        public String toString() {
            String str;
            boolean z2 = true;
            String str2 = "HeavyweightFocusRequest[heavweight=" + ((Object) this.heavyweight) + ",lightweightRequests=";
            if (this.lightweightRequests == null) {
                str = str2 + ((Object) null);
            } else {
                String str3 = str2 + "[";
                Iterator<LightweightFocusRequest> it = this.lightweightRequests.iterator();
                while (it.hasNext()) {
                    LightweightFocusRequest next = it.next();
                    if (z2) {
                        z2 = false;
                    } else {
                        str3 = str3 + ",";
                    }
                    str3 = str3 + ((Object) next);
                }
                str = str3 + "]";
            }
            return str + "]";
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v44, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v51, types: [java.lang.Throwable] */
    static boolean processSynchronousLightweightTransfer(Component component, Component component2, boolean z2, boolean z3, long j2) throws SecurityException {
        Window containingWindow = SunToolkit.getContainingWindow(component);
        if (containingWindow == null || !containingWindow.syncLWRequests) {
            return false;
        }
        if (component2 == null) {
            component2 = component;
        }
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager(SunToolkit.targetToAppContext(component2));
        FocusEvent focusEvent = null;
        FocusEvent focusEvent2 = null;
        Component globalFocusOwner = currentKeyboardFocusManager.getGlobalFocusOwner();
        synchronized (heavyweightRequests) {
            if (getLastHWRequest() == null && component == currentKeyboardFocusManager.getNativeFocusOwner() && allowSyncFocusRequests) {
                if (component2 == globalFocusOwner) {
                    return true;
                }
                currentKeyboardFocusManager.enqueueKeyEvents(j2, component2);
                heavyweightRequests.add(new HeavyweightFocusRequest(component, component2, z2, CausedFocusEvent.Cause.UNKNOWN));
                if (globalFocusOwner != null) {
                    focusEvent = new FocusEvent(globalFocusOwner, 1005, z2, component2);
                }
                focusEvent2 = new FocusEvent(component2, 1004, z2, globalFocusOwner);
            }
            boolean z4 = false;
            boolean z5 = clearingCurrentLightweightRequests;
            Error errorDispatchAndCatchException = null;
            try {
                clearingCurrentLightweightRequests = false;
                synchronized (Component.LOCK) {
                    if (focusEvent != null && globalFocusOwner != null) {
                        focusEvent.isPosted = true;
                        errorDispatchAndCatchException = dispatchAndCatchException(null, globalFocusOwner, focusEvent);
                        z4 = true;
                        if (focusEvent2 != null) {
                            focusEvent2.isPosted = true;
                            errorDispatchAndCatchException = dispatchAndCatchException(errorDispatchAndCatchException, component2, focusEvent2);
                            z4 = true;
                        }
                    } else if (focusEvent2 != null && component2 != null) {
                        focusEvent2.isPosted = true;
                        errorDispatchAndCatchException = dispatchAndCatchException(errorDispatchAndCatchException, component2, focusEvent2);
                        z4 = true;
                    }
                }
                if (errorDispatchAndCatchException instanceof RuntimeException) {
                    throw ((RuntimeException) errorDispatchAndCatchException);
                }
                if (errorDispatchAndCatchException instanceof Error) {
                    throw errorDispatchAndCatchException;
                }
                return z4;
            } finally {
                clearingCurrentLightweightRequests = z5;
            }
        }
    }

    static int shouldNativelyFocusHeavyweight(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) throws SecurityException {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            if (component == null) {
                log.fine("Assertion (heavyweight != null) failed");
            }
            if (j2 == 0) {
                log.fine("Assertion (time != 0) failed");
            }
        }
        if (component2 == null) {
            component2 = component;
        }
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager(SunToolkit.targetToAppContext(component2));
        KeyboardFocusManager currentKeyboardFocusManager2 = getCurrentKeyboardFocusManager();
        Component globalFocusOwner = currentKeyboardFocusManager2.getGlobalFocusOwner();
        Component nativeFocusOwner = currentKeyboardFocusManager2.getNativeFocusOwner();
        Window nativeFocusedWindow = currentKeyboardFocusManager2.getNativeFocusedWindow();
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("SNFH for {0} in {1}", String.valueOf(component2), String.valueOf(component));
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
            focusLog.finest("0. Current focus owner {0}", String.valueOf(globalFocusOwner));
            focusLog.finest("0. Native focus owner {0}", String.valueOf(nativeFocusOwner));
            focusLog.finest("0. Native focused window {0}", String.valueOf(nativeFocusedWindow));
        }
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest lastHWRequest = getLastHWRequest();
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("Request {0}", String.valueOf(lastHWRequest));
            }
            if (lastHWRequest == null && component == nativeFocusOwner && component.getContainingWindow() == nativeFocusedWindow) {
                if (component2 == globalFocusOwner) {
                    if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                        focusLog.finest("1. SNFH_FAILURE for {0}", String.valueOf(component2));
                    }
                    return 0;
                }
                currentKeyboardFocusManager.enqueueKeyEvents(j2, component2);
                heavyweightRequests.add(new HeavyweightFocusRequest(component, component2, z2, cause));
                if (globalFocusOwner != null) {
                    SunToolkit.postEvent(globalFocusOwner.appContext, new CausedFocusEvent(globalFocusOwner, 1005, z2, component2, cause));
                }
                SunToolkit.postEvent(component2.appContext, new CausedFocusEvent(component2, 1004, z2, globalFocusOwner, cause));
                if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                    focusLog.finest("2. SNFH_HANDLED for {0}", String.valueOf(component2));
                }
                return 1;
            }
            if (lastHWRequest != null && lastHWRequest.heavyweight == component) {
                if (lastHWRequest.addLightweightRequest(component2, z2, cause)) {
                    currentKeyboardFocusManager.enqueueKeyEvents(j2, component2);
                }
                if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                    focusLog.finest("3. SNFH_HANDLED for lightweight" + ((Object) component2) + " in " + ((Object) component));
                }
                return 1;
            }
            if (!z3) {
                if (lastHWRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) {
                    int size = heavyweightRequests.size();
                    lastHWRequest = size >= 2 ? heavyweightRequests.get(size - 2) : null;
                }
                if (focusedWindowChanged(component, lastHWRequest != null ? lastHWRequest.heavyweight : nativeFocusedWindow)) {
                    if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                        focusLog.finest("4. SNFH_FAILURE for " + ((Object) component2));
                    }
                    return 0;
                }
            }
            currentKeyboardFocusManager.enqueueKeyEvents(j2, component2);
            heavyweightRequests.add(new HeavyweightFocusRequest(component, component2, z2, cause));
            if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                focusLog.finest("5. SNFH_PROCEED for " + ((Object) component2));
            }
            return 2;
        }
    }

    static Window markClearGlobalFocusOwner() {
        Window nativeFocusedWindow = getCurrentKeyboardFocusManager().getNativeFocusedWindow();
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest lastHWRequest = getLastHWRequest();
            if (lastHWRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) {
                return null;
            }
            heavyweightRequests.add(HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER);
            Window containingWindow = lastHWRequest != null ? SunToolkit.getContainingWindow(lastHWRequest.heavyweight) : nativeFocusedWindow;
            while (containingWindow != null && !(containingWindow instanceof Frame) && !(containingWindow instanceof Dialog)) {
                containingWindow = containingWindow.getParent_NoClientCode();
            }
            return containingWindow;
        }
    }

    Component getCurrentWaitingRequest(Component component) {
        LightweightFocusRequest first;
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest firstHWRequest = getFirstHWRequest();
            if (firstHWRequest != null && firstHWRequest.heavyweight == component && (first = firstHWRequest.lightweightRequests.getFirst()) != null) {
                return first.component;
            }
            return null;
        }
    }

    static boolean isAutoFocusTransferEnabled() {
        boolean z2;
        synchronized (heavyweightRequests) {
            z2 = heavyweightRequests.size() == 0 && !disableRestoreFocus && null == currentLightweightRequests;
        }
        return z2;
    }

    static boolean isAutoFocusTransferEnabledFor(Component component) {
        return isAutoFocusTransferEnabled() && component.isAutoFocusTransferOnDisposal();
    }

    private static Throwable dispatchAndCatchException(Throwable th, Component component, FocusEvent focusEvent) {
        Throwable th2 = null;
        try {
            component.dispatchEvent(focusEvent);
        } catch (Error e2) {
            th2 = e2;
        } catch (RuntimeException e3) {
            th2 = e3;
        }
        if (th2 != null) {
            if (th != null) {
                handleException(th);
            }
            return th2;
        }
        return th;
    }

    private static void handleException(Throwable th) {
        th.printStackTrace();
    }

    /* JADX WARN: Finally extract failed */
    static void processCurrentLightweightRequests() throws SecurityException {
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager();
        Component globalFocusOwner = currentKeyboardFocusManager.getGlobalFocusOwner();
        if (globalFocusOwner != null && globalFocusOwner.appContext != AppContext.getAppContext()) {
            return;
        }
        synchronized (heavyweightRequests) {
            if (currentLightweightRequests != null) {
                clearingCurrentLightweightRequests = true;
                disableRestoreFocus = true;
                LinkedList<LightweightFocusRequest> linkedList = currentLightweightRequests;
                allowSyncFocusRequests = linkedList.size() < 2;
                currentLightweightRequests = null;
                Throwable thDispatchAndCatchException = null;
                if (linkedList != null) {
                    try {
                        Component component = null;
                        Iterator<LightweightFocusRequest> it = linkedList.iterator();
                        while (it.hasNext()) {
                            Component globalFocusOwner2 = currentKeyboardFocusManager.getGlobalFocusOwner();
                            LightweightFocusRequest next = it.next();
                            if (!it.hasNext()) {
                                disableRestoreFocus = false;
                            }
                            CausedFocusEvent causedFocusEvent = null;
                            if (globalFocusOwner2 != null) {
                                causedFocusEvent = new CausedFocusEvent(globalFocusOwner2, 1005, next.temporary, next.component, next.cause);
                            }
                            CausedFocusEvent causedFocusEvent2 = new CausedFocusEvent(next.component, 1004, next.temporary, globalFocusOwner2 == null ? component : globalFocusOwner2, next.cause);
                            if (globalFocusOwner2 != null) {
                                causedFocusEvent.isPosted = true;
                                thDispatchAndCatchException = dispatchAndCatchException(thDispatchAndCatchException, globalFocusOwner2, causedFocusEvent);
                            }
                            causedFocusEvent2.isPosted = true;
                            thDispatchAndCatchException = dispatchAndCatchException(thDispatchAndCatchException, next.component, causedFocusEvent2);
                            if (currentKeyboardFocusManager.getGlobalFocusOwner() == next.component) {
                                component = next.component;
                            }
                        }
                    } catch (Throwable th) {
                        clearingCurrentLightweightRequests = false;
                        disableRestoreFocus = false;
                        allowSyncFocusRequests = true;
                        throw th;
                    }
                }
                clearingCurrentLightweightRequests = false;
                disableRestoreFocus = false;
                allowSyncFocusRequests = true;
                if (thDispatchAndCatchException instanceof RuntimeException) {
                    throw ((RuntimeException) thDispatchAndCatchException);
                }
                if (thDispatchAndCatchException instanceof Error) {
                    throw ((Error) thDispatchAndCatchException);
                }
            }
        }
    }

    static FocusEvent retargetUnexpectedFocusEvent(FocusEvent focusEvent) {
        synchronized (heavyweightRequests) {
            if (removeFirstRequest()) {
                return (FocusEvent) retargetFocusEvent(focusEvent);
            }
            Component component = focusEvent.getComponent();
            Component oppositeComponent = focusEvent.getOppositeComponent();
            boolean z2 = false;
            if (focusEvent.getID() == 1005 && (oppositeComponent == null || isTemporary(oppositeComponent, component))) {
                z2 = true;
            }
            return new CausedFocusEvent(component, focusEvent.getID(), z2, oppositeComponent, CausedFocusEvent.Cause.NATIVE_SYSTEM);
        }
    }

    static FocusEvent retargetFocusGained(FocusEvent focusEvent) throws SecurityException {
        if (!$assertionsDisabled && focusEvent.getID() != 1004) {
            throw new AssertionError();
        }
        Component globalFocusOwner = getCurrentKeyboardFocusManager().getGlobalFocusOwner();
        Component component = focusEvent.getComponent();
        Component oppositeComponent = focusEvent.getOppositeComponent();
        Component heavyweight = getHeavyweight(component);
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest firstHWRequest = getFirstHWRequest();
            if (firstHWRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) {
                return retargetUnexpectedFocusEvent(focusEvent);
            }
            if (component != null && heavyweight == null && firstHWRequest != null && component == firstHWRequest.getFirstLightweightRequest().component) {
                component = firstHWRequest.heavyweight;
                heavyweight = component;
            }
            if (firstHWRequest != null && heavyweight == firstHWRequest.heavyweight) {
                heavyweightRequests.removeFirst();
                LightweightFocusRequest lightweightFocusRequestRemoveFirst = firstHWRequest.lightweightRequests.removeFirst();
                Component component2 = lightweightFocusRequestRemoveFirst.component;
                if (globalFocusOwner != null) {
                    newFocusOwner = component2;
                }
                boolean z2 = (oppositeComponent == null || isTemporary(component2, oppositeComponent)) ? false : lightweightFocusRequestRemoveFirst.temporary;
                if (firstHWRequest.lightweightRequests.size() > 0) {
                    currentLightweightRequests = firstHWRequest.lightweightRequests;
                    EventQueue.invokeLater(new Runnable() { // from class: java.awt.KeyboardFocusManager.4
                        @Override // java.lang.Runnable
                        public void run() throws SecurityException {
                            KeyboardFocusManager.processCurrentLightweightRequests();
                        }
                    });
                }
                return new CausedFocusEvent(component2, 1004, z2, oppositeComponent, lightweightFocusRequestRemoveFirst.cause);
            }
            if (globalFocusOwner != null && globalFocusOwner.getContainingWindow() == component && (firstHWRequest == null || component != firstHWRequest.heavyweight)) {
                return new CausedFocusEvent(globalFocusOwner, 1004, false, null, CausedFocusEvent.Cause.ACTIVATION);
            }
            return retargetUnexpectedFocusEvent(focusEvent);
        }
    }

    static FocusEvent retargetFocusLost(FocusEvent focusEvent) throws SecurityException {
        if (!$assertionsDisabled && focusEvent.getID() != 1005) {
            throw new AssertionError();
        }
        Component globalFocusOwner = getCurrentKeyboardFocusManager().getGlobalFocusOwner();
        Component oppositeComponent = focusEvent.getOppositeComponent();
        Component heavyweight = getHeavyweight(oppositeComponent);
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest firstHWRequest = getFirstHWRequest();
            if (firstHWRequest == HeavyweightFocusRequest.CLEAR_GLOBAL_FOCUS_OWNER) {
                if (globalFocusOwner != null) {
                    heavyweightRequests.removeFirst();
                    return new CausedFocusEvent(globalFocusOwner, 1005, false, null, CausedFocusEvent.Cause.CLEAR_GLOBAL_FOCUS_OWNER);
                }
            } else {
                if (oppositeComponent == null) {
                    if (globalFocusOwner != null) {
                        return new CausedFocusEvent(globalFocusOwner, 1005, true, null, CausedFocusEvent.Cause.ACTIVATION);
                    }
                    return focusEvent;
                }
                if (firstHWRequest != null && (heavyweight == firstHWRequest.heavyweight || (heavyweight == null && oppositeComponent == firstHWRequest.getFirstLightweightRequest().component))) {
                    if (globalFocusOwner == null) {
                        return focusEvent;
                    }
                    LightweightFocusRequest first = firstHWRequest.lightweightRequests.getFirst();
                    return new CausedFocusEvent(globalFocusOwner, 1005, isTemporary(oppositeComponent, globalFocusOwner) ? true : first.temporary, first.component, first.cause);
                }
                if (focusedWindowChanged(oppositeComponent, globalFocusOwner)) {
                    if (!focusEvent.isTemporary() && globalFocusOwner != null) {
                        focusEvent = new CausedFocusEvent(globalFocusOwner, 1005, true, oppositeComponent, CausedFocusEvent.Cause.ACTIVATION);
                    }
                    return focusEvent;
                }
            }
            return retargetUnexpectedFocusEvent(focusEvent);
        }
    }

    static AWTEvent retargetFocusEvent(AWTEvent aWTEvent) {
        if (clearingCurrentLightweightRequests) {
            return aWTEvent;
        }
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager();
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            if ((aWTEvent instanceof FocusEvent) || (aWTEvent instanceof WindowEvent)) {
                focusLog.finer(">>> {0}", String.valueOf(aWTEvent));
            }
            if (focusLog.isLoggable(PlatformLogger.Level.FINER) && (aWTEvent instanceof KeyEvent)) {
                focusLog.finer("    focus owner is {0}", String.valueOf(currentKeyboardFocusManager.getGlobalFocusOwner()));
                focusLog.finer(">>> {0}", String.valueOf(aWTEvent));
            }
        }
        synchronized (heavyweightRequests) {
            if (newFocusOwner != null && aWTEvent.getID() == 1005) {
                FocusEvent focusEvent = (FocusEvent) aWTEvent;
                if (currentKeyboardFocusManager.getGlobalFocusOwner() == focusEvent.getComponent() && focusEvent.getOppositeComponent() == newFocusOwner) {
                    newFocusOwner = null;
                    return aWTEvent;
                }
            }
            processCurrentLightweightRequests();
            switch (aWTEvent.getID()) {
                case 1004:
                    aWTEvent = retargetFocusGained((FocusEvent) aWTEvent);
                    break;
                case 1005:
                    aWTEvent = retargetFocusLost((FocusEvent) aWTEvent);
                    break;
            }
            return aWTEvent;
        }
    }

    void clearMarkers() {
    }

    static boolean removeFirstRequest() {
        boolean z2;
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager();
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest firstHWRequest = getFirstHWRequest();
            if (firstHWRequest != null) {
                heavyweightRequests.removeFirst();
                if (firstHWRequest.lightweightRequests != null) {
                    Iterator<LightweightFocusRequest> it = firstHWRequest.lightweightRequests.iterator();
                    while (it.hasNext()) {
                        currentKeyboardFocusManager.dequeueKeyEvents(-1L, it.next().component);
                    }
                }
            }
            if (heavyweightRequests.size() == 0) {
                currentKeyboardFocusManager.clearMarkers();
            }
            z2 = heavyweightRequests.size() > 0;
        }
        return z2;
    }

    static void removeLastFocusRequest(Component component) {
        if (log.isLoggable(PlatformLogger.Level.FINE) && component == null) {
            log.fine("Assertion (heavyweight != null) failed");
        }
        KeyboardFocusManager currentKeyboardFocusManager = getCurrentKeyboardFocusManager();
        synchronized (heavyweightRequests) {
            HeavyweightFocusRequest lastHWRequest = getLastHWRequest();
            if (lastHWRequest != null && lastHWRequest.heavyweight == component) {
                heavyweightRequests.removeLast();
            }
            if (heavyweightRequests.size() == 0) {
                currentKeyboardFocusManager.clearMarkers();
            }
        }
    }

    private static boolean focusedWindowChanged(Component component, Component component2) {
        Window containingWindow = SunToolkit.getContainingWindow(component);
        Window containingWindow2 = SunToolkit.getContainingWindow(component2);
        return (containingWindow == null && containingWindow2 == null) || containingWindow == null || containingWindow2 == null || containingWindow != containingWindow2;
    }

    private static boolean isTemporary(Component component, Component component2) {
        Window containingWindow = SunToolkit.getContainingWindow(component);
        Window containingWindow2 = SunToolkit.getContainingWindow(component2);
        if (containingWindow == null && containingWindow2 == null) {
            return false;
        }
        if (containingWindow == null) {
            return true;
        }
        return (containingWindow2 == null || containingWindow == containingWindow2) ? false : true;
    }

    static Component getHeavyweight(Component component) {
        if (component == null || component.getPeer() == null) {
            return null;
        }
        if (component.getPeer() instanceof LightweightPeer) {
            return component.getNativeContainer();
        }
        return component;
    }

    private static boolean isProxyActiveImpl(KeyEvent keyEvent) {
        if (proxyActive == null) {
            proxyActive = (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: java.awt.KeyboardFocusManager.5
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !KeyboardFocusManager.class.desiredAssertionStatus();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Field run2() throws SecurityException {
                    Field declaredField = null;
                    try {
                        declaredField = KeyEvent.class.getDeclaredField("isProxyActive");
                        if (declaredField != null) {
                            declaredField.setAccessible(true);
                        }
                    } catch (NoSuchFieldException e2) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    }
                    return declaredField;
                }
            });
        }
        try {
            return proxyActive.getBoolean(keyEvent);
        } catch (IllegalAccessException e2) {
            if ($assertionsDisabled) {
                return false;
            }
            throw new AssertionError();
        }
    }

    static boolean isProxyActive(KeyEvent keyEvent) {
        if (!GraphicsEnvironment.isHeadless()) {
            return isProxyActiveImpl(keyEvent);
        }
        return false;
    }

    private static HeavyweightFocusRequest getLastHWRequest() {
        HeavyweightFocusRequest last;
        synchronized (heavyweightRequests) {
            last = heavyweightRequests.size() > 0 ? heavyweightRequests.getLast() : null;
        }
        return last;
    }

    private static HeavyweightFocusRequest getFirstHWRequest() {
        HeavyweightFocusRequest first;
        synchronized (heavyweightRequests) {
            first = heavyweightRequests.size() > 0 ? heavyweightRequests.getFirst() : null;
        }
        return first;
    }

    private static void checkReplaceKFMPermission() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (replaceKeyboardFocusManagerPermission == null) {
                replaceKeyboardFocusManagerPermission = new AWTPermission("replaceKeyboardFocusManager");
            }
            securityManager.checkPermission(replaceKeyboardFocusManagerPermission);
        }
    }

    private void checkKFMSecurity() throws SecurityException {
        if (this != getCurrentKeyboardFocusManager()) {
            checkReplaceKFMPermission();
        }
    }
}
