package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.JobAttributes;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PageAttributes;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.PrintJob;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SystemTray;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.im.InputMethodHighlight;
import java.awt.im.spi.InputMethodDescriptor;
import java.awt.image.ColorModel;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DesktopPeer;
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
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.text.JTextComponent;
import sun.awt.AWTAccessor;
import sun.awt.AWTAutoShutdown;
import sun.awt.AppContext;
import sun.awt.DisplayChangedListener;
import sun.awt.LightweightFrame;
import sun.awt.SunToolkit;
import sun.awt.Win32GraphicsDevice;
import sun.awt.Win32GraphicsEnvironment;
import sun.awt.datatransfer.DataTransferer;
import sun.font.FontManager;
import sun.font.FontManagerFactory;
import sun.font.SunFontManager;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.java2d.d3d.D3DRenderQueue;
import sun.java2d.opengl.OGLRenderQueue;
import sun.misc.PerformanceLogger;
import sun.misc.ThreadGroupUtils;
import sun.print.PrintJob2D;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WToolkit.class */
public final class WToolkit extends SunToolkit implements Runnable {
    public static final String XPSTYLE_THEME_ACTIVE = "win.xpstyle.themeActive";
    static GraphicsConfiguration config;
    WClipboard clipboard;
    private Hashtable<String, FontPeer> cacheFontPeer;
    private WDesktopProperties wprops;
    static ColorModel screenmodel;
    private static ExecutorService displayChangeExecutor;
    private static final String prefix = "DnD.Cursor.";
    private static final String postfix = ".32x32";
    private static final String awtPrefix = "awt.";
    private static final String dndPrefix = "DnD.";
    private static final WeakReference<Component> NULL_COMPONENT_WR;
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WToolkit");
    private static boolean areExtraMouseButtonsEnabled = true;
    private static boolean loaded = false;
    protected boolean dynamicLayoutSetting = false;
    private final Object anchor = new Object();
    private boolean inited = false;
    private volatile WeakReference<Component> compOnTouchDownEvent = NULL_COMPONENT_WR;
    private volatile WeakReference<Component> compOnMousePressedEvent = NULL_COMPONENT_WR;

    private static native void initIDs();

    private static native String getWindowsVersion();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void disableCustomPalette();

    public static native boolean embeddedInit();

    public static native boolean embeddedDispose();

    public native void embeddedEventLoopIdleProcessing();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void postDispose();

    private static native boolean startToolkitThread(Runnable runnable, ThreadGroup threadGroup);

    private native boolean init();

    private native void eventLoop();

    private native void shutdown();

    static native void startSecondaryEventLoop();

    static native void quitSecondaryEventLoop();

    private native void setDynamicLayoutNative(boolean z2);

    private native boolean isDynamicLayoutSupportedNative();

    static native ColorModel makeColorModel();

    @Override // sun.awt.SunToolkit
    protected native int getScreenWidth();

    @Override // sun.awt.SunToolkit
    protected native int getScreenHeight();

    private native Insets getScreenInsets(int i2);

    private native void nativeSync();

    @Override // java.awt.Toolkit
    public native void beep();

    private native boolean getLockingKeyStateNative(int i2);

    private native void setLockingKeyStateNative(int i2, boolean z2);

    @Override // java.awt.Toolkit
    protected native void loadSystemColors(int[] iArr);

    @Override // java.awt.Toolkit
    public native int getMaximumCursorColors();

    private native void showTouchKeyboard(boolean z2);

    private native void hideTouchKeyboard();

    @Override // sun.awt.SunToolkit
    public native boolean syncNativeQueue(long j2);

    private static native void setExtraMouseButtonsEnabledNative(boolean z2);

    private native synchronized int getNumberOfButtonsImpl();

