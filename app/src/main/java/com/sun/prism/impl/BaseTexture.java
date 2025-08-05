package com.sun.prism.impl;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.ManagedResource;
import java.nio.Buffer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/BaseTexture.class */
public abstract class BaseTexture<T extends ManagedResource> implements Texture {
    protected final T resource;
    private final PixelFormat format;
    private final int physicalWidth;
    private final int physicalHeight;
    private final int contentX;
    private final int contentY;
    protected int contentWidth;
    protected int contentHeight;
    private final int maxContentWidth;
    private final int maxContentHeight;
    private final Texture.WrapMode wrapMode;
    private final boolean useMipmap;
    private boolean linearFiltering;
    private int lastImageSerial;

    protected abstract Texture createSharedTexture(Texture.WrapMode wrapMode);

    protected BaseTexture(BaseTexture<T> sharedTex, Texture.WrapMode newMode, boolean useMipmap) {
        this.linearFiltering = true;
        this.resource = sharedTex.resource;
        this.format = sharedTex.format;
        this.wrapMode = newMode;
        this.physicalWidth = sharedTex.physicalWidth;
        this.physicalHeight = sharedTex.physicalHeight;
        this.contentX = sharedTex.contentX;
        this.contentY = sharedTex.contentY;
        this.contentWidth = sharedTex.contentWidth;
        this.contentHeight = sharedTex.contentHeight;
        this.maxContentWidth = sharedTex.maxContentWidth;
        this.maxContentHeight = sharedTex.maxContentHeight;
        this.useMipmap = useMipmap;
    }

    protected BaseTexture(T resource, PixelFormat format, Texture.WrapMode wrapMode, int width, int height) {
        this(resource, format, wrapMode, width, height, 0, 0, width, height, false);
    }

    protected BaseTexture(T resource, PixelFormat format, Texture.WrapMode wrapMode, int physicalWidth, int physicalHeight, int contentX, int contentY, int contentWidth, int contentHeight, boolean useMipmap) {
        this.linearFiltering = true;
        this.resource = resource;
        this.format = format;
        this.wrapMode = wrapMode;
        this.physicalWidth = physicalWidth;
        this.physicalHeight = physicalHeight;
        this.contentX = contentX;
        this.contentY = contentY;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        this.maxContentWidth = physicalWidth;
        this.maxContentHeight = physicalHeight;
        this.useMipmap = useMipmap;
    }

    protected BaseTexture(T resource, PixelFormat format, Texture.WrapMode wrapMode, int physicalWidth, int physicalHeight, int contentX, int contentY, int contentWidth, int contentHeight, int maxContentWidth, int maxContentHeight, boolean useMipmap) {
        this.linearFiltering = true;
        this.resource = resource;
        this.format = format;
        this.wrapMode = wrapMode;
        this.physicalWidth = physicalWidth;
        this.physicalHeight = physicalHeight;
        this.contentX = contentX;
        this.contentY = contentY;
        this.contentWidth = contentWidth;
        this.contentHeight = contentHeight;
        this.maxContentWidth = maxContentWidth;
        this.maxContentHeight = maxContentHeight;
        this.useMipmap = useMipmap;
    }

    @Override // com.sun.prism.Texture
    public final PixelFormat getPixelFormat() {
        return this.format;
    }

    @Override // com.sun.prism.Texture
    public final int getPhysicalWidth() {
        return this.physicalWidth;
    }

    @Override // com.sun.prism.Texture
    public final int getPhysicalHeight() {
        return this.physicalHeight;
    }

    @Override // com.sun.prism.Texture
    public final int getContentX() {
        return this.contentX;
    }

    @Override // com.sun.prism.Texture
    public final int getContentY() {
        return this.contentY;
    }

    @Override // com.sun.prism.Texture
    public final int getContentWidth() {
        return this.contentWidth;
    }

