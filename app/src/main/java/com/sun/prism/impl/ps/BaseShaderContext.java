package com.sun.prism.impl.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderContext.class */
public abstract class BaseShaderContext extends BaseContext {
    private static final int CHECK_SHADER = 1;
    private static final int CHECK_TRANSFORM = 2;
    private static final int CHECK_CLIP = 4;
    private static final int CHECK_COMPOSITE = 8;
    private static final int CHECK_PAINT_OP_MASK = 15;
    private static final int CHECK_TEXTURE_OP_MASK = 15;
    private static final int CHECK_CLEAR_OP_MASK = 4;
    private static final int NUM_STOCK_SHADER_SLOTS = MaskType.values().length << 4;
    private final Shader[] stockShaders;
    private final Shader[] stockATShaders;
    private final Shader[] specialShaders;
    private final Shader[] specialATShaders;
    private Shader externalShader;
    private RTTexture lcdBuffer;
    private final ShaderFactory factory;
    private State state;

    protected abstract State updateRenderTarget(RenderTarget renderTarget, NGCamera nGCamera, boolean z2);

    protected abstract void updateTexture(int i2, Texture texture);

    protected abstract void updateShaderTransform(Shader shader, BaseTransform baseTransform);

    protected abstract void updateWorldTransform(BaseTransform baseTransform);

    protected abstract void updateClipRect(Rectangle rectangle);

    protected abstract void updateCompositeMode(CompositeMode compositeMode);

