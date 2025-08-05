package com.sun.webkit.perf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/perf/PerfLogger.class */
public final class PerfLogger {
    private static Thread shutdownHook;
    private static Map<Logger, PerfLogger> loggers;
    private final Logger log;
    private final boolean isEnabled;
    private final HashMap<String, ProbeStat> probes = new HashMap<>();
    private final Comparator timeComparator = (arg0, arg1) -> {
        long t0 = this.probes.get((String) arg0).totalTime;
        long t1 = this.probes.get((String) arg1).totalTime;
        if (t0 > t1) {
            return 1;
        }
        if (t0 < t1) {
            return -1;
        }
        return 0;
    };
    private final Comparator countComparator = (arg0, arg1) -> {
        long c0 = this.probes.get((String) arg0).count;
        long c1 = this.probes.get((String) arg1).count;
        if (c0 > c1) {
            return 1;
        }
        if (c0 < c1) {
            return -1;
        }
        return 0;
    };

    public static synchronized PerfLogger getLogger(Logger log) {
        if (loggers == null) {
            loggers = new HashMap();
        }
        PerfLogger l2 = loggers.get(log);
        if (l2 == null) {
            l2 = new PerfLogger(log);
            loggers.put(log, l2);
        }
        if (l2.isEnabled() && shutdownHook == null) {
            shutdownHook = new Thread() { // from class: com.sun.webkit.perf.PerfLogger.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    for (PerfLogger l3 : PerfLogger.loggers.values()) {
                        if (l3.isEnabled()) {
                            l3.log(false);
                        }
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
        return l2;
    }

    public static synchronized PerfLogger getLogger(String name) {
        return getLogger(Logger.getLogger("com.sun.webkit.perf." + name));
    }

    private PerfLogger(Logger log) {
        this.log = log;
        this.isEnabled = log.isLoggable(Level.FINE);
        startCount("TOTALTIME");
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/perf/PerfLogger$ProbeStat.class */
    public static final class ProbeStat {
        private final String probe;
        private int count;
        private long totalTime;
        private long startTime;
        private boolean isRunning;

        private ProbeStat(String probe) {
            this.isRunning = false;
            this.probe = probe;
        }

        public String getProbe() {
            return this.probe;
        }

        public int getCount() {
            return this.count;
        }

        public long getTotalTime() {
            return this.totalTime;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.count = 0;
            this.startTime = 0L;
            this.totalTime = 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void suspend() {
            if (this.isRunning) {
                this.totalTime += System.currentTimeMillis() - this.startTime;
                this.isRunning = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void resume() {
            this.isRunning = true;
            this.count++;
            this.startTime = System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void snapshot() {
            if (this.isRunning) {
                this.totalTime += System.currentTimeMillis() - this.startTime;
                this.startTime = System.currentTimeMillis();
            }
        }

        public String toString() {
            return super.toString() + "[count=" + this.count + ", time=" + this.totalTime + "]";
        }
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    private synchronized String fullName(String probe) {
        return this.log.getName() + "." + probe;
    }

    public synchronized void reset() {
        for (Map.Entry<String, ProbeStat> entry : this.probes.entrySet()) {
            entry.getValue().reset();
        }
        startCount("TOTALTIME");
    }

    public static synchronized void resetAll() {
        for (PerfLogger l2 : loggers.values()) {
            l2.reset();
        }
    }

    private synchronized ProbeStat registerProbe(String probe) {
        String p2 = probe.intern();
        if (this.probes.containsKey(p2)) {
            this.log.fine("Warning: \"" + fullName(p2) + "\" probe already exists");
        } else {
            this.log.fine("Registering \"" + fullName(p2) + "\" probe");
        }
        ProbeStat stat = new ProbeStat(p2);
        this.probes.put(p2, stat);
        return stat;
    }

    public synchronized ProbeStat getProbeStat(String probe) {
        String p2 = probe.intern();
        ProbeStat s2 = this.probes.get(p2);
        if (s2 != null) {
            s2.snapshot();
        }
        return s2;
    }

    public synchronized void startCount(String probe) {
        if (!isEnabled()) {
            return;
        }
        String p2 = probe.intern();
        ProbeStat stat = this.probes.get(p2);
        if (stat == null) {
            stat = registerProbe(p2);
        }
        stat.reset();
        stat.resume();
    }

    public synchronized void suspendCount(String probe) {
        if (!isEnabled()) {
            return;
        }
        String p2 = probe.intern();
        ProbeStat stat = this.probes.get(p2);
        if (stat != null) {
            stat.suspend();
        } else {
            this.log.fine("Warning: \"" + fullName(p2) + "\" probe is not registered");
        }
    }

    public synchronized void resumeCount(String probe) {
        if (!isEnabled()) {
            return;
        }
        String p2 = probe.intern();
        ProbeStat stat = this.probes.get(p2);
        if (stat == null) {
            stat = registerProbe(p2);
        }
        stat.resume();
    }

    public synchronized void log(StringBuffer buf) {
        if (!isEnabled()) {
            return;
        }
        buf.append("=========== Performance Statistics =============\n");
        ProbeStat total = getProbeStat("TOTALTIME");
        ArrayList<String> list = new ArrayList<>();
        list.addAll(this.probes.keySet());
        buf.append("\nTime:\n");
        Collections.sort(list, this.timeComparator);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String p2 = it.next();
            ProbeStat s2 = getProbeStat(p2);
            buf.append(String.format("%s: %dms", fullName(p2), Long.valueOf(s2.totalTime)));
            if (total.totalTime > 0) {
                buf.append(String.format(", %.2f%%%n", Float.valueOf((100.0f * s2.totalTime) / total.totalTime)));
            } else {
                buf.append("\n");
            }
        }
        buf.append("\nInvocations count:\n");
        Collections.sort(list, this.countComparator);
        Iterator<String> it2 = list.iterator();
        while (it2.hasNext()) {
            String p3 = it2.next();
            buf.append(String.format("%s: %d%n", fullName(p3), Integer.valueOf(getProbeStat(p3).count)));
        }
        buf.append("================================================\n");
    }

    public synchronized void log() {
        log(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void log(boolean useLogger) {
        StringBuffer buf = new StringBuffer();
        log(buf);
        if (useLogger) {
            this.log.fine(buf.toString());
        } else {
            System.out.println(buf.toString());
            System.out.flush();
        }
    }

    public static synchronized void logAll() {
        for (PerfLogger l2 : loggers.values()) {
            l2.log();
        }
    }
}