    @Override // com.sun.prism.Texture
    public final int getContentHeight() {
        return this.contentHeight;
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentWidth() {
        return this.maxContentWidth;
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentHeight() {
        return this.maxContentHeight;
    }

    @Override // com.sun.prism.Texture
    public void setContentWidth(int contentW) {
        if (contentW > this.maxContentWidth) {
            throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentWidth");
        }
        this.contentWidth = contentW;
    }

    @Override // com.sun.prism.Texture
    public void setContentHeight(int contentH) {
        if (contentH > this.maxContentHeight) {
            throw new IllegalArgumentException("ContentWidth must be less than or equal to maxContentHeight");
        }
        this.contentHeight = contentH;
    }

    @Override // com.sun.prism.Texture
    public final Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override // com.sun.prism.Texture
    public boolean getUseMipmap() {
        return this.useMipmap;
    }

    @Override // com.sun.prism.Texture
    public Texture getSharedTexture(Texture.WrapMode altMode) {
        assertLocked();
        if (this.wrapMode == altMode) {
            lock();
            return this;
        }
        switch (altMode) {
            case REPEAT:
                if (this.wrapMode != Texture.WrapMode.CLAMP_TO_EDGE) {
                    return null;
                }
                break;
            case CLAMP_TO_EDGE:
                if (this.wrapMode != Texture.WrapMode.REPEAT) {
                    return null;
                }
                break;
            default:
                return null;
        }
        Texture altTex = createSharedTexture(altMode);
        altTex.lock();
        return altTex;
    }

    @Override // com.sun.prism.Texture
    public final boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override // com.sun.prism.Texture
    public void setLinearFiltering(boolean linear) {
        this.linearFiltering = linear;
    }

    @Override // com.sun.prism.Texture
    public final int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override // com.sun.prism.Texture
    public final void setLastImageSerial(int serial) {
        this.lastImageSerial = serial;
    }

    @Override // com.sun.prism.Texture
    public final void lock() {
        this.resource.lock();
    }

    @Override // com.sun.prism.Texture
    public final boolean isLocked() {
        return this.resource.isLocked();
    }

    @Override // com.sun.prism.Texture
    public final int getLockCount() {
        return this.resource.getLockCount();
    }

    @Override // com.sun.prism.Texture
    public final void assertLocked() {
        this.resource.assertLocked();
    }

    @Override // com.sun.prism.Texture
    public final void unlock() {
        this.resource.unlock();
    }

    @Override // com.sun.prism.Texture
    public final void makePermanent() {
        this.resource.makePermanent();
    }

    @Override // com.sun.prism.Texture
    public final void contentsUseful() {
        this.resource.contentsUseful();
    }

    @Override // com.sun.prism.Texture
    public final void contentsNotUseful() {
        this.resource.contentsNotUseful();
    }

    @Override // com.sun.prism.Texture
    public final boolean isSurfaceLost() {
        return !this.resource.isValid();
    }

    @Override // com.sun.prism.GraphicsResource
    public final void dispose() {
        this.resource.dispose();
    }

    @Override // com.sun.prism.Texture
    public void update(Image img) {
        update(img, 0, 0);
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty) {
        update(img, dstx, dsty, img.getWidth(), img.getHeight());
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int w2, int h2) {
        update(img, dstx, dsty, w2, h2, false);
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int srcw, int srch, boolean skipFlush) {
        Buffer pbuffer = img.getPixelBuffer();
        int pos = pbuffer.position();
        update(pbuffer, img.getPixelFormat(), dstx, dsty, img.getMinX(), img.getMinY(), srcw, srch, img.getScanlineStride(), skipFlush);
        pbuffer.position(pos);
    }

    protected void checkUpdateParams(Buffer buf, PixelFormat fmt, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan) {
        if (this.format == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("MULTI_YCbCr_420 requires multitexturing");
        }
        if (buf == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
        }
        if (fmt != this.format) {
            throw new IllegalArgumentException("Image format (" + ((Object) fmt) + ") must match texture format (" + ((Object) this.format) + ")");
        }
        if (dstx < 0 || dsty < 0) {
            throw new IllegalArgumentException("dstx (" + dstx + ") and dsty (" + dsty + ") must be >= 0");
        }
        if (srcx < 0 || srcy < 0) {
            throw new IllegalArgumentException("srcx (" + srcx + ") and srcy (" + srcy + ") must be >= 0");
        }
        if (srcw <= 0 || srch <= 0) {
            throw new IllegalArgumentException("srcw (" + srcw + ") and srch (" + srch + ") must be > 0");
        }
        if (srcscan >= Integer.MAX_VALUE / srch) {
            throw new IllegalArgumentException("srcscan * srch (" + srcscan + " * " + srch + ") must be < Integer.MAX_VALUE (2147483647)");
        }
        int bytesPerPixel = fmt.getBytesPerPixelUnit();
        if (srcscan % bytesPerPixel != 0) {
            throw new IllegalArgumentException("srcscan (" + srcscan + ") must be a multiple of the pixel stride (" + bytesPerPixel + ")");
        }
        if (srcw > srcscan / bytesPerPixel) {
            throw new IllegalArgumentException("srcw (" + srcw + ") must be <= srcscan/bytesPerPixel (" + (srcscan / bytesPerPixel) + ")");
        }
        if (dstx + srcw > this.contentWidth || dsty + srch > this.contentHeight) {
            throw new IllegalArgumentException("Destination region (x=" + dstx + ", y=" + dsty + ", w=" + srcw + ", h=" + srch + ") must fit within texture content bounds (contentWidth=" + this.contentWidth + ", contentHeight=" + this.contentHeight + ")");
        }
        int bytesNeeded = (srcx * bytesPerPixel) + (srcy * srcscan) + ((srch - 1) * srcscan) + (srcw * bytesPerPixel);
        int elemsNeeded = bytesNeeded / this.format.getDataType().getSizeInBytes();
        if (elemsNeeded < 0 || elemsNeeded > buf.remaining()) {
            throw new IllegalArgumentException("Upload requires " + elemsNeeded + " elements, but only " + buf.remaining() + " elements remain in the buffer");
        }
    }

    public String toString() {
        return super.toString() + " [format=" + ((Object) this.format) + " physicalWidth=" + this.physicalWidth + " physicalHeight=" + this.physicalHeight + " contentX=" + this.contentX + " contentY=" + this.contentY + " contentWidth=" + this.contentWidth + " contentHeight=" + this.contentHeight + " wrapMode=" + ((Object) this.wrapMode) + " linearFiltering=" + this.linearFiltering + "]";
    }
}