    public abstract void blit(RTTexture rTTexture, RTTexture rTTexture2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderContext$MaskType.class */
    public enum MaskType {
        SOLID("Solid"),
        TEXTURE("Texture"),
        ALPHA_ONE("AlphaOne", true),
        ALPHA_TEXTURE("AlphaTexture", true),
        ALPHA_TEXTURE_DIFF("AlphaTextureDifference", true),
        FILL_PGRAM("FillPgram"),
        DRAW_PGRAM("DrawPgram", FILL_PGRAM),
        FILL_CIRCLE("FillCircle"),
        DRAW_CIRCLE("DrawCircle", FILL_CIRCLE),
        FILL_ELLIPSE("FillEllipse"),
        DRAW_ELLIPSE("DrawEllipse", FILL_ELLIPSE),
        FILL_ROUNDRECT("FillRoundRect"),
        DRAW_ROUNDRECT("DrawRoundRect", FILL_ROUNDRECT),
        DRAW_SEMIROUNDRECT("DrawSemiRoundRect");

        private String name;
        private MaskType filltype;
        private boolean newPaintStyle;

        MaskType(String name) {
            this.name = name;
        }

        MaskType(String name, boolean newstyle) {
            this.name = name;
            this.newPaintStyle = newstyle;
        }

        MaskType(String name, MaskType filltype) {
            this.name = name;
            this.filltype = filltype;
        }

        public String getName() {
            return this.name;
        }

        public MaskType getFillType() {
            return this.filltype;
        }

        public boolean isNewPaintStyle() {
            return this.newPaintStyle;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderContext$SpecialShaderType.class */
    public enum SpecialShaderType {
        TEXTURE_RGB("Solid_TextureRGB"),
        TEXTURE_MASK_RGB("Mask_TextureRGB"),
        TEXTURE_YV12("Solid_TextureYV12"),
        TEXTURE_First_LCD("Solid_TextureFirstPassLCD"),
        TEXTURE_SECOND_LCD("Solid_TextureSecondPassLCD"),
        SUPER("Mask_TextureSuper");

        private String name;

        SpecialShaderType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    protected BaseShaderContext(Screen screen, ShaderFactory factory, int vbQuads) {
        super(screen, factory, vbQuads);
        this.stockShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
        this.stockATShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
        this.specialShaders = new Shader[SpecialShaderType.values().length];
        this.specialATShaders = new Shader[SpecialShaderType.values().length];
        this.factory = factory;
        init();
    }

    protected void init() {
        this.state = null;
        if (this.externalShader != null && !this.externalShader.isValid()) {
            this.externalShader.dispose();
            this.externalShader = null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/impl/ps/BaseShaderContext$State.class */
    public static class State {
        private Shader lastShader;
        private RenderTarget lastRenderTarget;
        private NGCamera lastCamera;
        private boolean lastDepthTest;
        private Rectangle lastClip;
        private CompositeMode lastComp;
        private boolean isXformValid;
        private BaseTransform lastTransform = new Affine3D();
        private Texture[] lastTextures = new Texture[4];
        private float lastConst1 = Float.NaN;
        private float lastConst2 = Float.NaN;
        private float lastConst3 = Float.NaN;
        private float lastConst4 = Float.NaN;
        private float lastConst5 = Float.NaN;
        private float lastConst6 = Float.NaN;
        private boolean lastState3D = false;
    }

    @Override // com.sun.prism.impl.BaseContext
    protected void setPerspectiveTransform(GeneralTransform3D transform) {
        if (checkDisposed()) {
            return;
        }
        this.state.isXformValid = false;
        super.setPerspectiveTransform(transform);
    }

    protected void resetLastClip(State state) {
        if (checkDisposed()) {
            return;
        }
        state.lastClip = null;
    }

    private static int getStockShaderIndex(MaskType maskType, Paint paint) {
        int paintType;
        int paintOption;
        if (paint == null) {
            paintType = 0;
            paintOption = 0;
        } else {
            paintType = paint.getType().ordinal();
            if (paint.getType().isGradient()) {
                paintOption = ((Gradient) paint).getSpreadMethod();
            } else {
                paintOption = 0;
            }
        }
        return (maskType.ordinal() << 4) | (paintType << 2) | (paintOption << 0);
    }

    private Shader getPaintShader(boolean alphaTest, MaskType maskType, Paint paint) {
        if (checkDisposed()) {
            return null;
        }
        int index = getStockShaderIndex(maskType, paint);
        Shader[] shaders = alphaTest ? this.stockATShaders : this.stockShaders;
        Shader shader = shaders[index];
        if (shader != null && !shader.isValid()) {
            shader.dispose();
            shader = null;
        }
        if (shader == null) {
            String shaderName = maskType.getName() + "_" + paint.getType().getName();
            if (paint.getType().isGradient() && !maskType.isNewPaintStyle()) {
                Gradient grad = (Gradient) paint;
                int spreadMethod = grad.getSpreadMethod();
                if (spreadMethod == 0) {
                    shaderName = shaderName + "_PAD";
                } else if (spreadMethod == 1) {
                    shaderName = shaderName + "_REFLECT";
                } else if (spreadMethod == 2) {
                    shaderName = shaderName + "_REPEAT";
                }
            }
            if (alphaTest) {
                shaderName = shaderName + "_AlphaTest";
            }
            Shader shaderCreateStockShader = this.factory.createStockShader(shaderName);
            shaders[index] = shaderCreateStockShader;
            shader = shaderCreateStockShader;
        }
        return shader;
    }

    private void updatePaintShader(BaseShaderGraphics g2, Shader shader, MaskType maskType, Paint paint, float bx2, float by2, float bw2, float bh2) {
        Paint.Type paintType;
        float rx;
        float ry;
        float rw;
        float rh;
        if (checkDisposed() || (paintType = paint.getType()) == Paint.Type.COLOR || maskType.isNewPaintStyle()) {
            return;
        }
        if (paint.isProportional()) {
            rx = bx2;
            ry = by2;
            rw = bw2;
            rh = bh2;
        } else {
            rx = 0.0f;
            ry = 0.0f;
            rw = 1.0f;
            rh = 1.0f;
        }
        switch (paintType) {
            case LINEAR_GRADIENT:
                PaintHelper.setLinearGradient(g2, shader, (LinearGradient) paint, rx, ry, rw, rh);
                break;
            case RADIAL_GRADIENT:
                PaintHelper.setRadialGradient(g2, shader, (RadialGradient) paint, rx, ry, rw, rh);
                break;
            case IMAGE_PATTERN:
                PaintHelper.setImagePattern(g2, shader, (ImagePattern) paint, rx, ry, rw, rh);
                break;
        }
    }

    private Shader getSpecialShader(BaseGraphics g2, SpecialShaderType sst) {
        if (checkDisposed()) {
            return null;
        }
        boolean alphaTest = g2.isAlphaTestShader();
        Shader[] shaders = alphaTest ? this.specialATShaders : this.specialShaders;
        Shader shader = shaders[sst.ordinal()];
        if (shader != null && !shader.isValid()) {
            shader.dispose();
            shader = null;
        }
        if (shader == null) {
            String shaderName = sst.getName();
            if (alphaTest) {
                shaderName = shaderName + "_AlphaTest";
            }
            int iOrdinal = sst.ordinal();
            Shader shaderCreateStockShader = this.factory.createStockShader(shaderName);
            shader = shaderCreateStockShader;
            shaders[iOrdinal] = shaderCreateStockShader;
        }
        return shader;
    }

    @Override // com.sun.prism.impl.BaseContext
    public boolean isSuperShaderEnabled() {
        if (checkDisposed()) {
            return false;
        }
        return this.state.lastShader == this.specialATShaders[SpecialShaderType.SUPER.ordinal()] || this.state.lastShader == this.specialShaders[SpecialShaderType.SUPER.ordinal()];
    }

    private void updatePerVertexColor(Paint paint, float extraAlpha) {
        if (checkDisposed()) {
            return;
        }
        if (paint != null && paint.getType() == Paint.Type.COLOR) {
            getVertexBuffer().setPerVertexColor((Color) paint, extraAlpha);
        } else {
            getVertexBuffer().setPerVertexColor(extraAlpha);
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    public void validateClearOp(BaseGraphics g2) {
        checkState((BaseShaderGraphics) g2, 4, null, null);
    }

    @Override // com.sun.prism.impl.BaseContext
    public void validatePaintOp(BaseGraphics g2, BaseTransform xform, Texture maskTex, float bx2, float by2, float bw2, float bh2) {
        validatePaintOp((BaseShaderGraphics) g2, xform, maskTex, bx2, by2, bw2, bh2);
    }

    Shader validatePaintOp(BaseShaderGraphics g2, BaseTransform xform, MaskType maskType, float bx2, float by2, float bw2, float bh2) {
        return validatePaintOp(g2, xform, maskType, null, bx2, by2, bw2, bh2);
    }

    Shader validatePaintOp(BaseShaderGraphics g2, BaseTransform xform, MaskType maskType, float bx2, float by2, float bw2, float bh2, float k1, float k2, float k3, float k4, float k5, float k6) {
        if (checkDisposed()) {
            return null;
        }
        if (this.state.lastConst1 != k1 || this.state.lastConst2 != k2 || this.state.lastConst3 != k3 || this.state.lastConst4 != k4 || this.state.lastConst5 != k5 || this.state.lastConst6 != k6) {
            flushVertexBuffer();
            this.state.lastConst1 = k1;
            this.state.lastConst2 = k2;
            this.state.lastConst3 = k3;
            this.state.lastConst4 = k4;
            this.state.lastConst5 = k5;
            this.state.lastConst6 = k6;
        }
        return validatePaintOp(g2, xform, maskType, null, bx2, by2, bw2, bh2);
    }

    Shader validatePaintOp(BaseShaderGraphics g2, BaseTransform xform, MaskType maskType, Texture maskTex, float bx2, float by2, float bw2, float bh2, float k1, float k2, float k3, float k4, float k5, float k6) {
        if (this.state.lastConst1 != k1 || this.state.lastConst2 != k2 || this.state.lastConst3 != k3 || this.state.lastConst4 != k4 || this.state.lastConst5 != k5 || this.state.lastConst6 != k6) {
            flushVertexBuffer();
            this.state.lastConst1 = k1;
            this.state.lastConst2 = k2;
            this.state.lastConst3 = k3;
            this.state.lastConst4 = k4;
            this.state.lastConst5 = k5;
            this.state.lastConst6 = k6;
        }
        return validatePaintOp(g2, xform, maskType, maskTex, bx2, by2, bw2, bh2);
    }

    Shader validatePaintOp(BaseShaderGraphics g2, BaseTransform xform, Texture maskTex, float bx2, float by2, float bw2, float bh2) {
        return validatePaintOp(g2, xform, MaskType.TEXTURE, maskTex, bx2, by2, bw2, bh2);
    }

    Shader validatePaintOp(BaseShaderGraphics g2, BaseTransform xform, MaskType maskType, Texture maskTex, float bx2, float by2, float bw2, float bh2) {
        Texture tex0;
        Texture tex1;
        Shader shader;
        if (maskType == null) {
            throw new InternalError("maskType must be non-null");
        }
        if (this.externalShader == null) {
            Paint paint = g2.getPaint();
            Texture paintTex = null;
            if (paint.getType().isGradient()) {
                flushVertexBuffer();
                if (maskType.isNewPaintStyle()) {
                    paintTex = PaintHelper.getWrapGradientTexture(g2);
                } else {
                    paintTex = PaintHelper.getGradientTexture(g2, (Gradient) paint);
                }
            } else if (paint.getType() == Paint.Type.IMAGE_PATTERN) {
                flushVertexBuffer();
                ImagePattern texPaint = (ImagePattern) paint;
                ResourceFactory rf = g2.getResourceFactory();
                paintTex = rf.getCachedTexture(texPaint.getImage(), Texture.WrapMode.REPEAT);
            }
            if (this.factory.isSuperShaderAllowed() && paintTex == null && maskTex == this.factory.getGlyphTexture()) {
                shader = getSpecialShader(g2, SpecialShaderType.SUPER);
                tex0 = this.factory.getRegionTexture();
                tex1 = maskTex;
            } else {
                if (maskTex != null) {
                    tex0 = maskTex;
                    tex1 = paintTex;
                } else {
                    tex0 = paintTex;
                    tex1 = null;
                }
                shader = getPaintShader(g2.isAlphaTestShader(), maskType, paint);
            }
            checkState(g2, 15, xform, shader);
            setTexture(0, tex0);
            setTexture(1, tex1);
            updatePaintShader(g2, shader, maskType, paint, bx2, by2, bw2, bh2);
            updatePerVertexColor(paint, g2.getExtraAlpha());
            if (paintTex != null) {
                paintTex.unlock();
            }
            return shader;
        }
        checkState(g2, 15, xform, this.externalShader);
        setTexture(0, maskTex);
        setTexture(1, null);
        updatePerVertexColor(null, g2.getExtraAlpha());
        return this.externalShader;
    }

    @Override // com.sun.prism.impl.BaseContext
    public void validateTextureOp(BaseGraphics g2, BaseTransform xform, Texture tex0, PixelFormat format) {
        validateTextureOp((BaseShaderGraphics) g2, xform, tex0, null, format);
    }

    public Shader validateLCDOp(BaseShaderGraphics g2, BaseTransform xform, Texture tex0, Texture tex1, boolean firstPass, Paint fillColor) {
        if (checkDisposed()) {
            return null;
        }
        Shader shader = firstPass ? getSpecialShader(g2, SpecialShaderType.TEXTURE_First_LCD) : getSpecialShader(g2, SpecialShaderType.TEXTURE_SECOND_LCD);
        checkState(g2, 15, xform, shader);
        setTexture(0, tex0);
        setTexture(1, tex1);
        updatePerVertexColor(fillColor, g2.getExtraAlpha());
        return shader;
    }

    Shader validateTextureOp(BaseShaderGraphics g2, BaseTransform xform, Texture[] textures, PixelFormat format) {
        Shader shader;
        if (checkDisposed() || format != PixelFormat.MULTI_YCbCr_420 || textures.length < 3) {
            return null;
        }
        if (this.externalShader == null) {
            shader = getSpecialShader(g2, SpecialShaderType.TEXTURE_YV12);
        } else {
            shader = this.externalShader;
        }
        if (null != shader) {
            checkState(g2, 15, xform, shader);
            int texCount = Math.max(0, Math.min(textures.length, 4));
            for (int index = 0; index < texCount; index++) {
                setTexture(index, textures[index]);
            }
            updatePerVertexColor(null, g2.getExtraAlpha());
        }
        return shader;
    }

    Shader validateTextureOp(BaseShaderGraphics g2, BaseTransform xform, Texture tex0, Texture tex1, PixelFormat format) {
        Shader shader;
        if (checkDisposed()) {
            return null;
        }
        if (this.externalShader == null) {
            switch (format) {
                case INT_ARGB_PRE:
                case BYTE_BGRA_PRE:
                case BYTE_RGB:
                case BYTE_GRAY:
                case BYTE_APPLE_422:
                    if (this.factory.isSuperShaderAllowed() && tex0 == this.factory.getRegionTexture() && tex1 == null) {
                        shader = getSpecialShader(g2, SpecialShaderType.SUPER);
                        tex1 = this.factory.getGlyphTexture();
                        break;
                    } else {
                        shader = getSpecialShader(g2, SpecialShaderType.TEXTURE_RGB);
                        break;
                    }
                case MULTI_YCbCr_420:
                case BYTE_ALPHA:
                default:
                    throw new InternalError("Pixel format not supported: " + ((Object) format));
            }
        } else {
            shader = this.externalShader;
        }
        checkState(g2, 15, xform, shader);
        setTexture(0, tex0);
        setTexture(1, tex1);
        updatePerVertexColor(null, g2.getExtraAlpha());
        return shader;
    }

    Shader validateMaskTextureOp(BaseShaderGraphics g2, BaseTransform xform, Texture tex0, Texture tex1, PixelFormat format) {
        Shader shader;
        if (checkDisposed()) {
            return null;
        }
        if (this.externalShader == null) {
            switch (format) {
                case INT_ARGB_PRE:
                case BYTE_BGRA_PRE:
                case BYTE_RGB:
                case BYTE_GRAY:
                case BYTE_APPLE_422:
                    shader = getSpecialShader(g2, SpecialShaderType.TEXTURE_MASK_RGB);
                    break;
                case MULTI_YCbCr_420:
                case BYTE_ALPHA:
                default:
                    throw new InternalError("Pixel format not supported: " + ((Object) format));
            }
        } else {
            shader = this.externalShader;
        }
        checkState(g2, 15, xform, shader);
        setTexture(0, tex0);
        setTexture(1, tex1);
        updatePerVertexColor(null, g2.getExtraAlpha());
        return shader;
    }

    void setExternalShader(BaseShaderGraphics g2, Shader shader) {
        if (checkDisposed()) {
            return;
        }
        flushVertexBuffer();
        if (shader != null) {
            shader.enable();
        }
        this.externalShader = shader;
    }

    private void checkState(BaseShaderGraphics g2, int checkFlags, BaseTransform xform, Shader shader) {
        CompositeMode mode;
        Rectangle clip;
        if (checkDisposed()) {
            return;
        }
        setRenderTarget(g2);
        if ((checkFlags & 1) != 0 && shader != this.state.lastShader) {
            flushVertexBuffer();
            shader.enable();
            this.state.lastShader = shader;
            this.state.isXformValid = false;
            checkFlags |= 2;
        }
        if ((checkFlags & 2) != 0 && (!this.state.isXformValid || !xform.equals(this.state.lastTransform))) {
            flushVertexBuffer();
            updateShaderTransform(shader, xform);
            this.state.lastTransform.setTransform(xform);
            this.state.isXformValid = true;
        }
        if ((checkFlags & 4) != 0 && (clip = g2.getClipRectNoClone()) != this.state.lastClip) {
            flushVertexBuffer();
            updateClipRect(clip);
            this.state.lastClip = clip;
        }
        if ((checkFlags & 8) != 0 && (mode = g2.getCompositeMode()) != this.state.lastComp) {
            flushVertexBuffer();
            updateCompositeMode(mode);
            this.state.lastComp = mode;
        }
    }

    private void setTexture(int texUnit, Texture tex) {
        if (checkDisposed()) {
            return;
        }
        if (tex != null) {
            tex.assertLocked();
        }
        if (tex != this.state.lastTextures[texUnit]) {
            flushVertexBuffer();
            updateTexture(texUnit, tex);
            this.state.lastTextures[texUnit] = tex;
        }
    }

    public void initLCDBuffer(int width, int height) {
        if (checkDisposed()) {
            return;
        }
        this.lcdBuffer = this.factory.createRTTexture(width, height, Texture.WrapMode.CLAMP_NOT_NEEDED);
        this.lcdBuffer.makePermanent();
    }

    public void disposeLCDBuffer() {
        if (this.lcdBuffer != null) {
            this.lcdBuffer.dispose();
            this.lcdBuffer = null;
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    public RTTexture getLCDBuffer() {
        return this.lcdBuffer;
    }

    public void validateLCDBuffer(RenderTarget renderTarget) {
        if (checkDisposed()) {
            return;
        }
        if (this.lcdBuffer == null || this.lcdBuffer.getPhysicalWidth() < renderTarget.getPhysicalWidth() || this.lcdBuffer.getPhysicalHeight() < renderTarget.getPhysicalHeight()) {
            disposeLCDBuffer();
            initLCDBuffer(renderTarget.getPhysicalWidth(), renderTarget.getPhysicalHeight());
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    protected void setRenderTarget(RenderTarget target, NGCamera camera, boolean depthTest, boolean state3D) {
        if (checkDisposed()) {
            return;
        }
        if (target instanceof Texture) {
            ((Texture) target).assertLocked();
        }
        if (this.state == null || state3D != this.state.lastState3D || target != this.state.lastRenderTarget || camera != this.state.lastCamera || depthTest != this.state.lastDepthTest) {
            flushVertexBuffer();
            this.state = updateRenderTarget(target, camera, depthTest);
            this.state.lastRenderTarget = target;
            this.state.lastCamera = camera;
            this.state.lastDepthTest = depthTest;
            this.state.isXformValid = false;
            if (state3D != this.state.lastState3D) {
                this.state.lastState3D = state3D;
                this.state.lastShader = null;
                this.state.lastConst1 = Float.NaN;
                this.state.lastConst2 = Float.NaN;
                this.state.lastConst3 = Float.NaN;
                this.state.lastConst4 = Float.NaN;
                this.state.lastConst5 = Float.NaN;
                this.state.lastConst6 = Float.NaN;
                this.state.lastComp = null;
                this.state.lastClip = null;
                for (int i2 = 0; i2 != this.state.lastTextures.length; i2++) {
                    this.state.lastTextures[i2] = null;
                }
                if (state3D) {
                    setDeviceParametersFor3D();
                } else {
                    setDeviceParametersFor2D();
                }
            }
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    protected void releaseRenderTarget() {
        if (this.state != null) {
            this.state.lastRenderTarget = null;
            for (int i2 = 0; i2 < this.state.lastTextures.length; i2++) {
                this.state.lastTextures[i2] = null;
            }
        }
    }

    private void disposeShaders(Shader[] shaders) {
        for (int i2 = 0; i2 < shaders.length; i2++) {
            if (shaders[i2] != null) {
                shaders[i2].dispose();
                shaders[i2] = null;
            }
        }
    }

    @Override // com.sun.prism.impl.BaseContext
    public void dispose() {
        disposeShaders(this.stockShaders);
        disposeShaders(this.stockATShaders);
        disposeShaders(this.specialShaders);
        disposeShaders(this.specialATShaders);
        if (this.externalShader != null) {
            this.externalShader.dispose();
            this.externalShader = null;
        }
        disposeLCDBuffer();
        releaseRenderTarget();
        this.state = null;
        super.dispose();
    }
}
