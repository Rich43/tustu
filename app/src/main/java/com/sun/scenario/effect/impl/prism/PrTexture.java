package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import com.sun.scenario.effect.LockableResource;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrTexture.class */
public class PrTexture<T extends Texture> implements LockableResource {
    private final T tex;
    private final Rectangle bounds;

    public PrTexture(T tex) {
        if (tex == null) {
            throw new IllegalArgumentException("Texture must be non-null");
        }
        this.tex = tex;
        this.bounds = new Rectangle(tex.getPhysicalWidth(), tex.getPhysicalHeight());
    }

    @Override // com.sun.scenario.effect.LockableResource
    public void lock() {
        if (this.tex != null) {
            this.tex.lock();
        }
    }

    @Override // com.sun.scenario.effect.LockableResource
    public void unlock() {
        if (this.tex != null) {
            this.tex.unlock();
        }
    }

    @Override // com.sun.scenario.effect.LockableResource
    public boolean isLost() {
        return this.tex.isSurfaceLost();
    }

    public Rectangle getNativeBounds() {
        return this.bounds;
    }

    public T getTextureObject() {
        return this.tex;
    }
}
