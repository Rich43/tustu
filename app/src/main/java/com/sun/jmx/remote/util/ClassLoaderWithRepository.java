package com.sun.jmx.remote.util;

import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/remote/util/ClassLoaderWithRepository.class */
public class ClassLoaderWithRepository extends ClassLoader {
    private ClassLoaderRepository repository;
    private ClassLoader cl2;

    public ClassLoaderWithRepository(ClassLoaderRepository classLoaderRepository, ClassLoader classLoader) {
        if (classLoaderRepository == null) {
            throw new IllegalArgumentException("Null ClassLoaderRepository object.");
        }
        this.repository = classLoaderRepository;
        this.cl2 = classLoader;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String str) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = this.repository.loadClass(str);
            if (!clsLoadClass.getName().equals(str)) {
                if (this.cl2 != null) {
                    return this.cl2.loadClass(str);
                }
                throw new ClassNotFoundException(str);
            }
            return clsLoadClass;
        } catch (ClassNotFoundException e2) {
            if (this.cl2 != null) {
                return this.cl2.loadClass(str);
            }
            throw e2;
        }
    }
}
