package sun.management.counter.perf;

import java.io.ObjectStreamException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import sun.management.counter.StringCounter;
import sun.management.counter.Units;
import sun.management.counter.Variability;

/* loaded from: rt.jar:sun/management/counter/perf/PerfStringCounter.class */
public class PerfStringCounter extends PerfByteArrayCounter implements StringCounter {
    private static Charset defaultCharset = Charset.defaultCharset();
    private static final long serialVersionUID = 6802913433363692452L;

    PerfStringCounter(String str, Variability variability, int i2, ByteBuffer byteBuffer) {
        this(str, variability, i2, byteBuffer.limit(), byteBuffer);
    }

    PerfStringCounter(String str, Variability variability, int i2, int i3, ByteBuffer byteBuffer) {
        super(str, Units.STRING, variability, i2, i3, byteBuffer);
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public boolean isVector() {
        return false;
    }

    @Override // sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public int getVectorLength() {
        return 0;
    }

    @Override // sun.management.counter.perf.PerfByteArrayCounter, sun.management.counter.AbstractCounter, sun.management.counter.Counter
    public Object getValue() {
        return stringValue();
    }

    @Override // sun.management.counter.StringCounter
    public String stringValue() {
        byte[] bArrByteArrayValue = byteArrayValue();
        if (bArrByteArrayValue == null || bArrByteArrayValue.length <= 1) {
            return "";
        }
        int i2 = 0;
        while (i2 < bArrByteArrayValue.length && bArrByteArrayValue[i2] != 0) {
            i2++;
        }
        return new String(bArrByteArrayValue, 0, i2, defaultCharset);
    }

    @Override // sun.management.counter.perf.PerfByteArrayCounter
    protected Object writeReplace() throws ObjectStreamException {
        return new StringCounterSnapshot(getName(), getUnits(), getVariability(), getFlags(), stringValue());
    }
}
