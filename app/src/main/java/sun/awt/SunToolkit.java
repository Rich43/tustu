package sun.awt;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.MouseInfoPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.SystemTrayPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.TrayIconPeer;
import java.awt.peer.WindowPeer;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketPermission;
import java.net.URL;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javafx.fxml.FXMLLoader;
import sun.awt.AWTAccessor;
import sun.awt.im.InputContext;
import sun.awt.im.SimpleInputMethodWindow;
import sun.awt.image.ByteArrayImageSource;
import sun.awt.image.FileImageSource;
import sun.awt.image.MultiResolutionImage;
import sun.awt.image.MultiResolutionToolkitImage;
import sun.awt.image.ToolkitImage;
import sun.awt.image.URLImageSource;
import sun.font.FontDesignMetrics;
import sun.misc.SoftCache;
import sun.net.util.URLUtil;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/SunToolkit.class */
public abstract class SunToolkit extends Toolkit implements WindowClosingSupport, WindowClosingListener, ComponentFactory, InputMethodSupport, KeyboardFocusManagerPeerProvider {
    public static final int GRAB_EVENT_MASK = Integer.MIN_VALUE;
    private static final String POST_EVENT_QUEUE_KEY = "PostEventQueue";
    protected static int numberOfButtons;
    public static final int MAX_BUTTONS_SUPPORTED = 20;
    private static final ReentrantLock AWT_LOCK;
    private static final Condition AWT_LOCK_COND;
    private static final Map<Object, AppContext> appContextMap;
    static final SoftCache fileImgCache;
    static final SoftCache urlImgCache;
    private static Locale startupLocale;
    private static DefaultMouseInfoPeer mPeer;
    private static Dialog.ModalExclusionType DEFAULT_MODAL_EXCLUSION_TYPE;
    public static final int DEFAULT_WAIT_TIME = 10000;
    private static final int MAX_ITERS = 20;
    private static final int MIN_ITERS = 0;
    private static final int MINIMAL_EDELAY = 0;
    private boolean eventDispatched;
    private boolean queueEmpty;
    private static boolean touchKeyboardAutoShowIsEnabled;
    private static boolean checkedSystemAAFontSettings;
    private static boolean useSystemAAFontSettings;
    private static boolean lastExtraCondition;
    private static RenderingHints desktopFontHints;
    public static final String DESKTOPFONTHINTS = "awt.font.desktophints";
    private static Boolean sunAwtDisableMixing;
    private static final Object DEACTIVATION_TIMES_MAP_KEY;
    static final /* synthetic */ boolean $assertionsDisabled;
    private transient WindowClosingListener windowClosingListener = null;
    private ModalityListenerList modalityListeners = new ModalityListenerList();
    private final Object waitLock = new Object();

    /* loaded from: rt.jar:sun/awt/SunToolkit$InfiniteLoop.class */
    public static class InfiniteLoop extends RuntimeException {
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract WindowPeer createWindow(Window window) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract FramePeer createFrame(Frame frame) throws HeadlessException;

    public abstract FramePeer createLightweightFrame(LightweightFrame lightweightFrame) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract DialogPeer createDialog(Dialog dialog) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract ButtonPeer createButton(Button button) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract TextFieldPeer createTextField(TextField textField) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract ChoicePeer createChoice(Choice choice) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract LabelPeer createLabel(Label label) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract ListPeer createList(List list) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract CheckboxPeer createCheckbox(Checkbox checkbox) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract ScrollbarPeer createScrollbar(Scrollbar scrollbar) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract ScrollPanePeer createScrollPane(ScrollPane scrollPane) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract TextAreaPeer createTextArea(TextArea textArea) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract FileDialogPeer createFileDialog(FileDialog fileDialog) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract MenuBarPeer createMenuBar(MenuBar menuBar) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract MenuPeer createMenu(Menu menu) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract PopupMenuPeer createPopupMenu(PopupMenu popupMenu) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract MenuItemPeer createMenuItem(MenuItem menuItem) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) throws HeadlessException;

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException;

    public abstract TrayIconPeer createTrayIcon(TrayIcon trayIcon) throws AWTException, HeadlessException;

    public abstract SystemTrayPeer createSystemTray(SystemTray systemTray);

    public abstract boolean isTraySupported();

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public abstract FontPeer getFontPeer(String str, int i2);

    public abstract RobotPeer createRobot(Robot robot, GraphicsDevice graphicsDevice) throws AWTException;

    public abstract KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException;

    protected abstract int getScreenWidth();

    protected abstract int getScreenHeight();

    protected abstract boolean syncNativeQueue(long j2);

    public abstract void grab(Window window);

    public abstract void ungrab(Window window);

    public static native void closeSplashScreen();

    public abstract boolean isDesktopSupported();

    static {
        $assertionsDisabled = !SunToolkit.class.desiredAssertionStatus();
        if (((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.awt.nativedebug"))).booleanValue()) {
            DebugSettings.init();
        }
        touchKeyboardAutoShowIsEnabled = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("awt.touchKeyboardAutoShowIsEnabled", "true"))).booleanValue();
        numberOfButtons = 0;
        AWT_LOCK = new ReentrantLock();
        AWT_LOCK_COND = AWT_LOCK.newCondition();
        appContextMap = Collections.synchronizedMap(new WeakHashMap());
        fileImgCache = new SoftCache();
        urlImgCache = new SoftCache();
        startupLocale = null;
        mPeer = null;
        DEFAULT_MODAL_EXCLUSION_TYPE = null;
        lastExtraCondition = true;
        sunAwtDisableMixing = null;
        DEACTIVATION_TIMES_MAP_KEY = new Object();
    }

