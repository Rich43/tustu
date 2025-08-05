package java.awt.geom;

import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/awt/geom/FlatteningPathIterator.class */
public class FlatteningPathIterator implements PathIterator {
    static final int GROW_SIZE = 24;
    PathIterator src;
    double squareflat;
    int limit;
    double[] hold;
    double curx;
    double cury;
    double movx;
    double movy;
    int holdType;
    int holdEnd;
    int holdIndex;
    int[] levels;
    int levelIndex;
    boolean done;

    public FlatteningPathIterator(PathIterator pathIterator, double d2) {
        this(pathIterator, d2, 10);
    }

    public FlatteningPathIterator(PathIterator pathIterator, double d2, int i2) {
        this.hold = new double[14];
        if (d2 < 0.0d) {
            throw new IllegalArgumentException("flatness must be >= 0");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("limit must be >= 0");
        }
        this.src = pathIterator;
        this.squareflat = d2 * d2;
        this.limit = i2;
        this.levels = new int[i2 + 1];
        next(false);
    }

    public double getFlatness() {
        return Math.sqrt(this.squareflat);
    }

    public int getRecursionLimit() {
        return this.limit;
    }

    @Override // java.awt.geom.PathIterator
    public int getWindingRule() {
        return this.src.getWindingRule();
    }

    @Override // java.awt.geom.PathIterator
    public boolean isDone() {
        return this.done;
    }

    void ensureHoldCapacity(int i2) {
        if (this.holdIndex - i2 < 0) {
            int length = this.hold.length - this.holdIndex;
            double[] dArr = new double[this.hold.length + 24];
            System.arraycopy(this.hold, this.holdIndex, dArr, this.holdIndex + 24, length);
            this.hold = dArr;
            this.holdIndex += 24;
            this.holdEnd += 24;
        }
    }

    @Override // java.awt.geom.PathIterator
    public void next() {
        next(true);
    }

    private void next(boolean z2) {
        if (this.holdIndex >= this.holdEnd) {
            if (z2) {
                this.src.next();
            }
            if (this.src.isDone()) {
                this.done = true;
            } else {
                this.holdType = this.src.currentSegment(this.hold);
                this.levelIndex = 0;
                this.levels[0] = 0;
            }
        }
        switch (this.holdType) {
            case 0:
            case 1:
                this.curx = this.hold[0];
                this.cury = this.hold[1];
                if (this.holdType == 0) {
                    this.movx = this.curx;
                    this.movy = this.cury;
                }
                this.holdIndex = 0;
                this.holdEnd = 0;
                break;
            case 2:
                if (this.holdIndex >= this.holdEnd) {
                    this.holdIndex = this.hold.length - 6;
                    this.holdEnd = this.hold.length - 2;
                    this.hold[this.holdIndex + 0] = this.curx;
                    this.hold[this.holdIndex + 1] = this.cury;
                    this.hold[this.holdIndex + 2] = this.hold[0];
                    this.hold[this.holdIndex + 3] = this.hold[1];
                    double[] dArr = this.hold;
                    int i2 = this.holdIndex + 4;
                    double d2 = this.hold[2];
                    this.curx = d2;
                    dArr[i2] = d2;
                    double[] dArr2 = this.hold;
                    int i3 = this.holdIndex + 5;
                    double d3 = this.hold[3];
                    this.cury = d3;
                    dArr2[i3] = d3;
                }
                int i4 = this.levels[this.levelIndex];
                while (i4 < this.limit && QuadCurve2D.getFlatnessSq(this.hold, this.holdIndex) >= this.squareflat) {
                    ensureHoldCapacity(4);
                    QuadCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 4, this.hold, this.holdIndex);
                    this.holdIndex -= 4;
                    i4++;
                    this.levels[this.levelIndex] = i4;
                    this.levelIndex++;
                    this.levels[this.levelIndex] = i4;
                }
                this.holdIndex += 4;
                this.levelIndex--;
                break;
            case 3:
                if (this.holdIndex >= this.holdEnd) {
                    this.holdIndex = this.hold.length - 8;
                    this.holdEnd = this.hold.length - 2;
                    this.hold[this.holdIndex + 0] = this.curx;
                    this.hold[this.holdIndex + 1] = this.cury;
                    this.hold[this.holdIndex + 2] = this.hold[0];
                    this.hold[this.holdIndex + 3] = this.hold[1];
                    this.hold[this.holdIndex + 4] = this.hold[2];
                    this.hold[this.holdIndex + 5] = this.hold[3];
                    double[] dArr3 = this.hold;
                    int i5 = this.holdIndex + 6;
                    double d4 = this.hold[4];
                    this.curx = d4;
                    dArr3[i5] = d4;
                    double[] dArr4 = this.hold;
                    int i6 = this.holdIndex + 7;
                    double d5 = this.hold[5];
                    this.cury = d5;
                    dArr4[i6] = d5;
                }
                int i7 = this.levels[this.levelIndex];
                while (i7 < this.limit && CubicCurve2D.getFlatnessSq(this.hold, this.holdIndex) >= this.squareflat) {
                    ensureHoldCapacity(6);
                    CubicCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 6, this.hold, this.holdIndex);
                    this.holdIndex -= 6;
                    i7++;
                    this.levels[this.levelIndex] = i7;
                    this.levelIndex++;
                    this.levels[this.levelIndex] = i7;
                }
                this.holdIndex += 6;
                this.levelIndex--;
                break;
            case 4:
                this.curx = this.movx;
                this.cury = this.movy;
                this.holdIndex = 0;
                this.holdEnd = 0;
                break;
        }
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(float[] fArr) {
        if (isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int i2 = this.holdType;
        if (i2 != 4) {
            fArr[0] = (float) this.hold[this.holdIndex + 0];
            fArr[1] = (float) this.hold[this.holdIndex + 1];
            if (i2 != 0) {
                i2 = 1;
            }
        }
        return i2;
    }

    @Override // java.awt.geom.PathIterator
    public int currentSegment(double[] dArr) {
        if (isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int i2 = this.holdType;
        if (i2 != 4) {
            dArr[0] = this.hold[this.holdIndex + 0];
            dArr[1] = this.hold[this.holdIndex + 1];
            if (i2 != 0) {
                i2 = 1;
            }
        }
        return i2;
    }
}
