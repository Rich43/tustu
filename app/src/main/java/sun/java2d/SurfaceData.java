package sun.java2d;

import java.awt.AWTPermission;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.security.Permission;
import sun.awt.image.SurfaceManager;
import sun.font.FontUtilities;
import sun.java2d.StateTrackable;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.DrawGlyphList;
import sun.java2d.loops.DrawGlyphListAA;
import sun.java2d.loops.DrawGlyphListLCD;
import sun.java2d.loops.DrawLine;
import sun.java2d.loops.DrawParallelogram;
import sun.java2d.loops.DrawPath;
import sun.java2d.loops.DrawPolygons;
import sun.java2d.loops.DrawRect;
import sun.java2d.loops.FillParallelogram;
import sun.java2d.loops.FillPath;
import sun.java2d.loops.FillRect;
import sun.java2d.loops.FillSpans;
import sun.java2d.loops.MaskFill;
import sun.java2d.loops.RenderCache;
import sun.java2d.loops.RenderLoops;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.AAShapePipe;
import sun.java2d.pipe.AATextRenderer;
import sun.java2d.pipe.AlphaColorPipe;
import sun.java2d.pipe.AlphaPaintPipe;
import sun.java2d.pipe.CompositePipe;
import sun.java2d.pipe.DrawImage;
import sun.java2d.pipe.DrawImagePipe;
import sun.java2d.pipe.GeneralCompositePipe;
import sun.java2d.pipe.LCDTextRenderer;
import sun.java2d.pipe.LoopBasedPipe;
import sun.java2d.pipe.LoopPipe;
import sun.java2d.pipe.OutlineTextRenderer;
import sun.java2d.pipe.ParallelogramPipe;
import sun.java2d.pipe.PixelToParallelogramConverter;
import sun.java2d.pipe.PixelToShapeConverter;
import sun.java2d.pipe.ShapeDrawPipe;
import sun.java2d.pipe.SolidTextRenderer;
import sun.java2d.pipe.SpanClipRenderer;
import sun.java2d.pipe.SpanShapeRenderer;
import sun.java2d.pipe.TextPipe;
import sun.java2d.pipe.TextRenderer;

/* loaded from: rt.jar:sun/java2d/SurfaceData.class */
public abstract class SurfaceData implements Transparency, DisposerTarget, StateTrackable, Surface {
    private long pData;
    private boolean valid;
    private boolean surfaceLost;
    private SurfaceType surfaceType;
    private ColorModel colorModel;
    private Object disposerReferent;
    private Object blitProxyKey;
    private StateTrackableDelegate stateDelegate;
    protected static final LoopPipe colorPrimitives;
    public static final TextPipe outlineTextRenderer;
    public static final TextPipe solidTextRenderer;
    public static final TextPipe aaTextRenderer;
    public static final TextPipe lcdTextRenderer;
    protected static final AlphaColorPipe colorPipe;
    protected static final PixelToShapeConverter colorViaShape;
    protected static final PixelToParallelogramConverter colorViaPgram;
    protected static final TextPipe colorText;
    protected static final CompositePipe clipColorPipe;
    protected static final TextPipe clipColorText;
    protected static final AAShapePipe AAColorShape;
    protected static final PixelToParallelogramConverter AAColorViaShape;
    protected static final PixelToParallelogramConverter AAColorViaPgram;
    protected static final AAShapePipe AAClipColorShape;
    protected static final PixelToParallelogramConverter AAClipColorViaShape;
    protected static final CompositePipe paintPipe;
    protected static final SpanShapeRenderer paintShape;
    protected static final PixelToShapeConverter paintViaShape;
    protected static final TextPipe paintText;
    protected static final CompositePipe clipPaintPipe;
    protected static final TextPipe clipPaintText;
    protected static final AAShapePipe AAPaintShape;
    protected static final PixelToParallelogramConverter AAPaintViaShape;
    protected static final AAShapePipe AAClipPaintShape;
    protected static final PixelToParallelogramConverter AAClipPaintViaShape;
    protected static final CompositePipe compPipe;
    protected static final SpanShapeRenderer compShape;
    protected static final PixelToShapeConverter compViaShape;
    protected static final TextPipe compText;
    protected static final CompositePipe clipCompPipe;
    protected static final TextPipe clipCompText;
    protected static final AAShapePipe AACompShape;
    protected static final PixelToParallelogramConverter AACompViaShape;
    protected static final AAShapePipe AAClipCompShape;
    protected static final PixelToParallelogramConverter AAClipCompViaShape;
    protected static final DrawImagePipe imagepipe;
    static final int LOOP_UNKNOWN = 0;
    static final int LOOP_FOUND = 1;
    static final int LOOP_NOTFOUND = 2;
    int haveLCDLoop;
    int havePgramXORLoop;
    int havePgramSolidLoop;
    private static RenderCache loopcache;
    static Permission compPermission;

