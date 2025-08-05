package java.awt;

import java.awt.geom.Rectangle2D;
import java.beans.Transient;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/Rectangle.class */
public class Rectangle extends Rectangle2D implements Shape, Serializable {

    /* renamed from: x, reason: collision with root package name */
    public int f12372x;

    /* renamed from: y, reason: collision with root package name */
    public int f12373y;
    public int width;
    public int height;
    private static final long serialVersionUID = -4345857070255674764L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(Rectangle rectangle) {
        this(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public Rectangle(int i2, int i3, int i4, int i5) {
        this.f12372x = i2;
        this.f12373y = i3;
        this.width = i4;
        this.height = i5;
    }

    public Rectangle(int i2, int i3) {
        this(0, 0, i2, i3);
    }

    public Rectangle(Point point, Dimension dimension) {
        this(point.f12370x, point.f12371y, dimension.width, dimension.height);
    }

    public Rectangle(Point point) {
        this(point.f12370x, point.f12371y, 0, 0);
    }

    public Rectangle(Dimension dimension) {
        this(0, 0, dimension.width, dimension.height);
    }

    @Override // java.awt.geom.RectangularShape
    public double getX() {
        return this.f12372x;
    }

    @Override // java.awt.geom.RectangularShape
    public double getY() {
        return this.f12373y;
    }

    @Override // java.awt.geom.RectangularShape
    public double getWidth() {
        return this.width;
    }

    @Override // java.awt.geom.RectangularShape
    public double getHeight() {
        return this.height;
    }

    @Override // java.awt.geom.RectangularShape, java.awt.Shape
    @Transient
    public Rectangle getBounds() {
        return new Rectangle(this.f12372x, this.f12373y, this.width, this.height);
    }

    @Override // java.awt.geom.Rectangle2D, java.awt.Shape
    public Rectangle2D getBounds2D() {
        return new Rectangle(this.f12372x, this.f12373y, this.width, this.height);
    }

    public void setBounds(Rectangle rectangle) {
        setBounds(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public void setBounds(int i2, int i3, int i4, int i5) {
        reshape(i2, i3, i4, i5);
    }

    @Override // java.awt.geom.Rectangle2D
    public void setRect(double d2, double d3, double d4, double d5) {
        int iClip;
        int iClip2;
        int iClip3;
        int iClip4;
        if (d2 > 4.294967294E9d) {
            iClip = Integer.MAX_VALUE;
            iClip2 = -1;
        } else {
            iClip = clip(d2, false);
            if (d4 >= 0.0d) {
                d4 += d2 - iClip;
            }
            iClip2 = clip(d4, d4 >= 0.0d);
        }
        if (d3 > 4.294967294E9d) {
            iClip3 = Integer.MAX_VALUE;
            iClip4 = -1;
        } else {
            iClip3 = clip(d3, false);
            if (d5 >= 0.0d) {
                d5 += d3 - iClip3;
            }
            iClip4 = clip(d5, d5 >= 0.0d);
        }
        reshape(iClip, iClip3, iClip2, iClip4);
    }

    private static int clip(double d2, boolean z2) {
        if (d2 <= -2.147483648E9d) {
            return Integer.MIN_VALUE;
        }
        if (d2 >= 2.147483647E9d) {
            return Integer.MAX_VALUE;
        }
        return (int) (z2 ? Math.ceil(d2) : Math.floor(d2));
    }

    @Deprecated
    public void reshape(int i2, int i3, int i4, int i5) {
        this.f12372x = i2;
        this.f12373y = i3;
        this.width = i4;
        this.height = i5;
    }

    public Point getLocation() {
        return new Point(this.f12372x, this.f12373y);
    }

    public void setLocation(Point point) {
        setLocation(point.f12370x, point.f12371y);
    }

    public void setLocation(int i2, int i3) {
        move(i2, i3);
    }

    @Deprecated
    public void move(int i2, int i3) {
        this.f12372x = i2;
        this.f12373y = i3;
    }

    public void translate(int i2, int i3) {
        int i4 = this.f12372x;
        int i5 = i4 + i2;
        if (i2 < 0) {
            if (i5 > i4) {
                if (this.width >= 0) {
                    this.width += i5 - Integer.MIN_VALUE;
                }
                i5 = Integer.MIN_VALUE;
            }
        } else if (i5 < i4) {
            if (this.width >= 0) {
                this.width += i5 - Integer.MAX_VALUE;
                if (this.width < 0) {
                    this.width = Integer.MAX_VALUE;
                }
            }
            i5 = Integer.MAX_VALUE;
        }
        this.f12372x = i5;
        int i6 = this.f12373y;
        int i7 = i6 + i3;
        if (i3 < 0) {
            if (i7 > i6) {
                if (this.height >= 0) {
                    this.height += i7 - Integer.MIN_VALUE;
                }
                i7 = Integer.MIN_VALUE;
            }
        } else if (i7 < i6) {
            if (this.height >= 0) {
                this.height += i7 - Integer.MAX_VALUE;
                if (this.height < 0) {
                    this.height = Integer.MAX_VALUE;
                }
            }
            i7 = Integer.MAX_VALUE;
        }
        this.f12373y = i7;
    }

    public Dimension getSize() {
        return new Dimension(this.width, this.height);
    }

    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int i2, int i3) {
        resize(i2, i3);
    }

    @Deprecated
    public void resize(int i2, int i3) {
        this.width = i2;
        this.height = i3;
    }

    public boolean contains(Point point) {
        return contains(point.f12370x, point.f12371y);
    }

    public boolean contains(int i2, int i3) {
        return inside(i2, i3);
    }

    public boolean contains(Rectangle rectangle) {
        return contains(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    public boolean contains(int i2, int i3, int i4, int i5) {
        int i6 = this.width;
        int i7 = this.height;
        if ((i6 | i7 | i4 | i5) < 0) {
            return false;
        }
        int i8 = this.f12372x;
        int i9 = this.f12373y;
        if (i2 < i8 || i3 < i9) {
            return false;
        }
        int i10 = i6 + i8;
        int i11 = i4 + i2;
        if (i11 <= i2) {
            if (i10 >= i8 || i11 > i10) {
                return false;
            }
        } else if (i10 >= i8 && i11 > i10) {
            return false;
        }
        int i12 = i7 + i9;
        int i13 = i5 + i3;
        return i13 <= i3 ? i12 < i9 && i13 <= i12 : i12 < i9 || i13 <= i12;
    }

    @Deprecated
    public boolean inside(int i2, int i3) {
        int i4 = this.width;
        int i5 = this.height;
        if ((i4 | i5) < 0) {
            return false;
        }
        int i6 = this.f12372x;
        int i7 = this.f12373y;
        if (i2 < i6 || i3 < i7) {
            return false;
        }
        int i8 = i4 + i6;
        int i9 = i5 + i7;
        return (i8 < i6 || i8 > i2) && (i9 < i7 || i9 > i3);
    }

    public boolean intersects(Rectangle rectangle) {
        int i2 = this.width;
        int i3 = this.height;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        if (i4 <= 0 || i5 <= 0 || i2 <= 0 || i3 <= 0) {
            return false;
        }
        int i6 = this.f12372x;
        int i7 = this.f12373y;
        int i8 = rectangle.f12372x;
        int i9 = rectangle.f12373y;
        int i10 = i4 + i8;
        int i11 = i5 + i9;
        int i12 = i2 + i6;
        int i13 = i3 + i7;
        return (i10 < i8 || i10 > i6) && (i11 < i9 || i11 > i7) && ((i12 < i6 || i12 > i8) && (i13 < i7 || i13 > i9));
    }

    public Rectangle intersection(Rectangle rectangle) {
        int i2 = this.f12372x;
        int i3 = this.f12373y;
        int i4 = rectangle.f12372x;
        int i5 = rectangle.f12373y;
        long j2 = i2 + this.width;
        long j3 = i3 + this.height;
        long j4 = i4 + rectangle.width;
        long j5 = i5 + rectangle.height;
        if (i2 < i4) {
            i2 = i4;
        }
        if (i3 < i5) {
            i3 = i5;
        }
        if (j2 > j4) {
            j2 = j4;
        }
        if (j3 > j5) {
            j3 = j5;
        }
        long j6 = j2 - i2;
        long j7 = j3 - i3;
        if (j6 < -2147483648L) {
            j6 = -2147483648L;
        }
        if (j7 < -2147483648L) {
            j7 = -2147483648L;
        }
        return new Rectangle(i2, i3, (int) j6, (int) j7);
    }

    public Rectangle union(Rectangle rectangle) {
        long j2 = this.width;
        long j3 = this.height;
        if ((j2 | j3) < 0) {
            return new Rectangle(rectangle);
        }
        long j4 = rectangle.width;
        long j5 = rectangle.height;
        if ((j4 | j5) < 0) {
            return new Rectangle(this);
        }
        int i2 = this.f12372x;
        int i3 = this.f12373y;
        long j6 = j2 + i2;
        long j7 = j3 + i3;
        int i4 = rectangle.f12372x;
        int i5 = rectangle.f12373y;
        long j8 = j4 + i4;
        long j9 = j5 + i5;
        if (i2 > i4) {
            i2 = i4;
        }
        if (i3 > i5) {
            i3 = i5;
        }
        if (j6 < j8) {
            j6 = j8;
        }
        if (j7 < j9) {
            j7 = j9;
        }
        long j10 = j6 - i2;
        long j11 = j7 - i3;
        if (j10 > 2147483647L) {
            j10 = 2147483647L;
        }
        if (j11 > 2147483647L) {
            j11 = 2147483647L;
        }
        return new Rectangle(i2, i3, (int) j10, (int) j11);
    }

    public void add(int i2, int i3) {
        if ((this.width | this.height) < 0) {
            this.f12372x = i2;
            this.f12373y = i3;
            this.height = 0;
            this.width = 0;
            return;
        }
        int i4 = this.f12372x;
        int i5 = this.f12373y;
        long j2 = this.width + i4;
        long j3 = this.height + i5;
        if (i4 > i2) {
            i4 = i2;
        }
        if (i5 > i3) {
            i5 = i3;
        }
        if (j2 < i2) {
            j2 = i2;
        }
        if (j3 < i3) {
            j3 = i3;
        }
        long j4 = j2 - i4;
        long j5 = j3 - i5;
        if (j4 > 2147483647L) {
            j4 = 2147483647L;
        }
        if (j5 > 2147483647L) {
            j5 = 2147483647L;
        }
        reshape(i4, i5, (int) j4, (int) j5);
    }

    public void add(Point point) {
        add(point.f12370x, point.f12371y);
    }

    public void add(Rectangle rectangle) {
        long j2 = this.width;
        long j3 = this.height;
        if ((j2 | j3) < 0) {
            reshape(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        }
        long j4 = rectangle.width;
        long j5 = rectangle.height;
        if ((j4 | j5) < 0) {
            return;
        }
        int i2 = this.f12372x;
        int i3 = this.f12373y;
        long j6 = j2 + i2;
        long j7 = j3 + i3;
        int i4 = rectangle.f12372x;
        int i5 = rectangle.f12373y;
        long j8 = j4 + i4;
        long j9 = j5 + i5;
        if (i2 > i4) {
            i2 = i4;
        }
        if (i3 > i5) {
            i3 = i5;
        }
        if (j6 < j8) {
            j6 = j8;
        }
        if (j7 < j9) {
            j7 = j9;
        }
        long j10 = j6 - i2;
        long j11 = j7 - i3;
        if (j10 > 2147483647L) {
            j10 = 2147483647L;
        }
        if (j11 > 2147483647L) {
            j11 = 2147483647L;
        }
        reshape(i2, i3, (int) j10, (int) j11);
    }

    public void grow(int i2, int i3) {
        long j2;
        long j3;
        long j4 = this.f12372x;
        long j5 = this.f12373y;
        long j6 = this.width + j4;
        long j7 = this.height + j5;
        long j8 = j4 - i2;
        long j9 = j5 - i3;
        long j10 = j6 + i2;
        long j11 = j7 + i3;
        if (j10 < j8) {
            j2 = j10 - j8;
            if (j2 < -2147483648L) {
                j2 = -2147483648L;
            }
            if (j8 < -2147483648L) {
                j8 = -2147483648L;
            } else if (j8 > 2147483647L) {
                j8 = 2147483647L;
            }
        } else {
            if (j8 < -2147483648L) {
                j8 = -2147483648L;
            } else if (j8 > 2147483647L) {
                j8 = 2147483647L;
            }
            j2 = j10 - j8;
            if (j2 < -2147483648L) {
                j2 = -2147483648L;
            } else if (j2 > 2147483647L) {
                j2 = 2147483647L;
            }
        }
        if (j11 < j9) {
            j3 = j11 - j9;
            if (j3 < -2147483648L) {
                j3 = -2147483648L;
            }
            if (j9 < -2147483648L) {
                j9 = -2147483648L;
            } else if (j9 > 2147483647L) {
                j9 = 2147483647L;
            }
        } else {
            if (j9 < -2147483648L) {
                j9 = -2147483648L;
            } else if (j9 > 2147483647L) {
                j9 = 2147483647L;
            }
            j3 = j11 - j9;
            if (j3 < -2147483648L) {
                j3 = -2147483648L;
            } else if (j3 > 2147483647L) {
                j3 = 2147483647L;
            }
        }
        reshape((int) j8, (int) j9, (int) j2, (int) j3);
    }

    @Override // java.awt.geom.RectangularShape
    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    @Override // java.awt.geom.Rectangle2D
    public int outcode(double d2, double d3) {
        int i2 = 0;
        if (this.width <= 0) {
            i2 = 0 | 5;
        } else if (d2 < this.f12372x) {
            i2 = 0 | 1;
        } else if (d2 > this.f12372x + this.width) {
            i2 = 0 | 4;
        }
        if (this.height <= 0) {
            i2 |= 10;
        } else if (d3 < this.f12373y) {
            i2 |= 2;
        } else if (d3 > this.f12373y + this.height) {
            i2 |= 8;
        }
        return i2;
    }

    @Override // java.awt.geom.Rectangle2D
    public Rectangle2D createIntersection(Rectangle2D rectangle2D) {
        if (rectangle2D instanceof Rectangle) {
            return intersection((Rectangle) rectangle2D);
        }
        Rectangle2D.Double r0 = new Rectangle2D.Double();
        Rectangle2D.intersect(this, rectangle2D, r0);
        return r0;
    }

    @Override // java.awt.geom.Rectangle2D
    public Rectangle2D createUnion(Rectangle2D rectangle2D) {
        if (rectangle2D instanceof Rectangle) {
            return union((Rectangle) rectangle2D);
        }
        Rectangle2D.Double r0 = new Rectangle2D.Double();
        Rectangle2D.union(this, rectangle2D, r0);
        return r0;
    }

    @Override // java.awt.geom.Rectangle2D
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) obj;
            return this.f12372x == rectangle.f12372x && this.f12373y == rectangle.f12373y && this.width == rectangle.width && this.height == rectangle.height;
        }
        return super.equals(obj);
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.f12372x + ",y=" + this.f12373y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}
