package sun.management.counter.perf;

import sun.management.counter.AbstractCounter;
import sun.management.counter.StringCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/StringCounterSnapshot.class */
class StringCounterSnapshot extends AbstractCounter implements StringCounter {
    String value;
    private static final long serialVersionUID = 1132921539085572034L;

    StringCounterSnapshot(String str, Units units, Variability variability, int i2, String str2) {
        super(str, units, variability, i2);
        this.value = str2;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return this.value;
    }

    @Override // sun.management.counter.StringCounter
    public String stringValue() {
        return this.value;
    }
}
