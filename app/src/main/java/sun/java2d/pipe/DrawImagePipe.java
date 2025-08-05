package sun.java2d.pipe;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/DrawImagePipe.class */
public interface DrawImagePipe {
    boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, Color color, ImageObserver imageObserver);

    boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver);

    boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver);

    boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver);

    boolean transformImage(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, ImageObserver imageObserver);

    void transformImage(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3);
}
