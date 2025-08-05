package javax.swing;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/* loaded from: rt.jar:javax/swing/DebugGraphicsFilter.class */
class DebugGraphicsFilter extends RGBImageFilter {
    Color color;

    DebugGraphicsFilter(Color color) {
        this.canFilterIndexColorModel = true;
        this.color = color;
    }

    @Override // java.awt.image.RGBImageFilter
    public int filterRGB(int i2, int i3, int i4) {
        return this.color.getRGB() | (i4 & (-16777216));
    }
}
