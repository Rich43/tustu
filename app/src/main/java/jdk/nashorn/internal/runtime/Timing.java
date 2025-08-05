package jdk.nashorn.internal.runtime;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.function.Supplier;
import jdk.nashorn.internal.codegen.CompileUnit;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;

@Logger(name = SchemaSymbols.ATTVAL_TIME)
/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Timing.class */
public final class Timing implements Loggable {
    private DebugLogger log;
    private TimeSupplier timeSupplier;
    private final boolean isEnabled;
    private final long startTime = System.nanoTime();
    private static final String LOGGER_NAME;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Timing.class.desiredAssertionStatus();
        LOGGER_NAME = ((Logger) Timing.class.getAnnotation(Logger.class)).name();
    }

    public Timing(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getLogInfo() {
        if ($assertionsDisabled || isEnabled()) {
            return this.timeSupplier.get();
        }
        throw new AssertionError();
    }

    public String[] getLogInfoLines() {
        if ($assertionsDisabled || isEnabled()) {
            return this.timeSupplier.getStrings();
        }
        throw new AssertionError();
    }

    boolean isEnabled() {
        return this.isEnabled;
    }

    public void accumulateTime(String module, long durationNano) {
        if (isEnabled()) {
            ensureInitialized(Context.getContextTrusted());
            this.timeSupplier.accumulateTime(module, durationNano);
        }
    }

    private DebugLogger ensureInitialized(Context context) {
        if (isEnabled() && this.log == null) {
            this.log = initLogger(context);
            if (this.log.isEnabled()) {
                this.timeSupplier = new TimeSupplier();
                Runtime.getRuntime().addShutdownHook(new Thread() { // from class: jdk.nashorn.internal.runtime.Timing.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        StringBuilder sb = new StringBuilder();
                        for (String str : Timing.this.timeSupplier.getStrings()) {
                            sb.append('[').append(Timing.getLoggerName()).append("] ").append(str).append('\n');
                        }
                        System.err.print(sb);
                    }
                });
            }
        }
        return this.log;
    }

    static String getLoggerName() {
        return LOGGER_NAME;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger initLogger(Context context) {
        return context.getLogger(getClass());
    }

    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger getLogger() {
        return this.log;
    }

    public static String toMillisPrint(long durationNano) {
        return Long.toString(TimeUnit.NANOSECONDS.toMillis(durationNano));
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Timing$TimeSupplier.class */
    final class TimeSupplier implements Supplier<String> {
        private final Map<String, LongAdder> timings = new ConcurrentHashMap();
        private final LinkedBlockingQueue<String> orderedTimingNames = new LinkedBlockingQueue<>();
        private final Function<String, LongAdder> newTimingCreator = new Function<String, LongAdder>() { // from class: jdk.nashorn.internal.runtime.Timing.TimeSupplier.1
            @Override // java.util.function.Function
            public LongAdder apply(String s2) {
                TimeSupplier.this.orderedTimingNames.add(s2);
                return new LongAdder();
            }
        };

        TimeSupplier() {
        }

        String[] getStrings() {
            List<String> strs = new ArrayList<>();
            BufferedReader br2 = new BufferedReader(new StringReader(get()));
            while (true) {
                try {
                    String line = br2.readLine();
                    if (line != null) {
                        strs.add(line);
                    } else {
                        return (String[]) strs.toArray(new String[strs.size()]);
                    }
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public String get() {
            long t2 = System.nanoTime();
            long knownTime = 0;
            int maxKeyLength = 0;
            int maxValueLength = 0;
            for (Map.Entry<String, LongAdder> entry : this.timings.entrySet()) {
                maxKeyLength = Math.max(maxKeyLength, entry.getKey().length());
                maxValueLength = Math.max(maxValueLength, Timing.toMillisPrint(entry.getValue().longValue()).length());
            }
            int maxKeyLength2 = maxKeyLength + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("Accumulated compilation phase timings:\n\n");
            Iterator<String> it = this.orderedTimingNames.iterator();
            while (it.hasNext()) {
                String timingName = it.next();
                int len = sb.length();
                sb.append(timingName);
                int len2 = sb.length() - len;
                while (true) {
                    int i2 = len2;
                    len2++;
                    if (i2 >= maxKeyLength2) {
                        break;
                    }
                    sb.append(' ');
                }
                long duration = this.timings.get(timingName).longValue();
                String strDuration = Timing.toMillisPrint(duration);
                int len3 = strDuration.length();
                for (int i3 = 0; i3 < maxValueLength - len3; i3++) {
                    sb.append(' ');
                }
                sb.append(strDuration).append(" ms\n");
                knownTime += duration;
            }
            long total = t2 - Timing.this.startTime;
            sb.append('\n');
            sb.append("Total runtime: ").append(Timing.toMillisPrint(total)).append(" ms (Non-runtime: ").append(Timing.toMillisPrint(knownTime)).append(" ms [").append((int) ((knownTime * 100.0d) / total)).append("%])");
            sb.append("\n\nEmitted compile units: ").append(CompileUnit.getEmittedUnitCount());
            return sb.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void accumulateTime(String module, long duration) {
            this.timings.computeIfAbsent(module, this.newTimingCreator).add(duration);
        }
    }
}
