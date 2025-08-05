package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentEx;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder.class */
public final class ServiceFinder<T> implements Iterable<T> {
    private static final String prefix = "META-INF/services/";
    private static WeakHashMap<ClassLoader, ConcurrentHashMap<String, ServiceName[]>> serviceNameCache = new WeakHashMap<>();
    private final Class<T> serviceClass;

    @Nullable
    private final ClassLoader classLoader;

    @Nullable
    private final ComponentEx component;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder$ServiceName.class */
    private static class ServiceName {
        final String className;
        final URL config;

        public ServiceName(String className, URL config) {
            this.className = className;
            this.config = config;
        }
    }

    public static <T> ServiceFinder<T> find(@NotNull Class<T> service, @Nullable ClassLoader loader, Component component) {
        return new ServiceFinder<>(service, loader, component);
    }

    public static <T> ServiceFinder<T> find(@NotNull Class<T> service, Component component) {
        return find(service, Thread.currentThread().getContextClassLoader(), component);
    }

    public static <T> ServiceFinder<T> find(@NotNull Class<T> service, @Nullable ClassLoader loader) {
        return find(service, loader, ContainerResolver.getInstance().getContainer());
    }

    public static <T> ServiceFinder<T> find(Class<T> service) {
        return find(service, Thread.currentThread().getContextClassLoader());
    }

