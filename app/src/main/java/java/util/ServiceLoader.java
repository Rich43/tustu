package java.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/util/ServiceLoader.class */
public final class ServiceLoader<S> implements Iterable<S> {
    private static final String PREFIX = "META-INF/services/";
    private final Class<S> service;
    private final ClassLoader loader;
    private final AccessControlContext acc;
    private LinkedHashMap<String, S> providers = new LinkedHashMap<>();
    private ServiceLoader<S>.LazyIterator lookupIterator;

    public void reload() {
        this.providers.clear();
        this.lookupIterator = new LazyIterator(this.service, this.loader);
    }

    private ServiceLoader(Class<S> cls, ClassLoader classLoader) {
        this.service = (Class) Objects.requireNonNull(cls, "Service interface cannot be null");
        this.loader = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
        this.acc = System.getSecurityManager() != null ? AccessController.getContext() : null;
        reload();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class<?> cls, String str, Throwable th) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(cls.getName() + ": " + str, th);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class<?> cls, String str) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(cls.getName() + ": " + str);
    }

    private static void fail(Class<?> cls, URL url, int i2, String str) throws ServiceConfigurationError {
        fail(cls, ((Object) url) + CallSiteDescriptor.TOKEN_DELIMITER + i2 + ": " + str);
    }

    private int parseLine(Class<?> cls, URL url, BufferedReader bufferedReader, int i2, List<String> list) throws ServiceConfigurationError, IOException {
        String line = bufferedReader.readLine();
        if (line == null) {
            return -1;
        }
        int iIndexOf = line.indexOf(35);
        if (iIndexOf >= 0) {
            line = line.substring(0, iIndexOf);
        }
        String strTrim = line.trim();
        int length = strTrim.length();
        if (length != 0) {
            if (strTrim.indexOf(32) >= 0 || strTrim.indexOf(9) >= 0) {
                fail(cls, url, i2, "Illegal configuration-file syntax");
            }
            int iCodePointAt = strTrim.codePointAt(0);
            if (!Character.isJavaIdentifierStart(iCodePointAt)) {
                fail(cls, url, i2, "Illegal provider-class name: " + strTrim);
            }
            int iCharCount = Character.charCount(iCodePointAt);
            while (true) {
                int i3 = iCharCount;
                if (i3 >= length) {
                    break;
                }
                int iCodePointAt2 = strTrim.codePointAt(i3);
                if (!Character.isJavaIdentifierPart(iCodePointAt2) && iCodePointAt2 != 46) {
                    fail(cls, url, i2, "Illegal provider-class name: " + strTrim);
                }
                iCharCount = i3 + Character.charCount(iCodePointAt2);
            }
            if (!this.providers.containsKey(strTrim) && !list.contains(strTrim)) {
                list.add(strTrim);
            }
        }
        return i2 + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Iterator<String> parse(Class<?> cls, URL url) throws ServiceConfigurationError {
        int line;
        InputStream inputStreamOpenStream = null;
        BufferedReader bufferedReader = null;
        ArrayList arrayList = new ArrayList();
        try {
            try {
                inputStreamOpenStream = url.openStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpenStream, "utf-8"));
                int i2 = 1;
                do {
                    line = parseLine(cls, url, bufferedReader, i2, arrayList);
                    i2 = line;
                } while (line >= 0);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        fail(cls, "Error closing configuration file", e2);
                    }
                }
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
            } catch (IOException e3) {
                fail(cls, "Error reading configuration file", e3);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                        fail(cls, "Error closing configuration file", e4);
                    }
                }
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
            }
            return arrayList.iterator();
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e5) {
                    fail(cls, "Error closing configuration file", e5);
                    throw th;
                }
            }
            if (inputStreamOpenStream != null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    /* loaded from: rt.jar:java/util/ServiceLoader$LazyIterator.class */
    private class LazyIterator implements Iterator<S> {
        Class<S> service;
        ClassLoader loader;
        Enumeration<URL> configs;
        Iterator<String> pending;
        String nextName;

        private LazyIterator(Class<S> cls, ClassLoader classLoader) {
            this.configs = null;
            this.pending = null;
            this.nextName = null;
            this.service = cls;
            this.loader = classLoader;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNextService() throws ServiceConfigurationError {
            if (this.nextName != null) {
                return true;
            }
            if (this.configs == null) {
                try {
                    String str = ServiceLoader.PREFIX + this.service.getName();
                    if (this.loader == null) {
                        this.configs = ClassLoader.getSystemResources(str);
                    } else {
                        this.configs = this.loader.getResources(str);
                    }
                } catch (IOException e2) {
                    ServiceLoader.fail(this.service, "Error locating configuration files", e2);
                }
            }
            while (true) {
                if (this.pending == null || !this.pending.hasNext()) {
                    if (!this.configs.hasMoreElements()) {
                        return false;
                    }
                    this.pending = ServiceLoader.this.parse(this.service, this.configs.nextElement2());
                } else {
                    this.nextName = this.pending.next();
                    return true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public S nextService() throws ServiceConfigurationError {
            if (!hasNextService()) {
                throw new NoSuchElementException();
            }
            String str = this.nextName;
            this.nextName = null;
            Class<?> cls = null;
            try {
                cls = Class.forName(str, false, this.loader);
            } catch (ClassNotFoundException e2) {
                ServiceLoader.fail(this.service, "Provider " + str + " not found");
            }
            if (!this.service.isAssignableFrom(cls)) {
                ServiceLoader.fail(this.service, "Provider " + str + " not a subtype");
            }
            try {
                S sCast = this.service.cast(cls.newInstance());
                ServiceLoader.this.providers.put(str, sCast);
                return sCast;
            } catch (Throwable th) {
                ServiceLoader.fail(this.service, "Provider " + str + " could not be instantiated", th);
                throw new Error();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (ServiceLoader.this.acc == null) {
                return hasNextService();
            }
            return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.util.ServiceLoader.LazyIterator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Boolean run2() {
                    return Boolean.valueOf(LazyIterator.this.hasNextService());
                }
            }, ServiceLoader.this.acc)).booleanValue();
        }

        @Override // java.util.Iterator
        public S next() {
            if (ServiceLoader.this.acc == null) {
                return (S) nextService();
            }
            return (S) AccessController.doPrivileged(new PrivilegedAction<S>() { // from class: java.util.ServiceLoader.LazyIterator.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public S run2() {
                    return (S) LazyIterator.this.nextService();
                }
            }, ServiceLoader.this.acc);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<S> iterator() {
        return new Iterator<S>() { // from class: java.util.ServiceLoader.1
            Iterator<Map.Entry<String, S>> knownProviders;

            {
                this.knownProviders = ServiceLoader.this.providers.entrySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (!this.knownProviders.hasNext()) {
                    return ServiceLoader.this.lookupIterator.hasNext();
                }
                return true;
            }

            @Override // java.util.Iterator
            public S next() {
                if (!this.knownProviders.hasNext()) {
                    return (S) ServiceLoader.this.lookupIterator.next();
                }
                return this.knownProviders.next().getValue();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <S> ServiceLoader<S> load(Class<S> cls, ClassLoader classLoader) {
        return new ServiceLoader<>(cls, classLoader);
    }

    public static <S> ServiceLoader<S> load(Class<S> cls) {
        return load(cls, Thread.currentThread().getContextClassLoader());
    }

    public static <S> ServiceLoader<S> loadInstalled(Class<S> cls) {
        ClassLoader classLoader = null;
        for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
            classLoader = systemClassLoader;
        }
        return load(cls, classLoader);
    }

    public String toString() {
        return "java.util.ServiceLoader[" + this.service.getName() + "]";
    }
}
