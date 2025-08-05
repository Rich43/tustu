package sun.tracing;

import com.sun.tracing.Provider;
import java.lang.reflect.Method;

/* compiled from: NullProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/NullProvider.class */
class NullProvider extends ProviderSkeleton {
    NullProvider(Class<? extends Provider> cls) {
        super(cls);
    }

    @Override // sun.tracing.ProviderSkeleton
    protected ProbeSkeleton createProbe(Method method) {
        return new NullProbe(method.getParameterTypes());
    }
}
