package com.sun.corba.se.spi.orbutil.proxy;

import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/proxy/DelegateInvocationHandlerImpl.class */
public abstract class DelegateInvocationHandlerImpl {
    private DelegateInvocationHandlerImpl() {
    }

    public static InvocationHandler create(final Object obj) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new DynamicAccessPermission("access"));
        }
        return new InvocationHandler() { // from class: com.sun.corba.se.spi.orbutil.proxy.DelegateInvocationHandlerImpl.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj2, Method method, Object[] objArr) throws Throwable {
                try {
                    return method.invoke(obj, objArr);
                } catch (InvocationTargetException e2) {
                    throw e2.getCause();
                }
            }
        };
    }
}
