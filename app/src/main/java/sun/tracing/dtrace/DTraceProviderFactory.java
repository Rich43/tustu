package sun.tracing.dtrace;

import com.sun.tracing.Provider;
import com.sun.tracing.ProviderFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:sun/tracing/dtrace/DTraceProviderFactory.class */
public final class DTraceProviderFactory extends ProviderFactory {
    @Override // com.sun.tracing.ProviderFactory
    public <T extends Provider> T createProvider(Class<T> cls) {
        DTraceProvider dTraceProvider = new DTraceProvider(cls);
        T t2 = (T) dTraceProvider.newProxyInstance();
        dTraceProvider.setProxy(t2);
        dTraceProvider.init();
        new Activation(dTraceProvider.getModuleName(), new DTraceProvider[]{dTraceProvider});
        return t2;
    }

    public Map<Class<? extends Provider>, Provider> createProviders(Set<Class<? extends Provider>> set, String str) {
        HashMap map = new HashMap();
        HashSet hashSet = new HashSet();
        for (Class<? extends Provider> cls : set) {
            DTraceProvider dTraceProvider = new DTraceProvider(cls);
            hashSet.add(dTraceProvider);
            map.put(cls, dTraceProvider.newProxyInstance());
        }
        new Activation(str, (DTraceProvider[]) hashSet.toArray(new DTraceProvider[0]));
        return map;
    }

    public static boolean isSupported() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new RuntimePermission("com.sun.tracing.dtrace.createProvider"));
            }
            return JVM.isSupported();
        } catch (SecurityException e2) {
            return false;
        }
    }
}
