package java.awt;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.im.InputContext;
import java.awt.image.BufferStrategy;
import java.awt.peer.WindowPeer;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;
import sun.awt.util.IdentityArrayList;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.java2d.pipe.Region;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: rt.jar:java/awt/Window.class */
public class Window extends Container implements Accessible {
    String warningString;
    transient java.util.List<Image> icons;
    private transient Component temporaryLostComponent;
    static boolean systemSyncLWRequests;
    boolean syncLWRequests;
    transient boolean beforeFirstShow;
    private transient boolean disposing;
    transient WindowDisposerRecord disposerRecord;
    static final int OPENED = 1;
    int state;
    private boolean alwaysOnTop;
    transient Vector<WeakReference<Window>> ownedWindowList;
    private transient WeakReference<Window> weakThis;
    transient boolean showWithParent;
    transient Dialog modalBlocker;
    Dialog.ModalExclusionType modalExclusionType;
    transient WindowListener windowListener;
    transient WindowStateListener windowStateListener;
    transient WindowFocusListener windowFocusListener;
    transient InputContext inputContext;
    private transient Object inputContextLock;
    private FocusManager focusMgr;
    private boolean focusableWindowState;
    private volatile boolean autoRequestFocus;
    transient boolean isInShow;
    private volatile float opacity;
    private Shape shape;
    private static final String base = "win";
    private static final long serialVersionUID = 4497834738069338734L;
    private static final boolean locationByPlatformProp;
    transient boolean isTrayIconWindow;
    private volatile transient int securityWarningWidth;
    private volatile transient int securityWarningHeight;
    private transient double securityWarningPointX;
    private transient double securityWarningPointY;
    private transient float securityWarningAlignmentX;
    private transient float securityWarningAlignmentY;
    transient Object anchor;
    private static final AtomicBoolean beforeFirstWindowShown;
    private Type type;
    private int windowSerializedDataVersion;
    private volatile boolean locationByPlatform;
    private static final IdentityArrayList<Window> allWindows = new IdentityArrayList<>();
    private static int nameCounter = 0;
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.Window");

    /* loaded from: rt.jar:java/awt/Window$Type.class */
    public enum Type {
        NORMAL,
        UTILITY,
        POPUP
    }

