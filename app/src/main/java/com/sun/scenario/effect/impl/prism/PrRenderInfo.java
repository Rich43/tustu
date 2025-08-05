package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.ImageDataRenderer;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrRenderInfo.class */
public class PrRenderInfo implements ImageDataRenderer {

    /* renamed from: g, reason: collision with root package name */
    private Graphics f12038g;

    public PrRenderInfo(Graphics g2) {
        this.f12038g = g2;
    }

    public Graphics getGraphics() {
        return this.f12038g;
    }

    @Override // com.sun.scenario.effect.ImageDataRenderer
    public void renderImage(ImageData image, BaseTransform transform, FilterContext fctx) {
        if (image.validate(fctx)) {
            Rectangle r2 = image.getUntransformedBounds();
            Texture tex = ((PrTexture) image.getUntransformedImage()).getTextureObject();
            BaseTransform savedTx = null;
            if (!transform.isIdentity()) {
                savedTx = this.f12038g.getTransformNoClone().copy();
                this.f12038g.transform(transform);
            }
            BaseTransform idtx = image.getTransform();
            if (!idtx.isIdentity()) {
                if (savedTx == null) {
                    savedTx = this.f12038g.getTransformNoClone().copy();
                }
                this.f12038g.transform(idtx);
            }
            this.f12038g.drawTexture(tex, r2.f11913x, r2.f11914y, r2.width, r2.height);
            if (savedTx != null) {
                this.f12038g.setTransform(savedTx);
            }
        }
    }
}
