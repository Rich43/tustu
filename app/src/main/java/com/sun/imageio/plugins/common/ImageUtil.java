package com.sun.imageio.plugins.common;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import javax.imageio.IIOException;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/ImageUtil.class */
public class ImageUtil {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v64, types: [java.awt.color.ColorSpace] */
    /* JADX WARN: Type inference failed for: r0v77, types: [java.awt.color.ColorSpace] */
    public static final ColorModel createColorModel(SampleModel sampleModel) {
        int i2;
        int i3;
        int i4;
        BogusColorSpace bogusColorSpace;
        if (sampleModel == null) {
            throw new IllegalArgumentException("sampleModel == null!");
        }
        int dataType = sampleModel.getDataType();
        switch (dataType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                ColorModel indexColorModel = null;
                int[] sampleSize = sampleModel.getSampleSize();
                if (sampleModel instanceof ComponentSampleModel) {
                    int numBands = sampleModel.getNumBands();
                    if (numBands <= 2) {
                        bogusColorSpace = ColorSpace.getInstance(1003);
                    } else if (numBands <= 4) {
                        bogusColorSpace = ColorSpace.getInstance(1000);
                    } else {
                        bogusColorSpace = new BogusColorSpace(numBands);
                    }
                    boolean z2 = numBands == 2 || numBands == 4;
                    indexColorModel = new ComponentColorModel(bogusColorSpace, sampleSize, z2, false, z2 ? 3 : 1, dataType);
                } else {
                    if (sampleModel.getNumBands() <= 4 && (sampleModel instanceof SinglePixelPackedSampleModel)) {
                        int[] bitMasks = ((SinglePixelPackedSampleModel) sampleModel).getBitMasks();
                        int i5 = 0;
                        int length = bitMasks.length;
                        if (length <= 2) {
                            int i6 = bitMasks[0];
                            i4 = i6;
                            i3 = i6;
                            i2 = i6;
                            if (length == 2) {
                                i5 = bitMasks[1];
                            }
                        } else {
                            i2 = bitMasks[0];
                            i3 = bitMasks[1];
                            i4 = bitMasks[2];
                            if (length == 4) {
                                i5 = bitMasks[3];
                            }
                        }
                        int i7 = 0;
                        for (int i8 : sampleSize) {
                            i7 += i8;
                        }
                        return new DirectColorModel(i7, i2, i3, i4, i5);
                    }
                    if (sampleModel instanceof MultiPixelPackedSampleModel) {
                        int i9 = sampleSize[0];
                        int i10 = 1 << i9;
                        byte[] bArr = new byte[i10];
                        for (int i11 = 0; i11 < i10; i11++) {
                            bArr[i11] = (byte) ((i11 * 255) / (i10 - 1));
                        }
                        indexColorModel = new IndexColorModel(i9, i10, bArr, bArr, bArr);
                    }
                }
                return indexColorModel;
            default:
                return null;
        }
    }

