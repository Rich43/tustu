package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/ColorAdjust.class */
public class ColorAdjust extends CoreEffect<RenderState> {
    private float hue;
    private float saturation;
    private float brightness;
    private float contrast;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public ColorAdjust() {
        this(DefaultInput);
    }

    public ColorAdjust(Effect input) {
        super(input);
        this.hue = 0.0f;
        this.saturation = 0.0f;
        this.brightness = 0.0f;
        this.contrast = 0.0f;
        updatePeerKey("ColorAdjust");
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public float getHue() {
        return this.hue;
    }

    public void setHue(float hue) {
        if (hue < -1.0f || hue > 1.0f) {
            throw new IllegalArgumentException("Hue must be in the range [-1, 1]");
        }
        float f2 = this.hue;
        this.hue = hue;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setSaturation(float saturation) {
        if (saturation < -1.0f || saturation > 1.0f) {
            throw new IllegalArgumentException("Saturation must be in the range [-1, 1]");
        }
        float f2 = this.saturation;
        this.saturation = saturation;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setBrightness(float brightness) {
        if (brightness < -1.0f || brightness > 1.0f) {
            throw new IllegalArgumentException("Brightness must be in the range [-1, 1]");
        }
        float f2 = this.brightness;
        this.brightness = brightness;
    }

    public float getContrast() {
        return this.contrast;
    }

    public void setContrast(float contrast) {
        if (contrast < -1.0f || contrast > 1.0f) {
            throw new IllegalArgumentException("Contrast must be in the range [-1, 1]");
        }
        float f2 = this.contrast;
        this.contrast = contrast;
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        Effect input = getInput();
        return input != null && input.reducesOpaquePixels();
    }
}
