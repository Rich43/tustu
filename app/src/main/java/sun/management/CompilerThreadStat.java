package sun.management;

import java.io.Serializable;

/* loaded from: rt.jar:sun/management/CompilerThreadStat.class */
public class CompilerThreadStat implements Serializable {
    private String name;
    private long taskCount;
    private long compileTime;
    private MethodInfo lastMethod;
    private static final long serialVersionUID = 6992337162326171013L;

    CompilerThreadStat(String str, long j2, long j3, MethodInfo methodInfo) {
        this.name = str;
        this.taskCount = j2;
        this.compileTime = j3;
        this.lastMethod = methodInfo;
    }

    public String getName() {
        return this.name;
    }

    public long getCompileTaskCount() {
        return this.taskCount;
    }

    public long getCompileTime() {
        return this.compileTime;
    }

    public MethodInfo getLastCompiledMethodInfo() {
        return this.lastMethod;
    }

    public String toString() {
        return getName() + " compileTasks = " + getCompileTaskCount() + " compileTime = " + getCompileTime();
    }
}
