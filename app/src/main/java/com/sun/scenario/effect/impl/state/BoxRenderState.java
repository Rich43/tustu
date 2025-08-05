package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/BoxRenderState.class */
public class BoxRenderState extends LinearConvolveRenderState {
    private static final int[] MAX_BOX_SIZES;
    private final boolean isShadow;
    private final int blurPasses;
    private final float spread;
    private Color4f shadowColor;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private final float inputSizeH;
    private final float inputSizeV;
    private final int spreadPass;
    private float[] samplevectors;
    private int validatedPass;
    private float passSize;
    private FloatBuffer weights;
    private float weightsValidSize;
    private float weightsValidSpread;
    private boolean swCompatible;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BoxRenderState.class.desiredAssertionStatus();
        MAX_BOX_SIZES = new int[]{getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 0), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 1), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 2), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 3)};
    }

    public static int getMaxSizeForKernelSize(int kernelSize, int blurPasses) {
        if (blurPasses == 0) {
            return Integer.MAX_VALUE;
        }
        int passSize = ((((kernelSize - 1) | 1) - 1) / blurPasses) | 1;
        if ($assertionsDisabled || getKernelSize(passSize, blurPasses) <= kernelSize) {
            return passSize;
        }
        throw new AssertionError();
    }

    public static int getKernelSize(int passSize, int blurPasses) {
        int kernelSize = passSize < 1 ? 1 : passSize;
        return (((kernelSize - 1) * blurPasses) + 1) | 1;
    }

    public BoxRenderState(float hsize, float vsize, int blurPasses, float spread, boolean isShadow, Color4f shadowColor, BaseTransform filtertx) {
        this.isShadow = isShadow;
        this.shadowColor = shadowColor;
        this.spread = spread;
        this.blurPasses = blurPasses;
        filtertx = filtertx == null ? BaseTransform.IDENTITY_TRANSFORM : filtertx;
        double txScaleX = Math.hypot(filtertx.getMxx(), filtertx.getMyx());
        double txScaleY = Math.hypot(filtertx.getMxy(), filtertx.getMyy());
        float fSizeH = (float) (hsize * txScaleX);
        float fSizeV = (float) (vsize * txScaleY);
        int maxPassSize = MAX_BOX_SIZES[blurPasses];
        if (fSizeH > maxPassSize) {
            txScaleX = maxPassSize / hsize;
            fSizeH = maxPassSize;
        }
        if (fSizeV > maxPassSize) {
            txScaleY = maxPassSize / vsize;
            fSizeV = maxPassSize;
        }
        this.inputSizeH = fSizeH;
        this.inputSizeV = fSizeV;
        this.spreadPass = fSizeV > 1.0f ? 1 : 0;
        boolean custom = (txScaleX == filtertx.getMxx() && 0.0d == filtertx.getMyx() && txScaleY == filtertx.getMyy() && 0.0d == filtertx.getMxy()) ? false : true;
        if (custom) {
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            this.inputtx = BaseTransform.getScaleInstance(txScaleX, txScaleY);
            this.resulttx = filtertx.copy().deriveWithScale(1.0d / txScaleX, 1.0d / txScaleY, 1.0d);
        } else {
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = filtertx;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
        }
    }

    public int getBoxPixelSize(int pass) {
        float size = this.passSize;
        if (size < 1.0f) {
            size = 1.0f;
        }
        int boxsize = ((int) Math.ceil(size)) | 1;
        return boxsize;
    }

    public int getBlurPasses() {
        return this.blurPasses;
    }

    public float getSpread() {
        return this.spread;
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

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public EffectPeer<BoxRenderState> getPassPeer(Renderer r2, FilterContext fctx) {
        String name;
        if (isPassNop()) {
            return null;
        }
        int ksize = getPassKernelSize();
        int psize = getPeerSize(ksize);
        Effect.AccelType actype = r2.getAccelType();
        switch (actype) {
            case NONE:
            case SIMD:
                if (this.swCompatible && this.spread == 0.0f) {
                    name = isShadow() ? "BoxShadow" : "BoxBlur";
                    break;
                }
                break;
            default:
                name = isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
                break;
        }
        EffectPeer peer = r2.getPeerInstance(fctx, name, psize);
        return peer;
    }

    @Override // com.sun.scenario.effect.impl.state.RenderState
    public Rectangle getInputClip(int i2, Rectangle filterClip) {
        if (filterClip != null) {
            int klenh = getInputKernelSize(0);
            int klenv = getInputKernelSize(1);
            if ((klenh | klenv) > 1) {
                filterClip = new Rectangle(filterClip);
                filterClip.grow(klenh / 2, klenv / 2);
            }
        }
        return filterClip;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public ImageData validatePassInput(ImageData src, int pass) {
        this.validatedPass = pass;
        BaseTransform srcTx = src.getTransform();
        this.samplevectors = new float[2];
        this.samplevectors[pass] = 1.0f;
        float iSize = pass == 0 ? this.inputSizeH : this.inputSizeV;
        if (srcTx.isTranslateOrIdentity()) {
            this.swCompatible = true;
            this.passSize = iSize;
        } else {
            try {
                srcTx.inverseDeltaTransform(this.samplevectors, 0, this.samplevectors, 0, 1);
                double srcScale = Math.hypot(this.samplevectors[0], this.samplevectors[1]);
                float pSize = (float) (((float) (iSize * srcScale)) * srcScale);
                int maxPassSize = MAX_BOX_SIZES[this.blurPasses];
                if (pSize > maxPassSize) {
                    pSize = maxPassSize;
                    srcScale = maxPassSize / iSize;
                }
                this.passSize = pSize;
                this.samplevectors[0] = (float) (r0[0] / srcScale);
                this.samplevectors[1] = (float) (r0[1] / srcScale);
                Rectangle srcSize = src.getUntransformedBounds();
                if (pass == 0) {
                    this.swCompatible = nearOne(this.samplevectors[0], srcSize.width) && nearZero(this.samplevectors[1], srcSize.width);
                } else {
                    this.swCompatible = nearZero(this.samplevectors[0], srcSize.height) && nearOne(this.samplevectors[1], srcSize.height);
                }
            } catch (NoninvertibleTransformException e2) {
                this.passSize = 0.0f;
                float[] fArr = this.samplevectors;
                this.samplevectors[1] = 0.0f;
                fArr[0] = 0.0f;
                this.swCompatible = true;
                return src;
            }
        }
        Filterable f2 = src.getUntransformedImage();
        float[] fArr2 = this.samplevectors;
        fArr2[0] = fArr2[0] / f2.getPhysicalWidth();
        float[] fArr3 = this.samplevectors;
        fArr3[1] = fArr3[1] / f2.getPhysicalHeight();
        return src;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public Rectangle getPassResultBounds(Rectangle srcdimension, Rectangle outputClip) {
        Rectangle ret = new Rectangle(srcdimension);
        if (this.validatedPass == 0) {
            ret.grow(getInputKernelSize(0) / 2, 0);
        } else {
            ret.grow(0, getInputKernelSize(1) / 2);
        }
        if (outputClip != null) {
            if (this.validatedPass == 0) {
                outputClip = new Rectangle(outputClip);
                outputClip.grow(0, getInputKernelSize(1) / 2);
            }
            ret.intersectWith(outputClip);
        }
        return ret;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public float[] getPassVector() {
        float xoff = this.samplevectors[0];
        float yoff = this.samplevectors[1];
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

    private void validateWeights() {
        float pSize;
        if (this.blurPasses == 0) {
            pSize = 1.0f;
        } else {
            pSize = this.passSize;
            if (pSize < 1.0f) {
                pSize = 1.0f;
            }
        }
        float passSpread = this.validatedPass == this.spreadPass ? this.spread : 0.0f;
        if (this.weights != null && this.weightsValidSize == pSize && this.weightsValidSpread == passSpread) {
            return;
        }
        int klen = ((int) Math.ceil(pSize)) | 1;
        int totalklen = klen;
        for (int p2 = 1; p2 < this.blurPasses; p2++) {
            totalklen += klen - 1;
        }
        double[] ik = new double[totalklen];
        for (int i2 = 0; i2 < klen; i2++) {
            ik[i2] = 1.0d;
        }
        double excess = klen - pSize;
        if (excess > 0.0d) {
            double d2 = 1.0d - (excess * 0.5d);
            ik[klen - 1] = d2;
            ik[0] = d2;
        }
        int filledklen = klen;
        for (int p3 = 1; p3 < this.blurPasses; p3++) {
            filledklen += klen - 1;
            int i3 = filledklen - 1;
            while (i3 > klen) {
                double sum = ik[i3];
                for (int k2 = 1; k2 < klen; k2++) {
                    sum += ik[i3 - k2];
                }
                int i4 = i3;
                i3--;
                ik[i4] = sum;
            }
            while (i3 > 0) {
                double sum2 = ik[i3];
                for (int k3 = 0; k3 < i3; k3++) {
                    sum2 += ik[k3];
                }
                int i5 = i3;
                i3--;
                ik[i5] = sum2;
            }
        }
        double sum3 = 0.0d;
        for (double d3 : ik) {
            sum3 += d3;
        }
        double sum4 = sum3 + ((1.0d - sum3) * passSpread);
        if (this.weights == null) {
            int maxbufsize = getPeerSize(MAX_KERNEL_SIZE);
            this.weights = BufferUtil.newFloatBuffer((maxbufsize + 3) & (-4));
        }
        this.weights.clear();
        for (double d4 : ik) {
            this.weights.put((float) (d4 / sum4));
        }
        int limit = getPeerSize(ik.length);
        while (this.weights.position() < limit) {
            this.weights.put(0.0f);
        }
        this.weights.limit(limit);
        this.weights.rewind();
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public int getInputKernelSize(int pass) {
        float size = pass == 0 ? this.inputSizeH : this.inputSizeV;
        if (size < 1.0f) {
            size = 1.0f;
        }
        int klen = ((int) Math.ceil(size)) | 1;
        int totalklen = 1;
        for (int p2 = 0; p2 < this.blurPasses; p2++) {
            totalklen += klen - 1;
        }
        return totalklen;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public int getPassKernelSize() {
        float size = this.passSize;
        if (size < 1.0f) {
            size = 1.0f;
        }
        int klen = ((int) Math.ceil(size)) | 1;
        int totalklen = 1;
        for (int p2 = 0; p2 < this.blurPasses; p2++) {
            totalklen += klen - 1;
        }
        return totalklen;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public boolean isNop() {
        if (this.isShadow) {
            return false;
        }
        return this.blurPasses == 0 || (this.inputSizeH <= 1.0f && this.inputSizeV <= 1.0f);
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveRenderState
    public boolean isPassNop() {
        if (this.isShadow && this.validatedPass == 1) {
            return false;
        }
        return this.blurPasses == 0 || this.passSize <= 1.0f;
    }
}
