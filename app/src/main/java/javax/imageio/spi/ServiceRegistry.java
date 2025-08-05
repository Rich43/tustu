package javax.imageio.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/* loaded from: rt.jar:javax/imageio/spi/ServiceRegistry.class */
public class ServiceRegistry {
    private Map categoryMap = new HashMap();

    /* loaded from: rt.jar:javax/imageio/spi/ServiceRegistry$Filter.class */
    public interface Filter {
        boolean filter(Object obj);
    }

    public ServiceRegistry(Iterator<Class<?>> it) {
        if (it == null) {
            throw new IllegalArgumentException("categories == null!");
        }
        while (it.hasNext()) {
            Class<?> next = it.next();
            this.categoryMap.put(next, new SubRegistry(this, next));
        }
    }

    public static <T> Iterator<T> lookupProviders(Class<T> cls, ClassLoader classLoader) {
        if (cls == null) {
            throw new IllegalArgumentException("providerClass == null!");
        }
        return ServiceLoader.load(cls, classLoader).iterator();
    }

    public static <T> Iterator<T> lookupProviders(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("providerClass == null!");
        }
        return ServiceLoader.load(cls).iterator();
    }

    public Iterator<Class<?>> getCategories() {
        return this.categoryMap.keySet().iterator();
    }

    private Iterator getSubRegistries(Object obj) {
        ArrayList arrayList = new ArrayList();
        for (Class cls : this.categoryMap.keySet()) {
            if (cls.isAssignableFrom(obj.getClass())) {
                arrayList.add((SubRegistry) this.categoryMap.get(cls));
            }
        }
        return arrayList.iterator();
    }

    public <T> boolean registerServiceProvider(T t2, Class<T> cls) {
        if (t2 == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (!cls.isAssignableFrom(t2.getClass())) {
            throw new ClassCastException();
        }
        return subRegistry.registerServiceProvider(t2);
    }

    public void registerServiceProvider(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        Iterator subRegistries = getSubRegistries(obj);
        while (subRegistries.hasNext()) {
            ((SubRegistry) subRegistries.next()).registerServiceProvider(obj);
        }
    }

    public void registerServiceProviders(Iterator<?> it) {
        if (it == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        while (it.hasNext()) {
            registerServiceProvider(it.next());
        }
    }

    public <T> boolean deregisterServiceProvider(T t2, Class<T> cls) {
        if (t2 == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (!cls.isAssignableFrom(t2.getClass())) {
            throw new ClassCastException();
        }
        return subRegistry.deregisterServiceProvider(t2);
    }

    public void deregisterServiceProvider(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        Iterator subRegistries = getSubRegistries(obj);
        while (subRegistries.hasNext()) {
            ((SubRegistry) subRegistries.next()).deregisterServiceProvider(obj);
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("provider == null!");
        }
        Iterator subRegistries = getSubRegistries(obj);
        while (subRegistries.hasNext()) {
            if (((SubRegistry) subRegistries.next()).contains(obj)) {
                return true;
            }
        }
        return false;
    }

    public <T> Iterator<T> getServiceProviders(Class<T> cls, boolean z2) {
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        return subRegistry.getServiceProviders(z2);
    }

    public <T> Iterator<T> getServiceProviders(Class<T> cls, Filter filter, boolean z2) {
        if (((SubRegistry) this.categoryMap.get(cls)) == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        return new FilterIterator(getServiceProviders(cls, z2), filter);
    }

    public <T> T getServiceProviderByClass(Class<T> cls) {
        T t2;
        if (cls == null) {
            throw new IllegalArgumentException("providerClass == null!");
        }
        for (Class cls2 : this.categoryMap.keySet()) {
            if (cls2.isAssignableFrom(cls) && (t2 = (T) ((SubRegistry) this.categoryMap.get(cls2)).getServiceProviderByClass(cls)) != null) {
                return t2;
            }
        }
        return null;
    }

    public <T> boolean setOrdering(Class<T> cls, T t2, T t3) {
        if (t2 == null || t3 == null) {
            throw new IllegalArgumentException("provider is null!");
        }
        if (t2 == t3) {
            throw new IllegalArgumentException("providers are the same!");
        }
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (subRegistry.contains(t2) && subRegistry.contains(t3)) {
            return subRegistry.setOrdering(t2, t3);
        }
        return false;
    }

    public <T> boolean unsetOrdering(Class<T> cls, T t2, T t3) {
        if (t2 == null || t3 == null) {
            throw new IllegalArgumentException("provider is null!");
        }
        if (t2 == t3) {
            throw new IllegalArgumentException("providers are the same!");
        }
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        if (subRegistry.contains(t2) && subRegistry.contains(t3)) {
            return subRegistry.unsetOrdering(t2, t3);
        }
        return false;
    }

    public void deregisterAll(Class<?> cls) {
        SubRegistry subRegistry = (SubRegistry) this.categoryMap.get(cls);
        if (subRegistry == null) {
            throw new IllegalArgumentException("category unknown!");
        }
        subRegistry.clear();
    }

    public void deregisterAll() {
        Iterator it = this.categoryMap.values().iterator();
        while (it.hasNext()) {
            ((SubRegistry) it.next()).clear();
        }
    }

    public void finalize() throws Throwable {
        deregisterAll();
        super.finalize();
    }
}
