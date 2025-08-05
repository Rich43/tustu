package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.spi.resolver.Resolver;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/InitialServerRequestDispatcher.class */
public interface InitialServerRequestDispatcher extends CorbaServerRequestDispatcher {
    void init(Resolver resolver);
}
