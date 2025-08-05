package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseByteToByteConverter.class */
abstract class BaseByteToByteConverter implements ByteToBytePixelConverter {
    protected final BytePixelGetter getter;
    protected final BytePixelSetter setter;
    protected final int nSrcElems;
    protected final int nDstElems;

    abstract void doConvert(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, int i6, int i7);

    abstract void doConvert(ByteBuffer byteBuffer, int i2, int i3, ByteBuffer byteBuffer2, int i4, int i5, int i6, int i7);

    BaseByteToByteConverter(BytePixelGetter getter, BytePixelSetter setter) {
        this.getter = getter;
        this.setter = setter;
        this.nSrcElems = getter.getNumElements();
        this.nDstElems = setter.getNumElements();
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final BytePixelGetter getGetter() {
        return this.getter;
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final BytePixelSetter getSetter() {
        return this.setter;
    }

    @Override // com.sun.javafx.image.ByteToBytePixelConverter
    public final void convert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        doConvert(srcarr, srcoff, srcscanbytes, dstarr, dstoff, dstscanbytes, w2, h2);
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final void convert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray() && dstbuf.hasArray()) {
            doConvert(srcbuf.array(), srcoff + srcbuf.arrayOffset(), srcscanbytes, dstbuf.array(), dstoff + dstbuf.arrayOffset(), dstscanbytes, w2, h2);
            return;
        }
        doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanbytes, w2, h2);
    }

    @Override // com.sun.javafx.image.ByteToBytePixelConverter
    public final void convert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray()) {
            byte[] srcarr = srcbuf.array();
            doConvert(srcarr, srcoff + srcbuf.arrayOffset(), srcscanbytes, dstarr, dstoff, dstscanbytes, w2, h2);
        } else {
            ByteBuffer dstbuf = ByteBuffer.wrap(dstarr);
            doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanbytes, w2, h2);
        }
    }

    @Override // com.sun.javafx.image.ByteToBytePixelConverter
    public final void convert(byte[] srcarr, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanbytes == w2 * this.nSrcElems && dstscanbytes == w2 * this.nDstElems) {
            w2 *= h2;
            h2 = 1;
        }
        if (dstbuf.hasArray()) {
            byte[] dstarr = dstbuf.array();
            doConvert(srcarr, srcoff, srcscanbytes, dstarr, dstoff + dstbuf.arrayOffset(), dstscanbytes, w2, h2);
        } else {
            ByteBuffer srcbuf = ByteBuffer.wrap(srcarr);
            doConvert(srcbuf, srcoff, srcscanbytes, dstbuf, dstoff, dstscanbytes, w2, h2);
        }
    }

    static ByteToBytePixelConverter create(BytePixelAccessor fmt) {
        return new ByteAnyToSameConverter(fmt);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseByteToByteConverter$ByteAnyToSameConverter.class */
    static class ByteAnyToSameConverter extends BaseByteToByteConverter {
        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelSetter getSetter() {
            return super.getSetter();
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelGetter getGetter() {
            return super.getGetter();
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ void convert(Buffer buffer, int i2, int i3, Buffer buffer2, int i4, int i5, int i6, int i7) {
            super.convert((ByteBuffer) buffer, i2, i3, (ByteBuffer) buffer2, i4, i5, i6, i7);
        }

        ByteAnyToSameConverter(BytePixelAccessor fmt) {
            super(fmt, fmt);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            while (true) {
                h2--;
                if (h2 >= 0) {
                    System.arraycopy(srcarr, srcoff, dstarr, dstoff, w2 * this.nSrcElems);
                    srcoff += srcscanbytes;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srclimit = srcbuf.limit();
            int origsrcpos = srcbuf.position();
            int origdstpos = dstbuf.position();
            while (true) {
                try {
                    h2--;
                    if (h2 >= 0) {
                        int newlimit = srcoff + (w2 * this.nSrcElems);
                        if (newlimit > srclimit) {
                            throw new IndexOutOfBoundsException("" + srclimit);
                        }
                        srcbuf.limit(newlimit);
                        srcbuf.position(srcoff);
                        dstbuf.position(dstoff);
                        dstbuf.put(srcbuf);
                        srcoff += srcscanbytes;
                        dstoff += dstscanbytes;
                    } else {
                        return;
                    }
                } finally {
                    srcbuf.limit(srclimit);
                    srcbuf.position(origsrcpos);
                    dstbuf.position(origdstpos);
                }
            }
        }
    }

    public static ByteToBytePixelConverter createReorderer(BytePixelGetter getter, BytePixelSetter setter, int c0, int c1, int c2, int c3) {
        return new FourByteReorderer(getter, setter, c0, c1, c2, c3);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseByteToByteConverter$FourByteReorderer.class */
    static class FourByteReorderer extends BaseByteToByteConverter {
        private final int c0;
        private final int c1;
        private final int c2;
        private final int c3;

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelSetter getSetter() {
            return super.getSetter();
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelGetter getGetter() {
            return super.getGetter();
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ void convert(Buffer buffer, int i2, int i3, Buffer buffer2, int i4, int i5, int i6, int i7) {
            super.convert((ByteBuffer) buffer, i2, i3, (ByteBuffer) buffer2, i4, i5, i6, i7);
        }

        FourByteReorderer(BytePixelGetter getter, BytePixelSetter setter, int c0, int c1, int c2, int c3) {
            super(getter, setter);
            this.c0 = c0;
            this.c1 = c1;
            this.c2 = c2;
            this.c3 = c3;
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        byte b0 = srcarr[srcoff + this.c0];
                        byte b1 = srcarr[srcoff + this.c1];
                        byte b2 = srcarr[srcoff + this.c2];
                        byte b3 = srcarr[srcoff + this.c3];
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = b0;
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = b1;
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = b2;
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = b3;
                        srcoff += 4;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 4);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        byte b0 = srcbuf.get(srcoff + this.c0);
                        byte b1 = srcbuf.get(srcoff + this.c1);
                        byte b2 = srcbuf.get(srcoff + this.c2);
                        byte b3 = srcbuf.get(srcoff + this.c3);
                        dstbuf.put(dstoff, b0);
                        dstbuf.put(dstoff + 1, b1);
                        dstbuf.put(dstoff + 2, b2);
                        dstbuf.put(dstoff + 3, b3);
                        srcoff += 4;
                        dstoff += 4;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }
}
