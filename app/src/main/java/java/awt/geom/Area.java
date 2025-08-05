package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Vector;
import sun.awt.geom.AreaOp;
import sun.awt.geom.Crossings;
import sun.awt.geom.Curve;

/* loaded from: rt.jar:java/awt/geom/Area.class */
public class Area implements Shape, Cloneable {
    private static Vector EmptyCurves = new Vector();
    private Vector curves;
    private Rectangle2D cachedBounds;

    public Area() {
        this.curves = EmptyCurves;
    }

    public Area(Shape shape) {
        if (shape instanceof Area) {
            this.curves = ((Area) shape).curves;
        } else {
            this.curves = pathToCurves(shape.getPathIterator(null));
        }
    }

    private static Vector pathToCurves(PathIterator pathIterator) {
        AreaOp nZWindOp;
        Vector vector = new Vector();
        int windingRule = pathIterator.getWindingRule();
        double[] dArr = new double[23];
        double d2 = 0.0d;
        double d3 = 0.0d;
        double d4 = 0.0d;
        double d5 = 0.0d;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(dArr)) {
                case 0:
                    double d6 = d5;
                    Curve.insertLine(vector, d4, d6, d2, d3);
                    d2 = d6;
                    d4 = dArr[0];
                    d3 = d6;
                    d5 = dArr[1];
                    Curve.insertMove(vector, d2, d3);
                    break;
                case 1:
                    double d7 = dArr[0];
                    double d8 = dArr[1];
                    Curve.insertLine(vector, d4, d5, d7, d8);
                    d4 = d7;
                    d5 = d8;
                    break;
                case 2:
                    double d9 = dArr[2];
                    double d10 = dArr[3];
                    Curve.insertQuad(vector, d4, d5, dArr);
                    d4 = d9;
                    d5 = d10;
                    break;
                case 3:
                    double d11 = dArr[4];
                    double d12 = dArr[5];
                    Curve.insertCubic(vector, d4, d5, dArr);
                    d4 = d11;
                    d5 = d12;
                    break;
                case 4:
                    Curve.insertLine(vector, d4, d5, d2, d3);
                    d4 = d2;
                    d5 = d3;
                    break;
            }
            pathIterator.next();
        }
        Curve.insertLine(vector, d4, d5, d2, d3);
        if (windingRule == 0) {
            nZWindOp = new AreaOp.EOWindOp();
        } else {
            nZWindOp = new AreaOp.NZWindOp();
        }
        return nZWindOp.calculate(vector, EmptyCurves);
    }

    public void add(Area area) {
        this.curves = new AreaOp.AddOp().calculate(this.curves, area.curves);
        invalidateBounds();
    }

    public void subtract(Area area) {
        this.curves = new AreaOp.SubOp().calculate(this.curves, area.curves);
        invalidateBounds();
    }

    public void intersect(Area area) {
        this.curves = new AreaOp.IntOp().calculate(this.curves, area.curves);
        invalidateBounds();
    }

    public void exclusiveOr(Area area) {
        this.curves = new AreaOp.XorOp().calculate(this.curves, area.curves);
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
        Enumeration enumerationElements = this.curves.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((Curve) enumerationElements.nextElement2()).getOrder() > 1) {
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
        Curve curve = (Curve) this.curves.get(1);
        Curve curve2 = (Curve) this.curves.get(2);
        if (curve.getOrder() != 1 || curve2.getOrder() != 1 || curve.getXTop() != curve.getXBot() || curve2.getXTop() != curve2.getXBot() || curve.getYTop() != curve2.getYTop() || curve.getYBot() != curve2.getYBot()) {
            return false;
        }
        return true;
    }

    public boolean isSingular() {
        if (this.curves.size() < 3) {
            return true;
        }
        Enumeration enumerationElements = this.curves.elements();
        enumerationElements.nextElement2();
        while (enumerationElements.hasMoreElements()) {
            if (((Curve) enumerationElements.nextElement2()).getOrder() == 0) {
                return false;
            }
        }
        return true;
    }

    private void invalidateBounds() {
        this.cachedBounds = null;
    }

    private Rectangle2D getCachedBounds() {
        if (this.cachedBounds != null) {
            return this.cachedBounds;
        }
        Rectangle2D.Double r0 = new Rectangle2D.Double();
        if (this.curves.size() > 0) {
            Curve curve = (Curve) this.curves.get(0);
            r0.setRect(curve.getX0(), curve.getY0(), 0.0d, 0.0d);
            for (int i2 = 1; i2 < this.curves.size(); i2++) {
                ((Curve) this.curves.get(i2)).enlarge(r0);
            }
        }
        this.cachedBounds = r0;
        return r0;
    }

    @Override // java.awt.Shape
    public Rectangle2D getBounds2D() {
        return getCachedBounds().getBounds2D();
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        return getCachedBounds().getBounds();
    }

    public Object clone() {
        return new Area(this);
    }

    public boolean equals(Area area) {
        if (area == this) {
            return true;
        }
        if (area == null) {
            return false;
        }
        return new AreaOp.XorOp().calculate(this.curves, area.curves).isEmpty();
    }

    public void transform(AffineTransform affineTransform) {
        if (affineTransform == null) {
            throw new NullPointerException("transform must not be null");
        }
        this.curves = pathToCurves(getPathIterator(affineTransform));
        invalidateBounds();
    }

    public Area createTransformedArea(AffineTransform affineTransform) {
        Area area = new Area(this);
        area.transform(affineTransform);
        return area;
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        int i2;
        if (!getCachedBounds().contains(d2, d3)) {
            return false;
        }
        Enumeration enumerationElements = this.curves.elements();
        int iCrossingsFor = 0;
        while (true) {
            i2 = iCrossingsFor;
            if (!enumerationElements.hasMoreElements()) {
                break;
            }
            iCrossingsFor = i2 + ((Curve) enumerationElements.nextElement2()).crossingsFor(d2, d3);
        }
        return (i2 & 1) == 1;
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        Crossings crossingsFindCrossings;
        return d4 >= 0.0d && d5 >= 0.0d && getCachedBounds().contains(d2, d3, d4, d5) && (crossingsFindCrossings = Crossings.findCrossings(this.curves, d2, d3, d2 + d4, d3 + d5)) != null && crossingsFindCrossings.covers(d3, d3 + d5);
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        if (d4 < 0.0d || d5 < 0.0d || !getCachedBounds().intersects(d2, d3, d4, d5)) {
            return false;
        }
        Crossings crossingsFindCrossings = Crossings.findCrossings(this.curves, d2, d3, d2 + d4, d3 + d5);
        return crossingsFindCrossings == null || !crossingsFindCrossings.isEmpty();
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new AreaIterator(this.curves, affineTransform);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new FlatteningPathIterator(getPathIterator(affineTransform), d2);
    }
}
