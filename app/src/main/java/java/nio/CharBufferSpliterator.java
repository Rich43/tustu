package java.nio;

import java.util.Spliterator;
import java.util.function.IntConsumer;

/* loaded from: rt.jar:java/nio/CharBufferSpliterator.class */
class CharBufferSpliterator implements Spliterator.OfInt {
    private final CharBuffer buffer;
    private int index;
    private final int limit;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CharBufferSpliterator.class.desiredAssertionStatus();
    }

    CharBufferSpliterator(CharBuffer charBuffer) {
        this(charBuffer, charBuffer.position(), charBuffer.limit());
    }

    CharBufferSpliterator(CharBuffer charBuffer, int i2, int i3) {
        if (!$assertionsDisabled && i2 > i3) {
            throw new AssertionError();
        }
        this.buffer = charBuffer;
        this.index = i2 <= i3 ? i2 : i3;
        this.limit = i3;
    }

    @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
    public Spliterator.OfInt trySplit() {
        int i2 = this.index;
        int i3 = (i2 + this.limit) >>> 1;
        if (i2 >= i3) {
            return null;
        }
        CharBuffer charBuffer = this.buffer;
        this.index = i3;
        return new CharBufferSpliterator(charBuffer, i2, i3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
    public void forEachRemaining(IntConsumer intConsumer) {
        if (intConsumer == null) {
            throw new NullPointerException();
        }
        CharBuffer charBuffer = this.buffer;
        int i2 = this.index;
        int i3 = this.limit;
        this.index = i3;
        while (i2 < i3) {
            int i4 = i2;
            i2++;
            intConsumer.accept(charBuffer.getUnchecked(i4));
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
    public boolean tryAdvance(IntConsumer intConsumer) {
        if (intConsumer == null) {
            throw new NullPointerException();
        }
        if (this.index >= 0 && this.index < this.limit) {
            CharBuffer charBuffer = this.buffer;
            int i2 = this.index;
            this.index = i2 + 1;
            intConsumer.accept(charBuffer.getUnchecked(i2));
            return true;
        }
        return false;
    }

    @Override // java.util.Spliterator
    public long estimateSize() {
        return this.limit - this.index;
    }

    @Override // java.util.Spliterator
    public int characteristics() {
        return 16464;
    }
}