    public static byte[] getPackedBinaryData(Raster raster, Rectangle rectangle) {
        short[] data;
        short[] data2;
        SampleModel sampleModel = raster.getSampleModel();
        if (!isBinary(sampleModel)) {
            throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        DataBuffer dataBuffer = raster.getDataBuffer();
        int sampleModelTranslateX = i2 - raster.getSampleModelTranslateX();
        int sampleModelTranslateY = i3 - raster.getSampleModelTranslateY();
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
        int scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
        int offset = dataBuffer.getOffset() + multiPixelPackedSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY);
        int bitOffset = multiPixelPackedSampleModel.getBitOffset(sampleModelTranslateX);
        int i6 = (i4 + 7) / 8;
        if ((dataBuffer instanceof DataBufferByte) && offset == 0 && bitOffset == 0 && i6 == scanlineStride && ((DataBufferByte) dataBuffer).getData().length == i6 * i5) {
            return ((DataBufferByte) dataBuffer).getData();
        }
        byte[] bArr = new byte[i6 * i5];
        int i7 = 0;
        if (bitOffset == 0) {
            if (dataBuffer instanceof DataBufferByte) {
                byte[] data3 = ((DataBufferByte) dataBuffer).getData();
                int i8 = 0;
                for (int i9 = 0; i9 < i5; i9++) {
                    System.arraycopy(data3, offset, bArr, i8, i6);
                    i8 += i6;
                    offset += scanlineStride;
                }
            } else if ((dataBuffer instanceof DataBufferShort) || (dataBuffer instanceof DataBufferUShort)) {
                if (dataBuffer instanceof DataBufferShort) {
                    data2 = ((DataBufferShort) dataBuffer).getData();
                } else {
                    data2 = ((DataBufferUShort) dataBuffer).getData();
                }
                short[] sArr = data2;
                for (int i10 = 0; i10 < i5; i10++) {
                    int i11 = i4;
                    int i12 = offset;
                    while (i11 > 8) {
                        int i13 = i12;
                        i12++;
                        short s2 = sArr[i13];
                        int i14 = i7;
                        int i15 = i7 + 1;
                        bArr[i14] = (byte) ((s2 >>> 8) & 255);
                        i7 = i15 + 1;
                        bArr[i15] = (byte) (s2 & 255);
                        i11 -= 16;
                    }
                    if (i11 > 0) {
                        int i16 = i7;
                        i7++;
                        bArr[i16] = (byte) ((sArr[i12] >>> 8) & 255);
                    }
                    offset += scanlineStride;
                }
            } else if (dataBuffer instanceof DataBufferInt) {
                int[] data4 = ((DataBufferInt) dataBuffer).getData();
                for (int i17 = 0; i17 < i5; i17++) {
                    int i18 = i4;
                    int i19 = offset;
                    while (i18 > 24) {
                        int i20 = i19;
                        i19++;
                        int i21 = data4[i20];
                        int i22 = i7;
                        int i23 = i7 + 1;
                        bArr[i22] = (byte) ((i21 >>> 24) & 255);
                        int i24 = i23 + 1;
                        bArr[i23] = (byte) ((i21 >>> 16) & 255);
                        int i25 = i24 + 1;
                        bArr[i24] = (byte) ((i21 >>> 8) & 255);
                        i7 = i25 + 1;
                        bArr[i25] = (byte) (i21 & 255);
                        i18 -= 32;
                    }
                    int i26 = 24;
                    while (i18 > 0) {
                        int i27 = i7;
                        i7++;
                        bArr[i27] = (byte) ((data4[i19] >>> i26) & 255);
                        i26 -= 8;
                        i18 -= 8;
                    }
                    offset += scanlineStride;
                }
            }
        } else if (dataBuffer instanceof DataBufferByte) {
            byte[] data5 = ((DataBufferByte) dataBuffer).getData();
            if ((bitOffset & 7) == 0) {
                int i28 = 0;
                for (int i29 = 0; i29 < i5; i29++) {
                    System.arraycopy(data5, offset, bArr, i28, i6);
                    i28 += i6;
                    offset += scanlineStride;
                }
            } else {
                int i30 = bitOffset & 7;
                int i31 = 8 - i30;
                for (int i32 = 0; i32 < i5; i32++) {
                    int i33 = offset;
                    for (int i34 = i4; i34 > 0; i34 -= 8) {
                        if (i34 > i31) {
                            int i35 = i7;
                            i7++;
                            int i36 = i33;
                            i33++;
                            bArr[i35] = (byte) (((data5[i36] & 255) << i30) | ((data5[i33] & 255) >>> i31));
                        } else {
                            int i37 = i7;
                            i7++;
                            bArr[i37] = (byte) ((data5[i33] & 255) << i30);
                        }
                    }
                    offset += scanlineStride;
                }
            }
        } else if ((dataBuffer instanceof DataBufferShort) || (dataBuffer instanceof DataBufferUShort)) {
            if (dataBuffer instanceof DataBufferShort) {
                data = ((DataBufferShort) dataBuffer).getData();
            } else {
                data = ((DataBufferUShort) dataBuffer).getData();
            }
            short[] sArr2 = data;
            for (int i38 = 0; i38 < i5; i38++) {
                int i39 = bitOffset;
                int i40 = 0;
                while (i40 < i4) {
                    int i41 = offset + (i39 / 16);
                    int i42 = i39 % 16;
                    int i43 = sArr2[i41] & 65535;
                    if (i42 <= 8) {
                        int i44 = i7;
                        i7++;
                        bArr[i44] = (byte) (i43 >>> (8 - i42));
                    } else {
                        int i45 = i42 - 8;
                        int i46 = i7;
                        i7++;
                        bArr[i46] = (byte) ((i43 << i45) | ((sArr2[i41 + 1] & 65535) >>> (16 - i45)));
                    }
                    i40 += 8;
                    i39 += 8;
                }
                offset += scanlineStride;
            }
        } else if (dataBuffer instanceof DataBufferInt) {
            int[] data6 = ((DataBufferInt) dataBuffer).getData();
            for (int i47 = 0; i47 < i5; i47++) {
                int i48 = bitOffset;
                int i49 = 0;
                while (i49 < i4) {
                    int i50 = offset + (i48 / 32);
                    int i51 = i48 % 32;
                    int i52 = data6[i50];
                    if (i51 <= 24) {
                        int i53 = i7;
                        i7++;
                        bArr[i53] = (byte) (i52 >>> (24 - i51));
                    } else {
                        int i54 = i51 - 24;
                        int i55 = i7;
                        i7++;
                        bArr[i55] = (byte) ((i52 << i54) | (data6[i50 + 1] >>> (32 - i54)));
                    }
                    i49 += 8;
                    i48 += 8;
                }
                offset += scanlineStride;
            }
        }
        return bArr;
    }

