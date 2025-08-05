package java.awt.geom;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/Rectangle2D.class */
public abstract class Rectangle2D extends RectangularShape {
    public static final int OUT_LEFT = 1;
    public static final int OUT_TOP = 2;
    public static final int OUT_RIGHT = 4;
    public static final int OUT_BOTTOM = 8;

    public abstract void setRect(double d2, double d3, double d4, double d5);

    public abstract int outcode(double d2, double d3);

    public abstract Rectangle2D createIntersection(Rectangle2D rectangle2D);

    public abstract Rectangle2D createUnion(Rectangle2D rectangle2D);

    /* loaded from: rt.jar:java/awt/geom/Rectangle2D$Float.class */
    public static class Float extends Rectangle2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public float f12404x;

        /* renamed from: y, reason: collision with root package name */
        public float f12405y;
        public float width;
        public float height;
        private static final long serialVersionUID = 3798716824173675777L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5) {
            setRect(f2, f3, f4, f5);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12404x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12405y;
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
            return this.width <= 0.0f || this.height <= 0.0f;
        }

        public void setRect(float f2, float f3, float f4, float f5) {
            this.f12404x = f2;
            this.f12405y = f3;
            this.width = f4;
            this.height = f5;
        }

        @Override // java.awt.geom.Rectangle2D
        public void setRect(double d2, double d3, double d4, double d5) {
            this.f12404x = (float) d2;
            this.f12405y = (float) d3;
            this.width = (float) d4;
            this.height = (float) d5;
        }

        @Override // java.awt.geom.Rectangle2D
        public void setRect(Rectangle2D rectangle2D) {
            this.f12404x = (float) rectangle2D.getX();
            this.f12405y = (float) rectangle2D.getY();
            this.width = (float) rectangle2D.getWidth();
            this.height = (float) rectangle2D.getHeight();
        }

        @Override // java.awt.geom.Rectangle2D
        public int outcode(double d2, double d3) {
            int i2 = 0;
            if (this.width <= 0.0f) {
                i2 = 0 | 5;
            } else if (d2 < this.f12404x) {
                i2 = 0 | 1;
            } else if (d2 > this.f12404x + this.width) {
                i2 = 0 | 4;
            }
            if (this.height <= 0.0f) {
                i2 |= 10;
            } else if (d3 < this.f12405y) {
                i2 |= 2;
            } else if (d3 > this.f12405y + this.height) {
                i2 |= 8;
            }
            return i2;
        }

        @Override // java.awt.geom.Rectangle2D, java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Float(this.f12404x, this.f12405y, this.width, this.height);
        }

        @Override // java.awt.geom.Rectangle2D
        public Rectangle2D createIntersection(Rectangle2D rectangle2D) {
            Rectangle2D rectangle2D2;
            if (rectangle2D instanceof Float) {
                rectangle2D2 = new Float();
            } else {
                rectangle2D2 = new Double();
            }
            Rectangle2D.intersect(this, rectangle2D, rectangle2D2);
            return rectangle2D2;
        }

        @Override // java.awt.geom.Rectangle2D
        public Rectangle2D createUnion(Rectangle2D rectangle2D) {
            Rectangle2D rectangle2D2;
            if (rectangle2D instanceof Float) {
                rectangle2D2 = new Float();
            } else {
                rectangle2D2 = new Double();
            }
            Rectangle2D.union(this, rectangle2D, rectangle2D2);
            return rectangle2D2;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f12404x + ",y=" + this.f12405y + ",w=" + this.width + ",h=" + this.height + "]";
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Rectangle2D$Double.class */
    public static class Double extends Rectangle2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public double f12402x;

        /* renamed from: y, reason: collision with root package name */
        public double f12403y;
        public double width;
        public double height;
        private static final long serialVersionUID = 7771313791441850493L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5) {
            setRect(d2, d3, d4, d5);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12402x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12403y;
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

        @Override // java.awt.geom.Rectangle2D
        public void setRect(double d2, double d3, double d4, double d5) {
            this.f12402x = d2;
            this.f12403y = d3;
            this.width = d4;
            this.height = d5;
        }

        @Override // java.awt.geom.Rectangle2D
        public void setRect(Rectangle2D rectangle2D) {
            this.f12402x = rectangle2D.getX();
            this.f12403y = rectangle2D.getY();
            this.width = rectangle2D.getWidth();
            this.height = rectangle2D.getHeight();
        }

        @Override // java.awt.geom.Rectangle2D
        public int outcode(double d2, double d3) {
            int i2 = 0;
            if (this.width <= 0.0d) {
                i2 = 0 | 5;
            } else if (d2 < this.f12402x) {
                i2 = 0 | 1;
            } else if (d2 > this.f12402x + this.width) {
                i2 = 0 | 4;
            }
            if (this.height <= 0.0d) {
                i2 |= 10;
            } else if (d3 < this.f12403y) {
                i2 |= 2;
            } else if (d3 > this.f12403y + this.height) {
                i2 |= 8;
            }
            return i2;
        }

        @Override // java.awt.geom.Rectangle2D, java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Double(this.f12402x, this.f12403y, this.width, this.height);
        }

        @Override // java.awt.geom.Rectangle2D
        public Rectangle2D createIntersection(Rectangle2D rectangle2D) {
            Double r0 = new Double();
            Rectangle2D.intersect(this, rectangle2D, r0);
            return r0;
        }

        @Override // java.awt.geom.Rectangle2D
        public Rectangle2D createUnion(Rectangle2D rectangle2D) {
            Double r0 = new Double();
            Rectangle2D.union(this, rectangle2D, r0);
            return r0;
        }

        public String toString() {
            return getClass().getName() + "[x=" + this.f12402x + ",y=" + this.f12403y + ",w=" + this.width + ",h=" + this.height + "]";
        }
    }

    protected Rectangle2D() {
    }

    public void setRect(Rectangle2D rectangle2D) {
        setRect(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    public boolean intersectsLine(double d2, double d3, double d4, double d5) {
        int iOutcode = outcode(d4, d5);
        if (iOutcode == 0) {
            return true;
        }
        while (true) {
            int iOutcode2 = outcode(d2, d3);
            if (iOutcode2 != 0) {
                if ((iOutcode2 & iOutcode) != 0) {
                    return false;
                }
                if ((iOutcode2 & 5) != 0) {
                    double x2 = getX();
                    if ((iOutcode2 & 4) != 0) {
                        x2 += getWidth();
                    }
                    d3 += ((x2 - d2) * (d5 - d3)) / (d4 - d2);
                    d2 = x2;
                } else {
                    double y2 = getY();
                    if ((iOutcode2 & 8) != 0) {
                        y2 += getHeight();
                    }
                    d2 += ((y2 - d3) * (d4 - d2)) / (d5 - d3);
                    d3 = y2;
                }
            } else {
                return true;
            }
        }
    }

    public boolean intersectsLine(Line2D line2D) {
        return intersectsLine(line2D.getX1(), line2D.getY1(), line2D.getX2(), line2D.getY2());
    }

    public int outcode(Point2D point2D) {
        return outcode(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.geom.RectangularShape
    public void setFrame(double d2, double d3, double d4, double d5) {
        setRect(d2, d3, d4, d5);
    }

    @Override // java.awt.Shape
    public Rectangle2D getBounds2D() {
        return (Rectangle2D) clone();
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        double x2 = getX();
        double y2 = getY();
        return d2 >= x2 && d3 >= y2 && d2 < x2 + getWidth() && d3 < y2 + getHeight();
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        if (isEmpty() || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        double x2 = getX();
        double y2 = getY();
        return d2 + d4 > x2 && d3 + d5 > y2 && d2 < x2 + getWidth() && d3 < y2 + getHeight();
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        if (isEmpty() || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        double x2 = getX();
        double y2 = getY();
        return d2 >= x2 && d3 >= y2 && d2 + d4 <= x2 + getWidth() && d3 + d5 <= y2 + getHeight();
    }

    public static void intersect(Rectangle2D rectangle2D, Rectangle2D rectangle2D2, Rectangle2D rectangle2D3) {
        double dMax = Math.max(rectangle2D.getMinX(), rectangle2D2.getMinX());
        double dMax2 = Math.max(rectangle2D.getMinY(), rectangle2D2.getMinY());
        rectangle2D3.setFrame(dMax, dMax2, Math.min(rectangle2D.getMaxX(), rectangle2D2.getMaxX()) - dMax, Math.min(rectangle2D.getMaxY(), rectangle2D2.getMaxY()) - dMax2);
    }

    public static void union(Rectangle2D rectangle2D, Rectangle2D rectangle2D2, Rectangle2D rectangle2D3) {
        rectangle2D3.setFrameFromDiagonal(Math.min(rectangle2D.getMinX(), rectangle2D2.getMinX()), Math.min(rectangle2D.getMinY(), rectangle2D2.getMinY()), Math.max(rectangle2D.getMaxX(), rectangle2D2.getMaxX()), Math.max(rectangle2D.getMaxY(), rectangle2D2.getMaxY()));
    }

    public void add(double d2, double d3) {
        double dMin = Math.min(getMinX(), d2);
        double dMax = Math.max(getMaxX(), d2);
        double dMin2 = Math.min(getMinY(), d3);
        setRect(dMin, dMin2, dMax - dMin, Math.max(getMaxY(), d3) - dMin2);
    }

    public void add(Point2D point2D) {
        add(point2D.getX(), point2D.getY());
    }

    public void add(Rectangle2D rectangle2D) {
        double dMin = Math.min(getMinX(), rectangle2D.getMinX());
        double dMax = Math.max(getMaxX(), rectangle2D.getMaxX());
        double dMin2 = Math.min(getMinY(), rectangle2D.getMinY());
        setRect(dMin, dMin2, dMax - dMin, Math.max(getMaxY(), rectangle2D.getMaxY()) - dMin2);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new RectIterator(this, affineTransform);
    }

    @Override // java.awt.geom.RectangularShape, java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new RectIterator(this, affineTransform);
    }

    public int hashCode() {
        long jDoubleToLongBits = java.lang.Double.doubleToLongBits(getX()) + (java.lang.Double.doubleToLongBits(getY()) * 37) + (java.lang.Double.doubleToLongBits(getWidth()) * 43) + (java.lang.Double.doubleToLongBits(getHeight()) * 47);
        return ((int) jDoubleToLongBits) ^ ((int) (jDoubleToLongBits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Rectangle2D) {
            Rectangle2D rectangle2D = (Rectangle2D) obj;
            return getX() == rectangle2D.getX() && getY() == rectangle2D.getY() && getWidth() == rectangle2D.getWidth() && getHeight() == rectangle2D.getHeight();
        }
        return false;
    }
}
