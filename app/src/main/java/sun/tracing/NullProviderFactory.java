package sun.tracing;

import com.sun.tracing.Provider;
import com.sun.tracing.ProviderFactory;

/* loaded from: rt.jar:sun/tracing/NullProviderFactory.class */
public class NullProviderFactory extends ProviderFactory {
    @Override // com.sun.tracing.ProviderFactory
    public <T extends Provider> T createProvider(Class<T> cls) {
        NullProvider nullProvider = new NullProvider(cls);
        nullProvider.init();
        return (T) nullProvider.newProxyInstance();
    }
}
