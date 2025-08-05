package sun.awt;

import java.awt.AWTPermission;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ColorModel;
import java.awt.peer.WindowPeer;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Vector;
import sun.awt.windows.WWindowPeer;
import sun.java2d.opengl.WGLGraphicsConfig;
import sun.java2d.windows.WindowsFlags;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/Win32GraphicsDevice.class */
public class Win32GraphicsDevice extends GraphicsDevice implements DisplayChangedListener {
    int screen;
    ColorModel dynamicColorModel;
    ColorModel colorModel;
    protected GraphicsConfiguration[] configs;
    protected GraphicsConfiguration defaultConfig;
    private final String idString;
    protected String descString;
    protected static boolean pfDisabled;
    private static AWTPermission fullScreenExclusivePermission;
    private DisplayMode defaultDisplayMode;
    private WindowListener fsWindowListener;
    private SunDisplayChanger topLevels = new SunDisplayChanger();
    private boolean valid = true;

    private static native void initIDs();

    native void initDevice(int i2);

    private native int getMaxConfigsImpl(int i2);

    protected native boolean isPixFmtSupported(int i2, int i3);

    private native int getDefaultPixIDImpl(int i2);

    protected native void enterFullScreenExclusive(int i2, WindowPeer windowPeer);

    protected native void exitFullScreenExclusive(int i2, WindowPeer windowPeer);

    protected native DisplayMode getCurrentDisplayMode(int i2);

    protected native void configDisplayMode(int i2, WindowPeer windowPeer, int i3, int i4, int i5, int i6);

    protected native void enumDisplayModes(int i2, ArrayList arrayList);

    private native ColorModel makeColorModel(int i2, boolean z2);

    static {
        pfDisabled = ((String) AccessController.doPrivileged(new GetPropertyAction("sun.awt.nopixfmt"))) != null;
        initIDs();
    }

    public Win32GraphicsDevice(int i2) {
        this.screen = i2;
        this.idString = "\\Display" + this.screen;
        this.descString = "Win32GraphicsDevice[screen=" + this.screen;
        initDevice(i2);
    }

    @Override // java.awt.GraphicsDevice
    public int getType() {
        return 0;
    }

    public int getScreen() {
        return this.screen;
    }

    public boolean isValid() {
        return this.valid;
    }

    protected void invalidate(int i2) {
        this.valid = false;
        this.screen = i2;
    }

    @Override // java.awt.GraphicsDevice
    public String getIDstring() {
        return this.idString;
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration[] getConfigurations() {
        if (this.configs == null) {
            if (WindowsFlags.isOGLEnabled() && isDefaultDevice()) {
                this.defaultConfig = getDefaultConfiguration();
                if (this.defaultConfig != null) {
                    this.configs = new GraphicsConfiguration[1];
                    this.configs[0] = this.defaultConfig;
                    return (GraphicsConfiguration[]) this.configs.clone();
                }
            }
            int maxConfigs = getMaxConfigs(this.screen);
            int defaultPixID = getDefaultPixID(this.screen);
            Vector vector = new Vector(maxConfigs);
            if (defaultPixID == 0) {
                this.defaultConfig = Win32GraphicsConfig.getConfig(this, defaultPixID);
                vector.addElement(this.defaultConfig);
            } else {
                for (int i2 = 1; i2 <= maxConfigs; i2++) {
                    if (isPixFmtSupported(i2, this.screen)) {
                        if (i2 == defaultPixID) {
                            this.defaultConfig = Win32GraphicsConfig.getConfig(this, i2);
                            vector.addElement(this.defaultConfig);
                        } else {
                            vector.addElement(Win32GraphicsConfig.getConfig(this, i2));
                        }
                    }
                }
            }
            this.configs = new GraphicsConfiguration[vector.size()];
            vector.copyInto(this.configs);
        }
        return (GraphicsConfiguration[]) this.configs.clone();
    }

    protected int getMaxConfigs(int i2) {
        if (pfDisabled) {
            return 1;
        }
        return getMaxConfigsImpl(i2);
    }

    protected int getDefaultPixID(int i2) {
        if (pfDisabled) {
            return 0;
        }
        return getDefaultPixIDImpl(i2);
    }

    @Override // java.awt.GraphicsDevice
    public GraphicsConfiguration getDefaultConfiguration() {
        if (this.defaultConfig == null) {
            if (WindowsFlags.isOGLEnabled() && isDefaultDevice()) {
                this.defaultConfig = WGLGraphicsConfig.getConfig(this, WGLGraphicsConfig.getDefaultPixFmt(this.screen));
                if (WindowsFlags.isOGLVerbose()) {
                    if (this.defaultConfig != null) {
                        System.out.print("OpenGL pipeline enabled");
                    } else {
                        System.out.print("Could not enable OpenGL pipeline");
                    }
                    System.out.println(" for default config on screen " + this.screen);
                }
            }
            if (this.defaultConfig == null) {
                this.defaultConfig = Win32GraphicsConfig.getConfig(this, 0);
            }
        }
        return this.defaultConfig;
    }

    public String toString() {
        return this.valid ? this.descString + "]" : this.descString + ", removed]";
    }

    private boolean isDefaultDevice() {
        return this == GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    private static boolean isFSExclusiveModeAllowed() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (fullScreenExclusivePermission == null) {
                fullScreenExclusivePermission = new AWTPermission("fullScreenExclusive");
            }
            try {
                securityManager.checkPermission(fullScreenExclusivePermission);
                return true;
            } catch (SecurityException e2) {
                return false;
            }
        }
        return true;
    }

