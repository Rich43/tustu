package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/image/BufferedImageOp.class */
public interface BufferedImageOp {
    BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2);

    Rectangle2D getBounds2D(BufferedImage bufferedImage);

    BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel);

    Point2D getPoint2D(Point2D point2D, Point2D point2D2);

    RenderingHints getRenderingHints();
}
