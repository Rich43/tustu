package sun.management.counter.perf;

import java.io.ObjectStreamException;
import java.nio.ByteBuffer;
import sun.management.counter.AbstractCounter;
import sun.management.counter.ByteArrayCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/PerfByteArrayCounter.class */
public class PerfByteArrayCounter extends AbstractCounter implements ByteArrayCounter {

    /* renamed from: bb, reason: collision with root package name */
    ByteBuffer f13577bb;
    private static final long serialVersionUID = 2545474036937279921L;

    PerfByteArrayCounter(String str, Units units, Variability variability, int i2, int i3, ByteBuffer byteBuffer) {
        super(str, units, variability, i2, i3);
        this.f13577bb = byteBuffer;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return byteArrayValue();
    }

    @Override // sun.management.counter.ByteArrayCounter
    public byte[] byteArrayValue() {
        this.f13577bb.position(0);
        byte[] bArr = new byte[this.f13577bb.limit()];
        this.f13577bb.get(bArr);
        return bArr;
    }

    @Override // sun.management.counter.ByteArrayCounter
    public byte byteAt(int i2) {
        this.f13577bb.position(i2);
        return this.f13577bb.get();
    }

    @Override // sun.management.counter.AbstractCounter
    public String toString() {
        String str = getName() + ": " + new String(byteArrayValue()) + " " + ((Object) getUnits());
        if (isInternal()) {
            return str + " [INTERNAL]";
        }
        return str;
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new ByteArrayCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), getVectorLength(), byteArrayValue());
    }
}
