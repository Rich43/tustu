package com.sun.imageio.plugins.png;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;

/* loaded from: rt.jar:com/sun/imageio/plugins/png/PNGImageReader.class */
public class PNGImageReader extends ImageReader {
    static final int IHDR_TYPE = 1229472850;
    static final int PLTE_TYPE = 1347179589;
    static final int IDAT_TYPE = 1229209940;
    static final int IEND_TYPE = 1229278788;
    static final int bKGD_TYPE = 1649100612;
    static final int cHRM_TYPE = 1665684045;
    static final int gAMA_TYPE = 1732332865;
    static final int hIST_TYPE = 1749635924;
    static final int iCCP_TYPE = 1766015824;
    static final int iTXt_TYPE = 1767135348;
    static final int pHYs_TYPE = 1883789683;
    static final int sBIT_TYPE = 1933723988;
    static final int sPLT_TYPE = 1934642260;
    static final int sRGB_TYPE = 1934772034;
    static final int tEXt_TYPE = 1950701684;
    static final int tIME_TYPE = 1950960965;
    static final int tRNS_TYPE = 1951551059;
    static final int zTXt_TYPE = 2052348020;
    static final int PNG_COLOR_GRAY = 0;
    static final int PNG_COLOR_RGB = 2;
    static final int PNG_COLOR_PALETTE = 3;
    static final int PNG_COLOR_GRAY_ALPHA = 4;
    static final int PNG_COLOR_RGB_ALPHA = 6;
    static final int PNG_FILTER_NONE = 0;
    static final int PNG_FILTER_SUB = 1;
    static final int PNG_FILTER_UP = 2;
    static final int PNG_FILTER_AVERAGE = 3;
    static final int PNG_FILTER_PAETH = 4;
    private static final boolean debug = true;
    ImageInputStream stream;
    boolean gotHeader;
    boolean gotMetadata;
    ImageReadParam lastParam;
    long imageStartPosition;
    Rectangle sourceRegion;
    int sourceXSubsampling;
    int sourceYSubsampling;
    int sourceMinProgressivePass;
    int sourceMaxProgressivePass;
    int[] sourceBands;
    int[] destinationBands;
    Point destinationOffset;
    PNGMetadata metadata;
    DataInputStream pixelStream;
    BufferedImage theImage;
    int pixelsDone;
    int totalPixels;
    static final int[] inputBandsForColorType = {1, -1, 3, 1, 2, -1, 4};
    static final int[] adam7XOffset = {0, 4, 0, 2, 0, 1, 0};
    static final int[] adam7YOffset = {0, 0, 4, 0, 2, 0, 1};
    static final int[] adam7XSubsampling = {8, 8, 4, 4, 2, 2, 1, 1};
    static final int[] adam7YSubsampling = {8, 8, 8, 4, 4, 2, 2, 1};
    private static final int[][] bandOffsets = {0, new int[]{0}, new int[]{0, 1}, new int[]{0, 1, 2}, new int[]{0, 1, 2, 3}};