    static {
        loadLibraries();
        initIDs();
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("Win version: " + getWindowsVersion());
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.windows.WToolkit.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                String property = System.getProperty("browser");
                if (property != null && property.equals("sun.plugin")) {
                    WToolkit.disableCustomPalette();
                    return null;
                }
                return null;
            }
        });
        NULL_COMPONENT_WR = new WeakReference<>(null);
    }

    public static void loadLibraries() {
        if (!loaded) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.windows.WToolkit.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    System.loadLibrary("awt");
                    return null;
                }
            });
            loaded = true;
        }
    }

    public static void resetGC() {
        if (GraphicsEnvironment.isHeadless()) {
            config = null;
        } else {
            config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WToolkit$ToolkitDisposer.class */
    static class ToolkitDisposer implements DisposerRecord {
        ToolkitDisposer() {
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            WToolkit.postDispose();
        }
    }

    public WToolkit() {
        if (PerformanceLogger.loggingEnabled()) {
            PerformanceLogger.setTime("WToolkit construction");
        }
        Disposer.addRecord(this.anchor, new ToolkitDisposer());
        AWTAutoShutdown.notifyToolkitThreadBusy();
        ThreadGroup threadGroup = (ThreadGroup) AccessController.doPrivileged(ThreadGroupUtils::getRootThreadGroup);
        if (!startToolkitThread(this, threadGroup)) {
            Thread thread = new Thread(threadGroup, this, "AWT-Windows");
            thread.setDaemon(true);
            thread.start();
        }
        try {
            synchronized (this) {
                while (!this.inited) {
                    wait();
                }
            }
        } catch (InterruptedException e2) {
        }
        setDynamicLayout(true);
        areExtraMouseButtonsEnabled = Boolean.parseBoolean(System.getProperty("sun.awt.enableExtraMouseButtons", "true"));
        System.setProperty("sun.awt.enableExtraMouseButtons", "" + areExtraMouseButtonsEnabled);
        setExtraMouseButtonsEnabledNative(areExtraMouseButtonsEnabled);
    }

    private final void registerShutdownHook() {
        AccessController.doPrivileged(() -> {
            Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), this::shutdown);
            thread.setContextClassLoader(null);
            Runtime.getRuntime().addShutdownHook(thread);
            return null;
        });
    }

    @Override // java.lang.Runnable
    public void run() {
        AccessController.doPrivileged(() -> {
            Thread.currentThread().setContextClassLoader(null);
            return null;
        });
        Thread.currentThread().setPriority(6);
        boolean zInit = init();
        if (zInit) {
            registerShutdownHook();
        }
        synchronized (this) {
            this.inited = true;
            notifyAll();
        }
        if (zInit) {
            eventLoop();
        }
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public ButtonPeer createButton(Button button) {
        WButtonPeer wButtonPeer = new WButtonPeer(button);
        targetCreatedPeer(button, wButtonPeer);
        return wButtonPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public TextFieldPeer createTextField(TextField textField) {
        WTextFieldPeer wTextFieldPeer = new WTextFieldPeer(textField);
        targetCreatedPeer(textField, wTextFieldPeer);
        return wTextFieldPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public LabelPeer createLabel(Label label) {
        WLabelPeer wLabelPeer = new WLabelPeer(label);
        targetCreatedPeer(label, wLabelPeer);
        return wLabelPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public ListPeer createList(List list) {
        WListPeer wListPeer = new WListPeer(list);
        targetCreatedPeer(list, wListPeer);
        return wListPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public CheckboxPeer createCheckbox(Checkbox checkbox) {
        WCheckboxPeer wCheckboxPeer = new WCheckboxPeer(checkbox);
        targetCreatedPeer(checkbox, wCheckboxPeer);
        return wCheckboxPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public ScrollbarPeer createScrollbar(Scrollbar scrollbar) {
        WScrollbarPeer wScrollbarPeer = new WScrollbarPeer(scrollbar);
        targetCreatedPeer(scrollbar, wScrollbarPeer);
        return wScrollbarPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public ScrollPanePeer createScrollPane(ScrollPane scrollPane) {
        WScrollPanePeer wScrollPanePeer = new WScrollPanePeer(scrollPane);
        targetCreatedPeer(scrollPane, wScrollPanePeer);
        return wScrollPanePeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public TextAreaPeer createTextArea(TextArea textArea) {
        WTextAreaPeer wTextAreaPeer = new WTextAreaPeer(textArea);
        targetCreatedPeer(textArea, wTextAreaPeer);
        return wTextAreaPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public ChoicePeer createChoice(Choice choice) {
        WChoicePeer wChoicePeer = new WChoicePeer(choice);
        targetCreatedPeer(choice, wChoicePeer);
        return wChoicePeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public FramePeer createFrame(Frame frame) {
        WFramePeer wFramePeer = new WFramePeer(frame);
        targetCreatedPeer(frame, wFramePeer);
        return wFramePeer;
    }

    @Override // sun.awt.SunToolkit
    public FramePeer createLightweightFrame(LightweightFrame lightweightFrame) {
        WLightweightFramePeer wLightweightFramePeer = new WLightweightFramePeer(lightweightFrame);
        targetCreatedPeer(lightweightFrame, wLightweightFramePeer);
        return wLightweightFramePeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public CanvasPeer createCanvas(Canvas canvas) {
        WCanvasPeer wCanvasPeer = new WCanvasPeer(canvas);
        targetCreatedPeer(canvas, wCanvasPeer);
        return wCanvasPeer;
    }

    @Override // sun.awt.SunToolkit
    public void disableBackgroundErase(Canvas canvas) {
        WCanvasPeer wCanvasPeer = (WCanvasPeer) canvas.getPeer();
        if (wCanvasPeer == null) {
            throw new IllegalStateException("Canvas must have a valid peer");
        }
        wCanvasPeer.disableBackgroundErase();
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public PanelPeer createPanel(Panel panel) {
        WPanelPeer wPanelPeer = new WPanelPeer(panel);
        targetCreatedPeer(panel, wPanelPeer);
        return wPanelPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public WindowPeer createWindow(Window window) {
        WWindowPeer wWindowPeer = new WWindowPeer(window);
        targetCreatedPeer(window, wWindowPeer);
        return wWindowPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public DialogPeer createDialog(Dialog dialog) {
        WDialogPeer wDialogPeer = new WDialogPeer(dialog);
        targetCreatedPeer(dialog, wDialogPeer);
        return wDialogPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public FileDialogPeer createFileDialog(FileDialog fileDialog) {
        WFileDialogPeer wFileDialogPeer = new WFileDialogPeer(fileDialog);
        targetCreatedPeer(fileDialog, wFileDialogPeer);
        return wFileDialogPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuBarPeer createMenuBar(MenuBar menuBar) {
        WMenuBarPeer wMenuBarPeer = new WMenuBarPeer(menuBar);
        targetCreatedPeer(menuBar, wMenuBarPeer);
        return wMenuBarPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuPeer createMenu(Menu menu) {
        WMenuPeer wMenuPeer = new WMenuPeer(menu);
        targetCreatedPeer(menu, wMenuPeer);
        return wMenuPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public PopupMenuPeer createPopupMenu(PopupMenu popupMenu) {
        WPopupMenuPeer wPopupMenuPeer = new WPopupMenuPeer(popupMenu);
        targetCreatedPeer(popupMenu, wPopupMenuPeer);
        return wPopupMenuPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public MenuItemPeer createMenuItem(MenuItem menuItem) {
        WMenuItemPeer wMenuItemPeer = new WMenuItemPeer(menuItem);
        targetCreatedPeer(menuItem, wMenuItemPeer);
        return wMenuItemPeer;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) {
        WCheckboxMenuItemPeer wCheckboxMenuItemPeer = new WCheckboxMenuItemPeer(checkboxMenuItem);
        targetCreatedPeer(checkboxMenuItem, wCheckboxMenuItemPeer);
        return wCheckboxMenuItemPeer;
    }

    @Override // sun.awt.SunToolkit, sun.awt.ComponentFactory
    public RobotPeer createRobot(Robot robot, GraphicsDevice graphicsDevice) {
        return new WRobotPeer(graphicsDevice);
    }

    public WEmbeddedFramePeer createEmbeddedFrame(WEmbeddedFrame wEmbeddedFrame) {
        WEmbeddedFramePeer wEmbeddedFramePeer = new WEmbeddedFramePeer(wEmbeddedFrame);
        targetCreatedPeer(wEmbeddedFrame, wEmbeddedFramePeer);
        return wEmbeddedFramePeer;
    }

    WPrintDialogPeer createWPrintDialog(WPrintDialog wPrintDialog) {
        WPrintDialogPeer wPrintDialogPeer = new WPrintDialogPeer(wPrintDialog);
        targetCreatedPeer(wPrintDialog, wPrintDialogPeer);
        return wPrintDialogPeer;
    }

    WPageDialogPeer createWPageDialog(WPageDialog wPageDialog) {
        WPageDialogPeer wPageDialogPeer = new WPageDialogPeer(wPageDialog);
        targetCreatedPeer(wPageDialog, wPageDialogPeer);
        return wPageDialogPeer;
    }

    @Override // sun.awt.SunToolkit
    public TrayIconPeer createTrayIcon(TrayIcon trayIcon) {
        WTrayIconPeer wTrayIconPeer = new WTrayIconPeer(trayIcon);
        targetCreatedPeer(trayIcon, wTrayIconPeer);
        return wTrayIconPeer;
    }

    @Override // sun.awt.SunToolkit
    public SystemTrayPeer createSystemTray(SystemTray systemTray) {
        return new WSystemTrayPeer(systemTray);
    }

    @Override // sun.awt.SunToolkit
    public boolean isTraySupported() {
        return true;
    }

    @Override // sun.awt.ComponentFactory
    public DataTransferer getDataTransferer() {
        return WDataTransferer.getInstanceImpl();
    }

    @Override // sun.awt.SunToolkit, sun.awt.KeyboardFocusManagerPeerProvider
    public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException {
        return WKeyboardFocusManagerPeer.getInstance();
    }

    @Override // java.awt.Toolkit
    public void setDynamicLayout(boolean z2) {
        if (z2 == this.dynamicLayoutSetting) {
            return;
        }
        this.dynamicLayoutSetting = z2;
        setDynamicLayoutNative(z2);
    }

    @Override // java.awt.Toolkit
    protected boolean isDynamicLayoutSet() {
        return this.dynamicLayoutSetting;
    }

    @Override // java.awt.Toolkit
    public boolean isDynamicLayoutActive() {
        return isDynamicLayoutSet() && isDynamicLayoutSupported();
    }

    @Override // java.awt.Toolkit
    public boolean isFrameStateSupported(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 6:
                return true;
            default:
                return false;
        }
    }

    static ColorModel getStaticColorModel() {
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalArgumentException();
        }
        if (config == null) {
            resetGC();
        }
        return config.getColorModel();
    }

    @Override // java.awt.Toolkit
    public ColorModel getColorModel() {
        return getStaticColorModel();
    }

    @Override // java.awt.Toolkit
    public Insets getScreenInsets(GraphicsConfiguration graphicsConfiguration) {
        return getScreenInsets(((Win32GraphicsDevice) graphicsConfiguration.getDevice()).getScreen());
    }

    @Override // java.awt.Toolkit
    public int getScreenResolution() {
        return ((Win32GraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment()).getXResolution();
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit
    public FontMetrics getFontMetrics(Font font) {
        FontManager fontManagerFactory = FontManagerFactory.getInstance();
        if ((fontManagerFactory instanceof SunFontManager) && ((SunFontManager) fontManagerFactory).usePlatformFontMetrics()) {
            return WFontMetrics.getFontMetrics(font);
        }
        return super.getFontMetrics(font);
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public FontPeer getFontPeer(String str, int i2) {
        FontPeer fontPeer;
        String lowerCase = str.toLowerCase();
        if (null != this.cacheFontPeer && null != (fontPeer = this.cacheFontPeer.get(lowerCase + i2))) {
            return fontPeer;
        }
        WFontPeer wFontPeer = new WFontPeer(str, i2);
        if (wFontPeer != null) {
            if (null == this.cacheFontPeer) {
                this.cacheFontPeer = new Hashtable<>(5, 0.9f);
            }
            if (null != this.cacheFontPeer) {
                this.cacheFontPeer.put(lowerCase + i2, wFontPeer);
            }
        }
        return wFontPeer;
    }

    @Override // java.awt.Toolkit
    public void sync() {
        nativeSync();
        OGLRenderQueue.sync();
        D3DRenderQueue.sync();
    }

    @Override // java.awt.Toolkit
    public PrintJob getPrintJob(Frame frame, String str, Properties properties) {
        return getPrintJob(frame, str, null, null);
    }

    @Override // java.awt.Toolkit
    public PrintJob getPrintJob(Frame frame, String str, JobAttributes jobAttributes, PageAttributes pageAttributes) {
        if (frame == null) {
            throw new NullPointerException("frame must not be null");
        }
        PrintJob2D printJob2D = new PrintJob2D(frame, str, jobAttributes, pageAttributes);
        if (!printJob2D.printDialog()) {
            printJob2D = null;
        }
        return printJob2D;
    }

    @Override // java.awt.Toolkit
    public boolean getLockingKeyState(int i2) {
        if (i2 != 20 && i2 != 144 && i2 != 145 && i2 != 262) {
            throw new IllegalArgumentException("invalid key for Toolkit.getLockingKeyState");
        }
        return getLockingKeyStateNative(i2);
    }

    @Override // java.awt.Toolkit
    public void setLockingKeyState(int i2, boolean z2) {
        if (i2 != 20 && i2 != 144 && i2 != 145 && i2 != 262) {
            throw new IllegalArgumentException("invalid key for Toolkit.setLockingKeyState");
        }
        setLockingKeyStateNative(i2, z2);
    }

    @Override // java.awt.Toolkit
    public Clipboard getSystemClipboard() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
        }
        synchronized (this) {
            if (this.clipboard == null) {
                this.clipboard = new WClipboard();
            }
        }
        return this.clipboard;
    }

    public static final Object targetToPeer(Object obj) {
        return SunToolkit.targetToPeer(obj);
    }

    public static final void targetDisposedPeer(Object obj, Object obj2) {
        SunToolkit.targetDisposedPeer(obj, obj2);
    }

    @Override // sun.awt.InputMethodSupport
    public InputMethodDescriptor getInputMethodAdapterDescriptor() {
        return new WInputMethodDescriptor();
    }

    @Override // java.awt.Toolkit
    public Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight inputMethodHighlight) {
        return WInputMethod.mapInputMethodHighlight(inputMethodHighlight);
    }

    @Override // sun.awt.SunToolkit, sun.awt.InputMethodSupport
    public boolean enableInputMethodsForTextComponent() {
        return true;
    }

    @Override // sun.awt.SunToolkit, sun.awt.InputMethodSupport
    public Locale getDefaultKeyboardLocale() {
        Locale nativeLocale = WInputMethod.getNativeLocale();
        if (nativeLocale == null) {
            return super.getDefaultKeyboardLocale();
        }
        return nativeLocale;
    }

    @Override // java.awt.Toolkit
    public Cursor createCustomCursor(Image image, Point point, String str) throws IndexOutOfBoundsException {
        return new WCustomCursor(image, point, str);
    }

    @Override // java.awt.Toolkit
    public Dimension getBestCursorSize(int i2, int i3) {
        return new Dimension(WCustomCursor.getCursorWidth(), WCustomCursor.getCursorHeight());
    }

    static void paletteChanged() {
        ((Win32GraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment()).paletteChanged();
    }

    public static void displayChanged() {
        Runnable runnable = () -> {
            Object localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            if (localGraphicsEnvironment instanceof DisplayChangedListener) {
                ((DisplayChangedListener) localGraphicsEnvironment).displayChanged();
            }
        };
        if (AppContext.getAppContext() != null) {
            EventQueue.invokeLater(runnable);
            return;
        }
        if (displayChangeExecutor == null) {
            displayChangeExecutor = Executors.newFixedThreadPool(1, runnable2 -> {
                Thread threadNewThread = Executors.defaultThreadFactory().newThread(runnable2);
                threadNewThread.setDaemon(true);
                return threadNewThread;
            });
        }
        displayChangeExecutor.submit(runnable);
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit, sun.awt.ComponentFactory
    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        LightweightFrame lightweightFrame = SunToolkit.getLightweightFrame(dragGestureEvent.getComponent());
        if (lightweightFrame != null) {
            return lightweightFrame.createDragSourceContextPeer(dragGestureEvent);
        }
        return WDragSourceContextPeer.createDragSourceContextPeer(dragGestureEvent);
    }

    @Override // java.awt.Toolkit
    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        LightweightFrame lightweightFrame = SunToolkit.getLightweightFrame(component);
        if (lightweightFrame != null) {
            return (T) lightweightFrame.createDragGestureRecognizer(cls, dragSource, component, i2, dragGestureListener);
        }
        if (MouseDragGestureRecognizer.class.equals(cls)) {
            return new WMouseDragGestureRecognizer(dragSource, component, i2, dragGestureListener);
        }
        return null;
    }

    @Override // java.awt.Toolkit
    protected Object lazilyLoadDesktopProperty(String str) {
        Object obj;
        if (str.startsWith(prefix)) {
            String str2 = str.substring(prefix.length(), str.length()) + postfix;
            try {
                return Cursor.getSystemCustomCursor(str2);
            } catch (AWTException e2) {
                throw new RuntimeException("cannot load system cursor: " + str2, e2);
            }
        }
        if (str.equals("awt.dynamicLayoutSupported")) {
            return Boolean.valueOf(isDynamicLayoutSupported());
        }
        if (WDesktopProperties.isWindowsProperty(str) || str.startsWith(awtPrefix) || str.startsWith(dndPrefix)) {
            synchronized (this) {
                lazilyInitWProps();
                obj = this.desktopProperties.get(str);
            }
            return obj;
        }
        return super.lazilyLoadDesktopProperty(str);
    }

    private synchronized void lazilyInitWProps() {
        if (this.wprops == null) {
            this.wprops = new WDesktopProperties(this);
            updateProperties(this.wprops.getProperties());
        }
    }

    private synchronized boolean isDynamicLayoutSupported() {
        boolean zIsDynamicLayoutSupportedNative = isDynamicLayoutSupportedNative();
        lazilyInitWProps();
        Boolean bool = (Boolean) this.desktopProperties.get("awt.dynamicLayoutSupported");
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("In WTK.isDynamicLayoutSupported()   nativeDynamic == " + zIsDynamicLayoutSupportedNative + "   wprops.dynamic == " + ((Object) bool));
        }
        if (bool == null || zIsDynamicLayoutSupportedNative != bool.booleanValue()) {
            windowsSettingChange();
            return zIsDynamicLayoutSupportedNative;
        }
        return bool.booleanValue();
    }

    private void windowsSettingChange() {
        Map<String, Object> wProps = getWProps();
        if (wProps == null) {
            return;
        }
        updateXPStyleEnabled(wProps.get(XPSTYLE_THEME_ACTIVE));
        if (AppContext.getAppContext() == null) {
            updateProperties(wProps);
        } else {
            EventQueue.invokeLater(() -> {
                updateProperties(wProps);
            });
        }
    }

    private synchronized void updateProperties(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        updateXPStyleEnabled(map.get(XPSTYLE_THEME_ACTIVE));
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("changed " + str + " to " + obj);
            }
            setDesktopProperty(str, obj);
        }
    }

    private synchronized Map<String, Object> getWProps() {
        if (this.wprops != null) {
            return this.wprops.getProperties();
        }
        return null;
    }

    private void updateXPStyleEnabled(Object obj) {
        ThemeReader.xpStyleEnabled = Boolean.TRUE.equals(obj);
    }

    @Override // java.awt.Toolkit
    public synchronized void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (str == null) {
            return;
        }
        if (WDesktopProperties.isWindowsProperty(str) || str.startsWith(awtPrefix) || str.startsWith(dndPrefix)) {
            lazilyInitWProps();
        }
        super.addPropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.awt.Toolkit
    protected synchronized void initializeDesktopProperties() {
        this.desktopProperties.put("DnD.Autoscroll.initialDelay", 50);
        this.desktopProperties.put("DnD.Autoscroll.interval", 50);
        this.desktopProperties.put("DnD.isDragImageSupported", Boolean.TRUE);
        this.desktopProperties.put("Shell.shellFolderManager", "sun.awt.shell.Win32ShellFolderManager2");
    }

    @Override // sun.awt.SunToolkit
    protected synchronized RenderingHints getDesktopAAHints() {
        if (this.wprops == null) {
            return null;
        }
        return this.wprops.getDesktopAAHints();
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit
    public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
        return modalityType == null || modalityType == Dialog.ModalityType.MODELESS || modalityType == Dialog.ModalityType.DOCUMENT_MODAL || modalityType == Dialog.ModalityType.APPLICATION_MODAL || modalityType == Dialog.ModalityType.TOOLKIT_MODAL;
    }

    @Override // sun.awt.SunToolkit, java.awt.Toolkit
    public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType) {
        return modalExclusionType == null || modalExclusionType == Dialog.ModalExclusionType.NO_EXCLUDE || modalExclusionType == Dialog.ModalExclusionType.APPLICATION_EXCLUDE || modalExclusionType == Dialog.ModalExclusionType.TOOLKIT_EXCLUDE;
    }

    public static WToolkit getWToolkit() {
        return (WToolkit) Toolkit.getDefaultToolkit();
    }

    @Override // sun.awt.SunToolkit
    public boolean useBufferPerWindow() {
        return !Win32GraphicsEnvironment.isDWMCompositionEnabled();
    }

    @Override // sun.awt.SunToolkit
    public void grab(Window window) {
        if (window.getPeer() != null) {
            ((WWindowPeer) window.getPeer()).grab();
        }
    }

    @Override // sun.awt.SunToolkit
    public void ungrab(Window window) {
        if (window.getPeer() != null) {
            ((WWindowPeer) window.getPeer()).ungrab();
        }
    }

    private boolean isComponentValidForTouchKeyboard(Component component) {
        if (component != null && component.isEnabled() && component.isFocusable()) {
            if (!(component instanceof TextComponent) || !((TextComponent) component).isEditable()) {
                if ((component instanceof JTextComponent) && ((JTextComponent) component).isEditable()) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    @Override // sun.awt.SunToolkit
    public void showOrHideTouchKeyboard(Component component, AWTEvent aWTEvent) {
        if (!(component instanceof TextComponent) && !(component instanceof JTextComponent)) {
            return;
        }
        if (!(aWTEvent instanceof MouseEvent) || !isComponentValidForTouchKeyboard(component)) {
            if (aWTEvent instanceof FocusEvent) {
                FocusEvent focusEvent = (FocusEvent) aWTEvent;
                if (focusEvent.getID() == 1005 && !isComponentValidForTouchKeyboard(focusEvent.getOppositeComponent())) {
                    hideTouchKeyboard();
                    return;
                }
                return;
            }
            return;
        }
        MouseEvent mouseEvent = (MouseEvent) aWTEvent;
        if (mouseEvent.getID() == 501) {
            if (AWTAccessor.getMouseEventAccessor().isCausedByTouchEvent(mouseEvent)) {
                this.compOnTouchDownEvent = new WeakReference<>(component);
                return;
            } else {
                this.compOnMousePressedEvent = new WeakReference<>(component);
                return;
            }
        }
        if (mouseEvent.getID() == 502) {
            if (AWTAccessor.getMouseEventAccessor().isCausedByTouchEvent(mouseEvent)) {
                if (this.compOnTouchDownEvent.get() == component) {
                    showTouchKeyboard(true);
                }
                this.compOnTouchDownEvent = NULL_COMPONENT_WR;
            } else {
                if (this.compOnMousePressedEvent.get() == component) {
                    showTouchKeyboard(false);
                }
                this.compOnMousePressedEvent = NULL_COMPONENT_WR;
            }
        }
    }

    @Override // sun.awt.SunToolkit
    public boolean isDesktopSupported() {
        return true;
    }

    @Override // java.awt.Toolkit
    public DesktopPeer createDesktopPeer(Desktop desktop) {
        return new WDesktopPeer();
    }

    @Override // java.awt.Toolkit
    public boolean areExtraMouseButtonsEnabled() throws HeadlessException {
        return areExtraMouseButtonsEnabled;
    }

    @Override // sun.awt.SunToolkit
    public int getNumberOfButtons() {
        if (numberOfButtons == 0) {
            numberOfButtons = getNumberOfButtonsImpl();
        }
        if (numberOfButtons > 20) {
            return 20;
        }
        return numberOfButtons;
    }

    @Override // sun.awt.SunToolkit
    public boolean isWindowOpacitySupported() {
        return true;
    }

    @Override // sun.awt.SunToolkit
    public boolean isWindowShapingSupported() {
        return true;
    }

    @Override // sun.awt.SunToolkit
    public boolean isWindowTranslucencySupported() {
        return true;
    }

    @Override // sun.awt.SunToolkit
    public boolean isTranslucencyCapable(GraphicsConfiguration graphicsConfiguration) {
        return true;
    }

    @Override // sun.awt.SunToolkit
    public boolean needUpdateWindow() {
        return true;
    }
}