    public static byte[] getUnpackedBinaryData(Raster raster, Rectangle rectangle) {
        short[] data;
        SampleModel sampleModel = raster.getSampleModel();
        if (!isBinary(sampleModel)) {
            throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        DataBuffer dataBuffer = raster.getDataBuffer();
        int sampleModelTranslateX = i2 - raster.getSampleModelTranslateX();
        int sampleModelTranslateY = i3 - raster.getSampleModelTranslateY();
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
        int scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
        int offset = dataBuffer.getOffset() + multiPixelPackedSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY);
        int bitOffset = multiPixelPackedSampleModel.getBitOffset(sampleModelTranslateX);
        byte[] bArr = new byte[i4 * i5];
        int i6 = i3 + i5;
        int i7 = i2 + i4;
        int i8 = 0;
        if (dataBuffer instanceof DataBufferByte) {
            byte[] data2 = ((DataBufferByte) dataBuffer).getData();
            for (int i9 = i3; i9 < i6; i9++) {
                int i10 = (offset * 8) + bitOffset;
                for (int i11 = i2; i11 < i7; i11++) {
                    int i12 = i8;
                    i8++;
                    bArr[i12] = (byte) ((data2[i10 / 8] >>> ((7 - i10) & 7)) & 1);
                    i10++;
                }
                offset += scanlineStride;
            }
        } else if ((dataBuffer instanceof DataBufferShort) || (dataBuffer instanceof DataBufferUShort)) {
            if (dataBuffer instanceof DataBufferShort) {
                data = ((DataBufferShort) dataBuffer).getData();
            } else {
                data = ((DataBufferUShort) dataBuffer).getData();
            }
            short[] sArr = data;
            for (int i13 = i3; i13 < i6; i13++) {
                int i14 = (offset * 16) + bitOffset;
                for (int i15 = i2; i15 < i7; i15++) {
                    int i16 = i8;
                    i8++;
                    bArr[i16] = (byte) ((sArr[i14 / 16] >>> (15 - (i14 % 16))) & 1);
                    i14++;
                }
                offset += scanlineStride;
            }
        } else if (dataBuffer instanceof DataBufferInt) {
            int[] data3 = ((DataBufferInt) dataBuffer).getData();
            for (int i17 = i3; i17 < i6; i17++) {
                int i18 = (offset * 32) + bitOffset;
                for (int i19 = i2; i19 < i7; i19++) {
                    int i20 = i8;
                    i8++;
                    bArr[i20] = (byte) ((data3[i18 / 32] >>> (31 - (i18 % 32))) & 1);
                    i18++;
                }
                offset += scanlineStride;
            }
        }
        return bArr;
    }

