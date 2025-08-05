package com.sun.org.apache.xerces.internal.xinclude;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/SecuritySupport.class */
final class SecuritySupport {
    private static final SecuritySupport securitySupport = new SecuritySupport();

    static SecuritySupport getInstance() {
        return securitySupport;
    }

    ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    ClassLoader getSystemClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = null;
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    ClassLoader getParentClassLoader(final ClassLoader cl) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader parent = null;
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

    String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(propName);
            }
        });
    }

    FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.5
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }

    boolean getFileExists(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.6
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Boolean(f2.exists());
            }
        })).booleanValue();
    }

    long getLastModified(final File f2) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.xinclude.SecuritySupport.7
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Long(f2.lastModified());
            }
        })).longValue();
    }

    private SecuritySupport() {
    }
}
