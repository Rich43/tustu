package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.beans.Transient;

/* loaded from: rt.jar:java/awt/geom/RectangularShape.class */
public abstract class RectangularShape implements Shape, Cloneable {
    public abstract double getX();

    public abstract double getY();

    public abstract double getWidth();

    public abstract double getHeight();

    public abstract boolean isEmpty();

    public abstract void setFrame(double d2, double d3, double d4, double d5);

    protected RectangularShape() {
    }

    public double getMinX() {
        return getX();
    }

    public double getMinY() {
        return getY();
    }

    public double getMaxX() {
        return getX() + getWidth();
    }

    public double getMaxY() {
        return getY() + getHeight();
    }

    public double getCenterX() {
        return getX() + (getWidth() / 2.0d);
    }

    public double getCenterY() {
        return getY() + (getHeight() / 2.0d);
    }

    @Transient
    public Rectangle2D getFrame() {
        return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
    }

    public void setFrame(Point2D point2D, Dimension2D dimension2D) {
        setFrame(point2D.getX(), point2D.getY(), dimension2D.getWidth(), dimension2D.getHeight());
    }

    public void setFrame(Rectangle2D rectangle2D) {
        setFrame(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public void setFrameFromDiagonal(double d2, double d3, double d4, double d5) {
        if (d4 < d2) {
            d2 = d4;
            d4 = d2;
        }
        if (d5 < d3) {
            d3 = d5;
            d5 = d3;
        }
        setFrame(d2, d3, d4 - d2, d5 - d3);
    }

    public void setFrameFromDiagonal(Point2D point2D, Point2D point2D2) {
        setFrameFromDiagonal(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY());
    }

    public void setFrameFromCenter(double d2, double d3, double d4, double d5) {
        double dAbs = Math.abs(d4 - d2);
        double dAbs2 = Math.abs(d5 - d3);
        setFrame(d2 - dAbs, d3 - dAbs2, dAbs * 2.0d, dAbs2 * 2.0d);
    }

    public void setFrameFromCenter(Point2D point2D, Point2D point2D2) {
        setFrameFromCenter(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY());
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        double width = getWidth();
        double height = getHeight();
        if (width < 0.0d || height < 0.0d) {
            return new Rectangle();
        }
        double x2 = getX();
        double y2 = getY();
        double dFloor = Math.floor(x2);
        double dFloor2 = Math.floor(y2);
        return new Rectangle((int) dFloor, (int) dFloor2, (int) (Math.ceil(x2 + width) - dFloor), (int) (Math.ceil(y2 + height) - dFloor2));
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new FlatteningPathIterator(getPathIterator(affineTransform), d2);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
