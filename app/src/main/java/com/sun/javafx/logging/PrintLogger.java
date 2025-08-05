package com.sun.javafx.logging;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: jfxrt.jar:com/sun/javafx/logging/PrintLogger.class */
class PrintLogger extends Logger {
    private static PrintLogger printLogger;
    private static long THRESHOLD = ((Integer) AccessController.doPrivileged(() -> {
        return Integer.getInteger("javafx.pulseLogger.threshold", 17);
    })).intValue();
    private static final int EXIT_ON_PULSE = ((Integer) AccessController.doPrivileged(() -> {
        return Integer.getInteger("javafx.pulseLogger.exitOnPulse", 0);
    })).intValue();
    private static final int INTER_PULSE_DATA = -1;
    private volatile PulseData fxData;
    private volatile PulseData renderData;
    private long lastPulseStartTime;
    private Thread fxThread;
    private AtomicInteger active;
    private static final int AVAILABLE = 0;
    private static final int INCOMPLETE = 1;
    private static final int COMPLETE = 2;
    private int pulseCount = 1;
    private volatile int wrapCount = 0;
    private final ThreadLocal<ThreadLocalData> phaseData = new ThreadLocal() { // from class: com.sun.javafx.logging.PrintLogger.1
        @Override // java.lang.ThreadLocal
        public ThreadLocalData initialValue() {
            return PrintLogger.this.new ThreadLocalData();
        }
    };
    private PulseData head = new PulseData();
    private PulseData tail = new PulseData();

