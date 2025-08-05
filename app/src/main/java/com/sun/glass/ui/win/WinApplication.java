package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.InvokeLaterDispatcher;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinApplication.class */
final class WinApplication extends Application implements InvokeLaterDispatcher.InvokeLaterSubmitter {
    static float overrideUIScale;
    static float overrideRenderScale;
    static float minDPIScale;
    static boolean forceIntegerRenderScale;
    private final InvokeLaterDispatcher invokeLaterDispatcher;
    private static boolean verbose;
    private static final int Process_DPI_Unaware = 0;
    private static final int Process_System_DPI_Aware = 1;
    private static final int Process_Per_Monitor_DPI_Aware = 2;

    private static native void initIDs(float f2, float f3, float f4, boolean z2);

    private native long _init(int i2);

    private native void _setClassLoader(ClassLoader classLoader);

    private native void _runLoop(Runnable runnable);

    private native void _terminateLoop();

    private native Object _enterNestedEventLoopImpl();

    private native void _leaveNestedEventLoopImpl(Object obj);

    @Override // com.sun.glass.ui.Application
    protected native Screen[] staticScreen_getScreens();

    @Override // com.sun.glass.ui.Application
    protected native void _invokeAndWait(Runnable runnable);

    private native void _submitForLaterInvocation(Runnable runnable);

    private native String _getHighContrastTheme();

    @Override // com.sun.glass.ui.Application
    protected native boolean _supportsUnifiedWindows();

    @Override // com.sun.glass.ui.Application
    protected native int _getKeyCodeForChar(char c2);

