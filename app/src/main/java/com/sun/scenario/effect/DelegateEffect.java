package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/DelegateEffect.class */
public abstract class DelegateEffect extends Effect {
    protected abstract Effect getDelegate();

    protected DelegateEffect(Effect input) {
        super(input);
    }

    protected DelegateEffect(Effect input1, Effect input2) {
        super(input1, input2);
    }

    @Override // com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        return getDelegate().getBounds(transform, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return getDelegate().filter(fctx, transform, outputClip, renderHelper, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDelegate().untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDelegate().transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return getDelegate().getAccelType(fctx);
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return getDelegate().reducesOpaquePixels();
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        return getDelegate().getDirtyRegions(defaultInput, regionPool);
    }
}
