package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/ByteBandedRaster.class */
public class ByteBandedRaster extends SunWritableRaster {
    int[] dataOffsets;
    int scanlineStride;
    byte[][] data;
    private int maxX;
    private int maxY;

    public ByteBandedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ByteBandedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    /* JADX WARN: Type inference failed for: r1v15, types: [byte[], byte[][]] */
    public ByteBandedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, ByteBandedRaster byteBandedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, byteBandedRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferByte)) {
            throw new RasterFormatException("ByteBandedRaster must havebyte DataBuffers");
        }
        DataBufferByte dataBufferByte = (DataBufferByte) dataBuffer;
        if (sampleModel instanceof BandedSampleModel) {
            BandedSampleModel bandedSampleModel = (BandedSampleModel) sampleModel;
            this.scanlineStride = bandedSampleModel.getScanlineStride();
            int[] bankIndices = bandedSampleModel.getBankIndices();
            int[] bandOffsets = bandedSampleModel.getBandOffsets();
            int[] offsets = dataBufferByte.getOffsets();
            this.dataOffsets = new int[bankIndices.length];
            this.data = new byte[bankIndices.length];
            int i2 = rectangle.f12372x - point.f12370x;
            int i3 = rectangle.f12373y - point.f12371y;
            for (int i4 = 0; i4 < bankIndices.length; i4++) {
                this.data[i4] = stealData(dataBufferByte, bankIndices[i4]);
                this.dataOffsets[i4] = offsets[bankIndices[i4]] + i2 + (i3 * this.scanlineStride) + bandOffsets[i4];
            }
            verify();
            return;
        }
        throw new RasterFormatException("ByteBandedRasters must haveBandedSampleModels");
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

    public byte[][] getDataStorage() {
        return this.data;
    }

    public byte[] getDataStorage(int i2) {
        return this.data[i2];
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
        int i4 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            bArr[i5] = this.data[i5][this.dataOffsets[i5] + i4];
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
            bArr = new byte[this.numDataElements * i4 * i5];
        } else {
            bArr = (byte[]) obj;
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            byte[] bArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    bArr[i8] = bArr2[i14];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
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
        int i7 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[i6];
        if (this.scanlineStride == i4) {
            System.arraycopy(this.data[i6], i7, bArr, 0, i4 * i5);
        } else {
            int i8 = 0;
            int i9 = 0;
            while (i9 < i5) {
                System.arraycopy(this.data[i6], i7, bArr, i8, i4);
                i8 += i4;
                i9++;
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
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            byte[] bArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    bArr[i8] = bArr2[i14];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        return bArr;
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        byte[] bArr = (byte[]) obj;
        int i4 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            this.data[i5][this.dataOffsets[i5] + i4] = bArr[i5];
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
        byte[] bArr = (byte[]) obj;
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            byte[] bArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    bArr2[i14] = bArr[i8];
                    i8 += this.numDataElements;
                }
                i11++;
                i10 += this.scanlineStride;
            }
        }
        markDirty();
    }

    public void putByteData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.scanlineStride == i4) {
            System.arraycopy(bArr, 0, this.data[i6], i7, i4 * i5);
        } else {
            int i9 = 0;
            while (i9 < i5) {
                System.arraycopy(bArr, i8, this.data[i6], i7, i4);
                i8 += i4;
                i9++;
                i7 += this.scanlineStride;
            }
        }
        markDirty();
    }

    public void putByteData(int i2, int i3, int i4, int i5, byte[] bArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
        for (int i7 = 0; i7 < this.numDataElements; i7++) {
            int i8 = i7;
            byte[] bArr2 = this.data[i7];
            int i9 = this.dataOffsets[i7];
            int i10 = i6;
            int i11 = 0;
            while (i11 < i5) {
                int i12 = i9 + i10;
                for (int i13 = 0; i13 < i4; i13++) {
                    int i14 = i12;
                    i12++;
                    bArr2[i14] = bArr[i8];
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
        if (i2 + i4 < i2 || i2 + i4 > this.width + this.minX) {
            throw new RasterFormatException("(x + width) is outside raster");
        }
        if (i3 + i5 < i3 || i3 + i5 > this.height + this.minY) {
            throw new RasterFormatException("(y + height) is outside raster");
        }
        if (iArr != null) {
            sampleModelCreateSubsetSampleModel = this.sampleModel.createSubsetSampleModel(iArr);
        } else {
            sampleModelCreateSubsetSampleModel = this.sampleModel;
        }
        return new ByteBandedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
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
        return new ByteBandedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
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
        if (this.data.length == 1) {
            if (this.data[0].length <= i6 * this.numDataElements) {
                throw new RasterFormatException("Data array too small (it is " + this.data[0].length + " and should be > " + (i6 * this.numDataElements) + " )");
            }
            return;
        }
        for (int i9 = 0; i9 < this.numDataElements; i9++) {
            if (this.data[i9].length <= i6) {
                throw new RasterFormatException("Data array too small (it is " + this.data[i9].length + " and should be > " + i6 + " )");
            }
        }
    }

    public String toString() {
        return new String("ByteBandedRaster: width = " + this.width + " height = " + this.height + " #bands " + this.numDataElements + " minX = " + this.minX + " minY = " + this.minY);
    }
}
