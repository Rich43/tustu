package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferUShort;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/ShortInterleavedRaster.class */
public class ShortInterleavedRaster extends ShortComponentRaster {
    private int maxX;
    private int maxY;

    public ShortInterleavedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ShortInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public ShortInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, ShortInterleavedRaster shortInterleavedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, shortInterleavedRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferUShort)) {
            throw new RasterFormatException("ShortInterleavedRasters must have ushort DataBuffers");
        }
        DataBufferUShort dataBufferUShort = (DataBufferUShort) dataBuffer;
        this.data = stealData(dataBufferUShort, 0);
        if ((sampleModel instanceof PixelInterleavedSampleModel) || ((sampleModel instanceof ComponentSampleModel) && sampleModel.getNumBands() == 1)) {
            ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel;
            this.scanlineStride = componentSampleModel.getScanlineStride();
            this.pixelStride = componentSampleModel.getPixelStride();
            this.dataOffsets = componentSampleModel.getBandOffsets();
            int i2 = rectangle.f12372x - point.f12370x;
            int i3 = rectangle.f12373y - point.f12371y;
            for (int i4 = 0; i4 < getNumDataElements(); i4++) {
                int[] iArr = this.dataOffsets;
                int i5 = i4;
                iArr[i5] = iArr[i5] + (i2 * this.pixelStride) + (i3 * this.scanlineStride);
            }
        } else if (sampleModel instanceof SinglePixelPackedSampleModel) {
            this.scanlineStride = ((SinglePixelPackedSampleModel) sampleModel).getScanlineStride();
            this.pixelStride = 1;
            this.dataOffsets = new int[1];
            this.dataOffsets[0] = dataBufferUShort.getOffset();
            int i6 = rectangle.f12372x - point.f12370x;
            int i7 = rectangle.f12373y - point.f12371y;
            int[] iArr2 = this.dataOffsets;
            iArr2[0] = iArr2[0] + i6 + (i7 * this.scanlineStride);
        } else {
            throw new RasterFormatException("ShortInterleavedRasters must have PixelInterleavedSampleModel, SinglePixelPackedSampleModel or 1 band ComponentSampleModel.  Sample model is " + ((Object) sampleModel));
        }
        this.bandOffset = this.dataOffsets[0];
        verify();
    }

    @Override // sun.awt.image.ShortComponentRaster
    public int[] getDataOffsets() {
        return (int[]) this.dataOffsets.clone();
    }

    @Override // sun.awt.image.ShortComponentRaster
    public int getDataOffset(int i2) {
        return this.dataOffsets[i2];
    }

    @Override // sun.awt.image.ShortComponentRaster
    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override // sun.awt.image.ShortComponentRaster
    public int getPixelStride() {
        return this.pixelStride;
    }

    @Override // sun.awt.image.ShortComponentRaster
    public short[] getDataStorage() {
        return this.data;
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.Raster
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
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            sArr[i5] = this.data[this.dataOffsets[i5] + i4];
        }
        return sArr;
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        short[] sArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            sArr = new short[i4 * i5 * this.numDataElements];
        } else {
            sArr = (short[]) obj;
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
                    sArr[i12] = this.data[this.dataOffsets[i11] + i9];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        return sArr;
    }

    @Override // sun.awt.image.ShortComponentRaster
    public short[] getShortData(int i2, int i3, int i4, int i5, int i6, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (sArr == null) {
            sArr = new short[this.numDataElements * i4 * i5];
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.pixelStride == 1) {
            if (this.scanlineStride == i4) {
                System.arraycopy(this.data, i7, sArr, 0, i4 * i5);
            } else {
                int i9 = 0;
                while (i9 < i5) {
                    System.arraycopy(this.data, i7, sArr, i8, i4);
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
                    sArr[i13] = this.data[i11];
                    i12++;
                    i11 += this.pixelStride;
                }
                i10++;
                i7 += this.scanlineStride;
            }
        }
        return sArr;
    }

    @Override // sun.awt.image.ShortComponentRaster
    public short[] getShortData(int i2, int i3, int i4, int i5, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (sArr == null) {
            sArr = new short[this.numDataElements * i4 * i5];
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
                    sArr[i12] = this.data[this.dataOffsets[i11] + i9];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        return sArr;
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        short[] sArr = (short[]) obj;
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            this.data[this.dataOffsets[i5] + i4] = sArr[i5];
        }
        markDirty();
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.WritableRaster
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

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        short[] sArr = (short[]) obj;
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
                    this.data[this.dataOffsets[i11] + i9] = sArr[i12];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        markDirty();
    }

    @Override // sun.awt.image.ShortComponentRaster
    public void putShortData(int i2, int i3, int i4, int i5, int i6, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i7 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride) + this.dataOffsets[i6];
        int i8 = 0;
        if (this.pixelStride == 1) {
            if (this.scanlineStride == i4) {
                System.arraycopy(sArr, 0, this.data, i7, i4 * i5);
            } else {
                int i9 = 0;
                while (i9 < i5) {
                    System.arraycopy(sArr, i8, this.data, i7, i4);
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
                    this.data[i11] = sArr[i13];
                    i12++;
                    i11 += this.pixelStride;
                }
                i10++;
                i7 += this.scanlineStride;
            }
        }
        markDirty();
    }

    @Override // sun.awt.image.ShortComponentRaster
    public void putShortData(int i2, int i3, int i4, int i5, short[] sArr) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
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
                    this.data[this.dataOffsets[i11] + i9] = sArr[i12];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        markDirty();
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.Raster
    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        return createWritableChild(i2, i3, i4, i5, i6, i7, iArr);
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.WritableRaster
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
        return new ShortInterleavedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new ShortInterleavedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // sun.awt.image.ShortComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    @Override // sun.awt.image.ShortComponentRaster
    public String toString() {
        return new String("ShortInterleavedRaster: width = " + this.width + " height = " + this.height + " #numDataElements " + this.numDataElements);
    }
}
