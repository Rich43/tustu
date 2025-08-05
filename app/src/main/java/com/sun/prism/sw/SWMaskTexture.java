package com.sun.prism.sw;

import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWMaskTexture.class */
public class SWMaskTexture extends SWTexture {
    private byte[] data;

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void setLinearFiltering(boolean z2) {
        super.setLinearFiltering(z2);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ boolean getLinearFiltering() {
        return super.getLinearFiltering();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ Texture getSharedTexture(Texture.WrapMode wrapMode) {
        return super.getSharedTexture(wrapMode);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ boolean getUseMipmap() {
        return super.getUseMipmap();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ Texture.WrapMode getWrapMode() {
        return super.getWrapMode();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void update(Image image, int i2, int i3, int i4, int i5, boolean z2) {
        super.update(image, i2, i3, i4, i5, z2);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void update(Image image, int i2, int i3, int i4, int i5) {
        super.update(image, i2, i3, i4, i5);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void update(Image image, int i2, int i3) {
        super.update(image, i2, i3);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void update(Image image) {
        super.update(image);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void setLastImageSerial(int i2) {
        super.setLastImageSerial(i2);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getLastImageSerial() {
        return super.getLastImageSerial();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getMaxContentHeight() {
        return super.getMaxContentHeight();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getMaxContentWidth() {
        return super.getMaxContentWidth();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void setContentHeight(int i2) {
        super.setContentHeight(i2);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void setContentWidth(int i2) {
        super.setContentWidth(i2);
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getContentWidth() {
        return super.getContentWidth();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getContentY() {
        return super.getContentY();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getContentX() {
        return super.getContentX();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getPhysicalHeight() {
        return super.getPhysicalHeight();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getPhysicalWidth() {
        return super.getPhysicalWidth();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.GraphicsResource
    public /* bridge */ /* synthetic */ void dispose() {
        super.dispose();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ boolean isSurfaceLost() {
        return super.isSurfaceLost();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void contentsNotUseful() {
        super.contentsNotUseful();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void contentsUseful() {
        super.contentsUseful();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void makePermanent() {
        super.makePermanent();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void assertLocked() {
        super.assertLocked();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ int getLockCount() {
        return super.getLockCount();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ boolean isLocked() {
        return super.isLocked();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void unlock() {
        super.unlock();
    }

    @Override // com.sun.prism.sw.SWTexture, com.sun.prism.Texture
    public /* bridge */ /* synthetic */ void lock() {
        super.lock();
    }

    SWMaskTexture(SWResourceFactory factory, Texture.WrapMode wrapMode, int w2, int h2) {
        super(factory, wrapMode, w2, h2);
    }

    SWMaskTexture(SWMaskTexture sharedTex, Texture.WrapMode altMode) {
        super(sharedTex, altMode);
        this.data = sharedTex.data;
    }

    byte[] getDataNoClone() {
        return this.data;
    }

    @Override // com.sun.prism.Texture
    public PixelFormat getPixelFormat() {
        return PixelFormat.BYTE_ALPHA;
    }

    @Override // com.sun.prism.Texture
    public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        if (PrismSettings.debug) {
            System.out.println("MASK TEXTURE, Pixel format: " + ((Object) format) + ", buffer: " + ((Object) buffer));
            System.out.println("dstx:" + dstx + " dsty:" + dsty);
            System.out.println("srcx:" + srcx + " srcy:" + srcy + " srcw:" + srcw + " srch:" + srch + " srcscan: " + srcscan);
        }
        if (format != PixelFormat.BYTE_ALPHA) {
            throw new IllegalArgumentException("SWMaskTexture supports BYTE_ALPHA format only.");
        }
        checkAllocation(srcw, srch);
        this.physicalWidth = srcw;
        this.physicalHeight = srch;
        allocate();
        ByteBuffer bb2 = (ByteBuffer) buffer;
        for (int i2 = 0; i2 < srch; i2++) {
            bb2.position(((srcy + i2) * srcscan) + srcx);
            bb2.get(this.data, i2 * this.physicalWidth, srcw);
        }
    }

    @Override // com.sun.prism.Texture
    public void update(MediaFrame frame, boolean skipFlush) {
        throw new UnsupportedOperationException("update6:unimp");
    }

    void checkAllocation(int srcw, int srch) {
        if (this.allocated) {
            int nlen = srcw * srch;
            if (nlen > this.data.length) {
                throw new IllegalArgumentException("SRCW * SRCH exceeds buffer length");
            }
        }
    }

    @Override // com.sun.prism.sw.SWTexture
    void allocateBuffer() {
        this.data = new byte[this.physicalWidth * this.physicalHeight];
    }

    @Override // com.sun.prism.sw.SWTexture
    Texture createSharedLockedTexture(Texture.WrapMode altMode) {
        return new SWMaskTexture(this, altMode);
    }
}