    public static void setPackedBinaryData(byte[] bArr, WritableRaster writableRaster, Rectangle rectangle) {
        short[] data;
        short[] data2;
        SampleModel sampleModel = writableRaster.getSampleModel();
        if (!isBinary(sampleModel)) {
            throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        DataBuffer dataBuffer = writableRaster.getDataBuffer();
        int sampleModelTranslateX = i2 - writableRaster.getSampleModelTranslateX();
        int sampleModelTranslateY = i3 - writableRaster.getSampleModelTranslateY();
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
        int scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
        int offset = dataBuffer.getOffset() + multiPixelPackedSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY);
        int bitOffset = multiPixelPackedSampleModel.getBitOffset(sampleModelTranslateX);
        int i6 = 0;
        if (bitOffset == 0) {
            if (dataBuffer instanceof DataBufferByte) {
                byte[] data3 = ((DataBufferByte) dataBuffer).getData();
                if (data3 == bArr) {
                    return;
                }
                int i7 = (i4 + 7) / 8;
                int i8 = 0;
                for (int i9 = 0; i9 < i5; i9++) {
                    System.arraycopy(bArr, i8, data3, offset, i7);
                    i8 += i7;
                    offset += scanlineStride;
                }
                return;
            }
            if ((dataBuffer instanceof DataBufferShort) || (dataBuffer instanceof DataBufferUShort)) {
                if (dataBuffer instanceof DataBufferShort) {
                    data2 = ((DataBufferShort) dataBuffer).getData();
                } else {
                    data2 = ((DataBufferUShort) dataBuffer).getData();
                }
                short[] sArr = data2;
                for (int i10 = 0; i10 < i5; i10++) {
                    int i11 = i4;
                    int i12 = offset;
                    while (i11 > 8) {
                        int i13 = i12;
                        i12++;
                        int i14 = i6;
                        int i15 = i6 + 1;
                        i6 = i15 + 1;
                        sArr[i13] = (short) (((bArr[i14] & 255) << 8) | (bArr[i15] & 255));
                        i11 -= 16;
                    }
                    if (i11 > 0) {
                        int i16 = i12;
                        int i17 = i12 + 1;
                        int i18 = i6;
                        i6++;
                        sArr[i16] = (short) ((bArr[i18] & 255) << 8);
                    }
                    offset += scanlineStride;
                }
                return;
            }
            if (dataBuffer instanceof DataBufferInt) {
                int[] data4 = ((DataBufferInt) dataBuffer).getData();
                for (int i19 = 0; i19 < i5; i19++) {
                    int i20 = i4;
                    int i21 = offset;
                    while (i20 > 24) {
                        int i22 = i21;
                        i21++;
                        int i23 = i6;
                        int i24 = i6 + 1;
                        int i25 = i24 + 1;
                        int i26 = ((bArr[i23] & 255) << 24) | ((bArr[i24] & 255) << 16);
                        int i27 = i25 + 1;
                        int i28 = i26 | ((bArr[i25] & 255) << 8);
                        i6 = i27 + 1;
                        data4[i22] = i28 | (bArr[i27] & 255);
                        i20 -= 32;
                    }
                    int i29 = 24;
                    while (i20 > 0) {
                        int i30 = i21;
                        int i31 = i6;
                        i6++;
                        data4[i30] = data4[i30] | ((bArr[i31] & 255) << i29);
                        i29 -= 8;
                        i20 -= 8;
                    }
                    offset += scanlineStride;
                }
                return;
            }
            return;
        }
        int i32 = (i4 + 7) / 8;
        int i33 = 0;
        if (dataBuffer instanceof DataBufferByte) {
            byte[] data5 = ((DataBufferByte) dataBuffer).getData();
            if ((bitOffset & 7) == 0) {
                for (int i34 = 0; i34 < i5; i34++) {
                    System.arraycopy(bArr, i33, data5, offset, i32);
                    i33 += i32;
                    offset += scanlineStride;
                }
                return;
            }
            int i35 = bitOffset & 7;
            int i36 = 8 - i35;
            int i37 = 8 + i36;
            byte b2 = (byte) (255 << i36);
            byte b3 = (byte) (b2 ^ (-1));
            for (int i38 = 0; i38 < i5; i38++) {
                int i39 = offset;
                for (int i40 = i4; i40 > 0; i40 -= 8) {
                    int i41 = i6;
                    i6++;
                    byte b4 = bArr[i41];
                    if (i40 > i37) {
                        data5[i39] = (byte) ((data5[i39] & b2) | ((b4 & 255) >>> i35));
                        i39++;
                        data5[i39] = (byte) ((b4 & 255) << i36);
                    } else if (i40 > i36) {
                        data5[i39] = (byte) ((data5[i39] & b2) | ((b4 & 255) >>> i35));
                        i39++;
                        data5[i39] = (byte) ((data5[i39] & b3) | ((b4 & 255) << i36));
                    } else {
                        int i42 = (1 << (i36 - i40)) - 1;
                        data5[i39] = (byte) ((data5[i39] & (b2 | i42)) | (((b4 & 255) >>> i35) & (i42 ^ (-1))));
                    }
                }
                offset += scanlineStride;
            }
            return;
        }
        if (!(dataBuffer instanceof DataBufferShort) && !(dataBuffer instanceof DataBufferUShort)) {
            if (dataBuffer instanceof DataBufferInt) {
                int[] data6 = ((DataBufferInt) dataBuffer).getData();
                int i43 = bitOffset & 7;
                int i44 = 8 - i43;
                int i45 = 32 + i44;
                int i46 = (-1) << i44;
                int i47 = i46 ^ (-1);
                for (int i48 = 0; i48 < i5; i48++) {
                    int i49 = bitOffset;
                    int i50 = i4;
                    int i51 = 0;
                    while (i51 < i4) {
                        int i52 = offset + (i49 >> 5);
                        int i53 = i49 & 31;
                        int i54 = i6;
                        i6++;
                        int i55 = bArr[i54] & 255;
                        if (i53 <= 24) {
                            int i56 = 24 - i53;
                            if (i50 < 8) {
                                i55 &= 255 << (8 - i50);
                            }
                            data6[i52] = (data6[i52] & ((255 << i56) ^ (-1))) | (i55 << i56);
                        } else if (i50 > i45) {
                            data6[i52] = (data6[i52] & i46) | (i55 >>> i43);
                            data6[i52 + 1] = i55 << i44;
                        } else if (i50 > i44) {
                            data6[i52] = (data6[i52] & i46) | (i55 >>> i43);
                            int i57 = i52 + 1;
                            data6[i57] = (data6[i57] & i47) | (i55 << i44);
                        } else {
                            int i58 = (1 << (i44 - i50)) - 1;
                            data6[i52] = (data6[i52] & (i46 | i58)) | ((i55 >>> i43) & (i58 ^ (-1)));
                        }
                        i51 += 8;
                        i49 += 8;
                        i50 -= 8;
                    }
                    offset += scanlineStride;
                }
                return;
            }
            return;
        }
        if (dataBuffer instanceof DataBufferShort) {
            data = ((DataBufferShort) dataBuffer).getData();
        } else {
            data = ((DataBufferUShort) dataBuffer).getData();
        }
        short[] sArr2 = data;
        int i59 = bitOffset & 7;
        int i60 = 8 - i59;
        int i61 = 16 + i60;
        short s2 = (short) ((255 << i60) ^ (-1));
        short s3 = (short) (65535 << i60);
        short s4 = (short) (s3 ^ (-1));
        for (int i62 = 0; i62 < i5; i62++) {
            int i63 = bitOffset;
            int i64 = i4;
            int i65 = 0;
            while (i65 < i4) {
                int i66 = offset + (i63 >> 4);
                int i67 = i63 & 15;
                int i68 = i6;
                i6++;
                int i69 = bArr[i68] & 255;
                if (i67 <= 8) {
                    if (i64 < 8) {
                        i69 &= 255 << (8 - i64);
                    }
                    sArr2[i66] = (short) ((sArr2[i66] & s2) | (i69 << i60));
                } else if (i64 > i61) {
                    sArr2[i66] = (short) ((sArr2[i66] & s3) | ((i69 >>> i59) & 65535));
                    sArr2[i66 + 1] = (short) ((i69 << i60) & 65535);
                } else if (i64 > i60) {
                    sArr2[i66] = (short) ((sArr2[i66] & s3) | ((i69 >>> i59) & 65535));
                    int i70 = i66 + 1;
                    sArr2[i70] = (short) ((sArr2[i70] & s4) | ((i69 << i60) & 65535));
                } else {
                    int i71 = (1 << (i60 - i64)) - 1;
                    sArr2[i66] = (short) ((sArr2[i66] & (s3 | i71)) | ((i69 >>> i59) & 65535 & (i71 ^ (-1))));
                }
                i65 += 8;
                i63 += 8;
                i64 -= 8;
            }
            offset += scanlineStride;
        }
    }

