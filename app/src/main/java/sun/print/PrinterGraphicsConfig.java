package sun.print;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/print/PrinterGraphicsConfig.class */
public class PrinterGraphicsConfig extends GraphicsConfiguration {
    static ColorModel theModel;
    GraphicsDevice gd;
    int pageWidth;
    int pageHeight;
    AffineTransform deviceTransform;

    public PrinterGraphicsConfig(String str, AffineTransform affineTransform, int i2, int i3) {
        this.pageWidth = i2;
        this.pageHeight = i3;
        this.deviceTransform = affineTransform;
        this.gd = new PrinterGraphicsDevice(this, str);
    }

    @Override // java.awt.GraphicsConfiguration
    public GraphicsDevice getDevice() {
        return this.gd;
    }

    @Override // java.awt.GraphicsConfiguration
    public ColorModel getColorModel() {
        if (theModel == null) {
            theModel = new BufferedImage(1, 1, 5).getColorModel();
        }
        return theModel;
    }

    @Override // java.awt.GraphicsConfiguration
    public ColorModel getColorModel(int i2) {
        switch (i2) {
            case 1:
                return getColorModel();
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
        return new AffineTransform(this.deviceTransform);
    }

    @Override // java.awt.GraphicsConfiguration
    public AffineTransform getNormalizingTransform() {
        return new AffineTransform();
    }

    @Override // java.awt.GraphicsConfiguration
    public Rectangle getBounds() {
        return new Rectangle(0, 0, this.pageWidth, this.pageHeight);
    }
}