    private static void initEQ(AppContext appContext) {
        EventQueue eventQueue;
        String property = System.getProperty("AWT.EventQueueClass", "java.awt.EventQueue");
        try {
            eventQueue = (EventQueue) Class.forName(property).newInstance();
        } catch (Exception e2) {
            e2.printStackTrace();
            System.err.println("Failed loading " + property + ": " + ((Object) e2));
            eventQueue = new EventQueue();
        }
        appContext.put(AppContext.EVENT_QUEUE_KEY, eventQueue);
        appContext.put(POST_EVENT_QUEUE_KEY, new PostEventQueue(eventQueue));
    }

    public boolean useBufferPerWindow() {
        return false;
    }

    public static final void awtLock() {
        AWT_LOCK.lock();
    }

    public static final boolean awtTryLock() {
        return AWT_LOCK.tryLock();
    }

    public static final void awtUnlock() {
        AWT_LOCK.unlock();
    }

    public static final void awtLockWait() throws InterruptedException {
        AWT_LOCK_COND.await();
    }

    public static final void awtLockWait(long j2) throws InterruptedException {
        AWT_LOCK_COND.await(j2, TimeUnit.MILLISECONDS);
    }

    public static final void awtLockNotify() {
        AWT_LOCK_COND.signal();
    }

    public static final void awtLockNotifyAll() {
        AWT_LOCK_COND.signalAll();
    }

    public static final boolean isAWTLockHeldByCurrentThread() {
        return AWT_LOCK.isHeldByCurrentThread();
    }

    public static AppContext createNewAppContext() {
        return createNewAppContext(Thread.currentThread().getThreadGroup());
    }

    static final AppContext createNewAppContext(ThreadGroup threadGroup) {
        AppContext appContext = new AppContext(threadGroup);
        initEQ(appContext);
        return appContext;
    }

    static void wakeupEventQueue(EventQueue eventQueue, boolean z2) {
        AWTAccessor.getEventQueueAccessor().wakeup(eventQueue, z2);
    }

    protected static Object targetToPeer(Object obj) {
        if (obj != null && !GraphicsEnvironment.isHeadless()) {
            return AWTAutoShutdown.getInstance().getPeer(obj);
        }
        return null;
    }

    protected static void targetCreatedPeer(Object obj, Object obj2) {
        if (obj != null && obj2 != null && !GraphicsEnvironment.isHeadless()) {
            AWTAutoShutdown.getInstance().registerPeer(obj, obj2);
        }
    }

    protected static void targetDisposedPeer(Object obj, Object obj2) {
        if (obj != null && obj2 != null && !GraphicsEnvironment.isHeadless()) {
            AWTAutoShutdown.getInstance().unregisterPeer(obj, obj2);
        }
    }

    private static boolean setAppContext(Object obj, AppContext appContext) {
        if (obj instanceof Component) {
            AWTAccessor.getComponentAccessor().setAppContext((Component) obj, appContext);
            return true;
        }
        if (obj instanceof MenuComponent) {
            AWTAccessor.getMenuComponentAccessor().setAppContext((MenuComponent) obj, appContext);
            return true;
        }
        return false;
    }

    private static AppContext getAppContext(Object obj) {
        if (obj instanceof Component) {
            return AWTAccessor.getComponentAccessor().getAppContext((Component) obj);
        }
        if (obj instanceof MenuComponent) {
            return AWTAccessor.getMenuComponentAccessor().getAppContext((MenuComponent) obj);
        }
        return null;
    }

    public static AppContext targetToAppContext(Object obj) {
        if (obj == null) {
            return null;
        }
        AppContext appContext = getAppContext(obj);
        if (appContext == null) {
            appContext = appContextMap.get(obj);
        }
        return appContext;
    }

    public static void setLWRequestStatus(Window window, boolean z2) {
        AWTAccessor.getWindowAccessor().setLWRequestStatus(window, z2);
    }

    public static void checkAndSetPolicy(Container container) {
        container.setFocusTraversalPolicy(KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy());
    }

