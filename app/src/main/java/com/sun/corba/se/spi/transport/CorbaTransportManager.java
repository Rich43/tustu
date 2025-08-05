package com.sun.corba.se.spi.transport;

import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.pept.transport.TransportManager;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaTransportManager.class */
public interface CorbaTransportManager extends TransportManager {
    public static final String SOCKET_OR_CHANNEL_CONNECTION_CACHE = "SocketOrChannelConnectionCache";

    Collection getAcceptors(String str, ObjectAdapterId objectAdapterId);

    void addToIORTemplate(IORTemplate iORTemplate, Policies policies, String str, String str2, ObjectAdapterId objectAdapterId);
}
