package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DRTTexture.class */
class J2DRTTexture extends J2DTexture implements RTTexture {
    protected J2DResourceFactory factory;
    private boolean opaque;

    J2DRTTexture(int w2, int h2, J2DResourceFactory factory) {
        super(new BufferedImage(w2, h2, 3), PixelFormat.INT_ARGB_PRE, IntArgbPre.setter, Texture.WrapMode.CLAMP_TO_ZERO);
        this.factory = factory;
        this.opaque = false;
    }

    @Override // com.sun.prism.RTTexture
    public int[] getPixels() {
        BufferedImage bimg = getBufferedImage();
        DataBuffer db = bimg.getRaster().getDataBuffer();
        if (db instanceof DataBufferInt) {
            return ((DataBufferInt) db).getData();
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
        int w2 = getContentWidth();
        int h2 = getContentHeight();
        int[] pixbuf = getPixels();
        pixels.clear();
        for (int i2 = 0; i2 < w2 * h2; i2++) {
            int argb = pixbuf[i2];
            if (pixels instanceof IntBuffer) {
                ((IntBuffer) pixels).put(argb);
            } else if (pixels instanceof ByteBuffer) {
                byte a2 = (byte) (argb >> 24);
                byte r2 = (byte) (argb >> 16);
                byte g2 = (byte) (argb >> 8);
                byte b2 = (byte) argb;
                ((ByteBuffer) pixels).put(b2);
                ((ByteBuffer) pixels).put(g2);
                ((ByteBuffer) pixels).put(r2);
                ((ByteBuffer) pixels).put(a2);
            }
        }
        pixels.rewind();
        return true;
    }

    @Override // com.sun.prism.RenderTarget
    public Graphics createGraphics() {
        BufferedImage bimg = getBufferedImage();
        J2DPresentable presentable = J2DPresentable.create(bimg, this.factory);
        Graphics2D g2d = bimg.createGraphics();
        return this.factory.createJ2DPrismGraphics(presentable, g2d);
    }

    Graphics2D createAWTGraphics2D() {
        return getBufferedImage().createGraphics();
    }

    @Override // com.sun.prism.RenderTarget
    public Screen getAssociatedScreen() {
        return this.factory.getScreen();
    }

    @Override // com.sun.prism.impl.BaseTexture, com.sun.prism.Texture
    public void update(Image img) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.impl.BaseTexture, com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.impl.BaseTexture, com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int w2, int h2) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.impl.BaseTexture, com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int srcw, int srch, boolean skipFlush) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.j2d.J2DTexture, com.sun.prism.Texture
    public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isOpaque() {
        return this.opaque;
    }

    @Override // com.sun.prism.RenderTarget
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    @Override // com.sun.prism.RTTexture
    public boolean isVolatile() {
        return false;
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isMSAA() {
        return false;
    }
}
