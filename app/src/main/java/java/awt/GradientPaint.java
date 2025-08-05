package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:java/awt/GradientPaint.class */
public class GradientPaint implements Paint {
    Point2D.Float p1;
    Point2D.Float p2;
    Color color1;
    Color color2;
    boolean cyclic;

    public GradientPaint(float f2, float f3, Color color, float f4, float f5, Color color2) {
        if (color == null || color2 == null) {
            throw new NullPointerException("Colors cannot be null");
        }
        this.p1 = new Point2D.Float(f2, f3);
        this.p2 = new Point2D.Float(f4, f5);
        this.color1 = color;
        this.color2 = color2;
    }

    public GradientPaint(Point2D point2D, Color color, Point2D point2D2, Color color2) {
        if (color == null || color2 == null || point2D == null || point2D2 == null) {
            throw new NullPointerException("Colors and points should be non-null");
        }
        this.p1 = new Point2D.Float((float) point2D.getX(), (float) point2D.getY());
        this.p2 = new Point2D.Float((float) point2D2.getX(), (float) point2D2.getY());
        this.color1 = color;
        this.color2 = color2;
    }

    public GradientPaint(float f2, float f3, Color color, float f4, float f5, Color color2, boolean z2) {
        this(f2, f3, color, f4, f5, color2);
        this.cyclic = z2;
    }

    @ConstructorProperties({"point1", "color1", "point2", "color2", "cyclic"})
    public GradientPaint(Point2D point2D, Color color, Point2D point2D2, Color color2, boolean z2) {
        this(point2D, color, point2D2, color2);
        this.cyclic = z2;
    }

    public Point2D getPoint1() {
        return new Point2D.Float(this.p1.f12396x, this.p1.f12397y);
    }

    public Color getColor1() {
        return this.color1;
    }

    public Point2D getPoint2() {
        return new Point2D.Float(this.p2.f12396x, this.p2.f12397y);
    }

    public Color getColor2() {
        return this.color2;
    }

    public boolean isCyclic() {
        return this.cyclic;
    }

    @Override // java.awt.Paint
    public PaintContext createContext(ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints) {
        return new GradientPaintContext(colorModel, this.p1, this.p2, affineTransform, this.color1, this.color2, this.cyclic);
    }

    @Override // java.awt.Transparency
    public int getTransparency() {
        return (this.color1.getAlpha() & this.color2.getAlpha()) == 255 ? 1 : 3;
    }
}
