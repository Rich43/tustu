package java.awt.geom;

import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/Point2D.class */
public abstract class Point2D implements Cloneable {
    public abstract double getX();

    public abstract double getY();

    public abstract void setLocation(double d2, double d3);

    /* loaded from: rt.jar:java/awt/geom/Point2D$Float.class */
    public static class Float extends Point2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public float f12396x;

        /* renamed from: y, reason: collision with root package name */
        public float f12397y;
        private static final long serialVersionUID = -2870572449815403710L;

        public Float() {
        }

        public Float(float f2, float f3) {
            this.f12396x = f2;
            this.f12397y = f3;
        }

        @Override // java.awt.geom.Point2D
        public double getX() {
            return this.f12396x;
        }

        @Override // java.awt.geom.Point2D
        public double getY() {
            return this.f12397y;
        }

        @Override // java.awt.geom.Point2D
        public void setLocation(double d2, double d3) {
            this.f12396x = (float) d2;
            this.f12397y = (float) d3;
        }

        public void setLocation(float f2, float f3) {
            this.f12396x = f2;
            this.f12397y = f3;
        }

        public String toString() {
            return "Point2D.Float[" + this.f12396x + ", " + this.f12397y + "]";
        }
    }

    /* loaded from: rt.jar:java/awt/geom/Point2D$Double.class */
    public static class Double extends Point2D implements Serializable {

        /* renamed from: x, reason: collision with root package name */
        public double f12394x;

        /* renamed from: y, reason: collision with root package name */
        public double f12395y;
        private static final long serialVersionUID = 6150783262733311327L;

        public Double() {
        }

        public Double(double d2, double d3) {
            this.f12394x = d2;
            this.f12395y = d3;
        }

        @Override // java.awt.geom.Point2D
        public double getX() {
            return this.f12394x;
        }

        @Override // java.awt.geom.Point2D
        public double getY() {
            return this.f12395y;
        }

        @Override // java.awt.geom.Point2D
        public void setLocation(double d2, double d3) {
            this.f12394x = d2;
            this.f12395y = d3;
        }

        public String toString() {
            return "Point2D.Double[" + this.f12394x + ", " + this.f12395y + "]";
        }
    }

    protected Point2D() {
    }

    public void setLocation(Point2D point2D) {
        setLocation(point2D.getX(), point2D.getY());
    }

    public static double distanceSq(double d2, double d3, double d4, double d5) {
        double d6 = d2 - d4;
        double d7 = d3 - d5;
        return (d6 * d6) + (d7 * d7);
    }

    public static double distance(double d2, double d3, double d4, double d5) {
        double d6 = d2 - d4;
        double d7 = d3 - d5;
        return Math.sqrt((d6 * d6) + (d7 * d7));
    }

    public double distanceSq(double d2, double d3) {
        double x2 = d2 - getX();
        double y2 = d3 - getY();
        return (x2 * x2) + (y2 * y2);
    }

    public double distanceSq(Point2D point2D) {
        double x2 = point2D.getX() - getX();
        double y2 = point2D.getY() - getY();
        return (x2 * x2) + (y2 * y2);
    }

    public double distance(double d2, double d3) {
        double x2 = d2 - getX();
        double y2 = d3 - getY();
        return Math.sqrt((x2 * x2) + (y2 * y2));
    }

    public double distance(Point2D point2D) {
        double x2 = point2D.getX() - getX();
        double y2 = point2D.getY() - getY();
        return Math.sqrt((x2 * x2) + (y2 * y2));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public int hashCode() {
        long jDoubleToLongBits = java.lang.Double.doubleToLongBits(getX()) ^ (java.lang.Double.doubleToLongBits(getY()) * 31);
        return ((int) jDoubleToLongBits) ^ ((int) (jDoubleToLongBits >> 32));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Point2D) {
            Point2D point2D = (Point2D) obj;
            return getX() == point2D.getX() && getY() == point2D.getY();
        }
        return super.equals(obj);
    }
}