    private static native void initIDs();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$802(java.awt.Window r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.securityWarningPointX = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Window.access$802(java.awt.Window, double):double");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$902(java.awt.Window r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.securityWarningPointY = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Window.access$902(java.awt.Window, double):double");
    }

    static {
        systemSyncLWRequests = false;
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.awt.syncLWRequests"));
        systemSyncLWRequests = str != null && str.equals("true");
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("java.awt.Window.locationByPlatform"));
        locationByPlatformProp = str2 != null && str2.equals("true");
        beforeFirstWindowShown = new AtomicBoolean(true);
        AWTAccessor.setWindowAccessor(new AWTAccessor.WindowAccessor() { // from class: java.awt.Window.1
            @Override // sun.awt.AWTAccessor.WindowAccessor
            public float getOpacity(Window window) {
                return window.opacity;
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setOpacity(Window window, float f2) {
                window.setOpacity(f2);
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public Shape getShape(Window window) {
                return window.getShape();
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setShape(Window window, Shape shape) {
                window.setShape(shape);
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setOpaque(Window window, boolean z2) {
                Color background = window.getBackground();
                if (background == null) {
                    background = new Color(0, 0, 0, 0);
                }
                window.setBackground(new Color(background.getRed(), background.getGreen(), background.getBlue(), z2 ? 255 : 0));
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void updateWindow(Window window) {
                window.updateWindow();
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public Dimension getSecurityWarningSize(Window window) {
                return new Dimension(window.securityWarningWidth, window.securityWarningHeight);
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setSecurityWarningSize(Window window, int i2, int i3) {
                window.securityWarningWidth = i2;
                window.securityWarningHeight = i3;
            }

            /* JADX WARN: Failed to check method for inline after forced processjava.awt.Window.access$802(java.awt.Window, double):double */
            /* JADX WARN: Failed to check method for inline after forced processjava.awt.Window.access$902(java.awt.Window, double):double */
            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setSecurityWarningPosition(Window window, Point2D point2D, float f2, float f3) {
                Window.access$802(window, point2D.getX());
                Window.access$902(window, point2D.getY());
                window.securityWarningAlignmentX = f2;
                window.securityWarningAlignmentY = f3;
                synchronized (window.getTreeLock()) {
                    WindowPeer windowPeer = (WindowPeer) window.getPeer();
                    if (windowPeer != null) {
                        windowPeer.repositionSecurityWarning();
                    }
                }
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public Point2D calculateSecurityWarningPosition(Window window, double d2, double d3, double d4, double d5) {
                return window.calculateSecurityWarningPosition(d2, d3, d4, d5);
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setLWRequestStatus(Window window, boolean z2) {
                window.syncLWRequests = z2;
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public boolean isAutoRequestFocus(Window window) {
                return window.autoRequestFocus;
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public boolean isTrayIconWindow(Window window) {
                return window.isTrayIconWindow;
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public void setTrayIconWindow(Window window, boolean z2) {
                window.isTrayIconWindow = z2;
            }

            @Override // sun.awt.AWTAccessor.WindowAccessor
            public Window[] getOwnedWindows(Window window) {
                return window.getOwnedWindows_NoClientCode();
            }
        });
    }

    Window(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        this.syncLWRequests = false;
        this.beforeFirstShow = true;
        this.disposing = false;
        this.disposerRecord = null;
        this.ownedWindowList = new Vector<>();
        this.inputContextLock = new Object();
        this.focusableWindowState = true;
        this.autoRequestFocus = true;
        this.isInShow = false;
        this.opacity = 1.0f;
        this.shape = null;
        this.isTrayIconWindow = false;
        this.securityWarningWidth = 0;
        this.securityWarningHeight = 0;
        this.securityWarningPointX = 2.0d;
        this.securityWarningPointY = 0.0d;
        this.securityWarningAlignmentX = 1.0f;
        this.securityWarningAlignmentY = 0.0f;
        this.anchor = new Object();
        this.type = Type.NORMAL;
        this.windowSerializedDataVersion = 2;
        this.locationByPlatform = locationByPlatformProp;
        init(graphicsConfiguration);
    }

    /* loaded from: rt.jar:java/awt/Window$WindowDisposerRecord.class */
    static class WindowDisposerRecord implements DisposerRecord {
        WeakReference<Window> owner;
        final WeakReference<Window> weakThis;
        final WeakReference<AppContext> context;

        WindowDisposerRecord(AppContext appContext, Window window) {
            this.weakThis = window.weakThis;
            this.context = new WeakReference<>(appContext);
        }

        public void updateOwner() {
            Window window = this.weakThis.get();
            this.owner = window == null ? null : new WeakReference<>(window.getOwner());
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            Window window;
            if (this.owner != null && (window = this.owner.get()) != null) {
                window.removeOwnedWindow(this.weakThis);
            }
            AppContext appContext = this.context.get();
            if (null != appContext) {
                Window.removeFromWindowList(appContext, this.weakThis);
            }
        }
    }

    private GraphicsConfiguration initGC(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (graphicsConfiguration == null) {
            graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        setGraphicsConfiguration(graphicsConfiguration);
        return graphicsConfiguration;
    }

    private void init(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        this.syncLWRequests = systemSyncLWRequests;
        this.weakThis = new WeakReference<>(this);
        addToWindowList();
        setWarningString();
        this.cursor = Cursor.getPredefinedCursor(0);
        this.visible = false;
        GraphicsConfiguration graphicsConfigurationInitGC = initGC(graphicsConfiguration);
        if (graphicsConfigurationInitGC.getDevice().getType() != 0) {
            throw new IllegalArgumentException("not a screen device");
        }
        setLayout(new BorderLayout());
        Rectangle bounds = graphicsConfigurationInitGC.getBounds();
        Insets screenInsets = getToolkit().getScreenInsets(graphicsConfigurationInitGC);
        int x2 = getX() + bounds.f12372x + screenInsets.left;
        int y2 = getY() + bounds.f12373y + screenInsets.top;
        if (x2 != this.f12361x || y2 != this.f12362y) {
            setLocation(x2, y2);
            setLocationByPlatform(locationByPlatformProp);
        }
        this.modalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
        this.disposerRecord = new WindowDisposerRecord(this.appContext, this);
        Disposer.addRecord(this.anchor, this.disposerRecord);
        SunToolkit.checkAndSetPolicy(this);
    }

    Window() throws HeadlessException {
        this.syncLWRequests = false;
        this.beforeFirstShow = true;
        this.disposing = false;
        this.disposerRecord = null;
        this.ownedWindowList = new Vector<>();
        this.inputContextLock = new Object();
        this.focusableWindowState = true;
        this.autoRequestFocus = true;
        this.isInShow = false;
        this.opacity = 1.0f;
        this.shape = null;
        this.isTrayIconWindow = false;
        this.securityWarningWidth = 0;
        this.securityWarningHeight = 0;
        this.securityWarningPointX = 2.0d;
        this.securityWarningPointY = 0.0d;
        this.securityWarningAlignmentX = 1.0f;
        this.securityWarningAlignmentY = 0.0f;
        this.anchor = new Object();
        this.type = Type.NORMAL;
        this.windowSerializedDataVersion = 2;
        this.locationByPlatform = locationByPlatformProp;
        GraphicsEnvironment.checkHeadless();
        init((GraphicsConfiguration) null);
    }

    public Window(Frame frame) {
        this(frame == null ? (GraphicsConfiguration) null : frame.getGraphicsConfiguration());
        ownedInit(frame);
    }

    public Window(Window window) {
        this(window == null ? (GraphicsConfiguration) null : window.getGraphicsConfiguration());
        ownedInit(window);
    }

    public Window(Window window, GraphicsConfiguration graphicsConfiguration) {
        this(graphicsConfiguration);
        ownedInit(window);
    }

    private void ownedInit(Window window) {
        this.parent = window;
        if (window != null) {
            window.addOwnedWindow(this.weakThis);
            if (window.isAlwaysOnTop()) {
                try {
                    setAlwaysOnTop(true);
                } catch (SecurityException e2) {
                }
            }
        }
        this.disposerRecord.updateOwner();
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Window.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    public java.util.List<Image> getIconImages() {
        java.util.List<Image> list = this.icons;
        if (list == null || list.size() == 0) {
            return new ArrayList();
        }
        return new ArrayList(list);
    }

    public synchronized void setIconImages(java.util.List<? extends Image> list) {
        this.icons = list == null ? new ArrayList() : new ArrayList(list);
        WindowPeer windowPeer = (WindowPeer) this.peer;
        if (windowPeer != null) {
            windowPeer.updateIconImages();
        }
        firePropertyChange("iconImage", (Object) null, (Object) null);
    }

    public void setIconImage(Image image) {
        ArrayList arrayList = new ArrayList();
        if (image != null) {
            arrayList.add(image);
        }
        setIconImages(arrayList);
    }

    @Override // java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            Container container = this.parent;
            if (container != null && container.getPeer() == null) {
                container.addNotify();
            }
            if (this.peer == null) {
                this.peer = getToolkit().createWindow(this);
            }
            synchronized (allWindows) {
                allWindows.add(this);
            }
            super.addNotify();
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void removeNotify() {
        synchronized (getTreeLock()) {
            synchronized (allWindows) {
                allWindows.remove(this);
            }
            super.removeNotify();
        }
    }

    public void pack() {
        Container container = this.parent;
        if (container != null && container.getPeer() == null) {
            container.addNotify();
        }
        if (this.peer == null) {
            addNotify();
        }
        Dimension preferredSize = getPreferredSize();
        if (this.peer != null) {
            setClientSize(preferredSize.width, preferredSize.height);
        }
        if (this.beforeFirstShow) {
            this.isPacked = true;
        }
        validateUnconditionally();
    }

    @Override // java.awt.Component
    public void setMinimumSize(Dimension dimension) {
        synchronized (getTreeLock()) {
            super.setMinimumSize(dimension);
            Dimension size = getSize();
            if (isMinimumSizeSet() && (size.width < dimension.width || size.height < dimension.height)) {
                setSize(Math.max(this.width, dimension.width), Math.max(this.height, dimension.height));
            }
            if (this.peer != null) {
                ((WindowPeer) this.peer).updateMinimumSize();
            }
        }
    }

    @Override // java.awt.Component
    public void setSize(Dimension dimension) {
        super.setSize(dimension);
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        super.setSize(i2, i3);
    }

    @Override // java.awt.Component
    public void setLocation(int i2, int i3) {
        super.setLocation(i2, i3);
    }

    @Override // java.awt.Component
    public void setLocation(Point point) {
        super.setLocation(point);
    }

    @Override // java.awt.Component
    @Deprecated
    public void reshape(int i2, int i3, int i4, int i5) {
        if (isMinimumSizeSet()) {
            Dimension minimumSize = getMinimumSize();
            if (i4 < minimumSize.width) {
                i4 = minimumSize.width;
            }
            if (i5 < minimumSize.height) {
                i5 = minimumSize.height;
            }
        }
        super.reshape(i2, i3, i4, i5);
    }

    void setClientSize(int i2, int i3) {
        synchronized (getTreeLock()) {
            setBoundsOp(4);
            setBounds(this.f12361x, this.f12362y, i2, i3);
        }
    }

    final void closeSplashScreen() {
        if (!this.isTrayIconWindow && beforeFirstWindowShown.getAndSet(false)) {
            SunToolkit.closeSplashScreen();
            SplashScreen.markClosed();
        }
    }

    @Override // java.awt.Component
    public void setVisible(boolean z2) {
        super.setVisible(z2);
    }

    @Override // java.awt.Component
    @Deprecated
    public void show() {
        if (this.peer == null) {
            addNotify();
        }
        validateUnconditionally();
        this.isInShow = true;
        if (this.visible) {
            toFront();
        } else {
            this.beforeFirstShow = false;
            closeSplashScreen();
            Dialog.checkShouldBeBlocked(this);
            super.show();
            this.locationByPlatform = false;
            for (int i2 = 0; i2 < this.ownedWindowList.size(); i2++) {
                Window window = this.ownedWindowList.elementAt(i2).get();
                if (window != null && window.showWithParent) {
                    window.show();
                    window.showWithParent = false;
                }
            }
            if (!isModalBlocked()) {
                updateChildrenBlocking();
            } else {
                this.modalBlocker.toFront_NoClientCode();
            }
            if ((this instanceof Frame) || (this instanceof Dialog)) {
                updateChildFocusableWindowState(this);
            }
        }
        this.isInShow = false;
        if ((this.state & 1) == 0) {
            postWindowEvent(200);
            this.state |= 1;
        }
    }

    static void updateChildFocusableWindowState(Window window) {
        if (window.getPeer() != null && window.isShowing()) {
            ((WindowPeer) window.getPeer()).updateFocusableWindowState();
        }
        for (int i2 = 0; i2 < window.ownedWindowList.size(); i2++) {
            Window window2 = window.ownedWindowList.elementAt(i2).get();
            if (window2 != null) {
                updateChildFocusableWindowState(window2);
            }
        }
    }

    synchronized void postWindowEvent(int i2) {
        if (this.windowListener != null || (this.eventMask & 64) != 0 || Toolkit.enabledOnToolkit(64L)) {
            Toolkit.getEventQueue().postEvent(new WindowEvent(this, i2));
        }
    }

    @Override // java.awt.Component
    @Deprecated
    public void hide() {
        synchronized (this.ownedWindowList) {
            for (int i2 = 0; i2 < this.ownedWindowList.size(); i2++) {
                Window window = this.ownedWindowList.elementAt(i2).get();
                if (window != null && window.visible) {
                    window.hide();
                    window.showWithParent = true;
                }
            }
        }
        if (isModalBlocked()) {
            this.modalBlocker.unblockWindow(this);
        }
        super.hide();
        this.locationByPlatform = false;
    }

    @Override // java.awt.Container, java.awt.Component
    final void clearMostRecentFocusOwnerOnHide() {
    }

    public void dispose() {
        doDispose();
    }

    void disposeImpl() {
        dispose();
        if (getPeer() != null) {
            doDispose();
        }
    }

    void doDispose() {
        boolean zIsDisplayable = isDisplayable();
        Runnable runnable = new Runnable() { // from class: java.awt.Window.1DisposeAction
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                Object[] objArr;
                Window.this.disposing = true;
                try {
                    GraphicsDevice device = Window.this.getGraphicsConfiguration().getDevice();
                    if (device.getFullScreenWindow() == Window.this) {
                        device.setFullScreenWindow(null);
                    }
                    synchronized (Window.this.ownedWindowList) {
                        objArr = new Object[Window.this.ownedWindowList.size()];
                        Window.this.ownedWindowList.copyInto(objArr);
                    }
                    for (Object obj : objArr) {
                        Window window = (Window) ((WeakReference) obj).get();
                        if (window != null) {
                            window.disposeImpl();
                        }
                    }
                    Window.this.hide();
                    Window.this.beforeFirstShow = true;
                    Window.this.removeNotify();
                    synchronized (Window.this.inputContextLock) {
                        if (Window.this.inputContext != null) {
                            Window.this.inputContext.dispose();
                            Window.this.inputContext = null;
                        }
                    }
                    Window.this.clearCurrentFocusCycleRootOnHide();
                    Window.this.disposing = false;
                } catch (Throwable th) {
                    Window.this.disposing = false;
                    throw th;
                }
            }
        };
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            try {
                EventQueue.invokeAndWait(this, runnable);
            } catch (InterruptedException e2) {
                System.err.println("Disposal was interrupted:");
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                System.err.println("Exception during disposal:");
                e3.printStackTrace();
            }
        }
        if (zIsDisplayable) {
            postWindowEvent(202);
        }
    }

    @Override // java.awt.Component
    void adjustListeningChildrenOnParent(long j2, int i2) {
    }

    @Override // java.awt.Container
    void adjustDecendantsOnParent(int i2) {
    }

    public void toFront() {
        toFront_NoClientCode();
    }

    final void toFront_NoClientCode() {
        if (this.visible) {
            WindowPeer windowPeer = (WindowPeer) this.peer;
            if (windowPeer != null) {
                windowPeer.toFront();
            }
            if (isModalBlocked()) {
                this.modalBlocker.toFront_NoClientCode();
            }
        }
    }

    public void toBack() {
        toBack_NoClientCode();
    }

    final void toBack_NoClientCode() {
        WindowPeer windowPeer;
        if (isAlwaysOnTop()) {
            try {
                setAlwaysOnTop(false);
            } catch (SecurityException e2) {
            }
        }
        if (this.visible && (windowPeer = (WindowPeer) this.peer) != null) {
            windowPeer.toBack();
        }
    }

    @Override // java.awt.Component
    public Toolkit getToolkit() {
        return Toolkit.getDefaultToolkit();
    }

    public final String getWarningString() {
        return this.warningString;
    }

    private void setWarningString() {
        this.warningString = null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission(SecurityConstants.AWT.TOPLEVEL_WINDOW_PERMISSION);
            } catch (SecurityException e2) {
                this.warningString = (String) AccessController.doPrivileged(new GetPropertyAction("awt.appletWarning", "Java Applet Window"));
            }
        }
    }

    @Override // java.awt.Component
    public Locale getLocale() {
        if (this.locale == null) {
            return Locale.getDefault();
        }
        return this.locale;
    }

    @Override // java.awt.Component
    public InputContext getInputContext() {
        synchronized (this.inputContextLock) {
            if (this.inputContext == null) {
                this.inputContext = InputContext.getInstance();
            }
        }
        return this.inputContext;
    }

    @Override // java.awt.Component
    public void setCursor(Cursor cursor) {
        if (cursor == null) {
            cursor = Cursor.getPredefinedCursor(0);
        }
        super.setCursor(cursor);
    }

    public Window getOwner() {
        return getOwner_NoClientCode();
    }

    final Window getOwner_NoClientCode() {
        return (Window) this.parent;
    }

    public Window[] getOwnedWindows() {
        return getOwnedWindows_NoClientCode();
    }

    final Window[] getOwnedWindows_NoClientCode() {
        Window[] windowArr;
        synchronized (this.ownedWindowList) {
            int size = this.ownedWindowList.size();
            int i2 = 0;
            Window[] windowArr2 = new Window[size];
            for (int i3 = 0; i3 < size; i3++) {
                windowArr2[i2] = this.ownedWindowList.elementAt(i3).get();
                if (windowArr2[i2] != null) {
                    i2++;
                }
            }
            if (size != i2) {
                windowArr = (Window[]) Arrays.copyOf(windowArr2, i2);
            } else {
                windowArr = windowArr2;
            }
        }
        return windowArr;
    }

    boolean isModalBlocked() {
        return this.modalBlocker != null;
    }

    void setModalBlocked(Dialog dialog, boolean z2, boolean z3) {
        WindowPeer windowPeer;
        this.modalBlocker = z2 ? dialog : null;
        if (z3 && (windowPeer = (WindowPeer) this.peer) != null) {
            windowPeer.setModalBlocked(dialog, z2);
        }
    }

    Dialog getModalBlocker() {
        return this.modalBlocker;
    }

    static IdentityArrayList<Window> getAllWindows() {
        IdentityArrayList<Window> identityArrayList;
        synchronized (allWindows) {
            identityArrayList = new IdentityArrayList<>();
            identityArrayList.addAll(allWindows);
        }
        return identityArrayList;
    }

    static IdentityArrayList<Window> getAllUnblockedWindows() {
        IdentityArrayList<Window> identityArrayList;
        synchronized (allWindows) {
            identityArrayList = new IdentityArrayList<>();
            for (int i2 = 0; i2 < allWindows.size(); i2++) {
                Window window = allWindows.get(i2);
                if (!window.isModalBlocked()) {
                    identityArrayList.add(window);
                }
            }
        }
        return identityArrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Window[] getWindows(AppContext appContext) {
        Window[] windowArr;
        Window[] windowArr2;
        synchronized (Window.class) {
            Vector vector = (Vector) appContext.get(Window.class);
            if (vector != null) {
                int size = vector.size();
                int i2 = 0;
                Window[] windowArr3 = new Window[size];
                for (int i3 = 0; i3 < size; i3++) {
                    Window window = (Window) ((WeakReference) vector.get(i3)).get();
                    if (window != null) {
                        int i4 = i2;
                        i2++;
                        windowArr3[i4] = window;
                    }
                }
                if (size != i2) {
                    windowArr = (Window[]) Arrays.copyOf(windowArr3, i2);
                } else {
                    windowArr = windowArr3;
                }
            } else {
                windowArr = new Window[0];
            }
            windowArr2 = windowArr;
        }
        return windowArr2;
    }

    public static Window[] getWindows() {
        return getWindows(AppContext.getAppContext());
    }

    public static Window[] getOwnerlessWindows() {
        Window[] windows = getWindows();
        int i2 = 0;
        for (Window window : windows) {
            if (window.getOwner() == null) {
                i2++;
            }
        }
        Window[] windowArr = new Window[i2];
        int i3 = 0;
        for (Window window2 : windows) {
            if (window2.getOwner() == null) {
                int i4 = i3;
                i3++;
                windowArr[i4] = window2;
            }
        }
        return windowArr;
    }

    Window getDocumentRoot() {
        Window window;
        synchronized (getTreeLock()) {
            Window owner = this;
            while (owner.getOwner() != null) {
                owner = owner.getOwner();
            }
            window = owner;
        }
        return window;
    }

    public void setModalExclusionType(Dialog.ModalExclusionType modalExclusionType) {
        SecurityManager securityManager;
        if (modalExclusionType == null) {
            modalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
        }
        if (!Toolkit.getDefaultToolkit().isModalExclusionTypeSupported(modalExclusionType)) {
            modalExclusionType = Dialog.ModalExclusionType.NO_EXCLUDE;
        }
        if (this.modalExclusionType == modalExclusionType) {
            return;
        }
        if (modalExclusionType == Dialog.ModalExclusionType.TOOLKIT_EXCLUDE && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SecurityConstants.AWT.TOOLKIT_MODALITY_PERMISSION);
        }
        this.modalExclusionType = modalExclusionType;
    }

    public Dialog.ModalExclusionType getModalExclusionType() {
        return this.modalExclusionType;
    }

    boolean isModalExcluded(Dialog.ModalExclusionType modalExclusionType) {
        if (this.modalExclusionType != null && this.modalExclusionType.compareTo(modalExclusionType) >= 0) {
            return true;
        }
        Window owner_NoClientCode = getOwner_NoClientCode();
        return owner_NoClientCode != null && owner_NoClientCode.isModalExcluded(modalExclusionType);
    }

    void updateChildrenBlocking() {
        Vector vector = new Vector();
        for (Window window : getOwnedWindows()) {
            vector.add(window);
        }
        for (int i2 = 0; i2 < vector.size(); i2++) {
            Window window2 = (Window) vector.get(i2);
            if (window2.isVisible()) {
                if (window2.isModalBlocked()) {
                    window2.getModalBlocker().unblockWindow(window2);
                }
                Dialog.checkShouldBeBlocked(window2);
                for (Window window3 : window2.getOwnedWindows()) {
                    vector.add(window3);
                }
            }
        }
    }

    public synchronized void addWindowListener(WindowListener windowListener) {
        if (windowListener == null) {
            return;
        }
        this.newEventsOnly = true;
        this.windowListener = AWTEventMulticaster.add(this.windowListener, windowListener);
    }

    public synchronized void addWindowStateListener(WindowStateListener windowStateListener) {
        if (windowStateListener == null) {
            return;
        }
        this.windowStateListener = AWTEventMulticaster.add(this.windowStateListener, windowStateListener);
        this.newEventsOnly = true;
    }

    public synchronized void addWindowFocusListener(WindowFocusListener windowFocusListener) {
        if (windowFocusListener == null) {
            return;
        }
        this.windowFocusListener = AWTEventMulticaster.add(this.windowFocusListener, windowFocusListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeWindowListener(WindowListener windowListener) {
        if (windowListener == null) {
            return;
        }
        this.windowListener = AWTEventMulticaster.remove(this.windowListener, windowListener);
    }

    public synchronized void removeWindowStateListener(WindowStateListener windowStateListener) {
        if (windowStateListener == null) {
            return;
        }
        this.windowStateListener = AWTEventMulticaster.remove(this.windowStateListener, windowStateListener);
    }

    public synchronized void removeWindowFocusListener(WindowFocusListener windowFocusListener) {
        if (windowFocusListener == null) {
            return;
        }
        this.windowFocusListener = AWTEventMulticaster.remove(this.windowFocusListener, windowFocusListener);
    }

    public synchronized WindowListener[] getWindowListeners() {
        return (WindowListener[]) getListeners(WindowListener.class);
    }

    public synchronized WindowFocusListener[] getWindowFocusListeners() {
        return (WindowFocusListener[]) getListeners(WindowFocusListener.class);
    }

    public synchronized WindowStateListener[] getWindowStateListeners() {
        return (WindowStateListener[]) getListeners(WindowStateListener.class);
    }

    @Override // java.awt.Container, java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        EventListener eventListener;
        if (cls == WindowFocusListener.class) {
            eventListener = this.windowFocusListener;
        } else if (cls == WindowStateListener.class) {
            eventListener = this.windowStateListener;
        } else if (cls == WindowListener.class) {
            eventListener = this.windowListener;
        } else {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(eventListener, cls);
    }

    @Override // java.awt.Container, java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        switch (aWTEvent.id) {
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
                if ((this.eventMask & 64) != 0 || this.windowListener != null) {
                    return true;
                }
                return false;
            case 207:
            case 208:
                if ((this.eventMask & 524288) != 0 || this.windowFocusListener != null) {
                    return true;
                }
                return false;
            case 209:
                if ((this.eventMask & 262144) != 0 || this.windowStateListener != null) {
                    return true;
                }
                return false;
            default:
                return super.eventEnabled(aWTEvent);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof WindowEvent) {
            switch (aWTEvent.getID()) {
                case 200:
                case 201:
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                    processWindowEvent((WindowEvent) aWTEvent);
                    break;
                case 207:
                case 208:
                    processWindowFocusEvent((WindowEvent) aWTEvent);
                    break;
                case 209:
                    processWindowStateEvent((WindowEvent) aWTEvent);
                    break;
            }
            return;
        }
        super.processEvent(aWTEvent);
    }

    protected void processWindowEvent(WindowEvent windowEvent) {
        WindowListener windowListener = this.windowListener;
        if (windowListener != null) {
            switch (windowEvent.getID()) {
                case 200:
                    windowListener.windowOpened(windowEvent);
                    break;
                case 201:
                    windowListener.windowClosing(windowEvent);
                    break;
                case 202:
                    windowListener.windowClosed(windowEvent);
                    break;
                case 203:
                    windowListener.windowIconified(windowEvent);
                    break;
                case 204:
                    windowListener.windowDeiconified(windowEvent);
                    break;
                case 205:
                    windowListener.windowActivated(windowEvent);
                    break;
                case 206:
                    windowListener.windowDeactivated(windowEvent);
                    break;
            }
        }
    }

    protected void processWindowFocusEvent(WindowEvent windowEvent) {
        WindowFocusListener windowFocusListener = this.windowFocusListener;
        if (windowFocusListener != null) {
            switch (windowEvent.getID()) {
                case 207:
                    windowFocusListener.windowGainedFocus(windowEvent);
                    break;
                case 208:
                    windowFocusListener.windowLostFocus(windowEvent);
                    break;
            }
        }
    }

    protected void processWindowStateEvent(WindowEvent windowEvent) {
        WindowStateListener windowStateListener = this.windowStateListener;
        if (windowStateListener != null) {
            switch (windowEvent.getID()) {
                case 209:
                    windowStateListener.windowStateChanged(windowEvent);
                    break;
            }
        }
    }

    @Override // java.awt.Container
    void preProcessKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.isActionKey() && keyEvent.getKeyCode() == 112 && keyEvent.isControlDown() && keyEvent.isShiftDown() && keyEvent.getID() == 401) {
            list(System.out, 0);
        }
    }

    @Override // java.awt.Container
    void postProcessKeyEvent(KeyEvent keyEvent) {
    }

    public final void setAlwaysOnTop(boolean z2) throws SecurityException {
        boolean z3;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.SET_WINDOW_ALWAYS_ON_TOP_PERMISSION);
        }
        synchronized (this) {
            z3 = this.alwaysOnTop;
            this.alwaysOnTop = z2;
        }
        if (z3 != z2) {
            if (isAlwaysOnTopSupported()) {
                WindowPeer windowPeer = (WindowPeer) this.peer;
                synchronized (getTreeLock()) {
                    if (windowPeer != null) {
                        windowPeer.updateAlwaysOnTopState();
                    }
                }
            }
            firePropertyChange("alwaysOnTop", z3, z2);
        }
        setOwnedWindowsAlwaysOnTop(z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setOwnedWindowsAlwaysOnTop(boolean z2) {
        WeakReference[] weakReferenceArr;
        synchronized (this.ownedWindowList) {
            weakReferenceArr = new WeakReference[this.ownedWindowList.size()];
            this.ownedWindowList.copyInto(weakReferenceArr);
        }
        for (WeakReference weakReference : weakReferenceArr) {
            Window window = (Window) weakReference.get();
            if (window != null) {
                try {
                    window.setAlwaysOnTop(z2);
                } catch (SecurityException e2) {
                }
            }
        }
    }

    public boolean isAlwaysOnTopSupported() {
        return Toolkit.getDefaultToolkit().isAlwaysOnTopSupported();
    }

    public final boolean isAlwaysOnTop() {
        return this.alwaysOnTop;
    }

    public Component getFocusOwner() {
        if (isFocused()) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        }
        return null;
    }

    public Component getMostRecentFocusOwner() {
        if (isFocused()) {
            return getFocusOwner();
        }
        Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(this);
        if (mostRecentFocusOwner != null) {
            return mostRecentFocusOwner;
        }
        if (isFocusableWindow()) {
            return getFocusTraversalPolicy().getInitialComponent(this);
        }
        return null;
    }

    public boolean isActive() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() == this;
    }

    public boolean isFocused() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getGlobalFocusedWindow() == this;
    }

    @Override // java.awt.Container, java.awt.Component
    public Set<AWTKeyStroke> getFocusTraversalKeys(int i2) {
        if (i2 < 0 || i2 >= 4) {
            throw new IllegalArgumentException("invalid focus traversal key identifier");
        }
        Set<AWTKeyStroke> set = this.focusTraversalKeys != null ? this.focusTraversalKeys[i2] : null;
        if (set != null) {
            return set;
        }
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(i2);
    }

    @Override // java.awt.Container
    public final void setFocusCycleRoot(boolean z2) {
    }

    @Override // java.awt.Container
    public final boolean isFocusCycleRoot() {
        return true;
    }

    @Override // java.awt.Component
    public final Container getFocusCycleRootAncestor() {
        return null;
    }

    public final boolean isFocusableWindow() {
        Window window;
        if (!getFocusableWindowState()) {
            return false;
        }
        if ((this instanceof Frame) || (this instanceof Dialog)) {
            return true;
        }
        if (getFocusTraversalPolicy().getDefaultComponent(this) == null) {
            return false;
        }
        Window owner = getOwner();
        while (true) {
            window = owner;
            if (window != null) {
                if ((window instanceof Frame) || (window instanceof Dialog)) {
                    break;
                }
                owner = window.getOwner();
            } else {
                return false;
            }
        }
        return window.isShowing();
    }

    public boolean getFocusableWindowState() {
        return this.focusableWindowState;
    }

    public void setFocusableWindowState(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.focusableWindowState;
            this.focusableWindowState = z2;
        }
        WindowPeer windowPeer = (WindowPeer) this.peer;
        if (windowPeer != null) {
            windowPeer.updateFocusableWindowState();
        }
        firePropertyChange("focusableWindowState", z3, z2);
        if (z3 && !z2 && isFocused()) {
            Window owner = getOwner();
            while (true) {
                Window window = owner;
                if (window != null) {
                    Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(window);
                    if (mostRecentFocusOwner == null || !mostRecentFocusOwner.requestFocus(false, CausedFocusEvent.Cause.ACTIVATION)) {
                        owner = window.getOwner();
                    } else {
                        return;
                    }
                } else {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwnerPriv();
                    return;
                }
            }
        }
    }

    public void setAutoRequestFocus(boolean z2) {
        this.autoRequestFocus = z2;
    }

    public boolean isAutoRequestFocus() {
        return this.autoRequestFocus;
    }

    @Override // java.awt.Container, java.awt.Component, java.beans.PropertyEditor
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        super.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // java.awt.Container, java.awt.Component
    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        super.addPropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.Container
    public boolean isValidateRoot() {
        return true;
    }

    @Override // java.awt.Container, java.awt.Component
    void dispatchEventImpl(AWTEvent aWTEvent) {
        if (aWTEvent.getID() == 101) {
            invalidate();
            validate();
        }
        super.dispatchEventImpl(aWTEvent);
    }

    @Override // java.awt.Component, java.awt.MenuContainer
    @Deprecated
    public boolean postEvent(Event event) {
        if (handleEvent(event)) {
            event.consume();
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    public boolean isShowing() {
        return this.visible;
    }

    boolean isDisposing() {
        return this.disposing;
    }

    @Deprecated
    public void applyResourceBundle(ResourceBundle resourceBundle) {
        applyComponentOrientation(ComponentOrientation.getOrientation(resourceBundle));
    }

    @Deprecated
    public void applyResourceBundle(String str) {
        applyResourceBundle(ResourceBundle.getBundle(str, Locale.getDefault(), ClassLoader.getSystemClassLoader()));
    }

    void addOwnedWindow(WeakReference<Window> weakReference) {
        if (weakReference != null) {
            synchronized (this.ownedWindowList) {
                if (!this.ownedWindowList.contains(weakReference)) {
                    this.ownedWindowList.addElement(weakReference);
                }
            }
        }
    }

    void removeOwnedWindow(WeakReference<Window> weakReference) {
        if (weakReference != null) {
            this.ownedWindowList.removeElement(weakReference);
        }
    }

    void connectOwnedWindow(Window window) {
        window.parent = this;
        addOwnedWindow(window.weakThis);
        window.disposerRecord.updateOwner();
    }

    private void addToWindowList() {
        synchronized (Window.class) {
            Vector vector = (Vector) this.appContext.get(Window.class);
            if (vector == null) {
                vector = new Vector();
                this.appContext.put(Window.class, vector);
            }
            vector.add(this.weakThis);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeFromWindowList(AppContext appContext, WeakReference<Window> weakReference) {
        synchronized (Window.class) {
            Vector vector = (Vector) appContext.get(Window.class);
            if (vector != null) {
                vector.remove(weakReference);
            }
        }
    }

    private void removeFromWindowList() {
        removeFromWindowList(this.appContext, this.weakThis);
    }

    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("type should not be null.");
        }
        synchronized (getTreeLock()) {
            if (isDisplayable()) {
                throw new IllegalComponentStateException("The window is displayable.");
            }
            synchronized (getObjectLock()) {
                this.type = type;
            }
        }
    }

    public Type getType() {
        Type type;
        synchronized (getObjectLock()) {
            type = this.type;
        }
        return type;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this) {
            this.focusMgr = new FocusManager();
            this.focusMgr.focusRoot = this;
            this.focusMgr.focusOwner = getMostRecentFocusOwner();
            objectOutputStream.defaultWriteObject();
            this.focusMgr = null;
            AWTEventMulticaster.save(objectOutputStream, "windowL", this.windowListener);
            AWTEventMulticaster.save(objectOutputStream, "windowFocusL", this.windowFocusListener);
            AWTEventMulticaster.save(objectOutputStream, "windowStateL", this.windowStateListener);
        }
        objectOutputStream.writeObject(null);
        synchronized (this.ownedWindowList) {
            for (int i2 = 0; i2 < this.ownedWindowList.size(); i2++) {
                Window window = this.ownedWindowList.elementAt(i2).get();
                if (window != null) {
                    objectOutputStream.writeObject("ownedL");
                    objectOutputStream.writeObject(window);
                }
            }
        }
        objectOutputStream.writeObject(null);
        if (this.icons != null) {
            for (Image image : this.icons) {
                if (image instanceof Serializable) {
                    objectOutputStream.writeObject(image);
                }
            }
        }
        objectOutputStream.writeObject(null);
    }

    private void initDeserializedWindow() throws HeadlessException {
        setWarningString();
        this.inputContextLock = new Object();
        this.visible = false;
        this.weakThis = new WeakReference<>(this);
        this.anchor = new Object();
        this.disposerRecord = new WindowDisposerRecord(this.appContext, this);
        Disposer.addRecord(this.anchor, this.disposerRecord);
        addToWindowList();
        initGC(null);
        this.ownedWindowList = new Vector<>();
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00ce A[Catch: OptionalDataException -> 0x00ee, TryCatch #0 {OptionalDataException -> 0x00ee, blocks: (B:23:0x008b, B:25:0x0095, B:27:0x00a4, B:28:0x00b2, B:30:0x00ba, B:33:0x00ce, B:35:0x00d5, B:36:0x00e3), top: B:40:0x008b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void deserializeResources(java.io.ObjectInputStream r5) throws java.io.IOException, java.lang.ClassNotFoundException, java.awt.HeadlessException {
        /*
            Method dump skipped, instructions count: 240
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Window.deserializeResources(java.io.ObjectInputStream):void");
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException, SecurityException {
        GraphicsEnvironment.checkHeadless();
        initDeserializedWindow();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.syncLWRequests = fields.get("syncLWRequests", systemSyncLWRequests);
        this.state = fields.get("state", 0);
        this.focusableWindowState = fields.get("focusableWindowState", true);
        this.windowSerializedDataVersion = fields.get("windowSerializedDataVersion", 1);
        this.locationByPlatform = fields.get("locationByPlatform", locationByPlatformProp);
        this.focusMgr = (FocusManager) fields.get("focusMgr", (Object) null);
        setModalExclusionType((Dialog.ModalExclusionType) fields.get("modalExclusionType", Dialog.ModalExclusionType.NO_EXCLUDE));
        boolean z2 = fields.get("alwaysOnTop", false);
        if (z2) {
            setAlwaysOnTop(z2);
        }
        this.shape = (Shape) fields.get("shape", (Object) null);
        this.opacity = Float.valueOf(fields.get("opacity", 1.0f)).floatValue();
        this.securityWarningWidth = 0;
        this.securityWarningHeight = 0;
        this.securityWarningPointX = 2.0d;
        this.securityWarningPointY = 0.0d;
        this.securityWarningAlignmentX = 1.0f;
        this.securityWarningAlignmentY = 0.0f;
        deserializeResources(objectInputStream);
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTWindow();
        }
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Window$AccessibleAWTWindow.class */
    public class AccessibleAWTWindow extends Container.AccessibleAWTContainer {
        private static final long serialVersionUID = 4215068635060671780L;

        protected AccessibleAWTWindow() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.WINDOW;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (Window.this.getFocusOwner() != null) {
                accessibleStateSet.add(AccessibleState.ACTIVE);
            }
            return accessibleStateSet;
        }
    }

    @Override // java.awt.Component
    void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        if (graphicsConfiguration == null) {
            graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        synchronized (getTreeLock()) {
            super.setGraphicsConfiguration(graphicsConfiguration);
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("+ Window.setGraphicsConfiguration(): new GC is \n+ " + ((Object) getGraphicsConfiguration_NoClientCode()) + "\n+ this is " + ((Object) this));
            }
        }
    }

    public void setLocationRelativeTo(Component component) throws HeadlessException {
        Rectangle bounds;
        int i2;
        int i3;
        getGraphicsConfiguration_NoClientCode().getBounds();
        Dimension size = getSize();
        Window containingWindow = SunToolkit.getContainingWindow(component);
        if (component == null || containingWindow == null) {
            GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            bounds = localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            Point centerPoint = localGraphicsEnvironment.getCenterPoint();
            i2 = centerPoint.f12370x - (size.width / 2);
            i3 = centerPoint.f12371y - (size.height / 2);
        } else if (!component.isShowing()) {
            bounds = containingWindow.getGraphicsConfiguration().getBounds();
            i2 = bounds.f12372x + ((bounds.width - size.width) / 2);
            i3 = bounds.f12373y + ((bounds.height - size.height) / 2);
        } else {
            bounds = containingWindow.getGraphicsConfiguration().getBounds();
            Dimension size2 = component.getSize();
            Point locationOnScreen = component.getLocationOnScreen();
            i2 = locationOnScreen.f12370x + ((size2.width - size.width) / 2);
            i3 = locationOnScreen.f12371y + ((size2.height - size.height) / 2);
            if (i3 + size.height > bounds.f12373y + bounds.height) {
                i3 = (bounds.f12373y + bounds.height) - size.height;
                if ((locationOnScreen.f12370x - bounds.f12372x) + (size2.width / 2) < bounds.width / 2) {
                    i2 = locationOnScreen.f12370x + size2.width;
                } else {
                    i2 = locationOnScreen.f12370x - size.width;
                }
            }
        }
        if (i3 + size.height > bounds.f12373y + bounds.height) {
            i3 = (bounds.f12373y + bounds.height) - size.height;
        }
        if (i3 < bounds.f12373y) {
            i3 = bounds.f12373y;
        }
        if (i2 + size.width > bounds.f12372x + bounds.width) {
            i2 = (bounds.f12372x + bounds.width) - size.width;
        }
        if (i2 < bounds.f12372x) {
            i2 = bounds.f12372x;
        }
        setLocation(i2, i3);
    }

    void deliverMouseWheelToAncestor(MouseWheelEvent mouseWheelEvent) {
    }

    @Override // java.awt.Component
    boolean dispatchMouseWheelToAncestor(MouseWheelEvent mouseWheelEvent) {
        return false;
    }

    @Override // java.awt.Component
    public void createBufferStrategy(int i2) {
        super.createBufferStrategy(i2);
    }

    @Override // java.awt.Component
    public void createBufferStrategy(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        super.createBufferStrategy(i2, bufferCapabilities);
    }

    @Override // java.awt.Component
    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
    }

    Component getTemporaryLostComponent() {
        return this.temporaryLostComponent;
    }

    Component setTemporaryLostComponent(Component component) {
        Component component2 = this.temporaryLostComponent;
        if (component == null || component.canBeFocusOwner()) {
            this.temporaryLostComponent = component;
        } else {
            this.temporaryLostComponent = null;
        }
        return component2;
    }

    @Override // java.awt.Container
    boolean canContainFocusOwner(Component component) {
        return super.canContainFocusOwner(component) && isFocusableWindow();
    }

    public void setLocationByPlatform(boolean z2) {
        synchronized (getTreeLock()) {
            if (z2) {
                if (isShowing()) {
                    throw new IllegalComponentStateException("The window is showing on screen.");
                }
            }
            this.locationByPlatform = z2;
        }
    }

    public boolean isLocationByPlatform() {
        return this.locationByPlatform;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        synchronized (getTreeLock()) {
            if (getBoundsOp() == 1 || getBoundsOp() == 3) {
                this.locationByPlatform = false;
            }
            super.setBounds(i2, i3, i4, i5);
        }
    }

    @Override // java.awt.Component
    public void setBounds(Rectangle rectangle) {
        setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    @Override // java.awt.Component
    boolean isRecursivelyVisible() {
        return this.visible;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float f2) {
        synchronized (getTreeLock()) {
            if (f2 < 0.0f || f2 > 1.0f) {
                throw new IllegalArgumentException("The value of opacity should be in the range [0.0f .. 1.0f].");
            }
            if (f2 < 1.0f) {
                GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
                GraphicsDevice device = graphicsConfiguration.getDevice();
                if (graphicsConfiguration.getDevice().getFullScreenWindow() == this) {
                    throw new IllegalComponentStateException("Setting opacity for full-screen window is not supported.");
                }
                if (!device.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
                    throw new UnsupportedOperationException("TRANSLUCENT translucency is not supported.");
                }
            }
            this.opacity = f2;
            WindowPeer windowPeer = (WindowPeer) getPeer();
            if (windowPeer != null) {
                windowPeer.setOpacity(f2);
            }
        }
    }

    public Shape getShape() {
        Path2D.Float r0;
        synchronized (getTreeLock()) {
            r0 = this.shape == null ? null : new Path2D.Float(this.shape);
        }
        return r0;
    }

    public void setShape(Shape shape) {
        synchronized (getTreeLock()) {
            if (shape != null) {
                GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
                GraphicsDevice device = graphicsConfiguration.getDevice();
                if (graphicsConfiguration.getDevice().getFullScreenWindow() == this) {
                    throw new IllegalComponentStateException("Setting shape for full-screen window is not supported.");
                }
                if (!device.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) {
                    throw new UnsupportedOperationException("PERPIXEL_TRANSPARENT translucency is not supported.");
                }
            }
            this.shape = shape == null ? null : new Path2D.Float(shape);
            WindowPeer windowPeer = (WindowPeer) getPeer();
            if (windowPeer != null) {
                windowPeer.applyShape(shape == null ? null : Region.getInstance(shape, null));
            }
        }
    }

    @Override // java.awt.Component
    public Color getBackground() {
        return super.getBackground();
    }

    @Override // java.awt.Component
    public void setBackground(Color color) {
        Color background = getBackground();
        super.setBackground(color);
        if (background != null && background.equals(color)) {
            return;
        }
        int alpha = background != null ? background.getAlpha() : 255;
        int alpha2 = color != null ? color.getAlpha() : 255;
        if (alpha == 255 && alpha2 < 255) {
            GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
            GraphicsDevice device = graphicsConfiguration.getDevice();
            if (graphicsConfiguration.getDevice().getFullScreenWindow() == this) {
                throw new IllegalComponentStateException("Making full-screen window non opaque is not supported.");
            }
            if (!graphicsConfiguration.isTranslucencyCapable()) {
                GraphicsConfiguration translucencyCapableGC = device.getTranslucencyCapableGC();
                if (translucencyCapableGC == null) {
                    throw new UnsupportedOperationException("PERPIXEL_TRANSLUCENT translucency is not supported");
                }
                setGraphicsConfiguration(translucencyCapableGC);
            }
            setLayersOpaque(this, false);
        } else if (alpha < 255 && alpha2 == 255) {
            setLayersOpaque(this, true);
        }
        WindowPeer windowPeer = (WindowPeer) getPeer();
        if (windowPeer != null) {
            windowPeer.setOpaque(alpha2 == 255);
        }
    }

    @Override // java.awt.Component
    public boolean isOpaque() {
        Color background = getBackground();
        return background == null || background.getAlpha() == 255;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWindow() {
        synchronized (getTreeLock()) {
            WindowPeer windowPeer = (WindowPeer) getPeer();
            if (windowPeer != null) {
                windowPeer.updateWindow();
            }
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (!isOpaque()) {
            Graphics graphicsCreate = graphics.create();
            try {
                if (graphicsCreate instanceof Graphics2D) {
                    graphicsCreate.setColor(getBackground());
                    ((Graphics2D) graphicsCreate).setComposite(AlphaComposite.getInstance(2));
                    graphicsCreate.fillRect(0, 0, getWidth(), getHeight());
                }
            } finally {
                graphicsCreate.dispose();
            }
        }
        super.paint(graphics);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void setLayersOpaque(Component component, boolean z2) {
        if (SunToolkit.isInstanceOf(component, "javax.swing.RootPaneContainer")) {
            JRootPane rootPane = ((RootPaneContainer) component).getRootPane();
            JLayeredPane layeredPane = rootPane.getLayeredPane();
            Container contentPane = rootPane.getContentPane();
            JComponent jComponent = contentPane instanceof JComponent ? (JComponent) contentPane : null;
            layeredPane.setOpaque(z2);
            rootPane.setOpaque(z2);
            if (jComponent != null) {
                jComponent.setOpaque(z2);
                if (jComponent.getComponentCount() > 0) {
                    Component component2 = jComponent.getComponent(0);
                    if (component2 instanceof RootPaneContainer) {
                        setLayersOpaque(component2, z2);
                    }
                }
            }
        }
    }

    @Override // java.awt.Component
    final Container getContainer() {
        return null;
    }

    @Override // java.awt.Component
    final void applyCompoundShape(Region region) {
    }

    @Override // java.awt.Component
    final void applyCurrentShape() {
    }

    @Override // java.awt.Container, java.awt.Component
    final void mixOnReshaping() {
    }

    @Override // java.awt.Component
    final Point getLocationOnWindow() {
        return new Point(0, 0);
    }

    private static double limit(double d2, double d3, double d4) {
        return Math.min(Math.max(d2, d3), d4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point2D calculateSecurityWarningPosition(double d2, double d3, double d4, double d5) throws HeadlessException {
        double d6 = d2 + (d4 * this.securityWarningAlignmentX) + this.securityWarningPointX;
        double d7 = d3 + (d5 * this.securityWarningAlignmentY) + this.securityWarningPointY;
        double dLimit = limit(d6, (d2 - this.securityWarningWidth) - 2.0d, d2 + d4 + 2.0d);
        double dLimit2 = limit(d7, (d3 - this.securityWarningHeight) - 2.0d, d3 + d5 + 2.0d);
        GraphicsConfiguration graphicsConfiguration_NoClientCode = getGraphicsConfiguration_NoClientCode();
        Rectangle bounds = graphicsConfiguration_NoClientCode.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfiguration_NoClientCode);
        return new Point2D.Double(limit(dLimit, bounds.f12372x + screenInsets.left, ((bounds.f12372x + bounds.width) - screenInsets.right) - this.securityWarningWidth), limit(dLimit2, bounds.f12373y + screenInsets.top, ((bounds.f12373y + bounds.height) - screenInsets.bottom) - this.securityWarningHeight));
    }

    @Override // java.awt.Component
    void updateZOrder() {
    }
}
