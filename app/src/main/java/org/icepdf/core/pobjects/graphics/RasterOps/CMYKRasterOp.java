package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/CMYKRasterOp.class */
public class CMYKRasterOp implements RasterOp {
    private static float blackRatio;
    private RenderingHints hints;

    public CMYKRasterOp(RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
        blackRatio = Defs.intProperty("org.icepdf.core.cmyk.image.black", 255);
    }

    public static void setBlackRatio(float blackRatio2) {
        blackRatio = blackRatio2;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        int[] destPixels = ((DataBufferInt) dest.getDataBuffer()).getData();
        float lastCyan = -1.0f;
        float lastMagenta = -1.0f;
        float lastYellow = -1.0f;
        float lastBlack = -1.0f;
        int rValue = 0;
        int gValue = 0;
        int bValue = 0;
        int alpha = 0;
        int bands = src.getNumBands();
        int pixel = 0;
        int intPixels = 0;
        while (pixel < srcPixels.length) {
            float inCyan = (srcPixels[pixel] & 255) / 255.0f;
            float inMagenta = (srcPixels[pixel + 1] & 255) / 255.0f;
            float inYellow = (srcPixels[pixel + 2] & 255) / 255.0f;
            float inBlack = (srcPixels[pixel + 3] & 255) / blackRatio;
            if (inCyan != lastCyan || inMagenta != lastMagenta || inYellow != lastYellow || inBlack != lastBlack) {
                double c2 = clip(0.0d, 1.0d, inCyan + inBlack);
                double m2 = clip(0.0d, 1.0d, inMagenta + inBlack);
                double y2 = clip(0.0d, 1.0d, inYellow + inBlack);
                double aw2 = (1.0d - c2) * (1.0d - m2) * (1.0d - y2);
                double ac2 = c2 * (1.0d - m2) * (1.0d - y2);
                double am2 = (1.0d - c2) * m2 * (1.0d - y2);
                double ay2 = (1.0d - c2) * (1.0d - m2) * y2;
                double ar2 = (1.0d - c2) * m2 * y2;
                double ag2 = c2 * (1.0d - m2) * y2;
                double ab2 = c2 * m2 * (1.0d - y2);
                float outRed = (float) clip(0.0d, 1.0d, aw2 + (0.9137d * am2) + (0.9961d * ay2) + (0.9882d * ar2));
                float outGreen = (float) clip(0.0d, 1.0d, aw2 + (0.6196d * ac2) + ay2 + (0.5176d * ag2));
                float outBlue = (float) clip(0.0d, 1.0d, aw2 + (0.7804d * ac2) + (0.5412d * am2) + (0.0667d * ar2) + (0.2118d * ag2) + (0.4863d * ab2));
                rValue = (int) (outRed * 255.0f);
                gValue = (int) (outGreen * 255.0f);
                bValue = (int) (outBlue * 255.0f);
                alpha = 255;
            }
            lastCyan = inCyan;
            lastMagenta = inMagenta;
            lastYellow = inYellow;
            lastBlack = inBlack;
            destPixels[intPixels] = ((alpha & 255) << 24) | ((rValue & 255) << 16) | ((gValue & 255) << 8) | (bValue & 255);
            pixel += bands;
            intPixels++;
        }
        return dest;
    }

    @Override // java.awt.image.RasterOp
    public Rectangle2D getBounds2D(Raster src) {
        return null;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster src) {
        return src.createCompatibleWritableRaster();
    }

    @Override // java.awt.image.RasterOp
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = (Point2D) srcPt.clone();
        } else {
            dstPt.setLocation(srcPt);
        }
        return dstPt;
    }

    @Override // java.awt.image.RasterOp
    public RenderingHints getRenderingHints() {
        return this.hints;
    }

    private static double clip(double floor, double ceiling, double value) {
        if (value < floor) {
            value = floor;
        }
        if (value > ceiling) {
            value = ceiling;
        }
        return value;
    }
}
