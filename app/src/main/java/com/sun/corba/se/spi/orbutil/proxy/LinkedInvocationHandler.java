package com.sun.corba.se.spi.orbutil.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/proxy/LinkedInvocationHandler.class */
public interface LinkedInvocationHandler extends InvocationHandler {
    void setProxy(Proxy proxy);

    Proxy getProxy();
}
