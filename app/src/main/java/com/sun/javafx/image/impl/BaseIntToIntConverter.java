package com.sun.javafx.image.impl;

import com.sun.javafx.image.IntPixelAccessor;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import java.nio.Buffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseIntToIntConverter.class */
public abstract class BaseIntToIntConverter implements IntToIntPixelConverter {
    protected final IntPixelGetter getter;
    protected final IntPixelSetter setter;

    abstract void doConvert(int[] iArr, int i2, int i3, int[] iArr2, int i4, int i5, int i6, int i7);

    abstract void doConvert(IntBuffer intBuffer, int i2, int i3, IntBuffer intBuffer2, int i4, int i5, int i6, int i7);

    public BaseIntToIntConverter(IntPixelGetter getter, IntPixelSetter setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final IntPixelGetter getGetter() {
        return this.getter;
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final IntPixelSetter getSetter() {
        return this.setter;
    }

    @Override // com.sun.javafx.image.IntToIntPixelConverter
    public final void convert(int[] srcarr, int srcoff, int srcscanints, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        doConvert(srcarr, srcoff, srcscanints, dstarr, dstoff, dstscanints, w2, h2);
    }

    @Override // com.sun.javafx.image.PixelConverter
    public final void convert(IntBuffer srcbuf, int srcoff, int srcscanints, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray() && dstbuf.hasArray()) {
            doConvert(srcbuf.array(), srcoff + srcbuf.arrayOffset(), srcscanints, dstbuf.array(), dstoff + dstbuf.arrayOffset(), dstscanints, w2, h2);
            return;
        }
        doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanints, w2, h2);
    }

    @Override // com.sun.javafx.image.IntToIntPixelConverter
    public final void convert(IntBuffer srcbuf, int srcoff, int srcscanints, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (srcbuf.hasArray()) {
            int[] srcarr = srcbuf.array();
            doConvert(srcarr, srcoff + srcbuf.arrayOffset(), srcscanints, dstarr, dstoff, dstscanints, w2, h2);
        } else {
            IntBuffer dstbuf = IntBuffer.wrap(dstarr);
            doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanints, w2, h2);
        }
    }

    @Override // com.sun.javafx.image.IntToIntPixelConverter
    public final void convert(int[] srcarr, int srcoff, int srcscanints, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            return;
        }
        if (srcscanints == w2 && dstscanints == w2) {
            w2 *= h2;
            h2 = 1;
        }
        if (dstbuf.hasArray()) {
            int[] dstarr = dstbuf.array();
            doConvert(srcarr, srcoff, srcscanints, dstarr, dstoff + dstbuf.arrayOffset(), dstscanints, w2, h2);
        } else {
            IntBuffer srcbuf = IntBuffer.wrap(srcarr);
            doConvert(srcbuf, srcoff, srcscanints, dstbuf, dstoff, dstscanints, w2, h2);
        }
    }

    static IntToIntPixelConverter create(IntPixelAccessor fmt) {
        return new IntAnyToSameConverter(fmt);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/BaseIntToIntConverter$IntAnyToSameConverter.class */
    static class IntAnyToSameConverter extends BaseIntToIntConverter {
        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelSetter getSetter() {
            return super.getSetter();
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ PixelGetter getGetter() {
            return super.getGetter();
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter, com.sun.javafx.image.PixelConverter
        public /* bridge */ /* synthetic */ void convert(Buffer buffer, int i2, int i3, Buffer buffer2, int i4, int i5, int i6, int i7) {
            super.convert((IntBuffer) buffer, i2, i3, (IntBuffer) buffer2, i4, i5, i6, i7);
        }

        IntAnyToSameConverter(IntPixelAccessor fmt) {
            super(fmt, fmt);
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(int[] srcarr, int srcoff, int srcscanints, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            while (true) {
                h2--;
                if (h2 >= 0) {
                    System.arraycopy(srcarr, srcoff, dstarr, dstoff, w2);
                    srcoff += srcscanints;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(IntBuffer srcbuf, int srcoff, int srcscanints, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            int srclimit = srcbuf.limit();
            int origsrcpos = srcbuf.position();
            int origdstpos = dstbuf.position();
            while (true) {
                try {
                    h2--;
                    if (h2 >= 0) {
                        int newlimit = srcoff + w2;
                        if (newlimit > srclimit) {
                            throw new IndexOutOfBoundsException("" + srclimit);
                        }
                        srcbuf.limit(newlimit);
                        srcbuf.position(srcoff);
                        dstbuf.position(dstoff);
                        dstbuf.put(srcbuf);
                        srcoff += srcscanints;
                        dstoff += dstscanints;
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
}