    @Override // com.sun.glass.ui.Application
    protected native int _isKeyLocked(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean getBoolean(String propname, boolean defval, String description) {
        String str = System.getProperty(propname);
        if (str == null) {
            str = System.getenv(propname);
        }
        if (str == null) {
            return defval;
        }
        Boolean ret = Boolean.valueOf(Boolean.parseBoolean(str));
        if (PrismSettings.verbose) {
            System.out.println((ret.booleanValue() ? "" : "not ") + description);
        }
        return ret.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float getFloat(String propname, float defval, String description) throws NumberFormatException {
        float val;
        String str = System.getProperty(propname);
        if (str == null) {
            str = System.getenv(propname);
        }
        if (str == null) {
            return defval;
        }
        String str2 = str.trim();
        if (str2.endsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
            val = Integer.parseInt(str2.substring(0, str2.length() - 1)) / 100.0f;
        } else if (str2.endsWith("DPI") || str2.endsWith("dpi")) {
            val = Integer.parseInt(str2.substring(0, str2.length() - 3)) / 96.0f;
        } else {
            val = Float.parseFloat(str2);
        }
        if (PrismSettings.verbose) {
            System.out.println(description + val);
        }
        return val;
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.glass.ui.win.WinApplication.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                boolean unused = WinApplication.verbose = Boolean.getBoolean("javafx.verbose");
                if (PrismSettings.allowHiDPIScaling) {
                    WinApplication.overrideUIScale = WinApplication.getFloat("glass.win.uiScale", -1.0f, "Forcing UI scaling factor: ");
                    WinApplication.overrideRenderScale = WinApplication.getFloat("glass.win.renderScale", -1.0f, "Forcing Rendering scaling factor: ");
                    WinApplication.minDPIScale = WinApplication.getFloat("glass.win.minHiDPI", 1.5f, "Threshold to enable UI scaling factor: ");
                    WinApplication.forceIntegerRenderScale = WinApplication.getBoolean("glass.win.forceIntegerRenderScale", true, "forcing integer rendering scale");
                } else {
                    WinApplication.overrideRenderScale = 1.0f;
                    WinApplication.overrideUIScale = 1.0f;
                    WinApplication.minDPIScale = Float.MAX_VALUE;
                    WinApplication.forceIntegerRenderScale = false;
                }
                Toolkit.loadMSWindowsLibraries();
                WinApplication.loadNativeLibrary();
                return null;
            }
        });
        initIDs(overrideUIScale, overrideRenderScale, minDPIScale, forceIntegerRenderScale);
    }

    WinApplication() {
        boolean isEventThread = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("javafx.embed.isEventThread"));
        })).booleanValue();
        if (!isEventThread) {
            this.invokeLaterDispatcher = new InvokeLaterDispatcher(this);
            this.invokeLaterDispatcher.start();
        } else {
            this.invokeLaterDispatcher = null;
        }
    }

    private static int getDesiredAwarenesslevel() {
        if (!PrismSettings.allowHiDPIScaling) {
            return 0;
        }
        String awareRequested = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("javafx.glass.winDPIawareness");
        });
        if (awareRequested != null) {
            String awareRequested2 = awareRequested.toLowerCase();
            if (awareRequested2.equals("aware")) {
                return 1;
            }
            if (awareRequested2.equals("permonitor")) {
                return 2;
            }
            if (!awareRequested2.equals("unaware")) {
                System.err.println("unrecognized DPI awareness request, defaulting to unaware: " + awareRequested2);
                return 0;
            }
            return 0;
        }
        return 2;
    }

    @Override // com.sun.glass.ui.Application
    protected void runLoop(Runnable launchable) {
        boolean isEventThread = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("javafx.embed.isEventThread"));
        })).booleanValue();
        int awareness = getDesiredAwarenesslevel();
        ClassLoader classLoader = WinApplication.class.getClassLoader();
        _setClassLoader(classLoader);
        if (isEventThread) {
            _init(awareness);
            setEventThread(Thread.currentThread());
            launchable.run();
        } else {
            Thread toolkitThread = (Thread) AccessController.doPrivileged(() -> {
                return new Thread(() -> {
                    _init(awareness);
                    _runLoop(launchable);
                }, "WindowsNativeRunloopThread");
            });
            setEventThread(toolkitThread);
            toolkitThread.start();
        }
    }

    @Override // com.sun.glass.ui.Application
    protected void finishTerminating() {
        Thread toolkitThread = getEventThread();
        if (toolkitThread != null) {
            _terminateLoop();
            setEventThread(null);
        }
        super.finishTerminating();
    }

    @Override // com.sun.glass.ui.Application
    public boolean shouldUpdateWindow() {
        return true;
    }

    @Override // com.sun.glass.ui.Application
    protected Object _enterNestedEventLoop() {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        try {
            return _enterNestedEventLoopImpl();
        } finally {
            if (this.invokeLaterDispatcher != null) {
                this.invokeLaterDispatcher.notifyLeftNestedEventLoop();
            }
        }
    }

    @Override // com.sun.glass.ui.Application
    protected void _leaveNestedEventLoop(Object retValue) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyLeavingNestedEventLoop();
        }
        _leaveNestedEventLoopImpl(retValue);
    }

    @Override // com.sun.glass.ui.Application
    public Window createWindow(Window owner, Screen screen, int styleMask) {
        return new WinWindow(owner, screen, styleMask);
    }

    @Override // com.sun.glass.ui.Application
    public Window createWindow(long parent) {
        return new WinChildWindow(parent);
    }

    @Override // com.sun.glass.ui.Application
    public View createView() {
        return new WinView();
    }

    @Override // com.sun.glass.ui.Application
    public Cursor createCursor(int type) {
        return new WinCursor(type);
    }

    @Override // com.sun.glass.ui.Application
    public Cursor createCursor(int x2, int y2, Pixels pixels) {
        return new WinCursor(x2, y2, pixels);
    }

    @Override // com.sun.glass.ui.Application
    protected void staticCursor_setVisible(boolean visible) {
        WinCursor.setVisible_impl(visible);
    }

    @Override // com.sun.glass.ui.Application
    protected Size staticCursor_getBestSize(int width, int height) {
        return WinCursor.getBestSize_impl(width, height);
    }

    @Override // com.sun.glass.ui.Application
    public Pixels createPixels(int width, int height, ByteBuffer data) {
        return new WinPixels(width, height, data);
    }

    @Override // com.sun.glass.ui.Application
    public Pixels createPixels(int width, int height, IntBuffer data) {
        return new WinPixels(width, height, data);
    }

    @Override // com.sun.glass.ui.Application
    public Pixels createPixels(int width, int height, IntBuffer data, float scale) {
        return new WinPixels(width, height, data, scale);
    }

    @Override // com.sun.glass.ui.Application
    protected int staticPixels_getNativeFormat() {
        return WinPixels.getNativeFormat_impl();
    }

    @Override // com.sun.glass.ui.Application
    public Robot createRobot() {
        return new WinRobot();
    }

    @Override // com.sun.glass.ui.Application
    protected double staticScreen_getVideoRefreshPeriod() {
        return 0.0d;
    }

    @Override // com.sun.glass.ui.Application
    public Timer createTimer(Runnable runnable) {
        return new WinTimer(runnable);
    }

    @Override // com.sun.glass.ui.Application
    protected int staticTimer_getMinPeriod() {
        return WinTimer.getMinPeriod_impl();
    }

    @Override // com.sun.glass.ui.Application
    protected int staticTimer_getMaxPeriod() {
        return WinTimer.getMaxPeriod_impl();
    }

    @Override // com.sun.glass.ui.Application
    public Accessible createAccessible() {
        return new WinAccessible();
    }

    @Override // com.sun.glass.ui.Application
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window owner, String folder, String filename, String title, int type, boolean multipleMode, CommonDialogs.ExtensionFilter[] extensionFilters, int defaultFilterIndex) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        return WinCommonDialogs.showFileChooser_impl(owner, folder, filename, title, type, multipleMode, extensionFilters, defaultFilterIndex);
    }

    @Override // com.sun.glass.ui.Application
    protected File staticCommonDialogs_showFolderChooser(Window owner, String folder, String title) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        return WinCommonDialogs.showFolderChooser_impl(owner, folder, title);
    }

    @Override // com.sun.glass.ui.Application
    protected long staticView_getMultiClickTime() {
        return WinView.getMultiClickTime_impl();
    }

    @Override // com.sun.glass.ui.Application
    protected int staticView_getMultiClickMaxX() {
        return WinView.getMultiClickMaxX_impl();
    }

    @Override // com.sun.glass.ui.Application
    protected int staticView_getMultiClickMaxY() {
        return WinView.getMultiClickMaxY_impl();
    }

    @Override // com.sun.glass.ui.InvokeLaterDispatcher.InvokeLaterSubmitter
    public void submitForLaterInvocation(Runnable r2) {
        _submitForLaterInvocation(r2);
    }

    @Override // com.sun.glass.ui.Application
    protected void _invokeLater(Runnable runnable) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.invokeLater(runnable);
        } else {
            submitForLaterInvocation(runnable);
        }
    }

    @Override // com.sun.glass.ui.Application
    public String getHighContrastTheme() {
        checkEventThread();
        return _getHighContrastTheme();
    }

    @Override // com.sun.glass.ui.Application
    protected boolean _supportsInputMethods() {
        return true;
    }

    @Override // com.sun.glass.ui.Application
    protected boolean _supportsTransparentWindows() {
        return true;
    }

    @Override // com.sun.glass.ui.Application
    public String getDataDirectory() {
        checkEventThread();
        String baseDirectory = (String) AccessController.doPrivileged(() -> {
            return System.getenv("APPDATA");
        });
        if (baseDirectory == null || baseDirectory.length() == 0) {
            return super.getDataDirectory();
        }
        return baseDirectory + File.separator + this.name + File.separator;
    }
}
