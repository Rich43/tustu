package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/* loaded from: rt.jar:javax/activation/SecuritySupport.class */
class SecuritySupport {
    private SecuritySupport() {
    }

    public static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.1
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

    public static InputStream getResourceAsStream(final Class c2, final String name) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.activation.SecuritySupport.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return c2.getResourceAsStream(name);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    public static URL[] getResources(final ClassLoader cl, final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                URL[] ret = null;
                try {
                    List v2 = new ArrayList();
                    Enumeration e2 = cl.getResources(name);
                    while (e2 != null && e2.hasMoreElements()) {
                        URL url = e2.nextElement2();
                        if (url != null) {
                            v2.add(url);
                        }
                    }
                    if (v2.size() > 0) {
                        ret = (URL[]) v2.toArray(new URL[v2.size()]);
                    }
                } catch (IOException e3) {
                } catch (SecurityException e4) {
                }
                return ret;
            }
        });
    }

    public static URL[] getSystemResources(final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                URL[] ret = null;
                try {
                    List v2 = new ArrayList();
                    Enumeration e2 = ClassLoader.getSystemResources(name);
                    while (e2 != null && e2.hasMoreElements()) {
                        URL url = e2.nextElement2();
                        if (url != null) {
                            v2.add(url);
                        }
                    }
                    if (v2.size() > 0) {
                        ret = (URL[]) v2.toArray(new URL[v2.size()]);
                    }
                } catch (IOException e3) {
                } catch (SecurityException e4) {
                }
                return ret;
            }
        });
    }

    public static InputStream openStream(final URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.activation.SecuritySupport.5
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return url.openStream();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }
}
