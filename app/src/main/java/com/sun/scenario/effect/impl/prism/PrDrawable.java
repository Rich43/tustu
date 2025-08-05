package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.ImagePool;
import com.sun.scenario.effect.impl.PoolFilterable;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.ref.WeakReference;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrDrawable.class */
public abstract class PrDrawable extends PrTexture<RTTexture> implements PoolFilterable {
    private WeakReference<ImagePool> pool;

    public abstract Graphics createGraphics();

    public static PrDrawable create(FilterContext fctx, RTTexture rtt) {
        return ((PrRenderer) Renderer.getRenderer(fctx)).createDrawable(rtt);
    }

    protected PrDrawable(RTTexture rtt) {
        super(rtt);
    }

    @Override // com.sun.scenario.effect.impl.PoolFilterable
    public void setImagePool(ImagePool pool) {
        this.pool = new WeakReference<>(pool);
    }

    @Override // com.sun.scenario.effect.impl.PoolFilterable
    public ImagePool getImagePool() {
        if (this.pool == null) {
            return null;
        }
        return this.pool.get();
    }

    @Override // com.sun.scenario.effect.Filterable
    public float getPixelScale() {
        return 1.0f;
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getMaxContentWidth() {
        return getTextureObject().getMaxContentWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getMaxContentHeight() {
        return getTextureObject().getMaxContentHeight();
    }

    @Override // com.sun.scenario.effect.Filterable
    public void setContentWidth(int contentW) {
        getTextureObject().setContentWidth(contentW);
    }

    @Override // com.sun.scenario.effect.Filterable
    public void setContentHeight(int contentH) {
        getTextureObject().setContentHeight(contentH);
    }

    public void clear() {
        Graphics g2 = createGraphics();
        g2.clear();
    }
}
