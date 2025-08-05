package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.paint.MultipleGradientPaint;
import com.sun.prism.j2d.paint.RadialGradientPaint;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics.class */
public class J2DPrismGraphics implements ReadbackGraphics, MaskTextureGraphics {
    private int clipRectIndex;
    private boolean hasPreCullingBits;
    private float pixelScale;
    private boolean antialiasedShape;
    J2DPresentable target;
    Graphics2D g2d;
    Affine2D transform;
    Rectangle clipRect;
    RectBounds devClipRect;
    RectBounds finalClipRect;
    Paint paint;
    boolean paintWasProportional;
    BasicStroke stroke;
    boolean cull;
    Rectangle2D nodeBounds;
    private NodePath renderRoot;
    static final MultipleGradientPaint.CycleMethod[] LGP_CYCLE_METHODS = {MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.CycleMethod.REFLECT, MultipleGradientPaint.CycleMethod.REPEAT};
    static final MultipleGradientPaint.CycleMethod[] RGP_CYCLE_METHODS = {MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.CycleMethod.REFLECT, MultipleGradientPaint.CycleMethod.REPEAT};
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    static AffineTransform J2D_IDENTITY = new AffineTransform();
    private static ConcurrentHashMap<Font, WeakReference<Font>> fontMap = new ConcurrentHashMap<>();
    private static volatile int cleared = 0;
    private static AffineTransform tmpAT = new AffineTransform();
    private static Path2D tmpQuadShape = new Path2D.Float();
    private static Rectangle2D.Float tmpRect = new Rectangle2D.Float();
    private static Ellipse2D tmpEllipse = new Ellipse2D.Float();
    private static RoundRectangle2D tmpRRect = new RoundRectangle2D.Float();
    private static Line2D tmpLine = new Line2D.Float();
    private static AdaptorShape tmpAdaptor = new AdaptorShape();

    static java.awt.Color toJ2DColor(Color c2) {
        return new java.awt.Color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
    }

    static int fixFractions(float[] fractions, java.awt.Color[] colors) {
        float fprev = fractions[0];
        int i2 = 1;
        int n2 = 1;
        while (i2 < fractions.length) {
            float f2 = fractions[i2];
            int i3 = i2;
            i2++;
            java.awt.Color c2 = colors[i3];
            if (f2 <= fprev) {
                if (f2 >= 1.0f) {
                    break;
                }
                f2 = fprev + Math.ulp(fprev);
                while (i2 < fractions.length && fractions[i2] <= f2) {
                    int i4 = i2;
                    i2++;
                    c2 = colors[i4];
                }
            }
            float f3 = f2;
            fprev = f3;
            fractions[n2] = f3;
            int i5 = n2;
            n2++;
            colors[i5] = c2;
        }
        return n2;
    }

    java.awt.Paint toJ2DPaint(Paint p2, Rectangle2D b2) {
        int n2;
        if (p2 instanceof Color) {
            return toJ2DColor((Color) p2);
        }
        if (p2 instanceof Gradient) {
            Gradient g2 = (Gradient) p2;
            if (g2.isProportional() && b2 == null) {
                return null;
            }
            List<Stop> stops = g2.getStops();
            int n3 = stops.size();
            float[] fractions = new float[n3];
            java.awt.Color[] colors = new java.awt.Color[n3];
            float prevf = -1.0f;
            boolean needsFix = false;
            for (int i2 = 0; i2 < n3; i2++) {
                Stop stop = stops.get(i2);
                float f2 = stop.getOffset();
                needsFix = needsFix || f2 <= prevf;
                prevf = f2;
                fractions[i2] = f2;
                colors[i2] = toJ2DColor(stop.getColor());
            }
            if (needsFix && (n2 = fixFractions(fractions, colors)) < fractions.length) {
                float[] newf = new float[n2];
                System.arraycopy(fractions, 0, newf, 0, n2);
                fractions = newf;
                java.awt.Color[] newc = new java.awt.Color[n2];
                System.arraycopy(colors, 0, newc, 0, n2);
                colors = newc;
            }
            if (g2 instanceof LinearGradient) {
                LinearGradient lg = (LinearGradient) p2;
                float x1 = lg.getX1();
                float y1 = lg.getY1();
                float x2 = lg.getX2();
                float y2 = lg.getY2();
                if (g2.isProportional()) {
                    float x3 = (float) b2.getX();
                    float y3 = (float) b2.getY();
                    float w2 = (float) b2.getWidth();
                    float h2 = (float) b2.getHeight();
                    x1 = x3 + (w2 * x1);
                    y1 = y3 + (h2 * y1);
                    x2 = x3 + (w2 * x2);
                    y2 = y3 + (h2 * y2);
                }
                if (x1 == x2 && y1 == y1) {
                    return colors[0];
                }
                Point2D p1 = new Point2D.Float(x1, y1);
                Point2D p22 = new Point2D.Float(x2, y2);
                MultipleGradientPaint.CycleMethod method = LGP_CYCLE_METHODS[g2.getSpreadMethod()];
                return new LinearGradientPaint(p1, p22, fractions, colors, method);
            }
            if (g2 instanceof RadialGradient) {
                RadialGradient rg = (RadialGradient) g2;
                float cx = rg.getCenterX();
                float cy = rg.getCenterY();
                float r2 = rg.getRadius();
                double fa = Math.toRadians(rg.getFocusAngle());
                float fd = rg.getFocusDistance();
                AffineTransform at2 = J2D_IDENTITY;
                if (g2.isProportional()) {
                    float x4 = (float) b2.getX();
                    float y4 = (float) b2.getY();
                    float w3 = (float) b2.getWidth();
                    float h3 = (float) b2.getHeight();
                    float dim = Math.min(w3, h3);
                    float bcx = x4 + (w3 * 0.5f);
                    float bcy = y4 + (h3 * 0.5f);
                    cx = bcx + ((cx - 0.5f) * dim);
                    cy = bcy + ((cy - 0.5f) * dim);
                    r2 *= dim;
                    if (w3 != h3 && w3 != 0.0d && h3 != 0.0d) {
                        at2 = AffineTransform.getTranslateInstance(bcx, bcy);
                        at2.scale(w3 / dim, h3 / dim);
                        at2.translate(-bcx, -bcy);
                    }
                }
                Point2D center = new Point2D.Float(cx, cy);
                float fx = (float) (cx + (fd * r2 * Math.cos(fa)));
                float fy = (float) (cy + (fd * r2 * Math.sin(fa)));
                Point2D focus = new Point2D.Float(fx, fy);
                MultipleGradientPaint.CycleMethod method2 = RGP_CYCLE_METHODS[g2.getSpreadMethod()];
                return new RadialGradientPaint(center, r2, focus, fractions, colors, method2, MultipleGradientPaint.ColorSpaceType.SRGB, at2);
            }
        } else if (p2 instanceof ImagePattern) {
            ImagePattern imgpat = (ImagePattern) p2;
            float x5 = imgpat.getX();
            float y5 = imgpat.getY();
            float w4 = imgpat.getWidth();
            float h4 = imgpat.getHeight();
            if (p2.isProportional()) {
                if (b2 == null) {
                    return null;
                }
                float bx2 = (float) b2.getX();
                float by2 = (float) b2.getY();
                float bw2 = (float) b2.getWidth();
                float bh2 = (float) b2.getHeight();
                float w5 = w4 + x5;
                float h5 = h4 + y5;
                x5 = bx2 + (x5 * bw2);
                y5 = by2 + (y5 * bh2);
                w4 = (bx2 + (w5 * bw2)) - x5;
                h4 = (by2 + (h5 * bh2)) - y5;
            }
            Texture tex = getResourceFactory().getCachedTexture(imgpat.getImage(), Texture.WrapMode.REPEAT);
            BufferedImage bimg = ((J2DTexture) tex).getBufferedImage();
            tex.unlock();
            return new TexturePaint(bimg, tmpRect(x5, y5, w4, h4));
        }
        throw new UnsupportedOperationException("Paint " + ((Object) p2) + " not supported yet.");
    }

