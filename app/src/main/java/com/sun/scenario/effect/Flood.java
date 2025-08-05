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

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Flood.class */
public class Flood extends CoreEffect<RenderState> {
    private Object paint;
    private RectBounds bounds;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public Flood(Object paint) {
        this.bounds = new RectBounds();
        if (paint == null) {
            throw new IllegalArgumentException("Paint must be non-null");
        }
        this.paint = paint;
        updatePeerKey("Flood");
    }

    public Flood(Object paint, RectBounds bounds) {
        this(paint);
        if (bounds == null) {
            throw new IllegalArgumentException("Bounds must be non-null");
        }
        this.bounds.setBounds(bounds);
    }

    public Object getPaint() {
        return this.paint;
    }

    public void setPaint(Object paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Paint must be non-null");
        }
        Object obj = this.paint;
        this.paint = paint;
    }

    public RectBounds getFloodBounds() {
        return new RectBounds(this.bounds);
    }

    public void setFloodBounds(RectBounds bounds) {
        if (bounds == null) {
            throw new IllegalArgumentException("Bounds must be non-null");
        }
        new RectBounds(this.bounds);
        this.bounds.setBounds(bounds);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        return transformBounds(transform, this.bounds);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        DirtyRegionContainer drc = regionPool.checkOut();
        drc.reset();
        return drc;
    }
}
