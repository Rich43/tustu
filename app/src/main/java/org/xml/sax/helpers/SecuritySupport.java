package org.xml.sax.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:org/xml/sax/helpers/SecuritySupport.class */
class SecuritySupport {
    SecuritySupport() {
    }

    ClassLoader getContextClassLoader() throws SecurityException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.xml.sax.helpers.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl == null) {
                    cl = ClassLoader.getSystemClassLoader();
                }
                return cl;
            }
        });
    }

    String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.xml.sax.helpers.SecuritySupport.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(propName);
            }
        });
    }

    FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.xml.sax.helpers.SecuritySupport.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }

    InputStream getResourceAsStream(final ClassLoader cl, final String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.xml.sax.helpers.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            public Object run() {
                InputStream ris;
                if (cl == null) {
                    ris = SecuritySupport.class.getResourceAsStream(name);
                } else {
                    ris = cl.getResourceAsStream(name);
                }
                return ris;
            }
        });
    }

    boolean doesFileExist(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.xml.sax.helpers.SecuritySupport.5
            @Override // java.security.PrivilegedAction
            public Object run() {
                return new Boolean(f2.exists());
            }
        })).booleanValue();
    }
}
