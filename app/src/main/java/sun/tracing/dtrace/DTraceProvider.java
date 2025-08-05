package sun.tracing.dtrace;

import com.sun.tracing.ProbeName;
import com.sun.tracing.Provider;
import com.sun.tracing.dtrace.Attributes;
import com.sun.tracing.dtrace.DependencyClass;
import com.sun.tracing.dtrace.FunctionName;
import com.sun.tracing.dtrace.ModuleName;
import com.sun.tracing.dtrace.StabilityLevel;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import sun.misc.ProxyGenerator;
import sun.tracing.ProbeSkeleton;
import sun.tracing.ProviderSkeleton;

/* loaded from: rt.jar:sun/tracing/dtrace/DTraceProvider.class */
class DTraceProvider extends ProviderSkeleton {
    private Activation activation;
    private Object proxy;
    private static final Class[] constructorParams;
    private final String proxyClassNamePrefix = "$DTraceTracingProxy";
    static final String DEFAULT_MODULE = "java_tracing";
    static final String DEFAULT_FUNCTION = "unspecified";
    private static long nextUniqueNumber;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DTraceProvider.class.desiredAssertionStatus();
        constructorParams = new Class[]{InvocationHandler.class};
        nextUniqueNumber = 0L;
    }

    private static synchronized long getUniqueNumber() {
        long j2 = nextUniqueNumber;
        nextUniqueNumber = j2 + 1;
        return j2;
    }

    @Override // sun.tracing.ProviderSkeleton
    protected ProbeSkeleton createProbe(Method method) {
        return new DTraceProbe(this.proxy, method);
    }

    DTraceProvider(Class<? extends Provider> cls) {
        super(cls);
        this.proxyClassNamePrefix = "$DTraceTracingProxy";
    }

    void setProxy(Object obj) {
        this.proxy = obj;
    }

    void setActivation(Activation activation) {
        this.activation = activation;
    }

    @Override // sun.tracing.ProviderSkeleton, com.sun.tracing.Provider
    public void dispose() {
        if (this.activation != null) {
            this.activation.disposeProvider(this);
            this.activation = null;
        }
        super.dispose();
    }

    @Override // sun.tracing.ProviderSkeleton
    public <T extends Provider> T newProxyInstance() {
        long uniqueNumber = getUniqueNumber();
        String strSubstring = "";
        if (!Modifier.isPublic(this.providerType.getModifiers())) {
            String name = this.providerType.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            strSubstring = iLastIndexOf == -1 ? "" : name.substring(0, iLastIndexOf + 1);
        }
        String str = strSubstring + "$DTraceTracingProxy" + uniqueNumber;
        byte[] bArrGenerateProxyClass = ProxyGenerator.generateProxyClass(str, new Class[]{this.providerType});
        try {
            try {
                return (T) JVM.defineClass(this.providerType.getClassLoader(), str, bArrGenerateProxyClass, 0, bArrGenerateProxyClass.length).getConstructor(constructorParams).newInstance(this);
            } catch (ReflectiveOperationException e2) {
                throw new InternalError(e2.toString(), e2);
            }
        } catch (ClassFormatError e3) {
            throw new IllegalArgumentException(e3.toString());
        }
    }

    @Override // sun.tracing.ProviderSkeleton
    protected void triggerProbe(Method method, Object[] objArr) {
        if (!$assertionsDisabled) {
            throw new AssertionError((Object) "This method should have been overridden by the JVM");
        }
    }

    @Override // sun.tracing.ProviderSkeleton
    public String getProviderName() {
        return super.getProviderName();
    }

    String getModuleName() {
        return getAnnotationString(this.providerType, ModuleName.class, DEFAULT_MODULE);
    }

    static String getProbeName(Method method) {
        return getAnnotationString(method, ProbeName.class, method.getName());
    }

    static String getFunctionName(Method method) {
        return getAnnotationString(method, FunctionName.class, DEFAULT_FUNCTION);
    }

    DTraceProbe[] getProbes() {
        return (DTraceProbe[]) this.probes.values().toArray(new DTraceProbe[0]);
    }

    StabilityLevel getNameStabilityFor(Class<? extends Annotation> cls) {
        Attributes attributes = (Attributes) getAnnotationValue(this.providerType, cls, "value", null);
        if (attributes == null) {
            return StabilityLevel.PRIVATE;
        }
        return attributes.name();
    }

    StabilityLevel getDataStabilityFor(Class<? extends Annotation> cls) {
        Attributes attributes = (Attributes) getAnnotationValue(this.providerType, cls, "value", null);
        if (attributes == null) {
            return StabilityLevel.PRIVATE;
        }
        return attributes.data();
    }

    DependencyClass getDependencyClassFor(Class<? extends Annotation> cls) {
        Attributes attributes = (Attributes) getAnnotationValue(this.providerType, cls, "value", null);
        if (attributes == null) {
            return DependencyClass.UNKNOWN;
        }
        return attributes.dependency();
    }
}
