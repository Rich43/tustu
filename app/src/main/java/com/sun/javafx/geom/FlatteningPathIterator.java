package com.sun.javafx.geom;

import java.util.NoSuchElementException;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/FlatteningPathIterator.class */
public class FlatteningPathIterator implements PathIterator {
    static final int GROW_SIZE = 24;
    PathIterator src;
    float squareflat;
    int limit;
    volatile float[] hold;
    float curx;
    float cury;
    float movx;
    float movy;
    int holdType;
    int holdEnd;
    int holdIndex;
    int[] levels;
    int levelIndex;
    boolean done;

    public FlatteningPathIterator(PathIterator src, float flatness) {
        this(src, flatness, 10);
    }

    public FlatteningPathIterator(PathIterator src, float flatness, int limit) {
        this.hold = new float[14];
        if (flatness < 0.0f) {
            throw new IllegalArgumentException("flatness must be >= 0");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be >= 0");
        }
        this.src = src;
        this.squareflat = flatness * flatness;
        this.limit = limit;
        this.levels = new int[limit + 1];
        next(false);
    }

    public float getFlatness() {
        return (float) Math.sqrt(this.squareflat);
    }

    public int getRecursionLimit() {
        return this.limit;
    }

    @Override // com.sun.javafx.geom.PathIterator
    public int getWindingRule() {
        return this.src.getWindingRule();
    }

    @Override // com.sun.javafx.geom.PathIterator
    public boolean isDone() {
        return this.done;
    }

    void ensureHoldCapacity(int want) {
        if (this.holdIndex - want < 0) {
            int have = this.hold.length - this.holdIndex;
            int newsize = this.hold.length + 24;
            float[] newhold = new float[newsize];
            System.arraycopy(this.hold, this.holdIndex, newhold, this.holdIndex + 24, have);
            this.hold = newhold;
            this.holdIndex += 24;
            this.holdEnd += 24;
        }
    }

    @Override // com.sun.javafx.geom.PathIterator
    public void next() {
        next(true);
    }

    private void next(boolean doNext) {
        if (this.holdIndex >= this.holdEnd) {
            if (doNext) {
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
                    float[] fArr = this.hold;
                    int i2 = this.holdIndex + 4;
                    float f2 = this.hold[2];
                    this.curx = f2;
                    fArr[i2] = f2;
                    float[] fArr2 = this.hold;
                    int i3 = this.holdIndex + 5;
                    float f3 = this.hold[3];
                    this.cury = f3;
                    fArr2[i3] = f3;
                }
                int level = this.levels[this.levelIndex];
                while (level < this.limit && QuadCurve2D.getFlatnessSq(this.hold, this.holdIndex) >= this.squareflat) {
                    ensureHoldCapacity(4);
                    QuadCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 4, this.hold, this.holdIndex);
                    this.holdIndex -= 4;
                    level++;
                    this.levels[this.levelIndex] = level;
                    this.levelIndex++;
                    this.levels[this.levelIndex] = level;
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
                    float[] fArr3 = this.hold;
                    int i4 = this.holdIndex + 6;
                    float f4 = this.hold[4];
                    this.curx = f4;
                    fArr3[i4] = f4;
                    float[] fArr4 = this.hold;
                    int i5 = this.holdIndex + 7;
                    float f5 = this.hold[5];
                    this.cury = f5;
                    fArr4[i5] = f5;
                }
                int level2 = this.levels[this.levelIndex];
                while (level2 < this.limit && CubicCurve2D.getFlatnessSq(this.hold, this.holdIndex) >= this.squareflat) {
                    ensureHoldCapacity(6);
                    CubicCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 6, this.hold, this.holdIndex);
                    this.holdIndex -= 6;
                    level2++;
                    this.levels[this.levelIndex] = level2;
                    this.levelIndex++;
                    this.levels[this.levelIndex] = level2;
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

    @Override // com.sun.javafx.geom.PathIterator
    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("flattening iterator out of bounds");
        }
        int type = this.holdType;
        if (type != 4) {
            coords[0] = this.hold[this.holdIndex + 0];
            coords[1] = this.hold[this.holdIndex + 1];
            if (type != 0) {
                type = 1;
            }
        }
        return type;
    }
}
