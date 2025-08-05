package java.nio;

/* loaded from: rt.jar:java/nio/ByteBuffer.class */
public abstract class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {
    final byte[] hb;
    final int offset;
    boolean isReadOnly;
    boolean bigEndian;
    boolean nativeByteOrder;

    public abstract ByteBuffer slice();

    public abstract ByteBuffer duplicate();

    public abstract ByteBuffer asReadOnlyBuffer();

    public abstract byte get();

    public abstract ByteBuffer put(byte b2);

    public abstract byte get(int i2);

    public abstract ByteBuffer put(int i2, byte b2);

    public abstract ByteBuffer compact();

    @Override // java.nio.Buffer
    public abstract boolean isDirect();

    abstract byte _get(int i2);

    abstract void _put(int i2, byte b2);

    public abstract char getChar();

    public abstract ByteBuffer putChar(char c2);

    public abstract char getChar(int i2);

    public abstract ByteBuffer putChar(int i2, char c2);

    public abstract CharBuffer asCharBuffer();

    public abstract short getShort();

    public abstract ByteBuffer putShort(short s2);

    public abstract short getShort(int i2);

    public abstract ByteBuffer putShort(int i2, short s2);

    public abstract ShortBuffer asShortBuffer();

    public abstract int getInt();

    public abstract ByteBuffer putInt(int i2);

    public abstract int getInt(int i2);

    public abstract ByteBuffer putInt(int i2, int i3);

    public abstract IntBuffer asIntBuffer();

    public abstract long getLong();

    public abstract ByteBuffer putLong(long j2);

    public abstract long getLong(int i2);

    public abstract ByteBuffer putLong(int i2, long j2);

    public abstract LongBuffer asLongBuffer();

    public abstract float getFloat();

    public abstract ByteBuffer putFloat(float f2);

    public abstract float getFloat(int i2);

    public abstract ByteBuffer putFloat(int i2, float f2);

    public abstract FloatBuffer asFloatBuffer();

    public abstract double getDouble();

    public abstract ByteBuffer putDouble(double d2);

    public abstract double getDouble(int i2);

    public abstract ByteBuffer putDouble(int i2, double d2);

    public abstract DoubleBuffer asDoubleBuffer();

    ByteBuffer(int i2, int i3, int i4, int i5, byte[] bArr, int i6) {
        super(i2, i3, i4, i5);
        this.bigEndian = true;
        this.nativeByteOrder = Bits.byteOrder() == ByteOrder.BIG_ENDIAN;
        this.hb = bArr;
        this.offset = i6;
    }

    ByteBuffer(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0);
    }

    public static ByteBuffer allocateDirect(int i2) {
        return new DirectByteBuffer(i2);
    }

    public static ByteBuffer allocate(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapByteBuffer(i2, i2);
    }

    public static ByteBuffer wrap(byte[] bArr, int i2, int i3) {
        try {
            return new HeapByteBuffer(bArr, i2, i3);
        } catch (IllegalArgumentException e2) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static ByteBuffer wrap(byte[] bArr) {
        return wrap(bArr, 0, bArr.length);
    }

    public ByteBuffer get(byte[] bArr, int i2, int i3) {
        checkBounds(i2, i3, bArr.length);
        if (i3 > remaining()) {
            throw new BufferUnderflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            bArr[i5] = get();
        }
        return this;
    }

    public ByteBuffer get(byte[] bArr) {
        return get(bArr, 0, bArr.length);
    }

    public ByteBuffer put(ByteBuffer byteBuffer) {
        if (byteBuffer == this) {
            throw new IllegalArgumentException();
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        int iRemaining = byteBuffer.remaining();
        if (iRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i2 = 0; i2 < iRemaining; i2++) {
            put(byteBuffer.get());
        }
        return this;
    }

    public ByteBuffer put(byte[] bArr, int i2, int i3) {
        checkBounds(i2, i3, bArr.length);
        if (i3 > remaining()) {
            throw new BufferOverflowException();
        }
        int i4 = i2 + i3;
        for (int i5 = i2; i5 < i4; i5++) {
            put(bArr[i5]);
        }
        return this;
    }

    public final ByteBuffer put(byte[] bArr) {
        return put(bArr, 0, bArr.length);
    }

    @Override // java.nio.Buffer
    public final boolean hasArray() {
        return (this.hb == null || this.isReadOnly) ? false : true;
    }

    @Override // java.nio.Buffer
    public final byte[] array() {
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
        if (!(obj instanceof ByteBuffer)) {
            return false;
        }
        ByteBuffer byteBuffer = (ByteBuffer) obj;
        int iPosition = position();
        int iLimit = limit();
        int iPosition2 = byteBuffer.position();
        int iLimit2 = byteBuffer.limit();
        int i2 = iLimit - iPosition;
        int i3 = iLimit2 - iPosition2;
        if (i2 < 0 || i2 != i3) {
            return false;
        }
        int i4 = iLimit - 1;
        int i5 = iLimit2 - 1;
        while (i4 >= iPosition) {
            if (equals(get(i4), byteBuffer.get(i5))) {
                i4--;
                i5--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean equals(byte b2, byte b3) {
        return b2 == b3;
    }

    @Override // java.lang.Comparable
    public int compareTo(ByteBuffer byteBuffer) {
        int iPosition = position();
        int iLimit = limit() - iPosition;
        int iPosition2 = byteBuffer.position();
        int iLimit2 = byteBuffer.limit() - iPosition2;
        if (Math.min(iLimit, iLimit2) < 0) {
            return -1;
        }
        int iMin = iPosition + Math.min(iLimit, iLimit2);
        int i2 = iPosition;
        int i3 = iPosition2;
        while (i2 < iMin) {
            int iCompare = compare(get(i2), byteBuffer.get(i3));
            if (iCompare == 0) {
                i2++;
                i3++;
            } else {
                return iCompare;
            }
        }
        return iLimit - iLimit2;
    }

    private static int compare(byte b2, byte b3) {
        return Byte.compare(b2, b3);
    }

    public final ByteOrder order() {
        return this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }

    public final ByteBuffer order(ByteOrder byteOrder) {
        this.bigEndian = byteOrder == ByteOrder.BIG_ENDIAN;
        this.nativeByteOrder = this.bigEndian == (Bits.byteOrder() == ByteOrder.BIG_ENDIAN);
        return this;
    }
}
