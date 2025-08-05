package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/Shape.class */
public interface Shape {
    Rectangle getBounds();

    Rectangle2D getBounds2D();

    boolean contains(double d2, double d3);

    boolean contains(Point2D point2D);

    boolean intersects(double d2, double d3, double d4, double d5);

    boolean intersects(Rectangle2D rectangle2D);

    boolean contains(double d2, double d3, double d4, double d5);

    boolean contains(Rectangle2D rectangle2D);

    PathIterator getPathIterator(AffineTransform affineTransform);

    PathIterator getPathIterator(AffineTransform affineTransform, double d2);
}
