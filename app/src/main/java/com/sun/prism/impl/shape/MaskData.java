package com.sun.prism.impl.shape;

import com.sun.prism.Texture;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/MaskData.class */
public class MaskData {
    private ByteBuffer maskBuffer;
    private int originX;
    private int originY;
    private int width;
    private int height;

    public ByteBuffer getMaskBuffer() {
        return this.maskBuffer;
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void uploadToTexture(Texture tex, int dstx, int dsty, boolean skipFlush) {
        int scan = this.width * tex.getPixelFormat().getBytesPerPixelUnit();
        tex.update(this.maskBuffer, tex.getPixelFormat(), dstx, dsty, 0, 0, this.width, this.height, scan, skipFlush);
    }

    public void update(ByteBuffer maskBuffer, int originX, int originY, int width, int height) {
        this.maskBuffer = maskBuffer;
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
    }

    public static MaskData create(byte[] pixels, int originX, int originY, int width, int height) {
        MaskData maskData = new MaskData();
        maskData.update(ByteBuffer.wrap(pixels), originX, originY, width, height);
        return maskData;
    }
}
