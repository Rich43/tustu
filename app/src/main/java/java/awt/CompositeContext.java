package java.awt;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:java/awt/CompositeContext.class */
public interface CompositeContext {
    void dispose();

    void compose(Raster raster, Raster raster2, WritableRaster writableRaster);
}
