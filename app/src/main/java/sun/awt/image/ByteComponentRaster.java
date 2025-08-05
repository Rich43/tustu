package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/ByteComponentRaster.class */
public class ByteComponentRaster extends SunWritableRaster {
    protected int bandOffset;
    protected int[] dataOffsets;
    protected int scanlineStride;
    protected int pixelStride;
    protected byte[] data;
    int type;
    private int maxX;
    private int maxY;

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
    }

    public ByteComponentRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ByteComponentRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ByteComponentRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, ByteComponentRaster byteComponentRaster) {
        super(sampleModel, dataBuffer, rectangle, point, byteComponentRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferByte)) {
            throw new RasterFormatException("ByteComponentRasters must have byte DataBuffers");
        }
        DataBufferByte dataBufferByte = (DataBufferByte) dataBuffer;
        this.data = stealData(dataBufferByte, 0);
        if (dataBufferByte.getNumBanks() != 1) {
            throw new RasterFormatException("DataBuffer for ByteComponentRasters must only have 1 bank.");
        }
        int offset = dataBufferByte.getOffset();
        if (sampleModel instanceof ComponentSampleModel) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            this.type = 1;
            this.scanlineStride = componentSampleModel.getScanlineStride();
            this.pixelStride = componentSampleModel.getPixelStride();
            this.dataOffsets = componentSampleModel.getBandOffsets();
            int i2 = rectangle.f12372x - point.f12370x;
            int i3 = rectangle.f12373y - point.f12371y;
            for (int i4 = 0; i4 < getNumDataElements(); i4++) {
                int[] iArr = this.dataOffsets;
                int i5 = i4;
                iArr[i5] = iArr[i5] + offset + (i2 * this.pixelStride) + (i3 * this.scanlineStride);
            }
        } else if (sampleModel instanceof SinglePixelPackedSampleModel) {
            this.type = 7;
            this.scanlineStride = ((SinglePixelPackedSampleModel) sampleModel).getScanlineStride();
            this.pixelStride = 1;
            this.dataOffsets = new int[1];
            this.dataOffsets[0] = offset;
            int i6 = rectangle.f12372x - point.f12370x;
            int i7 = rectangle.f12373y - point.f12371y;
            int[] iArr2 = this.dataOffsets;
            iArr2[0] = iArr2[0] + (i6 * this.pixelStride) + (i7 * this.scanlineStride);
        } else {
            throw new RasterFormatException("IntegerComponentRasters must have ComponentSampleModel or SinglePixelPackedSampleModel");
        }
        this.bandOffset = this.dataOffsets[0];
        verify();
    }

    public int[] getDataOffsets() {
        return (int[]) this.dataOffsets.clone();
    }

    public int getDataOffset(int i2) {
        return this.dataOffsets[i2];
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }

    public int getPixelStride() {
        return this.pixelStride;
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
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            bArr[i5] = this.data[this.dataOffsets[i5] + i4];
        }
        return bArr;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        byte[] bArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            bArr = new byte[i4 * i5 * this.numDataElements];
        } else {
            bArr = (byte[]) obj;
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        int i8 = 0;
        while (i8 < i5) {
            int i9 = i6;
            int i10 = 0;
            while (i10 < i4) {
                for (int i11 = 0; i11 < this.numDataElements; i11++) {
                    int i12 = i7;
                    i7++;
                    bArr[i12] = this.data[this.dataOffsets[i11] + i9];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        return bArr;
    }

    public byte[] getByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (bArr == null) {
            bArr = new byte[this.scanlineStride * i5];
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

    public byte[] getByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (bArr == null) {
            bArr = new byte[this.numDataElements * this.scanlineStride * i5];
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        int i8 = 0;
        while (i8 < i5) {
            int i9 = i6;
            int i10 = 0;
            while (i10 < i4) {
                for (int i11 = 0; i11 < this.numDataElements; i11++) {
                    int i12 = i7;
                    i7++;
                    bArr[i12] = this.data[this.dataOffsets[i11] + i9];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        return bArr;
    }

    @Override // java.awt.image.WritableRaster
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

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Raster raster) {
        int minX = raster.getMinX() + i2;
        int minY = raster.getMinY() + i3;
        int width = raster.getWidth();
        int height = raster.getHeight();
        if (minX < this.minX || minY < this.minY || minX + width > this.maxX || minY + height > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        setDataElements(minX, minY, width, height, raster);
    }

    private void setDataElements(int i2, int i3, int i4, int i5, Raster raster) {
        if (i4 <= 0 || i5 <= 0) {
            return;
        }
        int minX = raster.getMinX();
        int minY = raster.getMinY();
        Object dataElements = null;
        if (raster instanceof ByteComponentRaster) {
            ByteComponentRaster byteComponentRaster = (ByteComponentRaster) raster;
            byte[] dataStorage = byteComponentRaster.getDataStorage();
            if (this.numDataElements == 1) {
                int dataOffset = byteComponentRaster.getDataOffset(0);
                int scanlineStride = byteComponentRaster.getScanlineStride();
                int i6 = dataOffset;
                int i7 = this.dataOffsets[0] + ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
                if (this.pixelStride == byteComponentRaster.getPixelStride()) {
                    int i8 = i4 * this.pixelStride;
                    for (int i9 = 0; i9 < i5; i9++) {
                        System.arraycopy(dataStorage, i6, this.data, i7, i8);
                        i6 += scanlineStride;
                        i7 += this.scanlineStride;
                    }
                    markDirty();
                    return;
                }
            }
        }
        for (int i10 = 0; i10 < i5; i10++) {
            dataElements = raster.getDataElements(minX, minY + i10, i4, 1, dataElements);
            setDataElements(i2, i3 + i10, i4, 1, dataElements);
        }
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        byte[] bArr = (byte[]) obj;
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        if (this.numDataElements == 1) {
            int i8 = 0;
            int i9 = i6 + this.dataOffsets[0];
            for (int i10 = 0; i10 < i5; i10++) {
                System.arraycopy(bArr, i8, this.data, i9, i4);
                i8 += i4;
                i9 += this.scanlineStride;
            }
            markDirty();
            return;
        }
        int i11 = 0;
        while (i11 < i5) {
            int i12 = i6;
            int i13 = 0;
            while (i13 < i4) {
                for (int i14 = 0; i14 < this.numDataElements; i14++) {
                    int i15 = i7;
                    i7++;
                    this.data[this.dataOffsets[i14] + i12] = bArr[i15];
                }
                i13++;
                i12 += this.pixelStride;
            }
            i11++;
            i6 += this.scanlineStride;
        }
        markDirty();
    }

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

    public void putByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        int i7 = 0;
        if (this.numDataElements == 1) {
            int i8 = i6 + this.dataOffsets[0];
            if (this.pixelStride == 1) {
                if (this.scanlineStride == i4) {
                    System.arraycopy(bArr, 0, this.data, i8, i4 * i5);
                } else {
                    for (int i9 = 0; i9 < i5; i9++) {
                        System.arraycopy(bArr, i7, this.data, i8, i4);
                        i7 += i4;
                        i8 += this.scanlineStride;
                    }
                }
            } else {
                int i10 = 0;
                while (i10 < i5) {
                    int i11 = i8;
                    int i12 = 0;
                    while (i12 < i4) {
                        int i13 = i7;
                        i7++;
                        this.data[i11] = bArr[i13];
                        i12++;
                        i11 += this.pixelStride;
                    }
                    i10++;
                    i8 += this.scanlineStride;
                }
            }
        } else {
            int i14 = 0;
            while (i14 < i5) {
                int i15 = i6;
                int i16 = 0;
                while (i16 < i4) {
                    for (int i17 = 0; i17 < this.numDataElements; i17++) {
                        int i18 = i7;
                        i7++;
                        this.data[this.dataOffsets[i17] + i15] = bArr[i18];
                    }
                    i16++;
                    i15 += this.pixelStride;
                }
                i14++;
                i6 += this.scanlineStride;
            }
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
        return new ByteComponentRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new ByteComponentRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    protected final void verify() {
        if (this.width <= 0 || this.height <= 0 || this.height > Integer.MAX_VALUE / this.width) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        for (int i2 = 0; i2 < this.dataOffsets.length; i2++) {
            if (this.dataOffsets[i2] < 0) {
                throw new RasterFormatException("Data offsets for band " + i2 + "(" + this.dataOffsets[i2] + ") must be >= 0");
            }
        }
        if (this.minX - this.sampleModelTranslateX < 0 || this.minY - this.sampleModelTranslateY < 0) {
            throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
        }
        if (this.scanlineStride < 0 || this.scanlineStride > Integer.MAX_VALUE / this.height) {
            throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
        }
        if ((this.height > 1 || this.minY - this.sampleModelTranslateY > 0) && this.scanlineStride > this.data.length) {
            throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
        }
        int i3 = (this.height - 1) * this.scanlineStride;
        if (this.pixelStride < 0 || this.pixelStride > Integer.MAX_VALUE / this.width || this.pixelStride > this.data.length) {
            throw new RasterFormatException("Incorrect pixel stride: " + this.pixelStride);
        }
        int i4 = (this.width - 1) * this.pixelStride;
        if (i4 > Integer.MAX_VALUE - i3) {
            throw new RasterFormatException("Incorrect raster attributes");
        }
        int i5 = i4 + i3;
        int i6 = 0;
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            if (this.dataOffsets[i7] > Integer.MAX_VALUE - i5) {
                throw new RasterFormatException("Incorrect band offset: " + this.dataOffsets[i7]);
            }
            int i8 = i5 + this.dataOffsets[i7];
            if (i8 > i6) {
                i6 = i8;
            }
        }
        if (this.data.length <= i6) {
            throw new RasterFormatException("Data array too small (should be > " + i6 + " )");
        }
    }

    public String toString() {
        return new String("ByteComponentRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements + " dataOff[0] = " + this.dataOffsets[0]);
    }
}
