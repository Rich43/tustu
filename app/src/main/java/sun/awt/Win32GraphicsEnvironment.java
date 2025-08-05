package sun.awt;

import java.awt.AWTError;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.peer.ComponentPeer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ListIterator;
import sun.awt.windows.WToolkit;
import sun.java2d.SunGraphicsEnvironment;
import sun.java2d.SurfaceManagerFactory;
import sun.java2d.WindowsSurfaceManagerFactory;
import sun.java2d.d3d.D3DGraphicsDevice;
import sun.java2d.windows.WindowsFlags;

/* loaded from: rt.jar:sun/awt/Win32GraphicsEnvironment.class */
public class Win32GraphicsEnvironment extends SunGraphicsEnvironment {
    private static boolean displayInitialized;
    private ArrayList<WeakReference<Win32GraphicsDevice>> oldDevices;
    private static volatile boolean isDWMCompositionEnabled;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initDisplay();

    @Override // sun.java2d.SunGraphicsEnvironment
    protected native int getNumScreens();

    protected native int getDefaultScreen();

    public native int getXResolution();

    public native int getYResolution();

    public static native boolean isVistaOS();

    static {
        $assertionsDisabled = !Win32GraphicsEnvironment.class.desiredAssertionStatus();
        WToolkit.loadLibraries();
        WindowsFlags.initFlags();
        initDisplayWrapper();
        SurfaceManagerFactory.setInstance(new WindowsSurfaceManagerFactory());
    }

    public static void initDisplayWrapper() {
        if (!displayInitialized) {
            displayInitialized = true;
            initDisplay();
        }
    }

    @Override // sun.java2d.SunGraphicsEnvironment, java.awt.GraphicsEnvironment
    public GraphicsDevice getDefaultScreenDevice() {
        GraphicsDevice[] screenDevices = getScreenDevices();
        if (screenDevices.length == 0) {
            throw new AWTError("no screen devices");
        }
        int defaultScreen = getDefaultScreen();
        return screenDevices[(0 >= defaultScreen || defaultScreen >= screenDevices.length) ? 0 : defaultScreen];
    }

    @Override // sun.java2d.SunGraphicsEnvironment, sun.awt.DisplayChangedListener
    public void displayChanged() {
        GraphicsDevice[] graphicsDeviceArr = new GraphicsDevice[getNumScreens()];
        GraphicsDevice[] graphicsDeviceArr2 = this.screens;
        if (graphicsDeviceArr2 != null) {
            for (int i2 = 0; i2 < graphicsDeviceArr2.length; i2++) {
                if (!(this.screens[i2] instanceof Win32GraphicsDevice)) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError(graphicsDeviceArr2[i2]);
                    }
                } else {
                    Win32GraphicsDevice win32GraphicsDevice = (Win32GraphicsDevice) graphicsDeviceArr2[i2];
                    if (!win32GraphicsDevice.isValid()) {
                        if (this.oldDevices == null) {
                            this.oldDevices = new ArrayList<>();
                        }
                        this.oldDevices.add(new WeakReference<>(win32GraphicsDevice));
                    } else if (i2 < graphicsDeviceArr.length) {
                        graphicsDeviceArr[i2] = win32GraphicsDevice;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < graphicsDeviceArr.length; i3++) {
            if (graphicsDeviceArr[i3] == null) {
                graphicsDeviceArr[i3] = makeScreenDevice(i3);
            }
        }
        this.screens = graphicsDeviceArr;
        for (Object obj : this.screens) {
            if (obj instanceof DisplayChangedListener) {
                ((DisplayChangedListener) obj).displayChanged();
            }
        }
        if (this.oldDevices != null) {
            int defaultScreen = getDefaultScreen();
            ListIterator<WeakReference<Win32GraphicsDevice>> listIterator = this.oldDevices.listIterator();
            while (listIterator.hasNext()) {
                Win32GraphicsDevice win32GraphicsDevice2 = listIterator.next().get();
                if (win32GraphicsDevice2 != null) {
                    win32GraphicsDevice2.invalidate(defaultScreen);
                    win32GraphicsDevice2.displayChanged();
                } else {
                    listIterator.remove();
                }
            }
        }
        WToolkit.resetGC();
        this.displayChanger.notifyListeners();
    }

    @Override // sun.java2d.SunGraphicsEnvironment
    protected GraphicsDevice makeScreenDevice(int i2) {
        Win32GraphicsDevice win32GraphicsDevice = null;
        if (WindowsFlags.isD3DEnabled()) {
            win32GraphicsDevice = D3DGraphicsDevice.createDevice(i2);
        }
        if (win32GraphicsDevice == null) {
            win32GraphicsDevice = new Win32GraphicsDevice(i2);
        }
        return win32GraphicsDevice;
    }

    @Override // sun.java2d.SunGraphicsEnvironment
    public boolean isDisplayLocal() {
        return true;
    }

    @Override // sun.java2d.SunGraphicsEnvironment
    public boolean isFlipStrategyPreferred(ComponentPeer componentPeer) {
        GraphicsConfiguration graphicsConfiguration;
        if (componentPeer != null && (graphicsConfiguration = componentPeer.getGraphicsConfiguration()) != null) {
            GraphicsDevice device = graphicsConfiguration.getDevice();
            if (device instanceof D3DGraphicsDevice) {
                return ((D3DGraphicsDevice) device).isD3DEnabledOnDevice();
            }
            return false;
        }
        return false;
    }

    public static boolean isDWMCompositionEnabled() {
        return isDWMCompositionEnabled;
    }

    private static void dwmCompositionChanged(boolean z2) {
        isDWMCompositionEnabled = z2;
    }
}
