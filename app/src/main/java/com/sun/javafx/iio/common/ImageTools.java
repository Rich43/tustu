package com.sun.javafx.iio.common;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/ImageTools.class */
public class ImageTools {
    public static final int PROGRESS_INTERVAL = 5;

    public static int readFully(InputStream stream, byte[] b2, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (off < 0 || len < 0 || off + len > b2.length || off + len < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
        }
        while (len > 0) {
            int nbytes = stream.read(b2, off, len);
            if (nbytes == -1) {
                throw new EOFException();
            }
            off += nbytes;
            len -= nbytes;
        }
        return len;
    }

    public static int readFully(InputStream stream, byte[] b2) throws IOException {
        return readFully(stream, b2, 0, b2.length);
    }

    public static void skipFully(InputStream stream, long n2) throws IOException {
        while (n2 > 0) {
            long skipped = stream.skip(n2);
            if (skipped <= 0) {
                if (stream.read() == -1) {
                    throw new EOFException();
                }
                n2--;
            } else {
                n2 -= skipped;
            }
        }
    }

    public static ImageStorage.ImageType getConvertedType(ImageStorage.ImageType type) {
        ImageStorage.ImageType retType;
        switch (type) {
            case GRAY:
                retType = ImageStorage.ImageType.GRAY;
                break;
            case GRAY_ALPHA:
            case GRAY_ALPHA_PRE:
            case PALETTE_ALPHA:
            case PALETTE_ALPHA_PRE:
            case PALETTE_TRANS:
            case RGBA:
                retType = ImageStorage.ImageType.RGBA_PRE;
                break;
            case PALETTE:
            case RGB:
                retType = ImageStorage.ImageType.RGB;
                break;
            case RGBA_PRE:
                retType = ImageStorage.ImageType.RGBA_PRE;
                break;
            default:
                throw new IllegalArgumentException("Unsupported ImageType " + ((Object) type));
        }
        return retType;
    }

    public static byte[] createImageArray(ImageStorage.ImageType type, int width, int height) {
        int numBands;
        switch (type) {
            case GRAY:
            case PALETTE_ALPHA:
            case PALETTE_ALPHA_PRE:
            case PALETTE:
                numBands = 1;
                break;
            case GRAY_ALPHA:
            case GRAY_ALPHA_PRE:
                numBands = 2;
                break;
            case PALETTE_TRANS:
            default:
                throw new IllegalArgumentException("Unsupported ImageType " + ((Object) type));
            case RGBA:
            case RGBA_PRE:
                numBands = 4;
                break;
            case RGB:
                numBands = 3;
                break;
        }
        return new byte[width * height * numBands];
    }

    public static ImageFrame convertImageFrame(ImageFrame frame) {
        byte[] inArray;
        ImageFrame retFrame;
        ImageStorage.ImageType type = frame.getImageType();
        ImageStorage.ImageType convertedType = getConvertedType(type);
        if (convertedType == type) {
            retFrame = frame;
        } else {
            Buffer buf = frame.getImageData();
            if (!(buf instanceof ByteBuffer)) {
                throw new IllegalArgumentException("!(frame.getImageData() instanceof ByteBuffer)");
            }
            ByteBuffer bbuf = (ByteBuffer) buf;
            if (bbuf.hasArray()) {
                inArray = bbuf.array();
            } else {
                inArray = new byte[bbuf.capacity()];
                bbuf.get(inArray);
            }
            int width = frame.getWidth();
            int height = frame.getHeight();
            int inStride = frame.getStride();
            byte[] outArray = createImageArray(convertedType, width, height);
            ByteBuffer newBuf = ByteBuffer.wrap(outArray);
            int outStride = outArray.length / height;
            byte[][] palette = frame.getPalette();
            ImageMetadata metadata = frame.getMetadata();
            int transparentIndex = metadata.transparentIndex != null ? metadata.transparentIndex.intValue() : 0;
            convert(width, height, type, inArray, 0, inStride, outArray, 0, outStride, palette, transparentIndex, false);
            ImageMetadata imd = new ImageMetadata(metadata.gamma, metadata.blackIsZero, null, metadata.backgroundColor, null, metadata.delayTime, metadata.loopCount, metadata.imageWidth, metadata.imageHeight, metadata.imageLeftPosition, metadata.imageTopPosition, metadata.disposalMethod);
            retFrame = new ImageFrame(convertedType, newBuf, width, height, outStride, (byte[][]) null, imd);
        }
        return retFrame;
    }

