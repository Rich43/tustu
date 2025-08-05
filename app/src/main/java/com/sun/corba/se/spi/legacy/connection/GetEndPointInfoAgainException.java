package com.sun.corba.se.spi.legacy.connection;

import com.sun.corba.se.spi.transport.SocketInfo;

/* loaded from: rt.jar:com/sun/corba/se/spi/legacy/connection/GetEndPointInfoAgainException.class */
public class GetEndPointInfoAgainException extends Exception {
    private SocketInfo socketInfo;

    public GetEndPointInfoAgainException(SocketInfo socketInfo) {
        this.socketInfo = socketInfo;
    }

    public SocketInfo getEndPointInfo() {
        return this.socketInfo;
    }
}
