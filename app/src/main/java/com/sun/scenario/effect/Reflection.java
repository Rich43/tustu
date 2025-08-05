package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Reflection.class */
public class Reflection extends CoreEffect<RenderState> {
    private float topOffset;
    private float topOpacity;
    private float bottomOpacity;
    private float fraction;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public Reflection() {
        this(DefaultInput);
    }

    public Reflection(Effect input) {
        super(input);
        this.topOffset = 0.0f;
        this.topOpacity = 0.5f;
        this.bottomOpacity = 0.0f;
        this.fraction = 0.75f;
        updatePeerKey("Reflection");
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public float getTopOffset() {
        return this.topOffset;
    }

    public void setTopOffset(float topOffset) {
        float f2 = this.topOffset;
        this.topOffset = topOffset;
    }

    public float getTopOpacity() {
        return this.topOpacity;
    }

    public void setTopOpacity(float topOpacity) {
        if (topOpacity < 0.0f || topOpacity > 1.0f) {
            throw new IllegalArgumentException("Top opacity must be in the range [0,1]");
        }
        float f2 = this.topOpacity;
        this.topOpacity = topOpacity;
    }

    public float getBottomOpacity() {
        return this.bottomOpacity;
    }

    public void setBottomOpacity(float bottomOpacity) {
        if (bottomOpacity < 0.0f || bottomOpacity > 1.0f) {
            throw new IllegalArgumentException("Bottom opacity must be in the range [0,1]");
        }
        float f2 = this.bottomOpacity;
        this.bottomOpacity = bottomOpacity;
    }

    public float getFraction() {
        return this.fraction;
    }

    public void setFraction(float fraction) {
        if (fraction < 0.0f || fraction > 1.0f) {
            throw new IllegalArgumentException("Fraction must be in the range [0,1]");
        }
        float f2 = this.fraction;
        this.fraction = fraction;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        Effect input = getDefaultedInput(0, defaultInput);
        BaseBounds r2 = input.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        r2.roundOut();
        float x1 = r2.getMinX();
        float y1 = r2.getMaxY() + this.topOffset;
        float x2 = r2.getMaxX();
        float y2 = y1 + (this.fraction * r2.getHeight());
        BaseBounds ret = new RectBounds(x1, y1, x2, y2);
        return transformBounds(transform, ret.deriveWithUnion(r2));
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        Effect input = getInput();
        return input != null && input.reducesOpaquePixels();
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        BaseBounds contentBounds = di.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        float cbMaxY = contentBounds.getMaxY();
        float reflectedMaxYBase = (2.0f * cbMaxY) + getTopOffset();
        float reflecteCbMaxY = cbMaxY + getTopOffset() + (this.fraction * contentBounds.getHeight());
        DirtyRegionContainer newDRC = regionPool.checkOut();
        for (int i2 = 0; i2 < drc.size(); i2++) {
            BaseBounds regionBounds = drc.getDirtyRegion(i2);
            float reflectedRegionMinY = reflectedMaxYBase - regionBounds.getMaxY();
            float reflectedRegionMaxY = Math.min(reflecteCbMaxY, reflectedRegionMinY + regionBounds.getHeight());
            newDRC.addDirtyRegion(new RectBounds(regionBounds.getMinX(), reflectedRegionMinY, regionBounds.getMaxX(), reflectedRegionMaxY));
        }
        drc.merge(newDRC);
        regionPool.checkIn(newDRC);
        return drc;
    }
}
