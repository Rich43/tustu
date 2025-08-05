package java.awt;

import java.awt.image.ColorModel;
import java.awt.image.Raster;

/* loaded from: rt.jar:java/awt/PaintContext.class */
public interface PaintContext {
    void dispose();

    ColorModel getColorModel();

    Raster getRaster(int i2, int i3, int i4, int i5);
}
