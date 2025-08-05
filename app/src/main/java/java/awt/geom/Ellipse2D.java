package java.awt.geom;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/Ellipse2D.class */
public abstract class Ellipse2D extends RectangularShape {

    /* loaded from: rt.jar:java/awt/geom/Ellipse2D$Float.class */
    public static class Float extends Ellipse2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public float f12388x;

        /* renamed from: y, reason: collision with root package name */
        public float f12389y;
        public float width;
        public float height;
        private static final long serialVersionUID = -6633761252372475977L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5) {
            setFrame(f2, f3, f4, f5);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12388x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12389y;
        }

        @Override // java.awt.geom.RectangularShape
        public double getWidth() {
            return this.width;
        }

        @Override // java.awt.geom.RectangularShape
        public double getHeight() {
            return this.height;
        }

        @Override // java.awt.geom.RectangularShape
        public boolean isEmpty() {
            return ((double) this.width) <= 0.0d || ((double) this.height) <= 0.0d;
        }

        public void setFrame(float f2, float f3, float f4, float f5) {
            this.f12388x = f2;
            this.f12389y = f3;
            this.width = f4;
            this.height = f5;
        }

        @Override // java.awt.geom.RectangularShape
        public void setFrame(double d2, double d3, double d4, double d5) {
            this.f12388x = (float) d2;
            this.f12389y = (float) d3;
            this.width = (float) d4;
            this.height = (float) d5;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Float(this.f12388x, this.f12389y, this.width, this.height);
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Ellipse2D$Double.class */
    public static class Double extends Ellipse2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public double f12386x;

        /* renamed from: y, reason: collision with root package name */
        public double f12387y;
        public double width;
        public double height;
        private static final long serialVersionUID = 5555464816372320683L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5) {
            setFrame(d2, d3, d4, d5);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12386x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12387y;
        }

        @Override // java.awt.geom.RectangularShape
        public double getWidth() {
            return this.width;
        }

        @Override // java.awt.geom.RectangularShape
        public double getHeight() {
            return this.height;
        }

        @Override // java.awt.geom.RectangularShape
        public boolean isEmpty() {
            return this.width <= 0.0d || this.height <= 0.0d;
        }

        @Override // java.awt.geom.RectangularShape
        public void setFrame(double d2, double d3, double d4, double d5) {
            this.f12386x = d2;
            this.f12387y = d3;
            this.width = d4;
            this.height = d5;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(this.f12386x, this.f12387y, this.width, this.height);
        }
    }

    protected Ellipse2D() {
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        double width = getWidth();
        if (width <= 0.0d) {
            return false;
        }
        double x2 = ((d2 - getX()) / width) - 0.5d;
        double height = getHeight();
        if (height <= 0.0d) {
            return false;
        }
        double y2 = ((d3 - getY()) / height) - 0.5d;
        return (x2 * x2) + (y2 * y2) < 0.25d;
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        double d6;
        double d7;
        if (d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        double width = getWidth();
        if (width <= 0.0d) {
            return false;
        }
        double x2 = ((d2 - getX()) / width) - 0.5d;
        double d8 = x2 + (d4 / width);
        double height = getHeight();
        if (height <= 0.0d) {
            return false;
        }
        double y2 = ((d3 - getY()) / height) - 0.5d;
        double d9 = y2 + (d5 / height);
        if (x2 > 0.0d) {
            d6 = x2;
        } else if (d8 < 0.0d) {
            d6 = d8;
        } else {
            d6 = 0.0d;
        }
        if (y2 > 0.0d) {
            d7 = y2;
        } else if (d9 < 0.0d) {
            d7 = d9;
        } else {
            d7 = 0.0d;
        }
        return (d6 * d6) + (d7 * d7) < 0.25d;
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        return contains(d2, d3) && contains(d2 + d4, d3) && contains(d2, d3 + d5) && contains(d2 + d4, d3 + d5);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new EllipseIterator(this, affineTransform);
    }

    public int hashCode() {
        long jDoubleToLongBits = java.lang.Double.doubleToLongBits(getX()) + (java.lang.Double.doubleToLongBits(getY()) * 37) + (java.lang.Double.doubleToLongBits(getWidth()) * 43) + (java.lang.Double.doubleToLongBits(getHeight()) * 47);
        return ((int) jDoubleToLongBits) ^ ((int) (jDoubleToLongBits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Ellipse2D) {
            Ellipse2D ellipse2D = (Ellipse2D) obj;
            return getX() == ellipse2D.getX() && getY() == ellipse2D.getY() && getWidth() == ellipse2D.getWidth() && getHeight() == ellipse2D.getHeight();
        }
        return false;
    }
}
