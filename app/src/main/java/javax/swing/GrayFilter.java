package javax.swing;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

/* loaded from: rt.jar:javax/swing/GrayFilter.class */
public class GrayFilter extends RGBImageFilter {
    private boolean brighter;
    private int percent;

    public static Image createDisabledImage(Image image) {
        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), new GrayFilter(true, 50)));
    }

    public GrayFilter(boolean z2, int i2) {
        this.brighter = z2;
        this.percent = i2;
        this.canFilterIndexColorModel = true;
    }

    @Override // java.awt.image.RGBImageFilter
    public int filterRGB(int i2, int i3, int i4) {
        int i5;
        int i6 = (int) ((((0.3d * ((i4 >> 16) & 255)) + (0.59d * ((i4 >> 8) & 255))) + (0.11d * (i4 & 255))) / 3.0d);
        if (this.brighter) {
            i5 = 255 - (((255 - i6) * (100 - this.percent)) / 100);
        } else {
            i5 = (i6 * (100 - this.percent)) / 100;
        }
        if (i5 < 0) {
            i5 = 0;
        }
        if (i5 > 255) {
            i5 = 255;
        }
        return (i4 & (-16777216)) | (i5 << 16) | (i5 << 8) | (i5 << 0);
    }
}
