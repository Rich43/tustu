package com.sun.naming.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.naming.NamingEnumeration;

/* loaded from: rt.jar:com/sun/naming/internal/VersionHelper12.class */
final class VersionHelper12 extends VersionHelper {
    private static final String TRUST_URL_CODEBASE_PROPERTY = "com.sun.jndi.ldap.object.trustURLCodebase";
    private static final String trustURLCodebase = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.naming.internal.VersionHelper12.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public String run2() {
            try {
                return System.getProperty(VersionHelper12.TRUST_URL_CODEBASE_PROPERTY, "false");
            } catch (SecurityException e2) {
                return "false";
            }
        }
    });

    VersionHelper12() {
    }

    @Override // com.sun.naming.internal.VersionHelper
    public Class<?> loadClass(String str) throws ClassNotFoundException {
        return loadClass(str, getContextClassLoader());
    }

    @Override // com.sun.naming.internal.VersionHelper
    public Class<?> loadClassWithoutInit(String str) throws ClassNotFoundException {
        return loadClass(str, false, getContextClassLoader());
    }

    Class<?> loadClass(String str, boolean z2, ClassLoader classLoader) throws ClassNotFoundException {
        return Class.forName(str, z2, classLoader);
    }

    @Override // com.sun.naming.internal.VersionHelper
    Class<?> loadClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        return loadClass(str, true, classLoader);
    }

    @Override // com.sun.naming.internal.VersionHelper
    public Class<?> loadClass(String str, String str2) throws MalformedURLException, ClassNotFoundException {
        if ("true".equalsIgnoreCase(trustURLCodebase)) {
            return loadClass(str, URLClassLoader.newInstance(getUrlArray(str2), getContextClassLoader()));
        }
        return null;
    }

    @Override // com.sun.naming.internal.VersionHelper
    String getJndiProperty(final int i2) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.naming.internal.VersionHelper12.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    return System.getProperty(VersionHelper.PROPS[i2]);
                } catch (SecurityException e2) {
                    return null;
                }
            }
        });
    }

    @Override // com.sun.naming.internal.VersionHelper
    String[] getJndiProperties() {
        Properties properties = (Properties) AccessController.doPrivileged(new PrivilegedAction<Properties>() { // from class: com.sun.naming.internal.VersionHelper12.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Properties run2() {
                try {
                    return System.getProperties();
                } catch (SecurityException e2) {
                    return null;
                }
            }
        });
        if (properties == null) {
            return null;
        }
        String[] strArr = new String[PROPS.length];
        for (int i2 = 0; i2 < PROPS.length; i2++) {
            strArr[i2] = properties.getProperty(PROPS[i2]);
        }
        return strArr;
    }

    @Override // com.sun.naming.internal.VersionHelper
    InputStream getResourceAsStream(final Class<?> cls, final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: com.sun.naming.internal.VersionHelper12.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InputStream run2() {
                return cls.getResourceAsStream(str);
            }
        });
    }

    @Override // com.sun.naming.internal.VersionHelper
    InputStream getJavaHomeLibStream(final String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: com.sun.naming.internal.VersionHelper12.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InputStream run2() {
                try {
                    String property = System.getProperty("java.home");
                    if (property == null) {
                        return null;
                    }
                    return new FileInputStream(property + File.separator + "lib" + File.separator + str);
                } catch (Exception e2) {
                    return null;
                }
            }
        });
    }

    @Override // com.sun.naming.internal.VersionHelper
    NamingEnumeration<InputStream> getResources(final ClassLoader classLoader, final String str) throws IOException {
        try {
            return new InputStreamEnumeration((Enumeration) AccessController.doPrivileged(new PrivilegedExceptionAction<Enumeration<URL>>() { // from class: com.sun.naming.internal.VersionHelper12.6
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Enumeration<URL> run() throws IOException {
                    if (classLoader == null) {
                        return ClassLoader.getSystemResources(str);
                    }
                    return classLoader.getResources(str);
                }
            }));
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    @Override // com.sun.naming.internal.VersionHelper
    ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.naming.internal.VersionHelper12.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader == null) {
                    contextClassLoader = ClassLoader.getSystemClassLoader();
                }
                return contextClassLoader;
            }
        });
    }

    /* loaded from: rt.jar:com/sun/naming/internal/VersionHelper12$InputStreamEnumeration.class */
    class InputStreamEnumeration implements NamingEnumeration<InputStream> {
        private final Enumeration<URL> urls;
        private InputStream nextElement = null;

        InputStreamEnumeration(Enumeration<URL> enumeration) {
            this.urls = enumeration;
        }

        private InputStream getNextElement() {
            return (InputStream) AccessController.doPrivileged(new PrivilegedAction<InputStream>() { // from class: com.sun.naming.internal.VersionHelper12.InputStreamEnumeration.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public InputStream run2() {
                    while (InputStreamEnumeration.this.urls.hasMoreElements()) {
                        try {
                            return ((URL) InputStreamEnumeration.this.urls.nextElement2()).openStream();
                        } catch (IOException e2) {
                        }
                    }
                    return null;
                }
            });
        }

        @Override // javax.naming.NamingEnumeration
        public boolean hasMore() {
            if (this.nextElement != null) {
                return true;
            }
            this.nextElement = getNextElement();
            return this.nextElement != null;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return hasMore();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.naming.NamingEnumeration
        public InputStream next() {
            if (hasMore()) {
                InputStream inputStream = this.nextElement;
                this.nextElement = null;
                return inputStream;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public InputStream nextElement2() {
            return next();
        }

        @Override // javax.naming.NamingEnumeration
        public void close() {
        }
    }
}
