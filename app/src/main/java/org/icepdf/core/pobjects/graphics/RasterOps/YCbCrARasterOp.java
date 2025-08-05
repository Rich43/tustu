package org.icepdf.core.pobjects.graphics.RasterOps;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/RasterOps/YCbCrARasterOp.class */
public class YCbCrARasterOp implements RasterOp {
    private RenderingHints hints;

    public YCbCrARasterOp(RenderingHints hints) {
        this.hints = null;
        this.hints = hints;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = src.createCompatibleWritableRaster();
        }
        float[] origValues = new float[4];
        int[] rgbaValues = new int[4];
        int width = src.getWidth();
        int height = src.getHeight();
        for (int y2 = 0; y2 < height; y2++) {
            for (int x2 = 0; x2 < width; x2++) {
                src.getPixel(x2, y2, origValues);
                float Y2 = origValues[0];
                float Cb = origValues[1];
                float Cr = origValues[2];
                float K2 = origValues[3];
                float Y3 = K2 - Y2;
                float Cr_128 = Cr - 128.0f;
                float Cb_128 = Cb - 128.0f;
                float rVal = Y3 + ((1370705.0f * Cr_128) / 1000000.0f);
                float gVal = (Y3 - ((337633.0f * Cb_128) / 1000000.0f)) - ((698001.0f * Cr_128) / 1000000.0f);
                float bVal = Y3 + ((1732446.0f * Cb_128) / 1000000.0f);
                byte rByte = rVal < 0.0f ? (byte) 0 : rVal > 255.0f ? (byte) -1 : (byte) rVal;
                byte gByte = gVal < 0.0f ? (byte) 0 : gVal > 255.0f ? (byte) -1 : (byte) gVal;
                byte bByte = bVal < 0.0f ? (byte) 0 : bVal > 255.0f ? (byte) -1 : (byte) bVal;
                rgbaValues[0] = rByte;
                rgbaValues[1] = gByte;
                rgbaValues[2] = bByte;
                rgbaValues[3] = (int) K2;
                dest.setPixel(x2, y2, rgbaValues);
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
