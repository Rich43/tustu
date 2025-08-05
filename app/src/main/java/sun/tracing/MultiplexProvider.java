package sun.tracing;

import com.sun.tracing.Provider;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/* compiled from: MultiplexProviderFactory.java */
/* loaded from: rt.jar:sun/tracing/MultiplexProvider.class */
class MultiplexProvider extends ProviderSkeleton {
    private Set<Provider> providers;

    @Override // sun.tracing.ProviderSkeleton
    protected ProbeSkeleton createProbe(Method method) {
        return new MultiplexProbe(method, this.providers);
    }

    MultiplexProvider(Class<? extends Provider> cls, Set<Provider> set) {
        super(cls);
        this.providers = set;
    }

    @Override // sun.tracing.ProviderSkeleton, com.sun.tracing.Provider
    public void dispose() {
        Iterator<Provider> it = this.providers.iterator();
        while (it.hasNext()) {
            it.next().dispose();
        }
        super.dispose();
    }
}
