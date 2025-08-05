package sun.java2d.d3d;

import java.awt.AlphaComposite;
import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import sun.awt.SunToolkit;
import sun.awt.image.DataBufferNative;
import sun.awt.image.PixelConverter;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.image.WritableRasterNative;
import sun.awt.windows.WComponentPeer;
import sun.java2d.InvalidPipeException;
import sun.java2d.ScreenUpdateManager;
import sun.java2d.StateTracker;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.MaskFill;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.BufferedContext;
import sun.java2d.pipe.ParallelogramPipe;
import sun.java2d.pipe.PixelToParallelogramConverter;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.TextPipe;
import sun.java2d.pipe.hw.AccelSurface;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities;

/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceData.class */
public class D3DSurfaceData extends SurfaceData implements AccelSurface {
    public static final int D3D_DEVICE_RESOURCE = 100;
    public static final int ST_INT_ARGB = 0;
    public static final int ST_INT_ARGB_PRE = 1;
    public static final int ST_INT_ARGB_BM = 2;
    public static final int ST_INT_RGB = 3;
    public static final int ST_INT_BGR = 4;
    public static final int ST_USHORT_565_RGB = 5;
    public static final int ST_USHORT_555_RGB = 6;
    public static final int ST_BYTE_INDEXED = 7;
    public static final int ST_BYTE_INDEXED_BM = 8;
    public static final int ST_3BYTE_BGR = 9;
    public static final int SWAP_DISCARD = 1;
    public static final int SWAP_FLIP = 2;
    public static final int SWAP_COPY = 3;
    private int type;
    private int width;
    private int height;
    private int nativeWidth;
    private int nativeHeight;
    protected WComponentPeer peer;
    private Image offscreenImage;
    protected D3DGraphicsDevice graphicsDevice;
    private int swapEffect;
    private ExtendedBufferCapabilities.VSyncType syncType;
    private int backBuffersNum;
    private WritableRasterNative wrn;
    protected static D3DRenderer d3dRenderPipe;
    protected static PixelToParallelogramConverter d3dTxRenderPipe;
    protected static ParallelogramPipe d3dAAPgramPipe;
    protected static D3DTextRenderer d3dTextPipe;
    protected static D3DDrawImage d3dImagePipe;
    private static final String DESC_D3D_SURFACE = "D3D Surface";
    static final SurfaceType D3DSurface = SurfaceType.Any.deriveSubType(DESC_D3D_SURFACE, PixelConverter.ArgbPre.instance);
    private static final String DESC_D3D_SURFACE_RTT = "D3D Surface (render-to-texture)";
    static final SurfaceType D3DSurfaceRTT = D3DSurface.deriveSubType(DESC_D3D_SURFACE_RTT);
    private static final String DESC_D3D_TEXTURE = "D3D Texture";
    static final SurfaceType D3DTexture = SurfaceType.Any.deriveSubType(DESC_D3D_TEXTURE);

    private native boolean initTexture(long j2, boolean z2, boolean z3);

    private native boolean initFlipBackbuffer(long j2, long j3, int i2, int i3, int i4);

    private native boolean initRTSurface(long j2, boolean z2);