    @Override // java.awt.GraphicsDevice
    public boolean isFullScreenSupported() {
        return isFSExclusiveModeAllowed();
    }

    @Override // java.awt.GraphicsDevice
    public synchronized void setFullScreenWindow(Window window) {
        Window fullScreenWindow = getFullScreenWindow();
        if (window == fullScreenWindow) {
            return;
        }
        if (!isFullScreenSupported()) {
            super.setFullScreenWindow(window);
            return;
        }
        if (fullScreenWindow != null) {
            if (this.defaultDisplayMode != null) {
                setDisplayMode(this.defaultDisplayMode);
                this.defaultDisplayMode = null;
            }
            WWindowPeer wWindowPeer = (WWindowPeer) fullScreenWindow.getPeer();
            if (wWindowPeer != null) {
                wWindowPeer.setFullScreenExclusiveModeState(false);
                synchronized (wWindowPeer) {
                    exitFullScreenExclusive(this.screen, wWindowPeer);
                }
            }
            removeFSWindowListener(fullScreenWindow);
        }
        super.setFullScreenWindow(window);
        if (window != null) {
            this.defaultDisplayMode = getDisplayMode();
            addFSWindowListener(window);
            WWindowPeer wWindowPeer2 = (WWindowPeer) window.getPeer();
            if (wWindowPeer2 != null) {
                synchronized (wWindowPeer2) {
                    enterFullScreenExclusive(this.screen, wWindowPeer2);
                }
                wWindowPeer2.setFullScreenExclusiveModeState(true);
            }
            wWindowPeer2.updateGC();
        }
    }

    @Override // java.awt.GraphicsDevice
    public boolean isDisplayChangeSupported() {
        return isFullScreenSupported() && getFullScreenWindow() != null;
    }

    @Override // java.awt.GraphicsDevice
    public synchronized void setDisplayMode(DisplayMode displayMode) {
        DisplayMode matchingDisplayMode;
        if (!isDisplayChangeSupported()) {
            super.setDisplayMode(displayMode);
            return;
        }
        if (displayMode == null || (matchingDisplayMode = getMatchingDisplayMode(displayMode)) == null) {
            throw new IllegalArgumentException("Invalid display mode");
        }
        if (getDisplayMode().equals(matchingDisplayMode)) {
            return;
        }
        Window fullScreenWindow = getFullScreenWindow();
        if (fullScreenWindow != null) {
            configDisplayMode(this.screen, (WWindowPeer) fullScreenWindow.getPeer(), matchingDisplayMode.getWidth(), matchingDisplayMode.getHeight(), matchingDisplayMode.getBitDepth(), matchingDisplayMode.getRefreshRate());
            Rectangle bounds = getDefaultConfiguration().getBounds();
            fullScreenWindow.setBounds(bounds.f12372x, bounds.f12373y, matchingDisplayMode.getWidth(), matchingDisplayMode.getHeight());
            return;
        }
        throw new IllegalStateException("Must be in fullscreen mode in order to set display mode");
    }

