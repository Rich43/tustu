package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.spi.ior.IOR;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/LocalClientRequestDispatcherFactory.class */
public interface LocalClientRequestDispatcherFactory {
    LocalClientRequestDispatcher create(int i2, IOR ior);
}
