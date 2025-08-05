package com.sun.xml.internal.ws.api.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/client/ServiceInterceptorFactory.class */
public abstract class ServiceInterceptorFactory {
    private static ThreadLocal<Set<ServiceInterceptorFactory>> threadLocalFactories = new ThreadLocal<Set<ServiceInterceptorFactory>>() { // from class: com.sun.xml.internal.ws.api.client.ServiceInterceptorFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Set<ServiceInterceptorFactory> initialValue() {
            return new HashSet();
        }
    };

    public abstract ServiceInterceptor create(@NotNull WSService wSService);

    @NotNull
    public static ServiceInterceptor load(@NotNull WSService service, @Nullable ClassLoader cl) {
        List<ServiceInterceptor> l2 = new ArrayList<>();
        Iterator it = ServiceFinder.find(ServiceInterceptorFactory.class).iterator();
        while (it.hasNext()) {
            ServiceInterceptorFactory f2 = (ServiceInterceptorFactory) it.next();
            l2.add(f2.create(service));
        }
        for (ServiceInterceptorFactory f3 : threadLocalFactories.get()) {
            l2.add(f3.create(service));
        }
        return ServiceInterceptor.aggregate((ServiceInterceptor[]) l2.toArray(new ServiceInterceptor[l2.size()]));
    }

    public static boolean registerForThread(ServiceInterceptorFactory factory) {
        return threadLocalFactories.get().add(factory);
    }

    public static boolean unregisterForThread(ServiceInterceptorFactory factory) {
        return threadLocalFactories.get().remove(factory);
    }
}
