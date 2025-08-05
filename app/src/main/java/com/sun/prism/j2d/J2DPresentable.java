package com.sun.prism.j2d;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Graphics;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.QueuedPixelSource;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPresentable.class */
public abstract class J2DPresentable implements Presentable {
    J2DResourceFactory factory;
    boolean needsResize;
    BufferedImage buffer;
    IntBuffer ib;
    J2DRTTexture readbackBuffer;

    public abstract BufferedImage createBuffer(int i2, int i3);

    static J2DPresentable create(PresentableState pState, J2DResourceFactory factory) {
        return new Glass(pState, factory);
    }

    static J2DPresentable create(BufferedImage buffer, J2DResourceFactory factory) {
        return new Bimg(buffer, factory);
    }

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPresentable$Glass.class */
    private static class Glass extends J2DPresentable {
        private final PresentableState pState;
        private final int theFormat;
        private Pixels pixels;
        private QueuedPixelSource pixelSource;
        private boolean opaque;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !J2DPresentable.class.desiredAssertionStatus();
        }

        Glass(PresentableState pState, J2DResourceFactory factory) {
            super(null, factory);
            this.pixelSource = new QueuedPixelSource(false);
            this.pState = pState;
            this.theFormat = pState.getPixelFormat();
            this.needsResize = true;
        }

        @Override // com.sun.prism.j2d.J2DPresentable
        public BufferedImage createBuffer(int w2, int h2) {
            if (PrismSettings.verbose) {
                System.out.println("Glass native format: " + this.theFormat);
            }
            ByteOrder byteorder = ByteOrder.nativeOrder();
            switch (this.theFormat) {
                case 1:
                    if (byteorder == ByteOrder.LITTLE_ENDIAN) {
                        return new BufferedImage(w2, h2, 3);
                    }
                    throw new UnsupportedOperationException("BYTE_BGRA_PRE pixel format on BIG_ENDIAN");
                case 2:
                    if (byteorder == ByteOrder.BIG_ENDIAN) {
                        return new BufferedImage(w2, h2, 2);
                    }
                    throw new UnsupportedOperationException("BYTE_ARGB pixel format on LITTLE_ENDIAN");
                default:
                    throw new UnsupportedOperationException("unrecognized pixel format: " + this.theFormat);
            }
        }

        @Override // com.sun.prism.Presentable
        public boolean lockResources(PresentableState pState) {
            if (this.pState != pState || this.theFormat != pState.getPixelFormat()) {
                return true;
            }
            this.needsResize = (this.buffer != null && this.buffer.getWidth() == pState.getWidth() && this.buffer.getHeight() == pState.getHeight()) ? false : true;
            return false;
        }

        @Override // com.sun.prism.Presentable
        public boolean prepare(Rectangle dirty) {
            if (!this.pState.isViewClosed()) {
                int w2 = getPhysicalWidth();
                int h2 = getPhysicalHeight();
                this.pixels = this.pixelSource.getUnusedPixels(w2, h2, 1.0f);
                IntBuffer pixBuf = (IntBuffer) this.pixels.getPixels();
                if (!$assertionsDisabled && !this.ib.hasArray()) {
                    throw new AssertionError();
                }
                System.arraycopy(this.ib.array(), 0, pixBuf.array(), 0, w2 * h2);
                return true;
            }
            return false;
        }

        @Override // com.sun.prism.Presentable
        public boolean present() {
            this.pixelSource.enqueuePixels(this.pixels);
            this.pState.uploadPixels(this.pixelSource);
            return true;
        }

        @Override // com.sun.prism.Surface
        public int getContentWidth() {
            return this.pState.getWidth();
        }

        @Override // com.sun.prism.Surface
        public int getContentHeight() {
            return this.pState.getHeight();
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

    /* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPresentable$Bimg.class */
    private static class Bimg extends J2DPresentable {
        private boolean opaque;

        public Bimg(BufferedImage buffer, J2DResourceFactory factory) {
            super(buffer, factory);
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
            throw new UnsupportedOperationException("cannot prepare/present on image");
        }

        @Override // com.sun.prism.Presentable
        public boolean present() {
            throw new UnsupportedOperationException("cannot prepare/present on image");
        }

        @Override // com.sun.prism.Surface
        public int getContentWidth() {
            return this.buffer.getWidth();
        }

        @Override // com.sun.prism.Surface
        public int getContentHeight() {
            return this.buffer.getHeight();
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

    J2DPresentable(BufferedImage buffer, J2DResourceFactory factory) {
        this.buffer = buffer;
        this.factory = factory;
    }

    ResourceFactory getResourceFactory() {
        return this.factory;
    }

    @Override // com.sun.prism.RenderTarget
    public Graphics createGraphics() {
        if (this.needsResize) {
            int w2 = getContentWidth();
            int h2 = getContentHeight();
            this.buffer = null;
            this.readbackBuffer = null;
            this.buffer = createBuffer(w2, h2);
            Raster r2 = this.buffer.getRaster();
            DataBuffer db = r2.getDataBuffer();
            int[] pixels = ((DataBufferInt) db).getData();
            this.ib = IntBuffer.wrap(pixels, db.getOffset(), db.getSize());
            this.needsResize = false;
        }
        Graphics2D g2d = this.buffer.createGraphics();
        return this.factory.createJ2DPrismGraphics(this, g2d);
    }

    J2DRTTexture getReadbackBuffer() {
        if (this.readbackBuffer == null) {
            this.readbackBuffer = (J2DRTTexture) this.factory.createRTTexture(getContentWidth(), getContentHeight(), Texture.WrapMode.CLAMP_NOT_NEEDED);
            this.readbackBuffer.makePermanent();
        }
        return this.readbackBuffer;
    }

    BufferedImage getBackBuffer() {
        return this.buffer;
    }

    @Override // com.sun.prism.RenderTarget
    public Screen getAssociatedScreen() {
        return this.factory.getScreen();
    }

    @Override // com.sun.prism.Surface
    public int getContentX() {
        return 0;
    }

    @Override // com.sun.prism.Surface
    public int getContentY() {
        return 0;
    }

    @Override // com.sun.prism.Presentable
    public float getPixelScaleFactor() {
        return 1.0f;
    }

    @Override // com.sun.prism.Surface
    public int getPhysicalWidth() {
        return this.buffer == null ? getContentWidth() : this.buffer.getWidth();
    }

    @Override // com.sun.prism.Surface
    public int getPhysicalHeight() {
        return this.buffer == null ? getContentHeight() : this.buffer.getHeight();
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isMSAA() {
        return false;
    }
}
