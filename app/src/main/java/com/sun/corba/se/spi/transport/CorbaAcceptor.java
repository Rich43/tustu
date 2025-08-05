package com.sun.corba.se.spi.transport;

import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.ior.IORTemplate;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaAcceptor.class */
public interface CorbaAcceptor extends Acceptor {
    String getObjectAdapterId();

    String getObjectAdapterManagerId();

    void addToIORTemplate(IORTemplate iORTemplate, Policies policies, String str);

    String getMonitoringName();
}
