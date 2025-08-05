package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/GaussianRenderState.class */
public class GaussianRenderState extends LinearConvolveRenderState {
    public static final float MAX_RADIUS = (MAX_KERNEL_SIZE - 1) / 2;
    private boolean isShadow;
    private Color4f shadowColor;
    private float spread;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private float inputRadiusX;
    private float inputRadiusY;
    private float spreadPass;
    private int validatedPass;
    private LinearConvolveRenderState.PassType passType;
    private float passRadius;
    private FloatBuffer weights;
    private float[] samplevectors;
    private float weightsValidRadius;
    private float weightsValidSpread;

    static FloatBuffer getGaussianWeights(FloatBuffer weights, int pad, float radius, float spread) {
        int klen = (pad * 2) + 1;
        if (weights == null) {
            weights = BufferUtil.newFloatBuffer(128);
        }
        weights.clear();
        float sigma = radius / 3.0f;
        float sigma22 = 2.0f * sigma * sigma;
        if (sigma22 < Float.MIN_VALUE) {
            sigma22 = Float.MIN_VALUE;
        }
        float total = 0.0f;
        for (int row = -pad; row <= pad; row++) {
            float kval = (float) Math.exp((-(row * row)) / sigma22);
            weights.put(kval);
            total += kval;
        }
        float total2 = total + ((weights.get(0) - total) * spread);
        for (int i2 = 0; i2 < klen; i2++) {
            weights.put(i2, weights.get(i2) / total2);
        }
        int limit = getPeerSize(klen);
        while (weights.position() < limit) {
            weights.put(0.0f);
        }
        weights.limit(limit);
        weights.rewind();
        return weights;
    }

    public GaussianRenderState(float xradius, float yradius, float spread, boolean isShadow, Color4f shadowColor, BaseTransform filtertx) {
        this.isShadow = isShadow;
        this.shadowColor = shadowColor;
        this.spread = spread;
        filtertx = filtertx == null ? BaseTransform.IDENTITY_TRANSFORM : filtertx;
        double mxx = filtertx.getMxx();
        double mxy = filtertx.getMxy();
        double myx = filtertx.getMyx();
        double myy = filtertx.getMyy();
        double txScaleX = Math.hypot(mxx, myx);
        double txScaleY = Math.hypot(mxy, myy);
        boolean scaled = false;
        float scaledRadiusX = (float) (xradius * txScaleX);
        float scaledRadiusY = (float) (yradius * txScaleY);
        if (scaledRadiusX < 0.00390625f && scaledRadiusY < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = filtertx;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
            return;
        }
        if (scaledRadiusX > MAX_RADIUS) {
            scaledRadiusX = MAX_RADIUS;
            txScaleX = MAX_RADIUS / xradius;
            scaled = true;
        }
        if (scaledRadiusY > MAX_RADIUS) {
            scaledRadiusY = MAX_RADIUS;
            txScaleY = MAX_RADIUS / yradius;
            scaled = true;
        }
        this.inputRadiusX = scaledRadiusX;
        this.inputRadiusY = scaledRadiusY;
        this.spreadPass = (this.inputRadiusY > 1.0f || this.inputRadiusY >= this.inputRadiusX) ? 1.0f : 0.0f;
        if (scaled) {
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            this.inputtx = BaseTransform.getScaleInstance(txScaleX, txScaleY);
            this.resulttx = filtertx.copy().deriveWithScale(1.0d / txScaleX, 1.0d / txScaleY, 1.0d);
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
            return;
        }
        this.space = RenderState.EffectCoordinateSpace.RenderSpace;
        this.inputtx = filtertx;
        this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
        this.samplevectors = new float[]{(float) (mxx / txScaleX), (float) (myx / txScaleX), (float) (mxy / txScaleY), (float) (myy / txScaleY), 0.0f, 0.0f};
    }

