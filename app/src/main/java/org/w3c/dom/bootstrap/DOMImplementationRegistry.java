package org.w3c.dom.bootstrap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.DOMImplementationSource;

/* loaded from: rt.jar:org/w3c/dom/bootstrap/DOMImplementationRegistry.class */
public final class DOMImplementationRegistry {
    public static final String PROPERTY = "org.w3c.dom.DOMImplementationSourceList";
    private static final int DEFAULT_LINE_LENGTH = 80;
    private Vector sources;
    private static final String FALLBACK_CLASS = "com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl";
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal.dom";

    private DOMImplementationRegistry(Vector srcs) {
        this.sources = srcs;
    }

    public static DOMImplementationRegistry newInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException, ClassCastException {
        Class cls;
        Vector sources = new Vector();
        ClassLoader classLoader = getClassLoader();
        String p2 = getSystemProperty(PROPERTY);
        if (p2 == null) {
            p2 = getServiceValue(classLoader);
        }
        if (p2 == null) {
            p2 = FALLBACK_CLASS;
        }
        if (p2 != null) {
            StringTokenizer st = new StringTokenizer(p2);
            while (st.hasMoreTokens()) {
                String sourceName = st.nextToken();
                boolean internal = false;
                if (System.getSecurityManager() != null && sourceName != null && sourceName.startsWith(DEFAULT_PACKAGE)) {
                    internal = true;
                }
                if (classLoader != null && !internal) {
                    cls = classLoader.loadClass(sourceName);
                } else {
                    cls = Class.forName(sourceName);
                }
                Class sourceClass = cls;
                DOMImplementationSource source = (DOMImplementationSource) sourceClass.newInstance();
                sources.addElement(source);
            }
        }
        return new DOMImplementationRegistry(sources);
    }

    public DOMImplementation getDOMImplementation(String features) {
        int size = this.sources.size();
        for (int i2 = 0; i2 < size; i2++) {
            DOMImplementationSource source = (DOMImplementationSource) this.sources.elementAt(i2);
            DOMImplementation impl = source.getDOMImplementation(features);
            if (impl != null) {
                return impl;
            }
        }
        return null;
    }

    public DOMImplementationList getDOMImplementationList(String features) {
        final Vector implementations = new Vector();
        int size = this.sources.size();
        for (int i2 = 0; i2 < size; i2++) {
            DOMImplementationSource source = (DOMImplementationSource) this.sources.elementAt(i2);
            DOMImplementationList impls = source.getDOMImplementationList(features);
            for (int j2 = 0; j2 < impls.getLength(); j2++) {
                DOMImplementation impl = impls.item(j2);
                implementations.addElement(impl);
            }
        }
        return new DOMImplementationList() { // from class: org.w3c.dom.bootstrap.DOMImplementationRegistry.1
            @Override // org.w3c.dom.DOMImplementationList
            public DOMImplementation item(int index) {
                if (index >= 0 && index < implementations.size()) {
                    try {
                        return (DOMImplementation) implementations.elementAt(index);
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        return null;
                    }
                }
                return null;
            }

            @Override // org.w3c.dom.DOMImplementationList
            public int getLength() {
                return implementations.size();
            }
        };
    }

    public void addSource(DOMImplementationSource s2) {
        if (s2 == null) {
            throw new NullPointerException();
        }
        if (!this.sources.contains(s2)) {
            this.sources.addElement(s2);
        }
    }

    private static ClassLoader getClassLoader() {
        try {
            ClassLoader contextClassLoader = getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader;
            }
            return DOMImplementationRegistry.class.getClassLoader();
        } catch (Exception e2) {
            return DOMImplementationRegistry.class.getClassLoader();
        }
    }

    private static String getServiceValue(ClassLoader classLoader) {
        BufferedReader rd;
        try {
            InputStream is = getResourceAsStream(classLoader, "META-INF/services/org.w3c.dom.DOMImplementationSourceList");
            if (is != null) {
                try {
                    rd = new BufferedReader(new InputStreamReader(is, "UTF-8"), 80);
                } catch (UnsupportedEncodingException e2) {
                    rd = new BufferedReader(new InputStreamReader(is), 80);
                }
                String serviceValue = rd.readLine();
                rd.close();
                if (serviceValue == null) {
                    return null;
                }
                if (serviceValue.length() > 0) {
                    return serviceValue;
                }
                return null;
            }
            return null;
        } catch (Exception e3) {
            return null;
        }
    }

    private static boolean isJRE11() {
        try {
            Class.forName("java.security.AccessController");
            return false;
        } catch (Exception e2) {
            return true;
        }
    }

    private static ClassLoader getContextClassLoader() {
        if (isJRE11()) {
            return null;
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.w3c.dom.bootstrap.DOMImplementationRegistry.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader classLoader = null;
                try {
                    classLoader = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e2) {
                }
                return classLoader;
            }
        });
    }

    private static String getSystemProperty(final String name) {
        if (isJRE11()) {
            return System.getProperty(name);
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.w3c.dom.bootstrap.DOMImplementationRegistry.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(name);
            }
        });
    }

    private static InputStream getResourceAsStream(final ClassLoader classLoader, final String name) {
        InputStream ris;
        if (isJRE11()) {
            if (classLoader == null) {
                ris = ClassLoader.getSystemResourceAsStream(name);
            } else {
                ris = classLoader.getResourceAsStream(name);
            }
            return ris;
        }
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.w3c.dom.bootstrap.DOMImplementationRegistry.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                InputStream ris2;
                if (classLoader == null) {
                    ris2 = ClassLoader.getSystemResourceAsStream(name);
                } else {
                    ris2 = classLoader.getResourceAsStream(name);
                }
                return ris2;
            }
        });
    }
}
