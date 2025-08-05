package sun.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:sun/misc/Service.class */
public final class Service<S> {
    private static final String prefix = "META-INF/services/";

    private Service() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class<?> cls, String str, Throwable th) throws ServiceConfigurationError {
        ServiceConfigurationError serviceConfigurationError = new ServiceConfigurationError(cls.getName() + ": " + str);
        serviceConfigurationError.initCause(th);
        throw serviceConfigurationError;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fail(Class<?> cls, String str) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(cls.getName() + ": " + str);
    }

    private static void fail(Class<?> cls, URL url, int i2, String str) throws ServiceConfigurationError {
        fail(cls, ((Object) url) + CallSiteDescriptor.TOKEN_DELIMITER + i2 + ": " + str);
    }

    private static int parseLine(Class<?> cls, URL url, BufferedReader bufferedReader, int i2, List<String> list, Set<String> set) throws ServiceConfigurationError, IOException {
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
            if (!set.contains(strTrim)) {
                list.add(strTrim);
                set.add(strTrim);
            }
        }
        return i2 + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Iterator<String> parse(Class<?> cls, URL url, Set<String> set) throws ServiceConfigurationError {
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
                    line = parseLine(cls, url, bufferedReader, i2, arrayList, set);
                    i2 = line;
                } while (line >= 0);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        fail(cls, ": " + ((Object) e2));
                    }
                }
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
            } catch (IOException e3) {
                fail(cls, ": " + ((Object) e3));
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                        fail(cls, ": " + ((Object) e4));
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
                    fail(cls, ": " + ((Object) e5));
                    throw th;
                }
            }
            if (inputStreamOpenStream != null) {
                inputStreamOpenStream.close();
            }
            throw th;
        }
    }

    /* loaded from: rt.jar:sun/misc/Service$LazyIterator.class */
    private static class LazyIterator<S> implements Iterator<S> {
        Class<S> service;
        ClassLoader loader;
        Enumeration<URL> configs;
        Iterator<String> pending;
        Set<String> returned;
        String nextName;

        private LazyIterator(Class<S> cls, ClassLoader classLoader) {
            this.configs = null;
            this.pending = null;
            this.returned = new TreeSet();
            this.nextName = null;
            this.service = cls;
            this.loader = classLoader;
        }

        @Override // java.util.Iterator
        public boolean hasNext() throws ServiceConfigurationError {
            if (this.nextName != null) {
                return true;
            }
            if (this.configs == null) {
                try {
                    String str = Service.prefix + this.service.getName();
                    if (this.loader == null) {
                        this.configs = ClassLoader.getSystemResources(str);
                    } else {
                        this.configs = this.loader.getResources(str);
                    }
                } catch (IOException e2) {
                    Service.fail(this.service, ": " + ((Object) e2));
                }
            }
            while (true) {
                if (this.pending == null || !this.pending.hasNext()) {
                    if (!this.configs.hasMoreElements()) {
                        return false;
                    }
                    this.pending = Service.parse(this.service, this.configs.nextElement2(), this.returned);
                } else {
                    this.nextName = this.pending.next();
                    return true;
                }
            }
        }

        @Override // java.util.Iterator
        public S next() throws ServiceConfigurationError {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            String str = this.nextName;
            this.nextName = null;
            Class<?> cls = null;
            try {
                cls = Class.forName(str, false, this.loader);
            } catch (ClassNotFoundException e2) {
                Service.fail(this.service, "Provider " + str + " not found");
            }
            if (!this.service.isAssignableFrom(cls)) {
                Service.fail(this.service, "Provider " + str + " not a subtype");
            }
            try {
                return this.service.cast(cls.newInstance());
            } catch (Throwable th) {
                Service.fail(this.service, "Provider " + str + " could not be instantiated", th);
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static <S> Iterator<S> providers(Class<S> cls, ClassLoader classLoader) throws ServiceConfigurationError {
        return new LazyIterator(cls, classLoader);
    }

    public static <S> Iterator<S> providers(Class<S> cls) throws ServiceConfigurationError {
        return providers(cls, Thread.currentThread().getContextClassLoader());
    }

    public static <S> Iterator<S> installedProviders(Class<S> cls) throws ServiceConfigurationError {
        ClassLoader classLoader = null;
        for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
            classLoader = systemClassLoader;
        }
        return providers(cls, classLoader);
    }
}
