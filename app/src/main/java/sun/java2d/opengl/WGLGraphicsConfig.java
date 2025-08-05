package sun.java2d.opengl;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.ImageCapabilities;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import sun.awt.Win32GraphicsConfig;
import sun.awt.Win32GraphicsDevice;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.windows.WComponentPeer;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.java2d.SunGraphics2D;
import sun.java2d.Surface;
import sun.java2d.SurfaceData;
import sun.java2d.opengl.OGLContext;
import sun.java2d.opengl.WGLSurfaceData;
import sun.java2d.pipe.hw.AccelDeviceEventListener;
import sun.java2d.pipe.hw.AccelDeviceEventNotifier;
import sun.java2d.pipe.hw.AccelSurface;
import sun.java2d.pipe.hw.AccelTypedVolatileImage;
import sun.java2d.pipe.hw.ContextCapabilities;
import sun.java2d.windows.GDIWindowSurfaceData;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/java2d/opengl/WGLGraphicsConfig.class */
public class WGLGraphicsConfig extends Win32GraphicsConfig implements OGLGraphicsConfig {
    private BufferCapabilities bufferCaps;
    private long pConfigInfo;
    private ContextCapabilities oglCaps;
    private OGLContext context;
    private Object disposerReferent;
    private static ImageCapabilities imageCaps = new WGLImageCaps();
    protected static boolean wglAvailable = initWGL();

    public static native int getDefaultPixFmt(int i2);

    private static native boolean initWGL();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getWGLConfigInfo(int i2, int i3);

    private static native int getOGLCapabilities(long j2);

    protected WGLGraphicsConfig(Win32GraphicsDevice win32GraphicsDevice, int i2, long j2, ContextCapabilities contextCapabilities) {
        super(win32GraphicsDevice, i2);
        this.disposerReferent = new Object();
        this.pConfigInfo = j2;
        this.oglCaps = contextCapabilities;
        this.context = new OGLContext(OGLRenderQueue.getInstance(), this);
        Disposer.addRecord(this.disposerReferent, new WGLGCDisposerRecord(this.pConfigInfo, win32GraphicsDevice.getScreen()));
    }

    @Override // sun.awt.Win32GraphicsConfig, sun.awt.image.SurfaceManager.ProxiedGraphicsConfig
    public Object getProxyKey() {
        return this;
    }

    @Override // sun.java2d.opengl.OGLGraphicsConfig
    public SurfaceData createManagedSurface(int i2, int i3, int i4) {
        return WGLSurfaceData.createData(this, i2, i3, getColorModel(i4), null, 3);
    }

