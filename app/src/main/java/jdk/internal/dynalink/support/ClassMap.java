package jdk.internal.dynalink.support;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/ClassMap.class */
public abstract class ClassMap<T> {
    private final ConcurrentMap<Class<?>, T> map = new ConcurrentHashMap();
    private final Map<Class<?>, Reference<T>> weakMap = new WeakHashMap();
    private final ClassLoader classLoader;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract T computeValue(Class<?> cls);

    static {
        $assertionsDisabled = !ClassMap.class.desiredAssertionStatus();
    }

    protected ClassMap(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public T get(final Class<?> clazz) {
        Reference<T> ref;
        T oldV;
        T refv;
        T v2 = this.map.get(clazz);
        if (v2 != null) {
            return v2;
        }
        synchronized (this.weakMap) {
            ref = this.weakMap.get(clazz);
        }
        if (ref != null && (refv = ref.get()) != null) {
            return refv;
        }
        T newV = computeValue(clazz);
        if (!$assertionsDisabled && newV == null) {
            throw new AssertionError();
        }
        ClassLoader clazzLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: jdk.internal.dynalink.support.ClassMap.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
        if (Guards.canReferenceDirectly(this.classLoader, clazzLoader)) {
            T oldV2 = this.map.putIfAbsent(clazz, newV);
            return oldV2 != null ? oldV2 : newV;
        }
        synchronized (this.weakMap) {
            Reference<T> ref2 = this.weakMap.get(clazz);
            if (ref2 != null && (oldV = ref2.get()) != null) {
                return oldV;
            }
            this.weakMap.put(clazz, new SoftReference(newV));
            return newV;
        }
    }
}
