package com.sun.prism.j2d;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.PresentableState;
import com.sun.prism.PrinterGraphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/PrismPrintGraphics.class */
public final class PrismPrintGraphics extends J2DPrismGraphics implements PrinterGraphics {
    private AffineTransform origTx2D;

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/PrismPrintGraphics$PrintResourceFactory.class */
    static class PrintResourceFactory extends J2DResourceFactory {
        PrintResourceFactory() {
            super(null);
        }

        @Override // com.sun.prism.j2d.J2DResourceFactory
        J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable target, Graphics2D g2d) {
            J2DPrismGraphics pg = new PrismPrintGraphics(target, g2d);
            Rectangle cr = new Rectangle(0, 0, target.getContentWidth(), target.getContentHeight());
            pg.setClipRect(cr);
            return pg;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/PrismPrintGraphics$PagePresentable.class */
    static class PagePresentable extends J2DPresentable {
        private int width;
        private int height;
        static J2DResourceFactory factory = new PrintResourceFactory();
        private boolean opaque;

        PagePresentable(int width, int height) {
            super(null, factory);
            this.width = width;
            this.height = height;
        }

        @Override // com.sun.prism.j2d.J2DPresentable
        public BufferedImage createBuffer(int w2, int h2) {
            throw new UnsupportedOperationException("cannot create new buffers for image");
        }

        @Override // com.sun.prism.Presentable
        public boolean lockResources(PresentableState pState) {
            return false;
        }

        @Override // com.sun.prism.Presentable
        public boolean prepare(Rectangle dirtyregion) {
            throw new UnsupportedOperationException("Cannot prepare an image");
        }

        @Override // com.sun.prism.Presentable
        public boolean present() {
            throw new UnsupportedOperationException("Cannot present on image");
        }

        @Override // com.sun.prism.Surface
        public int getContentWidth() {
            return this.width;
        }

        @Override // com.sun.prism.Surface
        public int getContentHeight() {
            return this.height;
        }

        @Override // com.sun.prism.RenderTarget
        public void setOpaque(boolean opaque) {
            this.opaque = opaque;
        }

        @Override // com.sun.prism.RenderTarget
        public boolean isOpaque() {
            return this.opaque;
        }
    }

    @Override // com.sun.prism.j2d.J2DPrismGraphics
    protected void setTransformG2D(AffineTransform tx) {
        this.g2d.setTransform(this.origTx2D);
        this.g2d.transform(tx);
    }

    @Override // com.sun.prism.j2d.J2DPrismGraphics
    protected void captureTransform(Graphics2D g2d) {
        this.origTx2D = g2d.getTransform();
    }

    public PrismPrintGraphics(Graphics2D g2d, int width, int height) {
        super(new PagePresentable(width, height), g2d);
        setClipRect(new Rectangle(0, 0, width, height));
    }

    PrismPrintGraphics(J2DPresentable target, Graphics2D g2d) {
        super(target, g2d);
    }
}
