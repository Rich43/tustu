package sun.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/image/IntegerInterleavedRaster.class */
public class IntegerInterleavedRaster extends IntegerComponentRaster {
    private int maxX;
    private int maxY;

    public IntegerInterleavedRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public IntegerInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public IntegerInterleavedRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, IntegerInterleavedRaster integerInterleavedRaster) {
        super(sampleModel, dataBuffer, rectangle, point, integerInterleavedRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferInt)) {
            throw new RasterFormatException("IntegerInterleavedRasters must haveinteger DataBuffers");
        }
        DataBufferInt dataBufferInt = (DataBufferInt) dataBuffer;
        this.data = stealData(dataBufferInt, 0);
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            this.scanlineStride = singlePixelPackedSampleModel.getScanlineStride();
            this.pixelStride = 1;
            this.dataOffsets = new int[1];
            this.dataOffsets[0] = dataBufferInt.getOffset();
            this.bandOffset = this.dataOffsets[0];
            int i2 = rectangle.f12372x - point.f12370x;
            int i3 = rectangle.f12373y - point.f12371y;
            int[] iArr = this.dataOffsets;
            iArr[0] = iArr[0] + i2 + (i3 * this.scanlineStride);
            this.numDataElems = singlePixelPackedSampleModel.getNumDataElements();
            verify();
            return;
        }
        throw new RasterFormatException("IntegerInterleavedRasters must have SinglePixelPackedSampleModel");
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public int[] getDataOffsets() {
        return (int[]) this.dataOffsets.clone();
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public int getDataOffset(int i2) {
        return this.dataOffsets[i2];
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public int getScanlineStride() {
        return this.scanlineStride;
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public int getPixelStride() {
        return this.pixelStride;
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public int[] getDataStorage() {
        return this.data;
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.Raster
    public Object getDataElements(int i2, int i3, Object obj) {
        int[] iArr;
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            iArr = new int[1];
        } else {
            iArr = (int[]) obj;
        }
        iArr[0] = this.data[((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[0]];
        return iArr;
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        int[] iArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj instanceof int[]) {
            iArr = (int[]) obj;
        } else {
            iArr = new int[i4 * i5];
        }
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[0];
        int i7 = 0;
        for (int i8 = 0; i8 < i5; i8++) {
            System.arraycopy(this.data, i6, iArr, i7, i4);
            i7 += i4;
            i6 += this.scanlineStride;
        }
        return iArr;
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int i4 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[0];
        this.data[i4] = ((int[]) obj)[0];
        markDirty();
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.WritableRaster
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
        if (raster instanceof IntegerInterleavedRaster) {
            IntegerInterleavedRaster integerInterleavedRaster = (IntegerInterleavedRaster) raster;
            int[] dataStorage = integerInterleavedRaster.getDataStorage();
            int scanlineStride = integerInterleavedRaster.getScanlineStride();
            int dataOffset = integerInterleavedRaster.getDataOffset(0);
            int i6 = this.dataOffsets[0] + ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
            for (int i7 = 0; i7 < i5; i7++) {
                System.arraycopy(dataStorage, dataOffset, this.data, i6, i4);
                dataOffset += scanlineStride;
                i6 += this.scanlineStride;
            }
            markDirty();
            return;
        }
        Object dataElements = null;
        for (int i8 = 0; i8 < i5; i8++) {
            dataElements = raster.getDataElements(minX, minY + i8, i4, 1, dataElements);
            setDataElements(i2, i3 + i8, i4, 1, dataElements);
        }
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int[] iArr = (int[]) obj;
        int i6 = ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX) + this.dataOffsets[0];
        int i7 = 0;
        for (int i8 = 0; i8 < i5; i8++) {
            System.arraycopy(iArr, i7, this.data, i6, i4);
            i7 += i4;
            i6 += this.scanlineStride;
        }
        markDirty();
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.WritableRaster
    public WritableRaster createWritableChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        SampleModel sampleModelCreateSubsetSampleModel;
        if (i2 < this.minX) {
            throw new RasterFormatException("x lies outside raster");
        }
        if (i3 < this.minY) {
            throw new RasterFormatException("y lies outside raster");
        }
        if (i2 + i4 < i2 || i2 + i4 > this.minX + this.width) {
            throw new RasterFormatException("(x + width) is outside raster");
        }
        if (i3 + i5 < i3 || i3 + i5 > this.minY + this.height) {
            throw new RasterFormatException("(y + height) is outside raster");
        }
        if (iArr != null) {
            sampleModelCreateSubsetSampleModel = this.sampleModel.createSubsetSampleModel(iArr);
        } else {
            sampleModelCreateSubsetSampleModel = this.sampleModel;
        }
        return new IntegerInterleavedRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.Raster
    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        return createWritableChild(i2, i3, i4, i5, i6, i7, iArr);
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new IntegerInterleavedRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // sun.awt.image.IntegerComponentRaster, java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    @Override // sun.awt.image.IntegerComponentRaster
    public String toString() {
        return new String("IntegerInterleavedRaster: width = " + this.width + " height = " + this.height + " #Bands = " + this.numBands + " xOff = " + this.sampleModelTranslateX + " yOff = " + this.sampleModelTranslateY + " dataOffset[0] " + this.dataOffsets[0]);
    }
}
