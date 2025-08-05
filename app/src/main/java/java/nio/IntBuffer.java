package java.nio;

/* loaded from: rt.jar:java/nio/IntBuffer.class */
public abstract class IntBuffer extends Buffer implements Comparable<IntBuffer> {
    final int[] hb;
    final int offset;
    boolean isReadOnly;

    public abstract IntBuffer slice();

    public abstract IntBuffer duplicate();

    public abstract IntBuffer asReadOnlyBuffer();

    public abstract int get();

    public abstract IntBuffer put(int i2);

    public abstract int get(int i2);

    public abstract IntBuffer put(int i2, int i3);

    public abstract IntBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    public abstract ByteOrder order();

    IntBuffer(int i2, int i3, int i4, int i5, int[] iArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = iArr;
        this.offset = i6;
    }

    IntBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static IntBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapIntBuffer(i2, i2);
    }

    public static IntBuffer wrap(int[] iArr, int i2, int i3) {
        try {
            return new HeapIntBuffer(iArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static IntBuffer wrap(int[] iArr) {
        return wrap(iArr, 0, iArr.length);
    }

    public IntBuffer get(int[] iArr, int i2, int i3) {
        checkBounds(i2, i3, iArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            iArr[i5] = get();
        }
        return this;
    }

    public IntBuffer get(int[] iArr) {
        return get(iArr, 0, iArr.length);
    }

    public IntBuffer put(IntBuffer intBuffer) {
        if (intBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = intBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(intBuffer.get());
        }
        return this;
    }

    public IntBuffer put(int[] iArr, int i2, int i3) {
        checkBounds(i2, i3, iArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(iArr[i5]);
        }
        return this;
    }

    public final IntBuffer put(int[] iArr) {
        return put(iArr, 0, iArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final int[] array() {
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
            i2 = (31 * i2) + get(iLimit);
        }
        return i2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntBuffer)) {
            return false;
        }
        IntBuffer intBuffer = (IntBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = intBuffer.position();
        int iLimit2 = intBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), intBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(int i2, int i3) {
        return i2 == i3;
    }

    @Override // java.lang.Comparable
    public int compareTo(IntBuffer intBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = intBuffer.position();
        int iLimit2 = intBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), intBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(int i2, int i3) {
        return Integer.compare(i2, i3);
    }
}
