package sun.java2d.d3d;

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.peer.WindowPeer;
import java.util.ArrayList;
import sun.awt.Win32GraphicsDevice;
import sun.awt.windows.WWindowPeer;
import sun.java2d.d3d.D3DContext;
import sun.java2d.pipe.hw.ContextCapabilities;
import sun.java2d.windows.WindowsFlags;
import sun.misc.PerfCounter;

/* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice.class */
public class D3DGraphicsDevice extends Win32GraphicsDevice {
    private D3DContext context;
    private static boolean d3dAvailable;
    private ContextCapabilities d3dCaps;
    private boolean fsStatus;
    private Rectangle ownerOrigBounds;
    private boolean ownerWasVisible;
    private Window realFSWindow;
    private WindowListener fsWindowListener;
    private boolean fsWindowWasAlwaysOnTop;

    private static native boolean initD3D();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getDeviceCapsNative(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getDeviceIdNative(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean enterFullScreenExclusiveNative(int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean exitFullScreenExclusiveNative(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native DisplayMode getCurrentDisplayModeNative(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void configDisplayModeNative(int i2, long j2, int i3, int i4, int i5, int i6);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void enumDisplayModesNative(int i2, ArrayList arrayList);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getAvailableAcceleratedMemoryNative(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean isD3DAvailableOnDeviceNative(int i2);

    static {
        Toolkit.getDefaultToolkit();
        d3dAvailable = initD3D();
        if (d3dAvailable) {
            pfDisabled = true;
            PerfCounter.getD3DAvailable().set(1L);
        } else {
            PerfCounter.getD3DAvailable().set(0L);
        }
    }

    public static D3DGraphicsDevice createDevice(int i2) {
        if (!d3dAvailable) {
            return null;
        }
        ContextCapabilities deviceCaps = getDeviceCaps(i2);
        if ((deviceCaps.getCaps() & 262144) == 0) {
            if (WindowsFlags.isD3DVerbose()) {
                System.out.println("Could not enable Direct3D pipeline on screen " + i2);
                return null;
            }
            return null;
        }
        if (WindowsFlags.isD3DVerbose()) {
            System.out.println("Direct3D pipeline enabled on screen " + i2);
        }
        return new D3DGraphicsDevice(i2, deviceCaps);
    }

    private static ContextCapabilities getDeviceCaps(final int i2) {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            final C1Result c1Result = new C1Result();
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.1
                @Override // java.lang.Runnable
                public void run() {
                    c1Result.caps = D3DGraphicsDevice.getDeviceCapsNative(i2);
                    c1Result.id = D3DGraphicsDevice.getDeviceIdNative(i2);
                }
            });
            D3DContext.D3DContextCaps d3DContextCaps = new D3DContext.D3DContextCaps(c1Result.caps, c1Result.id);
            d3DRenderQueue.unlock();
            return d3DContextCaps != null ? d3DContextCaps : new D3DContext.D3DContextCaps(0, null);
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    /* renamed from: sun.java2d.d3d.D3DGraphicsDevice$1Result, reason: invalid class name */
    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice$1Result.class */
    class C1Result {
        int caps;
        String id;

        C1Result() {
        }
    }

    public final boolean isCapPresent(int i2) {
        return (this.d3dCaps.getCaps() & i2) != 0;
    }

    private D3DGraphicsDevice(int i2, ContextCapabilities contextCapabilities) {
        super(i2);
        this.ownerOrigBounds = null;
        this.descString = "D3DGraphicsDevice[screen=" + i2;
        this.d3dCaps = contextCapabilities;
        this.context = new D3DContext(D3DRenderQueue.getInstance(), this);
    }

    public boolean isD3DEnabledOnDevice() {
        return isValid() && isCapPresent(262144);
    }

    public static boolean isD3DAvailable() {
        return d3dAvailable;
    }

    private Frame getToplevelOwner(Window window) {
        Window owner = window;
        while (owner != null) {
            owner = owner.getOwner();
            if (owner instanceof Frame) {
                return (Frame) owner;
            }
        }
        return null;
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void enterFullScreenExclusive(final int i2, WindowPeer windowPeer) {
        final WWindowPeer wWindowPeer = (WWindowPeer) this.realFSWindow.getPeer();
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.2
                @Override // java.lang.Runnable
                public void run() {
                    long hWnd = wWindowPeer.getHWnd();
                    if (hWnd == 0) {
                        D3DGraphicsDevice.this.fsStatus = false;
                    } else {
                        D3DGraphicsDevice.this.fsStatus = D3DGraphicsDevice.enterFullScreenExclusiveNative(i2, hWnd);
                    }
                }
            });
            d3DRenderQueue.unlock();
            if (!this.fsStatus) {
                super.enterFullScreenExclusive(i2, windowPeer);
            }
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void exitFullScreenExclusive(final int i2, WindowPeer windowPeer) {
        if (this.fsStatus) {
            D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
            d3DRenderQueue.lock();
            try {
                d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.3
                    @Override // java.lang.Runnable
                    public void run() {
                        D3DGraphicsDevice.exitFullScreenExclusiveNative(i2);
                    }
                });
                d3DRenderQueue.unlock();
                return;
            } catch (Throwable th) {
                d3DRenderQueue.unlock();
                throw th;
            }
        }
        super.exitFullScreenExclusive(i2, windowPeer);
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice$D3DFSWindowAdapter.class */
    private static class D3DFSWindowAdapter extends WindowAdapter {
        private D3DFSWindowAdapter() {
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
            D3DRenderQueue.getInstance();
            D3DRenderQueue.restoreDevices();
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
            D3DRenderQueue.getInstance();
            D3DRenderQueue.restoreDevices();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x005d  */
    @Override // sun.awt.Win32GraphicsDevice
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void addFSWindowListener(java.awt.Window r7) {
        /*
            r6 = this;
            r0 = r7
            boolean r0 = r0 instanceof java.awt.Frame
            if (r0 != 0) goto L5d
            r0 = r7
            boolean r0 = r0 instanceof java.awt.Dialog
            if (r0 != 0) goto L5d
            r0 = r6
            r1 = r6
            r2 = r7
            java.awt.Frame r1 = r1.getToplevelOwner(r2)
            r2 = r1; r1 = r0; r0 = r2; 
            r1.realFSWindow = r2
            if (r0 == 0) goto L5d
            r0 = r6
            r1 = r6
            java.awt.Window r1 = r1.realFSWindow
            java.awt.Rectangle r1 = r1.getBounds()
            r0.ownerOrigBounds = r1
            r0 = r6
            java.awt.Window r0 = r0.realFSWindow
            java.awt.peer.ComponentPeer r0 = r0.getPeer()
            sun.awt.windows.WWindowPeer r0 = (sun.awt.windows.WWindowPeer) r0
            r8 = r0
            r0 = r6
            r1 = r6
            java.awt.Window r1 = r1.realFSWindow
            boolean r1 = r1.isVisible()
            r0.ownerWasVisible = r1
            r0 = r7
            java.awt.Rectangle r0 = r0.getBounds()
            r9 = r0
            r0 = r8
            r1 = r9
            int r1 = r1.f12372x
            r2 = r9
            int r2 = r2.f12373y
            r3 = r9
            int r3 = r3.width
            r4 = r9
            int r4 = r4.height
            r0.reshape(r1, r2, r3, r4)
            r0 = r8
            r1 = 1
            r0.setVisible(r1)
            goto L62
        L5d:
            r0 = r6
            r1 = r7
            r0.realFSWindow = r1
        L62:
            r0 = r6
            r1 = r6
            java.awt.Window r1 = r1.realFSWindow
            boolean r1 = r1.isAlwaysOnTop()
            r0.fsWindowWasAlwaysOnTop = r1
            r0 = r6
            java.awt.Window r0 = r0.realFSWindow
            java.awt.peer.ComponentPeer r0 = r0.getPeer()
            sun.awt.windows.WWindowPeer r0 = (sun.awt.windows.WWindowPeer) r0
            r1 = 1
            r0.setAlwaysOnTop(r1)
            r0 = r6
            sun.java2d.d3d.D3DGraphicsDevice$D3DFSWindowAdapter r1 = new sun.java2d.d3d.D3DGraphicsDevice$D3DFSWindowAdapter
            r2 = r1
            r3 = 0
            r2.<init>()
            r0.fsWindowListener = r1
            r0 = r6
            java.awt.Window r0 = r0.realFSWindow
            r1 = r6
            java.awt.event.WindowListener r1 = r1.fsWindowListener
            r0.addWindowListener(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.java2d.d3d.D3DGraphicsDevice.addFSWindowListener(java.awt.Window):void");
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void removeFSWindowListener(Window window) {
        this.realFSWindow.removeWindowListener(this.fsWindowListener);
        this.fsWindowListener = null;
        WWindowPeer wWindowPeer = (WWindowPeer) this.realFSWindow.getPeer();
        if (wWindowPeer != null) {
            if (this.ownerOrigBounds != null) {
                if (this.ownerOrigBounds.width == 0) {
                    this.ownerOrigBounds.width = 1;
                }
                if (this.ownerOrigBounds.height == 0) {
                    this.ownerOrigBounds.height = 1;
                }
                wWindowPeer.reshape(this.ownerOrigBounds.f12372x, this.ownerOrigBounds.f12373y, this.ownerOrigBounds.width, this.ownerOrigBounds.height);
                if (!this.ownerWasVisible) {
                    wWindowPeer.setVisible(false);
                }
                this.ownerOrigBounds = null;
            }
            if (!this.fsWindowWasAlwaysOnTop) {
                wWindowPeer.setAlwaysOnTop(false);
            }
        }
        this.realFSWindow = null;
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected DisplayMode getCurrentDisplayMode(final int i2) {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            final C2Result c2Result = new C2Result();
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.4
                @Override // java.lang.Runnable
                public void run() {
                    c2Result.dm = D3DGraphicsDevice.getCurrentDisplayModeNative(i2);
                }
            });
            if (c2Result.dm == null) {
                DisplayMode currentDisplayMode = super.getCurrentDisplayMode(i2);
                d3DRenderQueue.unlock();
                return currentDisplayMode;
            }
            DisplayMode displayMode = c2Result.dm;
            d3DRenderQueue.unlock();
            return displayMode;
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    /* renamed from: sun.java2d.d3d.D3DGraphicsDevice$2Result, reason: invalid class name */
    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice$2Result.class */
    class C2Result {
        DisplayMode dm = null;

        C2Result() {
        }
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void configDisplayMode(final int i2, WindowPeer windowPeer, final int i3, final int i4, final int i5, final int i6) {
        if (!this.fsStatus) {
            super.configDisplayMode(i2, windowPeer, i3, i4, i5, i6);
            return;
        }
        final WWindowPeer wWindowPeer = (WWindowPeer) this.realFSWindow.getPeer();
        if (getFullScreenWindow() != this.realFSWindow) {
            Rectangle bounds = getDefaultConfiguration().getBounds();
            wWindowPeer.reshape(bounds.f12372x, bounds.f12373y, i3, i4);
        }
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.5
                @Override // java.lang.Runnable
                public void run() {
                    long hWnd = wWindowPeer.getHWnd();
                    if (hWnd != 0) {
                        D3DGraphicsDevice.configDisplayModeNative(i2, hWnd, i3, i4, i5, i6);
                    }
                }
            });
            d3DRenderQueue.unlock();
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void enumDisplayModes(final int i2, final ArrayList arrayList) {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.6
                @Override // java.lang.Runnable
                public void run() {
                    D3DGraphicsDevice.enumDisplayModesNative(i2, arrayList);
                }
            });
            if (arrayList.size() == 0) {
                arrayList.add(getCurrentDisplayModeNative(i2));
            }
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    @Override // java.awt.GraphicsDevice
    public int getAvailableAcceleratedMemory() {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            final C3Result c3Result = new C3Result();
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.7
                @Override // java.lang.Runnable
                public void run() {
                    c3Result.mem = D3DGraphicsDevice.getAvailableAcceleratedMemoryNative(D3DGraphicsDevice.this.getScreen());
                }
            });
            int i2 = (int) c3Result.mem;
            d3DRenderQueue.unlock();
            return i2;
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    /* renamed from: sun.java2d.d3d.D3DGraphicsDevice$3Result, reason: invalid class name */
    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice$3Result.class */
    class C3Result {
        long mem = 0;

        C3Result() {
        }
    }

    @Override // sun.awt.Win32GraphicsDevice, java.awt.GraphicsDevice
    public GraphicsConfiguration[] getConfigurations() {
        if (this.configs == null && isD3DEnabledOnDevice()) {
            this.defaultConfig = getDefaultConfiguration();
            if (this.defaultConfig != null) {
                this.configs = new GraphicsConfiguration[1];
                this.configs[0] = this.defaultConfig;
                return (GraphicsConfiguration[]) this.configs.clone();
            }
        }
        return super.getConfigurations();
    }

    @Override // sun.awt.Win32GraphicsDevice, java.awt.GraphicsDevice
    public GraphicsConfiguration getDefaultConfiguration() {
        if (this.defaultConfig == null) {
            if (isD3DEnabledOnDevice()) {
                this.defaultConfig = new D3DGraphicsConfig(this);
            } else {
                this.defaultConfig = super.getDefaultConfiguration();
            }
        }
        return this.defaultConfig;
    }

    public static boolean isD3DAvailableOnDevice(final int i2) {
        if (!d3dAvailable) {
            return false;
        }
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            final C4Result c4Result = new C4Result();
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DGraphicsDevice.8
                @Override // java.lang.Runnable
                public void run() {
                    c4Result.avail = D3DGraphicsDevice.isD3DAvailableOnDeviceNative(i2);
                }
            });
            boolean z2 = c4Result.avail;
            d3DRenderQueue.unlock();
            return z2;
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    /* renamed from: sun.java2d.d3d.D3DGraphicsDevice$4Result, reason: invalid class name */
    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsDevice$4Result.class */
    class C4Result {
        boolean avail = false;

        C4Result() {
        }
    }

    D3DContext getContext() {
        return this.context;
    }

    ContextCapabilities getContextCapabilities() {
        return this.d3dCaps;
    }

    @Override // sun.awt.Win32GraphicsDevice, sun.awt.DisplayChangedListener
    public void displayChanged() {
        super.displayChanged();
        if (d3dAvailable) {
            this.d3dCaps = getDeviceCaps(getScreen());
        }
    }

    @Override // sun.awt.Win32GraphicsDevice
    protected void invalidate(int i2) {
        super.invalidate(i2);
        this.d3dCaps = new D3DContext.D3DContextCaps(0, null);
    }
}
