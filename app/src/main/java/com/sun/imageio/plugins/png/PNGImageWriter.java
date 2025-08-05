package com.sun.imageio.plugins.png;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.DeflaterOutputStream;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageWriter.class */
public class PNGImageWriter extends ImageWriter {
    ImageOutputStream stream;
    PNGMetadata metadata;
    int sourceXOffset;
    int sourceYOffset;
    int sourceWidth;
    int sourceHeight;
    int[] sourceBands;
    int periodX;
    int periodY;
    int numBands;
    int bpp;
    RowFilter rowFilter;
    byte[] prevRow;
    byte[] currRow;
    byte[][] filteredRows;
    int[] sampleSize;
    int scalingBitDepth;
    byte[][] scale;
    byte[] scale0;
    byte[][] scaleh;
    byte[][] scalel;
    int totalPixels;
    int pixelsDone;
    private static int[] allowedProgressivePasses = {1, 7};

    public PNGImageWriter(ImageWriterSpi imageWriterSpi) {
        super(imageWriterSpi);
        this.stream = null;
        this.metadata = null;
        this.sourceXOffset = 0;
        this.sourceYOffset = 0;
        this.sourceWidth = 0;
        this.sourceHeight = 0;
        this.sourceBands = null;
        this.periodX = 1;
        this.periodY = 1;
        this.rowFilter = new RowFilter();
        this.prevRow = null;
        this.currRow = null;
        this.filteredRows = (byte[][]) null;
        this.sampleSize = null;
        this.scalingBitDepth = -1;
        this.scale = (byte[][]) null;
        this.scale0 = null;
        this.scaleh = (byte[][]) null;
        this.scalel = (byte[][]) null;
    }

    @Override // javax.imageio.ImageWriter
    public void setOutput(Object obj) {
        super.setOutput(obj);
        if (obj != null) {
            if (!(obj instanceof ImageOutputStream)) {
                throw new IllegalArgumentException("output not an ImageOutputStream!");
            }
            this.stream = (ImageOutputStream) obj;
            return;
        }
        this.stream = null;
    }

    @Override // javax.imageio.ImageWriter
    public ImageWriteParam getDefaultWriteParam() {
        return new PNGImageWriteParam(getLocale());
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        PNGMetadata pNGMetadata = new PNGMetadata();
        pNGMetadata.initialize(imageTypeSpecifier, imageTypeSpecifier.getSampleModel().getNumBands());
        return pNGMetadata;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam) {
        return null;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        if (iIOMetadata instanceof PNGMetadata) {
            return (PNGMetadata) ((PNGMetadata) iIOMetadata).clone();
        }
        return new PNGMetadata(iIOMetadata);
    }

