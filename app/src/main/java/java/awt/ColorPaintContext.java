package java.awt;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import sun.awt.image.IntegerComponentRaster;

/* loaded from: rt.jar:java/awt/ColorPaintContext.class */
class ColorPaintContext implements PaintContext {
    int color;
    WritableRaster savedTile;

    protected ColorPaintContext(int i2, ColorModel colorModel) {
        this.color = i2;
    }

    @Override // java.awt.PaintContext
    public void dispose() {
    }

    int getRGB() {
        return this.color;
    }

    @Override // java.awt.PaintContext
    public ColorModel getColorModel() {
        return ColorModel.getRGBdefault();
    }

    @Override // java.awt.PaintContext
    public synchronized Raster getRaster(int i2, int i3, int i4, int i5) {
        WritableRaster writableRasterCreateCompatibleWritableRaster = this.savedTile;
        if (writableRasterCreateCompatibleWritableRaster == null || i4 > writableRasterCreateCompatibleWritableRaster.getWidth() || i5 > writableRasterCreateCompatibleWritableRaster.getHeight()) {
            writableRasterCreateCompatibleWritableRaster = getColorModel().createCompatibleWritableRaster(i4, i5);
            IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) writableRasterCreateCompatibleWritableRaster;
            Arrays.fill(integerComponentRaster.getDataStorage(), this.color);
            integerComponentRaster.markDirty();
            if (i4 <= 64 && i5 <= 64) {
                this.savedTile = writableRasterCreateCompatibleWritableRaster;
            }
        }
        return writableRasterCreateCompatibleWritableRaster;
    }
}
