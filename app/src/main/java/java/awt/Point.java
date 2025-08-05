package java.awt;

import java.awt.geom.Point2D;
import java.beans.Transient;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/Point.class */
public class Point extends Point2D implements Serializable {

    /* renamed from: x, reason: collision with root package name */
    public int f12370x;

    /* renamed from: y, reason: collision with root package name */
    public int f12371y;
    private static final long serialVersionUID = -5276940640259749850L;

    public Point() {
        this(0, 0);
    }

    public Point(Point point) {
        this(point.f12370x, point.f12371y);
    }

    public Point(int i2, int i3) {
        this.f12370x = i2;
        this.f12371y = i3;
    }

    @Override // java.awt.geom.Point2D
    public double getX() {
        return this.f12370x;
    }

    @Override // java.awt.geom.Point2D
    public double getY() {
        return this.f12371y;
    }

    @Transient
    public Point getLocation() {
        return new Point(this.f12370x, this.f12371y);
    }

    public void setLocation(Point point) {
        setLocation(point.f12370x, point.f12371y);
    }

    public void setLocation(int i2, int i3) {
        move(i2, i3);
    }

    @Override // java.awt.geom.Point2D
    public void setLocation(double d2, double d3) {
        this.f12370x = (int) Math.floor(d2 + 0.5d);
        this.f12371y = (int) Math.floor(d3 + 0.5d);
    }

    public void move(int i2, int i3) {
        this.f12370x = i2;
        this.f12371y = i3;
    }

    public void translate(int i2, int i3) {
        this.f12370x += i2;
        this.f12371y += i3;
    }

    @Override // java.awt.geom.Point2D
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point) obj;
            return this.f12370x == point.f12370x && this.f12371y == point.f12371y;
        }
        return super.equals(obj);
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.f12370x + ",y=" + this.f12371y + "]";
    }
}
