package sun.java2d.windows;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import sun.awt.Win32GraphicsConfig;
import sun.awt.Win32GraphicsDevice;
import sun.awt.windows.WComponentPeer;
import sun.java2d.InvalidPipeException;
import sun.java2d.ScreenUpdateManager;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.RenderLoops;
import sun.java2d.loops.SurfaceType;
import sun.java2d.loops.XORComposite;
import sun.java2d.pipe.PixelToShapeConverter;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/windows/GDIWindowSurfaceData.class */
public class GDIWindowSurfaceData extends SurfaceData {
    private WComponentPeer peer;
    private Win32GraphicsConfig graphicsConfig;
    private RenderLoops solidloops;
    public static final String DESC_GDI = "GDI";
    public static final SurfaceType AnyGdi = SurfaceType.IntRgb.deriveSubType(DESC_GDI);
    public static final SurfaceType IntRgbGdi = SurfaceType.IntRgb.deriveSubType(DESC_GDI);
    public static final SurfaceType Ushort565RgbGdi = SurfaceType.Ushort565Rgb.deriveSubType(DESC_GDI);
    public static final SurfaceType Ushort555RgbGdi = SurfaceType.Ushort555Rgb.deriveSubType(DESC_GDI);
    public static final SurfaceType ThreeByteBgrGdi = SurfaceType.ThreeByteBgr.deriveSubType(DESC_GDI);
    protected static GDIRenderer gdiPipe;
    protected static PixelToShapeConverter gdiTxPipe;

    private static native void initIDs(Class cls);

    private native void initOps(WComponentPeer wComponentPeer, int i2, int i3, int i4, int i5, int i6);

    private native void invalidateSD();

    static {
        initIDs(XORComposite.class);
        if (WindowsFlags.isGdiBlitEnabled()) {
            GDIBlitLoops.register();
        }
        gdiPipe = new GDIRenderer();
        if (GraphicsPrimitive.tracingEnabled()) {
            gdiPipe = gdiPipe.traceWrap();
        }
        gdiTxPipe = new PixelToShapeConverter(gdiPipe);
    }

    public static SurfaceType getSurfaceType(ColorModel colorModel) {
        switch (colorModel.getPixelSize()) {
            case 8:
                if (colorModel.getColorSpace().getType() == 6 && (colorModel instanceof ComponentColorModel)) {
                    return SurfaceType.ByteGray;
                }
                if ((colorModel instanceof IndexColorModel) && isOpaqueGray((IndexColorModel) colorModel)) {
                    return SurfaceType.Index8Gray;
                }
                return SurfaceType.ByteIndexedOpaque;
            case 15:
                return Ushort555RgbGdi;
            case 16:
                if ((colorModel instanceof DirectColorModel) && ((DirectColorModel) colorModel).getBlueMask() == 62) {
                    return SurfaceType.Ushort555Rgbx;
                }
                return Ushort565RgbGdi;
            case 24:
            case 32:
                if (colorModel instanceof DirectColorModel) {
                    if (((DirectColorModel) colorModel).getRedMask() == 16711680) {
                        return IntRgbGdi;
                    }
                    return SurfaceType.IntRgbx;
                }
                return ThreeByteBgrGdi;
            default:
                throw new InvalidPipeException("Unsupported bit depth: " + colorModel.getPixelSize());
        }
    }

    public static GDIWindowSurfaceData createData(WComponentPeer wComponentPeer) {
        return new GDIWindowSurfaceData(wComponentPeer, getSurfaceType(wComponentPeer.getDeviceColorModel()));
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceDataProxy makeProxyFor(SurfaceData surfaceData) {
        return SurfaceDataProxy.UNCACHED;
    }

    @Override // sun.java2d.SurfaceData
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        throw new InternalError("not implemented yet");
    }

