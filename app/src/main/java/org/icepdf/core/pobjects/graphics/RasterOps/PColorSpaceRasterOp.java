package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;
import org.icepdf.core.pobjects.graphics.DeviceRGB;
import org.icepdf.core.pobjects.graphics.PColorSpace;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/PColorSpaceRasterOp.class */
public class PColorSpaceRasterOp implements RasterOp {
    private RenderingHints hints;
    private PColorSpace colorSpace;

    public PColorSpaceRasterOp(PColorSpace colorSpace, RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
        this.colorSpace = colorSpace;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        int[] destPixels = ((DataBufferInt) dest.getDataBuffer()).getData();
        if (this.colorSpace instanceof DeviceRGB) {
            int bands = src.getNumBands();
            int[] rgbValues = new int[3];
            int pixel = 0;
            int intPixels = 0;
            while (pixel < srcPixels.length) {
                rgbValues[0] = srcPixels[pixel] & 255;
                rgbValues[1] = srcPixels[pixel + 1] & 255;
                rgbValues[2] = srcPixels[pixel + 2] & 255;
                destPixels[intPixels] = ((rgbValues[0] & 255) << 16) | ((rgbValues[1] & 255) << 8) | (rgbValues[2] & 255);
                pixel += bands;
                intPixels++;
            }
        } else {
            int bands2 = src.getNumBands();
            float[] values = new float[3];
            int pixel2 = 0;
            int intPixels2 = 0;
            while (pixel2 < srcPixels.length) {
                for (int i2 = 0; i2 < bands2; i2++) {
                    values[i2] = (srcPixels[pixel2 + i2] & 255) / 255.0f;
                }
                PColorSpace.reverseInPlace(values);
                destPixels[intPixels2] = this.colorSpace.getColor(values).getRGB();
                pixel2 += bands2;
                intPixels2++;
            }
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
}
