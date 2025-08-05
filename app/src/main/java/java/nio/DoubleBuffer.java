package java.nio;

/* loaded from: rt.jar:java/nio/DoubleBuffer.class */
public abstract class DoubleBuffer extends Buffer implements Comparable<DoubleBuffer> {
    final double[] hb;
    final int offset;
    boolean isReadOnly;

    public abstract DoubleBuffer slice();

    public abstract DoubleBuffer duplicate();

    public abstract DoubleBuffer asReadOnlyBuffer();

    public abstract double get();

    public abstract DoubleBuffer put(double d2);

    public abstract double get(int i2);

    public abstract DoubleBuffer put(int i2, double d2);

    public abstract DoubleBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    public abstract ByteOrder order();

    DoubleBuffer(int i2, int i3, int i4, int i5, double[] dArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = dArr;
        this.offset = i6;
    }

    DoubleBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static DoubleBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapDoubleBuffer(i2, i2);
    }

    public static DoubleBuffer wrap(double[] dArr, int i2, int i3) {
        try {
            return new HeapDoubleBuffer(dArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static DoubleBuffer wrap(double[] dArr) {
        return wrap(dArr, 0, dArr.length);
    }

    public DoubleBuffer get(double[] dArr, int i2, int i3) {
        checkBounds(i2, i3, dArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            dArr[i5] = get();
        }
        return this;
    }

    public DoubleBuffer get(double[] dArr) {
        return get(dArr, 0, dArr.length);
    }

    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        if (doubleBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = doubleBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(doubleBuffer.get());
        }
        return this;
    }

    public DoubleBuffer put(double[] dArr, int i2, int i3) {
        checkBounds(i2, i3, dArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(dArr[i5]);
        }
        return this;
    }

    public final DoubleBuffer put(double[] dArr) {
        return put(dArr, 0, dArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final double[] array() {
        if (this.hb == null) {
            throw new UnsupportedOperationException();
        }
        if (this.isReadOnly) {
            throw new ReadOnlyBufferException();
        }
        return this.hb;
    }

    @Override // java.nio.Buffer
    public final int arrayOffset() {
        if (this.hb == null) {
            throw new UnsupportedOperationException();
        }
        if (this.isReadOnly) {
            throw new ReadOnlyBufferException();
        }
        return this.offset;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getName());
        stringBuffer.append("[pos=");
        stringBuffer.append(position());
        stringBuffer.append(" lim=");
        stringBuffer.append(limit());
        stringBuffer.append(" cap=");
        stringBuffer.append(capacity());
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    public int hashCode() {
        int i2 = 1;
        int iPosition = position();
        for (int iLimit = limit() - 1; iLimit >= iPosition; iLimit--) {
            i2 = (31 * i2) + ((int) get(iLimit));
        }
        return i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DoubleBuffer)) {
            return false;
        }
        DoubleBuffer doubleBuffer = (DoubleBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = doubleBuffer.position();
        int iLimit2 = doubleBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), doubleBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(double d2, double d3) {
        return d2 == d3 || (Double.isNaN(d2) && Double.isNaN(d3));
    }

    @Override // java.lang.Comparable
    public int compareTo(DoubleBuffer doubleBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = doubleBuffer.position();
        int iLimit2 = doubleBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), doubleBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(double d2, double d3) {
        if (d2 < d3) {
            return -1;
        }
        if (d2 > d3) {
            return 1;
        }
        if (d2 == d3) {
            return 0;
        }
        if (Double.isNaN(d2)) {
            return Double.isNaN(d3) ? 0 : 1;
        }
        return -1;
    }
}
