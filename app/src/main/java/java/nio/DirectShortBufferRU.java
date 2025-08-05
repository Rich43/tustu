package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectShortBufferRU.class */
class DirectShortBufferRU extends DirectShortBufferU implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectShortBufferRU.class.desiredAssertionStatus();
    }

    DirectShortBufferRU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 1;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectShortBufferRU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer duplicate() {
        return new DirectShortBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer put(short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer put(int i2, short s2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer put(ShortBuffer shortBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer put(short[] sArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ShortBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectShortBufferU, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectShortBufferU, java.nio.ShortBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
