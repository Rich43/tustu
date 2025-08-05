package com.sun.imageio.plugins.gif;

import com.sun.imageio.plugins.common.LZWCompressor;
import com.sun.imageio.plugins.common.PaletteBuilder;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.apache.commons.net.telnet.TelnetCommand;
import org.w3c.dom.NodeList;
import sun.awt.image.ByteComponentRaster;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageWriter.class */
public class GIFImageWriter extends ImageWriter {
    private static final boolean DEBUG = false;
    static final String STANDARD_METADATA_NAME = "javax_imageio_1.0";
    static final String STREAM_METADATA_NAME = "javax_imageio_gif_stream_1.0";
    static final String IMAGE_METADATA_NAME = "javax_imageio_gif_image_1.0";
    private ImageOutputStream stream;
    private boolean isWritingSequence;
    private boolean wroteSequenceHeader;
    private GIFWritableStreamMetadata theStreamMetadata;
    private int imageIndex;

    private static int getNumBits(int i2) throws IOException {
        int i3;
        switch (i2) {
            case 2:
                i3 = 1;
                break;
            case 4:
                i3 = 2;
                break;
            case 8:
                i3 = 3;
                break;
            case 16:
                i3 = 4;
                break;
            case 32:
                i3 = 5;
                break;
            case 64:
                i3 = 6;
                break;
            case 128:
                i3 = 7;
                break;
            case 256:
                i3 = 8;
                break;
            default:
                throw new IOException("Bad palette length: " + i2 + "!");
        }
        return i3;
    }

    private static void computeRegions(Rectangle rectangle, Dimension dimension, ImageWriteParam imageWriteParam) {
        int sourceXSubsampling = 1;
        int sourceYSubsampling = 1;
        if (imageWriteParam != null) {
            int[] sourceBands = imageWriteParam.getSourceBands();
            if (sourceBands != null && (sourceBands.length != 1 || sourceBands[0] != 0)) {
                throw new IllegalArgumentException("Cannot sub-band image!");
            }
            Rectangle sourceRegion = imageWriteParam.getSourceRegion();
            if (sourceRegion != null) {
                rectangle.setBounds(sourceRegion.intersection(rectangle));
            }
            int subsamplingXOffset = imageWriteParam.getSubsamplingXOffset();
            int subsamplingYOffset = imageWriteParam.getSubsamplingYOffset();
            rectangle.f12372x += subsamplingXOffset;
            rectangle.f12373y += subsamplingYOffset;
            rectangle.width -= subsamplingXOffset;
            rectangle.height -= subsamplingYOffset;
            sourceXSubsampling = imageWriteParam.getSourceXSubsampling();
            sourceYSubsampling = imageWriteParam.getSourceYSubsampling();
        }
        dimension.setSize(((rectangle.width + sourceXSubsampling) - 1) / sourceXSubsampling, ((rectangle.height + sourceYSubsampling) - 1) / sourceYSubsampling);
        if (dimension.width <= 0 || dimension.height <= 0) {
            throw new IllegalArgumentException("Empty source region!");
        }
    }

    private static byte[] createColorTable(ColorModel colorModel, SampleModel sampleModel) {
        byte[] bArr;
        if (colorModel instanceof IndexColorModel) {
            IndexColorModel indexColorModel = (IndexColorModel) colorModel;
            int mapSize = indexColorModel.getMapSize();
            int gifPaletteSize = getGifPaletteSize(mapSize);
            byte[] bArr2 = new byte[gifPaletteSize];
            byte[] bArr3 = new byte[gifPaletteSize];
            byte[] bArr4 = new byte[gifPaletteSize];
            indexColorModel.getReds(bArr2);
            indexColorModel.getGreens(bArr3);
            indexColorModel.getBlues(bArr4);
            for (int i2 = mapSize; i2 < gifPaletteSize; i2++) {
                bArr2[i2] = bArr2[0];
                bArr3[i2] = bArr3[0];
                bArr4[i2] = bArr4[0];
            }
            bArr = new byte[3 * gifPaletteSize];
            int i3 = 0;
            for (int i4 = 0; i4 < gifPaletteSize; i4++) {
                int i5 = i3;
                int i6 = i3 + 1;
                bArr[i5] = bArr2[i4];
                int i7 = i6 + 1;
                bArr[i6] = bArr3[i4];
                i3 = i7 + 1;
                bArr[i7] = bArr4[i4];
            }
        } else if (sampleModel.getNumBands() == 1) {
            int i8 = sampleModel.getSampleSize()[0];
            if (i8 > 8) {
                i8 = 8;
            }
            int i9 = 3 * (1 << i8);
            bArr = new byte[i9];
            for (int i10 = 0; i10 < i9; i10++) {
                bArr[i10] = (byte) (i10 / 3);
            }
        } else {
            bArr = null;
        }
        return bArr;
    }

