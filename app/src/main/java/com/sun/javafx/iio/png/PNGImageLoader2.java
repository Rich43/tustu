package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/png/PNGImageLoader2.class */
public final class PNGImageLoader2 extends ImageLoaderImpl {
    static final int IHDR_TYPE = 1229472850;
    static final int PLTE_TYPE = 1347179589;
    static final int IDAT_TYPE = 1229209940;
    static final int IEND_TYPE = 1229278788;
    static final int tRNS_TYPE = 1951551059;
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
    private final DataInputStream stream;
    private int width;
    private int height;
    private int bitDepth;
    private int colorType;
    private boolean isInterlaced;
    private boolean tRNS_present;
    private boolean tRNS_GRAY_RGB;
    private int trnsR;
    private int trnsG;
    private int trnsB;
    private byte[][] palette;
    static final byte[] FILE_SIG = {-119, 80, 78, 71, 13, 10, 26, 10};
    static final int[] numBandsPerColorType = {1, -1, 3, 1, 2, -1, 4};
    private static final int[] starting_y = {0, 0, 4, 0, 2, 0, 1, 0};
    private static final int[] starting_x = {0, 4, 0, 2, 0, 1, 0, 0};
    private static final int[] increment_y = {8, 8, 8, 4, 4, 2, 2, 1};
    private static final int[] increment_x = {8, 8, 4, 4, 2, 2, 1, 1};

    public PNGImageLoader2(InputStream input) throws IOException {
        super(PNGDescriptor.getInstance());
        this.tRNS_present = false;
        this.tRNS_GRAY_RGB = false;
        this.stream = new DataInputStream(input);
        byte[] signature = readBytes(new byte[8]);
        if (!Arrays.equals(FILE_SIG, signature)) {
            throw new IOException("Bad PNG signature!");
        }
        readHeader();
    }

    private void readHeader() throws IOException {
        int[] hdrData = readChunk();
        if (hdrData[1] != IHDR_TYPE && hdrData[0] != 13) {
            throw new IOException("Bad PNG header!");
        }
        this.width = this.stream.readInt();
        this.height = this.stream.readInt();
        if (this.width <= 0) {
            throw new IOException("Bad PNG image width, must be > 0!");
        }
        if (this.height <= 0) {
            throw new IOException("Bad PNG image height, must be > 0!");
        }
        if (this.width >= Integer.MAX_VALUE / this.height) {
            throw new IOException("Bad PNG image size!");
        }
        this.bitDepth = this.stream.readByte();
        if (this.bitDepth != 1 && this.bitDepth != 2 && this.bitDepth != 4 && this.bitDepth != 8 && this.bitDepth != 16) {
            throw new IOException("Bad PNG bit depth");
        }
        this.colorType = this.stream.readByte();
        if (this.colorType > 6 || this.colorType == 1 || this.colorType == 5) {
            throw new IOException("Bad PNG color type");
        }
        if ((this.colorType != 3 && this.colorType != 0 && this.bitDepth < 8) || (this.colorType == 3 && this.bitDepth == 16)) {
            throw new IOException("Bad color type/bit depth combination!");
        }
        byte compressionMethod = this.stream.readByte();
        if (compressionMethod != 0) {
            throw new IOException("Bad PNG comression!");
        }
        byte filterMethod = this.stream.readByte();
        if (filterMethod != 0) {
            throw new IOException("Bad PNG filter method!");
        }
        byte interlaceMethod = this.stream.readByte();
        if (interlaceMethod != 0 && interlaceMethod != 1) {
            throw new IOException("Unknown interlace method (not 0 or 1)!");
        }
        this.stream.readInt();
        this.isInterlaced = interlaceMethod == 1;
    }

    private int[] readChunk() throws IOException {
        return new int[]{this.stream.readInt(), this.stream.readInt()};
    }

    private byte[] readBytes(byte[] data) throws IOException {
        return readBytes(data, 0, data.length);
    }

    private byte[] readBytes(byte[] data, int offs, int size) throws IOException {
        this.stream.readFully(data, offs, size);
        return data;
    }

