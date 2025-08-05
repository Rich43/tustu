package sun.management.counter.perf;

import sun.management.counter.AbstractCounter;
import sun.management.counter.ByteArrayCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/ByteArrayCounterSnapshot.class */
class ByteArrayCounterSnapshot extends AbstractCounter implements ByteArrayCounter {
    byte[] value;
    private static final long serialVersionUID = 1444793459838438979L;

    ByteArrayCounterSnapshot(String str, Units units, Variability variability, int i2, int i3, byte[] bArr) {
        super(str, units, variability, i2, i3);
        this.value = bArr;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return this.value;
    }

    @Override // sun.management.counter.ByteArrayCounter
    public byte[] byteArrayValue() {
        return this.value;
    }

    @Override // sun.management.counter.ByteArrayCounter
    public byte byteAt(int i2) {
        return this.value[i2];
    }
}
