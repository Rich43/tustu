package sun.management.counter.perf;

import java.io.ObjectStreamException;
import java.nio.LongBuffer;
import sun.management.counter.AbstractCounter;
import sun.management.counter.LongCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/PerfLongCounter.class */
public class PerfLongCounter extends AbstractCounter implements LongCounter {
    LongBuffer lb;
    private static final long serialVersionUID = 857711729279242948L;

    PerfLongCounter(String str, Units units, Variability variability, int i2, LongBuffer longBuffer) {
        super(str, units, variability, i2);
        this.lb = longBuffer;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return new Long(this.lb.get(0));
    }

    @Override // sun.management.counter.LongCounter
    public long longValue() {
        return this.lb.get(0);
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new LongCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), longValue());
    }
}
