package java.awt.image;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Point;
import java.awt.Rectangle;
import sun.awt.image.ByteBandedRaster;
import sun.awt.image.ByteInterleavedRaster;
import sun.awt.image.BytePackedRaster;
import sun.awt.image.IntegerInterleavedRaster;
import sun.awt.image.ShortBandedRaster;
import sun.awt.image.ShortInterleavedRaster;
import sun.awt.image.SunWritableRaster;

/* loaded from: rt.jar:java/awt/image/Raster.class */
public class Raster {
    protected SampleModel sampleModel;
    protected DataBuffer dataBuffer;
    protected int minX;
    protected int minY;
    protected int width;
    protected int height;
    protected int sampleModelTranslateX;
    protected int sampleModelTranslateY;
    protected int numBands;
    protected int numDataElements;
    protected Raster parent;

    private static native void initIDs();

    static {
        ColorModel.loadLibraries();
        initIDs();
    }

    public static WritableRaster createInterleavedRaster(int i2, int i3, int i4, int i5, Point point) {
        int[] iArr = new int[i5];
        for (int i6 = 0; i6 < i5; i6++) {
            iArr[i6] = i6;
        }
        return createInterleavedRaster(i2, i3, i4, i3 * i5, i5, iArr, point);
    }

    public static WritableRaster createInterleavedRaster(int i2, int i3, int i4, int i5, int i6, int[] iArr, Point point) {
        DataBuffer dataBufferUShort;
        int i7 = (i5 * (i4 - 1)) + (i6 * i3);
        switch (i2) {
            case 0:
                dataBufferUShort = new DataBufferByte(i7);
                break;
            case 1:
                dataBufferUShort = new DataBufferUShort(i7);
                break;
            default:
                throw new IllegalArgumentException("Unsupported data type " + i2);
        }
        return createInterleavedRaster(dataBufferUShort, i3, i4, i5, i6, iArr, point);
    }

    public static WritableRaster createBandedRaster(int i2, int i3, int i4, int i5, Point point) {
        if (i5 < 1) {
            throw new ArrayIndexOutOfBoundsException("Number of bands (" + i5 + ") must be greater than 0");
        }
        int[] iArr = new int[i5];
        int[] iArr2 = new int[i5];
        for (int i6 = 0; i6 < i5; i6++) {
            iArr[i6] = i6;
            iArr2[i6] = 0;
        }
        return createBandedRaster(i2, i3, i4, i3, iArr, iArr2, point);
    }

    public static WritableRaster createBandedRaster(int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2, Point point) {
        DataBuffer dataBufferInt;
        int length = iArr2.length;
        if (iArr == null) {
            throw new ArrayIndexOutOfBoundsException("Bank indices array is null");
        }
        if (iArr2 == null) {
            throw new ArrayIndexOutOfBoundsException("Band offsets array is null");
        }
        int i6 = iArr[0];
        int i7 = iArr2[0];
        for (int i8 = 1; i8 < length; i8++) {
            if (iArr[i8] > i6) {
                i6 = iArr[i8];
            }
            if (iArr2[i8] > i7) {
                i7 = iArr2[i8];
            }
        }
        int i9 = i6 + 1;
        int i10 = i7 + (i5 * (i4 - 1)) + i3;
        switch (i2) {
            case 0:
                dataBufferInt = new DataBufferByte(i10, i9);
                break;
            case 1:
                dataBufferInt = new DataBufferUShort(i10, i9);
                break;
            case 2:
            default:
                throw new IllegalArgumentException("Unsupported data type " + i2);
            case 3:
                dataBufferInt = new DataBufferInt(i10, i9);
                break;
        }
        return createBandedRaster(dataBufferInt, i3, i4, i5, iArr, iArr2, point);
    }

    public static WritableRaster createPackedRaster(int i2, int i3, int i4, int[] iArr, Point point) {
        DataBuffer dataBufferInt;
        switch (i2) {
            case 0:
                dataBufferInt = new DataBufferByte(i3 * i4);
                break;
            case 1:
                dataBufferInt = new DataBufferUShort(i3 * i4);
                break;
            case 2:
            default:
                throw new IllegalArgumentException("Unsupported data type " + i2);
            case 3:
                dataBufferInt = new DataBufferInt(i3 * i4);
                break;
        }
        return createPackedRaster(dataBufferInt, i3, i4, i3, iArr, point);
    }

