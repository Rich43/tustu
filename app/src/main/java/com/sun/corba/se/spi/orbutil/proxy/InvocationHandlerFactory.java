package com.sun.corba.se.spi.orbutil.proxy;

import java.lang.reflect.InvocationHandler;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/proxy/InvocationHandlerFactory.class */
public interface InvocationHandlerFactory {
    InvocationHandler getInvocationHandler();

    Class[] getProxyInterfaces();
}
