package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.DataBufferInt;
import java.awt.peer.ComponentPeer;
import java.awt.peer.WindowPeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.DisplayChangedListener;
import sun.awt.SunToolkit;
import sun.awt.TimedWindowEvent;
import sun.awt.Win32GraphicsConfig;
import sun.awt.Win32GraphicsDevice;
import sun.awt.Win32GraphicsEnvironment;
import sun.java2d.pipe.Region;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WWindowPeer.class */
public class WWindowPeer extends WPanelPeer implements WindowPeer, DisplayChangedListener {
    private WWindowPeer modalBlocker;
    private boolean isOpaque;
    private TranslucentWindowPainter painter;
    private WindowListener windowListener;
    private volatile Window.Type windowType;
    private volatile int sysX;
    private volatile int sysY;
    private volatile int sysW;
    private volatile int sysH;
    private float opacity;
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WWindowPeer");
    private static final PlatformLogger screenLog = PlatformLogger.getLogger("sun.awt.windows.screen.WWindowPeer");
    private static final StringBuffer ACTIVE_WINDOWS_KEY = new StringBuffer("active_windows_list");
    private static PropertyChangeListener activeWindowListener = new ActiveWindowListener();
    private static final PropertyChangeListener guiDisposedListener = new GuiDisposedListener();

    private static native void initIDs();

    private native void _toFront();

    public native void toBack();

    private native void setAlwaysOnTopNative(boolean z2);

    native void setFocusableWindow(boolean z2);

    private native void _setTitle(String str);

    private native void _setResizable(boolean z2);

    native void createAwtWindow(WComponentPeer wComponentPeer);

    native void updateInsets(Insets insets);

    static native int getSysMinWidth();

    static native int getSysMinHeight();

    static native int getSysIconWidth();

    static native int getSysIconHeight();

    static native int getSysSmIconWidth();

    static native int getSysSmIconHeight();

    native void setIconImagesData(int[] iArr, int i2, int i3, int[] iArr2, int i4, int i5);

    native synchronized void reshapeFrame(int i2, int i3, int i4, int i5);

    private native boolean requestWindowFocus(boolean z2);

    native void setMinSize(int i2, int i3);

    native void modalDisable(Dialog dialog, long j2);

    native void modalEnable(Dialog dialog);

    private native int getScreenImOn();

    public final native void setFullScreenExclusiveModeState(boolean z2);

    private native void nativeGrab();

    private native void nativeUngrab();

    @Override // java.awt.peer.WindowPeer
    public native void repositionSecurityWarning();

    private native void setOpacity(int i2);

    private native void setOpaqueImpl(boolean z2);

    native void updateWindowImpl(int[] iArr, int i2, int i3);

    @Override // sun.awt.windows.WPanelPeer
    public /* bridge */ /* synthetic */ Insets insets() {
        return super.insets();
    }

    @Override // sun.awt.windows.WPanelPeer, java.awt.peer.ContainerPeer
    public /* bridge */ /* synthetic */ Insets getInsets() {
        return super.getInsets();
    }

