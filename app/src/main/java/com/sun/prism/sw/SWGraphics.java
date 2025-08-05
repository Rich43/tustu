package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWGraphics.class */
final class SWGraphics implements ReadbackGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    private final PiscesRenderer pr;
    private final SWContext context;
    private final SWRTTexture target;
    private final SWPaint swPaint;
    private Rectangle clip;
    private RectBounds nodeBounds;
    private int clipRectIndex;
    private Ellipse2D ellipse2d;
    private Line2D line2d;
    private RoundRectangle2D rect2d;
    private NodePath renderRoot;
    private final BaseTransform tx = new Affine2D();
    private CompositeMode compositeMode = CompositeMode.SRC_OVER;
    private final Rectangle finalClip = new Rectangle();
    private Paint paint = DEFAULT_PAINT;
    private BasicStroke stroke = DEFAULT_STROKE;
    private boolean antialiasedShape = true;
    private boolean hasPreCullingBits = false;
    private float pixelScale = 1.0f;

    @Override // com.sun.prism.Graphics
    public void setRenderRoot(NodePath root) {
        this.renderRoot = root;
    }

    @Override // com.sun.prism.Graphics
    public NodePath getRenderRoot() {
        return this.renderRoot;
    }

    public SWGraphics(SWRTTexture target, SWContext context, PiscesRenderer pr) {
        this.target = target;
        this.context = context;
        this.pr = pr;
        this.swPaint = new SWPaint(context, pr);
        setClipRect(null);
    }

    @Override // com.sun.prism.Graphics
    public RenderTarget getRenderTarget() {
        return this.target;
    }

    @Override // com.sun.prism.Graphics
    public SWResourceFactory getResourceFactory() {
        return this.target.getResourceFactory();
    }

    @Override // com.sun.prism.Graphics
    public Screen getAssociatedScreen() {
        return this.target.getAssociatedScreen();
    }

    @Override // com.sun.prism.Graphics
    public void sync() {
    }

    @Override // com.sun.prism.Graphics
    public BaseTransform getTransformNoClone() {
        if (PrismSettings.debug) {
            System.out.println("+ getTransformNoClone " + ((Object) this) + "; tr: " + ((Object) this.tx));
        }
        return this.tx;
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(BaseTransform xform) {
        if (xform == null) {
            xform = BaseTransform.IDENTITY_TRANSFORM;
        }
        if (PrismSettings.debug) {
            System.out.println("+ setTransform " + ((Object) this) + "; tr: " + ((Object) xform));
        }
        this.tx.setTransform(xform);
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.tx.restoreTransform(m00, m10, m01, m11, m02, m12);
        if (PrismSettings.debug) {
            System.out.println("+ restoreTransform " + ((Object) this) + "; tr: " + ((Object) this.tx));
        }
    }

    @Override // com.sun.prism.Graphics
    public void setTransform3D(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        if (mxz != 0.0d || myz != 0.0d || mzx != 0.0d || mzy != 0.0d || mzz != 1.0d || mzt != 0.0d) {
            throw new UnsupportedOperationException("3D transforms not supported.");
        }
        setTransform(mxx, myx, mxy, myy, mxt, myt);
    }

    @Override // com.sun.prism.Graphics
    public void transform(BaseTransform xform) {
        if (PrismSettings.debug) {
            System.out.println("+ concatTransform " + ((Object) this) + "; tr: " + ((Object) xform));
        }
        this.tx.deriveWithConcatenation(xform);
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty) {
        if (PrismSettings.debug) {
            System.out.println("+ concat translate " + ((Object) this) + "; tx: " + tx + "; ty: " + ty);
        }
        this.tx.deriveWithTranslation(tx, ty);
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty, float tz) {
        throw new UnsupportedOperationException("translate3D: unimp");
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy) {
        if (PrismSettings.debug) {
            System.out.println("+ concat scale " + ((Object) this) + "; sx: " + sx + "; sy: " + sy);
        }
        this.tx.deriveWithConcatenation(sx, 0.0d, 0.0d, sy, 0.0d, 0.0d);
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy, float sz) {
        throw new UnsupportedOperationException("scale3D: unimp");
    }

    @Override // com.sun.prism.Graphics
    public void setCamera(NGCamera camera) {
    }

    @Override // com.sun.prism.Graphics
    public void setPerspectiveTransform(GeneralTransform3D transform) {
    }

    @Override // com.sun.prism.Graphics
    public NGCamera getCameraNoClone() {
        throw new UnsupportedOperationException("getCameraNoClone: unimp");
    }

    @Override // com.sun.prism.Graphics
    public void setDepthTest(boolean depthTest) {
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthTest() {
        return false;
    }

    @Override // com.sun.prism.Graphics
    public void setDepthBuffer(boolean depthBuffer) {
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthBuffer() {
        return false;
    }

    @Override // com.sun.prism.Graphics
    public boolean isAlphaTestShader() {
        if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
            System.out.println("SW pipe doesn't support shader with alpha testing");
            return false;
        }
        return false;
    }

    @Override // com.sun.prism.Graphics
    public void setAntialiasedShape(boolean aa2) {
        this.antialiasedShape = aa2;
    }

    @Override // com.sun.prism.Graphics
    public boolean isAntialiasedShape() {
        return this.antialiasedShape;
    }

    @Override // com.sun.prism.Graphics
    public Rectangle getClipRect() {
        if (this.clip == null) {
            return null;
        }
        return new Rectangle(this.clip);
    }

    @Override // com.sun.prism.Graphics
    public Rectangle getClipRectNoClone() {
        return this.clip;
    }

    @Override // com.sun.prism.Graphics
    public RectBounds getFinalClipNoClone() {
        return this.finalClip.toRectBounds();
    }

    @Override // com.sun.prism.Graphics
    public void setClipRect(Rectangle clipRect) {
        this.finalClip.setBounds(this.target.getDimensions());
        if (clipRect == null) {
            if (PrismSettings.debug) {
                System.out.println("+ PR.resetClip");
            }
            this.clip = null;
        } else {
            if (PrismSettings.debug) {
                System.out.println("+ PR.setClip: " + ((Object) clipRect));
            }
            this.finalClip.intersectWith(clipRect);
            this.clip = new Rectangle(clipRect);
        }
        this.pr.setClip(this.finalClip.f11913x, this.finalClip.f11914y, this.finalClip.width, this.finalClip.height);
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
    public int getClipRectIndex() {
        return this.clipRectIndex;
    }

    @Override // com.sun.prism.Graphics
    public void setClipRectIndex(int index) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.setClipRectIndex: " + index);
        }
        this.clipRectIndex = index;
    }

    @Override // com.sun.prism.Graphics
    public float getExtraAlpha() {
        return this.swPaint.getCompositeAlpha();
    }

    @Override // com.sun.prism.Graphics
    public void setExtraAlpha(float extraAlpha) {
        if (PrismSettings.debug) {
            System.out.println("PR.setCompositeAlpha, value: " + extraAlpha);
        }
        this.swPaint.setCompositeAlpha(extraAlpha);
    }

    @Override // com.sun.prism.Graphics
    public Paint getPaint() {
        return this.paint;
    }

    @Override // com.sun.prism.Graphics
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override // com.sun.prism.Graphics
    public BasicStroke getStroke() {
        return this.stroke;
    }

    @Override // com.sun.prism.Graphics
    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    @Override // com.sun.prism.Graphics
    public CompositeMode getCompositeMode() {
        return this.compositeMode;
    }

    @Override // com.sun.prism.Graphics
    public void setCompositeMode(CompositeMode mode) {
        int piscesComp;
        this.compositeMode = mode;
        switch (mode) {
            case CLEAR:
                piscesComp = 0;
                if (PrismSettings.debug) {
                    System.out.println("PR.setCompositeRule - CLEAR");
                    break;
                }
                break;
            case SRC:
                piscesComp = 1;
                if (PrismSettings.debug) {
                    System.out.println("PR.setCompositeRule - SRC");
                    break;
                }
                break;
            case SRC_OVER:
                piscesComp = 2;
                if (PrismSettings.debug) {
                    System.out.println("PR.setCompositeRule - SRC_OVER");
                    break;
                }
                break;
            default:
                throw new InternalError("Unrecognized composite mode: " + ((Object) mode));
        }
        this.pr.setCompositeRule(piscesComp);
    }

    @Override // com.sun.prism.Graphics
    public void setNodeBounds(RectBounds bounds) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.setNodeBounds: " + ((Object) bounds));
        }
        this.nodeBounds = bounds;
    }

    @Override // com.sun.prism.Graphics
    public void clear() {
        clear(Color.TRANSPARENT);
    }

    @Override // com.sun.prism.Graphics
    public void clear(Color color) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.clear: " + ((Object) color));
        }
        this.swPaint.setColor(color, 1.0f);
        this.pr.clearRect(0, 0, this.target.getPhysicalWidth(), this.target.getPhysicalHeight());
        getRenderTarget().setOpaque(color.isOpaque());
    }

    @Override // com.sun.prism.Graphics
    public void clearQuad(float x1, float y1, float x2, float y2) {
        CompositeMode cm = this.compositeMode;
        Paint p2 = this.paint;
        setCompositeMode(CompositeMode.SRC);
        setPaint(Color.TRANSPARENT);
        fillQuad(x1, y1, x2, y2);
        setCompositeMode(cm);
        setPaint(p2);
    }

    @Override // com.sun.prism.Graphics
    public void fill(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ fill(Shape)");
        }
        paintShape(shape, null, this.tx);
    }

    @Override // com.sun.prism.Graphics
    public void fillQuad(float x1, float y1, float x2, float y2) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillQuad");
        }
        fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    @Override // com.sun.prism.Graphics
    public void fillRect(float x2, float y2, float width, float height) {
        int imageMode;
        if (PrismSettings.debug) {
            System.out.printf("+ SWG.fillRect, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(width), Float.valueOf(height));
        }
        if (this.tx.getMxy() == 0.0d && this.tx.getMyx() == 0.0d) {
            if (PrismSettings.debug) {
                System.out.println("GR: " + ((Object) this));
                System.out.println("target: " + ((Object) this.target) + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + ((Object) this.target.getDimensions()));
                System.out.println("Tx: " + ((Object) this.tx));
                System.out.println("Clip: " + ((Object) this.finalClip));
                System.out.println("Composite rule: " + ((Object) this.compositeMode));
            }
            Point2D p1 = new Point2D(x2, y2);
            Point2D p2 = new Point2D(x2 + width, y2 + height);
            this.tx.transform(p1, p1);
            this.tx.transform(p2, p2);
            if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
                ImagePattern ip = (ImagePattern) this.paint;
                if (ip.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                    throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
                }
                Transform6 piscesTx = this.swPaint.computeSetTexturePaintTransform(this.paint, this.tx, this.nodeBounds, x2, y2, width, height);
                SWArgbPreTexture tex = this.context.validateImagePaintTexture(ip.getImage().getWidth(), ip.getImage().getHeight());
                tex.update(ip.getImage());
                float compositeAlpha = this.swPaint.getCompositeAlpha();
                if (compositeAlpha == 1.0f) {
                    imageMode = 1;
                } else {
                    imageMode = 2;
                    this.pr.setColor(255, 255, 255, (int) (255.0f * compositeAlpha));
                }
                this.pr.drawImage(1, imageMode, tex.getDataNoClone(), tex.getContentWidth(), tex.getContentHeight(), tex.getOffset(), tex.getPhysicalWidth(), piscesTx, tex.getWrapMode() == Texture.WrapMode.REPEAT, tex.getLinearFiltering(), (int) (Math.min(p1.f11907x, p2.f11907x) * 65536.0f), (int) (Math.min(p1.f11908y, p2.f11908y) * 65536.0f), (int) (Math.abs(p2.f11907x - p1.f11907x) * 65536.0f), (int) (Math.abs(p2.f11908y - p1.f11908y) * 65536.0f), 0, 0, 0, 0, 0, 0, tex.getContentWidth() - 1, tex.getContentHeight() - 1, tex.hasAlpha());
                return;
            }
            this.swPaint.setPaintFromShape(this.paint, this.tx, null, this.nodeBounds, x2, y2, width, height);
            this.pr.fillRect((int) (Math.min(p1.f11907x, p2.f11907x) * 65536.0f), (int) (Math.min(p1.f11908y, p2.f11908y) * 65536.0f), (int) (Math.abs(p2.f11907x - p1.f11907x) * 65536.0f), (int) (Math.abs(p2.f11908y - p1.f11908y) * 65536.0f));
            return;
        }
        fillRoundRect(x2, y2, width, height, 0.0f, 0.0f);
    }

    @Override // com.sun.prism.Graphics
    public void fillRoundRect(float x2, float y2, float width, float height, float arcw, float arch) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillRoundRect");
        }
        paintRoundRect(x2, y2, width, height, arcw, arch, null);
    }

    @Override // com.sun.prism.Graphics
    public void fillEllipse(float x2, float y2, float width, float height) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillEllipse");
        }
        paintEllipse(x2, y2, width, height, null);
    }

    @Override // com.sun.prism.Graphics
    public void draw(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ draw(Shape)");
        }
        paintShape(shape, this.stroke, this.tx);
    }

    private void paintShape(Shape shape, BasicStroke st, BaseTransform tr) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + ((Object) shape));
            }
        } else {
            this.swPaint.setPaintFromShape(this.paint, this.tx, shape, this.nodeBounds, 0.0f, 0.0f, 0.0f, 0.0f);
            paintShapePaintAlreadySet(shape, st, tr);
        }
    }

    private void paintShapePaintAlreadySet(Shape shape, BasicStroke st, BaseTransform tr) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + ((Object) shape));
                return;
            }
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("GR: " + ((Object) this));
            System.out.println("target: " + ((Object) this.target) + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + ((Object) this.target.getDimensions()));
            System.out.println("Shape: " + ((Object) shape));
            System.out.println("Stroke: " + ((Object) st));
            System.out.println("Tx: " + ((Object) tr));
            System.out.println("Clip: " + ((Object) this.finalClip));
            System.out.println("Composite rule: " + ((Object) this.compositeMode));
        }
        this.context.renderShape(this.pr, shape, st, tr, this.finalClip, isAntialiasedShape());
    }

    private void paintRoundRect(float x2, float y2, float width, float height, float arcw, float arch, BasicStroke st) {
        if (this.rect2d == null) {
            this.rect2d = new RoundRectangle2D(x2, y2, width, height, arcw, arch);
        } else {
            this.rect2d.setRoundRect(x2, y2, width, height, arcw, arch);
        }
        paintShape(this.rect2d, st, this.tx);
    }

    private void paintEllipse(float x2, float y2, float width, float height, BasicStroke st) {
        if (this.ellipse2d == null) {
            this.ellipse2d = new Ellipse2D(x2, y2, width, height);
        } else {
            this.ellipse2d.setFrame(x2, y2, width, height);
        }
        paintShape(this.ellipse2d, st, this.tx);
    }

    @Override // com.sun.prism.Graphics
    public void drawLine(float x1, float y1, float x2, float y2) {
        if (PrismSettings.debug) {
            System.out.println("+ drawLine");
        }
        if (this.line2d == null) {
            this.line2d = new Line2D(x1, y1, x2, y2);
        } else {
            this.line2d.setLine(x1, y1, x2, y2);
        }
        paintShape(this.line2d, this.stroke, this.tx);
    }

    @Override // com.sun.prism.Graphics
    public void drawRect(float x2, float y2, float width, float height) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRect");
        }
        drawRoundRect(x2, y2, width, height, 0.0f, 0.0f);
    }

    @Override // com.sun.prism.Graphics
    public void drawRoundRect(float x2, float y2, float width, float height, float arcw, float arch) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRoundRect");
        }
        paintRoundRect(x2, y2, width, height, arcw, arch, this.stroke);
    }

    @Override // com.sun.prism.Graphics
    public void drawEllipse(float x2, float y2, float width, float height) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawEllipse");
        }
        paintEllipse(x2, y2, width, height, this.stroke);
    }

    @Override // com.sun.prism.Graphics
    public void drawString(GlyphList gl, FontStrike strike, float x2, float y2, Color selectColor, int selectStart, int selectEnd) {
        float bh2;
        float bw2;
        float by2;
        float bx2;
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawGlyphList, gl.Count: " + gl.getGlyphCount() + ", x: " + x2 + ", y: " + y2 + ", selectStart: " + selectStart + ", selectEnd: " + selectEnd);
        }
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                bx2 = this.nodeBounds.getMinX();
                by2 = this.nodeBounds.getMinY();
                bw2 = this.nodeBounds.getWidth();
                bh2 = this.nodeBounds.getHeight();
            } else {
                Metrics m2 = strike.getMetrics();
                bx2 = 0.0f;
                by2 = m2.getAscent();
                bw2 = gl.getWidth();
                bh2 = m2.getLineHeight();
            }
        } else {
            bh2 = 0.0f;
            bw2 = 0.0f;
            by2 = 0.0f;
            bx2 = 0.0f;
        }
        boolean drawAsMasks = this.tx.isTranslateOrIdentity() && !strike.drawAsShapes();
        boolean doLCDText = drawAsMasks && strike.getAAMode() == 1 && getRenderTarget().isOpaque() && this.paint.getType() == Paint.Type.COLOR && this.tx.is2D();
        BaseTransform glyphTx = null;
        if (doLCDText) {
            this.pr.setLCDGammaCorrection(1.0f / PrismFontFactory.getLCDContrast());
        } else if (drawAsMasks) {
            FontResource fr = strike.getFontResource();
            float origSize = strike.getSize();
            BaseTransform origTx = strike.getTransform();
            strike = fr.getStrike(origSize, origTx, 0);
        } else {
            glyphTx = new Affine2D();
        }
        if (selectColor == null) {
            this.swPaint.setPaintBeforeDraw(this.paint, this.tx, bx2, by2, bw2, bh2);
            for (int i2 = 0; i2 < gl.getGlyphCount(); i2++) {
                drawGlyph(strike, gl, i2, glyphTx, drawAsMasks, x2, y2);
            }
            return;
        }
        for (int i3 = 0; i3 < gl.getGlyphCount(); i3++) {
            int offset = gl.getCharOffset(i3);
            boolean selected = selectStart <= offset && offset < selectEnd;
            this.swPaint.setPaintBeforeDraw(selected ? selectColor : this.paint, this.tx, bx2, by2, bw2, bh2);
            drawGlyph(strike, gl, i3, glyphTx, drawAsMasks, x2, y2);
        }
    }

    private void drawGlyph(FontStrike strike, GlyphList gl, int idx, BaseTransform glyphTx, boolean drawAsMasks, float x2, float y2) {
        Glyph g2 = strike.getGlyph(gl.getGlyphCode(idx));
        if (drawAsMasks) {
            Point2D pt = new Point2D((float) (x2 + this.tx.getMxt() + gl.getPosX(idx)), (float) (y2 + this.tx.getMyt() + gl.getPosY(idx)));
            int subPixel = strike.getQuantizedPosition(pt);
            byte[] pixelData = g2.getPixelData(subPixel);
            if (pixelData != null) {
                int intPosX = g2.getOriginX() + ((int) pt.f11907x);
                int intPosY = g2.getOriginY() + ((int) pt.f11908y);
                if (g2.isLCDGlyph()) {
                    this.pr.fillLCDAlphaMask(pixelData, intPosX, intPosY, g2.getWidth(), g2.getHeight(), 0, g2.getWidth());
                    return;
                } else {
                    this.pr.fillAlphaMask(pixelData, intPosX, intPosY, g2.getWidth(), g2.getHeight(), 0, g2.getWidth());
                    return;
                }
            }
            return;
        }
        Shape shape = g2.getShape();
        if (shape != null) {
            glyphTx.setTransform(this.tx);
            glyphTx.deriveWithTranslation(x2 + gl.getPosX(idx), y2 + gl.getPosY(idx));
            paintShapePaintAlreadySet(shape, null, glyphTx);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float x2, float y2, float w2, float h2) {
        if (PrismSettings.debug) {
            System.out.printf("+ drawTexture1, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(w2), Float.valueOf(h2));
        }
        drawTexture(tex, x2, y2, x2 + w2, y2 + h2, 0.0f, 0.0f, w2, h2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        drawTexture(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, 0, 0, 0, 0);
    }

    private void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, int lEdge, int rEdge, int tEdge, int bEdge) {
        int imageMode;
        float compositeAlpha = this.swPaint.getCompositeAlpha();
        if (compositeAlpha == 1.0f) {
            imageMode = 1;
        } else {
            imageMode = 2;
            this.pr.setColor(255, 255, 255, (int) (255.0f * compositeAlpha));
        }
        drawTexture(tex, imageMode, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, lEdge, rEdge, tEdge, bEdge);
    }

    private void drawTexture(Texture tex, int imageMode, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, int lEdge, int rEdge, int tEdge, int bEdge) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTexture: " + ((Object) tex) + ", imageMode: " + imageMode + ", tex.w: " + tex.getPhysicalWidth() + ", tex.h: " + tex.getPhysicalHeight() + ", tex.cw: " + tex.getContentWidth() + ", tex.ch: " + tex.getContentHeight());
            System.out.println("target: " + ((Object) this.target) + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + ((Object) this.target.getDimensions()));
            System.out.println("GR: " + ((Object) this));
            System.out.println("dx1:" + dx1 + " dy1:" + dy1 + " dx2:" + dx2 + " dy2:" + dy2);
            System.out.println("sx1:" + sx1 + " sy1:" + sy1 + " sx2:" + sx2 + " sy2:" + sy2);
            System.out.println("Clip: " + ((Object) this.finalClip));
            System.out.println("Composite rule: " + ((Object) this.compositeMode));
        }
        SWArgbPreTexture swTex = (SWArgbPreTexture) tex;
        int[] data = swTex.getDataNoClone();
        RectBounds srcBBox = new RectBounds(Math.min(dx1, dx2), Math.min(dy1, dy2), Math.max(dx1, dx2), Math.max(dy1, dy2));
        RectBounds dstBBox = new RectBounds();
        this.tx.transform(srcBBox, dstBBox);
        Transform6 piscesTx = this.swPaint.computeDrawTexturePaintTransform(this.tx, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
        if (PrismSettings.debug) {
            System.out.println("tx: " + ((Object) this.tx));
            System.out.println("piscesTx: " + ((Object) piscesTx));
            System.out.println("srcBBox: " + ((Object) srcBBox));
            System.out.println("dstBBox: " + ((Object) dstBBox));
        }
        int txMin = Math.max(0, SWUtils.fastFloor(Math.min(sx1, sx2)));
        int tyMin = Math.max(0, SWUtils.fastFloor(Math.min(sy1, sy2)));
        int txMax = Math.min(tex.getContentWidth() - 1, SWUtils.fastCeil(Math.max(sx1, sx2)) - 1);
        int tyMax = Math.min(tex.getContentHeight() - 1, SWUtils.fastCeil(Math.max(sy1, sy2)) - 1);
        this.pr.drawImage(1, imageMode, data, tex.getContentWidth(), tex.getContentHeight(), swTex.getOffset(), tex.getPhysicalWidth(), piscesTx, tex.getWrapMode() == Texture.WrapMode.REPEAT, tex.getLinearFiltering(), (int) (65536.0f * dstBBox.getMinX()), (int) (65536.0f * dstBBox.getMinY()), (int) (65536.0f * dstBBox.getWidth()), (int) (65536.0f * dstBBox.getHeight()), lEdge, rEdge, tEdge, bEdge, txMin, tyMin, txMax, tyMax, swTex.hasAlpha());
        if (PrismSettings.debug) {
            System.out.println("* drawTexture, DONE");
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceH(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dh2, float sh1, float sh2) {
        drawTexture(tex, dx1, dy1, dh1, dy2, sx1, sy1, sh1, sy2, 0, 1, 0, 0);
        drawTexture(tex, dh1, dy1, dh2, dy2, sh1, sy1, sh2, sy2, 2, 1, 0, 0);
        drawTexture(tex, dh2, dy1, dx2, dy2, sh2, sy1, sx2, sy2, 2, 0, 0, 0);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceV(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dv1, float dv2, float sv1, float sv2) {
        drawTexture(tex, dx1, dy1, dx2, dv1, sx1, sy1, sx2, sv1, 0, 0, 0, 1);
        drawTexture(tex, dx1, dv1, dx2, dv2, sx1, sv1, sx2, sv2, 0, 0, 2, 1);
        drawTexture(tex, dx1, dv2, dx2, dy2, sx1, sv2, sx2, sy2, 0, 0, 2, 0);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture9Slice(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dv1, float dh2, float dv2, float sh1, float sv1, float sh2, float sv2) {
        drawTexture(tex, dx1, dy1, dh1, dv1, sx1, sy1, sh1, sv1, 0, 1, 0, 1);
        drawTexture(tex, dh1, dy1, dh2, dv1, sh1, sy1, sh2, sv1, 2, 1, 0, 1);
        drawTexture(tex, dh2, dy1, dx2, dv1, sh2, sy1, sx2, sv1, 2, 0, 0, 1);
        drawTexture(tex, dx1, dv1, dh1, dv2, sx1, sv1, sh1, sv2, 0, 1, 2, 1);
        drawTexture(tex, dh1, dv1, dh2, dv2, sh1, sv1, sh2, sv2, 2, 1, 2, 1);
        drawTexture(tex, dh2, dv1, dx2, dv2, sh2, sv1, sx2, sv2, 2, 0, 2, 1);
        drawTexture(tex, dx1, dv2, dh1, dy2, sx1, sv2, sh1, sy2, 0, 1, 2, 0);
        drawTexture(tex, dh1, dv2, dh2, dy2, sh1, sv2, sh2, sy2, 2, 1, 2, 0);
        drawTexture(tex, dh2, dv2, dx2, dy2, sh2, sv2, sx2, sy2, 2, 0, 2, 0);
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureVO(Texture tex, float topopacity, float botopacity, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        if (PrismSettings.debug) {
            System.out.println("* drawTextureVO");
        }
        int[] fractions = {0, 65536};
        int[] argb = {16777215 | (((int) (topopacity * 255.0f)) << 24), 16777215 | (((int) (botopacity * 255.0f)) << 24)};
        Transform6 t6 = new Transform6();
        SWUtils.convertToPiscesTransform(this.tx, t6);
        this.pr.setLinearGradient(0, (int) (65536.0f * dy1), 0, (int) (65536.0f * dy2), fractions, argb, 0, t6);
        drawTexture(tex, 2, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, 0, 0, 0, 0);
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTextureRaw");
        }
        int w2 = tex.getContentWidth();
        int h2 = tex.getContentHeight();
        drawTexture(tex, dx1, dy1, dx2, dy2, tx1 * w2, ty1 * h2, tx2 * w2, ty2 * h2);
    }

    @Override // com.sun.prism.Graphics
    public void drawMappedTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx11, float ty11, float tx21, float ty21, float tx12, float ty12, float tx22, float ty22) {
        if (PrismSettings.debug) {
            System.out.println("+ drawMappedTextureRaw");
        }
        double _mxx = this.tx.getMxx();
        double _myx = this.tx.getMyx();
        double _mxy = this.tx.getMxy();
        double _myy = this.tx.getMyy();
        double _mxt = this.tx.getMxt();
        double _myt = this.tx.getMyt();
        try {
            float mxx = tx21 - tx11;
            float myx = ty21 - ty11;
            float mxy = tx12 - tx11;
            float myy = ty12 - ty11;
            BaseTransform tmpTx = new Affine2D(mxx, myx, mxy, myy, tx11, ty11);
            tmpTx.invert();
            this.tx.setToIdentity();
            this.tx.deriveWithTranslation(dx1, dy1);
            this.tx.deriveWithConcatenation(dx2 - dx1, 0.0d, 0.0d, dy2 - dy2, 0.0d, 0.0d);
            this.tx.deriveWithConcatenation(tmpTx);
            drawTexture(tex, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, tex.getContentWidth(), tex.getContentHeight());
        } catch (NoninvertibleTransformException e2) {
        }
        this.tx.restoreTransform(_mxx, _myx, _mxy, _myy, _mxt, _myt);
    }

    @Override // com.sun.prism.ReadbackGraphics
    public boolean canReadBack() {
        return true;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public RTTexture readBack(Rectangle view) {
        if (PrismSettings.debug) {
            System.out.println("+ readBack, rect: " + ((Object) view) + ", target.dims: " + ((Object) this.target.getDimensions()));
        }
        int w2 = Math.max(1, view.width);
        int h2 = Math.max(1, view.height);
        SWRTTexture rbb = this.context.validateRBBuffer(w2, h2);
        if (view.isEmpty()) {
            return rbb;
        }
        int[] pixels = rbb.getDataNoClone();
        this.target.getSurface().getRGB(pixels, 0, rbb.getPhysicalWidth(), view.f11913x, view.f11914y, w2, h2);
        return rbb;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public void releaseReadBackBuffer(RTTexture view) {
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
    public void setLights(NGLightBase[] lights) {
    }

    @Override // com.sun.prism.Graphics
    public NGLightBase[] getLights() {
        return null;
    }

    @Override // com.sun.prism.Graphics
    public void blit(RTTexture srcTex, RTTexture dstTex, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1) {
        Graphics g2 = dstTex.createGraphics();
        g2.drawTexture(srcTex, dstX0, dstY0, dstX1, dstY1, srcX0, srcY0, srcX1, srcY1);
    }
}
