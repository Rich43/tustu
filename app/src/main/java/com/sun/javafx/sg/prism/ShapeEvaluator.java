package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/ShapeEvaluator.class */
class ShapeEvaluator {
    private Shape savedv0;
    private Shape savedv1;
    private Geometry geom0;
    private Geometry geom1;

    ShapeEvaluator() {
    }

    public Shape evaluate(Shape v0, Shape v1, float fraction) {
        if (this.savedv0 != v0 || this.savedv1 != v1) {
            if (this.savedv0 == v1 && this.savedv1 == v0) {
                Geometry gtmp = this.geom0;
                this.geom0 = this.geom1;
                this.geom1 = gtmp;
            } else {
                recalculate(v0, v1);
            }
            this.savedv0 = v0;
            this.savedv1 = v1;
        }
        return getShape(fraction);
    }

    private void recalculate(Shape v0, Shape v1) {
        this.geom0 = new Geometry(v0);
        this.geom1 = new Geometry(v1);
        float[] tvals0 = this.geom0.getTvals();
        float[] tvals1 = this.geom1.getTvals();
        float[] masterTvals = mergeTvals(tvals0, tvals1);
        this.geom0.setTvals(masterTvals);
        this.geom1.setTvals(masterTvals);
    }

    private Shape getShape(float fraction) {
        return new MorphedShape(this.geom0, this.geom1, fraction);
    }

    private static float[] mergeTvals(float[] tvals0, float[] tvals1) {
        int count = sortTvals(tvals0, tvals1, null);
        float[] newtvals = new float[count];
        sortTvals(tvals0, tvals1, newtvals);
        return newtvals;
    }