    private ServiceFinder(Class<T> service, ClassLoader loader, Component component) {
        this.serviceClass = service;
        this.classLoader = loader;
        this.component = getComponentEx(component);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ServiceName[] serviceClassNames(Class serviceClass, ClassLoader classLoader) {
        ArrayList<ServiceName> l2 = new ArrayList<>();
        Iterator<ServiceName> it = new ServiceNameIterator(serviceClass, classLoader);
        while (it.hasNext()) {
            l2.add(it.next());
        }
        return (ServiceName[]) l2.toArray(new ServiceName[l2.size()]);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<T> iterator() {
        Iterator<T> it = new LazyIterator<>(this.serviceClass, this.classLoader);
        return this.component != null ? new CompositeIterator(this.component.getIterableSPI(this.serviceClass).iterator(), it) : it;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T[] toArray() {
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return (T[]) arrayList.toArray((Object[]) Array.newInstance((Class<?>) this.serviceClass, arrayList.size()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class service, String msg, Throwable cause) throws ServiceConfigurationError {
        ServiceConfigurationError sce = new ServiceConfigurationError(service.getName() + ": " + msg);
        sce.initCause(cause);
        throw sce;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class service, String msg) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class service, URL u2, int line, String msg) throws ServiceConfigurationError {
        fail(service, ((Object) u2) + CallSiteDescriptor.TOKEN_DELIMITER + line + ": " + msg);
    }

    private static int parseLine(Class service, URL u2, BufferedReader r2, int lc, List<String> names, Set<String> returned) throws ServiceConfigurationError, IOException {
        String ln = r2.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf(35);
        if (ci >= 0) {
            ln = ln.substring(0, ci);
        }
        String ln2 = ln.trim();
        int n2 = ln2.length();
        if (n2 != 0) {
            if (ln2.indexOf(32) >= 0 || ln2.indexOf(9) >= 0) {
                fail(service, u2, lc, "Illegal configuration-file syntax");
            }
            int cp = ln2.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)) {
                fail(service, u2, lc, "Illegal provider-class name: " + ln2);
            }
            int iCharCount = Character.charCount(cp);
            while (true) {
                int i2 = iCharCount;
                if (i2 >= n2) {
                    break;
                }
                int cp2 = ln2.codePointAt(i2);
                if (!Character.isJavaIdentifierPart(cp2) && cp2 != 46) {
                    fail(service, u2, lc, "Illegal provider-class name: " + ln2);
                }
                iCharCount = i2 + Character.charCount(cp2);
            }
            if (!returned.contains(ln2)) {
                names.add(ln2);
                returned.add(ln2);
            }
        }
        return lc + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterator<String> parse(Class service, URL u2, Set<String> returned) throws ServiceConfigurationError {
        int line;
        InputStream in = null;
        BufferedReader r2 = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            try {
                in = u2.openStream();
                r2 = new BufferedReader(new InputStreamReader(in, "utf-8"));
                int lc = 1;
                do {
                    line = parseLine(service, u2, r2, lc, names, returned);
                    lc = line;
                } while (line >= 0);
                if (r2 != null) {
                    try {
                        r2.close();
                    } catch (IOException y2) {
                        fail(service, ": " + ((Object) y2));
                    }
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException x2) {
                fail(service, ": " + ((Object) x2));
                if (r2 != null) {
                    try {
                        r2.close();
                    } catch (IOException y3) {
                        fail(service, ": " + ((Object) y3));
                    }
                }
                if (in != null) {
                    in.close();
                }
            }
            return names.iterator();
        } catch (Throwable th) {
            if (r2 != null) {
                try {
                    r2.close();
                } catch (IOException y4) {
                    fail(service, ": " + ((Object) y4));
                    throw th;
                }
            }
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    private static ComponentEx getComponentEx(Component component) {
        if (component instanceof ComponentEx) {
            return (ComponentEx) component;
        }
        if (component != null) {
            return new ComponentExWrapper(component);
        }
        return null;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder$ComponentExWrapper.class */
    private static class ComponentExWrapper implements ComponentEx {
        private final Component component;

        public ComponentExWrapper(Component component) {
            this.component = component;
        }

        @Override // com.sun.xml.internal.ws.api.Component
        public <S> S getSPI(Class<S> cls) {
            return (S) this.component.getSPI(cls);
        }

        @Override // com.sun.xml.internal.ws.api.ComponentEx
        public <S> Iterable<S> getIterableSPI(Class<S> spiType) {
            Object spi = getSPI(spiType);
            if (spi != null) {
                Collection<S> c2 = Collections.singletonList(spi);
                return c2;
            }
            return Collections.emptySet();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder$CompositeIterator.class */
    private static class CompositeIterator<T> implements Iterator<T> {
        private final Iterator<Iterator<T>> it;
        private Iterator<T> current = null;

        public CompositeIterator(Iterator<T>... iterators) {
            this.it = Arrays.asList(iterators).iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.current != null && this.current.hasNext()) {
                return true;
            }
            while (this.it.hasNext()) {
                this.current = this.it.next();
                if (this.current.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return this.current.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder$ServiceNameIterator.class */
    private static class ServiceNameIterator implements Iterator<ServiceName> {
        Class service;

        @Nullable
        ClassLoader loader;
        Enumeration<URL> configs;
        Iterator<String> pending;
        Set<String> returned;
        String nextName;
        URL currentConfig;

        private ServiceNameIterator(Class service, ClassLoader loader) {
            this.configs = null;
            this.pending = null;
            this.returned = new TreeSet();
            this.nextName = null;
            this.currentConfig = null;
            this.service = service;
            this.loader = loader;
        }

        @Override // java.util.Iterator
        public boolean hasNext() throws ServiceConfigurationError {
            if (this.nextName != null) {
                return true;
            }
            if (this.configs == null) {
                try {
                    String fullName = ServiceFinder.prefix + this.service.getName();
                    if (this.loader == null) {
                        this.configs = ClassLoader.getSystemResources(fullName);
                    } else {
                        this.configs = this.loader.getResources(fullName);
                    }
                } catch (IOException x2) {
                    ServiceFinder.fail(this.service, ": " + ((Object) x2));
                }
            }
            while (true) {
                if (this.pending == null || !this.pending.hasNext()) {
                    if (!this.configs.hasMoreElements()) {
                        return false;
                    }
                    this.currentConfig = this.configs.nextElement2();
                    this.pending = ServiceFinder.parse(this.service, this.currentConfig, this.returned);
                } else {
                    this.nextName = this.pending.next();
                    return true;
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public ServiceName next() throws ServiceConfigurationError {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String cn = this.nextName;
            this.nextName = null;
            return new ServiceName(cn, this.currentConfig);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/ServiceFinder$LazyIterator.class */
    private static class LazyIterator<T> implements Iterator<T> {
        Class<T> service;

        @Nullable
        ClassLoader loader;
        ServiceName[] names;
        int index;

        private LazyIterator(Class<T> service, ClassLoader loader) {
            this.service = service;
            this.loader = loader;
            this.names = null;
            this.index = 0;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            ConcurrentHashMap<String, ServiceName[]> nameMap;
            if (this.names == null) {
                synchronized (ServiceFinder.serviceNameCache) {
                    nameMap = (ConcurrentHashMap) ServiceFinder.serviceNameCache.get(this.loader);
                }
                this.names = nameMap != null ? nameMap.get(this.service.getName()) : null;
                if (this.names == null) {
                    this.names = ServiceFinder.serviceClassNames(this.service, this.loader);
                    if (nameMap == null) {
                        nameMap = new ConcurrentHashMap<>();
                    }
                    nameMap.put(this.service.getName(), this.names);
                    synchronized (ServiceFinder.serviceNameCache) {
                        ServiceFinder.serviceNameCache.put(this.loader, nameMap);
                    }
                }
            }
            return this.index < this.names.length;
        }

        @Override // java.util.Iterator
        public T next() throws ServiceConfigurationError {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            ServiceName[] serviceNameArr = this.names;
            int i2 = this.index;
            this.index = i2 + 1;
            ServiceName sn = serviceNameArr[i2];
            String cn = sn.className;
            URL currentConfig = sn.config;
            try {
                return this.service.cast(Class.forName(cn, true, this.loader).newInstance());
            } catch (ClassNotFoundException e2) {
                ServiceFinder.fail(this.service, "Provider " + cn + " is specified in " + ((Object) currentConfig) + " but not found");
                return null;
            } catch (Exception x2) {
                ServiceFinder.fail(this.service, "Provider " + cn + " is specified in " + ((Object) currentConfig) + "but could not be instantiated: " + ((Object) x2), x2);
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
