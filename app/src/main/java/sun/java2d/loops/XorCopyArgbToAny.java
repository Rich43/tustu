package sun.java2d.loops;

import java.awt.Composite;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import sun.awt.image.IntegerComponentRaster;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.SpanIterator;

/* compiled from: CustomComponent.java */
/* loaded from: rt.jar:sun/java2d/loops/XorCopyArgbToAny.class */
class XorCopyArgbToAny extends Blit {
    XorCopyArgbToAny() {
        super(SurfaceType.IntArgb, CompositeType.Xor, SurfaceType.Any);
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) surfaceData.getRaster(i2, i3, i6, i7);
        int[] dataStorage = integerComponentRaster.getDataStorage();
        WritableRaster writableRaster = (WritableRaster) surfaceData2.getRaster(i4, i5, i6, i7);
        ColorModel colorModel = surfaceData2.getColorModel();
        SpanIterator spanIterator = CustomComponent.getRegionOfInterest(surfaceData, surfaceData2, region, i2, i3, i4, i5, i6, i7).getSpanIterator();
        Object dataElements = colorModel.getDataElements(((XORComposite) composite).getXorColor().getRGB(), null);
        Object dataElements2 = null;
        Object dataElements3 = null;
        int scanlineStride = integerComponentRaster.getScanlineStride();
        int i8 = i2 - i4;
        int i9 = i3 - i5;
        int[] iArr = new int[4];
        while (spanIterator.nextSpan(iArr)) {
            int dataOffset = integerComponentRaster.getDataOffset(0) + ((i9 + iArr[1]) * scanlineStride) + i8 + iArr[0];
            for (int i10 = iArr[1]; i10 < iArr[3]; i10++) {
                int i11 = dataOffset;
                for (int i12 = iArr[0]; i12 < iArr[2]; i12++) {
                    int i13 = i11;
                    i11++;
                    dataElements2 = colorModel.getDataElements(dataStorage[i13], dataElements2);
                    dataElements3 = writableRaster.getDataElements(i12, i10, dataElements3);
                    switch (colorModel.getTransferType()) {
                        case 0:
                            byte[] bArr = (byte[]) dataElements2;
                            byte[] bArr2 = (byte[]) dataElements3;
                            byte[] bArr3 = (byte[]) dataElements;
                            for (int i14 = 0; i14 < bArr2.length; i14++) {
                                int i15 = i14;
                                bArr2[i15] = (byte) (bArr2[i15] ^ (bArr[i14] ^ bArr3[i14]));
                            }
                            break;
                        case 1:
                        case 2:
                            short[] sArr = (short[]) dataElements2;
                            short[] sArr2 = (short[]) dataElements3;
                            short[] sArr3 = (short[]) dataElements;
                            for (int i16 = 0; i16 < sArr2.length; i16++) {
                                int i17 = i16;
                                sArr2[i17] = (short) (sArr2[i17] ^ (sArr[i16] ^ sArr3[i16]));
                            }
                            break;
                        case 3:
                            int[] iArr2 = (int[]) dataElements2;
                            short[] sArr4 = (int[]) dataElements3;
                            int[] iArr3 = (int[]) dataElements;
                            for (int i18 = 0; i18 < sArr4.length; i18++) {
                                int i19 = i18;
                                sArr4[i19] = sArr4[i19] ^ (iArr2[i18] ^ iArr3[i18]);
                            }
                            break;
                        case 4:
                            float[] fArr = (float[]) dataElements2;
                            short[] sArr5 = (float[]) dataElements3;
                            float[] fArr2 = (float[]) dataElements;
                            for (int i20 = 0; i20 < sArr5.length; i20++) {
                                sArr5[i20] = Float.intBitsToFloat((Float.floatToIntBits(sArr5[i20]) ^ Float.floatToIntBits(fArr[i20])) ^ Float.floatToIntBits(fArr2[i20]));
                            }
                            break;
                        case 5:
                            double[] dArr = (double[]) dataElements2;
                            short[] sArr6 = (double[]) dataElements3;
                            double[] dArr2 = (double[]) dataElements;
                            for (int i21 = 0; i21 < sArr6.length; i21++) {
                                sArr6[i21] = Double.longBitsToDouble((Double.doubleToLongBits(sArr6[i21]) ^ Double.doubleToLongBits(dArr[i21])) ^ Double.doubleToLongBits(dArr2[i21]));
                            }
                            break;
                        default:
                            throw new InternalError("Unsupported XOR pixel type");
                    }
                    writableRaster.setDataElements(i12, i10, dataElements3);
                }
                dataOffset += scanlineStride;
            }
        }
    }
}
