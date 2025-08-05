package com.sun.corba.se.spi.oa;

import com.sun.corba.se.impl.oa.poa.POAFactory;
import com.sun.corba.se.impl.oa.toa.TOAFactory;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/oa/OADefault.class */
public class OADefault {
    public static ObjectAdapterFactory makePOAFactory(ORB orb) {
        POAFactory pOAFactory = new POAFactory();
        pOAFactory.init(orb);
        return pOAFactory;
    }

    public static ObjectAdapterFactory makeTOAFactory(ORB orb) {
        TOAFactory tOAFactory = new TOAFactory();
        tOAFactory.init(orb);
        return tOAFactory;
    }
}
