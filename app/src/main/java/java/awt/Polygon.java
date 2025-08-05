package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Arrays;
import sun.awt.geom.Crossings;

/* loaded from: rt.jar:java/awt/Polygon.class */
public class Polygon implements Shape, Serializable {
    public int npoints;
    public int[] xpoints;
    public int[] ypoints;
    protected Rectangle bounds;
    private static final long serialVersionUID = -6460061437900069969L;
    private static final int MIN_LENGTH = 4;

    public Polygon() {
        this.xpoints = new int[4];
        this.ypoints = new int[4];
    }

    public Polygon(int[] iArr, int[] iArr2, int i2) {
        if (i2 > iArr.length || i2 > iArr2.length) {
            throw new IndexOutOfBoundsException("npoints > xpoints.length || npoints > ypoints.length");
        }
        if (i2 < 0) {
            throw new NegativeArraySizeException("npoints < 0");
        }
        this.npoints = i2;
        this.xpoints = Arrays.copyOf(iArr, i2);
        this.ypoints = Arrays.copyOf(iArr2, i2);
    }

    public void reset() {
        this.npoints = 0;
        this.bounds = null;
    }

    public void invalidate() {
        this.bounds = null;
    }

    public void translate(int i2, int i3) {
        for (int i4 = 0; i4 < this.npoints; i4++) {
            int[] iArr = this.xpoints;
            int i5 = i4;
            iArr[i5] = iArr[i5] + i2;
            int[] iArr2 = this.ypoints;
            int i6 = i4;
            iArr2[i6] = iArr2[i6] + i3;
        }
        if (this.bounds != null) {
            this.bounds.translate(i2, i3);
        }
    }

    void calculateBounds(int[] iArr, int[] iArr2, int i2) {
        int iMin = Integer.MAX_VALUE;
        int iMin2 = Integer.MAX_VALUE;
        int iMax = Integer.MIN_VALUE;
        int iMax2 = Integer.MIN_VALUE;
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = iArr[i3];
            iMin = Math.min(iMin, i4);
            iMax = Math.max(iMax, i4);
            int i5 = iArr2[i3];
            iMin2 = Math.min(iMin2, i5);
            iMax2 = Math.max(iMax2, i5);
        }
        this.bounds = new Rectangle(iMin, iMin2, iMax - iMin, iMax2 - iMin2);
    }

    void updateBounds(int i2, int i3) {
        if (i2 < this.bounds.f12372x) {
            this.bounds.width += this.bounds.f12372x - i2;
            this.bounds.f12372x = i2;
        } else {
            this.bounds.width = Math.max(this.bounds.width, i2 - this.bounds.f12372x);
        }
        if (i3 < this.bounds.f12373y) {
            this.bounds.height += this.bounds.f12373y - i3;
            this.bounds.f12373y = i3;
        } else {
            this.bounds.height = Math.max(this.bounds.height, i3 - this.bounds.f12373y);
        }
    }

    public void addPoint(int i2, int i3) {
        if (this.npoints >= this.xpoints.length || this.npoints >= this.ypoints.length) {
            int iHighestOneBit = this.npoints * 2;
            if (iHighestOneBit < 4) {
                iHighestOneBit = 4;
            } else if ((iHighestOneBit & (iHighestOneBit - 1)) != 0) {
                iHighestOneBit = Integer.highestOneBit(iHighestOneBit);
            }
            this.xpoints = Arrays.copyOf(this.xpoints, iHighestOneBit);
            this.ypoints = Arrays.copyOf(this.ypoints, iHighestOneBit);
        }
        this.xpoints[this.npoints] = i2;
        this.ypoints[this.npoints] = i3;
        this.npoints++;
        if (this.bounds != null) {
            updateBounds(i2, i3);
        }
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        return getBoundingBox();
    }

    @Deprecated
    public Rectangle getBoundingBox() {
        if (this.npoints == 0) {
            return new Rectangle();
        }
        if (this.bounds == null) {
            calculateBounds(this.xpoints, this.ypoints, this.npoints);
        }
        return this.bounds.getBounds();
    }

    public boolean contains(Point point) {
        return contains(point.f12370x, point.f12371y);
    }

    public boolean contains(int i2, int i3) {
        return contains(i2, i3);
    }

    @Deprecated
    public boolean inside(int i2, int i3) {
        return contains(i2, i3);
    }

    @Override // java.awt.Shape
    public Rectangle2D getBounds2D() {
        return getBounds();
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0104 A[SYNTHETIC] */
    @Override // java.awt.Shape
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean contains(double r8, double r10) {
        /*
            Method dump skipped, instructions count: 287
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Polygon.contains(double, double):boolean");
    }

    private Crossings getCrossings(double d2, double d3, double d4, double d5) {
        Crossings.EvenOdd evenOdd = new Crossings.EvenOdd(d2, d3, d4, d5);
        int i2 = this.xpoints[this.npoints - 1];
        int i3 = this.ypoints[this.npoints - 1];
        for (int i4 = 0; i4 < this.npoints; i4++) {
            int i5 = this.xpoints[i4];
            int i6 = this.ypoints[i4];
            if (evenOdd.accumulateLine(i2, i3, i5, i6)) {
                return null;
            }
            i2 = i5;
            i3 = i6;
        }
        return evenOdd;
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        if (this.npoints <= 0 || !getBoundingBox().intersects(d2, d3, d4, d5)) {
            return false;
        }
        Crossings crossings = getCrossings(d2, d3, d2 + d4, d3 + d5);
        return crossings == null || !crossings.isEmpty();
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        Crossings crossings;
        return this.npoints > 0 && getBoundingBox().intersects(d2, d3, d4, d5) && (crossings = getCrossings(d2, d3, d2 + d4, d3 + d5)) != null && crossings.covers(d3, d3 + d5);
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new PolygonPathIterator(this, affineTransform);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return getPathIterator(affineTransform);
    }

    /* loaded from: rt.jar:java/awt/Polygon$PolygonPathIterator.class */
    class PolygonPathIterator implements PathIterator {
        Polygon poly;
        AffineTransform transform;
        int index;

        public PolygonPathIterator(Polygon polygon, AffineTransform affineTransform) {
            this.poly = polygon;
            this.transform = affineTransform;
            if (polygon.npoints == 0) {
                this.index = 1;
            }
        }

        @Override // java.awt.geom.PathIterator
        public int getWindingRule() {
            return 0;
        }

        @Override // java.awt.geom.PathIterator
        public boolean isDone() {
            return this.index > this.poly.npoints;
        }

        @Override // java.awt.geom.PathIterator
        public void next() {
            this.index++;
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(float[] fArr) {
            if (this.index >= this.poly.npoints) {
                return 4;
            }
            fArr[0] = this.poly.xpoints[this.index];
            fArr[1] = this.poly.ypoints[this.index];
            if (this.transform != null) {
                this.transform.transform(fArr, 0, fArr, 0, 1);
            }
            return this.index == 0 ? 0 : 1;
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(double[] dArr) {
            if (this.index >= this.poly.npoints) {
                return 4;
            }
            dArr[0] = this.poly.xpoints[this.index];
            dArr[1] = this.poly.ypoints[this.index];
            if (this.transform != null) {
                this.transform.transform(dArr, 0, dArr, 0, 1);
            }
            return this.index == 0 ? 0 : 1;
        }
    }
}
