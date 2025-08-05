package com.sun.jmx.mbeanserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/ObjectInputStreamWithLoader.class */
class ObjectInputStreamWithLoader extends ObjectInputStream {
    private ClassLoader loader;

    public ObjectInputStreamWithLoader(InputStream inputStream, ClassLoader classLoader) throws IOException {
        super(inputStream);
        this.loader = classLoader;
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        if (this.loader == null) {
            return super.resolveClass(objectStreamClass);
        }
        String name = objectStreamClass.getName();
        ReflectUtil.checkPackageAccess(name);
        return Class.forName(name, false, this.loader);
    }
}
