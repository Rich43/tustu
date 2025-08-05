package javax.imageio.spi;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: ServiceRegistry.java */
/* loaded from: rt.jar:javax/imageio/spi/SubRegistry.class */
class SubRegistry {
    ServiceRegistry registry;
    Class category;
    final PartiallyOrderedSet poset = new PartiallyOrderedSet();
    final Map<Class<?>, Object> map = new HashMap();
    final Map<Class<?>, AccessControlContext> accMap = new HashMap();

    public SubRegistry(ServiceRegistry serviceRegistry, Class cls) {
        this.registry = serviceRegistry;
        this.category = cls;
    }

    public boolean registerServiceProvider(Object obj) {
        Object obj2 = this.map.get(obj.getClass());
        boolean z2 = obj2 != null;
        if (z2) {
            deregisterServiceProvider(obj2);
        }
        this.map.put(obj.getClass(), obj);
        this.accMap.put(obj.getClass(), AccessController.getContext());
        this.poset.add(obj);
        if (obj instanceof RegisterableService) {
            ((RegisterableService) obj).onRegistration(this.registry, this.category);
        }
        return !z2;
    }

    public boolean deregisterServiceProvider(Object obj) {
        if (obj == this.map.get(obj.getClass())) {
            this.map.remove(obj.getClass());
            this.accMap.remove(obj.getClass());
            this.poset.remove(obj);
            if (obj instanceof RegisterableService) {
                ((RegisterableService) obj).onDeregistration(this.registry, this.category);
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean contains(Object obj) {
        return this.map.get(obj.getClass()) == obj;
    }

    public boolean setOrdering(Object obj, Object obj2) {
        return this.poset.setOrdering(obj, obj2);
    }

    public boolean unsetOrdering(Object obj, Object obj2) {
        return this.poset.unsetOrdering(obj, obj2);
    }

    public Iterator getServiceProviders(boolean z2) {
        if (z2) {
            return this.poset.iterator();
        }
        return this.map.values().iterator();
    }

    public <T> T getServiceProviderByClass(Class<T> cls) {
        return (T) this.map.get(cls);
    }

    public void clear() {
        Iterator<Object> it = this.map.values().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            it.remove();
            if (next instanceof RegisterableService) {
                RegisterableService registerableService = (RegisterableService) next;
                AccessControlContext accessControlContext = this.accMap.get(next.getClass());
                if (accessControlContext != null || System.getSecurityManager() == null) {
                    AccessController.doPrivileged(() -> {
                        registerableService.onDeregistration(this.registry, this.category);
                        return null;
                    }, accessControlContext);
                }
            }
        }
        this.poset.clear();
        this.accMap.clear();
    }

    public void finalize() {
        clear();
    }
}
