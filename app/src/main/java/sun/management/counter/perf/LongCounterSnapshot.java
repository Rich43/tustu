package sun.management.counter.perf;

import sun.management.counter.AbstractCounter;
import sun.management.counter.LongCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/LongCounterSnapshot.class */
class LongCounterSnapshot extends AbstractCounter implements LongCounter {
    long value;
    private static final long serialVersionUID = 2054263861474565758L;

    LongCounterSnapshot(String str, Units units, Variability variability, int i2, long j2) {
        super(str, units, variability, i2);
        this.value = j2;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return new Long(this.value);
    }

    @Override // sun.management.counter.LongCounter
    public long longValue() {
        return this.value;
    }
}
