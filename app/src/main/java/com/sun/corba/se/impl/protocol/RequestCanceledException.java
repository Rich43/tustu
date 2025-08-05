package com.sun.corba.se.impl.protocol;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/RequestCanceledException.class */
public class RequestCanceledException extends RuntimeException {
    private int requestId;

    public RequestCanceledException(int i2) {
        this.requestId = 0;
        this.requestId = i2;
    }

    public int getRequestId() {
        return this.requestId;
    }
}