    private static native void initIDs();

    public abstract SurfaceData getReplacement();

    public abstract GraphicsConfiguration getDeviceConfiguration();

    public abstract Raster getRaster(int i2, int i3, int i4, int i5);

    public abstract Rectangle getBounds();

    protected static native boolean isOpaqueGray(IndexColorModel indexColorModel);

    public abstract Object getDestination();

    static {
        initIDs();
        colorPrimitives = new LoopPipe();
        outlineTextRenderer = new OutlineTextRenderer();
        aaTextRenderer = new AATextRenderer();
        if (FontUtilities.isMacOSX14) {
            solidTextRenderer = aaTextRenderer;
        } else {
            solidTextRenderer = new SolidTextRenderer();
        }
        lcdTextRenderer = new LCDTextRenderer();
        colorPipe = new AlphaColorPipe();
        colorViaShape = new PixelToShapeLoopConverter(colorPrimitives);
        colorViaPgram = new PixelToPgramLoopConverter(colorPrimitives, colorPrimitives, 1.0d, 0.25d, true);
        colorText = new TextRenderer(colorPipe);
        clipColorPipe = new SpanClipRenderer(colorPipe);
        clipColorText = new TextRenderer(clipColorPipe);
        AAColorShape = new AAShapePipe(colorPipe);
        AAColorViaShape = makeConverter(AAColorShape);
        AAColorViaPgram = makeConverter(AAColorShape, colorPipe);
        AAClipColorShape = new AAShapePipe(clipColorPipe);
        AAClipColorViaShape = makeConverter(AAClipColorShape);
        paintPipe = new AlphaPaintPipe();
        paintShape = new SpanShapeRenderer.Composite(paintPipe);
        paintViaShape = new PixelToShapeConverter(paintShape);
        paintText = new TextRenderer(paintPipe);
        clipPaintPipe = new SpanClipRenderer(paintPipe);
        clipPaintText = new TextRenderer(clipPaintPipe);
        AAPaintShape = new AAShapePipe(paintPipe);
        AAPaintViaShape = makeConverter(AAPaintShape);
        AAClipPaintShape = new AAShapePipe(clipPaintPipe);
        AAClipPaintViaShape = makeConverter(AAClipPaintShape);
        compPipe = new GeneralCompositePipe();
        compShape = new SpanShapeRenderer.Composite(compPipe);
        compViaShape = new PixelToShapeConverter(compShape);
        compText = new TextRenderer(compPipe);
        clipCompPipe = new SpanClipRenderer(compPipe);
        clipCompText = new TextRenderer(clipCompPipe);
        AACompShape = new AAShapePipe(compPipe);
        AACompViaShape = makeConverter(AACompShape);
        AAClipCompShape = new AAShapePipe(clipCompPipe);
        AAClipCompViaShape = makeConverter(AAClipCompShape);
        imagepipe = new DrawImage();
        loopcache = new RenderCache(30);
    }

    protected SurfaceData(SurfaceType surfaceType, ColorModel colorModel) {
        this(StateTrackable.State.STABLE, surfaceType, colorModel);
    }

    protected SurfaceData(StateTrackable.State state, SurfaceType surfaceType, ColorModel colorModel) {
        this(StateTrackableDelegate.createInstance(state), surfaceType, colorModel);
    }

    protected SurfaceData(StateTrackableDelegate stateTrackableDelegate, SurfaceType surfaceType, ColorModel colorModel) {
        this.disposerReferent = new Object();
        this.stateDelegate = stateTrackableDelegate;
        this.colorModel = colorModel;
        this.surfaceType = surfaceType;
        this.valid = true;
    }

    protected SurfaceData(StateTrackable.State state) {
        this.disposerReferent = new Object();
        this.stateDelegate = StateTrackableDelegate.createInstance(state);
        this.valid = true;
    }

