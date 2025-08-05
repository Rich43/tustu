package com.sun.xml.internal.ws.policy.privateutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/ServiceFinder.class */
final class ServiceFinder<T> implements Iterable<T> {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) ServiceFinder.class);
    private static final String prefix = "META-INF/services/";
    private final Class<T> serviceClass;
    private final ClassLoader classLoader;

    static <T> ServiceFinder<T> find(Class<T> service, ClassLoader loader) {
        if (null == service) {
            throw ((NullPointerException) LOGGER.logSevereException(new NullPointerException(LocalizationMessages.WSP_0032_SERVICE_CAN_NOT_BE_NULL())));
        }
        return new ServiceFinder<>(service, loader);
    }

    public static <T> ServiceFinder<T> find(Class<T> service) {
        return find(service, Thread.currentThread().getContextClassLoader());
    }

    private ServiceFinder(Class<T> service, ClassLoader loader) {
        this.serviceClass = service;
        this.classLoader = loader;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<T> iterator() {
        return new LazyIterator(this.serviceClass, this.classLoader);
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
        ServiceConfigurationError sce = new ServiceConfigurationError(LocalizationMessages.WSP_0025_SPI_FAIL_SERVICE_MSG(service.getName(), msg));
        if (null != cause) {
            sce.initCause(cause);
        }
        throw ((ServiceConfigurationError) LOGGER.logSevereException(sce));
    }

    private static void fail(Class service, URL u2, int line, String msg, Throwable cause) throws ServiceConfigurationError {
        fail(service, LocalizationMessages.WSP_0024_SPI_FAIL_SERVICE_URL_LINE_MSG(u2, Integer.valueOf(line), msg), cause);
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
                fail(service, u2, lc, LocalizationMessages.WSP_0067_ILLEGAL_CFG_FILE_SYNTAX(), null);
            }
            int cp = ln2.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)) {
                fail(service, u2, lc, LocalizationMessages.WSP_0066_ILLEGAL_PROVIDER_CLASSNAME(ln2), null);
            }
            int iCharCount = Character.charCount(cp);
            while (true) {
                int i2 = iCharCount;
                if (i2 >= n2) {
                    break;
                }
                int cp2 = ln2.codePointAt(i2);
                if (!Character.isJavaIdentifierPart(cp2) && cp2 != 46) {
                    fail(service, u2, lc, LocalizationMessages.WSP_0066_ILLEGAL_PROVIDER_CLASSNAME(ln2), null);
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
                        fail(service, ": " + ((Object) y2), y2);
                    }
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException x2) {
                fail(service, ": " + ((Object) x2), x2);
                if (r2 != null) {
                    try {
                        r2.close();
                    } catch (IOException y3) {
                        fail(service, ": " + ((Object) y3), y3);
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
                    fail(service, ": " + ((Object) y4), y4);
                    throw th;
                }
            }
            if (in != null) {
                in.close();
            }
            throw th;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/ServiceFinder$LazyIterator.class */
    private static class LazyIterator<T> implements Iterator<T> {
        Class<T> service;
        ClassLoader loader;
        Enumeration<URL> configs;
        Iterator<String> pending;
        Set<String> returned;
        String nextName;

        private LazyIterator(Class<T> service, ClassLoader loader) {
            this.configs = null;
            this.pending = null;
            this.returned = new TreeSet();
            this.nextName = null;
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
                    ServiceFinder.fail(this.service, ": " + ((Object) x2), x2);
                }
            }
            while (true) {
                if (this.pending == null || !this.pending.hasNext()) {
                    if (!this.configs.hasMoreElements()) {
                        return false;
                    }
                    this.pending = ServiceFinder.parse(this.service, this.configs.nextElement2(), this.returned);
                } else {
                    this.nextName = this.pending.next();
                    return true;
                }
            }
        }

        @Override // java.util.Iterator
        public T next() throws ServiceConfigurationError {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String cn = this.nextName;
            this.nextName = null;
            try {
                return this.service.cast(Class.forName(cn, true, this.loader).newInstance());
            } catch (ClassNotFoundException x2) {
                ServiceFinder.fail(this.service, LocalizationMessages.WSP_0027_SERVICE_PROVIDER_NOT_FOUND(cn), x2);
                return null;
            } catch (Exception x3) {
                ServiceFinder.fail(this.service, LocalizationMessages.WSP_0028_SERVICE_PROVIDER_COULD_NOT_BE_INSTANTIATED(cn), x3);
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
