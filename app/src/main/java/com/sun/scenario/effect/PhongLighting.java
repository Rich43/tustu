package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.light.Light;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/PhongLighting.class */
public class PhongLighting extends CoreEffect<RenderState> {
    private float surfaceScale;
    private float diffuseConstant;
    private float specularConstant;
    private float specularExponent;
    private Light light;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public PhongLighting(Light light) {
        this(light, new GaussianShadow(10.0f), DefaultInput);
    }

    public PhongLighting(Light light, Effect bumpInput, Effect contentInput) {
        super(bumpInput, contentInput);
        this.surfaceScale = 1.0f;
        this.diffuseConstant = 1.0f;
        this.specularConstant = 1.0f;
        this.specularExponent = 1.0f;
        setLight(light);
    }

    public final Effect getBumpInput() {
        return getInputs().get(0);
    }

    public void setBumpInput(Effect bumpInput) {
        setInput(0, bumpInput);
    }

    public final Effect getContentInput() {
        return getInputs().get(1);
    }

    private Effect getContentInput(Effect defaultInput) {
        return getDefaultedInput(1, defaultInput);
    }

    public void setContentInput(Effect contentInput) {
        setInput(1, contentInput);
    }

    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        if (light == null) {
            throw new IllegalArgumentException("Light must be non-null");
        }
        this.light = light;
        updatePeerKey("PhongLighting_" + light.getType().name());
    }

    public float getDiffuseConstant() {
        return this.diffuseConstant;
    }

    public void setDiffuseConstant(float diffuseConstant) {
        if (diffuseConstant < 0.0f || diffuseConstant > 2.0f) {
            throw new IllegalArgumentException("Diffuse constant must be in the range [0,2]");
        }
        float f2 = this.diffuseConstant;
        this.diffuseConstant = diffuseConstant;
    }

    public float getSpecularConstant() {
        return this.specularConstant;
    }

    public void setSpecularConstant(float specularConstant) {
        if (specularConstant < 0.0f || specularConstant > 2.0f) {
            throw new IllegalArgumentException("Specular constant must be in the range [0,2]");
        }
        float f2 = this.specularConstant;
        this.specularConstant = specularConstant;
    }

    public float getSpecularExponent() {
        return this.specularExponent;
    }

    public void setSpecularExponent(float specularExponent) {
        if (specularExponent < 0.0f || specularExponent > 40.0f) {
            throw new IllegalArgumentException("Specular exponent must be in the range [0,40]");
        }
        float f2 = this.specularExponent;
        this.specularExponent = specularExponent;
    }

    public float getSurfaceScale() {
        return this.surfaceScale;
    }

    public void setSurfaceScale(float surfaceScale) {
        if (surfaceScale < 0.0f || surfaceScale > 10.0f) {
            throw new IllegalArgumentException("Surface scale must be in the range [0,10]");
        }
        float f2 = this.surfaceScale;
        this.surfaceScale = surfaceScale;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        return getContentInput(defaultInput).getBounds(transform, defaultInput);
    }

    @Override // com.sun.scenario.effect.Effect
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        return super.getResultBounds(transform, outputClip, inputDatas[1]);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getContentInput(defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getContentInput(defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return new RenderState() { // from class: com.sun.scenario.effect.PhongLighting.1
            @Override // com.sun.scenario.effect.impl.state.RenderState
            public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
                return RenderState.EffectCoordinateSpace.RenderSpace;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public BaseTransform getInputTransform(BaseTransform filterTransform) {
                return filterTransform;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public BaseTransform getResultTransform(BaseTransform filterTransform) {
                return BaseTransform.IDENTITY_TRANSFORM;
            }

            @Override // com.sun.scenario.effect.impl.state.RenderState
            public Rectangle getInputClip(int i2, Rectangle filterClip) {
                if (i2 == 0 && filterClip != null) {
                    Rectangle r2 = new Rectangle(filterClip);
                    r2.grow(1, 1);
                    return r2;
                }
                return filterClip;
            }
        };
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        Effect contentInput = getContentInput();
        return contentInput != null && contentInput.reducesOpaquePixels();
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect bump = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc1 = bump.getDirtyRegions(defaultInput, regionPool);
        drc1.grow(1, 1);
        Effect content = getDefaultedInput(1, defaultInput);
        DirtyRegionContainer drc2 = content.getDirtyRegions(defaultInput, regionPool);
        drc1.merge(drc2);
        regionPool.checkIn(drc2);
        return drc1;
    }
}
