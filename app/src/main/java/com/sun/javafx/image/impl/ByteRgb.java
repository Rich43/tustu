package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelSetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb.class */
public class ByteRgb {
    public static final BytePixelGetter getter = Getter.instance;

    public static ByteToBytePixelConverter ToByteBgraConverter() {
        return ToByteBgrfConv.nonpremult;
    }

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ToByteBgrfConv.premult;
    }

    public static ByteToIntPixelConverter ToIntArgbConverter() {
        return ToIntFrgbConv.nonpremult;
    }

    public static ByteToIntPixelConverter ToIntArgbPreConverter() {
        return ToIntFrgbConv.premult;
    }

    public static ByteToBytePixelConverter ToByteArgbConverter() {
        return ToByteFrgbConv.nonpremult;
    }

    public static final ByteToBytePixelConverter ToByteBgrConverter() {
        return SwapThreeByteConverter.rgbToBgrInstance;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb$Getter.class */
    static class Getter implements BytePixelGetter {
        static final BytePixelGetter instance = new Getter();

        private Getter() {
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public AlphaType getAlphaType() {
            return AlphaType.OPAQUE;
        }

        @Override // com.sun.javafx.image.PixelGetter, com.sun.javafx.image.PixelSetter
        public int getNumElements() {
            return 3;
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgb(byte[] arr, int offset) {
            return (arr[offset + 2] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset] & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.BytePixelGetter
        public int getArgbPre(byte[] arr, int offset) {
            return (arr[offset + 2] & 255) | ((arr[offset + 1] & 255) << 8) | ((arr[offset] & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgb(ByteBuffer buffer, int offset) {
            return (buffer.get(offset + 2) & 255) | ((buffer.get(offset + 1) & 255) << 8) | ((buffer.get(offset) & 255) << 16) | (-16777216);
        }

        @Override // com.sun.javafx.image.PixelGetter
        public int getArgbPre(ByteBuffer buffer, int offset) {
            return (buffer.get(offset + 2) & 255) | ((buffer.get(offset + 1) & 255) << 8) | ((buffer.get(offset) & 255) << 16) | (-16777216);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb$ToByteBgrfConv.class */
    static class ToByteBgrfConv extends BaseByteToByteConverter {
        public static final ByteToBytePixelConverter nonpremult = new ToByteBgrfConv(ByteBgra.setter);
        public static final ByteToBytePixelConverter premult = new ToByteBgrfConv(ByteBgraPre.setter);

        private ToByteBgrfConv(BytePixelSetter setter) {
            super(ByteRgb.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = srcarr[srcoff + 2];
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = srcarr[srcoff + 1];
                        int dstoff4 = dstoff3 + 1;
                        dstarr[dstoff3] = srcarr[srcoff];
                        dstoff = dstoff4 + 1;
                        dstarr[dstoff4] = -1;
                        srcoff += 3;
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
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            int dstscanbytes2 = dstscanbytes - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        dstbuf.put(dstoff, srcbuf.get(srcoff + 2));
                        dstbuf.put(dstoff + 1, srcbuf.get(srcoff + 1));
                        dstbuf.put(dstoff + 2, srcbuf.get(srcoff));
                        dstbuf.put(dstoff + 3, (byte) -1);
                        srcoff += 3;
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

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb$ToIntFrgbConv.class */
    static class ToIntFrgbConv extends BaseByteToIntConverter {
        public static final ByteToIntPixelConverter nonpremult = new ToIntFrgbConv(IntArgb.setter);
        public static final ByteToIntPixelConverter premult = new ToIntFrgbConv(IntArgbPre.setter);

        private ToIntFrgbConv(IntPixelSetter setter) {
            super(ByteRgb.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = srcoff;
                        int srcoff2 = srcoff + 1;
                        int r2 = srcarr[i2] & 255;
                        int srcoff3 = srcoff2 + 1;
                        int g2 = srcarr[srcoff2] & 255;
                        srcoff = srcoff3 + 1;
                        int b2 = srcarr[srcoff3] & 255;
                        dstarr[dstoff + x2] = (-16777216) | (r2 << 16) | (g2 << 8) | b2;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanints, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (w2 * 3);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int r2 = srcbuf.get(srcoff) & 255;
                        int g2 = srcbuf.get(srcoff + 1) & 255;
                        int b2 = srcbuf.get(srcoff + 2) & 255;
                        srcoff += 3;
                        dstbuf.put(dstoff + x2, (-16777216) | (r2 << 16) | (g2 << 8) | b2);
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanints;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb$ToByteFrgbConv.class */
    static class ToByteFrgbConv extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter nonpremult = new ToByteFrgbConv(ByteArgb.setter);

        private ToByteFrgbConv(BytePixelSetter setter) {
            super(ByteRgb.getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = -1;
                        int dstoff3 = dstoff2 + 1;
                        int i3 = srcoff;
                        int srcoff2 = srcoff + 1;
                        dstarr[dstoff2] = srcarr[i3];
                        int dstoff4 = dstoff3 + 1;
                        int srcoff3 = srcoff2 + 1;
                        dstarr[dstoff3] = srcarr[srcoff2];
                        dstoff = dstoff4 + 1;
                        srcoff = srcoff3 + 1;
                        dstarr[dstoff4] = srcarr[srcoff3];
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i2, (byte) -1);
                        int dstoff3 = dstoff2 + 1;
                        int i3 = srcoff;
                        int srcoff2 = srcoff + 1;
                        dstbuf.put(dstoff2, srcbuf.get(i3));
                        int dstoff4 = dstoff3 + 1;
                        int srcoff3 = srcoff2 + 1;
                        dstbuf.put(dstoff3, srcbuf.get(srcoff2));
                        dstoff = dstoff4 + 1;
                        srcoff = srcoff3 + 1;
                        dstbuf.put(dstoff4, srcbuf.get(srcoff3));
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteRgb$SwapThreeByteConverter.class */
    static class SwapThreeByteConverter extends BaseByteToByteConverter {
        static final ByteToBytePixelConverter rgbToBgrInstance = new SwapThreeByteConverter(ByteRgb.getter, ByteBgr.accessor);

        public SwapThreeByteConverter(BytePixelGetter getter, BytePixelSetter setter) {
            super(getter, setter);
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstarr[i2] = srcarr[srcoff + 2];
                        int dstoff3 = dstoff2 + 1;
                        dstarr[dstoff2] = srcarr[srcoff + 1];
                        dstoff = dstoff3 + 1;
                        dstarr[dstoff3] = srcarr[srcoff];
                        srcoff += 3;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = (srcscanbytes - (w2 * 3)) - (w2 * 4);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        int i2 = dstoff;
                        int dstoff2 = dstoff + 1;
                        dstbuf.put(i2, srcbuf.get(srcoff + 2));
                        int dstoff3 = dstoff2 + 1;
                        dstbuf.put(dstoff2, srcbuf.get(srcoff + 1));
                        dstoff = dstoff3 + 1;
                        dstbuf.put(dstoff3, srcbuf.get(srcoff));
                        srcoff += 3;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes;
                } else {
                    return;
                }
            }
        }
    }
}
