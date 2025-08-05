package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.ObjectKey;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateRequestMessage.class */
public interface LocateRequestMessage extends Message {
    int getRequestId();

    ObjectKey getObjectKey();
}
