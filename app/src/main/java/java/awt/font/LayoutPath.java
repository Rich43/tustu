package java.awt.font;

import java.awt.geom.Point2D;

/* loaded from: rt.jar:java/awt/font/LayoutPath.class */
public abstract class LayoutPath {
    public abstract boolean pointToPath(Point2D point2D, Point2D point2D2);

    public abstract void pathToPoint(Point2D point2D, boolean z2, Point2D point2D2);
}