    private static int sortTvals(float[] tvals0, float[] tvals1, float[] newtvals) {
        int i0 = 0;
        int i1 = 0;
        int numtvals = 0;
        while (i0 < tvals0.length && i1 < tvals1.length) {
            float t0 = tvals0[i0];
            float t1 = tvals1[i1];
            if (t0 <= t1) {
                if (newtvals != null) {
                    newtvals[numtvals] = t0;
                }
                i0++;
            }
            if (t1 <= t0) {
                if (newtvals != null) {
                    newtvals[numtvals] = t1;
                }
                i1++;
            }
            numtvals++;
        }
        return numtvals;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static float interp(float v0, float v1, float t2) {
        return v0 + ((v1 - v0) * t2);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/ShapeEvaluator$Geometry.class */
    private static class Geometry {
        static final float THIRD = 0.33333334f;
        static final float MIN_LEN = 0.001f;
        float[] bezierCoords;
        int numCoords;
        int windingrule;
        float[] myTvals;

        public Geometry(Shape s2) {
            this.bezierCoords = new float[20];
            PathIterator pi = s2.getPathIterator(null);
            this.windingrule = pi.getWindingRule();
            if (pi.isDone()) {
                this.numCoords = 8;
            }
            float[] coords = new float[6];
            int type = pi.currentSegment(coords);
            pi.next();
            if (type != 0) {
                throw new IllegalPathStateException("missing initial moveto");
            }
            float[] fArr = this.bezierCoords;
            float f2 = coords[0];
            float movx = f2;
            float curx = f2;
            fArr[0] = f2;
            float[] fArr2 = this.bezierCoords;
            float f3 = coords[1];
            float movy = f3;
            float cury = f3;
            fArr2[1] = f3;
            Vector<Point2D> savedpathendpoints = new Vector<>();
            this.numCoords = 2;
            while (!pi.isDone()) {
                switch (pi.currentSegment(coords)) {
                    case 0:
                        if (curx != movx || cury != movy) {
                            appendLineTo(curx, cury, movx, movy);
                            curx = movx;
                            cury = movy;
                        }
                        float newx = coords[0];
                        float newy = coords[1];
                        if (curx != newx || cury != newy) {
                            savedpathendpoints.add(new Point2D(movx, movy));
                            appendLineTo(curx, cury, newx, newy);
                            movx = newx;
                            curx = newx;
                            movy = newy;
                            cury = newy;
                            break;
                        } else {
                            break;
                        }
                        break;
                    case 1:
                        float newx2 = coords[0];
                        float newy2 = coords[1];
                        appendLineTo(curx, cury, newx2, newy2);
                        curx = newx2;
                        cury = newy2;
                        break;
                    case 2:
                        float ctrlx = coords[0];
                        float ctrly = coords[1];
                        float newx3 = coords[2];
                        float newy3 = coords[3];
                        appendQuadTo(curx, cury, ctrlx, ctrly, newx3, newy3);
                        curx = newx3;
                        cury = newy3;
                        break;
                    case 3:
                        float f4 = coords[0];
                        float f5 = coords[1];
                        float f6 = coords[2];
                        float f7 = coords[3];
                        float f8 = coords[4];
                        curx = f8;
                        float f9 = coords[5];
                        cury = f9;
                        appendCubicTo(f4, f5, f6, f7, f8, f9);
                        break;
                    case 4:
                        if (curx != movx || cury != movy) {
                            appendLineTo(curx, cury, movx, movy);
                            curx = movx;
                            cury = movy;
                            break;
                        } else {
                            break;
                        }
                        break;
                }
                pi.next();
            }
            if (this.numCoords < 8 || curx != movx || cury != movy) {
                appendLineTo(curx, cury, movx, movy);
                curx = movx;
                cury = movy;
            }
            for (int i2 = savedpathendpoints.size() - 1; i2 >= 0; i2--) {
                Point2D p2 = savedpathendpoints.get(i2);
                float newx4 = p2.f11907x;
                float newy4 = p2.f11908y;
                if (curx != newx4 || cury != newy4) {
                    appendLineTo(curx, cury, newx4, newy4);
                    curx = newx4;
                    cury = newy4;
                }
            }
            int minPt = 0;
            float minX = this.bezierCoords[0];
            float minY = this.bezierCoords[1];
            for (int ci = 6; ci < this.numCoords; ci += 6) {
                float x2 = this.bezierCoords[ci];
                float y2 = this.bezierCoords[ci + 1];
                if (y2 < minY || (y2 == minY && x2 < minX)) {
                    minPt = ci;
                    minX = x2;
                    minY = y2;
                }
            }
            if (minPt > 0) {
                float[] newCoords = new float[this.numCoords];
                System.arraycopy(this.bezierCoords, minPt, newCoords, 0, this.numCoords - minPt);
                System.arraycopy(this.bezierCoords, 2, newCoords, this.numCoords - minPt, minPt);
                this.bezierCoords = newCoords;
            }
            float area = 0.0f;
            float curx2 = this.bezierCoords[0];
            float cury2 = this.bezierCoords[1];
            for (int i3 = 2; i3 < this.numCoords; i3 += 2) {
                float newx5 = this.bezierCoords[i3];
                float newy5 = this.bezierCoords[i3 + 1];
                area += (curx2 * newy5) - (newx5 * cury2);
                curx2 = newx5;
                cury2 = newy5;
            }
            if (area < 0.0f) {
                int i4 = 2;
                for (int j2 = this.numCoords - 4; i4 < j2; j2 -= 2) {
                    float curx3 = this.bezierCoords[i4];
                    float cury3 = this.bezierCoords[i4 + 1];
                    this.bezierCoords[i4] = this.bezierCoords[j2];
                    this.bezierCoords[i4 + 1] = this.bezierCoords[j2 + 1];
                    this.bezierCoords[j2] = curx3;
                    this.bezierCoords[j2 + 1] = cury3;
                    i4 += 2;
                }
            }
        }

        private void appendLineTo(float x0, float y0, float x1, float y1) {
            appendCubicTo(ShapeEvaluator.interp(x0, x1, THIRD), ShapeEvaluator.interp(y0, y1, THIRD), ShapeEvaluator.interp(x1, x0, THIRD), ShapeEvaluator.interp(y1, y0, THIRD), x1, y1);
        }

        private void appendQuadTo(float x0, float y0, float ctrlx, float ctrly, float x1, float y1) {
            appendCubicTo(ShapeEvaluator.interp(ctrlx, x0, THIRD), ShapeEvaluator.interp(ctrly, y0, THIRD), ShapeEvaluator.interp(ctrlx, x1, THIRD), ShapeEvaluator.interp(ctrly, y1, THIRD), x1, y1);
        }

        private void appendCubicTo(float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x1, float y1) {
            if (this.numCoords + 6 > this.bezierCoords.length) {
                int newsize = ((this.numCoords - 2) * 2) + 2;
                float[] newCoords = new float[newsize];
                System.arraycopy(this.bezierCoords, 0, newCoords, 0, this.numCoords);
                this.bezierCoords = newCoords;
            }
            float[] fArr = this.bezierCoords;
            int i2 = this.numCoords;
            this.numCoords = i2 + 1;
            fArr[i2] = ctrlx1;
            float[] fArr2 = this.bezierCoords;
            int i3 = this.numCoords;
            this.numCoords = i3 + 1;
            fArr2[i3] = ctrly1;
            float[] fArr3 = this.bezierCoords;
            int i4 = this.numCoords;
            this.numCoords = i4 + 1;
            fArr3[i4] = ctrlx2;
            float[] fArr4 = this.bezierCoords;
            int i5 = this.numCoords;
            this.numCoords = i5 + 1;
            fArr4[i5] = ctrly2;
            float[] fArr5 = this.bezierCoords;
            int i6 = this.numCoords;
            this.numCoords = i6 + 1;
            fArr5[i6] = x1;
            float[] fArr6 = this.bezierCoords;
            int i7 = this.numCoords;
            this.numCoords = i7 + 1;
            fArr6[i7] = y1;
        }

        public int getWindingRule() {
            return this.windingrule;
        }

        public int getNumCoords() {
            return this.numCoords;
        }

        public float getCoord(int i2) {
            return this.bezierCoords[i2];
        }

        public float[] getTvals() {
            if (this.myTvals != null) {
                return this.myTvals;
            }
            float[] tvals = new float[((this.numCoords - 2) / 6) + 1];
            float segx = this.bezierCoords[0];
            float segy = this.bezierCoords[1];
            float tlen = 0.0f;
            int ci = 2;
            int ti = 0;
            while (ci < this.numCoords) {
                int i2 = ci;
                int ci2 = ci + 1;
                float newx = this.bezierCoords[i2];
                int ci3 = ci2 + 1;
                float newy = this.bezierCoords[ci2];
                float prevx = segx - newx;
                float prevy = segy - newy;
                float len = (float) Math.sqrt((prevx * prevx) + (prevy * prevy));
                int ci4 = ci3 + 1;
                float newx2 = this.bezierCoords[ci3];
                int ci5 = ci4 + 1;
                float newy2 = this.bezierCoords[ci4];
                float prevx2 = newx - newx2;
                float prevy2 = newy - newy2;
                float len2 = len + ((float) Math.sqrt((prevx2 * prevx2) + (prevy2 * prevy2)));
                int ci6 = ci5 + 1;
                float newx3 = this.bezierCoords[ci5];
                ci = ci6 + 1;
                float newy3 = this.bezierCoords[ci6];
                float prevx3 = newx2 - newx3;
                float prevy3 = newy2 - newy3;
                float len3 = len2 + ((float) Math.sqrt((prevx3 * prevx3) + (prevy3 * prevy3)));
                float segx2 = segx - newx3;
                float segy2 = segy - newy3;
                float len4 = (len3 + ((float) Math.sqrt((segx2 * segx2) + (segy2 * segy2)))) / 2.0f;
                if (len4 < MIN_LEN) {
                    len4 = 0.001f;
                }
                tlen += len4;
                int i3 = ti;
                ti++;
                tvals[i3] = tlen;
                segx = newx3;
                segy = newy3;
            }
            float prevt = tvals[0];
            tvals[0] = 0.0f;
            int ti2 = 1;
            while (ti2 < tvals.length - 1) {
                float nextt = tvals[ti2];
                tvals[ti2] = prevt / tlen;
                prevt = nextt;
                ti2++;
            }
            tvals[ti2] = 1.0f;
            this.myTvals = tvals;
            return tvals;
        }

        public void setTvals(float[] newTvals) {
            float[] oldCoords = this.bezierCoords;
            float[] newCoords = new float[2 + ((newTvals.length - 1) * 6)];
            float[] oldTvals = getTvals();
            int oldci = 0 + 1;
            float f2 = oldCoords[0];
            float x1 = f2;
            float xc1 = f2;
            float xc0 = f2;
            float x0 = f2;
            int oldci2 = oldci + 1;
            float f3 = oldCoords[oldci];
            float y1 = f3;
            float yc1 = f3;
            float yc0 = f3;
            float y0 = f3;
            int newci = 0 + 1;
            newCoords[0] = x0;
            int newci2 = newci + 1;
            newCoords[newci] = y0;
            float t0 = 0.0f;
            float t1 = 0.0f;
            int oldti = 1;
            int newti = 1;
            while (newti < newTvals.length) {
                if (t0 >= t1) {
                    x0 = x1;
                    y0 = y1;
                    int i2 = oldci2;
                    int oldci3 = oldci2 + 1;
                    xc0 = oldCoords[i2];
                    int oldci4 = oldci3 + 1;
                    yc0 = oldCoords[oldci3];
                    int oldci5 = oldci4 + 1;
                    xc1 = oldCoords[oldci4];
                    int oldci6 = oldci5 + 1;
                    yc1 = oldCoords[oldci5];
                    int oldci7 = oldci6 + 1;
                    x1 = oldCoords[oldci6];
                    oldci2 = oldci7 + 1;
                    y1 = oldCoords[oldci7];
                    int i3 = oldti;
                    oldti++;
                    t1 = oldTvals[i3];
                }
                int i4 = newti;
                newti++;
                float nt = newTvals[i4];
                if (nt < t1) {
                    float relt = (nt - t0) / (t1 - t0);
                    int i5 = newci2;
                    int newci3 = newci2 + 1;
                    float x02 = ShapeEvaluator.interp(x0, xc0, relt);
                    newCoords[i5] = x02;
                    int newci4 = newci3 + 1;
                    float y02 = ShapeEvaluator.interp(y0, yc0, relt);
                    newCoords[newci3] = y02;
                    float xc02 = ShapeEvaluator.interp(xc0, xc1, relt);
                    float yc02 = ShapeEvaluator.interp(yc0, yc1, relt);
                    xc1 = ShapeEvaluator.interp(xc1, x1, relt);
                    yc1 = ShapeEvaluator.interp(yc1, y1, relt);
                    int newci5 = newci4 + 1;
                    float x03 = ShapeEvaluator.interp(x02, xc02, relt);
                    newCoords[newci4] = x03;
                    int newci6 = newci5 + 1;
                    float y03 = ShapeEvaluator.interp(y02, yc02, relt);
                    newCoords[newci5] = y03;
                    xc0 = ShapeEvaluator.interp(xc02, xc1, relt);
                    yc0 = ShapeEvaluator.interp(yc02, yc1, relt);
                    int newci7 = newci6 + 1;
                    float fInterp = ShapeEvaluator.interp(x03, xc0, relt);
                    x0 = fInterp;
                    newCoords[newci6] = fInterp;
                    newci2 = newci7 + 1;
                    float fInterp2 = ShapeEvaluator.interp(y03, yc0, relt);
                    y0 = fInterp2;
                    newCoords[newci7] = fInterp2;
                } else {
                    int i6 = newci2;
                    int newci8 = newci2 + 1;
                    newCoords[i6] = xc0;
                    int newci9 = newci8 + 1;
                    newCoords[newci8] = yc0;
                    int newci10 = newci9 + 1;
                    newCoords[newci9] = xc1;
                    int newci11 = newci10 + 1;
                    newCoords[newci10] = yc1;
                    int newci12 = newci11 + 1;
                    newCoords[newci11] = x1;
                    newci2 = newci12 + 1;
                    newCoords[newci12] = y1;
                }
                t0 = nt;
            }
            this.bezierCoords = newCoords;
            this.numCoords = newCoords.length;
            this.myTvals = newTvals;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/ShapeEvaluator$MorphedShape.class */
    private static class MorphedShape extends Shape {
        Geometry geom0;
        Geometry geom1;

        /* renamed from: t, reason: collision with root package name */
        float f11963t;

        MorphedShape(Geometry geom0, Geometry geom1, float t2) {
            this.geom0 = geom0;
            this.geom1 = geom1;
            this.f11963t = t2;
        }

        public Rectangle getRectangle() {
            return new Rectangle(getBounds());
        }

        @Override // com.sun.javafx.geom.Shape
        public RectBounds getBounds() {
            int n2 = this.geom0.getNumCoords();
            float fInterp = ShapeEvaluator.interp(this.geom0.getCoord(0), this.geom1.getCoord(0), this.f11963t);
            float xmax = fInterp;
            float xmin = fInterp;
            float fInterp2 = ShapeEvaluator.interp(this.geom0.getCoord(1), this.geom1.getCoord(1), this.f11963t);
            float ymax = fInterp2;
            float ymin = fInterp2;
            for (int i2 = 2; i2 < n2; i2 += 2) {
                float x2 = ShapeEvaluator.interp(this.geom0.getCoord(i2), this.geom1.getCoord(i2), this.f11963t);
                float y2 = ShapeEvaluator.interp(this.geom0.getCoord(i2 + 1), this.geom1.getCoord(i2 + 1), this.f11963t);
                if (xmin > x2) {
                    xmin = x2;
                }
                if (ymin > y2) {
                    ymin = y2;
                }
                if (xmax < x2) {
                    xmax = x2;
                }
                if (ymax < y2) {
                    ymax = y2;
                }
            }
            return new RectBounds(xmin, ymin, xmax, ymax);
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2) {
            return Path2D.contains(getPathIterator(null), x2, y2);
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean intersects(float x2, float y2, float w2, float h2) {
            return Path2D.intersects(getPathIterator(null), x2, y2, w2, h2);
        }

        @Override // com.sun.javafx.geom.Shape
        public boolean contains(float x2, float y2, float width, float height) {
            return Path2D.contains(getPathIterator(null), x2, y2, width, height);
        }

        @Override // com.sun.javafx.geom.Shape
        public PathIterator getPathIterator(BaseTransform at2) {
            return new Iterator(at2, this.geom0, this.geom1, this.f11963t);
        }

        @Override // com.sun.javafx.geom.Shape
        public PathIterator getPathIterator(BaseTransform at2, float flatness) {
            return new FlatteningPathIterator(getPathIterator(at2), flatness);
        }

        @Override // com.sun.javafx.geom.Shape
        public Shape copy() {
            return new Path2D(this);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/ShapeEvaluator$Iterator.class */
    private static class Iterator implements PathIterator {

        /* renamed from: at, reason: collision with root package name */
        BaseTransform f11961at;
        Geometry g0;
        Geometry g1;

        /* renamed from: t, reason: collision with root package name */
        float f11962t;
        int cindex;

        public Iterator(BaseTransform at2, Geometry g0, Geometry g1, float t2) {
            this.f11961at = at2;
            this.g0 = g0;
            this.g1 = g1;
            this.f11962t = t2;
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int getWindingRule() {
            return ((double) this.f11962t) < 0.5d ? this.g0.getWindingRule() : this.g1.getWindingRule();
        }

        @Override // com.sun.javafx.geom.PathIterator
        public boolean isDone() {
            return this.cindex > this.g0.getNumCoords();
        }

        @Override // com.sun.javafx.geom.PathIterator
        public void next() {
            if (this.cindex == 0) {
                this.cindex = 2;
            } else {
                this.cindex += 6;
            }
        }

        @Override // com.sun.javafx.geom.PathIterator
        public int currentSegment(float[] coords) {
            int type;
            int n2;
            if (this.cindex == 0) {
                type = 0;
                n2 = 2;
            } else if (this.cindex >= this.g0.getNumCoords()) {
                type = 4;
                n2 = 0;
            } else {
                type = 3;
                n2 = 6;
            }
            if (n2 > 0) {
                for (int i2 = 0; i2 < n2; i2++) {
                    coords[i2] = ShapeEvaluator.interp(this.g0.getCoord(this.cindex + i2), this.g1.getCoord(this.cindex + i2), this.f11962t);
                }
                if (this.f11961at != null) {
                    this.f11961at.transform(coords, 0, coords, 0, n2 / 2);
                }
            }
            return type;
        }
    }
}
