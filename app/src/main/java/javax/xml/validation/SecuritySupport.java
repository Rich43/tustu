package javax.xml.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

/* loaded from: rt.jar:javax/xml/validation/SecuritySupport.class */
class SecuritySupport {
    SecuritySupport() {
    }

    ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.validation.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl == null) {
                    cl = ClassLoader.getSystemClassLoader();
                }
                return cl;
            }
        });
    }

    String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.validation.SecuritySupport.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(propName);
            }
        });
    }

    FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.xml.validation.SecuritySupport.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }

    InputStream getURLInputStream(final URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.xml.validation.SecuritySupport.4
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return url.openStream();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    URL getResourceAsURL(final ClassLoader cl, final String name) {
        return (URL) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.validation.SecuritySupport.5
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                URL url;
                if (cl == null) {
                    url = Object.class.getResource(name);
                } else {
                    url = cl.getResource(name);
                }
                return url;
            }
        });
    }

    Enumeration getResources(final ClassLoader cl, final String name) throws IOException {
        try {
            return (Enumeration) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.xml.validation.SecuritySupport.6
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    Enumeration enumeration;
                    if (cl == null) {
                        enumeration = ClassLoader.getSystemResources(name);
                    } else {
                        enumeration = cl.getResources(name);
                    }
                    return enumeration;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    boolean doesFileExist(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.xml.validation.SecuritySupport.7
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Boolean(f2.exists());
            }
        })).booleanValue();
    }
}
