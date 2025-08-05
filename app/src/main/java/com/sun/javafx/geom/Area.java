package com.sun.javafx.geom;

import com.sun.javafx.geom.AreaOp;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Area.class */
public class Area extends Shape {
    private static final Vector EmptyCurves = new Vector();
    private Vector curves;
    private RectBounds cachedBounds;

    public Area() {
        this.curves = EmptyCurves;
    }

    public Area(Shape s2) {
        if (s2 instanceof Area) {
            this.curves = ((Area) s2).curves;
        } else {
            this.curves = pathToCurves(s2.getPathIterator(null));
        }
    }

    public Area(PathIterator iter) {
        this.curves = pathToCurves(iter);
    }

    private static Vector pathToCurves(PathIterator pi) {
        AreaOp operator;
        Vector curves = new Vector();
        int windingRule = pi.getWindingRule();
        float[] coords = new float[6];
        double[] tmp = new double[23];
        double movx = 0.0d;
        double movy = 0.0d;
        double curx = 0.0d;
        double cury = 0.0d;
        while (!pi.isDone()) {
            switch (pi.currentSegment(coords)) {
                case 0:
                    double d2 = cury;
                    Curve.insertLine(curves, curx, d2, movx, movy);
                    movx = d2;
                    curx = coords[0];
                    movy = d2;
                    cury = coords[1];
                    Curve.insertMove(curves, movx, movy);
                    break;
                case 1:
                    double newx = coords[0];
                    double newy = coords[1];
                    Curve.insertLine(curves, curx, cury, newx, newy);
                    curx = newx;
                    cury = newy;
                    break;
                case 2:
                    double newx2 = coords[2];
                    double newy2 = coords[3];
                    Curve.insertQuad(curves, tmp, curx, cury, coords[0], coords[1], coords[2], coords[3]);
                    curx = newx2;
                    cury = newy2;
                    break;
                case 3:
                    double newx3 = coords[4];
                    double newy3 = coords[5];
                    Curve.insertCubic(curves, tmp, curx, cury, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    curx = newx3;
                    cury = newy3;
                    break;
                case 4:
                    Curve.insertLine(curves, curx, cury, movx, movy);
                    curx = movx;
                    cury = movy;
                    break;
            }
            pi.next();
        }
        Curve.insertLine(curves, curx, cury, movx, movy);
        if (windingRule == 0) {
            operator = new AreaOp.EOWindOp();
        } else {
            operator = new AreaOp.NZWindOp();
        }
        return operator.calculate(curves, EmptyCurves);
    }

    public void add(Area rhs) {
        this.curves = new AreaOp.AddOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    public void subtract(Area rhs) {
        this.curves = new AreaOp.SubOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    public void intersect(Area rhs) {
        this.curves = new AreaOp.IntOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    public void exclusiveOr(Area rhs) {
        this.curves = new AreaOp.XorOp().calculate(this.curves, rhs.curves);
        invalidateBounds();
    }

    public void reset() {
        this.curves = new Vector();
        invalidateBounds();
    }

    public boolean isEmpty() {
        return this.curves.size() == 0;
    }

    public boolean isPolygonal() {
        Enumeration enum_ = this.curves.elements();
        while (enum_.hasMoreElements()) {
            if (((Curve) enum_.nextElement2()).getOrder() > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isRectangular() {
        int size = this.curves.size();
        if (size == 0) {
            return true;
        }
        if (size > 3) {
            return false;
        }
        Curve c1 = (Curve) this.curves.get(1);
        Curve c2 = (Curve) this.curves.get(2);
        if (c1.getOrder() != 1 || c2.getOrder() != 1 || c1.getXTop() != c1.getXBot() || c2.getXTop() != c2.getXBot() || c1.getYTop() != c2.getYTop() || c1.getYBot() != c2.getYBot()) {
            return false;
        }
        return true;
    }

    public boolean isSingular() {
        if (this.curves.size() < 3) {
            return true;
        }
        Enumeration enum_ = this.curves.elements();
        enum_.nextElement2();
        while (enum_.hasMoreElements()) {
            if (((Curve) enum_.nextElement2()).getOrder() == 0) {
                return false;
            }
        }
        return true;
    }

    private void invalidateBounds() {
        this.cachedBounds = null;
    }

    private RectBounds getCachedBounds() {
        if (this.cachedBounds != null) {
            return this.cachedBounds;
        }
        RectBounds r2 = new RectBounds();
        if (this.curves.size() > 0) {
            Curve c2 = (Curve) this.curves.get(0);
            r2.setBounds((float) c2.getX0(), (float) c2.getY0(), 0.0f, 0.0f);
            for (int i2 = 1; i2 < this.curves.size(); i2++) {
                ((Curve) this.curves.get(i2)).enlarge(r2);
            }
        }
        this.cachedBounds = r2;
        return r2;
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        return new RectBounds(getCachedBounds());
    }

    public boolean isEquivalent(Area other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        Vector c2 = new AreaOp.XorOp().calculate(this.curves, other.curves);
        return c2.isEmpty();
    }

    public void transform(BaseTransform tx) {
        if (tx == null) {
            throw new NullPointerException("transform must not be null");
        }
        this.curves = pathToCurves(getPathIterator(tx));
        invalidateBounds();
    }

    public Area createTransformedArea(BaseTransform tx) {
        Area a2 = new Area(this);
        a2.transform(tx);
        return a2;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        int crossings;
        if (!getCachedBounds().contains(x2, y2)) {
            return false;
        }
        Enumeration enum_ = this.curves.elements();
        int iCrossingsFor = 0;
        while (true) {
            crossings = iCrossingsFor;
            if (!enum_.hasMoreElements()) {
                break;
            }
            Curve c2 = (Curve) enum_.nextElement2();
            iCrossingsFor = crossings + c2.crossingsFor(x2, y2);
        }
        return (crossings & 1) == 1;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        Crossings c2;
        return w2 >= 0.0f && h2 >= 0.0f && getCachedBounds().contains(x2, y2) && getCachedBounds().contains(x2 + w2, y2 + h2) && (c2 = Crossings.findCrossings(this.curves, (double) x2, (double) y2, (double) (x2 + w2), (double) (y2 + h2))) != null && c2.covers((double) y2, (double) (y2 + h2));
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        if (w2 < 0.0f || h2 < 0.0f || !getCachedBounds().intersects(x2, y2, w2, h2)) {
            return false;
        }
        Crossings c2 = Crossings.findCrossings(this.curves, x2, y2, x2 + w2, y2 + h2);
        return c2 == null || !c2.isEmpty();
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new AreaIterator(this.curves, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }

    @Override // com.sun.javafx.geom.Shape
    public Area copy() {
        return new Area(this);
    }
}
