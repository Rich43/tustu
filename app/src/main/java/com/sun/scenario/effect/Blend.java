package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Blend.class */
public class Blend extends CoreEffect<RenderState> {
    private Mode mode;
    private float opacity;

    /* loaded from: jfxrt.jar:com/sun/scenario/effect/Blend$Mode.class */
    public enum Mode {
        SRC_OVER,
        SRC_IN,
        SRC_OUT,
        SRC_ATOP,
        ADD,
        MULTIPLY,
        SCREEN,
        OVERLAY,
        DARKEN,
        LIGHTEN,
        COLOR_DODGE,
        COLOR_BURN,
        HARD_LIGHT,
        SOFT_LIGHT,
        DIFFERENCE,
        EXCLUSION,
        RED,
        GREEN,
        BLUE
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public Blend(Mode mode, Effect bottomInput, Effect topInput) {
        super(bottomInput, topInput);
        setMode(mode);
        setOpacity(1.0f);
    }

    public final Effect getBottomInput() {
        return getInputs().get(0);
    }

    public void setBottomInput(Effect bottomInput) {
        setInput(0, bottomInput);
    }

    public final Effect getTopInput() {
        return getInputs().get(1);
    }

    public void setTopInput(Effect topInput) {
        setInput(1, topInput);
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setMode(Mode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode must be non-null");
        }
        Mode mode2 = this.mode;
        this.mode = mode;
        updatePeerKey("Blend_" + mode.name());
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setOpacity(float opacity) {
        if (opacity < 0.0f || opacity > 1.0f) {
            throw new IllegalArgumentException("Opacity must be in the range [0,1]");
        }
        float f2 = this.opacity;
        this.opacity = opacity;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        Effect bottomInput = getBottomInput();
        Effect topInput = getTopInput();
        switch (getMode()) {
            case SRC_ATOP:
                if (bottomInput == null || !bottomInput.reducesOpaquePixels()) {
                }
                break;
            case SRC_OVER:
            case ADD:
            case MULTIPLY:
            case SCREEN:
            case OVERLAY:
            case DARKEN:
            case LIGHTEN:
            case COLOR_DODGE:
            case COLOR_BURN:
            case HARD_LIGHT:
            case SOFT_LIGHT:
            case DIFFERENCE:
            case EXCLUSION:
            case RED:
            case GREEN:
            case BLUE:
                if (topInput == null || !topInput.reducesOpaquePixels() || bottomInput == null || !bottomInput.reducesOpaquePixels()) {
                }
                break;
        }
        return true;
    }
}
