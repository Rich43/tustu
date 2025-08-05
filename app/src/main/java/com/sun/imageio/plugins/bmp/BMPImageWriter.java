package com.sun.imageio.plugins.bmp;

import com.sun.imageio.plugins.common.I18N;
import com.sun.imageio.plugins.common.ImageUtil;
import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.bmp.BMPImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPImageWriter.class */
public class BMPImageWriter extends ImageWriter implements BMPConstants {
    private ImageOutputStream stream;
    private ByteArrayOutputStream embedded_stream;
    private int version;
    private int compressionType;
    private boolean isTopDown;

    /* renamed from: w, reason: collision with root package name */
    private int f11836w;

    /* renamed from: h, reason: collision with root package name */
    private int f11837h;
    private int compImageSize;
    private int[] bitMasks;
    private int[] bitPos;
    private byte[] bpixels;
    private short[] spixels;
    private int[] ipixels;

    public BMPImageWriter(ImageWriterSpi imageWriterSpi) {
        super(imageWriterSpi);
        this.stream = null;
        this.embedded_stream = null;
        this.compImageSize = 0;
    }

    @Override // javax.imageio.ImageWriter
    public void setOutput(Object obj) {
        super.setOutput(obj);
        if (obj != null) {
            if (!(obj instanceof ImageOutputStream)) {
                throw new IllegalArgumentException(I18N.getString("BMPImageWriter0"));
            }
            this.stream = (ImageOutputStream) obj;
            this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            return;
        }
        this.stream = null;
    }

