package sun.java2d.loops;

import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/* compiled from: GeneralRenderer.java */
/* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter.class */
abstract class XorPixelWriter extends PixelWriter {
    protected ColorModel dstCM;

    protected abstract void xorPixel(Object obj);

    XorPixelWriter() {
    }

    @Override // sun.java2d.loops.PixelWriter
    public void writePixel(int i2, int i3) {
        Object dataElements = this.dstRast.getDataElements(i2, i3, null);
        xorPixel(dataElements);
        this.dstRast.setDataElements(i2, i3, dataElements);
    }

    /* compiled from: GeneralRenderer.java */
    /* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter$ByteData.class */
    public static class ByteData extends XorPixelWriter {
        byte[] xorData;

        @Override // sun.java2d.loops.XorPixelWriter, sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void writePixel(int i2, int i3) {
            super.writePixel(i2, i3);
        }

        @Override // sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void setRaster(WritableRaster writableRaster) {
            super.setRaster(writableRaster);
        }

        ByteData(Object obj, Object obj2) {
            this.xorData = (byte[]) obj;
            xorPixel(obj2);
            this.xorData = (byte[]) obj2;
        }

        @Override // sun.java2d.loops.XorPixelWriter
        protected void xorPixel(Object obj) {
            byte[] bArr = (byte[]) obj;
            for (int i2 = 0; i2 < bArr.length; i2++) {
                int i3 = i2;
                bArr[i3] = (byte) (bArr[i3] ^ this.xorData[i2]);
            }
        }
    }

    /* compiled from: GeneralRenderer.java */
    /* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter$ShortData.class */
    public static class ShortData extends XorPixelWriter {
        short[] xorData;

        @Override // sun.java2d.loops.XorPixelWriter, sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void writePixel(int i2, int i3) {
            super.writePixel(i2, i3);
        }

        @Override // sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void setRaster(WritableRaster writableRaster) {
            super.setRaster(writableRaster);
        }

        ShortData(Object obj, Object obj2) {
            this.xorData = (short[]) obj;
            xorPixel(obj2);
            this.xorData = (short[]) obj2;
        }

        @Override // sun.java2d.loops.XorPixelWriter
        protected void xorPixel(Object obj) {
            short[] sArr = (short[]) obj;
            for (int i2 = 0; i2 < sArr.length; i2++) {
                int i3 = i2;
                sArr[i3] = (short) (sArr[i3] ^ this.xorData[i2]);
            }
        }
    }

    /* compiled from: GeneralRenderer.java */
    /* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter$IntData.class */
    public static class IntData extends XorPixelWriter {
        int[] xorData;

        @Override // sun.java2d.loops.XorPixelWriter, sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void writePixel(int i2, int i3) {
            super.writePixel(i2, i3);
        }

        @Override // sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void setRaster(WritableRaster writableRaster) {
            super.setRaster(writableRaster);
        }

        IntData(Object obj, Object obj2) {
            this.xorData = (int[]) obj;
            xorPixel(obj2);
            this.xorData = (int[]) obj2;
        }

        @Override // sun.java2d.loops.XorPixelWriter
        protected void xorPixel(Object obj) {
            int[] iArr = (int[]) obj;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = i2;
                iArr[i3] = iArr[i3] ^ this.xorData[i2];
            }
        }
    }

    /* compiled from: GeneralRenderer.java */
    /* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter$FloatData.class */
    public static class FloatData extends XorPixelWriter {
        int[] xorData;

        @Override // sun.java2d.loops.XorPixelWriter, sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void writePixel(int i2, int i3) {
            super.writePixel(i2, i3);
        }

        @Override // sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void setRaster(WritableRaster writableRaster) {
            super.setRaster(writableRaster);
        }

        FloatData(Object obj, Object obj2) {
            float[] fArr = (float[]) obj;
            float[] fArr2 = (float[]) obj2;
            this.xorData = new int[fArr.length];
            for (int i2 = 0; i2 < fArr.length; i2++) {
                this.xorData[i2] = Float.floatToIntBits(fArr[i2]) ^ Float.floatToIntBits(fArr2[i2]);
            }
        }

        @Override // sun.java2d.loops.XorPixelWriter
        protected void xorPixel(Object obj) {
            float[] fArr = (float[]) obj;
            for (int i2 = 0; i2 < fArr.length; i2++) {
                fArr[i2] = Float.intBitsToFloat(Float.floatToIntBits(fArr[i2]) ^ this.xorData[i2]);
            }
        }
    }

    /* compiled from: GeneralRenderer.java */
    /* loaded from: rt.jar:sun/java2d/loops/XorPixelWriter$DoubleData.class */
    public static class DoubleData extends XorPixelWriter {
        long[] xorData;

        @Override // sun.java2d.loops.XorPixelWriter, sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void writePixel(int i2, int i3) {
            super.writePixel(i2, i3);
        }

        @Override // sun.java2d.loops.PixelWriter
        public /* bridge */ /* synthetic */ void setRaster(WritableRaster writableRaster) {
            super.setRaster(writableRaster);
        }

        DoubleData(Object obj, Object obj2) {
            double[] dArr = (double[]) obj;
            double[] dArr2 = (double[]) obj2;
            this.xorData = new long[dArr.length];
            for (int i2 = 0; i2 < dArr.length; i2++) {
                this.xorData[i2] = Double.doubleToLongBits(dArr[i2]) ^ Double.doubleToLongBits(dArr2[i2]);
            }
        }

        @Override // sun.java2d.loops.XorPixelWriter
        protected void xorPixel(Object obj) {
            double[] dArr = (double[]) obj;
            for (int i2 = 0; i2 < dArr.length; i2++) {
                dArr[i2] = Double.longBitsToDouble(Double.doubleToLongBits(dArr[i2]) ^ this.xorData[i2]);
            }
        }
    }
}
