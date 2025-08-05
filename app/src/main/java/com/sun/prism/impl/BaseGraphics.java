package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseGraphics.class */
public abstract class BaseGraphics implements RectShadowGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    protected static final RoundRectangle2D scratchRRect = new RoundRectangle2D();
    protected static final Ellipse2D scratchEllipse = new Ellipse2D();
    protected static final Line2D scratchLine = new Line2D();
    protected static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private RectBounds devClipRect;
    private RectBounds finalClipRect;
    private Rectangle clipRect;
    private int clipRectIndex;
    protected float transX;
    protected float transY;
    private final BaseContext context;
    private final RenderTarget renderTarget;
    private NodePath renderRoot;
    private final Affine3D transform3D = new Affine3D();
    private NGCamera camera = NGCamera.INSTANCE;
    protected RectBounds nodeBounds = null;
    private boolean hasPreCullingBits = false;
    private float extraAlpha = 1.0f;
    private boolean antialiasedShape = true;
    private boolean depthBuffer = false;
    private boolean depthTest = false;
    protected Paint paint = DEFAULT_PAINT;
    protected BasicStroke stroke = DEFAULT_STROKE;
    protected boolean isSimpleTranslate = true;
    private boolean state3D = false;
    private float pixelScale = 1.0f;
    private CompositeMode compMode = CompositeMode.SRC_OVER;

    protected abstract void renderShape(Shape shape, BasicStroke basicStroke, float f2, float f3, float f4, float f5);

    protected BaseGraphics(BaseContext context, RenderTarget target) {
        this.context = context;
        this.renderTarget = target;
        this.devClipRect = new RectBounds(0.0f, 0.0f, target.getContentWidth(), target.getContentHeight());
        this.finalClipRect = new RectBounds(this.devClipRect);
        if (context != null) {
            context.setRenderTarget(this);
        }
    }

    protected NGCamera getCamera() {
        return this.camera;
    }

    @Override // com.sun.prism.Graphics
    public RenderTarget getRenderTarget() {
        return this.renderTarget;
    }

    @Override // com.sun.prism.Graphics
    public void setState3D(boolean flag) {
        this.state3D = flag;
    }

    @Override // com.sun.prism.Graphics
    public boolean isState3D() {
        return this.state3D;
    }

    @Override // com.sun.prism.Graphics
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
    }

    @Override // com.sun.prism.Graphics
    public ResourceFactory getResourceFactory() {
        return this.context.getResourceFactory();
    }

    @Override // com.sun.prism.Graphics
    public BaseTransform getTransformNoClone() {
        return this.transform3D;
    }

    @Override // com.sun.prism.Graphics
    public void setPerspectiveTransform(GeneralTransform3D transform) {
        this.context.setPerspectiveTransform(transform);
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(BaseTransform transform) {
        if (transform == null) {
            this.transform3D.setToIdentity();
        } else {
            this.transform3D.setTransform(transform);
        }
        validateTransformAndPaint();
    }

    @Override // com.sun.prism.Graphics
    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.transform3D.setTransform(m00, m10, m01, m11, m02, m12);
        validateTransformAndPaint();
    }

    @Override // com.sun.prism.Graphics
    public void setTransform3D(double mxx, double mxy, double mxz, double mxt, double myx, double myy, double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
        this.transform3D.setTransform(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
        validateTransformAndPaint();
    }

    @Override // com.sun.prism.Graphics
    public void transform(BaseTransform transform) {
        this.transform3D.concatenate(transform);
        validateTransformAndPaint();
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty) {
        if (tx != 0.0f || ty != 0.0f) {
            this.transform3D.translate(tx, ty);
            validateTransformAndPaint();
        }
    }

    @Override // com.sun.prism.Graphics
    public void translate(float tx, float ty, float tz) {
        if (tx != 0.0f || ty != 0.0f || tz != 0.0f) {
            this.transform3D.translate(tx, ty, tz);
            validateTransformAndPaint();
        }
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy) {
        if (sx != 1.0f || sy != 1.0f) {
            this.transform3D.scale(sx, sy);
            validateTransformAndPaint();
        }
    }

    @Override // com.sun.prism.Graphics
    public void scale(float sx, float sy, float sz) {
        if (sx != 1.0f || sy != 1.0f || sz != 1.0f) {
            this.transform3D.scale(sx, sy, sz);
            validateTransformAndPaint();
        }
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
    public final void setRenderRoot(NodePath root) {
        this.renderRoot = root;
    }

    @Override // com.sun.prism.Graphics
    public final NodePath getRenderRoot() {
        return this.renderRoot;
    }

    private void validateTransformAndPaint() {
        if (this.transform3D.isTranslateOrIdentity() && this.paint.getType() == Paint.Type.COLOR) {
            this.isSimpleTranslate = true;
            this.transX = (float) this.transform3D.getMxt();
            this.transY = (float) this.transform3D.getMyt();
        } else {
            this.isSimpleTranslate = false;
            this.transX = 0.0f;
            this.transY = 0.0f;
        }
    }

    @Override // com.sun.prism.Graphics
    public NGCamera getCameraNoClone() {
        return this.camera;
    }

    @Override // com.sun.prism.Graphics
    public void setDepthTest(boolean depthTest) {
        this.depthTest = depthTest;
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthTest() {
        return this.depthTest;
    }

    @Override // com.sun.prism.Graphics
    public void setDepthBuffer(boolean depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    @Override // com.sun.prism.Graphics
    public boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    @Override // com.sun.prism.Graphics
    public boolean isAlphaTestShader() {
        return PrismSettings.forceAlphaTestShader || (isDepthTest() && isDepthBuffer());
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
    public void setPixelScaleFactor(float pixelScale) {
        this.pixelScale = pixelScale;
    }

    @Override // com.sun.prism.Graphics
    public float getPixelScaleFactor() {
        return this.pixelScale;
    }

    @Override // com.sun.prism.Graphics
    public void setCamera(NGCamera camera) {
        this.camera = camera;
    }

    @Override // com.sun.prism.Graphics
    public Rectangle getClipRect() {
        if (this.clipRect != null) {
            return new Rectangle(this.clipRect);
        }
        return null;
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
        } else {
            this.clipRect = new Rectangle(clipRect);
            this.finalClipRect.intersectWith(clipRect);
        }
    }

    @Override // com.sun.prism.Graphics
    public float getExtraAlpha() {
        return this.extraAlpha;
    }

    @Override // com.sun.prism.Graphics
    public void setExtraAlpha(float extraAlpha) {
        this.extraAlpha = extraAlpha;
    }

    @Override // com.sun.prism.Graphics
    public CompositeMode getCompositeMode() {
        return this.compMode;
    }

    @Override // com.sun.prism.Graphics
    public void setCompositeMode(CompositeMode compMode) {
        this.compMode = compMode;
    }

    @Override // com.sun.prism.Graphics
    public Paint getPaint() {
        return this.paint;
    }

    @Override // com.sun.prism.Graphics
    public void setPaint(Paint paint) {
        this.paint = paint;
        validateTransformAndPaint();
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
    public void clear() {
        clear(Color.TRANSPARENT);
    }

    @Override // com.sun.prism.Graphics
    public void fill(Shape shape) {
        float bx2 = 0.0f;
        float by2 = 0.0f;
        float bw2 = 0.0f;
        float bh2 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                bx2 = this.nodeBounds.getMinX();
                by2 = this.nodeBounds.getMinY();
                bw2 = this.nodeBounds.getWidth();
                bh2 = this.nodeBounds.getHeight();
            } else {
                float[] bbox = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(bbox, shape, BaseTransform.IDENTITY_TRANSFORM);
                bx2 = bbox[0];
                by2 = bbox[1];
                bw2 = bbox[2] - bx2;
                bh2 = bbox[3] - by2;
            }
        }
        renderShape(shape, null, bx2, by2, bw2, bh2);
    }

    @Override // com.sun.prism.Graphics
    public void draw(Shape shape) {
        float bx2 = 0.0f;
        float by2 = 0.0f;
        float bw2 = 0.0f;
        float bh2 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                bx2 = this.nodeBounds.getMinX();
                by2 = this.nodeBounds.getMinY();
                bw2 = this.nodeBounds.getWidth();
                bh2 = this.nodeBounds.getHeight();
            } else {
                float[] bbox = {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(bbox, shape, BaseTransform.IDENTITY_TRANSFORM);
                bx2 = bbox[0];
                by2 = bbox[1];
                bw2 = bbox[2] - bx2;
                bh2 = bbox[3] - by2;
            }
        }
        renderShape(shape, this.stroke, bx2, by2, bw2, bh2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float x2, float y2, float w2, float h2) {
        drawTexture(tex, x2, y2, x2 + w2, y2 + h2, 0.0f, 0.0f, w2, h2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        BaseTransform xform = this.isSimpleTranslate ? IDENT : getTransformNoClone();
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        if (this.isSimpleTranslate) {
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        float pw = tex.getPhysicalWidth();
        float ph = tex.getPhysicalHeight();
        float cx1 = tex.getContentX();
        float cy1 = tex.getContentY();
        float tx1 = (cx1 + sx1) / pw;
        float ty1 = (cy1 + sy1) / ph;
        float tx2 = (cx1 + sx2) / pw;
        float ty2 = (cy1 + sy2) / ph;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vb.addSuperQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2, false);
        } else {
            vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceH(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dh2, float sh1, float sh2) {
        BaseTransform xform = this.isSimpleTranslate ? IDENT : getTransformNoClone();
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        if (this.isSimpleTranslate) {
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
            dh1 += this.transX;
            dh2 += this.transX;
        }
        float pw = tex.getPhysicalWidth();
        float ph = tex.getPhysicalHeight();
        float cx1 = tex.getContentX();
        float cy1 = tex.getContentY();
        float tx1 = (cx1 + sx1) / pw;
        float ty1 = (cy1 + sy1) / ph;
        float tx2 = (cx1 + sx2) / pw;
        float ty2 = (cy1 + sy2) / ph;
        float th1 = (cx1 + sh1) / pw;
        float th2 = (cx1 + sh2) / pw;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vb.addSuperQuad(dx1, dy1, dh1, dy2, tx1, ty1, th1, ty2, false);
            vb.addSuperQuad(dh1, dy1, dh2, dy2, th1, ty1, th2, ty2, false);
            vb.addSuperQuad(dh2, dy1, dx2, dy2, th2, ty1, tx2, ty2, false);
        } else {
            vb.addQuad(dx1, dy1, dh1, dy2, tx1, ty1, th1, ty2);
            vb.addQuad(dh1, dy1, dh2, dy2, th1, ty1, th2, ty2);
            vb.addQuad(dh2, dy1, dx2, dy2, th2, ty1, tx2, ty2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture3SliceV(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dv1, float dv2, float sv1, float sv2) {
        BaseTransform xform = this.isSimpleTranslate ? IDENT : getTransformNoClone();
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        if (this.isSimpleTranslate) {
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
            dv1 += this.transY;
            dv2 += this.transY;
        }
        float pw = tex.getPhysicalWidth();
        float ph = tex.getPhysicalHeight();
        float cx1 = tex.getContentX();
        float cy1 = tex.getContentY();
        float tx1 = (cx1 + sx1) / pw;
        float ty1 = (cy1 + sy1) / ph;
        float tx2 = (cx1 + sx2) / pw;
        float ty2 = (cy1 + sy2) / ph;
        float tv1 = (cy1 + sv1) / ph;
        float tv2 = (cy1 + sv2) / ph;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vb.addSuperQuad(dx1, dy1, dx2, dv1, tx1, ty1, tx2, tv1, false);
            vb.addSuperQuad(dx1, dv1, dx2, dv2, tx1, tv1, tx2, tv2, false);
            vb.addSuperQuad(dx1, dv2, dx2, dy2, tx1, tv2, tx2, ty2, false);
        } else {
            vb.addQuad(dx1, dy1, dx2, dv1, tx1, ty1, tx2, tv1);
            vb.addQuad(dx1, dv1, dx2, dv2, tx1, tv1, tx2, tv2);
            vb.addQuad(dx1, dv2, dx2, dy2, tx1, tv2, tx2, ty2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTexture9Slice(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dv1, float dh2, float dv2, float sh1, float sv1, float sh2, float sv2) {
        BaseTransform xform = this.isSimpleTranslate ? IDENT : getTransformNoClone();
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        if (this.isSimpleTranslate) {
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
            dh1 += this.transX;
            dv1 += this.transY;
            dh2 += this.transX;
            dv2 += this.transY;
        }
        float pw = tex.getPhysicalWidth();
        float ph = tex.getPhysicalHeight();
        float cx1 = tex.getContentX();
        float cy1 = tex.getContentY();
        float tx1 = (cx1 + sx1) / pw;
        float ty1 = (cy1 + sy1) / ph;
        float tx2 = (cx1 + sx2) / pw;
        float ty2 = (cy1 + sy2) / ph;
        float th1 = (cx1 + sh1) / pw;
        float tv1 = (cy1 + sv1) / ph;
        float th2 = (cx1 + sh2) / pw;
        float tv2 = (cy1 + sv2) / ph;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vb.addSuperQuad(dx1, dy1, dh1, dv1, tx1, ty1, th1, tv1, false);
            vb.addSuperQuad(dh1, dy1, dh2, dv1, th1, ty1, th2, tv1, false);
            vb.addSuperQuad(dh2, dy1, dx2, dv1, th2, ty1, tx2, tv1, false);
            vb.addSuperQuad(dx1, dv1, dh1, dv2, tx1, tv1, th1, tv2, false);
            vb.addSuperQuad(dh1, dv1, dh2, dv2, th1, tv1, th2, tv2, false);
            vb.addSuperQuad(dh2, dv1, dx2, dv2, th2, tv1, tx2, tv2, false);
            vb.addSuperQuad(dx1, dv2, dh1, dy2, tx1, tv2, th1, ty2, false);
            vb.addSuperQuad(dh1, dv2, dh2, dy2, th1, tv2, th2, ty2, false);
            vb.addSuperQuad(dh2, dv2, dx2, dy2, th2, tv2, tx2, ty2, false);
            return;
        }
        vb.addQuad(dx1, dy1, dh1, dv1, tx1, ty1, th1, tv1);
        vb.addQuad(dh1, dy1, dh2, dv1, th1, ty1, th2, tv1);
        vb.addQuad(dh2, dy1, dx2, dv1, th2, ty1, tx2, tv1);
        vb.addQuad(dx1, dv1, dh1, dv2, tx1, tv1, th1, tv2);
        vb.addQuad(dh1, dv1, dh2, dv2, th1, tv1, th2, tv2);
        vb.addQuad(dh2, dv1, dx2, dv2, th2, tv1, tx2, tv2);
        vb.addQuad(dx1, dv2, dh1, dy2, tx1, tv2, th1, ty2);
        vb.addQuad(dh1, dv2, dh2, dy2, th1, tv2, th2, ty2);
        vb.addQuad(dh2, dv2, dx2, dy2, th2, tv2, tx2, ty2);
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureVO(Texture tex, float topopacity, float botopacity, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        BaseTransform xform = this.isSimpleTranslate ? IDENT : getTransformNoClone();
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        if (this.isSimpleTranslate) {
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        float tw = tex.getPhysicalWidth();
        float th = tex.getPhysicalHeight();
        float cx1 = tex.getContentX();
        float cy1 = tex.getContentY();
        float tx1 = (cx1 + sx1) / tw;
        float ty1 = (cy1 + sy1) / th;
        float tx2 = (cx1 + sx2) / tw;
        float ty2 = (cy1 + sy2) / th;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (topopacity == 1.0f && botopacity == 1.0f) {
            vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
        } else {
            vb.addQuadVO(topopacity * getExtraAlpha(), botopacity * getExtraAlpha(), dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx1, float ty1, float tx2, float ty2) {
        float bw2 = dx2 - dx1;
        float bh2 = dy2 - dy1;
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            xform = IDENT;
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, bw2, bh2);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
    }

    @Override // com.sun.prism.Graphics
    public void drawMappedTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx11, float ty11, float tx21, float ty21, float tx12, float ty12, float tx22, float ty22) {
        float bw2 = dx2 - dx1;
        float bh2 = dy2 - dy1;
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            xform = IDENT;
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        PixelFormat format = tex.getPixelFormat();
        if (format == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, xform, tex, dx1, dy1, bw2, bh2);
        } else {
            this.context.validateTextureOp(this, xform, tex, format);
        }
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addMappedQuad(dx1, dy1, dx2, dy2, tx11, ty11, tx21, ty21, tx12, ty12, tx22, ty22);
    }
}
