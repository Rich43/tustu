package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/IntTo4ByteSameConverter.class */
class IntTo4ByteSameConverter extends BaseIntToByteConverter {
    IntTo4ByteSameConverter(IntPixelGetter getter, BytePixelSetter setter) {
        super(getter, setter);
    }

    @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
    void doConvert(int[] srcarr, int srcoff, int srcscanints, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
        int srcscanints2 = srcscanints - w2;
        int dstscanbytes2 = dstscanbytes - (w2 * 4);
        while (true) {
            h2--;
            if (h2 >= 0) {
                for (int x2 = 0; x2 < w2; x2++) {
                    int i2 = srcoff;
                    srcoff++;
                    int pixel = srcarr[i2];
                    int i3 = dstoff;
                    int dstoff2 = dstoff + 1;
                    dstarr[i3] = (byte) pixel;
                    int dstoff3 = dstoff2 + 1;
                    dstarr[dstoff2] = (byte) (pixel >> 8);
                    int dstoff4 = dstoff3 + 1;
                    dstarr[dstoff3] = (byte) (pixel >> 16);
                    dstoff = dstoff4 + 1;
                    dstarr[dstoff4] = (byte) (pixel >> 24);
                }
                srcoff += srcscanints2;
                dstoff += dstscanbytes2;
            } else {
                return;
            }
        }
    }

    @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
    void doConvert(IntBuffer srcbuf, int srcoff, int srcscanints, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
        int dstscanbytes2 = dstscanbytes - (w2 * 4);
        while (true) {
            h2--;
            if (h2 >= 0) {
                for (int x2 = 0; x2 < w2; x2++) {
                    int pixel = srcbuf.get(srcoff + x2);
                    dstbuf.put(dstoff, (byte) pixel);
                    dstbuf.put(dstoff + 1, (byte) (pixel >> 8));
                    dstbuf.put(dstoff + 2, (byte) (pixel >> 16));
                    dstbuf.put(dstoff + 3, (byte) (pixel >> 24));
                    dstoff += 4;
                }
                srcoff += srcscanints;
                dstoff += dstscanbytes2;
            } else {
                return;
            }
        }
    }
}
