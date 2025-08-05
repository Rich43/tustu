package sun.java2d;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;
import java.util.Map;
import sun.awt.ConstrainableGraphics;
import sun.awt.SunHints;
import sun.awt.image.MultiResolutionImage;
import sun.awt.image.MultiResolutionToolkitImage;
import sun.awt.image.SurfaceManager;
import sun.awt.image.ToolkitImage;
import sun.font.FontDesignMetrics;
import sun.font.FontUtilities;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.FontInfo;
import sun.java2d.loops.MaskFill;
import sun.java2d.loops.RenderLoops;
import sun.java2d.loops.SurfaceType;
import sun.java2d.loops.XORComposite;
import sun.java2d.pipe.DrawImagePipe;
import sun.java2d.pipe.LoopPipe;
import sun.java2d.pipe.PixelDrawPipe;
import sun.java2d.pipe.PixelFillPipe;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderingEngine;
import sun.java2d.pipe.ShapeDrawPipe;
import sun.java2d.pipe.ShapeSpanIterator;
import sun.java2d.pipe.TextPipe;
import sun.java2d.pipe.ValidatePipe;
import sun.misc.PerformanceLogger;

/* loaded from: rt.jar:sun/java2d/SunGraphics2D.class */
public final class SunGraphics2D extends Graphics2D implements ConstrainableGraphics, Cloneable, DestSurfaceProvider {
    public static final int PAINT_CUSTOM = 6;
    public static final int PAINT_TEXTURE = 5;
    public static final int PAINT_RAD_GRADIENT = 4;
    public static final int PAINT_LIN_GRADIENT = 3;
    public static final int PAINT_GRADIENT = 2;
    public static final int PAINT_ALPHACOLOR = 1;
    public static final int PAINT_OPAQUECOLOR = 0;
    public static final int COMP_CUSTOM = 3;
    public static final int COMP_XOR = 2;
    public static final int COMP_ALPHA = 1;
    public static final int COMP_ISCOPY = 0;
    public static final int STROKE_CUSTOM = 3;
    public static final int STROKE_WIDE = 2;
    public static final int STROKE_THINDASHED = 1;
    public static final int STROKE_THIN = 0;
    public static final int TRANSFORM_GENERIC = 4;
    public static final int TRANSFORM_TRANSLATESCALE = 3;
    public static final int TRANSFORM_ANY_TRANSLATE = 2;
    public static final int TRANSFORM_INT_TRANSLATE = 1;
    public static final int TRANSFORM_ISIDENT = 0;
    public static final int CLIP_SHAPE = 2;
    public static final int CLIP_RECTANGULAR = 1;
    public static final int CLIP_DEVICE = 0;
    public int eargb;
    public int pixel;
    public SurfaceData surfaceData;
    public PixelDrawPipe drawpipe;
    public PixelFillPipe fillpipe;
    public DrawImagePipe imagepipe;
    public ShapeDrawPipe shapepipe;
    public TextPipe textpipe;
    public MaskFill alphafill;
    public RenderLoops loops;
    public int paintState;
    public int compositeState;
    public int strokeState;
    public int transformState;
    public int clipState;
    public Color foregroundColor;
    public Color backgroundColor;
    public int transX;
    public int transY;
    public Paint paint;
    protected Font font;
    protected FontMetrics fontMetrics;
    public RenderingHints hints;
    public Region constrainClip;
    public int constrainX;
    public int constrainY;
    public Region clipRegion;
    public Shape usrClip;
    protected Region devClip;
    private final int devScale;
    private boolean validFontInfo;
    private FontInfo fontInfo;
    private FontInfo glyphVectorFontInfo;
    private FontRenderContext glyphVectorFRC;
    private static final int slowTextTransformMask = 120;
    protected static ValidatePipe invalidpipe;
    private static final double[] IDENT_MATRIX;
    private static final AffineTransform IDENT_ATX;
    private static final int MINALLOCATED = 8;
    private static final int TEXTARRSIZE = 17;
    private static double[][] textTxArr;
    private static AffineTransform[] textAtArr;
    static final int NON_UNIFORM_SCALE_MASK = 36;
    public static final double MinPenSizeAA;
    public static final double MinPenSizeAASquared;
    public static final double MinPenSizeSquared = 1.000000001d;
    static final int NON_RECTILINEAR_TRANSFORM_MASK = 48;
    Blit lastCAblit;
    Composite lastCAcomp;
    private FontRenderContext cachedFRC;
    protected static final Stroke defaultStroke = new BasicStroke();
    protected static final Composite defaultComposite = AlphaComposite.SrcOver;
    private static final Font defaultFont = new Font(Font.DIALOG, 0, 12);
    private static int lcdTextContrastDefaultValue = 140;
    public AffineTransform transform = new AffineTransform();
    public Stroke stroke = defaultStroke;
    public Composite composite = defaultComposite;
    public CompositeType imageComp = CompositeType.SrcOverNoEa;
    public int renderHint = 0;
    public int antialiasHint = 1;
    public int textAntialiasHint = 0;
    protected int fractionalMetricsHint = 1;
    public int lcdTextContrast = lcdTextContrastDefaultValue;
    private int interpolationHint = -1;
    public int strokeHint = 0;
    private int resolutionVariantHint = 0;
    public int interpolationType = 1;

    /* JADX WARN: Type inference failed for: r0v10, types: [double[], double[][]] */
    static {
        if (PerformanceLogger.loggingEnabled()) {
            PerformanceLogger.setTime("SunGraphics2D static initialization");
        }
        invalidpipe = new ValidatePipe();
        IDENT_MATRIX = new double[]{1.0d, 0.0d, 0.0d, 1.0d};
        IDENT_ATX = new AffineTransform();
        textTxArr = new double[17];
        textAtArr = new AffineTransform[17];
        for (int i2 = 8; i2 < 17; i2++) {
            textTxArr[i2] = new double[]{i2, 0.0d, 0.0d, i2};
            textAtArr[i2] = new AffineTransform(textTxArr[i2]);
        }
        MinPenSizeAA = RenderingEngine.getInstance().getMinimumAAPenSize();
        MinPenSizeAASquared = MinPenSizeAA * MinPenSizeAA;
    }

    public SunGraphics2D(SurfaceData surfaceData, Color color, Color color2, Font font) {
        this.surfaceData = surfaceData;
        this.foregroundColor = color;
        this.backgroundColor = color2;
        this.paint = this.foregroundColor;
        validateColor();
        this.devScale = surfaceData.getDefaultScale();
        if (this.devScale != 1) {
            this.transform.setToScale(this.devScale, this.devScale);
            invalidateTransform();
        }
        this.font = font;
        if (this.font == null) {
            this.font = defaultFont;
        }
        setDevClip(surfaceData.getBounds());
        invalidatePipe();
    }

