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

/* loaded from: rt.jar:sun/awt/image/IntegerComponentRaster.class */
public class IntegerComponentRaster extends SunWritableRaster {
    static final int TYPE_CUSTOM = 0;
    static final int TYPE_BYTE_SAMPLES = 1;
    static final int TYPE_USHORT_SAMPLES = 2;
    static final int TYPE_INT_SAMPLES = 3;
    static final int TYPE_BYTE_BANDED_SAMPLES = 4;
    static final int TYPE_USHORT_BANDED_SAMPLES = 5;
    static final int TYPE_INT_BANDED_SAMPLES = 6;
    static final int TYPE_BYTE_PACKED_SAMPLES = 7;
    static final int TYPE_USHORT_PACKED_SAMPLES = 8;
    static final int TYPE_INT_PACKED_SAMPLES = 9;
    static final int TYPE_INT_8BIT_SAMPLES = 10;
    static final int TYPE_BYTE_BINARY_SAMPLES = 11;
    protected int bandOffset;
    protected int[] dataOffsets;
    protected int scanlineStride;
    protected int pixelStride;
    protected int[] data;
    protected int numDataElems;
    int type;
    private int maxX;
    private int maxY;

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
    }

    public IntegerComponentRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public IntegerComponentRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    public IntegerComponentRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, IntegerComponentRaster integerComponentRaster) {
        super(sampleModel, dataBuffer, rectangle, point, integerComponentRaster);
        this.maxX = this.minX + this.width;
        this.maxY = this.minY + this.height;
        if (!(dataBuffer instanceof DataBufferInt)) {
            throw new RasterFormatException("IntegerComponentRasters must haveinteger DataBuffers");
        }
        DataBufferInt dataBufferInt = (DataBufferInt) dataBuffer;
        if (dataBufferInt.getNumBanks() != 1) {
            throw new RasterFormatException("DataBuffer for IntegerComponentRasters must only have 1 bank.");
        }
        this.data = stealData(dataBufferInt, 0);
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            SinglePixelPackedSampleModel singlePixelPackedSampleModel = (SinglePixelPackedSampleModel) sampleModel;
            int[] bitOffsets = singlePixelPackedSampleModel.getBitOffsets();
            boolean z2 = false;
            for (int i2 = 1; i2 < bitOffsets.length; i2++) {
                if (bitOffsets[i2] % 8 != 0) {
                    z2 = true;
                }
            }
            this.type = z2 ? 9 : 10;
            this.scanlineStride = singlePixelPackedSampleModel.getScanlineStride();
            this.pixelStride = 1;
            this.dataOffsets = new int[1];
            this.dataOffsets[0] = dataBufferInt.getOffset();
            this.bandOffset = this.dataOffsets[0];
            int i3 = rectangle.f12372x - point.f12370x;
            int i4 = rectangle.f12373y - point.f12371y;
            int[] iArr = this.dataOffsets;
            iArr[0] = iArr[0] + i3 + (i4 * this.scanlineStride);
            this.numDataElems = singlePixelPackedSampleModel.getNumDataElements();
            verify();
            return;
        }
        throw new RasterFormatException("IntegerComponentRasters must have SinglePixelPackedSampleModel");
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

    public int[] getDataStorage() {
        return this.data;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, Object obj) {
        int[] iArr;
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj == null) {
            iArr = new int[this.numDataElements];
        } else {
            iArr = (int[]) obj;
        }
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            iArr[i5] = this.data[this.dataOffsets[i5] + i4];
        }
        return iArr;
    }

    @Override // java.awt.image.Raster
    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        int[] iArr;
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        if (obj instanceof int[]) {
            iArr = (int[]) obj;
        } else {
            iArr = new int[this.numDataElements * i4 * i5];
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
                    iArr[i12] = this.data[this.dataOffsets[i11] + i9];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
        }
        return iArr;
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 >= this.maxX || i3 >= this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int[] iArr = (int[]) obj;
        int i4 = ((i3 - this.minY) * this.scanlineStride) + ((i2 - this.minX) * this.pixelStride);
        for (int i5 = 0; i5 < this.numDataElements; i5++) {
            this.data[this.dataOffsets[i5] + i4] = iArr[i5];
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
        if ((raster instanceof IntegerComponentRaster) && this.pixelStride == 1 && this.numDataElements == 1) {
            IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) raster;
            if (integerComponentRaster.getNumDataElements() != 1) {
                throw new ArrayIndexOutOfBoundsException("Number of bands does not match");
            }
            int[] dataStorage = integerComponentRaster.getDataStorage();
            int scanlineStride = integerComponentRaster.getScanlineStride();
            int dataOffset = integerComponentRaster.getDataOffset(0);
            int i6 = this.dataOffsets[0] + ((i3 - this.minY) * this.scanlineStride) + (i2 - this.minX);
            if (integerComponentRaster.getPixelStride() == this.pixelStride) {
                int i7 = i4 * this.pixelStride;
                for (int i8 = 0; i8 < i5; i8++) {
                    System.arraycopy(dataStorage, dataOffset, this.data, i6, i7);
                    dataOffset += scanlineStride;
                    i6 += this.scanlineStride;
                }
                markDirty();
                return;
            }
        }
        Object dataElements = null;
        for (int i9 = 0; i9 < i5; i9++) {
            dataElements = raster.getDataElements(minX, minY + i9, i4, 1, dataElements);
            setDataElements(i2, i3 + i9, i4, 1, dataElements);
        }
    }

    @Override // java.awt.image.WritableRaster
    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        if (i2 < this.minX || i3 < this.minY || i2 + i4 > this.maxX || i3 + i5 > this.maxY) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int[] iArr = (int[]) obj;
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
                    this.data[this.dataOffsets[i11] + i9] = iArr[i12];
                }
                i10++;
                i9 += this.pixelStride;
            }
            i8++;
            i6 += this.scanlineStride;
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
        return new IntegerComponentRaster(sampleModelCreateSubsetSampleModel, this.dataBuffer, new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
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
        return new IntegerComponentRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    @Override // java.awt.image.Raster
    public WritableRaster createCompatibleWritableRaster() {
        return createCompatibleWritableRaster(this.width, this.height);
    }

    protected final void verify() {
        if (this.width <= 0 || this.height <= 0 || this.height > Integer.MAX_VALUE / this.width) {
            throw new RasterFormatException("Invalid raster dimension");
        }
        if (this.dataOffsets[0] < 0) {
            throw new RasterFormatException("Data offset (" + this.dataOffsets[0] + ") must be >= 0");
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
        int i2 = (this.height - 1) * this.scanlineStride;
        if (this.pixelStride < 0 || this.pixelStride > Integer.MAX_VALUE / this.width || this.pixelStride > this.data.length) {
            throw new RasterFormatException("Incorrect pixel stride: " + this.pixelStride);
        }
        int i3 = (this.width - 1) * this.pixelStride;
        if (i3 > Integer.MAX_VALUE - i2) {
            throw new RasterFormatException("Incorrect raster attributes");
        }
        int i4 = i3 + i2;
        int i5 = 0;
        for (int i6 = 0; i6 < this.numDataElements; i6++) {
            if (this.dataOffsets[i6] > Integer.MAX_VALUE - i4) {
                throw new RasterFormatException("Incorrect band offset: " + this.dataOffsets[i6]);
            }
            int i7 = i4 + this.dataOffsets[i6];
            if (i7 > i5) {
                i5 = i7;
            }
        }
        if (this.data.length <= i5) {
            throw new RasterFormatException("Data array too small (should be > " + i5 + " )");
        }
    }

    public String toString() {
        return new String("IntegerComponentRaster: width = " + this.width + " height = " + this.height + " #Bands = " + this.numBands + " #DataElements " + this.numDataElements + " xOff = " + this.sampleModelTranslateX + " yOff = " + this.sampleModelTranslateY + " dataOffset[0] " + this.dataOffsets[0]);
    }
}
