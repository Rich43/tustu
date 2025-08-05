package java.nio;

/* loaded from: rt.jar:java/nio/LongBuffer.class */
public abstract class LongBuffer extends Buffer implements Comparable<LongBuffer> {
    final long[] hb;
    final int offset;
    boolean isReadOnly;

    public abstract LongBuffer slice();

    public abstract LongBuffer duplicate();

    public abstract LongBuffer asReadOnlyBuffer();

    public abstract long get();

    public abstract LongBuffer put(long j2);

    public abstract long get(int i2);

    public abstract LongBuffer put(int i2, long j2);

    public abstract LongBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    public abstract ByteOrder order();

    LongBuffer(int i2, int i3, int i4, int i5, long[] jArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = jArr;
        this.offset = i6;
    }

    LongBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static LongBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapLongBuffer(i2, i2);
    }

    public static LongBuffer wrap(long[] jArr, int i2, int i3) {
        try {
            return new HeapLongBuffer(jArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static LongBuffer wrap(long[] jArr) {
        return wrap(jArr, 0, jArr.length);
    }

    public LongBuffer get(long[] jArr, int i2, int i3) {
        checkBounds(i2, i3, jArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            jArr[i5] = get();
        }
        return this;
    }

    public LongBuffer get(long[] jArr) {
        return get(jArr, 0, jArr.length);
    }

    public LongBuffer put(LongBuffer longBuffer) {
        if (longBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = longBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(longBuffer.get());
        }
        return this;
    }

    public LongBuffer put(long[] jArr, int i2, int i3) {
        checkBounds(i2, i3, jArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(jArr[i5]);
        }
        return this;
    }

    public final LongBuffer put(long[] jArr) {
        return put(jArr, 0, jArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final long[] array() {
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
        if (!(obj instanceof LongBuffer)) {
            return false;
        }
        LongBuffer longBuffer = (LongBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = longBuffer.position();
        int iLimit2 = longBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), longBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(long j2, long j3) {
        return j2 == j3;
    }

    @Override // java.lang.Comparable
    public int compareTo(LongBuffer longBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = longBuffer.position();
        int iLimit2 = longBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), longBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(long j2, long j3) {
        return Long.compare(j2, j3);
    }
}
