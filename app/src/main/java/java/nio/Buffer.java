package java.nio;

/* loaded from: rt.jar:java/nio/Buffer.class */
public abstract class Buffer {
    static final int SPLITERATOR_CHARACTERISTICS = 16464;
    private int mark;
    private int position = 0;
    private int limit;
    private int capacity;
    long address;

    public abstract boolean isReadOnly();

    public abstract boolean hasArray();

    public abstract Object array();

    public abstract int arrayOffset();

    public abstract boolean isDirect();

    Buffer(int i2, int i3, int i4, int i5) {
        this.mark = -1;
        if (i5 < 0) {
            throw new IllegalArgumentException("Negative capacity: " + i5);
        }
        this.capacity = i5;
        limit(i4);
        position(i3);
        if (i2 >= 0) {
            if (i2 > i3) {
                throw new IllegalArgumentException("mark > position: (" + i2 + " > " + i3 + ")");
            }
            this.mark = i2;
        }
    }

    public final int capacity() {
        return this.capacity;
    }

    public final int position() {
        return this.position;
    }

    public final Buffer position(int i2) {
        if (i2 > this.limit || i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (this.mark > i2) {
            this.mark = -1;
        }
        this.position = i2;
        return this;
    }

    public final int limit() {
        return this.limit;
    }

    public final Buffer limit(int i2) {
        if (i2 > this.capacity || i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.limit = i2;
        if (this.position > i2) {
            this.position = i2;
        }
        if (this.mark > i2) {
            this.mark = -1;
        }
        return this;
    }

    public final Buffer mark() {
        this.mark = this.position;
        return this;
    }

    public final Buffer reset() {
        int i2 = this.mark;
        if (i2 < 0) {
            throw new InvalidMarkException();
        }
        this.position = i2;
        return this;
    }

    public final Buffer clear() {
        this.position = 0;
        this.limit = this.capacity;
        this.mark = -1;
        return this;
    }

    public final Buffer flip() {
        this.limit = this.position;
        this.position = 0;
        this.mark = -1;
        return this;
    }

    public final Buffer rewind() {
        this.position = 0;
        this.mark = -1;
        return this;
    }

    public final int remaining() {
        int i2 = this.limit - this.position;
        if (i2 > 0) {
            return i2;
        }
        return 0;
    }

    public final boolean hasRemaining() {
        return this.position < this.limit;
    }

    final int nextGetIndex() {
        int i2 = this.position;
        if (i2 >= this.limit) {
            throw new BufferUnderflowException();
        }
        this.position = i2 + 1;
        return i2;
    }

    final int nextGetIndex(int i2) {
        int i3 = this.position;
        if (this.limit - i3 < i2) {
            throw new BufferUnderflowException();
        }
        this.position = i3 + i2;
        return i3;
    }

    final int nextPutIndex() {
        int i2 = this.position;
        if (i2 >= this.limit) {
            throw new BufferOverflowException();
        }
        this.position = i2 + 1;
        return i2;
    }

    final int nextPutIndex(int i2) {
        int i3 = this.position;
        if (this.limit - i3 < i2) {
            throw new BufferOverflowException();
        }
        this.position = i3 + i2;
        return i3;
    }

    final int checkIndex(int i2) {
        if (i2 < 0 || i2 >= this.limit) {
            throw new IndexOutOfBoundsException();
        }
        return i2;
    }

    final int checkIndex(int i2, int i3) {
        if (i2 < 0 || i3 > this.limit - i2) {
            throw new IndexOutOfBoundsException();
        }
        return i2;
    }

    final int markValue() {
        return this.mark;
    }

    final void truncate() {
        this.mark = -1;
        this.position = 0;
        this.limit = 0;
        this.capacity = 0;
    }

    final void discardMark() {
        this.mark = -1;
    }

    static void checkBounds(int i2, int i3, int i4) {
        if ((i2 | i3 | (i2 + i3) | (i4 - (i2 + i3))) < 0) {
            throw new IndexOutOfBoundsException();
        }
    }
}
