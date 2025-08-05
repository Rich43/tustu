package sun.java2d.pisces;

import java.util.Arrays;

/* loaded from: rt.jar:sun/java2d/pisces/PiscesCache.class */
final class PiscesCache {
    final int bboxX0;
    final int bboxY0;
    final int bboxX1;
    final int bboxY1;
    final int[][] rowAARLE;
    private int x0;
    private int y0;
    private final int[][] touchedTile;
    static final int TILE_SIZE_LG = 5;
    static final int TILE_SIZE = 32;
    private static final int INIT_ROW_SIZE = 8;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PiscesCache.class.desiredAssertionStatus();
    }

    PiscesCache(int i2, int i3, int i4, int i5) {
        this.x0 = Integer.MIN_VALUE;
        this.y0 = Integer.MIN_VALUE;
        if (!$assertionsDisabled && (i5 < i3 || i4 < i2)) {
            throw new AssertionError();
        }
        this.bboxX0 = i2;
        this.bboxY0 = i3;
        this.bboxX1 = i4 + 1;
        this.bboxY1 = i5 + 1;
        this.rowAARLE = new int[(this.bboxY1 - this.bboxY0) + 1][8];
        this.x0 = 0;
        this.y0 = -1;
        this.touchedTile = new int[((i5 - i3) + 32) >> 5][((i4 - i2) + 32) >> 5];
    }

    void addRLERun(int i2, int i3) {
        if (i3 > 0) {
            addTupleToRow(this.y0, i2, i3);
            if (i2 != 0) {
                int i4 = this.x0 >> 5;
                int i5 = this.y0 >> 5;
                int length = ((this.x0 + i3) - 1) >> 5;
                if (length >= this.touchedTile[i5].length) {
                    length = this.touchedTile[i5].length - 1;
                }
                if (i4 <= length) {
                    int i6 = (i4 + 1) << 5;
                    if (i6 > this.x0 + i3) {
                        int[] iArr = this.touchedTile[i5];
                        iArr[i4] = iArr[i4] + (i2 * i3);
                    } else {
                        int[] iArr2 = this.touchedTile[i5];
                        iArr2[i4] = iArr2[i4] + (i2 * (i6 - this.x0));
                    }
                    i4++;
                }
                while (i4 < length) {
                    int[] iArr3 = this.touchedTile[i5];
                    int i7 = i4;
                    iArr3[i7] = iArr3[i7] + (i2 << 5);
                    i4++;
                }
                if (i4 == length) {
                    int[] iArr4 = this.touchedTile[i5];
                    int i8 = i4;
                    iArr4[i8] = iArr4[i8] + (i2 * (Math.min(this.x0 + i3, (i4 + 1) << 5) - (i4 << 5)));
                }
            }
            this.x0 += i3;
        }
    }

    void startRow(int i2, int i3) {
        if (!$assertionsDisabled && i2 - this.bboxY0 <= this.y0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 > this.bboxY1) {
            throw new AssertionError();
        }
        this.y0 = i2 - this.bboxY0;
        if (!$assertionsDisabled && this.rowAARLE[this.y0][1] != 0) {
            throw new AssertionError();
        }
        this.x0 = i3 - this.bboxX0;
        if (!$assertionsDisabled && this.x0 < 0) {
            throw new AssertionError((Object) "Input must not be to the left of bbox bounds");
        }
        this.rowAARLE[this.y0][0] = i3;
        this.rowAARLE[this.y0][1] = 2;
    }

    int alphaSumInTile(int i2, int i3) {
        int i4 = i2 - this.bboxX0;
        return this.touchedTile[(i3 - this.bboxY0) >> 5][i4 >> 5];
    }

    int minTouched(int i2) {
        return this.rowAARLE[i2][0];
    }

    int rowLength(int i2) {
        return this.rowAARLE[i2][1];
    }

    private void addTupleToRow(int i2, int i3, int i4) {
        int i5 = this.rowAARLE[i2][1];
        this.rowAARLE[i2] = Helpers.widenArray(this.rowAARLE[i2], i5, 2);
        int i6 = i5 + 1;
        this.rowAARLE[i2][i5] = i3;
        this.rowAARLE[i2][i6] = i4;
        this.rowAARLE[i2][1] = i6 + 1;
    }

    public String toString() {
        String str;
        String str2 = "bbox = [" + this.bboxX0 + ", " + this.bboxY0 + " => " + this.bboxX1 + ", " + this.bboxY1 + "]\n";
        for (int[] iArr : this.rowAARLE) {
            if (iArr != null) {
                str = str2 + "minTouchedX=" + iArr[0] + "\tRLE Entries: " + Arrays.toString(Arrays.copyOfRange(iArr, 2, iArr[1])) + "\n";
            } else {
                str = str2 + "[]\n";
            }
            str2 = str;
        }
        return str2;
    }
}