    private void write_magic() throws IOException {
        this.stream.write(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10});
    }

    private void write_IHDR() throws IOException {
        ChunkStream chunkStream = new ChunkStream(1229472850, this.stream);
        chunkStream.writeInt(this.metadata.IHDR_width);
        chunkStream.writeInt(this.metadata.IHDR_height);
        chunkStream.writeByte(this.metadata.IHDR_bitDepth);
        chunkStream.writeByte(this.metadata.IHDR_colorType);
        if (this.metadata.IHDR_compressionMethod != 0) {
            throw new IIOException("Only compression method 0 is defined in PNG 1.1");
        }
        chunkStream.writeByte(this.metadata.IHDR_compressionMethod);
        if (this.metadata.IHDR_filterMethod != 0) {
            throw new IIOException("Only filter method 0 is defined in PNG 1.1");
        }
        chunkStream.writeByte(this.metadata.IHDR_filterMethod);
        if (this.metadata.IHDR_interlaceMethod < 0 || this.metadata.IHDR_interlaceMethod > 1) {
            throw new IIOException("Only interlace methods 0 (node) and 1 (adam7) are defined in PNG 1.1");
        }
        chunkStream.writeByte(this.metadata.IHDR_interlaceMethod);
        chunkStream.finish();
    }

    private void write_cHRM() throws IOException {
        if (this.metadata.cHRM_present) {
            ChunkStream chunkStream = new ChunkStream(1665684045, this.stream);
            chunkStream.writeInt(this.metadata.cHRM_whitePointX);
            chunkStream.writeInt(this.metadata.cHRM_whitePointY);
            chunkStream.writeInt(this.metadata.cHRM_redX);
            chunkStream.writeInt(this.metadata.cHRM_redY);
            chunkStream.writeInt(this.metadata.cHRM_greenX);
            chunkStream.writeInt(this.metadata.cHRM_greenY);
            chunkStream.writeInt(this.metadata.cHRM_blueX);
            chunkStream.writeInt(this.metadata.cHRM_blueY);
            chunkStream.finish();
        }
    }

    private void write_gAMA() throws IOException {
        if (this.metadata.gAMA_present) {
            ChunkStream chunkStream = new ChunkStream(1732332865, this.stream);
            chunkStream.writeInt(this.metadata.gAMA_gamma);
            chunkStream.finish();
        }
    }

    private void write_iCCP() throws IOException {
        if (this.metadata.iCCP_present) {
            ChunkStream chunkStream = new ChunkStream(1766015824, this.stream);
            chunkStream.writeBytes(this.metadata.iCCP_profileName);
            chunkStream.writeByte(0);
            chunkStream.writeByte(this.metadata.iCCP_compressionMethod);
            chunkStream.write(this.metadata.iCCP_compressedProfile);
            chunkStream.finish();
        }
    }

    private void write_sBIT() throws IOException {
        if (this.metadata.sBIT_present) {
            ChunkStream chunkStream = new ChunkStream(1933723988, this.stream);
            int i2 = this.metadata.IHDR_colorType;
            if (this.metadata.sBIT_colorType != i2) {
                processWarningOccurred(0, "sBIT metadata has wrong color type.\nThe chunk will not be written.");
                return;
            }
            if (i2 == 0 || i2 == 4) {
                chunkStream.writeByte(this.metadata.sBIT_grayBits);
            } else if (i2 == 2 || i2 == 3 || i2 == 6) {
                chunkStream.writeByte(this.metadata.sBIT_redBits);
                chunkStream.writeByte(this.metadata.sBIT_greenBits);
                chunkStream.writeByte(this.metadata.sBIT_blueBits);
            }
            if (i2 == 4 || i2 == 6) {
                chunkStream.writeByte(this.metadata.sBIT_alphaBits);
            }
            chunkStream.finish();
        }
    }

    private void write_sRGB() throws IOException {
        if (this.metadata.sRGB_present) {
            ChunkStream chunkStream = new ChunkStream(1934772034, this.stream);
            chunkStream.writeByte(this.metadata.sRGB_renderingIntent);
            chunkStream.finish();
        }
    }

    private void write_PLTE() throws IOException {
        if (this.metadata.PLTE_present) {
            if (this.metadata.IHDR_colorType == 0 || this.metadata.IHDR_colorType == 4) {
                processWarningOccurred(0, "A PLTE chunk may not appear in a gray or gray alpha image.\nThe chunk will not be written");
                return;
            }
            ChunkStream chunkStream = new ChunkStream(1347179589, this.stream);
            int length = this.metadata.PLTE_red.length;
            byte[] bArr = new byte[length * 3];
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                int i4 = i2;
                int i5 = i2 + 1;
                bArr[i4] = this.metadata.PLTE_red[i3];
                int i6 = i5 + 1;
                bArr[i5] = this.metadata.PLTE_green[i3];
                i2 = i6 + 1;
                bArr[i6] = this.metadata.PLTE_blue[i3];
            }
            chunkStream.write(bArr);
            chunkStream.finish();
        }
    }

    private void write_hIST() throws IOException {
        if (this.metadata.hIST_present) {
            ChunkStream chunkStream = new ChunkStream(1749635924, this.stream);
            if (!this.metadata.PLTE_present) {
                throw new IIOException("hIST chunk without PLTE chunk!");
            }
            chunkStream.writeChars(this.metadata.hIST_histogram, 0, this.metadata.hIST_histogram.length);
            chunkStream.finish();
        }
    }

    private void write_tRNS() throws IOException {
        if (this.metadata.tRNS_present) {
            ChunkStream chunkStream = new ChunkStream(1951551059, this.stream);
            int i2 = this.metadata.IHDR_colorType;
            int i3 = this.metadata.tRNS_colorType;
            int i4 = this.metadata.tRNS_red;
            int i5 = this.metadata.tRNS_green;
            int i6 = this.metadata.tRNS_blue;
            if (i2 == 2 && i3 == 0) {
                i3 = i2;
                int i7 = this.metadata.tRNS_gray;
                i6 = i7;
                i5 = i7;
                i4 = i7;
            }
            if (i3 != i2) {
                processWarningOccurred(0, "tRNS metadata has incompatible color type.\nThe chunk will not be written.");
                return;
            }
            if (i2 == 3) {
                if (!this.metadata.PLTE_present) {
                    throw new IIOException("tRNS chunk without PLTE chunk!");
                }
                chunkStream.write(this.metadata.tRNS_alpha);
            } else if (i2 == 0) {
                chunkStream.writeShort(this.metadata.tRNS_gray);
            } else if (i2 == 2) {
                chunkStream.writeShort(i4);
                chunkStream.writeShort(i5);
                chunkStream.writeShort(i6);
            } else {
                throw new IIOException("tRNS chunk for color type 4 or 6!");
            }
            chunkStream.finish();
        }
    }

    private void write_bKGD() throws IOException {
        if (this.metadata.bKGD_present) {
            ChunkStream chunkStream = new ChunkStream(1649100612, this.stream);
            int i2 = this.metadata.IHDR_colorType & 3;
            int i3 = this.metadata.bKGD_colorType;
            int i4 = this.metadata.bKGD_red;
            int i5 = this.metadata.bKGD_red;
            int i6 = this.metadata.bKGD_red;
            if (i2 == 2 && i3 == 0) {
                i3 = i2;
                int i7 = this.metadata.bKGD_gray;
                i6 = i7;
                i5 = i7;
                i4 = i7;
            }
            if (i3 != i2) {
                processWarningOccurred(0, "bKGD metadata has incompatible color type.\nThe chunk will not be written.");
                return;
            }
            if (i2 == 3) {
                chunkStream.writeByte(this.metadata.bKGD_index);
            } else if (i2 == 0 || i2 == 4) {
                chunkStream.writeShort(this.metadata.bKGD_gray);
            } else {
                chunkStream.writeShort(i4);
                chunkStream.writeShort(i5);
                chunkStream.writeShort(i6);
            }
            chunkStream.finish();
        }
    }

    private void write_pHYs() throws IOException {
        if (this.metadata.pHYs_present) {
            ChunkStream chunkStream = new ChunkStream(1883789683, this.stream);
            chunkStream.writeInt(this.metadata.pHYs_pixelsPerUnitXAxis);
            chunkStream.writeInt(this.metadata.pHYs_pixelsPerUnitYAxis);
            chunkStream.writeByte(this.metadata.pHYs_unitSpecifier);
            chunkStream.finish();
        }
    }

    private void write_sPLT() throws IOException {
        if (this.metadata.sPLT_present) {
            ChunkStream chunkStream = new ChunkStream(1934642260, this.stream);
            chunkStream.writeBytes(this.metadata.sPLT_paletteName);
            chunkStream.writeByte(0);
            chunkStream.writeByte(this.metadata.sPLT_sampleDepth);
            int length = this.metadata.sPLT_red.length;
            if (this.metadata.sPLT_sampleDepth == 8) {
                for (int i2 = 0; i2 < length; i2++) {
                    chunkStream.writeByte(this.metadata.sPLT_red[i2]);
                    chunkStream.writeByte(this.metadata.sPLT_green[i2]);
                    chunkStream.writeByte(this.metadata.sPLT_blue[i2]);
                    chunkStream.writeByte(this.metadata.sPLT_alpha[i2]);
                    chunkStream.writeShort(this.metadata.sPLT_frequency[i2]);
                }
            } else {
                for (int i3 = 0; i3 < length; i3++) {
                    chunkStream.writeShort(this.metadata.sPLT_red[i3]);
                    chunkStream.writeShort(this.metadata.sPLT_green[i3]);
                    chunkStream.writeShort(this.metadata.sPLT_blue[i3]);
                    chunkStream.writeShort(this.metadata.sPLT_alpha[i3]);
                    chunkStream.writeShort(this.metadata.sPLT_frequency[i3]);
                }
            }
            chunkStream.finish();
        }
    }

    private void write_tIME() throws IOException {
        if (this.metadata.tIME_present) {
            ChunkStream chunkStream = new ChunkStream(1950960965, this.stream);
            chunkStream.writeShort(this.metadata.tIME_year);
            chunkStream.writeByte(this.metadata.tIME_month);
            chunkStream.writeByte(this.metadata.tIME_day);
            chunkStream.writeByte(this.metadata.tIME_hour);
            chunkStream.writeByte(this.metadata.tIME_minute);
            chunkStream.writeByte(this.metadata.tIME_second);
            chunkStream.finish();
        }
    }

    private void write_tEXt() throws IOException {
        Iterator<String> it = this.metadata.tEXt_keyword.iterator();
        Iterator<String> it2 = this.metadata.tEXt_text.iterator();
        while (it.hasNext()) {
            ChunkStream chunkStream = new ChunkStream(1950701684, this.stream);
            chunkStream.writeBytes(it.next());
            chunkStream.writeByte(0);
            chunkStream.writeBytes(it2.next());
            chunkStream.finish();
        }
    }

    private byte[] deflate(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        deflaterOutputStream.write(bArr);
        deflaterOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void write_iTXt() throws IOException {
        Iterator<String> it = this.metadata.iTXt_keyword.iterator();
        Iterator<Boolean> it2 = this.metadata.iTXt_compressionFlag.iterator();
        Iterator<Integer> it3 = this.metadata.iTXt_compressionMethod.iterator();
        Iterator<String> it4 = this.metadata.iTXt_languageTag.iterator();
        Iterator<String> it5 = this.metadata.iTXt_translatedKeyword.iterator();
        Iterator<String> it6 = this.metadata.iTXt_text.iterator();
        while (it.hasNext()) {
            ChunkStream chunkStream = new ChunkStream(1767135348, this.stream);
            chunkStream.writeBytes(it.next());
            chunkStream.writeByte(0);
            Boolean next = it2.next();
            chunkStream.writeByte(next.booleanValue() ? 1 : 0);
            chunkStream.writeByte(it3.next().intValue());
            chunkStream.writeBytes(it4.next());
            chunkStream.writeByte(0);
            chunkStream.write(it5.next().getBytes(InternalZipConstants.CHARSET_UTF8));
            chunkStream.writeByte(0);
            String next2 = it6.next();
            if (next.booleanValue()) {
                chunkStream.write(deflate(next2.getBytes(InternalZipConstants.CHARSET_UTF8)));
            } else {
                chunkStream.write(next2.getBytes(InternalZipConstants.CHARSET_UTF8));
            }
            chunkStream.finish();
        }
    }

    private void write_zTXt() throws IOException {
        Iterator<String> it = this.metadata.zTXt_keyword.iterator();
        Iterator<Integer> it2 = this.metadata.zTXt_compressionMethod.iterator();
        Iterator<String> it3 = this.metadata.zTXt_text.iterator();
        while (it.hasNext()) {
            ChunkStream chunkStream = new ChunkStream(2052348020, this.stream);
            chunkStream.writeBytes(it.next());
            chunkStream.writeByte(0);
            chunkStream.writeByte(it2.next().intValue());
            chunkStream.write(deflate(it3.next().getBytes(FTP.DEFAULT_CONTROL_ENCODING)));
            chunkStream.finish();
        }
    }

    private void writeUnknownChunks() throws IOException {
        Iterator<String> it = this.metadata.unknownChunkType.iterator();
        Iterator<byte[]> it2 = this.metadata.unknownChunkData.iterator();
        while (it.hasNext() && it2.hasNext()) {
            ChunkStream chunkStream = new ChunkStream(chunkType(it.next()), this.stream);
            chunkStream.write(it2.next());
            chunkStream.finish();
        }
    }

    private static int chunkType(String str) {
        return (str.charAt(0) << 24) | (str.charAt(1) << 16) | (str.charAt(2) << '\b') | str.charAt(3);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:75:0x041c A[LOOP:0: B:19:0x0128->B:75:0x041c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x041b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void encodePass(javax.imageio.stream.ImageOutputStream r10, java.awt.image.RenderedImage r11, int r12, int r13, int r14, int r15) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1063
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.imageio.plugins.png.PNGImageWriter.encodePass(javax.imageio.stream.ImageOutputStream, java.awt.image.RenderedImage, int, int, int, int):void");
    }

    private void write_IDAT(RenderedImage renderedImage) throws IOException {
        IDATOutputStream iDATOutputStream = new IDATOutputStream(this.stream, 32768);
        try {
            if (this.metadata.IHDR_interlaceMethod == 1) {
                for (int i2 = 0; i2 < 7; i2++) {
                    encodePass(iDATOutputStream, renderedImage, PNGImageReader.adam7XOffset[i2], PNGImageReader.adam7YOffset[i2], PNGImageReader.adam7XSubsampling[i2], PNGImageReader.adam7YSubsampling[i2]);
                    if (abortRequested()) {
                        break;
                    }
                }
            } else {
                encodePass(iDATOutputStream, renderedImage, 0, 0, 1, 1);
            }
        } finally {
            iDATOutputStream.finish();
        }
    }

    private void writeIEND() throws IOException {
        new ChunkStream(1229278788, this.stream).finish();
    }

    private boolean equals(int[] iArr, int[] iArr2) {
        if (iArr == null || iArr2 == null || iArr.length != iArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v12, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v34, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v9, types: [byte[], byte[][]] */
    private void initializeScaleTables(int[] iArr) {
        int i2 = this.metadata.IHDR_bitDepth;
        if (i2 == this.scalingBitDepth && equals(iArr, this.sampleSize)) {
            return;
        }
        this.sampleSize = iArr;
        this.scalingBitDepth = i2;
        int i3 = (1 << i2) - 1;
        if (i2 <= 8) {
            this.scale = new byte[this.numBands];
            for (int i4 = 0; i4 < this.numBands; i4++) {
                int i5 = (1 << iArr[i4]) - 1;
                int i6 = i5 / 2;
                this.scale[i4] = new byte[i5 + 1];
                for (int i7 = 0; i7 <= i5; i7++) {
                    this.scale[i4][i7] = (byte) (((i7 * i3) + i6) / i5);
                }
            }
            this.scale0 = this.scale[0];
            byte[][] bArr = (byte[][]) null;
            this.scalel = bArr;
            this.scaleh = bArr;
            return;
        }
        this.scaleh = new byte[this.numBands];
        this.scalel = new byte[this.numBands];
        for (int i8 = 0; i8 < this.numBands; i8++) {
            int i9 = (1 << iArr[i8]) - 1;
            int i10 = i9 / 2;
            this.scaleh[i8] = new byte[i9 + 1];
            this.scalel[i8] = new byte[i9 + 1];
            for (int i11 = 0; i11 <= i9; i11++) {
                int i12 = ((i11 * i3) + i10) / i9;
                this.scaleh[i8][i11] = (byte) (i12 >> 8);
                this.scalel[i8][i11] = (byte) (i12 & 255);
            }
        }
        this.scale = (byte[][]) null;
        this.scale0 = null;
    }

    @Override // javax.imageio.ImageWriter
    public void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IIOException {
        if (this.stream == null) {
            throw new IllegalStateException("output == null!");
        }
        if (iIOImage == null) {
            throw new IllegalArgumentException("image == null!");
        }
        if (iIOImage.hasRaster()) {
            throw new UnsupportedOperationException("image has a Raster!");
        }
        RenderedImage renderedImage = iIOImage.getRenderedImage();
        SampleModel sampleModel = renderedImage.getSampleModel();
        this.numBands = sampleModel.getNumBands();
        this.sourceXOffset = renderedImage.getMinX();
        this.sourceYOffset = renderedImage.getMinY();
        this.sourceWidth = renderedImage.getWidth();
        this.sourceHeight = renderedImage.getHeight();
        this.sourceBands = null;
        this.periodX = 1;
        this.periodY = 1;
        if (imageWriteParam != null) {
            Rectangle sourceRegion = imageWriteParam.getSourceRegion();
            if (sourceRegion != null) {
                Rectangle rectangleIntersection = sourceRegion.intersection(new Rectangle(renderedImage.getMinX(), renderedImage.getMinY(), renderedImage.getWidth(), renderedImage.getHeight()));
                this.sourceXOffset = rectangleIntersection.f12372x;
                this.sourceYOffset = rectangleIntersection.f12373y;
                this.sourceWidth = rectangleIntersection.width;
                this.sourceHeight = rectangleIntersection.height;
            }
            int subsamplingXOffset = imageWriteParam.getSubsamplingXOffset();
            int subsamplingYOffset = imageWriteParam.getSubsamplingYOffset();
            this.sourceXOffset += subsamplingXOffset;
            this.sourceYOffset += subsamplingYOffset;
            this.sourceWidth -= subsamplingXOffset;
            this.sourceHeight -= subsamplingYOffset;
            this.periodX = imageWriteParam.getSourceXSubsampling();
            this.periodY = imageWriteParam.getSourceYSubsampling();
            int[] sourceBands = imageWriteParam.getSourceBands();
            if (sourceBands != null) {
                this.sourceBands = sourceBands;
                this.numBands = this.sourceBands.length;
            }
        }
        int i2 = ((this.sourceWidth + this.periodX) - 1) / this.periodX;
        int i3 = ((this.sourceHeight + this.periodY) - 1) / this.periodY;
        if (i2 <= 0 || i3 <= 0) {
            throw new IllegalArgumentException("Empty source region!");
        }
        this.totalPixels = i2 * i3;
        this.pixelsDone = 0;
        IIOMetadata metadata = iIOImage.getMetadata();
        if (metadata != null) {
            this.metadata = (PNGMetadata) convertImageMetadata(metadata, ImageTypeSpecifier.createFromRenderedImage(renderedImage), null);
        } else {
            this.metadata = new PNGMetadata();
        }
        if (imageWriteParam != null) {
            switch (imageWriteParam.getProgressiveMode()) {
                case 0:
                    this.metadata.IHDR_interlaceMethod = 0;
                    break;
                case 1:
                    this.metadata.IHDR_interlaceMethod = 1;
                    break;
            }
        }
        this.metadata.initialize(new ImageTypeSpecifier(renderedImage), this.numBands);
        this.metadata.IHDR_width = i2;
        this.metadata.IHDR_height = i3;
        this.bpp = this.numBands * (this.metadata.IHDR_bitDepth == 16 ? 2 : 1);
        initializeScaleTables(sampleModel.getSampleSize());
        clearAbortRequest();
        processImageStarted(0);
        try {
            write_magic();
            write_IHDR();
            write_cHRM();
            write_gAMA();
            write_iCCP();
            write_sBIT();
            write_sRGB();
            write_PLTE();
            write_hIST();
            write_tRNS();
            write_bKGD();
            write_pHYs();
            write_sPLT();
            write_tIME();
            write_tEXt();
            write_iTXt();
            write_zTXt();
            writeUnknownChunks();
            write_IDAT(renderedImage);
            if (abortRequested()) {
                processWriteAborted();
            } else {
                writeIEND();
                processImageComplete();
            }
        } catch (IOException e2) {
            throw new IIOException("I/O error writing PNG file!", e2);
        }
    }
}
