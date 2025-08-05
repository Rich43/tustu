package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/image/BytePackedRaster.class */
public class BytePackedRaster extends SunWritableRaster {
    int dataBitOffset;
    int scanlineStride;
    int pixelBitStride;
    int bitMask;
    byte[] data;
    int shiftOffset;
    int type;
    private int maxX;
    private int maxY;

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
    }

    public BytePackedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public BytePackedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public BytePackedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, BytePackedRaster bytePackedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, bytePackedRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferByte)) {
            throw new RasterFormatException("BytePackedRasters must havebyte DataBuffers");
        }
        DataBufferByte dataBufferByte = (DataBufferByte) dataBuffer;
        this.data = stealData(dataBufferByte, 0);
        if (dataBufferByte.getNumBanks() != 1) {
            throw new RasterFormatException("DataBuffer for BytePackedRasters must only have 1 bank.");
        }
        int offset = dataBufferByte.getOffset();
        if (sampleModel instanceof MultiPixelPackedSampleModel) {
            MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) sampleModel;
            this.type = 11;
            this.pixelBitStride = multiPixelPackedSampleModel.getPixelBitStride();
            if (this.pixelBitStride != 1 && this.pixelBitStride != 2 && this.pixelBitStride != 4) {
                throw new RasterFormatException("BytePackedRasters must have a bit depth of 1, 2, or 4");
            }
            this.scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
            this.dataBitOffset = multiPixelPackedSampleModel.getDataBitOffset() + (offset * 8);
            this.dataBitOffset += ((rectangle.f12372x - point.f12370x) * this.pixelBitStride) + ((rectangle.f12373y - point.f12371y) * this.scanlineStride * 8);
            this.bitMask = (1 << this.pixelBitStride) - 1;
            this.shiftOffset = 8 - this.pixelBitStride;
            verify(false);
            return;
        }
        throw new RasterFormatException("BytePackedRasters must haveMultiPixelPackedSampleModel");
    }

    public int getDataBitOffset() {
        return this.dataBitOffset;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelBitStride() {
        return this.pixelBitStride;
    }

    public byte[] getDataStorage() {
        return this.data;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, Object obj) {
        byte[] bArr;
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            bArr = new byte[this.numDataElements];
        } else {
            bArr = (byte[]) obj;
        }
        int i4 = this.dataBitOffset + ((i2 - this.minX) * this.pixelBitStride);
        bArr[0] = (byte) (((this.data[((i3 - this.minY) * this.scanlineStride) + (i4 >> 3)] & 255) >> (this.shiftOffset - (i4 & 7))) & this.bitMask);
        return bArr;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        return getByteData(i2, i3, i4, i5, (byte[]) obj);
    }

    public Object getPixelData(int i2, int i3, int i4, int i5, Object obj) {
        byte[] bArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            bArr = new byte[this.numDataElements * i4 * i5];
        } else {
            bArr = (byte[]) obj;
        }
        int i6 = this.pixelBitStride;
        int i7 = this.dataBitOffset + ((i2 - this.minX) * i6);
        int i8 = (i3 - this.minY) * this.scanlineStride;
        int i9 = 0;
        byte[] bArr2 = this.data;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            for (int i12 = 0; i12 < i4; i12++) {
                int i13 = i9;
                i9++;
                bArr[i13] = (byte) (this.bitMask & (bArr2[i8 + (i11 >> 3)] >> (this.shiftOffset - (i11 & 7))));
                i11 += i6;
            }
            i8 += this.scanlineStride;
        }
        return bArr;
    }

    public byte[] getByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        return getByteData(i2, i3, i4, i5, bArr);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public byte[] getByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (bArr == null) {
            bArr = new byte[i4 * i5];
        }
        int i6 = this.pixelBitStride;
        int i7 = this.dataBitOffset + ((i2 - this.minX) * i6);
        int i8 = (i3 - this.minY) * this.scanlineStride;
        int i9 = 0;
        byte[] bArr2 = this.data;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            int i12 = 0;
            while (i12 < i4 && (i11 & 7) != 0) {
                int i13 = i9;
                i9++;
                bArr[i13] = (byte) (this.bitMask & (bArr2[i8 + (i11 >> 3)] >> (this.shiftOffset - (i11 & 7))));
                i11 += i6;
                i12++;
            }
            int i14 = i8 + (i11 >> 3);
            switch (i6) {
                case 1:
                    while (i12 < i4 - 7) {
                        int i15 = i14;
                        i14++;
                        byte b2 = bArr2[i15];
                        int i16 = i9;
                        int i17 = i9 + 1;
                        bArr[i16] = (byte) ((b2 >> 7) & 1);
                        int i18 = i17 + 1;
                        bArr[i17] = (byte) ((b2 >> 6) & 1);
                        int i19 = i18 + 1;
                        bArr[i18] = (byte) ((b2 >> 5) & 1);
                        int i20 = i19 + 1;
                        bArr[i19] = (byte) ((b2 >> 4) & 1);
                        int i21 = i20 + 1;
                        bArr[i20] = (byte) ((b2 >> 3) & 1);
                        int i22 = i21 + 1;
                        bArr[i21] = (byte) ((b2 >> 2) & 1);
                        int i23 = i22 + 1;
                        bArr[i22] = (byte) ((b2 >> 1) & 1);
                        i9 = i23 + 1;
                        bArr[i23] = (byte) (b2 & 1);
                        i11 += 8;
                        i12 += 8;
                    }
                    break;
                case 2:
                    while (i12 < i4 - 7) {
                        int i24 = i14;
                        int i25 = i14 + 1;
                        byte b3 = bArr2[i24];
                        int i26 = i9;
                        int i27 = i9 + 1;
                        bArr[i26] = (byte) ((b3 >> 6) & 3);
                        int i28 = i27 + 1;
                        bArr[i27] = (byte) ((b3 >> 4) & 3);
                        int i29 = i28 + 1;
                        bArr[i28] = (byte) ((b3 >> 2) & 3);
                        int i30 = i29 + 1;
                        bArr[i29] = (byte) (b3 & 3);
                        i14 = i25 + 1;
                        byte b4 = bArr2[i25];
                        int i31 = i30 + 1;
                        bArr[i30] = (byte) ((b4 >> 6) & 3);
                        int i32 = i31 + 1;
                        bArr[i31] = (byte) ((b4 >> 4) & 3);
                        int i33 = i32 + 1;
                        bArr[i32] = (byte) ((b4 >> 2) & 3);
                        i9 = i33 + 1;
                        bArr[i33] = (byte) (b4 & 3);
                        i11 += 16;
                        i12 += 8;
                    }
                    break;
                case 4:
                    while (i12 < i4 - 7) {
                        int i34 = i14;
                        int i35 = i14 + 1;
                        byte b5 = bArr2[i34];
                        int i36 = i9;
                        int i37 = i9 + 1;
                        bArr[i36] = (byte) ((b5 >> 4) & 15);
                        int i38 = i37 + 1;
                        bArr[i37] = (byte) (b5 & 15);
                        int i39 = i35 + 1;
                        byte b6 = bArr2[i35];
                        int i40 = i38 + 1;
                        bArr[i38] = (byte) ((b6 >> 4) & 15);
                        int i41 = i40 + 1;
                        bArr[i40] = (byte) (b6 & 15);
                        int i42 = i39 + 1;
                        byte b7 = bArr2[i39];
                        int i43 = i41 + 1;
                        bArr[i41] = (byte) ((b7 >> 4) & 15);
                        int i44 = i43 + 1;
                        bArr[i43] = (byte) (b7 & 15);
                        i14 = i42 + 1;
                        byte b8 = bArr2[i42];
                        int i45 = i44 + 1;
                        bArr[i44] = (byte) ((b8 >> 4) & 15);
                        i9 = i45 + 1;
                        bArr[i45] = (byte) (b8 & 15);
                        i11 += 32;
                        i12 += 8;
                    }
                    break;
            }
            while (i12 < i4) {
                int i46 = i9;
                i9++;
                bArr[i46] = (byte) (this.bitMask & (bArr2[i8 + (i11 >> 3)] >> (this.shiftOffset - (i11 & 7))));
                i11 += i6;
                i12++;
            }
            i8 += this.scanlineStride;
        }
        return bArr;
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = this.dataBitOffset + ((i2 - this.minX) * this.pixelBitStride);
        int i5 = ((i3 - this.minY) * this.scanlineStride) + (i4 >> 3);
        int i6 = this.shiftOffset - (i4 & 7);
        this.data[i5] = (byte) (((byte) (this.data[i5] & ((this.bitMask << i6) ^ (-1)))) | ((((byte[]) obj)[0] & this.bitMask) << i6));
        markDirty();
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Raster raster) {
        if (!(raster instanceof BytePackedRaster) || ((BytePackedRaster) raster).pixelBitStride != this.pixelBitStride) {
            super.setDataElements(i2, i3, raster);
            return;
        }
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        int i4 = minX + i2;
        int i5 = minY + i3;
        int width = raster.getWidth();
        int height = raster.getHeight();
        if (i4 < this.minX || i5 < this.minY || i4 + width > this.maxX || i5 + height > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        setDataElements(i4, i5, minX, minY, width, height, (BytePackedRaster) raster);
    }

    private void setDataElements(int i2, int i3, int i4, int i5, int i6, int i7, BytePackedRaster bytePackedRaster) {
        if (i6 <= 0 || i7 <= 0) {
            return;
        }
        byte[] bArr = bytePackedRaster.data;
        byte[] bArr2 = this.data;
        int i8 = bytePackedRaster.scanlineStride;
        int i9 = this.scanlineStride;
        int i10 = bytePackedRaster.dataBitOffset + (8 * (i5 - bytePackedRaster.minY) * i8) + ((i4 - bytePackedRaster.minX) * bytePackedRaster.pixelBitStride);
        int i11 = this.dataBitOffset + (8 * (i3 - this.minY) * i9) + ((i2 - this.minX) * this.pixelBitStride);
        int i12 = i6 * this.pixelBitStride;
        if ((i10 & 7) == (i11 & 7)) {
            int i13 = i11 & 7;
            if (i13 != 0) {
                int i14 = 8 - i13;
                int i15 = i10 >> 3;
                int i16 = i11 >> 3;
                int i17 = 255 >> i13;
                if (i12 < i14) {
                    i17 &= 255 << (i14 - i12);
                    i14 = i12;
                }
                for (int i18 = 0; i18 < i7; i18++) {
                    bArr2[i16] = (byte) ((bArr2[i16] & (i17 ^ (-1))) | (bArr[i15] & i17));
                    i15 += i8;
                    i16 += i9;
                }
                i10 += i14;
                i11 += i14;
                i12 -= i14;
            }
            if (i12 >= 8) {
                int i19 = i10 >> 3;
                int i20 = i11 >> 3;
                int i21 = i12 >> 3;
                if (i21 == i8 && i8 == i9) {
                    System.arraycopy(bArr, i19, bArr2, i20, i8 * i7);
                } else {
                    for (int i22 = 0; i22 < i7; i22++) {
                        System.arraycopy(bArr, i19, bArr2, i20, i21);
                        i19 += i8;
                        i20 += i9;
                    }
                }
                int i23 = i21 * 8;
                i10 += i23;
                i11 += i23;
                i12 -= i23;
            }
            if (i12 > 0) {
                int i24 = i10 >> 3;
                int i25 = i11 >> 3;
                int i26 = (NormalizerImpl.CC_MASK >> i12) & 255;
                for (int i27 = 0; i27 < i7; i27++) {
                    bArr2[i25] = (byte) ((bArr2[i25] & (i26 ^ (-1))) | (bArr[i24] & i26));
                    i24 += i8;
                    i25 += i9;
                }
            }
        } else {
            int i28 = i11 & 7;
            if (i28 != 0 || i12 < 8) {
                int i29 = 8 - i28;
                int i30 = i10 >> 3;
                int i31 = i11 >> 3;
                int i32 = i10 & 7;
                int i33 = 8 - i32;
                int i34 = 255 >> i28;
                if (i12 < i29) {
                    i34 &= 255 << (i29 - i12);
                    i29 = i12;
                }
                int length = bArr.length - 1;
                for (int i35 = 0; i35 < i7; i35++) {
                    byte b2 = bArr[i30];
                    byte b3 = 0;
                    if (i30 < length) {
                        b3 = bArr[i30 + 1];
                    }
                    bArr2[i31] = (byte) ((bArr2[i31] & (i34 ^ (-1))) | ((((b2 << i32) | ((b3 & 255) >> i33)) >> i28) & i34));
                    i30 += i8;
                    i31 += i9;
                }
                i10 += i29;
                i11 += i29;
                i12 -= i29;
            }
            if (i12 >= 8) {
                int i36 = i10 >> 3;
                int i37 = i11 >> 3;
                int i38 = i12 >> 3;
                int i39 = i10 & 7;
                int i40 = 8 - i39;
                for (int i41 = 0; i41 < i7; i41++) {
                    int i42 = i36 + (i41 * i8);
                    int i43 = i37 + (i41 * i9);
                    byte b4 = bArr[i42];
                    for (int i44 = 0; i44 < i38; i44++) {
                        byte b5 = bArr[i42 + 1];
                        bArr2[i43] = (byte) ((b4 << i39) | ((b5 & 255) >> i40));
                        b4 = b5;
                        i42++;
                        i43++;
                    }
                }
                int i45 = i38 * 8;
                i10 += i45;
                i11 += i45;
                i12 -= i45;
            }
            if (i12 > 0) {
                int i46 = i10 >> 3;
                int i47 = i11 >> 3;
                int i48 = (NormalizerImpl.CC_MASK >> i12) & 255;
                int i49 = i10 & 7;
                int i50 = 8 - i49;
                int length2 = bArr.length - 1;
                for (int i51 = 0; i51 < i7; i51++) {
                    byte b6 = bArr[i46];
                    byte b7 = 0;
                    if (i46 < length2) {
                        b7 = bArr[i46 + 1];
                    }
                    bArr2[i47] = (byte) ((bArr2[i47] & (i48 ^ (-1))) | (((b6 << i49) | ((b7 & 255) >> i50)) & i48));
                    i46 += i8;
                    i47 += i9;
                }
            }
        }
        markDirty();
    }

    @Override // java.awt.image.WritableRaster
    public void setRect(int i2, int i3, Raster raster) {
        if (!(raster instanceof BytePackedRaster) || ((BytePackedRaster) raster).pixelBitStride != this.pixelBitStride) {
            super.setRect(i2, i3, raster);
            return;
        }
        int width = raster.getWidth();
        int height = raster.getHeight();
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        int i4 = i2 + minX;
        int i5 = i3 + minY;
        if (i4 < this.minX) {
            int i6 = this.minX - i4;
            width -= i6;
            minX += i6;
            i4 = this.minX;
        }
        if (i5 < this.minY) {
            int i7 = this.minY - i5;
            height -= i7;
            minY += i7;
            i5 = this.minY;
        }
        if (i4 + width > this.maxX) {
            width = this.maxX - i4;
        }
        if (i5 + height > this.maxY) {
            height = this.maxY - i5;
        }
        setDataElements(i4, i5, minX, minY, width, height, (BytePackedRaster) raster);
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        putByteData(i2, i3, i4, i5, (byte[]) obj);
    }

    public void putByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        putByteData(i2, i3, i4, i5, bArr);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void putByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (i4 == 0 || i5 == 0) {
            return;
        }
        int i6 = this.pixelBitStride;
        int i7 = this.dataBitOffset + ((i2 - this.minX) * i6);
        int i8 = (i3 - this.minY) * this.scanlineStride;
        int i9 = 0;
        byte[] bArr2 = this.data;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            int i12 = 0;
            while (i12 < i4 && (i11 & 7) != 0) {
                int i13 = this.shiftOffset - (i11 & 7);
                int i14 = i9;
                i9++;
                bArr2[i8 + (i11 >> 3)] = (byte) ((bArr2[i8 + (i11 >> 3)] & ((this.bitMask << i13) ^ (-1))) | ((bArr[i14] & this.bitMask) << i13));
                i11 += i6;
                i12++;
            }
            int i15 = i8 + (i11 >> 3);
            switch (i6) {
                case 1:
                    while (i12 < i4 - 7) {
                        int i16 = i9;
                        int i17 = i9 + 1;
                        int i18 = i17 + 1;
                        int i19 = ((bArr[i16] & 1) << 7) | ((bArr[i17] & 1) << 6);
                        int i20 = i18 + 1;
                        int i21 = i19 | ((bArr[i18] & 1) << 5);
                        int i22 = i20 + 1;
                        int i23 = i21 | ((bArr[i20] & 1) << 4);
                        int i24 = i22 + 1;
                        int i25 = i23 | ((bArr[i22] & 1) << 3);
                        int i26 = i24 + 1;
                        int i27 = i25 | ((bArr[i24] & 1) << 2);
                        int i28 = i26 + 1;
                        int i29 = i27 | ((bArr[i26] & 1) << 1);
                        i9 = i28 + 1;
                        int i30 = i15;
                        i15++;
                        bArr2[i30] = (byte) (i29 | (bArr[i28] & 1));
                        i11 += 8;
                        i12 += 8;
                    }
                    break;
                case 2:
                    while (i12 < i4 - 7) {
                        int i31 = i9;
                        int i32 = i9 + 1;
                        int i33 = i32 + 1;
                        int i34 = ((bArr[i31] & 3) << 6) | ((bArr[i32] & 3) << 4);
                        int i35 = i33 + 1;
                        int i36 = i34 | ((bArr[i33] & 3) << 2);
                        int i37 = i35 + 1;
                        int i38 = i15;
                        int i39 = i15 + 1;
                        bArr2[i38] = (byte) (i36 | (bArr[i35] & 3));
                        int i40 = i37 + 1;
                        int i41 = (bArr[i37] & 3) << 6;
                        int i42 = i40 + 1;
                        int i43 = i41 | ((bArr[i40] & 3) << 4);
                        int i44 = i42 + 1;
                        int i45 = i43 | ((bArr[i42] & 3) << 2);
                        i9 = i44 + 1;
                        i15 = i39 + 1;
                        bArr2[i39] = (byte) (i45 | (bArr[i44] & 3));
                        i11 += 16;
                        i12 += 8;
                    }
                    break;
                case 4:
                    while (i12 < i4 - 7) {
                        int i46 = i9;
                        int i47 = i9 + 1;
                        int i48 = i47 + 1;
                        int i49 = ((bArr[i46] & 15) << 4) | (bArr[i47] & 15);
                        int i50 = i15;
                        int i51 = i15 + 1;
                        bArr2[i50] = (byte) i49;
                        int i52 = i48 + 1;
                        int i53 = (bArr[i48] & 15) << 4;
                        int i54 = i52 + 1;
                        int i55 = i51 + 1;
                        bArr2[i51] = (byte) (i53 | (bArr[i52] & 15));
                        int i56 = i54 + 1;
                        int i57 = (bArr[i54] & 15) << 4;
                        int i58 = i56 + 1;
                        int i59 = i55 + 1;
                        bArr2[i55] = (byte) (i57 | (bArr[i56] & 15));
                        int i60 = i58 + 1;
                        int i61 = (bArr[i58] & 15) << 4;
                        i9 = i60 + 1;
                        i15 = i59 + 1;
                        bArr2[i59] = (byte) (i61 | (bArr[i60] & 15));
                        i11 += 32;
                        i12 += 8;
                    }
                    break;
            }
            while (i12 < i4) {
                int i62 = this.shiftOffset - (i11 & 7);
                int i63 = i9;
                i9++;
                bArr2[i8 + (i11 >> 3)] = (byte) ((bArr2[i8 + (i11 >> 3)] & ((this.bitMask << i62) ^ (-1))) | ((bArr[i63] & this.bitMask) << i62));
                i11 += i6;
                i12++;
            }
            i8 += this.scanlineStride;
        }
        markDirty();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // java.awt.image.Raster
    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr == null) {
            iArr = new int[i4 * i5];
        }
        int i6 = this.pixelBitStride;
        int i7 = this.dataBitOffset + ((i2 - this.minX) * i6);
        int i8 = (i3 - this.minY) * this.scanlineStride;
        int i9 = 0;
        byte[] bArr = this.data;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            int i12 = 0;
            while (i12 < i4 && (i11 & 7) != 0) {
                int i13 = i9;
                i9++;
                iArr[i13] = this.bitMask & (bArr[i8 + (i11 >> 3)] >> (this.shiftOffset - (i11 & 7)));
                i11 += i6;
                i12++;
            }
            int i14 = i8 + (i11 >> 3);
            switch (i6) {
                case 1:
                    while (i12 < i4 - 7) {
                        int i15 = i14;
                        i14++;
                        byte b2 = bArr[i15];
                        int i16 = i9;
                        int i17 = i9 + 1;
                        iArr[i16] = (b2 >> 7) & 1;
                        int i18 = i17 + 1;
                        iArr[i17] = (b2 >> 6) & 1;
                        int i19 = i18 + 1;
                        iArr[i18] = (b2 >> 5) & 1;
                        int i20 = i19 + 1;
                        iArr[i19] = (b2 >> 4) & 1;
                        int i21 = i20 + 1;
                        iArr[i20] = (b2 >> 3) & 1;
                        int i22 = i21 + 1;
                        iArr[i21] = (b2 >> 2) & 1;
                        int i23 = i22 + 1;
                        iArr[i22] = (b2 >> 1) & 1;
                        i9 = i23 + 1;
                        iArr[i23] = b2 & 1;
                        i11 += 8;
                        i12 += 8;
                    }
                    break;
                case 2:
                    while (i12 < i4 - 7) {
                        int i24 = i14;
                        int i25 = i14 + 1;
                        byte b3 = bArr[i24];
                        int i26 = i9;
                        int i27 = i9 + 1;
                        iArr[i26] = (b3 >> 6) & 3;
                        int i28 = i27 + 1;
                        iArr[i27] = (b3 >> 4) & 3;
                        int i29 = i28 + 1;
                        iArr[i28] = (b3 >> 2) & 3;
                        int i30 = i29 + 1;
                        iArr[i29] = b3 & 3;
                        i14 = i25 + 1;
                        byte b4 = bArr[i25];
                        int i31 = i30 + 1;
                        iArr[i30] = (b4 >> 6) & 3;
                        int i32 = i31 + 1;
                        iArr[i31] = (b4 >> 4) & 3;
                        int i33 = i32 + 1;
                        iArr[i32] = (b4 >> 2) & 3;
                        i9 = i33 + 1;
                        iArr[i33] = b4 & 3;
                        i11 += 16;
                        i12 += 8;
                    }
                    break;
                case 4:
                    while (i12 < i4 - 7) {
                        int i34 = i14;
                        int i35 = i14 + 1;
                        byte b5 = bArr[i34];
                        int i36 = i9;
                        int i37 = i9 + 1;
                        iArr[i36] = (b5 >> 4) & 15;
                        int i38 = i37 + 1;
                        iArr[i37] = b5 & 15;
                        int i39 = i35 + 1;
                        byte b6 = bArr[i35];
                        int i40 = i38 + 1;
                        iArr[i38] = (b6 >> 4) & 15;
                        int i41 = i40 + 1;
                        iArr[i40] = b6 & 15;
                        int i42 = i39 + 1;
                        byte b7 = bArr[i39];
                        int i43 = i41 + 1;
                        iArr[i41] = (b7 >> 4) & 15;
                        int i44 = i43 + 1;
                        iArr[i43] = b7 & 15;
                        i14 = i42 + 1;
                        byte b8 = bArr[i42];
                        int i45 = i44 + 1;
                        iArr[i44] = (b8 >> 4) & 15;
                        i9 = i45 + 1;
                        iArr[i45] = b8 & 15;
                        i11 += 32;
                        i12 += 8;
                    }
                    break;
            }
            while (i12 < i4) {
                int i46 = i9;
                i9++;
                iArr[i46] = this.bitMask & (bArr[i8 + (i11 >> 3)] >> (this.shiftOffset - (i11 & 7)));
                i11 += i6;
                i12++;
            }
            i8 += this.scanlineStride;
        }
        return iArr;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // java.awt.image.WritableRaster
    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = this.pixelBitStride;
        int i7 = this.dataBitOffset + ((i2 - this.minX) * i6);
        int i8 = (i3 - this.minY) * this.scanlineStride;
        int i9 = 0;
        byte[] bArr = this.data;
        for (int i10 = 0; i10 < i5; i10++) {
            int i11 = i7;
            int i12 = 0;
            while (i12 < i4 && (i11 & 7) != 0) {
                int i13 = this.shiftOffset - (i11 & 7);
                int i14 = i9;
                i9++;
                bArr[i8 + (i11 >> 3)] = (byte) ((bArr[i8 + (i11 >> 3)] & ((this.bitMask << i13) ^ (-1))) | ((iArr[i14] & this.bitMask) << i13));
                i11 += i6;
                i12++;
            }
            int i15 = i8 + (i11 >> 3);
            switch (i6) {
                case 1:
                    while (i12 < i4 - 7) {
                        int i16 = i9;
                        int i17 = i9 + 1;
                        int i18 = i17 + 1;
                        int i19 = ((iArr[i16] & 1) << 7) | ((iArr[i17] & 1) << 6);
                        int i20 = i18 + 1;
                        int i21 = i19 | ((iArr[i18] & 1) << 5);
                        int i22 = i20 + 1;
                        int i23 = i21 | ((iArr[i20] & 1) << 4);
                        int i24 = i22 + 1;
                        int i25 = i23 | ((iArr[i22] & 1) << 3);
                        int i26 = i24 + 1;
                        int i27 = i25 | ((iArr[i24] & 1) << 2);
                        int i28 = i26 + 1;
                        int i29 = i27 | ((iArr[i26] & 1) << 1);
                        i9 = i28 + 1;
                        int i30 = i15;
                        i15++;
                        bArr[i30] = (byte) (i29 | (iArr[i28] & 1));
                        i11 += 8;
                        i12 += 8;
                    }
                    break;
                case 2:
                    while (i12 < i4 - 7) {
                        int i31 = i9;
                        int i32 = i9 + 1;
                        int i33 = i32 + 1;
                        int i34 = ((iArr[i31] & 3) << 6) | ((iArr[i32] & 3) << 4);
                        int i35 = i33 + 1;
                        int i36 = i34 | ((iArr[i33] & 3) << 2);
                        int i37 = i35 + 1;
                        int i38 = i15;
                        int i39 = i15 + 1;
                        bArr[i38] = (byte) (i36 | (iArr[i35] & 3));
                        int i40 = i37 + 1;
                        int i41 = (iArr[i37] & 3) << 6;
                        int i42 = i40 + 1;
                        int i43 = i41 | ((iArr[i40] & 3) << 4);
                        int i44 = i42 + 1;
                        int i45 = i43 | ((iArr[i42] & 3) << 2);
                        i9 = i44 + 1;
                        i15 = i39 + 1;
                        bArr[i39] = (byte) (i45 | (iArr[i44] & 3));
                        i11 += 16;
                        i12 += 8;
                    }
                    break;
                case 4:
                    while (i12 < i4 - 7) {
                        int i46 = i9;
                        int i47 = i9 + 1;
                        int i48 = i47 + 1;
                        int i49 = ((iArr[i46] & 15) << 4) | (iArr[i47] & 15);
                        int i50 = i15;
                        int i51 = i15 + 1;
                        bArr[i50] = (byte) i49;
                        int i52 = i48 + 1;
                        int i53 = (iArr[i48] & 15) << 4;
                        int i54 = i52 + 1;
                        int i55 = i51 + 1;
                        bArr[i51] = (byte) (i53 | (iArr[i52] & 15));
                        int i56 = i54 + 1;
                        int i57 = (iArr[i54] & 15) << 4;
                        int i58 = i56 + 1;
                        int i59 = i55 + 1;
                        bArr[i55] = (byte) (i57 | (iArr[i56] & 15));
                        int i60 = i58 + 1;
                        int i61 = (iArr[i58] & 15) << 4;
                        i9 = i60 + 1;
                        i15 = i59 + 1;
                        bArr[i59] = (byte) (i61 | (iArr[i60] & 15));
                        i11 += 32;
                        i12 += 8;
                    }
                    break;
            }
            while (i12 < i4) {
                int i62 = this.shiftOffset - (i11 & 7);
                int i63 = i9;
                i9++;
                bArr[i8 + (i11 >> 3)] = (byte) ((bArr[i8 + (i11 >> 3)] & ((this.bitMask << i62) ^ (-1))) | ((iArr[i63] & this.bitMask) << i62));
                i11 += i6;
                i12++;
            }
            i8 += this.scanlineStride;
        }
        markDirty();
    }

    @Override // java.awt.image.Raster
    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        return createWritableChild(i2, i3, i4, i5, i6, i7, iArr);
    }

    @Override // java.awt.image.WritableRaster
    public WritableRaster createWritableChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        SampleModel sampleModelCreateSubsetSampleModel;
        if (i2 < this.minX) {
            throw new RasterFormatException("x lies outside the raster");
        }
        if (i3 < this.minY) {
            throw new RasterFormatException("y lies outside the raster");
        }
        if (i2 + i4 < i2 || i2 + i4 > this.minX + this.width) {
            throw new RasterFormatException("(x + width) is outside of Raster");
        }
        if (i3 + i5 < i3 || i3 + i5 > this.minY + this.height) {
            throw new RasterFormatException("(y + height) is outside of Raster");
        }
        if (iArr != null) {
            sampleModelCreateSubsetSampleModel = this.sampleModel.createSubsetSampleModel(iArr);
        } else {
            sampleModelCreateSubsetSampleModel = this.sampleModel;
        }
        return new BytePackedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new BytePackedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    private void verify(boolean z2) {
        if (this.dataBitOffset < 0) {
            throw new RasterFormatException("Data offsets must be >= 0");
        }
        if (this.width <= 0 || this.height <= 0 || this.height > Integer.MAX_VALUE / this.width) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        if (this.width - 1 > Integer.MAX_VALUE / this.pixelBitStride) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        if (this.minX - this.sampleModelTranslateX < 0 || this.minY - this.sampleModelTranslateY < 0) {
            throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
        }
        if (this.scanlineStride < 0 || this.scanlineStride > Integer.MAX_VALUE / this.height) {
            throw new RasterFormatException("Invalid scanline stride");
        }
        if ((this.height > 1 || this.minY - this.sampleModelTranslateY > 0) && this.scanlineStride > this.data.length) {
            throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
        }
        long j2 = (((this.dataBitOffset + (((this.height - 1) * this.scanlineStride) * 8)) + ((this.width - 1) * this.pixelBitStride)) + this.pixelBitStride) - 1;
        if (j2 < 0 || j2 / 8 >= this.data.length) {
            throw new RasterFormatException("raster dimensions overflow array bounds");
        }
        if (z2 && this.height > 1 && ((this.width * this.pixelBitStride) - 1) / 8 >= this.scanlineStride) {
            throw new RasterFormatException("data for adjacent scanlines overlaps");
        }
    }

    public String toString() {
        return new String("BytePackedRaster: width = " + this.width + " height = " + this.height + " #channels " + this.numBands + " xOff = " + this.sampleModelTranslateX + " yOff = " + this.sampleModelTranslateY);
    }
}
