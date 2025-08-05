package sun.java2d.opengl;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.security.AccessController;
import sun.awt.image.PixelConverter;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.MaskFill;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.ParallelogramPipe;
import sun.java2d.pipe.PixelToParallelogramConverter;
import sun.java2d.pipe.RenderBuffer;
import sun.java2d.pipe.TextPipe;
import sun.java2d.pipe.hw.AccelSurface;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/opengl/OGLSurfaceData.class */
public abstract class OGLSurfaceData extends SurfaceData implements AccelSurface {
    public static final int FBOBJECT = 5;
    public static final int PF_INT_ARGB = 0;
    public static final int PF_INT_ARGB_PRE = 1;
    public static final int PF_INT_RGB = 2;
    public static final int PF_INT_RGBX = 3;
    public static final int PF_INT_BGR = 4;
    public static final int PF_INT_BGRX = 5;
    public static final int PF_USHORT_565_RGB = 6;
    public static final int PF_USHORT_555_RGB = 7;
    public static final int PF_USHORT_555_RGBX = 8;
    public static final int PF_BYTE_GRAY = 9;
    public static final int PF_USHORT_GRAY = 10;
    public static final int PF_3BYTE_BGR = 11;
    private static boolean isFBObjectEnabled;
    private static boolean isLCDShaderEnabled;
    private static boolean isBIOpShaderEnabled;
    private static boolean isGradShaderEnabled;
    private OGLGraphicsConfig graphicsConfig;
    protected int type;
    private int nativeWidth;
    private int nativeHeight;
    protected static OGLRenderer oglRenderPipe;
    protected static PixelToParallelogramConverter oglTxRenderPipe;
    protected static ParallelogramPipe oglAAPgramPipe;
    protected static OGLTextRenderer oglTextPipe;
    protected static OGLDrawImage oglImagePipe;
    private static final String DESC_OPENGL_SURFACE = "OpenGL Surface";
    static final SurfaceType OpenGLSurface = SurfaceType.Any.deriveSubType(DESC_OPENGL_SURFACE, PixelConverter.ArgbPre.instance);
    private static final String DESC_OPENGL_SURFACE_RTT = "OpenGL Surface (render-to-texture)";
    static final SurfaceType OpenGLSurfaceRTT = OpenGLSurface.deriveSubType(DESC_OPENGL_SURFACE_RTT);
    private static final String DESC_OPENGL_TEXTURE = "OpenGL Texture";
    static final SurfaceType OpenGLTexture = SurfaceType.Any.deriveSubType(DESC_OPENGL_TEXTURE);

    protected native boolean initTexture(long j2, boolean z2, boolean z3, boolean z4, int i2, int i3);

    protected native boolean initFBObject(long j2, boolean z2, boolean z3, boolean z4, int i2, int i3);

    protected native boolean initFlipBackbuffer(long j2);

    private native int getTextureTarget(long j2);

    private native int getTextureID(long j2);

