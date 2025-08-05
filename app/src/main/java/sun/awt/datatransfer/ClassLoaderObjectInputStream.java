package sun.awt.datatransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* compiled from: TransferableProxy.java */
/* loaded from: rt.jar:sun/awt/datatransfer/ClassLoaderObjectInputStream.class */
final class ClassLoaderObjectInputStream extends ObjectInputStream {
    private final Map<Set<String>, ClassLoader> map;

    ClassLoaderObjectInputStream(InputStream inputStream, Map<Set<String>, ClassLoader> map) throws IOException {
        super(inputStream);
        if (map == null) {
            throw new NullPointerException("Null map");
        }
        this.map = map;
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        String name = objectStreamClass.getName();
        HashSet hashSet = new HashSet(1);
        hashSet.add(name);
        ClassLoader classLoader = this.map.get(hashSet);
        if (classLoader != null) {
            return Class.forName(name, false, classLoader);
        }
        return super.resolveClass(objectStreamClass);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveProxyClass(String[] strArr) throws ClassNotFoundException, IOException {
        HashSet hashSet = new HashSet(strArr.length);
        for (String str : strArr) {
            hashSet.add(str);
        }
        ClassLoader classLoader = this.map.get(hashSet);
        if (classLoader == null) {
            return super.resolveProxyClass(strArr);
        }
        ClassLoader classLoader2 = null;
        boolean z2 = false;
        Class[] clsArr = new Class[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Class<?> cls = Class.forName(strArr[i2], false, classLoader);
            if ((cls.getModifiers() & 1) == 0) {
                if (z2) {
                    if (classLoader2 != cls.getClassLoader()) {
                        throw new IllegalAccessError("conflicting non-public interface class loaders");
                    }
                } else {
                    classLoader2 = cls.getClassLoader();
                    z2 = true;
                }
            }
            clsArr[i2] = cls;
        }
        try {
            return Proxy.getProxyClass(z2 ? classLoader2 : classLoader, clsArr);
        } catch (IllegalArgumentException e2) {
            throw new ClassNotFoundException(null, e2);
        }
    }
}
