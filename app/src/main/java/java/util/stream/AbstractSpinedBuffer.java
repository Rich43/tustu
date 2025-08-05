package java.util.stream;

/* loaded from: rt.jar:java/util/stream/AbstractSpinedBuffer.class */
abstract class AbstractSpinedBuffer {
    public static final int MIN_CHUNK_POWER = 4;
    public static final int MIN_CHUNK_SIZE = 16;
    public static final int MAX_CHUNK_POWER = 30;
    public static final int MIN_SPINE_SIZE = 8;
    protected final int initialChunkPower;
    protected int elementIndex;
    protected int spineIndex;
    protected long[] priorElementCount;

    public abstract void clear();

    protected AbstractSpinedBuffer() {
        this.initialChunkPower = 4;
    }

    protected AbstractSpinedBuffer(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
        this.initialChunkPower = Math.max(4, 32 - Integer.numberOfLeadingZeros(i2 - 1));
    }

    public boolean isEmpty() {
        return this.spineIndex == 0 && this.elementIndex == 0;
    }

    public long count() {
        return this.spineIndex == 0 ? this.elementIndex : this.priorElementCount[this.spineIndex] + this.elementIndex;
    }

    protected int chunkSize(int i2) {
        return 1 << ((i2 == 0 || i2 == 1) ? this.initialChunkPower : Math.min((this.initialChunkPower + i2) - 1, 30));
    }
}
