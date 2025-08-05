package com.sun.scenario.effect.impl.state;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.Rectangle;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/LinearConvolveRenderState.class */
public abstract class LinearConvolveRenderState implements RenderState {
    public static final int MAX_COMPILED_KERNEL_SIZE = 128;
    public static final int MAX_KERNEL_SIZE;
    static final float MIN_EFFECT_RADIUS = 0.00390625f;
    static final float[] BLACK_COMPONENTS = Color4f.BLACK.getPremultipliedRGBComponents();

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/LinearConvolveRenderState$PassType.class */
    public enum PassType {
        HORIZONTAL_CENTERED,
        VERTICAL_CENTERED,
        GENERAL_VECTOR
    }

    public abstract boolean isShadow();

    public abstract Color4f getShadowColor();

    public abstract int getInputKernelSize(int i2);

    public abstract boolean isNop();

    public abstract ImageData validatePassInput(ImageData imageData, int i2);

    public abstract boolean isPassNop();

    public abstract Rectangle getPassResultBounds(Rectangle rectangle, Rectangle rectangle2);

    public abstract FloatBuffer getPassWeights();

    public abstract int getPassWeightsArrayLength();

    public abstract float[] getPassVector();

    public abstract float[] getPassShadowColorComponents();

    public abstract int getPassKernelSize();

    static {
        int defSize = PlatformUtil.isEmbedded() ? 64 : 128;
        int size = ((Integer) AccessController.doPrivileged(() -> {
            return Integer.getInteger("decora.maxLinearConvolveKernelSize", defSize);
        })).intValue();
        if (size > 128) {
            System.out.println("Clamping maxLinearConvolveKernelSize to 128");
            size = 128;
        }
        MAX_KERNEL_SIZE = size;
    }

    public static int getPeerSize(int ksize) {
        if (ksize < 32) {
            return (ksize + 3) & (-4);
        }
        if (ksize <= MAX_KERNEL_SIZE) {
            return (ksize + 31) & (-32);
        }
        throw new RuntimeException("No peer available for kernel size: " + ksize);
    }

    static boolean nearZero(float v2, int size) {
        return ((double) Math.abs(v2 * ((float) size))) < 0.001953125d;
    }

    static boolean nearOne(float v2, int size) {
        return ((double) Math.abs((v2 * ((float) size)) - ((float) size))) < 0.001953125d;
    }

    public EffectPeer<? extends LinearConvolveRenderState> getPassPeer(Renderer r2, FilterContext fctx) {
        if (isPassNop()) {
            return null;
        }
        int ksize = getPassKernelSize();
        int psize = getPeerSize(ksize);
        String opname = isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
        return r2.getPeerInstance(fctx, opname, psize);
    }

    public PassType getPassType() {
        return PassType.GENERAL_VECTOR;
    }
}
