package com.sun.prism.sw;

import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWTexture.class */
abstract class SWTexture implements Texture {
    boolean allocated;
    int physicalWidth;
    int physicalHeight;
    int contentWidth;
    int contentHeight;
    private SWResourceFactory factory;
    private int lastImageSerial;
    private final Texture.WrapMode wrapMode;
    private boolean linearFiltering;
    private int lockcount;
    boolean permanent;
    int employcount;

    abstract void allocateBuffer();

    abstract Texture createSharedLockedTexture(Texture.WrapMode wrapMode);

    static Texture create(SWResourceFactory factory, PixelFormat formatHint, Texture.WrapMode wrapMode, int w2, int h2) {
        switch (formatHint) {
            case BYTE_ALPHA:
                return new SWMaskTexture(factory, wrapMode, w2, h2);
            default:
                return new SWArgbPreTexture(factory, wrapMode, w2, h2);
        }
    }

    SWTexture(SWResourceFactory factory, Texture.WrapMode wrapMode, int w2, int h2) {
        this.allocated = false;
        this.linearFiltering = true;
        this.factory = factory;
        this.wrapMode = wrapMode;
        this.physicalWidth = w2;
        this.physicalHeight = h2;
        this.contentWidth = w2;
        this.contentHeight = h2;
        lock();
    }

    SWTexture(SWTexture sharedTex, Texture.WrapMode altMode) {
        this.allocated = false;
        this.linearFiltering = true;
        this.allocated = sharedTex.allocated;
        this.physicalWidth = sharedTex.physicalWidth;
        this.physicalHeight = sharedTex.physicalHeight;
        this.contentWidth = sharedTex.contentWidth;
        this.contentHeight = sharedTex.contentHeight;
        this.factory = sharedTex.factory;
        this.lastImageSerial = sharedTex.lastImageSerial;
        this.linearFiltering = sharedTex.linearFiltering;
        this.wrapMode = altMode;
        lock();
    }

    SWResourceFactory getResourceFactory() {
        return this.factory;
    }

    int getOffset() {
        return 0;
    }

    @Override // com.sun.prism.Texture
    public void lock() {
        this.lockcount++;
    }

    @Override // com.sun.prism.Texture
    public void unlock() {
        assertLocked();
        this.lockcount--;
    }

    @Override // com.sun.prism.Texture
    public boolean isLocked() {
        return this.lockcount > 0;
    }

    @Override // com.sun.prism.Texture
    public int getLockCount() {
        return this.lockcount;
    }

    @Override // com.sun.prism.Texture
    public void assertLocked() {
        if (this.lockcount <= 0) {
            throw new IllegalStateException("texture not locked");
        }
    }

    @Override // com.sun.prism.Texture
    public void makePermanent() {
        this.permanent = true;
    }

    @Override // com.sun.prism.Texture
    public void contentsUseful() {
        assertLocked();
        this.employcount++;
    }

    @Override // com.sun.prism.Texture
    public void contentsNotUseful() {
        if (this.employcount <= 0) {
            throw new IllegalStateException("Resource obsoleted too many times");
        }
        this.employcount--;
    }

    @Override // com.sun.prism.Texture
    public boolean isSurfaceLost() {
        return false;
    }

    @Override // com.sun.prism.GraphicsResource
    public void dispose() {
    }

    @Override // com.sun.prism.Texture
    public int getPhysicalWidth() {
        return this.physicalWidth;
    }

    @Override // com.sun.prism.Texture
    public int getPhysicalHeight() {
        return this.physicalHeight;
    }

    @Override // com.sun.prism.Texture
    public int getContentX() {
        return 0;
    }

    @Override // com.sun.prism.Texture
    public int getContentY() {
        return 0;
    }

    @Override // com.sun.prism.Texture
    public int getContentWidth() {
        return this.contentWidth;
    }

    @Override // com.sun.prism.Texture
    public void setContentWidth(int contentWidth) {
        if (contentWidth > this.physicalWidth) {
            throw new IllegalArgumentException("contentWidth cannot exceed physicalWidth");
        }
        this.contentWidth = contentWidth;
    }

    @Override // com.sun.prism.Texture
    public int getContentHeight() {
        return this.contentHeight;
    }

    @Override // com.sun.prism.Texture
    public void setContentHeight(int contentHeight) {
        if (contentHeight > this.physicalHeight) {
            throw new IllegalArgumentException("contentHeight cannot exceed physicalHeight");
        }
        this.contentHeight = contentHeight;
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentWidth() {
        return getPhysicalWidth();
    }

    @Override // com.sun.prism.Texture
    public int getMaxContentHeight() {
        return getPhysicalHeight();
    }

    @Override // com.sun.prism.Texture
    public int getLastImageSerial() {
        return this.lastImageSerial;
    }

    @Override // com.sun.prism.Texture
    public void setLastImageSerial(int serial) {
        this.lastImageSerial = serial;
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
    public void update(Image img, int dstx, int dsty, int srcw, int srch) {
        update(img, dstx, dsty, srcw, srch, false);
    }

    @Override // com.sun.prism.Texture
    public void update(Image img, int dstx, int dsty, int srcw, int srch, boolean skipFlush) {
        if (PrismSettings.debug) {
            System.out.println("IMG.Bytes per pixel: " + img.getBytesPerPixelUnit());
            System.out.println("IMG.scanline: " + img.getScanlineStride());
        }
        update(img.getPixelBuffer(), img.getPixelFormat(), dstx, dsty, 0, 0, srcw, srch, img.getScanlineStride(), skipFlush);
    }

    @Override // com.sun.prism.Texture
    public Texture.WrapMode getWrapMode() {
        return this.wrapMode;
    }

    @Override // com.sun.prism.Texture
    public boolean getUseMipmap() {
        return false;
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
        return createSharedLockedTexture(altMode);
    }

    @Override // com.sun.prism.Texture
    public boolean getLinearFiltering() {
        return this.linearFiltering;
    }

    @Override // com.sun.prism.Texture
    public void setLinearFiltering(boolean linear) {
        this.linearFiltering = linear;
    }

    void allocate() {
        if (this.allocated) {
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("PCS Texture allocating buffer: " + ((Object) this) + ", " + this.physicalWidth + LanguageTag.PRIVATEUSE + this.physicalHeight);
        }
        allocateBuffer();
        this.allocated = true;
    }
}
