package jdk.internal.dynalink;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Lookup;

/* loaded from: nashorn.jar:jdk/internal/dynalink/DynamicLinker.class */
public class DynamicLinker {
    private static final String INITIAL_LINK_CLASS_NAME = "java.lang.invoke.MethodHandleNatives";
    private static final String INITIAL_LINK_METHOD_NAME = "linkCallSite";
    private final LinkerServices linkerServices;
    private final GuardedInvocationFilter prelinkFilter;
    private final int runtimeContextArgCount;
    private final boolean syncOnRelink;
    private final int unstableRelinkThreshold;
    private static final String CLASS_NAME = DynamicLinker.class.getName();
    private static final String RELINK_METHOD_NAME = "relink";
    private static final MethodHandle RELINK = Lookup.findOwnSpecial(MethodHandles.lookup(), RELINK_METHOD_NAME, MethodHandle.class, RelinkableCallSite.class, Integer.TYPE, Object[].class);

    DynamicLinker(LinkerServices linkerServices, GuardedInvocationFilter prelinkFilter, int runtimeContextArgCount, boolean syncOnRelink, int unstableRelinkThreshold) {
        if (runtimeContextArgCount < 0) {
            throw new IllegalArgumentException("runtimeContextArgCount < 0");
        }
        if (unstableRelinkThreshold < 0) {
            throw new IllegalArgumentException("unstableRelinkThreshold < 0");
        }
        this.linkerServices = linkerServices;
        this.prelinkFilter = prelinkFilter;
        this.runtimeContextArgCount = runtimeContextArgCount;
        this.syncOnRelink = syncOnRelink;
        this.unstableRelinkThreshold = unstableRelinkThreshold;
    }

    public <T extends RelinkableCallSite> T link(T callSite) {
        callSite.initialize(createRelinkAndInvokeMethod(callSite, 0));
        return callSite;
    }

    public LinkerServices getLinkerServices() {
        return this.linkerServices;
    }

    private MethodHandle createRelinkAndInvokeMethod(RelinkableCallSite callSite, int relinkCount) throws RuntimeException {
        MethodHandle boundRelinker = MethodHandles.insertArguments(RELINK, 0, this, callSite, Integer.valueOf(relinkCount));
        MethodType type = callSite.getDescriptor().getMethodType();
        MethodHandle collectingRelinker = boundRelinker.asCollector(Object[].class, type.parameterCount());
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(type), collectingRelinker.asType(type.changeReturnType(MethodHandle.class)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:32:0x010d A[PHI: r18
  0x010d: PHI (r18v1 'newRelinkCount' int) = (r18v0 'newRelinkCount' int), (r18v0 'newRelinkCount' int), (r18v2 'newRelinkCount' int) binds: [B:26:0x00e3, B:28:0x00ec, B:30:0x00f8] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.invoke.MethodHandle relink(jdk.internal.dynalink.RelinkableCallSite r10, int r11, java.lang.Object... r12) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 311
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.dynalink.DynamicLinker.relink(jdk.internal.dynalink.RelinkableCallSite, int, java.lang.Object[]):java.lang.invoke.MethodHandle");
    }

    public static StackTraceElement getLinkedCallSiteLocation() {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        for (int i2 = 0; i2 < trace.length - 1; i2++) {
            StackTraceElement frame = trace[i2];
            if (isRelinkFrame(frame) || isInitialLinkFrame(frame)) {
                return trace[i2 + 1];
            }
        }
        return null;
    }

    @Deprecated
    public static StackTraceElement getRelinkedCallSiteLocation() {
        return getLinkedCallSiteLocation();
    }

    private static boolean isInitialLinkFrame(StackTraceElement frame) {
        return testFrame(frame, INITIAL_LINK_METHOD_NAME, INITIAL_LINK_CLASS_NAME);
    }

    private static boolean isRelinkFrame(StackTraceElement frame) {
        return testFrame(frame, RELINK_METHOD_NAME, CLASS_NAME);
    }

    private static boolean testFrame(StackTraceElement frame, String methodName, String className) {
        return methodName.equals(frame.getMethodName()) && className.equals(frame.getClassName());
    }
}
