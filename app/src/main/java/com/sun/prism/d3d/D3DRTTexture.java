package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DRTTexture.class */
class D3DRTTexture extends D3DTexture implements D3DRenderTarget, RTTexture, ReadbackRenderTarget {
    private boolean opaque;

    D3DRTTexture(D3DContext context, Texture.WrapMode wrapMode, long pResource, int physicalWidth, int physicalHeight, int contentWidth, int contentHeight) {
        super(context, PixelFormat.INT_ARGB_PRE, wrapMode, pResource, physicalWidth, physicalHeight, contentWidth, contentHeight, true);
        this.opaque = false;
    }

    D3DRTTexture(D3DContext context, Texture.WrapMode wrapMode, long pResource, int physicalWidth, int physicalHeight, int contentX, int contentY, int contentWidth, int contentHeight, int samples) {
        super(context, PixelFormat.INT_ARGB_PRE, wrapMode, pResource, physicalWidth, physicalHeight, contentX, contentY, contentWidth, contentHeight, true, samples, false);
        this.opaque = false;
    }

    @Override // com.sun.prism.ReadbackRenderTarget
    public Texture getBackBuffer() {
        return this;
    }

    @Override // com.sun.prism.d3d.D3DRenderTarget
    public long getResourceHandle() {
        return ((D3DTextureResource) this.resource).getResource().getResource();
    }

    @Override // com.sun.prism.RenderTarget
    public Graphics createGraphics() {
        return D3DGraphics.create(this, getContext());
    }

    @Override // com.sun.prism.RTTexture
    public int[] getPixels() {
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
        int res;
        D3DContext context = getContext();
        if (context.isDisposed()) {
            return false;
        }
        context.flushVertexBuffer();
        long ctx = getContext().getContextHandle();
        if (pixels instanceof ByteBuffer) {
            ByteBuffer buf = (ByteBuffer) pixels;
            byte[] arr = buf.hasArray() ? buf.array() : null;
            long length = buf.capacity();
            res = D3DResourceFactory.nReadPixelsB(ctx, getNativeSourceHandle(), length, pixels, arr, getContentWidth(), getContentHeight());
        } else if (pixels instanceof IntBuffer) {
            IntBuffer buf2 = (IntBuffer) pixels;
            int[] arr2 = buf2.hasArray() ? buf2.array() : null;
            long length2 = buf2.capacity() * 4;
            res = D3DResourceFactory.nReadPixelsI(ctx, getNativeSourceHandle(), length2, pixels, arr2, getContentWidth(), getContentHeight());
        } else {
            throw new IllegalArgumentException("Buffer of this type is not supported: " + ((Object) pixels));
        }
        return context.validatePresent(res);
    }

    @Override // com.sun.prism.RenderTarget
    public Screen getAssociatedScreen() {
        return getContext().getAssociatedScreen();
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
    public void update(Image img, int dstx, int dsty, int w2, int h2, boolean skipFlush) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.d3d.D3DTexture, com.sun.prism.Texture
    public void update(Buffer pixels, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override // com.sun.prism.RenderTarget
    public void setOpaque(boolean isOpaque) {
        this.opaque = isOpaque;
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isOpaque() {
        return this.opaque;
    }

    @Override // com.sun.prism.RTTexture
    public boolean isVolatile() {
        return getContext().isRTTVolatile();
    }

    @Override // com.sun.prism.RenderTarget
    public boolean isMSAA() {
        return ((D3DTextureResource) this.resource).getResource().getSamples() != 0;
    }
}
