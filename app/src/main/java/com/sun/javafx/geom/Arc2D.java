package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Arc2D.class */
public class Arc2D extends RectangularShape {
    public static final int OPEN = 0;
    public static final int CHORD = 1;
    public static final int PIE = 2;
    private int type;

    /* renamed from: x, reason: collision with root package name */
    public float f11893x;

    /* renamed from: y, reason: collision with root package name */
    public float f11894y;
    public float width;
    public float height;
    public float start;
    public float extent;

    public Arc2D() {
        this(0);
    }

    public Arc2D(int type) {
        setArcType(type);
    }

    public Arc2D(float x2, float y2, float w2, float h2, float start, float extent, int type) {
        this(type);
        this.f11893x = x2;
        this.f11894y = y2;
        this.width = w2;
        this.height = h2;
        this.start = start;
        this.extent = extent;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getX() {
        return this.f11893x;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getY() {
        return this.f11894y;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getWidth() {
        return this.width;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public float getHeight() {
        return this.height;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public boolean isEmpty() {
        return this.width <= 0.0f || this.height <= 0.0f;
    }

    public void setArc(float x2, float y2, float w2, float h2, float angSt, float angExt, int closure) {
        setArcType(closure);
        this.f11893x = x2;
        this.f11894y = y2;
        this.width = w2;
        this.height = h2;
        this.start = angSt;
        this.extent = angExt;
    }

    public int getArcType() {
        return this.type;
    }

    public Point2D getStartPoint() {
        double angle = Math.toRadians(-this.start);
        double x2 = this.f11893x + (((Math.cos(angle) * 0.5d) + 0.5d) * this.width);
        double y2 = this.f11894y + (((Math.sin(angle) * 0.5d) + 0.5d) * this.height);
        return new Point2D((float) x2, (float) y2);
    }

    public Point2D getEndPoint() {
        double angle = Math.toRadians((-this.start) - this.extent);
        double x2 = this.f11893x + (((Math.cos(angle) * 0.5d) + 0.5d) * this.width);
        double y2 = this.f11894y + (((Math.sin(angle) * 0.5d) + 0.5d) * this.height);
        return new Point2D((float) x2, (float) y2);
    }

    public void setArc(Point2D loc, Dimension2D size, float angSt, float angExt, int closure) {
        setArc(loc.f11907x, loc.f11908y, size.width, size.height, angSt, angExt, closure);
    }

    public void setArc(Arc2D a2) {
        setArc(a2.f11893x, a2.f11894y, a2.width, a2.height, a2.start, a2.extent, a2.type);
    }

    public void setArcByCenter(float x2, float y2, float radius, float angSt, float angExt, int closure) {
        setArc(x2 - radius, y2 - radius, radius * 2.0f, radius * 2.0f, angSt, angExt, closure);
    }

    public void setArcByTangent(Point2D p1, Point2D p2, Point2D p3, float radius) {
        double ang1;
        double ang2;
        double diff;
        double ang12 = Math.atan2(p1.f11908y - p2.f11908y, p1.f11907x - p2.f11907x);
        double ang22 = Math.atan2(p3.f11908y - p2.f11908y, p3.f11907x - p2.f11907x);
        double diff2 = ang22 - ang12;
        if (diff2 > 3.141592653589793d) {
            ang22 -= 6.283185307179586d;
        } else if (diff2 < -3.141592653589793d) {
            ang22 += 6.283185307179586d;
        }
        double bisect = (ang12 + ang22) / 2.0d;
        double theta = Math.abs(ang22 - bisect);
        double dist = radius / Math.sin(theta);
        double x2 = p2.f11907x + (dist * Math.cos(bisect));
        double y2 = p2.f11908y + (dist * Math.sin(bisect));
        if (ang12 < ang22) {
            ang1 = ang12 - 1.5707963267948966d;
            ang2 = ang22 + 1.5707963267948966d;
        } else {
            ang1 = ang12 + 1.5707963267948966d;
            ang2 = ang22 - 1.5707963267948966d;
        }
        double ang13 = Math.toDegrees(-ang1);
        double diff3 = Math.toDegrees(-ang2) - ang13;
        if (diff3 < 0.0d) {
            diff = diff3 + 360.0d;
        } else {
            diff = diff3 - 360.0d;
        }
        setArcByCenter((float) x2, (float) y2, radius, (float) ang13, (float) diff, this.type);
    }

    public void setAngleStart(Point2D p2) {
        double dx = this.height * (p2.f11907x - getCenterX());
        double dy = this.width * (p2.f11908y - getCenterY());
        this.start = (float) (-Math.toDegrees(Math.atan2(dy, dx)));
    }

    public void setAngles(float x1, float y1, float x2, float y2) {
        double x3 = getCenterX();
        double y3 = getCenterY();
        double w2 = this.width;
        double h2 = this.height;
        double ang1 = Math.atan2(w2 * (y3 - y1), h2 * (x1 - x3));
        double ang2 = Math.atan2(w2 * (y3 - y2), h2 * (x2 - x3)) - ang1;
        if (ang2 <= 0.0d) {
            ang2 += 6.283185307179586d;
        }
        this.start = (float) Math.toDegrees(ang1);
        this.extent = (float) Math.toDegrees(ang2);
    }

    public void setAngles(Point2D p1, Point2D p2) {
        setAngles(p1.f11907x, p1.f11908y, p2.f11907x, p2.f11908y);
    }

    public void setArcType(int type) {
        if (type < 0 || type > 2) {
            throw new IllegalArgumentException("invalid type for Arc: " + type);
        }
        this.type = type;
    }

    @Override // com.sun.javafx.geom.RectangularShape
    public void setFrame(float x2, float y2, float w2, float h2) {
        setArc(x2, y2, w2, h2, this.start, this.extent, this.type);
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException
        */
    /* JADX WARN: Not initialized variable reg: 4, insn: MOVE (r3 I:??) = (r4 I:??), block:B:8:0x0031 */
    /* JADX WARN: Not initialized variable reg: 4, insn: MOVE (r3 I:??) = (r4 I:??), block:B:9:0x003e */
    @Override // com.sun.javafx.geom.RectangularShape, com.sun.javafx.geom.Shape
    public com.sun.javafx.geom.RectBounds getBounds() {
        /*
            Method dump skipped, instructions count: 312
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.Arc2D.getBounds():com.sun.javafx.geom.RectBounds");
    }

    static float normalizeDegrees(double angle) {
        if (angle > 180.0d) {
            if (angle <= 540.0d) {
                angle -= 360.0d;
            } else {
                angle = Math.IEEEremainder(angle, 360.0d);
                if (angle == -180.0d) {
                    angle = 180.0d;
                }
            }
        } else if (angle <= -180.0d) {
            if (angle > -540.0d) {
                angle += 360.0d;
            } else {
                angle = Math.IEEEremainder(angle, 360.0d);
                if (angle == -180.0d) {
                    angle = 180.0d;
                }
            }
        }
        return (float) angle;
    }

    public boolean containsAngle(float angle) {
        double angExt = this.extent;
        boolean backwards = angExt < 0.0d;
        if (backwards) {
            angExt = -angExt;
        }
        if (angExt >= 360.0d) {
            return true;
        }
        float angle2 = normalizeDegrees(angle) - normalizeDegrees(this.start);
        if (backwards) {
            angle2 = -angle2;
        }
        if (angle2 < 0.0d) {
            angle2 = (float) (angle2 + 360.0d);
        }
        return ((double) angle2) >= 0.0d && ((double) angle2) < angExt;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        double ellw = this.width;
        if (ellw <= 0.0d) {
            return false;
        }
        double normx = ((x2 - this.f11893x) / ellw) - 0.5d;
        double ellh = this.height;
        if (ellh <= 0.0d) {
            return false;
        }
        double normy = ((y2 - this.f11894y) / ellh) - 0.5d;
        double distSq = (normx * normx) + (normy * normy);
        if (distSq >= 0.25d) {
            return false;
        }
        double angExt = Math.abs(this.extent);
        if (angExt >= 360.0d) {
            return true;
        }
        boolean inarc = containsAngle((float) (-Math.toDegrees(Math.atan2(normy, normx))));
        if (this.type == 2) {
            return inarc;
        }
        if (inarc) {
            if (angExt >= 180.0d) {
                return true;
            }
        } else if (angExt <= 180.0d) {
            return false;
        }
        double angle = Math.toRadians(-this.start);
        double x1 = Math.cos(angle);
        double y1 = Math.sin(angle);
        double angle2 = angle + Math.toRadians(-this.extent);
        double x22 = Math.cos(angle2);
        double y22 = Math.sin(angle2);
        boolean inside = Line2D.relativeCCW((float) x1, (float) y1, (float) x22, (float) y22, (float) (2.0d * normx), (float) (2.0d * normy)) * Line2D.relativeCCW((float) x1, (float) y1, (float) x22, (float) y22, 0.0f, 0.0f) >= 0;
        return inarc ? !inside : inside;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        float aw2 = this.width;
        float ah2 = this.height;
        if (w2 <= 0.0f || h2 <= 0.0f || aw2 <= 0.0f || ah2 <= 0.0f) {
            return false;
        }
        float ext = this.extent;
        if (ext == 0.0f) {
            return false;
        }
        float ax2 = this.f11893x;
        float ay2 = this.f11894y;
        float axw = ax2 + aw2;
        float ayh = ay2 + ah2;
        float xw = x2 + w2;
        float yh = y2 + h2;
        if (x2 >= axw || y2 >= ayh || xw <= ax2 || yh <= ay2) {
            return false;
        }
        float axc = getCenterX();
        float ayc = getCenterY();
        double sangle = Math.toRadians(-this.start);
        float sx = (float) (this.f11893x + (((Math.cos(sangle) * 0.5d) + 0.5d) * this.width));
        float sy = (float) (this.f11894y + (((Math.sin(sangle) * 0.5d) + 0.5d) * this.height));
        double eangle = Math.toRadians((-this.start) - this.extent);
        float ex = (float) (this.f11893x + (((Math.cos(eangle) * 0.5d) + 0.5d) * this.width));
        float ey = (float) (this.f11894y + (((Math.sin(eangle) * 0.5d) + 0.5d) * this.height));
        if (ayc >= y2 && ayc <= yh) {
            if (sx >= xw || ex >= xw || axc >= xw || axw <= x2 || !containsAngle(0.0f)) {
                if (sx > x2 && ex > x2 && axc > x2 && ax2 < xw && containsAngle(180.0f)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        if (axc >= x2 && axc <= xw) {
            if (sy <= y2 || ey <= y2 || ayc <= y2 || ay2 >= yh || !containsAngle(90.0f)) {
                if (sy < yh && ey < yh && ayc < yh && ayh > y2 && containsAngle(270.0f)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        if (this.type == 2 || Math.abs(ext) > 180.0f) {
            if (Shape.intersectsLine(x2, y2, w2, h2, axc, ayc, sx, sy) || Shape.intersectsLine(x2, y2, w2, h2, axc, ayc, ex, ey)) {
                return true;
            }
        } else if (Shape.intersectsLine(x2, y2, w2, h2, sx, sy, ex, ey)) {
            return true;
        }
        if (contains(x2, y2) || contains(x2 + w2, y2) || contains(x2, y2 + h2) || contains(x2 + w2, y2 + h2)) {
            return true;
        }
        return false;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        if (!contains(x2, y2) || !contains(x2 + w2, y2) || !contains(x2, y2 + h2) || !contains(x2 + w2, y2 + h2)) {
            return false;
        }
        if (this.type != 2 || Math.abs(this.extent) <= 180.0d) {
            return true;
        }
        float halfW = getWidth() / 2.0f;
        float halfH = getHeight() / 2.0f;
        float xc = x2 + halfW;
        float yc = y2 + halfH;
        float angle = (float) Math.toRadians(-this.start);
        float xe = (float) (xc + (halfW * Math.cos(angle)));
        float ye = (float) (yc + (halfH * Math.sin(angle)));
        if (Shape.intersectsLine(x2, y2, w2, h2, xc, yc, xe, ye)) {
            return false;
        }
        float angle2 = angle + ((float) Math.toRadians(-this.extent));
        float xe2 = (float) (xc + (halfW * Math.cos(angle2)));
        float ye2 = (float) (yc + (halfH * Math.sin(angle2)));
        return !Shape.intersectsLine(x2, y2, w2, h2, xc, yc, xe2, ye2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new ArcIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public Arc2D copy() {
        return new Arc2D(this.f11893x, this.f11894y, this.width, this.height, this.start, this.extent, this.type);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.f11893x);
        return bits + (Float.floatToIntBits(this.f11894y) * 37) + (Float.floatToIntBits(this.width) * 43) + (Float.floatToIntBits(this.height) * 47) + (Float.floatToIntBits(this.start) * 53) + (Float.floatToIntBits(this.extent) * 59) + (getArcType() * 61);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Arc2D) {
            Arc2D a2d = (Arc2D) obj;
            return this.f11893x == a2d.f11893x && this.f11894y == a2d.f11894y && this.width == a2d.width && this.height == a2d.height && this.start == a2d.start && this.extent == a2d.extent && this.type == a2d.type;
        }
        return false;
    }
}