    protected void setBlitProxyKey(Object obj) {
        if (SurfaceDataProxy.isCachingAllowed()) {
            this.blitProxyKey = obj;
        }
    }

    public SurfaceData getSourceSurfaceData(Image image, int i2, CompositeType compositeType, Color color) {
        SurfaceManager manager = SurfaceManager.getManager(image);
        SurfaceData primarySurfaceData = manager.getPrimarySurfaceData();
        if (image.getAccelerationPriority() > 0.0f && this.blitProxyKey != null) {
            SurfaceDataProxy surfaceDataProxyMakeProxyFor = (SurfaceDataProxy) manager.getCacheData(this.blitProxyKey);
            if (surfaceDataProxyMakeProxyFor == null || !surfaceDataProxyMakeProxyFor.isValid()) {
                if (primarySurfaceData.getState() == StateTrackable.State.UNTRACKABLE) {
                    surfaceDataProxyMakeProxyFor = SurfaceDataProxy.UNCACHED;
                } else {
                    surfaceDataProxyMakeProxyFor = makeProxyFor(primarySurfaceData);
                }
                manager.setCacheData(this.blitProxyKey, surfaceDataProxyMakeProxyFor);
            }
            primarySurfaceData = surfaceDataProxyMakeProxyFor.replaceData(primarySurfaceData, i2, compositeType, color);
        }
        return primarySurfaceData;
    }

    public SurfaceDataProxy makeProxyFor(SurfaceData surfaceData) {
        return SurfaceDataProxy.UNCACHED;
    }

    public static SurfaceData getPrimarySurfaceData(Image image) {
        return SurfaceManager.getManager(image).getPrimarySurfaceData();
    }

    public static SurfaceData restoreContents(Image image) {
        return SurfaceManager.getManager(image).restoreContents();
    }

    @Override // sun.java2d.StateTrackable
    public StateTrackable.State getState() {
        return this.stateDelegate.getState();
    }

    @Override // sun.java2d.StateTrackable
    public StateTracker getStateTracker() {
        return this.stateDelegate.getStateTracker();
    }

    public final void markDirty() {
        this.stateDelegate.markDirty();
    }

    public void setSurfaceLost(boolean z2) {
        this.surfaceLost = z2;
        this.stateDelegate.markDirty();
    }

    public boolean isSurfaceLost() {
        return this.surfaceLost;
    }

    public final boolean isValid() {
        return this.valid;
    }

    @Override // sun.java2d.DisposerTarget
    public Object getDisposerReferent() {
        return this.disposerReferent;
    }

    public long getNativeOps() {
        return this.pData;
    }

    public void invalidate() {
        this.valid = false;
        this.stateDelegate.markDirty();
    }

    /* loaded from: rt.jar:sun/java2d/SurfaceData$PixelToShapeLoopConverter.class */
    static class PixelToShapeLoopConverter extends PixelToShapeConverter implements LoopBasedPipe {
        public PixelToShapeLoopConverter(ShapeDrawPipe shapeDrawPipe) {
            super(shapeDrawPipe);
        }
    }

    /* loaded from: rt.jar:sun/java2d/SurfaceData$PixelToPgramLoopConverter.class */
    static class PixelToPgramLoopConverter extends PixelToParallelogramConverter implements LoopBasedPipe {
        public PixelToPgramLoopConverter(ShapeDrawPipe shapeDrawPipe, ParallelogramPipe parallelogramPipe, double d2, double d3, boolean z2) {
            super(shapeDrawPipe, parallelogramPipe, d2, d3, z2);
        }
    }

    private static PixelToParallelogramConverter makeConverter(AAShapePipe aAShapePipe, ParallelogramPipe parallelogramPipe) {
        return new PixelToParallelogramConverter(aAShapePipe, parallelogramPipe, 0.125d, 0.499d, false);
    }

    private static PixelToParallelogramConverter makeConverter(AAShapePipe aAShapePipe) {
        return makeConverter(aAShapePipe, aAShapePipe);
    }

