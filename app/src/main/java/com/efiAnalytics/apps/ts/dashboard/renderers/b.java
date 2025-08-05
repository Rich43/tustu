package com.efiAnalytics.apps.ts.dashboard.renderers;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.eJ;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/b.class */
class b implements Shape, Serializable {

    /* renamed from: a, reason: collision with root package name */
    Gauge f9521a;

    /* renamed from: g, reason: collision with root package name */
    a f9527g;

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ AsymetricSweepRenderer f9531k;

    /* renamed from: b, reason: collision with root package name */
    CubicCurve2D.Float f9522b = new CubicCurve2D.Float();

    /* renamed from: c, reason: collision with root package name */
    CubicCurve2D.Float f9523c = new CubicCurve2D.Float();

    /* renamed from: d, reason: collision with root package name */
    float f9524d = 0.27f;

    /* renamed from: e, reason: collision with root package name */
    float f9525e = 0.2f;

    /* renamed from: f, reason: collision with root package name */
    float f9526f = 0.12f;

    /* renamed from: h, reason: collision with root package name */
    final HashMap f9528h = new HashMap();

    /* renamed from: i, reason: collision with root package name */
    final int f9529i = eJ.a(30);

    /* renamed from: j, reason: collision with root package name */
    int f9530j = this.f9529i;

    b(AsymetricSweepRenderer asymetricSweepRenderer, Gauge gauge) {
        this.f9531k = asymetricSweepRenderer;
        this.f9521a = null;
        this.f9527g = new a(this.f9531k, this);
        this.f9521a = gauge;
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        return new Rectangle(this.f9521a.getWidth(), this.f9521a.getHeight());
    }

    @Override // java.awt.Shape
    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Float(0.0f, 0.0f, this.f9521a.getWidth(), this.f9521a.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        double borderWidth = (d2 - this.f9521a.getBorderWidth()) / (this.f9521a.getWidth() - (2 * this.f9521a.getBorderWidth()));
        return d3 < c(this.f9523c, borderWidth) && d3 > c(this.f9522b, borderWidth);
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        return true;
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        return contains(d2, d3) && contains(d2 + d4, d3) && contains(d2 + d4, d3 + d5) && contains(d2, d3);
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        this.f9527g.a();
        this.f9527g.a(affineTransform);
        return this.f9527g;
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        this.f9527g.a();
        this.f9527g.a(affineTransform);
        return this.f9527g;
    }

    private int a() {
        return this.f9530j;
    }

