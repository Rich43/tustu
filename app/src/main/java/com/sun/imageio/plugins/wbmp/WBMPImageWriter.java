package com.sun.imageio.plugins.wbmp;

import com.sun.imageio.plugins.common.I18N;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/wbmp/WBMPImageWriter.class */
public class WBMPImageWriter extends ImageWriter {
    private ImageOutputStream stream;

    private static int getNumBits(int i2) {
        int i3 = 32;
        int i4 = Integer.MIN_VALUE;
        while (true) {
            int i5 = i4;
            if (i5 == 0 || (i2 & i5) != 0) {
                break;
            }
            i3--;
            i4 = i5 >>> 1;
        }
        return i3;
    }

    private static byte[] intToMultiByte(int i2) {
        byte[] bArr = new byte[(getNumBits(i2) + 6) / 7];
        int length = bArr.length - 1;
        for (int i3 = 0; i3 <= length; i3++) {
            bArr[i3] = (byte) ((i2 >>> ((length - i3) * 7)) & 127);
            if (i3 != length) {
                int i4 = i3;
                bArr[i4] = (byte) (bArr[i4] | Byte.MIN_VALUE);
            }
        }
        return bArr;
    }

    public WBMPImageWriter(ImageWriterSpi imageWriterSpi) {
        super(imageWriterSpi);
        this.stream = null;
    }

