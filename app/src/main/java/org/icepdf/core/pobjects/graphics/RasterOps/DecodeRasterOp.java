package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/DecodeRasterOp.class */
public class DecodeRasterOp implements RasterOp {
    private static final float NORMAL_DECODE_CEIL = 0.003921569f;
    private RenderingHints hints;
    private float[] decode;

    public DecodeRasterOp(float[] decode, RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
        this.decode = decode;
    }

    private static boolean isNormalDecode(float[] decode) {
        int max = decode.length;
        for (int i2 = 0; i2 < max; i2 += 2) {
            if (decode[i2] != 0.0f || decode[i2 + 1] != NORMAL_DECODE_CEIL) {
                return false;
            }
        }
        return true;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (isNormalDecode(this.decode)) {
            return (WritableRaster) src;
        }
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        byte[] srcPixels = ((DataBufferByte) src.getDataBuffer()).getData();
        byte[] destPixels = ((DataBufferByte) dest.getDataBuffer()).getData();
        int bands = src.getNumBands();
        int i2 = 0;
        while (true) {
            int pixel = i2;
            if (pixel < srcPixels.length) {
                for (int i3 = 0; i3 < bands; i3++) {
                    destPixels[pixel + i3] = normalizeComponents(srcPixels[pixel + i3], this.decode, i3);
                }
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

    private static byte normalizeComponents(byte pixels, float[] decode, int i2) {
        return (byte) ((decode[i2 * 2] * 255.0f) + ((pixels & 255) * decode[(i2 * 2) + 1] * 255.0f));
    }
}
