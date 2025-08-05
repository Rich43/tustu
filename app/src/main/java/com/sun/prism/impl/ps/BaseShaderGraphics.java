package com.sun.prism.impl.ps;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderGraphics.class */
public abstract class BaseShaderGraphics extends BaseGraphics implements ShaderGraphics, ReadbackGraphics, MaskTextureGraphics {
    private final BaseShaderContext context;
    private Shader externalShader;
    private boolean isComplexPaint;
    private NGLightBase[] lights;
    private static final float FRINGE_FACTOR;
    private static final double SQRT_2;
    private boolean lcdSampleInvalid;
    private static Affine2D TEMP_TX2D = new Affine2D();
    private static Affine3D TEMP_TX3D = new Affine3D();
    private static RectBounds TMP_BOUNDS = new RectBounds();

    static {
        String v2 = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("prism.primshaderpad");
        });
        if (v2 == null) {
            FRINGE_FACTOR = -0.5f;
        } else {
            FRINGE_FACTOR = -Float.valueOf(v2).floatValue();
            System.out.println("Prism ShaderGraphics primitive shader pad = " + FRINGE_FACTOR);
        }
        SQRT_2 = Math.sqrt(2.0d);
    }

    protected BaseShaderGraphics(BaseShaderContext context, RenderTarget renderTarget) {
        super(context, renderTarget);
        this.lights = null;
        this.lcdSampleInvalid = false;
        this.context = context;
    }

    BaseShaderContext getContext() {
        return this.context;
    }

    boolean isComplexPaint() {
        return this.isComplexPaint;
    }

    public void getPaintShaderTransform(Affine3D ret) {
        ret.setTransform(getTransformNoClone());
    }

    public Shader getExternalShader() {
        return this.externalShader;
    }

    @Override // com.sun.prism.ps.ShaderGraphics
    public void setExternalShader(Shader shader) {
        this.externalShader = shader;
        this.context.setExternalShader(this, shader);
    }

    @Override // com.sun.prism.impl.BaseGraphics, com.sun.prism.Graphics
    public void setPaint(Paint paint) {
        if (paint.getType().isGradient()) {
            Gradient grad = (Gradient) paint;
            this.isComplexPaint = grad.getNumStops() > 12;
        } else {
            this.isComplexPaint = false;
        }
        super.setPaint(paint);
    }

    @Override // com.sun.prism.Graphics
    public void setLights(NGLightBase[] lights) {
        this.lights = lights;
    }

    @Override // com.sun.prism.Graphics
    public final NGLightBase[] getLights() {
        return this.lights;
    }

    @Override // com.sun.prism.impl.BaseGraphics, com.sun.prism.Graphics
    public void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        if (tex instanceof MultiTexture) {
            drawMultiTexture((MultiTexture) tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
        } else {
            super.drawTexture(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
        }
    }

    @Override // com.sun.prism.impl.BaseGraphics, com.sun.prism.Graphics
    public void drawTexture3SliceH(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dh2, float sh1, float sh2) {
        if (!(tex instanceof MultiTexture)) {
            super.drawTexture3SliceH(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dh1, dh2, sh1, sh2);
            return;
        }
        MultiTexture mtex = (MultiTexture) tex;
        drawMultiTexture(mtex, dx1, dy1, dh1, dy2, sx1, sy1, sh1, sy2);
        drawMultiTexture(mtex, dh1, dy1, dh2, dy2, sh1, sy1, sh2, sy2);
        drawMultiTexture(mtex, dh2, dy1, dx2, dy2, sh2, sy1, sx2, sy2);
    }

    @Override // com.sun.prism.impl.BaseGraphics, com.sun.prism.Graphics
    public void drawTexture3SliceV(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dv1, float dv2, float sv1, float sv2) {
        if (!(tex instanceof MultiTexture)) {
            super.drawTexture3SliceV(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dv1, dv2, sv1, sv2);
            return;
        }
        MultiTexture mtex = (MultiTexture) tex;
        drawMultiTexture(mtex, dx1, dy1, dx2, dv1, sx1, sy1, sx2, sv1);
        drawMultiTexture(mtex, dx1, dv1, dx2, dv2, sx1, sv1, sx2, sv2);
        drawMultiTexture(mtex, dx1, dv2, dx2, dy2, sx1, sv2, sx2, sy2);
    }

    @Override // com.sun.prism.impl.BaseGraphics, com.sun.prism.Graphics
    public void drawTexture9Slice(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2, float dh1, float dv1, float dh2, float dv2, float sh1, float sv1, float sh2, float sv2) {
        if (!(tex instanceof MultiTexture)) {
            super.drawTexture9Slice(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dh1, dv1, dh2, dv2, sh1, sv1, sh2, sv2);
            return;
        }
        MultiTexture mtex = (MultiTexture) tex;
        drawMultiTexture(mtex, dx1, dy1, dh1, dv1, sx1, sy1, sh1, sv1);
        drawMultiTexture(mtex, dh1, dy1, dh2, dv1, sh1, sy1, sh2, sv1);
        drawMultiTexture(mtex, dh2, dy1, dx2, dv1, sh2, sy1, sx2, sv1);
        drawMultiTexture(mtex, dx1, dv1, dh1, dv2, sx1, sv1, sh1, sv2);
        drawMultiTexture(mtex, dh1, dv1, dh2, dv2, sh1, sv1, sh2, sv2);
        drawMultiTexture(mtex, dh2, dv1, dx2, dv2, sh2, sv1, sx2, sv2);
        drawMultiTexture(mtex, dx1, dv2, dh1, dy2, sx1, sv2, sh1, sy2);
        drawMultiTexture(mtex, dh1, dv2, dh2, dy2, sh1, sv2, sh2, sy2);
        drawMultiTexture(mtex, dh2, dv2, dx2, dy2, sh2, sv2, sx2, sy2);
    }

    private static float calculateScaleFactor(float contentDim, float physicalDim) {
        if (contentDim == physicalDim) {
            return 1.0f;
        }
        return (contentDim - 1.0f) / physicalDim;
    }

    protected void drawMultiTexture(MultiTexture tex, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        float alphaScaleY;
        float alphaScaleX;
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            xform = IDENT;
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        Texture[] textures = tex.getTextures();
        Shader shader = this.context.validateTextureOp(this, xform, textures, tex.getPixelFormat());
        if (null == shader) {
            return;
        }
        if (tex.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            Texture lumaTex = textures[0];
            Texture cbTex = textures[2];
            Texture crTex = textures[1];
            float imgWidth = tex.getContentWidth();
            float imgHeight = tex.getContentHeight();
            float lumaScaleX = calculateScaleFactor(imgWidth, lumaTex.getPhysicalWidth());
            float lumaScaleY = calculateScaleFactor(imgHeight, lumaTex.getPhysicalHeight());
            if (textures.length > 3) {
                Texture alphaTex = textures[3];
                alphaScaleX = calculateScaleFactor(imgWidth, alphaTex.getPhysicalWidth());
                alphaScaleY = calculateScaleFactor(imgHeight, alphaTex.getPhysicalHeight());
            } else {
                alphaScaleY = 0.0f;
                alphaScaleX = 0.0f;
            }
            float chromaWidth = (float) Math.floor(imgWidth / 2.0d);
            float chromaHeight = (float) Math.floor(imgHeight / 2.0d);
            float cbScaleX = calculateScaleFactor(chromaWidth, cbTex.getPhysicalWidth());
            float cbScaleY = calculateScaleFactor(chromaHeight, cbTex.getPhysicalHeight());
            float crScaleX = calculateScaleFactor(chromaWidth, crTex.getPhysicalWidth());
            float crScaleY = calculateScaleFactor(chromaHeight, crTex.getPhysicalHeight());
            shader.setConstant("lumaAlphaScale", lumaScaleX, lumaScaleY, alphaScaleX, alphaScaleY);
            shader.setConstant("cbCrScale", cbScaleX, cbScaleY, crScaleX, crScaleY);
            float tx1 = sx1 / imgWidth;
            float ty1 = sy1 / imgHeight;
            float tx2 = sx2 / imgWidth;
            float ty2 = sy2 / imgHeight;
            VertexBuffer vb = this.context.getVertexBuffer();
            vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
            return;
        }
        throw new UnsupportedOperationException("Unsupported multitexture format " + ((Object) tex.getPixelFormat()));
    }

    @Override // com.sun.prism.ps.ShaderGraphics
    public void drawTextureRaw2(Texture src1, Texture src2, float dx1, float dy1, float dx2, float dy2, float t1x1, float t1y1, float t1x2, float t1y2, float t2x1, float t2y1, float t2x2, float t2y2) {
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            xform = IDENT;
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        this.context.validateTextureOp(this, xform, src1, src2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(dx1, dy1, dx2, dy2, t1x1, t1y1, t1x2, t1y2, t2x1, t2y1, t2x2, t2y2);
    }

    @Override // com.sun.prism.ps.ShaderGraphics
    public void drawMappedTextureRaw2(Texture src1, Texture src2, float dx1, float dy1, float dx2, float dy2, float t1x11, float t1y11, float t1x21, float t1y21, float t1x12, float t1y12, float t1x22, float t1y22, float t2x11, float t2y11, float t2x21, float t2y21, float t2x12, float t2y12, float t2x22, float t2y22) {
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            xform = IDENT;
            dx1 += this.transX;
            dy1 += this.transY;
            dx2 += this.transX;
            dy2 += this.transY;
        }
        this.context.validateTextureOp(this, xform, src1, src2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addMappedQuad(dx1, dy1, dx2, dy2, t1x11, t1y11, t1x21, t1y21, t1x12, t1y12, t1x22, t1y22, t2x11, t2y11, t2x21, t2y21, t2x12, t2y12, t2x22, t2y22);
    }

    @Override // com.sun.prism.MaskTextureGraphics
    public void drawPixelsMasked(RTTexture imgtex, RTTexture masktex, int dx, int dy, int dw, int dh, int ix, int iy, int mx, int my) {
        if (dw <= 0 || dh <= 0) {
            return;
        }
        float iw = imgtex.getPhysicalWidth();
        float ih = imgtex.getPhysicalHeight();
        float mw = masktex.getPhysicalWidth();
        float mh = masktex.getPhysicalHeight();
        float dx1 = dx;
        float dy1 = dy;
        float dx2 = dx + dw;
        float dy2 = dy + dh;
        float ix1 = ix / iw;
        float iy1 = iy / ih;
        float ix2 = (ix + dw) / iw;
        float iy2 = (iy + dh) / ih;
        float mx1 = mx / mw;
        float my1 = my / mh;
        float mx2 = (mx + dw) / mw;
        float my2 = (my + dh) / mh;
        this.context.validateMaskTextureOp(this, IDENT, imgtex, masktex, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(dx1, dy1, dx2, dy2, ix1, iy1, ix2, iy2, mx1, my1, mx2, my2);
    }

    @Override // com.sun.prism.MaskTextureGraphics
    public void maskInterpolatePixels(RTTexture imgtex, RTTexture masktex, int dx, int dy, int dw, int dh, int ix, int iy, int mx, int my) {
        if (dw <= 0 || dh <= 0) {
            return;
        }
        float iw = imgtex.getPhysicalWidth();
        float ih = imgtex.getPhysicalHeight();
        float mw = masktex.getPhysicalWidth();
        float mh = masktex.getPhysicalHeight();
        float dx1 = dx;
        float dy1 = dy;
        float dx2 = dx + dw;
        float dy2 = dy + dh;
        float ix1 = ix / iw;
        float iy1 = iy / ih;
        float ix2 = (ix + dw) / iw;
        float iy2 = (iy + dh) / ih;
        float mx1 = mx / mw;
        float my1 = my / mh;
        float mx2 = (mx + dw) / mw;
        float my2 = (my + dh) / mh;
        CompositeMode oldmode = getCompositeMode();
        setCompositeMode(CompositeMode.DST_OUT);
        this.context.validateTextureOp(this, IDENT, masktex, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(dx1, dy1, dx2, dy2, mx1, my1, mx2, my2);
        setCompositeMode(CompositeMode.ADD);
        this.context.validateMaskTextureOp(this, IDENT, imgtex, masktex, PixelFormat.INT_ARGB_PRE);
        vb.addQuad(dx1, dy1, dx2, dy2, ix1, iy1, ix2, iy2, mx1, my1, mx2, my2);
        setCompositeMode(oldmode);
    }

    private void renderWithComplexPaint(Shape shape, BasicStroke stroke, float bx2, float by2, float bw2, float bh2) {
        this.context.flushVertexBuffer();
        BaseTransform xform = getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, stroke, getFinalClipNoClone(), xform, true, isAntialiasedShape());
        int maskW = maskData.getWidth();
        int maskH = maskData.getHeight();
        float dx1 = maskData.getOriginX();
        float dy1 = maskData.getOriginY();
        float dx2 = dx1 + maskW;
        float dy2 = dy1 + maskH;
        Gradient grad = (Gradient) this.paint;
        TEMP_TX2D.setToTranslation(-dx1, -dy1);
        TEMP_TX2D.concatenate(xform);
        Texture tex = this.context.getGradientTexture(grad, TEMP_TX2D, maskW, maskH, maskData, bx2, by2, bw2, bh2);
        float tx2 = 0.0f + (maskW / tex.getPhysicalWidth());
        float ty2 = 0.0f + (maskH / tex.getPhysicalHeight());
        VertexBuffer vb = this.context.getVertexBuffer();
        this.context.validateTextureOp(this, IDENT, tex, null, tex.getPixelFormat());
        vb.addQuad(dx1, dy1, dx2, dy2, 0.0f, 0.0f, tx2, ty2);
        tex.unlock();
    }

    @Override // com.sun.prism.impl.BaseGraphics
    protected void renderShape(Shape shape, BasicStroke stroke, float bx2, float by2, float bw2, float bh2) {
        AffineBase paintTx;
        if (this.isComplexPaint) {
            renderWithComplexPaint(shape, stroke, bx2, by2, bw2, bh2);
            return;
        }
        BaseTransform xform = getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, stroke, getFinalClipNoClone(), xform, true, isAntialiasedShape());
        Texture maskTex = this.context.validateMaskTexture(maskData, false);
        if (PrismSettings.primTextureSize != 0) {
            Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, maskTex, bx2, by2, bw2, bh2);
            paintTx = getPaintTextureTx(xform, shader, bx2, by2, bw2, bh2);
        } else {
            this.context.validatePaintOp(this, IDENT, maskTex, bx2, by2, bw2, bh2);
            paintTx = null;
        }
        this.context.updateMaskTexture(maskData, TMP_BOUNDS, false);
        float dx1 = maskData.getOriginX();
        float dy1 = maskData.getOriginY();
        float dx2 = dx1 + maskData.getWidth();
        float dy2 = dy1 + maskData.getHeight();
        float tx1 = TMP_BOUNDS.getMinX();
        float ty1 = TMP_BOUNDS.getMinY();
        float tx2 = TMP_BOUNDS.getMaxX();
        float ty2 = TMP_BOUNDS.getMaxY();
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2, paintTx);
        maskTex.unlock();
    }

    private static float getStrokeExpansionFactor(BasicStroke stroke) {
        if (stroke.getType() == 2) {
            return 1.0f;
        }
        if (stroke.getType() == 0) {
            return 0.5f;
        }
        return 0.0f;
    }

    private BaseTransform extract3Dremainder(BaseTransform xform) {
        if (xform.is2D()) {
            return IDENT;
        }
        TEMP_TX3D.setTransform(xform);
        TEMP_TX2D.setTransform(xform.getMxx(), xform.getMyx(), xform.getMxy(), xform.getMyy(), xform.getMxt(), xform.getMyt());
        try {
            TEMP_TX2D.invert();
            TEMP_TX3D.concatenate(TEMP_TX2D);
        } catch (NoninvertibleTransformException e2) {
        }
        return TEMP_TX3D;
    }

    private void renderGeneralRoundedRect(float rx, float ry, float rw, float rh, float arcw, float arch, BaseShaderContext.MaskType type, BasicStroke stroke) {
        float bx2;
        float by2;
        float bw2;
        float bh2;
        float ifractw;
        float ifracth;
        BaseTransform rendertx;
        float wdx;
        float hdx;
        float wdy;
        float hdy;
        float ox;
        float oy;
        if (stroke == null) {
            bx2 = rx;
            by2 = ry;
            bw2 = rw;
            bh2 = rh;
            ifracth = 0.0f;
            ifractw = 0.0f;
        } else {
            float sw = stroke.getLineWidth();
            float ow = getStrokeExpansionFactor(stroke) * sw;
            bx2 = rx - ow;
            by2 = ry - ow;
            float ow2 = ow * 2.0f;
            bw2 = rw + ow2;
            bh2 = rh + ow2;
            if (arcw > 0.0f && arch > 0.0f) {
                arcw += ow2;
                arch += ow2;
            } else if (stroke.getLineJoin() == 1) {
                arch = ow2;
                arcw = ow2;
                type = BaseShaderContext.MaskType.DRAW_ROUNDRECT;
            } else {
                arch = 0.0f;
                arcw = 0.0f;
            }
            ifractw = (bw2 - (sw * 2.0f)) / bw2;
            ifracth = (bh2 - (sw * 2.0f)) / bh2;
            if (ifractw <= 0.0f || ifracth <= 0.0f) {
                type = type.getFillType();
            }
        }
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            hdy = 1.0f;
            wdx = 1.0f;
            hdx = 0.0f;
            wdy = 0.0f;
            ox = bx2 + this.transX;
            oy = by2 + this.transY;
            rendertx = IDENT;
        } else {
            rendertx = extract3Dremainder(xform);
            wdx = (float) xform.getMxx();
            hdx = (float) xform.getMxy();
            wdy = (float) xform.getMyx();
            hdy = (float) xform.getMyy();
            ox = (bx2 * wdx) + (by2 * hdx) + ((float) xform.getMxt());
            oy = (bx2 * wdy) + (by2 * hdy) + ((float) xform.getMyt());
        }
        float arcfractw = arcw / bw2;
        float arcfracth = arch / bh2;
        renderGeneralRoundedPgram(ox, oy, wdx * bw2, wdy * bw2, hdx * bh2, hdy * bh2, arcfractw, arcfracth, ifractw, ifracth, rendertx, type, rx, ry, rw, rh);
    }

    private void renderGeneralRoundedPgram(float ox, float oy, float wvecx, float wvecy, float hvecx, float hvecy, float arcfractw, float arcfracth, float ifractw, float ifracth, BaseTransform rendertx, BaseShaderContext.MaskType type, float rx, float ry, float rw, float rh) {
        float ivalh;
        float ivalw;
        float wlen = len(wvecx, wvecy);
        float hlen = len(hvecx, hvecy);
        if (wlen == 0.0f || hlen == 0.0f) {
            return;
        }
        float xUR = ox + wvecx;
        float yUR = oy + wvecy;
        float xLL = ox + hvecx;
        float yLL = oy + hvecy;
        float xLR = xUR + hvecx;
        float yLR = yUR + hvecy;
        float halfarea = ((wvecx * hvecy) - (wvecy * hvecx)) * 0.5f;
        float pwdist = halfarea / hlen;
        float phdist = halfarea / wlen;
        if (pwdist < 0.0f) {
            pwdist = -pwdist;
        }
        if (phdist < 0.0f) {
            phdist = -phdist;
        }
        float nwvecx = wvecx / wlen;
        float nwvecy = wvecy / wlen;
        float nhvecx = hvecx / hlen;
        float nhvecy = hvecy / hlen;
        float num = ((-hvecx) * (nwvecx + nhvecx)) - (hvecy * (nwvecy + nhvecy));
        float den = (hvecy * wvecx) - (hvecx * wvecy);
        float t2 = num / den;
        float factor = FRINGE_FACTOR * Math.signum(t2);
        float offx = ((t2 * wvecx) + nwvecy) * factor;
        float offy = ((t2 * wvecy) - nwvecx) * factor;
        float xUL = ox + offx;
        float yUL = oy + offy;
        float xLR2 = xLR - offx;
        float yLR2 = yLR - offy;
        float num2 = (wvecy * (nhvecy - nwvecy)) - (wvecx * (nwvecx - nhvecx));
        float t3 = num2 / den;
        float factor2 = FRINGE_FACTOR * Math.signum(t3);
        float offx2 = ((t3 * hvecx) + nhvecy) * factor2;
        float offy2 = ((t3 * hvecy) - nhvecx) * factor2;
        float xUR2 = xUR + offx2;
        float yUR2 = yUR + offy2;
        float xLL2 = xLL - offx2;
        float yLL2 = yLL - offy2;
        float xC = (xUL + xLR2) * 0.5f;
        float yC = (yUL + yLR2) * 0.5f;
        float uC = (xC * nhvecy) - (yC * nhvecx);
        float vC = (xC * nwvecy) - (yC * nwvecx);
        float uUL = ((xUL * nhvecy) - (yUL * nhvecx)) - uC;
        float vUL = ((xUL * nwvecy) - (yUL * nwvecx)) - vC;
        float uUR = ((xUR2 * nhvecy) - (yUR2 * nhvecx)) - uC;
        float vUR = ((xUR2 * nwvecy) - (yUR2 * nwvecx)) - vC;
        float uLL = ((xLL2 * nhvecy) - (yLL2 * nhvecx)) - uC;
        float vLL = ((xLL2 * nwvecy) - (yLL2 * nwvecx)) - vC;
        float uLR = ((xLR2 * nhvecy) - (yLR2 * nhvecx)) - uC;
        float vLR = ((xLR2 * nwvecy) - (yLR2 * nwvecx)) - vC;
        if (type == BaseShaderContext.MaskType.DRAW_ROUNDRECT || type == BaseShaderContext.MaskType.FILL_ROUNDRECT) {
            float oarcw = pwdist * arcfractw;
            float oarch = phdist * arcfracth;
            if (oarcw < 0.5d || oarch < 0.5d) {
                type = type == BaseShaderContext.MaskType.DRAW_ROUNDRECT ? BaseShaderContext.MaskType.DRAW_PGRAM : BaseShaderContext.MaskType.FILL_PGRAM;
            } else {
                float flatw = pwdist - oarcw;
                float flath = phdist - oarch;
                if (type == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    float iwdist = pwdist * ifractw;
                    float ihdist = phdist * ifracth;
                    float ivalw2 = iwdist - flatw;
                    float ivalh2 = ihdist - flath;
                    if (ivalw2 < 0.5f || ivalh2 < 0.5f) {
                        ivalw = iwdist;
                        ivalh = ihdist;
                        type = BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT;
                    } else {
                        ivalw = 1.0f / ivalw2;
                        ivalh = 1.0f / ivalh2;
                    }
                } else {
                    ivalh = 0.0f;
                    ivalw = 0.0f;
                }
                float oarcw2 = 1.0f / oarcw;
                float oarch2 = 1.0f / oarch;
                Shader shader = this.context.validatePaintOp(this, rendertx, type, rx, ry, rw, rh, oarcw2, oarch2, ivalw, ivalh, 0.0f, 0.0f);
                shader.setConstant("oinvarcradii", oarcw2, oarch2);
                if (type == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    shader.setConstant("iinvarcradii", ivalw, ivalh);
                } else if (type == BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT) {
                    shader.setConstant("idim", ivalw, ivalh);
                }
                pwdist = flatw;
                phdist = flath;
            }
        }
        if (type == BaseShaderContext.MaskType.DRAW_PGRAM || type == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
            float idimw = pwdist * ifractw;
            float idimh = phdist * ifracth;
            if (type == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
                if (Math.abs(pwdist - phdist) < 0.01d) {
                    type = BaseShaderContext.MaskType.DRAW_CIRCLE;
                    phdist = (float) Math.min(1.0d, phdist * phdist * 3.141592653589793d);
                    idimh = (float) Math.min(1.0d, idimh * idimh * 3.141592653589793d);
                } else {
                    pwdist = 1.0f / pwdist;
                    phdist = 1.0f / phdist;
                    idimw = 1.0f / idimw;
                    idimh = 1.0f / idimh;
                }
            }
            this.context.validatePaintOp(this, rendertx, type, rx, ry, rw, rh, idimw, idimh, 0.0f, 0.0f, 0.0f, 0.0f).setConstant("idim", idimw, idimh);
        } else if (type == BaseShaderContext.MaskType.FILL_ELLIPSE) {
            if (Math.abs(pwdist - phdist) < 0.01d) {
                type = BaseShaderContext.MaskType.FILL_CIRCLE;
                phdist = (float) Math.min(1.0d, phdist * phdist * 3.141592653589793d);
            } else {
                pwdist = 1.0f / pwdist;
                phdist = 1.0f / phdist;
                uUL *= pwdist;
                vUL *= phdist;
                uUR *= pwdist;
                vUR *= phdist;
                uLL *= pwdist;
                vLL *= phdist;
                uLR *= pwdist;
                vLR *= phdist;
            }
            this.context.validatePaintOp(this, rendertx, type, rx, ry, rw, rh);
        } else if (type == BaseShaderContext.MaskType.FILL_PGRAM) {
            this.context.validatePaintOp(this, rendertx, type, rx, ry, rw, rh);
        }
        this.context.getVertexBuffer().addMappedPgram(xUL, yUL, xUR2, yUR2, xLL2, yLL2, xLR2, yLR2, uUL, vUL, uUR, vUR, uLL, vLL, uLR, vLR, pwdist, phdist);
    }

    AffineBase getPaintTextureTx(BaseTransform renderTx, Shader shader, float rx, float ry, float rw, float rh) {
        switch (this.paint.getType()) {
            case COLOR:
                return null;
            case LINEAR_GRADIENT:
                return PaintHelper.getLinearGradientTx((LinearGradient) this.paint, shader, renderTx, rx, ry, rw, rh);
            case RADIAL_GRADIENT:
                return PaintHelper.getRadialGradientTx((RadialGradient) this.paint, shader, renderTx, rx, ry, rw, rh);
            case IMAGE_PATTERN:
                return PaintHelper.getImagePatternTx(this, (ImagePattern) this.paint, shader, renderTx, rx, ry, rw, rh);
            default:
                throw new InternalError("Unrecogized paint type: " + ((Object) this.paint));
        }
    }

    boolean fillPrimRect(float x2, float y2, float w2, float h2, Texture rectTex, Texture wrapTex, float bx2, float by2, float bw2, float bh2) {
        BaseTransform xform = getTransformNoClone();
        float mxx = (float) xform.getMxx();
        float mxy = (float) xform.getMxy();
        float mxt = (float) xform.getMxt();
        float myx = (float) xform.getMyx();
        float myy = (float) xform.getMyy();
        float myt = (float) xform.getMyt();
        float dxdist = len(mxx, myx);
        float dydist = len(mxy, myy);
        if (dxdist == 0.0f || dydist == 0.0f) {
            return true;
        }
        float pixelw = 1.0f / dxdist;
        float pixelh = 1.0f / dydist;
        float x0 = x2 - (pixelw * 0.5f);
        float y0 = y2 - (pixelh * 0.5f);
        float x1 = x2 + w2 + (pixelw * 0.5f);
        float y1 = y2 + h2 + (pixelh * 0.5f);
        int cellw = (int) Math.ceil((w2 * dxdist) - 0.001953125f);
        int cellh = (int) Math.ceil((h2 * dydist) - 0.001953125f);
        VertexBuffer vb = this.context.getVertexBuffer();
        int max = this.context.getRectTextureMaxSize();
        if (cellw <= max && cellh <= max) {
            float u0 = ((cellw * (cellw + 1)) / 2) - 0.5f;
            float v0 = ((cellh * (cellh + 1)) / 2) - 0.5f;
            float u1 = u0 + cellw + 1.0f;
            float v1 = v0 + cellh + 1.0f;
            float u02 = u0 / rectTex.getPhysicalWidth();
            float v02 = v0 / rectTex.getPhysicalHeight();
            float u12 = u1 / rectTex.getPhysicalWidth();
            float v12 = v1 / rectTex.getPhysicalHeight();
            if (xform.isTranslateOrIdentity()) {
                x0 += mxt;
                y0 += myt;
                x1 += mxt;
                y1 += myt;
                xform = IDENT;
            } else {
                if (xform.is2D()) {
                    Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, rectTex, bx2, by2, bw2, bh2);
                    AffineBase paintTx = getPaintTextureTx(IDENT, shader, bx2, by2, bw2, bh2);
                    if (paintTx == null) {
                        vb.addMappedPgram((x0 * mxx) + (y0 * mxy) + mxt, (x0 * myx) + (y0 * myy) + myt, (x1 * mxx) + (y0 * mxy) + mxt, (x1 * myx) + (y0 * myy) + myt, (x0 * mxx) + (y1 * mxy) + mxt, (x0 * myx) + (y1 * myy) + myt, (x1 * mxx) + (y1 * mxy) + mxt, (x1 * myx) + (y1 * myy) + myt, u02, v02, u12, v02, u02, v12, u12, v12, 0.0f, 0.0f);
                        return true;
                    }
                    vb.addMappedPgram((x0 * mxx) + (y0 * mxy) + mxt, (x0 * myx) + (y0 * myy) + myt, (x1 * mxx) + (y0 * mxy) + mxt, (x1 * myx) + (y0 * myy) + myt, (x0 * mxx) + (y1 * mxy) + mxt, (x0 * myx) + (y1 * myy) + myt, (x1 * mxx) + (y1 * mxy) + mxt, (x1 * myx) + (y1 * myy) + myt, u02, v02, u12, v02, u02, v12, u12, v12, x0, y0, x1, y1, paintTx);
                    return true;
                }
                System.out.println("Not a 2d transform!");
                myt = 0.0f;
                mxt = 0.0f;
            }
            Shader shader2 = this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, rectTex, bx2, by2, bw2, bh2);
            AffineBase paintTx2 = getPaintTextureTx(IDENT, shader2, bx2, by2, bw2, bh2);
            if (paintTx2 == null) {
                vb.addQuad(x0, y0, x1, y1, u02, v02, u12, v12);
                return true;
            }
            paintTx2.translate(-mxt, -myt);
            vb.addQuad(x0, y0, x1, y1, u02, v02, u12, v12, paintTx2);
            return true;
        }
        if (wrapTex == null) {
            return false;
        }
        float u03 = 0.5f / wrapTex.getPhysicalWidth();
        float v03 = 0.5f / wrapTex.getPhysicalHeight();
        float uc = ((cellw * 0.5f) + 1.0f) / wrapTex.getPhysicalWidth();
        float vc = ((cellh * 0.5f) + 1.0f) / wrapTex.getPhysicalHeight();
        float xc = x2 + (w2 * 0.5f);
        float yc = y2 + (h2 * 0.5f);
        if (xform.isTranslateOrIdentity()) {
            x0 += mxt;
            y0 += myt;
            xc += mxt;
            yc += myt;
            x1 += mxt;
            y1 += myt;
            xform = IDENT;
        } else {
            if (xform.is2D()) {
                Shader shader3 = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, wrapTex, bx2, by2, bw2, bh2);
                AffineBase paintTx3 = getPaintTextureTx(IDENT, shader3, bx2, by2, bw2, bh2);
                float mxx_x0 = mxx * x0;
                float myx_x0 = myx * x0;
                float mxy_y0 = mxy * y0;
                float myy_y0 = myy * y0;
                float mxx_xc = mxx * xc;
                float myx_xc = myx * xc;
                float mxy_yc = mxy * yc;
                float myy_yc = myy * yc;
                float mxx_x1 = mxx * x1;
                float myx_x1 = myx * x1;
                float mxy_y1 = mxy * y1;
                float myy_y1 = myy * y1;
                float xcc = mxx_xc + mxy_yc + mxt;
                float ycc = myx_xc + myy_yc + myt;
                float xc0 = mxx_xc + mxy_y0 + mxt;
                float yc0 = myx_xc + myy_y0 + myt;
                float x0c = mxx_x0 + mxy_yc + mxt;
                float y0c = myx_x0 + myy_yc + myt;
                float xc1 = mxx_xc + mxy_y1 + mxt;
                float yc1 = myx_xc + myy_y1 + myt;
                float x1c = mxx_x1 + mxy_yc + mxt;
                float y1c = myx_x1 + myy_yc + myt;
                if (paintTx3 == null) {
                    vb.addMappedPgram((x0 * mxx) + (y0 * mxy) + mxt, (x0 * myx) + (y0 * myy) + myt, xc0, yc0, x0c, y0c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, 0.0f, 0.0f);
                    vb.addMappedPgram((x1 * mxx) + (y0 * mxy) + mxt, (x1 * myx) + (y0 * myy) + myt, xc0, yc0, x1c, y1c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, 0.0f, 0.0f);
                    vb.addMappedPgram((x0 * mxx) + (y1 * mxy) + mxt, (x0 * myx) + (y1 * myy) + myt, xc1, yc1, x0c, y0c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, 0.0f, 0.0f);
                    vb.addMappedPgram((x1 * mxx) + (y1 * mxy) + mxt, (x1 * myx) + (y1 * myy) + myt, xc1, yc1, x1c, y1c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, 0.0f, 0.0f);
                    return true;
                }
                vb.addMappedPgram((x0 * mxx) + (y0 * mxy) + mxt, (x0 * myx) + (y0 * myy) + myt, xc0, yc0, x0c, y0c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, x0, y0, xc, yc, paintTx3);
                vb.addMappedPgram((x1 * mxx) + (y0 * mxy) + mxt, (x1 * myx) + (y0 * myy) + myt, xc0, yc0, x1c, y1c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, x1, y0, xc, yc, paintTx3);
                vb.addMappedPgram((x0 * mxx) + (y1 * mxy) + mxt, (x0 * myx) + (y1 * myy) + myt, xc1, yc1, x0c, y0c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, x0, y1, xc, yc, paintTx3);
                vb.addMappedPgram((x1 * mxx) + (y1 * mxy) + mxt, (x1 * myx) + (y1 * myy) + myt, xc1, yc1, x1c, y1c, xcc, ycc, u03, v03, uc, v03, u03, vc, uc, vc, x1, y1, xc, yc, paintTx3);
                return true;
            }
            System.out.println("Not a 2d transform!");
            myt = 0.0f;
            mxt = 0.0f;
        }
        Shader shader4 = this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, wrapTex, bx2, by2, bw2, bh2);
        AffineBase paintTx4 = getPaintTextureTx(IDENT, shader4, bx2, by2, bw2, bh2);
        if (paintTx4 != null) {
            paintTx4.translate(-mxt, -myt);
        }
        vb.addQuad(x0, y0, xc, yc, u03, v03, uc, vc, paintTx4);
        vb.addQuad(x1, y0, xc, yc, u03, v03, uc, vc, paintTx4);
        vb.addQuad(x0, y1, xc, yc, u03, v03, uc, vc, paintTx4);
        vb.addQuad(x1, y1, xc, yc, u03, v03, uc, vc, paintTx4);
        return true;
    }

    boolean drawPrimRect(float x2, float y2, float w2, float h2) {
        float lw = this.stroke.getLineWidth();
        float pad = getStrokeExpansionFactor(this.stroke) * lw;
        BaseTransform xform = getTransformNoClone();
        float mxx = (float) xform.getMxx();
        float mxy = (float) xform.getMxy();
        float mxt = (float) xform.getMxt();
        float myx = (float) xform.getMyx();
        float myy = (float) xform.getMyy();
        float myt = (float) xform.getMyt();
        float dxdist = len(mxx, myx);
        float dydist = len(mxy, myy);
        if (dxdist == 0.0f || dydist == 0.0f) {
            return true;
        }
        float pixelw = 1.0f / dxdist;
        float pixelh = 1.0f / dydist;
        float x0 = (x2 - pad) - (pixelw * 0.5f);
        float y0 = (y2 - pad) - (pixelh * 0.5f);
        float xc = x2 + (w2 * 0.5f);
        float yc = y2 + (h2 * 0.5f);
        float x1 = x2 + w2 + pad + (pixelw * 0.5f);
        float y1 = y2 + h2 + pad + (pixelh * 0.5f);
        Texture rTex = this.context.getWrapRectTexture();
        float wscale = 1.0f / rTex.getPhysicalWidth();
        float hscale = 1.0f / rTex.getPhysicalHeight();
        float ou0 = 0.5f * wscale;
        float ov0 = 0.5f * hscale;
        float ouc = ((((w2 * 0.5f) + pad) * dxdist) + 1.0f) * wscale;
        float ovc = ((((h2 * 0.5f) + pad) * dydist) + 1.0f) * hscale;
        float offsetx = lw * dxdist * wscale;
        float offsety = lw * dydist * hscale;
        VertexBuffer vb = this.context.getVertexBuffer();
        if (xform.isTranslateOrIdentity()) {
            x0 += mxt;
            y0 += myt;
            xc += mxt;
            yc += myt;
            x1 += mxt;
            y1 += myt;
            xform = IDENT;
        } else {
            if (xform.is2D()) {
                Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, rTex, x2, y2, w2, h2, offsetx, offsety, 0.0f, 0.0f, 0.0f, 0.0f);
                shader.setConstant("innerOffset", offsetx, offsety);
                AffineBase paintTx = getPaintTextureTx(IDENT, shader, x2, y2, w2, h2);
                float mxx_x0 = mxx * x0;
                float myx_x0 = myx * x0;
                float mxy_y0 = mxy * y0;
                float myy_y0 = myy * y0;
                float mxx_xc = mxx * xc;
                float myx_xc = myx * xc;
                float mxy_yc = mxy * yc;
                float myy_yc = myy * yc;
                float mxx_x1 = mxx * x1;
                float myx_x1 = myx * x1;
                float mxy_y1 = mxy * y1;
                float myy_y1 = myy * y1;
                float xcc = mxx_xc + mxy_yc + mxt;
                float ycc = myx_xc + myy_yc + myt;
                float xc0 = mxx_xc + mxy_y0 + mxt;
                float yc0 = myx_xc + myy_y0 + myt;
                float x0c = mxx_x0 + mxy_yc + mxt;
                float y0c = myx_x0 + myy_yc + myt;
                float xc1 = mxx_xc + mxy_y1 + mxt;
                float yc1 = myx_xc + myy_y1 + myt;
                float x1c = mxx_x1 + mxy_yc + mxt;
                float y1c = myx_x1 + myy_yc + myt;
                if (paintTx == null) {
                    vb.addMappedPgram(mxx_x0 + mxy_y0 + mxt, myx_x0 + myy_y0 + myt, xc0, yc0, x0c, y0c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, 0.0f, 0.0f);
                    vb.addMappedPgram(mxx_x1 + mxy_y0 + mxt, myx_x1 + myy_y0 + myt, xc0, yc0, x1c, y1c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, 0.0f, 0.0f);
                    vb.addMappedPgram(mxx_x0 + mxy_y1 + mxt, myx_x0 + myy_y1 + myt, xc1, yc1, x0c, y0c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, 0.0f, 0.0f);
                    vb.addMappedPgram(mxx_x1 + mxy_y1 + mxt, myx_x1 + myy_y1 + myt, xc1, yc1, x1c, y1c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, 0.0f, 0.0f);
                } else {
                    vb.addMappedPgram(mxx_x0 + mxy_y0 + mxt, myx_x0 + myy_y0 + myt, xc0, yc0, x0c, y0c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, x0, y0, xc, yc, paintTx);
                    vb.addMappedPgram(mxx_x1 + mxy_y0 + mxt, myx_x1 + myy_y0 + myt, xc0, yc0, x1c, y1c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, x1, y0, xc, yc, paintTx);
                    vb.addMappedPgram(mxx_x0 + mxy_y1 + mxt, myx_x0 + myy_y1 + myt, xc1, yc1, x0c, y0c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, x0, y1, xc, yc, paintTx);
                    vb.addMappedPgram(mxx_x1 + mxy_y1 + mxt, myx_x1 + myy_y1 + myt, xc1, yc1, x1c, y1c, xcc, ycc, ou0, ov0, ouc, ov0, ou0, ovc, ouc, ovc, x1, y1, xc, yc, paintTx);
                }
                rTex.unlock();
                return true;
            }
            System.out.println("Not a 2d transform!");
            myt = 0.0f;
            mxt = 0.0f;
        }
        Shader shader2 = this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, rTex, x2, y2, w2, h2, offsetx, offsety, 0.0f, 0.0f, 0.0f, 0.0f);
        shader2.setConstant("innerOffset", offsetx, offsety);
        AffineBase paintTx2 = getPaintTextureTx(IDENT, shader2, x2, y2, w2, h2);
        if (paintTx2 != null) {
            paintTx2.translate(-mxt, -myt);
        }
        vb.addQuad(x0, y0, xc, yc, ou0, ov0, ouc, ovc, paintTx2);
        vb.addQuad(x1, y0, xc, yc, ou0, ov0, ouc, ovc, paintTx2);
        vb.addQuad(x0, y1, xc, yc, ou0, ov0, ouc, ovc, paintTx2);
        vb.addQuad(x1, y1, xc, yc, ou0, ov0, ouc, ovc, paintTx2);
        rTex.unlock();
        return true;
    }

    boolean drawPrimDiagonal(float x1, float y1, float x2, float y2, float lw, int cap, float bx2, float by2, float bw2, float bh2) {
        float hdx;
        float hdy;
        float vdx;
        float vdy;
        int cellw;
        int cellh;
        BaseTransform xform;
        if (this.stroke.getType() == 0) {
            lw *= 0.5f;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = len(dx, dy);
        float dx2 = dx / len;
        float dy2 = dy / len;
        float ldx = dx2 * lw;
        float ldy = dy2 * lw;
        float xUL = x1 + ldy;
        float yUL = y1 - ldx;
        float xUR = x2 + ldy;
        float yUR = y2 - ldx;
        float xLL = x1 - ldy;
        float yLL = y1 + ldx;
        float xLR = x2 - ldy;
        float yLR = y2 + ldx;
        if (cap == 2) {
            xUL -= ldx;
            yUL -= ldy;
            xLL -= ldx;
            yLL -= ldy;
            xUR += ldx;
            yUR += ldy;
            xLR += ldx;
            yLR += ldy;
        }
        BaseTransform xform2 = getTransformNoClone();
        float mxt = (float) xform2.getMxt();
        float myt = (float) xform2.getMyt();
        if (xform2.isTranslateOrIdentity()) {
            hdx = dx2;
            hdy = dy2;
            vdx = dy2;
            vdy = -dx2;
            cellw = (int) Math.ceil(len(xUR - xUL, yUR - yUL));
            cellh = (int) Math.ceil(len(xLL - xUL, yLL - yUL));
            xform = IDENT;
        } else if (xform2.is2D()) {
            float mxx = (float) xform2.getMxx();
            float mxy = (float) xform2.getMxy();
            float myx = (float) xform2.getMyx();
            float myy = (float) xform2.getMyy();
            float tx = (mxx * xUL) + (mxy * yUL);
            float ty = (myx * xUL) + (myy * yUL);
            xUL = tx;
            yUL = ty;
            float tx2 = (mxx * xUR) + (mxy * yUR);
            float ty2 = (myx * xUR) + (myy * yUR);
            xUR = tx2;
            yUR = ty2;
            float tx3 = (mxx * xLL) + (mxy * yLL);
            float ty3 = (myx * xLL) + (myy * yLL);
            xLL = tx3;
            yLL = ty3;
            float tx4 = (mxx * xLR) + (mxy * yLR);
            float ty4 = (myx * xLR) + (myy * yLR);
            xLR = tx4;
            yLR = ty4;
            float hdx2 = (mxx * dx2) + (mxy * dy2);
            float hdy2 = (myx * dx2) + (myy * dy2);
            float dlen = len(hdx2, hdy2);
            if (dlen == 0.0f) {
                return true;
            }
            hdx = hdx2 / dlen;
            hdy = hdy2 / dlen;
            float vdx2 = (mxx * dy2) - (mxy * dx2);
            float vdy2 = (myx * dy2) - (myy * dx2);
            float dlen2 = len(vdx2, vdy2);
            if (dlen2 == 0.0f) {
                return true;
            }
            vdx = vdx2 / dlen2;
            vdy = vdy2 / dlen2;
            cellw = (int) Math.ceil(Math.abs(((xUR - xUL) * hdx) + ((yUR - yUL) * hdy)));
            cellh = (int) Math.ceil(Math.abs(((xLL - xUL) * vdx) + ((yLL - yUL) * vdy)));
            xform = IDENT;
        } else {
            System.out.println("Not a 2d transform!");
            return false;
        }
        float hdx3 = hdx * 0.5f;
        float hdy3 = hdy * 0.5f;
        float vdx3 = vdx * 0.5f;
        float vdy3 = vdy * 0.5f;
        float xUL2 = ((xUL + mxt) + vdx3) - hdx3;
        float yUL2 = ((yUL + myt) + vdy3) - hdy3;
        float xUR2 = xUR + mxt + vdx3 + hdx3;
        float yUR2 = yUR + myt + vdy3 + hdy3;
        float xLL2 = ((xLL + mxt) - vdx3) - hdx3;
        float yLL2 = ((yLL + myt) - vdy3) - hdy3;
        float xLR2 = ((xLR + mxt) - vdx3) + hdx3;
        float yLR2 = ((yLR + myt) - vdy3) + hdy3;
        VertexBuffer vb = this.context.getVertexBuffer();
        int cellmax = this.context.getRectTextureMaxSize();
        if (cellh <= cellmax) {
            float v0 = ((cellh * (cellh + 1)) / 2) - 0.5f;
            float v1 = v0 + cellh + 1.0f;
            Texture rTex = this.context.getRectTexture();
            float v02 = v0 / rTex.getPhysicalHeight();
            float v12 = v1 / rTex.getPhysicalHeight();
            if (cellw <= cellmax) {
                float u0 = ((cellw * (cellw + 1)) / 2) - 0.5f;
                float u1 = u0 + cellw + 1.0f;
                float u02 = u0 / rTex.getPhysicalWidth();
                float u12 = u1 / rTex.getPhysicalWidth();
                this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, rTex, bx2, by2, bw2, bh2);
                vb.addMappedPgram(xUL2, yUL2, xUR2, yUR2, xLL2, yLL2, xLR2, yLR2, u02, v02, u12, v02, u02, v12, u12, v12, 0.0f, 0.0f);
                rTex.unlock();
                return true;
            }
            if (cellw <= (cellmax * 2) - 1) {
                float xUC = (xUL2 + xUR2) * 0.5f;
                float yUC = (yUL2 + yUR2) * 0.5f;
                float xLC = (xLL2 + xLR2) * 0.5f;
                float yLC = (yLL2 + yLR2) * 0.5f;
                float u03 = ((cellmax * (cellmax + 1)) / 2) - 0.5f;
                float u13 = u03 + 0.5f + (cellw * 0.5f);
                float u04 = u03 / rTex.getPhysicalWidth();
                float u14 = u13 / rTex.getPhysicalWidth();
                this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, rTex, bx2, by2, bw2, bh2);
                vb.addMappedPgram(xUL2, yUL2, xUC, yUC, xLL2, yLL2, xLC, yLC, u04, v02, u14, v02, u04, v12, u14, v12, 0.0f, 0.0f);
                vb.addMappedPgram(xUR2, yUR2, xUC, yUC, xLR2, yLR2, xLC, yLC, u04, v02, u14, v02, u04, v12, u14, v12, 0.0f, 0.0f);
                rTex.unlock();
                return true;
            }
            float u05 = 0.5f / rTex.getPhysicalWidth();
            float u15 = 1.5f / rTex.getPhysicalWidth();
            float hdx4 = hdx3 * 2.0f;
            float hdy4 = hdy3 * 2.0f;
            float xUl = xUL2 + hdx4;
            float yUl = yUL2 + hdy4;
            float xUr = xUR2 - hdx4;
            float yUr = yUR2 - hdy4;
            float xLl = xLL2 + hdx4;
            float yLl = yLL2 + hdy4;
            float xLr = xLR2 - hdx4;
            float yLr = yLR2 - hdy4;
            this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, rTex, bx2, by2, bw2, bh2);
            vb.addMappedPgram(xUL2, yUL2, xUl, yUl, xLL2, yLL2, xLl, yLl, u05, v02, u15, v02, u05, v12, u15, v12, 0.0f, 0.0f);
            vb.addMappedPgram(xUl, yUl, xUr, yUr, xLl, yLl, xLr, yLr, u15, v02, u15, v02, u15, v12, u15, v12, 0.0f, 0.0f);
            vb.addMappedPgram(xUr, yUr, xUR2, yUR2, xLr, yLr, xLR2, yLR2, u15, v02, u05, v02, u15, v12, u05, v12, 0.0f, 0.0f);
            rTex.unlock();
            return true;
        }
        float xUC2 = (xUL2 + xUR2) * 0.5f;
        float yUC2 = (yUL2 + yUR2) * 0.5f;
        float xLC2 = (xLL2 + xLR2) * 0.5f;
        float yLC2 = (yLL2 + yLR2) * 0.5f;
        float xCL = (xUL2 + xLL2) * 0.5f;
        float yCL = (yUL2 + yLL2) * 0.5f;
        float xCR = (xUR2 + xLR2) * 0.5f;
        float yCR = (yUR2 + yLR2) * 0.5f;
        float xCC = (xUC2 + xLC2) * 0.5f;
        float yCC = (yUC2 + yLC2) * 0.5f;
        Texture rTex2 = this.context.getWrapRectTexture();
        float u06 = 0.5f / rTex2.getPhysicalWidth();
        float v03 = 0.5f / rTex2.getPhysicalHeight();
        float uc = ((cellw * 0.5f) + 1.0f) / rTex2.getPhysicalWidth();
        float vc = ((cellh * 0.5f) + 1.0f) / rTex2.getPhysicalHeight();
        this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_TEXTURE, rTex2, bx2, by2, bw2, bh2);
        vb.addMappedPgram(xUL2, yUL2, xUC2, yUC2, xCL, yCL, xCC, yCC, u06, v03, uc, v03, u06, vc, uc, vc, 0.0f, 0.0f);
        vb.addMappedPgram(xUR2, yUR2, xUC2, yUC2, xCR, yCR, xCC, yCC, u06, v03, uc, v03, u06, vc, uc, vc, 0.0f, 0.0f);
        vb.addMappedPgram(xLL2, yLL2, xLC2, yLC2, xCL, yCL, xCC, yCC, u06, v03, uc, v03, u06, vc, uc, vc, 0.0f, 0.0f);
        vb.addMappedPgram(xLR2, yLR2, xLC2, yLC2, xCR, yCR, xCC, yCC, u06, v03, uc, v03, u06, vc, uc, vc, 0.0f, 0.0f);
        rTex2.unlock();
        return true;
    }

    @Override // com.sun.prism.Graphics
    public void fillRect(float x2, float y2, float w2, float h2) {
        if (w2 <= 0.0f || h2 <= 0.0f) {
            return;
        }
        if (!isAntialiasedShape()) {
            fillQuad(x2, y2, x2 + w2, y2 + h2);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(x2, y2, w2, h2, 0.0f, 0.0f);
            renderWithComplexPaint(scratchRRect, null, x2, y2, w2, h2);
            return;
        }
        if (PrismSettings.primTextureSize != 0) {
            Texture rTex = this.context.getRectTexture();
            Texture wTex = this.context.getWrapRectTexture();
            boolean success = fillPrimRect(x2, y2, w2, h2, rTex, wTex, x2, y2, w2, h2);
            rTex.unlock();
            wTex.unlock();
            if (success) {
                return;
            }
        }
        renderGeneralRoundedRect(x2, y2, w2, h2, 0.0f, 0.0f, BaseShaderContext.MaskType.FILL_PGRAM, null);
    }

    @Override // com.sun.prism.Graphics
    public void fillEllipse(float x2, float y2, float w2, float h2) {
        if (w2 <= 0.0f || h2 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchEllipse.setFrame(x2, y2, w2, h2);
            renderWithComplexPaint(scratchEllipse, null, x2, y2, w2, h2);
        } else if (!isAntialiasedShape()) {
            scratchEllipse.setFrame(x2, y2, w2, h2);
            renderShape(scratchEllipse, null, x2, y2, w2, h2);
        } else {
            if (PrismSettings.primTextureSize != 0 && fillPrimRect(x2, y2, w2, h2, this.context.getOvalTexture(), null, x2, y2, w2, h2)) {
                return;
            }
            renderGeneralRoundedRect(x2, y2, w2, h2, w2, h2, BaseShaderContext.MaskType.FILL_ELLIPSE, null);
        }
    }

    @Override // com.sun.prism.Graphics
    public void fillRoundRect(float x2, float y2, float w2, float h2, float arcw, float arch) {
        float arcw2 = Math.min(Math.abs(arcw), w2);
        float arch2 = Math.min(Math.abs(arch), h2);
        if (w2 <= 0.0f || h2 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(x2, y2, w2, h2, arcw2, arch2);
            renderWithComplexPaint(scratchRRect, null, x2, y2, w2, h2);
        } else if (!isAntialiasedShape()) {
            scratchRRect.setRoundRect(x2, y2, w2, h2, arcw2, arch2);
            renderShape(scratchRRect, null, x2, y2, w2, h2);
        } else {
            renderGeneralRoundedRect(x2, y2, w2, h2, arcw2, arch2, BaseShaderContext.MaskType.FILL_ROUNDRECT, null);
        }
    }

    @Override // com.sun.prism.Graphics
    public void fillQuad(float x1, float y1, float x2, float y2) {
        float bx2;
        float bw2;
        float by2;
        float bh2;
        float myt;
        float mxt;
        if (x1 <= x2) {
            bx2 = x1;
            bw2 = x2 - x1;
        } else {
            bx2 = x2;
            bw2 = x1 - x2;
        }
        if (y1 <= y2) {
            by2 = y1;
            bh2 = y2 - y1;
        } else {
            by2 = y2;
            bh2 = y1 - y2;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(bx2, by2, bw2, bh2, 0.0f, 0.0f);
            renderWithComplexPaint(scratchRRect, null, bx2, by2, bw2, bh2);
            return;
        }
        BaseTransform xform = getTransformNoClone();
        if (PrismSettings.primTextureSize != 0) {
            if (xform.isTranslateOrIdentity()) {
                mxt = (float) xform.getMxt();
                myt = (float) xform.getMyt();
                xform = IDENT;
                x1 += mxt;
                y1 += myt;
                x2 += mxt;
                y2 += myt;
            } else {
                myt = 0.0f;
                mxt = 0.0f;
            }
            Shader shader = this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.ALPHA_ONE, null, bx2, by2, bw2, bh2);
            AffineBase paintTx = getPaintTextureTx(IDENT, shader, bx2, by2, bw2, bh2);
            if (paintTx != null) {
                paintTx.translate(-mxt, -myt);
            }
            this.context.getVertexBuffer().addQuad(x1, y1, x2, y2, 0.0f, 0.0f, 0.0f, 0.0f, paintTx);
            return;
        }
        if (this.isSimpleTranslate) {
            xform = IDENT;
            bx2 += this.transX;
            by2 += this.transY;
        }
        this.context.validatePaintOp(this, xform, BaseShaderContext.MaskType.SOLID, bx2, by2, bw2, bh2);
        VertexBuffer vb = this.context.getVertexBuffer();
        vb.addQuad(bx2, by2, bx2 + bw2, by2 + bh2);
    }

    private static boolean canUseStrokeShader(BasicStroke bs2) {
        return !bs2.isDashed() && (bs2.getType() == 1 || bs2.getLineJoin() == 1 || (bs2.getLineJoin() == 0 && ((double) bs2.getMiterLimit()) >= SQRT_2));
    }

    @Override // com.sun.prism.Graphics
    public void blit(RTTexture srcTex, RTTexture dstTex, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1) {
        if (dstTex == null) {
            this.context.setRenderTarget(this);
        } else {
            this.context.setRenderTarget((BaseGraphics) dstTex.createGraphics());
        }
        this.context.blit(srcTex, dstTex, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1);
    }

    @Override // com.sun.prism.Graphics
    public void drawRect(float x2, float y2, float w2, float h2) {
        if (w2 < 0.0f || h2 < 0.0f) {
            return;
        }
        if (w2 == 0.0f || h2 == 0.0f) {
            drawLine(x2, y2, x2 + w2, y2 + h2);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(x2, y2, w2, h2, 0.0f, 0.0f);
            renderWithComplexPaint(scratchRRect, this.stroke, x2, y2, w2, h2);
            return;
        }
        if (!isAntialiasedShape()) {
            scratchRRect.setRoundRect(x2, y2, w2, h2, 0.0f, 0.0f);
            renderShape(scratchRRect, this.stroke, x2, y2, w2, h2);
        } else {
            if (canUseStrokeShader(this.stroke)) {
                if (PrismSettings.primTextureSize != 0 && this.stroke.getLineJoin() != 1 && drawPrimRect(x2, y2, w2, h2)) {
                    return;
                }
                renderGeneralRoundedRect(x2, y2, w2, h2, 0.0f, 0.0f, BaseShaderContext.MaskType.DRAW_PGRAM, this.stroke);
                return;
            }
            scratchRRect.setRoundRect(x2, y2, w2, h2, 0.0f, 0.0f);
            renderShape(scratchRRect, this.stroke, x2, y2, w2, h2);
        }
    }

    private boolean checkInnerCurvature(float arcw, float arch) {
        float inset = this.stroke.getLineWidth() * (1.0f - getStrokeExpansionFactor(this.stroke));
        float arcw2 = arcw - inset;
        float arch2 = arch - inset;
        return arcw2 <= 0.0f || arch2 <= 0.0f || (arcw2 * 2.0f > arch2 && arch2 * 2.0f > arcw2);
    }

    @Override // com.sun.prism.Graphics
    public void drawEllipse(float x2, float y2, float w2, float h2) {
        if (w2 < 0.0f || h2 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && checkInnerCurvature(w2, h2) && isAntialiasedShape()) {
            renderGeneralRoundedRect(x2, y2, w2, h2, w2, h2, BaseShaderContext.MaskType.DRAW_ELLIPSE, this.stroke);
        } else {
            scratchEllipse.setFrame(x2, y2, w2, h2);
            renderShape(scratchEllipse, this.stroke, x2, y2, w2, h2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawRoundRect(float x2, float y2, float w2, float h2, float arcw, float arch) {
        float arcw2 = Math.min(Math.abs(arcw), w2);
        float arch2 = Math.min(Math.abs(arch), h2);
        if (w2 < 0.0f || h2 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && checkInnerCurvature(arcw2, arch2) && isAntialiasedShape()) {
            renderGeneralRoundedRect(x2, y2, w2, h2, arcw2, arch2, BaseShaderContext.MaskType.DRAW_ROUNDRECT, this.stroke);
        } else {
            scratchRRect.setRoundRect(x2, y2, w2, h2, arcw2, arch2);
            renderShape(scratchRRect, this.stroke, x2, y2, w2, h2);
        }
    }

    @Override // com.sun.prism.Graphics
    public void drawLine(float x1, float y1, float x2, float y2) {
        float bx2;
        float bw2;
        float by2;
        float bh2;
        float ldx;
        float ldy;
        BaseTransform rendertx;
        float x12;
        float y12;
        float pdx;
        float pdy;
        float arcfracth;
        float arcfractw;
        BaseShaderContext.MaskType type;
        float padx;
        float pady;
        if (x1 <= x2) {
            bx2 = x1;
            bw2 = x2 - x1;
        } else {
            bx2 = x2;
            bw2 = x1 - x2;
        }
        if (y1 <= y2) {
            by2 = y1;
            bh2 = y2 - y1;
        } else {
            by2 = y2;
            bh2 = y1 - y2;
        }
        if (this.stroke.getType() == 1) {
            return;
        }
        if (this.isComplexPaint) {
            scratchLine.setLine(x1, y1, x2, y2);
            renderWithComplexPaint(scratchLine, this.stroke, bx2, by2, bw2, bh2);
            return;
        }
        if (!isAntialiasedShape()) {
            scratchLine.setLine(x1, y1, x2, y2);
            renderShape(scratchLine, this.stroke, bx2, by2, bw2, bh2);
            return;
        }
        int cap = this.stroke.getEndCap();
        if (this.stroke.isDashed()) {
            scratchLine.setLine(x1, y1, x2, y2);
            renderShape(scratchLine, this.stroke, bx2, by2, bw2, bh2);
            return;
        }
        float lw = this.stroke.getLineWidth();
        if (PrismSettings.primTextureSize != 0 && cap != 1) {
            float pad = lw;
            if (this.stroke.getType() == 0) {
                pad *= 0.5f;
            }
            if (bw2 == 0.0f || bh2 == 0.0f) {
                if (cap == 2) {
                    float f2 = pad;
                    pady = f2;
                    padx = f2;
                } else if (bw2 != 0.0f) {
                    padx = 0.0f;
                    pady = pad;
                } else if (bh2 != 0.0f) {
                    padx = pad;
                    pady = 0.0f;
                } else {
                    return;
                }
                Texture rTex = this.context.getRectTexture();
                Texture wTex = this.context.getWrapRectTexture();
                boolean success = fillPrimRect(bx2 - padx, by2 - pady, bw2 + padx + padx, bh2 + pady + pady, rTex, wTex, bx2, by2, bw2, bh2);
                rTex.unlock();
                wTex.unlock();
                if (success) {
                    return;
                }
            } else if (drawPrimDiagonal(x1, y1, x2, y2, lw, cap, bx2, by2, bw2, bh2)) {
                return;
            }
        }
        if (this.stroke.getType() == 2) {
            lw *= 2.0f;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = len(dx, dy);
        if (len == 0.0f) {
            if (cap == 0) {
                return;
            }
            ldx = lw;
            ldy = 0.0f;
        } else {
            ldx = (lw * dx) / len;
            ldy = (lw * dy) / len;
        }
        BaseTransform xform = getTransformNoClone();
        if (this.isSimpleTranslate) {
            double tx = xform.getMxt();
            double ty = xform.getMyt();
            x12 = (float) (x1 + tx);
            y12 = (float) (y1 + ty);
            pdx = ldy;
            pdy = -ldx;
            rendertx = IDENT;
        } else {
            rendertx = extract3Dremainder(xform);
            double[] coords = {x1, y1, x2, y2};
            xform.transform(coords, 0, coords, 0, 2);
            x12 = (float) coords[0];
            y12 = (float) coords[1];
            dx = ((float) coords[2]) - x12;
            dy = ((float) coords[3]) - y12;
            coords[0] = ldx;
            coords[1] = ldy;
            coords[2] = ldy;
            coords[3] = -ldx;
            xform.deltaTransform(coords, 0, coords, 0, 2);
            ldx = (float) coords[0];
            ldy = (float) coords[1];
            pdx = (float) coords[2];
            pdy = (float) coords[3];
        }
        float px = x12 - (pdx / 2.0f);
        float py = y12 - (pdy / 2.0f);
        if (cap != 0) {
            px -= ldx / 2.0f;
            py -= ldy / 2.0f;
            dx += ldx;
            dy += ldy;
            if (cap == 1) {
                arcfractw = len(ldx, ldy) / len(dx, dy);
                arcfracth = 1.0f;
                type = BaseShaderContext.MaskType.FILL_ROUNDRECT;
            } else {
                arcfracth = 0.0f;
                arcfractw = 0.0f;
                type = BaseShaderContext.MaskType.FILL_PGRAM;
            }
        } else {
            arcfracth = 0.0f;
            arcfractw = 0.0f;
            type = BaseShaderContext.MaskType.FILL_PGRAM;
        }
        renderGeneralRoundedPgram(px, py, dx, dy, pdx, pdy, arcfractw, arcfracth, 0.0f, 0.0f, rendertx, type, bx2, by2, bw2, bh2);
    }

    private static float len(float x2, float y2) {
        if (x2 == 0.0f) {
            return Math.abs(y2);
        }
        if (y2 == 0.0f) {
            return Math.abs(x2);
        }
        return (float) Math.sqrt((x2 * x2) + (y2 * y2));
    }

    @Override // com.sun.prism.Graphics
    public void setNodeBounds(RectBounds bounds) {
        this.nodeBounds = bounds;
        this.lcdSampleInvalid = bounds != null;
    }

    private void initLCDSampleRT() {
        if (this.lcdSampleInvalid) {
            RectBounds textBounds = new RectBounds();
            getTransformNoClone().transform(this.nodeBounds, textBounds);
            Rectangle clipRect = getClipRectNoClone();
            if (clipRect != null && !clipRect.isEmpty()) {
                textBounds.intersectWith(clipRect);
            }
            float bx2 = textBounds.getMinX() - 1.0f;
            float by2 = textBounds.getMinY();
            float bw2 = textBounds.getWidth() + 2.0f;
            float bh2 = textBounds.getHeight() + 1.0f;
            this.context.validateLCDBuffer(getRenderTarget());
            BaseShaderGraphics bsg = (BaseShaderGraphics) this.context.getLCDBuffer().createGraphics();
            bsg.setCompositeMode(CompositeMode.SRC);
            this.context.validateLCDOp(bsg, IDENT, (Texture) getRenderTarget(), null, true, null);
            int srch = getRenderTarget().getPhysicalHeight();
            int srcw = getRenderTarget().getPhysicalWidth();
            float tx1 = bx2 / srcw;
            float ty1 = by2 / srch;
            float tx2 = (bx2 + bw2) / srcw;
            float ty2 = (by2 + bh2) / srch;
            bsg.drawLCDBuffer(bx2, by2, bw2, bh2, tx1, ty1, tx2, ty2);
            this.context.setRenderTarget(this);
        }
        this.lcdSampleInvalid = false;
    }

    @Override // com.sun.prism.Graphics
    public void drawString(GlyphList gl, FontStrike strike, float x2, float y2, Color selectColor, int selectStart, int selectEnd) {
        if (this.isComplexPaint || this.paint.getType().isImagePattern() || strike.drawAsShapes()) {
            Shape shape = strike.getOutline(gl, BaseTransform.getTranslateInstance(x2, y2));
            fill(shape);
            return;
        }
        BaseTransform xform = getTransformNoClone();
        Paint textPaint = getPaint();
        Color textColor = textPaint.getType() == Paint.Type.COLOR ? (Color) textPaint : null;
        CompositeMode blendMode = getCompositeMode();
        boolean lcdSupported = blendMode == CompositeMode.SRC_OVER && textColor != null && xform.is2D() && !getRenderTarget().isMSAA();
        if (strike.getAAMode() == 1 && !lcdSupported) {
            FontResource fr = strike.getFontResource();
            float size = strike.getSize();
            BaseTransform tx = strike.getTransform();
            strike = fr.getStrike(size, tx, 0);
        }
        float bx2 = 0.0f;
        float by2 = 0.0f;
        float bw2 = 0.0f;
        float bh2 = 0.0f;
        if (this.paint.getType().isGradient() && ((Gradient) this.paint).isProportional()) {
            RectBounds textBounds = this.nodeBounds;
            if (textBounds == null) {
                Metrics m2 = strike.getMetrics();
                float pad = (-m2.getAscent()) * 0.4f;
                textBounds = new RectBounds(-pad, m2.getAscent(), gl.getWidth() + (2.0f * pad), m2.getDescent() + m2.getLineGap());
                bx2 = x2;
                by2 = y2;
            }
            bx2 += textBounds.getMinX();
            by2 += textBounds.getMinY();
            bw2 = textBounds.getWidth();
            bh2 = textBounds.getHeight();
        }
        BaseBounds clip = null;
        Point2D p2d = new Point2D(x2, y2);
        if (this.isSimpleTranslate) {
            clip = getFinalClipNoClone();
            xform = IDENT;
            p2d.f11907x += this.transX;
            p2d.f11908y += this.transY;
        }
        GlyphCache glyphCache = this.context.getGlyphCache(strike);
        Texture cacheTex = glyphCache.getBackingStore();
        if (strike.getAAMode() == 1) {
            if (this.nodeBounds == null) {
                Metrics m3 = strike.getMetrics();
                setNodeBounds(new RectBounds(x2 - 2.0f, y2 + m3.getAscent(), x2 + 2.0f + gl.getWidth(), y2 + 1.0f + m3.getDescent() + m3.getLineGap()));
                initLCDSampleRT();
                setNodeBounds(null);
            } else {
                initLCDSampleRT();
            }
            float invgamma = PrismFontFactory.getLCDContrast();
            float gamma = 1.0f / invgamma;
            textColor = new Color((float) Math.pow(textColor.getRed(), invgamma), (float) Math.pow(textColor.getGreen(), invgamma), (float) Math.pow(textColor.getBlue(), invgamma), (float) Math.pow(textColor.getAlpha(), invgamma));
            if (selectColor != null) {
                selectColor = new Color((float) Math.pow(selectColor.getRed(), invgamma), (float) Math.pow(selectColor.getGreen(), invgamma), (float) Math.pow(selectColor.getBlue(), invgamma), (float) Math.pow(selectColor.getAlpha(), invgamma));
            }
            setCompositeMode(CompositeMode.SRC);
            Shader shader = this.context.validateLCDOp(this, IDENT, this.context.getLCDBuffer(), cacheTex, false, textColor);
            float unitXCoord = 1.0f / cacheTex.getPhysicalWidth();
            shader.setConstant("gamma", gamma, invgamma, unitXCoord);
            setCompositeMode(blendMode);
        } else {
            this.context.validatePaintOp(this, IDENT, cacheTex, bx2, by2, bw2, bh2);
        }
        if (this.isSimpleTranslate) {
            p2d.f11908y = Math.round(p2d.f11908y);
            p2d.f11907x = Math.round(p2d.f11907x);
        }
        glyphCache.render(this.context, gl, p2d.f11907x, p2d.f11908y, selectStart, selectEnd, selectColor, textColor, xform, clip);
    }

    private void drawLCDBuffer(float bx2, float by2, float bw2, float bh2, float tx1, float ty1, float tx2, float ty2) {
        this.context.setRenderTarget(this);
        this.context.getVertexBuffer().addQuad(bx2, by2, bx2 + bw2, by2 + bh2, tx1, ty1, tx2, ty2);
    }

    @Override // com.sun.prism.ReadbackGraphics
    public boolean canReadBack() {
        RenderTarget rt = getRenderTarget();
        return (rt instanceof ReadbackRenderTarget) && ((ReadbackRenderTarget) rt).getBackBuffer() != null;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public RTTexture readBack(Rectangle view) {
        RenderTarget rt = getRenderTarget();
        this.context.flushVertexBuffer();
        this.context.validateLCDBuffer(rt);
        RTTexture lcdrtt = this.context.getLCDBuffer();
        Texture bbtex = ((ReadbackRenderTarget) rt).getBackBuffer();
        float x1 = view.f11913x;
        float y1 = view.f11914y;
        float x2 = x1 + view.width;
        float y2 = y1 + view.height;
        BaseShaderGraphics bsg = (BaseShaderGraphics) lcdrtt.createGraphics();
        bsg.setCompositeMode(CompositeMode.SRC);
        this.context.validateTextureOp(bsg, IDENT, bbtex, bbtex.getPixelFormat());
        bsg.drawTexture(bbtex, 0.0f, 0.0f, view.width, view.height, x1, y1, x2, y2);
        this.context.flushVertexBuffer();
        this.context.setRenderTarget(this);
        return lcdrtt;
    }

    @Override // com.sun.prism.ReadbackGraphics
    public void releaseReadBackBuffer(RTTexture rtt) {
    }

    @Override // com.sun.prism.Graphics
    public void setup3DRendering() {
        this.context.setRenderTarget(this);
    }
}
