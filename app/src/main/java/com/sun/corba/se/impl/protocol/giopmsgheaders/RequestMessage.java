package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import org.omg.CORBA.Principal;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/RequestMessage.class */
public interface RequestMessage extends Message {
    public static final byte RESPONSE_EXPECTED_BIT = 1;

    ServiceContexts getServiceContexts();

    int getRequestId();

    boolean isResponseExpected();

    byte[] getReserved();

    ObjectKey getObjectKey();

    String getOperation();

    Principal getPrincipal();

    void setThreadPoolToUse(int i2);
}