    @Override // sun.java2d.SurfaceData
    public void validatePipe(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.antialiasHint != 2 && sunGraphics2D.paintState <= 1 && (sunGraphics2D.compositeState <= 0 || sunGraphics2D.compositeState == 2)) {
            if (sunGraphics2D.clipState == 2) {
                super.validatePipe(sunGraphics2D);
            } else {
                switch (sunGraphics2D.textAntialiasHint) {
                    case 0:
                    case 1:
                        sunGraphics2D.textpipe = solidTextRenderer;
                        break;
                    case 2:
                        sunGraphics2D.textpipe = aaTextRenderer;
                        break;
                    default:
                        switch (sunGraphics2D.getFontInfo().aaHint) {
                            case 2:
                                sunGraphics2D.textpipe = aaTextRenderer;
                                break;
                            case 3:
                            case 5:
                            default:
                                sunGraphics2D.textpipe = solidTextRenderer;
                                break;
                            case 4:
                            case 6:
                                sunGraphics2D.textpipe = lcdTextRenderer;
                                break;
                        }
                }
            }
            sunGraphics2D.imagepipe = imagepipe;
            if (sunGraphics2D.transformState >= 3) {
                sunGraphics2D.drawpipe = gdiTxPipe;
                sunGraphics2D.fillpipe = gdiTxPipe;
            } else if (sunGraphics2D.strokeState != 0) {
                sunGraphics2D.drawpipe = gdiTxPipe;
                sunGraphics2D.fillpipe = gdiPipe;
            } else {
                sunGraphics2D.drawpipe = gdiPipe;
                sunGraphics2D.fillpipe = gdiPipe;
            }
            sunGraphics2D.shapepipe = gdiPipe;
            if (sunGraphics2D.loops == null) {
                sunGraphics2D.loops = getRenderLoops(sunGraphics2D);
                return;
            }
            return;
        }
        super.validatePipe(sunGraphics2D);
    }

    @Override // sun.java2d.SurfaceData
    public RenderLoops getRenderLoops(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.paintState <= 1 && sunGraphics2D.compositeState <= 0) {
            return this.solidloops;
        }
        return super.getRenderLoops(sunGraphics2D);
    }

    @Override // sun.java2d.SurfaceData
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.graphicsConfig;
    }

    private GDIWindowSurfaceData(WComponentPeer wComponentPeer, SurfaceType surfaceType) {
        int pixelSize;
        super(surfaceType, wComponentPeer.getDeviceColorModel());
        ColorModel deviceColorModel = wComponentPeer.getDeviceColorModel();
        this.peer = wComponentPeer;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        switch (deviceColorModel.getPixelSize()) {
            case 24:
            case 32:
                if (deviceColorModel instanceof DirectColorModel) {
                    pixelSize = 32;
                    break;
                } else {
                    pixelSize = 24;
                    break;
                }
            default:
                pixelSize = deviceColorModel.getPixelSize();
                break;
        }
        if (deviceColorModel instanceof DirectColorModel) {
            DirectColorModel directColorModel = (DirectColorModel) deviceColorModel;
            redMask = directColorModel.getRedMask();
            greenMask = directColorModel.getGreenMask();
            blueMask = directColorModel.getBlueMask();
        }
        this.graphicsConfig = (Win32GraphicsConfig) wComponentPeer.getGraphicsConfiguration();
        this.solidloops = this.graphicsConfig.getSolidLoops(surfaceType);
        initOps(wComponentPeer, pixelSize, redMask, greenMask, blueMask, ((Win32GraphicsDevice) this.graphicsConfig.getDevice()).getScreen());
        setBlitProxyKey(this.graphicsConfig.getProxyKey());
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceData getReplacement() {
        return ScreenUpdateManager.getInstance().getReplacementScreenSurface(this.peer, this);
    }

    @Override // sun.java2d.SurfaceData
    public Rectangle getBounds() {
        Rectangle bounds = this.peer.getBounds();
        bounds.f12373y = 0;
        bounds.f12372x = 0;
        return bounds;
    }

    @Override // sun.java2d.SurfaceData
    public boolean copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        CompositeType compositeType = sunGraphics2D.imageComp;
        if (sunGraphics2D.transformState < 3 && sunGraphics2D.clipState != 2) {
            if (CompositeType.SrcOverNoEa.equals(compositeType) || CompositeType.SrcNoEa.equals(compositeType)) {
                int loX = i2 + sunGraphics2D.transX + i6;
                int loY = i3 + sunGraphics2D.transY + i7;
                int hiX = loX + i4;
                int hiY = loY + i5;
                Region compClip = sunGraphics2D.getCompClip();
                if (loX < compClip.getLoX()) {
                    loX = compClip.getLoX();
                }
                if (loY < compClip.getLoY()) {
                    loY = compClip.getLoY();
                }
                if (hiX > compClip.getHiX()) {
                    hiX = compClip.getHiX();
                }
                if (hiY > compClip.getHiY()) {
                    hiY = compClip.getHiY();
                }
                if (loX < hiX && loY < hiY) {
                    gdiPipe.devCopyArea(this, loX - i6, loY - i7, i6, i7, hiX - loX, hiY - loY);
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // sun.java2d.SurfaceData
    public void invalidate() {
        if (isValid()) {
            invalidateSD();
            super.invalidate();
        }
    }

    @Override // sun.java2d.SurfaceData
    public Object getDestination() {
        return this.peer.getTarget();
    }

    public WComponentPeer getPeer() {
        return this.peer;
    }
}
