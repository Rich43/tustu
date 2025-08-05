package sun.misc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.security.AccessController;
import sun.misc.Perf;

/* loaded from: rt.jar:sun/misc/PerfCounter.class */
public class PerfCounter {
    private static final Perf perf = (Perf) AccessController.doPrivileged(new Perf.GetPerfAction());
    private static final int V_Constant = 1;
    private static final int V_Monotonic = 2;
    private static final int V_Variable = 3;
    private static final int U_None = 1;
    private final String name;
    private final LongBuffer lb;

    private PerfCounter(String str, int i2) {
        this.name = str;
        ByteBuffer byteBufferCreateLong = perf.createLong(str, i2, 1, 0L);
        byteBufferCreateLong.order(ByteOrder.nativeOrder());
        this.lb = byteBufferCreateLong.asLongBuffer();
    }

    static PerfCounter newPerfCounter(String str) {
        return new PerfCounter(str, 3);
    }

    static PerfCounter newConstantPerfCounter(String str) {
        return new PerfCounter(str, 1);
    }

    public synchronized long get() {
        return this.lb.get(0);
    }

    public synchronized void set(long j2) {
        this.lb.put(0, j2);
    }

    public synchronized void add(long j2) {
        this.lb.put(0, get() + j2);
    }

    public void increment() {
        add(1L);
    }

    public void addTime(long j2) {
        add(j2);
    }

    public void addElapsedTimeFrom(long j2) {
        add(System.nanoTime() - j2);
    }

    public String toString() {
        return this.name + " = " + get();
    }

    /* loaded from: rt.jar:sun/misc/PerfCounter$CoreCounters.class */
    static class CoreCounters {
        static final PerfCounter pdt = PerfCounter.newPerfCounter("sun.classloader.parentDelegationTime");
        static final PerfCounter lc = PerfCounter.newPerfCounter("sun.classloader.findClasses");
        static final PerfCounter lct = PerfCounter.newPerfCounter("sun.classloader.findClassTime");
        static final PerfCounter rcbt = PerfCounter.newPerfCounter("sun.urlClassLoader.readClassBytesTime");
        static final PerfCounter zfc = PerfCounter.newPerfCounter("sun.zip.zipFiles");
        static final PerfCounter zfot = PerfCounter.newPerfCounter("sun.zip.zipFile.openTime");

        CoreCounters() {
        }
    }

    /* loaded from: rt.jar:sun/misc/PerfCounter$WindowsClientCounters.class */
    static class WindowsClientCounters {
        static final PerfCounter d3dAvailable = PerfCounter.newConstantPerfCounter("sun.java2d.d3d.available");

        WindowsClientCounters() {
        }
    }

    public static PerfCounter getFindClasses() {
        return CoreCounters.lc;
    }

    public static PerfCounter getFindClassTime() {
        return CoreCounters.lct;
    }

    public static PerfCounter getReadClassBytesTime() {
        return CoreCounters.rcbt;
    }

    public static PerfCounter getParentDelegationTime() {
        return CoreCounters.pdt;
    }

    public static PerfCounter getZipFileCount() {
        return CoreCounters.zfc;
    }

    public static PerfCounter getZipFileOpenTime() {
        return CoreCounters.zfot;
    }

    public static PerfCounter getD3DAvailable() {
        return WindowsClientCounters.d3dAvailable;
    }
}