    static /* synthetic */ int access$308(PrintLogger x0) {
        int i2 = x0.wrapCount;
        x0.wrapCount = i2 + 1;
        return i2;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/logging/PrintLogger$ThreadLocalData.class */
    class ThreadLocalData {
        String phaseName;
        long phaseStart;

        ThreadLocalData() {
        }
    }

    private PrintLogger() {
        this.head.next = this.tail;
        this.active = new AtomicInteger(0);
    }

    public static Logger getInstance() {
        if (printLogger == null) {
            boolean enabled = ((Boolean) AccessController.doPrivileged(() -> {
                return Boolean.valueOf(Boolean.getBoolean("javafx.pulseLogger"));
            })).booleanValue();
            if (enabled) {
                printLogger = new PrintLogger();
            }
        }
        return printLogger;
    }

    private PulseData allocate(int n2) {
        PulseData res;
        if (this.head != this.tail && this.head.state == 0) {
            res = this.head;
            this.head = this.head.next;
            res.next = null;
        } else {
            res = new PulseData();
        }
        this.tail.next = res;
        this.tail = res;
        res.init(n2);
        return res;
    }

    @Override // com.sun.javafx.logging.Logger
    public void pulseStart() {
        if (this.fxThread == null) {
            this.fxThread = Thread.currentThread();
        }
        if (this.fxData != null) {
            this.fxData.state = 2;
            if (this.active.incrementAndGet() == 1) {
                this.fxData.printAndReset();
                this.active.decrementAndGet();
            }
        }
        int i2 = this.pulseCount;
        this.pulseCount = i2 + 1;
        this.fxData = allocate(i2);
        if (this.lastPulseStartTime > 0) {
            this.fxData.interval = (this.fxData.startTime - this.lastPulseStartTime) / 1000000;
        }
        this.lastPulseStartTime = this.fxData.startTime;
    }

    @Override // com.sun.javafx.logging.Logger
    public void renderStart() {
        newPhase(null);
        this.fxData.pushedRender = true;
        this.renderData = this.fxData;
        this.active.incrementAndGet();
    }

    @Override // com.sun.javafx.logging.Logger
    public void pulseEnd() {
        if (this.fxData != null && !this.fxData.pushedRender) {
            this.fxData.state = 2;
            if (this.active.incrementAndGet() == 1) {
                this.fxData.printAndReset();
                this.active.decrementAndGet();
            }
        }
        this.fxData = null;
    }

    @Override // com.sun.javafx.logging.Logger
    public void renderEnd() {
        newPhase(null);
        this.renderData.state = 2;
        while (true) {
            this.renderData.printAndReset();
            if (this.active.decrementAndGet() != 0) {
                this.renderData = this.renderData.next;
            } else {
                this.renderData = null;
                return;
            }
        }
    }

    @Override // com.sun.javafx.logging.Logger
    public void addMessage(String message) {
        PulseData pulseData;
        if (this.fxThread == null || Thread.currentThread() == this.fxThread) {
            if (this.fxData == null) {
                this.fxData = allocate(-1);
            }
            pulseData = this.fxData;
        } else {
            pulseData = this.renderData;
        }
        if (pulseData == null) {
            return;
        }
        pulseData.message.append("T").append(Thread.currentThread().getId()).append(" : ").append(message).append("\n");
    }

    @Override // com.sun.javafx.logging.Logger
    public void incrementCounter(String counter) {
        PulseData pulseData;
        if (this.fxThread == null || Thread.currentThread() == this.fxThread) {
            if (this.fxData == null) {
                this.fxData = allocate(-1);
            }
            pulseData = this.fxData;
        } else {
            pulseData = this.renderData;
        }
        if (pulseData == null) {
            return;
        }
        Map<String, Counter> counters = pulseData.counters;
        Counter cval = counters.get(counter);
        if (cval == null) {
            cval = new Counter();
            counters.put(counter, cval);
        }
        cval.value++;
    }

    @Override // com.sun.javafx.logging.Logger
    public void newPhase(String name) {
        long curTime = System.nanoTime();
        ThreadLocalData curPhase = this.phaseData.get();
        if (curPhase.phaseName != null) {
            PulseData pulseData = Thread.currentThread() == this.fxThread ? this.fxData : this.renderData;
            if (pulseData != null) {
                pulseData.message.append("T").append(Thread.currentThread().getId()).append(" (").append((curPhase.phaseStart - pulseData.startTime) / 1000000).append(" +").append((curTime - curPhase.phaseStart) / 1000000).append("ms): ").append(curPhase.phaseName).append("\n");
            }
        }
        curPhase.phaseName = name;
        curPhase.phaseStart = curTime;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/logging/PrintLogger$Counter.class */
    private static class Counter {
        int value;

        private Counter() {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/logging/PrintLogger$PulseData.class */
    private final class PulseData {
        PulseData next;
        volatile int state;
        long startTime;
        long interval;
        int pulseCount;
        boolean pushedRender;
        StringBuffer message;
        Map<String, Counter> counters;

        private PulseData() {
            this.state = 0;
            this.message = new StringBuffer();
            this.counters = new ConcurrentHashMap();
        }

        void init(int n2) {
            this.state = 1;
            this.pulseCount = n2;
            this.startTime = System.nanoTime();
            this.interval = 0L;
            this.pushedRender = false;
        }

        void printAndReset() {
            long endTime = System.nanoTime();
            long totalTime = (endTime - this.startTime) / 1000000;
            if (this.state != 2) {
                System.err.println("\nWARNING: logging incomplete state");
            }
            if (totalTime <= PrintLogger.THRESHOLD) {
                if (this.pulseCount != -1) {
                    System.err.print((PrintLogger.access$308(PrintLogger.this) % 10 == 0 ? "\n[" : "[") + this.pulseCount + " " + this.interval + "ms:" + totalTime + "ms]");
                }
            } else {
                if (this.pulseCount == -1) {
                    System.err.println("\n\nINTER PULSE LOG DATA");
                } else {
                    System.err.print("\n\nPULSE: " + this.pulseCount + " [" + this.interval + "ms:" + totalTime + "ms]");
                    if (!this.pushedRender) {
                        System.err.print(" Required No Rendering");
                    }
                    System.err.println();
                }
                System.err.print(this.message);
                if (!this.counters.isEmpty()) {
                    System.err.println("Counters:");
                    List<Map.Entry<String, Counter>> entries = new ArrayList<>(this.counters.entrySet());
                    Collections.sort(entries, (a2, b2) -> {
                        return ((String) a2.getKey()).compareTo((String) b2.getKey());
                    });
                    for (Map.Entry<String, Counter> entry : entries) {
                        System.err.println("\t" + entry.getKey() + ": " + entry.getValue().value);
                    }
                }
                PrintLogger.this.wrapCount = 0;
            }
            this.message.setLength(0);
            this.counters.clear();
            this.state = 0;
            if (PrintLogger.EXIT_ON_PULSE > 0 && this.pulseCount >= PrintLogger.EXIT_ON_PULSE) {
                System.err.println("Exiting after pulse #" + this.pulseCount);
                System.exit(0);
            }
        }
    }
}
