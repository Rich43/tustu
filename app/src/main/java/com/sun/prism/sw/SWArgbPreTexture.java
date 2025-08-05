package com.sun.prism.sw;

import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWArgbPreTexture.class */
class SWArgbPreTexture extends SWTexture {
    private int[] data;
    private int offset;
    private boolean hasAlpha;

    SWArgbPreTexture(SWResourceFactory factory, Texture.WrapMode wrapMode, int w2, int h2) {
        super(factory, wrapMode, w2, h2);
        this.hasAlpha = true;
        this.offset = 0;
    }

    SWArgbPreTexture(SWArgbPreTexture sharedTex, Texture.WrapMode altMode) {
        super(sharedTex, altMode);
        this.hasAlpha = true;
        this.data = sharedTex.data;
        this.offset = sharedTex.offset;
        this.hasAlpha = sharedTex.hasAlpha;
    }

    int[] getDataNoClone() {
        return this.data;
    }

    @Override // com.sun.prism.sw.SWTexture
    int getOffset() {
        return this.offset;
    }

    boolean hasAlpha() {
        return this.hasAlpha;
    }

    @Override // com.sun.prism.Texture
    public PixelFormat getPixelFormat() {
        return PixelFormat.INT_ARGB_PRE;
    }

    @Override // com.sun.prism.Texture
    public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy, int srcw, int srch, int srcscan, boolean skipFlush) {
        PixelGetter getter;
        if (PrismSettings.debug) {
            System.out.println("ARGB_PRE TEXTURE, Pixel format: " + ((Object) format) + ", buffer: " + ((Object) buffer));
            System.out.println("dstx:" + dstx + " dsty:" + dsty);
            System.out.println("srcx:" + srcx + " srcy:" + srcy + " srcw:" + srcw + " srch:" + srch + " srcscan: " + srcscan);
        }
        checkDimensions(dstx + srcw, dsty + srch);
        allocate();
        switch (format) {
            case BYTE_RGB:
                getter = ByteRgb.getter;
                this.hasAlpha = false;
                break;
            case INT_ARGB_PRE:
                getter = IntArgbPre.getter;
                srcscan >>= 2;
                this.hasAlpha = true;
                break;
            case BYTE_BGRA_PRE:
                getter = ByteBgraPre.getter;
                this.hasAlpha = true;
                break;
            case BYTE_GRAY:
                getter = ByteGray.getter;
                this.hasAlpha = false;
                break;
            default:
                throw new UnsupportedOperationException("!!! UNSUPPORTED PIXEL FORMAT: " + ((Object) format));
        }
        PixelConverter converter = PixelUtils.getConverter(getter, IntArgbPre.setter);
        buffer.position(0);
        converter.convert(buffer, (srcy * srcscan) + srcx, srcscan, IntBuffer.wrap(this.data), (dsty * this.physicalWidth) + dstx, this.physicalWidth, srcw, srch);
    }

    @Override // com.sun.prism.Texture
    public void update(MediaFrame frame, boolean skipFlush) {
        if (PrismSettings.debug) {
            System.out.println("Media Pixel format: " + ((Object) frame.getPixelFormat()));
        }
        frame.holdFrame();
        if (frame.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
            MediaFrame f2 = frame.convertToFormat(PixelFormat.INT_ARGB_PRE);
            frame.releaseFrame();
            frame = f2;
        }
        int stride = frame.strideForPlane(0) / 4;
        IntBuffer ib = frame.getBufferForPlane(0).asIntBuffer();
        if (ib.hasArray()) {
            this.allocated = false;
            this.offset = 0;
            this.physicalWidth = stride;
            this.data = ib.array();
        } else {
            allocate();
            for (int i2 = 0; i2 < this.contentHeight; i2++) {
                ib.position(this.offset + (i2 * stride));
                ib.get(this.data, i2 * this.physicalWidth, this.contentWidth);
            }
        }
        frame.releaseFrame();
    }

    void checkDimensions(int srcw, int srch) {
        if (srcw < 0) {
            throw new IllegalArgumentException("srcw must be >=0");
        }
        if (srch < 0) {
            throw new IllegalArgumentException("srch must be >=0");
        }
        if (srcw > this.physicalWidth) {
            throw new IllegalArgumentException("srcw exceeds WIDTH");
        }
        if (srch > this.physicalHeight) {
            throw new IllegalArgumentException("srch exceeds HEIGHT");
        }
    }

    void applyCompositeAlpha(float alpha) {
        if (this.allocated) {
            this.hasAlpha = this.hasAlpha || alpha < 1.0f;
            for (int i2 = 0; i2 < this.data.length; i2++) {
                int finalAlpha = ((int) (((this.data[i2] >> 24) * alpha) + 0.5f)) & 255;
                this.data[i2] = (finalAlpha << 24) | (this.data[i2] & 16777215);
            }
            return;
        }
        throw new IllegalStateException("Cannot apply composite alpha to texture with non-allocated data");
    }

    @Override // com.sun.prism.sw.SWTexture
    void allocateBuffer() {
        this.data = new int[this.physicalWidth * this.physicalHeight];
    }

    @Override // com.sun.prism.sw.SWTexture
    Texture createSharedLockedTexture(Texture.WrapMode altMode) {
        return new SWArgbPreTexture(this, altMode);
    }
}
