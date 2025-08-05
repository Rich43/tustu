package com.sun.corba.se.spi.resolver;

import com.sun.corba.se.spi.orbutil.closure.Closure;

/* loaded from: rt.jar:com/sun/corba/se/spi/resolver/LocalResolver.class */
public interface LocalResolver extends Resolver {
    void register(String str, Closure closure);
}