    static Stroke toJ2DStroke(BasicStroke stroke) {
        float lineWidth = stroke.getLineWidth();
        int type = stroke.getType();
        if (type != 0) {
            lineWidth *= 2.0f;
        }
        java.awt.BasicStroke bs2 = new java.awt.BasicStroke(lineWidth, stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase());
        if (type == 1) {
            return new InnerStroke(bs2);
        }
        if (type == 2) {
            return new OuterStroke(bs2);
        }
        return bs2;
    }

    private static Font toJ2DFont(FontStrike strike) {
        Font j2dfont;
        FontResource fr = strike.getFontResource();
        Object peer = fr.getPeer();
        if (peer == null && fr.isEmbeddedFont()) {
            J2DFontFactory.registerFont(fr);
            peer = fr.getPeer();
        }
        if (peer != null && (peer instanceof Font)) {
            j2dfont = (Font) peer;
        } else {
            if (PlatformUtil.isMac()) {
                String psName = fr.getPSName();
                j2dfont = new Font(psName, 0, 12);
                if (!j2dfont.getPSName().equals(psName)) {
                    int style = fr.isBold() ? 1 : 0;
                    j2dfont = new Font(fr.getFamilyName(), style | (fr.isItalic() ? 2 : 0), 12);
                    if (!j2dfont.getPSName().equals(psName)) {
                        Font[] allj2dFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                        int length = allj2dFonts.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                break;
                            }
                            Font f2 = allj2dFonts[i2];
                            if (!f2.getPSName().equals(psName)) {
                                i2++;
                            } else {
                                j2dfont = f2;
                                break;
                            }
                        }
                    }
                }
            } else {
                j2dfont = new Font(fr.getFullName(), 0, 12);
            }
            fr.setPeer(j2dfont);
        }
        Font j2dfont2 = j2dfont.deriveFont(strike.getSize());
        Font compFont = null;
        WeakReference<Font> ref = fontMap.get(j2dfont2);
        if (ref != null) {
            compFont = ref.get();
            if (compFont == null) {
                cleared++;
            }
        }
        if (compFont == null) {
            if (fontMap.size() > 100 && cleared > 10) {
                Iterator<Font> it = fontMap.keySet().iterator();
                while (it.hasNext()) {
                    Font key = it.next();
                    WeakReference<Font> ref2 = fontMap.get(key);
                    if (ref2 == null || ref2.get() == null) {
                        fontMap.remove(key);
                    }
                }
                cleared = 0;
            }
            compFont = J2DFontFactory.getCompositeFont(j2dfont2);
            fontMap.put(j2dfont2, new WeakReference<>(compFont));
        }
        return compFont;
    }

    public static AffineTransform toJ2DTransform(BaseTransform t2) {
        return new AffineTransform(t2.getMxx(), t2.getMyx(), t2.getMxy(), t2.getMyy(), t2.getMxt(), t2.getMyt());
    }

    static AffineTransform tmpJ2DTransform(BaseTransform t2) {
        tmpAT.setTransform(t2.getMxx(), t2.getMyx(), t2.getMxy(), t2.getMyy(), t2.getMxt(), t2.getMyt());
        return tmpAT;
    }

    static BaseTransform toPrTransform(AffineTransform t2) {
        return BaseTransform.getInstance(t2.getScaleX(), t2.getShearY(), t2.getShearX(), t2.getScaleY(), t2.getTranslateX(), t2.getTranslateY());
    }

    static Rectangle toPrRect(java.awt.Rectangle r2) {
        return new Rectangle(r2.f12372x, r2.f12373y, r2.width, r2.height);
    }

    private static Shape tmpQuad(float x1, float y1, float x2, float y2) {
        tmpQuadShape.reset();
        tmpQuadShape.moveTo(x1, y1);
        tmpQuadShape.lineTo(x2, y1);
        tmpQuadShape.lineTo(x2, y2);
        tmpQuadShape.lineTo(x1, y2);
        tmpQuadShape.closePath();
        return tmpQuadShape;
    }

    private static Rectangle2D tmpRect(float x2, float y2, float w2, float h2) {
        tmpRect.setRect(x2, y2, w2, h2);
        return tmpRect;
    }

    private static Shape tmpEllipse(float x2, float y2, float w2, float h2) {
        tmpEllipse.setFrame(x2, y2, w2, h2);
        return tmpEllipse;
    }

    private static Shape tmpRRect(float x2, float y2, float w2, float h2, float aw2, float ah2) {
        tmpRRect.setRoundRect(x2, y2, w2, h2, aw2, ah2);
        return tmpRRect;
    }

    private static Shape tmpLine(float x1, float y1, float x2, float y2) {
        tmpLine.setLine(x1, y1, x2, y2);
        return tmpLine;
    }

    private static Shape tmpShape(com.sun.javafx.geom.Shape s2) {
        tmpAdaptor.setShape(s2);
        return tmpAdaptor;
    }

    J2DPrismGraphics(J2DPresentable target, Graphics2D g2d) {
        this(g2d, target.getContentWidth(), target.getContentHeight());
        this.target = target;
    }

    J2DPrismGraphics(Graphics2D g2d, int width, int height) {
        this.hasPreCullingBits = false;
        this.pixelScale = 1.0f;
        this.antialiasedShape = true;
        this.nodeBounds = null;
        this.g2d = g2d;
        captureTransform(g2d);
        this.transform = new Affine2D();
        this.devClipRect = new RectBounds(0.0f, 0.0f, width, height);
        this.finalClipRect = new RectBounds(0.0f, 0.0f, width, height);
        this.cull = true;
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        setTransform(BaseTransform.IDENTITY_TRANSFORM);
        setPaint(DEFAULT_PAINT);
        setStroke(DEFAULT_STROKE);
    }

    @Override // com.sun.prism.Graphics
    public RenderTarget getRenderTarget() {
        return this.target;
    }

    @Override // com.sun.prism.Graphics
    public Screen getAssociatedScreen() {
        return this.target.getAssociatedScreen();
    }

    @Override // com.sun.prism.Graphics
    public ResourceFactory getResourceFactory() {
        return this.target.getResourceFactory();
    }

    public void reset() {
    }

    @Override // com.sun.prism.Graphics
    public Rectangle getClipRect() {
        if (this.clipRect == null) {
            return null;
        }
        return new Rectangle(this.clipRect);
    }

    @Override // com.sun.prism.Graphics
    public Rectangle getClipRectNoClone() {
        return this.clipRect;
    }

    @Override // com.sun.prism.Graphics
    public RectBounds getFinalClipNoClone() {
        return this.finalClipRect;
    }

    @Override // com.sun.prism.Graphics
    public void setClipRect(Rectangle clipRect) {
        this.finalClipRect.setBounds(this.devClipRect);
        if (clipRect == null) {
            this.clipRect = null;
            this.g2d.setClip(null);
            return;
        }
        this.clipRect = new Rectangle(clipRect);
        this.finalClipRect.intersectWith(clipRect);
        setTransformG2D(J2D_IDENTITY);
        this.g2d.setClip(clipRect.f11913x, clipRect.f11914y, clipRect.width, clipRect.height);
        setTransformG2D(tmpJ2DTransform(this.transform));
    }

    private AlphaComposite getAWTComposite() {
        return (AlphaComposite) this.g2d.getComposite();
    }

    @Override // com.sun.prism.Graphics
    public float getExtraAlpha() {
        return getAWTComposite().getAlpha();
    }

    @Override // com.sun.prism.Graphics
    public void setExtraAlpha(float extraAlpha) {
        this.g2d.setComposite(getAWTComposite().derive(extraAlpha));
    }

    @Override // com.sun.prism.Graphics
    public CompositeMode getCompositeMode() {
        int rule = getAWTComposite().getRule();
        switch (rule) {
            case 1:
                return CompositeMode.CLEAR;
            case 2:
                return CompositeMode.SRC;
            case 3:
                return CompositeMode.SRC_OVER;
            default:
                throw new InternalError("Unrecognized AlphaCompsite rule: " + rule);
        }
    }

    @Override // com.sun.prism.Graphics
    public void setCompositeMode(CompositeMode mode) {
        AlphaComposite awtComp;
        AlphaComposite awtComp2 = getAWTComposite();
        switch (mode) {
            case CLEAR:
                awtComp = awtComp2.derive(1);
                break;
            case SRC:
                awtComp = awtComp2.derive(2);
                break;
            case SRC_OVER:
                awtComp = awtComp2.derive(3);
                break;
            default:
                throw new InternalError("Unrecognized composite mode: " + ((Object) mode));
        }
        this.g2d.setComposite(awtComp);
    }

    @Override // com.sun.prism.Graphics
    public Paint getPaint() {
        return this.paint;
    }

    @Override // com.sun.prism.Graphics
    public void setPaint(Paint paint) {
        this.paint = paint;
        java.awt.Paint j2dpaint = toJ2DPaint(paint, null);
        if (j2dpaint == null) {
            this.paintWasProportional = true;
        } else {
            this.paintWasProportional = false;
            this.g2d.setPaint(j2dpaint);
        }
    }

    @Override // com.sun.prism.Graphics
    public BasicStroke getStroke() {
        return this.stroke;
    }

    @Override // com.sun.prism.Graphics
    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
        this.g2d.setStroke(toJ2DStroke(stroke));
    }

    @Override // com.sun.prism.Graphics
    public BaseTransform getTransformNoClone() {
        return this.transform;
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty) {
        this.transform.translate(tx, ty);
        this.g2d.translate(tx, ty);
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy) {
        this.transform.scale(sx, sy);
        this.g2d.scale(sx, sy);
    }

    @Override // com.sun.prism.Graphics
    public void transform(BaseTransform xform) {
        if (!xform.is2D()) {
            return;
        }
        this.transform.concatenate(xform);
        setTransformG2D(tmpJ2DTransform(this.transform));
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(BaseTransform xform) {
        if (xform == null) {
            xform = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.transform.setTransform(xform);
        setTransformG2D(tmpJ2DTransform(this.transform));
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.transform.setTransform(m00, m10, m01, m11, m02, m12);
        setTransformG2D(tmpJ2DTransform(this.transform));
    }

    @Override // com.sun.prism.Graphics
    public void clear() {
        clear(Color.TRANSPARENT);
    }

    @Override // com.sun.prism.Graphics
    public void clear(Color color) {
        getRenderTarget().setOpaque(color.isOpaque());
        clear(toJ2DColor(color));
    }

    void clear(java.awt.Color c2) {
        Graphics2D gtmp = (Graphics2D) this.g2d.create();
        gtmp.setTransform(J2D_IDENTITY);
        gtmp.setComposite(AlphaComposite.Src);
        gtmp.setColor(c2);
        gtmp.fillRect(0, 0, this.target.getContentWidth(), this.target.getContentHeight());
        gtmp.dispose();
    }

    @Override // com.sun.prism.Graphics
    public void clearQuad(float x1, float y1, float x2, float y2) {
        this.g2d.setComposite(AlphaComposite.Clear);
        this.g2d.fill(tmpQuad(x1, y1, x2, y2));
    }

    void fill(Shape shape) {
        if (this.paintWasProportional) {
            if (this.nodeBounds != null) {
                this.g2d.setPaint(toJ2DPaint(this.paint, this.nodeBounds));
            } else {
                this.g2d.setPaint(toJ2DPaint(this.paint, shape.getBounds2D()));
            }
        }
        if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
            ImagePattern imgpat = (ImagePattern) this.paint;
            AffineTransform at2 = toJ2DTransform(imgpat.getPatternTransformNoClone());
            if (!at2.isIdentity()) {
                this.g2d.setClip(shape);
                this.g2d.transform(at2);
                tmpAT.setTransform(at2);
                try {
                    tmpAT.invert();
                    this.g2d.fill(tmpAT.createTransformedShape(shape));
                } catch (NoninvertibleTransformException e2) {
                }
                setTransform(this.transform);
                setClipRect(this.clipRect);
                return;
            }
        }
        this.g2d.fill(shape);
    }

    @Override // com.sun.prism.Graphics
    public void fill(com.sun.javafx.geom.Shape shape) {
        fill(tmpShape(shape));
    }

    @Override // com.sun.prism.Graphics
    public void fillRect(float x2, float y2, float width, float height) {
        fill(tmpRect(x2, y2, width, height));
    }

    @Override // com.sun.prism.Graphics
    public void fillRoundRect(float x2, float y2, float width, float height, float arcw, float arch) {
        fill(tmpRRect(x2, y2, width, height, arcw, arch));
    }

    @Override // com.sun.prism.Graphics
    public void fillEllipse(float x2, float y2, float width, float height) {
        fill(tmpEllipse(x2, y2, width, height));
    }

    @Override // com.sun.prism.Graphics
    public void fillQuad(float x1, float y1, float x2, float y2) {
        fill(tmpQuad(x1, y1, x2, y2));
    }

    void draw(Shape shape) {
        if (this.paintWasProportional) {
            if (this.nodeBounds != null) {
                this.g2d.setPaint(toJ2DPaint(this.paint, this.nodeBounds));
            } else {
                this.g2d.setPaint(toJ2DPaint(this.paint, shape.getBounds2D()));
            }
        }
        try {
            this.g2d.draw(shape);
        } catch (Throwable th) {
        }
    }

    @Override // com.sun.prism.Graphics
    public void draw(com.sun.javafx.geom.Shape shape) {
        draw(tmpShape(shape));
    }

    @Override // com.sun.prism.Graphics
    public void drawLine(float x1, float y1, float x2, float y2) {
        draw(tmpLine(x1, y1, x2, y2));
    }

    @Override // com.sun.prism.Graphics
    public void drawRect(float x2, float y2, float width, float height) {
        draw(tmpRect(x2, y2, width, height));
    }

    @Override // com.sun.prism.Graphics
    public void drawRoundRect(float x2, float y2, float width, float height, float arcw, float arch) {
        draw(tmpRRect(x2, y2, width, height, arcw, arch));
    }

    @Override // com.sun.prism.Graphics
    public void drawEllipse(float x2, float y2, float width, float height) {
        draw(tmpEllipse(x2, y2, width, height));
    }

    @Override // com.sun.prism.Graphics
    public void setNodeBounds(RectBounds bounds) {
        this.nodeBounds = bounds != null ? new Rectangle2D.Float(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()) : null;
    }

    private void drawString(GlyphList gl, int start, int end, FontStrike strike, float x2, float y2) {
        if (start == end) {
            return;
        }
        int count = end - start;
        int[] glyphs = new int[count];
        for (int i2 = 0; i2 < count; i2++) {
            glyphs[i2] = gl.getGlyphCode(start + i2) & 16777215;
        }
        Font j2dfont = toJ2DFont(strike);
        GlyphVector gv = j2dfont.createGlyphVector(this.g2d.getFontRenderContext(), glyphs);
        Point2D pt = new Point2D.Float();
        for (int i3 = 0; i3 < count; i3++) {
            pt.setLocation(gl.getPosX(start + i3), gl.getPosY(start + i3));
            gv.setGlyphPosition(i3, pt);
        }
        this.g2d.drawGlyphVector(gv, x2, y2);
    }

    @Override // com.sun.prism.Graphics
    public void drawString(GlyphList gl, FontStrike strike, float x2, float y2, Color selectColor, int start, int end) {
        int count = gl.getGlyphCount();
        if (count == 0) {
            return;
        }
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (strike.getAAMode() == 1) {
            this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        }
        if (this.paintWasProportional) {
            Rectangle2D rectBounds = this.nodeBounds;
            if (rectBounds == null) {
                Metrics m2 = strike.getMetrics();
                rectBounds = new Rectangle2D.Float(0.0f, m2.getAscent(), gl.getWidth(), m2.getLineHeight());
            }
            this.g2d.setPaint(toJ2DPaint(this.paint, rectBounds));
        }
        CompositeStrike cStrike = null;
        int slot = 0;
        if (strike instanceof CompositeStrike) {
            cStrike = (CompositeStrike) strike;
            int glyphCode = gl.getGlyphCode(0);
            slot = cStrike.getStrikeSlotForGlyph(glyphCode);
        }
        java.awt.Color sColor = null;
        java.awt.Color tColor = null;
        boolean selected = false;
        if (selectColor != null) {
            sColor = toJ2DColor(selectColor);
            tColor = this.g2d.getColor();
            int offset = gl.getCharOffset(0);
            selected = start <= offset && offset < end;
        }
        int index = 0;
        if (sColor != null || cStrike != null) {
            for (int i2 = 1; i2 < count; i2++) {
                if (sColor != null) {
                    int offset2 = gl.getCharOffset(i2);
                    boolean glyphSelected = start <= offset2 && offset2 < end;
                    if (selected != glyphSelected) {
                        if (cStrike != null) {
                            strike = cStrike.getStrikeSlot(slot);
                        }
                        this.g2d.setColor(selected ? sColor : tColor);
                        drawString(gl, index, i2, strike, x2, y2);
                        index = i2;
                        selected = glyphSelected;
                    }
                }
                if (cStrike != null) {
                    int glyphCode2 = gl.getGlyphCode(i2);
                    int glyphSlot = cStrike.getStrikeSlotForGlyph(glyphCode2);
                    if (slot != glyphSlot) {
                        strike = cStrike.getStrikeSlot(slot);
                        if (sColor != null) {
                            this.g2d.setColor(selected ? sColor : tColor);
                        }
                        drawString(gl, index, i2, strike, x2, y2);
                        index = i2;
                        slot = glyphSlot;
                    }
                }
            }
            if (cStrike != null) {
                strike = cStrike.getStrikeSlot(slot);
            }
            if (sColor != null) {
                this.g2d.setColor(selected ? sColor : tColor);
            }
        }
        drawString(gl, index, count, strike, x2, y2);
        if (selectColor != null) {
            this.g2d.setColor(tColor);
        }
        this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    protected void setTransformG2D(AffineTransform tx) {
        this.g2d.setTransform(tx);
    }

    protected void captureTransform(Graphics2D g2d) {
    }

    @Override // com.sun.prism.Graphics
    public void drawMappedTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx11, float ty11, float tx21, float ty21, float tx12, float ty12, float tx22, float ty22) {
        Image img = ((J2DTexture) tex).getBufferedImage();
        float mxx = tx21 - tx11;
        float myx = ty21 - ty11;
        float mxy = tx12 - tx11;
        float myy = ty12 - ty11;
        setTransformG2D(J2D_IDENTITY);
        tmpAT.setTransform(mxx, myx, mxy, myy, tx11, ty11);
        try {
            tmpAT.invert();
            this.g2d.translate(dx1, dy1);
            this.g2d.scale(dx2 - dx1, dy2 - dy1);
            this.g2d.transform(tmpAT);
            this.g2d.drawImage(img, 0, 0, 1, 1, null);
        } catch (NoninvertibleTransformException e2) {
        }
        setTransform(this.transform);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float x2, float y2, float w2, float h2) {
        Image img = ((J2DTexture) tex).getBufferedImage();
        this.g2d.drawImage(img, (int) x2, (int) y2, (int) (x2 + w2), (int) (y2 + h2), 0, 0, (int) w2, (int) h2, null);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        Image img = ((J2DTexture) tex).getBufferedImage();
        this.g2d.drawImage(img, (int) dx1, (int) dy1, (int) dx2, (int) dy2, (int) sx1, (int) sy1, (int) sx2, (int) sy2, null);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceH(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dh2, float sh1, float sh2) {
        if (sh1 + 0.1f > sh2) {
            sh2 += 1.0f;
        }
        drawTexture(tex, dx1, dy1, dh1, dy2, sx1, sy1, sh1, sy2);
        drawTexture(tex, dh1, dy1, dh2, dy2, sh1, sy1, sh2, sy2);
        drawTexture(tex, dh2, dy1, dx2, dy2, sh2, sy1, sx2, sy2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceV(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dv1, float dv2, float sv1, float sv2) {
        if (sv1 + 0.1f > sv2) {
            sv2 += 1.0f;
        }
        drawTexture(tex, dx1, dy1, dx2, dv1, sx1, sy1, sx2, sv1);
        drawTexture(tex, dx1, dv1, dx2, dv2, sx1, sv1, sx2, sv2);
        drawTexture(tex, dx1, dv2, dx2, dy2, sx1, sv2, sx2, sy2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture9Slice(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dv1, float dh2, float dv2, float sh1, float sv1, float sh2, float sv2) {
        if (sh1 + 0.1f > sh2) {
            sh2 += 1.0f;
        }
        if (sv1 + 0.1f > sv2) {
            sv2 += 1.0f;
        }
        drawTexture(tex, dx1, dy1, dh1, dv1, sx1, sy1, sh1, sv1);
        drawTexture(tex, dh1, dy1, dh2, dv1, sh1, sy1, sh2, sv1);
        drawTexture(tex, dh2, dy1, dx2, dv1, sh2, sy1, sx2, sv1);
        drawTexture(tex, dx1, dv1, dh1, dv2, sx1, sv1, sh1, sv2);
        drawTexture(tex, dh1, dv1, dh2, dv2, sh1, sv1, sh2, sv2);
        drawTexture(tex, dh2, dv1, dx2, dv2, sh2, sv1, sx2, sv2);
        drawTexture(tex, dx1, dv2, dh1, dy2, sx1, sv2, sh1, sy2);
        drawTexture(tex, dh1, dv2, dh2, dy2, sh1, sv2, sh2, sy2);
        drawTexture(tex, dh2, dv2, dx2, dy2, sh2, sv2, sx2, sy2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2) {
        int w2 = tex.getContentWidth();
        int h2 = tex.getContentHeight();
        drawTexture(tex, dx1, dy1, dx2, dy2, tx1 * w2, ty1 * h2, tx2 * w2, ty2 * h2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureVO(Texture tex, float topopacity, float botopacity, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        java.awt.Paint savepaint = this.g2d.getPaint();
        Composite savecomp = this.g2d.getComposite();
        java.awt.Color c1 = new java.awt.Color(1.0f, 1.0f, 1.0f, topopacity);
        java.awt.Color c2 = new java.awt.Color(1.0f, 1.0f, 1.0f, botopacity);
        this.g2d.setPaint(new GradientPaint(0.0f, dy1, c1, 0.0f, dy2, c2, true));
        this.g2d.setComposite(AlphaComposite.Src);
        int x2 = (int) Math.floor(Math.min(dx1, dx2));
        int y2 = (int) Math.floor(Math.min(dy1, dy2));
        int w2 = ((int) Math.ceil(Math.max(dx1, dx2))) - x2;
        int h2 = ((int) Math.ceil(Math.max(dy1, dy2))) - y2;
        this.g2d.fillRect(x2, y2, w2, h2);
        this.g2d.setComposite(AlphaComposite.SrcIn);
        drawTexture(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
        this.g2d.setComposite(savecomp);
        this.g2d.setPaint(savepaint);
    }

    @Override // com.sun.prism.MaskTextureGraphics
    public void drawPixelsMasked(RTTexture imgtex, RTTexture masktex, int dx, int dy, int dw, int dh, int ix, int iy, int mx, int my) {
        doDrawMaskTexture((J2DRTTexture) imgtex, (J2DRTTexture) masktex, dx, dy, dw, dh, ix, iy, mx, my, true);
    }

    @Override // com.sun.prism.MaskTextureGraphics
    public void maskInterpolatePixels(RTTexture imgtex, RTTexture masktex, int dx, int dy, int dw, int dh, int ix, int iy, int mx, int my) {
        doDrawMaskTexture((J2DRTTexture) imgtex, (J2DRTTexture) masktex, dx, dy, dw, dh, ix, iy, mx, my, false);
    }

    private void doDrawMaskTexture(J2DRTTexture imgtex, J2DRTTexture masktex, int dx, int dy, int dw, int dh, int ix, int iy, int mx, int my, boolean srcover) {
        int imgpix;
        int a2;
        int a3;
        int r2;
        int g2;
        int b2;
        int cx0 = this.clipRect.f11913x;
        int cy0 = this.clipRect.f11914y;
        int cx1 = cx0 + this.clipRect.width;
        int cy1 = cy0 + this.clipRect.height;
        if (dw <= 0 || dh <= 0) {
            return;
        }
        if (dx < cx0) {
            int bump = cx0 - dx;
            int i2 = dw - bump;
            dw = i2;
            if (i2 <= 0) {
                return;
            }
            ix += bump;
            mx += bump;
            dx = cx0;
        }
        if (dy < cy0) {
            int bump2 = cy0 - dy;
            int i3 = dh - bump2;
            dh = i3;
            if (i3 <= 0) {
                return;
            }
            iy += bump2;
            my += bump2;
            dy = cy0;
        }
        if (dx + dw > cx1) {
            int i4 = cx1 - dx;
            dw = i4;
            if (i4 <= 0) {
                return;
            }
        }
        if (dy + dh > cy1) {
            int i5 = cy1 - dy;
            dh = i5;
            if (i5 <= 0) {
                return;
            }
        }
        int iw = imgtex.getContentWidth();
        int ih = imgtex.getContentHeight();
        if (ix < 0) {
            int i6 = dw + ix;
            dw = i6;
            if (i6 <= 0) {
                return;
            }
            dx -= ix;
            mx -= ix;
            ix = 0;
        }
        if (iy < 0) {
            int i7 = dh + iy;
            dh = i7;
            if (i7 <= 0) {
                return;
            }
            dy -= iy;
            my -= iy;
            iy = 0;
        }
        if (ix + dw > iw) {
            int i8 = iw - ix;
            dw = i8;
            if (i8 <= 0) {
                return;
            }
        }
        if (iy + dh > ih) {
            int i9 = ih - iy;
            dh = i9;
            if (i9 <= 0) {
                return;
            }
        }
        int mw = masktex.getContentWidth();
        int mh = masktex.getContentHeight();
        if (mx < 0) {
            int i10 = dw + mx;
            dw = i10;
            if (i10 <= 0) {
                return;
            }
            dx -= mx;
            ix -= mx;
            mx = 0;
        }
        if (my < 0) {
            int i11 = dh + my;
            dh = i11;
            if (i11 <= 0) {
                return;
            }
            dy -= my;
            iy -= my;
            my = 0;
        }
        if (mx + dw > mw) {
            int i12 = mw - mx;
            dw = i12;
            if (i12 <= 0) {
                return;
            }
        }
        if (my + dh > mh) {
            int i13 = mh - my;
            dh = i13;
            if (i13 <= 0) {
                return;
            }
        }
        int[] imgbuf = imgtex.getPixels();
        int[] maskbuf = masktex.getPixels();
        DataBuffer db = this.target.getBackBuffer().getRaster().getDataBuffer();
        int[] dstbuf = ((DataBufferInt) db).getData();
        int iscan = imgtex.getBufferedImage().getWidth();
        int mscan = masktex.getBufferedImage().getWidth();
        int dscan = this.target.getBackBuffer().getWidth();
        int ioff = (iy * iscan) + ix;
        int moff = (my * mscan) + mx;
        int doff = (dy * dscan) + dx;
        if (srcover) {
            for (int y2 = 0; y2 < dh; y2++) {
                for (int x2 = 0; x2 < dw; x2++) {
                    int maskalpha = maskbuf[moff + x2] >>> 24;
                    if (maskalpha != 0 && (a2 = (imgpix = imgbuf[ioff + x2]) >>> 24) != 0) {
                        if (maskalpha < 255) {
                            int maskalpha2 = maskalpha + (maskalpha >> 7);
                            a3 = a2 * maskalpha2;
                            r2 = ((imgpix >> 16) & 255) * maskalpha2;
                            g2 = ((imgpix >> 8) & 255) * maskalpha2;
                            b2 = (imgpix & 255) * maskalpha2;
                        } else if (a2 < 255) {
                            a3 = a2 << 8;
                            r2 = ((imgpix >> 16) & 255) << 8;
                            g2 = ((imgpix >> 8) & 255) << 8;
                            b2 = (imgpix & 255) << 8;
                        } else {
                            dstbuf[doff + x2] = imgpix;
                        }
                        int maskalpha3 = (a3 + 128) >> 8;
                        int maskalpha4 = 256 - (maskalpha3 + (maskalpha3 >> 7));
                        int imgpix2 = dstbuf[doff + x2];
                        dstbuf[doff + x2] = (((a3 + (((imgpix2 >>> 24) * maskalpha4) + 128)) >> 8) << 24) + (((r2 + ((((imgpix2 >> 16) & 255) * maskalpha4) + 128)) >> 8) << 16) + (((g2 + ((((imgpix2 >> 8) & 255) * maskalpha4) + 128)) >> 8) << 8) + ((b2 + (((imgpix2 & 255) * maskalpha4) + 128)) >> 8);
                    }
                }
                ioff += iscan;
                moff += mscan;
                doff += dscan;
            }
            return;
        }
        for (int y3 = 0; y3 < dh; y3++) {
            for (int x3 = 0; x3 < dw; x3++) {
                int maskalpha5 = maskbuf[moff + x3] >>> 24;
                if (maskalpha5 != 0) {
                    int imgpix3 = imgbuf[ioff + x3];
                    if (maskalpha5 < 255) {
                        int maskalpha6 = maskalpha5 + (maskalpha5 >> 7);
                        int a4 = (imgpix3 >>> 24) * maskalpha6;
                        int r3 = ((imgpix3 >> 16) & 255) * maskalpha6;
                        int g3 = ((imgpix3 >> 8) & 255) * maskalpha6;
                        int b3 = (imgpix3 & 255) * maskalpha6;
                        int maskalpha7 = 256 - maskalpha6;
                        int imgpix4 = dstbuf[doff + x3];
                        imgpix3 = (((a4 + (((imgpix4 >>> 24) * maskalpha7) + 128)) >> 8) << 24) + (((r3 + ((((imgpix4 >> 16) & 255) * maskalpha7) + 128)) >> 8) << 16) + (((g3 + ((((imgpix4 >> 8) & 255) * maskalpha7) + 128)) >> 8) << 8) + ((b3 + (((imgpix4 & 255) * maskalpha7) + 128)) >> 8);
                    }
                    dstbuf[doff + x3] = imgpix3;
                }
            }
            ioff += iscan;
            moff += mscan;
            doff += dscan;
        }
    }

    @Override // com.sun.prism.ReadbackGraphics
    public boolean canReadBack() {
        return true;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public RTTexture readBack(Rectangle view) {
        J2DRTTexture rtt = this.target.getReadbackBuffer();
        Graphics2D rttg2d = rtt.createAWTGraphics2D();
        rttg2d.setComposite(AlphaComposite.Src);
        int x0 = view.f11913x;
        int y0 = view.f11914y;
        int w2 = view.width;
        int h2 = view.height;
        int x1 = x0 + w2;
        int y1 = y0 + h2;
        rttg2d.drawImage(this.target.getBackBuffer(), 0, 0, w2, h2, x0, y0, x1, y1, null);
        rttg2d.dispose();
        return rtt;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public void releaseReadBackBuffer(RTTexture view) {
    }

    @Override // com.sun.prism.Graphics
    public NGCamera getCameraNoClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.prism.Graphics
    public void setPerspectiveTransform(GeneralTransform3D transform) {
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthBuffer() {
        return false;
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthTest() {
        return false;
    }

    @Override // com.sun.prism.Graphics
    public boolean isAlphaTestShader() {
        if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
            System.out.println("J2D pipe doesn't support shader with alpha testing");
            return false;
        }
        return false;
    }

    @Override // com.sun.prism.Graphics
    public void setAntialiasedShape(boolean aa2) {
        this.antialiasedShape = aa2;
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antialiasedShape ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override // com.sun.prism.Graphics
    public boolean isAntialiasedShape() {
        return this.antialiasedShape;
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy, float sz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.prism.Graphics
    public void setTransform3D(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz != 0.0d || myz != 0.0d || mzx != 0.0d || mzy != 0.0d || mzz != 1.0d || mzt != 0.0d) {
            throw new UnsupportedOperationException("3D transforms not supported.");
        }
        setTransform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override // com.sun.prism.Graphics
    public void setCamera(NGCamera camera) {
    }

    @Override // com.sun.prism.Graphics
    public void setDepthBuffer(boolean depthBuffer) {
    }

    @Override // com.sun.prism.Graphics
    public void setDepthTest(boolean depthTest) {
    }

    @Override // com.sun.prism.Graphics
    public void sync() {
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty, float tz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCulling(boolean cull) {
        this.cull = cull;
    }

    public boolean isCulling() {
        return this.cull;
    }

    @Override // com.sun.prism.Graphics
    public void setClipRectIndex(int index) {
        this.clipRectIndex = index;
    }

    @Override // com.sun.prism.Graphics
    public int getClipRectIndex() {
        return this.clipRectIndex;
    }

    @Override // com.sun.prism.Graphics
    public void setHasPreCullingBits(boolean hasBits) {
        this.hasPreCullingBits = hasBits;
    }

    @Override // com.sun.prism.Graphics
    public boolean hasPreCullingBits() {
        return this.hasPreCullingBits;
    }

    @Override // com.sun.prism.Graphics
    public void setRenderRoot(NodePath root) {
        this.renderRoot = root;
    }

    @Override // com.sun.prism.Graphics
    public NodePath getRenderRoot() {
        return this.renderRoot;
    }

    @Override // com.sun.prism.Graphics
    public void setState3D(boolean flag) {
    }

    @Override // com.sun.prism.Graphics
    public boolean isState3D() {
        return false;
    }

    @Override // com.sun.prism.Graphics
    public void setup3DRendering() {
    }

    @Override // com.sun.prism.Graphics
    public void setPixelScaleFactor(float pixelScale) {
        this.pixelScale = pixelScale;
    }

    @Override // com.sun.prism.Graphics
    public float getPixelScaleFactor() {
        return this.pixelScale;
    }

    @Override // com.sun.prism.Graphics
    public void blit(RTTexture srcTex, RTTexture dstTex, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics$AdaptorShape.class */
    private static class AdaptorShape implements Shape {
        private com.sun.javafx.geom.Shape prshape;
        private static AdaptorPathIterator tmpAdaptor = new AdaptorPathIterator();

        private AdaptorShape() {
        }

        public void setShape(com.sun.javafx.geom.Shape prshape) {
            this.prshape = prshape;
        }

        @Override // java.awt.Shape
        public boolean contains(double x2, double y2) {
            return this.prshape.contains((float) x2, (float) y2);
        }

        @Override // java.awt.Shape
        public boolean contains(Point2D p2) {
            return contains(p2.getX(), p2.getY());
        }

        @Override // java.awt.Shape
        public boolean contains(double x2, double y2, double w2, double h2) {
            return this.prshape.contains((float) x2, (float) y2, (float) w2, (float) h2);
        }

        @Override // java.awt.Shape
        public boolean contains(Rectangle2D r2) {
            return contains(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
        }

        @Override // java.awt.Shape
        public boolean intersects(double x2, double y2, double w2, double h2) {
            return this.prshape.intersects((float) x2, (float) y2, (float) w2, (float) h2);
        }

        @Override // java.awt.Shape
        public boolean intersects(Rectangle2D r2) {
            return intersects(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
        }

        @Override // java.awt.Shape
        public java.awt.Rectangle getBounds() {
            return getBounds2D().getBounds();
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            RectBounds b2 = this.prshape.getBounds();
            Rectangle2D r2d = new Rectangle2D.Float();
            r2d.setFrameFromDiagonal(b2.getMinX(), b2.getMinY(), b2.getMaxX(), b2.getMaxY());
            return r2d;
        }

        private static PathIterator tmpAdaptor(com.sun.javafx.geom.PathIterator pi) {
            tmpAdaptor.setIterator(pi);
            return tmpAdaptor;
        }

        @Override // java.awt.Shape
        public PathIterator getPathIterator(AffineTransform at2) {
            BaseTransform tx = at2 == null ? null : J2DPrismGraphics.toPrTransform(at2);
            return tmpAdaptor(this.prshape.getPathIterator(tx));
        }

        @Override // java.awt.Shape
        public PathIterator getPathIterator(AffineTransform at2, double flatness) {
            BaseTransform tx = at2 == null ? null : J2DPrismGraphics.toPrTransform(at2);
            return tmpAdaptor(this.prshape.getPathIterator(tx, (float) flatness));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics$AdaptorPathIterator.class */
    private static class AdaptorPathIterator implements PathIterator {
        private static int[] NUM_COORDS = {2, 2, 4, 6, 0};
        com.sun.javafx.geom.PathIterator priterator;
        float[] tmpcoords;

        private AdaptorPathIterator() {
        }

        public void setIterator(com.sun.javafx.geom.PathIterator priterator) {
            this.priterator = priterator;
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(float[] coords) {
            return this.priterator.currentSegment(coords);
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(double[] coords) {
            if (this.tmpcoords == null) {
                this.tmpcoords = new float[6];
            }
            int ret = this.priterator.currentSegment(this.tmpcoords);
            for (int i2 = 0; i2 < NUM_COORDS[ret]; i2++) {
                coords[i2] = this.tmpcoords[i2];
            }
            return ret;
        }

        @Override // java.awt.geom.PathIterator
        public int getWindingRule() {
            return this.priterator.getWindingRule();
        }

        @Override // java.awt.geom.PathIterator
        public boolean isDone() {
            return this.priterator.isDone();
        }

        @Override // java.awt.geom.PathIterator
        public void next() {
            this.priterator.next();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics$FilterStroke.class */
    static abstract class FilterStroke implements Stroke {
        protected java.awt.BasicStroke stroke;
        static final double CtrlVal = 0.5522847498307933d;

        protected abstract Shape makeStrokedRect(Rectangle2D rectangle2D);

        protected abstract Shape makeStrokedShape(Shape shape);

        FilterStroke(java.awt.BasicStroke stroke) {
            this.stroke = stroke;
        }

        @Override // java.awt.Stroke
        public Shape createStrokedShape(Shape p2) {
            Shape s2;
            if ((p2 instanceof Rectangle2D) && (s2 = makeStrokedRect((Rectangle2D) p2)) != null) {
                return s2;
            }
            return makeStrokedShape(p2);
        }

        static Point2D cornerArc(GeneralPath gp, float x0, float y0, float xc, float yc, float x1, float y1) {
            return cornerArc(gp, x0, y0, xc, yc, x1, y1, 0.5f);
        }

        static Point2D cornerArc(GeneralPath gp, float x0, float y0, float xc, float yc, float x1, float y1, float t2) {
            float xc0 = (float) (x0 + (0.5522847498307933d * (xc - x0)));
            float yc0 = (float) (y0 + (0.5522847498307933d * (yc - y0)));
            float xc1 = (float) (x1 + (0.5522847498307933d * (xc - x1)));
            float yc1 = (float) (y1 + (0.5522847498307933d * (yc - y1)));
            gp.curveTo(xc0, yc0, xc1, yc1, x1, y1);
            return new Point2D.Float(eval(x0, xc0, xc1, x1, t2), eval(y0, yc0, yc1, y1, t2));
        }

        static float eval(float c0, float c1, float c2, float c3, float t2) {
            float c02 = c0 + ((c1 - c0) * t2);
            float c12 = c1 + ((c2 - c1) * t2);
            float c03 = c02 + ((c12 - c02) * t2);
            return c03 + (((c12 + (((c2 + ((c3 - c2) * t2)) - c12) * t2)) - c03) * t2);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics$InnerStroke.class */
    static class InnerStroke extends FilterStroke {
        InnerStroke(java.awt.BasicStroke stroke) {
            super(stroke);
        }

        @Override // com.sun.prism.j2d.J2DPrismGraphics.FilterStroke
        protected Shape makeStrokedRect(Rectangle2D r2) {
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float pad = this.stroke.getLineWidth() / 2.0f;
            if (pad >= r2.getWidth() || pad >= r2.getHeight()) {
                return r2;
            }
            float rx0 = (float) r2.getX();
            float ry0 = (float) r2.getY();
            float rx1 = rx0 + ((float) r2.getWidth());
            float ry1 = ry0 + ((float) r2.getHeight());
            GeneralPath gp = new GeneralPath();
            gp.moveTo(rx0, ry0);
            gp.lineTo(rx1, ry0);
            gp.lineTo(rx1, ry1);
            gp.lineTo(rx0, ry1);
            gp.closePath();
            float rx02 = rx0 + pad;
            float ry02 = ry0 + pad;
            float rx12 = rx1 - pad;
            float ry12 = ry1 - pad;
            gp.moveTo(rx02, ry02);
            gp.lineTo(rx02, ry12);
            gp.lineTo(rx12, ry12);
            gp.lineTo(rx12, ry02);
            gp.closePath();
            return gp;
        }

        protected Shape makeStrokedEllipse(Ellipse2D e2) {
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float pad = this.stroke.getLineWidth() / 2.0f;
            float w2 = (float) e2.getWidth();
            float h2 = (float) e2.getHeight();
            if (w2 - (2.0f * pad) > h2 * 2.0f || h2 - (2.0f * pad) > w2 * 2.0f) {
                return null;
            }
            if (pad >= w2 || pad >= h2) {
                return e2;
            }
            float x0 = (float) e2.getX();
            float y0 = (float) e2.getY();
            float xc = x0 + (w2 / 2.0f);
            float yc = y0 + (h2 / 2.0f);
            float x1 = x0 + w2;
            float y1 = y0 + h2;
            GeneralPath gp = new GeneralPath();
            gp.moveTo(xc, y0);
            cornerArc(gp, xc, y0, x1, y0, x1, yc);
            cornerArc(gp, x1, yc, x1, y1, xc, y1);
            cornerArc(gp, xc, y1, x0, y1, x0, yc);
            cornerArc(gp, x0, yc, x0, y0, xc, y0);
            gp.closePath();
            float x02 = x0 + pad;
            float y02 = y0 + pad;
            float x12 = x1 - pad;
            float y12 = y1 - pad;
            gp.moveTo(xc, y02);
            cornerArc(gp, xc, y02, x02, y02, x02, yc);
            cornerArc(gp, x02, yc, x02, y12, xc, y12);
            cornerArc(gp, xc, y12, x12, y12, x12, yc);
            cornerArc(gp, x12, yc, x12, y02, xc, y02);
            gp.closePath();
            return gp;
        }

        @Override // com.sun.prism.j2d.J2DPrismGraphics.FilterStroke
        protected Shape makeStrokedShape(Shape s2) {
            Shape ss = this.stroke.createStrokedShape(s2);
            Area b2 = new Area(ss);
            b2.intersect(new Area(s2));
            return b2;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPrismGraphics$OuterStroke.class */
    static class OuterStroke extends FilterStroke {
        static double SQRT_2 = Math.sqrt(2.0d);

        OuterStroke(java.awt.BasicStroke stroke) {
            super(stroke);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:11:0x00db  */
        @Override // com.sun.prism.j2d.J2DPrismGraphics.FilterStroke
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected java.awt.Shape makeStrokedRect(java.awt.geom.Rectangle2D r9) {
            /*
                Method dump skipped, instructions count: 432
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.prism.j2d.J2DPrismGraphics.OuterStroke.makeStrokedRect(java.awt.geom.Rectangle2D):java.awt.Shape");
        }

        protected Shape makeStrokedEllipse(Ellipse2D e2) {
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float pad = this.stroke.getLineWidth() / 2.0f;
            float w2 = (float) e2.getWidth();
            float h2 = (float) e2.getHeight();
            if (w2 > h2 * 2.0f || h2 > w2 * 2.0f) {
                return null;
            }
            float x0 = (float) e2.getX();
            float y0 = (float) e2.getY();
            float xc = x0 + (w2 / 2.0f);
            float yc = y0 + (h2 / 2.0f);
            float x1 = x0 + w2;
            float y1 = y0 + h2;
            GeneralPath gp = new GeneralPath();
            gp.moveTo(xc, y0);
            cornerArc(gp, xc, y0, x1, y0, x1, yc);
            cornerArc(gp, x1, yc, x1, y1, xc, y1);
            cornerArc(gp, xc, y1, x0, y1, x0, yc);
            cornerArc(gp, x0, yc, x0, y0, xc, y0);
            gp.closePath();
            float x02 = x0 - pad;
            float y02 = y0 - pad;
            float x12 = x1 + pad;
            float y12 = y1 + pad;
            gp.moveTo(xc, y02);
            cornerArc(gp, xc, y02, x02, y02, x02, yc);
            cornerArc(gp, x02, yc, x02, y12, xc, y12);
            cornerArc(gp, xc, y12, x12, y12, x12, yc);
            cornerArc(gp, x12, yc, x12, y02, xc, y02);
            gp.closePath();
            return gp;
        }

        @Override // com.sun.prism.j2d.J2DPrismGraphics.FilterStroke
        protected Shape makeStrokedShape(Shape s2) {
            Shape ss = this.stroke.createStrokedShape(s2);
            Area b2 = new Area(ss);
            b2.subtract(new Area(s2));
            return b2;
        }
    }

    @Override // com.sun.prism.Graphics
    public void setLights(NGLightBase[] lights) {
    }

    @Override // com.sun.prism.Graphics
    public NGLightBase[] getLights() {
        return null;
    }
}