    public static WritableRaster createPackedRaster(int i2, int i3, int i4, int i5, int i6, Point point) {
        DataBuffer dataBufferInt;
        if (i5 <= 0) {
            throw new IllegalArgumentException("Number of bands (" + i5 + ") must be greater than 0");
        }
        if (i6 <= 0) {
            throw new IllegalArgumentException("Bits per band (" + i6 + ") must be greater than 0");
        }
        if (i5 != 1) {
            int[] iArr = new int[i5];
            int i7 = (1 << i6) - 1;
            int i8 = (i5 - 1) * i6;
            if (i8 + i6 > DataBuffer.getDataTypeSize(i2)) {
                throw new IllegalArgumentException("bitsPerBand(" + i6 + ") * bands is  greater than data type size.");
            }
            switch (i2) {
                case 0:
                case 1:
                case 3:
                    for (int i9 = 0; i9 < i5; i9++) {
                        iArr[i9] = i7 << i8;
                        i8 -= i6;
                    }
                    return createPackedRaster(i2, i3, i4, iArr, point);
                case 2:
                default:
                    throw new IllegalArgumentException("Unsupported data type " + i2);
            }
        }
        double d2 = i3;
        switch (i2) {
            case 0:
                dataBufferInt = new DataBufferByte(((int) Math.ceil(d2 / (8 / i6))) * i4);
                break;
            case 1:
                dataBufferInt = new DataBufferUShort(((int) Math.ceil(d2 / (16 / i6))) * i4);
                break;
            case 2:
            default:
                throw new IllegalArgumentException("Unsupported data type " + i2);
            case 3:
                dataBufferInt = new DataBufferInt(((int) Math.ceil(d2 / (32 / i6))) * i4);
                break;
        }
        return createPackedRaster(dataBufferInt, i3, i4, i6, point);
    }

