package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/Line2D.class */
public abstract class Line2D implements Shape, Cloneable {
    public abstract double getX1();

    public abstract double getY1();

    public abstract Point2D getP1();

    public abstract double getX2();

    public abstract double getY2();

    public abstract Point2D getP2();

    public abstract void setLine(double d2, double d3, double d4, double d5);

    /* loaded from: rt.jar:java/awt/geom/Line2D$Float.class */
    public static class Float extends Line2D implements Serializable {
        public float x1;
        public float y1;
        public float x2;
        public float y2;
        private static final long serialVersionUID = 6161772511649436349L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5) {
            setLine(f2, f3, f4, f5);
        }

        public Float(Point2D point2D, Point2D point2D2) {
            setLine(point2D, point2D2);
        }

        @Override // java.awt.geom.Line2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.Line2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.Line2D
        public Point2D getP1() {
            return new Point2D.Float(this.x1, this.y1);
        }

        @Override // java.awt.geom.Line2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.Line2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.Line2D
        public Point2D getP2() {
            return new Point2D.Float(this.x2, this.y2);
        }

        @Override // java.awt.geom.Line2D
        public void setLine(double d2, double d3, double d4, double d5) {
            this.x1 = (float) d2;
            this.y1 = (float) d3;
            this.x2 = (float) d4;
            this.y2 = (float) d5;
        }

        public void setLine(float f2, float f3, float f4, float f5) {
            this.x1 = f2;
            this.y1 = f3;
            this.x2 = f4;
            this.y2 = f5;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            float f2;
            float f3;
            float f4;
            float f5;
            if (this.x1 < this.x2) {
                f2 = this.x1;
                f3 = this.x2 - this.x1;
            } else {
                f2 = this.x2;
                f3 = this.x1 - this.x2;
            }
            if (this.y1 < this.y2) {
                f4 = this.y1;
                f5 = this.y2 - this.y1;
            } else {
                f4 = this.y2;
                f5 = this.y1 - this.y2;
            }
            return new Rectangle2D.Float(f2, f4, f3, f5);
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Line2D$Double.class */
    public static class Double extends Line2D implements Serializable {
        public double x1;
        public double y1;
        public double x2;
        public double y2;
        private static final long serialVersionUID = 7979627399746467499L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5) {
            setLine(d2, d3, d4, d5);
        }

        public Double(Point2D point2D, Point2D point2D2) {
            setLine(point2D, point2D2);
        }

        @Override // java.awt.geom.Line2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.Line2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.Line2D
        public Point2D getP1() {
            return new Point2D.Double(this.x1, this.y1);
        }

        @Override // java.awt.geom.Line2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.Line2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.Line2D
        public Point2D getP2() {
            return new Point2D.Double(this.x2, this.y2);
        }

        @Override // java.awt.geom.Line2D
        public void setLine(double d2, double d3, double d4, double d5) {
            this.x1 = d2;
            this.y1 = d3;
            this.x2 = d4;
            this.y2 = d5;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            double d2;
            double d3;
            double d4;
            double d5;
            if (this.x1 < this.x2) {
                d2 = this.x1;
                d3 = this.x2 - this.x1;
            } else {
                d2 = this.x2;
                d3 = this.x1 - this.x2;
            }
            if (this.y1 < this.y2) {
                d4 = this.y1;
                d5 = this.y2 - this.y1;
            } else {
                d4 = this.y2;
                d5 = this.y1 - this.y2;
            }
            return new Rectangle2D.Double(d2, d4, d3, d5);
        }
    }

    protected Line2D() {
    }

    public void setLine(Point2D point2D, Point2D point2D2) {
        setLine(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY());
    }

    public void setLine(Line2D line2D) {
        setLine(line2D.getX1(), line2D.getY1(), line2D.getX2(), line2D.getY2());
    }

    public static int relativeCCW(double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8 = d4 - d2;
        double d9 = d5 - d3;
        double d10 = d6 - d2;
        double d11 = d7 - d3;
        double d12 = (d10 * d9) - (d11 * d8);
        if (d12 == 0.0d) {
            d12 = (d10 * d8) + (d11 * d9);
            if (d12 > 0.0d) {
                d12 = ((d10 - d8) * d8) + ((d11 - d9) * d9);
                if (d12 < 0.0d) {
                    d12 = 0.0d;
                }
            }
        }
        if (d12 < 0.0d) {
            return -1;
        }
        return d12 > 0.0d ? 1 : 0;
    }

