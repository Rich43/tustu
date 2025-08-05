package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/ShortBandedRaster.class */
public class ShortBandedRaster extends SunWritableRaster {
    int[] dataOffsets;
    int scanlineStride;
    short[][] data;
    private int maxX;
    private int maxY;

    public ShortBandedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ShortBandedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [short[], short[][]] */
    public ShortBandedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, ShortBandedRaster shortBandedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, shortBandedRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferUShort)) {
            throw new RasterFormatException("ShortBandedRaster must have ushort DataBuffers");
        }
        DataBufferUShort dataBufferUShort = (DataBufferUShort) dataBuffer;
        if (sampleModel instanceof BandedSampleModel) {
            BandedSampleModel bandedSampleModel = (BandedSampleModel) sampleModel;
            this.scanlineStride = bandedSampleModel.getScanlineStride();
            int[] bankIndices = bandedSampleModel.getBankIndices();
            int[] bandOffsets = bandedSampleModel.getBandOffsets();
            int[] offsets = dataBufferUShort.getOffsets();
            this.dataOffsets = new int[bankIndices.length];
            this.data = new short[bankIndices.length];
            int i2 = rectangle.f12372x - point.f12370x;
            int i3 = rectangle.f12373y - point.f12371y;
            for (int i4 = 0; i4 < bankIndices.length; i4++) {
                this.data[i4] = stealData(dataBufferUShort, bankIndices[i4]);
                this.dataOffsets[i4] = offsets[bankIndices[i4]] + i2 + (i3 * this.scanlineStride) + bandOffsets[i4];
            }
            verify();
            return;
        }
        throw new RasterFormatException("ShortBandedRasters must have BandedSampleModels");
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
        return 1;
    }

    public short[][] getDataStorage() {
        return this.data;
    }

    public short[] getDataStorage(int i2) {
        return this.data[i2];
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, Object obj) {
        short[] sArr;
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            sArr = new short[this.numDataElements];
        } else {
            sArr = (short[]) obj;
        }
        int i4 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            sArr[i5] = this.data[i5][this.dataOffsets[i5] + i4];
        }
        return sArr;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        short[] sArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            sArr = new short[this.numDataElements * i4 * i5];
        } else {
            sArr = (short[]) obj;
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            short[] sArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    sArr[i8] = sArr2[i14];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        return sArr;
    }

    public short[] getShortData(int i2, int i3, int i4, int i5, int i6, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (sArr == null) {
            sArr = new short[this.scanlineStride * i5];
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[i6];
        if (this.scanlineStride == i4) {
            System.arraycopy(this.data[i6], i7, sArr, 0, i4 * i5);
        } else {
            int i8 = 0;
            int i9 = 0;
            while (i9 < i5) {
                System.arraycopy(this.data[i6], i7, sArr, i8, i4);
                i8 += i4;
                i9++;
                i7 += this.scanlineStride;
            }
        }
        return sArr;
    }

    public short[] getShortData(int i2, int i3, int i4, int i5, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (sArr == null) {
            sArr = new short[this.numDataElements * this.scanlineStride * i5];
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            short[] sArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    sArr[i8] = sArr2[i14];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        return sArr;
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        short[] sArr = (short[]) obj;
        int i4 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            this.data[i5][this.dataOffsets[i5] + i4] = sArr[i5];
        }
        markDirty();
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Raster raster) {
        int minX = i2 + raster.getMinX();
        int minY = i3 + raster.getMinY();
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
        for (int i6 = 0; i6 < i5; i6++) {
            dataElements = raster.getDataElements(minX, minY + i6, i4, 1, dataElements);
            setDataElements(i2, i3 + i6, i4, 1, dataElements);
        }
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        short[] sArr = (short[]) obj;
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            short[] sArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    sArr2[i14] = sArr[i8];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        markDirty();
    }

    public void putShortData(int i2, int i3, int i4, int i5, int i6, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.scanlineStride == i4) {
            System.arraycopy(sArr, 0, this.data[i6], i7, i4 * i5);
        } else {
            int i9 = 0;
            while (i9 < i5) {
                System.arraycopy(sArr, i8, this.data[i6], i7, i4);
                i8 += i4;
                i9++;
                i7 += this.scanlineStride;
            }
        }
        markDirty();
    }

    public void putShortData(int i2, int i3, int i4, int i5, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            short[] sArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    sArr2[i14] = sArr[i8];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        markDirty();
    }

    @Override // java.awt.image.WritableRaster
    public WritableRaster createWritableChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        SampleModel sampleModelCreateSubsetSampleModel;
        if (i2 < this.minX) {
            throw new RasterFormatException("x lies outside raster");
        }
        if (i3 < this.minY) {
            throw new RasterFormatException("y lies outside raster");
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
        return new ShortBandedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // java.awt.image.Raster
    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        return createWritableChild(i2, i3, i4, i5, i6, i7, iArr);
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new ShortBandedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    private void verify() {
        if (this.width <= 0 || this.height <= 0 || this.height > Integer.MAX_VALUE / this.width) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        if (this.scanlineStride < 0 || this.scanlineStride > Integer.MAX_VALUE / this.height) {
            throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
        }
        if (this.minX - this.sampleModelTranslateX < 0 || this.minY - this.sampleModelTranslateY < 0) {
            throw new RasterFormatException("Incorrect origin/translate: (" + this.minX + ", " + this.minY + ") / (" + this.sampleModelTranslateX + ", " + this.sampleModelTranslateY + ")");
        }
        if (this.height > 1 || this.minY - this.sampleModelTranslateY > 0) {
            for (int i2 = 0; i2 < this.data.length; i2++) {
                if (this.scanlineStride > this.data[i2].length) {
                    throw new RasterFormatException("Incorrect scanline stride: " + this.scanlineStride);
                }
            }
        }
        for (int i3 = 0; i3 < this.dataOffsets.length; i3++) {
            if (this.dataOffsets[i3] < 0) {
                throw new RasterFormatException("Data offsets for band " + i3 + "(" + this.dataOffsets[i3] + ") must be >= 0");
            }
        }
        int i4 = (this.height - 1) * this.scanlineStride;
        if (this.width - 1 > Integer.MAX_VALUE - i4) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        int i5 = i4 + (this.width - 1);
        int i6 = 0;
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            if (this.dataOffsets[i7] > Integer.MAX_VALUE - i5) {
                throw new RasterFormatException("Invalid raster dimension");
            }
            int i8 = i5 + this.dataOffsets[i7];
            if (i8 > i6) {
                i6 = i8;
            }
        }
        for (int i9 = 0; i9 < this.numDataElements; i9++) {
            if (this.data[i9].length <= i6) {
                throw new RasterFormatException("Data array too small (should be > " + i6 + " )");
            }
        }
    }

    public String toString() {
        return new String("ShortBandedRaster: width = " + this.width + " height = " + this.height + " #numBands " + this.numBands + " #dataElements " + this.numDataElements);
    }
}