    @Override // java.awt.GraphicsDevice
    public synchronized DisplayMode getDisplayMode() {
        return getCurrentDisplayMode(this.screen);
    }

    @Override // java.awt.GraphicsDevice
    public synchronized DisplayMode[] getDisplayModes() {
        ArrayList arrayList = new ArrayList();
        enumDisplayModes(this.screen, arrayList);
        int size = arrayList.size();
        DisplayMode[] displayModeArr = new DisplayMode[size];
        for (int i2 = 0; i2 < size; i2++) {
            displayModeArr[i2] = (DisplayMode) arrayList.get(i2);
        }
        return displayModeArr;
    }

    protected synchronized DisplayMode getMatchingDisplayMode(DisplayMode displayMode) {
        if (!isDisplayChangeSupported()) {
            return null;
        }
        for (DisplayMode displayMode2 : getDisplayModes()) {
            if (displayMode.equals(displayMode2) || (displayMode.getRefreshRate() == 0 && displayMode.getWidth() == displayMode2.getWidth() && displayMode.getHeight() == displayMode2.getHeight() && displayMode.getBitDepth() == displayMode2.getBitDepth())) {
                return displayMode2;
            }
        }
        return null;
    }

    @Override // sun.awt.DisplayChangedListener
    public void displayChanged() {
        this.dynamicColorModel = null;
        this.defaultConfig = null;
        this.configs = null;
        this.topLevels.notifyListeners();
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
    }

    public void addDisplayChangedListener(DisplayChangedListener displayChangedListener) {
        this.topLevels.add(displayChangedListener);
    }

    public void removeDisplayChangedListener(DisplayChangedListener displayChangedListener) {
        this.topLevels.remove(displayChangedListener);
    }

    public ColorModel getDynamicColorModel() {
        if (this.dynamicColorModel == null) {
            this.dynamicColorModel = makeColorModel(this.screen, true);
        }
        return this.dynamicColorModel;
    }

    public ColorModel getColorModel() {
        if (this.colorModel == null) {
            this.colorModel = makeColorModel(this.screen, false);
        }
        return this.colorModel;
    }

    /* loaded from: rt.jar:sun/awt/Win32GraphicsDevice$Win32FSWindowAdapter.class */
    private static class Win32FSWindowAdapter extends WindowAdapter {
        private Win32GraphicsDevice device;
        private DisplayMode dm;

        Win32FSWindowAdapter(Win32GraphicsDevice win32GraphicsDevice) {
            this.device = win32GraphicsDevice;
        }

        private void setFSWindowsState(Window window, int i2) throws HeadlessException {
            GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            if (window != null) {
                for (GraphicsDevice graphicsDevice : screenDevices) {
                    if (window == graphicsDevice.getFullScreenWindow()) {
                        return;
                    }
                }
            }
            for (GraphicsDevice graphicsDevice2 : screenDevices) {
                Window fullScreenWindow = graphicsDevice2.getFullScreenWindow();
                if (fullScreenWindow instanceof Frame) {
                    ((Frame) fullScreenWindow).setExtendedState(i2);
                }
            }
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) throws HeadlessException {
            setFSWindowsState(windowEvent.getOppositeWindow(), 1);
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) throws HeadlessException {
            setFSWindowsState(windowEvent.getOppositeWindow(), 0);
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
            DisplayMode displayMode = this.device.defaultDisplayMode;
            if (displayMode != null) {
                this.dm = this.device.getDisplayMode();
                this.device.setDisplayMode(displayMode);
            }
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
            if (this.dm != null) {
                this.device.setDisplayMode(this.dm);
                this.dm = null;
            }
        }
    }

    protected void addFSWindowListener(final Window window) {
        this.fsWindowListener = new Win32FSWindowAdapter(this);
        EventQueue.invokeLater(new Runnable() { // from class: sun.awt.Win32GraphicsDevice.1
            @Override // java.lang.Runnable
            public void run() {
                window.addWindowListener(Win32GraphicsDevice.this.fsWindowListener);
            }
        });
    }

    protected void removeFSWindowListener(Window window) {
        window.removeWindowListener(this.fsWindowListener);
        this.fsWindowListener = null;
    }
}
