package com.sun.corba.se.spi.orbutil.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/proxy/CompositeInvocationHandler.class */
public interface CompositeInvocationHandler extends InvocationHandler, Serializable {
    void addInvocationHandler(Class cls, InvocationHandler invocationHandler);

    void setDefaultHandler(InvocationHandler invocationHandler);
}
