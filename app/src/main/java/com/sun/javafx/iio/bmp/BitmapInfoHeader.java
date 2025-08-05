package com.sun.javafx.iio.bmp;

import java.io.IOException;

/* compiled from: BMPImageLoaderFactory.java */
/* loaded from: jfxrt.jar:com/sun/javafx/iio/bmp/BitmapInfoHeader.class */
final class BitmapInfoHeader {
    static final int BIH_SIZE = 40;
    static final int BIH4_SIZE = 108;
    static final int BIH5_SIZE = 124;
    static final int BI_RGB = 0;
    static final int BI_RLE8 = 1;
    static final int BI_RLE4 = 2;
    static final int BI_BITFIELDS = 3;
    static final int BI_JPEG = 4;
    static final int BI_PNG = 5;
    final int biSize;
    final int biWidth;
    final int biHeight;
    final short biPlanes;
    final short biBitCount;
    final int biCompression;
    final int biSizeImage;
    final int biXPelsPerMeter;
    final int biYPelsPerMeter;
    final int biClrUsed;
    final int biClrImportant;

    BitmapInfoHeader(LEInputStream data) throws IOException {
        this.biSize = data.readInt();
        this.biWidth = data.readInt();
        this.biHeight = data.readInt();
        this.biPlanes = data.readShort();
        this.biBitCount = data.readShort();
        this.biCompression = data.readInt();
        this.biSizeImage = data.readInt();
        this.biXPelsPerMeter = data.readInt();
        this.biYPelsPerMeter = data.readInt();
        this.biClrUsed = data.readInt();
        this.biClrImportant = data.readInt();
        if (this.biSize > 40) {
            if (this.biSize == 108 || this.biSize == 124) {
                data.skipBytes(this.biSize - 40);
            } else {
                throw new IOException("BitmapInfoHeader is corrupt");
            }
        }
        if (this.biWidth <= 0) {
            throw new IOException("Bad BMP image width, must be > 0!");
        }
        int height = Math.abs(this.biHeight);
        if (height == 0) {
            throw new IOException("Bad BMP image height, must be != 0!");
        }
        if (this.biWidth >= Integer.MAX_VALUE / height) {
            throw new IOException("Bad BMP image size!");
        }
        validate();
    }

    void validate() throws IOException {
        if (this.biBitCount < 1 || this.biCompression == 4 || this.biCompression == 5) {
            throw new IOException("Unsupported BMP image: Embedded JPEG or PNG images are not supported");
        }
        switch (this.biCompression) {
            case 0:
                return;
            case 1:
                if (this.biBitCount != 8) {
                    throw new IOException("Invalid BMP image: Only 8 bpp images can be RLE8 compressed");
                }
                return;
            case 2:
                if (this.biBitCount != 4) {
                    throw new IOException("Invalid BMP image: Only 4 bpp images can be RLE4 compressed");
                }
                return;
            case 3:
                if (this.biBitCount != 16 && this.biBitCount != 32) {
                    throw new IOException("Invalid BMP image: Only 16 or 32 bpp images can use BITFIELDS compression");
                }
                return;
            default:
                throw new IOException("Unknown BMP compression type");
        }
    }
}