    public static WGLGraphicsConfig getConfig(Win32GraphicsDevice win32GraphicsDevice, int i2) {
        if (!wglAvailable) {
            return null;
        }
        final String[] strArr = new String[1];
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.invalidateCurrentContext();
            WGLGetConfigInfo wGLGetConfigInfo = new WGLGetConfigInfo(win32GraphicsDevice.getScreen(), i2);
            oGLRenderQueue.flushAndInvokeNow(wGLGetConfigInfo);
            long configInfo = wGLGetConfigInfo.getConfigInfo();
            if (configInfo != 0) {
                OGLContext.setScratchSurface(configInfo);
                oGLRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.opengl.WGLGraphicsConfig.1
                    @Override // java.lang.Runnable
                    public void run() {
                        strArr[0] = OGLContext.getOGLIdString();
                    }
                });
            }
            if (configInfo == 0) {
                return null;
            }
            return new WGLGraphicsConfig(win32GraphicsDevice, i2, configInfo, new OGLContext.OGLContextCaps(getOGLCapabilities(configInfo), strArr[0]));
        } finally {
            oGLRenderQueue.unlock();
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLGraphicsConfig$WGLGetConfigInfo.class */
    private static class WGLGetConfigInfo implements Runnable {
        private int screen;
        private int pixfmt;
        private long cfginfo;

        private WGLGetConfigInfo(int i2, int i3) {
            this.screen = i2;
            this.pixfmt = i3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.cfginfo = WGLGraphicsConfig.getWGLConfigInfo(this.screen, this.pixfmt);
        }

        public long getConfigInfo() {
            return this.cfginfo;
        }
    }

    public static boolean isWGLAvailable() {
        return wglAvailable;
    }

    @Override // sun.java2d.opengl.OGLGraphicsConfig
    public final boolean isCapPresent(int i2) {
        return (this.oglCaps.getCaps() & i2) != 0;
    }

    @Override // sun.java2d.opengl.OGLGraphicsConfig
    public final long getNativeConfigInfo() {
        return this.pConfigInfo;
    }

    @Override // sun.java2d.opengl.OGLGraphicsConfig, sun.java2d.pipe.hw.BufferedContextProvider
    public final OGLContext getContext() {
        return this.context;
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLGraphicsConfig$WGLGCDisposerRecord.class */
    private static class WGLGCDisposerRecord implements DisposerRecord {
        private long pCfgInfo;
        private int screen;

        public WGLGCDisposerRecord(long j2, int i2) {
            this.pCfgInfo = j2;
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
            oGLRenderQueue.lock();
            try {
                oGLRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.opengl.WGLGraphicsConfig.WGLGCDisposerRecord.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AccelDeviceEventNotifier.eventOccured(WGLGCDisposerRecord.this.screen, 0);
                        AccelDeviceEventNotifier.eventOccured(WGLGCDisposerRecord.this.screen, 1);
                    }
                });
                if (this.pCfgInfo != 0) {
                    OGLRenderQueue.disposeGraphicsConfig(this.pCfgInfo);
                    this.pCfgInfo = 0L;
                }
            } finally {
                oGLRenderQueue.unlock();
            }
        }
    }

    @Override // sun.awt.Win32GraphicsConfig, sun.awt.DisplayChangedListener
    public synchronized void displayChanged() {
        super.displayChanged();
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.invalidateCurrentContext();
        } finally {
            oGLRenderQueue.unlock();
        }
    }

    @Override // sun.awt.Win32GraphicsConfig, java.awt.GraphicsConfiguration
    public ColorModel getColorModel(int i2) {
        switch (i2) {
            case 1:
                return new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
            case 2:
                return new DirectColorModel(25, 16711680, NormalizerImpl.CC_MASK, 255, 16777216);
            case 3:
                return new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, true, 3);
            default:
                return null;
        }
    }

    @Override // sun.awt.Win32GraphicsConfig
    public String toString() {
        return "WGLGraphicsConfig[dev=" + ((Object) this.screen) + ",pixfmt=" + this.visual + "]";
    }

    @Override // sun.awt.Win32GraphicsConfig
    public SurfaceData createSurfaceData(WComponentPeer wComponentPeer, int i2) {
        SurfaceData surfaceDataCreateData = WGLSurfaceData.createData(wComponentPeer);
        if (surfaceDataCreateData == null) {
            surfaceDataCreateData = GDIWindowSurfaceData.createData(wComponentPeer);
        }
        return surfaceDataCreateData;
    }

    @Override // sun.awt.Win32GraphicsConfig
    public void assertOperationSupported(Component component, int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        if (i2 > 2) {
            throw new AWTException("Only double or single buffering is supported");
        }
        if (!getBufferCapabilities().isPageFlipping()) {
            throw new AWTException("Page flipping is not supported");
        }
        if (bufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.PRIOR) {
            throw new AWTException("FlipContents.PRIOR is not supported");
        }
    }

    @Override // sun.awt.Win32GraphicsConfig
    public VolatileImage createBackBuffer(WComponentPeer wComponentPeer) {
        Component component = (Component) wComponentPeer.getTarget();
        return new SunVolatileImage(component, Math.max(1, component.getWidth()), Math.max(1, component.getHeight()), Boolean.TRUE);
    }

    @Override // sun.awt.Win32GraphicsConfig
    public void flip(WComponentPeer wComponentPeer, Component component, VolatileImage volatileImage, int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
        if (flipContents == BufferCapabilities.FlipContents.COPIED) {
            SurfaceData primarySurfaceData = SurfaceManager.getManager(volatileImage).getPrimarySurfaceData();
            if (primarySurfaceData instanceof WGLSurfaceData.WGLVSyncOffScreenSurfaceData) {
                SunGraphics2D sunGraphics2D = new SunGraphics2D(((WGLSurfaceData.WGLVSyncOffScreenSurfaceData) primarySurfaceData).getFlipSurface(), Color.black, Color.white, null);
                try {
                    sunGraphics2D.drawImage(volatileImage, 0, 0, (ImageObserver) null);
                    sunGraphics2D.dispose();
                } catch (Throwable th) {
                    sunGraphics2D.dispose();
                    throw th;
                }
            } else {
                Graphics graphics = wComponentPeer.getGraphics();
                try {
                    graphics.drawImage(volatileImage, i2, i3, i4, i5, i2, i3, i4, i5, null);
                    graphics.dispose();
                    return;
                } catch (Throwable th2) {
                    graphics.dispose();
                    throw th2;
                }
            }
        } else if (flipContents == BufferCapabilities.FlipContents.PRIOR) {
            return;
        }
        OGLSurfaceData.swapBuffers(wComponentPeer.getData());
        if (flipContents == BufferCapabilities.FlipContents.BACKGROUND) {
            Graphics graphics2 = volatileImage.getGraphics();
            try {
                graphics2.setColor(component.getBackground());
                graphics2.fillRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());
                graphics2.dispose();
            } catch (Throwable th3) {
                graphics2.dispose();
                throw th3;
            }
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLGraphicsConfig$WGLBufferCaps.class */
    private static class WGLBufferCaps extends BufferCapabilities {
        public WGLBufferCaps(boolean z2) {
            super(WGLGraphicsConfig.imageCaps, WGLGraphicsConfig.imageCaps, z2 ? BufferCapabilities.FlipContents.UNDEFINED : null);
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public BufferCapabilities getBufferCapabilities() {
        if (this.bufferCaps == null) {
            this.bufferCaps = new WGLBufferCaps(isCapPresent(65536));
        }
        return this.bufferCaps;
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLGraphicsConfig$WGLImageCaps.class */
    private static class WGLImageCaps extends ImageCapabilities {
        private WGLImageCaps() {
            super(true);
        }

        @Override // java.awt.ImageCapabilities
        public boolean isTrueVolatile() {
            return true;
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public ImageCapabilities getImageCapabilities() {
        return imageCaps;
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public VolatileImage createCompatibleVolatileImage(int i2, int i3, int i4, int i5) {
        if ((i5 == 5 || i5 == 3) && i4 != 2) {
            if (i5 == 5 && !isCapPresent(12)) {
                return null;
            }
            AccelTypedVolatileImage accelTypedVolatileImage = new AccelTypedVolatileImage(this, i2, i3, i4, i5);
            Surface destSurface = accelTypedVolatileImage.getDestSurface();
            if (!(destSurface instanceof AccelSurface) || ((AccelSurface) destSurface).getType() != i5) {
                accelTypedVolatileImage.flush();
                accelTypedVolatileImage = null;
            }
            return accelTypedVolatileImage;
        }
        return null;
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public ContextCapabilities getContextCapabilities() {
        return this.oglCaps;
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public void addDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener) {
        AccelDeviceEventNotifier.addListener(accelDeviceEventListener, this.screen.getScreen());
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public void removeDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener) {
        AccelDeviceEventNotifier.removeListener(accelDeviceEventListener);
    }
}
