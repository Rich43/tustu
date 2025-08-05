package sun.tracing;

import com.sun.tracing.ProbeName;
import com.sun.tracing.Provider;
import java.io.PrintStream;
import java.lang.reflect.Method;

/* compiled from: PrintStreamProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/PrintStreamProvider.class */
class PrintStreamProvider extends ProviderSkeleton {
    private PrintStream stream;
    private String providerName;

    @Override // sun.tracing.ProviderSkeleton
    protected ProbeSkeleton createProbe(Method method) {
        return new PrintStreamProbe(this, getAnnotationString(method, ProbeName.class, method.getName()), method.getParameterTypes());
    }

    PrintStreamProvider(Class<? extends Provider> cls, PrintStream printStream) {
        super(cls);
        this.stream = printStream;
        this.providerName = getProviderName();
    }

    PrintStream getStream() {
        return this.stream;
    }

    String getName() {
        return this.providerName;
    }
}
