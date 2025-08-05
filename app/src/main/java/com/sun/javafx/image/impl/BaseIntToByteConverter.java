package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseIntToByteConverter.class */
public abstract class BaseIntToByteConverter implements IntToBytePixelConverter {
    protected final IntPixelGetter getter;
    protected final BytePixelSetter setter;
    protected final int nDstElems;

    abstract void doConvert(int[] iArr, int i2, int i3, byte[] bArr, int i4, int i5, int i6, int i7);

    abstract void doConvert(IntBuffer intBuffer, int i2, int i3, ByteBuffer byteBuffer, int i4, int i5, int i6, int i7);

    BaseIntToByteConverter(IntPixelGetter getter, BytePixelSetter setter) {
        this.getter = getter;
        this.setter = setter;
        this.nDstElems = setter.getNumElements();
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final IntPixelGetter getGetter() {
        return this.getter;
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final BytePixelSetter getSetter() {
        return this.setter;
    }

    @Override // com.sun.javafx.image.IntToBytePixelConverter
    public final void convert(int[] srcarr, int srcoff, int srcscanints, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        doConvert(srcarr, srcoff, srcscanints, dstarr, dstoff, dstscanbytes, w2, h2);
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final void convert(IntBuffer srcbuf, int srcoff, int srcscanints, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray() && dstbuf.hasArray()) {
            doConvert(srcbuf.array(), srcoff + srcbuf.arrayOffset(), srcscanints, dstbuf.array(), dstoff + dstbuf.arrayOffset(), dstscanbytes, w2, h2);
            return;
        }
        doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanbytes, w2, h2);
    }

    @Override // com.sun.javafx.image.IntToBytePixelConverter
    public final void convert(IntBuffer srcbuf, int srcoff, int srcscanints, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray()) {
            int[] srcarr = srcbuf.array();
            doConvert(srcarr, srcoff + srcbuf.arrayOffset(), srcscanints, dstarr, dstoff, dstscanbytes, w2, h2);
        } else {
            ByteBuffer dstbuf = ByteBuffer.wrap(dstarr);
            doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanbytes, w2, h2);
        }
    }

    @Override // com.sun.javafx.image.IntToBytePixelConverter
    public final void convert(int[] srcarr, int srcoff, int srcscanints, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (dstbuf.hasArray()) {
            byte[] dstarr = dstbuf.array();
            doConvert(srcarr, srcoff, srcscanints, dstarr, dstoff + dstbuf.arrayOffset(), dstscanbytes, w2, h2);
        } else {
            IntBuffer srcbuf = IntBuffer.wrap(srcarr);
            doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanbytes, w2, h2);
        }
    }
}
