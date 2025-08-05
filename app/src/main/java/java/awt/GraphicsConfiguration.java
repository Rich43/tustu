package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;
import java.util.Hashtable;
import sun.awt.image.SunVolatileImage;

/* loaded from: rt.jar:java/awt/GraphicsConfiguration.class */
public abstract class GraphicsConfiguration {
    private static BufferCapabilities defaultBufferCaps;
    private static ImageCapabilities defaultImageCaps;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract GraphicsDevice getDevice();

    public abstract ColorModel getColorModel();

    public abstract ColorModel getColorModel(int i2);

    public abstract AffineTransform getDefaultTransform();

    public abstract AffineTransform getNormalizingTransform();

    public abstract Rectangle getBounds();

    static {
        $assertionsDisabled = !GraphicsConfiguration.class.desiredAssertionStatus();
    }

    protected GraphicsConfiguration() {
    }

    public BufferedImage createCompatibleImage(int i2, int i3) {
        ColorModel colorModel = getColorModel();
        return new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(i2, i3), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    public BufferedImage createCompatibleImage(int i2, int i3, int i4) {
        if (getColorModel().getTransparency() == i4) {
            return createCompatibleImage(i2, i3);
        }
        ColorModel colorModel = getColorModel(i4);
        if (colorModel == null) {
            throw new IllegalArgumentException("Unknown transparency: " + i4);
        }
        return new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(i2, i3), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    public VolatileImage createCompatibleVolatileImage(int i2, int i3) {
        VolatileImage volatileImageCreateCompatibleVolatileImage = null;
        try {
            volatileImageCreateCompatibleVolatileImage = createCompatibleVolatileImage(i2, i3, null, 1);
        } catch (AWTException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        return volatileImageCreateCompatibleVolatileImage;
    }

    public VolatileImage createCompatibleVolatileImage(int i2, int i3, int i4) {
        VolatileImage volatileImageCreateCompatibleVolatileImage = null;
        try {
            volatileImageCreateCompatibleVolatileImage = createCompatibleVolatileImage(i2, i3, null, i4);
        } catch (AWTException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        return volatileImageCreateCompatibleVolatileImage;
    }

    public VolatileImage createCompatibleVolatileImage(int i2, int i3, ImageCapabilities imageCapabilities) throws AWTException {
        return createCompatibleVolatileImage(i2, i3, imageCapabilities, 1);
    }

    public VolatileImage createCompatibleVolatileImage(int i2, int i3, ImageCapabilities imageCapabilities, int i4) throws AWTException {
        SunVolatileImage sunVolatileImage = new SunVolatileImage(this, i2, i3, i4, imageCapabilities);
        if (imageCapabilities != null && imageCapabilities.isAccelerated() && !sunVolatileImage.getCapabilities().isAccelerated()) {
            throw new AWTException("Supplied image capabilities could not be met by this graphics configuration.");
        }
        return sunVolatileImage;
    }

    /* loaded from: rt.jar:java/awt/GraphicsConfiguration$DefaultBufferCapabilities.class */
    private static class DefaultBufferCapabilities extends BufferCapabilities {
        public DefaultBufferCapabilities(ImageCapabilities imageCapabilities) {
            super(imageCapabilities, imageCapabilities, null);
        }
    }

    public BufferCapabilities getBufferCapabilities() {
        if (defaultBufferCaps == null) {
            defaultBufferCaps = new DefaultBufferCapabilities(getImageCapabilities());
        }
        return defaultBufferCaps;
    }

    public ImageCapabilities getImageCapabilities() {
        if (defaultImageCaps == null) {
            defaultImageCaps = new ImageCapabilities(false);
        }
        return defaultImageCaps;
    }

    public boolean isTranslucencyCapable() {
        return false;
    }
}
