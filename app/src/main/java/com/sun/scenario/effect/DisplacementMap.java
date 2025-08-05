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

/* loaded from: jfxrt.jar:com/sun/scenario/effect/DisplacementMap.class */
public class DisplacementMap extends CoreEffect<RenderState> {
    private FloatMap mapData;
    private float scaleX;
    private float scaleY;
    private float offsetX;
    private float offsetY;
    private boolean wrap;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    public DisplacementMap(FloatMap mapData) {
        this(mapData, DefaultInput);
    }

    public DisplacementMap(FloatMap mapData, Effect contentInput) {
        super(contentInput);
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        setMapData(mapData);
        updatePeerKey("DisplacementMap");
    }

    public final FloatMap getMapData() {
        return this.mapData;
    }

    public void setMapData(FloatMap mapData) {
        if (mapData == null) {
            throw new IllegalArgumentException("Map data must be non-null");
        }
        FloatMap floatMap = this.mapData;
        this.mapData = mapData;
    }

    public final Effect getContentInput() {
        return getInputs().get(0);
    }

    public void setContentInput(Effect contentInput) {
        setInput(0, contentInput);
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float scaleX) {
        float f2 = this.scaleX;
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float scaleY) {
        float f2 = this.scaleY;
        this.scaleY = scaleY;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float offsetX) {
        float f2 = this.offsetX;
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offsetY) {
        float f2 = this.offsetY;
        this.offsetY = offsetY;
    }

    public boolean getWrap() {
        return this.wrap;
    }

    public void setWrap(boolean wrap) {
        boolean z2 = this.wrap;
        this.wrap = wrap;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        BaseBounds r2 = getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        float rw = r2.getWidth();
        float rh = r2.getHeight();
        float x2 = (p2.f11907x - r2.getMinX()) / rw;
        float y2 = (p2.f11908y - r2.getMinY()) / rh;
        if (x2 >= 0.0f && y2 >= 0.0f && x2 < 1.0f && y2 < 1.0f) {
            int mx = (int) (x2 * this.mapData.getWidth());
            int my = (int) (y2 * this.mapData.getHeight());
            float dx = this.mapData.getSample(mx, my, 0);
            float dy = this.mapData.getSample(mx, my, 1);
            float x3 = x2 + (this.scaleX * (dx + this.offsetX));
            float y3 = y2 + (this.scaleY * (dy + this.offsetY));
            if (this.wrap) {
                x3 = (float) (x3 - Math.floor(x3));
                y3 = (float) (y3 - Math.floor(y3));
            }
            p2 = new Point2D((x3 * rw) + r2.getMinX(), (y3 * rh) + r2.getMinY());
        }
        return getDefaultedInput(0, defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public ImageData filterImageDatas(FilterContext fctx, BaseTransform transform, Rectangle outputClip, RenderState rstate, ImageData... inputs) {
        return super.filterImageDatas(fctx, transform, null, rstate, inputs);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        DirtyRegionContainer drc = regionPool.checkOut();
        drc.deriveWithNewRegion((RectBounds) getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput));
        return drc;
    }
}
