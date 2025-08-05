package com.sun.jmx.mbeanserver;

import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/SecureClassLoaderRepository.class */
final class SecureClassLoaderRepository implements ClassLoaderRepository {
    private final ClassLoaderRepository clr;

    public SecureClassLoaderRepository(ClassLoaderRepository classLoaderRepository) {
        this.clr = classLoaderRepository;
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClass(String str) throws ClassNotFoundException {
        return this.clr.loadClass(str);
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException {
        return this.clr.loadClassWithout(classLoader, str);
    }

    @Override // javax.management.loading.ClassLoaderRepository
    public final Class<?> loadClassBefore(ClassLoader classLoader, String str) throws ClassNotFoundException {
        return this.clr.loadClassBefore(classLoader, str);
    }
}
