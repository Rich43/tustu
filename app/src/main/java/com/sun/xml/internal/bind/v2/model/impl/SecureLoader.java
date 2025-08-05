package com.sun.xml.internal.bind.v2.model.impl;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/SecureLoader.class */
class SecureLoader {
    SecureLoader() {
    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.internal.bind.v2.model.impl.SecureLoader.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    static ClassLoader getClassClassLoader(final Class c2) {
        if (System.getSecurityManager() == null) {
            return c2.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.internal.bind.v2.model.impl.SecureLoader.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return c2.getClassLoader();
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.internal.bind.v2.model.impl.SecureLoader.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
}
