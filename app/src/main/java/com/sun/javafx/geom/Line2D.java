package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Line2D.class */
public class Line2D extends Shape {
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public Line2D() {
    }

    public Line2D(float x1, float y1, float x2, float y2) {
        setLine(x1, y1, x2, y2);
    }

    public Line2D(Point2D p1, Point2D p2) {
        setLine(p1, p2);
    }

    public void setLine(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setLine(Point2D p1, Point2D p2) {
        setLine(p1.f11907x, p1.f11908y, p2.f11907x, p2.f11908y);
    }

    public void setLine(Line2D l2) {
        setLine(l2.x1, l2.y1, l2.x2, l2.y2);
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        RectBounds b2 = new RectBounds();
        b2.setBoundsAndSort(this.x1, this.y1, this.x2, this.y2);
        return b2;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        return false;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return false;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(Point2D p2) {
        return false;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        int out2 = outcode(x2, y2, w2, h2, this.x2, this.y2);
        if (out2 == 0) {
            return true;
        }
        float px = this.x1;
        float py = this.y1;
        while (true) {
            int out1 = outcode(x2, y2, w2, h2, px, py);
            if (out1 != 0) {
                if ((out1 & out2) != 0) {
                    return false;
                }
                if ((out1 & 5) != 0) {
                    px = x2;
                    if ((out1 & 4) != 0) {
                        px += w2;
                    }
                    py = this.y1 + (((px - this.x1) * (this.y2 - this.y1)) / (this.x2 - this.x1));
                } else {
                    py = y2;
                    if ((out1 & 8) != 0) {
                        py += h2;
                    }
                    px = this.x1 + (((py - this.y1) * (this.x2 - this.x1)) / (this.y2 - this.y1));
                }
            } else {
                return true;
            }
        }
    }

    public static int relativeCCW(float x1, float y1, float x2, float y2, float px, float py) {
        float x22 = x2 - x1;
        float y22 = y2 - y1;
        float px2 = px - x1;
        float py2 = py - y1;
        float ccw = (px2 * y22) - (py2 * x22);
        if (ccw == 0.0f) {
            ccw = (px2 * x22) + (py2 * y22);
            if (ccw > 0.0f) {
                ccw = ((px2 - x22) * x22) + ((py2 - y22) * y22);
                if (ccw < 0.0f) {
                    ccw = 0.0f;
                }
            }
        }
        if (ccw < 0.0f) {
            return -1;
        }
        return ccw > 0.0f ? 1 : 0;
    }

    public int relativeCCW(float px, float py) {
        return relativeCCW(this.x1, this.y1, this.x2, this.y2, px, py);
    }

    public int relativeCCW(Point2D p2) {
        return relativeCCW(this.x1, this.y1, this.x2, this.y2, p2.f11907x, p2.f11908y);
    }

    public static boolean linesIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        return relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0 && relativeCCW(x3, y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0;
    }

    public boolean intersectsLine(float x1, float y1, float x2, float y2) {
        return linesIntersect(x1, y1, x2, y2, this.x1, this.y1, this.x2, this.y2);
    }

    public boolean intersectsLine(Line2D l2) {
        return linesIntersect(l2.x1, l2.y1, l2.x2, l2.y2, this.x1, this.y1, this.x2, this.y2);
    }

    public static float ptSegDistSq(float x1, float y1, float x2, float y2, float px, float py) {
        float projlenSq;
        float x22 = x2 - x1;
        float y22 = y2 - y1;
        float px2 = px - x1;
        float py2 = py - y1;
        if ((px2 * x22) + (py2 * y22) <= 0.0f) {
            projlenSq = 0.0f;
        } else {
            px2 = x22 - px2;
            py2 = y22 - py2;
            float dotprod = (px2 * x22) + (py2 * y22);
            if (dotprod <= 0.0f) {
                projlenSq = 0.0f;
            } else {
                projlenSq = (dotprod * dotprod) / ((x22 * x22) + (y22 * y22));
            }
        }
        float lenSq = ((px2 * px2) + (py2 * py2)) - projlenSq;
        if (lenSq < 0.0f) {
            lenSq = 0.0f;
        }
        return lenSq;
    }

    public static float ptSegDist(float x1, float y1, float x2, float y2, float px, float py) {
        return (float) Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
    }

    public float ptSegDistSq(float px, float py) {
        return ptSegDistSq(this.x1, this.y1, this.x2, this.y2, px, py);
    }

    public float ptSegDistSq(Point2D pt) {
        return ptSegDistSq(this.x1, this.y1, this.x2, this.y2, pt.f11907x, pt.f11908y);
    }

    public double ptSegDist(float px, float py) {
        return ptSegDist(this.x1, this.y1, this.x2, this.y2, px, py);
    }

    public float ptSegDist(Point2D pt) {
        return ptSegDist(this.x1, this.y1, this.x2, this.y2, pt.f11907x, pt.f11908y);
    }

    public static float ptLineDistSq(float x1, float y1, float x2, float y2, float px, float py) {
        float x22 = x2 - x1;
        float y22 = y2 - y1;
        float px2 = px - x1;
        float py2 = py - y1;
        float dotprod = (px2 * x22) + (py2 * y22);
        float projlenSq = (dotprod * dotprod) / ((x22 * x22) + (y22 * y22));
        float lenSq = ((px2 * px2) + (py2 * py2)) - projlenSq;
        if (lenSq < 0.0f) {
            lenSq = 0.0f;
        }
        return lenSq;
    }

    public static float ptLineDist(float x1, float y1, float x2, float y2, float px, float py) {
        return (float) Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
    }

    public float ptLineDistSq(float px, float py) {
        return ptLineDistSq(this.x1, this.y1, this.x2, this.y2, px, py);
    }

    public float ptLineDistSq(Point2D pt) {
        return ptLineDistSq(this.x1, this.y1, this.x2, this.y2, pt.f11907x, pt.f11908y);
    }

    public float ptLineDist(float px, float py) {
        return ptLineDist(this.x1, this.y1, this.x2, this.y2, px, py);
    }

    public float ptLineDist(Point2D pt) {
        return ptLineDist(this.x1, this.y1, this.x2, this.y2, pt.f11907x, pt.f11908y);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new LineIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new LineIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public Line2D copy() {
        return new Line2D(this.x1, this.y1, this.x2, this.y2);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.x1);
        return bits + (Float.floatToIntBits(this.y1) * 37) + (Float.floatToIntBits(this.x2) * 43) + (Float.floatToIntBits(this.y2) * 47);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Line2D) {
            Line2D line = (Line2D) obj;
            return this.x1 == line.x1 && this.y1 == line.y1 && this.x2 == line.x2 && this.y2 == line.y2;
        }
        return false;
    }
}
