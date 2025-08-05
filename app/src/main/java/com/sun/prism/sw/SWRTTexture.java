package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.pisces.JavaSurface;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWRTTexture.class */
class SWRTTexture extends SWArgbPreTexture implements RTTexture {
    private PiscesRenderer pr;
    private JavaSurface surface;
    private final Rectangle dimensions;
    private boolean isOpaque;

    SWRTTexture(SWResourceFactory factory, int w2, int h2) {
        super(factory, Texture.WrapMode.CLAMP_TO_ZERO, w2, h2);
        this.dimensions = new Rectangle();
        allocate();
        this.surface = new JavaSurface(getDataNoClone(), 1, w2, h2);
        this.dimensions.setBounds(0, 0, w2, h2);
    }

    JavaSurface getSurface() {
        return this.surface;
    }

    @Override // com.sun.prism.RTTexture
    public int[] getPixels() {
        if (this.contentWidth == this.physicalWidth) {
            return getDataNoClone();
        }
        return null;
    }

    @Override // com.sun.prism.RTTexture
    public boolean readPixels(Buffer pixels, int x2, int y2, int width, int height) {
        if (x2 != getContentX() || y2 != getContentY() || width != getContentWidth() || height != getContentHeight()) {
            throw new IllegalArgumentException("reading subtexture not yet supported!");
        }
        return readPixels(pixels);
    }

    @Override // com.sun.prism.RTTexture
    public boolean readPixels(Buffer pixels) {
        if (PrismSettings.debug) {
            System.out.println("+ SWRTT.readPixels: this: " + ((Object) this));
        }
        int[] pixbuf = getDataNoClone();
        pixels.clear();
        if (pixels instanceof IntBuffer) {
            IntBuffer iPixels = (IntBuffer) pixels;
            for (int i2 = 0; i2 < this.contentHeight; i2++) {
                iPixels.put(pixbuf, i2 * this.physicalWidth, this.contentWidth);
            }
        } else if (pixels instanceof ByteBuffer) {
            ByteBuffer bPixels = (ByteBuffer) pixels;
            for (int i3 = 0; i3 < this.contentHeight; i3++) {
                for (int j2 = 0; j2 < this.contentWidth; j2++) {
                    int argb = pixbuf[(i3 * this.physicalWidth) + j2];
                    byte a2 = (byte) (argb >> 24);
                    byte r2 = (byte) (argb >> 16);
                    byte g2 = (byte) (argb >> 8);
                    byte b2 = (byte) argb;
                    bPixels.put(b2).put(g2).put(r2).put(a2);
                }
            }
        } else {
            return false;
        }
        pixels.rewind();
        return true;
    }

    @Override // com.sun.prism.RenderTarget
    public Screen getAssociatedScreen() {
        return getResourceFactory().getScreen();
    }

    @Override // com.sun.prism.RenderTarget
    public Graphics createGraphics() {
        if (this.pr == null) {
            this.pr = new PiscesRenderer(this.surface);
        }
        return new SWGraphics(this, getResourceFactory().getContext(), this.pr);
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isOpaque() {
        return this.isOpaque;
    }

    @Override // com.sun.prism.RenderTarget
    public void setOpaque(boolean opaque) {
        this.isOpaque = opaque;
    }

    Rectangle getDimensions() {
        return this.dimensions;
    }

    @Override // com.sun.prism.RTTexture
    public boolean isVolatile() {
        return false;
    }

    public boolean isMSAA() {
        return false;
    }
}
