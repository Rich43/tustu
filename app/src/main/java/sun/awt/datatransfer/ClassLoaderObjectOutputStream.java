package sun.awt.datatransfer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* compiled from: TransferableProxy.java */
/* loaded from: rt.jar:sun/awt/datatransfer/ClassLoaderObjectOutputStream.class */
final class ClassLoaderObjectOutputStream extends ObjectOutputStream {
    private final Map<Set<String>, ClassLoader> map;

    ClassLoaderObjectOutputStream(OutputStream outputStream) throws IOException {
        super(outputStream);
        this.map = new HashMap();
    }

    @Override // java.io.ObjectOutputStream
    protected void annotateClass(final Class<?> cls) throws IOException {
        ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.datatransfer.ClassLoaderObjectOutputStream.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getClassLoader();
            }
        });
        HashSet hashSet = new HashSet(1);
        hashSet.add(cls.getName());
        this.map.put(hashSet, classLoader);
    }

    @Override // java.io.ObjectOutputStream
    protected void annotateProxyClass(final Class<?> cls) throws IOException {
        ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.datatransfer.ClassLoaderObjectOutputStream.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getClassLoader();
            }
        });
        Class<?>[] interfaces = cls.getInterfaces();
        HashSet hashSet = new HashSet(interfaces.length);
        for (Class<?> cls2 : interfaces) {
            hashSet.add(cls2.getName());
        }
        this.map.put(hashSet, classLoader);
    }

    Map<Set<String>, ClassLoader> getClassLoaderMap() {
        return new HashMap(this.map);
    }
}