    static {
        if (!GraphicsEnvironment.isHeadless()) {
            isFBObjectEnabled = !"false".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.fbobject")));
            isLCDShaderEnabled = !"false".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.lcdshader")));
            isBIOpShaderEnabled = !"false".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.biopshader")));
            isGradShaderEnabled = !"false".equals((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.opengl.gradshader")));
            OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
            oglImagePipe = new OGLDrawImage();
            oglTextPipe = new OGLTextRenderer(oGLRenderQueue);
            oglRenderPipe = new OGLRenderer(oGLRenderQueue);
            if (GraphicsPrimitive.tracingEnabled()) {
                oglTextPipe = oglTextPipe.traceWrap();
            }
            oglAAPgramPipe = oglRenderPipe.getAAParallelogramPipe();
            oglTxRenderPipe = new PixelToParallelogramConverter(oglRenderPipe, oglRenderPipe, 1.0d, 0.25d, true);
            OGLBlitLoops.register();
            OGLMaskFill.register();
            OGLMaskBlit.register();
        }
    }

    protected OGLSurfaceData(OGLGraphicsConfig oGLGraphicsConfig, ColorModel colorModel, int i2) {
        super(getCustomSurfaceType(i2), colorModel);
        this.graphicsConfig = oGLGraphicsConfig;
        this.type = i2;
        setBlitProxyKey(oGLGraphicsConfig.getProxyKey());
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceDataProxy makeProxyFor(SurfaceData surfaceData) {
        return OGLSurfaceDataProxy.createProxy(surfaceData, this.graphicsConfig);
    }

    private static SurfaceType getCustomSurfaceType(int i2) {
        switch (i2) {
            case 3:
                return OpenGLTexture;
            case 5:
                return OpenGLSurfaceRTT;
            default:
                return OpenGLSurface;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initSurfaceNow(int i2, int i3) {
        boolean z2 = getTransparency() == 1;
        boolean zInitFlipBackbuffer = false;
        switch (this.type) {
            case 3:
                zInitFlipBackbuffer = initTexture(getNativeOps(), z2, isTexNonPow2Available(), isTexRectAvailable(), i2, i3);
                break;
            case 4:
                zInitFlipBackbuffer = initFlipBackbuffer(getNativeOps());
                break;
            case 5:
                zInitFlipBackbuffer = initFBObject(getNativeOps(), z2, isTexNonPow2Available(), isTexRectAvailable(), i2, i3);
                break;
        }
        if (!zInitFlipBackbuffer) {
            throw new OutOfMemoryError("can't create offscreen surface");
        }
    }

    protected void initSurface(final int i2, final int i3) {
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            switch (this.type) {
                case 3:
                case 5:
                    OGLContext.setScratchSurface(this.graphicsConfig);
                    break;
            }
            oGLRenderQueue.flushAndInvokeNow(new Runnable() { // from class: sun.java2d.opengl.OGLSurfaceData.1
                @Override // java.lang.Runnable
                public void run() {
                    OGLSurfaceData.this.initSurfaceNow(i2, i3);
                }
            });
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }

    @Override // sun.java2d.pipe.hw.BufferedContextProvider
    public final OGLContext getContext() {
        return this.graphicsConfig.getContext();
    }

    final OGLGraphicsConfig getOGLGraphicsConfig() {
        return this.graphicsConfig;
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public final int getType() {
        return this.type;
    }

    public final int getTextureTarget() {
        return getTextureTarget(getNativeOps());
    }

    public final int getTextureID() {
        return getTextureID(getNativeOps());
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public long getNativeResource(int i2) {
        if (i2 == 3) {
            return getTextureID();
        }
        return 0L;
    }

    @Override // sun.java2d.SurfaceData
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        throw new InternalError("not implemented yet");
    }

    @Override // sun.java2d.SurfaceData
    public boolean canRenderLCDText(SunGraphics2D sunGraphics2D) {
        return this.graphicsConfig.isCapPresent(131072) && sunGraphics2D.surfaceData.getTransparency() == 1 && sunGraphics2D.paintState <= 0 && (sunGraphics2D.compositeState <= 0 || (sunGraphics2D.compositeState <= 1 && canHandleComposite(sunGraphics2D.composite)));
    }

    private boolean canHandleComposite(Composite composite) {
        if (composite instanceof AlphaComposite) {
            AlphaComposite alphaComposite = (AlphaComposite) composite;
            return alphaComposite.getRule() == 3 && alphaComposite.getAlpha() >= 1.0f;
        }
        return false;
    }

    @Override // sun.java2d.SurfaceData
    public void validatePipe(SunGraphics2D sunGraphics2D) {
        TextPipe textPipe;
        boolean z2 = false;
        if ((sunGraphics2D.compositeState <= 0 && sunGraphics2D.paintState <= 1) || ((sunGraphics2D.compositeState == 1 && sunGraphics2D.paintState <= 1 && ((AlphaComposite) sunGraphics2D.composite).getRule() == 3) || (sunGraphics2D.compositeState == 2 && sunGraphics2D.paintState <= 1))) {
            textPipe = oglTextPipe;
        } else {
            super.validatePipe(sunGraphics2D);
            textPipe = sunGraphics2D.textpipe;
            z2 = true;
        }
        PixelToParallelogramConverter pixelToParallelogramConverter = null;
        OGLRenderer oGLRenderer = null;
        if (sunGraphics2D.antialiasHint != 2) {
            if (sunGraphics2D.paintState <= 1) {
                if (sunGraphics2D.compositeState <= 2) {
                    pixelToParallelogramConverter = oglTxRenderPipe;
                    oGLRenderer = oglRenderPipe;
                }
            } else if (sunGraphics2D.compositeState <= 1 && OGLPaints.isValid(sunGraphics2D)) {
                pixelToParallelogramConverter = oglTxRenderPipe;
                oGLRenderer = oglRenderPipe;
            }
        } else if (sunGraphics2D.paintState <= 1) {
            if (this.graphicsConfig.isCapPresent(256) && (sunGraphics2D.imageComp == CompositeType.SrcOverNoEa || sunGraphics2D.imageComp == CompositeType.SrcOver)) {
                if (!z2) {
                    super.validatePipe(sunGraphics2D);
                    z2 = true;
                }
                PixelToParallelogramConverter pixelToParallelogramConverter2 = new PixelToParallelogramConverter(sunGraphics2D.shapepipe, oglAAPgramPipe, 0.125d, 0.499d, false);
                sunGraphics2D.drawpipe = pixelToParallelogramConverter2;
                sunGraphics2D.fillpipe = pixelToParallelogramConverter2;
                sunGraphics2D.shapepipe = pixelToParallelogramConverter2;
            } else if (sunGraphics2D.compositeState == 2) {
                pixelToParallelogramConverter = oglTxRenderPipe;
                oGLRenderer = oglRenderPipe;
            }
        }
        if (pixelToParallelogramConverter != null) {
            if (sunGraphics2D.transformState >= 3) {
                sunGraphics2D.drawpipe = pixelToParallelogramConverter;
                sunGraphics2D.fillpipe = pixelToParallelogramConverter;
            } else if (sunGraphics2D.strokeState != 0) {
                sunGraphics2D.drawpipe = pixelToParallelogramConverter;
                sunGraphics2D.fillpipe = oGLRenderer;
            } else {
                sunGraphics2D.drawpipe = oGLRenderer;
                sunGraphics2D.fillpipe = oGLRenderer;
            }
            sunGraphics2D.shapepipe = pixelToParallelogramConverter;
        } else if (!z2) {
            super.validatePipe(sunGraphics2D);
        }
        sunGraphics2D.textpipe = textPipe;
        sunGraphics2D.imagepipe = oglImagePipe;
    }

    @Override // sun.java2d.SurfaceData
    protected MaskFill getMaskFill(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.paintState > 1 && (!OGLPaints.isValid(sunGraphics2D) || !this.graphicsConfig.isCapPresent(16))) {
            return null;
        }
        return super.getMaskFill(sunGraphics2D);
    }

    @Override // sun.java2d.SurfaceData
    public boolean copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (sunGraphics2D.transformState < 3 && sunGraphics2D.compositeState < 2) {
            oglRenderPipe.copyArea(sunGraphics2D, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
            return true;
        }
        return false;
    }

    @Override // sun.java2d.SurfaceData
    public void flush() {
        invalidate();
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.setScratchSurface(this.graphicsConfig);
            RenderBuffer buffer = oGLRenderQueue.getBuffer();
            oGLRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(72);
            buffer.putLong(getNativeOps());
            oGLRenderQueue.flushNow();
        } finally {
            oGLRenderQueue.unlock();
        }
    }

    static void dispose(long j2, OGLGraphicsConfig oGLGraphicsConfig) {
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            OGLContext.setScratchSurface(oGLGraphicsConfig);
            RenderBuffer buffer = oGLRenderQueue.getBuffer();
            oGLRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(73);
            buffer.putLong(j2);
            oGLRenderQueue.flushNow();
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }

    static void swapBuffers(long j2) {
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            RenderBuffer buffer = oGLRenderQueue.getBuffer();
            oGLRenderQueue.ensureCapacityAndAlignment(12, 4);
            buffer.putInt(80);
            buffer.putLong(j2);
            oGLRenderQueue.flushNow();
            oGLRenderQueue.unlock();
        } catch (Throwable th) {
            oGLRenderQueue.unlock();
            throw th;
        }
    }

    boolean isTexNonPow2Available() {
        return this.graphicsConfig.isCapPresent(32);
    }

    boolean isTexRectAvailable() {
        return this.graphicsConfig.isCapPresent(1048576);
    }

    @Override // sun.java2d.pipe.hw.AccelSurface
    public Rectangle getNativeBounds() {
        OGLRenderQueue oGLRenderQueue = OGLRenderQueue.getInstance();
        oGLRenderQueue.lock();
        try {
            return new Rectangle(this.nativeWidth, this.nativeHeight);
        } finally {
            oGLRenderQueue.unlock();
        }
    }

    boolean isOnScreen() {
        return getType() == 1;
    }
}
