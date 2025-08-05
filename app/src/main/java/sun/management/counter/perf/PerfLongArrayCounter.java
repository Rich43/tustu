package sun.management.counter.perf;

import java.io.ObjectStreamException;
import java.nio.LongBuffer;
import sun.management.counter.AbstractCounter;
import sun.management.counter.LongArrayCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/PerfLongArrayCounter.class */
public class PerfLongArrayCounter extends AbstractCounter implements LongArrayCounter {
    LongBuffer lb;
    private static final long serialVersionUID = -2733617913045487126L;

    PerfLongArrayCounter(String str, Units units, Variability variability, int i2, int i3, LongBuffer longBuffer) {
        super(str, units, variability, i2, i3);
        this.lb = longBuffer;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return longArrayValue();
    }

    @Override // sun.management.counter.LongArrayCounter
    public long[] longArrayValue() {
        this.lb.position(0);
        long[] jArr = new long[this.lb.limit()];
        this.lb.get(jArr);
        return jArr;
    }

    @Override // sun.management.counter.LongArrayCounter
    public long longAt(int i2) {
        this.lb.position(i2);
        return this.lb.get();
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new LongArrayCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), getVectorLength(), longArrayValue());
    }
}
