package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* compiled from: BMPImageLoaderFactory.java */
/* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/BMPImageLoader.class */
final class BMPImageLoader extends ImageLoaderImpl {
    static final short BM = 19778;
    static final int BFH_SIZE = 14;
    final LEInputStream data;
    int bfSize;
    int bfOffBits;
    byte[] bgra_palette;
    BitmapInfoHeader bih;
    int[] bitMasks;
    int[] bitOffsets;

    /* compiled from: BMPImageLoaderFactory.java */
    @FunctionalInterface
    /* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/BMPImageLoader$BitConverter.class */
    private interface BitConverter {
        byte convert(int i2, int i3, int i4);
    }

    BMPImageLoader(InputStream input) throws IOException {
        super(BMPDescriptor.theInstance);
        this.data = new LEInputStream(input);
        if (this.data.readShort() != BM) {
            throw new IOException("Invalid BMP file signature");
        }
        readHeader();
    }

    private void readHeader() throws IOException {
        this.bfSize = this.data.readInt();
        this.data.skipBytes(4);
        this.bfOffBits = this.data.readInt();
        this.bih = new BitmapInfoHeader(this.data);
        if (this.bfOffBits < this.bih.biSize + 14) {
            throw new IOException("Invalid bitmap bits offset");
        }
        if (this.bih.biSize + 14 != this.bfOffBits) {
            int length = (this.bfOffBits - this.bih.biSize) - 14;
            int paletteSize = length / 4;
            this.bgra_palette = new byte[paletteSize * 4];
            int read = this.data.in.read(this.bgra_palette);
            if (read < length) {
                this.data.skipBytes(length - read);
            }
        }
        if (this.bih.biCompression == 3) {
            parseBitfields();
        } else if (this.bih.biCompression == 0 && this.bih.biBitCount == 16) {
            this.bitMasks = new int[]{31744, 992, 31};
            this.bitOffsets = new int[]{10, 5, 0};
        }
    }

    private void parseBitfields() throws IOException {
        if (this.bgra_palette.length != 12) {
            throw new IOException("Invalid bit masks");
        }
        this.bitMasks = new int[3];
        this.bitOffsets = new int[3];
        for (int i2 = 0; i2 < 3; i2++) {
            int mask = getDWord(this.bgra_palette, i2 * 4);
            this.bitMasks[i2] = mask;
            int offset = 0;
            if (mask != 0) {
                while ((mask & 1) == 0) {
                    offset++;
                    mask >>>= 1;
                }
                if (!isPow2Minus1(mask)) {
                    throw new IOException("Bit mask is not contiguous");
                }
            }
            this.bitOffsets[i2] = offset;
        }
        if (!checkDisjointMasks(this.bitMasks[0], this.bitMasks[1], this.bitMasks[2])) {
            throw new IOException("Bit masks overlap");
        }
    }

    static boolean checkDisjointMasks(int m1, int m2, int m3) {
        return (((m1 & m2) | (m1 & m3)) | (m2 & m3)) == 0;
    }

