package sun.awt;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.ComponentColorModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/awt/Win32ColorModel24.class */
public class Win32ColorModel24 extends ComponentColorModel {
    public Win32ColorModel24() {
        super(ColorSpace.getInstance(1000), new int[]{8, 8, 8}, false, false, 1, 0);
    }

    @Override // java.awt.image.ComponentColorModel, java.awt.image.ColorModel
    public WritableRaster createCompatibleWritableRaster(int i2, int i3) {
        return Raster.createInterleavedRaster(0, i2, i3, i2 * 3, 3, new int[]{2, 1, 0}, (Point) null);
    }

    @Override // java.awt.image.ComponentColorModel, java.awt.image.ColorModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        return new PixelInterleavedSampleModel(0, i2, i3, 3, i2 * 3, new int[]{2, 1, 0});
    }
}
