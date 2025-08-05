package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/InvertMask.class */
public class InvertMask extends CoreEffect<RenderState> {
    private int pad;
    private int xoff;
    private int yoff;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public InvertMask() {
        this(10);
    }

    public InvertMask(Effect input) {
        this(10, input);
    }

    public InvertMask(int pad) {
        this(pad, DefaultInput);
    }

    public InvertMask(int pad, Effect input) {
        super(input);
        setPad(pad);
        updatePeerKey("InvertMask");
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public int getPad() {
        return this.pad;
    }

    public void setPad(int pad) {
        if (pad < 0) {
            throw new IllegalArgumentException("Pad value must be non-negative");
        }
        int i2 = this.pad;
        this.pad = pad;
    }

    public int getOffsetX() {
        return this.xoff;
    }

    public void setOffsetX(int xoff) {
        int i2 = this.xoff;
        this.xoff = xoff;
    }

    public int getOffsetY() {
        return this.yoff;
    }

    public void setOffsetY(int yoff) {
        float f2 = this.yoff;
        this.yoff = yoff;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseBounds bounds = super.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        BaseBounds ret = new RectBounds(bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY());
        ((RectBounds) ret).grow(this.pad, this.pad);
        if (!transform.isIdentity()) {
            ret = transformBounds(transform, ret);
        }
        return ret;
    }

    @Override // com.sun.scenario.effect.Effect
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle r2 = super.getResultBounds(transform, outputClip, inputDatas);
        Rectangle ret = new Rectangle(r2);
        ret.grow(this.pad, this.pad);
        return ret;
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return new RenderState() { // from class: com.sun.scenario.effect.InvertMask.1
            @Override // com.sun.scenario.effect.impl.state.RenderState
            public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
                return RenderState.EffectCoordinateSpace.UserSpace;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public BaseTransform getInputTransform(BaseTransform filterTransform) {
                return BaseTransform.IDENTITY_TRANSFORM;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public BaseTransform getResultTransform(BaseTransform filterTransform) {
                return filterTransform;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public Rectangle getInputClip(int i2, Rectangle filterClip) {
                if (filterClip != null && InvertMask.this.pad != 0) {
                    filterClip = new Rectangle(filterClip);
                    filterClip.grow(InvertMask.this.pad, InvertMask.this.pad);
                }
                return filterClip;
            }
        };
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        if (this.xoff != 0 || this.yoff != 0) {
            for (int i2 = 0; i2 < drc.size(); i2++) {
                drc.getDirtyRegion(i2).translate(this.xoff, this.yoff, 0.0f);
            }
        }
        return drc;
    }
}
