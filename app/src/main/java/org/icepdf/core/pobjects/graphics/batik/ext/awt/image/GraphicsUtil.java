package org.icepdf.core.pobjects.graphics.batik.ext.awt.image;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/batik/ext/awt/image/GraphicsUtil.class */
public class GraphicsUtil {
    public static AffineTransform IDENTITY = new AffineTransform();
    public static final ColorModel Linear_sRGB = new DirectColorModel(ColorSpace.getInstance(1004), 24, 16711680, NormalizerImpl.CC_MASK, 255, 0, false, 3);
    public static final ColorModel Linear_sRGB_Pre = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, true, 3);
    public static final ColorModel Linear_sRGB_Unpre = new DirectColorModel(ColorSpace.getInstance(1004), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3);
    public static final ColorModel sRGB = new DirectColorModel(ColorSpace.getInstance(1000), 24, 16711680, NormalizerImpl.CC_MASK, 255, 0, false, 3);
    public static final ColorModel sRGB_Pre = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, true, 3);
    public static final ColorModel sRGB_Unpre = new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, false, 3);

    public static ColorModel makeLinear_sRGBCM(boolean premult) {
        return premult ? Linear_sRGB_Pre : Linear_sRGB_Unpre;
    }

    public static BufferedImage makeLinearBufferedImage(int width, int height, boolean premult) {
        ColorModel cm = makeLinear_sRGBCM(premult);
        WritableRaster wr = cm.createCompatibleWritableRaster(width, height);
        return new BufferedImage(cm, wr, premult, (Hashtable<?, ?>) null);
    }

    public static void copyData_INT_PACK(Raster src, WritableRaster dst) {
        int x0 = dst.getMinX();
        if (x0 < src.getMinX()) {
            x0 = src.getMinX();
        }
        int y0 = dst.getMinY();
        if (y0 < src.getMinY()) {
            y0 = src.getMinY();
        }
        int x1 = (dst.getMinX() + dst.getWidth()) - 1;
        if (x1 > (src.getMinX() + src.getWidth()) - 1) {
            x1 = (src.getMinX() + src.getWidth()) - 1;
        }
        int y1 = (dst.getMinY() + dst.getHeight()) - 1;
        if (y1 > (src.getMinY() + src.getHeight()) - 1) {
            y1 = (src.getMinY() + src.getHeight()) - 1;
        }
        int width = (x1 - x0) + 1;
        int height = (y1 - y0) + 1;
        SinglePixelPackedSampleModel srcSPPSM = (SinglePixelPackedSampleModel) src.getSampleModel();
        int srcScanStride = srcSPPSM.getScanlineStride();
        DataBufferInt srcDB = (DataBufferInt) src.getDataBuffer();
        int[] srcPixels = srcDB.getBankData()[0];
        int srcBase = srcDB.getOffset() + srcSPPSM.getOffset(x0 - src.getSampleModelTranslateX(), y0 - src.getSampleModelTranslateY());
        SinglePixelPackedSampleModel dstSPPSM = (SinglePixelPackedSampleModel) dst.getSampleModel();
        int dstScanStride = dstSPPSM.getScanlineStride();
        DataBufferInt dstDB = (DataBufferInt) dst.getDataBuffer();
        int[] dstPixels = dstDB.getBankData()[0];
        int dstBase = dstDB.getOffset() + dstSPPSM.getOffset(x0 - dst.getSampleModelTranslateX(), y0 - dst.getSampleModelTranslateY());
        if (srcScanStride == dstScanStride && srcScanStride == width) {
            System.arraycopy(srcPixels, srcBase, dstPixels, dstBase, width * height);
            return;
        }
        if (width > 128) {
            int srcSP = srcBase;
            int dstSP = dstBase;
            for (int y2 = 0; y2 < height; y2++) {
                System.arraycopy(srcPixels, srcSP, dstPixels, dstSP, width);
                srcSP += srcScanStride;
                dstSP += dstScanStride;
            }
            return;
        }
        for (int y3 = 0; y3 < height; y3++) {
            int srcSP2 = srcBase + (y3 * srcScanStride);
            int dstSP2 = dstBase + (y3 * dstScanStride);
            for (int x2 = 0; x2 < width; x2++) {
                int i2 = dstSP2;
                dstSP2++;
                int i3 = srcSP2;
                srcSP2++;
                dstPixels[i2] = srcPixels[i3];
            }
        }
    }

    public static void copyData_FALLBACK(Raster src, WritableRaster dst) {
        int x0 = dst.getMinX();
        if (x0 < src.getMinX()) {
            x0 = src.getMinX();
        }
        int y0 = dst.getMinY();
        if (y0 < src.getMinY()) {
            y0 = src.getMinY();
        }
        int x1 = (dst.getMinX() + dst.getWidth()) - 1;
        if (x1 > (src.getMinX() + src.getWidth()) - 1) {
            x1 = (src.getMinX() + src.getWidth()) - 1;
        }
        int y1 = (dst.getMinY() + dst.getHeight()) - 1;
        if (y1 > (src.getMinY() + src.getHeight()) - 1) {
            y1 = (src.getMinY() + src.getHeight()) - 1;
        }
        int width = (x1 - x0) + 1;
        int[] data = null;
        for (int y2 = y0; y2 <= y1; y2++) {
            data = src.getPixels(x0, y2, width, 1, data);
            dst.setPixels(x0, y2, width, 1, data);
        }
    }

    public static void copyData(Raster src, WritableRaster dst) {
        if (is_INT_PACK_Data(src.getSampleModel(), false) && is_INT_PACK_Data(dst.getSampleModel(), false)) {
            copyData_INT_PACK(src, dst);
        } else {
            copyData_FALLBACK(src, dst);
        }
    }

    public static WritableRaster copyRaster(Raster ras) {
        return copyRaster(ras, ras.getMinX(), ras.getMinY());
    }

    public static WritableRaster copyRaster(Raster ras, int minX, int minY) {
        WritableRaster ret = Raster.createWritableRaster(ras.getSampleModel(), new Point(0, 0)).createWritableChild(ras.getMinX() - ras.getSampleModelTranslateX(), ras.getMinY() - ras.getSampleModelTranslateY(), ras.getWidth(), ras.getHeight(), minX, minY, null);
        DataBuffer srcDB = ras.getDataBuffer();
        DataBuffer retDB = ret.getDataBuffer();
        if (srcDB.getDataType() != retDB.getDataType()) {
            throw new IllegalArgumentException("New DataBuffer doesn't match original");
        }
        int len = srcDB.getSize();
        int banks = srcDB.getNumBanks();
        int[] offsets = srcDB.getOffsets();
        for (int b2 = 0; b2 < banks; b2++) {
            switch (srcDB.getDataType()) {
                case 0:
                    DataBufferByte srcDBT = (DataBufferByte) srcDB;
                    DataBufferByte retDBT = (DataBufferByte) retDB;
                    System.arraycopy(srcDBT.getData(b2), offsets[b2], retDBT.getData(b2), offsets[b2], len);
                    break;
                case 1:
                    DataBufferUShort srcDBT2 = (DataBufferUShort) srcDB;
                    DataBufferUShort retDBT2 = (DataBufferUShort) retDB;
                    System.arraycopy(srcDBT2.getData(b2), offsets[b2], retDBT2.getData(b2), offsets[b2], len);
                    break;
                case 2:
                    DataBufferShort srcDBT3 = (DataBufferShort) srcDB;
                    DataBufferShort retDBT3 = (DataBufferShort) retDB;
                    System.arraycopy(srcDBT3.getData(b2), offsets[b2], retDBT3.getData(b2), offsets[b2], len);
                    break;
                case 3:
                    DataBufferInt srcDBT4 = (DataBufferInt) srcDB;
                    DataBufferInt retDBT4 = (DataBufferInt) retDB;
                    System.arraycopy(srcDBT4.getData(b2), offsets[b2], retDBT4.getData(b2), offsets[b2], len);
                    break;
            }
        }
        return ret;
    }

    public static WritableRaster makeRasterWritable(Raster ras) {
        return makeRasterWritable(ras, ras.getMinX(), ras.getMinY());
    }

    public static WritableRaster makeRasterWritable(Raster ras, int minX, int minY) {
        WritableRaster ret = Raster.createWritableRaster(ras.getSampleModel(), ras.getDataBuffer(), new Point(0, 0));
        return ret.createWritableChild(ras.getMinX() - ras.getSampleModelTranslateX(), ras.getMinY() - ras.getSampleModelTranslateY(), ras.getWidth(), ras.getHeight(), minX, minY, null);
    }

    public static ColorModel coerceColorModel(ColorModel cm, boolean newAlphaPreMult) {
        if (cm.isAlphaPremultiplied() == newAlphaPreMult) {
            return cm;
        }
        WritableRaster wr = cm.createCompatibleWritableRaster(1, 1);
        return cm.coerceData(wr, newAlphaPreMult);
    }

    public static ColorModel coerceData(WritableRaster wr, ColorModel cm, boolean newAlphaPreMult) {
        if (!cm.hasAlpha()) {
            return cm;
        }
        if (cm.isAlphaPremultiplied() == newAlphaPreMult) {
            return cm;
        }
        if (newAlphaPreMult) {
            multiplyAlpha(wr);
        } else {
            divideAlpha(wr);
        }
        return coerceColorModel(cm, newAlphaPreMult);
    }

    public static void multiplyAlpha(WritableRaster wr) {
        if (is_BYTE_COMP_Data(wr.getSampleModel())) {
            mult_BYTE_COMP_Data(wr);
            return;
        }
        if (is_INT_PACK_Data(wr.getSampleModel(), true)) {
            mult_INT_PACK_Data(wr);
            return;
        }
        int[] pixel = null;
        int bands = wr.getNumBands();
        int x0 = wr.getMinX();
        int x1 = x0 + wr.getWidth();
        int y0 = wr.getMinY();
        int y1 = y0 + wr.getHeight();
        for (int y2 = y0; y2 < y1; y2++) {
            for (int x2 = x0; x2 < x1; x2++) {
                pixel = wr.getPixel(x2, y2, pixel);
                int a2 = pixel[bands - 1];
                if (a2 >= 0 && a2 < 255) {
                    float alpha = a2 * 0.003921569f;
                    for (int b2 = 0; b2 < bands - 1; b2++) {
                        pixel[b2] = (int) ((pixel[b2] * alpha) + 0.5f);
                    }
                    wr.setPixel(x2, y2, pixel);
                }
            }
        }
    }

    public static void divideAlpha(WritableRaster wr) {
        if (is_BYTE_COMP_Data(wr.getSampleModel())) {
            divide_BYTE_COMP_Data(wr);
            return;
        }
        if (is_INT_PACK_Data(wr.getSampleModel(), true)) {
            divide_INT_PACK_Data(wr);
            return;
        }
        int bands = wr.getNumBands();
        int[] pixel = null;
        int x0 = wr.getMinX();
        int x1 = x0 + wr.getWidth();
        int y0 = wr.getMinY();
        int y1 = y0 + wr.getHeight();
        for (int y2 = y0; y2 < y1; y2++) {
            for (int x2 = x0; x2 < x1; x2++) {
                pixel = wr.getPixel(x2, y2, pixel);
                int a2 = pixel[bands - 1];
                if (a2 > 0 && a2 < 255) {
                    float ialpha = 255.0f / a2;
                    for (int b2 = 0; b2 < bands - 1; b2++) {
                        pixel[b2] = (int) ((pixel[b2] * ialpha) + 0.5f);
                    }
                    wr.setPixel(x2, y2, pixel);
                }
            }
        }
    }

    public static void copyData(BufferedImage src, BufferedImage dst) {
        Rectangle srcRect = new Rectangle(0, 0, src.getWidth(), src.getHeight());
        copyData(src, srcRect, dst, new Point(0, 0));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static void copyData(BufferedImage src, Rectangle srcRect, BufferedImage dst, Point destP) {
        boolean srcAlpha = src.getColorModel().hasAlpha();
        boolean dstAlpha = dst.getColorModel().hasAlpha();
        if (srcAlpha == dstAlpha && (!srcAlpha || src.isAlphaPremultiplied() == dst.isAlphaPremultiplied())) {
            copyData(src.getRaster(), dst.getRaster());
            return;
        }
        int[] pixel = null;
        Raster srcR = src.getRaster();
        WritableRaster dstR = dst.getRaster();
        int bands = dstR.getNumBands();
        int dx = destP.f12370x - srcRect.f12372x;
        int dy = destP.f12371y - srcRect.f12373y;
        int w2 = srcRect.width;
        int x0 = srcRect.f12372x;
        int y0 = srcRect.f12373y;
        int y1 = (y0 + srcRect.height) - 1;
        if (!srcAlpha) {
            int[] oPix = new int[bands * w2];
            int i2 = w2 * bands;
            int i3 = 1;
            while (true) {
                int out = i2 - i3;
                if (out < 0) {
                    break;
                }
                oPix[out] = 255;
                i2 = out;
                i3 = bands;
            }
            for (int y2 = y0; y2 <= y1; y2++) {
                pixel = srcR.getPixels(x0, y2, w2, 1, pixel);
                int in = (w2 * (bands - 1)) - 1;
                int out2 = (w2 * bands) - 2;
                switch (bands) {
                    case 4:
                        while (in >= 0) {
                            int i4 = out2;
                            int out3 = i4 - 1;
                            int i5 = in;
                            int in2 = i5 - 1;
                            oPix[i4] = pixel[i5];
                            int out4 = out3 - 1;
                            int in3 = in2 - 1;
                            oPix[out3] = pixel[in2];
                            in = in3 - 1;
                            oPix[out4] = pixel[in3];
                            out2 = (out4 - 1) - 1;
                        }
                        break;
                    default:
                        while (in >= 0) {
                            for (int b2 = 0; b2 < bands - 1; b2++) {
                                int i6 = out2;
                                out2 = i6 - 1;
                                int i7 = in;
                                in = i7 - 1;
                                oPix[i6] = pixel[i7];
                            }
                            out2--;
                        }
                        break;
                }
                dstR.setPixels(x0 + dx, y2 + dy, w2, 1, oPix);
            }
            return;
        }
        if (dstAlpha && dst.isAlphaPremultiplied()) {
            for (int y3 = y0; y3 <= y1; y3++) {
                pixel = srcR.getPixels(x0, y3, w2, 1, pixel);
                int in4 = (bands * w2) - 1;
                switch (bands) {
                    case 4:
                        while (in4 >= 0) {
                            int a2 = pixel[in4];
                            if (a2 == 255) {
                                in4 -= 4;
                            } else {
                                int in5 = in4 - 1;
                                int alpha = 65793 * a2;
                                pixel[in5] = ((pixel[in5] * alpha) + 8388608) >>> 24;
                                int in6 = in5 - 1;
                                pixel[in6] = ((pixel[in6] * alpha) + 8388608) >>> 24;
                                int in7 = in6 - 1;
                                pixel[in7] = ((pixel[in7] * alpha) + 8388608) >>> 24;
                                in4 = in7 - 1;
                            }
                        }
                        break;
                    default:
                        while (in4 >= 0) {
                            int a3 = pixel[in4];
                            if (a3 == 255) {
                                in4 -= bands;
                            } else {
                                in4--;
                                int alpha2 = 65793 * a3;
                                for (int b3 = 0; b3 < bands - 1; b3++) {
                                    pixel[in4] = ((pixel[in4] * alpha2) + 8388608) >>> 24;
                                    in4--;
                                }
                            }
                        }
                        break;
                }
                dstR.setPixels(x0 + dx, y3 + dy, w2, 1, pixel);
            }
            return;
        }
        if (dstAlpha && !dst.isAlphaPremultiplied()) {
            for (int y4 = y0; y4 <= y1; y4++) {
                pixel = srcR.getPixels(x0, y4, w2, 1, pixel);
                int in8 = (bands * w2) - 1;
                switch (bands) {
                    case 4:
                        while (in8 >= 0) {
                            int a4 = pixel[in8];
                            if (a4 <= 0 || a4 >= 255) {
                                in8 -= 4;
                            } else {
                                int in9 = in8 - 1;
                                int ialpha = 16711680 / a4;
                                pixel[in9] = ((pixel[in9] * ialpha) + 32768) >>> 16;
                                int in10 = in9 - 1;
                                pixel[in10] = ((pixel[in10] * ialpha) + 32768) >>> 16;
                                int in11 = in10 - 1;
                                pixel[in11] = ((pixel[in11] * ialpha) + 32768) >>> 16;
                                in8 = in11 - 1;
                            }
                        }
                        break;
                    default:
                        while (in8 >= 0) {
                            int a5 = pixel[in8];
                            if (a5 <= 0 || a5 >= 255) {
                                in8 -= bands;
                            } else {
                                in8--;
                                int ialpha2 = 16711680 / a5;
                                for (int b4 = 0; b4 < bands - 1; b4++) {
                                    pixel[in8] = ((pixel[in8] * ialpha2) + 32768) >>> 16;
                                    in8--;
                                }
                            }
                        }
                        break;
                }
                dstR.setPixels(x0 + dx, y4 + dy, w2, 1, pixel);
            }
            return;
        }
        if (src.isAlphaPremultiplied()) {
            int[] oPix2 = new int[bands * w2];
            for (int y5 = y0; y5 <= y1; y5++) {
                pixel = srcR.getPixels(x0, y5, w2, 1, pixel);
                int in12 = ((bands + 1) * w2) - 1;
                int out5 = (bands * w2) - 1;
                while (in12 >= 0) {
                    int a6 = pixel[in12];
                    in12--;
                    if (a6 > 0) {
                        if (a6 < 255) {
                            int ialpha3 = 16711680 / a6;
                            for (int b5 = 0; b5 < bands; b5++) {
                                int i8 = out5;
                                out5 = i8 - 1;
                                int i9 = in12;
                                in12 = i9 - 1;
                                oPix2[i8] = ((pixel[i9] * ialpha3) + 32768) >>> 16;
                            }
                        } else {
                            for (int b6 = 0; b6 < bands; b6++) {
                                int i10 = out5;
                                out5 = i10 - 1;
                                int i11 = in12;
                                in12 = i11 - 1;
                                oPix2[i10] = pixel[i11];
                            }
                        }
                    } else {
                        in12 -= bands;
                        for (int b7 = 0; b7 < bands; b7++) {
                            int i12 = out5;
                            out5 = i12 - 1;
                            oPix2[i12] = 255;
                        }
                    }
                }
                dstR.setPixels(x0 + dx, y5 + dy, w2, 1, oPix2);
            }
            return;
        }
        Rectangle dstRect = new Rectangle(destP.f12370x, destP.f12371y, srcRect.width, srcRect.height);
        for (int b8 = 0; b8 < bands; b8++) {
            copyBand(srcR, srcRect, b8, dstR, dstRect, b8);
        }
    }

    public static void copyBand(Raster src, int srcBand, WritableRaster dst, int dstBand) {
        Rectangle sR = src.getBounds();
        Rectangle dR = dst.getBounds();
        Rectangle cpR = sR.intersection(dR);
        copyBand(src, cpR, srcBand, dst, cpR, dstBand);
    }

    public static void copyBand(Raster src, Rectangle sR, int sBand, WritableRaster dst, Rectangle dR, int dBand) {
        int dy = dR.f12373y - sR.f12373y;
        int dx = dR.f12372x - sR.f12372x;
        Rectangle sR2 = sR.intersection(src.getBounds());
        Rectangle dR2 = dR.intersection(dst.getBounds());
        int width = dR2.width < sR2.width ? dR2.width : sR2.width;
        int height = dR2.height < sR2.height ? dR2.height : sR2.height;
        int x2 = sR2.f12372x + dx;
        int[] samples = null;
        for (int y2 = sR2.f12373y; y2 < sR2.f12373y + height; y2++) {
            samples = src.getSamples(sR2.f12372x, y2, width, 1, sBand, samples);
            dst.setSamples(x2, y2 + dy, width, 1, dBand, samples);
        }
    }

    public static boolean is_INT_PACK_Data(SampleModel sm, boolean requireAlpha) {
        if (!(sm instanceof SinglePixelPackedSampleModel) || sm.getDataType() != 3) {
            return false;
        }
        SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) sm;
        int[] masks = sppsm.getBitMasks();
        if (masks.length == 3) {
            if (requireAlpha) {
                return false;
            }
        } else if (masks.length != 4) {
            return false;
        }
        if (masks[0] != 16711680 || masks[1] != 65280 || masks[2] != 255) {
            return false;
        }
        if (masks.length == 4 && masks[3] != -16777216) {
            return false;
        }
        return true;
    }

    public static boolean is_BYTE_COMP_Data(SampleModel sm) {
        return (sm instanceof ComponentSampleModel) && sm.getDataType() == 0;
    }

    protected static void divide_INT_PACK_Data(WritableRaster wr) {
        SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) wr.getSampleModel();
        int width = wr.getWidth();
        int scanStride = sppsm.getScanlineStride();
        DataBufferInt db = (DataBufferInt) wr.getDataBuffer();
        int base = db.getOffset() + sppsm.getOffset(wr.getMinX() - wr.getSampleModelTranslateX(), wr.getMinY() - wr.getSampleModelTranslateY());
        int[] pixels = db.getBankData()[0];
        for (int y2 = 0; y2 < wr.getHeight(); y2++) {
            int sp = base + (y2 * scanStride);
            int end = sp + width;
            while (sp < end) {
                int pixel = pixels[sp];
                int a2 = pixel >>> 24;
                if (a2 <= 0) {
                    pixels[sp] = 16777215;
                } else if (a2 < 255) {
                    int aFP = 16711680 / a2;
                    pixels[sp] = (a2 << 24) | ((((pixel & 16711680) >> 16) * aFP) & 16711680) | (((((pixel & NormalizerImpl.CC_MASK) >> 8) * aFP) & 16711680) >> 8) | ((((pixel & 255) * aFP) & 16711680) >> 16);
                }
                sp++;
            }
        }
    }

    protected static void mult_INT_PACK_Data(WritableRaster wr) {
        SinglePixelPackedSampleModel sppsm = (SinglePixelPackedSampleModel) wr.getSampleModel();
        int width = wr.getWidth();
        int scanStride = sppsm.getScanlineStride();
        DataBufferInt db = (DataBufferInt) wr.getDataBuffer();
        int base = db.getOffset() + sppsm.getOffset(wr.getMinX() - wr.getSampleModelTranslateX(), wr.getMinY() - wr.getSampleModelTranslateY());
        int[] pixels = db.getBankData()[0];
        for (int y2 = 0; y2 < wr.getHeight(); y2++) {
            int sp = base + (y2 * scanStride);
            int end = sp + width;
            while (sp < end) {
                int pixel = pixels[sp];
                int a2 = pixel >>> 24;
                if (a2 >= 0 && a2 < 255) {
                    pixels[sp] = (a2 << 24) | ((((pixel & 16711680) * a2) >> 8) & 16711680) | ((((pixel & NormalizerImpl.CC_MASK) * a2) >> 8) & NormalizerImpl.CC_MASK) | ((((pixel & 255) * a2) >> 8) & 255);
                }
                sp++;
            }
        }
    }

    protected static void divide_BYTE_COMP_Data(WritableRaster wr) {
        ComponentSampleModel csm = (ComponentSampleModel) wr.getSampleModel();
        int width = wr.getWidth();
        int scanStride = csm.getScanlineStride();
        int pixStride = csm.getPixelStride();
        int[] bandOff = csm.getBandOffsets();
        DataBufferByte db = (DataBufferByte) wr.getDataBuffer();
        int base = db.getOffset() + csm.getOffset(wr.getMinX() - wr.getSampleModelTranslateX(), wr.getMinY() - wr.getSampleModelTranslateY());
        int aOff = bandOff[bandOff.length - 1];
        int bands = bandOff.length - 1;
        byte[] pixels = db.getBankData()[0];
        for (int y2 = 0; y2 < wr.getHeight(); y2++) {
            int sp = base + (y2 * scanStride);
            int end = sp + (width * pixStride);
            while (sp < end) {
                int a2 = pixels[sp + aOff] & 255;
                if (a2 == 0) {
                    for (int b2 = 0; b2 < bands; b2++) {
                        pixels[sp + bandOff[b2]] = -1;
                    }
                } else if (a2 < 255) {
                    int aFP = 16711680 / a2;
                    for (int b3 = 0; b3 < bands; b3++) {
                        int i2 = sp + bandOff[b3];
                        pixels[i2] = (byte) (((pixels[i2] & 255) * aFP) >>> 16);
                    }
                }
                sp += pixStride;
            }
        }
    }

    protected static void mult_BYTE_COMP_Data(WritableRaster wr) {
        ComponentSampleModel csm = (ComponentSampleModel) wr.getSampleModel();
        int width = wr.getWidth();
        int scanStride = csm.getScanlineStride();
        int pixStride = csm.getPixelStride();
        int[] bandOff = csm.getBandOffsets();
        DataBufferByte db = (DataBufferByte) wr.getDataBuffer();
        int base = db.getOffset() + csm.getOffset(wr.getMinX() - wr.getSampleModelTranslateX(), wr.getMinY() - wr.getSampleModelTranslateY());
        int aOff = bandOff[bandOff.length - 1];
        int bands = bandOff.length - 1;
        byte[] pixels = db.getBankData()[0];
        for (int y2 = 0; y2 < wr.getHeight(); y2++) {
            int sp = base + (y2 * scanStride);
            int end = sp + (width * pixStride);
            while (sp < end) {
                int a2 = pixels[sp + aOff] & 255;
                if (a2 != 255) {
                    for (int b2 = 0; b2 < bands; b2++) {
                        int i2 = sp + bandOff[b2];
                        pixels[i2] = (byte) (((pixels[i2] & 255) * a2) >> 8);
                    }
                }
                sp += pixStride;
            }
        }
    }
}
