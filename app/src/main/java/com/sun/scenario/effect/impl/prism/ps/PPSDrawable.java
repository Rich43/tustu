package com.sun.scenario.effect.impl.prism.ps;

import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.ps.ShaderGraphics;
import com.sun.scenario.effect.impl.prism.PrDrawable;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/ps/PPSDrawable.class */
public class PPSDrawable extends PrDrawable {
    private RTTexture rtt;

    private PPSDrawable(RTTexture rtt) {
        super(rtt);
        this.rtt = rtt;
    }

    static PPSDrawable create(RTTexture rtt) {
        return new PPSDrawable(rtt);
    }

    static int getCompatibleWidth(ResourceFactory factory, int w2) {
        return factory.getRTTWidth(w2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static int getCompatibleHeight(ResourceFactory factory, int h2) {
        return factory.getRTTHeight(h2, Texture.WrapMode.CLAMP_TO_ZERO);
    }

    static PPSDrawable create(ResourceFactory factory, int width, int height) {
        RTTexture rtt = factory.createRTTexture(width, height, Texture.WrapMode.CLAMP_TO_ZERO);
        return new PPSDrawable(rtt);
    }

    @Override // com.sun.scenario.effect.impl.prism.PrTexture, com.sun.scenario.effect.LockableResource
    public boolean isLost() {
        return this.rtt == null || this.rtt.isSurfaceLost();
    }

    @Override // com.sun.scenario.effect.Filterable
    public void flush() {
        if (this.rtt != null) {
            this.rtt.dispose();
            this.rtt = null;
        }
    }

    @Override // com.sun.scenario.effect.Filterable
    public Object getData() {
        return this;
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentWidth() {
        return this.rtt.getContentWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentHeight() {
        return this.rtt.getContentHeight();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public int getMaxContentWidth() {
        return this.rtt.getMaxContentWidth();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public int getMaxContentHeight() {
        return this.rtt.getMaxContentHeight();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public void setContentWidth(int contentW) {
        this.rtt.setContentWidth(contentW);
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable, com.sun.scenario.effect.Filterable
    public void setContentHeight(int contentH) {
        this.rtt.setContentHeight(contentH);
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalWidth() {
        return this.rtt.getPhysicalWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalHeight() {
        return this.rtt.getPhysicalHeight();
    }

    @Override // com.sun.scenario.effect.impl.prism.PrDrawable
    public ShaderGraphics createGraphics() {
        return (ShaderGraphics) this.rtt.createGraphics();
    }
}