    @Override // javax.imageio.ImageWriter
    public void setOutput(Object obj) {
        super.setOutput(obj);
        if (obj != null) {
            if (!(obj instanceof ImageOutputStream)) {
                throw new IllegalArgumentException(I18N.getString("WBMPImageWriter"));
            }
            this.stream = (ImageOutputStream) obj;
            return;
        }
        this.stream = null;
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        WBMPMetadata wBMPMetadata = new WBMPMetadata();
        wBMPMetadata.wbmpType = 0;
        return wBMPMetadata;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter
    public boolean canWriteRasters() {
        return true;
    }

    @Override // javax.imageio.ImageWriter
    public void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        SampleModel sampleModel;
        Raster data;
        Rectangle rectangleIntersection;
        if (this.stream == null) {
            throw new IllegalStateException(I18N.getString("WBMPImageWriter3"));
        }
        if (iIOImage == null) {
            throw new IllegalArgumentException(I18N.getString("WBMPImageWriter4"));
        }
        clearAbortRequest();
        processImageStarted(0);
        if (imageWriteParam == null) {
            imageWriteParam = getDefaultWriteParam();
        }
        RenderedImage renderedImage = null;
        boolean zHasRaster = iIOImage.hasRaster();
        Rectangle sourceRegion = imageWriteParam.getSourceRegion();
        if (zHasRaster) {
            data = iIOImage.getRaster();
            sampleModel = data.getSampleModel();
        } else {
            renderedImage = iIOImage.getRenderedImage();
            sampleModel = renderedImage.getSampleModel();
            data = renderedImage.getData();
        }
        checkSampleModel(sampleModel);
        if (sourceRegion == null) {
            rectangleIntersection = data.getBounds();
        } else {
            rectangleIntersection = sourceRegion.intersection(data.getBounds());
        }
        if (rectangleIntersection.isEmpty()) {
            throw new RuntimeException(I18N.getString("WBMPImageWriter1"));
        }
        int sourceXSubsampling = imageWriteParam.getSourceXSubsampling();
        int sourceYSubsampling = imageWriteParam.getSourceYSubsampling();
        int subsamplingXOffset = imageWriteParam.getSubsamplingXOffset();
        int subsamplingYOffset = imageWriteParam.getSubsamplingYOffset();
        rectangleIntersection.translate(subsamplingXOffset, subsamplingYOffset);
        rectangleIntersection.width -= subsamplingXOffset;
        rectangleIntersection.height -= subsamplingYOffset;
        int i2 = rectangleIntersection.f12372x / sourceXSubsampling;
        int i3 = rectangleIntersection.f12373y / sourceYSubsampling;
        int i4 = ((rectangleIntersection.width + sourceXSubsampling) - 1) / sourceXSubsampling;
        int i5 = ((rectangleIntersection.height + sourceYSubsampling) - 1) / sourceYSubsampling;
        Rectangle rectangle = new Rectangle(i2, i3, i4, i5);
        SampleModel sampleModelCreateCompatibleSampleModel = sampleModel.createCompatibleSampleModel(i4, i5);
        SampleModel multiPixelPackedSampleModel = sampleModelCreateCompatibleSampleModel;
        if (sampleModelCreateCompatibleSampleModel.getDataType() != 0 || !(sampleModelCreateCompatibleSampleModel instanceof MultiPixelPackedSampleModel) || ((MultiPixelPackedSampleModel) sampleModelCreateCompatibleSampleModel).getDataBitOffset() != 0) {
            multiPixelPackedSampleModel = new MultiPixelPackedSampleModel(0, i4, i5, 1, (i4 + 7) >> 3, 0);
        }
        if (!rectangle.equals(rectangleIntersection)) {
            if (sourceXSubsampling == 1 && sourceYSubsampling == 1) {
                data = data.createChild(data.getMinX(), data.getMinY(), i4, i5, i2, i3, null);
            } else {
                WritableRaster writableRasterCreateWritableRaster = Raster.createWritableRaster(multiPixelPackedSampleModel, new Point(i2, i3));
                byte[] data2 = ((DataBufferByte) writableRasterCreateWritableRaster.getDataBuffer()).getData();
                int i6 = i3;
                int i7 = rectangleIntersection.f12373y;
                int i8 = 0;
                while (i6 < i3 + i5) {
                    int i9 = 0;
                    int i10 = rectangleIntersection.f12372x;
                    while (true) {
                        int i11 = i10;
                        if (i9 < i4) {
                            int i12 = i8 + (i9 >> 3);
                            data2[i12] = (byte) (data2[i12] | (data.getSample(i11, i7, 0) << (7 - (i9 & 7))));
                            i9++;
                            i10 = i11 + sourceXSubsampling;
                        }
                    }
                    i8 += (i4 + 7) >> 3;
                    i6++;
                    i7 += sourceYSubsampling;
                }
                data = writableRasterCreateWritableRaster;
            }
        }
        if (!multiPixelPackedSampleModel.equals(data.getSampleModel())) {
            WritableRaster writableRasterCreateWritableRaster2 = Raster.createWritableRaster(multiPixelPackedSampleModel, new Point(data.getMinX(), data.getMinY()));
            writableRasterCreateWritableRaster2.setRect(data);
            data = writableRasterCreateWritableRaster2;
        }
        boolean z2 = false;
        if (!zHasRaster && (renderedImage.getColorModel() instanceof IndexColorModel)) {
            IndexColorModel indexColorModel = (IndexColorModel) renderedImage.getColorModel();
            z2 = indexColorModel.getRed(0) > indexColorModel.getRed(1);
        }
        int scanlineStride = ((MultiPixelPackedSampleModel) multiPixelPackedSampleModel).getScanlineStride();
        int i13 = (i4 + 7) / 8;
        byte[] data3 = ((DataBufferByte) data.getDataBuffer()).getData();
        this.stream.write(0);
        this.stream.write(0);
        this.stream.write(intToMultiByte(i4));
        this.stream.write(intToMultiByte(i5));
        if (!z2 && scanlineStride == i13) {
            this.stream.write(data3, 0, i5 * i13);
            processImageProgress(100.0f);
        } else {
            int i14 = 0;
            if (!z2) {
                for (int i15 = 0; i15 < i5 && !abortRequested(); i15++) {
                    this.stream.write(data3, i14, i13);
                    i14 += scanlineStride;
                    processImageProgress((100.0f * i15) / i5);
                }
            } else {
                byte[] bArr = new byte[i13];
                for (int i16 = 0; i16 < i5 && !abortRequested(); i16++) {
                    for (int i17 = 0; i17 < i13; i17++) {
                        bArr[i17] = (byte) (data3[i17 + i14] ^ (-1));
                    }
                    this.stream.write(bArr, 0, i13);
                    i14 += scanlineStride;
                    processImageProgress((100.0f * i16) / i5);
                }
            }
        }
        if (abortRequested()) {
            processWriteAborted();
        } else {
            processImageComplete();
            this.stream.flushBefore(this.stream.getStreamPosition());
        }
    }

    @Override // javax.imageio.ImageWriter
    public void reset() {
        super.reset();
        this.stream = null;
    }

    private void checkSampleModel(SampleModel sampleModel) {
        int dataType = sampleModel.getDataType();
        if (dataType < 0 || dataType > 3 || sampleModel.getNumBands() != 1 || sampleModel.getSampleSize(0) != 1) {
            throw new IllegalArgumentException(I18N.getString("WBMPImageWriter2"));
        }
    }
}
