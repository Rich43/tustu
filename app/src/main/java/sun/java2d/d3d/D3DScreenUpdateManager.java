package sun.java2d.d3d;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Window;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import sun.awt.AWTAccessor;
import sun.awt.Win32GraphicsConfig;
import sun.awt.windows.WComponentPeer;
import sun.java2d.InvalidPipeException;
import sun.java2d.ScreenUpdateManager;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.d3d.D3DSurfaceData;
import sun.java2d.windows.GDIWindowSurfaceData;
import sun.java2d.windows.WindowsFlags;
import sun.misc.ThreadGroupUtils;

/* loaded from: rt.jar:sun/java2d/d3d/D3DScreenUpdateManager.class */
public class D3DScreenUpdateManager extends ScreenUpdateManager implements Runnable {
    private static final int MIN_WIN_SIZE = 150;
    private volatile Thread screenUpdater;
    private boolean needsUpdateNow;
    private ArrayList<D3DSurfaceData.D3DWindowSurfaceData> d3dwSurfaces;
    private HashMap<D3DSurfaceData.D3DWindowSurfaceData, GDIWindowSurfaceData> gdiSurfaces;
    private Object runLock = new Object();
    private volatile boolean done = false;

    public D3DScreenUpdateManager() {
        AccessController.doPrivileged(() -> {
            Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), () -> {
                this.done = true;
                wakeUpUpdateThread();
            });
            thread.setContextClassLoader(null);
            try {
                Runtime.getRuntime().addShutdownHook(thread);
                return null;
            } catch (Exception e2) {
                this.done = true;
                return null;
            }
        });
    }

    @Override // sun.java2d.ScreenUpdateManager
    public SurfaceData createScreenSurface(Win32GraphicsConfig win32GraphicsConfig, WComponentPeer wComponentPeer, int i2, boolean z2) {
        if (this.done || !(win32GraphicsConfig instanceof D3DGraphicsConfig)) {
            return super.createScreenSurface(win32GraphicsConfig, wComponentPeer, i2, z2);
        }
        SurfaceData surfaceDataCreateData = null;
        if (canUseD3DOnScreen(wComponentPeer, win32GraphicsConfig, i2)) {
            try {
                surfaceDataCreateData = D3DSurfaceData.createData(wComponentPeer);
            } catch (InvalidPipeException e2) {
                surfaceDataCreateData = null;
            }
        }
        if (surfaceDataCreateData == null) {
            surfaceDataCreateData = GDIWindowSurfaceData.createData(wComponentPeer);
        }
        if (z2) {
            repaintPeerTarget(wComponentPeer);
        }
        return surfaceDataCreateData;
    }

    public static boolean canUseD3DOnScreen(WComponentPeer wComponentPeer, Win32GraphicsConfig win32GraphicsConfig, int i2) {
        if (!(win32GraphicsConfig instanceof D3DGraphicsConfig)) {
            return false;
        }
        D3DGraphicsDevice d3DDevice = ((D3DGraphicsConfig) win32GraphicsConfig).getD3DDevice();
        String name = wComponentPeer.getClass().getName();
        Rectangle bounds = wComponentPeer.getBounds();
        Component component = (Component) wComponentPeer.getTarget();
        Window fullScreenWindow = d3DDevice.getFullScreenWindow();
        return WindowsFlags.isD3DOnScreenEnabled() && d3DDevice.isD3DEnabledOnDevice() && wComponentPeer.isAccelCapable() && (bounds.width > 150 || bounds.height > 150) && i2 == 0 && ((fullScreenWindow == null || (fullScreenWindow == component && !hasHWChildren(component))) && (name.equals("sun.awt.windows.WCanvasPeer") || name.equals("sun.awt.windows.WDialogPeer") || name.equals("sun.awt.windows.WPanelPeer") || name.equals("sun.awt.windows.WWindowPeer") || name.equals("sun.awt.windows.WFramePeer") || name.equals("sun.awt.windows.WEmbeddedFramePeer")));
    }

    @Override // sun.java2d.ScreenUpdateManager
    public Graphics2D createGraphics(SurfaceData surfaceData, WComponentPeer wComponentPeer, Color color, Color color2, Font font) {
        if (!this.done && (surfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData)) {
            D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData) surfaceData;
            if (!d3DWindowSurfaceData.isSurfaceLost() || validate(d3DWindowSurfaceData)) {
                trackScreenSurface(d3DWindowSurfaceData);
                return new SunGraphics2D(surfaceData, color, color2, font);
            }
            surfaceData = getGdiSurface(d3DWindowSurfaceData);
        }
        return super.createGraphics(surfaceData, wComponentPeer, color, color2, font);
    }

    private void repaintPeerTarget(WComponentPeer wComponentPeer) {
        Rectangle bounds = AWTAccessor.getComponentAccessor().getBounds((Component) wComponentPeer.getTarget());
        wComponentPeer.handlePaint(0, 0, bounds.width, bounds.height);
    }

    private void trackScreenSurface(SurfaceData surfaceData) {
        if (!this.done && (surfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData)) {
            synchronized (this) {
                if (this.d3dwSurfaces == null) {
                    this.d3dwSurfaces = new ArrayList<>();
                }
                D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData) surfaceData;
                if (!this.d3dwSurfaces.contains(d3DWindowSurfaceData)) {
                    this.d3dwSurfaces.add(d3DWindowSurfaceData);
                }
            }
            startUpdateThread();
        }
    }

    @Override // sun.java2d.ScreenUpdateManager
    public synchronized void dropScreenSurface(SurfaceData surfaceData) {
        if (this.d3dwSurfaces != null && (surfaceData instanceof D3DSurfaceData.D3DWindowSurfaceData)) {
            D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData = (D3DSurfaceData.D3DWindowSurfaceData) surfaceData;
            removeGdiSurface(d3DWindowSurfaceData);
            this.d3dwSurfaces.remove(d3DWindowSurfaceData);
        }
    }

    @Override // sun.java2d.ScreenUpdateManager
    public SurfaceData getReplacementScreenSurface(WComponentPeer wComponentPeer, SurfaceData surfaceData) {
        SurfaceData replacementScreenSurface = super.getReplacementScreenSurface(wComponentPeer, surfaceData);
        trackScreenSurface(replacementScreenSurface);
        return replacementScreenSurface;
    }

    private void removeGdiSurface(D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData) {
        GDIWindowSurfaceData gDIWindowSurfaceData;
        if (this.gdiSurfaces != null && (gDIWindowSurfaceData = this.gdiSurfaces.get(d3DWindowSurfaceData)) != null) {
            gDIWindowSurfaceData.invalidate();
            this.gdiSurfaces.remove(d3DWindowSurfaceData);
        }
    }

    private synchronized void startUpdateThread() {
        if (this.screenUpdater == null) {
            this.screenUpdater = (Thread) AccessController.doPrivileged(() -> {
                Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), this, "D3D Screen Updater");
                thread.setPriority(7);
                thread.setDaemon(true);
                return thread;
            });
            this.screenUpdater.start();
        } else {
            wakeUpUpdateThread();
        }
    }

    public void wakeUpUpdateThread() {
        synchronized (this.runLock) {
            this.runLock.notifyAll();
        }
    }

    public void runUpdateNow() {
        synchronized (this) {
            if (this.done || this.screenUpdater == null || this.d3dwSurfaces == null || this.d3dwSurfaces.size() == 0) {
                return;
            }
            synchronized (this.runLock) {
                this.needsUpdateNow = true;
                this.runLock.notifyAll();
                while (this.needsUpdateNow) {
                    try {
                        this.runLock.wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        D3DSurfaceData.D3DWindowSurfaceData[] d3DWindowSurfaceDataArr;
        while (!this.done) {
            synchronized (this.runLock) {
                long j2 = this.d3dwSurfaces.size() > 0 ? 100L : 0L;
                if (!this.needsUpdateNow) {
                    try {
                        this.runLock.wait(j2);
                    } catch (InterruptedException e2) {
                    }
                }
            }
            D3DSurfaceData.D3DWindowSurfaceData[] d3DWindowSurfaceDataArr2 = new D3DSurfaceData.D3DWindowSurfaceData[0];
            synchronized (this) {
                d3DWindowSurfaceDataArr = (D3DSurfaceData.D3DWindowSurfaceData[]) this.d3dwSurfaces.toArray(d3DWindowSurfaceDataArr2);
            }
            for (D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData : d3DWindowSurfaceDataArr) {
                if (d3DWindowSurfaceData.isValid() && (d3DWindowSurfaceData.isDirty() || d3DWindowSurfaceData.isSurfaceLost())) {
                    if (!d3DWindowSurfaceData.isSurfaceLost()) {
                        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
                        d3DRenderQueue.lock();
                        try {
                            Rectangle bounds = d3DWindowSurfaceData.getBounds();
                            D3DSurfaceData.swapBuffers(d3DWindowSurfaceData, 0, 0, bounds.width, bounds.height);
                            d3DWindowSurfaceData.markClean();
                            d3DRenderQueue.unlock();
                        } catch (Throwable th) {
                            d3DRenderQueue.unlock();
                            throw th;
                        }
                    } else if (!validate(d3DWindowSurfaceData)) {
                        d3DWindowSurfaceData.getPeer().replaceSurfaceDataLater();
                    }
                }
            }
            synchronized (this.runLock) {
                this.needsUpdateNow = false;
                this.runLock.notifyAll();
            }
        }
    }

    private boolean validate(D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData) {
        if (d3DWindowSurfaceData.isSurfaceLost()) {
            try {
                d3DWindowSurfaceData.restoreSurface();
                Color backgroundNoSync = d3DWindowSurfaceData.getPeer().getBackgroundNoSync();
                SunGraphics2D sunGraphics2D = new SunGraphics2D(d3DWindowSurfaceData, backgroundNoSync, backgroundNoSync, null);
                sunGraphics2D.fillRect(0, 0, d3DWindowSurfaceData.getBounds().width, d3DWindowSurfaceData.getBounds().height);
                sunGraphics2D.dispose();
                d3DWindowSurfaceData.markClean();
                repaintPeerTarget(d3DWindowSurfaceData.getPeer());
                return true;
            } catch (InvalidPipeException e2) {
                return false;
            }
        }
        return true;
    }

    private synchronized SurfaceData getGdiSurface(D3DSurfaceData.D3DWindowSurfaceData d3DWindowSurfaceData) {
        if (this.gdiSurfaces == null) {
            this.gdiSurfaces = new HashMap<>();
        }
        GDIWindowSurfaceData gDIWindowSurfaceDataCreateData = this.gdiSurfaces.get(d3DWindowSurfaceData);
        if (gDIWindowSurfaceDataCreateData == null) {
            gDIWindowSurfaceDataCreateData = GDIWindowSurfaceData.createData(d3DWindowSurfaceData.getPeer());
            this.gdiSurfaces.put(d3DWindowSurfaceData, gDIWindowSurfaceDataCreateData);
        }
        return gDIWindowSurfaceDataCreateData;
    }

    private static boolean hasHWChildren(Component component) {
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                if ((component2.getPeer() instanceof WComponentPeer) || hasHWChildren(component2)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
