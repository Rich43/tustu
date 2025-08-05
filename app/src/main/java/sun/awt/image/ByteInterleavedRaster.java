package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/ByteInterleavedRaster.class */
public class ByteInterleavedRaster extends ByteComponentRaster {
    boolean inOrder;
    int dbOffset;
    int dbOffsetPacked;
    boolean packed;
    int[] bitMasks;
    int[] bitOffsets;
    private int maxX;
    private int maxY;

    public ByteInterleavedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ByteInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    private boolean isInterleaved(ComponentSampleModel componentSampleModel) {
        int numBands = this.sampleModel.getNumBands();
        if (numBands == 1) {
            return true;
        }
        int[] bankIndices = componentSampleModel.getBankIndices();
        for (int i2 = 0; i2 < numBands; i2++) {
            if (bankIndices[i2] != 0) {
                return false;
            }
        }
        int[] bandOffsets = componentSampleModel.getBandOffsets();
        int i3 = bandOffsets[0];
        int i4 = i3;
        for (int i5 = 1; i5 < numBands; i5++) {
            int i6 = bandOffsets[i5];
            if (i6 < i3) {
                i3 = i6;
            }
            if (i6 > i4) {
                i4 = i6;
            }
        }
        if (i4 - i3 >= componentSampleModel.getPixelStride()) {
            return false;
        }
        return true;
    }