    static boolean isPow2Minus1(int i2) {
        return (i2 & (i2 + 1)) == 0;
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public void dispose() {
    }

    private void readRLE(byte[] image, int rowLength, int hght, boolean isRLE4) throws IOException {
        int i2;
        int imgSize = this.bih.biSizeImage;
        if (imgSize == 0) {
            imgSize = this.bfSize - this.bfOffBits;
        }
        byte[] imgData = new byte[imgSize];
        ImageTools.readFully(this.data.in, imgData);
        boolean isBottomUp = this.bih.biHeight > 0;
        int line = isBottomUp ? hght - 1 : 0;
        int i3 = 0;
        int dstOffset = line * rowLength;
        while (i3 < imgSize) {
            int i4 = i3;
            int i5 = i3 + 1;
            int b1 = getByte(imgData, i4);
            i3 = i5 + 1;
            int b2 = getByte(imgData, i5);
            if (b1 == 0) {
                switch (b2) {
                    case 0:
                        line += isBottomUp ? -1 : 1;
                        dstOffset = line * rowLength;
                        break;
                    case 1:
                        return;
                    case 2:
                        int i6 = i3 + 1;
                        int deltaX = getByte(imgData, i3);
                        i3 = i6 + 1;
                        int deltaY = getByte(imgData, i6);
                        line += deltaY;
                        dstOffset = dstOffset + (deltaY * rowLength) + (deltaX * 3);
                        break;
                    default:
                        int indexData = 0;
                        for (int p2 = 0; p2 < b2; p2++) {
                            if (isRLE4) {
                                if ((p2 & 1) == 0) {
                                    int i7 = i3;
                                    i3++;
                                    indexData = getByte(imgData, i7);
                                    i2 = (indexData & 240) >> 4;
                                } else {
                                    i2 = indexData & 15;
                                }
                            } else {
                                int i8 = i3;
                                i3++;
                                i2 = getByte(imgData, i8);
                            }
                            int index = i2;
                            dstOffset = setRGBFromPalette(image, dstOffset, index);
                        }
                        if (isRLE4) {
                            if ((b2 & 3) != 1 && (b2 & 3) != 2) {
                                break;
                            } else {
                                i3++;
                                break;
                            }
                        } else if ((b2 & 1) == 1) {
                            i3++;
                            break;
                        } else {
                            break;
                        }
                }
            } else if (isRLE4) {
                int index1 = (b2 & 240) >> 4;
                int index2 = b2 & 15;
                for (int p3 = 0; p3 < b1; p3++) {
                    dstOffset = setRGBFromPalette(image, dstOffset, (p3 & 1) == 0 ? index1 : index2);
                }
            } else {
                for (int p4 = 0; p4 < b1; p4++) {
                    dstOffset = setRGBFromPalette(image, dstOffset, b2);
                }
            }
        }
    }

    private int setRGBFromPalette(byte[] image, int dstOffset, int index) {
        int index2 = index * 4;
        int dstOffset2 = dstOffset + 1;
        image[dstOffset] = this.bgra_palette[index2 + 2];
        int dstOffset3 = dstOffset2 + 1;
        image[dstOffset2] = this.bgra_palette[index2 + 1];
        int dstOffset4 = dstOffset3 + 1;
        image[dstOffset3] = this.bgra_palette[index2];
        return dstOffset4;
    }

    private void readPackedBits(byte[] image, int rowLength, int hght) throws IOException {
        int pixPerByte = 8 / this.bih.biBitCount;
        int bytesPerLine = ((this.bih.biWidth + pixPerByte) - 1) / pixPerByte;
        int srcStride = (bytesPerLine + 3) & (-4);
        int bitMask = (1 << this.bih.biBitCount) - 1;
        byte[] lineBuf = new byte[srcStride];
        for (int i2 = 0; i2 != hght; i2++) {
            ImageTools.readFully(this.data.in, lineBuf);
            int line = this.bih.biHeight < 0 ? i2 : (hght - i2) - 1;
            int dstOffset = line * rowLength;
            for (int x2 = 0; x2 != this.bih.biWidth; x2++) {
                int bitnum = x2 * this.bih.biBitCount;
                byte b2 = lineBuf[bitnum / 8];
                int shift = (8 - (bitnum & 7)) - this.bih.biBitCount;
                int index = (b2 >> shift) & bitMask;
                dstOffset = setRGBFromPalette(image, dstOffset, index);
            }
        }
    }

    private static int getDWord(byte[] buf, int pos) {
        return (buf[pos] & 255) | ((buf[pos + 1] & 255) << 8) | ((buf[pos + 2] & 255) << 16) | ((buf[pos + 3] & 255) << 24);
    }

    private static int getWord(byte[] buf, int pos) {
        return (buf[pos] & 255) | ((buf[pos + 1] & 255) << 8);
    }

    private static int getByte(byte[] buf, int pos) {
        return buf[pos] & 255;
    }

    private static byte convertFrom5To8Bit(int i2, int mask, int offset) {
        int b2 = (i2 & mask) >>> offset;
        return (byte) ((b2 << 3) | (b2 >> 2));
    }

    private static byte convertFromXTo8Bit(int i2, int mask, int offset) {
        int b2 = (i2 & mask) >>> offset;
        return (byte) ((b2 * 255.0d) / (mask >>> offset));
    }

    private void read16Bit(byte[] image, int rowLength, int hght, BitConverter converter) throws IOException {
        int bytesPerLine = this.bih.biWidth * 2;
        int srcStride = (bytesPerLine + 3) & (-4);
        byte[] lineBuf = new byte[srcStride];
        for (int i2 = 0; i2 != hght; i2++) {
            ImageTools.readFully(this.data.in, lineBuf);
            int line = this.bih.biHeight < 0 ? i2 : (hght - i2) - 1;
            int dstOffset = line * rowLength;
            for (int x2 = 0; x2 != this.bih.biWidth; x2++) {
                int element = getWord(lineBuf, x2 * 2);
                for (int j2 = 0; j2 < 3; j2++) {
                    int i3 = dstOffset;
                    dstOffset++;
                    image[i3] = converter.convert(element, this.bitMasks[j2], this.bitOffsets[j2]);
                }
            }
        }
    }

    private void read32BitRGB(byte[] image, int rowLength, int hght) throws IOException {
        int bytesPerLine = this.bih.biWidth * 4;
        byte[] lineBuf = new byte[bytesPerLine];
        for (int i2 = 0; i2 != hght; i2++) {
            ImageTools.readFully(this.data.in, lineBuf);
            int line = this.bih.biHeight < 0 ? i2 : (hght - i2) - 1;
            int dstOff = line * rowLength;
            for (int x2 = 0; x2 != this.bih.biWidth; x2++) {
                int srcOff = x2 * 4;
                int i3 = dstOff;
                int dstOff2 = dstOff + 1;
                image[i3] = lineBuf[srcOff + 2];
                int dstOff3 = dstOff2 + 1;
                image[dstOff2] = lineBuf[srcOff + 1];
                dstOff = dstOff3 + 1;
                image[dstOff3] = lineBuf[srcOff];
            }
        }
    }

    private void read32BitBF(byte[] image, int rowLength, int hght) throws IOException {
        int bytesPerLine = this.bih.biWidth * 4;
        byte[] lineBuf = new byte[bytesPerLine];
        for (int i2 = 0; i2 != hght; i2++) {
            ImageTools.readFully(this.data.in, lineBuf);
            int line = this.bih.biHeight < 0 ? i2 : (hght - i2) - 1;
            int dstOff = line * rowLength;
            for (int x2 = 0; x2 != this.bih.biWidth; x2++) {
                int srcOff = x2 * 4;
                int element = getDWord(lineBuf, srcOff);
                for (int j2 = 0; j2 < 3; j2++) {
                    int i3 = dstOff;
                    dstOff++;
                    image[i3] = convertFromXTo8Bit(element, this.bitMasks[j2], this.bitOffsets[j2]);
                }
            }
        }
    }

    private void read24Bit(byte[] image, int rowLength, int hght) throws IOException {
        int bmpStride = (rowLength + 3) & (-4);
        int padding = bmpStride - rowLength;
        for (int i2 = 0; i2 != hght; i2++) {
            int line = this.bih.biHeight < 0 ? i2 : (hght - i2) - 1;
            int lineOffset = line * rowLength;
            ImageTools.readFully(this.data.in, image, lineOffset, rowLength);
            this.data.skipBytes(padding);
            BGRtoRGB(image, lineOffset, rowLength);
        }
    }

    static void BGRtoRGB(byte[] data, int pos, int size) {
        for (int sz = size / 3; sz != 0; sz--) {
            byte b2 = data[pos];
            byte r2 = data[pos + 2];
            data[pos + 2] = b2;
            data[pos] = r2;
            pos += 3;
        }
    }

    @Override // com.sun.javafx.iio.ImageLoader
    public ImageFrame load(int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth) throws IOException {
        if (0 != imageIndex) {
            return null;
        }
        int hght = Math.abs(this.bih.biHeight);
        int[] outWH = ImageTools.computeDimensions(this.bih.biWidth, hght, width, height, preserveAspectRatio);
        int width2 = outWH[0];
        int height2 = outWH[1];
        if (width2 >= (Integer.MAX_VALUE / height2) / 3) {
            throw new IOException("Bad BMP image size!");
        }
        ImageMetadata imageMetadata = new ImageMetadata(null, Boolean.TRUE, null, null, null, null, null, Integer.valueOf(width2), Integer.valueOf(height2), null, null, null);
        updateImageMetadata(imageMetadata);
        int stride = this.bih.biWidth * 3;
        byte[] image = new byte[stride * hght];
        switch (this.bih.biBitCount) {
            case 1:
                readPackedBits(image, stride, hght);
                break;
            case 4:
                if (this.bih.biCompression == 2) {
                    readRLE(image, stride, hght, true);
                    break;
                } else {
                    readPackedBits(image, stride, hght);
                    break;
                }
            case 8:
                if (this.bih.biCompression == 1) {
                    readRLE(image, stride, hght, false);
                    break;
                } else {
                    readPackedBits(image, stride, hght);
                    break;
                }
            case 16:
                if (this.bih.biCompression == 3) {
                    read16Bit(image, stride, hght, BMPImageLoader::convertFromXTo8Bit);
                    break;
                } else {
                    read16Bit(image, stride, hght, BMPImageLoader::convertFrom5To8Bit);
                    break;
                }
            case 24:
                read24Bit(image, stride, hght);
                break;
            case 32:
                if (this.bih.biCompression == 3) {
                    read32BitBF(image, stride, hght);
                    break;
                } else {
                    read32BitRGB(image, stride, hght);
                    break;
                }
            default:
                throw new IOException("Unknown BMP bit depth");
        }
        ByteBuffer img = ByteBuffer.wrap(image);
        if (this.bih.biWidth != width2 || hght != height2) {
            img = ImageTools.scaleImage(img, this.bih.biWidth, hght, 3, width2, height2, smooth);
        }
        return new ImageFrame(ImageStorage.ImageType.RGB, img, width2, height2, width2 * 3, (byte[][]) null, imageMetadata);
    }
}
