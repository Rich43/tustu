package sun.tracing;

import com.sun.tracing.Provider;
import com.sun.tracing.ProviderFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: rt.jar:sun/tracing/MultiplexProviderFactory.class */
public class MultiplexProviderFactory extends ProviderFactory {
    private Set<ProviderFactory> factories;

    public MultiplexProviderFactory(Set<ProviderFactory> set) {
        this.factories = set;
    }

    @Override // com.sun.tracing.ProviderFactory
    public <T extends Provider> T createProvider(Class<T> cls) {
        HashSet hashSet = new HashSet();
        Iterator<ProviderFactory> it = this.factories.iterator();
        while (it.hasNext()) {
            hashSet.add(it.next().createProvider(cls));
        }
        MultiplexProvider multiplexProvider = new MultiplexProvider(cls, hashSet);
        multiplexProvider.init();
        return (T) multiplexProvider.newProxyInstance();
    }
}
