package com.sun.xml.internal.ws.api.model;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/MEP.class */
public enum MEP {
    REQUEST_RESPONSE(false),
    ONE_WAY(false),
    ASYNC_POLL(true),
    ASYNC_CALLBACK(true);

    public final boolean isAsync;

    MEP(boolean async) {
        this.isAsync = async;
    }

    public final boolean isOneWay() {
        return this == ONE_WAY;
    }
}
