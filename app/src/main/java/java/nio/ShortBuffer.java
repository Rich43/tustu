package java.nio;

/* loaded from: rt.jar:java/nio/ShortBuffer.class */
public abstract class ShortBuffer extends Buffer implements Comparable<ShortBuffer> {
    final short[] hb;
    final int offset;
    boolean isReadOnly;

    public abstract ShortBuffer slice();

    public abstract ShortBuffer duplicate();

    public abstract ShortBuffer asReadOnlyBuffer();

    public abstract short get();

    public abstract ShortBuffer put(short s2);

    public abstract short get(int i2);

    public abstract ShortBuffer put(int i2, short s2);

    public abstract ShortBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    public abstract ByteOrder order();

    ShortBuffer(int i2, int i3, int i4, int i5, short[] sArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = sArr;
        this.offset = i6;
    }

    ShortBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static ShortBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapShortBuffer(i2, i2);
    }

    public static ShortBuffer wrap(short[] sArr, int i2, int i3) {
        try {
            return new HeapShortBuffer(sArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static ShortBuffer wrap(short[] sArr) {
        return wrap(sArr, 0, sArr.length);
    }

    public ShortBuffer get(short[] sArr, int i2, int i3) {
        checkBounds(i2, i3, sArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            sArr[i5] = get();
        }
        return this;
    }

    public ShortBuffer get(short[] sArr) {
        return get(sArr, 0, sArr.length);
    }

    public ShortBuffer put(ShortBuffer shortBuffer) {
        if (shortBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = shortBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(shortBuffer.get());
        }
        return this;
    }

    public ShortBuffer put(short[] sArr, int i2, int i3) {
        checkBounds(i2, i3, sArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(sArr[i5]);
        }
        return this;
    }

    public final ShortBuffer put(short[] sArr) {
        return put(sArr, 0, sArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final short[] array() {
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
        if (!(obj instanceof ShortBuffer)) {
            return false;
        }
        ShortBuffer shortBuffer = (ShortBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = shortBuffer.position();
        int iLimit2 = shortBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), shortBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(short s2, short s3) {
        return s2 == s3;
    }

    @Override // java.lang.Comparable
    public int compareTo(ShortBuffer shortBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = shortBuffer.position();
        int iLimit2 = shortBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), shortBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(short s2, short s3) {
        return Short.compare(s2, s3);
    }
}