    public PNGImageReader(ImageReaderSpi imageReaderSpi) {
        super(imageReaderSpi);
        this.stream = null;
        this.gotHeader = false;
        this.gotMetadata = false;
        this.lastParam = null;
        this.imageStartPosition = -1L;
        this.sourceRegion = null;
        this.sourceXSubsampling = -1;
        this.sourceYSubsampling = -1;
        this.sourceMinProgressivePass = 0;
        this.sourceMaxProgressivePass = 6;
        this.sourceBands = null;
        this.destinationBands = null;
        this.destinationOffset = new Point(0, 0);
        this.metadata = new PNGMetadata();
        this.pixelStream = null;
        this.theImage = null;
        this.pixelsDone = 0;
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object obj, boolean z2, boolean z3) {
        super.setInput(obj, z2, z3);
        this.stream = (ImageInputStream) obj;
        resetStreamSettings();
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0046, code lost:
    
        return new java.lang.String(r0.toByteArray(), r6);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String readNullTerminatedString(java.lang.String r6, int r7) throws java.io.IOException {
        /*
            r5 = this;
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r1 = r0
            r1.<init>()
            r8 = r0
            r0 = 0
            r10 = r0
        Lb:
            r0 = r7
            r1 = r10
            int r10 = r10 + 1
            if (r0 <= r1) goto L3a
            r0 = r5
            javax.imageio.stream.ImageInputStream r0 = r0.stream
            int r0 = r0.read()
            r1 = r0
            r9 = r1
            if (r0 == 0) goto L3a
            r0 = r9
            r1 = -1
            if (r0 != r1) goto L31
            java.io.EOFException r0 = new java.io.EOFException
            r1 = r0
            r1.<init>()
            throw r0
        L31:
            r0 = r8
            r1 = r9
            r0.write(r1)
            goto Lb
        L3a:
            java.lang.String r0 = new java.lang.String
            r1 = r0
            r2 = r8
            byte[] r2 = r2.toByteArray()
            r3 = r6
            r1.<init>(r2, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.imageio.plugins.png.PNGImageReader.readNullTerminatedString(java.lang.String, int):java.lang.String");
    }

    private void readHeader() throws IIOException {
        if (this.gotHeader) {
            return;
        }
        if (this.stream == null) {
            throw new IllegalStateException("Input source not set!");
        }
        try {
            byte[] bArr = new byte[8];
            this.stream.readFully(bArr);
            if (bArr[0] != -119 || bArr[1] != 80 || bArr[2] != 78 || bArr[3] != 71 || bArr[4] != 13 || bArr[5] != 10 || bArr[6] != 26 || bArr[7] != 10) {
                throw new IIOException("Bad PNG signature!");
            }
            if (this.stream.readInt() != 13) {
                throw new IIOException("Bad length for IHDR chunk!");
            }
            if (this.stream.readInt() != IHDR_TYPE) {
                throw new IIOException("Bad type for IHDR chunk!");
            }
            this.metadata = new PNGMetadata();
            int i2 = this.stream.readInt();
            int i3 = this.stream.readInt();
            this.stream.readFully(bArr, 0, 5);
            int i4 = bArr[0] & 255;
            int i5 = bArr[1] & 255;
            int i6 = bArr[2] & 255;
            int i7 = bArr[3] & 255;
            int i8 = bArr[4] & 255;
            this.stream.skipBytes(4);
            this.stream.flushBefore(this.stream.getStreamPosition());
            if (i2 == 0) {
                throw new IIOException("Image width == 0!");
            }
            if (i3 == 0) {
                throw new IIOException("Image height == 0!");
            }
            if (i4 != 1 && i4 != 2 && i4 != 4 && i4 != 8 && i4 != 16) {
                throw new IIOException("Bit depth must be 1, 2, 4, 8, or 16!");
            }
            if (i5 != 0 && i5 != 2 && i5 != 3 && i5 != 4 && i5 != 6) {
                throw new IIOException("Color type must be 0, 2, 3, 4, or 6!");
            }
            if (i5 == 3 && i4 == 16) {
                throw new IIOException("Bad color type/bit depth combination!");
            }
            if ((i5 == 2 || i5 == 6 || i5 == 4) && i4 != 8 && i4 != 16) {
                throw new IIOException("Bad color type/bit depth combination!");
            }
            if (i6 != 0) {
                throw new IIOException("Unknown compression method (not 0)!");
            }
            if (i7 != 0) {
                throw new IIOException("Unknown filter method (not 0)!");
            }
            if (i8 != 0 && i8 != 1) {
                throw new IIOException("Unknown interlace method (not 0 or 1)!");
            }
            this.metadata.IHDR_present = true;
            this.metadata.IHDR_width = i2;
            this.metadata.IHDR_height = i3;
            this.metadata.IHDR_bitDepth = i4;
            this.metadata.IHDR_colorType = i5;
            this.metadata.IHDR_compressionMethod = i6;
            this.metadata.IHDR_filterMethod = i7;
            this.metadata.IHDR_interlaceMethod = i8;
            this.gotHeader = true;
        } catch (IOException e2) {
            throw new IIOException("I/O error reading PNG header!", e2);
        }
    }

    private void parse_PLTE_chunk(int i2) throws IOException {
        int i3;
        if (this.metadata.PLTE_present) {
            processWarningOccurred("A PNG image may not contain more than one PLTE chunk.\nThe chunk wil be ignored.");
            return;
        }
        if (this.metadata.IHDR_colorType == 0 || this.metadata.IHDR_colorType == 4) {
            processWarningOccurred("A PNG gray or gray alpha image cannot have a PLTE chunk.\nThe chunk wil be ignored.");
            return;
        }
        byte[] bArr = new byte[i2];
        this.stream.readFully(bArr);
        int iMin = i2 / 3;
        if (this.metadata.IHDR_colorType == 3) {
            int i4 = 1 << this.metadata.IHDR_bitDepth;
            if (iMin > i4) {
                processWarningOccurred("PLTE chunk contains too many entries for bit depth, ignoring extras.");
                iMin = i4;
            }
            iMin = Math.min(iMin, i4);
        }
        if (iMin > 16) {
            i3 = 256;
        } else if (iMin > 4) {
            i3 = 16;
        } else if (iMin > 2) {
            i3 = 4;
        } else {
            i3 = 2;
        }
        this.metadata.PLTE_present = true;
        this.metadata.PLTE_red = new byte[i3];
        this.metadata.PLTE_green = new byte[i3];
        this.metadata.PLTE_blue = new byte[i3];
        int i5 = 0;
        for (int i6 = 0; i6 < iMin; i6++) {
            int i7 = i5;
            int i8 = i5 + 1;
            this.metadata.PLTE_red[i6] = bArr[i7];
            int i9 = i8 + 1;
            this.metadata.PLTE_green[i6] = bArr[i8];
            i5 = i9 + 1;
            this.metadata.PLTE_blue[i6] = bArr[i9];
        }
    }

    private void parse_bKGD_chunk() throws IOException {
        if (this.metadata.IHDR_colorType == 3) {
            this.metadata.bKGD_colorType = 3;
            this.metadata.bKGD_index = this.stream.readUnsignedByte();
        } else if (this.metadata.IHDR_colorType == 0 || this.metadata.IHDR_colorType == 4) {
            this.metadata.bKGD_colorType = 0;
            this.metadata.bKGD_gray = this.stream.readUnsignedShort();
        } else {
            this.metadata.bKGD_colorType = 2;
            this.metadata.bKGD_red = this.stream.readUnsignedShort();
            this.metadata.bKGD_green = this.stream.readUnsignedShort();
            this.metadata.bKGD_blue = this.stream.readUnsignedShort();
        }
        this.metadata.bKGD_present = true;
    }

    private void parse_cHRM_chunk() throws IOException {
        this.metadata.cHRM_whitePointX = this.stream.readInt();
        this.metadata.cHRM_whitePointY = this.stream.readInt();
        this.metadata.cHRM_redX = this.stream.readInt();
        this.metadata.cHRM_redY = this.stream.readInt();
        this.metadata.cHRM_greenX = this.stream.readInt();
        this.metadata.cHRM_greenY = this.stream.readInt();
        this.metadata.cHRM_blueX = this.stream.readInt();
        this.metadata.cHRM_blueY = this.stream.readInt();
        this.metadata.cHRM_present = true;
    }

    private void parse_gAMA_chunk() throws IOException {
        this.metadata.gAMA_gamma = this.stream.readInt();
        this.metadata.gAMA_present = true;
    }

    private void parse_hIST_chunk(int i2) throws IOException {
        if (!this.metadata.PLTE_present) {
            throw new IIOException("hIST chunk without prior PLTE chunk!");
        }
        this.metadata.hIST_histogram = new char[i2 / 2];
        this.stream.readFully(this.metadata.hIST_histogram, 0, this.metadata.hIST_histogram.length);
        this.metadata.hIST_present = true;
    }

    private void parse_iCCP_chunk(int i2) throws IOException {
        String nullTerminatedString = readNullTerminatedString(FTP.DEFAULT_CONTROL_ENCODING, 80);
        this.metadata.iCCP_profileName = nullTerminatedString;
        this.metadata.iCCP_compressionMethod = this.stream.readUnsignedByte();
        byte[] bArr = new byte[(i2 - nullTerminatedString.length()) - 2];
        this.stream.readFully(bArr);
        this.metadata.iCCP_compressedProfile = bArr;
        this.metadata.iCCP_present = true;
    }

    private void parse_iTXt_chunk(int i2) throws IOException {
        String str;
        long streamPosition = this.stream.getStreamPosition();
        this.metadata.iTXt_keyword.add(readNullTerminatedString(FTP.DEFAULT_CONTROL_ENCODING, 80));
        int unsignedByte = this.stream.readUnsignedByte();
        this.metadata.iTXt_compressionFlag.add(Boolean.valueOf(unsignedByte == 1));
        this.metadata.iTXt_compressionMethod.add(Integer.valueOf(this.stream.readUnsignedByte()));
        this.metadata.iTXt_languageTag.add(readNullTerminatedString(InternalZipConstants.CHARSET_UTF8, 80));
        this.metadata.iTXt_translatedKeyword.add(readNullTerminatedString(InternalZipConstants.CHARSET_UTF8, (int) ((streamPosition + i2) - this.stream.getStreamPosition())));
        byte[] bArr = new byte[(int) ((streamPosition + i2) - this.stream.getStreamPosition())];
        this.stream.readFully(bArr);
        if (unsignedByte == 1) {
            str = new String(inflate(bArr), InternalZipConstants.CHARSET_UTF8);
        } else {
            str = new String(bArr, InternalZipConstants.CHARSET_UTF8);
        }
        this.metadata.iTXt_text.add(str);
    }

    private void parse_pHYs_chunk() throws IOException {
        this.metadata.pHYs_pixelsPerUnitXAxis = this.stream.readInt();
        this.metadata.pHYs_pixelsPerUnitYAxis = this.stream.readInt();
        this.metadata.pHYs_unitSpecifier = this.stream.readUnsignedByte();
        this.metadata.pHYs_present = true;
    }

    private void parse_sBIT_chunk() throws IOException {
        int i2 = this.metadata.IHDR_colorType;
        if (i2 == 0 || i2 == 4) {
            this.metadata.sBIT_grayBits = this.stream.readUnsignedByte();
        } else if (i2 == 2 || i2 == 3 || i2 == 6) {
            this.metadata.sBIT_redBits = this.stream.readUnsignedByte();
            this.metadata.sBIT_greenBits = this.stream.readUnsignedByte();
            this.metadata.sBIT_blueBits = this.stream.readUnsignedByte();
        }
        if (i2 == 4 || i2 == 6) {
            this.metadata.sBIT_alphaBits = this.stream.readUnsignedByte();
        }
        this.metadata.sBIT_colorType = i2;
        this.metadata.sBIT_present = true;
    }

    private void parse_sPLT_chunk(int i2) throws IOException {
        this.metadata.sPLT_paletteName = readNullTerminatedString(FTP.DEFAULT_CONTROL_ENCODING, 80);
        int length = i2 - (this.metadata.sPLT_paletteName.length() + 1);
        int unsignedByte = this.stream.readUnsignedByte();
        this.metadata.sPLT_sampleDepth = unsignedByte;
        int i3 = length / ((4 * (unsignedByte / 8)) + 2);
        this.metadata.sPLT_red = new int[i3];
        this.metadata.sPLT_green = new int[i3];
        this.metadata.sPLT_blue = new int[i3];
        this.metadata.sPLT_alpha = new int[i3];
        this.metadata.sPLT_frequency = new int[i3];
        if (unsignedByte == 8) {
            for (int i4 = 0; i4 < i3; i4++) {
                this.metadata.sPLT_red[i4] = this.stream.readUnsignedByte();
                this.metadata.sPLT_green[i4] = this.stream.readUnsignedByte();
                this.metadata.sPLT_blue[i4] = this.stream.readUnsignedByte();
                this.metadata.sPLT_alpha[i4] = this.stream.readUnsignedByte();
                this.metadata.sPLT_frequency[i4] = this.stream.readUnsignedShort();
            }
        } else if (unsignedByte == 16) {
            for (int i5 = 0; i5 < i3; i5++) {
                this.metadata.sPLT_red[i5] = this.stream.readUnsignedShort();
                this.metadata.sPLT_green[i5] = this.stream.readUnsignedShort();
                this.metadata.sPLT_blue[i5] = this.stream.readUnsignedShort();
                this.metadata.sPLT_alpha[i5] = this.stream.readUnsignedShort();
                this.metadata.sPLT_frequency[i5] = this.stream.readUnsignedShort();
            }
        } else {
            throw new IIOException("sPLT sample depth not 8 or 16!");
        }
        this.metadata.sPLT_present = true;
    }

    private void parse_sRGB_chunk() throws IOException {
        this.metadata.sRGB_renderingIntent = this.stream.readUnsignedByte();
        this.metadata.sRGB_present = true;
    }

    private void parse_tEXt_chunk(int i2) throws IOException {
        String nullTerminatedString = readNullTerminatedString(FTP.DEFAULT_CONTROL_ENCODING, 80);
        this.metadata.tEXt_keyword.add(nullTerminatedString);
        byte[] bArr = new byte[(i2 - nullTerminatedString.length()) - 1];
        this.stream.readFully(bArr);
        this.metadata.tEXt_text.add(new String(bArr, FTP.DEFAULT_CONTROL_ENCODING));
    }

    private void parse_tIME_chunk() throws IOException {
        this.metadata.tIME_year = this.stream.readUnsignedShort();
        this.metadata.tIME_month = this.stream.readUnsignedByte();
        this.metadata.tIME_day = this.stream.readUnsignedByte();
        this.metadata.tIME_hour = this.stream.readUnsignedByte();
        this.metadata.tIME_minute = this.stream.readUnsignedByte();
        this.metadata.tIME_second = this.stream.readUnsignedByte();
        this.metadata.tIME_present = true;
    }

    private void parse_tRNS_chunk(int i2) throws IOException {
        int i3 = this.metadata.IHDR_colorType;
        if (i3 == 3) {
            if (!this.metadata.PLTE_present) {
                processWarningOccurred("tRNS chunk without prior PLTE chunk, ignoring it.");
                return;
            }
            int length = this.metadata.PLTE_red.length;
            int i4 = i2;
            if (i4 > length) {
                processWarningOccurred("tRNS chunk has more entries than prior PLTE chunk, ignoring extras.");
                i4 = length;
            }
            this.metadata.tRNS_alpha = new byte[i4];
            this.metadata.tRNS_colorType = 3;
            this.stream.read(this.metadata.tRNS_alpha, 0, i4);
            this.stream.skipBytes(i2 - i4);
        } else if (i3 == 0) {
            if (i2 != 2) {
                processWarningOccurred("tRNS chunk for gray image must have length 2, ignoring chunk.");
                this.stream.skipBytes(i2);
                return;
            } else {
                this.metadata.tRNS_gray = this.stream.readUnsignedShort();
                this.metadata.tRNS_colorType = 0;
            }
        } else if (i3 == 2) {
            if (i2 != 6) {
                processWarningOccurred("tRNS chunk for RGB image must have length 6, ignoring chunk.");
                this.stream.skipBytes(i2);
                return;
            }
            this.metadata.tRNS_red = this.stream.readUnsignedShort();
            this.metadata.tRNS_green = this.stream.readUnsignedShort();
            this.metadata.tRNS_blue = this.stream.readUnsignedShort();
            this.metadata.tRNS_colorType = 2;
        } else {
            processWarningOccurred("Gray+Alpha and RGBS images may not have a tRNS chunk, ignoring it.");
            return;
        }
        this.metadata.tRNS_present = true;
    }

    private static byte[] inflate(byte[] bArr) throws IOException {
        InflaterInputStream inflaterInputStream = new InflaterInputStream(new ByteArrayInputStream(bArr));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            try {
                int i2 = inflaterInputStream.read();
                if (i2 != -1) {
                    byteArrayOutputStream.write(i2);
                } else {
                    return byteArrayOutputStream.toByteArray();
                }
            } finally {
                inflaterInputStream.close();
            }
        }
    }

    private void parse_zTXt_chunk(int i2) throws IOException {
        String nullTerminatedString = readNullTerminatedString(FTP.DEFAULT_CONTROL_ENCODING, 80);
        this.metadata.zTXt_keyword.add(nullTerminatedString);
        this.metadata.zTXt_compressionMethod.add(new Integer(this.stream.readUnsignedByte()));
        byte[] bArr = new byte[(i2 - nullTerminatedString.length()) - 2];
        this.stream.readFully(bArr);
        this.metadata.zTXt_text.add(new String(inflate(bArr), FTP.DEFAULT_CONTROL_ENCODING));
    }

    private void readMetadata() throws IIOException {
        if (this.gotMetadata) {
            return;
        }
        readHeader();
        int i2 = this.metadata.IHDR_colorType;
        if (!this.ignoreMetadata || i2 == 3) {
            while (true) {
                try {
                    int i3 = this.stream.readInt();
                    int i4 = this.stream.readInt();
                    if (i3 < 0) {
                        throw new IIOException("Invalid chunk lenght " + i3);
                    }
                    try {
                        this.stream.mark();
                        this.stream.seek(this.stream.getStreamPosition() + i3);
                        int i5 = this.stream.readInt();
                        this.stream.reset();
                        switch (i4) {
                            case IDAT_TYPE /* 1229209940 */:
                                this.stream.skipBytes(-8);
                                this.imageStartPosition = this.stream.getStreamPosition();
                                this.gotMetadata = true;
                                return;
                            case PLTE_TYPE /* 1347179589 */:
                                parse_PLTE_chunk(i3);
                                break;
                            case bKGD_TYPE /* 1649100612 */:
                                parse_bKGD_chunk();
                                break;
                            case cHRM_TYPE /* 1665684045 */:
                                parse_cHRM_chunk();
                                break;
                            case gAMA_TYPE /* 1732332865 */:
                                parse_gAMA_chunk();
                                break;
                            case hIST_TYPE /* 1749635924 */:
                                parse_hIST_chunk(i3);
                                break;
                            case iCCP_TYPE /* 1766015824 */:
                                parse_iCCP_chunk(i3);
                                break;
                            case iTXt_TYPE /* 1767135348 */:
                                if (this.ignoreMetadata) {
                                    this.stream.skipBytes(i3);
                                    break;
                                } else {
                                    parse_iTXt_chunk(i3);
                                    break;
                                }
                            case pHYs_TYPE /* 1883789683 */:
                                parse_pHYs_chunk();
                                break;
                            case sBIT_TYPE /* 1933723988 */:
                                parse_sBIT_chunk();
                                break;
                            case sPLT_TYPE /* 1934642260 */:
                                parse_sPLT_chunk(i3);
                                break;
                            case sRGB_TYPE /* 1934772034 */:
                                parse_sRGB_chunk();
                                break;
                            case tEXt_TYPE /* 1950701684 */:
                                parse_tEXt_chunk(i3);
                                break;
                            case tIME_TYPE /* 1950960965 */:
                                parse_tIME_chunk();
                                break;
                            case tRNS_TYPE /* 1951551059 */:
                                parse_tRNS_chunk(i3);
                                break;
                            case zTXt_TYPE /* 2052348020 */:
                                if (this.ignoreMetadata) {
                                    this.stream.skipBytes(i3);
                                    break;
                                } else {
                                    parse_zTXt_chunk(i3);
                                    break;
                                }
                            default:
                                byte[] bArr = new byte[i3];
                                this.stream.readFully(bArr);
                                StringBuilder sb = new StringBuilder(4);
                                sb.append((char) (i4 >>> 24));
                                sb.append((char) ((i4 >> 16) & 255));
                                sb.append((char) ((i4 >> 8) & 255));
                                sb.append((char) (i4 & 255));
                                if ((i4 >>> 28) == 0) {
                                    processWarningOccurred("Encountered unknown chunk with critical bit set!");
                                }
                                this.metadata.unknownChunkType.add(sb.toString());
                                this.metadata.unknownChunkData.add(bArr);
                                break;
                        }
                        if (i5 != this.stream.readInt()) {
                            throw new IIOException("Failed to read a chunk of type " + i4);
                        }
                        this.stream.flushBefore(this.stream.getStreamPosition());
                    } catch (IOException e2) {
                        throw new IIOException("Invalid chunk length " + i3);
                    }
                } catch (IOException e3) {
                    throw new IIOException("Error reading PNG metadata", e3);
                }
            }
        } else {
            while (true) {
                try {
                    int i6 = this.stream.readInt();
                    if (this.stream.readInt() == IDAT_TYPE) {
                        this.stream.skipBytes(-8);
                        this.imageStartPosition = this.stream.getStreamPosition();
                        this.gotMetadata = true;
                        return;
                    }
                    this.stream.skipBytes(i6 + 4);
                } catch (IOException e4) {
                    throw new IIOException("Error skipping PNG metadata", e4);
                }
            }
        }
    }

    private static void decodeSubFilter(byte[] bArr, int i2, int i3, int i4) {
        for (int i5 = i4; i5 < i3; i5++) {
            bArr[i5 + i2] = (byte) ((bArr[i5 + i2] & 255) + (bArr[(i5 + i2) - i4] & 255));
        }
    }

    private static void decodeUpFilter(byte[] bArr, int i2, byte[] bArr2, int i3, int i4) {
        for (int i5 = 0; i5 < i4; i5++) {
            bArr[i5 + i2] = (byte) ((bArr[i5 + i2] & 255) + (bArr2[i5 + i3] & 255));
        }
    }

    private static void decodeAverageFilter(byte[] bArr, int i2, byte[] bArr2, int i3, int i4, int i5) {
        for (int i6 = 0; i6 < i5; i6++) {
            bArr[i6 + i2] = (byte) ((bArr[i6 + i2] & 255) + ((bArr2[i6 + i3] & 255) / 2));
        }
        for (int i7 = i5; i7 < i4; i7++) {
            bArr[i7 + i2] = (byte) ((bArr[i7 + i2] & 255) + (((bArr[(i7 + i2) - i5] & 255) + (bArr2[i7 + i3] & 255)) / 2));
        }
    }

    private static int paethPredictor(int i2, int i3, int i4) {
        int i5 = (i2 + i3) - i4;
        int iAbs = Math.abs(i5 - i2);
        int iAbs2 = Math.abs(i5 - i3);
        int iAbs3 = Math.abs(i5 - i4);
        if (iAbs <= iAbs2 && iAbs <= iAbs3) {
            return i2;
        }
        if (iAbs2 <= iAbs3) {
            return i3;
        }
        return i4;
    }

    private static void decodePaethFilter(byte[] bArr, int i2, byte[] bArr2, int i3, int i4, int i5) {
        for (int i6 = 0; i6 < i5; i6++) {
            bArr[i6 + i2] = (byte) ((bArr[i6 + i2] & 255) + (bArr2[i6 + i3] & 255));
        }
        for (int i7 = i5; i7 < i4; i7++) {
            bArr[i7 + i2] = (byte) ((bArr[i7 + i2] & 255) + paethPredictor(bArr[(i7 + i2) - i5] & 255, bArr2[i7 + i3] & 255, bArr2[(i7 + i3) - i5] & 255));
        }
    }

    private WritableRaster createRaster(int i2, int i3, int i4, int i5, int i6) {
        WritableRaster writableRasterCreateInterleavedRaster;
        Point point = new Point(0, 0);
        if (i6 < 8 && i4 == 1) {
            writableRasterCreateInterleavedRaster = Raster.createPackedRaster(new DataBufferByte(i3 * i5), i2, i3, i6, point);
        } else if (i6 <= 8) {
            writableRasterCreateInterleavedRaster = Raster.createInterleavedRaster(new DataBufferByte(i3 * i5), i2, i3, i5, i4, bandOffsets[i4], point);
        } else {
            writableRasterCreateInterleavedRaster = Raster.createInterleavedRaster(new DataBufferUShort(i3 * i5), i2, i3, i5, i4, bandOffsets[i4], point);
        }
        return writableRasterCreateInterleavedRaster;
    }

    private void skipPass(int i2, int i3) throws IOException {
        if (i2 == 0 || i3 == 0) {
            return;
        }
        int i4 = (((inputBandsForColorType[this.metadata.IHDR_colorType] * i2) * this.metadata.IHDR_bitDepth) + 7) / 8;
        for (int i5 = 0; i5 < i3; i5++) {
            this.pixelStream.skipBytes(1 + i4);
            if (abortRequested()) {
                return;
            }
        }
    }

    private void updateImageProgress(int i2) {
        this.pixelsDone += i2;
        processImageProgress((100.0f * this.pixelsDone) / this.totalPixels);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:128:0x04a2 A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03bf  */
    /* JADX WARN: Type inference failed for: r0v167, types: [int[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void decodePass(int r16, int r17, int r18, int r19, int r20, int r21, int r22) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1201
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.imageio.plugins.png.PNGImageReader.decodePass(int, int, int, int, int, int, int):void");
    }

    private void decodeImage() throws IOException {
        int i2 = this.metadata.IHDR_width;
        int i3 = this.metadata.IHDR_height;
        this.pixelsDone = 0;
        this.totalPixels = i2 * i3;
        clearAbortRequest();
        if (this.metadata.IHDR_interlaceMethod == 0) {
            decodePass(0, 0, 0, 1, 1, i2, i3);
            return;
        }
        for (int i4 = 0; i4 <= this.sourceMaxProgressivePass; i4++) {
            int i5 = adam7XOffset[i4];
            int i6 = adam7YOffset[i4];
            int i7 = adam7XSubsampling[i4];
            int i8 = adam7YSubsampling[i4];
            int i9 = adam7XSubsampling[i4 + 1] - 1;
            int i10 = adam7YSubsampling[i4 + 1] - 1;
            if (i4 >= this.sourceMinProgressivePass) {
                decodePass(i4, i5, i6, i7, i8, (i2 + i9) / i7, (i3 + i10) / i8);
            } else {
                skipPass((i2 + i9) / i7, (i3 + i10) / i8);
            }
            if (abortRequested()) {
                return;
            }
        }
    }

    private void readImage(ImageReadParam imageReadParam) throws IIOException {
        readMetadata();
        int i2 = this.metadata.IHDR_width;
        int i3 = this.metadata.IHDR_height;
        if (i2 * i3 > 2147483645) {
            throw new IIOException("Can not read image of the size " + i2 + " by " + i3);
        }
        this.sourceXSubsampling = 1;
        this.sourceYSubsampling = 1;
        this.sourceMinProgressivePass = 0;
        this.sourceMaxProgressivePass = 6;
        this.sourceBands = null;
        this.destinationBands = null;
        this.destinationOffset = new Point(0, 0);
        if (imageReadParam != null) {
            this.sourceXSubsampling = imageReadParam.getSourceXSubsampling();
            this.sourceYSubsampling = imageReadParam.getSourceYSubsampling();
            this.sourceMinProgressivePass = Math.max(imageReadParam.getSourceMinProgressivePass(), 0);
            this.sourceMaxProgressivePass = Math.min(imageReadParam.getSourceMaxProgressivePass(), 6);
            this.sourceBands = imageReadParam.getSourceBands();
            this.destinationBands = imageReadParam.getDestinationBands();
            this.destinationOffset = imageReadParam.getDestinationOffset();
        }
        Inflater inflater = null;
        try {
            try {
                this.stream.seek(this.imageStartPosition);
                SequenceInputStream sequenceInputStream = new SequenceInputStream(new PNGImageDataEnumeration(this.stream));
                Inflater inflater2 = new Inflater();
                this.pixelStream = new DataInputStream(new BufferedInputStream(new InflaterInputStream(sequenceInputStream, inflater2)));
                this.theImage = getDestination(imageReadParam, getImageTypes(0), i2, i3);
                Rectangle rectangle = new Rectangle(0, 0, 0, 0);
                this.sourceRegion = new Rectangle(0, 0, 0, 0);
                computeRegions(imageReadParam, i2, i3, this.theImage, this.sourceRegion, rectangle);
                this.destinationOffset.setLocation(rectangle.getLocation());
                checkReadParamBandSettings(imageReadParam, inputBandsForColorType[this.metadata.IHDR_colorType], this.theImage.getSampleModel().getNumBands());
                processImageStarted(0);
                decodeImage();
                if (abortRequested()) {
                    processReadAborted();
                } else {
                    processImageComplete();
                }
                if (inflater2 != null) {
                    inflater2.end();
                }
            } catch (IOException e2) {
                throw new IIOException("Error reading PNG image data", e2);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                inflater.end();
            }
            throw th;
        }
    }

    @Override // javax.imageio.ImageReader
    public int getNumImages(boolean z2) throws IIOException {
        if (this.stream == null) {
            throw new IllegalStateException("No input source set!");
        }
        if (this.seekForwardOnly && z2) {
            throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
        }
        return 1;
    }

    @Override // javax.imageio.ImageReader
    public int getWidth(int i2) throws IIOException {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException("imageIndex != 0!");
        }
        readHeader();
        return this.metadata.IHDR_width;
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int i2) throws IIOException {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException("imageIndex != 0!");
        }
        readHeader();
        return this.metadata.IHDR_height;
    }

    @Override // javax.imageio.ImageReader
    public Iterator<ImageTypeSpecifier> getImageTypes(int i2) throws IIOException {
        int i3;
        if (i2 != 0) {
            throw new IndexOutOfBoundsException("imageIndex != 0!");
        }
        readHeader();
        ArrayList arrayList = new ArrayList(1);
        int i4 = this.metadata.IHDR_bitDepth;
        int i5 = this.metadata.IHDR_colorType;
        if (i4 <= 8) {
            i3 = 0;
        } else {
            i3 = 1;
        }
        switch (i5) {
            case 0:
                arrayList.add(ImageTypeSpecifier.createGrayscale(i4, i3, false));
                break;
            case 2:
                if (i4 == 8) {
                    arrayList.add(ImageTypeSpecifier.createFromBufferedImageType(5));
                    arrayList.add(ImageTypeSpecifier.createFromBufferedImageType(1));
                    arrayList.add(ImageTypeSpecifier.createFromBufferedImageType(4));
                }
                arrayList.add(ImageTypeSpecifier.createInterleaved(ColorSpace.getInstance(1000), new int[]{0, 1, 2}, i3, false, false));
                break;
            case 3:
                readMetadata();
                int i6 = 1 << i4;
                byte[] bArrCopyOf = this.metadata.PLTE_red;
                byte[] bArrCopyOf2 = this.metadata.PLTE_green;
                byte[] bArrCopyOf3 = this.metadata.PLTE_blue;
                if (this.metadata.PLTE_red.length < i6) {
                    bArrCopyOf = Arrays.copyOf(this.metadata.PLTE_red, i6);
                    Arrays.fill(bArrCopyOf, this.metadata.PLTE_red.length, i6, this.metadata.PLTE_red[this.metadata.PLTE_red.length - 1]);
                    bArrCopyOf2 = Arrays.copyOf(this.metadata.PLTE_green, i6);
                    Arrays.fill(bArrCopyOf2, this.metadata.PLTE_green.length, i6, this.metadata.PLTE_green[this.metadata.PLTE_green.length - 1]);
                    bArrCopyOf3 = Arrays.copyOf(this.metadata.PLTE_blue, i6);
                    Arrays.fill(bArrCopyOf3, this.metadata.PLTE_blue.length, i6, this.metadata.PLTE_blue[this.metadata.PLTE_blue.length - 1]);
                }
                byte[] bArrCopyOf4 = null;
                if (this.metadata.tRNS_present && this.metadata.tRNS_alpha != null) {
                    if (this.metadata.tRNS_alpha.length == bArrCopyOf.length) {
                        bArrCopyOf4 = this.metadata.tRNS_alpha;
                    } else {
                        bArrCopyOf4 = Arrays.copyOf(this.metadata.tRNS_alpha, bArrCopyOf.length);
                        Arrays.fill(bArrCopyOf4, this.metadata.tRNS_alpha.length, bArrCopyOf.length, (byte) -1);
                    }
                }
                arrayList.add(ImageTypeSpecifier.createIndexed(bArrCopyOf, bArrCopyOf2, bArrCopyOf3, bArrCopyOf4, i4, 0));
                break;
            case 4:
                arrayList.add(ImageTypeSpecifier.createInterleaved(ColorSpace.getInstance(1003), new int[]{0, 1}, i3, true, false));
                break;
            case 6:
                if (i4 == 8) {
                    arrayList.add(ImageTypeSpecifier.createFromBufferedImageType(6));
                    arrayList.add(ImageTypeSpecifier.createFromBufferedImageType(2));
                }
                arrayList.add(ImageTypeSpecifier.createInterleaved(ColorSpace.getInstance(1000), new int[]{0, 1, 2, 3}, i3, true, false));
                break;
        }
        return arrayList.iterator();
    }

    @Override // javax.imageio.ImageReader
    public ImageTypeSpecifier getRawImageType(int i2) throws IOException {
        ImageTypeSpecifier next;
        Iterator<ImageTypeSpecifier> imageTypes = getImageTypes(i2);
        do {
            next = imageTypes.next();
        } while (imageTypes.hasNext());
        return next;
    }

    @Override // javax.imageio.ImageReader
    public ImageReadParam getDefaultReadParam() {
        return new ImageReadParam();
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IIOException {
        return null;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int i2) throws IIOException {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException("imageIndex != 0!");
        }
        readMetadata();
        return this.metadata;
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int i2, ImageReadParam imageReadParam) throws IIOException {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException("imageIndex != 0!");
        }
        try {
            readImage(imageReadParam);
            return this.theImage;
        } catch (IOException | IllegalArgumentException | IllegalStateException e2) {
            throw e2;
        } catch (Throwable th) {
            throw new IIOException("Caught exception during read: ", th);
        }
    }

    @Override // javax.imageio.ImageReader
    public void reset() {
        super.reset();
        resetStreamSettings();
    }

    private void resetStreamSettings() {
        this.gotHeader = false;
        this.gotMetadata = false;
        this.metadata = null;
        this.pixelStream = null;
    }
}
