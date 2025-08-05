package com.sun.org.apache.bcel.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/SecuritySupport.class */
public final class SecuritySupport {
    private static final SecuritySupport securitySupport = new SecuritySupport();

    public static SecuritySupport getInstance() {
        return securitySupport;
    }

    static java.lang.ClassLoader getContextClassLoader() {
        return (java.lang.ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                java.lang.ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    static java.lang.ClassLoader getSystemClassLoader() {
        return (java.lang.ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                java.lang.ClassLoader cl = null;
                try {
                    cl = java.lang.ClassLoader.getSystemClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    static java.lang.ClassLoader getParentClassLoader(final java.lang.ClassLoader cl) {
        return (java.lang.ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                java.lang.ClassLoader parent = null;
                try {
                    parent = cl.getParent();
                } catch (SecurityException e2) {
                }
                if (parent == cl) {
                    return null;
                }
                return parent;
            }
        });
    }

    public static String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(propName);
            }
        });
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.5
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }

    public static ListResourceBundle getResourceBundle(String bundle) {
        return getResourceBundle(bundle, Locale.getDefault());
    }

    public static ListResourceBundle getResourceBundle(final String bundle, final Locale locale) {
        return (ListResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ListResourceBundle>() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ListResourceBundle run2() {
                try {
                    return (ListResourceBundle) ResourceBundle.getBundle(bundle, locale);
                } catch (MissingResourceException e2) {
                    try {
                        return (ListResourceBundle) ResourceBundle.getBundle(bundle, new Locale("en", "US"));
                    } catch (MissingResourceException e3) {
                        throw new MissingResourceException("Could not load any resource bundle by " + bundle, bundle, "");
                    }
                }
            }
        });
    }

    public static String[] getFileList(final File f2, final FilenameFilter filter) {
        return (String[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.7
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return f2.list(filter);
            }
        });
    }

    public static boolean getFileExists(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.8
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return f2.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static long getLastModified(final File f2) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.bcel.internal.util.SecuritySupport.9
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Long(f2.lastModified());
            }
        })).longValue();
    }

    public static java.lang.ClassLoader findClassLoader() {
        if (System.getSecurityManager() != null) {
            return null;
        }
        return SecuritySupport.class.getClassLoader();
    }

    private SecuritySupport() {
    }
}
