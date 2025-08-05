package javax.swing.text;

import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/text/GapVector.class */
abstract class GapVector implements Serializable {
    private Object array;
    private int g0;
    private int g1;

    protected abstract Object allocateArray(int i2);

    protected abstract int getArrayLength();

    public GapVector() {
        this(10);
    }

    public GapVector(int i2) {
        this.array = allocateArray(i2);
        this.g0 = 0;
        this.g1 = i2;
    }

    protected final Object getArray() {
        return this.array;
    }

    protected final int getGapStart() {
        return this.g0;
    }

    protected final int getGapEnd() {
        return this.g1;
    }

    protected void replace(int i2, int i3, Object obj, int i4) {
        if (i4 == 0) {
            close(i2, i3);
            return;
        }
        if (i3 > i4) {
            close(i2 + i4, i3 - i4);
        } else {
            int i5 = i4 - i3;
            System.arraycopy(obj, i3, this.array, open(i2 + i3, i5), i5);
            i4 = i3;
        }
        System.arraycopy(obj, 0, this.array, i2, i4);
    }

    void close(int i2, int i3) {
        if (i3 == 0) {
            return;
        }
        int i4 = i2 + i3;
        int i5 = (this.g1 - this.g0) + i3;
        if (i4 <= this.g0) {
            if (this.g0 != i4) {
                shiftGap(i4);
            }
            shiftGapStartDown(this.g0 - i3);
        } else if (i2 >= this.g0) {
            if (this.g0 != i2) {
                shiftGap(i2);
            }
            shiftGapEndUp(this.g0 + i5);
        } else {
            shiftGapStartDown(i2);
            shiftGapEndUp(this.g0 + i5);
        }
    }

    int open(int i2, int i3) {
        int i4 = this.g1 - this.g0;
        if (i3 == 0) {
            if (i2 > this.g0) {
                i2 += i4;
            }
            return i2;
        }
        shiftGap(i2);
        if (i3 >= i4) {
            shiftEnd((getArrayLength() - i4) + i3);
            int i5 = this.g1 - this.g0;
        }
        this.g0 += i3;
        return i2;
    }

    void resize(int i2) {
        Object objAllocateArray = allocateArray(i2);
        System.arraycopy(this.array, 0, objAllocateArray, 0, Math.min(i2, getArrayLength()));
        this.array = objAllocateArray;
    }

    protected void shiftEnd(int i2) {
        int arrayLength = getArrayLength();
        int i3 = this.g1;
        int i4 = arrayLength - i3;
        int newArraySize = getNewArraySize(i2);
        int i5 = newArraySize - i4;
        resize(newArraySize);
        this.g1 = i5;
        if (i4 != 0) {
            System.arraycopy(this.array, i3, this.array, i5, i4);
        }
    }

    int getNewArraySize(int i2) {
        return (i2 + 1) * 2;
    }

    protected void shiftGap(int i2) {
        if (i2 == this.g0) {
            return;
        }
        int i3 = this.g0;
        int i4 = i2 - i3;
        int i5 = this.g1;
        int i6 = i5 + i4;
        int i7 = i5 - i3;
        this.g0 = i2;
        this.g1 = i6;
        if (i4 > 0) {
            System.arraycopy(this.array, i5, this.array, i3, i4);
        } else if (i4 < 0) {
            System.arraycopy(this.array, i2, this.array, i6, -i4);
        }
    }

    protected void shiftGapStartDown(int i2) {
        this.g0 = i2;
    }

    protected void shiftGapEndUp(int i2) {
        this.g1 = i2;
    }
}
