package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.spi.ior.IOR;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/LocateReplyOrReplyMessage.class */
public interface LocateReplyOrReplyMessage extends Message {
    int getRequestId();

    int getReplyStatus();

    SystemException getSystemException(String str);

    IOR getIOR();

    short getAddrDisposition();
}
