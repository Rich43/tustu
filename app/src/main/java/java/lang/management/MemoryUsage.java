package java.lang.management;

import javax.management.openmbean.CompositeData;
import sun.management.MemoryUsageCompositeData;

/* loaded from: rt.jar:java/lang/management/MemoryUsage.class */
public class MemoryUsage {
    private final long init;
    private final long used;
    private final long committed;
    private final long max;

    public MemoryUsage(long j2, long j3, long j4, long j5) {
        if (j2 < -1) {
            throw new IllegalArgumentException("init parameter = " + j2 + " is negative but not -1.");
        }
        if (j5 < -1) {
            throw new IllegalArgumentException("max parameter = " + j5 + " is negative but not -1.");
        }
        if (j3 < 0) {
            throw new IllegalArgumentException("used parameter = " + j3 + " is negative.");
        }
        if (j4 < 0) {
            throw new IllegalArgumentException("committed parameter = " + j4 + " is negative.");
        }
        if (j3 > j4) {
            throw new IllegalArgumentException("used = " + j3 + " should be <= committed = " + j4);
        }
        if (j5 >= 0 && j4 > j5) {
            throw new IllegalArgumentException("committed = " + j4 + " should be < max = " + j5);
        }
        this.init = j2;
        this.used = j3;
        this.committed = j4;
        this.max = j5;
    }

    private MemoryUsage(CompositeData compositeData) {
        MemoryUsageCompositeData.validateCompositeData(compositeData);
        this.init = MemoryUsageCompositeData.getInit(compositeData);
        this.used = MemoryUsageCompositeData.getUsed(compositeData);
        this.committed = MemoryUsageCompositeData.getCommitted(compositeData);
        this.max = MemoryUsageCompositeData.getMax(compositeData);
    }

    public long getInit() {
        return this.init;
    }

    public long getUsed() {
        return this.used;
    }

    public long getCommitted() {
        return this.committed;
    }

    public long getMax() {
        return this.max;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("init = " + this.init + "(" + (this.init >> 10) + "K) ");
        stringBuffer.append("used = " + this.used + "(" + (this.used >> 10) + "K) ");
        stringBuffer.append("committed = " + this.committed + "(" + (this.committed >> 10) + "K) ");
        stringBuffer.append("max = " + this.max + "(" + (this.max >> 10) + "K)");
        return stringBuffer.toString();
    }

    public static MemoryUsage from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof MemoryUsageCompositeData) {
            return ((MemoryUsageCompositeData) compositeData).getMemoryUsage();
        }
        return new MemoryUsage(compositeData);
    }
}