    public int relativeCCW(double d2, double d3) {
        return relativeCCW(getX1(), getY1(), getX2(), getY2(), d2, d3);
    }

    public int relativeCCW(Point2D point2D) {
        return relativeCCW(getX1(), getY1(), getX2(), getY2(), point2D.getX(), point2D.getY());
    }

    public static boolean linesIntersect(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        return relativeCCW(d2, d3, d4, d5, d6, d7) * relativeCCW(d2, d3, d4, d5, d8, d9) <= 0 && relativeCCW(d6, d7, d8, d9, d2, d3) * relativeCCW(d6, d7, d8, d9, d4, d5) <= 0;
    }

    public boolean intersectsLine(double d2, double d3, double d4, double d5) {
        return linesIntersect(d2, d3, d4, d5, getX1(), getY1(), getX2(), getY2());
    }

    public boolean intersectsLine(Line2D line2D) {
        return linesIntersect(line2D.getX1(), line2D.getY1(), line2D.getX2(), line2D.getY2(), getX1(), getY1(), getX2(), getY2());
    }

    public static double ptSegDistSq(double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8;
        double d9 = d4 - d2;
        double d10 = d5 - d3;
        double d11 = d6 - d2;
        double d12 = d7 - d3;
        if ((d11 * d9) + (d12 * d10) <= 0.0d) {
            d8 = 0.0d;
        } else {
            d11 = d9 - d11;
            d12 = d10 - d12;
            double d13 = (d11 * d9) + (d12 * d10);
            if (d13 <= 0.0d) {
                d8 = 0.0d;
            } else {
                d8 = (d13 * d13) / ((d9 * d9) + (d10 * d10));
            }
        }
        double d14 = ((d11 * d11) + (d12 * d12)) - d8;
        if (d14 < 0.0d) {
            d14 = 0.0d;
        }
        return d14;
    }

    public static double ptSegDist(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Math.sqrt(ptSegDistSq(d2, d3, d4, d5, d6, d7));
    }

    public double ptSegDistSq(double d2, double d3) {
        return ptSegDistSq(getX1(), getY1(), getX2(), getY2(), d2, d3);
    }

    public double ptSegDistSq(Point2D point2D) {
        return ptSegDistSq(getX1(), getY1(), getX2(), getY2(), point2D.getX(), point2D.getY());
    }

    public double ptSegDist(double d2, double d3) {
        return ptSegDist(getX1(), getY1(), getX2(), getY2(), d2, d3);
    }

    public double ptSegDist(Point2D point2D) {
        return ptSegDist(getX1(), getY1(), getX2(), getY2(), point2D.getX(), point2D.getY());
    }

    public static double ptLineDistSq(double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8 = d4 - d2;
        double d9 = d5 - d3;
        double d10 = d6 - d2;
        double d11 = d7 - d3;
        double d12 = (d10 * d8) + (d11 * d9);
        double d13 = ((d10 * d10) + (d11 * d11)) - ((d12 * d12) / ((d8 * d8) + (d9 * d9)));
        if (d13 < 0.0d) {
            d13 = 0.0d;
        }
        return d13;
    }

    public static double ptLineDist(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Math.sqrt(ptLineDistSq(d2, d3, d4, d5, d6, d7));
    }

    public double ptLineDistSq(double d2, double d3) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), d2, d3);
    }

    public double ptLineDistSq(Point2D point2D) {
        return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), point2D.getX(), point2D.getY());
    }

    public double ptLineDist(double d2, double d3) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(), d2, d3);
    }

    public double ptLineDist(Point2D point2D) {
        return ptLineDist(getX1(), getY1(), getX2(), getY2(), point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        return false;
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return false;
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        return intersects(new Rectangle2D.Double(d2, d3, d4, d5));
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return rectangle2D.intersectsLine(getX1(), getY1(), getX2(), getY2());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        return false;
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return false;
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new LineIterator(this, affineTransform);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new LineIterator(this, affineTransform);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
