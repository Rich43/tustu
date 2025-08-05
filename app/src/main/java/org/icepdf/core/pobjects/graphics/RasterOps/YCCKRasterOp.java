package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/YCCKRasterOp.class */
public class YCCKRasterOp implements RasterOp {
    private RenderingHints hints;

    public YCCKRasterOp(RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        byte[] destPixels = ((DataBufferByte) dest.getDataBuffer()).getData();
        double lastY = -1.0d;
        double lastCb = -1.0d;
        double lastCr = -1.0d;
        double lastK = -1.0d;
        int c2 = 0;
        int m2 = 0;
        int y2 = 0;
        int k2 = 0;
        int bands = src.getNumBands();
        int i2 = 0;
        while (true) {
            int pixel = i2;
            if (pixel < srcPixels.length) {
                double Y2 = srcPixels[pixel] & 255;
                double Cb = srcPixels[pixel + 1] & 255;
                double Cr = srcPixels[pixel + 2] & 255;
                double K2 = srcPixels[pixel + 3] & 255;
                if (lastY != Y2 || lastCb != Cb || lastCr != Cr || lastK != K2) {
                    int c3 = 255 - ((int) ((Y2 + (1.402d * Cr)) - 179.456d));
                    int m3 = 255 - ((int) (((Y2 - (0.34414d * Cb)) - (0.71413636d * Cr)) + 135.45984d));
                    int y22 = 255 - ((int) ((Y2 + (1.7718d * Cb)) - 226.816d));
                    k2 = (int) K2;
                    c2 = clip(0, 255, c3);
                    m2 = clip(0, 255, m3);
                    y2 = clip(0, 255, y22);
                }
                lastY = Y2;
                lastCb = Cb;
                lastCr = Cr;
                lastK = K2;
                destPixels[pixel] = (byte) (c2 & 255);
                destPixels[pixel + 1] = (byte) (m2 & 255);
                destPixels[pixel + 2] = (byte) (y2 & 255);
                destPixels[pixel + 3] = (byte) (k2 & 255);
                i2 = pixel + bands;
            } else {
                return dest;
            }
        }
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

    private static int clip(int floor, int ceiling, int value) {
        if (value < floor) {
            value = floor;
        }
        if (value > ceiling) {
            value = ceiling;
        }
        return value;
    }
}