    private void skip(int n2) throws IOException {
        if (n2 != this.stream.skipBytes(n2)) {
            throw new EOFException();
        }
    }

    private void readPaletteChunk(int chunkLength) throws IOException {
        int numEntries = chunkLength / 3;
        int paletteEntries = 1 << this.bitDepth;
        if (numEntries > paletteEntries) {
            emitWarning("PLTE chunk contains too many entries for bit depth, ignoring extras.");
            numEntries = paletteEntries;
        }
        this.palette = new byte[3][paletteEntries];
        byte[] paletteData = readBytes(new byte[chunkLength]);
        int idx = 0;
        for (int i2 = 0; i2 != numEntries; i2++) {
            for (int k2 = 0; k2 != 3; k2++) {
                int i3 = idx;
                idx++;
                this.palette[k2][i2] = paletteData[i3];
            }
        }
    }

    private void parsePaletteChunk(int chunkLength) throws IOException {
        if (this.palette != null) {
            emitWarning("A PNG image may not contain more than one PLTE chunk.\nThe chunk wil be ignored.");
            skip(chunkLength);
            return;
        }
        switch (this.colorType) {
            case 0:
            case 4:
                emitWarning("A PNG gray or gray alpha image cannot have a PLTE chunk.\nThe chunk wil be ignored.");
                break;
            case 3:
                readPaletteChunk(chunkLength);
                return;
        }
        skip(chunkLength);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][], java.lang.Object] */
    private boolean readPaletteTransparency(int chunkLength) throws IOException {
        if (this.palette == null) {
            emitWarning("tRNS chunk without prior PLTE chunk, ignoring it.");
            skip(chunkLength);
            return false;
        }
        ?? r0 = new byte[4];
        System.arraycopy(this.palette, 0, r0, 0, 3);
        int paletteLength = this.palette[0].length;
        r0[3] = new byte[paletteLength];
        int nRead = chunkLength < paletteLength ? chunkLength : paletteLength;
        readBytes(r0[3], 0, nRead);
        for (int i2 = nRead; i2 < paletteLength; i2++) {
            r0[3][i2] = -1;
        }
        if (nRead < chunkLength) {
            skip(chunkLength - nRead);
        }
        this.palette = r0;
        return true;
    }

    private boolean readGrayTransparency(int chunkLength) throws IOException {
        if (chunkLength == 2) {
            this.trnsG = this.stream.readShort();
            return true;
        }
        return false;
    }

    private boolean readRgbTransparency(int chunkLength) throws IOException {
        if (chunkLength == 6) {
            this.trnsR = this.stream.readShort();
            this.trnsG = this.stream.readShort();
            this.trnsB = this.stream.readShort();
            return true;
        }
        return false;
    }

    private void parseTransparencyChunk(int chunkLength) throws IOException {
        switch (this.colorType) {
            case 0:
                boolean grayTransparency = readGrayTransparency(chunkLength);
                this.tRNS_present = grayTransparency;
                this.tRNS_GRAY_RGB = grayTransparency;
                break;
            case 1:
            default:
                emitWarning("TransparencyChunk may not present when alpha explicitly defined");
                skip(chunkLength);
                break;
            case 2:
                boolean rgbTransparency = readRgbTransparency(chunkLength);
                this.tRNS_present = rgbTransparency;
                this.tRNS_GRAY_RGB = rgbTransparency;
                break;
            case 3:
                this.tRNS_present = readPaletteTransparency(chunkLength);
                break;
        }
    }

    private int parsePngMeta() throws IOException {
        while (true) {
            int[] chunk = readChunk();
            if (chunk[0] < 0) {
                throw new IOException("Invalid chunk length");
            }
            switch (chunk[1]) {
                case IDAT_TYPE /* 1229209940 */:
                    return chunk[0];
                case IEND_TYPE /* 1229278788 */:
                    return 0;
                case PLTE_TYPE /* 1347179589 */:
                    parsePaletteChunk(chunk[0]);
                    break;
                case tRNS_TYPE /* 1951551059 */:
                    parseTransparencyChunk(chunk[0]);
                    break;
                default:
                    skip(chunk[0]);
                    break;
            }
            this.stream.readInt();
        }
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public void dispose() {
    }

    private ImageStorage.ImageType getType() {
        switch (this.colorType) {
            case 0:
                return this.tRNS_present ? ImageStorage.ImageType.GRAY_ALPHA : ImageStorage.ImageType.GRAY;
            case 1:
            case 5:
            default:
                throw new RuntimeException();
            case 2:
                return this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
            case 3:
                return ImageStorage.ImageType.PALETTE;
            case 4:
                return ImageStorage.ImageType.GRAY_ALPHA;
            case 6:
                return ImageStorage.ImageType.RGBA;
        }
    }

    private void doSubFilter(byte[] line, int bpp) {
        int l2 = line.length;
        for (int i2 = bpp; i2 != l2; i2++) {
            line[i2] = (byte) (line[i2] + line[i2 - bpp]);
        }
    }

    private void doUpFilter(byte[] line, byte[] pline) {
        int l2 = line.length;
        for (int i2 = 0; i2 != l2; i2++) {
            line[i2] = (byte) (line[i2] + pline[i2]);
        }
    }

    private void doAvrgFilter(byte[] line, byte[] pline, int bpp) {
        int l2 = line.length;
        for (int i2 = 0; i2 != bpp; i2++) {
            line[i2] = (byte) (line[i2] + ((pline[i2] & 255) / 2));
        }
        for (int i3 = bpp; i3 != l2; i3++) {
            line[i3] = (byte) (line[i3] + (((line[i3 - bpp] & 255) + (pline[i3] & 255)) / 2));
        }
    }

    private static int paethPr(int a2, int b2, int c2) {
        int pa = Math.abs(b2 - c2);
        int pb = Math.abs(a2 - c2);
        int pc = Math.abs(((b2 - c2) + a2) - c2);
        return (pa > pb || pa > pc) ? pb <= pc ? b2 : c2 : a2;
    }

    private void doPaethFilter(byte[] line, byte[] pline, int bpp) {
        int l2 = line.length;
        for (int i2 = 0; i2 != bpp; i2++) {
            line[i2] = (byte) (line[i2] + pline[i2]);
        }
        for (int i3 = bpp; i3 != l2; i3++) {
            line[i3] = (byte) (line[i3] + paethPr(line[i3 - bpp] & 255, pline[i3] & 255, pline[i3 - bpp] & 255));
        }
    }

    private void doFilter(byte[] line, byte[] pline, int fType, int bpp) {
        switch (fType) {
            case 1:
                doSubFilter(line, bpp);
                break;
            case 2:
                doUpFilter(line, pline);
                break;
            case 3:
                doAvrgFilter(line, pline, bpp);
                break;
            case 4:
                doPaethFilter(line, pline, bpp);
                break;
        }
    }

    private void downsample16to8trns_gray(byte[] line, byte[] image, int pos, int step) {
        int l2 = line.length / 2;
        int oPos = pos;
        for (int i2 = 0; i2 < l2; i2++) {
            int gray16 = (short) (((line[i2 * 2] & 255) * 256) + (line[(i2 * 2) + 1] & 255));
            image[oPos + 0] = line[i2 * 2];
            image[oPos + 1] = gray16 == this.trnsG ? (byte) 0 : (byte) -1;
            oPos += step * 2;
        }
    }

    private void downsample16to8trns_rgb(byte[] line, byte[] image, int pos, int step) {
        int l2 = (line.length / 2) / 3;
        int oPos = pos;
        for (int i2 = 0; i2 < l2; i2++) {
            int iPos = i2 * 6;
            int r16 = (short) (((line[iPos + 0] & 255) * 256) + (line[iPos + 1] & 255));
            int g16 = (short) (((line[iPos + 2] & 255) * 256) + (line[iPos + 3] & 255));
            int b16 = (short) (((line[iPos + 4] & 255) * 256) + (line[iPos + 5] & 255));
            image[oPos + 0] = line[iPos + 0];
            image[oPos + 1] = line[iPos + 2];
            image[oPos + 2] = line[iPos + 4];
            image[oPos + 3] = (r16 == this.trnsR && g16 == this.trnsG && b16 == this.trnsB) ? (byte) 0 : (byte) -1;
            oPos += step * 4;
        }
    }

    private void downsample16to8_plain(byte[] line, byte[] image, int pos, int step, int bpp) {
        int l2 = ((line.length / 2) / bpp) * bpp;
        int stepBpp = step * bpp;
        int i2 = 0;
        int oPos = pos;
        while (i2 != l2) {
            for (int b2 = 0; b2 != bpp; b2++) {
                image[oPos + b2] = line[(i2 + b2) * 2];
            }
            oPos += stepBpp;
            i2 += bpp;
        }
    }

    private void downsample16to8(byte[] line, byte[] image, int pos, int step, int bpp) {
        if (!this.tRNS_GRAY_RGB) {
            downsample16to8_plain(line, image, pos, step, bpp);
        } else if (this.colorType == 0) {
            downsample16to8trns_gray(line, image, pos, step);
        } else if (this.colorType == 2) {
            downsample16to8trns_rgb(line, image, pos, step);
        }
    }

    private void copyTrns_gray(byte[] line, byte[] image, int pos, int step) {
        byte tG = (byte) this.trnsG;
        int oPos = pos;
        int l2 = line.length;
        for (int i2 = 0; i2 < l2; i2++) {
            byte gray = line[i2];
            image[oPos] = gray;
            image[oPos + 1] = gray == tG ? (byte) 0 : (byte) -1;
            oPos += 2 * step;
        }
    }

    private void copyTrns_rgb(byte[] line, byte[] image, int pos, int step) {
        byte tR = (byte) this.trnsR;
        byte tG = (byte) this.trnsG;
        byte tB = (byte) this.trnsB;
        int l2 = line.length / 3;
        int oPos = pos;
        for (int i2 = 0; i2 < l2; i2++) {
            byte r2 = line[i2 * 3];
            byte g2 = line[(i2 * 3) + 1];
            byte b2 = line[(i2 * 3) + 2];
            image[oPos + 0] = r2;
            image[oPos + 1] = g2;
            image[oPos + 2] = b2;
            image[oPos + 3] = (r2 == tR && g2 == tG && b2 == tB) ? (byte) 0 : (byte) -1;
            oPos += step * 4;
        }
    }

    private void copy_plain(byte[] line, byte[] image, int pos, int step, int bpp) {
        int l2 = line.length;
        int stepBpp = step * bpp;
        int i2 = 0;
        int oPos = pos;
        while (i2 != l2) {
            for (int b2 = 0; b2 != bpp; b2++) {
                image[oPos + b2] = line[i2 + b2];
            }
            oPos += stepBpp;
            i2 += bpp;
        }
    }

    private void copy(byte[] line, byte[] image, int pos, int step, int resultBpp) {
        if (!this.tRNS_GRAY_RGB) {
            if (step == 1) {
                System.arraycopy(line, 0, image, pos, line.length);
                return;
            } else {
                copy_plain(line, image, pos, step, resultBpp);
                return;
            }
        }
        if (this.colorType == 0) {
            copyTrns_gray(line, image, pos, step);
        } else if (this.colorType == 2) {
            copyTrns_rgb(line, image, pos, step);
        }
    }

    private void upsampleTo8Palette(byte[] line, byte[] image, int pos, int w2, int step) {
        int samplesInByte = 8 / this.bitDepth;
        int maxV = (1 << this.bitDepth) - 1;
        int k2 = 0;
        for (int i2 = 0; i2 < w2; i2 += samplesInByte) {
            int p2 = w2 - i2 < samplesInByte ? w2 - i2 : samplesInByte;
            int in = line[k2] >> ((samplesInByte - p2) * this.bitDepth);
            for (int pp = p2 - 1; pp >= 0; pp--) {
                image[pos + ((i2 + pp) * step)] = (byte) (in & maxV);
                in >>= this.bitDepth;
            }
            k2++;
        }
    }

    private void upsampleTo8Gray(byte[] line, byte[] image, int pos, int w2, int step) {
        int samplesInByte = 8 / this.bitDepth;
        int maxV = (1 << this.bitDepth) - 1;
        int hmaxV = maxV / 2;
        int k2 = 0;
        for (int i2 = 0; i2 < w2; i2 += samplesInByte) {
            int p2 = w2 - i2 < samplesInByte ? w2 - i2 : samplesInByte;
            int in = line[k2] >> ((samplesInByte - p2) * this.bitDepth);
            for (int pp = p2 - 1; pp >= 0; pp--) {
                image[pos + ((i2 + pp) * step)] = (byte) ((((in & maxV) * 255) + hmaxV) / maxV);
                in >>= this.bitDepth;
            }
            k2++;
        }
    }

    private void upsampleTo8GrayTrns(byte[] line, byte[] image, int pos, int w2, int step) {
        int samplesInByte = 8 / this.bitDepth;
        int maxV = (1 << this.bitDepth) - 1;
        int hmaxV = maxV / 2;
        int k2 = 0;
        for (int i2 = 0; i2 < w2; i2 += samplesInByte) {
            int p2 = w2 - i2 < samplesInByte ? w2 - i2 : samplesInByte;
            int in = line[k2] >> ((samplesInByte - p2) * this.bitDepth);
            for (int pp = p2 - 1; pp >= 0; pp--) {
                int idx = pos + ((i2 + pp) * step * 2);
                int value = in & maxV;
                image[idx] = (byte) (((value * 255) + hmaxV) / maxV);
                image[idx + 1] = value == this.trnsG ? (byte) 0 : (byte) -1;
                in >>= this.bitDepth;
            }
            k2++;
        }
    }

    private void upsampleTo8(byte[] line, byte[] image, int pos, int w2, int step, int bpp) {
        if (this.colorType == 3) {
            upsampleTo8Palette(line, image, pos, w2, step);
            return;
        }
        if (bpp == 1) {
            upsampleTo8Gray(line, image, pos, w2, step);
        } else if (this.tRNS_GRAY_RGB && bpp == 2) {
            upsampleTo8GrayTrns(line, image, pos, w2, step);
        }
    }

    private static int mipSize(int size, int mip, int[] start, int[] increment) {
        return (((size - start[mip]) + increment[mip]) - 1) / increment[mip];
    }

    private static int mipPos(int pos, int mip, int[] start, int[] increment) {
        return start[mip] + (pos * increment[mip]);
    }

    private void loadMip(byte[] image, InputStream data, int mip) throws IOException {
        int mipWidth = mipSize(this.width, mip, starting_x, increment_x);
        int mipHeight = mipSize(this.height, mip, starting_y, increment_y);
        int scanLineSize = (((mipWidth * this.bitDepth) * numBandsPerColorType[this.colorType]) + 7) / 8;
        byte[] scanLine0 = new byte[scanLineSize];
        byte[] scanLine1 = new byte[scanLineSize];
        int resultBpp = bpp();
        int srcBpp = numBandsPerColorType[this.colorType] * bytesPerColor();
        for (int y2 = 0; y2 != mipHeight; y2++) {
            int filterByte = data.read();
            if (filterByte == -1) {
                throw new EOFException();
            }
            if (data.read(scanLine0) != scanLineSize) {
                throw new EOFException();
            }
            doFilter(scanLine0, scanLine1, filterByte, srcBpp);
            int pos = ((mipPos(y2, mip, starting_y, increment_y) * this.width) + starting_x[mip]) * resultBpp;
            int step = increment_x[mip];
            if (this.bitDepth == 16) {
                downsample16to8(scanLine0, image, pos, step, resultBpp);
            } else if (this.bitDepth < 8) {
                upsampleTo8(scanLine0, image, pos, mipWidth, step, resultBpp);
            } else {
                copy(scanLine0, image, pos, step, resultBpp);
            }
            byte[] scanLineSwp = scanLine0;
            scanLine0 = scanLine1;
            scanLine1 = scanLineSwp;
        }
    }

    private void load(byte[] image, InputStream data) throws IOException {
        if (this.isInterlaced) {
            for (int mip = 0; mip != 7; mip++) {
                if (this.width > starting_x[mip] && this.height > starting_y[mip]) {
                    loadMip(image, data, mip);
                }
            }
            return;
        }
        loadMip(image, data, 7);
    }

    private ImageFrame decodePalette(byte[] srcImage, ImageMetadata metadata) throws IOException {
        int bpp = this.tRNS_present ? 4 : 3;
        if (this.width >= (Integer.MAX_VALUE / this.height) / bpp) {
            throw new IOException("Bad PNG image size!");
        }
        byte[] newImage = new byte[this.width * this.height * bpp];
        int l2 = this.width * this.height;
        if (this.tRNS_present) {
            int j2 = 0;
            for (int i2 = 0; i2 != l2; i2++) {
                int index = 255 & srcImage[i2];
                newImage[j2 + 0] = this.palette[0][index];
                newImage[j2 + 1] = this.palette[1][index];
                newImage[j2 + 2] = this.palette[2][index];
                newImage[j2 + 3] = this.palette[3][index];
                j2 += 4;
            }
        } else {
            int j3 = 0;
            for (int i3 = 0; i3 != l2; i3++) {
                int index2 = 255 & srcImage[i3];
                newImage[j3 + 0] = this.palette[0][index2];
                newImage[j3 + 1] = this.palette[1][index2];
                newImage[j3 + 2] = this.palette[2][index2];
                j3 += 3;
            }
        }
        ImageStorage.ImageType type = this.tRNS_present ? ImageStorage.ImageType.RGBA : ImageStorage.ImageType.RGB;
        return new ImageFrame(type, ByteBuffer.wrap(newImage), this.width, this.height, this.width * bpp, (byte[][]) null, metadata);
    }

    private int bpp() {
        return numBandsPerColorType[this.colorType] + (this.tRNS_GRAY_RGB ? 1 : 0);
    }

    private int bytesPerColor() {
        return this.bitDepth == 16 ? 2 : 1;
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public ImageFrame load(int imageIndex, int rWidth, int rHeight, boolean preserveAspectRatio, boolean smooth) throws IOException {
        ImageFrame imageFrame;
        if (imageIndex != 0) {
            return null;
        }
        int dataSize = parsePngMeta();
        if (dataSize == 0) {
            emitWarning("No image data in PNG");
            return null;
        }
        int bpp = bpp();
        if (this.width >= (Integer.MAX_VALUE / this.height) / bpp) {
            throw new IOException("Bad PNG image size!");
        }
        int[] outWH = ImageTools.computeDimensions(this.width, this.height, rWidth, rHeight, preserveAspectRatio);
        int rWidth2 = outWH[0];
        int rHeight2 = outWH[1];
        ImageMetadata metaData = new ImageMetadata(null, true, null, null, null, null, null, Integer.valueOf(rWidth2), Integer.valueOf(rHeight2), null, null, null);
        updateImageMetadata(metaData);
        ByteBuffer bb2 = ByteBuffer.allocate(bpp * this.width * this.height);
        PNGIDATChunkInputStream iDat = new PNGIDATChunkInputStream(this.stream, dataSize);
        Inflater inf = new Inflater();
        InputStream data = new BufferedInputStream(new InflaterInputStream(iDat, inf));
        try {
            try {
                load(bb2.array(), data);
                if (inf != null) {
                    inf.end();
                }
                if (this.colorType == 3) {
                    imageFrame = decodePalette(bb2.array(), metaData);
                } else {
                    imageFrame = new ImageFrame(getType(), bb2, this.width, this.height, bpp * this.width, this.palette, metaData);
                }
                ImageFrame imgPNG = imageFrame;
                if (this.width != rWidth2 || this.height != rHeight2) {
                    imgPNG = ImageTools.scaleImageFrame(imgPNG, rWidth2, rHeight2, smooth);
                }
                return imgPNG;
            } catch (IOException e2) {
                throw e2;
            }
        } catch (Throwable th) {
            if (inf != null) {
                inf.end();
            }
            throw th;
        }
    }
}
