package sun.management;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import sun.management.counter.Counter;
import sun.management.counter.LongCounter;
import sun.management.counter.StringCounter;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/management/HotspotCompilation.class */
class HotspotCompilation implements HotspotCompilationMBean {
    private VMManagement jvm;
    private static final String JAVA_CI = "java.ci.";
    private static final String COM_SUN_CI = "com.sun.ci.";
    private static final String SUN_CI = "sun.ci.";
    private static final String CI_COUNTER_NAME_PATTERN = "java.ci.|com.sun.ci.|sun.ci.";
    private LongCounter compilerThreads;
    private LongCounter totalCompiles;
    private LongCounter totalBailouts;
    private LongCounter totalInvalidates;
    private LongCounter nmethodCodeSize;
    private LongCounter nmethodSize;
    private StringCounter lastMethod;
    private LongCounter lastSize;
    private LongCounter lastType;
    private StringCounter lastFailedMethod;
    private LongCounter lastFailedType;
    private StringCounter lastInvalidatedMethod;
    private LongCounter lastInvalidatedType;
    private CompilerThreadInfo[] threads;
    private int numActiveThreads;
    private Map<String, Counter> counters;

    HotspotCompilation(VMManagement vMManagement) {
        this.jvm = vMManagement;
        initCompilerCounters();
    }

    /* loaded from: rt.jar:sun/management/HotspotCompilation$CompilerThreadInfo.class */
    private class CompilerThreadInfo {
        int index;
        String name;
        StringCounter method;
        LongCounter type;
        LongCounter compiles;
        LongCounter time;

        CompilerThreadInfo(String str, int i2) {
            String str2 = str + "." + i2 + ".";
            this.name = str + LanguageTag.SEP + i2;
            this.method = (StringCounter) HotspotCompilation.this.lookup(str2 + "method");
            this.type = (LongCounter) HotspotCompilation.this.lookup(str2 + "type");
            this.compiles = (LongCounter) HotspotCompilation.this.lookup(str2 + "compiles");
            this.time = (LongCounter) HotspotCompilation.this.lookup(str2 + SchemaSymbols.ATTVAL_TIME);
        }

        CompilerThreadInfo(String str) {
            String str2 = str + ".";
            this.name = str;
            this.method = (StringCounter) HotspotCompilation.this.lookup(str2 + "method");
            this.type = (LongCounter) HotspotCompilation.this.lookup(str2 + "type");
            this.compiles = (LongCounter) HotspotCompilation.this.lookup(str2 + "compiles");
            this.time = (LongCounter) HotspotCompilation.this.lookup(str2 + SchemaSymbols.ATTVAL_TIME);
        }

        CompilerThreadStat getCompilerThreadStat() {
            return new CompilerThreadStat(this.name, this.compiles.longValue(), this.time.longValue(), new MethodInfo(this.method.stringValue(), (int) this.type.longValue(), -1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Counter lookup(String str) {
        Counter counter = this.counters.get(SUN_CI + str);
        if (counter != null) {
            return counter;
        }
        Counter counter2 = this.counters.get(COM_SUN_CI + str);
        if (counter2 != null) {
            return counter2;
        }
        Counter counter3 = this.counters.get(JAVA_CI + str);
        if (counter3 != null) {
            return counter3;
        }
        throw new AssertionError((Object) ("Counter " + str + " does not exist"));
    }

    private void initCompilerCounters() {
        this.counters = new TreeMap();
        for (Counter counter : getInternalCompilerCounters()) {
            this.counters.put(counter.getName(), counter);
        }
        this.compilerThreads = (LongCounter) lookup("threads");
        this.totalCompiles = (LongCounter) lookup("totalCompiles");
        this.totalBailouts = (LongCounter) lookup("totalBailouts");
        this.totalInvalidates = (LongCounter) lookup("totalInvalidates");
        this.nmethodCodeSize = (LongCounter) lookup("nmethodCodeSize");
        this.nmethodSize = (LongCounter) lookup("nmethodSize");
        this.lastMethod = (StringCounter) lookup("lastMethod");
        this.lastSize = (LongCounter) lookup("lastSize");
        this.lastType = (LongCounter) lookup("lastType");
        this.lastFailedMethod = (StringCounter) lookup("lastFailedMethod");
        this.lastFailedType = (LongCounter) lookup("lastFailedType");
        this.lastInvalidatedMethod = (StringCounter) lookup("lastInvalidatedMethod");
        this.lastInvalidatedType = (LongCounter) lookup("lastInvalidatedType");
        this.numActiveThreads = (int) this.compilerThreads.longValue();
        this.threads = new CompilerThreadInfo[this.numActiveThreads + 1];
        if (this.counters.containsKey("sun.ci.adapterThread.compiles")) {
            this.threads[0] = new CompilerThreadInfo("adapterThread", 0);
            this.numActiveThreads++;
        } else {
            this.threads[0] = null;
        }
        for (int i2 = 1; i2 < this.threads.length; i2++) {
            this.threads[i2] = new CompilerThreadInfo("compilerThread", i2 - 1);
        }
    }

    @Override // sun.management.HotspotCompilationMBean
    public int getCompilerThreadCount() {
        return this.numActiveThreads;
    }

    @Override // sun.management.HotspotCompilationMBean
    public long getTotalCompileCount() {
        return this.totalCompiles.longValue();
    }

    @Override // sun.management.HotspotCompilationMBean
    public long getBailoutCompileCount() {
        return this.totalBailouts.longValue();
    }

    @Override // sun.management.HotspotCompilationMBean
    public long getInvalidatedCompileCount() {
        return this.totalInvalidates.longValue();
    }

    @Override // sun.management.HotspotCompilationMBean
    public long getCompiledMethodCodeSize() {
        return this.nmethodCodeSize.longValue();
    }

    @Override // sun.management.HotspotCompilationMBean
    public long getCompiledMethodSize() {
        return this.nmethodSize.longValue();
    }

    @Override // sun.management.HotspotCompilationMBean
    public List<CompilerThreadStat> getCompilerThreadStats() {
        ArrayList arrayList = new ArrayList(this.threads.length);
        int i2 = 0;
        if (this.threads[0] == null) {
            i2 = 1;
        }
        while (i2 < this.threads.length) {
            arrayList.add(this.threads[i2].getCompilerThreadStat());
            i2++;
        }
        return arrayList;
    }

    @Override // sun.management.HotspotCompilationMBean
    public MethodInfo getLastCompile() {
        return new MethodInfo(this.lastMethod.stringValue(), (int) this.lastType.longValue(), (int) this.lastSize.longValue());
    }

    @Override // sun.management.HotspotCompilationMBean
    public MethodInfo getFailedCompile() {
        return new MethodInfo(this.lastFailedMethod.stringValue(), (int) this.lastFailedType.longValue(), -1);
    }

    @Override // sun.management.HotspotCompilationMBean
    public MethodInfo getInvalidatedCompile() {
        return new MethodInfo(this.lastInvalidatedMethod.stringValue(), (int) this.lastInvalidatedType.longValue(), -1);
    }

    @Override // sun.management.HotspotCompilationMBean
    public List<Counter> getInternalCompilerCounters() {
        return this.jvm.getInternalCounters(CI_COUNTER_NAME_PATTERN);
    }
}
