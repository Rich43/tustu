package com.sun.corba.se.spi.orbutil.proxy;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.presentation.rmi.DynamicAccessPermission;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/proxy/CompositeInvocationHandlerImpl.class */
public class CompositeInvocationHandlerImpl implements CompositeInvocationHandler {
    private Map classToInvocationHandler = new LinkedHashMap();
    private InvocationHandler defaultHandler = null;
    private static final DynamicAccessPermission perm = new DynamicAccessPermission("access");
    private static final long serialVersionUID = 4571178305984833743L;

    @Override // com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandler
    public void addInvocationHandler(Class cls, InvocationHandler invocationHandler) {
        checkAccess();
        this.classToInvocationHandler.put(cls, invocationHandler);
    }

    @Override // com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandler
    public void setDefaultHandler(InvocationHandler invocationHandler) {
        checkAccess();
        this.defaultHandler = invocationHandler;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        InvocationHandler invocationHandler = (InvocationHandler) this.classToInvocationHandler.get(method.getDeclaringClass());
        if (invocationHandler == null) {
            if (this.defaultHandler != null) {
                invocationHandler = this.defaultHandler;
            } else {
                throw ORBUtilSystemException.get(CORBALogDomains.UTIL).noInvocationHandler(PdfOps.DOUBLE_QUOTE__TOKEN + method.toString() + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
        return invocationHandler.invoke(obj, method, objArr);
    }

    private void checkAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(perm);
        }
    }
}