    protected Object clone() {
        try {
            SunGraphics2D sunGraphics2D = (SunGraphics2D) super.clone();
            sunGraphics2D.transform = new AffineTransform(this.transform);
            if (this.hints != null) {
                sunGraphics2D.hints = (RenderingHints) this.hints.clone();
            }
            if (this.fontInfo != null) {
                if (this.validFontInfo) {
                    sunGraphics2D.fontInfo = (FontInfo) this.fontInfo.clone();
                } else {
                    sunGraphics2D.fontInfo = null;
                }
            }
            if (this.glyphVectorFontInfo != null) {
                sunGraphics2D.glyphVectorFontInfo = (FontInfo) this.glyphVectorFontInfo.clone();
                sunGraphics2D.glyphVectorFRC = this.glyphVectorFRC;
            }
            return sunGraphics2D;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    @Override // java.awt.Graphics
    public Graphics create() {
        return (Graphics) clone();
    }

    public void setDevClip(int i2, int i3, int i4, int i5) {
        Region region = this.constrainClip;
        if (region == null) {
            this.devClip = Region.getInstanceXYWH(i2, i3, i4, i5);
        } else {
            this.devClip = region.getIntersectionXYWH(i2, i3, i4, i5);
        }
        validateCompClip();
    }

    public void setDevClip(Rectangle rectangle) {
        setDevClip(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public void constrain(int i2, int i3, int i4, int i5, Region region) {
        Region intersectionXYXY;
        if ((i2 | i3) != 0) {
            translate(i2, i3);
        }
        if (this.transformState > 3) {
            clipRect(0, 0, i4, i5);
            return;
        }
        double scaleX = this.transform.getScaleX();
        double scaleY = this.transform.getScaleY();
        int translateX = (int) this.transform.getTranslateX();
        this.constrainX = translateX;
        int translateY = (int) this.transform.getTranslateY();
        this.constrainY = translateY;
        int iDimAdd = Region.dimAdd(translateX, Region.clipScale(i4, scaleX));
        int iDimAdd2 = Region.dimAdd(translateY, Region.clipScale(i5, scaleY));
        Region region2 = this.constrainClip;
        if (region2 == null) {
            intersectionXYXY = Region.getInstanceXYXY(translateX, translateY, iDimAdd, iDimAdd2);
        } else {
            intersectionXYXY = region2.getIntersectionXYXY(translateX, translateY, iDimAdd, iDimAdd2);
        }
        if (region != null) {
            intersectionXYXY = intersectionXYXY.getIntersection(region.getScaledRegion(scaleX, scaleY).getTranslatedRegion(translateX, translateY));
        }
        if (intersectionXYXY == this.constrainClip) {
            return;
        }
        this.constrainClip = intersectionXYXY;
        if (!this.devClip.isInsideQuickCheck(intersectionXYXY)) {
            this.devClip = this.devClip.getIntersection(intersectionXYXY);
            validateCompClip();
        }
    }

    @Override // sun.awt.ConstrainableGraphics
    public void constrain(int i2, int i3, int i4, int i5) {
        constrain(i2, i3, i4, i5, null);
    }

    protected void invalidatePipe() {
        this.drawpipe = invalidpipe;
        this.fillpipe = invalidpipe;
        this.shapepipe = invalidpipe;
        this.textpipe = invalidpipe;
        this.imagepipe = invalidpipe;
        this.loops = null;
    }

    public void validatePipe() {
        if (!this.surfaceData.isValid()) {
            throw new InvalidPipeException("attempt to validate Pipe with invalid SurfaceData");
        }
        this.surfaceData.validatePipe(this);
    }

    Shape intersectShapes(Shape shape, Shape shape2, boolean z2, boolean z3) {
        if ((shape instanceof Rectangle) && (shape2 instanceof Rectangle)) {
            return ((Rectangle) shape).intersection((Rectangle) shape2);
        }
        if (shape instanceof Rectangle2D) {
            return intersectRectShape((Rectangle2D) shape, shape2, z2, z3);
        }
        if (shape2 instanceof Rectangle2D) {
            return intersectRectShape((Rectangle2D) shape2, shape, z3, z2);
        }
        return intersectByArea(shape, shape2, z2, z3);
    }

    Shape intersectRectShape(Rectangle2D rectangle2D, Shape shape, boolean z2, boolean z3) {
        Rectangle2D rectangle2D2;
        if (shape instanceof Rectangle2D) {
            Rectangle2D rectangle2D3 = (Rectangle2D) shape;
            if (!z2) {
                rectangle2D2 = rectangle2D;
            } else if (!z3) {
                rectangle2D2 = rectangle2D3;
            } else {
                rectangle2D2 = new Rectangle2D.Float();
            }
            double dMax = Math.max(rectangle2D.getX(), rectangle2D3.getX());
            double dMin = Math.min(rectangle2D.getX() + rectangle2D.getWidth(), rectangle2D3.getX() + rectangle2D3.getWidth());
            double dMax2 = Math.max(rectangle2D.getY(), rectangle2D3.getY());
            double dMin2 = Math.min(rectangle2D.getY() + rectangle2D.getHeight(), rectangle2D3.getY() + rectangle2D3.getHeight());
            if (dMin - dMax < 0.0d || dMin2 - dMax2 < 0.0d) {
                rectangle2D2.setFrameFromDiagonal(0.0d, 0.0d, 0.0d, 0.0d);
            } else {
                rectangle2D2.setFrameFromDiagonal(dMax, dMax2, dMin, dMin2);
            }
            return rectangle2D2;
        }
        if (rectangle2D.contains(shape.getBounds2D())) {
            if (z3) {
                shape = cloneShape(shape);
            }
            return shape;
        }
        return intersectByArea(rectangle2D, shape, z2, z3);
    }

    protected static Shape cloneShape(Shape shape) {
        return new GeneralPath(shape);
    }

    Shape intersectByArea(Shape shape, Shape shape2, boolean z2, boolean z3) {
        Area area;
        Area area2;
        if (!z2 && (shape instanceof Area)) {
            area = (Area) shape;
        } else if (!z3 && (shape2 instanceof Area)) {
            area = (Area) shape2;
            shape2 = shape;
        } else {
            area = new Area(shape);
        }
        if (shape2 instanceof Area) {
            area2 = (Area) shape2;
        } else {
            area2 = new Area(shape2);
        }
        area.intersect(area2);
        if (area.isRectangular()) {
            return area.getBounds();
        }
        return area;
    }

    public Region getCompClip() {
        if (!this.surfaceData.isValid()) {
            revalidateAll();
        }
        return this.clipRegion;
    }

    @Override // java.awt.Graphics
    public Font getFont() {
        if (this.font == null) {
            this.font = defaultFont;
        }
        return this.font;
    }

    public FontInfo checkFontInfo(FontInfo fontInfo, Font font, FontRenderContext fontRenderContext) {
        AffineTransform affineTransform;
        int index;
        if (fontInfo == null) {
            fontInfo = new FontInfo();
        }
        float size2D = font.getSize2D();
        AffineTransform affineTransform2 = null;
        if (font.isTransformed()) {
            affineTransform2 = font.getTransform();
            affineTransform2.scale(size2D, size2D);
            affineTransform2.getType();
            fontInfo.originX = (float) affineTransform2.getTranslateX();
            fontInfo.originY = (float) affineTransform2.getTranslateY();
            affineTransform2.translate(-fontInfo.originX, -fontInfo.originY);
            if (this.transformState >= 3) {
                AffineTransform affineTransform3 = this.transform;
                double[] dArr = new double[4];
                fontInfo.devTx = dArr;
                affineTransform3.getMatrix(dArr);
                affineTransform = new AffineTransform(fontInfo.devTx);
                affineTransform2.preConcatenate(affineTransform);
            } else {
                fontInfo.devTx = IDENT_MATRIX;
                affineTransform = IDENT_ATX;
            }
            double[] dArr2 = new double[4];
            fontInfo.glyphTx = dArr2;
            affineTransform2.getMatrix(dArr2);
            double shearX = affineTransform2.getShearX();
            double scaleY = affineTransform2.getScaleY();
            if (shearX != 0.0d) {
                scaleY = Math.sqrt((shearX * shearX) + (scaleY * scaleY));
            }
            fontInfo.pixelHeight = (int) (Math.abs(scaleY) + 0.5d);
        } else {
            fontInfo.originY = 0.0f;
            fontInfo.originX = 0.0f;
            if (this.transformState >= 3) {
                AffineTransform affineTransform4 = this.transform;
                double[] dArr3 = new double[4];
                fontInfo.devTx = dArr3;
                affineTransform4.getMatrix(dArr3);
                affineTransform = new AffineTransform(fontInfo.devTx);
                fontInfo.glyphTx = new double[4];
                for (int i2 = 0; i2 < 4; i2++) {
                    fontInfo.glyphTx[i2] = fontInfo.devTx[i2] * size2D;
                }
                affineTransform2 = new AffineTransform(fontInfo.glyphTx);
                double shearX2 = this.transform.getShearX();
                double scaleY2 = this.transform.getScaleY();
                if (shearX2 != 0.0d) {
                    scaleY2 = Math.sqrt((shearX2 * shearX2) + (scaleY2 * scaleY2));
                }
                fontInfo.pixelHeight = (int) (Math.abs(scaleY2 * size2D) + 0.5d);
            } else {
                int i3 = (int) size2D;
                if (size2D == i3 && i3 >= 8 && i3 < 17) {
                    fontInfo.glyphTx = textTxArr[i3];
                    affineTransform2 = textAtArr[i3];
                    fontInfo.pixelHeight = i3;
                } else {
                    fontInfo.pixelHeight = (int) (size2D + 0.5d);
                }
                if (affineTransform2 == null) {
                    fontInfo.glyphTx = new double[]{size2D, 0.0d, 0.0d, size2D};
                    affineTransform2 = new AffineTransform(fontInfo.glyphTx);
                }
                fontInfo.devTx = IDENT_MATRIX;
                affineTransform = IDENT_ATX;
            }
        }
        fontInfo.font2D = FontUtilities.getFont2D(font);
        int i4 = this.fractionalMetricsHint;
        if (i4 == 0) {
            i4 = 1;
        }
        fontInfo.lcdSubPixPos = false;
        if (fontRenderContext == null) {
            index = this.textAntialiasHint;
        } else {
            index = ((SunHints.Value) fontRenderContext.getAntiAliasingHint()).getIndex();
        }
        if (index == 0) {
            if (this.antialiasHint == 2) {
                index = 2;
            } else {
                index = 1;
            }
        } else if (index == 3) {
            if (fontInfo.font2D.useAAForPtSize(fontInfo.pixelHeight)) {
                index = 2;
            } else {
                index = 1;
            }
        } else if (index >= 4) {
            if (!this.surfaceData.canRenderLCDText(this)) {
                index = 2;
            } else {
                fontInfo.lcdRGBOrder = true;
                if (index == 5) {
                    index = 4;
                    fontInfo.lcdRGBOrder = false;
                } else if (index == 7) {
                    index = 6;
                    fontInfo.lcdRGBOrder = false;
                }
                fontInfo.lcdSubPixPos = i4 == 2 && index == 4;
            }
        }
        if (FontUtilities.isMacOSX14 && index == 1) {
            index = 2;
        }
        fontInfo.aaHint = index;
        fontInfo.fontStrike = fontInfo.font2D.getStrike(font, affineTransform, affineTransform2, index, i4);
        return fontInfo;
    }

    public static boolean isRotated(double[] dArr) {
        if (dArr[0] == dArr[3] && dArr[1] == 0.0d && dArr[2] == 0.0d && dArr[0] > 0.0d) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0052  */
    @Override // java.awt.Graphics
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setFont(java.awt.Font r5) {
        /*
            r4 = this;
            r0 = r5
            if (r0 == 0) goto L68
            r0 = r5
            r1 = r4
            java.awt.Font r1 = r1.font
            if (r0 == r1) goto L68
            r0 = r4
            int r0 = r0.textAntialiasHint
            r1 = 3
            if (r0 != r1) goto L59
            r0 = r4
            sun.java2d.pipe.TextPipe r0 = r0.textpipe
            sun.java2d.pipe.ValidatePipe r1 = sun.java2d.SunGraphics2D.invalidpipe
            if (r0 == r1) goto L59
            r0 = r4
            int r0 = r0.transformState
            r1 = 2
            if (r0 > r1) goto L52
            r0 = r5
            boolean r0 = r0.isTransformed()
            if (r0 != 0) goto L52
            r0 = r4
            sun.java2d.loops.FontInfo r0 = r0.fontInfo
            if (r0 == 0) goto L52
            r0 = r4
            sun.java2d.loops.FontInfo r0 = r0.fontInfo
            int r0 = r0.aaHint
            r1 = 2
            if (r0 != r1) goto L43
            r0 = 1
            goto L44
        L43:
            r0 = 0
        L44:
            r1 = r5
            sun.font.Font2D r1 = sun.font.FontUtilities.getFont2D(r1)
            r2 = r5
            int r2 = r2.getSize()
            boolean r1 = r1.useAAForPtSize(r2)
            if (r0 == r1) goto L59
        L52:
            r0 = r4
            sun.java2d.pipe.ValidatePipe r1 = sun.java2d.SunGraphics2D.invalidpipe
            r0.textpipe = r1
        L59:
            r0 = r4
            r1 = r5
            r0.font = r1
            r0 = r4
            r1 = 0
            r0.fontMetrics = r1
            r0 = r4
            r1 = 0
            r0.validFontInfo = r1
        L68:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.java2d.SunGraphics2D.setFont(java.awt.Font):void");
    }

    public FontInfo getFontInfo() {
        if (!this.validFontInfo) {
            this.fontInfo = checkFontInfo(this.fontInfo, this.font, null);
            this.validFontInfo = true;
        }
        return this.fontInfo;
    }

    public FontInfo getGVFontInfo(Font font, FontRenderContext fontRenderContext) {
        if (this.glyphVectorFontInfo != null && this.glyphVectorFontInfo.font == font && this.glyphVectorFRC == fontRenderContext) {
            return this.glyphVectorFontInfo;
        }
        this.glyphVectorFRC = fontRenderContext;
        FontInfo fontInfoCheckFontInfo = checkFontInfo(this.glyphVectorFontInfo, font, fontRenderContext);
        this.glyphVectorFontInfo = fontInfoCheckFontInfo;
        return fontInfoCheckFontInfo;
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics() {
        if (this.fontMetrics != null) {
            return this.fontMetrics;
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(this.font, getFontRenderContext());
        this.fontMetrics = metrics;
        return metrics;
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics(Font font) {
        if (this.fontMetrics != null && font == this.font) {
            return this.fontMetrics;
        }
        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font, getFontRenderContext());
        if (this.font == font) {
            this.fontMetrics = metrics;
        }
        return metrics;
    }

    @Override // java.awt.Graphics2D
    public boolean hit(Rectangle rectangle, Shape shape, boolean z2) {
        if (z2) {
            shape = this.stroke.createStrokedShape(shape);
        }
        Shape shapeTransformShape = transformShape(shape);
        if ((this.constrainX | this.constrainY) != 0) {
            rectangle = new Rectangle(rectangle);
            rectangle.translate(this.constrainX, this.constrainY);
        }
        return shapeTransformShape.intersects(rectangle);
    }

    public ColorModel getDeviceColorModel() {
        return this.surfaceData.getColorModel();
    }

    @Override // java.awt.Graphics2D
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.surfaceData.getDeviceConfiguration();
    }

    public final SurfaceData getSurfaceData() {
        return this.surfaceData;
    }

    @Override // java.awt.Graphics2D
    public void setComposite(Composite composite) {
        int i2;
        CompositeType compositeTypeForAlphaComposite;
        if (this.composite == composite) {
            return;
        }
        if (composite instanceof AlphaComposite) {
            compositeTypeForAlphaComposite = CompositeType.forAlphaComposite((AlphaComposite) composite);
            if (compositeTypeForAlphaComposite == CompositeType.SrcOverNoEa) {
                if (this.paintState == 0 || (this.paintState > 1 && this.paint.getTransparency() == 1)) {
                    i2 = 0;
                } else {
                    i2 = 1;
                }
            } else if (compositeTypeForAlphaComposite == CompositeType.SrcNoEa || compositeTypeForAlphaComposite == CompositeType.Src || compositeTypeForAlphaComposite == CompositeType.Clear) {
                i2 = 0;
            } else if (this.surfaceData.getTransparency() == 1 && compositeTypeForAlphaComposite == CompositeType.SrcIn) {
                i2 = 0;
            } else {
                i2 = 1;
            }
        } else if (composite instanceof XORComposite) {
            i2 = 2;
            compositeTypeForAlphaComposite = CompositeType.Xor;
        } else {
            if (composite == null) {
                throw new IllegalArgumentException("null Composite");
            }
            this.surfaceData.checkCustomComposite();
            i2 = 3;
            compositeTypeForAlphaComposite = CompositeType.General;
        }
        if (this.compositeState != i2 || this.imageComp != compositeTypeForAlphaComposite) {
            this.compositeState = i2;
            this.imageComp = compositeTypeForAlphaComposite;
            invalidatePipe();
            this.validFontInfo = false;
        }
        this.composite = composite;
        if (this.paintState <= 1) {
            validateColor();
        }
    }

    @Override // java.awt.Graphics2D
    public void setPaint(Paint paint) {
        if (paint instanceof Color) {
            setColor((Color) paint);
            return;
        }
        if (paint == null || this.paint == paint) {
            return;
        }
        this.paint = paint;
        if (this.imageComp == CompositeType.SrcOverNoEa) {
            if (paint.getTransparency() == 1) {
                if (this.compositeState != 0) {
                    this.compositeState = 0;
                }
            } else if (this.compositeState == 0) {
                this.compositeState = 1;
            }
        }
        Class<?> cls = paint.getClass();
        if (cls == GradientPaint.class) {
            this.paintState = 2;
        } else if (cls == LinearGradientPaint.class) {
            this.paintState = 3;
        } else if (cls == RadialGradientPaint.class) {
            this.paintState = 4;
        } else if (cls == TexturePaint.class) {
            this.paintState = 5;
        } else {
            this.paintState = 6;
        }
        this.validFontInfo = false;
        invalidatePipe();
    }

    private void validateBasicStroke(BasicStroke basicStroke) {
        double dSqrt;
        boolean z2 = this.antialiasHint == 2;
        if (this.transformState < 3) {
            if (z2) {
                if (basicStroke.getLineWidth() <= MinPenSizeAA) {
                    if (basicStroke.getDashArray() == null) {
                        this.strokeState = 0;
                        return;
                    } else {
                        this.strokeState = 1;
                        return;
                    }
                }
                this.strokeState = 2;
                return;
            }
            if (basicStroke == defaultStroke) {
                this.strokeState = 0;
                return;
            }
            if (basicStroke.getLineWidth() <= 1.0f) {
                if (basicStroke.getDashArray() == null) {
                    this.strokeState = 0;
                    return;
                } else {
                    this.strokeState = 1;
                    return;
                }
            }
            this.strokeState = 2;
            return;
        }
        if ((this.transform.getType() & 36) == 0) {
            dSqrt = Math.abs(this.transform.getDeterminant());
        } else {
            double scaleX = this.transform.getScaleX();
            double shearX = this.transform.getShearX();
            double shearY = this.transform.getShearY();
            double scaleY = this.transform.getScaleY();
            double d2 = (scaleX * scaleX) + (shearY * shearY);
            double d3 = 2.0d * ((scaleX * shearX) + (shearY * scaleY));
            double d4 = (shearX * shearX) + (scaleY * scaleY);
            dSqrt = ((d2 + d4) + Math.sqrt((d3 * d3) + ((d2 - d4) * (d2 - d4)))) / 2.0d;
        }
        if (basicStroke != defaultStroke) {
            dSqrt *= basicStroke.getLineWidth() * basicStroke.getLineWidth();
        }
        if (dSqrt <= (z2 ? MinPenSizeAASquared : 1.000000001d)) {
            if (basicStroke.getDashArray() == null) {
                this.strokeState = 0;
                return;
            } else {
                this.strokeState = 1;
                return;
            }
        }
        this.strokeState = 2;
    }

    @Override // java.awt.Graphics2D
    public void setStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("null Stroke");
        }
        int i2 = this.strokeState;
        this.stroke = stroke;
        if (stroke instanceof BasicStroke) {
            validateBasicStroke((BasicStroke) stroke);
        } else {
            this.strokeState = 3;
        }
        if (this.strokeState != i2) {
            invalidatePipe();
        }
    }

    @Override // java.awt.Graphics2D
    public void setRenderingHint(RenderingHints.Key key, Object obj) {
        int index;
        boolean z2;
        int i2;
        if (!key.isCompatibleValue(obj)) {
            throw new IllegalArgumentException(obj + " is not compatible with " + ((Object) key));
        }
        if (key instanceof SunHints.Key) {
            boolean z3 = false;
            boolean z4 = true;
            SunHints.Key key2 = (SunHints.Key) key;
            if (key2 == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST) {
                index = ((Integer) obj).intValue();
            } else {
                index = ((SunHints.Value) obj).getIndex();
            }
            switch (key2.getIndex()) {
                case 0:
                    z2 = this.renderHint != index;
                    if (z2) {
                        this.renderHint = index;
                        if (this.interpolationHint == -1) {
                            this.interpolationType = index == 2 ? 2 : 1;
                            break;
                        }
                    }
                    break;
                case 1:
                    z2 = this.antialiasHint != index;
                    this.antialiasHint = index;
                    if (z2) {
                        z3 = this.textAntialiasHint == 0;
                        if (this.strokeState != 3) {
                            validateBasicStroke((BasicStroke) this.stroke);
                            break;
                        }
                    }
                    break;
                case 2:
                    z2 = this.textAntialiasHint != index;
                    z3 = z2;
                    this.textAntialiasHint = index;
                    break;
                case 3:
                    z2 = this.fractionalMetricsHint != index;
                    z3 = z2;
                    this.fractionalMetricsHint = index;
                    break;
                case 5:
                    this.interpolationHint = index;
                    switch (index) {
                        case 0:
                        default:
                            i2 = 1;
                            break;
                        case 1:
                            i2 = 2;
                            break;
                        case 2:
                            i2 = 3;
                            break;
                    }
                    z2 = this.interpolationType != i2;
                    this.interpolationType = i2;
                    break;
                case 8:
                    z2 = this.strokeHint != index;
                    this.strokeHint = index;
                    break;
                case 9:
                    z2 = this.resolutionVariantHint != index;
                    this.resolutionVariantHint = index;
                    break;
                case 100:
                    z2 = false;
                    this.lcdTextContrast = index;
                    break;
                default:
                    z4 = false;
                    z2 = false;
                    break;
            }
            if (z4) {
                if (z2) {
                    invalidatePipe();
                    if (z3) {
                        this.fontMetrics = null;
                        this.cachedFRC = null;
                        this.validFontInfo = false;
                        this.glyphVectorFontInfo = null;
                    }
                }
                if (this.hints != null) {
                    this.hints.put(key, obj);
                    return;
                }
                return;
            }
        }
        if (this.hints == null) {
            this.hints = makeHints(null);
        }
        this.hints.put(key, obj);
    }

    @Override // java.awt.Graphics2D
    public Object getRenderingHint(RenderingHints.Key key) {
        if (this.hints != null) {
            return this.hints.get(key);
        }
        if (!(key instanceof SunHints.Key)) {
            return null;
        }
        switch (((SunHints.Key) key).getIndex()) {
            case 0:
                return SunHints.Value.get(0, this.renderHint);
            case 1:
                return SunHints.Value.get(1, this.antialiasHint);
            case 2:
                return SunHints.Value.get(2, this.textAntialiasHint);
            case 3:
                return SunHints.Value.get(3, this.fractionalMetricsHint);
            case 5:
                switch (this.interpolationHint) {
                    case 0:
                        return SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                    case 1:
                        return SunHints.VALUE_INTERPOLATION_BILINEAR;
                    case 2:
                        return SunHints.VALUE_INTERPOLATION_BICUBIC;
                    default:
                        return null;
                }
            case 8:
                return SunHints.Value.get(8, this.strokeHint);
            case 9:
                return SunHints.Value.get(9, this.resolutionVariantHint);
            case 100:
                return new Integer(this.lcdTextContrast);
            default:
                return null;
        }
    }

    @Override // java.awt.Graphics2D
    public void setRenderingHints(Map<?, ?> map) {
        this.hints = null;
        this.renderHint = 0;
        this.antialiasHint = 1;
        this.textAntialiasHint = 0;
        this.fractionalMetricsHint = 1;
        this.lcdTextContrast = lcdTextContrastDefaultValue;
        this.interpolationHint = -1;
        this.interpolationType = 1;
        boolean z2 = false;
        for (Object obj : map.keySet()) {
            if (obj == SunHints.KEY_RENDERING || obj == SunHints.KEY_ANTIALIASING || obj == SunHints.KEY_TEXT_ANTIALIASING || obj == SunHints.KEY_FRACTIONALMETRICS || obj == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST || obj == SunHints.KEY_STROKE_CONTROL || obj == SunHints.KEY_INTERPOLATION) {
                setRenderingHint((RenderingHints.Key) obj, map.get(obj));
            } else {
                z2 = true;
            }
        }
        if (z2) {
            this.hints = makeHints(map);
        }
        invalidatePipe();
    }

    @Override // java.awt.Graphics2D
    public void addRenderingHints(Map<?, ?> map) {
        boolean z2 = false;
        for (Object obj : map.keySet()) {
            if (obj == SunHints.KEY_RENDERING || obj == SunHints.KEY_ANTIALIASING || obj == SunHints.KEY_TEXT_ANTIALIASING || obj == SunHints.KEY_FRACTIONALMETRICS || obj == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST || obj == SunHints.KEY_STROKE_CONTROL || obj == SunHints.KEY_INTERPOLATION) {
                setRenderingHint((RenderingHints.Key) obj, map.get(obj));
            } else {
                z2 = true;
            }
        }
        if (z2) {
            if (this.hints == null) {
                this.hints = makeHints(map);
            } else {
                this.hints.putAll(map);
            }
        }
    }

    @Override // java.awt.Graphics2D
    public RenderingHints getRenderingHints() {
        if (this.hints == null) {
            return makeHints(null);
        }
        return (RenderingHints) this.hints.clone();
    }

    RenderingHints makeHints(Map map) {
        Object obj;
        RenderingHints renderingHints = new RenderingHints(map);
        renderingHints.put(SunHints.KEY_RENDERING, SunHints.Value.get(0, this.renderHint));
        renderingHints.put(SunHints.KEY_ANTIALIASING, SunHints.Value.get(1, this.antialiasHint));
        renderingHints.put(SunHints.KEY_TEXT_ANTIALIASING, SunHints.Value.get(2, this.textAntialiasHint));
        renderingHints.put(SunHints.KEY_FRACTIONALMETRICS, SunHints.Value.get(3, this.fractionalMetricsHint));
        renderingHints.put(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, Integer.valueOf(this.lcdTextContrast));
        switch (this.interpolationHint) {
            case 0:
                obj = SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                break;
            case 1:
                obj = SunHints.VALUE_INTERPOLATION_BILINEAR;
                break;
            case 2:
                obj = SunHints.VALUE_INTERPOLATION_BICUBIC;
                break;
            default:
                obj = null;
                break;
        }
        if (obj != null) {
            renderingHints.put(SunHints.KEY_INTERPOLATION, obj);
        }
        renderingHints.put(SunHints.KEY_STROKE_CONTROL, SunHints.Value.get(8, this.strokeHint));
        return renderingHints;
    }

    @Override // java.awt.Graphics2D
    public void translate(double d2, double d3) {
        this.transform.translate(d2, d3);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void rotate(double d2) {
        this.transform.rotate(d2);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void rotate(double d2, double d3, double d4) {
        this.transform.rotate(d2, d3, d4);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void scale(double d2, double d3) {
        this.transform.scale(d2, d3);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void shear(double d2, double d3) {
        this.transform.shear(d2, d3);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void transform(AffineTransform affineTransform) {
        this.transform.concatenate(affineTransform);
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void translate(int i2, int i3) {
        this.transform.translate(i2, i3);
        if (this.transformState <= 1) {
            this.transX += i2;
            this.transY += i3;
            this.transformState = (this.transX | this.transY) == 0 ? 0 : 1;
            return;
        }
        invalidateTransform();
    }

    @Override // java.awt.Graphics2D
    public void setTransform(AffineTransform affineTransform) {
        if ((this.constrainX | this.constrainY) == 0 && this.devScale == 1) {
            this.transform.setTransform(affineTransform);
        } else {
            this.transform.setTransform(this.devScale, 0.0d, 0.0d, this.devScale, this.constrainX, this.constrainY);
            this.transform.concatenate(affineTransform);
        }
        invalidateTransform();
    }

    protected void invalidateTransform() {
        int type = this.transform.getType();
        int i2 = this.transformState;
        if (type == 0) {
            this.transformState = 0;
            this.transY = 0;
            this.transX = 0;
        } else if (type == 1) {
            double translateX = this.transform.getTranslateX();
            double translateY = this.transform.getTranslateY();
            this.transX = (int) Math.floor(translateX + 0.5d);
            this.transY = (int) Math.floor(translateY + 0.5d);
            if (translateX == this.transX && translateY == this.transY) {
                this.transformState = 1;
            } else {
                this.transformState = 2;
            }
        } else if ((type & 120) == 0) {
            this.transformState = 3;
            this.transY = 0;
            this.transX = 0;
        } else {
            this.transformState = 4;
            this.transY = 0;
            this.transX = 0;
        }
        if (this.transformState >= 3 || i2 >= 3) {
            this.cachedFRC = null;
            this.validFontInfo = false;
            this.fontMetrics = null;
            this.glyphVectorFontInfo = null;
            if (this.transformState != i2) {
                invalidatePipe();
            }
        }
        if (this.strokeState != 3) {
            validateBasicStroke((BasicStroke) this.stroke);
        }
    }

    @Override // java.awt.Graphics2D
    public AffineTransform getTransform() {
        if ((this.constrainX | this.constrainY) == 0 && this.devScale == 1) {
            return new AffineTransform(this.transform);
        }
        double d2 = 1.0d / this.devScale;
        AffineTransform affineTransform = new AffineTransform(d2, 0.0d, 0.0d, d2, (-this.constrainX) * d2, (-this.constrainY) * d2);
        affineTransform.concatenate(this.transform);
        return affineTransform;
    }

    public AffineTransform cloneTransform() {
        return new AffineTransform(this.transform);
    }

    @Override // java.awt.Graphics2D
    public Paint getPaint() {
        return this.paint;
    }

    @Override // java.awt.Graphics2D
    public Composite getComposite() {
        return this.composite;
    }

    @Override // java.awt.Graphics
    public Color getColor() {
        return this.foregroundColor;
    }

    final void validateColor() {
        int rgb;
        if (this.imageComp == CompositeType.Clear) {
            rgb = 0;
        } else {
            rgb = this.foregroundColor.getRGB();
            if (this.compositeState <= 1 && this.imageComp != CompositeType.SrcNoEa && this.imageComp != CompositeType.SrcOverNoEa) {
                rgb = (rgb & 16777215) | (Math.round(((AlphaComposite) this.composite).getAlpha() * (rgb >>> 24)) << 24);
            }
        }
        this.eargb = rgb;
        this.pixel = this.surfaceData.pixelFor(rgb);
    }

    @Override // java.awt.Graphics
    public void setColor(Color color) {
        if (color == null || color == this.paint) {
            return;
        }
        this.foregroundColor = color;
        this.paint = color;
        validateColor();
        if ((this.eargb >> 24) == -1) {
            if (this.paintState == 0) {
                return;
            }
            this.paintState = 0;
            if (this.imageComp == CompositeType.SrcOverNoEa) {
                this.compositeState = 0;
            }
        } else {
            if (this.paintState == 1) {
                return;
            }
            this.paintState = 1;
            if (this.imageComp == CompositeType.SrcOverNoEa) {
                this.compositeState = 1;
            }
        }
        this.validFontInfo = false;
        invalidatePipe();
    }

    @Override // java.awt.Graphics2D
    public void setBackground(Color color) {
        this.backgroundColor = color;
    }

    @Override // java.awt.Graphics2D
    public Color getBackground() {
        return this.backgroundColor;
    }

    @Override // java.awt.Graphics2D
    public Stroke getStroke() {
        return this.stroke;
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds() {
        if (this.clipState == 0) {
            return null;
        }
        return getClipBounds(new Rectangle());
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds(Rectangle rectangle) {
        if (this.clipState != 0) {
            if (this.transformState <= 1) {
                if (this.usrClip instanceof Rectangle) {
                    rectangle.setBounds((Rectangle) this.usrClip);
                } else {
                    rectangle.setFrame(this.usrClip.getBounds2D());
                }
                rectangle.translate(-this.transX, -this.transY);
            } else {
                rectangle.setFrame(getClip().getBounds2D());
            }
        } else if (rectangle == null) {
            throw new NullPointerException("null rectangle parameter");
        }
        return rectangle;
    }

    @Override // java.awt.Graphics
    public boolean hitClip(int i2, int i3, int i4, int i5) {
        int iFloor;
        int iFloor2;
        int iCeil;
        int iCeil2;
        if (i4 <= 0 || i5 <= 0) {
            return false;
        }
        if (this.transformState > 1) {
            double[] dArr = {i2, i3, i2 + i4, i3, i2, i3 + i5, i2 + i4, i3 + i5};
            this.transform.transform(dArr, 0, dArr, 0, 4);
            iFloor = (int) Math.floor(Math.min(Math.min(dArr[0], dArr[2]), Math.min(dArr[4], dArr[6])));
            iFloor2 = (int) Math.floor(Math.min(Math.min(dArr[1], dArr[3]), Math.min(dArr[5], dArr[7])));
            iCeil = (int) Math.ceil(Math.max(Math.max(dArr[0], dArr[2]), Math.max(dArr[4], dArr[6])));
            iCeil2 = (int) Math.ceil(Math.max(Math.max(dArr[1], dArr[3]), Math.max(dArr[5], dArr[7])));
        } else {
            iFloor = i2 + this.transX;
            iFloor2 = i3 + this.transY;
            iCeil = i4 + iFloor;
            iCeil2 = i5 + iFloor2;
        }
        try {
            if (!getCompClip().intersectsQuickCheckXYXY(iFloor, iFloor2, iCeil, iCeil2)) {
                return false;
            }
            return true;
        } catch (InvalidPipeException e2) {
            return false;
        }
    }

    protected void validateCompClip() {
        int i2 = this.clipState;
        if (this.usrClip == null) {
            this.clipState = 0;
            this.clipRegion = this.devClip;
        } else if (this.usrClip instanceof Rectangle2D) {
            this.clipState = 1;
            if (this.usrClip instanceof Rectangle) {
                this.clipRegion = this.devClip.getIntersection((Rectangle) this.usrClip);
            } else {
                this.clipRegion = this.devClip.getIntersection(this.usrClip.getBounds());
            }
        } else {
            PathIterator pathIterator = this.usrClip.getPathIterator(null);
            int[] iArr = new int[4];
            ShapeSpanIterator fillSSI = LoopPipe.getFillSSI(this);
            try {
                fillSSI.setOutputArea(this.devClip);
                fillSSI.appendPath(pathIterator);
                fillSSI.getPathBox(iArr);
                Region region = Region.getInstance(iArr);
                region.appendSpans(fillSSI);
                this.clipRegion = region;
                this.clipState = region.isRectangular() ? 1 : 2;
                fillSSI.dispose();
            } catch (Throwable th) {
                fillSSI.dispose();
                throw th;
            }
        }
        if (i2 != this.clipState) {
            if (this.clipState == 2 || i2 == 2) {
                this.validFontInfo = false;
                invalidatePipe();
            }
        }
    }

    protected Shape transformShape(Shape shape) {
        if (shape == null) {
            return null;
        }
        if (this.transformState > 1) {
            return transformShape(this.transform, shape);
        }
        return transformShape(this.transX, this.transY, shape);
    }

    public Shape untransformShape(Shape shape) {
        if (shape == null) {
            return null;
        }
        if (this.transformState > 1) {
            try {
                return transformShape(this.transform.createInverse(), shape);
            } catch (NoninvertibleTransformException e2) {
                return null;
            }
        }
        return transformShape(-this.transX, -this.transY, shape);
    }

    protected static Shape transformShape(int i2, int i3, Shape shape) {
        if (shape == null) {
            return null;
        }
        if (shape instanceof Rectangle) {
            Rectangle bounds = shape.getBounds();
            bounds.translate(i2, i3);
            return bounds;
        }
        if (shape instanceof Rectangle2D) {
            Rectangle2D rectangle2D = (Rectangle2D) shape;
            return new Rectangle2D.Double(rectangle2D.getX() + i2, rectangle2D.getY() + i3, rectangle2D.getWidth(), rectangle2D.getHeight());
        }
        if (i2 == 0 && i3 == 0) {
            return cloneShape(shape);
        }
        return AffineTransform.getTranslateInstance(i2, i3).createTransformedShape(shape);
    }

    protected static Shape transformShape(AffineTransform affineTransform, Shape shape) {
        if (shape == null) {
            return null;
        }
        if ((shape instanceof Rectangle2D) && (affineTransform.getType() & 48) == 0) {
            Rectangle2D rectangle2D = (Rectangle2D) shape;
            double[] dArr = new double[4];
            dArr[0] = rectangle2D.getX();
            dArr[1] = rectangle2D.getY();
            dArr[2] = dArr[0] + rectangle2D.getWidth();
            dArr[3] = dArr[1] + rectangle2D.getHeight();
            affineTransform.transform(dArr, 0, dArr, 0, 2);
            fixRectangleOrientation(dArr, rectangle2D);
            return new Rectangle2D.Double(dArr[0], dArr[1], dArr[2] - dArr[0], dArr[3] - dArr[1]);
        }
        if (affineTransform.isIdentity()) {
            return cloneShape(shape);
        }
        return affineTransform.createTransformedShape(shape);
    }

    private static void fixRectangleOrientation(double[] dArr, Rectangle2D rectangle2D) {
        if ((rectangle2D.getWidth() > 0.0d) != (dArr[2] - dArr[0] > 0.0d)) {
            double d2 = dArr[0];
            dArr[0] = dArr[2];
            dArr[2] = d2;
        }
        if ((rectangle2D.getHeight() > 0.0d) != (dArr[3] - dArr[1] > 0.0d)) {
            double d3 = dArr[1];
            dArr[1] = dArr[3];
            dArr[3] = d3;
        }
    }

    @Override // java.awt.Graphics
    public void clipRect(int i2, int i3, int i4, int i5) {
        clip(new Rectangle(i2, i3, i4, i5));
    }

    @Override // java.awt.Graphics
    public void setClip(int i2, int i3, int i4, int i5) {
        setClip(new Rectangle(i2, i3, i4, i5));
    }

    @Override // java.awt.Graphics
    public Shape getClip() {
        return untransformShape(this.usrClip);
    }

    @Override // java.awt.Graphics
    public void setClip(Shape shape) {
        this.usrClip = transformShape(shape);
        validateCompClip();
    }

    @Override // java.awt.Graphics2D
    public void clip(Shape shape) {
        Shape shapeTransformShape = transformShape(shape);
        if (this.usrClip != null) {
            shapeTransformShape = intersectShapes(this.usrClip, shapeTransformShape, true, true);
        }
        this.usrClip = shapeTransformShape;
        validateCompClip();
    }

    @Override // java.awt.Graphics
    public void setPaintMode() {
        setComposite(AlphaComposite.SrcOver);
    }

    @Override // java.awt.Graphics
    public void setXORMode(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("null XORColor");
        }
        setComposite(new XORComposite(color, this.surfaceData));
    }

    @Override // java.awt.Graphics
    public void copyArea(int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            try {
                doCopyArea(i2, i3, i4, i5, i6, i7);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                doCopyArea(i2, i3, i4, i5, i6, i7);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    private void doCopyArea(int i2, int i3, int i4, int i5, int i6, int i7) {
        if (i4 <= 0 || i5 <= 0) {
            return;
        }
        SurfaceData surfaceData = this.surfaceData;
        if (surfaceData.copyArea(this, i2, i3, i4, i5, i6, i7)) {
            return;
        }
        if (this.transformState > 3) {
            throw new InternalError("transformed copyArea not implemented yet");
        }
        Region compClip = getCompClip();
        Composite composite = this.composite;
        if (this.lastCAcomp != composite) {
            SurfaceType surfaceType = surfaceData.getSurfaceType();
            CompositeType compositeType = this.imageComp;
            if (CompositeType.SrcOverNoEa.equals(compositeType) && surfaceData.getTransparency() == 1) {
                compositeType = CompositeType.SrcNoEa;
            }
            this.lastCAblit = Blit.locate(surfaceType, compositeType, surfaceType);
            this.lastCAcomp = composite;
        }
        double[] dArr = {i2, i3, i2 + i4, i3 + i5, i2 + i6, i3 + i7};
        this.transform.transform(dArr, 0, dArr, 0, 3);
        int iCeil = (int) Math.ceil(dArr[0] - 0.5d);
        int iCeil2 = (int) Math.ceil(dArr[1] - 0.5d);
        int iCeil3 = ((int) Math.ceil(dArr[2] - 0.5d)) - iCeil;
        int iCeil4 = ((int) Math.ceil(dArr[3] - 0.5d)) - iCeil2;
        int iCeil5 = ((int) Math.ceil(dArr[4] - 0.5d)) - iCeil;
        int iCeil6 = ((int) Math.ceil(dArr[5] - 0.5d)) - iCeil2;
        if (iCeil3 < 0) {
            iCeil3 *= -1;
            iCeil -= iCeil3;
        }
        if (iCeil4 < 0) {
            iCeil4 *= -1;
            iCeil2 -= iCeil4;
        }
        Blit blit = this.lastCAblit;
        if (iCeil6 == 0 && iCeil5 > 0 && iCeil5 < iCeil3) {
            while (iCeil3 > 0) {
                int iMin = Math.min(iCeil3, iCeil5);
                iCeil3 -= iMin;
                int i8 = iCeil + iCeil3;
                blit.Blit(surfaceData, surfaceData, composite, compClip, i8, iCeil2, i8 + iCeil5, iCeil2 + iCeil6, iMin, iCeil4);
            }
            return;
        }
        if (iCeil6 > 0 && iCeil6 < iCeil4 && iCeil5 > (-iCeil3) && iCeil5 < iCeil3) {
            while (iCeil4 > 0) {
                int iMin2 = Math.min(iCeil4, iCeil6);
                iCeil4 -= iMin2;
                int i9 = iCeil2 + iCeil4;
                blit.Blit(surfaceData, surfaceData, composite, compClip, iCeil, i9, iCeil + iCeil5, i9 + iCeil6, iCeil3, iMin2);
            }
            return;
        }
        blit.Blit(surfaceData, surfaceData, composite, compClip, iCeil, iCeil2, iCeil + iCeil5, iCeil2 + iCeil6, iCeil3, iCeil4);
    }

    @Override // java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        try {
            try {
                this.drawpipe.drawLine(this, i2, i3, i4, i5);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.drawpipe.drawLine(this, i2, i3, i4, i5);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            try {
                this.drawpipe.drawRoundRect(this, i2, i3, i4, i5, i6, i7);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.drawpipe.drawRoundRect(this, i2, i3, i4, i5, i6, i7);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            try {
                this.fillpipe.fillRoundRect(this, i2, i3, i4, i5, i6, i7);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.fillpipe.fillRoundRect(this, i2, i3, i4, i5, i6, i7);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        try {
            try {
                this.drawpipe.drawOval(this, i2, i3, i4, i5);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.drawpipe.drawOval(this, i2, i3, i4, i5);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        try {
            try {
                this.fillpipe.fillOval(this, i2, i3, i4, i5);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.fillpipe.fillOval(this, i2, i3, i4, i5);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            try {
                this.drawpipe.drawArc(this, i2, i3, i4, i5, i6, i7);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.drawpipe.drawArc(this, i2, i3, i4, i5, i6, i7);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            try {
                this.fillpipe.fillArc(this, i2, i3, i4, i5, i6, i7);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.fillpipe.fillArc(this, i2, i3, i4, i5, i6, i7);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        try {
            try {
                this.drawpipe.drawPolyline(this, iArr, iArr2, i2);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.drawpipe.drawPolyline(this, iArr, iArr2, i2);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    @Override // java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        try {
            try {
                this.drawpipe.drawPolygon(this, iArr, iArr2, i2);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.drawpipe.drawPolygon(this, iArr, iArr2, i2);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    @Override // java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        try {
            try {
                this.fillpipe.fillPolygon(this, iArr, iArr2, i2);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.fillpipe.fillPolygon(this, iArr, iArr2, i2);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    @Override // java.awt.Graphics
    public void drawRect(int i2, int i3, int i4, int i5) {
        try {
            try {
                this.drawpipe.drawRect(this, i2, i3, i4, i5);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.drawpipe.drawRect(this, i2, i3, i4, i5);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        try {
            try {
                this.fillpipe.fillRect(this, i2, i3, i4, i5);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.fillpipe.fillRect(this, i2, i3, i4, i5);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    private void revalidateAll() {
        this.surfaceData = this.surfaceData.getReplacement();
        if (this.surfaceData == null) {
            this.surfaceData = NullSurfaceData.theInstance;
        }
        invalidatePipe();
        setDevClip(this.surfaceData.getBounds());
        if (this.paintState <= 1) {
            validateColor();
        }
        if (this.composite instanceof XORComposite) {
            setComposite(new XORComposite(((XORComposite) this.composite).getXorColor(), this.surfaceData));
        }
        validatePipe();
    }

    @Override // java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        Composite composite = this.composite;
        Paint paint = this.paint;
        setComposite(AlphaComposite.Src);
        setColor(getBackground());
        fillRect(i2, i3, i4, i5);
        setPaint(paint);
        setComposite(composite);
    }

    @Override // java.awt.Graphics2D
    public void draw(Shape shape) {
        try {
            try {
                this.shapepipe.draw(this, shape);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.shapepipe.draw(this, shape);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics2D
    public void fill(Shape shape) {
        try {
            try {
                this.shapepipe.fill(this, shape);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.shapepipe.fill(this, shape);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    private static boolean isIntegerTranslation(AffineTransform affineTransform) {
        if (affineTransform.isIdentity()) {
            return true;
        }
        if (affineTransform.getType() == 1) {
            double translateX = affineTransform.getTranslateX();
            double translateY = affineTransform.getTranslateY();
            return translateX == ((double) ((int) translateX)) && translateY == ((double) ((int) translateY));
        }
        return false;
    }

    private static int getTileIndex(int i2, int i3, int i4) {
        int i5 = i2 - i3;
        if (i5 < 0) {
            i5 += 1 - i4;
        }
        return i5 / i4;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [double[]] */
    private static Rectangle getImageRegion(RenderedImage renderedImage, Region region, AffineTransform affineTransform, AffineTransform affineTransform2, int i2, int i3) {
        Rectangle rectangleIntersection;
        Rectangle rectangle = new Rectangle(renderedImage.getMinX(), renderedImage.getMinY(), renderedImage.getWidth(), renderedImage.getHeight());
        try {
            double loX = region.getLoX();
            double hiX = region.getHiX();
            double loY = region.getLoY();
            double hiY = region.getHiY();
            ?? r0 = {loX, loY, loX, hiY, hiX, loY, hiX, hiY};
            affineTransform.inverseTransform(r0, 0, r0, 0, 4);
            affineTransform2.inverseTransform(r0, 0, r0, 0, 4);
            double d2 = r0;
            double d3 = r0[0];
            double d4 = r0;
            double d5 = r0[1];
            int i4 = 2;
            while (i4 < 8) {
                int i5 = i4;
                int i6 = i4 + 1;
                long j2 = r0[i5];
                if (j2 < d3) {
                    d3 = j2;
                } else if (j2 > d2) {
                    d2 = j2;
                }
                i4 = i6 + 1;
                long j3 = r0[i6];
                if (j3 < d5) {
                    d5 = j3;
                } else if (j3 > d4) {
                    d4 = j3;
                }
            }
            rectangleIntersection = new Rectangle(((int) d3) - i2, ((int) d5) - i3, (int) ((d2 - d3) + (2 * i2)), (int) ((d4 - d5) + (2 * i3))).intersection(rectangle);
        } catch (NoninvertibleTransformException e2) {
            rectangleIntersection = rectangle;
        }
        return rectangleIntersection;
    }

    @Override // java.awt.Graphics2D
    public void drawRenderedImage(RenderedImage renderedImage, AffineTransform affineTransform) {
        if (renderedImage == null) {
            return;
        }
        if (renderedImage instanceof BufferedImage) {
            drawImage((BufferedImage) renderedImage, affineTransform, null);
            return;
        }
        boolean z2 = this.transformState <= 1 && isIntegerTranslation(affineTransform);
        int i2 = z2 ? 0 : 3;
        try {
            Rectangle imageRegion = getImageRegion(renderedImage, getCompClip(), this.transform, affineTransform, i2, i2);
            if (imageRegion.width <= 0 || imageRegion.height <= 0) {
                return;
            }
            if (z2) {
                drawTranslatedRenderedImage(renderedImage, imageRegion, (int) affineTransform.getTranslateX(), (int) affineTransform.getTranslateY());
                return;
            }
            Raster data = renderedImage.getData(imageRegion);
            WritableRaster writableRasterCreateWritableRaster = Raster.createWritableRaster(data.getSampleModel(), data.getDataBuffer(), null);
            int minX = data.getMinX();
            int minY = data.getMinY();
            int width = data.getWidth();
            int height = data.getHeight();
            int sampleModelTranslateX = minX - data.getSampleModelTranslateX();
            int sampleModelTranslateY = minY - data.getSampleModelTranslateY();
            if (sampleModelTranslateX != 0 || sampleModelTranslateY != 0 || width != writableRasterCreateWritableRaster.getWidth() || height != writableRasterCreateWritableRaster.getHeight()) {
                writableRasterCreateWritableRaster = writableRasterCreateWritableRaster.createWritableChild(sampleModelTranslateX, sampleModelTranslateY, width, height, 0, 0, null);
            }
            AffineTransform affineTransform2 = (AffineTransform) affineTransform.clone();
            affineTransform2.translate(minX, minY);
            ColorModel colorModel = renderedImage.getColorModel();
            drawImage(new BufferedImage(colorModel, writableRasterCreateWritableRaster, colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null), affineTransform2, null);
        } catch (InvalidPipeException e2) {
        }
    }

    private boolean clipTo(Rectangle rectangle, Rectangle rectangle2) {
        int iMax = Math.max(rectangle.f12372x, rectangle2.f12372x);
        int iMin = Math.min(rectangle.f12372x + rectangle.width, rectangle2.f12372x + rectangle2.width);
        int iMax2 = Math.max(rectangle.f12373y, rectangle2.f12373y);
        int iMin2 = Math.min(rectangle.f12373y + rectangle.height, rectangle2.f12373y + rectangle2.height);
        if (iMin - iMax < 0 || iMin2 - iMax2 < 0) {
            rectangle.width = -1;
            rectangle.height = -1;
            return false;
        }
        rectangle.f12372x = iMax;
        rectangle.f12373y = iMax2;
        rectangle.width = iMin - iMax;
        rectangle.height = iMin2 - iMax2;
        return true;
    }

    private void drawTranslatedRenderedImage(RenderedImage renderedImage, Rectangle rectangle, int i2, int i3) {
        WritableRaster writableRasterCreateWritableRaster;
        int tileGridXOffset = renderedImage.getTileGridXOffset();
        int tileGridYOffset = renderedImage.getTileGridYOffset();
        int tileWidth = renderedImage.getTileWidth();
        int tileHeight = renderedImage.getTileHeight();
        int tileIndex = getTileIndex(rectangle.f12372x, tileGridXOffset, tileWidth);
        int tileIndex2 = getTileIndex(rectangle.f12373y, tileGridYOffset, tileHeight);
        int tileIndex3 = getTileIndex((rectangle.f12372x + rectangle.width) - 1, tileGridXOffset, tileWidth);
        int tileIndex4 = getTileIndex((rectangle.f12373y + rectangle.height) - 1, tileGridYOffset, tileHeight);
        ColorModel colorModel = renderedImage.getColorModel();
        Rectangle rectangle2 = new Rectangle();
        for (int i4 = tileIndex2; i4 <= tileIndex4; i4++) {
            for (int i5 = tileIndex; i5 <= tileIndex3; i5++) {
                Raster tile = renderedImage.getTile(i5, i4);
                rectangle2.f12372x = (i5 * tileWidth) + tileGridXOffset;
                rectangle2.f12373y = (i4 * tileHeight) + tileGridYOffset;
                rectangle2.width = tileWidth;
                rectangle2.height = tileHeight;
                clipTo(rectangle2, rectangle);
                if (tile instanceof WritableRaster) {
                    writableRasterCreateWritableRaster = (WritableRaster) tile;
                } else {
                    writableRasterCreateWritableRaster = Raster.createWritableRaster(tile.getSampleModel(), tile.getDataBuffer(), null);
                }
                copyImage(new BufferedImage(colorModel, writableRasterCreateWritableRaster.createWritableChild(rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, 0, 0, null), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null), rectangle2.f12372x + i2, rectangle2.f12373y + i3, 0, 0, rectangle2.width, rectangle2.height, null, null);
            }
        }
    }

    @Override // java.awt.Graphics2D
    public void drawRenderableImage(RenderableImage renderableImage, AffineTransform affineTransform) {
        AffineTransform affineTransform2;
        if (renderableImage == null) {
            return;
        }
        AffineTransform affineTransform3 = this.transform;
        AffineTransform affineTransform4 = new AffineTransform(affineTransform);
        affineTransform4.concatenate(affineTransform3);
        RenderContext renderContext = new RenderContext(affineTransform4);
        try {
            affineTransform2 = affineTransform3.createInverse();
        } catch (NoninvertibleTransformException e2) {
            renderContext = new RenderContext(affineTransform3);
            affineTransform2 = new AffineTransform();
        }
        drawRenderedImage(renderableImage.createRendering(renderContext), affineTransform2);
    }

    protected Rectangle transformBounds(Rectangle rectangle, AffineTransform affineTransform) {
        if (affineTransform.isIdentity()) {
            return rectangle;
        }
        return transformShape(affineTransform, rectangle).getBounds();
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        if (str == null) {
            throw new NullPointerException("String is null");
        }
        if (this.font.hasLayoutAttributes()) {
            if (str.length() == 0) {
                return;
            }
            new TextLayout(str, this.font, getFontRenderContext()).draw(this, i2, i3);
            return;
        }
        try {
            try {
                this.textpipe.drawString(this, str, i2, i3);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.textpipe.drawString(this, str, i2, i3);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    @Override // java.awt.Graphics2D
    public void drawString(String str, float f2, float f3) {
        if (str == null) {
            throw new NullPointerException("String is null");
        }
        try {
            if (this.font.hasLayoutAttributes()) {
                if (str.length() == 0) {
                    return;
                }
                new TextLayout(str, this.font, getFontRenderContext()).draw(this, f2, f3);
            } else {
                try {
                    this.textpipe.drawString(this, str, f2, f3);
                    this.surfaceData.markDirty();
                } catch (InvalidPipeException e2) {
                    try {
                        revalidateAll();
                        this.textpipe.drawString(this, str, f2, f3);
                    } catch (InvalidPipeException e3) {
                    }
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics2D, java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        if (attributedCharacterIterator == null) {
            throw new NullPointerException("AttributedCharacterIterator is null");
        }
        if (attributedCharacterIterator.getBeginIndex() == attributedCharacterIterator.getEndIndex()) {
            return;
        }
        new TextLayout(attributedCharacterIterator, getFontRenderContext()).draw(this, i2, i3);
    }

    @Override // java.awt.Graphics2D
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, float f2, float f3) {
        if (attributedCharacterIterator == null) {
            throw new NullPointerException("AttributedCharacterIterator is null");
        }
        if (attributedCharacterIterator.getBeginIndex() == attributedCharacterIterator.getEndIndex()) {
            return;
        }
        new TextLayout(attributedCharacterIterator, getFontRenderContext()).draw(this, f2, f3);
    }

    @Override // java.awt.Graphics2D
    public void drawGlyphVector(GlyphVector glyphVector, float f2, float f3) {
        if (glyphVector == null) {
            throw new NullPointerException("GlyphVector is null");
        }
        try {
            try {
                this.textpipe.drawGlyphVector(this, glyphVector, f2, f3);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.textpipe.drawGlyphVector(this, glyphVector, f2, f3);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    @Override // java.awt.Graphics
    public void drawChars(char[] cArr, int i2, int i3, int i4, int i5) {
        if (cArr == null) {
            throw new NullPointerException("char data is null");
        }
        if (i2 < 0 || i3 < 0 || i2 + i3 < i3 || i2 + i3 > cArr.length) {
            throw new ArrayIndexOutOfBoundsException("bad offset/length");
        }
        try {
            if (this.font.hasLayoutAttributes()) {
                if (cArr.length == 0) {
                    return;
                }
                new TextLayout(new String(cArr, i2, i3), this.font, getFontRenderContext()).draw(this, i4, i5);
            } else {
                try {
                    this.textpipe.drawChars(this, cArr, i2, i3, i4, i5);
                    this.surfaceData.markDirty();
                } catch (InvalidPipeException e2) {
                    try {
                        revalidateAll();
                        this.textpipe.drawChars(this, cArr, i2, i3, i4, i5);
                    } catch (InvalidPipeException e3) {
                    }
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public void drawBytes(byte[] bArr, int i2, int i3, int i4, int i5) {
        if (bArr == null) {
            throw new NullPointerException("byte data is null");
        }
        if (i2 < 0 || i3 < 0 || i2 + i3 < i3 || i2 + i3 > bArr.length) {
            throw new ArrayIndexOutOfBoundsException("bad offset/length");
        }
        char[] cArr = new char[i3];
        int i6 = i3;
        while (true) {
            int i7 = i6;
            i6--;
            if (i7 <= 0) {
                break;
            } else {
                cArr[i6] = (char) (bArr[i6 + i2] & 255);
            }
        }
        if (this.font.hasLayoutAttributes()) {
            if (bArr.length == 0) {
                return;
            }
            new TextLayout(new String(cArr), this.font, getFontRenderContext()).draw(this, i4, i5);
            return;
        }
        try {
            try {
                this.textpipe.drawChars(this, cArr, 0, i3, i4, i5);
                this.surfaceData.markDirty();
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                this.textpipe.drawChars(this, cArr, 0, i3, i4, i5);
            } catch (InvalidPipeException e3) {
            }
        }
    }

    private boolean isHiDPIImage(Image image) {
        return SurfaceManager.getImageScale(image) != 1 || (this.resolutionVariantHint != 1 && (image instanceof MultiResolutionImage));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean drawHiDPIImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        Image image2;
        if (SurfaceManager.getImageScale(image) != 1) {
            int imageScale = SurfaceManager.getImageScale(image);
            i6 = Region.clipScale(i6, imageScale);
            i8 = Region.clipScale(i8, imageScale);
            i7 = Region.clipScale(i7, imageScale);
            i9 = Region.clipScale(i9, imageScale);
            image2 = image;
        } else {
            boolean z2 = image instanceof MultiResolutionImage;
            image2 = image;
            if (z2) {
                int width = image.getWidth(imageObserver);
                int height = image.getHeight(imageObserver);
                Image resolutionVariant = getResolutionVariant((MultiResolutionImage) image, width, height, i2, i3, i4, i5, i6, i7, i8, i9);
                image2 = image;
                if (resolutionVariant != image) {
                    image2 = image;
                    if (resolutionVariant != null) {
                        ImageObserver resolutionVariantObserver = MultiResolutionToolkitImage.getResolutionVariantObserver(image, imageObserver, width, height, -1, -1);
                        int width2 = resolutionVariant.getWidth(resolutionVariantObserver);
                        int height2 = resolutionVariant.getHeight(resolutionVariantObserver);
                        image2 = image;
                        if (0 < width) {
                            image2 = image;
                            if (0 < height) {
                                image2 = image;
                                if (0 < width2) {
                                    image2 = image;
                                    if (0 < height2) {
                                        float f2 = width2 / width;
                                        float f3 = height2 / height;
                                        i6 = Region.clipScale(i6, f2);
                                        i7 = Region.clipScale(i7, f3);
                                        i8 = Region.clipScale(i8, f2);
                                        i9 = Region.clipScale(i9, f3);
                                        imageObserver = resolutionVariantObserver;
                                        image2 = resolutionVariant;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            try {
                boolean zScaleImage = this.imagepipe.scaleImage(this, image2, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
                this.surfaceData.markDirty();
                return zScaleImage;
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    boolean zScaleImage2 = this.imagepipe.scaleImage(this, image2, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
                    this.surfaceData.markDirty();
                    return zScaleImage2;
                } catch (InvalidPipeException e3) {
                    return false;
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    private Image getResolutionVariant(MultiResolutionImage multiResolutionImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        double dHypot;
        double dHypot2;
        if (i2 <= 0 || i3 <= 0) {
            return null;
        }
        int i12 = i10 - i8;
        int i13 = i11 - i9;
        if (i12 == 0 || i13 == 0) {
            return null;
        }
        int type = this.transform.getType();
        int i14 = i6 - i4;
        int i15 = i7 - i5;
        if ((type & (-66)) == 0) {
            dHypot = i14;
            dHypot2 = i15;
        } else if ((type & (-72)) == 0) {
            dHypot = i14 * this.transform.getScaleX();
            dHypot2 = i15 * this.transform.getScaleY();
        } else {
            dHypot = i14 * Math.hypot(this.transform.getScaleX(), this.transform.getShearY());
            dHypot2 = i15 * Math.hypot(this.transform.getShearX(), this.transform.getScaleY());
        }
        Image resolutionVariant = multiResolutionImage.getResolutionVariant((int) Math.abs((i2 * dHypot) / i12), (int) Math.abs((i3 * dHypot2) / i13));
        if ((resolutionVariant instanceof ToolkitImage) && ((ToolkitImage) resolutionVariant).hasError()) {
            return null;
        }
        return resolutionVariant;
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, i4, i5, null, imageObserver);
    }

    public boolean copyImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
        try {
            try {
                boolean zCopyImage = this.imagepipe.copyImage(this, image, i2, i3, i4, i5, i6, i7, color, imageObserver);
                this.surfaceData.markDirty();
                return zCopyImage;
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                boolean zCopyImage2 = this.imagepipe.copyImage(this, image, i2, i3, i4, i5, i6, i7, color, imageObserver);
                this.surfaceData.markDirty();
                return zCopyImage2;
            } catch (InvalidPipeException e3) {
                return false;
            }
        }
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        if (image == null || i4 == 0 || i5 == 0) {
            return true;
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (isHiDPIImage(image)) {
            return drawHiDPIImage(image, i2, i3, i2 + i4, i3 + i5, 0, 0, width, height, color, imageObserver);
        }
        if (i4 == width && i5 == height) {
            return copyImage(image, i2, i3, 0, 0, i4, i5, color, imageObserver);
        }
        try {
            try {
                boolean zScaleImage = this.imagepipe.scaleImage(this, image, i2, i3, i4, i5, color, imageObserver);
                this.surfaceData.markDirty();
                return zScaleImage;
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                boolean zScaleImage2 = this.imagepipe.scaleImage(this, image, i2, i3, i4, i5, color, imageObserver);
                this.surfaceData.markDirty();
                return zScaleImage2;
            } catch (InvalidPipeException e3) {
                return false;
            }
        }
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, null, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        try {
            if (isHiDPIImage(image)) {
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                return drawHiDPIImage(image, i2, i3, i2 + width, i3 + height, 0, 0, width, height, color, imageObserver);
            }
            try {
                boolean zCopyImage = this.imagepipe.copyImage(this, image, i2, i3, color, imageObserver);
                this.surfaceData.markDirty();
                return zCopyImage;
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    boolean zCopyImage2 = this.imagepipe.copyImage(this, image, i2, i3, color, imageObserver);
                    this.surfaceData.markDirty();
                    return zCopyImage2;
                } catch (InvalidPipeException e3) {
                    return false;
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        return drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, null, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        if (image == null || i2 == i4 || i3 == i5 || i6 == i8 || i7 == i9) {
            return true;
        }
        if (isHiDPIImage(image)) {
            return drawHiDPIImage(image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
        }
        if (i8 - i6 != i4 - i2 || i9 - i7 != i5 - i3) {
            try {
                try {
                    boolean zScaleImage = this.imagepipe.scaleImage(this, image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
                    this.surfaceData.markDirty();
                    return zScaleImage;
                } finally {
                    this.surfaceData.markDirty();
                }
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    boolean zScaleImage2 = this.imagepipe.scaleImage(this, image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
                    this.surfaceData.markDirty();
                    return zScaleImage2;
                } catch (InvalidPipeException e3) {
                    return false;
                }
            }
        }
        if (i8 > i6) {
            i10 = i8 - i6;
            i11 = i6;
            i12 = i2;
        } else {
            i10 = i6 - i8;
            i11 = i8;
            i12 = i4;
        }
        if (i9 > i7) {
            i13 = i9 - i7;
            i14 = i7;
            i15 = i3;
        } else {
            i13 = i7 - i9;
            i14 = i9;
            i15 = i5;
        }
        return copyImage(image, i12, i15, i11, i14, i10, i13, color, imageObserver);
    }

    @Override // java.awt.Graphics2D
    public boolean drawImage(Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        if (image == null) {
            return true;
        }
        if (affineTransform == null || affineTransform.isIdentity()) {
            return drawImage(image, 0, 0, null, imageObserver);
        }
        if (isHiDPIImage(image)) {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            AffineTransform affineTransform2 = new AffineTransform(this.transform);
            transform(affineTransform);
            boolean zDrawHiDPIImage = drawHiDPIImage(image, 0, 0, width, height, 0, 0, width, height, null, imageObserver);
            this.transform.setTransform(affineTransform2);
            invalidateTransform();
            return zDrawHiDPIImage;
        }
        try {
            try {
                boolean zTransformImage = this.imagepipe.transformImage(this, image, affineTransform, imageObserver);
                this.surfaceData.markDirty();
                return zTransformImage;
            } finally {
                this.surfaceData.markDirty();
            }
        } catch (InvalidPipeException e2) {
            try {
                revalidateAll();
                boolean zTransformImage2 = this.imagepipe.transformImage(this, image, affineTransform, imageObserver);
                this.surfaceData.markDirty();
                return zTransformImage2;
            } catch (InvalidPipeException e3) {
                return false;
            }
        }
    }

    @Override // java.awt.Graphics2D
    public void drawImage(BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        try {
            if (bufferedImage == null) {
                return;
            }
            try {
                this.imagepipe.transformImage(this, bufferedImage, bufferedImageOp, i2, i3);
                this.surfaceData.markDirty();
            } catch (InvalidPipeException e2) {
                try {
                    revalidateAll();
                    this.imagepipe.transformImage(this, bufferedImage, bufferedImageOp, i2, i3);
                } catch (InvalidPipeException e3) {
                }
            }
        } finally {
            this.surfaceData.markDirty();
        }
    }

    @Override // java.awt.Graphics2D
    public FontRenderContext getFontRenderContext() {
        if (this.cachedFRC == null) {
            int i2 = this.textAntialiasHint;
            if (i2 == 0 && this.antialiasHint == 2) {
                i2 = 2;
            }
            AffineTransform affineTransform = null;
            if (this.transformState >= 3) {
                if (this.transform.getTranslateX() == 0.0d && this.transform.getTranslateY() == 0.0d) {
                    affineTransform = this.transform;
                } else {
                    affineTransform = new AffineTransform(this.transform.getScaleX(), this.transform.getShearY(), this.transform.getShearX(), this.transform.getScaleY(), 0.0d, 0.0d);
                }
            }
            this.cachedFRC = new FontRenderContext(affineTransform, SunHints.Value.get(2, i2), SunHints.Value.get(3, this.fractionalMetricsHint));
        }
        return this.cachedFRC;
    }

    @Override // java.awt.Graphics
    public void dispose() {
        this.surfaceData = NullSurfaceData.theInstance;
        invalidatePipe();
    }

    @Override // java.awt.Graphics
    public void finalize() {
    }

    public Object getDestination() {
        return this.surfaceData.getDestination();
    }

    @Override // sun.java2d.DestSurfaceProvider
    public Surface getDestSurface() {
        return this.surfaceData;
    }
}
