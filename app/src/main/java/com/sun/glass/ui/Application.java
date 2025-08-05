package com.sun.glass.ui;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.lang.Thread;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Application.class */
public abstract class Application {
    private static final String DEFAULT_NAME = "java";
    private EventHandler eventHandler;
    private static Application application;
    private static Thread eventThread;
    private static boolean loaded = false;
    private static final boolean disableThreadChecks = ((Boolean) AccessController.doPrivileged(() -> {
        String str = System.getProperty("glass.disableThreadChecks", "false");
        return Boolean.valueOf("true".equalsIgnoreCase(str));
    })).booleanValue();
    private static volatile Map deviceDetails = null;
    private static int nestedEventLoopCounter = 0;
    protected String name = DEFAULT_NAME;
    private boolean initialActiveEventReceived = false;
    private String[] initialOpenedFiles = null;
    private boolean terminateWhenLastWindowClosed = true;

    protected abstract void runLoop(Runnable runnable);

    protected abstract void _invokeAndWait(Runnable runnable);

    protected abstract void _invokeLater(Runnable runnable);

    protected abstract Object _enterNestedEventLoop();

    protected abstract void _leaveNestedEventLoop(Object obj);

    public abstract Window createWindow(Window window, Screen screen, int i2);

    public abstract Window createWindow(long j2);

    public abstract View createView();

    public abstract Cursor createCursor(int i2);

    public abstract Cursor createCursor(int i2, int i3, Pixels pixels);

    protected abstract void staticCursor_setVisible(boolean z2);

    protected abstract Size staticCursor_getBestSize(int i2, int i3);

    public abstract Pixels createPixels(int i2, int i3, ByteBuffer byteBuffer);

    public abstract Pixels createPixels(int i2, int i3, IntBuffer intBuffer);

    public abstract Pixels createPixels(int i2, int i3, IntBuffer intBuffer, float f2);

    protected abstract int staticPixels_getNativeFormat();

    public abstract Robot createRobot();

    protected abstract double staticScreen_getVideoRefreshPeriod();

    protected abstract Screen[] staticScreen_getScreens();

    public abstract Timer createTimer(Runnable runnable);

    protected abstract int staticTimer_getMinPeriod();

    protected abstract int staticTimer_getMaxPeriod();

