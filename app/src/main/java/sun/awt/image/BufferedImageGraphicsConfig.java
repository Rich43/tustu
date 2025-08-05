package sun.awt.image;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.util.Hashtable;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/image/BufferedImageGraphicsConfig.class */
public class BufferedImageGraphicsConfig extends GraphicsConfiguration {
    private static final int numconfigs = 12;
    private static BufferedImageGraphicsConfig[] configs = new BufferedImageGraphicsConfig[12];
    GraphicsDevice gd;
    ColorModel model;
    Raster raster;
    int width;
    int height;

    public static BufferedImageGraphicsConfig getConfig(BufferedImage bufferedImage) {
        BufferedImageGraphicsConfig bufferedImageGraphicsConfig;
        int type = bufferedImage.getType();
        if (type > 0 && type < 12 && (bufferedImageGraphicsConfig = configs[type]) != null) {
            return bufferedImageGraphicsConfig;
        }
        BufferedImageGraphicsConfig bufferedImageGraphicsConfig2 = new BufferedImageGraphicsConfig(bufferedImage, null);
        if (type > 0 && type < 12) {
            configs[type] = bufferedImageGraphicsConfig2;
        }
        return bufferedImageGraphicsConfig2;
    }

    public BufferedImageGraphicsConfig(BufferedImage bufferedImage, Component component) {
        if (component == null) {
            this.gd = new BufferedImageDevice(this);
        } else {
            this.gd = ((Graphics2D) component.getGraphics()).getDeviceConfiguration().getDevice();
        }
        this.model = bufferedImage.getColorModel();
        this.raster = bufferedImage.getRaster().createCompatibleWritableRaster(1, 1);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
    }

    @Override // java.awt.GraphicsConfiguration
    public GraphicsDevice getDevice() {
        return this.gd;
    }

    @Override // java.awt.GraphicsConfiguration
    public BufferedImage createCompatibleImage(int i2, int i3) {
        return new BufferedImage(this.model, this.raster.createCompatibleWritableRaster(i2, i3), this.model.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    @Override // java.awt.GraphicsConfiguration
    public ColorModel getColorModel() {
        return this.model;
    }

    @Override // java.awt.GraphicsConfiguration
    public ColorModel getColorModel(int i2) {
        if (this.model.getTransparency() == i2) {
            return this.model;
        }
        switch (i2) {
            case 1:
                return new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
            case 2:
                return new DirectColorModel(25, 16711680, NormalizerImpl.CC_MASK, 255, 16777216);
            case 3:
                return ColorModel.getRGBdefault();
            default:
                return null;
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public AffineTransform getDefaultTransform() {
        return new AffineTransform();
    }

    @Override // java.awt.GraphicsConfiguration
    public AffineTransform getNormalizingTransform() {
        return new AffineTransform();
    }

    @Override // java.awt.GraphicsConfiguration
    public Rectangle getBounds() {
        return new Rectangle(0, 0, this.width, this.height);
    }
}
