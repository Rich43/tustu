package sun.management.counter.perf;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/PerfDataEntry.class */
class PerfDataEntry {
    private String name;
    private int entryStart;
    private int entryLength;
    private int vectorLength;
    private PerfDataType dataType;
    private int flags;
    private Units unit;
    private Variability variability;
    private int dataOffset;
    private int dataSize;
    private ByteBuffer data;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/management/counter/perf/PerfDataEntry$EntryFieldOffset.class */
    private class EntryFieldOffset {
        private static final int SIZEOF_BYTE = 1;
        private static final int SIZEOF_INT = 4;
        private static final int SIZEOF_LONG = 8;
        private static final int ENTRY_LENGTH_SIZE = 4;
        private static final int NAME_OFFSET_SIZE = 4;
        private static final int VECTOR_LENGTH_SIZE = 4;
        private static final int DATA_TYPE_SIZE = 1;
        private static final int FLAGS_SIZE = 1;
        private static final int DATA_UNIT_SIZE = 1;
        private static final int DATA_VAR_SIZE = 1;
        private static final int DATA_OFFSET_SIZE = 4;
        static final int ENTRY_LENGTH = 0;
        static final int NAME_OFFSET = 4;
        static final int VECTOR_LENGTH = 8;
        static final int DATA_TYPE = 12;
        static final int FLAGS = 13;
        static final int DATA_UNIT = 14;
        static final int DATA_VAR = 15;
        static final int DATA_OFFSET = 16;

        private EntryFieldOffset() {
        }
    }

    static {
        $assertionsDisabled = !PerfDataEntry.class.desiredAssertionStatus();
    }

    PerfDataEntry(ByteBuffer byteBuffer) {
        this.entryStart = byteBuffer.position();
        this.entryLength = byteBuffer.getInt();
        if (this.entryLength <= 0 || this.entryLength > byteBuffer.limit()) {
            throw new InstrumentationException("Invalid entry length:  entryLength = " + this.entryLength);
        }
        if (this.entryStart + this.entryLength > byteBuffer.limit()) {
            throw new InstrumentationException("Entry extends beyond end of buffer:  entryStart = " + this.entryStart + " entryLength = " + this.entryLength + " buffer limit = " + byteBuffer.limit());
        }
        byteBuffer.position(this.entryStart + 4);
        int i2 = byteBuffer.getInt();
        if (this.entryStart + i2 > byteBuffer.limit()) {
            throw new InstrumentationException("Invalid name offset:  entryStart = " + this.entryStart + " nameOffset = " + i2 + " buffer limit = " + byteBuffer.limit());
        }
        byteBuffer.position(this.entryStart + 8);
        this.vectorLength = byteBuffer.getInt();
        byteBuffer.position(this.entryStart + 12);
        this.dataType = PerfDataType.toPerfDataType(byteBuffer.get());
        byteBuffer.position(this.entryStart + 13);
        this.flags = byteBuffer.get();
        byteBuffer.position(this.entryStart + 14);
        this.unit = Units.toUnits(byteBuffer.get());
        byteBuffer.position(this.entryStart + 15);
        this.variability = Variability.toVariability(byteBuffer.get());
        byteBuffer.position(this.entryStart + 16);
        this.dataOffset = byteBuffer.getInt();
        byteBuffer.position(this.entryStart + i2);
        int i3 = 0;
        while (byteBuffer.get() != 0) {
            i3++;
        }
        byte[] bArr = new byte[i3];
        byteBuffer.position(this.entryStart + i2);
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i4] = byteBuffer.get();
        }
        try {
            this.name = new String(bArr, "UTF-8");
            if (this.variability == Variability.INVALID) {
                throw new InstrumentationException("Invalid variability attribute: name = " + this.name);
            }
            if (this.unit == Units.INVALID) {
                throw new InstrumentationException("Invalid units attribute:  name = " + this.name);
            }
            if (this.vectorLength > 0) {
                this.dataSize = this.vectorLength * this.dataType.size();
            } else {
                this.dataSize = this.dataType.size();
            }
            if (this.entryStart + this.dataOffset + this.dataSize > byteBuffer.limit()) {
                throw new InstrumentationException("Data extends beyond end of buffer:  entryStart = " + this.entryStart + " dataOffset = " + this.dataOffset + " dataSize = " + this.dataSize + " buffer limit = " + byteBuffer.limit());
            }
            byteBuffer.position(this.entryStart + this.dataOffset);
            this.data = byteBuffer.slice();
            this.data.order(byteBuffer.order());
            this.data.limit(this.dataSize);
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(e2.getMessage(), e2);
        }
    }

    public int size() {
        return this.entryLength;
    }

    public String name() {
        return this.name;
    }

    public PerfDataType type() {
        return this.dataType;
    }

    public Units units() {
        return this.unit;
    }

    public int flags() {
        return this.flags;
    }

    public int vectorLength() {
        return this.vectorLength;
    }

    public Variability variability() {
        return this.variability;
    }

    public ByteBuffer byteData() {
        this.data.position(0);
        if ($assertionsDisabled || this.data.remaining() == vectorLength()) {
            return this.data.duplicate();
        }
        throw new AssertionError();
    }

    public LongBuffer longData() {
        return this.data.asLongBuffer();
    }
}
