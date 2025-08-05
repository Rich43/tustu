package com.sun.xml.internal.bind.v2.model.nav;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/nav/SecureLoader.class */
class SecureLoader {
    SecureLoader() {
    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.xml.internal.bind.v2.model.nav.SecureLoader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    static ClassLoader getClassClassLoader(final Class c2) {
        if (System.getSecurityManager() == null) {
            return c2.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.xml.internal.bind.v2.model.nav.SecureLoader.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return c2.getClassLoader();
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: com.sun.xml.internal.bind.v2.model.nav.SecureLoader.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
}
