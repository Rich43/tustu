package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Rectangle.class */
public class Rectangle {

    /* renamed from: x, reason: collision with root package name */
    public int f11913x;

    /* renamed from: y, reason: collision with root package name */
    public int f11914y;
    public int width;
    public int height;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(BaseBounds b2) {
        setBounds(b2);
    }

    public Rectangle(Rectangle r2) {
        this(r2.f11913x, r2.f11914y, r2.width, r2.height);
    }

    public Rectangle(int x2, int y2, int width, int height) {
        this.f11913x = x2;
        this.f11914y = y2;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }

    public void setBounds(Rectangle r2) {
        setBounds(r2.f11913x, r2.f11914y, r2.width, r2.height);
    }

    public void setBounds(int x2, int y2, int width, int height) {
        reshape(x2, y2, width, height);
    }

    public void setBounds(BaseBounds b2) {
        this.f11913x = (int) Math.floor(b2.getMinX());
        this.f11914y = (int) Math.floor(b2.getMinY());
        int x2 = (int) Math.ceil(b2.getMaxX());
        int y2 = (int) Math.ceil(b2.getMaxY());
        this.width = x2 - this.f11913x;
        this.height = y2 - this.f11914y;
    }

    public boolean contains(int cx, int cy) {
        int tw = this.width;
        int th = this.height;
        if ((tw | th) < 0) {
            return false;
        }
        int tx = this.f11913x;
        int ty = this.f11914y;
        if (cx < tx || cy < ty) {
            return false;
        }
        int tw2 = tw + tx;
        int th2 = th + ty;
        return (tw2 < tx || tw2 > cx) && (th2 < ty || th2 > cy);
    }

    public boolean contains(Rectangle r2) {
        return contains(r2.f11913x, r2.f11914y, r2.width, r2.height);
    }

    public boolean contains(int cx, int cy, int cw, int ch) {
        int tw = this.width;
        int th = this.height;
        if ((tw | th | cw | ch) < 0) {
            return false;
        }
        int tx = this.f11913x;
        int ty = this.f11914y;
        if (cx < tx || cy < ty) {
            return false;
        }
        int tw2 = tw + tx;
        int cw2 = cw + cx;
        if (cw2 <= cx) {
            if (tw2 >= tx || cw2 > tw2) {
                return false;
            }
        } else if (tw2 >= tx && cw2 > tw2) {
            return false;
        }
        int th2 = th + ty;
        int ch2 = ch + cy;
        return ch2 <= cy ? th2 < ty && ch2 <= th2 : th2 < ty || ch2 <= th2;
    }

    public Rectangle intersection(Rectangle r2) {
        Rectangle ret = new Rectangle(this);
        ret.intersectWith(r2);
        return ret;
    }

    public void intersectWith(Rectangle r2) {
        if (r2 == null) {
            return;
        }
        int tx1 = this.f11913x;
        int ty1 = this.f11914y;
        int rx1 = r2.f11913x;
        int ry1 = r2.f11914y;
        long tx2 = tx1 + this.width;
        long ty2 = ty1 + this.height;
        long rx2 = rx1 + r2.width;
        long ry2 = ry1 + r2.height;
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        long tx22 = tx2 - tx1;
        long ty22 = ty2 - ty1;
        if (tx22 < -2147483648L) {
            tx22 = -2147483648L;
        }
        if (ty22 < -2147483648L) {
            ty22 = -2147483648L;
        }
        setBounds(tx1, ty1, (int) tx22, (int) ty22);
    }

    public void translate(int dx, int dy) {
        int oldv = this.f11913x;
        int newv = oldv + dx;
        if (dx < 0) {
            if (newv > oldv) {
                if (this.width >= 0) {
                    this.width += newv - Integer.MIN_VALUE;
                }
                newv = Integer.MIN_VALUE;
            }
        } else if (newv < oldv) {
            if (this.width >= 0) {
                this.width += newv - Integer.MAX_VALUE;
                if (this.width < 0) {
                    this.width = Integer.MAX_VALUE;
                }
            }
            newv = Integer.MAX_VALUE;
        }
        this.f11913x = newv;
        int oldv2 = this.f11914y;
        int newv2 = oldv2 + dy;
        if (dy < 0) {
            if (newv2 > oldv2) {
                if (this.height >= 0) {
                    this.height += newv2 - Integer.MIN_VALUE;
                }
                newv2 = Integer.MIN_VALUE;
            }
        } else if (newv2 < oldv2) {
            if (this.height >= 0) {
                this.height += newv2 - Integer.MAX_VALUE;
                if (this.height < 0) {
                    this.height = Integer.MAX_VALUE;
                }
            }
            newv2 = Integer.MAX_VALUE;
        }
        this.f11914y = newv2;
    }

