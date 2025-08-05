package com.sun.corba.se.impl.io;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClassCorbaExt.class */
class ObjectStreamClassCorbaExt {
    ObjectStreamClassCorbaExt() {
    }

    static final boolean isAbstractInterface(Class cls) throws SecurityException {
        if (!cls.isInterface() || Remote.class.isAssignableFrom(cls)) {
            return false;
        }
        for (Method method : cls.getMethods()) {
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            boolean z2 = false;
            for (int i2 = 0; i2 < exceptionTypes.length && !z2; i2++) {
                if (RemoteException.class == exceptionTypes[i2] || Throwable.class == exceptionTypes[i2] || Exception.class == exceptionTypes[i2] || IOException.class == exceptionTypes[i2]) {
                    z2 = true;
                }
            }
            if (!z2) {
                return false;
            }
        }
        return true;
    }

    static final boolean isAny(String str) {
        boolean z2 = false;
        if (str != null && (str.equals(Constants.OBJECT_SIG) || str.equals("Ljava/io/Serializable;") || str.equals("Ljava/io/Externalizable;"))) {
            z2 = true;
        }
        return z2;
    }

    private static final Method[] getDeclaredMethods(final Class cls) {
        return (Method[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamClassCorbaExt.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getDeclaredMethods();
            }
        });
    }
}