    public void a(int i2, int i3) {
        float fAbs;
        float fAbs2;
        int borderWidth = this.f9521a.getBorderWidth() / 2;
        int i4 = i2 - (2 * borderWidth);
        this.f9530j = Math.round(i3 * (this.f9521a.getFaceAngle() / 360.0f));
        int startAngle = 360 - this.f9521a.getStartAngle();
        this.f9530j = this.f9530j < startAngle ? startAngle : this.f9530j;
        if (this.f9530j > this.f9521a.getHeight() - (2 * borderWidth)) {
            this.f9530j = this.f9521a.getHeight() - (2 * borderWidth);
        }
        int i5 = (i3 - this.f9530j) - (2 * borderWidth);
        this.f9525e = this.f9521a.getSweepBeginDegree() / 90.0f;
        this.f9526f = this.f9521a.getSweepAngle() / 90.0f;
        if (this.f9525e > 3.0f) {
            fAbs = 0.0f;
            this.f9525e -= 3.0f;
        } else if (this.f9525e > 2.0f) {
            fAbs = Math.abs(this.f9525e - 3.0f);
            this.f9525e = 0.0f;
        } else if (this.f9525e > 1.0f) {
            fAbs = 1.0f;
            this.f9525e = Math.abs(this.f9525e - 2.0f);
        } else {
            fAbs = this.f9525e;
            this.f9525e = 1.0f;
        }
        if (this.f9526f > 3.0f) {
            fAbs2 = 0.0f;
            this.f9526f -= 3.0f;
        } else if (this.f9526f > 2.0f) {
            fAbs2 = Math.abs(this.f9526f - 3.0f);
            this.f9526f = 0.0f;
        } else if (this.f9526f > 1.0f) {
            fAbs2 = 1.0f;
            this.f9526f = Math.abs(this.f9526f - 2.0f);
        } else {
            fAbs2 = this.f9526f;
            this.f9526f = 1.0f;
        }
        this.f9522b.setCurve(borderWidth, (this.f9521a.getHeight() - this.f9530j) - borderWidth, i4 * this.f9525e, i5 * fAbs, i4 * this.f9526f, i5 * fAbs2, i4, borderWidth);
        this.f9523c.setCurve(this.f9522b);
        this.f9523c.y1 += this.f9530j;
        this.f9523c.y2 += this.f9530j;
        this.f9523c.ctrly1 += this.f9530j;
        this.f9523c.ctrly2 += this.f9530j;
        this.f9528h.clear();
        int i6 = -1;
        double d2 = 0.0d;
        double d3 = 0.0d;
        for (int i7 = 0; i7 <= 5000 && i6 < 100; i7++) {
            double d4 = i7 / 1000.0f;
            double dA = (100.0d * a(this.f9522b, d4)) / (this.f9522b.getX2() - this.f9522b.getX1());
            if (dA >= i6 + 1) {
                i6++;
                if (dA - i6 > i6 - d2) {
                    this.f9528h.put(Integer.valueOf(i6), Double.valueOf(d3));
                } else {
                    this.f9528h.put(Integer.valueOf(i6), Double.valueOf(d4));
                }
            }
            d2 = dA;
            d3 = d4;
        }
    }

    public double a(CubicCurve2D cubicCurve2D, double d2) {
        return (Math.pow(1.0d - d2, 3.0d) * cubicCurve2D.getX1()) + (3.0d * Math.pow(1.0d - d2, 2.0d) * d2 * cubicCurve2D.getCtrlX1()) + (3.0d * (1.0d - d2) * Math.pow(d2, 2.0d) * cubicCurve2D.getCtrlX2()) + (Math.pow(d2, 3.0d) * cubicCurve2D.getX2());
    }

    public double b(CubicCurve2D cubicCurve2D, double d2) {
        return (Math.pow(1.0d - d2, 3.0d) * cubicCurve2D.getY1()) + (3.0d * Math.pow(1.0d - d2, 2.0d) * d2 * cubicCurve2D.getCtrlY1()) + (3.0d * (1.0d - d2) * Math.pow(d2, 2.0d) * cubicCurve2D.getCtrlY2()) + (Math.pow(d2, 3.0d) * cubicCurve2D.getY2());
    }

    public double a(double d2) {
        double d3 = this.f9521a.isCounterClockwise() ? 1.0d - d2 : d2;
        synchronized (this.f9528h) {
            if (this.f9528h.isEmpty()) {
                return 0.0d;
            }
            if (d3 >= 1.0d) {
                return ((Double) this.f9528h.get(100)).doubleValue();
            }
            if (d3 <= 0.0d) {
                return ((Double) this.f9528h.get(0)).doubleValue();
            }
            int iFloor = (int) Math.floor(d3 * 100.0d);
            double d4 = (d3 * 100.0d) - iFloor;
            double dDoubleValue = ((Double) this.f9528h.get(Integer.valueOf(iFloor))).doubleValue();
            return dDoubleValue + (d4 * (((Double) this.f9528h.get(Integer.valueOf(iFloor + 1))).doubleValue() - dDoubleValue));
        }
    }

    public double c(CubicCurve2D cubicCurve2D, double d2) {
        double dA = a(d2);
        return cubicCurve2D.equals(this.f9522b) ? b(cubicCurve2D, dA) : b(this.f9522b, dA) + a();
    }

    public float d(CubicCurve2D cubicCurve2D, double d2) {
        return (float) c(cubicCurve2D, (float) (d2 / this.f9521a.getWidth()));
    }

    public double b(double d2) {
        return a(this.f9522b, a(d2));
    }
}
