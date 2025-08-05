package sun.java2d.pipe.hw;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import sun.awt.image.SunVolatileImage;

/* loaded from: rt.jar:sun/java2d/pipe/hw/AccelTypedVolatileImage.class */
public class AccelTypedVolatileImage extends SunVolatileImage {
    public AccelTypedVolatileImage(GraphicsConfiguration graphicsConfiguration, int i2, int i3, int i4, int i5) {
        super(null, graphicsConfiguration, i2, i3, null, i4, null, i5);
    }

    @Override // sun.awt.image.SunVolatileImage, java.awt.image.VolatileImage
    public Graphics2D createGraphics() {
        if (getForcedAccelSurfaceType() == 3) {
            throw new UnsupportedOperationException("Can't render to a non-RT Texture");
        }
        return super.createGraphics();
    }
}
