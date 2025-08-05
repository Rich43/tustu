package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/GrayRasterOp.class */
public class GrayRasterOp implements RasterOp {
    private RenderingHints hints;
    private float[] decode;

    public GrayRasterOp(float[] decode, RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
        this.decode = decode;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        byte[] destPixels = ((DataBufferByte) dest.getDataBuffer()).getData();
        boolean defaultDecode = 0.0f == this.decode[0];
        int bands = src.getNumBands();
        int i2 = 0;
        while (true) {
            int pixel = i2;
            if (pixel < srcPixels.length) {
                int Y2 = srcPixels[pixel] & 255;
                int Y3 = defaultDecode ? 255 - Y2 : Y2;
                destPixels[pixel] = (byte) (Y3 < 0 ? 0 : Y3 > 255 ? -1 : (byte) Y3);
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
}
