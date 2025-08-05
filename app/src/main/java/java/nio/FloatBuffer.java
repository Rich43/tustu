package java.nio;

/* loaded from: rt.jar:java/nio/FloatBuffer.class */
public abstract class FloatBuffer extends Buffer implements Comparable<FloatBuffer> {
    final float[] hb;
    final int offset;
    boolean isReadOnly;

    public abstract FloatBuffer slice();

    public abstract FloatBuffer duplicate();

    public abstract FloatBuffer asReadOnlyBuffer();

    public abstract float get();

    public abstract FloatBuffer put(float f2);

    public abstract float get(int i2);

    public abstract FloatBuffer put(int i2, float f2);

    public abstract FloatBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    public abstract ByteOrder order();

    FloatBuffer(int i2, int i3, int i4, int i5, float[] fArr, int i6) {
        super(i2, i3, i4, i5);
        this.hb = fArr;
        this.offset = i6;
    }

    FloatBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static FloatBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapFloatBuffer(i2, i2);
    }

    public static FloatBuffer wrap(float[] fArr, int i2, int i3) {
        try {
            return new HeapFloatBuffer(fArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static FloatBuffer wrap(float[] fArr) {
        return wrap(fArr, 0, fArr.length);
    }

    public FloatBuffer get(float[] fArr, int i2, int i3) {
        checkBounds(i2, i3, fArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            fArr[i5] = get();
        }
        return this;
    }

    public FloatBuffer get(float[] fArr) {
        return get(fArr, 0, fArr.length);
    }

    public FloatBuffer put(FloatBuffer floatBuffer) {
        if (floatBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = floatBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(floatBuffer.get());
        }
        return this;
    }

    public FloatBuffer put(float[] fArr, int i2, int i3) {
        checkBounds(i2, i3, fArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(fArr[i5]);
        }
        return this;
    }

    public final FloatBuffer put(float[] fArr) {
        return put(fArr, 0, fArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final float[] array() {
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
        if (!(obj instanceof FloatBuffer)) {
            return false;
        }
        FloatBuffer floatBuffer = (FloatBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = floatBuffer.position();
        int iLimit2 = floatBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), floatBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(float f2, float f3) {
        return f2 == f3 || (Float.isNaN(f2) && Float.isNaN(f3));
    }

    @Override // java.lang.Comparable
    public int compareTo(FloatBuffer floatBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = floatBuffer.position();
        int iLimit2 = floatBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), floatBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(float f2, float f3) {
        if (f2 < f3) {
            return -1;
        }
        if (f2 > f3) {
            return 1;
        }
        if (f2 == f3) {
            return 0;
        }
        if (Float.isNaN(f2)) {
            return Float.isNaN(f3) ? 0 : 1;
        }
        return -1;
    }
}
