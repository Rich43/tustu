package java.awt.geom;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/RoundRectangle2D.class */
public abstract class RoundRectangle2D extends RectangularShape {
    public abstract double getArcWidth();

    public abstract double getArcHeight();

    public abstract void setRoundRect(double d2, double d3, double d4, double d5, double d6, double d7);

    /* loaded from: rt.jar:java/awt/geom/RoundRectangle2D$Float.class */
    public static class Float extends RoundRectangle2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public float f12417x;

        /* renamed from: y, reason: collision with root package name */
        public float f12418y;
        public float width;
        public float height;
        public float arcwidth;
        public float archeight;
        private static final long serialVersionUID = -3423150618393866922L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5, float f6, float f7) {
            setRoundRect(f2, f3, f4, f5, f6, f7);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12417x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12418y;
        }

        @Override // java.awt.geom.RectangularShape
        public double getWidth() {
            return this.width;
        }

        @Override // java.awt.geom.RectangularShape
        public double getHeight() {
            return this.height;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public double getArcWidth() {
            return this.arcwidth;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public double getArcHeight() {
            return this.archeight;
        }

        @Override // java.awt.geom.RectangularShape
        public boolean isEmpty() {
            return this.width <= 0.0f || this.height <= 0.0f;
        }

        public void setRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.f12417x = f2;
            this.f12418y = f3;
            this.width = f4;
            this.height = f5;
            this.arcwidth = f6;
            this.archeight = f7;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public void setRoundRect(double d2, double d3, double d4, double d5, double d6, double d7) {
            this.f12417x = (float) d2;
            this.f12418y = (float) d3;
            this.width = (float) d4;
            this.height = (float) d5;
            this.arcwidth = (float) d6;
            this.archeight = (float) d7;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public void setRoundRect(RoundRectangle2D roundRectangle2D) {
            this.f12417x = (float) roundRectangle2D.getX();
            this.f12418y = (float) roundRectangle2D.getY();
            this.width = (float) roundRectangle2D.getWidth();
            this.height = (float) roundRectangle2D.getHeight();
            this.arcwidth = (float) roundRectangle2D.getArcWidth();
            this.archeight = (float) roundRectangle2D.getArcHeight();
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Float(this.f12417x, this.f12418y, this.width, this.height);
        }
    }

    /* loaded from: rt.jar:java/awt/geom/RoundRectangle2D$Double.class */
    public static class Double extends RoundRectangle2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public double f12415x;

        /* renamed from: y, reason: collision with root package name */
        public double f12416y;
        public double width;
        public double height;
        public double arcwidth;
        public double archeight;
        private static final long serialVersionUID = 1048939333485206117L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5, double d6, double d7) {
            setRoundRect(d2, d3, d4, d5, d6, d7);
        }

        @Override // java.awt.geom.RectangularShape
        public double getX() {
            return this.f12415x;
        }

        @Override // java.awt.geom.RectangularShape
        public double getY() {
            return this.f12416y;
        }

        @Override // java.awt.geom.RectangularShape
        public double getWidth() {
            return this.width;
        }

        @Override // java.awt.geom.RectangularShape
        public double getHeight() {
            return this.height;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public double getArcWidth() {
            return this.arcwidth;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public double getArcHeight() {
            return this.archeight;
        }

        @Override // java.awt.geom.RectangularShape
        public boolean isEmpty() {
            return this.width <= 0.0d || this.height <= 0.0d;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public void setRoundRect(double d2, double d3, double d4, double d5, double d6, double d7) {
            this.f12415x = d2;
            this.f12416y = d3;
            this.width = d4;
            this.height = d5;
            this.arcwidth = d6;
            this.archeight = d7;
        }

        @Override // java.awt.geom.RoundRectangle2D
        public void setRoundRect(RoundRectangle2D roundRectangle2D) {
            this.f12415x = roundRectangle2D.getX();
            this.f12416y = roundRectangle2D.getY();
            this.width = roundRectangle2D.getWidth();
            this.height = roundRectangle2D.getHeight();
            this.arcwidth = roundRectangle2D.getArcWidth();
            this.archeight = roundRectangle2D.getArcHeight();
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            return new Rectangle2D.Double(this.f12415x, this.f12416y, this.width, this.height);
        }
    }

    protected RoundRectangle2D() {
    }

    public void setRoundRect(RoundRectangle2D roundRectangle2D) {
        setRoundRect(roundRectangle2D.getX(), roundRectangle2D.getY(), roundRectangle2D.getWidth(), roundRectangle2D.getHeight(), roundRectangle2D.getArcWidth(), roundRectangle2D.getArcHeight());
    }

    @Override // java.awt.geom.RectangularShape
    public void setFrame(double d2, double d3, double d4, double d5) {
        setRoundRect(d2, d3, d4, d5, getArcWidth(), getArcHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        if (isEmpty()) {
            return false;
        }
        double x2 = getX();
        double y2 = getY();
        double width = x2 + getWidth();
        double height = y2 + getHeight();
        if (d2 < x2 || d3 < y2 || d2 >= width || d3 >= height) {
            return false;
        }
        double dMin = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0d;
        double dMin2 = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0d;
        double d4 = d2;
        if (d2 >= x2 + dMin) {
            d4 = d2;
            if (d2 < width - dMin) {
                return true;
            }
        }
        double d5 = d3;
        if (d3 >= y2 + dMin2) {
            d5 = d3;
            if (d3 < height - dMin2) {
                return true;
            }
        }
        double d6 = (d2 - d4) / dMin;
        double d7 = (d3 - d5) / dMin2;
        return (d6 * d6) + (d7 * d7) <= 1.0d;
    }

    private int classify(double d2, double d3, double d4, double d5) {
        if (d2 < d3) {
            return 0;
        }
        if (d2 < d3 + d5) {
            return 1;
        }
        if (d2 < d4 - d5) {
            return 2;
        }
        if (d2 < d4) {
            return 3;
        }
        return 4;
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        if (isEmpty() || d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        double x2 = getX();
        double y2 = getY();
        double width = x2 + getWidth();
        double height = y2 + getHeight();
        if (d2 + d4 <= x2 || d2 >= width || d3 + d5 <= y2 || d3 >= height) {
            return false;
        }
        double dMin = Math.min(getWidth(), Math.abs(getArcWidth())) / 2.0d;
        double dMin2 = Math.min(getHeight(), Math.abs(getArcHeight())) / 2.0d;
        int iClassify = classify(d2, x2, width, dMin);
        int iClassify2 = classify(d2 + d4, x2, width, dMin);
        int iClassify3 = classify(d3, y2, height, dMin2);
        int iClassify4 = classify(d3 + d5, y2, height, dMin2);
        if (iClassify == 2 || iClassify2 == 2 || iClassify3 == 2 || iClassify4 == 2) {
            return true;
        }
        if (iClassify < 2 && iClassify2 > 2) {
            return true;
        }
        if (iClassify3 < 2 && iClassify4 > 2) {
            return true;
        }
        double d6 = iClassify2 == 1 ? (d2 + d4) - (x2 + dMin) : d2 - (width - dMin);
        double d7 = iClassify4 == 1 ? (d3 + d5) - (y2 + dMin2) : d3 - (height - dMin2);
        double d8 = d6 / dMin;
        double d9 = d7 / dMin2;
        return (d8 * d8) + (d9 * d9) <= 1.0d;
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        return !isEmpty() && d4 > 0.0d && d5 > 0.0d && contains(d2, d3) && contains(d2 + d4, d3) && contains(d2, d3 + d5) && contains(d2 + d4, d3 + d5);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new RoundRectIterator(this, affineTransform);
    }

    public int hashCode() {
        long jDoubleToLongBits = java.lang.Double.doubleToLongBits(getX()) + (java.lang.Double.doubleToLongBits(getY()) * 37) + (java.lang.Double.doubleToLongBits(getWidth()) * 43) + (java.lang.Double.doubleToLongBits(getHeight()) * 47) + (java.lang.Double.doubleToLongBits(getArcWidth()) * 53) + (java.lang.Double.doubleToLongBits(getArcHeight()) * 59);
        return ((int) jDoubleToLongBits) ^ ((int) (jDoubleToLongBits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof RoundRectangle2D) {
            RoundRectangle2D roundRectangle2D = (RoundRectangle2D) obj;
            return getX() == roundRectangle2D.getX() && getY() == roundRectangle2D.getY() && getWidth() == roundRectangle2D.getWidth() && getHeight() == roundRectangle2D.getHeight() && getArcWidth() == roundRectangle2D.getArcWidth() && getArcHeight() == roundRectangle2D.getArcHeight();
        }
        return false;
    }
}