    public RectBounds toRectBounds() {
        return new RectBounds(this.f11913x, this.f11914y, this.f11913x + this.width, this.f11914y + this.height);
    }

    public void add(int newx, int newy) {
        if ((this.width | this.height) < 0) {
            this.f11913x = newx;
            this.f11914y = newy;
            this.height = 0;
            this.width = 0;
            return;
        }
        int x1 = this.f11913x;
        int y1 = this.f11914y;
        long x2 = this.width + x1;
        long y2 = this.height + y1;
        if (x1 > newx) {
            x1 = newx;
        }
        if (y1 > newy) {
            y1 = newy;
        }
        if (x2 < newx) {
            x2 = newx;
        }
        if (y2 < newy) {
            y2 = newy;
        }
        long x22 = x2 - x1;
        long y22 = y2 - y1;
        if (x22 > 2147483647L) {
            x22 = 2147483647L;
        }
        if (y22 > 2147483647L) {
            y22 = 2147483647L;
        }
        reshape(x1, y1, (int) x22, (int) y22);
    }

    public void add(Rectangle r2) {
        long tx2 = this.width;
        long ty2 = this.height;
        if ((tx2 | ty2) < 0) {
            reshape(r2.f11913x, r2.f11914y, r2.width, r2.height);
        }
        long rx2 = r2.width;
        long ry2 = r2.height;
        if ((rx2 | ry2) < 0) {
            return;
        }
        int tx1 = this.f11913x;
        int ty1 = this.f11914y;
        long tx22 = tx2 + tx1;
        long ty22 = ty2 + ty1;
        int rx1 = r2.f11913x;
        int ry1 = r2.f11914y;
        long rx22 = rx2 + rx1;
        long ry22 = ry2 + ry1;
        if (tx1 > rx1) {
            tx1 = rx1;
        }
        if (ty1 > ry1) {
            ty1 = ry1;
        }
        if (tx22 < rx22) {
            tx22 = rx22;
        }
        if (ty22 < ry22) {
            ty22 = ry22;
        }
        long tx23 = tx22 - tx1;
        long ty23 = ty22 - ty1;
        if (tx23 > 2147483647L) {
            tx23 = 2147483647L;
        }
        if (ty23 > 2147483647L) {
            ty23 = 2147483647L;
        }
        reshape(tx1, ty1, (int) tx23, (int) ty23);
    }

    public void grow(int h2, int v2) {
        long x1;
        long y1;
        long x0 = this.f11913x;
        long y0 = this.f11914y;
        long x12 = this.width;
        long y12 = this.height;
        long x13 = x12 + x0;
        long y13 = y12 + y0;
        long x02 = x0 - h2;
        long y02 = y0 - v2;
        long x14 = x13 + h2;
        long y14 = y13 + v2;
        if (x14 < x02) {
            x1 = x14 - x02;
            if (x1 < -2147483648L) {
                x1 = -2147483648L;
            }
            if (x02 < -2147483648L) {
                x02 = -2147483648L;
            } else if (x02 > 2147483647L) {
                x02 = 2147483647L;
            }
        } else {
            if (x02 < -2147483648L) {
                x02 = -2147483648L;
            } else if (x02 > 2147483647L) {
                x02 = 2147483647L;
            }
            x1 = x14 - x02;
            if (x1 < -2147483648L) {
                x1 = -2147483648L;
            } else if (x1 > 2147483647L) {
                x1 = 2147483647L;
            }
        }
        if (y14 < y02) {
            y1 = y14 - y02;
            if (y1 < -2147483648L) {
                y1 = -2147483648L;
            }
            if (y02 < -2147483648L) {
                y02 = -2147483648L;
            } else if (y02 > 2147483647L) {
                y02 = 2147483647L;
            }
        } else {
            if (y02 < -2147483648L) {
                y02 = -2147483648L;
            } else if (y02 > 2147483647L) {
                y02 = 2147483647L;
            }
            y1 = y14 - y02;
            if (y1 < -2147483648L) {
                y1 = -2147483648L;
            } else if (y1 > 2147483647L) {
                y1 = 2147483647L;
            }
        }
        reshape((int) x02, (int) y02, (int) x1, (int) y1);
    }

    private void reshape(int x2, int y2, int width, int height) {
        this.f11913x = x2;
        this.f11914y = y2;
        this.width = width;
        this.height = height;
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle r2 = (Rectangle) obj;
            return this.f11913x == r2.f11913x && this.f11914y == r2.f11914y && this.width == r2.width && this.height == r2.height;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.f11913x);
        return bits + (Float.floatToIntBits(this.f11914y) * 37) + (Float.floatToIntBits(this.width) * 43) + (Float.floatToIntBits(this.height) * 47);
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.f11913x + ",y=" + this.f11914y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}