    private native void initOps(int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int dbGetPixelNative(long j2, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dbSetPixelNative(long j2, int i2, int i3, int i4);

    private static native long getNativeResourceNative(long j2, int i2);

    public static native boolean updateWindowAccelImpl(long j2, long j3, int i2, int i3);

    static {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3dImagePipe = new D3DDrawImage();
        d3dTextPipe = new D3DTextRenderer(d3DRenderQueue);
        d3dRenderPipe = new D3DRenderer(d3DRenderQueue);
        if (GraphicsPrimitive.tracingEnabled()) {
            d3dTextPipe = d3dTextPipe.traceWrap();
            d3dRenderPipe = d3dRenderPipe.traceWrap();
        }
        d3dAAPgramPipe = d3dRenderPipe.getAAParallelogramPipe();
        d3dTxRenderPipe = new PixelToParallelogramConverter(d3dRenderPipe, d3dRenderPipe, 1.0d, 0.25d, true);
        D3DBlitLoops.register();
        D3DMaskFill.register();
        D3DMaskBlit.register();
    }

    protected D3DSurfaceData(WComponentPeer wComponentPeer, D3DGraphicsConfig d3DGraphicsConfig, int i2, int i3, Image image, ColorModel colorModel, int i4, int i5, ExtendedBufferCapabilities.VSyncType vSyncType, int i6) {
        super(getCustomSurfaceType(i6), colorModel);
        this.graphicsDevice = d3DGraphicsConfig.getD3DDevice();
        this.peer = wComponentPeer;
        this.type = i6;
        this.width = i2;
        this.height = i3;
        this.offscreenImage = image;
        this.backBuffersNum = i4;
        this.swapEffect = i5;
        this.syncType = vSyncType;
        initOps(this.graphicsDevice.getScreen(), i2, i3);
        if (i6 == 1) {
            setSurfaceLost(true);
        } else {
            initSurface();
        }
        setBlitProxyKey(d3DGraphicsConfig.getProxyKey());
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceDataProxy makeProxyFor(SurfaceData surfaceData) {
        return D3DSurfaceDataProxy.createProxy(surfaceData, (D3DGraphicsConfig) this.graphicsDevice.getDefaultConfiguration());
    }

    public static D3DSurfaceData createData(WComponentPeer wComponentPeer, Image image) {
        int i2;
        D3DGraphicsConfig gc = getGC(wComponentPeer);
        if (gc == null || !wComponentPeer.isAccelCapable()) {
            return null;
        }
        BufferCapabilities backBufferCaps = wComponentPeer.getBackBufferCaps();
        ExtendedBufferCapabilities.VSyncType vSync = ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT;
        if (backBufferCaps instanceof ExtendedBufferCapabilities) {
            vSync = ((ExtendedBufferCapabilities) backBufferCaps).getVSync();
        }
        Rectangle bounds = wComponentPeer.getBounds();
        BufferCapabilities.FlipContents flipContents = backBufferCaps.getFlipContents();
        if (flipContents == BufferCapabilities.FlipContents.COPIED) {
            i2 = 3;
        } else if (flipContents == BufferCapabilities.FlipContents.PRIOR) {
            i2 = 2;
        } else {
            i2 = 1;
        }
        return new D3DSurfaceData(wComponentPeer, gc, bounds.width, bounds.height, image, wComponentPeer.getColorModel(), wComponentPeer.getBackBuffersNum(), i2, vSync, 4);
    }

    public static D3DSurfaceData createData(WComponentPeer wComponentPeer) {
        D3DGraphicsConfig gc = getGC(wComponentPeer);
        if (gc == null || !wComponentPeer.isAccelCapable()) {
            return null;
        }
        return new D3DWindowSurfaceData(wComponentPeer, gc);
    }

    public static D3DSurfaceData createData(D3DGraphicsConfig d3DGraphicsConfig, int i2, int i3, ColorModel colorModel, Image image, int i4) {
        if (i4 == 5) {
            if (!d3DGraphicsConfig.getD3DDevice().isCapPresent(colorModel.getTransparency() == 1 ? 8 : 4)) {
                i4 = 2;
            }
        }
        D3DSurfaceData d3DSurfaceData = null;
        try {
            d3DSurfaceData = new D3DSurfaceData(null, d3DGraphicsConfig, i2, i3, image, colorModel, 0, 1, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, i4);
        } catch (InvalidPipeException e2) {
            if (i4 == 5 && ((SunVolatileImage) image).getForcedAccelSurfaceType() != 5) {
                d3DSurfaceData = new D3DSurfaceData(null, d3DGraphicsConfig, i2, i3, image, colorModel, 0, 1, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, 2);
            }
        }
        return d3DSurfaceData;
    }

    private static SurfaceType getCustomSurfaceType(int i2) {
        switch (i2) {
            case 3:
                return D3DTexture;
            case 5:
                return D3DSurfaceRTT;
            default:
                return D3DSurface;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean initSurfaceNow() {
        boolean z2 = getTransparency() == 1;
        switch (this.type) {
            case 1:
            case 4:
                return initFlipBackbuffer(getNativeOps(), this.peer.getData(), this.backBuffersNum, this.swapEffect, this.syncType.id());
            case 2:
                return initRTSurface(getNativeOps(), z2);
            case 3:
                return initTexture(getNativeOps(), false, z2);
            case 5:
                return initTexture(getNativeOps(), true, z2);
            default:
                return false;
        }
    }

    protected void initSurface() {
        synchronized (this) {
            this.wrn = null;
        }
        final C1Status c1Status = new C1Status();
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DSurfaceData.1
                @Override // java.lang.Runnable
                public void run() {
                    c1Status.success = D3DSurfaceData.this.initSurfaceNow();
                }
            });
            if (!c1Status.success) {
                throw new InvalidPipeException("Error creating D3DSurface");
            }
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    /* renamed from: sun.java2d.d3d.D3DSurfaceData$1Status, reason: invalid class name */
    /* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceData$1Status.class */
    class C1Status {
        boolean success = false;

        C1Status() {
        }
    }

    @Override // sun.java2d.pipe.hw.BufferedContextProvider
    public final D3DContext getContext() {
        return this.graphicsDevice.getContext();
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public final int getType() {
        return this.type;
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceData$D3DDataBufferNative.class */
    static class D3DDataBufferNative extends DataBufferNative {
        int pixel;

        protected D3DDataBufferNative(SurfaceData surfaceData, int i2, int i3, int i4) {
            super(surfaceData, i2, i3, i4);
        }

        @Override // sun.awt.image.DataBufferNative
        protected int getElem(final int i2, final int i3, final SurfaceData surfaceData) {
            if (surfaceData.isSurfaceLost()) {
                return 0;
            }
            D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
            d3DRenderQueue.lock();
            try {
                d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DSurfaceData.D3DDataBufferNative.1
                    @Override // java.lang.Runnable
                    public void run() {
                        D3DDataBufferNative.this.pixel = D3DSurfaceData.dbGetPixelNative(surfaceData.getNativeOps(), i2, i3);
                    }
                });
                int i4 = this.pixel;
                d3DRenderQueue.unlock();
                return i4;
            } catch (Throwable th) {
                int i5 = this.pixel;
                d3DRenderQueue.unlock();
                throw th;
            }
        }

        @Override // sun.awt.image.DataBufferNative
        protected void setElem(final int i2, final int i3, final int i4, final SurfaceData surfaceData) {
            if (surfaceData.isSurfaceLost()) {
                return;
            }
            D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
            d3DRenderQueue.lock();
            try {
                d3DRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.d3d.D3DSurfaceData.D3DDataBufferNative.2
                    @Override // java.lang.Runnable
                    public void run() {
                        D3DSurfaceData.dbSetPixelNative(surfaceData.getNativeOps(), i2, i3, i4);
                    }
                });
                surfaceData.markDirty();
                d3DRenderQueue.unlock();
            } catch (Throwable th) {
                d3DRenderQueue.unlock();
                throw th;
            }
        }
    }

    @Override // sun.java2d.SurfaceData
    public synchronized Raster getRaster(int i2, int i3, int i4, int i5) {
        int i6;
        if (this.wrn == null) {
            DirectColorModel directColorModel = (DirectColorModel) getColorModel();
            int i7 = this.width;
            if (directColorModel.getPixelSize() > 16) {
                i6 = 3;
            } else {
                i6 = 1;
            }
            this.wrn = WritableRasterNative.createNativeRaster(new SinglePixelPackedSampleModel(i6, this.width, this.height, i7, directColorModel.getMasks()), new D3DDataBufferNative(this, i6, this.width, this.height));
        }
        return this.wrn;
    }

    @Override // sun.java2d.SurfaceData
    public boolean canRenderLCDText(SunGraphics2D sunGraphics2D) {
        return this.graphicsDevice.isCapPresent(65536) && sunGraphics2D.compositeState <= 0 && sunGraphics2D.paintState <= 0 && sunGraphics2D.surfaceData.getTransparency() == 1;
    }

    void disableAccelerationForSurface() {
        if (this.offscreenImage != null) {
            SurfaceManager manager = SurfaceManager.getManager(this.offscreenImage);
            if (manager instanceof D3DVolatileSurfaceManager) {
                setSurfaceLost(true);
                ((D3DVolatileSurfaceManager) manager).setAccelerationEnabled(false);
            }
        }
    }

    @Override // sun.java2d.SurfaceData
    public void validatePipe(SunGraphics2D sunGraphics2D) {
        TextPipe textPipe;
        boolean z2 = false;
        if (sunGraphics2D.compositeState >= 2) {
            super.validatePipe(sunGraphics2D);
            sunGraphics2D.imagepipe = d3dImagePipe;
            disableAccelerationForSurface();
            return;
        }
        if ((sunGraphics2D.compositeState <= 0 && sunGraphics2D.paintState <= 1) || ((sunGraphics2D.compositeState == 1 && sunGraphics2D.paintState <= 1 && ((AlphaComposite) sunGraphics2D.composite).getRule() == 3) || (sunGraphics2D.compositeState == 2 && sunGraphics2D.paintState <= 1))) {
            textPipe = d3dTextPipe;
        } else {
            super.validatePipe(sunGraphics2D);
            textPipe = sunGraphics2D.textpipe;
            z2 = true;
        }
        PixelToParallelogramConverter pixelToParallelogramConverter = null;
        D3DRenderer d3DRenderer = null;
        if (sunGraphics2D.antialiasHint != 2) {
            if (sunGraphics2D.paintState <= 1) {
                if (sunGraphics2D.compositeState <= 2) {
                    pixelToParallelogramConverter = d3dTxRenderPipe;
                    d3DRenderer = d3dRenderPipe;
                }
            } else if (sunGraphics2D.compositeState <= 1 && D3DPaints.isValid(sunGraphics2D)) {
                pixelToParallelogramConverter = d3dTxRenderPipe;
                d3DRenderer = d3dRenderPipe;
            }
        } else if (sunGraphics2D.paintState <= 1) {
            if (this.graphicsDevice.isCapPresent(524288) && (sunGraphics2D.imageComp == CompositeType.SrcOverNoEa || sunGraphics2D.imageComp == CompositeType.SrcOver)) {
                if (!z2) {
                    super.validatePipe(sunGraphics2D);
                    z2 = true;
                }
                PixelToParallelogramConverter pixelToParallelogramConverter2 = new PixelToParallelogramConverter(sunGraphics2D.shapepipe, d3dAAPgramPipe, 0.125d, 0.499d, false);
                sunGraphics2D.drawpipe = pixelToParallelogramConverter2;
                sunGraphics2D.fillpipe = pixelToParallelogramConverter2;
                sunGraphics2D.shapepipe = pixelToParallelogramConverter2;
            } else if (sunGraphics2D.compositeState == 2) {
                pixelToParallelogramConverter = d3dTxRenderPipe;
                d3DRenderer = d3dRenderPipe;
            }
        }
        if (pixelToParallelogramConverter != null) {
            if (sunGraphics2D.transformState >= 3) {
                sunGraphics2D.drawpipe = pixelToParallelogramConverter;
                sunGraphics2D.fillpipe = pixelToParallelogramConverter;
            } else if (sunGraphics2D.strokeState != 0) {
                sunGraphics2D.drawpipe = pixelToParallelogramConverter;
                sunGraphics2D.fillpipe = d3DRenderer;
            } else {
                sunGraphics2D.drawpipe = d3DRenderer;
                sunGraphics2D.fillpipe = d3DRenderer;
            }
            sunGraphics2D.shapepipe = pixelToParallelogramConverter;
        } else if (!z2) {
            super.validatePipe(sunGraphics2D);
        }
        sunGraphics2D.textpipe = textPipe;
        sunGraphics2D.imagepipe = d3dImagePipe;
    }

    @Override // sun.java2d.SurfaceData
    protected MaskFill getMaskFill(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.paintState > 1 && (!D3DPaints.isValid(sunGraphics2D) || !this.graphicsDevice.isCapPresent(16))) {
            return null;
        }
        return super.getMaskFill(sunGraphics2D);
    }

    @Override // sun.java2d.SurfaceData
    public boolean copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (sunGraphics2D.transformState < 3 && sunGraphics2D.compositeState < 2) {
            d3dRenderPipe.copyArea(sunGraphics2D, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
            return true;
        }
        return false;
    }

    @Override // sun.java2d.SurfaceData
    public void flush() {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            RenderBuffer buffer = d3DRenderQueue.getBuffer();
            d3DRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(72);
            buffer.putLong(getNativeOps());
            d3DRenderQueue.flushNow();
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    static void dispose(long j2) {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            RenderBuffer buffer = d3DRenderQueue.getBuffer();
            d3DRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(73);
            buffer.putLong(j2);
            d3DRenderQueue.flushNow();
            d3DRenderQueue.unlock();
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    static void swapBuffers(D3DSurfaceData d3DSurfaceData, final int i2, final int i3, final int i4, final int i5) {
        long nativeOps = d3DSurfaceData.getNativeOps();
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        if (D3DRenderQueue.isRenderQueueThread()) {
            if (!d3DRenderQueue.tryLock()) {
                final Component component = (Component) d3DSurfaceData.getPeer().getTarget();
                SunToolkit.executeOnEventHandlerThread(component, new Runnable() { // from class: sun.java2d.d3d.D3DSurfaceData.2
                    @Override // java.lang.Runnable
                    public void run() {
                        component.repaint(i2, i3, i4, i5);
                    }
                });
                return;
            }
        } else {
            d3DRenderQueue.lock();
        }
        try {
            RenderBuffer buffer = d3DRenderQueue.getBuffer();
            d3DRenderQueue.ensureCapacityAndAlignment(28, 4);
            buffer.putInt(80);
            buffer.putLong(nativeOps);
            buffer.putInt(i2);
            buffer.putInt(i3);
            buffer.putInt(i4);
            buffer.putInt(i5);
            d3DRenderQueue.flushNow();
            d3DRenderQueue.unlock();
        } catch (Throwable th) {
            d3DRenderQueue.unlock();
            throw th;
        }
    }

    @Override // sun.java2d.SurfaceData
    public Object getDestination() {
        return this.offscreenImage;
    }

    @Override // sun.java2d.SurfaceData
    public Rectangle getBounds() {
        if (this.type == 4 || this.type == 1) {
            Rectangle bounds = this.peer.getBounds();
            bounds.f12373y = 0;
            bounds.f12372x = 0;
            return bounds;
        }
        return new Rectangle(this.width, this.height);
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public Rectangle getNativeBounds() {
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            return new Rectangle(this.nativeWidth, this.nativeHeight);
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    @Override // sun.java2d.SurfaceData
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.graphicsDevice.getDefaultConfiguration();
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceData getReplacement() {
        return restoreContents(this.offscreenImage);
    }

    private static D3DGraphicsConfig getGC(WComponentPeer wComponentPeer) {
        GraphicsConfiguration defaultConfiguration;
        if (wComponentPeer != null) {
            defaultConfiguration = wComponentPeer.getGraphicsConfiguration();
        } else {
            defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        if (defaultConfiguration instanceof D3DGraphicsConfig) {
            return (D3DGraphicsConfig) defaultConfiguration;
        }
        return null;
    }

    void restoreSurface() {
        initSurface();
    }

    WComponentPeer getPeer() {
        return this.peer;
    }

    @Override // sun.java2d.SurfaceData
    public void setSurfaceLost(boolean z2) {
        super.setSurfaceLost(z2);
        if (z2 && this.offscreenImage != null) {
            SurfaceManager.getManager(this.offscreenImage).acceleratedSurfaceLost();
        }
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public long getNativeResource(int i2) {
        return getNativeResourceNative(getNativeOps(), i2);
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceData$D3DWindowSurfaceData.class */
    public static class D3DWindowSurfaceData extends D3DSurfaceData {
        StateTracker dirtyTracker;

        @Override // sun.java2d.d3d.D3DSurfaceData, sun.java2d.pipe.hw.BufferedContextProvider
        public /* bridge */ /* synthetic */ BufferedContext getContext() {
            return super.getContext();
        }

        public D3DWindowSurfaceData(WComponentPeer wComponentPeer, D3DGraphicsConfig d3DGraphicsConfig) {
            super(wComponentPeer, d3DGraphicsConfig, wComponentPeer.getBounds().width, wComponentPeer.getBounds().height, null, wComponentPeer.getColorModel(), 1, 3, ExtendedBufferCapabilities.VSyncType.VSYNC_DEFAULT, 1);
            this.dirtyTracker = getStateTracker();
        }

        @Override // sun.java2d.d3d.D3DSurfaceData, sun.java2d.SurfaceData
        public SurfaceData getReplacement() {
            return ScreenUpdateManager.getInstance().getReplacementScreenSurface(this.peer, this);
        }

        @Override // sun.java2d.d3d.D3DSurfaceData, sun.java2d.SurfaceData
        public Object getDestination() {
            return this.peer.getTarget();
        }

        @Override // sun.java2d.d3d.D3DSurfaceData
        void disableAccelerationForSurface() {
            setSurfaceLost(true);
            invalidate();
            flush();
            this.peer.disableAcceleration();
            ScreenUpdateManager.getInstance().dropScreenSurface(this);
        }

        @Override // sun.java2d.d3d.D3DSurfaceData
        void restoreSurface() {
            if (!this.peer.isAccelCapable()) {
                throw new InvalidPipeException("Onscreen acceleration disabled for this surface");
            }
            Window fullScreenWindow = this.graphicsDevice.getFullScreenWindow();
            if (fullScreenWindow != null && fullScreenWindow != this.peer.getTarget()) {
                throw new InvalidPipeException("Can't restore onscreen surface when in full-screen mode");
            }
            super.restoreSurface();
            setSurfaceLost(false);
            D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
            d3DRenderQueue.lock();
            try {
                getContext().invalidateContext();
            } finally {
                d3DRenderQueue.unlock();
            }
        }

        public boolean isDirty() {
            return !this.dirtyTracker.isCurrent();
        }

        public void markClean() {
            this.dirtyTracker = getStateTracker();
        }
    }
}
