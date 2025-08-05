package com.efiAnalytics.ui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dL.class */
public class dL implements Shape, Serializable {

    /* renamed from: a, reason: collision with root package name */
    public int f11338a;

    /* renamed from: b, reason: collision with root package name */
    public float[] f11339b;

    /* renamed from: c, reason: collision with root package name */
    public float[] f11340c;

    /* renamed from: d, reason: collision with root package name */
    protected Rectangle2D f11341d;

    /* renamed from: e, reason: collision with root package name */
    private GeneralPath f11342e;

    /* renamed from: f, reason: collision with root package name */
    private GeneralPath f11343f;

    public dL() {
        this.f11339b = new float[4];
        this.f11340c = new float[4];
    }

    public dL(float[] fArr, float[] fArr2, int i2) {
        if (i2 > fArr.length || i2 > fArr2.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
        }
        this.f11338a = i2;
        this.f11339b = new float[i2];
        this.f11340c = new float[i2];
        System.arraycopy(fArr, 0, this.f11339b, 0, i2);
        System.arraycopy(fArr2, 0, this.f11340c, 0, i2);
        a();
    }

    public Object clone() {
        dL dLVar = new dL();
        for (int i2 = 0; i2 < this.f11338a; i2++) {
            dLVar.a(this.f11339b[i2], this.f11340c[i2]);
        }
        return dLVar;
    }

    private void a() {
        this.f11342e = new GeneralPath();
        this.f11342e.moveTo(this.f11339b[0], this.f11340c[0]);
        for (int i2 = 1; i2 < this.f11338a; i2++) {
            this.f11342e.lineTo(this.f11339b[i2], this.f11340c[i2]);
        }
        this.f11341d = this.f11342e.getBounds2D();
        this.f11343f = null;
    }

    private void b(float f2, float f3) {
        this.f11343f = null;
        if (this.f11342e == null) {
            this.f11342e = new GeneralPath(0);
            this.f11342e.moveTo(f2, f3);
            this.f11341d = new Rectangle2D.Float(f2, f3, 0.0f, 0.0f);
            return;
        }
        this.f11342e.lineTo(f2, f3);
        float maxX = (float) this.f11341d.getMaxX();
        float maxY = (float) this.f11341d.getMaxY();
        float minX = (float) this.f11341d.getMinX();
        float minY = (float) this.f11341d.getMinY();
        if (f2 < minX) {
            minX = f2;
        } else if (f2 > maxX) {
            maxX = f2;
        }
        if (f3 < minY) {
            minY = f3;
        } else if (f3 > maxY) {
            maxY = f3;
        }
        this.f11341d = new Rectangle2D.Float(minX, minY, maxX - minX, maxY - minY);
    }

    public void a(float f2, float f3) {
        if (this.f11338a == this.f11339b.length) {
            float[] fArr = new float[this.f11338a * 2];
            System.arraycopy(this.f11339b, 0, fArr, 0, this.f11338a);
            this.f11339b = fArr;
            float[] fArr2 = new float[this.f11338a * 2];
            System.arraycopy(this.f11340c, 0, fArr2, 0, this.f11338a);
            this.f11340c = fArr2;
        }
        this.f11339b[this.f11338a] = f2;
        this.f11340c[this.f11338a] = f3;
        this.f11338a++;
        b(f2, f3);
    }

    @Override // java.awt.Shape
    public Rectangle2D getBounds2D() {
        return this.f11341d;
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        if (this.f11341d == null) {
            return null;
        }
        return this.f11341d.getBounds();
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        if (this.f11338a <= 2 || !this.f11341d.contains(d2, d3)) {
            return false;
        }
        b();
        return this.f11343f.contains(d2, d3);
    }

    private void b() {
        if (this.f11338a < 1 || this.f11343f != null) {
            return;
        }
        this.f11343f = (GeneralPath) this.f11342e.clone();
        this.f11343f.closePath();
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        if (this.f11338a <= 0 || !this.f11341d.intersects(d2, d3, d4, d5)) {
            return false;
        }
        b();
        return this.f11343f.intersects(d2, d3, d4, d5);
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        if (this.f11338a <= 0 || !this.f11341d.intersects(d2, d3, d4, d5)) {
            return false;
        }
        b();
        return this.f11343f.contains(d2, d3, d4, d5);
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        b();
        if (this.f11343f == null) {
            return null;
        }
        return this.f11343f.getPathIterator(affineTransform);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return getPathIterator(affineTransform);
    }
}
