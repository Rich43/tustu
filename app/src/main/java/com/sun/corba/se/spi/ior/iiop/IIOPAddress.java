package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.Writeable;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/iiop/IIOPAddress.class */
public interface IIOPAddress extends Writeable {
    String getHost();

    int getPort();
}
