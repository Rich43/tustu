package java.nio;

import sun.nio.ch.DirectBuffer;

/* loaded from: rt.jar:java/nio/DirectIntBufferRU.class */
class DirectIntBufferRU extends DirectIntBufferU implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectIntBufferRU.class.desiredAssertionStatus();
    }

    DirectIntBufferRU(DirectBuffer directBuffer, int i2, int i3, int i4, int i5, int i6) {
        super(directBuffer, i2, i3, i4, i5, i6);
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer slice() {
        int iPosition = position();
        int iLimit = limit();
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        int i3 = iPosition << 2;
        if ($assertionsDisabled || i3 >= 0) {
            return new DirectIntBufferRU(this, -1, 0, i2, i2, i3);
        }
        throw new AssertionError();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer duplicate() {
        return new DirectIntBufferRU(this, markValue(), position(), limit(), capacity(), 0);
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer put(int i2) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer put(int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer put(IntBuffer intBuffer) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer put(int[] iArr, int i2, int i3) {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public IntBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer, java.nio.Buffer
    public boolean isDirect() {
        return true;
    }

    @Override // java.nio.DirectIntBufferU, java.nio.Buffer
    public boolean isReadOnly() {
        return true;
    }

    @Override // java.nio.DirectIntBufferU, java.nio.IntBuffer
    public ByteOrder order() {
        return ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
    }
}