    @Override // sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public /* bridge */ /* synthetic */ void paint(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // sun.awt.windows.WCanvasPeer, java.awt.peer.CanvasPeer
    public /* bridge */ /* synthetic */ GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        return super.getAppropriateGraphicsConfiguration(graphicsConfiguration);
    }

    @Override // sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    public /* bridge */ /* synthetic */ boolean shouldClearRectBeforePaint() {
        return super.shouldClearRectBeforePaint();
    }

    static {
        initIDs();
    }

    @Override // sun.awt.windows.WComponentPeer, sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(this.target);
        synchronized (appContextTargetToAppContext) {
            List list = (List) appContextTargetToAppContext.get(ACTIVE_WINDOWS_KEY);
            if (list != null) {
                list.remove(this);
            }
        }
        ((Win32GraphicsDevice) getGraphicsConfiguration().getDevice()).removeDisplayChangedListener(this);
        synchronized (getStateLock()) {
            TranslucentWindowPainter translucentWindowPainter = this.painter;
            if (translucentWindowPainter != null) {
                translucentWindowPainter.flush();
            }
        }
        super.disposeImpl();
    }

    public void toFront() {
        updateFocusableWindowState();
        _toFront();
    }

    public void setAlwaysOnTop(boolean z2) {
        if ((z2 && ((Window) this.target).isVisible()) || !z2) {
            setAlwaysOnTopNative(z2);
        }
    }

    public void updateAlwaysOnTopState() {
        setAlwaysOnTop(((Window) this.target).isAlwaysOnTop());
    }

    public void updateFocusableWindowState() {
        setFocusableWindow(((Window) this.target).isFocusableWindow());
    }

    public void setTitle(String str) {
        if (str == null) {
            str = "";
        }
        _setTitle(str);
    }

    public void setResizable(boolean z2) {
        _setResizable(z2);
    }

    WWindowPeer(Window window) {
        super(window);
        this.modalBlocker = null;
        this.windowType = Window.Type.NORMAL;
        this.sysX = 0;
        this.sysY = 0;
        this.sysW = 0;
        this.sysH = 0;
        this.opacity = 1.0f;
    }

    @Override // sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        super.initialize();
        updateInsets(this.insets_);
        if (((Window) this.target).getFont() == null) {
            Font font = defaultFont;
            ((Window) this.target).setFont(font);
            setFont(font);
        }
        ((Win32GraphicsDevice) getGraphicsConfiguration().getDevice()).addDisplayChangedListener(this);
        initActiveWindowsTracking((Window) this.target);
        updateIconImages();
        Shape shape = ((Window) this.target).getShape();
        if (shape != null) {
            applyShape(Region.getInstance(shape, null));
        }
        float opacity = ((Window) this.target).getOpacity();
        if (opacity < 1.0f) {
            setOpacity(opacity);
        }
        synchronized (getStateLock()) {
            this.isOpaque = true;
            setOpaque(((Window) this.target).isOpaque());
        }
    }

    void preCreate(WComponentPeer wComponentPeer) {
        this.windowType = ((Window) this.target).getType();
    }

    @Override // sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void create(WComponentPeer wComponentPeer) {
        preCreate(wComponentPeer);
        createAwtWindow(wComponentPeer);
    }

    @Override // sun.awt.windows.WComponentPeer
    final WComponentPeer getNativeParent() {
        return (WComponentPeer) WToolkit.targetToPeer(((Window) this.target).getOwner());
    }

    protected void realShow() {
        super.show();
    }

    @Override // sun.awt.windows.WComponentPeer
    public void show() {
        updateFocusableWindowState();
        boolean zIsAlwaysOnTop = ((Window) this.target).isAlwaysOnTop();
        updateGC();
        realShow();
        updateMinimumSize();
        if (((Window) this.target).isAlwaysOnTopSupported() && zIsAlwaysOnTop) {
            setAlwaysOnTop(zIsAlwaysOnTop);
        }
        synchronized (getStateLock()) {
            if (!this.isOpaque) {
                updateWindow(true);
            }
        }
        WComponentPeer nativeParent = getNativeParent();
        if (nativeParent != null && nativeParent.isLightweightFramePeer()) {
            Rectangle bounds = getBounds();
            handleExpose(0, 0, bounds.width, bounds.height);
        }
    }

    public boolean requestWindowFocus(CausedFocusEvent.Cause cause) {
        if (!focusAllowedFor()) {
            return false;
        }
        return requestWindowFocus(cause == CausedFocusEvent.Cause.MOUSE_EVENT);
    }

    public boolean focusAllowedFor() {
        Window window = (Window) this.target;
        if (!window.isVisible() || !window.isEnabled() || !window.isFocusableWindow() || isModalBlocked()) {
            return false;
        }
        return true;
    }

    @Override // sun.awt.windows.WComponentPeer
    void hide() {
        WindowListener windowListener = this.windowListener;
        if (windowListener != null) {
            windowListener.windowClosing(new WindowEvent((Window) this.target, 201));
        }
        super.hide();
    }

    @Override // sun.awt.windows.WComponentPeer
    void preprocessPostEvent(AWTEvent aWTEvent) {
        WindowListener windowListener;
        if ((aWTEvent instanceof WindowEvent) && (windowListener = this.windowListener) != null) {
            switch (aWTEvent.getID()) {
                case 201:
                    windowListener.windowClosing((WindowEvent) aWTEvent);
                    break;
                case 203:
                    windowListener.windowIconified((WindowEvent) aWTEvent);
                    break;
            }
        }
    }

    private void notifyWindowStateChanged(int i2, int i3) {
        int i4 = i2 ^ i3;
        if (i4 == 0) {
            return;
        }
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("Reporting state change %x -> %x", Integer.valueOf(i2), Integer.valueOf(i3));
        }
        if (this.target instanceof Frame) {
            AWTAccessor.getFrameAccessor().setExtendedState((Frame) this.target, i3);
        }
        if ((i4 & 1) > 0) {
            if ((i3 & 1) > 0) {
                postEvent(new TimedWindowEvent((Window) this.target, 203, null, 0, 0, System.currentTimeMillis()));
            } else {
                postEvent(new TimedWindowEvent((Window) this.target, 204, null, 0, 0, System.currentTimeMillis()));
            }
        }
        postEvent(new TimedWindowEvent((Window) this.target, 209, null, i2, i3, System.currentTimeMillis()));
    }

    synchronized void addWindowListener(WindowListener windowListener) {
        this.windowListener = AWTEventMulticaster.add(this.windowListener, windowListener);
    }

    synchronized void removeWindowListener(WindowListener windowListener) {
        this.windowListener = AWTEventMulticaster.remove(this.windowListener, windowListener);
    }

    public void updateMinimumSize() {
        Dimension minimumSize = null;
        if (((Component) this.target).isMinimumSizeSet()) {
            minimumSize = ((Component) this.target).getMinimumSize();
        }
        if (minimumSize != null) {
            int sysMinWidth = getSysMinWidth();
            int sysMinHeight = getSysMinHeight();
            setMinSize(minimumSize.width >= sysMinWidth ? minimumSize.width : sysMinWidth, minimumSize.height >= sysMinHeight ? minimumSize.height : sysMinHeight);
            return;
        }
        setMinSize(0, 0);
    }

    public void updateIconImages() {
        List<Image> iconImages = ((Window) this.target).getIconImages();
        if (iconImages == null || iconImages.size() == 0) {
            setIconImagesData(null, 0, 0, null, 0, 0);
            return;
        }
        int sysIconWidth = getSysIconWidth();
        int sysIconHeight = getSysIconHeight();
        int sysSmIconWidth = getSysSmIconWidth();
        int sysSmIconHeight = getSysSmIconHeight();
        DataBufferInt scaledIconData = SunToolkit.getScaledIconData(iconImages, sysIconWidth, sysIconHeight);
        DataBufferInt scaledIconData2 = SunToolkit.getScaledIconData(iconImages, sysSmIconWidth, sysSmIconHeight);
        if (scaledIconData != null && scaledIconData2 != null) {
            setIconImagesData(scaledIconData.getData(), sysIconWidth, sysIconHeight, scaledIconData2.getData(), sysSmIconWidth, sysSmIconHeight);
        } else {
            setIconImagesData(null, 0, 0, null, 0, 0);
        }
    }

    public boolean isModalBlocked() {
        return this.modalBlocker != null;
    }

    @Override // java.awt.peer.WindowPeer
    public void setModalBlocked(Dialog dialog, boolean z2) {
        synchronized (((Component) getTarget()).getTreeLock()) {
            WWindowPeer wWindowPeer = (WWindowPeer) dialog.getPeer();
            if (z2) {
                this.modalBlocker = wWindowPeer;
                if (wWindowPeer instanceof WFileDialogPeer) {
                    ((WFileDialogPeer) wWindowPeer).blockWindow(this);
                } else if (wWindowPeer instanceof WPrintDialogPeer) {
                    ((WPrintDialogPeer) wWindowPeer).blockWindow(this);
                } else {
                    modalDisable(dialog, wWindowPeer.getHWnd());
                }
            } else {
                this.modalBlocker = null;
                if (wWindowPeer instanceof WFileDialogPeer) {
                    ((WFileDialogPeer) wWindowPeer).unblockWindow(this);
                } else if (wWindowPeer instanceof WPrintDialogPeer) {
                    ((WPrintDialogPeer) wWindowPeer).unblockWindow(this);
                } else {
                    modalEnable(dialog);
                }
            }
        }
    }

    public static long[] getActiveWindowHandles(Component component) {
        AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(component);
        if (appContextTargetToAppContext == null) {
            return null;
        }
        synchronized (appContextTargetToAppContext) {
            List list = (List) appContextTargetToAppContext.get(ACTIVE_WINDOWS_KEY);
            if (list == null) {
                return null;
            }
            long[] jArr = new long[list.size()];
            for (int i2 = 0; i2 < list.size(); i2++) {
                jArr[i2] = ((WWindowPeer) list.get(i2)).getHWnd();
            }
            return jArr;
        }
    }

    void draggedToNewScreen() {
        displayChanged();
    }

    public void updateGC() {
        Win32GraphicsDevice win32GraphicsDevice;
        int screenImOn = getScreenImOn();
        if (screenLog.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("Screen number: " + screenImOn);
        }
        Win32GraphicsDevice win32GraphicsDevice2 = (Win32GraphicsDevice) this.winGraphicsConfig.getDevice();
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screenImOn >= screenDevices.length) {
            win32GraphicsDevice = (Win32GraphicsDevice) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        } else {
            win32GraphicsDevice = (Win32GraphicsDevice) screenDevices[screenImOn];
        }
        this.winGraphicsConfig = (Win32GraphicsConfig) win32GraphicsDevice.getDefaultConfiguration();
        if (screenLog.isLoggable(PlatformLogger.Level.FINE) && this.winGraphicsConfig == null) {
            screenLog.fine("Assertion (winGraphicsConfig != null) failed");
        }
        if (win32GraphicsDevice2 != win32GraphicsDevice) {
            win32GraphicsDevice2.removeDisplayChangedListener(this);
            win32GraphicsDevice.addDisplayChangedListener(this);
        }
        AWTAccessor.getComponentAccessor().setGraphicsConfiguration((Component) this.target, this.winGraphicsConfig);
    }

    @Override // sun.awt.DisplayChangedListener
    public void displayChanged() {
        SunToolkit.executeOnEventHandlerThread(this.target, this::updateGC);
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
    }

    public void grab() {
        nativeGrab();
    }

    public void ungrab() {
        nativeUngrab();
    }

    private final boolean hasWarningWindow() {
        return ((Window) this.target).getWarningString() != null;
    }

    boolean isTargetUndecorated() {
        return true;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setBounds(int i2, int i3, int i4, int i5, int i6) {
        this.sysX = i2;
        this.sysY = i3;
        this.sysW = i4;
        this.sysH = i5;
        super.setBounds(i2, i3, i4, i5, i6);
    }

    @Override // sun.awt.windows.WPanelPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void print(Graphics graphics) {
        Shape shape = AWTAccessor.getWindowAccessor().getShape((Window) this.target);
        if (shape != null) {
            graphics.setClip(shape);
        }
        super.print(graphics);
    }

    private void replaceSurfaceDataRecursively(Component component) {
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                replaceSurfaceDataRecursively(component2);
            }
        }
        ComponentPeer peer = component.getPeer();
        if (peer instanceof WComponentPeer) {
            ((WComponentPeer) peer).replaceSurfaceDataLater();
        }
    }

    public final Graphics getTranslucentGraphics() {
        Graphics graphics;
        synchronized (getStateLock()) {
            graphics = this.isOpaque ? null : this.painter.getBackBuffer(false).getGraphics();
        }
        return graphics;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setBackground(Color color) {
        super.setBackground(color);
        synchronized (getStateLock()) {
            if (!this.isOpaque && ((Window) this.target).isVisible()) {
                updateWindow(true);
            }
        }
    }

    public void setOpacity(float f2) {
        if (((SunToolkit) ((Window) this.target).getToolkit()).isWindowOpacitySupported()) {
            if (f2 < 0.0f || f2 > 1.0f) {
                throw new IllegalArgumentException("The value of opacity should be in the range [0.0f .. 1.0f].");
            }
            if (((this.opacity == 1.0f && f2 < 1.0f) || (this.opacity < 1.0f && f2 == 1.0f)) && !Win32GraphicsEnvironment.isVistaOS()) {
                replaceSurfaceDataRecursively((Component) getTarget());
            }
            this.opacity = f2;
            int i2 = (int) (f2 * 255.0f);
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 > 255) {
                i2 = 255;
            }
            setOpacity(i2);
            synchronized (getStateLock()) {
                if (!this.isOpaque && ((Window) this.target).isVisible()) {
                    updateWindow(true);
                }
            }
        }
    }

    public void setOpaque(boolean z2) {
        Shape shape;
        synchronized (getStateLock()) {
            if (this.isOpaque == z2) {
                return;
            }
            Window window = (Window) getTarget();
            if (!z2) {
                SunToolkit sunToolkit = (SunToolkit) window.getToolkit();
                if (!sunToolkit.isWindowTranslucencySupported() || !sunToolkit.isTranslucencyCapable(window.getGraphicsConfiguration())) {
                    return;
                }
            }
            boolean zIsVistaOS = Win32GraphicsEnvironment.isVistaOS();
            if (this.isOpaque != z2 && !zIsVistaOS) {
                replaceSurfaceDataRecursively(window);
            }
            synchronized (getStateLock()) {
                this.isOpaque = z2;
                setOpaqueImpl(z2);
                if (z2) {
                    TranslucentWindowPainter translucentWindowPainter = this.painter;
                    if (translucentWindowPainter != null) {
                        translucentWindowPainter.flush();
                        this.painter = null;
                    }
                } else {
                    this.painter = TranslucentWindowPainter.createInstance(this);
                }
            }
            if (zIsVistaOS && (shape = window.getShape()) != null) {
                window.setShape(shape);
            }
            if (window.isVisible()) {
                updateWindow(true);
            }
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.WindowPeer
    public void updateWindow() {
        updateWindow(false);
    }

    private void updateWindow(boolean z2) {
        Window window = (Window) this.target;
        synchronized (getStateLock()) {
            if (this.isOpaque || !window.isVisible() || window.getWidth() <= 0 || window.getHeight() <= 0) {
                return;
            }
            TranslucentWindowPainter translucentWindowPainter = this.painter;
            if (translucentWindowPainter != null) {
                translucentWindowPainter.updateWindow(z2);
            } else if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("Translucent window painter is null in updateWindow");
            }
        }
    }

    private static void initActiveWindowsTracking(Window window) {
        AppContext appContext = AppContext.getAppContext();
        synchronized (appContext) {
            if (((List) appContext.get(ACTIVE_WINDOWS_KEY)) == null) {
                appContext.put(ACTIVE_WINDOWS_KEY, new LinkedList());
                appContext.addPropertyChangeListener(AppContext.GUI_DISPOSED, guiDisposedListener);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("activeWindow", activeWindowListener);
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WWindowPeer$GuiDisposedListener.class */
    private static class GuiDisposedListener implements PropertyChangeListener {
        private GuiDisposedListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (!((Boolean) propertyChangeEvent.getNewValue()).booleanValue() && WWindowPeer.log.isLoggable(PlatformLogger.Level.FINE)) {
                WWindowPeer.log.fine(" Assertion (newValue != true) failed for AppContext.GUI_DISPOSED ");
            }
            AppContext appContext = AppContext.getAppContext();
            synchronized (appContext) {
                appContext.remove(WWindowPeer.ACTIVE_WINDOWS_KEY);
                appContext.removePropertyChangeListener(AppContext.GUI_DISPOSED, this);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("activeWindow", WWindowPeer.activeWindowListener);
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WWindowPeer$ActiveWindowListener.class */
    private static class ActiveWindowListener implements PropertyChangeListener {
        private ActiveWindowListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Window window = (Window) propertyChangeEvent.getNewValue();
            if (window == null) {
                return;
            }
            AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(window);
            synchronized (appContextTargetToAppContext) {
                WWindowPeer wWindowPeer = (WWindowPeer) window.getPeer();
                List list = (List) appContextTargetToAppContext.get(WWindowPeer.ACTIVE_WINDOWS_KEY);
                if (list != null) {
                    list.remove(wWindowPeer);
                    list.add(wWindowPeer);
                }
            }
        }
    }
}