    public ByteInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, ByteInterleavedRaster byteInterleavedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, byteInterleavedRaster);
        this.packed = false;
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferByte)) {
            throw new RasterFormatException("ByteInterleavedRasters must have byte DataBuffers");
        }
        DataBufferByte dataBufferByte = (DataBufferByte) dataBuffer;
        this.data = stealData(dataBufferByte, 0);
        int i2 = rectangle.f12372x - point.f12370x;
        int i3 = rectangle.f12373y - point.f12371y;
        if ((sampleModel instanceof PixelInterleavedSampleModel) || ((sampleModel instanceof ComponentSampleModel) && isInterleaved((ComponentSampleModel) sampleModel))) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            this.scanlineStride = componentSampleModel.getScanlineStride();
            this.pixelStride = componentSampleModel.getPixelStride();
            this.dataOffsets = componentSampleModel.getBandOffsets();
            for (int i4 = 0; i4 < getNumDataElements(); i4++) {
                int[] iArr = this.dataOffsets;
                int i5 = i4;
                iArr[i5] = iArr[i5] + (i2 * this.pixelStride) + (i3 * this.scanlineStride);
            }
        } else if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            this.packed = true;
            this.bitMasks = singlePixelPackedSampleModel.getBitMasks();
            this.bitOffsets = singlePixelPackedSampleModel.getBitOffsets();
            this.scanlineStride = singlePixelPackedSampleModel.getScanlineStride();
            this.pixelStride = 1;
            this.dataOffsets = new int[1];
            this.dataOffsets[0] = dataBufferByte.getOffset();
            int[] iArr2 = this.dataOffsets;
            iArr2[0] = iArr2[0] + (i2 * this.pixelStride) + (i3 * this.scanlineStride);
        } else {
            throw new RasterFormatException("ByteInterleavedRasters must have PixelInterleavedSampleModel, SinglePixelPackedSampleModel or interleaved ComponentSampleModel.  Sample model is " + ((Object) sampleModel));
        }
        this.bandOffset = this.dataOffsets[0];
        this.dbOffsetPacked = (dataBuffer.getOffset() - (this.sampleModelTranslateY * this.scanlineStride)) - (this.sampleModelTranslateX * this.pixelStride);
        this.dbOffset = this.dbOffsetPacked - ((i2 * this.pixelStride) + (i3 * this.scanlineStride));
        this.inOrder = false;
        if (this.numDataElements == this.pixelStride) {
            this.inOrder = true;
            int i6 = 1;
            while (true) {
                if (i6 >= this.numDataElements) {
                    break;
                }
                if (this.dataOffsets[i6] - this.dataOffsets[0] == i6) {
                    i6++;
                } else {
                    this.inOrder = false;
                    break;
                }
            }
        }
        verify();
    }

    @Override // sun.awt.image.ByteComponentRaster
    public int[] getDataOffsets() {
        return (int[]) this.dataOffsets.clone();
    }

    @Override // sun.awt.image.ByteComponentRaster
    public int getDataOffset(int i2) {
        return this.dataOffsets[i2];
    }

    @Override // sun.awt.image.ByteComponentRaster
    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override // sun.awt.image.ByteComponentRaster
    public int getPixelStride() {
        return this.pixelStride;
    }

    @Override // sun.awt.image.ByteComponentRaster
    public byte[] getDataStorage() {
        return this.data;
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.Raster
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
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            bArr[i5] = this.data[this.dataOffsets[i5] + i4];
        }
        return bArr;
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        return getByteData(i2, i3, i4, i5, (byte[]) obj);
    }

    @Override // sun.awt.image.ByteComponentRaster
    public byte[] getByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (bArr == null) {
            bArr = new byte[i4 * i5];
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.pixelStride == 1) {
            if (this.scanlineStride == i4) {
                System.arraycopy(this.data, i7, bArr, 0, i4 * i5);
            } else {
                int i9 = 0;
                while (i9 < i5) {
                    System.arraycopy(this.data, i7, bArr, i8, i4);
                    i8 += i4;
                    i9++;
                    i7 += this.scanlineStride;
                }
            }
        } else {
            int i10 = 0;
            while (i10 < i5) {
                int i11 = i7;
                int i12 = 0;
                while (i12 < i4) {
                    int i13 = i8;
                    i8++;
                    bArr[i13] = this.data[i11];
                    i12++;
                    i11 += this.pixelStride;
                }
                i10++;
                i7 += this.scanlineStride;
            }
        }
        return bArr;
    }

    @Override // sun.awt.image.ByteComponentRaster
    public byte[] getByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (bArr == null) {
            bArr = new byte[this.numDataElements * i4 * i5];
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        if (this.inOrder) {
            int i8 = i6 + this.dataOffsets[0];
            int i9 = i4 * this.pixelStride;
            if (this.scanlineStride == i9) {
                System.arraycopy(this.data, i8, bArr, 0, i9 * i5);
            } else {
                int i10 = 0;
                while (i10 < i5) {
                    System.arraycopy(this.data, i8, bArr, i7, i9);
                    i7 += i9;
                    i10++;
                    i8 += this.scanlineStride;
                }
            }
        } else if (this.numDataElements == 1) {
            int i11 = i6 + this.dataOffsets[0];
            int i12 = 0;
            while (i12 < i5) {
                int i13 = i11;
                int i14 = 0;
                while (i14 < i4) {
                    int i15 = i7;
                    i7++;
                    bArr[i15] = this.data[i13];
                    i14++;
                    i13 += this.pixelStride;
                }
                i12++;
                i11 += this.scanlineStride;
            }
        } else if (this.numDataElements == 2) {
            int i16 = i6 + this.dataOffsets[0];
            int i17 = this.dataOffsets[1] - this.dataOffsets[0];
            int i18 = 0;
            while (i18 < i5) {
                int i19 = i16;
                int i20 = 0;
                while (i20 < i4) {
                    int i21 = i7;
                    int i22 = i7 + 1;
                    bArr[i21] = this.data[i19];
                    i7 = i22 + 1;
                    bArr[i22] = this.data[i19 + i17];
                    i20++;
                    i19 += this.pixelStride;
                }
                i18++;
                i16 += this.scanlineStride;
            }
        } else if (this.numDataElements == 3) {
            int i23 = i6 + this.dataOffsets[0];
            int i24 = this.dataOffsets[1] - this.dataOffsets[0];
            int i25 = this.dataOffsets[2] - this.dataOffsets[0];
            int i26 = 0;
            while (i26 < i5) {
                int i27 = i23;
                int i28 = 0;
                while (i28 < i4) {
                    int i29 = i7;
                    int i30 = i7 + 1;
                    bArr[i29] = this.data[i27];
                    int i31 = i30 + 1;
                    bArr[i30] = this.data[i27 + i24];
                    i7 = i31 + 1;
                    bArr[i31] = this.data[i27 + i25];
                    i28++;
                    i27 += this.pixelStride;
                }
                i26++;
                i23 += this.scanlineStride;
            }
        } else if (this.numDataElements == 4) {
            int i32 = i6 + this.dataOffsets[0];
            int i33 = this.dataOffsets[1] - this.dataOffsets[0];
            int i34 = this.dataOffsets[2] - this.dataOffsets[0];
            int i35 = this.dataOffsets[3] - this.dataOffsets[0];
            int i36 = 0;
            while (i36 < i5) {
                int i37 = i32;
                int i38 = 0;
                while (i38 < i4) {
                    int i39 = i7;
                    int i40 = i7 + 1;
                    bArr[i39] = this.data[i37];
                    int i41 = i40 + 1;
                    bArr[i40] = this.data[i37 + i33];
                    int i42 = i41 + 1;
                    bArr[i41] = this.data[i37 + i34];
                    i7 = i42 + 1;
                    bArr[i42] = this.data[i37 + i35];
                    i38++;
                    i37 += this.pixelStride;
                }
                i36++;
                i32 += this.scanlineStride;
            }
        } else {
            int i43 = 0;
            while (i43 < i5) {
                int i44 = i6;
                int i45 = 0;
                while (i45 < i4) {
                    for (int i46 = 0; i46 < this.numDataElements; i46++) {
                        int i47 = i7;
                        i7++;
                        bArr[i47] = this.data[this.dataOffsets[i46] + i44];
                    }
                    i45++;
                    i44 += this.pixelStride;
                }
                i43++;
                i6 += this.scanlineStride;
            }
        }
        return bArr;
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        byte[] bArr = (byte[]) obj;
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            this.data[this.dataOffsets[i5] + i4] = bArr[i5];
        }
        markDirty();
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Raster raster) {
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        int i4 = i2 + minX;
        int i5 = i3 + minY;
        int width = raster.getWidth();
        int height = raster.getHeight();
        if (i4 < this.minX || i5 < this.minY || i4 + width > this.maxX || i5 + height > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        setDataElements(i4, i5, minX, minY, width, height, raster);
    }

    private void setDataElements(int i2, int i3, int i4, int i5, int i6, int i7, Raster raster) {
        if (i6 <= 0 || i7 <= 0) {
            return;
        }
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        Object dataElements = null;
        if (raster instanceof ByteInterleavedRaster) {
            ByteInterleavedRaster byteInterleavedRaster = (ByteInterleavedRaster) raster;
            byte[] dataStorage = byteInterleavedRaster.getDataStorage();
            if (this.inOrder && byteInterleavedRaster.inOrder && this.pixelStride == byteInterleavedRaster.pixelStride) {
                int dataOffset = byteInterleavedRaster.getDataOffset(0);
                int scanlineStride = byteInterleavedRaster.getScanlineStride();
                int pixelStride = dataOffset + ((i5 - minY) * scanlineStride) + ((i4 - minX) * byteInterleavedRaster.getPixelStride());
                int i8 = this.dataOffsets[0] + ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
                int i9 = i6 * this.pixelStride;
                for (int i10 = 0; i10 < i7; i10++) {
                    System.arraycopy(dataStorage, pixelStride, this.data, i8, i9);
                    pixelStride += scanlineStride;
                    i8 += this.scanlineStride;
                }
                markDirty();
                return;
            }
        }
        for (int i11 = 0; i11 < i7; i11++) {
            dataElements = raster.getDataElements(minX, minY + i11, i6, 1, dataElements);
            setDataElements(i2, i3 + i11, i6, 1, dataElements);
        }
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        putByteData(i2, i3, i4, i5, (byte[]) obj);
    }

    @Override // sun.awt.image.ByteComponentRaster
    public void putByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.pixelStride == 1) {
            if (this.scanlineStride == i4) {
                System.arraycopy(bArr, 0, this.data, i7, i4 * i5);
            } else {
                int i9 = 0;
                while (i9 < i5) {
                    System.arraycopy(bArr, i8, this.data, i7, i4);
                    i8 += i4;
                    i9++;
                    i7 += this.scanlineStride;
                }
            }
        } else {
            int i10 = 0;
            while (i10 < i5) {
                int i11 = i7;
                int i12 = 0;
                while (i12 < i4) {
                    int i13 = i8;
                    i8++;
                    this.data[i11] = bArr[i13];
                    i12++;
                    i11 += this.pixelStride;
                }
                i10++;
                i7 += this.scanlineStride;
            }
        }
        markDirty();
    }

    @Override // sun.awt.image.ByteComponentRaster
    public void putByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        if (this.inOrder) {
            int i8 = i6 + this.dataOffsets[0];
            int i9 = i4 * this.pixelStride;
            if (i9 == this.scanlineStride) {
                System.arraycopy(bArr, 0, this.data, i8, i9 * i5);
            } else {
                int i10 = 0;
                while (i10 < i5) {
                    System.arraycopy(bArr, i7, this.data, i8, i9);
                    i7 += i9;
                    i10++;
                    i8 += this.scanlineStride;
                }
            }
        } else if (this.numDataElements == 1) {
            int i11 = i6 + this.dataOffsets[0];
            int i12 = 0;
            while (i12 < i5) {
                int i13 = i11;
                int i14 = 0;
                while (i14 < i4) {
                    int i15 = i7;
                    i7++;
                    this.data[i13] = bArr[i15];
                    i14++;
                    i13 += this.pixelStride;
                }
                i12++;
                i11 += this.scanlineStride;
            }
        } else if (this.numDataElements == 2) {
            int i16 = i6 + this.dataOffsets[0];
            int i17 = this.dataOffsets[1] - this.dataOffsets[0];
            int i18 = 0;
            while (i18 < i5) {
                int i19 = i16;
                int i20 = 0;
                while (i20 < i4) {
                    int i21 = i7;
                    int i22 = i7 + 1;
                    this.data[i19] = bArr[i21];
                    i7 = i22 + 1;
                    this.data[i19 + i17] = bArr[i22];
                    i20++;
                    i19 += this.pixelStride;
                }
                i18++;
                i16 += this.scanlineStride;
            }
        } else if (this.numDataElements == 3) {
            int i23 = i6 + this.dataOffsets[0];
            int i24 = this.dataOffsets[1] - this.dataOffsets[0];
            int i25 = this.dataOffsets[2] - this.dataOffsets[0];
            int i26 = 0;
            while (i26 < i5) {
                int i27 = i23;
                int i28 = 0;
                while (i28 < i4) {
                    int i29 = i7;
                    int i30 = i7 + 1;
                    this.data[i27] = bArr[i29];
                    int i31 = i30 + 1;
                    this.data[i27 + i24] = bArr[i30];
                    i7 = i31 + 1;
                    this.data[i27 + i25] = bArr[i31];
                    i28++;
                    i27 += this.pixelStride;
                }
                i26++;
                i23 += this.scanlineStride;
            }
        } else if (this.numDataElements == 4) {
            int i32 = i6 + this.dataOffsets[0];
            int i33 = this.dataOffsets[1] - this.dataOffsets[0];
            int i34 = this.dataOffsets[2] - this.dataOffsets[0];
            int i35 = this.dataOffsets[3] - this.dataOffsets[0];
            int i36 = 0;
            while (i36 < i5) {
                int i37 = i32;
                int i38 = 0;
                while (i38 < i4) {
                    int i39 = i7;
                    int i40 = i7 + 1;
                    this.data[i37] = bArr[i39];
                    int i41 = i40 + 1;
                    this.data[i37 + i33] = bArr[i40];
                    int i42 = i41 + 1;
                    this.data[i37 + i34] = bArr[i41];
                    i7 = i42 + 1;
                    this.data[i37 + i35] = bArr[i42];
                    i38++;
                    i37 += this.pixelStride;
                }
                i36++;
                i32 += this.scanlineStride;
            }
        } else {
            int i43 = 0;
            while (i43 < i5) {
                int i44 = i6;
                int i45 = 0;
                while (i45 < i4) {
                    for (int i46 = 0; i46 < this.numDataElements; i46++) {
                        int i47 = i7;
                        i7++;
                        this.data[this.dataOffsets[i46] + i44] = bArr[i47];
                    }
                    i45++;
                    i44 += this.pixelStride;
                }
                i43++;
                i6 += this.scanlineStride;
            }
        }
        markDirty();
    }

    @Override // java.awt.image.Raster
    public int getSample(int i2, int i3, int i4) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (this.packed) {
            return (this.data[((i3 * this.scanlineStride) + i2) + this.dbOffsetPacked] & this.bitMasks[i4]) >>> this.bitOffsets[i4];
        }
        return this.data[(i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.dbOffset + this.dataOffsets[i4]] & 255;
    }

    @Override // java.awt.image.WritableRaster
    public void setSample(int i2, int i3, int i4, int i5) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (this.packed) {
            int i6 = (i3 * this.scanlineStride) + i2 + this.dbOffsetPacked;
            int i7 = this.bitMasks[i4];
            this.data[i6] = (byte) (((byte) (this.data[i6] & (i7 ^ (-1)))) | ((i5 << this.bitOffsets[i4]) & i7));
        } else {
            this.data[(i3 * this.scanlineStride) + (i2 * this.pixelStride) + this.dbOffset + this.dataOffsets[i4]] = (byte) i5;
        }
        markDirty();
    }

    @Override // java.awt.image.Raster
    public int[] getSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        int[] iArr2;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5];
        }
        int i7 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i8 = 0;
        if (this.packed) {
            int i9 = i7 + this.dbOffsetPacked;
            int i10 = this.bitMasks[i6];
            int i11 = this.bitOffsets[i6];
            for (int i12 = 0; i12 < i5; i12++) {
                int i13 = i9;
                for (int i14 = 0; i14 < i4; i14++) {
                    int i15 = i13;
                    i13++;
                    byte b2 = this.data[i15];
                    int i16 = i8;
                    i8++;
                    iArr2[i16] = (b2 & i10) >>> i11;
                }
                i9 += this.scanlineStride;
            }
        } else {
            int i17 = i7 + this.dbOffset + this.dataOffsets[i6];
            for (int i18 = 0; i18 < i5; i18++) {
                int i19 = i17;
                for (int i20 = 0; i20 < i4; i20++) {
                    int i21 = i8;
                    i8++;
                    iArr2[i21] = this.data[i19] & 255;
                    i19 += this.pixelStride;
                }
                i17 += this.scanlineStride;
            }
        }
        return iArr2;
    }

    @Override // java.awt.image.WritableRaster
    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i8 = 0;
        if (this.packed) {
            int i9 = i7 + this.dbOffsetPacked;
            int i10 = this.bitMasks[i6];
            for (int i11 = 0; i11 < i5; i11++) {
                int i12 = i9;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i8;
                    i8++;
                    byte b2 = (byte) (((byte) (this.data[i12] & (i10 ^ (-1)))) | ((iArr[i14] << this.bitOffsets[i6]) & i10));
                    int i15 = i12;
                    i12++;
                    this.data[i15] = b2;
                }
                i9 += this.scanlineStride;
            }
        } else {
            int i16 = i7 + this.dbOffset + this.dataOffsets[i6];
            for (int i17 = 0; i17 < i5; i17++) {
                int i18 = i16;
                for (int i19 = 0; i19 < i4; i19++) {
                    int i20 = i8;
                    i8++;
                    this.data[i18] = (byte) iArr[i20];
                    i18 += this.pixelStride;
                }
                i16 += this.scanlineStride;
            }
        }
        markDirty();
    }

    @Override // java.awt.image.Raster
    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        int[] iArr2;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (iArr != null) {
            iArr2 = iArr;
        } else {
            iArr2 = new int[i4 * i5 * this.numBands];
        }
        int i6 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i7 = 0;
        if (this.packed) {
            int i8 = i6 + this.dbOffsetPacked;
            for (int i9 = 0; i9 < i5; i9++) {
                for (int i10 = 0; i10 < i4; i10++) {
                    byte b2 = this.data[i8 + i10];
                    for (int i11 = 0; i11 < this.numBands; i11++) {
                        int i12 = i7;
                        i7++;
                        iArr2[i12] = (b2 & this.bitMasks[i11]) >>> this.bitOffsets[i11];
                    }
                }
                i8 += this.scanlineStride;
            }
        } else {
            int i13 = i6 + this.dbOffset;
            int i14 = this.dataOffsets[0];
            if (this.numBands == 1) {
                for (int i15 = 0; i15 < i5; i15++) {
                    int i16 = i13 + i14;
                    for (int i17 = 0; i17 < i4; i17++) {
                        int i18 = i7;
                        i7++;
                        iArr2[i18] = this.data[i16] & 255;
                        i16 += this.pixelStride;
                    }
                    i13 += this.scanlineStride;
                }
            } else if (this.numBands == 2) {
                int i19 = this.dataOffsets[1] - i14;
                for (int i20 = 0; i20 < i5; i20++) {
                    int i21 = i13 + i14;
                    for (int i22 = 0; i22 < i4; i22++) {
                        int i23 = i7;
                        int i24 = i7 + 1;
                        iArr2[i23] = this.data[i21] & 255;
                        i7 = i24 + 1;
                        iArr2[i24] = this.data[i21 + i19] & 255;
                        i21 += this.pixelStride;
                    }
                    i13 += this.scanlineStride;
                }
            } else if (this.numBands == 3) {
                int i25 = this.dataOffsets[1] - i14;
                int i26 = this.dataOffsets[2] - i14;
                for (int i27 = 0; i27 < i5; i27++) {
                    int i28 = i13 + i14;
                    for (int i29 = 0; i29 < i4; i29++) {
                        int i30 = i7;
                        int i31 = i7 + 1;
                        iArr2[i30] = this.data[i28] & 255;
                        int i32 = i31 + 1;
                        iArr2[i31] = this.data[i28 + i25] & 255;
                        i7 = i32 + 1;
                        iArr2[i32] = this.data[i28 + i26] & 255;
                        i28 += this.pixelStride;
                    }
                    i13 += this.scanlineStride;
                }
            } else if (this.numBands == 4) {
                int i33 = this.dataOffsets[1] - i14;
                int i34 = this.dataOffsets[2] - i14;
                int i35 = this.dataOffsets[3] - i14;
                for (int i36 = 0; i36 < i5; i36++) {
                    int i37 = i13 + i14;
                    for (int i38 = 0; i38 < i4; i38++) {
                        int i39 = i7;
                        int i40 = i7 + 1;
                        iArr2[i39] = this.data[i37] & 255;
                        int i41 = i40 + 1;
                        iArr2[i40] = this.data[i37 + i33] & 255;
                        int i42 = i41 + 1;
                        iArr2[i41] = this.data[i37 + i34] & 255;
                        i7 = i42 + 1;
                        iArr2[i42] = this.data[i37 + i35] & 255;
                        i37 += this.pixelStride;
                    }
                    i13 += this.scanlineStride;
                }
            } else {
                for (int i43 = 0; i43 < i5; i43++) {
                    int i44 = i13;
                    for (int i45 = 0; i45 < i4; i45++) {
                        for (int i46 = 0; i46 < this.numBands; i46++) {
                            int i47 = i7;
                            i7++;
                            iArr2[i47] = this.data[i44 + this.dataOffsets[i46]] & 255;
                        }
                        i44 += this.pixelStride;
                    }
                    i13 += this.scanlineStride;
                }
            }
        }
        return iArr2;
    }

    @Override // java.awt.image.WritableRaster
    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = (i3 * this.scanlineStride) + (i2 * this.pixelStride);
        int i7 = 0;
        if (this.packed) {
            int i8 = i6 + this.dbOffsetPacked;
            for (int i9 = 0; i9 < i5; i9++) {
                for (int i10 = 0; i10 < i4; i10++) {
                    int i11 = 0;
                    for (int i12 = 0; i12 < this.numBands; i12++) {
                        int i13 = i7;
                        i7++;
                        i11 |= (iArr[i13] << this.bitOffsets[i12]) & this.bitMasks[i12];
                    }
                    this.data[i8 + i10] = (byte) i11;
                }
                i8 += this.scanlineStride;
            }
        } else {
            int i14 = i6 + this.dbOffset;
            int i15 = this.dataOffsets[0];
            if (this.numBands == 1) {
                for (int i16 = 0; i16 < i5; i16++) {
                    int i17 = i14 + i15;
                    for (int i18 = 0; i18 < i4; i18++) {
                        int i19 = i7;
                        i7++;
                        this.data[i17] = (byte) iArr[i19];
                        i17 += this.pixelStride;
                    }
                    i14 += this.scanlineStride;
                }
            } else if (this.numBands == 2) {
                int i20 = this.dataOffsets[1] - i15;
                for (int i21 = 0; i21 < i5; i21++) {
                    int i22 = i14 + i15;
                    for (int i23 = 0; i23 < i4; i23++) {
                        int i24 = i7;
                        int i25 = i7 + 1;
                        this.data[i22] = (byte) iArr[i24];
                        i7 = i25 + 1;
                        this.data[i22 + i20] = (byte) iArr[i25];
                        i22 += this.pixelStride;
                    }
                    i14 += this.scanlineStride;
                }
            } else if (this.numBands == 3) {
                int i26 = this.dataOffsets[1] - i15;
                int i27 = this.dataOffsets[2] - i15;
                for (int i28 = 0; i28 < i5; i28++) {
                    int i29 = i14 + i15;
                    for (int i30 = 0; i30 < i4; i30++) {
                        int i31 = i7;
                        int i32 = i7 + 1;
                        this.data[i29] = (byte) iArr[i31];
                        int i33 = i32 + 1;
                        this.data[i29 + i26] = (byte) iArr[i32];
                        i7 = i33 + 1;
                        this.data[i29 + i27] = (byte) iArr[i33];
                        i29 += this.pixelStride;
                    }
                    i14 += this.scanlineStride;
                }
            } else if (this.numBands == 4) {
                int i34 = this.dataOffsets[1] - i15;
                int i35 = this.dataOffsets[2] - i15;
                int i36 = this.dataOffsets[3] - i15;
                for (int i37 = 0; i37 < i5; i37++) {
                    int i38 = i14 + i15;
                    for (int i39 = 0; i39 < i4; i39++) {
                        int i40 = i7;
                        int i41 = i7 + 1;
                        this.data[i38] = (byte) iArr[i40];
                        int i42 = i41 + 1;
                        this.data[i38 + i34] = (byte) iArr[i41];
                        int i43 = i42 + 1;
                        this.data[i38 + i35] = (byte) iArr[i42];
                        i7 = i43 + 1;
                        this.data[i38 + i36] = (byte) iArr[i43];
                        i38 += this.pixelStride;
                    }
                    i14 += this.scanlineStride;
                }
            } else {
                for (int i44 = 0; i44 < i5; i44++) {
                    int i45 = i14;
                    for (int i46 = 0; i46 < i4; i46++) {
                        for (int i47 = 0; i47 < this.numBands; i47++) {
                            int i48 = i7;
                            i7++;
                            this.data[i45 + this.dataOffsets[i47]] = (byte) iArr[i48];
                        }
                        i45 += this.pixelStride;
                    }
                    i14 += this.scanlineStride;
                }
            }
        }
        markDirty();
    }

    @Override // java.awt.image.WritableRaster
    public void setRect(int i2, int i3, Raster raster) {
        if (!(raster instanceof ByteInterleavedRaster)) {
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
        setDataElements(i4, i5, minX, minY, width, height, raster);
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.Raster
    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        return createWritableChild(i2, i3, i4, i5, i6, i7, iArr);
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.WritableRaster
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
        return new ByteInterleavedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new ByteInterleavedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // sun.awt.image.ByteComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    @Override // sun.awt.image.ByteComponentRaster
    public String toString() {
        return new String("ByteInterleavedRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements + " dataOff[0] = " + this.dataOffsets[0]);
    }
}
