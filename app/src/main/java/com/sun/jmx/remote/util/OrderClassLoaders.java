package com.sun.jmx.remote.util;

import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/remote/util/OrderClassLoaders.class */
public class OrderClassLoaders extends ClassLoader {
    private ClassLoader cl2;

    public OrderClassLoaders(ClassLoader classLoader, ClassLoader classLoader2) {
        super(classLoader);
        this.cl2 = classLoader2;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        try {
            return super.loadClass(str, z2);
        } catch (ClassNotFoundException e2) {
            if (this.cl2 != null) {
                return this.cl2.loadClass(str);
            }
            throw e2;
        }
    }
}
