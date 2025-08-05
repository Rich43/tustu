package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.Renderer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Identity.class */
public class Identity extends Effect {
    private Filterable src;
    private Point2D loc = new Point2D();
    private final Map<FilterContext, ImageData> datacache = new HashMap();

    public Identity(Filterable src) {
        this.src = src;
    }

    public final Filterable getSource() {
        return this.src;
    }

    public void setSource(Filterable src) {
        Filterable filterable = this.src;
        this.src = src;
        clearCache();
    }

    public final Point2D getLocation() {
        return this.loc;
    }

    public void setLocation(Point2D pt) {
        if (pt == null) {
            throw new IllegalArgumentException("Location must be non-null");
        }
        Point2D point2D = this.loc;
        this.loc.setLocation(pt);
    }

    @Override // com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        if (this.src == null) {
            return new RectBounds();
        }
        float srcw = this.src.getPhysicalWidth() / this.src.getPixelScale();
        float srch = this.src.getPhysicalHeight() / this.src.getPixelScale();
        BaseBounds r2 = new RectBounds(this.loc.f11907x, this.loc.f11908y, this.loc.f11907x + srcw, this.loc.f11908y + srch);
        if (transform != null && !transform.isIdentity()) {
            r2 = transformBounds(transform, r2);
        }
        return r2;
    }

    @Override // com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        ImageData id = this.datacache.get(fctx);
        if (id != null && !id.addref()) {
            id.setReusable(false);
            this.datacache.remove(fctx);
            id.unref();
            id = null;
        }
        if (id == null) {
            Renderer r2 = Renderer.getRenderer(fctx);
            Filterable f2 = this.src;
            if (f2 == null) {
                id = new ImageData(fctx, getCompatibleImage(fctx, 1, 1), new Rectangle(1, 1));
            } else {
                id = r2.createImageData(fctx, f2);
            }
            if (id == null) {
                return new ImageData(fctx, (Filterable) null, (Rectangle) null);
            }
            id.setReusable(true);
            this.datacache.put(fctx, id);
        }
        return id.transform(Offset.getOffsetTransform(transform, this.loc.f11907x, this.loc.f11908y));
    }

    @Override // com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return Effect.AccelType.INTRINSIC;
    }

    private void clearCache() {
        this.datacache.clear();
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
