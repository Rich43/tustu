package com.sun.org.glassfish.gmbal;

import com.sun.org.glassfish.gmbal.util.GenericConstructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/ManagedObjectManagerFactory.class */
public final class ManagedObjectManagerFactory {
    private static GenericConstructor<ManagedObjectManager> objectNameCons = new GenericConstructor<>(ManagedObjectManager.class, "com.sun.org.glassfish.gmbal.impl.ManagedObjectManagerImpl", ObjectName.class);
    private static GenericConstructor<ManagedObjectManager> stringCons = new GenericConstructor<>(ManagedObjectManager.class, "com.sun.org.glassfish.gmbal.impl.ManagedObjectManagerImpl", String.class);

    private ManagedObjectManagerFactory() {
    }

    public static Method getMethod(final Class<?> cls, final String name, final Class<?>... types) {
        try {
            return (Method) AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() { // from class: com.sun.org.glassfish.gmbal.ManagedObjectManagerFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Method run() throws Exception {
                    return cls.getDeclaredMethod(name, types);
                }
            });
        } catch (SecurityException exc) {
            throw new GmbalException("Unexpected exception", exc);
        } catch (PrivilegedActionException ex) {
            throw new GmbalException("Unexpected exception", ex);
        }
    }

    public static ManagedObjectManager createStandalone(String domain) {
        ManagedObjectManager result = stringCons.create(domain);
        if (result == null) {
            return ManagedObjectManagerNOPImpl.self;
        }
        return result;
    }

    public static ManagedObjectManager createFederated(ObjectName rootParentName) {
        ManagedObjectManager result = objectNameCons.create(rootParentName);
        if (result == null) {
            return ManagedObjectManagerNOPImpl.self;
        }
        return result;
    }

    public static ManagedObjectManager createNOOP() {
        return ManagedObjectManagerNOPImpl.self;
    }
}
