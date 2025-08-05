package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/LinearConvolveKernel.class */
public abstract class LinearConvolveKernel {
    public abstract Rectangle getResultBounds(Rectangle rectangle, int i2);

    public abstract int getKernelSize(int i2);

    public abstract LinearConvolveRenderState getRenderState(BaseTransform baseTransform);

    public boolean isShadow() {
        return false;
    }

    public boolean isNop() {
        return false;
    }
}
