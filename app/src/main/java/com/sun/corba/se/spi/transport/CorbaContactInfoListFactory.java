package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaContactInfoListFactory.class */
public interface CorbaContactInfoListFactory {
    void setORB(ORB orb);

    CorbaContactInfoList create(IOR ior);
}