    public static void setUnpackedBinaryData(byte[] bArr, WritableRaster writableRaster, Rectangle rectangle) {
        short[] data;
        SampleModel sampleModel = writableRaster.getSampleModel();
        if (!isBinary(sampleModel)) {
            throw new IllegalArgumentException(I18N.getString("ImageUtil0"));
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        DataBuffer dataBuffer = writableRaster.getDataBuffer();
        int sampleModelTranslateX = i2 - writableRaster.getSampleModelTranslateX();
        int sampleModelTranslateY = i3 - writableRaster.getSampleModelTranslateY();
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
        int scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
        int offset = dataBuffer.getOffset() + multiPixelPackedSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY);
        int bitOffset = multiPixelPackedSampleModel.getBitOffset(sampleModelTranslateX);
        int i6 = 0;
        if (dataBuffer instanceof DataBufferByte) {
            byte[] data2 = ((DataBufferByte) dataBuffer).getData();
            for (int i7 = 0; i7 < i5; i7++) {
                int i8 = (offset * 8) + bitOffset;
                for (int i9 = 0; i9 < i4; i9++) {
                    int i10 = i6;
                    i6++;
                    if (bArr[i10] != 0) {
                        int i11 = i8 / 8;
                        data2[i11] = (byte) (data2[i11] | ((byte) (1 << ((7 - i8) & 7))));
                    }
                    i8++;
                }
                offset += scanlineStride;
            }
            return;
        }
        if (!(dataBuffer instanceof DataBufferShort) && !(dataBuffer instanceof DataBufferUShort)) {
            if (dataBuffer instanceof DataBufferInt) {
                int[] data3 = ((DataBufferInt) dataBuffer).getData();
                for (int i12 = 0; i12 < i5; i12++) {
                    int i13 = (offset * 32) + bitOffset;
                    for (int i14 = 0; i14 < i4; i14++) {
                        int i15 = i6;
                        i6++;
                        if (bArr[i15] != 0) {
                            int i16 = i13 / 32;
                            data3[i16] = data3[i16] | (1 << (31 - (i13 % 32)));
                        }
                        i13++;
                    }
                    offset += scanlineStride;
                }
                return;
            }
            return;
        }
        if (dataBuffer instanceof DataBufferShort) {
            data = ((DataBufferShort) dataBuffer).getData();
        } else {
            data = ((DataBufferUShort) dataBuffer).getData();
        }
        short[] sArr = data;
        for (int i17 = 0; i17 < i5; i17++) {
            int i18 = (offset * 16) + bitOffset;
            for (int i19 = 0; i19 < i4; i19++) {
                int i20 = i6;
                i6++;
                if (bArr[i20] != 0) {
                    int i21 = i18 / 16;
                    sArr[i21] = (short) (sArr[i21] | ((short) (1 << (15 - (i18 % 16)))));
                }
                i18++;
            }
            offset += scanlineStride;
        }
    }