    public boolean canRenderLCDText(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.compositeState <= 0 && sunGraphics2D.paintState <= 1 && sunGraphics2D.clipState <= 1 && sunGraphics2D.surfaceData.getTransparency() == 1) {
            if (this.haveLCDLoop == 0) {
                this.haveLCDLoop = DrawGlyphListLCD.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, getSurfaceType()) != null ? 1 : 2;
            }
            return this.haveLCDLoop == 1;
        }
        return false;
    }

    public boolean canRenderParallelograms(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.paintState <= 1) {
            if (sunGraphics2D.compositeState == 2) {
                if (this.havePgramXORLoop == 0) {
                    this.havePgramXORLoop = FillParallelogram.locate(SurfaceType.AnyColor, CompositeType.Xor, getSurfaceType()) != null ? 1 : 2;
                }
                return this.havePgramXORLoop == 1;
            }
            if (sunGraphics2D.compositeState <= 0 && sunGraphics2D.antialiasHint != 2 && sunGraphics2D.clipState != 2) {
                if (this.havePgramSolidLoop == 0) {
                    this.havePgramSolidLoop = FillParallelogram.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, getSurfaceType()) != null ? 1 : 2;
                }
                return this.havePgramSolidLoop == 1;
            }
            return false;
        }
        return false;
    }

    public void validatePipe(SunGraphics2D sunGraphics2D) {
        PixelToShapeConverter pixelToShapeConverter;
        PixelToShapeConverter pixelToShapeConverter2;
        sunGraphics2D.imagepipe = imagepipe;
        if (sunGraphics2D.compositeState == 2) {
            if (sunGraphics2D.paintState > 1) {
                sunGraphics2D.drawpipe = paintViaShape;
                sunGraphics2D.fillpipe = paintViaShape;
                sunGraphics2D.shapepipe = paintShape;
                sunGraphics2D.textpipe = outlineTextRenderer;
            } else {
                if (canRenderParallelograms(sunGraphics2D)) {
                    pixelToShapeConverter2 = colorViaPgram;
                    sunGraphics2D.shapepipe = colorViaPgram;
                } else {
                    pixelToShapeConverter2 = colorViaShape;
                    sunGraphics2D.shapepipe = colorPrimitives;
                }
                if (sunGraphics2D.clipState == 2) {
                    sunGraphics2D.drawpipe = pixelToShapeConverter2;
                    sunGraphics2D.fillpipe = pixelToShapeConverter2;
                    sunGraphics2D.textpipe = outlineTextRenderer;
                } else {
                    if (sunGraphics2D.transformState >= 3) {
                        sunGraphics2D.drawpipe = pixelToShapeConverter2;
                        sunGraphics2D.fillpipe = pixelToShapeConverter2;
                    } else {
                        if (sunGraphics2D.strokeState != 0) {
                            sunGraphics2D.drawpipe = pixelToShapeConverter2;
                        } else {
                            sunGraphics2D.drawpipe = colorPrimitives;
                        }
                        sunGraphics2D.fillpipe = colorPrimitives;
                    }
                    sunGraphics2D.textpipe = solidTextRenderer;
                }
            }
        } else if (sunGraphics2D.compositeState == 3) {
            if (sunGraphics2D.antialiasHint == 2) {
                if (sunGraphics2D.clipState == 2) {
                    sunGraphics2D.drawpipe = AAClipCompViaShape;
                    sunGraphics2D.fillpipe = AAClipCompViaShape;
                    sunGraphics2D.shapepipe = AAClipCompViaShape;
                    sunGraphics2D.textpipe = clipCompText;
                } else {
                    sunGraphics2D.drawpipe = AACompViaShape;
                    sunGraphics2D.fillpipe = AACompViaShape;
                    sunGraphics2D.shapepipe = AACompViaShape;
                    sunGraphics2D.textpipe = compText;
                }
            } else {
                sunGraphics2D.drawpipe = compViaShape;
                sunGraphics2D.fillpipe = compViaShape;
                sunGraphics2D.shapepipe = compShape;
                if (sunGraphics2D.clipState == 2) {
                    sunGraphics2D.textpipe = clipCompText;
                } else {
                    sunGraphics2D.textpipe = compText;
                }
            }
        } else if (sunGraphics2D.antialiasHint == 2) {
            sunGraphics2D.alphafill = getMaskFill(sunGraphics2D);
            if (sunGraphics2D.alphafill != null) {
                if (sunGraphics2D.clipState == 2) {
                    sunGraphics2D.drawpipe = AAClipColorViaShape;
                    sunGraphics2D.fillpipe = AAClipColorViaShape;
                    sunGraphics2D.shapepipe = AAClipColorViaShape;
                    sunGraphics2D.textpipe = clipColorText;
                } else {
                    PixelToParallelogramConverter pixelToParallelogramConverter = sunGraphics2D.alphafill.canDoParallelograms() ? AAColorViaPgram : AAColorViaShape;
                    sunGraphics2D.drawpipe = pixelToParallelogramConverter;
                    sunGraphics2D.fillpipe = pixelToParallelogramConverter;
                    sunGraphics2D.shapepipe = pixelToParallelogramConverter;
                    if (sunGraphics2D.paintState > 1 || sunGraphics2D.compositeState > 0) {
                        sunGraphics2D.textpipe = colorText;
                    } else {
                        sunGraphics2D.textpipe = getTextPipe(sunGraphics2D, true);
                    }
                }
            } else if (sunGraphics2D.clipState == 2) {
                sunGraphics2D.drawpipe = AAClipPaintViaShape;
                sunGraphics2D.fillpipe = AAClipPaintViaShape;
                sunGraphics2D.shapepipe = AAClipPaintViaShape;
                sunGraphics2D.textpipe = clipPaintText;
            } else {
                sunGraphics2D.drawpipe = AAPaintViaShape;
                sunGraphics2D.fillpipe = AAPaintViaShape;
                sunGraphics2D.shapepipe = AAPaintViaShape;
                sunGraphics2D.textpipe = paintText;
            }
        } else if (sunGraphics2D.paintState > 1 || sunGraphics2D.compositeState > 0 || sunGraphics2D.clipState == 2) {
            sunGraphics2D.drawpipe = paintViaShape;
            sunGraphics2D.fillpipe = paintViaShape;
            sunGraphics2D.shapepipe = paintShape;
            sunGraphics2D.alphafill = getMaskFill(sunGraphics2D);
            if (sunGraphics2D.alphafill != null) {
                if (sunGraphics2D.clipState == 2) {
                    sunGraphics2D.textpipe = clipColorText;
                } else {
                    sunGraphics2D.textpipe = colorText;
                }
            } else if (sunGraphics2D.clipState == 2) {
                sunGraphics2D.textpipe = clipPaintText;
            } else {
                sunGraphics2D.textpipe = paintText;
            }
        } else {
            if (canRenderParallelograms(sunGraphics2D)) {
                pixelToShapeConverter = colorViaPgram;
                sunGraphics2D.shapepipe = colorViaPgram;
            } else {
                pixelToShapeConverter = colorViaShape;
                sunGraphics2D.shapepipe = colorPrimitives;
            }
            if (sunGraphics2D.transformState >= 3) {
                sunGraphics2D.drawpipe = pixelToShapeConverter;
                sunGraphics2D.fillpipe = pixelToShapeConverter;
            } else {
                if (sunGraphics2D.strokeState != 0) {
                    sunGraphics2D.drawpipe = pixelToShapeConverter;
                } else {
                    sunGraphics2D.drawpipe = colorPrimitives;
                }
                sunGraphics2D.fillpipe = colorPrimitives;
            }
            sunGraphics2D.textpipe = getTextPipe(sunGraphics2D, false);
        }
        if ((sunGraphics2D.textpipe instanceof LoopBasedPipe) || (sunGraphics2D.shapepipe instanceof LoopBasedPipe) || (sunGraphics2D.fillpipe instanceof LoopBasedPipe) || (sunGraphics2D.drawpipe instanceof LoopBasedPipe) || (sunGraphics2D.imagepipe instanceof LoopBasedPipe)) {
            sunGraphics2D.loops = getRenderLoops(sunGraphics2D);
        }
    }

    private TextPipe getTextPipe(SunGraphics2D sunGraphics2D, boolean z2) {
        switch (sunGraphics2D.textAntialiasHint) {
            case 0:
                if (z2) {
                    return aaTextRenderer;
                }
                return solidTextRenderer;
            case 1:
                return solidTextRenderer;
            case 2:
                return aaTextRenderer;
            default:
                switch (sunGraphics2D.getFontInfo().aaHint) {
                    case 1:
                        return solidTextRenderer;
                    case 2:
                        return aaTextRenderer;
                    case 3:
                    case 5:
                    default:
                        if (z2) {
                            return aaTextRenderer;
                        }
                        return solidTextRenderer;
                    case 4:
                    case 6:
                        return lcdTextRenderer;
                }
        }
    }

    private static SurfaceType getPaintSurfaceType(SunGraphics2D sunGraphics2D) {
        switch (sunGraphics2D.paintState) {
            case 0:
                return SurfaceType.OpaqueColor;
            case 1:
                return SurfaceType.AnyColor;
            case 2:
                if (sunGraphics2D.paint.getTransparency() == 1) {
                    return SurfaceType.OpaqueGradientPaint;
                }
                return SurfaceType.GradientPaint;
            case 3:
                if (sunGraphics2D.paint.getTransparency() == 1) {
                    return SurfaceType.OpaqueLinearGradientPaint;
                }
                return SurfaceType.LinearGradientPaint;
            case 4:
                if (sunGraphics2D.paint.getTransparency() == 1) {
                    return SurfaceType.OpaqueRadialGradientPaint;
                }
                return SurfaceType.RadialGradientPaint;
            case 5:
                if (sunGraphics2D.paint.getTransparency() == 1) {
                    return SurfaceType.OpaqueTexturePaint;
                }
                return SurfaceType.TexturePaint;
            case 6:
            default:
                return SurfaceType.AnyPaint;
        }
    }

    private static CompositeType getFillCompositeType(SunGraphics2D sunGraphics2D) {
        CompositeType compositeType = sunGraphics2D.imageComp;
        if (sunGraphics2D.compositeState == 0) {
            if (compositeType == CompositeType.SrcOverNoEa) {
                compositeType = CompositeType.OpaqueSrcOverNoEa;
            } else {
                compositeType = CompositeType.SrcNoEa;
            }
        }
        return compositeType;
    }

    protected MaskFill getMaskFill(SunGraphics2D sunGraphics2D) {
        return MaskFill.getFromCache(getPaintSurfaceType(sunGraphics2D), getFillCompositeType(sunGraphics2D), getSurfaceType());
    }

    public RenderLoops getRenderLoops(SunGraphics2D sunGraphics2D) {
        SurfaceType paintSurfaceType = getPaintSurfaceType(sunGraphics2D);
        CompositeType fillCompositeType = getFillCompositeType(sunGraphics2D);
        SurfaceType surfaceType = sunGraphics2D.getSurfaceData().getSurfaceType();
        Object obj = loopcache.get(paintSurfaceType, fillCompositeType, surfaceType);
        if (obj != null) {
            return (RenderLoops) obj;
        }
        RenderLoops renderLoopsMakeRenderLoops = makeRenderLoops(paintSurfaceType, fillCompositeType, surfaceType);
        loopcache.put(paintSurfaceType, fillCompositeType, surfaceType, renderLoopsMakeRenderLoops);
        return renderLoopsMakeRenderLoops;
    }

    public static RenderLoops makeRenderLoops(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        RenderLoops renderLoops = new RenderLoops();
        renderLoops.drawLineLoop = DrawLine.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.fillRectLoop = FillRect.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawRectLoop = DrawRect.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawPolygonsLoop = DrawPolygons.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawPathLoop = DrawPath.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.fillPathLoop = FillPath.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.fillSpansLoop = FillSpans.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.fillParallelogramLoop = FillParallelogram.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawParallelogramLoop = DrawParallelogram.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawGlyphListLoop = DrawGlyphList.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawGlyphListAALoop = DrawGlyphListAA.locate(surfaceType, compositeType, surfaceType2);
        renderLoops.drawGlyphListLCDLoop = DrawGlyphListLCD.locate(surfaceType, compositeType, surfaceType2);
        return renderLoops;
    }

    public final SurfaceType getSurfaceType() {
        return this.surfaceType;
    }

    public final ColorModel getColorModel() {
        return this.colorModel;
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return getColorModel().getTransparency();
    }

    public boolean useTightBBoxes() {
        return true;
    }

    public int pixelFor(int i2) {
        return this.surfaceType.pixelFor(i2, this.colorModel);
    }

    public int pixelFor(Color color) {
        return pixelFor(color.getRGB());
    }

    public int rgbFor(int i2) {
        return this.surfaceType.rgbFor(i2, this.colorModel);
    }

    protected void checkCustomComposite() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (compPermission == null) {
                compPermission = new AWTPermission("readDisplayPixels");
            }
            securityManager.checkPermission(compPermission);
        }
    }

    public static boolean isNull(SurfaceData surfaceData) {
        if (surfaceData == null || surfaceData == NullSurfaceData.theInstance) {
            return true;
        }
        return false;
    }

    public boolean copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        return false;
    }

    public void flush() {
    }

    public int getDefaultScale() {
        return 1;
    }
}
