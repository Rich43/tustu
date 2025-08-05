package sun.management.counter.perf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.management.counter.AbstractCounter;
import sun.management.counter.Counter;
import sun.management.counter.Units;

/* loaded from: rt.jar:sun/management/counter/perf/PerfInstrumentation.class */
public class PerfInstrumentation {
    private ByteBuffer buffer;
    private Prologue prologue;
    private long lastModificationTime;
    private long lastUsed;
    private int nextEntry;
    private SortedMap<String, Counter> map;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PerfInstrumentation.class.desiredAssertionStatus();
    }

    public PerfInstrumentation(ByteBuffer byteBuffer) {
        this.prologue = new Prologue(byteBuffer);
        this.buffer = byteBuffer;
        this.buffer.order(this.prologue.getByteOrder());
        int majorVersion = getMajorVersion();
        int minorVersion = getMinorVersion();
        if (majorVersion < 2) {
            throw new InstrumentationException("Unsupported version: " + majorVersion + "." + minorVersion);
        }
        rewind();
    }

    public int getMajorVersion() {
        return this.prologue.getMajorVersion();
    }

    public int getMinorVersion() {
        return this.prologue.getMinorVersion();
    }

    public long getModificationTimeStamp() {
        return this.prologue.getModificationTimeStamp();
    }

    void rewind() {
        this.buffer.rewind();
        this.buffer.position(this.prologue.getEntryOffset());
        this.nextEntry = this.buffer.position();
        this.map = new TreeMap();
    }

    boolean hasNext() {
        return this.nextEntry < this.prologue.getUsed();
    }

    Counter getNextCounter() {
        if (!hasNext()) {
            return null;
        }
        if (this.nextEntry % 4 != 0) {
            throw new InstrumentationException("Entry index not properly aligned: " + this.nextEntry);
        }
        if (this.nextEntry < 0 || this.nextEntry > this.buffer.limit()) {
            throw new InstrumentationException("Entry index out of bounds: nextEntry = " + this.nextEntry + ", limit = " + this.buffer.limit());
        }
        this.buffer.position(this.nextEntry);
        PerfDataEntry perfDataEntry = new PerfDataEntry(this.buffer);
        this.nextEntry += perfDataEntry.size();
        AbstractCounter perfLongCounter = null;
        PerfDataType perfDataTypeType = perfDataEntry.type();
        if (perfDataTypeType == PerfDataType.BYTE) {
            if (perfDataEntry.units() == Units.STRING && perfDataEntry.vectorLength() > 0) {
                perfLongCounter = new PerfStringCounter(perfDataEntry.name(), perfDataEntry.variability(), perfDataEntry.flags(), perfDataEntry.vectorLength(), perfDataEntry.byteData());
            } else if (perfDataEntry.vectorLength() > 0) {
                perfLongCounter = new PerfByteArrayCounter(perfDataEntry.name(), perfDataEntry.units(), perfDataEntry.variability(), perfDataEntry.flags(), perfDataEntry.vectorLength(), perfDataEntry.byteData());
            } else if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } else if (perfDataTypeType == PerfDataType.LONG) {
            perfLongCounter = perfDataEntry.vectorLength() == 0 ? new PerfLongCounter(perfDataEntry.name(), perfDataEntry.units(), perfDataEntry.variability(), perfDataEntry.flags(), perfDataEntry.longData()) : new PerfLongArrayCounter(perfDataEntry.name(), perfDataEntry.units(), perfDataEntry.variability(), perfDataEntry.flags(), perfDataEntry.vectorLength(), perfDataEntry.longData());
        } else if (!$assertionsDisabled) {
            throw new AssertionError();
        }
        return perfLongCounter;
    }

    public synchronized List<Counter> getAllCounters() {
        while (hasNext()) {
            Counter nextCounter = getNextCounter();
            if (nextCounter != null) {
                this.map.put(nextCounter.getName(), nextCounter);
            }
        }
        return new ArrayList(this.map.values());
    }

    public synchronized List<Counter> findByPattern(String str) {
        while (hasNext()) {
            Counter nextCounter = getNextCounter();
            if (nextCounter != null) {
                this.map.put(nextCounter.getName(), nextCounter);
            }
        }
        Matcher matcher = Pattern.compile(str).matcher("");
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, Counter> entry : this.map.entrySet()) {
            matcher.reset(entry.getKey());
            if (matcher.lookingAt()) {
                arrayList.add(entry.getValue());
            }
        }
        return arrayList;
    }
}