    public static byte[] convert(int width, int height, ImageStorage.ImageType inputType, byte[] input, int inputOffset, int inRowStride, byte[] output, int outputOffset, int outRowStride, byte[][] palette, int transparentIndex, boolean skipTransparent) {
        if (inputType == ImageStorage.ImageType.GRAY || inputType == ImageStorage.ImageType.RGB || inputType == ImageStorage.ImageType.RGBA_PRE) {
            if (input != output) {
                int bytesPerRow = width;
                if (inputType == ImageStorage.ImageType.RGB) {
                    bytesPerRow *= 3;
                } else if (inputType == ImageStorage.ImageType.RGBA_PRE) {
                    bytesPerRow *= 4;
                }
                if (height == 1) {
                    System.arraycopy(input, inputOffset, output, outputOffset, bytesPerRow);
                } else {
                    int inRowOffset = inputOffset;
                    int outRowOffset = outputOffset;
                    for (int row = 0; row < height; row++) {
                        System.arraycopy(input, inRowOffset, output, outRowOffset, bytesPerRow);
                        inRowOffset += inRowStride;
                        outRowOffset += outRowStride;
                    }
                }
            }
        } else if (inputType == ImageStorage.ImageType.GRAY_ALPHA || inputType == ImageStorage.ImageType.GRAY_ALPHA_PRE) {
            int inOffset = inputOffset;
            int outOffset = outputOffset;
            if (inputType == ImageStorage.ImageType.GRAY_ALPHA) {
                for (int y2 = 0; y2 < height; y2++) {
                    int inOff = inOffset;
                    int outOff = outOffset;
                    for (int x2 = 0; x2 < width; x2++) {
                        int i2 = inOff;
                        int inOff2 = inOff + 1;
                        byte gray = input[i2];
                        inOff = inOff2 + 1;
                        int alpha = input[inOff2] & 255;
                        byte gray2 = (byte) ((alpha / 255.0f) * (gray & 255));
                        int i3 = outOff;
                        int outOff2 = outOff + 1;
                        output[i3] = gray2;
                        int outOff3 = outOff2 + 1;
                        output[outOff2] = gray2;
                        int outOff4 = outOff3 + 1;
                        output[outOff3] = gray2;
                        outOff = outOff4 + 1;
                        output[outOff4] = (byte) alpha;
                    }
                    inOffset += inRowStride;
                    outOffset += outRowStride;
                }
            } else {
                for (int y3 = 0; y3 < height; y3++) {
                    int inOff3 = inOffset;
                    int outOff5 = outOffset;
                    for (int x3 = 0; x3 < width; x3++) {
                        int i4 = inOff3;
                        int inOff4 = inOff3 + 1;
                        byte gray3 = input[i4];
                        int i5 = outOff5;
                        int outOff6 = outOff5 + 1;
                        output[i5] = gray3;
                        int outOff7 = outOff6 + 1;
                        output[outOff6] = gray3;
                        int outOff8 = outOff7 + 1;
                        output[outOff7] = gray3;
                        outOff5 = outOff8 + 1;
                        inOff3 = inOff4 + 1;
                        output[outOff8] = input[inOff4];
                    }
                    inOffset += inRowStride;
                    outOffset += outRowStride;
                }
            }
        } else if (inputType == ImageStorage.ImageType.PALETTE) {
            int outOffset2 = outputOffset;
            byte[] red = palette[0];
            byte[] green = palette[1];
            byte[] blue = palette[2];
            int inOff5 = inputOffset;
            int outOff9 = outOffset2;
            for (int x4 = 0; x4 < width; x4++) {
                int i6 = inOff5;
                inOff5++;
                int index = input[i6] & 255;
                int i7 = outOff9;
                int outOff10 = outOff9 + 1;
                output[i7] = red[index];
                int outOff11 = outOff10 + 1;
                output[outOff10] = green[index];
                outOff9 = outOff11 + 1;
                output[outOff11] = blue[index];
                outOffset2 += outRowStride;
            }
        } else if (inputType == ImageStorage.ImageType.PALETTE_ALPHA) {
            byte[] red2 = palette[0];
            byte[] green2 = palette[1];
            byte[] blue2 = palette[2];
            byte[] alpha2 = palette[3];
            int inOff6 = inputOffset;
            int outOff12 = outputOffset;
            for (int x5 = 0; x5 < width; x5++) {
                int i8 = inOff6;
                inOff6++;
                int index2 = input[i8] & 255;
                byte r2 = red2[index2];
                byte g2 = green2[index2];
                byte b2 = blue2[index2];
                int a2 = alpha2[index2] & 255;
                float f2 = a2 / 255.0f;
                int i9 = outOff12;
                int outOff13 = outOff12 + 1;
                output[i9] = (byte) (f2 * (r2 & 255));
                int outOff14 = outOff13 + 1;
                output[outOff13] = (byte) (f2 * (g2 & 255));
                int outOff15 = outOff14 + 1;
                output[outOff14] = (byte) (f2 * (b2 & 255));
                outOff12 = outOff15 + 1;
                output[outOff15] = (byte) a2;
            }
            int i10 = inputOffset + inRowStride;
            int i11 = outputOffset + outRowStride;
        } else if (inputType == ImageStorage.ImageType.PALETTE_ALPHA_PRE) {
            int inOffset2 = inputOffset;
            int outOffset3 = outputOffset;
            byte[] red3 = palette[0];
            byte[] green3 = palette[1];
            byte[] blue3 = palette[2];
            byte[] alpha3 = palette[3];
            for (int y4 = 0; y4 < height; y4++) {
                int inOff7 = inOffset2;
                int outOff16 = outOffset3;
                for (int x6 = 0; x6 < width; x6++) {
                    int i12 = inOff7;
                    inOff7++;
                    int index3 = input[i12] & 255;
                    int i13 = outOff16;
                    int outOff17 = outOff16 + 1;
                    output[i13] = red3[index3];
                    int outOff18 = outOff17 + 1;
                    output[outOff17] = green3[index3];
                    int outOff19 = outOff18 + 1;
                    output[outOff18] = blue3[index3];
                    outOff16 = outOff19 + 1;
                    output[outOff19] = alpha3[index3];
                }
                inOffset2 += inRowStride;
                outOffset3 += outRowStride;
            }
        } else if (inputType == ImageStorage.ImageType.PALETTE_TRANS) {
            int inOffset3 = inputOffset;
            int outOffset4 = outputOffset;
            for (int y5 = 0; y5 < height; y5++) {
                int inOff8 = inOffset3;
                int outOff20 = outOffset4;
                byte[] red4 = palette[0];
                byte[] green4 = palette[1];
                byte[] blue4 = palette[2];
                for (int x7 = 0; x7 < width; x7++) {
                    int i14 = inOff8;
                    inOff8++;
                    int index4 = input[i14] & 255;
                    if (index4 == transparentIndex) {
                        if (skipTransparent) {
                            outOff20 += 4;
                        } else {
                            int i15 = outOff20;
                            int outOff21 = outOff20 + 1;
                            output[i15] = 0;
                            int outOff22 = outOff21 + 1;
                            output[outOff21] = 0;
                            int outOff23 = outOff22 + 1;
                            output[outOff22] = 0;
                            outOff20 = outOff23 + 1;
                            output[outOff23] = 0;
                        }
                    } else {
                        int i16 = outOff20;
                        int outOff24 = outOff20 + 1;
                        output[i16] = red4[index4];
                        int outOff25 = outOff24 + 1;
                        output[outOff24] = green4[index4];
                        int outOff26 = outOff25 + 1;
                        output[outOff25] = blue4[index4];
                        outOff20 = outOff26 + 1;
                        output[outOff26] = -1;
                    }
                }
                inOffset3 += inRowStride;
                outOffset4 += outRowStride;
            }
        } else if (inputType == ImageStorage.ImageType.RGBA) {
            int inOffset4 = inputOffset;
            int outOffset5 = outputOffset;
            for (int y6 = 0; y6 < height; y6++) {
                int inOff9 = inOffset4;
                int outOff27 = outOffset5;
                for (int x8 = 0; x8 < width; x8++) {
                    int i17 = inOff9;
                    int inOff10 = inOff9 + 1;
                    byte red5 = input[i17];
                    int inOff11 = inOff10 + 1;
                    byte green5 = input[inOff10];
                    int inOff12 = inOff11 + 1;
                    byte blue5 = input[inOff11];
                    inOff9 = inOff12 + 1;
                    int alpha4 = input[inOff12] & 255;
                    float f3 = alpha4 / 255.0f;
                    int i18 = outOff27;
                    int outOff28 = outOff27 + 1;
                    output[i18] = (byte) (f3 * (red5 & 255));
                    int outOff29 = outOff28 + 1;
                    output[outOff28] = (byte) (f3 * (green5 & 255));
                    int outOff30 = outOff29 + 1;
                    output[outOff29] = (byte) (f3 * (blue5 & 255));
                    outOff27 = outOff30 + 1;
                    output[outOff30] = (byte) alpha4;
                }
                inOffset4 += inRowStride;
                outOffset5 += outRowStride;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported ImageType " + ((Object) inputType));
        }
        return output;
    }

    public static String getScaledImageName(String path) {
        StringBuilder result = new StringBuilder();
        int slash = path.lastIndexOf(47);
        String name = slash < 0 ? path : path.substring(slash + 1);
        int dot = name.lastIndexOf(".");
        if (dot < 0) {
            dot = name.length();
        }
        if (slash >= 0) {
            result.append(path.substring(0, slash + 1));
        }
        result.append(name.substring(0, dot));
        result.append("@2x");
        result.append(name.substring(dot));
        return result.toString();
    }

    public static InputStream createInputStream(String input) throws IOException {
        InputStream stream = null;
        try {
            File file = new File(input);
            if (file.exists()) {
                stream = new FileInputStream(file);
            }
        } catch (Exception e2) {
        }
        if (stream == null) {
            URL url = new URL(input);
            stream = url.openStream();
        }
        return stream;
    }

    private static void computeUpdatedPixels(int sourceOffset, int sourceExtent, int destinationOffset, int dstMin, int dstMax, int sourceSubsampling, int passStart, int passExtent, int passPeriod, int[] vals, int offset) {
        boolean gotPixel = false;
        int firstDst = -1;
        int secondDst = -1;
        int lastDst = -1;
        for (int i2 = 0; i2 < passExtent; i2++) {
            int src = passStart + (i2 * passPeriod);
            if (src >= sourceOffset && (src - sourceOffset) % sourceSubsampling == 0) {
                if (src >= sourceOffset + sourceExtent) {
                    break;
                }
                int dst = destinationOffset + ((src - sourceOffset) / sourceSubsampling);
                if (dst < dstMin) {
                    continue;
                } else {
                    if (dst > dstMax) {
                        break;
                    }
                    if (!gotPixel) {
                        firstDst = dst;
                        gotPixel = true;
                    } else if (secondDst == -1) {
                        secondDst = dst;
                    }
                    lastDst = dst;
                }
            }
        }
        vals[offset] = firstDst;
        if (!gotPixel) {
            vals[offset + 2] = 0;
        } else {
            vals[offset + 2] = (lastDst - firstDst) + 1;
        }
        vals[offset + 4] = Math.max(secondDst - firstDst, 1);
    }

    public static int[] computeUpdatedPixels(Rectangle sourceRegion, Point2D destinationOffset, int dstMinX, int dstMinY, int dstMaxX, int dstMaxY, int sourceXSubsampling, int sourceYSubsampling, int passXStart, int passYStart, int passWidth, int passHeight, int passPeriodX, int passPeriodY) {
        int[] vals = new int[6];
        computeUpdatedPixels(sourceRegion.f11913x, sourceRegion.width, (int) (destinationOffset.f11907x + 0.5f), dstMinX, dstMaxX, sourceXSubsampling, passXStart, passWidth, passPeriodX, vals, 0);
        computeUpdatedPixels(sourceRegion.f11914y, sourceRegion.height, (int) (destinationOffset.f11908y + 0.5f), dstMinY, dstMaxY, sourceYSubsampling, passYStart, passHeight, passPeriodY, vals, 1);
        return vals;
    }

    public static int[] computeDimensions(int sourceWidth, int sourceHeight, int maxWidth, int maxHeight, boolean preserveAspectRatio) {
        int finalWidth = maxWidth < 0 ? 0 : maxWidth;
        int finalHeight = maxHeight < 0 ? 0 : maxHeight;
        if (finalWidth == 0 && finalHeight == 0) {
            finalWidth = sourceWidth;
            finalHeight = sourceHeight;
        } else if (finalWidth != sourceWidth || finalHeight != sourceHeight) {
            if (preserveAspectRatio) {
                if (finalWidth == 0) {
                    finalWidth = (int) ((sourceWidth * finalHeight) / sourceHeight);
                } else if (finalHeight == 0) {
                    finalHeight = (int) ((sourceHeight * finalWidth) / sourceWidth);
                } else {
                    float scale = Math.min(finalWidth / sourceWidth, finalHeight / sourceHeight);
                    finalWidth = (int) (sourceWidth * scale);
                    finalHeight = (int) (sourceHeight * scale);
                }
            } else {
                if (finalHeight == 0) {
                    finalHeight = sourceHeight;
                }
                if (finalWidth == 0) {
                    finalWidth = sourceWidth;
                }
            }
            if (finalWidth <= 0) {
                finalWidth = 1;
            }
            if (finalHeight <= 0) {
                finalHeight = 1;
            }
        }
        return new int[]{finalWidth, finalHeight};
    }

    public static ImageFrame scaleImageFrame(ImageFrame src, int destWidth, int destHeight, boolean isSmooth) {
        int numBands = ImageStorage.getNumBands(src.getImageType());
        ByteBuffer dst = scaleImage((ByteBuffer) src.getImageData(), src.getWidth(), src.getHeight(), numBands, destWidth, destHeight, isSmooth);
        return new ImageFrame(src.getImageType(), dst, destWidth, destHeight, destWidth * numBands, (byte[][]) null, src.getMetadata());
    }

    public static ByteBuffer scaleImage(ByteBuffer src, int sourceWidth, int sourceHeight, int numBands, int destWidth, int destHeight, boolean isSmooth) {
        PushbroomScaler scaler = ScalerFactory.createScaler(sourceWidth, sourceHeight, numBands, destWidth, destHeight, isSmooth);
        int stride = sourceWidth * numBands;
        if (src.hasArray()) {
            byte[] image = src.array();
            for (int y2 = 0; y2 != sourceHeight; y2++) {
                scaler.putSourceScanline(image, y2 * stride);
            }
        } else {
            byte[] scanline = new byte[stride];
            for (int y3 = 0; y3 != sourceHeight; y3++) {
                src.get(scanline);
                scaler.putSourceScanline(scanline, 0);
            }
        }
        return scaler.getDestination();
    }
}
