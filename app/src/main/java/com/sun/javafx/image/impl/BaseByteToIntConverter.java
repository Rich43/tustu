package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseByteToIntConverter.class */
public abstract class BaseByteToIntConverter implements ByteToIntPixelConverter {
    protected final BytePixelGetter getter;
    protected final IntPixelSetter setter;
    protected final int nSrcElems;

    abstract void doConvert(byte[] bArr, int i2, int i3, int[] iArr, int i4, int i5, int i6, int i7);

    abstract void doConvert(ByteBuffer byteBuffer, int i2, int i3, IntBuffer intBuffer, int i4, int i5, int i6, int i7);

    BaseByteToIntConverter(BytePixelGetter getter, IntPixelSetter setter) {
        this.getter = getter;
        this.setter = setter;
        this.nSrcElems = getter.getNumElements();
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    @Override // com.sun.javafx.image.ByteToIntPixelConverter
    public final void convert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        doConvert(srcarr, srcoff, srcscanbytes, dstarr, dstoff, dstscanints, w2, h2);
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final void convert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray() && dstbuf.hasArray()) {
            doConvert(srcbuf.array(), srcoff + srcbuf.arrayOffset(), srcscanbytes, dstbuf.array(), dstoff + dstbuf.arrayOffset(), dstscanints, w2, h2);
            return;
        }
        doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanints, w2, h2);
    }

    @Override // com.sun.javafx.image.ByteToIntPixelConverter
    public final void convert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray()) {
            byte[] srcarr = srcbuf.array();
            doConvert(srcarr, srcoff + srcbuf.arrayOffset(), srcscanbytes, dstarr, dstoff, dstscanints, w2, h2);
        } else {
            IntBuffer dstbuf = IntBuffer.wrap(dstarr);
            doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanints, w2, h2);
        }
    }

    @Override // com.sun.javafx.image.ByteToIntPixelConverter
    public final void convert(byte[] srcarr, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (dstbuf.hasArray()) {
            int[] dstarr = dstbuf.array();
            doConvert(srcarr, srcoff, srcscanbytes, dstarr, dstoff + dstbuf.arrayOffset(), dstscanints, w2, h2);
        } else {
            ByteBuffer srcbuf = ByteBuffer.wrap(srcarr);
            doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanints, w2, h2);
        }
    }
}
