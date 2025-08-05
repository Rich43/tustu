package java.awt.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Transparency;

/* loaded from: rt.jar:java/awt/image/VolatileImage.class */
public abstract class VolatileImage extends Image implements Transparency {
    public static final int IMAGE_OK = 0;
    public static final int IMAGE_RESTORED = 1;
    public static final int IMAGE_INCOMPATIBLE = 2;
    protected int transparency = 3;

    public abstract BufferedImage getSnapshot();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract Graphics2D createGraphics();

    public abstract int validate(GraphicsConfiguration graphicsConfiguration);

    public abstract boolean contentsLost();

    public abstract ImageCapabilities getCapabilities();

    @Override // java.awt.Image
    public ImageProducer getSource() {
        return getSnapshot().getSource();
    }

    @Override // java.awt.Image
    public Graphics getGraphics() {
        return createGraphics();
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return this.transparency;
    }
}