    @Override // javax.imageio.ImageWriter
    public ImageWriteParam getDefaultWriteParam() {
        return new BMPImageWriteParam();
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        BMPMetadata bMPMetadata = new BMPMetadata();
        bMPMetadata.bmpVersion = BMPConstants.VERSION_3;
        bMPMetadata.compression = getPreferredCompressionType(imageTypeSpecifier);
        if (imageWriteParam != null && imageWriteParam.getCompressionMode() == 2) {
            bMPMetadata.compression = BMPCompressionTypes.getType(imageWriteParam.getCompressionType());
        }
        bMPMetadata.bitsPerPixel = (short) imageTypeSpecifier.getColorModel().getPixelSize();
        return bMPMetadata;
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
        ColorModel colorModel;
        Rectangle rectangleIntersection;
        BMPMetadata bMPMetadata;
        if (this.stream == null) {
            throw new IllegalStateException(I18N.getString("BMPImageWriter7"));
        }
        if (iIOImage == null) {
            throw new IllegalArgumentException(I18N.getString("BMPImageWriter8"));
        }
        clearAbortRequest();
        processImageStarted(0);
        if (imageWriteParam == null) {
            imageWriteParam = getDefaultWriteParam();
        }
        BMPImageWriteParam bMPImageWriteParam = (BMPImageWriteParam) imageWriteParam;
        int iRoundBpp = 24;
        boolean z2 = false;
        int mapSize = 0;
        IndexColorModel indexColorModel = null;
        RenderedImage renderedImage = null;
        Raster raster = null;
        boolean zHasRaster = iIOImage.hasRaster();
        Rectangle sourceRegion = imageWriteParam.getSourceRegion();
        this.compImageSize = 0;
        if (zHasRaster) {
            raster = iIOImage.getRaster();
            sampleModel = raster.getSampleModel();
            colorModel = ImageUtil.createColorModel(null, sampleModel);
            if (sourceRegion == null) {
                rectangleIntersection = raster.getBounds();
            } else {
                rectangleIntersection = sourceRegion.intersection(raster.getBounds());
            }
        } else {
            renderedImage = iIOImage.getRenderedImage();
            sampleModel = renderedImage.getSampleModel();
            colorModel = renderedImage.getColorModel();
            Rectangle rectangle = new Rectangle(renderedImage.getMinX(), renderedImage.getMinY(), renderedImage.getWidth(), renderedImage.getHeight());
            if (sourceRegion == null) {
                rectangleIntersection = rectangle;
            } else {
                rectangleIntersection = sourceRegion.intersection(rectangle);
            }
        }
        IIOMetadata metadata = iIOImage.getMetadata();
        if (metadata != null && (metadata instanceof BMPMetadata)) {
            bMPMetadata = (BMPMetadata) metadata;
        } else {
            bMPMetadata = (BMPMetadata) getDefaultImageMetadata(new ImageTypeSpecifier(colorModel, sampleModel), imageWriteParam);
        }
        if (rectangleIntersection.isEmpty()) {
            throw new RuntimeException(I18N.getString("BMPImageWrite0"));
        }
        int sourceXSubsampling = imageWriteParam.getSourceXSubsampling();
        int sourceYSubsampling = imageWriteParam.getSourceYSubsampling();
        int subsamplingXOffset = imageWriteParam.getSubsamplingXOffset();
        int subsamplingYOffset = imageWriteParam.getSubsamplingYOffset();
        int dataType = sampleModel.getDataType();
        rectangleIntersection.translate(subsamplingXOffset, subsamplingYOffset);
        rectangleIntersection.width -= subsamplingXOffset;
        rectangleIntersection.height -= subsamplingYOffset;
        int i2 = rectangleIntersection.f12372x / sourceXSubsampling;
        int i3 = rectangleIntersection.f12373y / sourceYSubsampling;
        this.f11836w = ((rectangleIntersection.width + sourceXSubsampling) - 1) / sourceXSubsampling;
        this.f11837h = ((rectangleIntersection.height + sourceYSubsampling) - 1) / sourceYSubsampling;
        int i4 = rectangleIntersection.f12372x % sourceXSubsampling;
        int i5 = rectangleIntersection.f12373y % sourceYSubsampling;
        boolean zEquals = new Rectangle(i2, i3, this.f11836w, this.f11837h).equals(rectangleIntersection);
        int[] sourceBands = imageWriteParam.getSourceBands();
        boolean z3 = true;
        int numBands = sampleModel.getNumBands();
        if (sourceBands != null) {
            sampleModel = sampleModel.createSubsetSampleModel(sourceBands);
            colorModel = null;
            z3 = false;
            numBands = sampleModel.getNumBands();
        } else {
            sourceBands = new int[numBands];
            for (int i6 = 0; i6 < numBands; i6++) {
                sourceBands[i6] = i6;
            }
        }
        int[] bandOffsets = null;
        boolean z4 = true;
        if (sampleModel instanceof ComponentSampleModel) {
            bandOffsets = ((ComponentSampleModel) sampleModel).getBandOffsets();
            if (sampleModel instanceof BandedSampleModel) {
                z4 = false;
            } else {
                for (int i7 = 0; i7 < bandOffsets.length; i7++) {
                    z4 &= bandOffsets[i7] == (bandOffsets.length - i7) - 1;
                }
            }
        } else if (sampleModel instanceof SinglePixelPackedSampleModel) {
            int[] bitOffsets = ((SinglePixelPackedSampleModel) sampleModel).getBitOffsets();
            for (int i8 = 0; i8 < bitOffsets.length - 1; i8++) {
                z4 &= bitOffsets[i8] > bitOffsets[i8 + 1];
            }
        }
        if (bandOffsets == null) {
            bandOffsets = new int[numBands];
            for (int i9 = 0; i9 < numBands; i9++) {
                bandOffsets[i9] = i9;
            }
        }
        boolean z5 = zEquals & z4;
        int[] sampleSize = sampleModel.getSampleSize();
        int i10 = this.f11836w * numBands;
        switch (bMPImageWriteParam.getCompressionMode()) {
            case 1:
                this.compressionType = getPreferredCompressionType(colorModel, sampleModel);
                break;
            case 2:
                this.compressionType = BMPCompressionTypes.getType(bMPImageWriteParam.getCompressionType());
                break;
            case 3:
                this.compressionType = bMPMetadata.compression;
                break;
            default:
                this.compressionType = 0;
                break;
        }
        if (!canEncodeImage(this.compressionType, colorModel, sampleModel)) {
            throw new IOException("Image can not be encoded with compression type " + BMPCompressionTypes.getName(this.compressionType));
        }
        byte[] bArr = null;
        byte[] bArr2 = null;
        byte[] bArr3 = null;
        byte[] bArr4 = null;
        if (this.compressionType == 3) {
            iRoundBpp = DataBuffer.getDataTypeSize(sampleModel.getDataType());
            if (iRoundBpp != 16 && iRoundBpp != 32) {
                iRoundBpp = 32;
                z5 = false;
            }
            i10 = ((this.f11836w * iRoundBpp) + 7) >> 3;
            z2 = true;
            mapSize = 3;
            bArr = new byte[3];
            bArr2 = new byte[3];
            bArr3 = new byte[3];
            bArr4 = new byte[3];
            int redMask = 16711680;
            int greenMask = 65280;
            int blueMask = 255;
            if (iRoundBpp == 16) {
                if (colorModel instanceof DirectColorModel) {
                    DirectColorModel directColorModel = (DirectColorModel) colorModel;
                    redMask = directColorModel.getRedMask();
                    greenMask = directColorModel.getGreenMask();
                    blueMask = directColorModel.getBlueMask();
                } else {
                    throw new IOException("Image can not be encoded with compression type " + BMPCompressionTypes.getName(this.compressionType));
                }
            }
            writeMaskToPalette(redMask, 0, bArr, bArr2, bArr3, bArr4);
            writeMaskToPalette(greenMask, 1, bArr, bArr2, bArr3, bArr4);
            writeMaskToPalette(blueMask, 2, bArr, bArr2, bArr3, bArr4);
            if (!z5) {
                this.bitMasks = new int[3];
                this.bitMasks[0] = redMask;
                this.bitMasks[1] = greenMask;
                this.bitMasks[2] = blueMask;
                this.bitPos = new int[3];
                this.bitPos[0] = firstLowBit(redMask);
                this.bitPos[1] = firstLowBit(greenMask);
                this.bitPos[2] = firstLowBit(blueMask);
            }
            if (colorModel instanceof IndexColorModel) {
                indexColorModel = (IndexColorModel) colorModel;
            }
        } else if (colorModel instanceof IndexColorModel) {
            z2 = true;
            indexColorModel = (IndexColorModel) colorModel;
            mapSize = indexColorModel.getMapSize();
            if (mapSize <= 2) {
                iRoundBpp = 1;
                i10 = (this.f11836w + 7) >> 3;
            } else if (mapSize <= 16) {
                iRoundBpp = 4;
                i10 = (this.f11836w + 1) >> 1;
            } else if (mapSize <= 256) {
                iRoundBpp = 8;
            } else {
                iRoundBpp = 24;
                z2 = false;
                mapSize = 0;
                i10 = this.f11836w * 3;
            }
            if (z2) {
                bArr = new byte[mapSize];
                bArr2 = new byte[mapSize];
                bArr3 = new byte[mapSize];
                bArr4 = new byte[mapSize];
                indexColorModel.getAlphas(bArr4);
                indexColorModel.getReds(bArr);
                indexColorModel.getGreens(bArr2);
                indexColorModel.getBlues(bArr3);
            }
        } else if (numBands == 1) {
            z2 = true;
            mapSize = 256;
            iRoundBpp = sampleSize[0];
            i10 = ((this.f11836w * iRoundBpp) + 7) >> 3;
            bArr = new byte[256];
            bArr2 = new byte[256];
            bArr3 = new byte[256];
            bArr4 = new byte[256];
            for (int i11 = 0; i11 < 256; i11++) {
                bArr[i11] = (byte) i11;
                bArr2[i11] = (byte) i11;
                bArr3[i11] = (byte) i11;
                bArr4[i11] = -1;
            }
        } else if ((sampleModel instanceof SinglePixelPackedSampleModel) && z3) {
            int i12 = 0;
            for (int i13 : sampleModel.getSampleSize()) {
                i12 += i13;
            }
            iRoundBpp = roundBpp(i12);
            if (iRoundBpp != DataBuffer.getDataTypeSize(sampleModel.getDataType())) {
                z5 = false;
            }
            i10 = ((this.f11836w * iRoundBpp) + 7) >> 3;
        }
        int i14 = mapSize;
        int i15 = i10 % 4;
        if (i15 != 0) {
            i15 = 4 - i15;
        }
        int i16 = 54 + (mapSize * 4);
        int i17 = (i10 + i15) * this.f11837h;
        int i18 = i17 + i16;
        long streamPosition = this.stream.getStreamPosition();
        writeFileHeader(i18, i16);
        if (this.compressionType == 0 || this.compressionType == 3) {
            this.isTopDown = bMPImageWriteParam.isTopDown();
        } else {
            this.isTopDown = false;
        }
        writeInfoHeader(40, iRoundBpp);
        this.stream.writeInt(this.compressionType);
        this.stream.writeInt(i17);
        this.stream.writeInt(0);
        this.stream.writeInt(0);
        this.stream.writeInt(0);
        this.stream.writeInt(i14);
        if (z2) {
            if (this.compressionType == 3) {
                for (int i19 = 0; i19 < 3; i19++) {
                    this.stream.writeInt((bArr4[i19] & 255) + ((bArr[i19] & 255) * 256) + ((bArr2[i19] & 255) * 65536) + ((bArr3[i19] & 255) * 16777216));
                }
            } else {
                for (int i20 = 0; i20 < mapSize; i20++) {
                    this.stream.writeByte(bArr3[i20]);
                    this.stream.writeByte(bArr2[i20]);
                    this.stream.writeByte(bArr[i20]);
                    this.stream.writeByte(bArr4[i20]);
                }
            }
        }
        int i21 = this.f11836w * numBands;
        int[] iArr = new int[i21 * sourceXSubsampling];
        this.bpixels = new byte[i10];
        if (this.compressionType == 4 || this.compressionType == 5) {
            this.embedded_stream = new ByteArrayOutputStream();
            writeEmbedded(iIOImage, bMPImageWriteParam);
            this.embedded_stream.flush();
            int size = this.embedded_stream.size();
            long streamPosition2 = this.stream.getStreamPosition();
            this.stream.seek(streamPosition);
            writeSize(i16 + size, 2);
            this.stream.seek(streamPosition);
            writeSize(size, 34);
            this.stream.seek(streamPosition2);
            this.stream.write(this.embedded_stream.toByteArray());
            this.embedded_stream = null;
            if (abortRequested()) {
                processWriteAborted();
                return;
            } else {
                processImageComplete();
                this.stream.flushBefore(this.stream.getStreamPosition());
                return;
            }
        }
        int i22 = bandOffsets[0];
        for (int i23 = 1; i23 < bandOffsets.length; i23++) {
            if (bandOffsets[i23] > i22) {
                i22 = bandOffsets[i23];
            }
        }
        int[] iArr2 = new int[i22 + 1];
        int dataTypeSize = i10;
        if (z5 && z3) {
            dataTypeSize = i10 / (DataBuffer.getDataTypeSize(dataType) >> 3);
        }
        for (int i24 = 0; i24 < this.f11837h && !abortRequested(); i24++) {
            int i25 = i3 + i24;
            if (!this.isTopDown) {
                i25 = ((i3 + this.f11837h) - i24) - 1;
            }
            Raster data = raster;
            Rectangle rectangle2 = new Rectangle((i2 * sourceXSubsampling) + i4, (i25 * sourceYSubsampling) + i5, ((this.f11836w - 1) * sourceXSubsampling) + 1, 1);
            if (!zHasRaster) {
                data = renderedImage.getData(rectangle2);
            }
            if (z5 && z3) {
                SampleModel sampleModel2 = data.getSampleModel();
                int offset = 0;
                int sampleModelTranslateX = rectangle2.f12372x - data.getSampleModelTranslateX();
                int sampleModelTranslateY = rectangle2.f12373y - data.getSampleModelTranslateY();
                if (sampleModel2 instanceof ComponentSampleModel) {
                    ComponentSampleModel componentSampleModel = (ComponentSampleModel) sampleModel2;
                    offset = componentSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY, 0);
                    for (int i26 = 1; i26 < componentSampleModel.getNumBands(); i26++) {
                        if (offset > componentSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY, i26)) {
                            offset = componentSampleModel.getOffset(sampleModelTranslateX, sampleModelTranslateY, i26);
                        }
                    }
                } else if (sampleModel2 instanceof MultiPixelPackedSampleModel) {
                    offset = ((MultiPixelPackedSampleModel) sampleModel2).getOffset(sampleModelTranslateX, sampleModelTranslateY);
                } else if (sampleModel2 instanceof SinglePixelPackedSampleModel) {
                    offset = ((SinglePixelPackedSampleModel) sampleModel2).getOffset(sampleModelTranslateX, sampleModelTranslateY);
                }
                if (this.compressionType == 0 || this.compressionType == 3) {
                    switch (dataType) {
                        case 0:
                            this.stream.write(((DataBufferByte) data.getDataBuffer()).getData(), offset, dataTypeSize);
                            break;
                        case 1:
                            this.stream.writeShorts(((DataBufferUShort) data.getDataBuffer()).getData(), offset, dataTypeSize);
                            break;
                        case 2:
                            this.stream.writeShorts(((DataBufferShort) data.getDataBuffer()).getData(), offset, dataTypeSize);
                            break;
                        case 3:
                            this.stream.writeInts(((DataBufferInt) data.getDataBuffer()).getData(), offset, dataTypeSize);
                            break;
                    }
                    for (int i27 = 0; i27 < i15; i27++) {
                        this.stream.writeByte(0);
                    }
                } else if (this.compressionType == 2) {
                    if (this.bpixels == null || this.bpixels.length < i21) {
                        this.bpixels = new byte[i21];
                    }
                    data.getPixels(rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, iArr);
                    for (int i28 = 0; i28 < i21; i28++) {
                        this.bpixels[i28] = (byte) iArr[i28];
                    }
                    encodeRLE4(this.bpixels, i21);
                } else if (this.compressionType == 1) {
                    if (this.bpixels == null || this.bpixels.length < i21) {
                        this.bpixels = new byte[i21];
                    }
                    data.getPixels(rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, iArr);
                    for (int i29 = 0; i29 < i21; i29++) {
                        this.bpixels[i29] = (byte) iArr[i29];
                    }
                    encodeRLE8(this.bpixels, i21);
                }
            } else {
                data.getPixels(rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height, iArr);
                if (sourceXSubsampling != 1 || i22 != numBands - 1) {
                    int i30 = 0;
                    int i31 = 0;
                    int i32 = 0;
                    while (true) {
                        int i33 = i32;
                        if (i30 < this.f11836w) {
                            System.arraycopy(iArr, i31, iArr2, 0, iArr2.length);
                            for (int i34 = 0; i34 < numBands; i34++) {
                                iArr[i33 + i34] = iArr2[sourceBands[i34]];
                            }
                            i30++;
                            i31 += sourceXSubsampling * numBands;
                            i32 = i33 + numBands;
                        }
                    }
                }
                writePixels(0, i21, iRoundBpp, iArr, i15, numBands, indexColorModel);
            }
            processImageProgress(100.0f * (i24 / this.f11837h));
        }
        if (this.compressionType == 2 || this.compressionType == 1) {
            this.stream.writeByte(0);
            this.stream.writeByte(1);
            incCompImageSize(2);
            int i35 = this.compImageSize;
            int i36 = this.compImageSize + i16;
            long streamPosition3 = this.stream.getStreamPosition();
            this.stream.seek(streamPosition);
            writeSize(i36, 2);
            this.stream.seek(streamPosition);
            writeSize(i35, 34);
            this.stream.seek(streamPosition3);
        }
        if (abortRequested()) {
            processWriteAborted();
        } else {
            processImageComplete();
            this.stream.flushBefore(this.stream.getStreamPosition());
        }
    }

    private void writePixels(int i2, int i3, int i4, int[] iArr, int i5, int i6, IndexColorModel indexColorModel) throws IOException {
        int i7 = 0;
        switch (i4) {
            case 1:
                for (int i8 = 0; i8 < i3 / 8; i8++) {
                    byte[] bArr = this.bpixels;
                    int i9 = i7;
                    i7++;
                    int i10 = i2;
                    int i11 = i2 + 1;
                    int i12 = i11 + 1;
                    int i13 = (iArr[i10] << 7) | (iArr[i11] << 6);
                    int i14 = i12 + 1;
                    int i15 = i13 | (iArr[i12] << 5);
                    int i16 = i14 + 1;
                    int i17 = i15 | (iArr[i14] << 4);
                    int i18 = i16 + 1;
                    int i19 = i17 | (iArr[i16] << 3);
                    int i20 = i18 + 1;
                    int i21 = i19 | (iArr[i18] << 2);
                    int i22 = i20 + 1;
                    int i23 = i21 | (iArr[i20] << 1);
                    i2 = i22 + 1;
                    bArr[i9] = (byte) (i23 | iArr[i22]);
                }
                if (i3 % 8 > 0) {
                    int i24 = 0;
                    for (int i25 = 0; i25 < i3 % 8; i25++) {
                        int i26 = i2;
                        i2++;
                        i24 |= iArr[i26] << (7 - i25);
                    }
                    int i27 = i7;
                    int i28 = i7 + 1;
                    this.bpixels[i27] = (byte) i24;
                }
                this.stream.write(this.bpixels, 0, (i3 + 7) / 8);
                break;
            case 4:
                if (this.compressionType == 2) {
                    byte[] bArr2 = new byte[i3];
                    for (int i29 = 0; i29 < i3; i29++) {
                        int i30 = i2;
                        i2++;
                        bArr2[i29] = (byte) iArr[i30];
                    }
                    encodeRLE4(bArr2, i3);
                    break;
                } else {
                    for (int i31 = 0; i31 < i3 / 2; i31++) {
                        int i32 = i2;
                        int i33 = i2 + 1;
                        i2 = i33 + 1;
                        int i34 = (iArr[i32] << 4) | iArr[i33];
                        int i35 = i7;
                        i7++;
                        this.bpixels[i35] = (byte) i34;
                    }
                    if (i3 % 2 == 1) {
                        int i36 = i7;
                        int i37 = i7 + 1;
                        this.bpixels[i36] = (byte) (iArr[i2] << 4);
                    }
                    this.stream.write(this.bpixels, 0, (i3 + 1) / 2);
                    break;
                }
            case 8:
                if (this.compressionType == 1) {
                    for (int i38 = 0; i38 < i3; i38++) {
                        int i39 = i2;
                        i2++;
                        this.bpixels[i38] = (byte) iArr[i39];
                    }
                    encodeRLE8(this.bpixels, i3);
                    break;
                } else {
                    for (int i40 = 0; i40 < i3; i40++) {
                        int i41 = i2;
                        i2++;
                        this.bpixels[i40] = (byte) iArr[i41];
                    }
                    this.stream.write(this.bpixels, 0, i3);
                    break;
                }
            case 16:
                if (this.spixels == null) {
                    this.spixels = new short[i3 / i6];
                }
                int i42 = 0;
                int i43 = 0;
                while (i42 < i3) {
                    this.spixels[i43] = 0;
                    if (this.compressionType == 0) {
                        this.spixels[i43] = (short) (((31 & iArr[i42]) << 10) | ((31 & iArr[i42 + 1]) << 5) | (31 & iArr[i42 + 2]));
                        i42 += 3;
                    } else {
                        int i44 = 0;
                        while (i44 < i6) {
                            short[] sArr = this.spixels;
                            int i45 = i43;
                            sArr[i45] = (short) (sArr[i45] | ((iArr[i42] << this.bitPos[i44]) & this.bitMasks[i44]));
                            i44++;
                            i42++;
                        }
                    }
                    i43++;
                }
                this.stream.writeShorts(this.spixels, 0, this.spixels.length);
                break;
            case 24:
                if (i6 == 3) {
                    for (int i46 = 0; i46 < i3; i46 += 3) {
                        int i47 = i7;
                        int i48 = i7 + 1;
                        this.bpixels[i47] = (byte) iArr[i2 + 2];
                        int i49 = i48 + 1;
                        this.bpixels[i48] = (byte) iArr[i2 + 1];
                        i7 = i49 + 1;
                        this.bpixels[i49] = (byte) iArr[i2];
                        i2 += 3;
                    }
                    this.stream.write(this.bpixels, 0, i3);
                    break;
                } else {
                    int mapSize = indexColorModel.getMapSize();
                    byte[] bArr3 = new byte[mapSize];
                    byte[] bArr4 = new byte[mapSize];
                    byte[] bArr5 = new byte[mapSize];
                    indexColorModel.getReds(bArr3);
                    indexColorModel.getGreens(bArr4);
                    indexColorModel.getBlues(bArr5);
                    for (int i50 = 0; i50 < i3; i50++) {
                        int i51 = iArr[i2];
                        int i52 = i7;
                        int i53 = i7 + 1;
                        this.bpixels[i52] = bArr5[i51];
                        int i54 = i53 + 1;
                        this.bpixels[i53] = bArr4[i51];
                        i7 = i54 + 1;
                        this.bpixels[i54] = bArr5[i51];
                        i2++;
                    }
                    this.stream.write(this.bpixels, 0, i3 * 3);
                    break;
                }
            case 32:
                if (this.ipixels == null) {
                    this.ipixels = new int[i3 / i6];
                }
                if (i6 == 3) {
                    int i55 = 0;
                    int i56 = 0;
                    while (i55 < i3) {
                        this.ipixels[i56] = 0;
                        if (this.compressionType == 0) {
                            this.ipixels[i56] = ((255 & iArr[i55 + 2]) << 16) | ((255 & iArr[i55 + 1]) << 8) | (255 & iArr[i55]);
                            i55 += 3;
                        } else {
                            int i57 = 0;
                            while (i57 < i6) {
                                int[] iArr2 = this.ipixels;
                                int i58 = i56;
                                iArr2[i58] = iArr2[i58] | ((iArr[i55] << this.bitPos[i57]) & this.bitMasks[i57]);
                                i57++;
                                i55++;
                            }
                        }
                        i56++;
                    }
                } else {
                    for (int i59 = 0; i59 < i3; i59++) {
                        if (indexColorModel != null) {
                            this.ipixels[i59] = indexColorModel.getRGB(iArr[i59]);
                        } else {
                            this.ipixels[i59] = (iArr[i59] << 16) | (iArr[i59] << 8) | iArr[i59];
                        }
                    }
                }
                this.stream.writeInts(this.ipixels, 0, this.ipixels.length);
                break;
        }
        if (this.compressionType == 0 || this.compressionType == 3) {
            for (int i60 = 0; i60 < i5; i60++) {
                this.stream.writeByte(0);
            }
        }
    }

    private void encodeRLE8(byte[] bArr, int i2) throws IOException {
        int i3 = 1;
        int i4 = -1;
        int i5 = (-1) + 1;
        byte b2 = bArr[i5];
        byte[] bArr2 = new byte[256];
        while (i5 < i2 - 1) {
            i5++;
            byte b3 = bArr[i5];
            if (b3 == b2) {
                if (i4 >= 3) {
                    this.stream.writeByte(0);
                    this.stream.writeByte(i4);
                    incCompImageSize(2);
                    for (int i6 = 0; i6 < i4; i6++) {
                        this.stream.writeByte(bArr2[i6]);
                        incCompImageSize(1);
                    }
                    if (!isEven(i4)) {
                        this.stream.writeByte(0);
                        incCompImageSize(1);
                    }
                } else if (i4 > -1) {
                    for (int i7 = 0; i7 < i4; i7++) {
                        this.stream.writeByte(1);
                        this.stream.writeByte(bArr2[i7]);
                        incCompImageSize(2);
                    }
                }
                i4 = -1;
                i3++;
                if (i3 == 256) {
                    this.stream.writeByte(i3 - 1);
                    this.stream.writeByte(b2);
                    incCompImageSize(2);
                    i3 = 1;
                }
            } else {
                if (i3 > 1) {
                    this.stream.writeByte(i3);
                    this.stream.writeByte(b2);
                    incCompImageSize(2);
                } else if (i4 < 0) {
                    int i8 = i4 + 1;
                    bArr2[i8] = b2;
                    i4 = i8 + 1;
                    bArr2[i4] = b3;
                } else if (i4 < 254) {
                    i4++;
                    bArr2[i4] = b3;
                } else {
                    this.stream.writeByte(0);
                    this.stream.writeByte(i4 + 1);
                    incCompImageSize(2);
                    for (int i9 = 0; i9 <= i4; i9++) {
                        this.stream.writeByte(bArr2[i9]);
                        incCompImageSize(1);
                    }
                    this.stream.writeByte(0);
                    incCompImageSize(1);
                    i4 = -1;
                }
                b2 = b3;
                i3 = 1;
            }
            if (i5 == i2 - 1) {
                if (i4 == -1) {
                    this.stream.writeByte(i3);
                    this.stream.writeByte(b2);
                    incCompImageSize(2);
                    i3 = 1;
                } else if (i4 >= 2) {
                    this.stream.writeByte(0);
                    this.stream.writeByte(i4 + 1);
                    incCompImageSize(2);
                    for (int i10 = 0; i10 <= i4; i10++) {
                        this.stream.writeByte(bArr2[i10]);
                        incCompImageSize(1);
                    }
                    if (!isEven(i4 + 1)) {
                        this.stream.writeByte(0);
                        incCompImageSize(1);
                    }
                } else if (i4 > -1) {
                    for (int i11 = 0; i11 <= i4; i11++) {
                        this.stream.writeByte(1);
                        this.stream.writeByte(bArr2[i11]);
                        incCompImageSize(2);
                    }
                }
                this.stream.writeByte(0);
                this.stream.writeByte(0);
                incCompImageSize(2);
            }
        }
    }

    private void encodeRLE4(byte[] bArr, int i2) throws IOException {
        int i3 = 2;
        int i4 = -1;
        byte[] bArr2 = new byte[256];
        int i5 = (-1) + 1;
        byte b2 = bArr[i5];
        int i6 = i5 + 1;
        byte b3 = bArr[i6];
        while (i6 < i2 - 2) {
            int i7 = i6 + 1;
            byte b4 = bArr[i7];
            i6 = i7 + 1;
            byte b5 = bArr[i6];
            if (b4 == b2) {
                if (i4 >= 4) {
                    this.stream.writeByte(0);
                    this.stream.writeByte(i4 - 1);
                    incCompImageSize(2);
                    for (int i8 = 0; i8 < i4 - 2; i8 += 2) {
                        this.stream.writeByte((byte) ((bArr2[i8] << 4) | bArr2[i8 + 1]));
                        incCompImageSize(1);
                    }
                    if (!isEven(i4 - 1)) {
                        this.stream.writeByte((bArr2[i4 - 2] << 4) | 0);
                        incCompImageSize(1);
                    }
                    if (!isEven((int) Math.ceil((i4 - 1) / 2))) {
                        this.stream.writeByte(0);
                        incCompImageSize(1);
                    }
                } else if (i4 > -1) {
                    this.stream.writeByte(2);
                    this.stream.writeByte((bArr2[0] << 4) | bArr2[1]);
                    incCompImageSize(2);
                }
                i4 = -1;
                if (b5 == b3) {
                    i3 += 2;
                    if (i3 == 256) {
                        this.stream.writeByte(i3 - 1);
                        this.stream.writeByte((b2 << 4) | b3);
                        incCompImageSize(2);
                        i3 = 2;
                        if (i6 < i2 - 1) {
                            b2 = b3;
                            i6++;
                            b3 = bArr[i6];
                        } else {
                            this.stream.writeByte(1);
                            this.stream.writeByte((b3 << 4) | 0);
                            incCompImageSize(2);
                            i3 = -1;
                        }
                    }
                } else {
                    this.stream.writeByte(i3 + 1);
                    this.stream.writeByte((b2 << 4) | b3);
                    incCompImageSize(2);
                    i3 = 2;
                    b2 = b5;
                    if (i6 < i2 - 1) {
                        i6++;
                        b3 = bArr[i6];
                    } else {
                        this.stream.writeByte(1);
                        this.stream.writeByte((b5 << 4) | 0);
                        incCompImageSize(2);
                        i3 = -1;
                    }
                }
            } else {
                if (i3 > 2) {
                    this.stream.writeByte(i3);
                    this.stream.writeByte((b2 << 4) | b3);
                    incCompImageSize(2);
                } else if (i4 < 0) {
                    int i9 = i4 + 1;
                    bArr2[i9] = b2;
                    int i10 = i9 + 1;
                    bArr2[i10] = b3;
                    int i11 = i10 + 1;
                    bArr2[i11] = b4;
                    i4 = i11 + 1;
                    bArr2[i4] = b5;
                } else if (i4 < 253) {
                    int i12 = i4 + 1;
                    bArr2[i12] = b4;
                    i4 = i12 + 1;
                    bArr2[i4] = b5;
                } else {
                    this.stream.writeByte(0);
                    this.stream.writeByte(i4 + 1);
                    incCompImageSize(2);
                    for (int i13 = 0; i13 < i4; i13 += 2) {
                        this.stream.writeByte((byte) ((bArr2[i13] << 4) | bArr2[i13 + 1]));
                        incCompImageSize(1);
                    }
                    this.stream.writeByte(0);
                    incCompImageSize(1);
                    i4 = -1;
                }
                b2 = b4;
                b3 = b5;
                i3 = 2;
            }
            if (i6 >= i2 - 2) {
                if (i4 == -1 && i3 >= 2) {
                    if (i6 == i2 - 2) {
                        i6++;
                        if (bArr[i6] == b2) {
                            i3++;
                            this.stream.writeByte(i3);
                            this.stream.writeByte((b2 << 4) | b3);
                            incCompImageSize(2);
                        } else {
                            this.stream.writeByte(i3);
                            this.stream.writeByte((b2 << 4) | b3);
                            this.stream.writeByte(1);
                            this.stream.writeByte((bArr[i6] << 4) | 0);
                            int i14 = (bArr[i6] << 4) | 0;
                            incCompImageSize(4);
                        }
                    } else {
                        this.stream.writeByte(i3);
                        this.stream.writeByte((b2 << 4) | b3);
                        incCompImageSize(2);
                    }
                } else if (i4 > -1) {
                    if (i6 == i2 - 2) {
                        i4++;
                        i6++;
                        bArr2[i4] = bArr[i6];
                    }
                    if (i4 >= 2) {
                        this.stream.writeByte(0);
                        this.stream.writeByte(i4 + 1);
                        incCompImageSize(2);
                        for (int i15 = 0; i15 < i4; i15 += 2) {
                            this.stream.writeByte((byte) ((bArr2[i15] << 4) | bArr2[i15 + 1]));
                            incCompImageSize(1);
                        }
                        if (!isEven(i4 + 1)) {
                            this.stream.writeByte((bArr2[i4] << 4) | 0);
                            incCompImageSize(1);
                        }
                        if (!isEven((int) Math.ceil((i4 + 1) / 2))) {
                            this.stream.writeByte(0);
                            incCompImageSize(1);
                        }
                    } else {
                        switch (i4) {
                            case 0:
                                this.stream.writeByte(1);
                                this.stream.writeByte((bArr2[0] << 4) | 0);
                                incCompImageSize(2);
                                break;
                            case 1:
                                this.stream.writeByte(2);
                                this.stream.writeByte((bArr2[0] << 4) | bArr2[1]);
                                incCompImageSize(2);
                                break;
                        }
                    }
                }
                this.stream.writeByte(0);
                this.stream.writeByte(0);
                incCompImageSize(2);
            }
        }
    }

    private synchronized void incCompImageSize(int i2) {
        this.compImageSize += i2;
    }

    private boolean isEven(int i2) {
        return i2 % 2 == 0;
    }

    private void writeFileHeader(int i2, int i3) throws IOException {
        this.stream.writeByte(66);
        this.stream.writeByte(77);
        this.stream.writeInt(i2);
        this.stream.writeInt(0);
        this.stream.writeInt(i3);
    }

    private void writeInfoHeader(int i2, int i3) throws IOException {
        this.stream.writeInt(i2);
        this.stream.writeInt(this.f11836w);
        this.stream.writeInt(this.isTopDown ? -this.f11837h : this.f11837h);
        this.stream.writeShort(1);
        this.stream.writeShort(i3);
    }

    private void writeSize(int i2, int i3) throws IOException {
        this.stream.skipBytes(i3);
        this.stream.writeInt(i2);
    }

    @Override // javax.imageio.ImageWriter
    public void reset() {
        super.reset();
        this.stream = null;
    }

    private void writeEmbedded(IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        String str = this.compressionType == 4 ? "jpeg" : "png";
        Iterator<ImageWriter> imageWritersByFormatName = ImageIO.getImageWritersByFormatName(str);
        ImageWriter next = null;
        if (imageWritersByFormatName.hasNext()) {
            next = imageWritersByFormatName.next();
        }
        if (next != null) {
            if (this.embedded_stream == null) {
                throw new RuntimeException("No stream for writing embedded image!");
            }
            next.addIIOWriteProgressListener(new IIOWriteProgressAdapter() { // from class: com.sun.imageio.plugins.bmp.BMPImageWriter.1
                @Override // com.sun.imageio.plugins.bmp.BMPImageWriter.IIOWriteProgressAdapter, javax.imageio.event.IIOWriteProgressListener
                public void imageProgress(ImageWriter imageWriter, float f2) {
                    BMPImageWriter.this.processImageProgress(f2);
                }
            });
            next.addIIOWriteWarningListener(new IIOWriteWarningListener() { // from class: com.sun.imageio.plugins.bmp.BMPImageWriter.2
                @Override // javax.imageio.event.IIOWriteWarningListener
                public void warningOccurred(ImageWriter imageWriter, int i2, String str2) {
                    BMPImageWriter.this.processWarningOccurred(i2, str2);
                }
            });
            next.setOutput(ImageIO.createImageOutputStream(this.embedded_stream));
            ImageWriteParam defaultWriteParam = next.getDefaultWriteParam();
            defaultWriteParam.setDestinationOffset(imageWriteParam.getDestinationOffset());
            defaultWriteParam.setSourceBands(imageWriteParam.getSourceBands());
            defaultWriteParam.setSourceRegion(imageWriteParam.getSourceRegion());
            defaultWriteParam.setSourceSubsampling(imageWriteParam.getSourceXSubsampling(), imageWriteParam.getSourceYSubsampling(), imageWriteParam.getSubsamplingXOffset(), imageWriteParam.getSubsamplingYOffset());
            next.write(null, iIOImage, defaultWriteParam);
            return;
        }
        throw new RuntimeException(I18N.getString("BMPImageWrite5") + " " + str);
    }

    private int firstLowBit(int i2) {
        int i3 = 0;
        while ((i2 & 1) == 0) {
            i3++;
            i2 >>>= 1;
        }
        return i3;
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPImageWriter$IIOWriteProgressAdapter.class */
    private class IIOWriteProgressAdapter implements IIOWriteProgressListener {
        private IIOWriteProgressAdapter() {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void imageComplete(ImageWriter imageWriter) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void imageProgress(ImageWriter imageWriter, float f2) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void imageStarted(ImageWriter imageWriter, int i2) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void thumbnailComplete(ImageWriter imageWriter) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void thumbnailProgress(ImageWriter imageWriter, float f2) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void thumbnailStarted(ImageWriter imageWriter, int i2, int i3) {
        }

        @Override // javax.imageio.event.IIOWriteProgressListener
        public void writeAborted(ImageWriter imageWriter) {
        }
    }

    protected int getPreferredCompressionType(ColorModel colorModel, SampleModel sampleModel) {
        return getPreferredCompressionType(new ImageTypeSpecifier(colorModel, sampleModel));
    }

    protected int getPreferredCompressionType(ImageTypeSpecifier imageTypeSpecifier) {
        if (imageTypeSpecifier.getBufferedImageType() == 8) {
            return 3;
        }
        return 0;
    }

    protected boolean canEncodeImage(int i2, ColorModel colorModel, SampleModel sampleModel) {
        return canEncodeImage(i2, new ImageTypeSpecifier(colorModel, sampleModel));
    }

    protected boolean canEncodeImage(int i2, ImageTypeSpecifier imageTypeSpecifier) {
        if (!getOriginatingProvider().canEncodeImage(imageTypeSpecifier)) {
            return false;
        }
        imageTypeSpecifier.getBufferedImageType();
        int pixelSize = imageTypeSpecifier.getColorModel().getPixelSize();
        if (this.compressionType == 2 && pixelSize != 4) {
            return false;
        }
        if (this.compressionType == 1 && pixelSize != 8) {
            return false;
        }
        if (pixelSize == 16) {
            boolean z2 = false;
            boolean z3 = false;
            SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
            if (sampleModel instanceof SinglePixelPackedSampleModel) {
                int[] sampleSize = ((SinglePixelPackedSampleModel) sampleModel).getSampleSize();
                z2 = true;
                z3 = true;
                int i3 = 0;
                while (i3 < sampleSize.length) {
                    z2 &= sampleSize[i3] == 5;
                    z3 &= sampleSize[i3] == 5 || (i3 == 1 && sampleSize[i3] == 6);
                    i3++;
                }
            }
            return (this.compressionType == 0 && z2) || (this.compressionType == 3 && z3);
        }
        return true;
    }

    protected void writeMaskToPalette(int i2, int i3, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        bArr3[i3] = (byte) (255 & (i2 >> 24));
        bArr2[i3] = (byte) (255 & (i2 >> 16));
        bArr[i3] = (byte) (255 & (i2 >> 8));
        bArr4[i3] = (byte) (255 & i2);
    }

    private int roundBpp(int i2) {
        if (i2 <= 8) {
            return 8;
        }
        if (i2 <= 16) {
            return 16;
        }
        if (i2 <= 24) {
            return 24;
        }
        return 32;
    }
}