    private static FocusTraversalPolicy createLayoutPolicy() {
        FocusTraversalPolicy focusTraversalPolicy = null;
        try {
            focusTraversalPolicy = (FocusTraversalPolicy) Class.forName("javax.swing.LayoutFocusTraversalPolicy").newInstance();
        } catch (ClassNotFoundException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (IllegalAccessException e3) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (InstantiationException e4) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        return focusTraversalPolicy;
    }

    public static void insertTargetMapping(Object obj, AppContext appContext) {
        if (!setAppContext(obj, appContext)) {
            appContextMap.put(obj, appContext);
        }
    }

    public static void postEvent(AppContext appContext, AWTEvent aWTEvent) {
        if (aWTEvent == null) {
            throw new NullPointerException();
        }
        AWTAccessor.SequencedEventAccessor sequencedEventAccessor = AWTAccessor.getSequencedEventAccessor();
        if (sequencedEventAccessor != null && sequencedEventAccessor.isSequencedEvent(aWTEvent)) {
            AWTEvent nested = sequencedEventAccessor.getNested(aWTEvent);
            if (nested.getID() == 208 && (nested instanceof TimedWindowEvent)) {
                TimedWindowEvent timedWindowEvent = (TimedWindowEvent) nested;
                ((SunToolkit) Toolkit.getDefaultToolkit()).setWindowDeactivationTime((Window) timedWindowEvent.getSource(), timedWindowEvent.getWhen());
            }
        }
        setSystemGenerated(aWTEvent);
        AppContext appContextTargetToAppContext = targetToAppContext(aWTEvent.getSource());
        if (appContextTargetToAppContext != null && !appContextTargetToAppContext.equals(appContext)) {
            throw new RuntimeException("Event posted on wrong app context : " + ((Object) aWTEvent));
        }
        PostEventQueue postEventQueue = (PostEventQueue) appContext.get(POST_EVENT_QUEUE_KEY);
        if (postEventQueue != null) {
            postEventQueue.postEvent(aWTEvent);
        }
    }

    public static void postPriorityEvent(final AWTEvent aWTEvent) {
        postEvent(targetToAppContext(aWTEvent.getSource()), new PeerEvent(Toolkit.getDefaultToolkit(), new Runnable() { // from class: sun.awt.SunToolkit.1
            @Override // java.lang.Runnable
            public void run() {
                AWTAccessor.getAWTEventAccessor().setPosted(aWTEvent);
                ((Component) aWTEvent.getSource()).dispatchEvent(aWTEvent);
            }
        }, 2L));
    }

    public static void flushPendingEvents() {
        flushPendingEvents(AppContext.getAppContext());
    }

    public static void flushPendingEvents(AppContext appContext) {
        PostEventQueue postEventQueue = (PostEventQueue) appContext.get(POST_EVENT_QUEUE_KEY);
        if (postEventQueue != null) {
            postEventQueue.flush();
        }
    }

    public static void executeOnEventHandlerThread(Object obj, Runnable runnable) {
        executeOnEventHandlerThread(new PeerEvent(obj, runnable, 1L));
    }

    public static void executeOnEventHandlerThread(Object obj, Runnable runnable, final long j2) {
        executeOnEventHandlerThread(new PeerEvent(obj, runnable, 1L) { // from class: sun.awt.SunToolkit.2
            @Override // java.awt.event.InvocationEvent
            public long getWhen() {
                return j2;
            }
        });
    }

    public static void executeOnEventHandlerThread(PeerEvent peerEvent) {
        postEvent(targetToAppContext(peerEvent.getSource()), peerEvent);
    }

    public static void invokeLaterOnAppContext(AppContext appContext, Runnable runnable) {
        postEvent(appContext, new PeerEvent(Toolkit.getDefaultToolkit(), runnable, 1L));
    }

    public static void executeOnEDTAndWait(Object obj, Runnable runnable) throws InterruptedException, InvocationTargetException {
        if (EventQueue.isDispatchThread()) {
            throw new Error("Cannot call executeOnEDTAndWait from any event dispatcher thread");
        }
        Object obj2 = new Object() { // from class: sun.awt.SunToolkit.1AWTInvocationLock
        };
        PeerEvent peerEvent = new PeerEvent(obj, runnable, obj2, true, 1L);
        synchronized (obj2) {
            executeOnEventHandlerThread(peerEvent);
            while (!peerEvent.isDispatched()) {
                obj2.wait();
            }
        }
        Throwable throwable = peerEvent.getThrowable();
        if (throwable != null) {
            throw new InvocationTargetException(throwable);
        }
    }

    public static boolean isDispatchThreadForAppContext(Object obj) {
        return AWTAccessor.getEventQueueAccessor().isDispatchThreadImpl((EventQueue) targetToAppContext(obj).get(AppContext.EVENT_QUEUE_KEY));
    }

    @Override // java.awt.Toolkit
    public Dimension getScreenSize() {
        return new Dimension(getScreenWidth(), getScreenHeight());
    }

    @Override // java.awt.Toolkit
    public FontMetrics getFontMetrics(Font font) {
        return FontDesignMetrics.getMetrics(font);
    }

    @Override // java.awt.Toolkit
    public String[] getFontList() {
        return new String[]{Font.DIALOG, "SansSerif", "Serif", "Monospaced", Font.DIALOG_INPUT};
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public PanelPeer createPanel(Panel panel) {
        return (PanelPeer) createComponent(panel);
    }

    @Override // java.awt.Toolkit, sun.awt.ComponentFactory
    public CanvasPeer createCanvas(Canvas canvas) {
        return (CanvasPeer) createComponent(canvas);
    }

    public void disableBackgroundErase(Canvas canvas) {
        disableBackgroundEraseImpl(canvas);
    }

    public void disableBackgroundErase(Component component) {
        disableBackgroundEraseImpl(component);
    }

    private void disableBackgroundEraseImpl(Component component) {
        AWTAccessor.getComponentAccessor().setBackgroundEraseDisabled(component, true);
    }

    public static boolean getSunAwtNoerasebackground() {
        return ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.awt.noerasebackground"))).booleanValue();
    }

    public static boolean getSunAwtErasebackgroundonresize() {
        return ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.awt.erasebackgroundonresize"))).booleanValue();
    }

    static Image getImageFromHash(Toolkit toolkit, URL url) {
        Image image;
        checkPermissions(url);
        synchronized (urlImgCache) {
            String string = url.toString();
            Image imageCreateImage = (Image) urlImgCache.get(string);
            if (imageCreateImage == null) {
                try {
                    imageCreateImage = toolkit.createImage(new URLImageSource(url));
                    urlImgCache.put(string, imageCreateImage);
                } catch (Exception e2) {
                }
            }
            image = imageCreateImage;
        }
        return image;
    }

    static Image getImageFromHash(Toolkit toolkit, String str) {
        Image image;
        checkPermissions(str);
        synchronized (fileImgCache) {
            Image imageCreateImage = (Image) fileImgCache.get(str);
            if (imageCreateImage == null) {
                try {
                    imageCreateImage = toolkit.createImage(new FileImageSource(str));
                    fileImgCache.put(str, imageCreateImage);
                } catch (Exception e2) {
                }
            }
            image = imageCreateImage;
        }
        return image;
    }

    @Override // java.awt.Toolkit
    public Image getImage(String str) {
        return getImageFromHash(this, str);
    }

    @Override // java.awt.Toolkit
    public Image getImage(URL url) {
        return getImageFromHash(this, url);
    }

    protected Image getImageWithResolutionVariant(String str, String str2) {
        synchronized (fileImgCache) {
            Image imageFromHash = getImageFromHash(this, str);
            if (imageFromHash instanceof MultiResolutionImage) {
                return imageFromHash;
            }
            Image imageCreateImageWithResolutionVariant = createImageWithResolutionVariant(imageFromHash, getImageFromHash(this, str2));
            fileImgCache.put(str, imageCreateImageWithResolutionVariant);
            return imageCreateImageWithResolutionVariant;
        }
    }

    protected Image getImageWithResolutionVariant(URL url, URL url2) {
        synchronized (urlImgCache) {
            Image imageFromHash = getImageFromHash(this, url);
            if (imageFromHash instanceof MultiResolutionImage) {
                return imageFromHash;
            }
            Image imageCreateImageWithResolutionVariant = createImageWithResolutionVariant(imageFromHash, getImageFromHash(this, url2));
            urlImgCache.put(url.toString(), imageCreateImageWithResolutionVariant);
            return imageCreateImageWithResolutionVariant;
        }
    }

    @Override // java.awt.Toolkit
    public Image createImage(String str) {
        checkPermissions(str);
        return createImage(new FileImageSource(str));
    }

    @Override // java.awt.Toolkit
    public Image createImage(URL url) {
        checkPermissions(url);
        return createImage(new URLImageSource(url));
    }

    @Override // java.awt.Toolkit
    public Image createImage(byte[] bArr, int i2, int i3) {
        return createImage(new ByteArrayImageSource(bArr, i2, i3));
    }

    @Override // java.awt.Toolkit
    public Image createImage(ImageProducer imageProducer) {
        return new ToolkitImage(imageProducer);
    }

    public static Image createImageWithResolutionVariant(Image image, Image image2) {
        return new MultiResolutionToolkitImage(image, image2);
    }

    @Override // java.awt.Toolkit
    public int checkImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        int iCheck;
        if (!(image instanceof ToolkitImage)) {
            return 32;
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (i2 == 0 || i3 == 0) {
            iCheck = 32;
        } else {
            iCheck = toolkitImage.getImageRep().check(imageObserver);
        }
        return (toolkitImage.check(imageObserver) | iCheck) & checkResolutionVariant(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Toolkit
    public boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        if (i2 == 0 || i3 == 0 || !(image instanceof ToolkitImage)) {
            return true;
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (toolkitImage.hasError()) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        return toolkitImage.getImageRep().prepare(imageObserver) & prepareResolutionVariant(image, i2, i3, imageObserver);
    }

    private int checkResolutionVariant(Image image, int i2, int i3, ImageObserver imageObserver) {
        ToolkitImage resolutionVariant = getResolutionVariant(image);
        int rVSize = getRVSize(i2);
        int rVSize2 = getRVSize(i3);
        if (resolutionVariant == null || resolutionVariant.hasError()) {
            return 65535;
        }
        return checkImage(resolutionVariant, rVSize, rVSize2, MultiResolutionToolkitImage.getResolutionVariantObserver(image, imageObserver, i2, i3, rVSize, rVSize2, true));
    }

    private boolean prepareResolutionVariant(Image image, int i2, int i3, ImageObserver imageObserver) {
        ToolkitImage resolutionVariant = getResolutionVariant(image);
        int rVSize = getRVSize(i2);
        int rVSize2 = getRVSize(i3);
        return resolutionVariant == null || resolutionVariant.hasError() || prepareImage(resolutionVariant, rVSize, rVSize2, MultiResolutionToolkitImage.getResolutionVariantObserver(image, imageObserver, i2, i3, rVSize, rVSize2, true));
    }

    private static int getRVSize(int i2) {
        if (i2 == -1) {
            return -1;
        }
        return 2 * i2;
    }

    private static ToolkitImage getResolutionVariant(Image image) {
        if (image instanceof MultiResolutionToolkitImage) {
            Image resolutionVariant = ((MultiResolutionToolkitImage) image).getResolutionVariant();
            if (resolutionVariant instanceof ToolkitImage) {
                return (ToolkitImage) resolutionVariant;
            }
            return null;
        }
        return null;
    }

    protected static boolean imageCached(String str) {
        return fileImgCache.containsKey(str);
    }

    protected static boolean imageCached(URL url) {
        return urlImgCache.containsKey(url.toString());
    }

    protected static boolean imageExists(String str) {
        if (str != null) {
            checkPermissions(str);
            return new File(str).exists();
        }
        return false;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:25:0x0047
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    /* JADX WARN: Unreachable blocks removed: 14, instructions: 22 */
    protected static boolean imageExists(java.net.URL r3) {
        /*
            r0 = r3
            if (r0 == 0) goto L5c
            r0 = r3
            checkPermissions(r0)
            r0 = r3
            java.io.InputStream r0 = r0.openStream()     // Catch: java.io.IOException -> L59
            r4 = r0
            r0 = 0
            r5 = r0
            r0 = 1
            r6 = r0
            r0 = r4
            if (r0 == 0) goto L2f
            r0 = r5
            if (r0 == 0) goto L2b
            r0 = r4
            r0.close()     // Catch: java.lang.Throwable -> L20 java.io.IOException -> L59
            goto L2f
        L20:
            r7 = move-exception
            r0 = r5
            r1 = r7
            r0.addSuppressed(r1)     // Catch: java.io.IOException -> L59
            goto L2f
        L2b:
            r0 = r4
            r0.close()     // Catch: java.io.IOException -> L59
        L2f:
            r0 = r6
            return r0
        L31:
            r6 = move-exception
            r0 = r6
            r5 = r0
            r0 = r6
            throw r0     // Catch: java.lang.Throwable -> L36 java.io.IOException -> L59
        L36:
            r8 = move-exception
            r0 = r4
            if (r0 == 0) goto L56
            r0 = r5
            if (r0 == 0) goto L52
            r0 = r4
            r0.close()     // Catch: java.lang.Throwable -> L47 java.io.IOException -> L59
            goto L56
        L47:
            r9 = move-exception
            r0 = r5
            r1 = r9
            r0.addSuppressed(r1)     // Catch: java.io.IOException -> L59
            goto L56
        L52:
            r0 = r4
            r0.close()     // Catch: java.io.IOException -> L59
        L56:
            r0 = r8
            throw r0     // Catch: java.io.IOException -> L59
        L59:
            r4 = move-exception
            r0 = 0
            return r0
        L5c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.SunToolkit.imageExists(java.net.URL):boolean");
    }

    private static void checkPermissions(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(str);
        }
    }

    private static void checkPermissions(URL url) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                Permission connectPermission = URLUtil.getConnectPermission(url);
                if (connectPermission != null) {
                    try {
                        securityManager.checkPermission(connectPermission);
                    } catch (SecurityException e2) {
                        if ((connectPermission instanceof FilePermission) && connectPermission.getActions().indexOf("read") != -1) {
                            securityManager.checkRead(connectPermission.getName());
                        } else if ((connectPermission instanceof SocketPermission) && connectPermission.getActions().indexOf(SecurityConstants.SOCKET_CONNECT_ACTION) != -1) {
                            securityManager.checkConnect(url.getHost(), url.getPort());
                        } else {
                            throw e2;
                        }
                    }
                }
            } catch (IOException e3) {
                securityManager.checkConnect(url.getHost(), url.getPort());
            }
        }
    }

    public static BufferedImage getScaledIconImage(java.util.List<Image> list, int i2, int i3) {
        double dFloor;
        int iRound;
        int iRound2;
        double d2;
        if (i2 == 0 || i3 == 0) {
            return null;
        }
        Image image = null;
        int i4 = 0;
        int i5 = 0;
        double d3 = 3.0d;
        for (Image image2 : list) {
            if (image2 != null) {
                if (image2 instanceof ToolkitImage) {
                    ((ToolkitImage) image2).getImageRep().reconstruct(32);
                }
                try {
                    int width = image2.getWidth(null);
                    int height = image2.getHeight(null);
                    if (width > 0 && height > 0) {
                        double dMin = Math.min(i2 / width, i3 / height);
                        if (dMin >= 2.0d) {
                            dFloor = Math.floor(dMin);
                            iRound = width * ((int) dFloor);
                            iRound2 = height * ((int) dFloor);
                            d2 = 1.0d - (0.5d / dFloor);
                        } else if (dMin >= 1.0d) {
                            dFloor = 1.0d;
                            iRound = width;
                            iRound2 = height;
                            d2 = 0.0d;
                        } else if (dMin >= 0.75d) {
                            dFloor = 0.75d;
                            iRound = (width * 3) / 4;
                            iRound2 = (height * 3) / 4;
                            d2 = 0.3d;
                        } else if (dMin >= 0.6666d) {
                            dFloor = 0.6666d;
                            iRound = (width * 2) / 3;
                            iRound2 = (height * 2) / 3;
                            d2 = 0.33d;
                        } else {
                            double dCeil = Math.ceil(1.0d / dMin);
                            dFloor = 1.0d / dCeil;
                            iRound = (int) Math.round(width / dCeil);
                            iRound2 = (int) Math.round(height / dCeil);
                            d2 = 1.0d - (1.0d / dCeil);
                        }
                        double d4 = ((i2 - iRound) / i2) + ((i3 - iRound2) / i3) + d2;
                        if (d4 < d3) {
                            d3 = d4;
                            image = image2;
                            i4 = iRound;
                            i5 = iRound2;
                        }
                        if (d4 == 0.0d) {
                            break;
                        }
                    }
                } catch (Exception e2) {
                }
            }
        }
        if (image == null) {
            return null;
        }
        BufferedImage bufferedImage = new BufferedImage(i2, i3, 2);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        graphics2DCreateGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        try {
            graphics2DCreateGraphics.drawImage(image, (i2 - i4) / 2, (i3 - i5) / 2, i4, i5, null);
            graphics2DCreateGraphics.dispose();
            return bufferedImage;
        } catch (Throwable th) {
            graphics2DCreateGraphics.dispose();
            throw th;
        }
    }

    public static DataBufferInt getScaledIconData(java.util.List<Image> list, int i2, int i3) {
        BufferedImage scaledIconImage = getScaledIconImage(list, i2, i3);
        if (scaledIconImage == null) {
            return null;
        }
        return (DataBufferInt) scaledIconImage.getRaster().getDataBuffer();
    }

    @Override // java.awt.Toolkit
    protected EventQueue getSystemEventQueueImpl() {
        return getSystemEventQueueImplPP();
    }

    static EventQueue getSystemEventQueueImplPP() {
        return getSystemEventQueueImplPP(AppContext.getAppContext());
    }

    public static EventQueue getSystemEventQueueImplPP(AppContext appContext) {
        return (EventQueue) appContext.get(AppContext.EVENT_QUEUE_KEY);
    }

    public static Container getNativeContainer(Component component) {
        return Toolkit.getNativeContainer(component);
    }

    public static Component getHeavyweightComponent(Component component) {
        while (component != null && AWTAccessor.getComponentAccessor().isLightweight(component)) {
            component = AWTAccessor.getComponentAccessor().getParent(component);
        }
        return component;
    }

    public int getFocusAcceleratorKeyMask() {
        return 8;
    }

    public boolean isPrintableCharacterModifiersMask(int i2) {
        return (i2 & 8) == (i2 & 2);
    }

    public boolean canPopupOverlapTaskBar() {
        boolean z2 = true;
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.AWT.SET_WINDOW_ALWAYS_ON_TOP_PERMISSION);
            }
        } catch (SecurityException e2) {
            z2 = false;
        }
        return z2;
    }

    @Override // sun.awt.InputMethodSupport
    public Window createInputMethodWindow(String str, InputContext inputContext) {
        return new SimpleInputMethodWindow(str, inputContext);
    }

    @Override // sun.awt.InputMethodSupport
    public boolean enableInputMethodsForTextComponent() {
        return false;
    }

    public static Locale getStartupLocale() {
        String strSubstring;
        String strSubstring2;
        if (startupLocale == null) {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("user.language", "en"));
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.region"));
            if (str2 != null) {
                int iIndexOf = str2.indexOf(95);
                if (iIndexOf >= 0) {
                    strSubstring = str2.substring(0, iIndexOf);
                    strSubstring2 = str2.substring(iIndexOf + 1);
                } else {
                    strSubstring = str2;
                    strSubstring2 = "";
                }
            } else {
                strSubstring = (String) AccessController.doPrivileged(new GetPropertyAction("user.country", ""));
                strSubstring2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.variant", ""));
            }
            startupLocale = new Locale(str, strSubstring, strSubstring2);
        }
        return startupLocale;
    }

    @Override // sun.awt.InputMethodSupport
    public Locale getDefaultKeyboardLocale() {
        return getStartupLocale();
    }

    @Override // sun.awt.WindowClosingSupport
    public WindowClosingListener getWindowClosingListener() {
        return this.windowClosingListener;
    }

    @Override // sun.awt.WindowClosingSupport
    public void setWindowClosingListener(WindowClosingListener windowClosingListener) {
        this.windowClosingListener = windowClosingListener;
    }

    @Override // sun.awt.WindowClosingListener
    public RuntimeException windowClosingNotify(WindowEvent windowEvent) {
        if (this.windowClosingListener != null) {
            return this.windowClosingListener.windowClosingNotify(windowEvent);
        }
        return null;
    }

    @Override // sun.awt.WindowClosingListener
    public RuntimeException windowClosingDelivered(WindowEvent windowEvent) {
        if (this.windowClosingListener != null) {
            return this.windowClosingListener.windowClosingDelivered(windowEvent);
        }
        return null;
    }

    @Override // java.awt.Toolkit
    protected synchronized MouseInfoPeer getMouseInfoPeer() {
        if (mPeer == null) {
            mPeer = new DefaultMouseInfoPeer();
        }
        return mPeer;
    }

    public static boolean needsXEmbed() {
        if ("true".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.awt.noxembed", "false")))) {
            return false;
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            return ((SunToolkit) defaultToolkit).needsXEmbedImpl();
        }
        return false;
    }

    protected boolean needsXEmbedImpl() {
        return false;
    }

    protected final boolean isXEmbedServerRequested() {
        return ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.awt.xembedserver"))).booleanValue();
    }

    public static boolean isModalExcludedSupported() {
        return Toolkit.getDefaultToolkit().isModalExclusionTypeSupported(DEFAULT_MODAL_EXCLUSION_TYPE);
    }

    protected boolean isModalExcludedSupportedImpl() {
        return false;
    }

    public static void setModalExcluded(Window window) {
        if (DEFAULT_MODAL_EXCLUSION_TYPE == null) {
            DEFAULT_MODAL_EXCLUSION_TYPE = Dialog.ModalExclusionType.APPLICATION_EXCLUDE;
        }
        window.setModalExclusionType(DEFAULT_MODAL_EXCLUSION_TYPE);
    }

    public static boolean isModalExcluded(Window window) {
        if (DEFAULT_MODAL_EXCLUSION_TYPE == null) {
            DEFAULT_MODAL_EXCLUSION_TYPE = Dialog.ModalExclusionType.APPLICATION_EXCLUDE;
        }
        return window.getModalExclusionType().compareTo(DEFAULT_MODAL_EXCLUSION_TYPE) >= 0;
    }

    @Override // java.awt.Toolkit
    public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
        return modalityType == Dialog.ModalityType.MODELESS || modalityType == Dialog.ModalityType.APPLICATION_MODAL;
    }

    @Override // java.awt.Toolkit
    public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
        return modalExclusionType == Dialog.ModalExclusionType.NO_EXCLUDE;
    }

    public void addModalityListener(ModalityListener modalityListener) {
        this.modalityListeners.add(modalityListener);
    }

    public void removeModalityListener(ModalityListener modalityListener) {
        this.modalityListeners.remove(modalityListener);
    }

    public void notifyModalityPushed(Dialog dialog) {
        notifyModalityChange(ModalityEvent.MODALITY_PUSHED, dialog);
    }

    public void notifyModalityPopped(Dialog dialog) {
        notifyModalityChange(ModalityEvent.MODALITY_POPPED, dialog);
    }

    final void notifyModalityChange(int i2, Dialog dialog) {
        new ModalityEvent(dialog, this.modalityListeners, i2).dispatch();
    }

    /* loaded from: rt.jar:sun/awt/SunToolkit$ModalityListenerList.class */
    static class ModalityListenerList implements ModalityListener {
        Vector<ModalityListener> listeners = new Vector<>();

        ModalityListenerList() {
        }

        void add(ModalityListener modalityListener) {
            this.listeners.addElement(modalityListener);
        }

        void remove(ModalityListener modalityListener) {
            this.listeners.removeElement(modalityListener);
        }

        @Override // sun.awt.ModalityListener
        public void modalityPushed(ModalityEvent modalityEvent) {
            Iterator<ModalityListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().modalityPushed(modalityEvent);
            }
        }

        @Override // sun.awt.ModalityListener
        public void modalityPopped(ModalityEvent modalityEvent) {
            Iterator<ModalityListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                it.next().modalityPopped(modalityEvent);
            }
        }
    }

    public static boolean isLightweightOrUnknown(Component component) {
        if (component.isLightweight() || !(getDefaultToolkit() instanceof SunToolkit)) {
            return true;
        }
        return ((component instanceof Button) || (component instanceof Canvas) || (component instanceof Checkbox) || (component instanceof Choice) || (component instanceof Label) || (component instanceof List) || (component instanceof Panel) || (component instanceof Scrollbar) || (component instanceof ScrollPane) || (component instanceof TextArea) || (component instanceof TextField) || (component instanceof Window)) ? false : true;
    }

    /* loaded from: rt.jar:sun/awt/SunToolkit$OperationTimedOut.class */
    public static class OperationTimedOut extends RuntimeException {
        public OperationTimedOut(String str) {
            super(str);
        }

        public OperationTimedOut() {
        }
    }

    /* loaded from: rt.jar:sun/awt/SunToolkit$IllegalThreadException.class */
    public static class IllegalThreadException extends RuntimeException {
        public IllegalThreadException(String str) {
            super(str);
        }

        public IllegalThreadException() {
        }
    }

    public void realSync() throws InfiniteLoop, OperationTimedOut {
        realSync(10000L);
    }

    public void realSync(long j2) throws InfiniteLoop, OperationTimedOut {
        if (EventQueue.isDispatchThread()) {
            throw new IllegalThreadException("The SunToolkit.realSync() method cannot be used on the event dispatch thread (EDT).");
        }
        int i2 = 0;
        do {
            sync();
            int i3 = 0;
            while (i3 < 0) {
                syncNativeQueue(j2);
                i3++;
            }
            while (syncNativeQueue(j2) && i3 < 20) {
                i3++;
            }
            if (i3 >= 20) {
                throw new InfiniteLoop();
            }
            int i4 = 0;
            while (i4 < 0) {
                waitForIdle(j2);
                i4++;
            }
            while (waitForIdle(j2) && i4 < 20) {
                i4++;
            }
            if (i4 >= 20) {
                throw new InfiniteLoop();
            }
            i2++;
            if (!syncNativeQueue(j2) && !waitForIdle(j2)) {
                return;
            }
        } while (i2 < 20);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEQEmpty() {
        return AWTAccessor.getEventQueueAccessor().noEvents(getSystemEventQueueImpl());
    }

    protected final boolean waitForIdle(final long j2) {
        boolean zIsEQEmpty;
        boolean z2;
        flushPendingEvents();
        synchronized (this.waitLock) {
            zIsEQEmpty = isEQEmpty();
            this.queueEmpty = false;
            this.eventDispatched = false;
            postEvent(AppContext.getAppContext(), new PeerEvent(getSystemEventQueueImpl(), null, 4L) { // from class: sun.awt.SunToolkit.3
                @Override // java.awt.event.InvocationEvent, java.awt.ActiveEvent
                public void dispatch() {
                    int i2 = 0;
                    while (i2 < 0) {
                        SunToolkit.this.syncNativeQueue(j2);
                        i2++;
                    }
                    while (SunToolkit.this.syncNativeQueue(j2) && i2 < 20) {
                        i2++;
                    }
                    SunToolkit.flushPendingEvents();
                    synchronized (SunToolkit.this.waitLock) {
                        SunToolkit.this.queueEmpty = SunToolkit.this.isEQEmpty();
                        SunToolkit.this.eventDispatched = true;
                        SunToolkit.this.waitLock.notifyAll();
                    }
                }
            });
            while (!this.eventDispatched) {
                try {
                    this.waitLock.wait();
                } catch (InterruptedException e2) {
                    return false;
                }
            }
        }
        try {
            Thread.sleep(0L);
            flushPendingEvents();
            synchronized (this.waitLock) {
                z2 = (this.queueEmpty && isEQEmpty() && zIsEQEmpty) ? false : true;
            }
            return z2;
        } catch (InterruptedException e3) {
            throw new RuntimeException("Interrupted");
        }
    }

    public void showOrHideTouchKeyboard(Component component, AWTEvent aWTEvent) {
    }

    public static boolean isTouchKeyboardAutoShowEnabled() {
        return touchKeyboardAutoShowIsEnabled;
    }

    private void fireDesktopFontPropertyChanges() {
        setDesktopProperty(DESKTOPFONTHINTS, getDesktopFontHints());
    }

    public static void setAAFontSettingsCondition(boolean z2) {
        if (z2 != lastExtraCondition) {
            lastExtraCondition = z2;
            if (checkedSystemAAFontSettings) {
                checkedSystemAAFontSettings = false;
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                if (defaultToolkit instanceof SunToolkit) {
                    ((SunToolkit) defaultToolkit).fireDesktopFontPropertyChanges();
                }
            }
        }
    }

    private static RenderingHints getDesktopAAHintsByName(String str) {
        Object obj = null;
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals(FXMLLoader.EVENT_HANDLER_PREFIX)) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
        } else if (lowerCase.equals("gasp")) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
        } else if (lowerCase.equals("lcd") || lowerCase.equals("lcd_hrgb")) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
        } else if (lowerCase.equals("lcd_hbgr")) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
        } else if (lowerCase.equals("lcd_vrgb")) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
        } else if (lowerCase.equals("lcd_vbgr")) {
            obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
        }
        if (obj != null) {
            RenderingHints renderingHints = new RenderingHints(null);
            renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, obj);
            return renderingHints;
        }
        return null;
    }

    private static boolean useSystemAAFontSettings() {
        if (!checkedSystemAAFontSettings) {
            useSystemAAFontSettings = true;
            String str = null;
            if (Toolkit.getDefaultToolkit() instanceof SunToolkit) {
                str = (String) AccessController.doPrivileged(new GetPropertyAction("awt.useSystemAAFontSettings"));
            }
            if (str != null) {
                useSystemAAFontSettings = Boolean.valueOf(str).booleanValue();
                if (!useSystemAAFontSettings) {
                    desktopFontHints = getDesktopAAHintsByName(str);
                }
            }
            if (useSystemAAFontSettings) {
                useSystemAAFontSettings = lastExtraCondition;
            }
            checkedSystemAAFontSettings = true;
        }
        return useSystemAAFontSettings;
    }

    protected RenderingHints getDesktopAAHints() {
        return null;
    }

    public static RenderingHints getDesktopFontHints() {
        if (useSystemAAFontSettings()) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if (defaultToolkit instanceof SunToolkit) {
                return ((SunToolkit) defaultToolkit).getDesktopAAHints();
            }
            return null;
        }
        if (desktopFontHints != null) {
            return (RenderingHints) desktopFontHints.clone();
        }
        return null;
    }

    public static synchronized void consumeNextKeyTyped(KeyEvent keyEvent) {
        try {
            AWTAccessor.getDefaultKeyboardFocusManagerAccessor().consumeNextKeyTyped((DefaultKeyboardFocusManager) KeyboardFocusManager.getCurrentKeyboardFocusManager(), keyEvent);
        } catch (ClassCastException e2) {
            e2.printStackTrace();
        }
    }

    protected static void dumpPeers(PlatformLogger platformLogger) {
        AWTAutoShutdown.getInstance().dumpPeers(platformLogger);
    }

    public static Window getContainingWindow(Component component) {
        while (component != null && !(component instanceof Window)) {
            component = component.getParent();
        }
        return (Window) component;
    }

    public static synchronized boolean getSunAwtDisableMixing() {
        if (sunAwtDisableMixing == null) {
            sunAwtDisableMixing = (Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.awt.disableMixing"));
        }
        return sunAwtDisableMixing.booleanValue();
    }

    public boolean isNativeGTKAvailable() {
        return false;
    }

    public synchronized void setWindowDeactivationTime(Window window, long j2) {
        AppContext appContext = getAppContext(window);
        if (appContext == null) {
            return;
        }
        WeakHashMap weakHashMap = (WeakHashMap) appContext.get(DEACTIVATION_TIMES_MAP_KEY);
        if (weakHashMap == null) {
            weakHashMap = new WeakHashMap();
            appContext.put(DEACTIVATION_TIMES_MAP_KEY, weakHashMap);
        }
        weakHashMap.put(window, Long.valueOf(j2));
    }

    public synchronized long getWindowDeactivationTime(Window window) {
        WeakHashMap weakHashMap;
        Long l2;
        AppContext appContext = getAppContext(window);
        if (appContext == null || (weakHashMap = (WeakHashMap) appContext.get(DEACTIVATION_TIMES_MAP_KEY)) == null || (l2 = (Long) weakHashMap.get(window)) == null) {
            return -1L;
        }
        return l2.longValue();
    }

    public boolean isWindowOpacitySupported() {
        return false;
    }

    public boolean isWindowShapingSupported() {
        return false;
    }

    public boolean isWindowTranslucencySupported() {
        return false;
    }

    public boolean isTranslucencyCapable(GraphicsConfiguration graphicsConfiguration) {
        return false;
    }

    public boolean isSwingBackbufferTranslucencySupported() {
        return false;
    }

    public static boolean isContainingTopLevelOpaque(Component component) {
        Window containingWindow = getContainingWindow(component);
        return containingWindow != null && containingWindow.isOpaque();
    }

    public static boolean isContainingTopLevelTranslucent(Component component) {
        Window containingWindow = getContainingWindow(component);
        return containingWindow != null && containingWindow.getOpacity() < 1.0f;
    }

    public boolean needUpdateWindow() {
        return false;
    }

    public int getNumberOfButtons() {
        return 3;
    }

    public static boolean isInstanceOf(Object obj, String str) {
        if (obj == null || str == null) {
            return false;
        }
        return isInstanceOf(obj.getClass(), str);
    }

    private static boolean isInstanceOf(Class<?> cls, String str) {
        if (cls == null) {
            return false;
        }
        if (cls.getName().equals(str)) {
            return true;
        }
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (cls2.getName().equals(str)) {
                return true;
            }
        }
        return isInstanceOf((Class<?>) cls.getSuperclass(), str);
    }

    protected static LightweightFrame getLightweightFrame(Component component) {
        while (component != null) {
            if (component instanceof LightweightFrame) {
                return (LightweightFrame) component;
            }
            if (!(component instanceof Window)) {
                component = component.getParent();
            } else {
                return null;
            }
        }
        return null;
    }

    public static void setSystemGenerated(AWTEvent aWTEvent) {
        AWTAccessor.getAWTEventAccessor().setSystemGenerated(aWTEvent);
    }

    public static boolean isSystemGenerated(AWTEvent aWTEvent) {
        return AWTAccessor.getAWTEventAccessor().isSystemGenerated(aWTEvent);
    }
}
