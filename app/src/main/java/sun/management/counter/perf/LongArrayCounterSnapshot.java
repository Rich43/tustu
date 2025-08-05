package sun.management.counter.perf;

import sun.management.counter.AbstractCounter;
import sun.management.counter.LongArrayCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/LongArrayCounterSnapshot.class */
class LongArrayCounterSnapshot extends AbstractCounter implements LongArrayCounter {
    long[] value;
    private static final long serialVersionUID = 3585870271405924292L;

    LongArrayCounterSnapshot(String str, Units units, Variability variability, int i2, int i3, long[] jArr) {
        super(str, units, variability, i2, i3);
        this.value = jArr;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return this.value;
    }

    @Override // sun.management.counter.LongArrayCounter
    public long[] longArrayValue() {
        return this.value;
    }

    @Override // sun.management.counter.LongArrayCounter
    public long longAt(int i2) {
        return this.value[i2];
    }
}