    public GaussianRenderState(float radius, float dx, float dy, BaseTransform filtertx) {
        BaseTransform a2di;
        this.isShadow = false;
        this.spread = 0.0f;
        filtertx = filtertx == null ? BaseTransform.IDENTITY_TRANSFORM : filtertx;
        double mxx = filtertx.getMxx();
        double mxy = filtertx.getMxy();
        double myx = filtertx.getMyx();
        double myy = filtertx.getMyy();
        double tdx = (mxx * dx) + (mxy * dy);
        double tdy = (myx * dx) + (myy * dy);
        double txScale = Math.hypot(tdx, tdy);
        boolean scaled = false;
        float scaledRadius = (float) (radius * txScale);
        if (scaledRadius < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = filtertx;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            return;
        }
        if (scaledRadius > MAX_RADIUS) {
            scaledRadius = MAX_RADIUS;
            txScale = MAX_RADIUS / radius;
            scaled = true;
        }
        this.inputRadiusX = scaledRadius;
        this.inputRadiusY = 0.0f;
        this.spreadPass = 0.0f;
        if (scaled) {
            double odx = (mxy * dx) - (mxx * dy);
            double ody = (myy * dx) - (myx * dy);
            double txOScale = Math.hypot(odx, ody);
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            Affine2D a2d = new Affine2D();
            a2d.scale(txScale, txOScale);
            a2d.rotate(dx, -dy);
            try {
                a2di = a2d.createInverse();
            } catch (NoninvertibleTransformException e2) {
                a2di = BaseTransform.IDENTITY_TRANSFORM;
            }
            this.inputtx = a2d;
            this.resulttx = filtertx.copy().deriveWithConcatenation(a2di);
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            return;
        }
        this.space = RenderState.EffectCoordinateSpace.RenderSpace;
        this.inputtx = filtertx;
        this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
        this.samplevectors = new float[]{(float) (tdx / txScale), (float) (tdy / txScale), 0.0f, 0.0f, 0.0f, 0.0f};
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public boolean isShadow() {
        return this.isShadow;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public float[] getPassShadowColorComponents() {
        return this.validatedPass == 0 ? BLACK_COMPONENTS : this.shadowColor.getPremultipliedRGBComponents();
    }

    @Override // com.sun.scenario.effect.impl.state.RenderState
    public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
        return this.space;
    }

    @Override // com.sun.scenario.effect.impl.state.RenderState
    public BaseTransform getInputTransform(BaseTransform filterTransform) {
        return this.inputtx;
    }

    @Override // com.sun.scenario.effect.impl.state.RenderState
    public BaseTransform getResultTransform(BaseTransform filterTransform) {
        return this.resulttx;
    }

    @Override // com.sun.scenario.effect.impl.state.RenderState
    public Rectangle getInputClip(int i2, Rectangle filterClip) {
        if (filterClip != null) {
            double dx0 = this.samplevectors[0] * this.inputRadiusX;
            double dy0 = this.samplevectors[1] * this.inputRadiusX;
            double dx1 = this.samplevectors[2] * this.inputRadiusY;
            double dy1 = this.samplevectors[3] * this.inputRadiusY;
            int padx = (int) Math.ceil(dx0 + dx1);
            int pady = (int) Math.ceil(dy0 + dy1);
            if ((padx | pady) != 0) {
                filterClip = new Rectangle(filterClip);
                filterClip.grow(padx, pady);
            }
        }
        return filterClip;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public ImageData validatePassInput(ImageData src, int pass) {
        this.validatedPass = pass;
        Filterable f2 = src.getUntransformedImage();
        BaseTransform srcTx = src.getTransform();
        float iRadius = pass == 0 ? this.inputRadiusX : this.inputRadiusY;
        int vecindex = pass * 2;
        if (srcTx.isTranslateOrIdentity()) {
            this.passRadius = iRadius;
            this.samplevectors[4] = this.samplevectors[vecindex];
            this.samplevectors[5] = this.samplevectors[vecindex + 1];
            if (this.validatedPass == 0) {
                if (nearOne(this.samplevectors[4], f2.getPhysicalWidth()) && nearZero(this.samplevectors[5], f2.getPhysicalWidth())) {
                    this.passType = LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED;
                } else {
                    this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
                }
            } else if (nearZero(this.samplevectors[4], f2.getPhysicalHeight()) && nearOne(this.samplevectors[5], f2.getPhysicalHeight())) {
                this.passType = LinearConvolveRenderState.PassType.VERTICAL_CENTERED;
            } else {
                this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
            }
        } else {
            this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
            try {
                srcTx.inverseDeltaTransform(this.samplevectors, vecindex, this.samplevectors, 4, 1);
                double srcScale = Math.hypot(this.samplevectors[4], this.samplevectors[5]);
                float pRad = (float) (iRadius * srcScale);
                if (pRad > MAX_RADIUS) {
                    pRad = MAX_RADIUS;
                    srcScale = MAX_RADIUS / iRadius;
                }
                this.passRadius = pRad;
                this.samplevectors[4] = (float) (r0[4] / srcScale);
                this.samplevectors[5] = (float) (r0[5] / srcScale);
            } catch (NoninvertibleTransformException e2) {
                this.passRadius = 0.0f;
                float[] fArr = this.samplevectors;
                this.samplevectors[5] = 0.0f;
                fArr[4] = 0.0f;
                return src;
            }
        }
        float[] fArr2 = this.samplevectors;
        fArr2[4] = fArr2[4] / f2.getPhysicalWidth();
        float[] fArr3 = this.samplevectors;
        fArr3[5] = fArr3[5] / f2.getPhysicalHeight();
        return src;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public Rectangle getPassResultBounds(Rectangle srcdimension, Rectangle outputClip) {
        double r2 = this.validatedPass == 0 ? this.inputRadiusX : this.inputRadiusY;
        int i2 = this.validatedPass * 2;
        double dx = this.samplevectors[i2 + 0] * r2;
        double dy = this.samplevectors[i2 + 1] * r2;
        int padx = (int) Math.ceil(Math.abs(dx));
        int pady = (int) Math.ceil(Math.abs(dy));
        Rectangle ret = new Rectangle(srcdimension);
        ret.grow(padx, pady);
        if (outputClip != null) {
            if (this.validatedPass == 0) {
                double dx2 = this.samplevectors[2] * r2;
                double dy2 = this.samplevectors[3] * r2;
                int padx2 = (int) Math.ceil(Math.abs(dx2));
                int pady2 = (int) Math.ceil(Math.abs(dy2));
                if ((padx2 | pady2) != 0) {
                    outputClip = new Rectangle(outputClip);
                    outputClip.grow(padx2, pady2);
                }
            }
            ret.intersectWith(outputClip);
        }
        return ret;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public LinearConvolveRenderState.PassType getPassType() {
        return this.passType;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public float[] getPassVector() {
        float xoff = this.samplevectors[4];
        float yoff = this.samplevectors[5];
        int ksize = getPassKernelSize();
        int center = ksize / 2;
        float[] ret = {xoff, yoff, (-center) * xoff, (-center) * yoff};
        return ret;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public int getPassWeightsArrayLength() {
        validateWeights();
        return this.weights.limit() / 4;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public FloatBuffer getPassWeights() {
        validateWeights();
        this.weights.rewind();
        return this.weights;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public int getInputKernelSize(int pass) {
        return 1 + (2 * ((int) Math.ceil(pass == 0 ? this.inputRadiusX : this.inputRadiusY)));
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public int getPassKernelSize() {
        return 1 + (2 * ((int) Math.ceil(this.passRadius)));
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public boolean isNop() {
        return !this.isShadow && this.inputRadiusX < 0.00390625f && this.inputRadiusY < 0.00390625f;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public boolean isPassNop() {
        return !(this.isShadow && this.validatedPass == 1) && this.passRadius < 0.00390625f;
    }

    private void validateWeights() {
        float r2 = this.passRadius;
        float s2 = ((float) this.validatedPass) == this.spreadPass ? this.spread : 0.0f;
        if (this.weights == null || this.weightsValidRadius != r2 || this.weightsValidSpread != s2) {
            this.weights = getGaussianWeights(this.weights, (int) Math.ceil(r2), r2, s2);
            this.weightsValidRadius = r2;
            this.weightsValidSpread = s2;
        }
    }
}
