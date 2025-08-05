package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Image;
import com.sun.scenario.effect.Filterable;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrImage.class */
public class PrImage implements Filterable {
    private final Image image;

    private PrImage(Image image) {
        this.image = image;
    }

    public static PrImage create(Image image) {
        return new PrImage(image);
    }

    public Image getImage() {
        return this.image;
    }

    @Override // com.sun.scenario.effect.Filterable
    public Object getData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentWidth() {
        return this.image.getWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getContentHeight() {
        return this.image.getHeight();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalWidth() {
        return this.image.getWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getPhysicalHeight() {
        return this.image.getHeight();
    }

    @Override // com.sun.scenario.effect.Filterable
    public float getPixelScale() {
        return this.image.getPixelScale();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getMaxContentWidth() {
        return this.image.getWidth();
    }

    @Override // com.sun.scenario.effect.Filterable
    public int getMaxContentHeight() {
        return this.image.getHeight();
    }

    @Override // com.sun.scenario.effect.Filterable
    public void setContentWidth(int contentW) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override // com.sun.scenario.effect.Filterable
    public void setContentHeight(int contentH) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.scenario.effect.LockableResource
    public void lock() {
    }

    @Override // com.sun.scenario.effect.LockableResource
    public void unlock() {
    }

    @Override // com.sun.scenario.effect.LockableResource
    public boolean isLost() {
        return false;
    }

    @Override // com.sun.scenario.effect.Filterable
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
