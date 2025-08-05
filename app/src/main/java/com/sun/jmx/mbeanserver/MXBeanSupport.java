package com.sun.jmx.mbeanserver;

import java.util.Set;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanSupport.class */
public class MXBeanSupport extends MBeanSupport<ConvertingMethod> {
    private final Object lock;
    private MXBeanLookup mxbeanLookup;
    private ObjectName objectName;

    public <T> MXBeanSupport(T t2, Class<T> cls) throws NotCompliantMBeanException {
        super(t2, cls);
        this.lock = new Object();
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    MBeanIntrospector<ConvertingMethod> getMBeanIntrospector() {
        return MXBeanIntrospector.getInstance();
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    Object getCookie() {
        return this.mxbeanLookup;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x009a, code lost:
    
        r0.remove();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static <T> java.lang.Class<? super T> findMXBeanInterface(java.lang.Class<T> r4) {
        /*
            Method dump skipped, instructions count: 280
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jmx.mbeanserver.MXBeanSupport.findMXBeanInterface(java.lang.Class):java.lang.Class");
    }

    private static Set<Class<?>> transitiveInterfaces(Class<?> cls) {
        Set<Class<?>> setNewSet = Util.newSet();
        transitiveInterfaces(cls, setNewSet);
        return setNewSet;
    }

    private static void transitiveInterfaces(Class<?> cls, Set<Class<?>> set) {
        if (cls == null) {
            return;
        }
        if (cls.isInterface()) {
            set.add(cls);
        }
        transitiveInterfaces(cls.getSuperclass(), set);
        for (Class<?> cls2 : cls.getInterfaces()) {
            transitiveInterfaces(cls2, set);
        }
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    public void register(MBeanServer mBeanServer, ObjectName objectName) throws InstanceAlreadyExistsException {
        if (objectName == null) {
            throw new IllegalArgumentException("Null object name");
        }
        synchronized (this.lock) {
            this.mxbeanLookup = MXBeanLookup.lookupFor(mBeanServer);
            this.mxbeanLookup.addReference(objectName, getResource());
            this.objectName = objectName;
        }
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    public void unregister() {
        synchronized (this.lock) {
            if (this.mxbeanLookup != null && this.mxbeanLookup.removeReference(this.objectName, getResource())) {
                this.objectName = null;
            }
        }
    }
}