    protected abstract CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window window, String str, String str2, String str3, int i2, boolean z2, CommonDialogs.ExtensionFilter[] extensionFilterArr, int i3);

    protected abstract File staticCommonDialogs_showFolderChooser(Window window, String str, String str2);

    protected abstract long staticView_getMultiClickTime();

    protected abstract int staticView_getMultiClickMaxX();

    protected abstract int staticView_getMultiClickMaxY();

    protected abstract boolean _supportsTransparentWindows();

    protected abstract boolean _supportsUnifiedWindows();

    protected abstract int _getKeyCodeForChar(char c2);

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Application$EventHandler.class */
    public static class EventHandler {
        public void handleWillFinishLaunchingAction(Application app, long time) {
        }

        public void handleDidFinishLaunchingAction(Application app, long time) {
        }

        public void handleWillBecomeActiveAction(Application app, long time) {
        }

        public void handleDidBecomeActiveAction(Application app, long time) {
        }

        public void handleWillResignActiveAction(Application app, long time) {
        }

        public void handleDidResignActiveAction(Application app, long time) {
        }

        public void handleDidReceiveMemoryWarning(Application app, long time) {
        }

        public void handleWillHideAction(Application app, long time) {
        }

        public void handleDidHideAction(Application app, long time) {
        }

        public void handleWillUnhideAction(Application app, long time) {
        }

        public void handleDidUnhideAction(Application app, long time) {
        }

        public void handleOpenFilesAction(Application app, long time, String[] files) {
        }

        public void handleQuitAction(Application app, long time) {
        }

        public boolean handleThemeChanged(String themeName) {
            return false;
        }
    }

    protected static synchronized void loadNativeLibrary(String libname) {
        if (!loaded) {
            NativeLibLoader.loadLibrary(libname);
            loaded = true;
        }
    }

    protected static synchronized void loadNativeLibrary() {
        loadNativeLibrary("glass");
    }

    public static void setDeviceDetails(Map details) {
        deviceDetails = details;
    }

    public static Map getDeviceDetails() {
        return deviceDetails;
    }

    protected Application() {
    }

    public static void run(Runnable launchable) {
        if (application != null) {
            throw new IllegalStateException("Application is already running");
        }
        application = PlatformFactory.getPlatformFactory().createApplication();
        try {
            application.runLoop(() -> {
                Screen.initScreens();
                launchable.run();
            });
        } catch (Throwable t2) {
            t2.printStackTrace();
        }
    }

    protected void finishTerminating() {
        application = null;
    }

    public String getName() {
        checkEventThread();
        return this.name;
    }

    public void setName(String name) {
        checkEventThread();
        if (name != null && DEFAULT_NAME.equals(this.name)) {
            this.name = name;
        }
    }

    public String getDataDirectory() {
        checkEventThread();
        String userHome = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("user.home");
        });
        return userHome + File.separator + "." + this.name + File.separator;
    }

    protected void notifyWillFinishLaunching() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleWillFinishLaunchingAction(this, System.nanoTime());
        }
    }

    protected void notifyDidFinishLaunching() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidFinishLaunchingAction(this, System.nanoTime());
        }
    }

    protected void notifyWillBecomeActive() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleWillBecomeActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyDidBecomeActive() {
        this.initialActiveEventReceived = true;
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidBecomeActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyWillResignActive() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleWillResignActiveAction(this, System.nanoTime());
        }
    }

    protected boolean notifyThemeChanged(String themeName) {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            return handler.handleThemeChanged(themeName);
        }
        return false;
    }

    protected void notifyDidResignActive() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidResignActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyDidReceiveMemoryWarning() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidReceiveMemoryWarning(this, System.nanoTime());
        }
    }

    protected void notifyWillHide() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleWillHideAction(this, System.nanoTime());
        }
    }

    protected void notifyDidHide() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidHideAction(this, System.nanoTime());
        }
    }

    protected void notifyWillUnhide() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleWillUnhideAction(this, System.nanoTime());
        }
    }

    protected void notifyDidUnhide() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleDidUnhideAction(this, System.nanoTime());
        }
    }

    protected void notifyOpenFiles(String[] files) {
        if (!this.initialActiveEventReceived && this.initialOpenedFiles == null) {
            this.initialOpenedFiles = files;
        }
        EventHandler handler = getEventHandler();
        if (handler != null && files != null) {
            handler.handleOpenFilesAction(this, System.nanoTime(), files);
        }
    }

    protected void notifyWillQuit() {
        EventHandler handler = getEventHandler();
        if (handler != null) {
            handler.handleQuitAction(this, System.nanoTime());
        }
    }

    public void installDefaultMenus(MenuBar menubar) {
        checkEventThread();
    }

    public EventHandler getEventHandler() {
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        checkEventThread();
        boolean resendOpenFiles = (this.eventHandler == null || this.initialOpenedFiles == null) ? false : true;
        this.eventHandler = eventHandler;
        if (resendOpenFiles) {
            notifyOpenFiles(this.initialOpenedFiles);
        }
    }

    public final boolean shouldTerminateWhenLastWindowClosed() {
        checkEventThread();
        return this.terminateWhenLastWindowClosed;
    }

    public final void setTerminateWhenLastWindowClosed(boolean b2) {
        checkEventThread();
        this.terminateWhenLastWindowClosed = b2;
    }

    public boolean shouldUpdateWindow() {
        checkEventThread();
        return false;
    }

    public boolean hasWindowManager() {
        return true;
    }

    public void notifyRenderingFinished() {
    }

    public void terminate() {
        checkEventThread();
        try {
            try {
                List<Window> windows = new LinkedList<>(Window.getWindows());
                for (Window window : windows) {
                    window.setVisible(false);
                }
                for (Window window2 : windows) {
                    window2.close();
                }
                finishTerminating();
            } catch (Throwable t2) {
                t2.printStackTrace();
                finishTerminating();
            }
        } catch (Throwable th) {
            finishTerminating();
            throw th;
        }
    }

    public static Application GetApplication() {
        return application;
    }

    protected static void setEventThread(Thread thread) {
        eventThread = thread;
    }

    protected static Thread getEventThread() {
        return eventThread;
    }

    public static boolean isEventThread() {
        return Thread.currentThread() == eventThread;
    }

    public static void checkEventThread() {
        if (!disableThreadChecks && Thread.currentThread() != eventThread) {
            throw new IllegalStateException("This operation is permitted on the event thread only; currentThread = " + Thread.currentThread().getName());
        }
    }

    public static void reportException(Throwable t2) {
        Thread currentThread = Thread.currentThread();
        Thread.UncaughtExceptionHandler handler = currentThread.getUncaughtExceptionHandler();
        handler.uncaughtException(currentThread, t2);
    }

    public static void invokeAndWait(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (isEventThread()) {
            runnable.run();
        } else {
            GetApplication()._invokeAndWait(runnable);
        }
    }

    public static void invokeLater(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        GetApplication()._invokeLater(runnable);
    }

    static Object enterNestedEventLoop() {
        checkEventThread();
        nestedEventLoopCounter++;
        try {
            Object obj_enterNestedEventLoop = GetApplication()._enterNestedEventLoop();
            nestedEventLoopCounter--;
            return obj_enterNestedEventLoop;
        } catch (Throwable th) {
            nestedEventLoopCounter--;
            throw th;
        }
    }

    static void leaveNestedEventLoop(Object retValue) {
        checkEventThread();
        if (nestedEventLoopCounter == 0) {
            throw new IllegalStateException("Not in a nested event loop");
        }
        GetApplication()._leaveNestedEventLoop(retValue);
    }

    public static boolean isNestedLoopRunning() {
        checkEventThread();
        return nestedEventLoopCounter > 0;
    }

    public void menuAboutAction() {
        System.err.println("about");
    }

    public final Window createWindow(Screen screen, int styleMask) {
        return createWindow(null, screen, styleMask);
    }

    public final Menu createMenu(String title) {
        return new Menu(title);
    }

    public final Menu createMenu(String title, boolean enabled) {
        return new Menu(title, enabled);
    }

    public final MenuBar createMenuBar() {
        return new MenuBar();
    }

    public final MenuItem createMenuItem(String title) {
        return createMenuItem(title, null);
    }

    public final MenuItem createMenuItem(String title, MenuItem.Callback callback) {
        return createMenuItem(title, callback, 0, 0);
    }

    public final MenuItem createMenuItem(String title, MenuItem.Callback callback, int shortcutKey, int shortcutModifiers) {
        return createMenuItem(title, callback, shortcutKey, shortcutModifiers, null);
    }

    public final MenuItem createMenuItem(String title, MenuItem.Callback callback, int shortcutKey, int shortcutModifiers, Pixels pixels) {
        return new MenuItem(title, callback, shortcutKey, shortcutModifiers, pixels);
    }

    static Pixels createPixels(int width, int height, int[] data, float scale) {
        return GetApplication().createPixels(width, height, IntBuffer.wrap(data), scale);
    }

    static float getScaleFactor(int x2, int y2, int w2, int h2) {
        float scale = 0.0f;
        for (Screen s2 : Screen.getScreens()) {
            int sx = s2.getX();
            int sy = s2.getY();
            int sw = s2.getWidth();
            int sh = s2.getHeight();
            if (x2 < sx + sw && x2 + w2 > sx && y2 < sy + sh && y2 + h2 > sy && scale < s2.getRenderScale()) {
                scale = s2.getRenderScale();
            }
        }
        if (scale == 0.0f) {
            return 1.0f;
        }
        return scale;
    }

    public final EventLoop createEventLoop() {
        return new EventLoop();
    }

    public Accessible createAccessible() {
        return null;
    }

    public String getHighContrastTheme() {
        checkEventThread();
        return null;
    }

    protected boolean _supportsInputMethods() {
        return false;
    }

    public final boolean supportsInputMethods() {
        checkEventThread();
        return _supportsInputMethods();
    }

    public final boolean supportsTransparentWindows() {
        checkEventThread();
        return _supportsTransparentWindows();
    }

    public boolean hasTwoLevelFocus() {
        return false;
    }

    public boolean hasVirtualKeyboard() {
        return false;
    }

    public boolean hasTouch() {
        return false;
    }

    public boolean hasMultiTouch() {
        return false;
    }

    public boolean hasPointer() {
        return true;
    }

    public final boolean supportsUnifiedWindows() {
        checkEventThread();
        return _supportsUnifiedWindows();
    }

    protected boolean _supportsSystemMenu() {
        return false;
    }

    public final boolean supportsSystemMenu() {
        checkEventThread();
        return _supportsSystemMenu();
    }

    public static int getKeyCodeForChar(char c2) {
        return application._getKeyCodeForChar(c2);
    }

    protected int _isKeyLocked(int keyCode) {
        return -1;
    }

    public final Optional<Boolean> isKeyLocked(int keyCode) {
        checkEventThread();
        int lockState = _isKeyLocked(keyCode);
        switch (lockState) {
            case 0:
                return Optional.of(false);
            case 1:
                return Optional.of(true);
            default:
                return Optional.empty();
        }
    }
}
