package com.sun.javafx.image.impl;

import com.sun.javafx.image.AlphaType;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/General.class */
public class General {
    public static ByteToBytePixelConverter create(BytePixelGetter src, BytePixelSetter dst) {
        return new ByteToByteGeneralConverter(src, dst);
    }

    public static ByteToIntPixelConverter create(BytePixelGetter src, IntPixelSetter dst) {
        return new ByteToIntGeneralConverter(src, dst);
    }

    public static IntToBytePixelConverter create(IntPixelGetter src, BytePixelSetter dst) {
        return new IntToByteGeneralConverter(src, dst);
    }

    public static IntToIntPixelConverter create(IntPixelGetter src, IntPixelSetter dst) {
        return new IntToIntGeneralConverter(src, dst);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/General$ByteToByteGeneralConverter.class */
    static class ByteToByteGeneralConverter extends BaseByteToByteConverter {
        boolean usePremult;

        ByteToByteGeneralConverter(BytePixelGetter getter, BytePixelSetter setter) {
            super(getter, setter);
            this.usePremult = (getter.getAlphaType() == AlphaType.NONPREMULTIPLIED || setter.getAlphaType() == AlphaType.NONPREMULTIPLIED) ? false : true;
        }

        @Override // com.sun.javafx.image.impl.BaseByteToByteConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (this.nSrcElems * w2);
            int dstscanbytes2 = dstscanbytes - (this.nDstElems * w2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre(dstarr, dstoff, this.getter.getArgbPre(srcarr, srcoff));
                        } else {
                            this.setter.setArgb(dstarr, dstoff, this.getter.getArgb(srcarr, srcoff));
                        }
                        srcoff += this.nSrcElems;
                        dstoff += this.nDstElems;
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
            int srcscanbytes2 = srcscanbytes - (this.nSrcElems * w2);
            int dstscanbytes2 = dstscanbytes - (this.nDstElems * w2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre((BytePixelSetter) dstbuf, dstoff, this.getter.getArgbPre((BytePixelGetter) srcbuf, srcoff));
                        } else {
                            this.setter.setArgb((BytePixelSetter) dstbuf, dstoff, this.getter.getArgb((BytePixelGetter) srcbuf, srcoff));
                        }
                        srcoff += this.nSrcElems;
                        dstoff += this.nDstElems;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/General$ByteToIntGeneralConverter.class */
    static class ByteToIntGeneralConverter extends BaseByteToIntConverter {
        boolean usePremult;

        ByteToIntGeneralConverter(BytePixelGetter getter, IntPixelSetter setter) {
            super(getter, setter);
            this.usePremult = (getter.getAlphaType() == AlphaType.NONPREMULTIPLIED || setter.getAlphaType() == AlphaType.NONPREMULTIPLIED) ? false : true;
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(byte[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (this.nSrcElems * w2);
            int dstscanbytes2 = dstscanbytes - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre(dstarr, dstoff, this.getter.getArgbPre(srcarr, srcoff));
                        } else {
                            this.setter.setArgb(dstarr, dstoff, this.getter.getArgb(srcarr, srcoff));
                        }
                        srcoff += this.nSrcElems;
                        dstoff++;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseByteToIntConverter
        void doConvert(ByteBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - (this.nSrcElems * w2);
            int dstscanbytes2 = dstscanbytes - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre((IntPixelSetter) dstbuf, dstoff, this.getter.getArgbPre((BytePixelGetter) srcbuf, srcoff));
                        } else {
                            this.setter.setArgb((IntPixelSetter) dstbuf, dstoff, this.getter.getArgb((BytePixelGetter) srcbuf, srcoff));
                        }
                        srcoff += this.nSrcElems;
                        dstoff++;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/General$IntToByteGeneralConverter.class */
    static class IntToByteGeneralConverter extends BaseIntToByteConverter {
        boolean usePremult;

        public IntToByteGeneralConverter(IntPixelGetter getter, BytePixelSetter setter) {
            super(getter, setter);
            this.usePremult = (getter.getAlphaType() == AlphaType.NONPREMULTIPLIED || setter.getAlphaType() == AlphaType.NONPREMULTIPLIED) ? false : true;
        }

        @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
        void doConvert(int[] srcarr, int srcoff, int srcscanbytes, byte[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - w2;
            int dstscanbytes2 = dstscanbytes - (this.nDstElems * w2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre(dstarr, dstoff, this.getter.getArgbPre(srcarr, srcoff));
                        } else {
                            this.setter.setArgb(dstarr, dstoff, this.getter.getArgb(srcarr, srcoff));
                        }
                        srcoff++;
                        dstoff += this.nDstElems;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseIntToByteConverter
        void doConvert(IntBuffer srcbuf, int srcoff, int srcscanbytes, ByteBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - w2;
            int dstscanbytes2 = dstscanbytes - (this.nDstElems * w2);
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre((BytePixelSetter) dstbuf, dstoff, this.getter.getArgbPre((IntPixelGetter) srcbuf, srcoff));
                        } else {
                            this.setter.setArgb((BytePixelSetter) dstbuf, dstoff, this.getter.getArgb((IntPixelGetter) srcbuf, srcoff));
                        }
                        srcoff++;
                        dstoff += this.nDstElems;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/image/impl/General$IntToIntGeneralConverter.class */
    static class IntToIntGeneralConverter extends BaseIntToIntConverter {
        boolean usePremult;

        public IntToIntGeneralConverter(IntPixelGetter getter, IntPixelSetter setter) {
            super(getter, setter);
            this.usePremult = (getter.getAlphaType() == AlphaType.NONPREMULTIPLIED || setter.getAlphaType() == AlphaType.NONPREMULTIPLIED) ? false : true;
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(int[] srcarr, int srcoff, int srcscanbytes, int[] dstarr, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - w2;
            int dstscanbytes2 = dstscanbytes - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre(dstarr, dstoff, this.getter.getArgbPre(srcarr, srcoff));
                        } else {
                            this.setter.setArgb(dstarr, dstoff, this.getter.getArgb(srcarr, srcoff));
                        }
                        srcoff++;
                        dstoff++;
                    }
                    srcoff += srcscanbytes2;
                    dstoff += dstscanbytes2;
                } else {
                    return;
                }
            }
        }

        @Override // com.sun.javafx.image.impl.BaseIntToIntConverter
        void doConvert(IntBuffer srcbuf, int srcoff, int srcscanbytes, IntBuffer dstbuf, int dstoff, int dstscanbytes, int w2, int h2) {
            int srcscanbytes2 = srcscanbytes - w2;
            int dstscanbytes2 = dstscanbytes - w2;
            while (true) {
                h2--;
                if (h2 >= 0) {
                    for (int x2 = 0; x2 < w2; x2++) {
                        if (this.usePremult) {
                            this.setter.setArgbPre((IntPixelSetter) dstbuf, dstoff, this.getter.getArgbPre((IntPixelGetter) srcbuf, srcoff));
                        } else {
                            this.setter.setArgb((IntPixelSetter) dstbuf, dstoff, this.getter.getArgb((IntPixelGetter) srcbuf, srcoff));
                        }
                        srcoff++;
                        dstoff++;
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
