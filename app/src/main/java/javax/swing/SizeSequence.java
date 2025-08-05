package javax.swing;

/* loaded from: rt.jar:javax/swing/SizeSequence.class */
public class SizeSequence {
    private static int[] emptyArray = new int[0];

    /* renamed from: a, reason: collision with root package name */
    private int[] f12803a;

    public SizeSequence() {
        this.f12803a = emptyArray;
    }

    public SizeSequence(int i2) {
        this(i2, 0);
    }

    public SizeSequence(int i2, int i3) {
        this();
        insertEntries(0, i2, i3);
    }

    public SizeSequence(int[] iArr) {
        this();
        setSizes(iArr);
    }

    void setSizes(int i2, int i3) {
        if (this.f12803a.length != i2) {
            this.f12803a = new int[i2];
        }
        setSizes(0, i2, i3);
    }

    private int setSizes(int i2, int i3, int i4) {
        if (i3 <= i2) {
            return 0;
        }
        int i5 = (i2 + i3) / 2;
        this.f12803a[i5] = i4 + setSizes(i2, i5, i4);
        return this.f12803a[i5] + setSizes(i5 + 1, i3, i4);
    }

    public void setSizes(int[] iArr) {
        if (this.f12803a.length != iArr.length) {
            this.f12803a = new int[iArr.length];
        }
        setSizes(0, this.f12803a.length, iArr);
    }

    private int setSizes(int i2, int i3, int[] iArr) {
        if (i3 <= i2) {
            return 0;
        }
        int i4 = (i2 + i3) / 2;
        this.f12803a[i4] = iArr[i4] + setSizes(i2, i4, iArr);
        return this.f12803a[i4] + setSizes(i4 + 1, i3, iArr);
    }

    public int[] getSizes() {
        int length = this.f12803a.length;
        int[] iArr = new int[length];
        getSizes(0, length, iArr);
        return iArr;
    }

    private int getSizes(int i2, int i3, int[] iArr) {
        if (i3 <= i2) {
            return 0;
        }
        int i4 = (i2 + i3) / 2;
        iArr[i4] = this.f12803a[i4] - getSizes(i2, i4, iArr);
        return this.f12803a[i4] + getSizes(i4 + 1, i3, iArr);
    }

    public int getPosition(int i2) {
        return getPosition(0, this.f12803a.length, i2);
    }

    private int getPosition(int i2, int i3, int i4) {
        if (i3 <= i2) {
            return 0;
        }
        int i5 = (i2 + i3) / 2;
        if (i4 <= i5) {
            return getPosition(i2, i5, i4);
        }
        return this.f12803a[i5] + getPosition(i5 + 1, i3, i4);
    }

    public int getIndex(int i2) {
        return getIndex(0, this.f12803a.length, i2);
    }

    private int getIndex(int i2, int i3, int i4) {
        if (i3 <= i2) {
            return i2;
        }
        int i5 = (i2 + i3) / 2;
        int i6 = this.f12803a[i5];
        if (i4 < i6) {
            return getIndex(i2, i5, i4);
        }
        return getIndex(i5 + 1, i3, i4 - i6);
    }

    public int getSize(int i2) {
        return getPosition(i2 + 1) - getPosition(i2);
    }

    public void setSize(int i2, int i3) {
        changeSize(0, this.f12803a.length, i2, i3 - getSize(i2));
    }

    private void changeSize(int i2, int i3, int i4, int i5) {
        if (i3 <= i2) {
            return;
        }
        int i6 = (i2 + i3) / 2;
        if (i4 <= i6) {
            int[] iArr = this.f12803a;
            iArr[i6] = iArr[i6] + i5;
            changeSize(i2, i6, i4, i5);
            return;
        }
        changeSize(i6 + 1, i3, i4, i5);
    }

    public void insertEntries(int i2, int i3, int i4) {
        int[] sizes = getSizes();
        int i5 = i2 + i3;
        int length = this.f12803a.length + i3;
        this.f12803a = new int[length];
        for (int i6 = 0; i6 < i2; i6++) {
            this.f12803a[i6] = sizes[i6];
        }
        for (int i7 = i2; i7 < i5; i7++) {
            this.f12803a[i7] = i4;
        }
        for (int i8 = i5; i8 < length; i8++) {
            this.f12803a[i8] = sizes[i8 - i3];
        }
        setSizes(this.f12803a);
    }

    public void removeEntries(int i2, int i3) {
        int[] sizes = getSizes();
        int i4 = i2 + i3;
        int length = this.f12803a.length - i3;
        this.f12803a = new int[length];
        for (int i5 = 0; i5 < i2; i5++) {
            this.f12803a[i5] = sizes[i5];
        }
        for (int i6 = i2; i6 < length; i6++) {
            this.f12803a[i6] = sizes[i6 + i3];
        }
        setSizes(this.f12803a);
    }
}
