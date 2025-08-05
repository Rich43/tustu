package com.sun.corba.se.spi.oa;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/oa/ObjectAdapterFactory.class */
public interface ObjectAdapterFactory {
    void init(ORB orb);

    void shutdown(boolean z2);

    ObjectAdapter find(ObjectAdapterId objectAdapterId);

    ORB getORB();
}
