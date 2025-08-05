package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/YCbCrRasterOp.class */
public class YCbCrRasterOp implements RasterOp {
    private RenderingHints hints;

    public YCbCrRasterOp(RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        int[] destPixels = ((DataBufferInt) dest.getDataBuffer()).getData();
        int lastY = -1;
        int lastCb = -1;
        int lastCr = -1;
        int rVal = 0;
        int gVal = 0;
        int bVal = 0;
        int bands = src.getNumBands();
        int pixel = 0;
        int intPixels = 0;
        while (pixel < srcPixels.length) {
            int Y2 = srcPixels[pixel] & 255;
            int Cb = srcPixels[pixel + 1] & 255;
            int Cr = srcPixels[pixel + 2] & 255;
            if (lastY != Y2 || lastCb != Cb || lastCr != Cr) {
                rVal = clamp((int) ((Y2 + (1.402d * Cr)) - 179.456d));
                gVal = clamp((int) (((Y2 - (0.34414d * Cb)) - (0.71414d * Cr)) + 135.45984d));
                bVal = clamp((int) ((Y2 + (1.772d * Cb)) - 226.816d));
            }
            lastY = Y2;
            lastCb = Cb;
            lastCr = Cr;
            destPixels[intPixels] = ((rVal & 255) << 16) | ((gVal & 255) << 8) | (bVal & 255);
            pixel += bands;
            intPixels++;
        }
        return dest;
    }

    private static int clamp(int x2) {
        if (x2 < 0) {
            return 0;
        }
        if (x2 > 255) {
            return 255;
        }
        return x2;
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
}
