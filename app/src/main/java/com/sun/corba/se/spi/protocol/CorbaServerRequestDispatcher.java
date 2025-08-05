package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.pept.protocol.ServerRequestDispatcher;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/CorbaServerRequestDispatcher.class */
public interface CorbaServerRequestDispatcher extends ServerRequestDispatcher {
    IOR locate(ObjectKey objectKey);
}