    public static WritableRaster createInterleavedRaster(DataBuffer dataBuffer, int i2, int i3, int i4, int i5, int[] iArr, Point point) {
        if (dataBuffer == null) {
            throw new NullPointerException("DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = dataBuffer.getDataType();
        PixelInterleavedSampleModel pixelInterleavedSampleModel = new PixelInterleavedSampleModel(dataType, i2, i3, i5, i4, iArr);
        switch (dataType) {
            case 0:
                return new ByteInterleavedRaster(pixelInterleavedSampleModel, dataBuffer, point);
            case 1:
                return new ShortInterleavedRaster(pixelInterleavedSampleModel, dataBuffer, point);
            default:
                throw new IllegalArgumentException("Unsupported data type " + dataType);
        }
    }

    public static WritableRaster createBandedRaster(DataBuffer dataBuffer, int i2, int i3, int i4, int[] iArr, int[] iArr2, Point point) {
        if (dataBuffer == null) {
            throw new NullPointerException("DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = dataBuffer.getDataType();
        if (iArr2.length != iArr.length) {
            throw new IllegalArgumentException("bankIndices.length != bandOffsets.length");
        }
        BandedSampleModel bandedSampleModel = new BandedSampleModel(dataType, i2, i3, i4, iArr, iArr2);
        switch (dataType) {
            case 0:
                return new ByteBandedRaster(bandedSampleModel, dataBuffer, point);
            case 1:
                return new ShortBandedRaster(bandedSampleModel, dataBuffer, point);
            case 2:
            default:
                throw new IllegalArgumentException("Unsupported data type " + dataType);
            case 3:
                return new SunWritableRaster(bandedSampleModel, dataBuffer, point);
        }
    }

    public static WritableRaster createPackedRaster(DataBuffer dataBuffer, int i2, int i3, int i4, int[] iArr, Point point) {
        if (dataBuffer == null) {
            throw new NullPointerException("DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = dataBuffer.getDataType();
        SinglePixelPackedSampleModel singlePixelPackedSampleModel = new SinglePixelPackedSampleModel(dataType, i2, i3, i4, iArr);
        switch (dataType) {
            case 0:
                return new ByteInterleavedRaster(singlePixelPackedSampleModel, dataBuffer, point);
            case 1:
                return new ShortInterleavedRaster(singlePixelPackedSampleModel, dataBuffer, point);
            case 2:
            default:
                throw new IllegalArgumentException("Unsupported data type " + dataType);
            case 3:
                return new IntegerInterleavedRaster(singlePixelPackedSampleModel, dataBuffer, point);
        }
    }

    public static WritableRaster createPackedRaster(DataBuffer dataBuffer, int i2, int i3, int i4, Point point) {
        if (dataBuffer == null) {
            throw new NullPointerException("DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = dataBuffer.getDataType();
        if (dataType != 0 && dataType != 1 && dataType != 3) {
            throw new IllegalArgumentException("Unsupported data type " + dataType);
        }
        if (dataBuffer.getNumBanks() != 1) {
            throw new RasterFormatException("DataBuffer for packed Rasters must only have 1 bank.");
        }
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = new MultiPixelPackedSampleModel(dataType, i2, i3, i4);
        if (dataType == 0 && (i4 == 1 || i4 == 2 || i4 == 4)) {
            return new BytePackedRaster(multiPixelPackedSampleModel, dataBuffer, point);
        }
        return new SunWritableRaster(multiPixelPackedSampleModel, dataBuffer, point);
    }

    public static Raster createRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        if (sampleModel == null || dataBuffer == null) {
            throw new NullPointerException("SampleModel and DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = sampleModel.getDataType();
        if (sampleModel instanceof PixelInterleavedSampleModel) {
            switch (dataType) {
                case 0:
                    return new ByteInterleavedRaster(sampleModel, dataBuffer, point);
                case 1:
                    return new ShortInterleavedRaster(sampleModel, dataBuffer, point);
            }
        }
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            switch (dataType) {
                case 0:
                    return new ByteInterleavedRaster(sampleModel, dataBuffer, point);
                case 1:
                    return new ShortInterleavedRaster(sampleModel, dataBuffer, point);
                case 3:
                    return new IntegerInterleavedRaster(sampleModel, dataBuffer, point);
            }
        }
        if ((sampleModel instanceof MultiPixelPackedSampleModel) && dataType == 0 && sampleModel.getSampleSize(0) < 8) {
            return new BytePackedRaster(sampleModel, dataBuffer, point);
        }
        return new Raster(sampleModel, dataBuffer, point);
    }

    public static WritableRaster createWritableRaster(SampleModel sampleModel, Point point) {
        if (point == null) {
            point = new Point(0, 0);
        }
        return createWritableRaster(sampleModel, sampleModel.createDataBuffer(), point);
    }

    public static WritableRaster createWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        if (sampleModel == null || dataBuffer == null) {
            throw new NullPointerException("SampleModel and DataBuffer cannot be null");
        }
        if (point == null) {
            point = new Point(0, 0);
        }
        int dataType = sampleModel.getDataType();
        if (sampleModel instanceof PixelInterleavedSampleModel) {
            switch (dataType) {
                case 0:
                    return new ByteInterleavedRaster(sampleModel, dataBuffer, point);
                case 1:
                    return new ShortInterleavedRaster(sampleModel, dataBuffer, point);
            }
        }
        if (sampleModel instanceof SinglePixelPackedSampleModel) {
            switch (dataType) {
                case 0:
                    return new ByteInterleavedRaster(sampleModel, dataBuffer, point);
                case 1:
                    return new ShortInterleavedRaster(sampleModel, dataBuffer, point);
                case 3:
                    return new IntegerInterleavedRaster(sampleModel, dataBuffer, point);
            }
        }
        if ((sampleModel instanceof MultiPixelPackedSampleModel) && dataType == 0 && sampleModel.getSampleSize(0) < 8) {
            return new BytePackedRaster(sampleModel, dataBuffer, point);
        }
        return new SunWritableRaster(sampleModel, dataBuffer, point);
    }

    protected Raster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    protected Raster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    protected Raster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, Raster raster) {
        if (sampleModel == null || dataBuffer == null || rectangle == null || point == null) {
            throw new NullPointerException("SampleModel, dataBuffer, aRegion and sampleModelTranslate cannot be null");
        }
        this.sampleModel = sampleModel;
        this.dataBuffer = dataBuffer;
        this.minX = rectangle.f12372x;
        this.minY = rectangle.f12373y;
        this.width = rectangle.width;
        this.height = rectangle.height;
        if (this.width <= 0 || this.height <= 0) {
            throw new RasterFormatException("negative or zero " + (this.width <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        if (this.minX + this.width < this.minX) {
            throw new RasterFormatException("overflow condition for X coordinates of Raster");
        }
        if (this.minY + this.height < this.minY) {
            throw new RasterFormatException("overflow condition for Y coordinates of Raster");
        }
        this.sampleModelTranslateX = point.f12370x;
        this.sampleModelTranslateY = point.f12371y;
        this.numBands = sampleModel.getNumBands();
        this.numDataElements = sampleModel.getNumDataElements();
        this.parent = raster;
    }

    public Raster getParent() {
        return this.parent;
    }

    public final int getSampleModelTranslateX() {
        return this.sampleModelTranslateX;
    }

    public final int getSampleModelTranslateY() {
        return this.sampleModelTranslateY;
    }

    public WritableRaster createCompatibleWritableRaster() {
        return new SunWritableRaster(this.sampleModel, new Point(0, 0));
    }

    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        if (i2 <= 0 || i3 <= 0) {
            throw new RasterFormatException("negative " + (i2 <= 0 ? MetadataParser.WIDTH_TAG_NAME : MetadataParser.HEIGHT_TAG_NAME));
        }
        return new SunWritableRaster(this.sampleModel.createCompatibleSampleModel(i2, i3), new Point(0, 0));
    }

    public WritableRaster createCompatibleWritableRaster(Rectangle rectangle) {
        if (rectangle == null) {
            throw new NullPointerException("Rect cannot be null");
        }
        return createCompatibleWritableRaster(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public WritableRaster createCompatibleWritableRaster(int i2, int i3, int i4, int i5) {
        return createCompatibleWritableRaster(i4, i5).createWritableChild(0, 0, i4, i5, i2, i3, null);
    }

    public Raster createTranslatedChild(int i2, int i3) {
        return createChild(this.minX, this.minY, this.width, this.height, i2, i3, null);
    }

    public Raster createChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        SampleModel sampleModelCreateSubsetSampleModel;
        if (i2 < this.minX) {
            throw new RasterFormatException("parentX lies outside raster");
        }
        if (i3 < this.minY) {
            throw new RasterFormatException("parentY lies outside raster");
        }
        if (i2 + i4 < i2 || i2 + i4 > this.width + this.minX) {
            throw new RasterFormatException("(parentX + width) is outside raster");
        }
        if (i3 + i5 < i3 || i3 + i5 > this.height + this.minY) {
            throw new RasterFormatException("(parentY + height) is outside raster");
        }
        if (iArr == null) {
            sampleModelCreateSubsetSampleModel = this.sampleModel;
        } else {
            sampleModelCreateSubsetSampleModel = this.sampleModel.createSubsetSampleModel(iArr);
        }
        return new Raster(sampleModelCreateSubsetSampleModel, getDataBuffer(), new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.minX, this.minY, this.width, this.height);
    }

    public final int getMinX() {
        return this.minX;
    }

    public final int getMinY() {
        return this.minY;
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getNumBands() {
        return this.numBands;
    }

    public final int getNumDataElements() {
        return this.sampleModel.getNumDataElements();
    }

    public final int getTransferType() {
        return this.sampleModel.getTransferType();
    }

    public DataBuffer getDataBuffer() {
        return this.dataBuffer;
    }

    public SampleModel getSampleModel() {
        return this.sampleModel;
    }

    public Object getDataElements(int i2, int i3, Object obj) {
        return this.sampleModel.getDataElements(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, obj, this.dataBuffer);
    }

    public Object getDataElements(int i2, int i3, int i4, int i5, Object obj) {
        return this.sampleModel.getDataElements(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, obj, this.dataBuffer);
    }

    public int[] getPixel(int i2, int i3, int[] iArr) {
        return this.sampleModel.getPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, iArr, this.dataBuffer);
    }

    public float[] getPixel(int i2, int i3, float[] fArr) {
        return this.sampleModel.getPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, fArr, this.dataBuffer);
    }

    public double[] getPixel(int i2, int i3, double[] dArr) {
        return this.sampleModel.getPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, dArr, this.dataBuffer);
    }

    public int[] getPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        return this.sampleModel.getPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, iArr, this.dataBuffer);
    }

    public float[] getPixels(int i2, int i3, int i4, int i5, float[] fArr) {
        return this.sampleModel.getPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, fArr, this.dataBuffer);
    }

    public double[] getPixels(int i2, int i3, int i4, int i5, double[] dArr) {
        return this.sampleModel.getPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, dArr, this.dataBuffer);
    }

    public int getSample(int i2, int i3, int i4) {
        return this.sampleModel.getSample(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, this.dataBuffer);
    }

    public float getSampleFloat(int i2, int i3, int i4) {
        return this.sampleModel.getSampleFloat(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, this.dataBuffer);
    }

    public double getSampleDouble(int i2, int i3, int i4) {
        return this.sampleModel.getSampleDouble(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, this.dataBuffer);
    }

    public int[] getSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        return this.sampleModel.getSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, iArr, this.dataBuffer);
    }

    public float[] getSamples(int i2, int i3, int i4, int i5, int i6, float[] fArr) {
        return this.sampleModel.getSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, fArr, this.dataBuffer);
    }

    public double[] getSamples(int i2, int i3, int i4, int i5, int i6, double[] dArr) {
        return this.sampleModel.getSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, dArr, this.dataBuffer);
    }
}