    public static boolean isBinary(SampleModel sampleModel) {
        return (sampleModel instanceof MultiPixelPackedSampleModel) && ((MultiPixelPackedSampleModel) sampleModel).getPixelBitStride() == 1 && sampleModel.getNumBands() == 1;
    }

    public static ColorModel createColorModel(ColorSpace colorSpace, SampleModel sampleModel) {
        int i2;
        int i3;
        int i4;
        ColorSpace colorSpace2;
        ColorModel indexColorModel = null;
        if (sampleModel == null) {
            throw new IllegalArgumentException(I18N.getString("ImageUtil1"));
        }
        int numBands = sampleModel.getNumBands();
        if (numBands < 1 || numBands > 4) {
            return null;
        }
        int dataType = sampleModel.getDataType();
        if (sampleModel instanceof ComponentSampleModel) {
            if (dataType < 0 || dataType > 5) {
                return null;
            }
            if (colorSpace == null) {
                if (numBands <= 2) {
                    colorSpace2 = ColorSpace.getInstance(1003);
                } else {
                    colorSpace2 = ColorSpace.getInstance(1000);
                }
                colorSpace = colorSpace2;
            }
            boolean z2 = numBands == 2 || numBands == 4;
            int i5 = z2 ? 3 : 1;
            int dataTypeSize = DataBuffer.getDataTypeSize(dataType);
            int[] iArr = new int[numBands];
            for (int i6 = 0; i6 < numBands; i6++) {
                iArr[i6] = dataTypeSize;
            }
            indexColorModel = new ComponentColorModel(colorSpace, iArr, z2, false, i5, dataType);
        } else if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            int[] bitMasks = singlePixelPackedSampleModel.getBitMasks();
            int i7 = 0;
            int length = bitMasks.length;
            if (length <= 2) {
                int i8 = bitMasks[0];
                i4 = i8;
                i3 = i8;
                i2 = i8;
                if (length == 2) {
                    i7 = bitMasks[1];
                }
            } else {
                i2 = bitMasks[0];
                i3 = bitMasks[1];
                i4 = bitMasks[2];
                if (length == 4) {
                    i7 = bitMasks[3];
                }
            }
            int i9 = 0;
            for (int i10 : singlePixelPackedSampleModel.getSampleSize()) {
                i9 += i10;
            }
            if (colorSpace == null) {
                colorSpace = ColorSpace.getInstance(1000);
            }
            indexColorModel = new DirectColorModel(colorSpace, i9, i2, i3, i4, i7, false, sampleModel.getDataType());
        } else if (sampleModel instanceof MultiPixelPackedSampleModel) {
            int pixelBitStride = ((MultiPixelPackedSampleModel) sampleModel).getPixelBitStride();
            int i11 = 1 << pixelBitStride;
            byte[] bArr = new byte[i11];
            for (int i12 = 0; i12 < i11; i12++) {
                bArr[i12] = (byte) ((255 * i12) / (i11 - 1));
            }
            indexColorModel = new IndexColorModel(pixelBitStride, i11, bArr, bArr, bArr);
        }
        return indexColorModel;
    }

    public static int getElementSize(SampleModel sampleModel) {
        int dataTypeSize = DataBuffer.getDataTypeSize(sampleModel.getDataType());
        if (sampleModel instanceof MultiPixelPackedSampleModel) {
            MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
            return multiPixelPackedSampleModel.getSampleSize(0) * multiPixelPackedSampleModel.getNumBands();
        }
        if (sampleModel instanceof ComponentSampleModel) {
            return sampleModel.getNumBands() * dataTypeSize;
        }
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            return dataTypeSize;
        }
        return dataTypeSize * sampleModel.getNumBands();
    }

    public static long getTileSize(SampleModel sampleModel) {
        int dataTypeSize = DataBuffer.getDataTypeSize(sampleModel.getDataType());
        if (sampleModel instanceof MultiPixelPackedSampleModel) {
            MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
            return ((multiPixelPackedSampleModel.getScanlineStride() * multiPixelPackedSampleModel.getHeight()) + (((multiPixelPackedSampleModel.getDataBitOffset() + dataTypeSize) - 1) / dataTypeSize)) * ((dataTypeSize + 7) / 8);
        }
        if (sampleModel instanceof ComponentSampleModel) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            int[] bandOffsets = componentSampleModel.getBandOffsets();
            int iMax = bandOffsets[0];
            for (int i2 = 1; i2 < bandOffsets.length; i2++) {
                iMax = Math.max(iMax, bandOffsets[i2]);
            }
            long height = 0;
            int pixelStride = componentSampleModel.getPixelStride();
            int scanlineStride = componentSampleModel.getScanlineStride();
            if (iMax >= 0) {
                height = 0 + iMax + 1;
            }
            if (pixelStride > 0) {
                height += pixelStride * (sampleModel.getWidth() - 1);
            }
            if (scanlineStride > 0) {
                height += scanlineStride * (sampleModel.getHeight() - 1);
            }
            int[] bankIndices = componentSampleModel.getBankIndices();
            int iMax2 = bankIndices[0];
            for (int i3 = 1; i3 < bankIndices.length; i3++) {
                iMax2 = Math.max(iMax2, bankIndices[i3]);
            }
            return height * (iMax2 + 1) * ((dataTypeSize + 7) / 8);
        }
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            return ((singlePixelPackedSampleModel.getScanlineStride() * (singlePixelPackedSampleModel.getHeight() - 1)) + singlePixelPackedSampleModel.getWidth()) * ((dataTypeSize + 7) / 8);
        }
        return 0L;
    }

    public static long getBandSize(SampleModel sampleModel) {
        int dataTypeSize = DataBuffer.getDataTypeSize(sampleModel.getDataType());
        if (sampleModel instanceof ComponentSampleModel) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            int pixelStride = componentSampleModel.getPixelStride();
            int scanlineStride = componentSampleModel.getScanlineStride();
            long jMin = Math.min(pixelStride, scanlineStride);
            if (pixelStride > 0) {
                jMin += pixelStride * (sampleModel.getWidth() - 1);
            }
            if (scanlineStride > 0) {
                jMin += scanlineStride * (sampleModel.getHeight() - 1);
            }
            return jMin * ((dataTypeSize + 7) / 8);
        }
        return getTileSize(sampleModel);
    }

    public static boolean isIndicesForGrayscale(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int length;
        if (bArr.length != bArr2.length || bArr.length != bArr3.length || (length = bArr.length) != 256) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            byte b2 = (byte) i2;
            if (bArr[i2] != b2 || bArr2[i2] != b2 || bArr3[i2] != b2) {
                return false;
            }
        }
        return true;
    }

    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        String str = "";
        if (obj instanceof byte[]) {
            for (byte b2 : (byte[]) obj) {
                str = str + ((int) b2) + " ";
            }
            return str;
        }
        if (obj instanceof int[]) {
            for (int i2 : (int[]) obj) {
                str = str + i2 + " ";
            }
            return str;
        }
        if (obj instanceof short[]) {
            for (short s2 : (short[]) obj) {
                str = str + ((int) s2) + " ";
            }
            return str;
        }
        return obj.toString();
    }

    public static final void canEncodeImage(ImageWriter imageWriter, ImageTypeSpecifier imageTypeSpecifier) throws IIOException {
        ImageWriterSpi originatingProvider = imageWriter.getOriginatingProvider();
        if (imageTypeSpecifier != null && originatingProvider != null && !originatingProvider.canEncodeImage(imageTypeSpecifier)) {
            throw new IIOException(I18N.getString("ImageUtil2") + " " + imageWriter.getClass().getName());
        }
    }

    public static final void canEncodeImage(ImageWriter imageWriter, ColorModel colorModel, SampleModel sampleModel) throws IIOException {
        ImageTypeSpecifier imageTypeSpecifier = null;
        if (colorModel != null && sampleModel != null) {
            imageTypeSpecifier = new ImageTypeSpecifier(colorModel, sampleModel);
        }
        canEncodeImage(imageWriter, imageTypeSpecifier);
    }

    public static final boolean imageIsContiguous(RenderedImage renderedImage) {
        SampleModel sampleModel;
        if (renderedImage instanceof BufferedImage) {
            sampleModel = ((BufferedImage) renderedImage).getRaster().getSampleModel();
        } else {
            sampleModel = renderedImage.getSampleModel();
        }
        if (sampleModel instanceof ComponentSampleModel) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            if (componentSampleModel.getPixelStride() != componentSampleModel.getNumBands()) {
                return false;
            }
            int[] bandOffsets = componentSampleModel.getBandOffsets();
            for (int i2 = 0; i2 < bandOffsets.length; i2++) {
                if (bandOffsets[i2] != i2) {
                    return false;
                }
            }
            int[] bankIndices = componentSampleModel.getBankIndices();
            for (int i3 = 0; i3 < bandOffsets.length; i3++) {
                if (bankIndices[i3] != 0) {
                    return false;
                }
            }
            return true;
        }
        return isBinary(sampleModel);
    }
}