    private static int getGifPaletteSize(int i2) {
        if (i2 <= 2) {
            return 2;
        }
        int i3 = i2 - 1;
        int i4 = i3 | (i3 >> 1);
        int i5 = i4 | (i4 >> 2);
        int i6 = i5 | (i5 >> 4);
        int i7 = i6 | (i6 >> 8);
        return (i7 | (i7 >> 16)) + 1;
    }

    public GIFImageWriter(GIFImageWriterSpi gIFImageWriterSpi) {
        super(gIFImageWriterSpi);
        this.stream = null;
        this.isWritingSequence = false;
        this.wroteSequenceHeader = false;
        this.theStreamMetadata = null;
        this.imageIndex = 0;
    }

    @Override // javax.imageio.ImageWriter
    public boolean canWriteSequence() {
        return true;
    }

    private void convertMetadata(String str, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        String str2 = null;
        String nativeMetadataFormatName = iIOMetadata.getNativeMetadataFormatName();
        if (nativeMetadataFormatName != null && nativeMetadataFormatName.equals(str)) {
            str2 = str;
        } else {
            String[] extraMetadataFormatNames = iIOMetadata.getExtraMetadataFormatNames();
            if (extraMetadataFormatNames != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= extraMetadataFormatNames.length) {
                        break;
                    }
                    if (!extraMetadataFormatNames[i2].equals(str)) {
                        i2++;
                    } else {
                        str2 = str;
                        break;
                    }
                }
            }
        }
        if (str2 == null && iIOMetadata.isStandardMetadataFormatSupported()) {
            str2 = "javax_imageio_1.0";
        }
        if (str2 != null) {
            try {
                iIOMetadata2.mergeTree(str2, iIOMetadata.getAsTree(str2));
            } catch (IIOInvalidTreeException e2) {
            }
        }
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam) {
        if (iIOMetadata == null) {
            throw new IllegalArgumentException("inData == null!");
        }
        IIOMetadata defaultStreamMetadata = getDefaultStreamMetadata(imageWriteParam);
        convertMetadata(STREAM_METADATA_NAME, iIOMetadata, defaultStreamMetadata);
        return defaultStreamMetadata;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        if (iIOMetadata == null) {
            throw new IllegalArgumentException("inData == null!");
        }
        if (imageTypeSpecifier == null) {
            throw new IllegalArgumentException("imageType == null!");
        }
        GIFWritableImageMetadata gIFWritableImageMetadata = (GIFWritableImageMetadata) getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
        boolean z2 = gIFWritableImageMetadata.interlaceFlag;
        convertMetadata(IMAGE_METADATA_NAME, iIOMetadata, gIFWritableImageMetadata);
        if (imageWriteParam != null && imageWriteParam.canWriteProgressive() && imageWriteParam.getProgressiveMode() != 3) {
            gIFWritableImageMetadata.interlaceFlag = z2;
        }
        return gIFWritableImageMetadata;
    }

    @Override // javax.imageio.ImageWriter
    public void endWriteSequence() throws IOException {
        if (this.stream == null) {
            throw new IllegalStateException("output == null!");
        }
        if (!this.isWritingSequence) {
            throw new IllegalStateException("prepareWriteSequence() was not invoked!");
        }
        writeTrailer();
        resetLocal();
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        int transparentPixel;
        GIFWritableImageMetadata gIFWritableImageMetadata = new GIFWritableImageMetadata();
        SampleModel sampleModel = imageTypeSpecifier.getSampleModel();
        Rectangle rectangle = new Rectangle(sampleModel.getWidth(), sampleModel.getHeight());
        Dimension dimension = new Dimension();
        computeRegions(rectangle, dimension, imageWriteParam);
        gIFWritableImageMetadata.imageWidth = dimension.width;
        gIFWritableImageMetadata.imageHeight = dimension.height;
        if (imageWriteParam != null && imageWriteParam.canWriteProgressive() && imageWriteParam.getProgressiveMode() == 0) {
            gIFWritableImageMetadata.interlaceFlag = false;
        } else {
            gIFWritableImageMetadata.interlaceFlag = true;
        }
        ColorModel colorModel = imageTypeSpecifier.getColorModel();
        gIFWritableImageMetadata.localColorTable = createColorTable(colorModel, sampleModel);
        if ((colorModel instanceof IndexColorModel) && (transparentPixel = ((IndexColorModel) colorModel).getTransparentPixel()) != -1) {
            gIFWritableImageMetadata.transparentColorFlag = true;
            gIFWritableImageMetadata.transparentColorIndex = transparentPixel;
        }
        return gIFWritableImageMetadata;
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        GIFWritableStreamMetadata gIFWritableStreamMetadata = new GIFWritableStreamMetadata();
        gIFWritableStreamMetadata.version = "89a";
        return gIFWritableStreamMetadata;
    }

    @Override // javax.imageio.ImageWriter
    public ImageWriteParam getDefaultWriteParam() {
        return new GIFImageWriteParam(getLocale());
    }

    @Override // javax.imageio.ImageWriter
    public void prepareWriteSequence(IIOMetadata iIOMetadata) throws IOException {
        if (this.stream == null) {
            throw new IllegalStateException("Output is not set.");
        }
        resetLocal();
        if (iIOMetadata == null) {
            this.theStreamMetadata = (GIFWritableStreamMetadata) getDefaultStreamMetadata(null);
        } else {
            this.theStreamMetadata = new GIFWritableStreamMetadata();
            convertMetadata(STREAM_METADATA_NAME, iIOMetadata, this.theStreamMetadata);
        }
        this.isWritingSequence = true;
    }

    @Override // javax.imageio.ImageWriter
    public void reset() {
        super.reset();
        resetLocal();
    }

    private void resetLocal() {
        this.isWritingSequence = false;
        this.wroteSequenceHeader = false;
        this.theStreamMetadata = null;
        this.imageIndex = 0;
    }

    @Override // javax.imageio.ImageWriter
    public void setOutput(Object obj) {
        super.setOutput(obj);
        if (obj != null) {
            if (!(obj instanceof ImageOutputStream)) {
                throw new IllegalArgumentException("output is not an ImageOutputStream");
            }
            this.stream = (ImageOutputStream) obj;
            this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            return;
        }
        this.stream = null;
    }

    @Override // javax.imageio.ImageWriter
    public void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        GIFWritableStreamMetadata gIFWritableStreamMetadata;
        if (this.stream == null) {
            throw new IllegalStateException("output == null!");
        }
        if (iIOImage == null) {
            throw new IllegalArgumentException("iioimage == null!");
        }
        if (iIOImage.hasRaster()) {
            throw new UnsupportedOperationException("canWriteRasters() == false!");
        }
        resetLocal();
        if (iIOMetadata == null) {
            gIFWritableStreamMetadata = (GIFWritableStreamMetadata) getDefaultStreamMetadata(imageWriteParam);
        } else {
            gIFWritableStreamMetadata = (GIFWritableStreamMetadata) convertStreamMetadata(iIOMetadata, imageWriteParam);
        }
        write(true, true, gIFWritableStreamMetadata, iIOImage, imageWriteParam);
    }

    @Override // javax.imageio.ImageWriter
    public void writeToSequence(IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        if (this.stream == null) {
            throw new IllegalStateException("output == null!");
        }
        if (iIOImage == null) {
            throw new IllegalArgumentException("image == null!");
        }
        if (iIOImage.hasRaster()) {
            throw new UnsupportedOperationException("canWriteRasters() == false!");
        }
        if (!this.isWritingSequence) {
            throw new IllegalStateException("prepareWriteSequence() was not invoked!");
        }
        write(!this.wroteSequenceHeader, false, this.theStreamMetadata, iIOImage, imageWriteParam);
        if (!this.wroteSequenceHeader) {
            this.wroteSequenceHeader = true;
        }
        this.imageIndex++;
    }

    private boolean needToCreateIndex(RenderedImage renderedImage) {
        SampleModel sampleModel = renderedImage.getSampleModel();
        return sampleModel.getNumBands() != 1 || sampleModel.getSampleSize()[0] > 8 || renderedImage.getColorModel().getComponentSize()[0] > 8;
    }

    private void write(boolean z2, boolean z3, IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        byte[] bArr;
        int sampleSize;
        int i2;
        clearAbortRequest();
        RenderedImage renderedImage = iIOImage.getRenderedImage();
        if (needToCreateIndex(renderedImage)) {
            renderedImage = PaletteBuilder.createIndexedImage(renderedImage);
            iIOImage.setRenderedImage(renderedImage);
        }
        ColorModel colorModel = renderedImage.getColorModel();
        SampleModel sampleModel = renderedImage.getSampleModel();
        Rectangle rectangle = new Rectangle(renderedImage.getMinX(), renderedImage.getMinY(), renderedImage.getWidth(), renderedImage.getHeight());
        Dimension dimension = new Dimension();
        computeRegions(rectangle, dimension, imageWriteParam);
        GIFWritableImageMetadata gIFWritableImageMetadata = null;
        if (iIOImage.getMetadata() != null) {
            gIFWritableImageMetadata = new GIFWritableImageMetadata();
            convertMetadata(IMAGE_METADATA_NAME, iIOImage.getMetadata(), gIFWritableImageMetadata);
            if (gIFWritableImageMetadata.localColorTable == null) {
                gIFWritableImageMetadata.localColorTable = createColorTable(colorModel, sampleModel);
                if (colorModel instanceof IndexColorModel) {
                    int transparentPixel = ((IndexColorModel) colorModel).getTransparentPixel();
                    gIFWritableImageMetadata.transparentColorFlag = transparentPixel != -1;
                    if (gIFWritableImageMetadata.transparentColorFlag) {
                        gIFWritableImageMetadata.transparentColorIndex = transparentPixel;
                    }
                }
            }
        }
        if (z2) {
            if (iIOMetadata == null) {
                throw new IllegalArgumentException("Cannot write null header!");
            }
            GIFWritableStreamMetadata gIFWritableStreamMetadata = (GIFWritableStreamMetadata) iIOMetadata;
            if (gIFWritableStreamMetadata.version == null) {
                gIFWritableStreamMetadata.version = "89a";
            }
            if (gIFWritableStreamMetadata.logicalScreenWidth == -1) {
                gIFWritableStreamMetadata.logicalScreenWidth = dimension.width;
            }
            if (gIFWritableStreamMetadata.logicalScreenHeight == -1) {
                gIFWritableStreamMetadata.logicalScreenHeight = dimension.height;
            }
            if (gIFWritableStreamMetadata.colorResolution == -1) {
                if (colorModel != null) {
                    i2 = colorModel.getComponentSize()[0];
                } else {
                    i2 = sampleModel.getSampleSize()[0];
                }
                gIFWritableStreamMetadata.colorResolution = i2;
            }
            if (gIFWritableStreamMetadata.globalColorTable == null) {
                if (this.isWritingSequence && gIFWritableImageMetadata != null && gIFWritableImageMetadata.localColorTable != null) {
                    gIFWritableStreamMetadata.globalColorTable = gIFWritableImageMetadata.localColorTable;
                } else if (gIFWritableImageMetadata == null || gIFWritableImageMetadata.localColorTable == null) {
                    gIFWritableStreamMetadata.globalColorTable = createColorTable(colorModel, sampleModel);
                }
            }
            bArr = gIFWritableStreamMetadata.globalColorTable;
            if (bArr != null) {
                sampleSize = getNumBits(bArr.length / 3);
            } else if (gIFWritableImageMetadata != null && gIFWritableImageMetadata.localColorTable != null) {
                sampleSize = getNumBits(gIFWritableImageMetadata.localColorTable.length / 3);
            } else {
                sampleSize = sampleModel.getSampleSize(0);
            }
            writeHeader(gIFWritableStreamMetadata, sampleSize);
        } else if (this.isWritingSequence) {
            bArr = this.theStreamMetadata.globalColorTable;
        } else {
            throw new IllegalArgumentException("Must write header for single image!");
        }
        writeImage(iIOImage.getRenderedImage(), gIFWritableImageMetadata, imageWriteParam, bArr, rectangle, dimension);
        if (z3) {
            writeTrailer();
        }
    }

    private void writeImage(RenderedImage renderedImage, GIFWritableImageMetadata gIFWritableImageMetadata, ImageWriteParam imageWriteParam, byte[] bArr, Rectangle rectangle, Dimension dimension) throws IOException {
        boolean z2;
        int length;
        renderedImage.getColorModel();
        SampleModel sampleModel = renderedImage.getSampleModel();
        if (gIFWritableImageMetadata == null) {
            gIFWritableImageMetadata = (GIFWritableImageMetadata) getDefaultImageMetadata(new ImageTypeSpecifier(renderedImage), imageWriteParam);
            z2 = gIFWritableImageMetadata.transparentColorFlag;
        } else {
            NodeList elementsByTagName = null;
            try {
                elementsByTagName = ((IIOMetadataNode) gIFWritableImageMetadata.getAsTree(IMAGE_METADATA_NAME)).getElementsByTagName("GraphicControlExtension");
            } catch (IllegalArgumentException e2) {
            }
            z2 = elementsByTagName != null && elementsByTagName.getLength() > 0;
            if (imageWriteParam != null && imageWriteParam.canWriteProgressive()) {
                if (imageWriteParam.getProgressiveMode() == 0) {
                    gIFWritableImageMetadata.interlaceFlag = false;
                } else if (imageWriteParam.getProgressiveMode() == 1) {
                    gIFWritableImageMetadata.interlaceFlag = true;
                }
            }
        }
        if (Arrays.equals(bArr, gIFWritableImageMetadata.localColorTable)) {
            gIFWritableImageMetadata.localColorTable = null;
        }
        gIFWritableImageMetadata.imageWidth = dimension.width;
        gIFWritableImageMetadata.imageHeight = dimension.height;
        if (z2) {
            writeGraphicControlExtension(gIFWritableImageMetadata);
        }
        writePlainTextExtension(gIFWritableImageMetadata);
        writeApplicationExtension(gIFWritableImageMetadata);
        writeCommentExtension(gIFWritableImageMetadata);
        if (gIFWritableImageMetadata.localColorTable == null) {
            length = bArr == null ? sampleModel.getSampleSize(0) : bArr.length / 3;
        } else {
            length = gIFWritableImageMetadata.localColorTable.length / 3;
        }
        writeImageDescriptor(gIFWritableImageMetadata, getNumBits(length));
        writeRasterData(renderedImage, rectangle, dimension, imageWriteParam, gIFWritableImageMetadata.interlaceFlag);
    }

    private void writeRows(RenderedImage renderedImage, LZWCompressor lZWCompressor, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12) throws IOException {
        int[] iArr = new int[i6];
        byte[] bArr = new byte[i9];
        Raster tile = (renderedImage.getNumXTiles() == 1 && renderedImage.getNumYTiles() == 1) ? renderedImage.getTile(0, 0) : renderedImage.getData();
        int i13 = i7;
        while (true) {
            int i14 = i13;
            if (i14 < i10) {
                if (i11 % i12 == 0) {
                    if (abortRequested()) {
                        processWriteAborted();
                        return;
                    }
                    processImageProgress((i11 * 100.0f) / i10);
                }
                tile.getSamples(i2, i4, i6, 1, 0, iArr);
                int i15 = 0;
                int i16 = 0;
                while (true) {
                    int i17 = i16;
                    if (i15 < i9) {
                        bArr[i15] = (byte) iArr[i17];
                        i15++;
                        i16 = i17 + i3;
                    }
                }
                lZWCompressor.compress(bArr, 0, i9);
                i11++;
                i4 += i5;
                i13 = i14 + i8;
            } else {
                return;
            }
        }
    }

    private void writeRowsOpt(byte[] bArr, int i2, int i3, LZWCompressor lZWCompressor, int i4, int i5, int i6, int i7, int i8, int i9) throws IOException {
        int i10 = i2 + (i4 * i3);
        int i11 = i3 * i5;
        int i12 = i4;
        while (true) {
            int i13 = i12;
            if (i13 < i7) {
                if (i8 % i9 == 0) {
                    if (abortRequested()) {
                        processWriteAborted();
                        return;
                    }
                    processImageProgress((i8 * 100.0f) / i7);
                }
                lZWCompressor.compress(bArr, i10, i6);
                i8++;
                i10 += i11;
                i12 = i13 + i5;
            } else {
                return;
            }
        }
    }

    private void writeRasterData(RenderedImage renderedImage, Rectangle rectangle, Dimension dimension, ImageWriteParam imageWriteParam, boolean z2) throws IOException {
        int sourceXSubsampling;
        int sourceYSubsampling;
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        int i6 = dimension.width;
        int i7 = dimension.height;
        if (imageWriteParam == null) {
            sourceXSubsampling = 1;
            sourceYSubsampling = 1;
        } else {
            sourceXSubsampling = imageWriteParam.getSourceXSubsampling();
            sourceYSubsampling = imageWriteParam.getSourceYSubsampling();
        }
        SampleModel sampleModel = renderedImage.getSampleModel();
        int i8 = sampleModel.getSampleSize()[0];
        if (i8 == 1) {
            i8++;
        }
        this.stream.write(i8);
        LZWCompressor lZWCompressor = new LZWCompressor(this.stream, i8, false);
        boolean z3 = sourceXSubsampling == 1 && sourceYSubsampling == 1 && renderedImage.getNumXTiles() == 1 && renderedImage.getNumYTiles() == 1 && (sampleModel instanceof ComponentSampleModel) && (renderedImage.getTile(0, 0) instanceof ByteComponentRaster) && (renderedImage.getTile(0, 0).getDataBuffer() instanceof DataBufferByte);
        int iMax = Math.max(i7 / 20, 1);
        processImageStarted(this.imageIndex);
        if (z2) {
            if (z3) {
                ByteComponentRaster byteComponentRaster = (ByteComponentRaster) renderedImage.getTile(0, 0);
                byte[] data = ((DataBufferByte) byteComponentRaster.getDataBuffer()).getData();
                ComponentSampleModel componentSampleModel = (ComponentSampleModel) byteComponentRaster.getSampleModel();
                int offset = componentSampleModel.getOffset(i2, i3, 0) + byteComponentRaster.getDataOffset(0);
                int scanlineStride = componentSampleModel.getScanlineStride();
                writeRowsOpt(data, offset, scanlineStride, lZWCompressor, 0, 8, i6, i7, 0, iMax);
                if (abortRequested()) {
                    return;
                }
                int i9 = 0 + (i7 / 8);
                writeRowsOpt(data, offset, scanlineStride, lZWCompressor, 4, 8, i6, i7, i9, iMax);
                if (abortRequested()) {
                    return;
                }
                int i10 = i9 + ((i7 - 4) / 8);
                writeRowsOpt(data, offset, scanlineStride, lZWCompressor, 2, 4, i6, i7, i10, iMax);
                if (abortRequested()) {
                    return;
                } else {
                    writeRowsOpt(data, offset, scanlineStride, lZWCompressor, 1, 2, i6, i7, i10 + ((i7 - 2) / 4), iMax);
                }
            } else {
                writeRows(renderedImage, lZWCompressor, i2, sourceXSubsampling, i3, 8 * sourceYSubsampling, i4, 0, 8, i6, i7, 0, iMax);
                if (abortRequested()) {
                    return;
                }
                int i11 = 0 + (i7 / 8);
                writeRows(renderedImage, lZWCompressor, i2, sourceXSubsampling, i3 + (4 * sourceYSubsampling), 8 * sourceYSubsampling, i4, 4, 8, i6, i7, i11, iMax);
                if (abortRequested()) {
                    return;
                }
                int i12 = i11 + ((i7 - 4) / 8);
                writeRows(renderedImage, lZWCompressor, i2, sourceXSubsampling, i3 + (2 * sourceYSubsampling), 4 * sourceYSubsampling, i4, 2, 4, i6, i7, i12, iMax);
                if (abortRequested()) {
                    return;
                } else {
                    writeRows(renderedImage, lZWCompressor, i2, sourceXSubsampling, i3 + sourceYSubsampling, 2 * sourceYSubsampling, i4, 1, 2, i6, i7, i12 + ((i7 - 2) / 4), iMax);
                }
            }
        } else if (z3) {
            Raster tile = renderedImage.getTile(0, 0);
            byte[] data2 = ((DataBufferByte) tile.getDataBuffer()).getData();
            ComponentSampleModel componentSampleModel2 = (ComponentSampleModel) tile.getSampleModel();
            writeRowsOpt(data2, componentSampleModel2.getOffset(i2, i3, 0), componentSampleModel2.getScanlineStride(), lZWCompressor, 0, 1, i6, i7, 0, iMax);
        } else {
            writeRows(renderedImage, lZWCompressor, i2, sourceXSubsampling, i3, sourceYSubsampling, i4, 0, 1, i6, i7, 0, iMax);
        }
        if (abortRequested()) {
            return;
        }
        processImageProgress(100.0f);
        lZWCompressor.flush();
        this.stream.write(0);
        processImageComplete();
    }

    private void writeHeader(String str, int i2, int i3, int i4, int i5, int i6, boolean z2, int i7, byte[] bArr) throws IOException {
        try {
            this.stream.writeBytes("GIF" + str);
            this.stream.writeShort((short) i2);
            this.stream.writeShort((short) i3);
            int i8 = (bArr != null ? 128 : 0) | (((i4 - 1) & 7) << 4);
            if (z2) {
                i8 |= 8;
            }
            this.stream.write(i8 | (i7 - 1));
            this.stream.write(i6);
            this.stream.write(i5);
            if (bArr != null) {
                this.stream.write(bArr);
            }
        } catch (IOException e2) {
            throw new IIOException("I/O error writing header!", e2);
        }
    }

    private void writeHeader(IIOMetadata iIOMetadata, int i2) throws IOException {
        GIFWritableStreamMetadata gIFWritableStreamMetadata;
        if (iIOMetadata instanceof GIFWritableStreamMetadata) {
            gIFWritableStreamMetadata = (GIFWritableStreamMetadata) iIOMetadata;
        } else {
            gIFWritableStreamMetadata = new GIFWritableStreamMetadata();
            gIFWritableStreamMetadata.setFromTree(STREAM_METADATA_NAME, iIOMetadata.getAsTree(STREAM_METADATA_NAME));
        }
        writeHeader(gIFWritableStreamMetadata.version, gIFWritableStreamMetadata.logicalScreenWidth, gIFWritableStreamMetadata.logicalScreenHeight, gIFWritableStreamMetadata.colorResolution, gIFWritableStreamMetadata.pixelAspectRatio, gIFWritableStreamMetadata.backgroundColorIndex, gIFWritableStreamMetadata.sortFlag, i2, gIFWritableStreamMetadata.globalColorTable);
    }

    private void writeGraphicControlExtension(int i2, boolean z2, boolean z3, int i3, int i4) throws IOException {
        try {
            this.stream.write(33);
            this.stream.write(TelnetCommand.GA);
            this.stream.write(4);
            int i5 = (i2 & 3) << 2;
            if (z2) {
                i5 |= 2;
            }
            if (z3) {
                i5 |= 1;
            }
            this.stream.write(i5);
            this.stream.writeShort((short) i3);
            this.stream.write(i4);
            this.stream.write(0);
        } catch (IOException e2) {
            throw new IIOException("I/O error writing Graphic Control Extension!", e2);
        }
    }

    private void writeGraphicControlExtension(GIFWritableImageMetadata gIFWritableImageMetadata) throws IOException {
        writeGraphicControlExtension(gIFWritableImageMetadata.disposalMethod, gIFWritableImageMetadata.userInputFlag, gIFWritableImageMetadata.transparentColorFlag, gIFWritableImageMetadata.delayTime, gIFWritableImageMetadata.transparentColorIndex);
    }

    private void writeBlocks(byte[] bArr) throws IOException {
        if (bArr != null && bArr.length > 0) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < bArr.length) {
                    int iMin = Math.min(bArr.length - i3, 255);
                    this.stream.write(iMin);
                    this.stream.write(bArr, i3, iMin);
                    i2 = i3 + iMin;
                } else {
                    return;
                }
            }
        }
    }

    private void writePlainTextExtension(GIFWritableImageMetadata gIFWritableImageMetadata) throws IOException {
        if (gIFWritableImageMetadata.hasPlainTextExtension) {
            try {
                this.stream.write(33);
                this.stream.write(1);
                this.stream.write(12);
                this.stream.writeShort(gIFWritableImageMetadata.textGridLeft);
                this.stream.writeShort(gIFWritableImageMetadata.textGridTop);
                this.stream.writeShort(gIFWritableImageMetadata.textGridWidth);
                this.stream.writeShort(gIFWritableImageMetadata.textGridHeight);
                this.stream.write(gIFWritableImageMetadata.characterCellWidth);
                this.stream.write(gIFWritableImageMetadata.characterCellHeight);
                this.stream.write(gIFWritableImageMetadata.textForegroundColor);
                this.stream.write(gIFWritableImageMetadata.textBackgroundColor);
                writeBlocks(gIFWritableImageMetadata.text);
                this.stream.write(0);
            } catch (IOException e2) {
                throw new IIOException("I/O error writing Plain Text Extension!", e2);
            }
        }
    }

    private void writeApplicationExtension(GIFWritableImageMetadata gIFWritableImageMetadata) throws IOException {
        if (gIFWritableImageMetadata.applicationIDs != null) {
            Iterator it = gIFWritableImageMetadata.applicationIDs.iterator();
            Iterator it2 = gIFWritableImageMetadata.authenticationCodes.iterator();
            Iterator it3 = gIFWritableImageMetadata.applicationData.iterator();
            while (it.hasNext()) {
                try {
                    this.stream.write(33);
                    this.stream.write(255);
                    this.stream.write(11);
                    this.stream.write((byte[]) it.next(), 0, 8);
                    this.stream.write((byte[]) it2.next(), 0, 3);
                    writeBlocks((byte[]) it3.next());
                    this.stream.write(0);
                } catch (IOException e2) {
                    throw new IIOException("I/O error writing Application Extension!", e2);
                }
            }
        }
    }

    private void writeCommentExtension(GIFWritableImageMetadata gIFWritableImageMetadata) throws IOException {
        if (gIFWritableImageMetadata.comments != null) {
            try {
                Iterator it = gIFWritableImageMetadata.comments.iterator();
                while (it.hasNext()) {
                    this.stream.write(33);
                    this.stream.write(254);
                    writeBlocks((byte[]) it.next());
                    this.stream.write(0);
                }
            } catch (IOException e2) {
                throw new IIOException("I/O error writing Comment Extension!", e2);
            }
        }
    }

    private void writeImageDescriptor(int i2, int i3, int i4, int i5, boolean z2, boolean z3, int i6, byte[] bArr) throws IOException {
        try {
            this.stream.write(44);
            this.stream.writeShort((short) i2);
            this.stream.writeShort((short) i3);
            this.stream.writeShort((short) i4);
            this.stream.writeShort((short) i5);
            int i7 = bArr != null ? 128 : 0;
            if (z2) {
                i7 |= 64;
            }
            if (z3) {
                i7 |= 8;
            }
            this.stream.write(i7 | (i6 - 1));
            if (bArr != null) {
                this.stream.write(bArr);
            }
        } catch (IOException e2) {
            throw new IIOException("I/O error writing Image Descriptor!", e2);
        }
    }

    private void writeImageDescriptor(GIFWritableImageMetadata gIFWritableImageMetadata, int i2) throws IOException {
        writeImageDescriptor(gIFWritableImageMetadata.imageLeftPosition, gIFWritableImageMetadata.imageTopPosition, gIFWritableImageMetadata.imageWidth, gIFWritableImageMetadata.imageHeight, gIFWritableImageMetadata.interlaceFlag, gIFWritableImageMetadata.sortFlag, i2, gIFWritableImageMetadata.localColorTable);
    }

    private void writeTrailer() throws IOException {
        this.stream.write(59);
    }
}
