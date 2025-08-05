package java.awt.image;

import java.awt.Point;
import java.awt.Rectangle;

/* loaded from: rt.jar:java/awt/image/WritableRaster.class */
public class WritableRaster extends Raster {
    protected WritableRaster(SampleModel sampleModel, Point point) {
        this(sampleModel, sampleModel.createDataBuffer(), new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    protected WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point point) {
        this(sampleModel, dataBuffer, new Rectangle(point.f12370x, point.f12371y, sampleModel.getWidth(), sampleModel.getHeight()), point, null);
    }

    protected WritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle rectangle, Point point, WritableRaster writableRaster) {
        super(sampleModel, dataBuffer, rectangle, point, writableRaster);
    }

    public WritableRaster getWritableParent() {
        return (WritableRaster) this.parent;
    }

    public WritableRaster createWritableTranslatedChild(int i2, int i3) {
        return createWritableChild(this.minX, this.minY, this.width, this.height, i2, i3, null);
    }

    public WritableRaster createWritableChild(int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
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
        if (iArr != null) {
            sampleModelCreateSubsetSampleModel = this.sampleModel.createSubsetSampleModel(iArr);
        } else {
            sampleModelCreateSubsetSampleModel = this.sampleModel;
        }
        return new WritableRaster(sampleModelCreateSubsetSampleModel, getDataBuffer(), new Rectangle(i6, i7, i4, i5), new Point(this.sampleModelTranslateX + (i6 - i2), this.sampleModelTranslateY + (i7 - i3)), this);
    }

    public void setDataElements(int i2, int i3, Object obj) {
        this.sampleModel.setDataElements(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, obj, this.dataBuffer);
    }

    public void setDataElements(int i2, int i3, Raster raster) {
        int minX = i2 + raster.getMinX();
        int minY = i3 + raster.getMinY();
        int width = raster.getWidth();
        int height = raster.getHeight();
        if (minX < this.minX || minY < this.minY || minX + width > this.minX + this.width || minY + height > this.minY + this.height) {
            throw new ArrayIndexOutOfBoundsException("Coordinate out of bounds!");
        }
        int minX2 = raster.getMinX();
        int minY2 = raster.getMinY();
        Object dataElements = null;
        for (int i4 = 0; i4 < height; i4++) {
            dataElements = raster.getDataElements(minX2, minY2 + i4, width, 1, dataElements);
            setDataElements(minX, minY + i4, width, 1, dataElements);
        }
    }

    public void setDataElements(int i2, int i3, int i4, int i5, Object obj) {
        this.sampleModel.setDataElements(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, obj, this.dataBuffer);
    }

    public void setRect(Raster raster) {
        setRect(0, 0, raster);
    }

    public void setRect(int i2, int i3, Raster raster) {
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
        if (i4 + width > this.minX + this.width) {
            width = (this.minX + this.width) - i4;
        }
        if (i5 + height > this.minY + this.height) {
            height = (this.minY + this.height) - i5;
        }
        if (width <= 0 || height <= 0) {
            return;
        }
        switch (raster.getSampleModel().getDataType()) {
            case 0:
            case 1:
            case 2:
            case 3:
                int[] pixels = null;
                for (int i8 = 0; i8 < height; i8++) {
                    pixels = raster.getPixels(minX, minY + i8, width, 1, pixels);
                    setPixels(i4, i5 + i8, width, 1, pixels);
                }
                break;
            case 4:
                float[] pixels2 = null;
                for (int i9 = 0; i9 < height; i9++) {
                    pixels2 = raster.getPixels(minX, minY + i9, width, 1, pixels2);
                    setPixels(i4, i5 + i9, width, 1, pixels2);
                }
                break;
            case 5:
                double[] pixels3 = null;
                for (int i10 = 0; i10 < height; i10++) {
                    pixels3 = raster.getPixels(minX, minY + i10, width, 1, pixels3);
                    setPixels(i4, i5 + i10, width, 1, pixels3);
                }
                break;
        }
    }

    public void setPixel(int i2, int i3, int[] iArr) {
        this.sampleModel.setPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, iArr, this.dataBuffer);
    }

    public void setPixel(int i2, int i3, float[] fArr) {
        this.sampleModel.setPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, fArr, this.dataBuffer);
    }

    public void setPixel(int i2, int i3, double[] dArr) {
        this.sampleModel.setPixel(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, dArr, this.dataBuffer);
    }

    public void setPixels(int i2, int i3, int i4, int i5, int[] iArr) {
        this.sampleModel.setPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, iArr, this.dataBuffer);
    }

    public void setPixels(int i2, int i3, int i4, int i5, float[] fArr) {
        this.sampleModel.setPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, fArr, this.dataBuffer);
    }

    public void setPixels(int i2, int i3, int i4, int i5, double[] dArr) {
        this.sampleModel.setPixels(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, dArr, this.dataBuffer);
    }

    public void setSample(int i2, int i3, int i4, int i5) {
        this.sampleModel.setSample(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, this.dataBuffer);
    }

    public void setSample(int i2, int i3, int i4, float f2) {
        this.sampleModel.setSample(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, f2, this.dataBuffer);
    }

    public void setSample(int i2, int i3, int i4, double d2) {
        this.sampleModel.setSample(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, d2, this.dataBuffer);
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, int[] iArr) {
        this.sampleModel.setSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, iArr, this.dataBuffer);
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, float[] fArr) {
        this.sampleModel.setSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, fArr, this.dataBuffer);
    }

    public void setSamples(int i2, int i3, int i4, int i5, int i6, double[] dArr) {
        this.sampleModel.setSamples(i2 - this.sampleModelTranslateX, i3 - this.sampleModelTranslateY, i4, i5, i6, dArr, this.dataBuffer);
    }
}
