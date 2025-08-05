package com.sun.jmx.mbeanserver;

import javax.management.ObjectName;
import javax.management.loading.ClassLoaderRepository;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/ModifiableClassLoaderRepository.class */
public interface ModifiableClassLoaderRepository extends ClassLoaderRepository {
    void addClassLoader(ClassLoader classLoader);

    void removeClassLoader(ClassLoader classLoader);

    void addClassLoader(ObjectName objectName, ClassLoader classLoader);

    void removeClassLoader(ObjectName objectName);

    ClassLoader getClassLoader(ObjectName objectName);
}
